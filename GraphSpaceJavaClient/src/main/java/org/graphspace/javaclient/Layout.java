package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//internal imports
import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.LayoutException;
import org.graphspace.javaclient.util.Config;

import org.json.JSONObject;

/**
 * This class defines Layout Object and corresponding methods (extends {@link org.graphspace.javaclient.Resource})
 * @author rishabh
 *
 */
public class Layout extends Resource {
	
	private JSONObject styleJson;
	private JSONObject positionsJson;
	private boolean isGraphShared;
	
	/**
	 * Constructor for Layout object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 */
	public Layout(RestClient restClient) {
		super(restClient);
		this.isGraphShared = false;
	}
	
	/**
	 * Constructor for Layout object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param json(JSONObject) response body retrieved from GraphSpace
	 */
	public Layout(RestClient restClient, JSONObject json) {
		super(restClient, json);
		this.isGraphShared = false;
		if (this.json.has("style_json")) {
			String styleJsonString = json.getString("style_json");
			this.styleJson = new JSONObject(styleJsonString);
		}
		if (this.json.has("positions_json")) {
			String positionsJsonString = json.getString("positions_json");
			this.positionsJson = new JSONObject(positionsJsonString);
		}
	}
	/**
	 * Constructor for Layout Object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param layoutName(String) name of the layout to be exported to graphspace
	 * @param styleJson(JSONObject) style json to be exported
	 * @param positionsJson(JSONObject) positions json to be exported
	 */
	public Layout(RestClient restClient, String layoutName, JSONObject styleJson, JSONObject positionsJson) {
		super(restClient);
		this.name = layoutName;
		this.positionsJson = positionsJson;
		this.styleJson = styleJson;
	}
	
	/**
	 * set style json for Layout object
	 * @param styleJson(JSONObject) style json defining style for various elements of a graph
	 */
	public void setStyleJson(JSONObject styleJson) {
		this.styleJson = styleJson;
	}
	
	/**
	 * set positions json for Layout object
	 * @param positionsJson(JSONObject) positions json defining positions of nodes
	 */
	public void setPositionsJson(JSONObject positionsJson) {
		this.positionsJson = positionsJson;
	}
	
	/**
	 * get positions json for layout
	 * @return the positions json for the Layout object
	 */
	public JSONObject getPositionsJson() {
		return this.positionsJson;
	}
	
	/**
	 * get style json for layout
	 * @return the style json for the Layout object
	 */
	public JSONObject getStyleJson() {
		return this.styleJson;
	}
	
	/**
     * ============================================
     * GET LAYOUT METHODS
     * ============================================
     */
	
	/**
	 * Retrieve layout with layoutId for a graph with graphId
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param graphId(int) id of the graph on GraphSpace
	 * @param layoutId(int) id of the layouts on GraphSpace
	 * @return Layout object returned from GraphSpace
	 * @throws Exception
	 */
    public static Layout getGraphLayout(RestClient restClient, int graphId, int layoutId) throws Exception{
    	String path = Config.getLayoutPath(graphId, layoutId);
    	JSONObject jsonResponse = restClient.get(path, null);
    	Response response = new Response(jsonResponse);
    	return response.getLayout();
    }
    
    /**
     * Retrieve list of all private layouts for a graph with graphId
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphId(int) id of the graph on GraphSpace
     * @param limit(int) number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return ArrayList of layouts for a graph
     * @throws Exception
     */
    public static ArrayList<Layout> getMyGraphLayouts(RestClient restClient, int graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", restClient.getUser());
    	String path = Config.getLayoutsPath(graphId);
    	JSONObject jsonResponse = restClient.get(path, query);
    	Response response = new Response(jsonResponse);
    	return response.getLayouts();
    }

    /**
     * Retrieve list of all shared layouts for a graph with graphId
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphId(int) id of the graph on GraphSpace
     * @param limit(int) number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return ArrayList of layouts for a graph
     * @throws Exception
     */
    public static ArrayList<Layout> getSharedGraphLayouts(RestClient restClient, int graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("is_shared", 1);
    	query.put("member_email", restClient.getUser());
    	String path = Config.getLayoutsPath(graphId);
    	JSONObject jsonResponse = restClient.get(path, query);
    	Response response = new Response(jsonResponse);
    	return response.getLayouts();
    }
    
    /**
     * ============================================
     * POST LAYOUT METHODS
     * ============================================
     */
    
    /**
     * Export a layout for the graph with given graphId
     * @param graphId(int) id of the graph
     * @param isGraphShared(boolean) true if graph is shared, false if graph is private
     * @return response status on post request to GraphSpace
     * @throws Exception
     */
	public String postGraphLayout(int graphId, boolean isGraphShared) throws Exception{

    	int isShared = (isGraphShared) ? 1 : 0;
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", this.name);
    	data.put("graph_id", graphId);
    	data.put("is_shared", isShared);
    	data.put("owner_email", this.restClient.getUser());
    	if (this.positionsJson != null){
    		data.put("positions_json", positionsJson);
    	}
    	else{
    		positionsJson = new JSONObject();
    		data.put("positions_json", positionsJson);
    	}
    	if (this.styleJson != null){
    		data.put("style_json", styleJson);
    	}
    	else {
    		throw new LayoutException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"style.json can't be null");
    	}
    	String path = Config.getLayoutsPath(graphId);
    	JSONObject jsonResponse = restClient.post(path, data);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }
	
	/**
	 * Update a layout with given layoutId or name for the graph with given graphId
	 * @param graphId(int) id of graph
	 * @param layoutId(int) id of layout
	 * @param isGraphShared(boolean) true if graph is shared, false if graph is private
	 * @return response status on update request to GraphSpace
	 * @throws Exception
	 */
    public String updateGraphLayout(int graphId, int layoutId, boolean isGraphShared) throws Exception{
    	
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
    	if (this.styleJson != null){
    		data.put("style_json", this.json);
    	}
    	else {
    		throw new LayoutException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"style.json can't be null");
    	}
		
    	String path = Config.getLayoutPath(graphId, layoutId);
    	JSONObject jsonResponse = restClient.put(path, data);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }


    /**
     * ============================================
     * DELETE LAYOUT METHODS
     * ============================================
     */
    
    /**
     * Delete a layout with the given layoutId or name for the graph with given graphId
     * 
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param graphId(int) id of the graph
     * @param layoutId(int) id of the layout
     * @return response status on delete request to GraphSpace
     * @throws Exception
     */
    public static String deleteGraphLayout(RestClient restClient, int graphId, int layoutId) throws Exception{
    	String path = Config.getLayoutPath(graphId, layoutId);
    	JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }

}