#!/usr/bin/env python3
"""Download latest APKs for all patched apps. Never modifies the repo.

Usage:
  python3 download-all-apks.py [--only <package>] [--all] [--out DIR]
                               [--jobs N] [--arch ABI]
                               [--play-email E] [--play-aas-token T |
                                --play-auth-token T]

  --only <package>  single package (e.g. xyz.rtrvr.pillo)
  --all             also download packages already up-to-date
  --out DIR         destination (default: build/apks)
  --jobs N          parallel downloads (default: 3)
  --arch ABI        APK abi (default: arm64-v8a, 'all' = universal).
                      Enforced post-download via droidsaw (apkeep's arch=
                      is only a request header): wrong-ABI artifacts are
                      rejected and the next source is tried.
  --keep N            keep N newest versions per app, delete older
                      downloads on success (default: 2, newest + previous
                      for old-vs-new diffing; 0 = keep everything)

  Google Play (no mirror lag) is tried first when credentials are set,
  via flags or env: APKEEP_PLAY_EMAIL plus APKEEP_PLAY_AAS_TOKEN (or
  APKEEP_PLAY_AUTH_TOKEN, e.g. from an Aurora dispenser). Then mirrors
  via apkget (APKPure/APKCombo/Uptodown/F-Droid/APKMirror/APK20),
  then APKPure via apkeep. Auth tokens: use a throwaway account, never
  your main one; apkeep also accepts an apkeep.ini (see its docs).

Requires: apkeep (`brew install apkeep`); apkget release binary
(`go install` is broken upstream — download the release and verify
the checksum).
XAPK (split) bundles land next to plain APKs; droidsaw reads both.
Bumping Constants.kt is always manual, after droidsaw verification
(see AGENTS.md "Actualization", batch loop below).
"""
import argparse
import concurrent.futures
import glob
import importlib.util
import json
import os
import re
import shutil
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
ACK_OPTS = "acknowledge_dangers=true"
CHECKER = os.path.normpath(os.path.join(
    HERE, "..", ".github", "scripts", "check-app-updates.py"))


def load_checker():
    spec = importlib.util.spec_from_file_location(
        "check_app_updates", CHECKER)
    m = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(m)
    return m


def log(msg):
    print(msg, file=sys.stderr, flush=True)


def play_creds(args):
    """Google Play credentials from flags or env. None when not configured."""
    email = args.play_email or os.environ.get("APKEEP_PLAY_EMAIL", "")
    aas = args.play_aas_token or os.environ.get("APKEEP_PLAY_AAS_TOKEN", "")
    auth = args.play_auth_token or os.environ.get("APKEEP_PLAY_AUTH_TOKEN", "")
    if email and (aas or auth):
        return {"email": email, "aas": aas, "auth": auth}
    return None


def version_of(apk_path):
    """APK version_name via droidsaw, or None when unavailable."""
    if shutil.which("droidsaw") is None:
        return None
    try:
        out = subprocess.run(["droidsaw", "info", apk_path], timeout=120,
                             check=False, stdout=subprocess.PIPE,
                             stderr=subprocess.DEVNULL, text=True).stdout
        return json.loads(out)["info"]["version_name"]
    except Exception:
        return None


def _artifact_files(dest):
    """Set of downloaded artifact paths (apk + xapk) in dest."""
    return set(glob.glob(os.path.join(dest, "*.apk")) +
               glob.glob(os.path.join(dest, "*.xapk")))


def abi_ok(path, arch):
    """True if the APK suits `arch` (or arch can't be checked).

    apkeep's `arch=` is only a request header — mirrors may still serve
    another ABI. droidsaw's native_lib_abis is authoritative; apks
    without native libs run anywhere.
    """
    if arch == "all":
        return True
    if shutil.which("droidsaw") is None:
        return True
    try:
        out = subprocess.run(["droidsaw", "info", path], timeout=120,
                             check=False, stdout=subprocess.PIPE,
                             stderr=subprocess.DEVNULL, text=True).stdout
        abis = json.loads(out)["info"]["layers"].get("native_lib_abis", [])
    except Exception:
        return True
    return not abis or arch in abis


def _verified(dest, before, version, arch="arm64-v8a"):
    """Newest matching artifact added since `before`, or None.

    A candidate must match the version (droidsaw, or version-carrying
    name when droidsaw is unavailable) AND the ABI. Falls back to
    unversioned leftovers (e.g. google-play's `<pkg>.apk`), which are
    never trusted blindly.
    """
    fresh = sorted(_artifact_files(dest) - before, key=os.path.getsize,
                   reverse=True)
    if not fresh:
        fresh = sorted([f for f in _artifact_files(dest)
                        if version not in os.path.basename(f)],
                       key=os.path.getsize, reverse=True)
    for cand in fresh:
        got = version_of(cand)
        if got is not None and got != version:
            if cand not in before:
                log(f"RM {cand} (got {got}, want {version})")
                try:
                    os.remove(cand)
                except OSError as e:
                    log(f"SKIP RM {cand}: {e}")
            continue
        if got is None and version not in os.path.basename(cand):
            continue
        if not abi_ok(cand, arch):
            log(f"SKIP {cand}: wrong ABI for {arch}, trying next source")
            if cand not in before:
                try:
                    os.remove(cand)
                except OSError as e:
                    log(f"SKIP RM {cand}: {e}")
            continue
        mb = os.path.getsize(cand) / 1e6
        log(f"OK {version} -> {cand} ({mb:.0f} MB)")
        return cand
    if fresh:
        log(f"SKIP: no artifact matched {version} [{arch}]")
    return None


def try_google_play(pkg, version, dest, creds, arch="arm64-v8a"):
    """Latest from Google Play (no mirror lag). Artifact path or None.

    Play serves latest only; _verified enforces version and ABI.
    """
    cmd = ["apkeep", "-a", pkg, "-d", "google-play",
           "-e", creds["email"], "--accept-tos",
           "-o", "split_apk=false", dest]
    if creds["aas"]:
        cmd += ["-t", creds["aas"]]
    else:
        cmd += ["--auth-token", creds["auth"]]
    log(f"DL {pkg} {version} [google-play] -> {dest} ...")
    before = _artifact_files(dest)
    try:
        subprocess.run(cmd, timeout=900, check=False)
    except Exception as e:
        log(f"SKIP {pkg}: google-play error: {e}")
        return None
    return _verified(dest, before, version, arch)


def apkget_sources_with(pkg, version):
    """Mirror names (apkget fallback order) already hosting version.

    Empty list when apkget is missing, on error, or when no mirror
    has it yet. Version truth always stays with Google Play.
    """
    if shutil.which("apkget") is None:
        return []
    try:
        out = subprocess.run(["apkget", "list", pkg], timeout=120,
                             check=False, stdout=subprocess.PIPE,
                             stderr=subprocess.DEVNULL, text=True).stdout
        entries = json.loads(out)
    except Exception as e:
        log(f"SKIP {pkg}: apkget list error: {e}")
        return []
    return [e["source"] for e in entries if version in e.get("versions", [])]


def try_apkget(pkg, version, dest, arch="arm64-v8a"):
    """Exact Play version from the first mirror that hosts it."""
    sources = apkget_sources_with(pkg, version)
    if not sources:
        return None
    for src in sources:
        log(f"DL {pkg} {version} [{src}] -> {dest} ...")
        before = _artifact_files(dest)
        try:
            subprocess.run(["apkget", "-output", dest, "-source", src,
                            "-version", version, pkg],
                           timeout=900, check=False)
        except Exception as e:
            log(f"SKIP {pkg}: {src} error: {e}")
            continue
        got = _verified(dest, before, version, arch)
        if got:
            return got
    return None


def download_apk(pkg, version, dest, arch="arm64-v8a", creds=None):
    os.makedirs(dest, exist_ok=True)
    have = sorted(f for f in glob.glob(os.path.join(dest, "*.apk")) +
                  glob.glob(os.path.join(dest, "*.xapk"))
                  if version in os.path.basename(f))
    if have:
        mb = os.path.getsize(have[0]) / 1e6
        log(f"REUSE {pkg} {version} -> {have[0]} ({mb:.0f} MB)")
        return have[0], False
    if shutil.which("apkeep") is None and not creds \
            and shutil.which("apkget") is None:
        log(f"SKIP {pkg}: no downloader found "
            "(`brew install apkeep`; apkget release binary, see script header)")
        return None, True
    opts = ACK_OPTS if arch == "all" else f"{ACK_OPTS},arch={arch}"
    # Google Play first when configured (no lag). try_google_play
    # enforces the version itself (content-checked via droidsaw).
    if creds:
        got = try_google_play(pkg, version, dest, creds, arch)
        if got:
            return got, False
    # Then mirrors via apkget (pins the exact Play version).
    got = try_apkget(pkg, version, dest, arch)
    if got:
        return got, False
    if shutil.which("apkeep") is None:
        log(f"SKIP {pkg}: APKPure fallback needs apkeep")
        return None, False
    # Then APKPure: exact version first, then latest as fallback.
    # NOTE: apkeep's arch= is only a request header — the ABI of what
    # lands is verified below, wrong-arch artifacts are skipped.
    before_all = _artifact_files(dest)
    for spec in (f"{pkg}@{version}", pkg):
        log(f"DL {pkg} {version} [{arch}] -> {dest} ...")
        try:
            subprocess.run(["apkeep", "-a", spec, "-d", "apk-pure",
                            "-o", opts, dest],
                           timeout=900, check=False)
        except Exception as e:
            log(f"SKIP {pkg}: download error: {e}")
            return None, False
        got = _verified(dest, before_all, version, arch)
        if got:
            return got, False
    log(f"SKIP {pkg}: no artifact from APKPure")
    return None, False


def artifact_matches(apk_path, version):
    """apkeep names versioned downloads <pkg>@<version>.*; fallback (latest)
    has no version in the name, so its content version is unconfirmed."""
    return version in os.path.basename(apk_path)


def _version_from_name(path):
    """Normalized version from the file name, or None if absent."""
    m = re.search(r"(\d+(?:[.\-]\d+)+)", os.path.basename(path))
    return m.group(1).replace("-", ".") if m else None


def _cleanup_old(dest, version, keep):
    """Delete stale downloads, keeping `keep` newest versions.

    Groups by normalized version (file name, else droidsaw content);
    unidentifiable files are never touched. keep<=0 keeps everything.
    """
    if keep <= 0:
        return
    want = version.replace("-", ".")
    groups = {}
    for p in _artifact_files(dest):
        v = _version_from_name(p) or version_of(p)
        if v is None:
            continue
        groups.setdefault(v, []).append(p)
    if len(groups) <= keep:
        return
    ranked = sorted(groups,
                    key=lambda v: (v == want,
                                   max(os.path.getmtime(p)
                                       for p in groups[v])),
                    reverse=True)
    for v in ranked[keep:]:
        for p in groups[v]:
            log(f"RM {p} (superseded by {version})")
            try:
                os.remove(p)
            except OSError as e:
                log(f"SKIP RM {p}: {e}")


def fetch_one(m, pkg, current, out, want_all, arch, creds=None, keep=2):
    log = m.log
    log(f"CHECK {pkg} (current: {', '.join(current)}) ...")
    play = m.play_version(pkg, current)
    if play is None:
        return ("skip", pkg, "unknown version")
    if not want_all and (play in current or not m.is_newer(play, current)):
        return ("skip", pkg, f"up-to-date ({play})")
    log(f"  -> {play}: NEW")
    apk, manual = download_apk(pkg, play, os.path.join(out, pkg), arch,
                               creds)
    if apk is None:
        return ("skip", pkg,
                "download failed" + (" (no downloader)" if manual else ""))
    if not (artifact_matches(apk, play) or version_of(apk) == play):
        return ("skip", pkg,
                f"got unversioned artifact ({apk}), sources lag Play")
    _cleanup_old(os.path.join(out, pkg), play, keep)
    return ("ok", pkg, f"{play} -> {apk}")


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--only", default="")
    ap.add_argument("--all", action="store_true",
                    help="download even up-to-date packages")
    ap.add_argument("--out", default=os.path.join("build", "apks"))
    ap.add_argument("--jobs", type=int, default=3,
                    help="parallel downloads (default: 3)")
    ap.add_argument("--arch", default="arm64-v8a",
                    help="APK abi (default arm64-v8a, 'all' = universal)")
    ap.add_argument("--keep", type=int, default=2, metavar="N",
                    help="keep N newest versions per app, delete the rest "
                         "(0 = keep everything)")
    ap.add_argument("--play-email", default="",
                    help="Google Play account email (or APKEEP_PLAY_EMAIL)")
    ap.add_argument("--play-aas-token", default="",
                    help="Google Play AAS token (or APKEEP_PLAY_AAS_TOKEN)")
    ap.add_argument("--play-auth-token", default="",
                    help="Google Play AUTH token, e.g. from an Aurora "
                         "dispenser (or APKEEP_PLAY_AUTH_TOKEN)")
    args = ap.parse_args()

    m = load_checker()
    os.makedirs(args.out, exist_ok=True)
    creds = play_creds(args)
    if creds:
        log("Google Play source enabled (falls back to APKPure)")
    jobs = [(pkg, current) for _, pkg, current in m.targets()
            if not args.only or args.only == pkg]
    ok, skipped = [], []
    with concurrent.futures.ThreadPoolExecutor(max_workers=args.jobs) as ex:
        futs = {ex.submit(fetch_one, m, pkg, cur, args.out, args.all,
                          args.arch, creds, args.keep): pkg
                for pkg, cur in jobs}
        for fut in concurrent.futures.as_completed(futs):
            status, pkg, msg = fut.result()
            (ok if status == "ok" else skipped).append((pkg, msg))
    print(f"downloaded: {len(ok)}, skipped: {len(skipped)}")
    for pkg, msg in sorted(ok):
        print(f"  OK {pkg} {msg}")
    for pkg, msg in sorted(skipped):
        print(f"  SKIP {pkg}: {msg}", file=sys.stderr)


if __name__ == "__main__":
    main()
