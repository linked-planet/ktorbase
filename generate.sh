#!/bin/bash

# Generates a new project based on the contents of this project.
# Basically works by simply copying all files to a destination. Names will be replaced appropriately.
# As the template represents a maximum build, take care to remove the parts you do not need after generation manually.

if [ "$#" -ne 3 ]; then
  echo "Usage: $0 <destination-folder> <group-id> <artifact-id>"
  echo "Example: $0 ~/tmp com.linked-planet example-project"
  exit 1
fi

set -e

DEST_FOLDER=$1
GROUP_ID=$2
ARTIFACT_ID=$3

if [[ "$GROUP_ID" == "com.linkedplanet" ]]; then
  echo "Use group-id com.linked-planet instead of com.linkedplanet!"
  exit 1
fi

NAME=$GROUP_ID.$ARTIFACT_ID
FULL_DEST_FOLDER=$DEST_FOLDER/$NAME
NAME_PATH=$(echo "$NAME" | tr . /)

JAVA_PACKAGE_NAME="${NAME//-}"
JAVA_PACKAGE_PATH="${NAME_PATH//-}"
echo "NAME: $NAME"
echo "NAME PATH: $NAME_PATH"
echo "FULL DEST FOLDER: $FULL_DEST_FOLDER"
echo "JAVA PACKAGE NAME: $JAVA_PACKAGE_NAME"
echo "JAVA PACKAGE PATH: $JAVA_PACKAGE_PATH"
echo

echo "Clean potential output from template builds first ..."
./gradlew clean

echo "Copying files to $FULL_DEST_FOLDER ..."
mkdir -p "$FULL_DEST_FOLDER"
cp -R . "$FULL_DEST_FOLDER"

echo "Clean up non-needed files & folders ..."
rm -rf "$FULL_DEST_FOLDER/.git"
rm -rf "$FULL_DEST_FOLDER/.idea"
rm -rf "$FULL_DEST_FOLDER/.gradle"
rm -rf "$FULL_DEST_FOLDER/.github"
rm "$FULL_DEST_FOLDER/generate.sh"
rm "$FULL_DEST_FOLDER/init.sh"
rm "$FULL_DEST_FOLDER/update.sh"
rm "$FULL_DEST_FOLDER/LICENSE"

echo "Renaming folders ..."
mkdir -p "$FULL_DEST_FOLDER/backend/src/main/kotlin/$JAVA_PACKAGE_PATH"
mkdir -p "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/$JAVA_PACKAGE_PATH"
mkdir -p "$FULL_DEST_FOLDER/frontend/src/main/kotlin/$JAVA_PACKAGE_PATH"
mv "$FULL_DEST_FOLDER/backend/src/main/kotlin/com/linkedplanet/ktorbase"/* "$FULL_DEST_FOLDER/backend/src/main/kotlin/$JAVA_PACKAGE_PATH/"
mv "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com/linkedplanet/ktorbase"/* "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/$JAVA_PACKAGE_PATH/"
mv "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com/linkedplanet/ktorbase"/* "$FULL_DEST_FOLDER/frontend/src/main/kotlin/$JAVA_PACKAGE_PATH/"

echo "Renaming cloud formation template ..."
mv "$FULL_DEST_FOLDER/aws/templates/ktorbase-test.json" "$FULL_DEST_FOLDER/aws/templates/$ARTIFACT_ID-test.json"
mv "$FULL_DEST_FOLDER/aws/templates/ktorbase.yml" "$FULL_DEST_FOLDER/aws/templates/$ARTIFACT_ID.yml"

echo "Clean up obsolete package folders as necessary ..."
if [[ "$GROUP_ID" != "com"* ]]; then
  rm -r "$FULL_DEST_FOLDER/backend/src/main/kotlin/com"
  rm -r "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com"
  rm -r "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com"
elif [[ "$GROUP_ID" != "com.linked-planet"* ]]; then
  rm -r "$FULL_DEST_FOLDER/backend/src/main/kotlin/com/linkedplanet"
  rm -r "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com/linkedplanet"
  rm -r "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com/linkedplanet"
elif [[ "$ARTIFACT_ID" != "ktorbase" ]]; then
  rm -r "$FULL_DEST_FOLDER/backend/src/main/kotlin/com/linkedplanet/ktorbase"
  rm -r "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com/linkedplanet/ktorbase"
  rm -r "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com/linkedplanet/ktorbase"
fi

echo "Replacing group id in source files ..."
# make sure to skip .kt files, as there might be valid com.linkedplanet imports (from our libraries)
find "$FULL_DEST_FOLDER" -type f -not -name "*.kt" -exec sed -i "s/com.linked-planet/$GROUP_ID/g" {} +

echo "Replacing package names and imports in source files ..."
find "$FULL_DEST_FOLDER" -type f -exec sed -i "s/com.linkedplanet.ktorbase/$JAVA_PACKAGE_NAME/g" {} +

echo "Replacing artifact id in source files ..."
find "$FULL_DEST_FOLDER" -type f -exec sed -i "s/ktorbase/$ARTIFACT_ID/g" {} +

echo "Fresh README.md ..."
echo "# $ARTIFACT_ID" > "$FULL_DEST_FOLDER/README.md"

echo "Done! Have fun hakking! Remember to read the SICP!! :-)"
echo "https://cpsc.yale.edu/epigrams-programming"
