#!/bin/bash

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

STACK_STATE=$(aws cloudformation describe-stacks --stack-name "$STACK_NAME" --query 'Stacks[].StackStatus' --output text)
if [ "$STACK_STATE" == "CREATE_IN_PROGRESS" ] ; then
  echo "Stack does not exist, waiting for creation ..."
  aws cloudformation wait stack-create-complete --stack-name "$STACK_NAME"
elif [ "$STACK_STATE" == "UPDATE_IN_PROGRESS" ] ; then
  echo "Stack exists, waiting for update ..."
  aws cloudformation wait stack-update-complete --stack-name "$STACK_NAME"
elif [ "$STACK_STATE" == "CREATE_COMPLETE" ] || [ "$STACK_STATE" == "UPDATE_COMPLETE" ] ; then
  echo "Stack was up to date, continuing ..."
else
  echo "Unexpected state - stopping with error ..."
  exit 1
fi

set +e
