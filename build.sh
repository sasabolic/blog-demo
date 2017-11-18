#!/bin/sh

echo "Building"

# Build Docker images for blog-demo-command & blog-demo-query projects
./gradlew clean buildDocker