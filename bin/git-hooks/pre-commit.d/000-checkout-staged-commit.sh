#!/bin/bash -x
set -e
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "Checking out staged commit."
rm -rf "${STAGED_FILES_TEMP_DIR}"
mkdir --parents "${STAGED_FILES_TEMP_DIR}"
git -C "${STAGED_FILES_TEMP_DIR}" clone --local --quiet --no-checkout "${GIT_DIR}" "${STAGED_FILES_TEMP_DIR}"
git -C "${STAGED_FILES_TEMP_DIR}" checkout --quiet "${GIT_HASH}"
git -C "${STAGED_FILES_TEMP_DIR}" submodule --quiet update --init --recursive --depth=1
