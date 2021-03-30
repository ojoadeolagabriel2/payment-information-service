#!/bin/sh

set -xe

# shellcheck disable=SC2039
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && cd .. && pwd )"
GAME_VERSION="${ENV_GAME_VERSION:-ruby}"
SERVICE_NAME="${ENV_SERVICE_NAME:-pokemon-information-service}"

# script vars
IMAGE_NAME="truelayer/${SERVICE_NAME}"
VERSION="1.${2:-$(date +%Y%m%d%H%M%S)}"

# switch to project dir
cd "$DIR"

# package app
mvn clean package spring-boot:repackage

# build latest image
docker build -t "${IMAGE_NAME}:latest" .
# build versioned image
docker build -t "${IMAGE_NAME}:${VERSION}" .

# run
docker container rm -f "${SERVICE_NAME}" > /dev/null 2>&1
docker container run -p 50000:50000 -e GAME_VERSION="${GAME_VERSION}" --rm --name "${SERVICE_NAME}" "${IMAGE_NAME}:latest"