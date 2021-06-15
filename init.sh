#!/bin/bash
set -e

if [[ -z $1 && -z $2 && -z $3 ]]; then
  # interactive
  echo "Starting in interactive mode ..."
  read -p "Enter destination-folder for your new project [./ktor-example]: " -r DEST_FOLDER_INPUT
  DEST_FOLDER=${DEST_FOLDER_INPUT:-./ktor-example}
  read -p "Enter group-id of your new project [com.linked-planet]: " -r GROUP_ID
  GROUP_ID=${GROUP_ID_INPUT:-com.linked-planet}
  read -p "Enter artifact-id of your new project [ktor-example]: " -r ARTIFACT_ID
  ARTIFACT_ID=${ARTIFACT_ID_INPUT:-ktor-example}
  read -p "Enter branch of ktorbase repo [master]: " -r ARTIFACT_ID
  ARTIFACT_ID=${ARTIFACT_ID_INPUT:-ktor-example}
elif [[ -z $1 || -z $2 || -z $3 ]]; then
  # unknown
  echo "- Interactive usage: $0"
  echo "- Parameterized usage: $0 <destination-folder> <group-id> <artifact-id> <git-branch of ktorbase repo>"
  echo "  Parameterized example: $0 ~/tmp com.linked-planet example-project master"
  exit 1
else
  # parameters
  echo "Starting in parameterized mode ..."
  DEST_FOLDER=$1
  GROUP_ID=$2
  ARTIFACT_ID=$3
  GIT_BRANCH=$4
fi

echo "Resolving absolute path to destination-folder ..."
DEST_FOLDER=$(realpath "$DEST_FOLDER")

echo "Creating temporary checkout directory ..."
CHECKOUT_DIR=$(mktemp -d)
if [ ! -e "$CHECKOUT_DIR" ]; then
    echo "Failed to create temporary checkout directory!"
    exit 1
fi

echo "Cloning ktorbase to temporary checkout directory ($CHECKOUT_DIR) ..."
git clone --branch="$GIT_BRANCH" --single-branch --depth=1 https://github.com/linked-planet/ktorbase.git "$CHECKOUT_DIR"

echo "Starting project generation using ktorbase ..."
cd "$CHECKOUT_DIR"
set +e
./generate.sh "$DEST_FOLDER" "$GROUP_ID" "$ARTIFACT_ID"
GENERATION_SUCCEEDED=$?
if [[ "$GENERATION_SUCCEEDED" == 0 ]]; then
  echo "Successfully created new ktorbase project in $DEST_FOLDER/$GROUP_ID.$ARTIFACT_ID!"
else
  echo "Error during project generation, see the previous logs for more details!"
fi
set -e

echo "Removing temporary checkout directory ($CHECKOUT_DIR) ..."
rm -rf "$CHECKOUT_DIR"

exit "$GENERATION_SUCCEEDED"
