# Blog demo

This is a demo project showing the usage of Event Sourcing and CQRS.
You will have to have Docker and Java 9 installed in order to run this demo.

## Testing

To run the application tests execute this command:

    ./gradlew test


## Running with Docker

Build a Docker image for the demo application by running:

    ./gradlew clean buildDocker

This will create the `blog-demo` Docker image in your machine's local Docker image registry.
To start the demo application, zookeper and kafka servers, run:

    docker-compose -f src/main/docker/docker-compose.yml up -d

You should now be able to access REST endpoints e.g.

http://localhost:8080/post 

To stop and remove the containers, run:

    docker-compose -f src/main/docker/docker-compose.yml down