# Source this file to set up the environment for the hooks.
export STAGED_FILES_TEMP_DIR="$(dirname "${BASH_SOURCE[0]}")/staged_files_temp_dir.tmp"
export GIT_DIR="$(git rev-parse --show-toplevel)/.git"
export GIT_HASH="$(git rev-parse --verify HEAD)"
