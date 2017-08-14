package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.LayoutException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Provides methods for layout related operations such as saving, fetching, updating and deleting layouts on GraphSpace.
 * 
 * @author rishabh
 *
 */
public class Layouts{
	
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
    public static JSONObject getGraphLayout(int graphId, int layoutId, String ownerEmail) throws Exception{
    	if (ownerEmail==null) {
    		ownerEmail = User.username;
    	}
    	
    	String path = String.format(Config.GRAPHS_PATH+"%s/layouts/%s", graphId, layoutId);
    	JSONObject response = Requests.makeRequest("GET", path, null, null);
    	if (response.getInt("status") == 200){
    		return response;
    	}
    	else {
    		throw new LayoutException(ExceptionCode.LAYOUT_NOT_FOUND_EXCEPTION, ExceptionMessage.LAYOUT_NOT_FOUND_EXCEPTION,
    				"Layout with layoutId: " + layoutId + " not found. \nResponse Status: "+response.getInt("status"));
    	}
    	
    	//
//    	if(layoutName!=null) {
//    		Map<String, Object> query = new HashMap<String, Object>();
//        	query.put("owner_email", ownerEmail);
//        	query.put("name", layoutName);
//        	if (ownerEmail != null && ownerEmail != User.username) {
//        		query.put("is_shared", 1);
//        	}
//        	String path = String.format(Config.GRAPHS_PATH+ graphId);
//        	JSONObject response = Requests.makeRequest("GET", path, query, null);
//        	JSONObject body = response.getJSONObject("body");
//    		JSONArray array = body.getJSONArray("array");
//        	int total = ((JSONObject) array.get(0)).getInt("total");
//        	if(total>0) {
//        		return Requests.makeRequest(method, path, urlParams, data)
//        	}
//    	}
//    	throw new LayoutException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION, 
//    			"Both layoutId and layoutName can't be null");
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
    public static JSONObject getMyGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", User.username);
    	String path = String.format(Config.GRAPHS_PATH+"%s/layouts/", graphId);
    	return Requests.makeRequest("GET", path, query, null);
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
    public static JSONObject getSharedGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("is_shared", 1);
    	String path = String.format(Config.GRAPHS_PATH+"%s/layouts/", graphId);
    	return Requests.makeRequest("GET", path, query, null);
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
	public static JSONObject postGraphLayout(int graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{

    	int isShared = (isGraphShared) ? 1 : 0;
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", layoutName);
    	data.put("graph_id", graphId);
    	data.put("is_shared", isShared);
    	data.put("owner_email", User.username);
    	if (positionsJSON != null){
    		data.put("positions_json", positionsJSON);
    	}
    	else{
    		positionsJSON = new JSONObject();
    		data.put("positions_json", positionsJSON);
    	}
    	if (styleJSON != null){
    		data.put("style_json", styleJSON);
    	}
    	else{
    		styleJSON = new JSONObject();
    		styleJSON.append("style", new JSONArray());
    		data.put("style_json", styleJSON);
    	}
    	String path = String.format(Config.GRAPHS_PATH+"%s/layouts/", graphId);
    	return Requests.makeRequest("POST", path, null, data);
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
    public static JSONObject updateGraphLayout(int graphId, int layoutId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{
    	
    	Map<String, Object> data = new HashMap<String, Object>();
		if (layoutName != null){
			data.put("name", layoutName);
		}
		
		data.put("is_shared", isGraphShared);
		
		if (positionsJSON != null){
			data.put("positions_json", positionsJSON);
		}
		if (styleJSON != null){
			data.put("style_json", styleJSON);
		}
		
		String path = String.format(Config.GRAPHS_PATH+"%s/layouts/%s", graphId, layoutId);
		return Requests.makeRequest("PUT", path, null, data);
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
    public static JSONObject deleteGraphLayout(int graphId, int layoutId) throws Exception{
    	String path = String.format(Config.GRAPHS_PATH+"%s/layouts/%s", graphId, layoutId);
    	return Requests.makeRequest("DELETE", path, null, null);
    }
}