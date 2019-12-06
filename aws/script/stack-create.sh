#!/bin/sh

# ----------------------------------------------------------------------
# Creates the ktorbase stack.
#
# If the CI flag is set, the script will add the current timestamp to
# the stack name. This is useful for temporarily spinning up the stack
# to see whether everything works.
# ----------------------------------------------------------------------

SCRIPT_DIR="$(dirname "$0")"
# shellcheck source=src/lib.sh
. "$SCRIPT_DIR/lib.sh"

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <test|prod>"
  echo "Example: $0 test"
  exit 1
fi

BASE_NAME=ktorbase
ENV=$1

STACK_NAME=$BASE_NAME-$ENV
TEMPLATES_DIR=$SCRIPT_DIR/../templates
TEMPLATE_FILE=$TEMPLATES_DIR/$BASE_NAME.yml
PARAM_FILE=$TEMPLATES_DIR/$BASE_NAME.json

if aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE UPDATE_COMPLETE | grep "/$STACK_NAME/" >/dev/null 2>/dev/null; then
  echo "Stack ${STACK_NAME} is running, so we update it"
  "${SCRIPT_DIR}/stack-update.sh" "${STACK_NAME}"
  exit $?
fi

prepareParamFile "$PARAM_FILE"
echo "Creating stack $STACK_NAME ..."
STACK_ID=$(aws cloudformation create-stack \
  --stack-name "${STACK_NAME}" \
  --template-body file://"${TEMPLATE_FILE}" \
  --parameters file://"${PARAM_FILE}" \
  --capabilities CAPABILITY_NAMED_IAM |
  jq -r .StackId)

CREATE_RES=$(aws cloudformation wait stack-create-complete --stack-name "${STACK_ID}")
if [ "${CREATE_RES}" != 0 ]; then
  aws cloudformation describe-stack-events --stack-name "${STACK_ID}" |
    jq .StackEvents |
    jq '.[] | select(.ResourceStatus == "CREATE_FAILED")' |
    jq -r .ResourceStatusReason
fi
