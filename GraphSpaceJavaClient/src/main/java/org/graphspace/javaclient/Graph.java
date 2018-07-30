package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//internal imports
import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.graphspace.javaclient.util.Config;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class defines Graph Object and corresponding methods (extends {@link org.graphspace.javaclient.Resource})
 * @author rishabh
 *
 */
public class Graph extends Resource {

	private JSONObject styleJson;
	private boolean isGraphPublic;
	private ArrayList<String> tags;
	private JSONObject graphJson;
	
	/**
	 * Constructor for Graph Object.
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 */
	public Graph(RestClient restClient) {
		super(restClient);
		tags = new ArrayList<String>();
		this.isGraphPublic = false;
	}
	
	/**
	 * Constructor for Graph Object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param json(JSONObject) response body retrieved from GraphSpace
	 */
	public Graph(RestClient restClient, JSONObject json) {
		super(restClient, json);
		tags = new ArrayList<String>();
		if(this.json.has("is_public")) {
			if(this.json.getInt("is_public")==0) {
				this.isGraphPublic = false;
			}
			else {
				this.isGraphPublic = true;
			}
		}
		else {
			isGraphPublic = false;
		}
		if (this.json.has("graph_json")) {
			this.graphJson = json.getJSONObject("graph_json");
		}
		if (this.json.has("style_json")) {
			this.styleJson = json.getJSONObject("style_json");
		}
		if(this.json.has("tags")) {
			JSONArray tagsArr = json.getJSONArray("tags");
			for(int i=0; i<tagsArr.length(); i++) {
				String tag = tagsArr.getString(i);
				this.addTag(tag);
			}
		}
		
	}
	
	/**
	 * Constructor for Graph Object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param isGraphPublic(boolean) true if graph is public, false otherwise
	 */
	public Graph(RestClient restClient, boolean isGraphPublic) {
		super(restClient);
		tags = new ArrayList<String>();
		this.isGraphPublic = isGraphPublic;
	}
	
	/**
	 * get style json from graph
	 * @return the style json corresponding to a graph object
	 */
	public JSONObject getStyleJson() {
		return this.styleJson;
	}
	
	/**
	 * get graph json from graph
	 * @return the graph json corresponding to a graph object
	 */
	public JSONObject getGraphJson() {
		return this.graphJson;
	}
	
	/**
	 * set graph json
	 * @param graphJson(JSONObject) graph json defining nodes and edges of a graph in cyjs format
	 */
	public void setGraphJson(JSONObject graphJson) {
		this.graphJson = graphJson;
		if (graphJson.has("data")) {
			this.name = graphJson.getJSONObject("data").getString("name");
		}
	}
	
	/**
	 * set style json
	 * @param styleJson(JSONObject) style json defining style for various elements of a graph
	 */
	public void setStyleJson(JSONObject styleJson) {
		this.styleJson = styleJson;
	}
	
	/**
	 * Add tags to a graph object
	 * @param tag(String) Tags are used to attach additional metadata to the graph to make them identifiable
	 */
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	
	/**
	 * Remove a tag from a graph object
	 * @param tag(String) Tags are used to attach additional metadata to the graph to make them identifiable
	 */
	public void removeTag(String tag) {
		if (tags.contains(tag)) {
			tags.remove(tag);
		}
		else {
			System.out.println(tag + "not found");
		}
	}
	
	/**
	 * get tags associated with a graph object
	 * @return(ArrayList) arraylist of tags
	 */
	public ArrayList<String> getTags(){
		return this.tags;
	}
	
	/**
	 * Tells if graph is public or private
	 * @return true if graph is public, false otherwise
	 */
	public boolean isPublic() {
		return this.isGraphPublic;
	}
	
	/**
	 * Get a graph with the graphId
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param graphId(int) id of the graph on GraphSpace
	 * @return graph object returned from GraphSpace
	 * @throws Exception
	 */
    public static Graph getGraph(RestClient restClient, int graphId) throws Exception{
    	String path = Config.GRAPHS_PATH+graphId;
    	JSONObject jsonResponse = restClient.get(path, null);
    	Response response = new Response(jsonResponse);
    	return response.getGraph();
    }

    /**
     * Get a graph with graphName
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphName(String) name of the graph on graphspace
     * @return graph object returned from graphspace
     * @throws Exception
     */
    public static Graph getGraph(RestClient restClient, String graphName) throws Exception {
    	String path = Config.GRAPHS_PATH;
    	String ownerEmail = restClient.getUser();
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", graphName);
    	JSONObject jsonResponse = restClient.get(path, urlParams);
    	Response response = new Response(jsonResponse);
    	return response.getGraph();
    }
    
    /**
     * Get a graph with graphName published by a user with id ownerEmail
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphName(String) name of the graph
     * @param ownerEmail(String) email of the owner of the graph
     * @return graph object returned from graphspace
     * @throws Exception
     */
    public static Graph getGraph(RestClient restClient, String graphName, String ownerEmail) throws Exception {
    	String path = Config.GRAPHS_PATH;
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", graphName);
    	JSONObject jsonResponse = restClient.get(path, urlParams);
    	Response response = new Response(jsonResponse);
    	return response.getGraph();
    }
    
    /**
     * This method can be used to get public graphs from GraphSpace.
     * REMEMBER: Using both graphNames and tagsList will return the intersection of the the matches. If you need the union
     * of those searches make two individual requests instead.
     * 
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
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
    	if (graphNames != null && !graphNames.isEmpty()){
    		for (String graphName: graphNames) {
    			query.put("names[]", graphName);
    		}
    	}
    	if (tagsList != null && !tagsList.isEmpty()){
    		for (String tagName: tagsList) {
    			query.put("tags[]", tagName);
    		}
    	}
    	JSONObject jsonResponse = restClient.get(path, query);
    	Response response = new Response(jsonResponse);
    	return response.getGraphs();
    }
    
    /**
     * This method can be used to get shared graphs from GraphSpace.
     * REMEMBER: Using both graphNames and tagsList will return the intersection of the the matches. If you need the union
     * of those searches make two individual requests instead.
     * 
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
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
//    		String[] names = new String[graphNames.size()];
//    		names = graphNames.toArray(names);
//    		query.put("names[]", names.toString());
    		for (String graphName: graphNames) {
    			query.put("names[]", graphName);
    		}
    	}
    	if (tagsList != null && !tagsList.isEmpty()){
    		for (String tagName: tagsList) {
    			query.put("tags[]", tagName);
    		}
    	}
    	JSONObject jsonResponse = restClient.get(path, query);
    	Response response = new Response(jsonResponse);
    	return response.getGraphs();
    }
    
    /**
     * This method can be used to get private graphs from GraphSpace.
     * REMEMBER: Using both graphNames and tagsList will return the intersection of the the matches. If you need the union
     * of those searches make two individual requests instead.
     * 
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
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
//    		String[] names = new String[graphNames.size()];
//    		names = graphNames.toArray(names);
//    		query.put("names[]", names.toString());
    		for (String graphName: graphNames) {
    			query.put("names[]", graphName);
    		}
    	}
    	if (tagsList != null && !tagsList.isEmpty()){
    		for (String tagName: tagsList) {
    			query.put("tags[]", tagName);
    		}
    	}
    	JSONObject jsonResponse = restClient.get(path, query);
    	Response response = new Response(jsonResponse);
    	return response.getGraphs();
    }
    
	/**
     * ============================================
     * POST GRAPH METHODS
     * ============================================
     */
    
    /**
     * Export a local graph to GraphSpace
     * @return response status on post request to GraphSpace
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
    	JSONObject jsonResponse = restClient.post(path, data);
    	return new Response(jsonResponse);
    }
    
    /**
     * Upadate an existing graph on GraphSpace
     * @return response status on update request to GraphSpace
     * @throws Exception
     */
    public Response updateGraph() throws Exception{
    	if(this.graphJson == null) {
    		throw new GraphException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Graph JSON is not set.");
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	Graph graphToBeUpdated = getGraph(restClient, this.name, restClient.getUser());
    	int isPublic = graphToBeUpdated.isPublic() ? 1 : 0;
    	JSONObject existingStyleJson = graphToBeUpdated.getStyleJson();
    	if (getGraph(restClient, this.name, restClient.getUser()) != null) {
    		data.put("name", this.name);
    		data.put("is_public", isPublic);
    		data.put("owner_email", restClient.getUser());
    		data.put("graph_json", this.graphJson);
    		if (this.styleJson == null) {
    			if (existingStyleJson != null) {
    				data.put("style_json", existingStyleJson);
    			}
    		}
    		else {
    			data.put("style_json", this.styleJson);
    		}
    		if (tags!=null && !tags.isEmpty()) {
    			data.put("tags[]", tags);
    		}
    		int graphId = graphToBeUpdated.getId();
    		String path = Config.GRAPHS_PATH + graphId;
        	JSONObject jsonResponse = restClient.put(path, data);
        	return new Response(jsonResponse);
    	}
    	throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION,
    			"This graph is not currently uploaded on GraphSpace. Please export the graph to GraphSpace first.");
    }
    
    /**
     * make a graph public
     * @return response status on update request to GraphSpace
     * @throws Exception
     */
    public String makeGraphPublic() throws Exception {
    	this.isGraphPublic = true;
    	return updateGraph().getResponseStatus();
    }
    
    /**
     * make a graph private
     * @return response status on update request to GraphSpace
     * @throws Exception
     */
    public String makeGraphPrivate() throws Exception {
    	this.isGraphPublic = false;
    	return updateGraph().getResponseStatus();
    }
    
	/**
     * ============================================
     * DELETE GRAPH METHODS
     * ============================================
     */
    
    /**
     * delete an existing graph on GraphSpace
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphId(int) id of the graph to be deleted
     * @return response status on delete request to GraphSpace
     * @throws Exception
     */
    public static String deleteGraph(RestClient restClient, int graphId) throws Exception{
    	String path = Config.GRAPHS_PATH + graphId;
    	JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }
    
    /**
     * delete an existing graph on GraphSpace
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphName(String) name of the graph to be deleted
     * @return response status on delete request to GraphSpace
     * @throws Exception
     */
    public static String deleteGraph(RestClient restClient, String graphName) throws Exception{
    	String ownerEmail = restClient.getUser();
    	Graph graph = Graph.getGraph(restClient, graphName, ownerEmail);
    	String path = Config.GRAPHS_PATH + graph.getId();
    	JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }
}
