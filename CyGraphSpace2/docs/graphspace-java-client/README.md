# Overview
GraphSpace Java Client is a Java client library for the GraphSpace REST API.

It simplifies the process of authentication, requesting and posting graphs to GraphSpace.

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

# Tutorial

### Import GraphSpace

`import org.graphspace.javaclient.Client`

### Connecting to GraphSpace

You can connect to GraphSpace using your username and password. You can also set a different api host.

`Client client = new Client();`<br/>
`client.authenticate(host, username, password);`

### Fetching a graph from GraphSpace

You can retrieve your saved graph anytime from GraphSpace using the **getGraph(String graphName)** method.

`JSONObject graph = client.getGraph("GraphName");`<br/>

### Saving a graph on GraphSpace

You can save your graph online using the **postGraph(JSONObject graphJSON, JSONObject styleJSON)** method.

`client.postGraph(graphJSON, styleJSON);`

### Updating a graph on GraphSpace

You can also update your graph anytime using the
**updateGraph(String graphName, String ownerEmail, JSONObject graphJSON, boolean isPublic)** method.

`updateGraph(name, ownerEmail, graphJSON, isPublic);`

### Making a graph public on GraphSpace

You can also make a graph public using the makeGraphPublic(String graphName) method.

`makeGraphPublic(graphName);`

### Making a graph private on GraphSpace

You can also make a graph private using the makeGraphPrivate(String graphName) method.

`makeGraphPrivate(graphName);`

### Deleting a graph on GraphSpace

You can also delete your graph anytime using the updateGraph(String graphName) method.

`deleteGraph(graphName);`

<br/>

# API Reference
