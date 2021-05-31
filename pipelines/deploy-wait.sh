#!/bin/sh

set -e

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <env> <dockerVersionTag>"
  echo "Example: $0 test latest"
  exit 1
fi

# --------------------------------------------------------------------------------
# PARAMS
# --------------------------------------------------------------------------------
BASE_NAME=ktorbase
ENV=$1
STACK_NAME=$BASE_NAME-$ENV

# --------------------------------------------------------------------------------
# HELPER FUNCTIONS
# --------------------------------------------------------------------------------
echoDemarcation() {
  TEXT=$1
  echo
  echo "--------------------------------------------------------------------------"
  echo "$STACK_NAME: $TEXT"
  echo "--------------------------------------------------------------------------"
}

# --------------------------------------------------------------------------------
# WAIT FOR POTENTIAL CF UPDATES TO COMPLETE
# - actually quite unfortunate, because we have to distinguish waiting for
#   create-complete or update-complete
# - to avoid the trouble, we sleep a while and then simply wait for
#   ecs services to become stable
# - note that we cannot wait immediately for ecs services stable since they will be
#   stable until cloud formation kills the nodes - this happens quite quickly
#   but not immediately
# -> see https://github.com/aws/aws-cli/issues/2887
# --------------------------------------------------------------------------------
echoDemarcation "Wait for deployment operations to complete ..."
sleep 20
aws ecs wait services-stable --cluster "$STACK_NAME" --services "$STACK_NAME-service"

set +e
