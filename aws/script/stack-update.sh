#!/bin/sh

SCRIPT_DIR="$(dirname "$0")"
# shellcheck source=src/lib.sh
. "$SCRIPT_DIR/lib.sh"

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <stackName>"
  echo "Example: $0 ktorbase-test"
  exit 1
fi

BASE_NAME=ktorbase
STACK_NAME=$1

TEMPLATES_DIR=$SCRIPT_DIR/../templates
TEMPLATE_FILE=$TEMPLATES_DIR/$BASE_NAME.yml
PARAM_FILE=$TEMPLATES_DIR/$BASE_NAME.json

prepareParamFile "$PARAM_FILE"
echo "Updating stack $STACK_NAME ..."
aws cloudformation update-stack \
  --stack-name "${STACK_NAME}" \
  --template-body file://"${TEMPLATE_FILE}" \
  --parameters file://"${PARAM_FILE}" \
  --capabilities CAPABILITY_NAMED_IAM

if [ $? -ne 0 ]; then
    exit
fi

RES=$(aws cloudformation wait stack-update-complete --stack-name "${STACK_NAME}")
if [ "${RES}" != 0 ]; then
  aws cloudformation describe-stack-events --stack-name "${STACK_NAME}" |
    jq .StackEvents |
    jq '.[] | select(.ResourceStatus == "CREATE_FAILED")' |
    jq -r .ResourceStatusReason |
    head -n 1
fi
