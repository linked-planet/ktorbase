#!/bin/sh

getOutputValue() {
  STACK_NAME=$1
  KEY=$2
  OUTPUTS=$(aws cloudformation describe-stacks --stack-name "$STACK_NAME" --query "Stacks[0].Outputs[]" --output json)
  echo "$OUTPUTS" | jq '.[] | select(.OutputKey == "'"$KEY"'")' | jq -r .OutputValue
}

overwriteParameter() {
  KEY=$1
  VALUE=$2
  PARAM_FILE=$3
  TMP_FILE=tmp.json
  jq <"$PARAM_FILE" '.[] | select(.ParameterKey == "'"$KEY"'") .ParameterValue = '"$VALUE"'' | jq --slurp '.' >"$TMP_FILE"
  mv "$TMP_FILE" "$PARAM_FILE"
}

prepareParamFile() {
  PARAM_FILE=$1
  cp "$PARAM_FILE.tmpl" "$PARAM_FILE"
  sed -i "s|{{AWS_CERTIFICATE_ARN}}|$AWS_CERTIFICATE_ARN|g" "$PARAM_FILE"
  sed -i "s|{{AWS_COCKPIT_BASE_URL}}|$AWS_COCKPIT_BASE_URL|g" "$PARAM_FILE"
  sed -i "s|{{AWS_COCKPIT_CPU}}|$AWS_COCKPIT_CPU|g" "$PARAM_FILE"
  sed -i "s|{{AWS_COCKPIT_DEREGISTRATION_DELAY_SECONDS}}|$AWS_COCKPIT_DEREGISTRATION_DELAY_SECONDS|g" "$PARAM_FILE"
  sed -i "s|{{AWS_COCKPIT_IMAGE}}|$AWS_COCKPIT_IMAGE|g" "$PARAM_FILE"
  sed -i "s|{{AWS_COCKPIT_MEMORY}}|$AWS_COCKPIT_MEMORY|g" "$PARAM_FILE"
  sed -i "s|{{AWS_COLLECTD_SERVER_IP}}|$AWS_COLLECTD_SERVER_IP|g" "$PARAM_FILE"
  sed -i "s|{{AWS_EXTERNAL_SUBNETS}}|$AWS_EXTERNAL_SUBNETS|g" "$PARAM_FILE"
  sed -i "s|{{AWS_INTERNAL_SUBNETS}}|$AWS_INTERNAL_SUBNETS|g" "$PARAM_FILE"
  sed -i "s|{{AWS_PARAMETER_STORE_ARN}}|$AWS_PARAMETER_STORE_ARN|g" "$PARAM_FILE"
  sed -i "s|{{AWS_VPC}}|$AWS_VPC|g" "$PARAM_FILE"
  sed -i "s|{{AWS_JIRA_BASE_URL}}|$AWS_JIRA_BASE_URL|g" "$PARAM_FILE"
  sed -i "s|{{AWS_CONFLUENCE_BASE_URL}}|$AWS_CONFLUENCE_BASE_URL|g" "$PARAM_FILE"
}
