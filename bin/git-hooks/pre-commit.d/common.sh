# Source this file to set up the environment for the hooks.
export CHECKED_OUT_TEST_DIR="${BASH_SOURCE[0]}.tmp"
export GIT_DIR="$(git rev-parse --show-toplevel)/.git"
export GIT_HASH="$(git rev-parse --verify HEAD)"
