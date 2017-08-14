package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.Requests;
import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.graphspace.javaclient.model.GSGraph;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public class Graphs{
    
    
	/**
     * ============================================
     * GET GRAPH METHODS
     * ============================================
     */
    
	/**
     * Get a graph with the graphId
     * 
     * @param graphId(int) id of the graph
     * @return graph with id <b>graphId</b>
     * @throws Exception
     */
    public static JSONObject getGraphById(int graphId) throws Exception{
    	String path = Config.GRAPHS_PATH+graphId;
    	return Requests.makeRequest("GET", path, null, null);
    }
    
	/**
	 * Get a graph with the graph name
	 * 
	 * @param graphName(String) Name of the graph to be fetched
	 * @param ownerEmail(String) Email of the owner of the graph
	 * @return graph JSONObject, if graph with the given name exists
	 * @throws Exception
	 */
    public static JSONObject getGraphByName(String graphName, String ownerEmail) throws Exception{
    	if(ownerEmail==null) {
    		ownerEmail = User.username;
    	}
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", graphName);
    	JSONObject response = Requests.makeRequest("GET", Config.GRAPHS_PATH, urlParams, null);
		JSONObject body = response.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int total = ((JSONObject) array.get(0)).getInt("total");
    	if (total > 0){
    		return ((JSONArray) ((JSONObject)((JSONArray)((JSONObject)response.getJSONObject("body")).getJSONArray("array")).get(0)).getJSONArray("graphs")).getJSONObject(0);
    	}
    	else{
    		throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION,
    				"Graph with name " + graphName + " not found.");
    	}
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
    public static JSONObject getGraphResponse(String graphName, String ownerEmail) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", graphName);
    	JSONObject response = Requests.makeRequest("GET", Config.GRAPHS_PATH, urlParams, null);
		JSONObject body = response.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int total = ((JSONObject) array.get(0)).getInt("total");
    	if (total > 0){
    		return response;
    	}
    	else{
    		return null;
    	}
    }
    
    
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
    public static JSONObject getPublicGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
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
    	return Requests.makeRequest("GET", Config.GRAPHS_PATH, query, null);
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
    public static JSONObject getSharedGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("member_email", User.username);
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
    	return Requests.makeRequest("GET", Config.GRAPHS_PATH, query, null);
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
    public static JSONObject getMyGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
		query.put("owner_email", User.username);
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
		return Requests.makeRequest("GET", Config.GRAPHS_PATH, query, null);
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
    public static JSONObject postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	int isPublic;
    	if (isGraphPublic){
    		isPublic = 1;
    	}
    	else{
    		isPublic = 0;
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	String graphName = graphJSON.getJSONObject("data").getString("name");
    	data.put("name", graphName);
    	data.put("is_public", isPublic);
        data.put("owner_email", User.username);
        data.put("graph_json", graphJSON);
        data.put("style_json", styleJSON);
        if (tagsList!=null && !tagsList.isEmpty()) {
        	data.put("tags[]", tagsList);
        }
    	return Requests.makeRequest("POST", Config.GRAPHS_PATH, null, data);
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
    public static JSONObject updateGraph(String graphName, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	String ownerEmail = User.username;
    	Map<String, Object> data = new HashMap<String, Object>();
    	int isPublic;
    	if (isGraphPublic){
    		isPublic = 1;
    	}
    	else{
    		isPublic = 0;
    	}
    	JSONObject graphResponse = getGraphResponse(graphName, ownerEmail);
    	int id = graphResponse.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getInt("id");
    	if (styleJSON==null) {
    		styleJSON = graphResponse.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONObject("style_json");
    	}
    	GSGraph graph = new GSGraph(graphJSON);
    	graph.setId(id);
    	graph.setStyleJSON(styleJSON);
    	if (graphJSON!=null){
    		data.put("name", graph.getName());
    		data.put("is_public", isPublic);
    		data.put("owner_email", User.username);
    		data.put("graph_json", graph.computeGraphJSON());
    		data.put("style_json", graph.getStyleJSON());
    		if (tagsList!=null && !tagsList.isEmpty()) {
    			data.put("tags[]", tagsList.get(0));
    		}
    		return Requests.makeRequest("PUT", Config.GRAPHS_PATH + graph.getId(), null, data);
    	}
    	else{
    		return null;
    	}
    }
    
    /**
     * Make an existing graph Public
     * @param graphName(String) graph's name
     * @return the response on updating the graph after making it public
     * @throws Exception
     */
    public static JSONObject makeGraphPublic(String graphName) throws Exception {
    	String ownerEmail = User.username;
    	JSONObject response = getGraphResponse(graphName, ownerEmail);
    	JSONObject graphJSON = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONObject("graph_json");
    	JSONObject styleJSON = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONObject("style_json");
    	JSONArray tagsArray = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONArray("tags[]");
    	ArrayList<String> tagsList = new ArrayList<String>();
    	for (Object tag: tagsArray) {
    		tagsList.add((String)tag);
    	}
    	return updateGraph(graphName, graphJSON, styleJSON, true, tagsList);
    }
    
    /**
     * Make an existing graph Private
     * @param graphName(String) graph's name
     * @return the response on updating the graph after making it private
     * @throws Exception
     */
    public static JSONObject makeGraphPrivate(String graphName) throws Exception {
    	String ownerEmail = User.username;
    	JSONObject response = getGraphResponse(graphName, ownerEmail);
    	JSONObject graphJSON = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONObject("graph_json");
    	JSONObject styleJSON = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONObject("style_json");
    	JSONArray tagsArray = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONArray("tags[]");
    	ArrayList<String> tagsList = new ArrayList<String>();
    	for (Object tag: tagsArray) {
    		tagsList.add((String)tag);
    	}
    	return updateGraph(graphName, graphJSON, styleJSON, false, tagsList);
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
    public static JSONObject deleteGraph(Integer id, String graphName) throws Exception{
    	if (id!=null) {
    		JSONObject graphJSON = getGraphByName(graphName, null);
        	if (graphJSON == null){
        		throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION, 
        				"Could not delete the graph since no graph found with name: "+graphName);
        	}
        	else{
        		GSGraph graph = new GSGraph(graphJSON);
        		return Requests.makeRequest("DELETE", Config.GRAPHS_PATH + graph.getId(), null, null);
        	}
    	}
    	if (graphName != null) {
    		JSONObject graphJSON = getGraphByName(graphName, null);
        	if (graphJSON == null){
        		throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION, 
        				"Could not delete the graph since no graph found with name: "+graphName);
        	}
        	else{
        		GSGraph graph = new GSGraph(graphJSON);
        		return Requests.makeRequest("DELETE", Config.GRAPHS_PATH + graph.getId(), null, null);
        	}
    	}
    	throw new GraphException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"Both id and name can't be null");
    }
    
}