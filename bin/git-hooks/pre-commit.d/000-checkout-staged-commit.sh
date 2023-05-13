#!/bin/bash

echo "Checking out staged commit."

commands () {
  set -e
  . "$(dirname "${BASH_SOURCE[0]}")/common.sh"

  rm -rf "${STAGED_FILES_TEMP_DIR}"
  mkdir --parents "${STAGED_FILES_TEMP_DIR}"
  git -C "${STAGED_FILES_TEMP_DIR}" clone --local --no-checkout "${GIT_DIR}" "${STAGED_FILES_TEMP_DIR}"
  git -C "${STAGED_FILES_TEMP_DIR}" checkout "${GIT_HASH}"
  git -C "${STAGED_FILES_TEMP_DIR}" submodule update --init --recursive --depth=1
}
OUTPUT="$(commands 2>&1)"

if [ $? -ne 0 ]; then
  echo "${OUTPUT}"
  echo -e
  echo "   !!! Checkout failed. !!!"
  echo -e
  echo "   Please examine the output above and try again."
  exit 1
fi
