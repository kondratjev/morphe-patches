#!/usr/bin/env python3
"""Report-only checker: compares Constants.kt targets with Google Play.

Usage:
  python3 check-app-updates.py [--only <pkg>]

Prints a JSON summary, writes nothing. Used by CI
(.github/workflows/check-updates.yml). Downloading lives in
download-all-apks.py; bumping Constants.kt is always manual.
"""
import argparse
import glob
import json
import os
import re
import sys
import urllib.request

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 Chrome/126.0 Safari/537.36"
PLAY_URL = "https://play.google.com/store/apps/details?id={pkg}&hl=en&gl=US"

PKG_RE = re.compile(r'packageName\s*=\s*"([^"]+)"')
TARGET_RE = re.compile(r'AppTarget\("([^"]+)"\)')
DATE_FMT = re.compile(r'^\d{4}\.\d{2}\.\d{2}-release$')
# Play embeds version history; current release is the newest entry.
# Anchor shape: "..."]]],"VERSION",null,null,null,...
ANCHOR_RE = re.compile(r'"\]\]\],"([^"]{1,32})",null,null,null')
TRI_RE = re.compile(r'\[\[\["([\w.\-() ]+)"\]\]')


def _vkey(v):
    v = v.strip()
    if DATE_FMT.match(v):
        return (1, (int(v[0:4]), int(v[5:7]), int(v[8:10])))
    return (0, tuple(int(p) for p in re.findall(r'\d+', v)) or (0,))


def _same_style(a, b):
    return (DATE_FMT.match(a) is not None) == (DATE_FMT.match(b) is not None)


def is_newer(play, current):
    """True only if play version is strictly newer than every current target."""
    same = [v for v in current if _same_style(v, play)]
    if not same:
        return play not in current
    return _vkey(play) > max(_vkey(v) for v in same)


def plausible(v):
    v = v.strip()
    return bool(v) and len(v) <= 32 and any(c.isdigit() for c in v) and "." in v


def play_version(pkg, current):
    req = urllib.request.Request(PLAY_URL.format(pkg=pkg), headers={"User-Agent": UA})
    try:
        with urllib.request.urlopen(req, timeout=20) as r:
            html = r.read().decode("utf-8", "ignore")
    except Exception as e:
        print(f"skip {pkg}: network error: {e}", file=sys.stderr)
        return None
    dated = any(DATE_FMT.match(v) for v in current)
    cands = set()
    for rx in (ANCHOR_RE, TRI_RE):
        for m in rx.finditer(html):
            v = m.group(1).strip()
            if plausible(v) and (DATE_FMT.match(v) is not None) == dated:
                cands.add(v)
    if not cands:
        print(f"skip {pkg}: version not found in Play page", file=sys.stderr)
        return None
    return max(cands, key=_vkey)


def log(msg):
    print(msg, file=sys.stderr, flush=True)


def targets():
    """Yield (constants_path, package, [targets]) for every patched app."""
    files = sorted(glob.glob(os.path.join(
        ROOT, "patches", "src", "main", "kotlin", "**", "shared", "Constants.kt"),
        recursive=True))
    for path in files:
        with open(path, encoding="utf-8") as f:
            text = f.read()
        m = PKG_RE.search(text)
        if not m:
            continue
        yield path, m.group(1), TARGET_RE.findall(text)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--only", default="")
    args = ap.parse_args()
    summary = []
    for path, pkg, current in targets():
        if args.only and args.only != pkg:
            continue
        log(f"CHECK {pkg} (current: {', '.join(current)}) ...")
        play = play_version(pkg, current)
        entry = {"package": pkg, "current": current, "play": play,
                 "update_available": False}
        if play is None or play in current or not is_newer(play, current):
            log(f"  -> {play or 'unknown'}: "
                f"{'up-to-date' if play in current else 'skip'}")
        else:
            log(f"  -> {play}: NEW")
            entry["update_available"] = True
        summary.append(entry)
    print(json.dumps(summary, indent=2, ensure_ascii=False))


if __name__ == "__main__":
    main()
