#!/bin/sh

set -e

SCRIPT_DIR="$(dirname "$0")"

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
SERVICE_IMAGE_VERSION=$2
STACK_NAME=$BASE_NAME-$ENV

TEMPLATE_FILE=$SCRIPT_DIR/../aws/templates/$BASE_NAME.yml
PARAM_FILE=$SCRIPT_DIR/../aws/templates/$STACK_NAME.json

# --------------------------------------------------------------------------------
# HELPER FUNCTIONS
# --------------------------------------------------------------------------------
getCfParam() {
  KEY=$1
  jq -r '.[] | select(.ParameterKey == "'"$KEY"'") | .ParameterValue' <"$PARAM_FILE"
}

echoDemarcation() {
  TEXT=$1
  echo
  echo "--------------------------------------------------------------------------"
  echo "$STACK_NAME: $TEXT"
  echo "--------------------------------------------------------------------------"
}

# --------------------------------------------------------------------------------
# TRIGGER DEPLOY
# --------------------------------------------------------------------------------
echoDemarcation "Deploy Cloud Formation Template ..."
DEPLOY_RES=$(
  aws cloudformation deploy \
    --template-file "${TEMPLATE_FILE}" \
    --stack-name "${STACK_NAME}" \
    --capabilities CAPABILITY_NAMED_IAM \
    --no-execute-changeset \
    --no-fail-on-empty-changeset \
    --parameter-overrides \
    "CertificateArn=$(getCfParam CertificateArn)" \
    "ServiceCpu=$(getCfParam ServiceCpu)" \
    "ServiceDeregistrationDelaySeconds=$(getCfParam ServiceDeregistrationDelaySeconds)" \
    "ServiceDeploymentMinimumPercent=$(getCfParam ServiceDeploymentMinimumPercent)" \
    "ServiceDeploymentMaximumPercent=$(getCfParam ServiceDeploymentMaximumPercent)" \
    "ServiceImageName=$(getCfParam ServiceImageName)" \
    "ServiceImageVersion=$SERVICE_IMAGE_VERSION" \
    "ServiceMemory=$(getCfParam ServiceMemory)" \
    "ServiceNodeCount=$(getCfParam ServiceNodeCount)" \
    "ExternalSubnets=$(getCfParam ExternalSubnets)" \
    "InternalSubnets=$(getCfParam InternalSubnets)" \
    "ParameterStoreArn=$(getCfParam ParameterStoreArn)" \
    "VPC=$(getCfParam VPC)" \
    "AppBannerBackgroundColor=$(getCfParam AppBannerBackgroundColor)" \
    "AppBannerMenuBackgroundColor=$(getCfParam AppBannerMenuBackgroundColor)" \
    "AppBaseUrl=$(getCfParam AppBaseUrl)" \
    "AppSsoSaml=$(getCfParam AppSsoSaml)" \
    "AppTitle=$(getCfParam AppTitle)" \
    "CollectdServerIp=$(getCfParam CollectdServerIp)" \
    "SamlIdentityProviderCertificate=$(getCfParam SamlIdentityProviderCertificate)" \
    "SamlIdentityProviderEntityId=$(getCfParam SamlIdentityProviderEntityId)" \
    "SamlIdentityProviderLoginUrl=$(getCfParam SamlIdentityProviderLoginUrl)" \
    "SamlIdentityProviderLogoutUrl=$(getCfParam SamlIdentityProviderLogoutUrl)"
)
echo "$DEPLOY_RES"

# --------------------------------------------------------------------------------
# DESCRIBE AND EXECUTE CHANGE SET IF CREATED
# --------------------------------------------------------------------------------
DESCRIBE_CMD=$(echo "$DEPLOY_RES" | tail -n1)
if echo "$DESCRIBE_CMD" | grep -q "describe-change-set"; then
  DESCRIBE_RES=$(eval "$DESCRIBE_CMD")
  CHANGE_SET_ID=$(echo "$DESCRIBE_RES" | tr '\r\n' ' ' | jq -r .ChangeSetId)
  echo "$DESCRIBE_RES"
  aws cloudformation execute-change-set \
    --change-set-name "$CHANGE_SET_ID" \
    --stack-name "$STACK_NAME"
fi

# --------------------------------------------------------------------------------
# CLEAN UP FAILED CHANGE SETS
# will be created if there were no changes
# -> see https://github.com/aws/aws-cli/issues/4534
# --------------------------------------------------------------------------------
echoDemarcation "Clean up failed change sets ..."
CHANGE_SETS=$(aws cloudformation list-change-sets \
  --stack-name "${STACK_NAME}" \
  --query "Summaries[?Status==\`FAILED\`].ChangeSetId" \
  --output text)
for CHANGE_SET in $CHANGE_SETS; do
  echo "$STACK_NAME: Deleting change set: ${CHANGE_SET} ..."
  aws cloudformation delete-change-set --change-set-name "${CHANGE_SET}"
done
