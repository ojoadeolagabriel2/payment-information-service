#!/bin/sh

set -xe

ROOT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

"${ROOT_DIR}"/scripts/build_publish_to_docker.sh
"${ROOT_DIR}"/scripts/publish_to_kubernetes.sh