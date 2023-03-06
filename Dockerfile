# Get the base image from Docker Hub.
FROM ubuntu:22.04

# Install Apache Maven, Git, Java, Nodejs, and Sass.
RUN apt update && apt install -y maven git openjdk-17-doc openjdk-17-jdk openjdk-17-source

# Check out the Project Leo source code and submodules.
WORKDIR /home
ADD project_leo/ project_leo/
WORKDIR /home/project_leo

# Build Project Leo using the Apache Maven tool.
RUN mvn verify

# Expose port 8080 for the Spring server.
EXPOSE 8080

# Run the Spring server in standalone mode from the root project folder.
# CMD does not support wildcards, so we have to specify the exact JAR file name.
CMD ["java", "-jar", "/home/project_leo/server/target/project-leo-server-0.0.1-SNAPSHOT.jar"]
