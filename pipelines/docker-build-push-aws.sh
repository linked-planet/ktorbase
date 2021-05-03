#!/bin/sh

set -e

DOCKER_TAGS=$@

DOCKER_REGISTRY_NAME=ktorbase
DOCKER_PROJECT_NAME=service
DOCKER_IMAGE_NAME=${DOCKER_REGISTRY_NAME}/${DOCKER_PROJECT_NAME}

# aws login
eval "$(aws ecr get-login --region "${AWS_DEFAULT_REGION}" --no-include-email)"

# create ecr repository if it does not exist
if ! aws ecr list-images --repository-name "${DOCKER_IMAGE_NAME}" >/dev/null 2>/dev/null; then
  echo "INFO: ${AWS_REGISTRY_URL}/${DOCKER_IMAGE_NAME} does not exist, we try to create it"
  aws ecr create-repository --repository-name ${DOCKER_IMAGE_NAME}
fi

# docker
docker build -t ${DOCKER_IMAGE_NAME}:latest .
for tag in ${DOCKER_TAGS}
do
    docker tag "${DOCKER_IMAGE_NAME}:latest" "${AWS_REGISTRY_URL}/${DOCKER_IMAGE_NAME}:${tag}"
    docker push "${AWS_REGISTRY_URL}/${DOCKER_IMAGE_NAME}:${tag}"
done

set +e
