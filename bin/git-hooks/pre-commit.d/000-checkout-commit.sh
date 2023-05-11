#!/bin/bash -x
set -e
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "Checking out staged commit."
rm -rf "${CHECKED_OUT_TEST_DIR}"
mkdir --parents "${CHECKED_OUT_TEST_DIR}"
git -C "${CHECKED_OUT_TEST_DIR}" clone --local --quiet --no-checkout "${GIT_DIR}" "${CHECKED_OUT_TEST_DIR}"
git -C "${CHECKED_OUT_TEST_DIR}" checkout --quiet "${GIT_HASH}"
git -C "${CHECKED_OUT_TEST_DIR}" submodule --quiet update --init --recursive --depth=1
