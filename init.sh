#!/bin/bash
set -e

echo "Welcome to the init-script of ktorbase!"
echo "---------------------------------------"
read -p "Enter desired location of your new project: " -r DEST_FOLDER
read -p "Enter group-id of your new project: " -r GROUP_ID
read -p "Enter artifact-id of your new project: " -r ARTIFACT_ID
echo "---------------------------------------"

echo "Creating temporary checkout directory ..."
CHECKOUT_DIR=$(mktemp -d)
if [ ! -e "$CHECKOUT_DIR" ]; then
    echo "Failed to temporary checkout directory!"
    exit 1
fi

echo "Cloning ktorbase to temporary checkout directory ($CHECKOUT_DIR) ..."
git clone --depth=1 https://github.com/linked-planet/ktorbase.git "$CHECKOUT_DIR"

echo "Starting project generation using ktorbase ..."
cd "$CHECKOUT_DIR"
set +e
GENERATION_SUCCEEDED=$(./generate.sh "$DEST_FOLDER" "$GROUP_ID" "$ARTIFACT_ID")
if [[ "$GENERATION_SUCCEEDED" != 0 ]]; then
  echo "Error during project generation, see the previous logs for more details!"
fi
set -e

echo "Removing temporary checkout directory ($CHECKOUT_DIR) ..."
rm -rf "$CHECKOUT_DIR"

exit "$GENERATION_SUCCEEDED"
