package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author rishabh
 *
 */
public class Groups{
	
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
    
    public JSONObject addGroupGraph(String graphId, String graphName, String groupId) throws Exception{
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