#!/usr/bin/env bash
# Regression check: generates every sample project in runtime-EclipseXtext/
# and compares the result with the committed (Eclipse generated) src-gen/.
#
#   scripts/check-samples.sh                      # uses bin/xrm (Docker)
#   XRM="java -jar path/to/xrm-cli.jar" scripts/check-samples.sh
set -euo pipefail
cd "$(dirname "$0")/.."
XRM=${XRM:-bin/xrm}
out=target/samples
rm -rf "$out"
failed=0
for dir in runtime-EclipseXtext/*/; do
  name=$(basename "$dir")
  echo "== $name"
  if ! $XRM generate -o "$out/$name" "$dir"; then
    echo "FAIL: $name has validation errors"; failed=1; continue
  fi
  if ! diff -ru "$dir/src-gen" "$out/$name"; then
    echo "FAIL: $name output differs"; failed=1
  fi
done
[ $failed -eq 0 ] && echo "All samples match." || exit 1
