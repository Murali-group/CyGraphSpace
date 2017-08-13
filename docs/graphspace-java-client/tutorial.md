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
