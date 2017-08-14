package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.graphspace.javaclient.exceptions.GroupException;
import org.graphspace.javaclient.model.GSGraph;
import org.graphspace.javaclient.model.GSGroup;
import org.graphspace.javaclient.model.GSMember;
import org.json.JSONArray;
import org.json.JSONException;
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
	
	/**
	 * Get group by name
	 * @param name(String) name of the group
	 * @return group JSON Object
	 * @throws Exception
	 */
    public static JSONObject getGroup(String name) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("member_email", User.username);
    	urlParams.put("name", name);
    	JSONObject response = Requests.makeRequest("GET", Config.GROUPS_PATH, urlParams, null);
    	int total = response.getJSONObject("body").getJSONObject("object").getInt("total");
    	if(total>0){
    		return response.getJSONObject("body").getJSONObject("object").getJSONArray("groups").getJSONObject(0);
    	}
    	return null;
    }
    
    /**
     * Get all personal groups
     * @param limit(int) limit the number of groups to return
     * @param offset(int) offset the group search
     * @return array of the groups json objects
     * @throws Exception
     */
    public static JSONArray getMyGroups(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", User.username);
    	JSONObject response = Requests.makeRequest("GET", Config.GROUPS_PATH, query, null);
    	return response.getJSONObject("body").getJSONObject("object").getJSONArray("groups");
    }
    
    /**
     * Get all groups
     * @param limit(int) limit the number of groups to return
     * @param offset(int) offset the group search
     * @return array of the groups json objects
     * @throws Exception
     */
    public static JSONArray getAllGroups(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("member_email", User.username);
    	JSONObject response = Requests.makeRequest("GET", Config.GROUPS_PATH, query, null);
    	return response.getJSONObject("body").getJSONObject("object").getJSONArray("groups");
    }
    
    /**
     * Post a group to GraphSpace
     * @param group(GSGroup) group object containing name and description
     * @return response from GraphSpace
     * @throws Exception
     */
    public static JSONObject postGroup(GSGroup group) throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("owner_email", User.username);
    	data.put("name", group.getName());
    	data.put("description", group.getDescription());
    	System.out.println(data.toString());
    	return Requests.makeRequest("POST", Config.GROUPS_PATH, null, data);
    }
    
    /**
     * Update a group. Use either group name or group Id to update group
     * @param group(GSGroup) group object to update
     * @param groupId(Integer) id of the group
     * @param groupName(String) name of the group
     * @return response from GraphSpace
     * @throws Exception
     */
    public static JSONObject updateGroup(GSGroup group, Integer groupId, String groupName) throws Exception {
    	if (groupId != null) {
    		return Requests.makeRequest("PUT", Config.GROUPS_PATH+String.valueOf(groupId), null, group.toMap());
    	}
    	if (groupName != null || group.getName()!=null) {
    		if (groupName==null) {
    			groupName = group.getName();
    		}
    		
    		JSONObject existingGroup = getGroup(groupName);
    		if (existingGroup == null) {
    			throw new GroupException(ExceptionCode.GROUP_NOT_FOUND_EXCEPTION, ExceptionMessage.GROUP_NOT_FOUND_EXCEPTION,
    					"Group with name "+groupName+" not found on GraphSpace.");
    		}
    		else {
    			groupId = existingGroup.getInt("id");
    			return Requests.makeRequest("PUT", Config.GROUPS_PATH+String.valueOf(groupId), null, group.toMap());
    		}
    	}
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"Both groupId and groupName can't be none when group object has no 'name' or 'id' attribute!");   	
    }
    
    /**
     * Delete a group
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @return response on deleting the group
     * @throws Exception
     */
    public static JSONObject deleteGroup(String groupName, Integer groupId) throws Exception {
    	if (groupId != null) {
    		return Requests.makeRequest("DELETE", Config.GROUPS_PATH+groupId, null, null);
    	}
    	if (groupName != null) {
    		JSONObject group = getGroup(groupName);
    		groupId = group.getInt("id");
    		return Requests.makeRequest("DELETE", Config.GROUPS_PATH+groupId, null, null);
    	}
    	else {
    		throw new GroupException(ExceptionCode.GROUP_NOT_FOUND_EXCEPTION, ExceptionMessage.GROUP_NOT_FOUND_EXCEPTION,
    				"Group not found");
    	}
    }
    
    /**
     * Get members of a group
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group object
     * @return members of the group as a JSON Array
     * @throws Exception
     */
    public static JSONArray getGroupMembers(String groupName, Integer groupId, GSGroup group) throws Exception {
    	if (group != null) {
    		groupName = group.getName();
    	}
    	if (groupId != null) {
    		JSONArray members = Requests.makeRequest("GET", Config.GROUPS_PATH+groupId+"/members", null, null).
    				getJSONObject("body").getJSONObject("object").getJSONArray("members"); 
    		return members;
    	}
    	if (groupName != null) {
    		groupId = getGroup(groupName).getInt("id");
    		JSONArray members = Requests.makeRequest("GET", Config.GROUPS_PATH+groupId+"/members", null, null).
    				getJSONObject("body").getJSONObject("object").getJSONArray("members"); 
    		return members;
    	}
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"group name, group id and group can't all be null.");
    	
    }
    
    /**
     * Add member to a group
     * @param memberEmail(String) email id of the member to be added
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group object
     * @return response JSON Object on adding the member to the group
     * @throws Exception
     */
    public static JSONObject addGroupMember(String memberEmail, String groupName, Integer groupId, GSGroup group) throws Exception {
    	if(memberEmail == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member email can't be null");
    	}
    	if (group != null) {
    		groupName = group.getName();
    	}
    	if (groupId != null) {
    		Map<String, Object> data = new HashMap<String, Object>();
    		data.put("member_email", memberEmail);
    		return Requests.makeRequest("POST", Config.GROUPS_PATH+groupId+"/members", null, data);
    	}
    	if (groupName != null) {
    		groupId = getGroup(groupName).getInt("id");
    		Map<String, Object> data = new HashMap<String, Object>();
    		data.put("member_email", memberEmail);
    		return Requests.makeRequest("POST", Config.GROUPS_PATH+groupId+"/members", null, data);
    	}
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"group name, group id and group can't all be null.");
    }
    
    /**
     * Delte a group member
     * @param memberId(Integer) id of the member to be deleted
     * @param member(GSMember) member that has to be deleted
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group GSGroup Object
     * @return response from GraphSpace on deleting the group member
     * @throws Exception
     */
    public static JSONObject deleteGroupMember(Integer memberId, GSMember member, String groupName, Integer groupId, GSGroup group) throws Exception {
    	
    	if (group!=null) {
    		groupName = group.getName();
    	}
    	if (member != null) {
    		memberId = member.getId();
    	}
    	if (memberId == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Both member id and member object can't be null");
    	}
    	if (groupId != null) {
    		return Requests.makeRequest("DELETE", Config.GROUPS_PATH+groupId+"/members/"+memberId, null, null);
    	}
    	if (groupName != null) {
    		groupId = getGroup(groupName).getInt("id");
    		if (groupId != null) {
    			return Requests.makeRequest("DELETE", Config.GROUPS_PATH+groupId+"/members/"+memberId, null, null);
    		}
    		
    	}
    	
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
				"group name, group id and group object can't all be null.");
    }
    
    /**
     * Get Graphs belonging to a group
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group GSGroup object
     * @return graphs JSONArray for the specified group
     * @throws Exception
     */
    public static JSONArray getGroupGraphs(String groupName, Integer groupId, GSGroup group) throws Exception {
    	if (group != null) {
    		groupName = group.getName();
    	}
    	if (groupId != null) {
    		JSONArray graphs = Requests.makeRequest("GET", Config.GROUPS_PATH+groupId+"/graphs", null, null).
    				getJSONObject("body").getJSONObject("object").getJSONArray("graphs"); 
    		return graphs;
    	}
    	if (groupName != null) {
    		groupId = getGroup(groupName).getInt("id");
    		JSONArray graphs = Requests.makeRequest("GET", Config.GROUPS_PATH+groupId+"/graphs", null, null).
    				getJSONObject("body").getJSONObject("object").getJSONArray("graphs"); 
    		return graphs;
    	}
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"group name, group id and group can't all be null.");
    }
    
    /**
     * Share a graph with a particular group
     * @param graphName(String) name of the graph
     * @param graphId(Integer) id of the graph
     * @param graph(GSGraph) graph GSGraph Object
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group GSGroup Object
     * @return response from GraphSpace on sharing the graph
     * @throws Exception
     */
    public static JSONObject shareGraph(String graphName, Integer graphId, GSGraph graph, String groupName, Integer groupId, GSGroup group) throws Exception{
    	if (group != null) {
    		groupName = group.getName();
    	}
    	if (graph != null) {
    		graphName = graph.getName();
    	}
    	if (graphId == null && graphName == null) {
        	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
        			"graph name, graph id and graph can't all be null.");
    	}
    	if(graphId == null) {
    		String ownerEmail = User.username;
    		JSONObject graphResponse = Graphs.getGraphByName(graphName, ownerEmail);
    		if(graphResponse != null) {
    			graphId = graphResponse.getInt("id");
    		}
    		else {
    			throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION,
    					"Graph to be shared was not found on GraphSpace.");
    		}
    	}
    	if(groupId != null) {
    		Map<String, Object> data = new HashMap<String, Object>();
    		data.put("graph_id", graphId);
    		return Requests.makeRequest("POST", Config.GROUPS_PATH+groupId+"/graphs", null, data);
    	}
    	if(groupName != null) {
    		JSONObject groupResponse = getGroup(groupName);
    		if (groupResponse!=null) {
    			groupId = groupResponse.getInt("id");
    			Map<String, Object> data = new HashMap<String, Object>();
    			data.put("graph_id", graphId);
        		return Requests.makeRequest("POST", Config.GROUPS_PATH+groupId+"/graphs", null, data);
    		}
    	}
    	
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"group name, group id and group can't all be null.");
    
    }
    
    
    /**
     * Un-Share a previously shared graph with a particular group
     * @param graphName(String) name of the graph
     * @param graphId(Integer) id of the graph
     * @param graph(GSGraph) graph GSGraph Object
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group GSGroup Object
     * @return response from GraphSpace on un-sharing the graph
     * @throws Exception
     */
    public static JSONObject unshareGraph(String graphName, Integer graphId, GSGraph graph, String groupName, Integer groupId, GSGroup group) throws Exception{
    	if (group != null) {
    		groupName = group.getName();
    	}
    	if (graph != null) {
    		graphName = graph.getName();
    	}
    	if (graphId == null && graphName == null) {
        	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
        			"graph name, graph id and graph can't all be null.");
    	}
    	if(graphId == null) {
    		String ownerEmail = User.username;
    		JSONObject graphResponse = Graphs.getGraphByName(graphName, ownerEmail);
    		if(graphResponse != null) {
    			graphId = graphResponse.getInt("id");
    		}
    		else {
    			throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION,
    					"Graph to be shared was not found on GraphSpace.");
    		}
    	}
    	if(groupId != null) {
    		return Requests.makeRequest("DELETE", Config.GROUPS_PATH+groupId+"/graphs/"+groupId, null, null);
    	}
    	
    	if(groupName != null) {
    		JSONObject groupResponse = getGroup(groupName);
    		if (groupResponse!=null) {
    			groupId = groupResponse.getInt("id");
    			return Requests.makeRequest("DELETE", Config.GROUPS_PATH+groupId+"/graphs/"+groupId, null, null);
    		}
    	}
    	
    	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    			"group name, group id and group can't all be null.");
    
    }
    
}