#!/bin/bash

# Register prepared run configurations with IntelliJ Project configuration
# Note: These are not checked into the repository under .idea folder, as users
#       often delete these when opening a project for the first time in Idea

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

mkdir -p .idea/runConfigurations
for f in "$SCRIPT_DIR"/runConfigurations/*; do
  echo "symlink $f"
  filename=$(basename -- "$f")
  ln -sfn "$f" ".idea/runConfigurations/$filename"
done
