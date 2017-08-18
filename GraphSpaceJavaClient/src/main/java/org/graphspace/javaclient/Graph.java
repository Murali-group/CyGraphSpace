package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.json.JSONObject;

import util.Config;
import util.ParseResponse;

public class Graph extends Resource {

	private JSONObject styleJson;
	private boolean isGraphPublic;
	private ArrayList<String> tags;
	private JSONObject graphJson;
	
	public Graph(RestClient restClient) {
		super(restClient);
		this.isGraphPublic = false;
	}
	
	public Graph(RestClient restClient, JSONObject json) {
		super(restClient, json);
		this.isGraphPublic = false;
		this.graphJson = json.getJSONObject("graph_json");
	}
	
	public Graph(RestClient restClient, boolean isGraphPublic) {
		super(restClient);
		this.isGraphPublic = isGraphPublic;
	}
	
	public JSONObject getStyleJson() {
		return this.styleJson;
	}
	
	public JSONObject getGraphJson() {
		return this.graphJson;
	}
	
	public void setGraphJson(JSONObject graphJson) {
		this.graphJson = graphJson;
	}
	
	public void setStyleJson(JSONObject graphJson) {
		this.graphJson = graphJson;
	}
	
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	
	public void removeTag(String tag) {
		if (tags.contains(tag)) {
			tags.remove(tag);
		}
		else {
			System.out.println(tag + "not found");
		}
	}
	/**
     * Get a graph with the graphId
     * 
     * @param graphId(int) id of the graph
     * @return graph with id <b>graphId</b>
     * @throws Exception
     */
    public static Graph getGraph(RestClient restClient, int graphId) throws Exception{
    	String path = Config.GRAPHS_PATH+graphId;
    	JSONObject response = restClient.get(path, null);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGraphs().get(0);
    }

	/**
	 * Get a graph with the graph name
	 * 
	 * @param graphName(String) Name of the graph to be fetched
	 * @param ownerEmail(String) Email of the owner of the graph
	 * @return graph JSONObject, if graph with the given name exists
	 * @throws Exception
	 */
    public static Graph getGraph(RestClient restClient, String graphName) throws Exception {
    	String path = Config.GRAPHS_PATH;
    	String ownerEmail = restClient.getUser();
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", graphName);
    	JSONObject response = restClient.get(path, urlParams);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	ArrayList<Graph> graphs = parseResponse.getGraphs();
    	if (graphs.isEmpty()) {
    		return null;
    	}
    	return graphs.get(0);
    }
    
    /**
	 * Get a graph with the graph name
	 * 
	 * @param graphName(String) Name of the graph to be fetched
	 * @param ownerEmail(String) Email of the owner of the graph
	 * @return graph JSONObject, if graph with the given name exists
	 * @throws Exception
	 */
    public static Graph getGraph(RestClient restClient, String graphName, String ownerEmail) throws Exception {
    	String path = Config.GRAPHS_PATH;
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", graphName);
    	JSONObject response = restClient.get(path, urlParams);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	ArrayList<Graph> graphs = parseResponse.getGraphs();
    	if (graphs.isEmpty()) {
    		return null;
    	}
    	return graphs.get(0);
    }
    
    /**
     * This method returns the response object for getGraph request from GraphSpace using the graph name and graph owner's email. 
     * 
     * @param graphName(String) name of the graph requested
     * @param ownerEmail(String) email of the owner of the graph requested
     * @return response JSONObject for getGraph request
     * @throws Exception
     * 
     */
//    public static JSONObject getGraphResponse(String graphName, String ownerEmail) throws Exception{
//    	Map<String, Object> urlParams = new HashMap<String, Object>();
//    	urlParams.put("owner_email", ownerEmail);
//    	urlParams.put("names[]", graphName);
//    	JSONObject response = RestClient.makeRequest("GET", Config.GRAPHS_PATH, urlParams, null);
//		JSONObject body = response.getJSONObject("body");
//		JSONArray array = body.getJSONArray("array");
//		int total = array.getJSONObject(0).getInt("total");
//    	if (total > 0){
//    		return response;
//    	}
//    	else{
//    		return null;
//    	}
//    }
    
    
    /**
     * This method can be used to get public graphs from GraphSpace.
     * REMEMBER: Using both graphNames and tagsList will return the intersection of the the matches. If you need the union
     * of those searches make two individual requests instead.
     * 
     * @param graphNames(ArrayList&lt;String&gt;) 
     * 			Search for graphs with the given given list of possible graph names.
	 *			In order to search for graphs with given names as a substring, wrap the name of the graph with percentage symbol.
	 *			For example, %xyz% will search for all graphs with 'xyz' in their names
     * @param tagsList(ArrayList&lt;String&gt;)
     * 			Search for graphs with the given given list of tag names.
	 *			In order to search for graphs with given tag as a substring, wrap the name of the tag with percentage symbol.
	 *			For example, %xyz% will search for all graphs with 'xyz' in their tag names
     * @param limit(int) Number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return list of public graphs based on graph <b>names</b> and <b>tags</b>
     * @throws Exception
     */
    public static ArrayList<Graph> getPublicGraphs(RestClient restClient, ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	String path = Config.GRAPHS_PATH;
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("is_public", 1);
    	query.put("limit", limit);
    	query.put("offset", offset);
    	if (tagsList != null && !tagsList.isEmpty()){
    		String[] tags = new String[tagsList.size()];
    		tags = tagsList.toArray(tags);
    		query.put("tags[]", tags.toString());
    	}
    	if (graphNames != null && !graphNames.isEmpty()){
    		String[] names = new String[graphNames.size()];
    		names = graphNames.toArray(names);
    		query.put("names[]", names.toString());
    	}
    	JSONObject response = restClient.get(path, query);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGraphs();
    }
    
    
    /**
     * This method can be used to get shared graphs from GraphSpace.
     * REMEMBER: Using both graphNames and tagsList will return the intersection of the the matches. If you need the union
     * of those searches make two individual requests instead.
     * 
     * @param graphNames(ArrayList&lt;String&gt;) 
     * 			Search for graphs with the given given list of possible graph names.
	 *			In order to search for graphs with given names as a substring, wrap the name of the graph with percentage symbol.
	 *			For example, %xyz% will search for all graphs with 'xyz' in their names
     * @param tagsList(ArrayList&lt;String&gt;)
     * 			Search for graphs with the given given list of tag names.
	 *			In order to search for graphs with given tag as a substring, wrap the name of the tag with percentage symbol.
	 *			For example, %xyz% will search for all graphs with 'xyz' in their tag names
     * @param limit(int) Number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return list of shared graphs based on graph <b>names</b> and <b>tags</b>
     * @throws Exception
     */
    public static ArrayList<Graph> getSharedGraphs(RestClient restClient, ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	String path = Config.GRAPHS_PATH;
    	Map<String, Object> query = new HashMap<String, Object>();
		query.put("member_email", restClient.getUser());
		query.put("limit", limit);
    	query.put("offset", offset);
    	if (graphNames != null && !graphNames.isEmpty()){
    		String[] names = new String[graphNames.size()];
    		names = graphNames.toArray(names);
    		query.put("names[]", names.toString());
    	}
    	if (tagsList != null && !tagsList.isEmpty()){
    		String[] tags = new String[tagsList.size()];
    		tags = tagsList.toArray(tags);
    		query.put("tags[]", tags.toString());
    	}
    	JSONObject response = restClient.get(path, query);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGraphs();
    }
    
    /**
     * This method can be used to get private graphs from GraphSpace.
     * REMEMBER: Using both graphNames and tagsList will return the intersection of the the matches. If you need the union
     * of those searches make two individual requests instead.
     * 
     * @param graphNames(ArrayList&lt;String&gt;) 
     * 			Search for graphs with the given given list of possible graph names.
	 *			In order to search for graphs with given names as a substring, wrap the name of the graph with percentage symbol.
	 *			For example, %xyz% will search for all graphs with 'xyz' in their names
     * @param tagsList(ArrayList&lt;String&gt;)
     * 			Search for graphs with the given given list of tag names.
	 *			In order to search for graphs with given tag as a substring, wrap the name of the tag with percentage symbol.
	 *			For example, %xyz% will search for all graphs with 'xyz' in their tag names
     * @param limit(int) Number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return list of private graphs based on graph <b>names</b> and <b>tags</b>
     * @throws Exception
     */
    public static ArrayList<Graph> getMyGraphs(RestClient restClient, ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	String path = Config.GRAPHS_PATH;
    	Map<String, Object> query = new HashMap<String, Object>();
		query.put("owner_email", restClient.getUser());
		query.put("limit", limit);
    	query.put("offset", offset);
    	if (graphNames != null && !graphNames.isEmpty()){
    		String[] names = new String[graphNames.size()];
    		names = graphNames.toArray(names);
    		query.put("names[]", names.toString());
    	}
    	if (tagsList != null && !tagsList.isEmpty()){
    		String[] tags = new String[tagsList.size()];
    		tags = tagsList.toArray(tags);
    		query.put("tags[]", tags.toString());
    	}
    	JSONObject response = restClient.get(path, query);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGraphs();
    }
    
	/**
     * ============================================
     * POST GRAPH METHODS
     * ============================================
     */
    
    /**
     * Post graph to GraphSpace
     * @param graphJSON(JSONObject) graph JSONObject
     * @param styleJSON(JSONObject) style JSONObject
     * @param isGraphPublic(boolean) true if graph is public, false otherwise
     * @param tagsList(ArrayList&lt;String&gt;)
     * @return Saved graph on GraphSpace
     * @throws Exception
     */
    public Response postGraph() throws Exception{
    	if(this.graphJson == null) {
    		throw new GraphException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Graph JSON is not set.");
    	}
    	String path = Config.GRAPHS_PATH;
    	int isPublic = this.isGraphPublic ? 1 : 0;
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", name);
    	data.put("is_public", isPublic);
        data.put("owner_email", restClient.getUser());
        data.put("graph_json", this.graphJson);
        if (styleJson!=null) {
        	data.put("style_json", this.styleJson);
        }
        if (tags!=null && !tags.isEmpty()) {
        	data.put("tags[]", tags);
        }
    	JSONObject response = restClient.post(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
    
    /**
     * Update existing graph on GraphSpace
     * @param graphJSON(JSONObject) graph JSONObject
     * @param styleJSON(JSONObject) style JSONObject
     * @param isGraphPublic(boolean) true if graph is public, false otherwise
     * @param tagsList(ArrayList&lt;String&gt;)
     * @return Saved graph on GraphSpace
     * @throws Exception
     */
    public Response updateGraph() throws Exception{
    	if(this.graphJson == null) {
    		throw new GraphException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Graph JSON is not set.");
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	int isPublic = this.isGraphPublic ? 1 : 0;
    	if (getGraph(restClient, this.name, restClient.getUser()) != null) {
    		data.put("name", this.name);
    		data.put("is_public", isPublic);
    		data.put("owner_email", this.ownerEmail);
    		data.put("graph_json", this.graphJson);
    		data.put("style_json", this.styleJson);
    		if (tags!=null && !tags.isEmpty()) {
    			data.put("tags[]", tags);
    		}
    		String path = Config.GRAPHS_PATH + this.id;
        	JSONObject response = restClient.put(path, data);
        	ParseResponse parseResponse = new ParseResponse(restClient, response);
        	return parseResponse.getResponse();
    	}
    	throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION,
    			"This graph is not currently uploaded on GraphSpace. Please export the graph to GraphSpace first.");
    }
    
    /**
     * Make an existing graph Public
     * @param graphName(String) graph's name
     * @return the response on updating the graph after making it public
     * @throws Exception
     */
    public Response makeGraphPublic() throws Exception {
    	this.isGraphPublic = true;
    	return updateGraph();
    }
    
    /**
     * Make an existing graph Private
     * @param graphName(String) graph's name
     * @return the response on updating the graph after making it private
     * @throws Exception
     */
    public Response makeGraphPrivate() throws Exception {
    	this.isGraphPublic = false;
    	return updateGraph();
    }
    
	/**
     * ============================================
     * DELETE GRAPH METHODS
     * ============================================
     */
    
    /**
     * Delete an existing graph on GraphSpace 
     * @param graphName(String) name of the graph to be deleted
     * @return message received from GraphSpace
     * @throws Exception (Graph Not Found)
     */
    public static Response deleteGraph(RestClient restClient, int graphId) throws Exception{
    	String path = Config.GRAPHS_PATH + graphId;
    	JSONObject response = restClient.delete(path);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
    
    /**
     * Delete an existing graph on GraphSpace 
     * @param graphName(String) name of the graph to be deleted
     * @return message received from GraphSpace
     * @throws Exception (Graph Not Found)
     */
    public static Response deleteGraph(RestClient restClient, String graphName) throws Exception{
    	String ownerEmail = restClient.getUser();
    	Graph graph = Graph.getGraph(restClient, graphName, ownerEmail);
    	String path = Config.GRAPHS_PATH + graph.getId();
    	JSONObject response = restClient.delete(path);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
}
