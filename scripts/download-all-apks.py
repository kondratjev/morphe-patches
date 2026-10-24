#!/usr/bin/env python3
"""Download latest APKs for all patched apps. Never modifies the repo.

Usage:
  python3 download-all-apks.py [--only <package>] [--all] [--out DIR]
                               [--jobs N] [--arch ABI]

  --only <package>  single package (e.g. xyz.rtrvr.pillo)
  --all             also download packages already up-to-date
  --out DIR         destination (default: build/apks)
  --jobs N          parallel downloads (default: 3)
  --arch ABI        APK abi (default: arm64-v8a, 'all' = universal, bigger)

Requires: apkeep (`brew install apkeep`). Downloads via APKPure, no auth.
XAPK (split) bundles land next to plain APKs; droidsaw reads both.
Bumping Constants.kt is always manual, after droidsaw verification
(see AGENTS.md "Actualization", batch loop below).
"""
import argparse
import concurrent.futures
import glob
import importlib.util
import os
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


def download_apk(pkg, version, dest, arch="arm64-v8a"):
    os.makedirs(dest, exist_ok=True)
    have = sorted(f for f in glob.glob(os.path.join(dest, "*.apk")) +
                  glob.glob(os.path.join(dest, "*.xapk"))
                  if version in os.path.basename(f))
    if have:
        mb = os.path.getsize(have[0]) / 1e6
        log(f"REUSE {pkg} {version} -> {have[0]} ({mb:.0f} MB)")
        return have[0], False
    if shutil.which("apkeep") is None:
        log(f"SKIP {pkg}: apkeep not found "
            "(`brew install apkeep`, see https://github.com/EFForg/apkeep)")
        return None, True
    opts = ACK_OPTS if arch == "all" else f"{ACK_OPTS},arch={arch}"
    # Exact version first, then latest as fallback (APKPure lags Play).
    for spec in (f"{pkg}@{version}", pkg):
        log(f"DL {pkg} {version} [{arch}] -> {dest} ...")
        try:
            subprocess.run(["apkeep", "-a", spec, "-d", "apk-pure",
                            "-o", opts, dest],
                           timeout=900, check=False)
        except Exception as e:
            log(f"SKIP {pkg}: download error: {e}")
            return None, False
        apks = sorted(glob.glob(os.path.join(dest, "*.apk")) +
                      glob.glob(os.path.join(dest, "*.xapk")))
        if apks:
            mb = os.path.getsize(apks[0]) / 1e6
            log(f"OK {pkg} {version} -> {apks[0]} ({mb:.0f} MB)")
            return apks[0], False
    log(f"SKIP {pkg}: no artifact from APKPure")
    return None, False


def artifact_matches(apk_path, version):
    """apkeep names versioned downloads <pkg>@<version>.*; fallback (latest)
    has no version in the name, so its content version is unconfirmed."""
    return version in os.path.basename(apk_path)


def fetch_one(m, pkg, current, out, want_all, arch):
    log = m.log
    log(f"CHECK {pkg} (current: {', '.join(current)}) ...")
    play = m.play_version(pkg, current)
    if play is None:
        return ("skip", pkg, "unknown version")
    if not want_all and (play in current or not m.is_newer(play, current)):
        return ("skip", pkg, f"up-to-date ({play})")
    log(f"  -> {play}: NEW")
    apk, manual = download_apk(pkg, play, os.path.join(out, pkg), arch)
    if apk is None:
        return ("skip", pkg,
                "download failed" + (" (no apkeep)" if manual else ""))
    if not artifact_matches(apk, play):
        return ("skip", pkg,
                f"got unversioned artifact ({apk}), APKPure lags Play")
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
    args = ap.parse_args()

    m = load_checker()
    os.makedirs(args.out, exist_ok=True)
    jobs = [(pkg, current) for _, pkg, current in m.targets()
            if not args.only or args.only == pkg]
    ok, skipped = [], []
    with concurrent.futures.ThreadPoolExecutor(max_workers=args.jobs) as ex:
        futs = {ex.submit(fetch_one, m, pkg, cur, args.out, args.all,
                           args.arch): pkg for pkg, cur in jobs}
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
