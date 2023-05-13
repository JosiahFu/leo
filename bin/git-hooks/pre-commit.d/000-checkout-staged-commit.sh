#!/bin/bash

echo "Checking out staged commit."

commands () {
  set -e
  . "$(dirname "${BASH_SOURCE[0]}")/common.sh"

  rm -rf "${STAGED_FILES_TEMP_DIR}"
  mkdir --parents "${STAGED_FILES_TEMP_DIR}"

  # HEAD in the original repository gets updated to point to the staged commit
  # when there's a checkout in the cloned repository, when it shouldn't. So,
  # back it up here to restore it after the git operations.
  OLD_HEAD="$(cat "${GIT_DIR}/HEAD")"

  git clone --no-checkout "${GIT_DIR}" "${STAGED_FILES_TEMP_DIR}"
  git -C "${STAGED_FILES_TEMP_DIR}" checkout "${GIT_HASH}"
  git -C "${STAGED_FILES_TEMP_DIR}" submodule update --init --recursive --depth=1

  # Restore HEAD to its original value.
  echo -n "${OLD_HEAD}" > "${GIT_DIR}/HEAD"
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
