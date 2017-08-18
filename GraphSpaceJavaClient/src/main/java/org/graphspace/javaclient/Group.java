package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.graphspace.javaclient.exceptions.GroupException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import util.Config;
import util.ParseResponse;

public class Group extends Resource {
	private String description;
	private int totalGraphs;
	private int totalMembers;
	private String inviteCode;
	private String createdAt;
	private String updatedAt;
	private String url;
	private String inviteLink;
	
	public JSONObject groupJson;
	
	public Group(RestClient restClient, JSONObject groupJson) {
		super(restClient, groupJson);
		this.description = groupJson.getString("description");
	}
	
	public Group(RestClient restClient, String name, String description) {
		super(restClient);
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String description () {
		return this.description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, Object> toMap(){
		Map<String, Object> group = new HashMap<String, Object>();
		group.put("name", name);
		group.put("description", description);
		return group;
	}
	
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
    public static Group getGroup(RestClient restClient, String name) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("member_email", restClient.getUser());
    	urlParams.put("name", name);
    	String path = Config.GROUPS_PATH;
    	JSONObject response = restClient.get(path, urlParams);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGroups().get(0);
    }
    
    /**
     * Get all personal groups
     * @param limit(int) limit the number of groups to return
     * @param offset(int) offset the group search
     * @return array of the groups json objects
     * @throws Exception
     */
    public static ArrayList<Group> getMyGroups(RestClient restClient, int limit, int offset) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("limit", limit);
    	urlParams.put("offset", offset);
    	urlParams.put("owner_email", restClient.getUser());
    	String path = Config.GROUPS_PATH;
    	JSONObject response = restClient.get(path, urlParams);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGroups();
    }
    
    /**
     * Get all groups
     * @param limit(int) limit the number of groups to return
     * @param offset(int) offset the group search
     * @return array of the groups json objects
     * @throws Exception
     */
    public static ArrayList<Group> getAllGroups(RestClient restClient, int limit, int offset) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("limit", limit);
    	urlParams.put("offset", offset);
    	urlParams.put("member_email", restClient.getUser());
    	String path = Config.GROUPS_PATH;
    	JSONObject response = restClient.get(path, urlParams);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGroups();
    }
    
    /**
     * Post a group to GraphSpace
     * @param group(GSGroup) group object containing name and description
     * @return response from GraphSpace
     * @throws Exception
     */
    public Response postGroup() throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("owner_email", restClient.getUser());
    	data.put("name", this.name);
    	data.put("description", this.description);
    	String path = Config.GROUPS_PATH;
    	JSONObject response = restClient.post(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
    
    /**
     * Update a group. Use either group name or group Id to update group
     * @param group(GSGroup) group object to update
     * @param groupId(Integer) id of the group
     * @param groupName(String) name of the group
     * @return response from GraphSpace
     * @throws Exception
     */
    public Response updateGroup() throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>(this.toMap());
    	String path = Config.getGroupPath(this.id);
		JSONObject response = restClient.post(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
    
    /**
     * Delete a group
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @return response on deleting the group
     * @throws Exception
     */
    public static Response deleteGroup(RestClient restClient, String groupName, Integer groupId) throws Exception {
    	if (groupId != null) {
    		String path = Config.getGroupPath(groupId);
    		JSONObject response = restClient.delete(path);
        	ParseResponse parseResponse = new ParseResponse(restClient, response);
        	return parseResponse.getResponse();
    	}
    	if (groupName != null) {
    		Group group = getGroup(restClient, groupName);
    		String path = Config.getGroupPath(group.getId());
    		JSONObject response = restClient.delete(path);
        	ParseResponse parseResponse = new ParseResponse(restClient, response);
        	return parseResponse.getResponse();
    	}
    	throw new GroupException(ExceptionCode.GROUP_NOT_FOUND_EXCEPTION, ExceptionMessage.GROUP_NOT_FOUND_EXCEPTION,
    				"Group not found");
    }
    
    /**
     * Get members of a group
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group object
     * @return members of the group as a JSON Array
     * @throws UnirestException 
     * @throws Exception
     */
    public ArrayList<Member> getGroupMembers() throws Exception{
    	String path = Config.getMembersPath(this.id);
		JSONObject response = restClient.get(path, null);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getMembers();
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
    public Response addGroupMember(Member member) throws Exception {
    	if(member.getEmail() == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member email can't be null");
    	}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("member_email", member.getEmail());
		String path = Config.getMembersPath(this.id);
		JSONObject response = restClient.post(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
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
    public Response deleteGroupMember(Member member) throws Exception {
    	if(member.getEmail() == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member email can't be null");
    	}
    	if (member.getId() == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member ID can't be null");
    	}
    	String path = Config.getMemberPath(this.id, member.getId());
    	JSONObject response = restClient.delete(path);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
    
    /**
     * Get Graphs belonging to a group
     * @param groupName(String) name of the group
     * @param groupId(Integer) id of the group
     * @param group(GSGroup) group GSGroup object
     * @return graphs JSONArray for the specified group
     * @throws Exception
     */
    public ArrayList<Graph> getGroupGraphs() throws Exception {
    	String path = Config.getGroupGraphsPath(this.id);
    	JSONObject response = restClient.get(path, null);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getGraphs();
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
    public Response shareGraph(Graph graph) throws Exception {
    	if (graph.getId() == null) {
        	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
        			"Graph id can't all be null.");
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("graph_id", graph.getId());
    	String path = Config.getGroupGraphsPath(this.id);
    	JSONObject response = restClient.post(path, data);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
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
    public Response unshareGraph(Graph graph) throws Exception{
    	if (graph.getId() == null) {
        	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
        			"Graph id can't all be null.");
    	}
    	String path = Config.getGroupGraphPath(this.id, graph.getId());
    	JSONObject response = restClient.delete(path);
    	ParseResponse parseResponse = new ParseResponse(restClient, response);
    	return parseResponse.getResponse();
    }
}
