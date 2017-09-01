package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//internal imports
import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GroupException;
import org.graphspace.javaclient.util.Config;

import org.json.JSONObject;

/**
 * This class defines Group Object and corresponding methods (extends {@link org.graphspace.javaclient.Resource})
 * @author rishabh
 *
 */
public class Group extends Resource {
	
	private String description;
	private int totalGraphs;
	private int totalMembers;
	private String inviteCode;
	private String createdAt;
	private String updatedAt;
	ArrayList<Member> members;
	
	/**
	 * Constructor for Group Object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 */
	public Group(RestClient restClient) {
		super(restClient);
	}
	
	/**
	 * Constructor for Group Object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param groupJson(JSONObject) response body retrieved from GraphSpace
	 */
	public Group(RestClient restClient, JSONObject groupJson) {
		super(restClient, groupJson);
		if (groupJson.getString("description") != null) {
			this.description = groupJson.getString("description");
			this.totalGraphs = groupJson.getInt("total_graphs");
			this.totalMembers = groupJson.getInt("total_members");
			this.updatedAt = groupJson.getString("updated_at");
			this.createdAt = groupJson.getString("updated_at");
			this.inviteCode = groupJson.getString("invite_code");			
		}
	}
	
	/**
	 * Constructor for Group Object
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param name(String) name of the group
	 * @param description(String) description of the group
	 */
	public Group(RestClient restClient, String name, String description) {
		super(restClient);
		this.name = name;
		if (description != null) {
			this.description = description;
		}
	}
	
	/**
	 * @return name of the group
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return description of the group
	 */
	public String getDescription () {
		return this.description;
	}
	
	/**
	 * get json of a group
	 * @return json of a group
	 */
	public JSONObject getGroupJson() {
		return this.json;
	}
	
	/**
	 * @return number of graphs shared by the group
	 */
	public int getTotalGraphs() {
		return this.totalGraphs;
	}
	
	/**
	 * @return number of members in the group
	 */
	public int getTotalMembers() {
		return this.totalMembers;
	}
	
	/**
	 * @return time stamp at which the group was last updated
	 */
	public String getUpdatedAt() {
		return this.updatedAt;
	}
	
	/**
	 * @return time stamp at which the group was created
	 */
	public String getCreatedAt() {
		return this.createdAt;
	}
	
	/**
	 * @return invite code for the group
	 */
	public String getInviteCode() {
		return this.inviteCode;
	}
	
	/**
	 * set name of the group
	 * @param groupName(String) name of the group
	 */
	public void setName(String groupName) {
		this.name = groupName;
	}

	/**
	 * set description of the group
	 * @param description(String) description of the group
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * set json of the group
	 * @param groupJson(JSONObject) json of the group
	 */
	public void setGroupJson(JSONObject groupJson) {
		this.json = groupJson;
	}
	
	//Convert group object to a map
	private Map<String, Object> toMap(){
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
	 * Get group from GraphSpace with groupName
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param name(String) name of the group
	 * @return group object returned from GraphSpace 
	 * @throws Exception
	 */
    public static Group getGroup(RestClient restClient, String name) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("member_email", restClient.getUser());
    	urlParams.put("name", name);
    	String path = Config.GROUPS_PATH;
    	JSONObject jsonResponse = restClient.get(path, urlParams);
    	Response response = new Response(jsonResponse);
    	return response.getGroup();
    }

    /**
     * Get all groups owned by you
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param limit(int) Number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return list of groups owned by you
     * @throws Exception
     */
    public static ArrayList<Group> getMyGroups(RestClient restClient, int limit, int offset) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("limit", limit);
    	urlParams.put("offset", offset);
    	urlParams.put("owner_email", restClient.getUser());
    	String path = Config.GROUPS_PATH;
    	JSONObject jsonResponse = restClient.get(path, urlParams);
    	Response response = new Response(jsonResponse);
    	return response.getGroups();
    }

    /**
     * Get all groups where you are a member
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param limit(int) Number of entities to return
     * @param offset(int) Offset the list of returned entities by this number
     * @return list of groups where you are a member
     * @throws Exception
     */
    public static ArrayList<Group> getAllGroups(RestClient restClient, int limit, int offset) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("limit", limit);
    	urlParams.put("offset", offset);
    	urlParams.put("member_email", restClient.getUser());
    	String path = Config.GROUPS_PATH;
    	JSONObject jsonResponse = restClient.get(path, urlParams);
    	Response response = new Response(jsonResponse);
    	return response.getGroups();
    }

    /**
     * Post a group to GraphSpace
     * @return response status on post request to GraphSpace
     * @throws Exception
     */
    public String postGroup() throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", this.name);
    	data.put("owner_email", restClient.getUser());
    	data.put("description", this.description);
    	String path = Config.GROUPS_PATH;
    	JSONObject jsonResponse = restClient.post(path, data);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }

    /**
     * Update an existing group on GraphSpace
     * @return response status on update request to GraphSpace
     * @throws Exception
     */
    public String updateGroup() throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>(this.toMap());
    	String path = Config.getGroupPath(this.id);
    	JSONObject jsonResponse = restClient.put(path, data);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }

    /**
     * Delete an existing group on GraphSpace with groupName
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param groupName(String) name of the group to be deleted
     * @return response status on delete request to GraphSpace
     * @throws Exception
     */
    public static String deleteGroup(RestClient restClient, String groupName) throws Exception {
		Group group = getGroup(restClient, groupName);
		String path = Config.getGroupPath(group.getId());
		JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }
    
    /**
     * Delete an existing group on GraphSpace with groupId
     * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
     * @param groupName(String) name of the group to be deleted
     * @return response status on delete request to GraphSpace
     * @throws Exception
     */
    public static String deleteGroup(RestClient restClient, Integer groupId) throws Exception {
		String path = Config.getGroupPath(groupId);
		JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }

    /**
     * Get list of members in a group 
     * @return list of members in a group
     * @throws Exception
     */
    public ArrayList<Member> getGroupMembers() throws Exception{
    	String path = Config.getMembersPath(this.id);
		JSONObject jsonResponse = restClient.get(path, null);
    	Response response = new Response(jsonResponse);
    	return response.getMembers();
    }
    
    /**
     * Get a particular member in a group with memberEmail
     * @param memberEmail(String) email of the member to be returned
     * @return member with memberEmail belonging to the group
     * @throws Exception
     */
    public Member getGroupMember(String memberEmail) throws Exception{
    	String path = Config.getMembersPath(this.id);
		JSONObject jsonResponse = restClient.get(path, null);
    	Response response = new Response(jsonResponse);
    	members = response.getMembers();
    	for (Member member: members) {
    		if(member.getEmail().equals(memberEmail)) {
    			return member;
    		}
    	}
    	throw new GroupException(ExceptionCode.MEMBER_NOT_FOUND_EXCEPTION, ExceptionMessage.MEMBER_NOT_FOUND_EXCEPTION,
    			"Member with email ID " + memberEmail + " not found.");
    }

    /**
     * Add member to a group
     * @param member(Member) member to be added to the group
     * @return response status from GraphSpace on adding member to a group
     * @throws Exception
     */
    public String addGroupMember(Member member) throws Exception {
    	if(member.getEmail() == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member email can't be null");
    	}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("member_email", member.getEmail());
		String path = Config.getMembersPath(this.id);
		JSONObject jsonResponse = restClient.post(path, data);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }

    /**
     * Delete member from a group
     * @param member(Member) member to be deleted from the group
     * @return response status from GraphSpace on deleted member from a group
     * @throws Exception
     */    
    public String deleteGroupMember(Member member) throws Exception {
    	if(member.getEmail() == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member email can't be null");
    	}
    	if (member.getId() == null) {
    		throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
    				"Member ID can't be null");
    	}
    	String path = Config.getMemberPath(this.id, member.getId());
    	JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }

    /**
     * Get a list of graphs belonging to group
     * @return list of graphs belonging to the group
     * @throws Exception
     */
    public ArrayList<Graph> getGroupGraphs() throws Exception {
    	String path = Config.getGroupGraphsPath(this.id);
    	JSONObject jsonResponse = restClient.get(path, null);
    	Response response = new Response(jsonResponse);
    	return response.getGraphs();
    }

    /**
     * Share a graph with a group
     * @param graph(Graph) graph to be shared with the group
     * @return response status on post request to GraphSpace
     * @throws Exception
     */
    public String shareGraph(Graph graph) throws Exception {
    	if (graph.getId() == null) {
        	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
        			"Graph id can't all be null.");
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("graph_id", graph.getId());
    	String path = Config.getGroupGraphsPath(this.id);
    	JSONObject jsonResponse = restClient.post(path, data);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }


    /**
     * Un-Share a shared graph with a group
     * @param graph(Graph) shared graph to be un-shared with the group
     * @return response status on delete request to GraphSpace
     * @throws Exception
     */
    public String unshareGraph(Graph graph) throws Exception{
    	if (graph.getId() == null) {
        	throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
        			"Graph id can't all be null.");
    	}
    	String path = Config.getGroupGraphPath(this.id, graph.getId());
    	JSONObject jsonResponse = restClient.delete(path);
    	Response response = new Response(jsonResponse);
    	return response.getResponseStatus();
    }
}
