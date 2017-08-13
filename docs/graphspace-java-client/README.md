# Overview
GraphSpace Java Client is a Java Client library for the GraphSpace REST API. It simplifies the process of authentication, request construction, and response parsing for Java developers using the GraphSpace API.

# Why use GraphSpace Java Client?
GraphSpace Java Client allows a user to import and upload the network from GraphSpace with a few lines of code. Moreover, the user need not know the details of the REST API to use this module. It is very easy to integrate this library into a user’s software pipeline.

# Who uses  GraphSpace Java Client?
The potential audience for graphspace_python includes biologists, computer scientists and data scientists.

# Note
This library currently does not allow you to create graphs from scratch. In case you need more functionlity, it is recommended to use [python client](https://graphspace-python.readthedocs.io) instead.

# Installation
Get the GraphsSpace Java Client jar file from [here](https://github.com/Murali-group/CyGraphSpace/blob/develop/apps/javaclient-0.0.1.jar)

### For Maven Projects

1. Save the jar file in the **{basedir}/lib** directory.

1. Then, in pom.xml file of your maven project, add the following dependency.

    > `<dependency>`<br/>
    > `<groupId>org.graphspace</groupId>`<br/>
    > `<artifactId>javaclient</artifactId>`<br/>
    > `<version>0.0.1</version>`<br/>
    > `</dependency>`

1. Then add **maven-install-plugin** and set the **graphspace-java-client path**.

    >`<plugins>`<br/>
    >`...`<br/>
    >`<plugin>`<br/>
    >`<groupId>org.apache.maven.plugins</groupId>`<br/>
    >`<artifactId>maven-install-plugin</artifactId>`<br/>
    >`<version>2.5.2</version>`<br/>
    >`<executions>`<br/>
    >`<execution>`<br/>
    >`<id>install-external</id>`<br/>
    >`<phase>clean</phase>`<br/>
    >`<configuration>`<br/>
    >`<file>${basedir}/lib/javaclient-0.0.1.jar</file>`<br/>
    >`<repositoryLayout>default</repositoryLayout>`<br/>
    >`<groupId>org.graphspace</groupId>`<br/>
    >`<artifactId>javaclient</artifactId>`<br/>
    >`<version>0.0.1</version>`<br/>
    >`<packaging>jar</packaging>`<br/>
    >`<generatePom>true</generatePom>`<br/>
    >`</configuration>`<br/>
    >`<goals>`<br/>
    >`<goal>install-file</goal>`<br/>
    >`</goals>`<br/>
    >`</execution>`<br/>
    >`</executions>`<br/>
    >`</plugin>`<br/>
    >`...`<br/>
    >`</plugins>`

<br/>

## [Tutorial](/graphspace-java-client/tutorial.md)

## [API Reference](https://rishabhsethi.com/cygraphspacejavadoc)
