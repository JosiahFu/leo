# Building Project Leo

Project Leo is built on Linux using [Apache Maven](https://maven.apache.org/). The instructions on
this page are for the [Ubuntu](https://ubuntu.com/) distribution. But, other linux distributions may
be used as well.

This discusses how to build and run Project Leo. See [Contributing to Project Leo](CONTRIBUTING.md)

## Configuring A Build Environment

Project Leo is built in Linux. Below we include instructions for setting up your Linux environment.

### Linux on Windows

If you use a Windows machine, you can install a Linux distribution from the Microsoft Store. We
use [Ubuntu 22.04 LTS on Windows](https://www.microsoft.com/store/productId/9PN20MSR04DW).
Clicking that link will take you to the Microsoft Store to install the Ubuntu distribution.

Once you install Ubuntu on Windows, run the Ubuntu terminal from the Start Menu. You will likely
need to type "Ubuntu" after clicking on the Start Menu to get to the app.

The first time you run it, it will ask for a username and password. They can be anything. But, you
will need to remember the password to install Ubuntu dependencies later. The window that pops up is
referred to as a *Terminal*. Commands that we execute later will be run in it.

### Install Ubuntu Dependencies

Project Leo requires the following Ubuntu dependencies to build and run:

* [Apache Maven](https://maven.apache.org/)
* [Git](https://git-scm.com/)
* [Java 17](https://www.java.com/) or later.

In Ubuntu, these can be installed using the following command:

```shell
# Install Apache Maven, Git, and Java.
sudo apt install maven git openjdk-17-doc openjdk-17-jdk openjdk-17-source
```

## Checking out Project Leo in a Terminal

Project Leo is stored in GitHub and has references to other git repositories (called submodules). To
do a full checkout of Project Leo, you need to checkout its source code *as well as* its submodules.
To do this, run the following commands in a Terminal:

```shell
# Check out the Project Leo source code and submodules.
git clone https://github.com/DaVinciSchools/leo.git project_leo
cd project_leo
git submodule update --init --recursive
```

This will create a subfolder called ```project_leo``` with the source code in it. We will run later
commands in this folder.

## Building Project Leo in a Terminal

To build Project Leo in a Terminal, go to the ```project_leo``` folder and run the following
command:

```shell
# Build Project Leo using the Apache Maven tool.
mvn verify
```

## Run Project Leo in a Terminal

To run Project Leo in a Terminal, go to the ```project_leo``` folder and run the following command:

```shell
# Run Project Leo jar built by the Apache Maven tool.
java -jar server/target/project-leo-server-*.jar
```

Then, open a browser to http://localhost:8080.
