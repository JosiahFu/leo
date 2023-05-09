#!/bin/bash -x
set -e

echo "Preparing staged files for maven."
TEMP_DIR="${BASH_SOURCE[0]}.tmp"
GIT_DIR="$(git rev-parse --show-toplevel)/.git"
GIT_HASH="$(git rev-parse --verify HEAD)"

echo "Checking out staged files."
mkdir --parents "${TEMP_DIR}/.git"
git -C "${TEMP_DIR}" --git-dir="${GIT_DIR}" checkout --quiet --force "${GIT_HASH}"
git -C "${TEMP_DIR}" --git-dir="${GIT_DIR}" submodule --quiet update --init --force --checkout --recursive --depth=1
# -d: Remove untracked directories in addition to untracked files.
git -C "${TEMP_DIR}" --git-dir="${GIT_DIR}" clean --quiet --force -d

echo "Executing Maven."
mvn --file "${TEMP_DIR}/pom.xml" clean package
if [ $? -ne 0 ]; then
  echo "Maven tests failed. Please run 'mvn package' locally and commit the fixes."
  echo "${MVN_OUTPUT}" >&2
  exit 1
fi

echo "Checking for code formatting fixes."
git --git-dir="${GIT_DIR}" diff --exit-code "${GIT_HASH}" "${TEMP_DIR}"
if [ $? -ne 0 ]; then
  echo "Code formatting fixes detected. Please run 'mvn package' locally and commit the changes."
  exit 1
fi
