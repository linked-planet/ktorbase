#!/bin/bash

# Generates a new project based on the contents of this project.
# Basically works by simply copying all files to a destination. Names will be replaced appropriately.
# As the template represents a maximum build, take care to remove the parts you do not need after generation manually.

if [ "$#" -ne 3 ]; then
  echo "Usage: $0 <destination-folder> <group-id> <artifact-id>"
  echo "Example: $0 ~/tmp com.linkedplanet ktorbase"
  exit 1
fi

DEST_FOLDER=$1
GROUP_ID=$2
ARTIFACT_ID=$3

NAME=$GROUP_ID.$ARTIFACT_ID
FULL_DEST_FOLDER=$DEST_FOLDER/$NAME
NAME_PATH=$(echo "$NAME" | tr . /)

JAVA_PACKAGE_NAME=$(echo "$NAME" | tr '-' '')
JAVA_PACKAGE_PATH=$(echo "$NAME_PATH" | tr '-' '')

echo "Copying files to $FULL_DEST_FOLDER ..."
mkdir -p "$FULL_DEST_FOLDER"
cp -R . "$FULL_DEST_FOLDER"
rm -rf "$FULL_DEST_FOLDER/.git"
rm -rf "$FULL_DEST_FOLDER/.idea"
rm -rf "$FULL_DEST_FOLDER/.gradle"

echo "Renaming folders ..."
mkdir -p "$FULL_DEST_FOLDER/backend/src/main/kotlin/$JAVA_PACKAGE_PATH"
mkdir -p "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/$JAVA_PACKAGE_PATH"
mkdir -p "$FULL_DEST_FOLDER/frontend/src/main/kotlin/$JAVA_PACKAGE_PATH"
mv "$FULL_DEST_FOLDER/backend/src/main/kotlin/com/linkedplanet/ktorbase"/* "$FULL_DEST_FOLDER/backend/src/main/kotlin/$JAVA_PACKAGE_PATH/"
mv "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com/linkedplanet/ktorbase"/* "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/$JAVA_PACKAGE_PATH/"
mv "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com/linkedplanet/ktorbase"/* "$FULL_DEST_FOLDER/frontend/src/main/kotlin/$JAVA_PACKAGE_PATH/"

echo "Renaming cloud formation template ..."
mv "$FULL_DEST_FOLDER/aws/templates/ktorbase.json.tmpl" "$FULL_DEST_FOLDER/aws/templates/$ARTIFACT_ID.json.tmpl"
mv "$FULL_DEST_FOLDER/aws/templates/ktorbase.yml" "$FULL_DEST_FOLDER/aws/templates/$ARTIFACT_ID.yml"

echo "Clean up ..."
# clean up com / linkedplanet folder(s) as necessary
if [[ "$GROUP_ID" != "com"* ]]; then
  rm -r "$FULL_DEST_FOLDER/backend/src/main/kotlin/com"
  rm -r "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com"
  rm -r "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com"
elif [[ "$GROUP_ID" != "com.linkedplanet"* ]]; then
  rm -r "$FULL_DEST_FOLDER/backend/src/main/kotlin/com/linkedplanet"
  rm -r "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com/linkedplanet"
  rm -r "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com/linkedplanet"
elif [[ "$ARTIFACT_ID" != "ktorbase" ]]; then
  rm -r "$FULL_DEST_FOLDER/backend/src/main/kotlin/com/linkedplanet/ktorbase"
  rm -r "$FULL_DEST_FOLDER/common/src/commonMain/kotlin/com/linkedplanet/ktorbase"
  rm -r "$FULL_DEST_FOLDER/frontend/src/main/kotlin/com/linkedplanet/ktorbase"
fi
# clean up obsolete files
rm "$FULL_DEST_FOLDER/generate.sh"
rm "$FULL_DEST_FOLDER/LICENSE"

echo "Replacing group id in source files ..."
# make sure to skip .kt files, as there might be valid com.linkedplanet imports (from our libraries)
find "$FULL_DEST_FOLDER" -type f -not -name "*.kt" -exec sed -i "s/com.linkedplanet/$GROUP_ID/g" {} +

echo "Replacing package names and imports in source files ..."
find "$FULL_DEST_FOLDER" -type f -exec sed -i "s/com.linkedplanet.ktorbase/$JAVA_PACKAGE_NAME/g" {} +

echo "Replacing artifact id in source files ..."
find "$FULL_DEST_FOLDER" -type f -exec sed -i "s/ktorbase/$ARTIFACT_ID/g" {} +

echo "Fresh README.md ..."
echo "# $ARTIFACT_ID" > "$FULL_DEST_FOLDER/README.md"
echo "TODO"
echo "* Say something about this project" >> "$FULL_DEST_FOLDER/README.md"
echo "* initial build the app by ./gradlew clean build"

echo "Done! Have fun hakking! Remember to read the SICP!! :-)"

echo "'I think that it's extraordinarily important that we in computer science keep fun in computing. When it started
out, it was an awful lot of fun. Of course, the paying customers got shafted every now and then, and after a while we
began to take their complaints seriously. We began to feel as if we really were responsible for the successful,
error-free perfect use of these machines. I don't think we are. I think we're responsible for stretching them, setting
them off in new directions, and keeping fun in the house. I hope the field of computer science never loses its sense of
fun. Above all, I hope we don't become missionaries. Don't feel as if you're Bible salesmen. The world has too many of
those already. What you know about computing other people will learn. Don't feel as if the key to successful computing
is only in your hands. What's in your hands, I think and hope, is intelligence: the ability to see the machine as more
than when you were first led up to it, that you can make it more.'

Alan J. Perlis (April 1, 1922-February 7, 1990)"
