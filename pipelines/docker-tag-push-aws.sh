#!/bin/sh

set -e

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <existingTag> <newTag>"
  exit 1
fi

EXISTING_TAG=$1
NEW_TAG=$2

DOCKER_REGISTRY_NAME=ktorbase
DOCKER_PROJECT_NAME=service
DOCKER_IMAGE_NAME=${DOCKER_REGISTRY_NAME}/${DOCKER_PROJECT_NAME}
AWS_DOCKER_IMAGE_NAME="${AWS_REGISTRY_URL}/${DOCKER_IMAGE_NAME}"

# aws login
eval "$(aws ecr get-login --region "${AWS_DEFAULT_REGION}" --no-include-email)"

# docker
docker pull "${AWS_DOCKER_IMAGE_NAME}:${EXISTING_TAG}"
docker tag "${AWS_DOCKER_IMAGE_NAME}:${EXISTING_TAG}" "${AWS_DOCKER_IMAGE_NAME}:${NEW_TAG}"
docker push "${AWS_DOCKER_IMAGE_NAME}:${NEW_TAG}"

set +e
