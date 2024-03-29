# Building Project Leo

Project Leo is built on Linux using [Apache Maven](https://maven.apache.org/).
The instructions on this page are for the [Ubuntu](https://ubuntu.com/)
distribution. But, other distributions may work as well.

If you use Windows, you can install an instance of
[Ubuntu 22.04 LTS on Windows](https://www.microsoft.com/store/productId/9PN20MSR04DW)
from the Microsoft Store to work in.

## Checking out Source Code

Project Leo has references to other git repositories (submodules). To do a
full checkout, you need to check out its submodules as well. Run the following
commands in a Terminal to do this:

```shell
# Check out the Project Leo source code and submodules.
git clone git@github.com:DaVinciSchools/leo.git project_leo
cd project_leo
git submodule update --init --recursive
```

This will create a subfolder called ```project_leo``` with the source code in
it.

## Build Dependencies

Building and running Project Leo requires the following dependencies:

* [Apache Maven 3](https://maven.apache.org/)
* [Java 17](https://www.java.com/)
* [Node.js](https://nodejs.org/)
* [Docker](https://www.docker.com/)

The first two of these can be installed in Ubuntu using the following
commands:

```shell
# Install Apache Maven and Java.
sudo apt update
sudo apt install maven openjdk-17-doc openjdk-17-jdk openjdk-17-source
```

### Build Dependencies: Node.js

[Node.js](https://nodejs.org/) in the standard Ubuntu repository is very old.

We have a helper script that will add the official Node.js repository and
install ```nodejs``` for you:
[bin/apt/install-nodejs-ubuntu](https://github.com/DaVinciSchools/leo/blob/main/bin/apt/install-nodejs-ubuntu).

### Build Dependencies: Docker Desktop

If you are on a non-production server, it's easiest to install
[Docker Desktop](https://docs.docker.com/get-docker/).

We have a helper script that will add the official Docker repository and
install Docker Desktop for you:
[bin/apt/install-docker-desktop-ubuntu](https://github.com/DaVinciSchools/leo/blob/main/bin/apt/install-docker-desktop-ubuntu).
Log out and log back in so that your group membership is re-evaluated.

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://github.com/Mqxx/GitHub-Markdown/blob/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://github.com/Mqxx/GitHub-Markdown/blob/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
> Be sure to log out and back in. Docker will not work until this is done
> (including for Maven).

## External Dependencies

By default, Project Leo creates temporary test instances of external
dependencies. To override these to point to real resources, create an
[external properties file](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.files).

Project Leo will read additional properties in
```${HOME}/project_leo.properties``` by default. Additional files can be
included with the
[spring.config.import](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.files.importing)
command line flag or SPRING_CONFIG_IMPORT environment variable.

The default properties override file, ```${HOME}/project_leo.properties```,
can have the following properties (only specify the ones you want):

```properties
# Set to the OpenAI key used for queries. Its value starts with "sk-".
openai.api.key=<your_api_key>

# Set to "false" so that the database configuration below will be used.
project_leo.use.test.database=false

# The database that Project Leo will connect to.
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

Properties can also be set using environment variables. For instance,
"openai.api.key" can be set using the environment variable "OPENAI_API_KEY".
Environment variables take precedence over property values.

## Building Project Leo

To build Project Leo go to the ```project_leo``` folder and run the following
command:

```shell
# Build Project Leo using the Apache Maven tool.
mvn install
```

This will do a number of things:

* Build the Java Spring server.
* Install Node.js and NPM for the React web client.
* Build the React web client.
* Embed the React web client in the Spring server.
* Run all tests. **Note: These will pause (for a long time) while waiting for 
  docker to start a test database.**
* Format code for a code review (see [CONTRIBUTING](CONTRIBUTING.md)).

## Running & Testing Project Leo

Project Leo can be run in the following ways:

### As a Standalone Spring Server

The standalone Spring server serves the compiled React website from an internal
copy. This is best when working on the Spring server itself. Or for production.

The following command runs the Spring server in this way:

```shell
# Run the Spring server in standalone mode from the root project folder.
java -jar server/target/project-leo-server-*.jar
```

Then, open a browser to http://localhost:8080.

### As Separate Spring and React Servers

Running separate Spring and React servers in parallel allows you to modify the
React content and serve it through the Spring server live. The Spring server
will proxy requests to the React server for http content. It's necessary to run
both because the React website makes calls to the Spring server to retrieve
and set data. This is useful when working on the website.

The following commands run the servers in parallel (Note the ```--react_port```
flag):

```shell
# Run the Spring server from the root project folder.
java -jar server/target/project-leo-server-*.jar --react_port=3000
```

``` shell
# Run the React server from the root project folder.
# Note: You will need to close http://localhost:3000, which the React server
#       automatically opens.
cd server/src/main/clients/web-browser
npm start
```

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://github.com/Mqxx/GitHub-Markdown/blob/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://github.com/Mqxx/GitHub-Markdown/blob/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
> Starting the React server will open a browser to http://localhost:3000. This
> does NOT go through the Spring server and will NOT work. You need to go to
> port 8080 on localhost.

Then, open a browser to http://localhost:8080.

### As a Docker Container

Running the server in a Docker container is most helpful for development
without access to a Linux distribution. Go to the parent directory of the
repository and run the following command in the terminal:

```shell
# Run this in the folder above the root project folder.
docker build -t project_leo -f project_leo\Dockerfile .
```

This will build a Docker image with all the dependencies and configuration
packed together. It will also expose the port `8080` automatically. From
here, run the following command to run the Docker container, making sure
to replace `<OPENAI_API_KEY>` with a valid key:

```shell
# the -e option is to define the environment variable for the API call
# the -p option to is expose the ports from 8080 to 8080
# the project_leo argument is to use the image built earlier
docker run -e OPENAI_API_KEY=<OPENAI_API_KEY> -p 8080:8080 project_leo
```

Then, open a browser to http://localhost:8080.

[^1]: Oxford English Dictionary
