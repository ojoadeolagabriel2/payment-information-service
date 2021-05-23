#!/bin/sh

set -xe

# shellcheck disable=SC2039
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && cd ../.. && pwd )"
DOCKER_ACCESS_TOKEN="${ENV_DOCKER_ACCESS_TOKEN:-51551b89-7a36-48a9-a8d1-e41c95b0077e}"
DOCKER_USERNAME="${ENV_DOCKER_USERNAME:-ojoadeolagabriel}"
SERVICE_NAME="${ENV_SERVICE_NAME:-pokemon-information-service}"

# script vars
IMAGE_NAME="ojoadeolagabriel/${SERVICE_NAME}"
VERSION="1.${2:-$(date +%Y%m%d%H%M%S)}"

# switch to project dir
cd "$DIR"

# package app
mvn clean package spring-boot:repackage

# docker login
echo "${DOCKER_ACCESS_TOKEN}" | docker login --username "${DOCKER_USERNAME}" --password-stdin

# build latest image
docker build -t "${IMAGE_NAME}:latest" .
# build versioned image
docker build -t "${IMAGE_NAME}:${VERSION}" .
# push
docker push "${IMAGE_NAME}:latest"