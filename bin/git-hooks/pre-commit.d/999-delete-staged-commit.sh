#!/bin/bash

echo "Deleting staged commit."

commands () {
  set -e
  . "$(dirname "${BASH_SOURCE[0]}")/common.sh"

  rm -rf "${STAGED_FILES_TEMP_DIR}"
}
OUTPUT="$(commands 2>&1)"

if [ $? -ne 0 ]; then
  echo "${OUTPUT}"
  echo -e
  echo "   !!! Deletion failed. !!!"
  echo -e
  echo "   Please examine the output above and try again."
  exit 1
fi
