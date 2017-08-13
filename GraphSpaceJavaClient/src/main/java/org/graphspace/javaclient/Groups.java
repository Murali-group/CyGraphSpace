package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GroupException;
import org.graphspace.javaclient.model.GSGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Provides methods for group related operations such as saving, fetching, updating, deleting groups on GraphSpace.
 *	
 * Also provides methods for group member and group graph related operations such as fetching
 * all members or graphs of the group, adding or deleting new member or graph to the group.
 * @author rishabh
 *
 */
public class Groups{
	
	/**
     * ============================================
     * GET GROUP METHODS
     * ============================================
     */
	
    public JSONObject getGroup(String name) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("member_email", User.username);
    	urlParams.put("name", name);
    	JSONObject response = Requests.makeRequest("GET", Config.GROUPS_PATH, urlParams, null);
    	JSONObject body = response.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
    	int total = ((JSONObject) array.get(0)).getInt("total");
    	if(total>0){
    		return response.getJSONArray("groups").getJSONObject(0);
    	}
    	return null;
    }
    
    public JSONObject getMyGroups(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", User.username);
    	return Requests.makeRequest("GET", Config.GROUPS_PATH, query, null);
    }
    
    public JSONObject getAllGroups(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("member_email", User.username);
    	return Requests.makeRequest("GET", Config.GROUPS_PATH, query, null);
    }
    
    public JSONObject postGroup(GSGroup group) throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>(group.toMap());
    	data.put("owner_email", User.username);
    	return Requests.makeRequest("POST", Config.GROUPS_PATH, null, data);
    }
    
    public JSONObject updateGroup(GSGroup group, Integer groupId, String groupName) throws Exception {
    	if (groupId != null) {
    		return Requests.makeRequest("PUT", Config.GROUPS_PATH+String.valueOf(groupId), null, group.toMap());
    	}
    	if (groupName != null || group.getName()!=null) {
    		if (groupName==null) {
    			groupName = group.getName();
    		}
//    		GSGroup existingGroup = getGroup(groupName);
    		JSONObject existingGroup = getGroup(groupName);
    		if (existingGroup == null) {
    			throw new GroupException(ExceptionCode.GROUP_NOT_FOUND_EXCEPTION, ExceptionMessage.GROUP_NOT_FOUND_EXCEPTION,
    					"Group with name "+groupName+" not found on GraphSpace.");
    		}
    		else {
    			return Requests.makeRequest("PUT", Config.GROUPS_PATH+String.valueOf(groupId), null, group.toMap()); //*************
    		}
    	}
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"Both groupId and groupName can't be none when group object has no 'name' or 'id' attribute!");   	
    }
    
    public JSONObject addGroupGraph(String graphId, String graphName, Integer groupId) throws Exception{
    	if (groupId != null){
    		Map<String, Object> data = new HashMap<String, Object>();
    		data.put("graph_id", graphId);
    		return Requests.makeRequest("POST", Config.GROUPS_PATH+groupId+"/graphs", null, data);
    	}
    	if (graphName != null){
    		JSONObject response = Graphs.getGraphByName(graphName, null);
    		if (response!=null){
    			int id = response.getInt("id");
    			Map<String, Object> data = new HashMap<String, Object>();
    			data.put("graph_id", graphId);
    			return Requests.makeRequest("POST", Config.GROUPS_PATH+String.valueOf(groupId)+"/graphs", null, data);
    		}
    		else{
    			return null;
    		}
    	}
    	else{
    		return null;
    	}
    }
    
}