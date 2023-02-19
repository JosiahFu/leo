Building Project Leo
====================

Project Leo is built on Linux using [Apache Maven](https://maven.apache.org/). The instructions on
this page are for the [Ubuntu](https://ubuntu.com/) distribution. But, other distributions may work
as well.

If you use Windows, you can install an instance of
[Ubuntu 22.04 LTS on Windows](https://www.microsoft.com/store/productId/9PN20MSR04DW)
from the Microsoft Store to work in.

## Build Dependencies

Building and running Project Leo requires the following dependencies:

* [Apache Maven 3](https://maven.apache.org/)
* [Git](https://git-scm.com/)
* [Java 17](https://www.java.com/)

These can be installed in Ubuntu using the following command:

```shell
# Install Apache Maven, Git, and Java.
sudo apt install maven git openjdk-17-doc openjdk-17-jdk openjdk-17-source
```

## Checking out Source Code

Project Leo has references to other git repositories (submodules). To do a full checkout, you need
to check out its submodules as well. Run the following commands in a Terminal to do this:

```shell
# Check out the Project Leo source code and submodules.
git clone https://github.com/DaVinciSchools/leo.git project_leo
cd project_leo
git submodule update --init --recursive
```

This will create a subfolder called ```project_leo``` with the source code in it.

## Building Project Leo

To build Project Leo go to the ```project_leo``` folder and run the following
command:

```shell
# Build Project Leo using the Apache Maven tool.
mvn verify
```

This will do a number of things:

* Build the Java Spring server.
* Install Node.js and NPM for the React web client.
* Build the React web client.
* Embed the React web client in the Spring server.
* Format code for a code review (see [CONTRIBUTING](CONTRIBUTING.md)).

## Running Project Leo

There are two ways to run Project Leo:

### As a Standalone Spring Server

The standalone Spring server serves the compiled React website from an internal copy. This is
best when working on the Spring server itself. Or for production.

The following command runs the Spring server in this way:

```shell
# Run the Spring server in standalone mode from the root project folder.
java -jar server/target/project-leo-server-*.jar
```

Then, open a browser to http://localhost:8080.

### As Separate Spring and React Servers

Running separate Spring and React servers in parallel allows you to modify the React content and
serve it through the Spring server live. The Spring server will proxy requests to the React
server for http content. It's necessary to run both because the React website makes callss to the
Spring server to retrieve and set data. This is useful when working on the website.

The following commands run the servers in parallel (Note the ```--react_port``` flag):

```shell
# Run the Spring server from the root project folder.
java -jar server/target/project-leo-server-*.jar --react_port 3000
```

``` shell
# Run the React server from the root project folder.
# Note: You will need to close http://localhost:3000, which the React server automatically opens.
cd server/src/main/clients/web-browser
./npm start
```

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://github.com/Mqxx/GitHub-Markdown/blob/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://github.com/Mqxx/GitHub-Markdown/blob/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
> Starting the React server will open a browser to http://localhost:3000. This does NOT go through
> the Spring server and will NOT work. You need to go to port 8080 on localhost.

Then, open a browser to http://localhost:8080.

[^1]: Oxford English Dictionary