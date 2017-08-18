package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.LayoutException;
import org.json.JSONArray;
import org.json.JSONObject;

import util.Config;
import util.ParseResponse;

public class Layout extends Resource {
	
	private JSONObject positionsJson;
	private boolean isGraphShared;
	
	public Layout(RestClient restClient) {
		super(restClient);
	}
	
	public Layout(RestClient restClient, JSONObject layoutJson) {
		super(restClient);
	}
	
	public Layout(RestClient restClient, JSONObject layoutJson, String layoutName, JSONObject positionsJson) {
		super(restClient);
		this.json = layoutJson;
		this.name = layoutName;
		this.positionsJson = positionsJson;
	}
	
	public void setPositionsJson(JSONObject positionsJson) {
		this.positionsJson = positionsJson;
	}
	
	/**
     * ============================================
     * GET LAYOUT METHODS
     * ============================================
     */
	
	/**
	 * Get a layout with given layoutId or name for the graph with given graphId.
	 * 
	 * @param graphId(int) ID of the graph
	 * @param layoutId(int) ID of the layout
	 * @param ownerEmail(String) Email of owner of layout
	 * @return layout JSON object, if layout with the given 'name' or 'layoutId' exists; otherwise null
	 * @throws Exception
	 */
    public static Layout getGraphLayout(RestClient restClient, int graphId, int layoutId) throws Exception{
		String ownerEmail = restClient.getUser();
    	String path = Config.getLayoutPath(graphId, layoutId);
    	JSONObject response = restClient.get(path, null);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getLayouts().get(0);
    }
    
    /**
	 * Get a layout with given layoutId or name for the graph with given graphId.
	 * 
	 * @param graphId(int) ID of the graph
	 * @param layoutId(int) ID of the layout
	 * @param ownerEmail(String) Email of owner of layout
	 * @return layout JSON object, if layout with the given 'name' or 'layoutId' exists; otherwise null
	 * @throws Exception
	 */
    public static Layout getGraphLayout(RestClient restClient, int graphId, int layoutId, String ownerEmail) throws Exception{
    	String path = Config.getLayoutPath(graphId, layoutId);
    	JSONObject response = restClient.get(path, null);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getLayouts().get(0);
    }
    
    /**
     * Get layouts created by the requesting user for the graph with given graphId.
     * 
     * @param graphId(int) ID of the graph
     * @param limit(int) Number of entities to return.
     * @param offset(int) Offset the list of returned entities by this number.
     * @return List of layouts owned by the user for graph with graphId
     * @throws Exception
     */
    public static ArrayList<Layout> getMyGraphLayouts(RestClient restClient, int graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", restClient.getUser());
    	String path = Config.getLayoutsPath(graphId);
    	JSONObject response = restClient.get(path, query);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getLayouts();
    }

    /**
     * Get layouts shared with the requesting user for the graph with given graphId
     * 
     * @param graphId(int) ID of the graph
     * @param limit(int) Number of entities to return.
     * @param offset(int) Offset the list of returned entities by this number.
     * @return List of layouts owned by the user for graph with graphId
     * @throws Exception
     */
    public static ArrayList<Layout> getSharedGraphLayouts(RestClient restClient, int graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("is_shared", 1);
    	query.put("member_email", restClient.getUser());
    	String path = Config.getLayoutsPath(graphId);
    	JSONObject response = restClient.get(path, query);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getLayouts();
    }
    
    /**
     * ============================================
     * POST LAYOUT METHODS
     * ============================================
     */
    
    /**
     * Export a layout for the graph with given graphId
     * 
     * @param graphId(int) id of the graph
     * @param layoutName(String) name of layout
     * @param positionsJSON(JSONObject) positions JSON Object
     * @param styleJSON(JSONObject) JSONObject containing style
     * @param isGraphShared(boolean) true if graph is shared, false if graph is private
     * @return Saved layout on GraphSpace
     * @throws Exception
     */
	public Response postGraphLayout(int graphId, boolean isGraphShared) throws Exception{

    	int isShared = (isGraphShared) ? 1 : 0;
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", this.name);
    	data.put("graph_id", graphId);
    	data.put("is_shared", isShared);
    	data.put("owner_email", this.restClient);
    	if (positionsJson != null){
    		data.put("positions_json", positionsJson);
    	}
    	else{
    		positionsJson = new JSONObject();
    		data.put("positions_json", positionsJson);
    	}
    	if (this.json != null){
    		data.put("style_json", this.json);
    	}
    	else{
    		this.json = new JSONObject();
    		this.json.append("style", new JSONArray());
    		data.put("style_json", this.json);
    	}
    	String path = Config.getLayoutsPath(graphId);
    	JSONObject response = restClient.post(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
	
	/**
	 * Update a layout with given layoutId or name for the graph with given graphId
	 * 
	 * @param graphId(int) id of graph
	 * @param layoutId(int) id of layout
	 * @param layoutName(String) name of layout
	 * @param positionsJSON(JSONObject) positions JSONObject
	 * @param styleJSON(JSONObject)JSONObject containing style
	 * @param isGraphShared(boolean) true if graph is shared, false if graph is private
	 * @return Updated layout on GraphSpace
	 * @throws Exception
	 */
    public Response updateGraphLayout(int graphId, int layoutId, boolean isGraphShared) throws Exception{
    	
    	int isShared = (isGraphShared) ? 1 : 0;
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", this.name);
    	data.put("graph_id", graphId);
    	data.put("is_shared", isShared);
    	data.put("owner_email", this.restClient);
    	if (positionsJson != null){
    		data.put("positions_json", positionsJson);
    	}
    	else{
    		positionsJson = new JSONObject();
    		data.put("positions_json", positionsJson);
    	}
    	if (this.json != null){
    		data.put("style_json", this.json);
    	}
    	else{
    		this.json = new JSONObject();
    		this.json.append("style", new JSONArray());
    		data.put("style_json", this.json);
    	}
		
		String path = Config.getLayoutPath(graphId, layoutId);
		JSONObject response = restClient.put(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }


    /**
     * ============================================
     * DELETE LAYOUT METHODS
     * ============================================
     */
    
    /**
     * Delete a layout with the given layoutId or name for the graph with given graphId
     * 
     * @param graphId(int) id of the graph
     * @param layoutId(int) id of the layout
     * @return Success/Error Message from GraphSpace
     * @throws Exception
     */
    public static Response deleteGraphLayout(RestClient restClient, int graphId, int layoutId) throws Exception{
    	String path = Config.getLayoutPath(graphId, layoutId);
    	JSONObject response = restClient.delete(path);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }

}
