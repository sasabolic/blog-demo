#!/bin/sh

echo "Building"

# Build blog-demo-command project
blog-demo-command/./gradlew -p blog-demo-command clean buildDocker

# Build blog-demo-query project
blog-demo-query/./gradlew -p blog-demo-query clean buildDocker