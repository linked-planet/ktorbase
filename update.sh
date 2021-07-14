#!/bin/sh
set -e

# Updates an existing project using the latest version of ktorbase.

if [ "$#" -eq 4 ]; then
   # parameters
  echo "Starting update in parameterized mode ..."
  PROJECT_FOLDER=$1
  GROUP_ID=$2
  ARTIFACT_ID=$3
  GIT_BRANCH=$4
elif [ "$#" -eq 0 ]; then
  # interactive
  echo "Starting update in interactive mode ..."
  echo "Enter path to your existing project [./ktor-example]: "
  read -r DEST_FOLDER_INPUT
  PROJECT_FOLDER=${DEST_FOLDER_INPUT:-./ktor-example}
  echo "Enter group-id of your existing project [com.linked-planet]: "
  read -r GROUP_ID_INPUT
  GROUP_ID=${GROUP_ID_INPUT:-com.linked-planet}
  echo "Enter artifact-id of your existing project [ktor-example]: "
  read -r ARTIFACT_ID_INPUT
  ARTIFACT_ID=${ARTIFACT_ID_INPUT:-ktor-example}
  echo "Enter desired branch of ktorbase repo [master]: "
  read -r GIT_BRANCH_INPUT
  GIT_BRANCH=${GIT_BRANCH_INPUT:-master}
else
  # unknown
  echo "- Interactive usage: $0"
  echo "- Parameterized usage: $0 <project-folder> <group-id> <artifact-id> <git-branch of ktorbase repo>"
  echo "  Parameterized example: $0 ~/tmp com.linked-planet example-project master"
  exit 1
fi

echo "------- Attention! -------"
echo "Ensure your project uses a VCS (version control system) - otherwise all your changes could be lost!"
echo "Waiting 10s, abort using Ctrl+C ..."
sleep 10

echo "Resolving absolute path to project-folder ..."
PROJECT_FOLDER=$(realpath "$PROJECT_FOLDER")

echo "Creating temporary generation directory ..."
GEN_DIR=$(mktemp -d)
if [ ! -e "$GEN_DIR" ]; then
    echo "Failed to create temporary generation directory!"
    exit 1
fi

echo "Downloading init.sh to current directory ..."
cd /tmp && { curl -O "https://raw.githubusercontent.com/linked-planet/ktorbase/${GIT_BRANCH}/init.sh" ; cd -; }

echo "Generating project in temporary generation directory ..."
set +e
bash /tmp/init.sh "$GEN_DIR" "$GROUP_ID" "$ARTIFACT_ID" "$GIT_BRANCH"
GENERATION_SUCCEEDED=$?
if [ "$GENERATION_SUCCEEDED" -eq 0 ]; then
  echo "Copying contents of temporary generated project ($GEN_DIR/$GROUP_ID.$ARTIFACT_ID/) to existing project directory ($PROJECT_FOLDER) ..."
  cp -R "$GEN_DIR/$GROUP_ID.$ARTIFACT_ID/." "$PROJECT_FOLDER"
  UPDATE_SUCCEEDED=$?
else
  echo "Error during temporary project generation, see the previous logs for more details!"
fi
set -e

echo "Removing init.sh in current directory ..."
rm /tmp/init.sh

echo "Removing temporary project directory ($GEN_DIR) ..."
rm -rf "$GEN_DIR"

if [ "$GENERATION_SUCCEEDED" -eq 0 ] && [ "$UPDATE_SUCCEEDED" -eq 0 ]; then
  echo "Successfully updated project in $PROJECT_FOLDER!"
else
  echo "Error during project update, see the previous logs for more details!"
fi
echo "Hint: Check the updates within your VCS."
