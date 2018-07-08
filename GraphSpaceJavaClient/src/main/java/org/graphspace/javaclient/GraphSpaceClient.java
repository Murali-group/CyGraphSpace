/**
 * 
 */
package org.graphspace.javaclient;

import java.util.ArrayList;
import org.graphspace.javaclient.util.Config;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * This is the main class that exposes all the graph, layout and group methods to the user.
 * Users can explore each of the methods in the respective classes.
 * 
 * @author rishabh
 *
 */
public class GraphSpaceClient {

	private RestClient restClient;
	
	/**
	 * Create client object with host, username and password
	 * @param host(String) host where graphs are located (default: http://www.graphspace.org)
	 * @param username(String) email of the user to be connected
	 * @param password(String) password of the user to be connected
	 */
	public GraphSpaceClient(String host, String username, String password) {
		restClient = new RestClient(host, username, password);
	}

	/**
	 * Create client object with username and password. The user can set the default host in Config.java.
	 * By default, Config.HOST = http://graphspace.org
	 * @param username(String) email of the user to be connected
	 * @param password(String) password of the user to be connected
	 */
	public GraphSpaceClient(String username, String password) {
		restClient = new RestClient(Config.HOST, username, password);
	}
	
	/**
	 * Connect to GraphSpace behind an HTTP/HTTPS proxy
	 * @param proxyHost(String) host of the http/https proxy
	 * @param proxyPort(int) port of the http/https proxy
	 */
    public void setProxy(String proxyHost, int proxyPort) {
    	restClient.setProxy(proxyHost, proxyPort);
    }
    
    /**
     * Get restClient associated with the connection to GraphSpace
     * @return restClient associated with the connection to GraphSpace
     */
    public RestClient getRestClient() {
    	return this.restClient;
    }
    
    /**
     * Checks if the user is authenticated
     * @return true if authenticated, false otherwise
     * @throws JSONException
     * @throws UnirestException
     */
    public boolean isAuthenticated() throws JSONException, UnirestException {
    	int id = 21752;
    	String path = Config.GRAPHS_PATH+id;
    	if (restClient.get(path, null).getInt("status")!=401) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * ============================================
     * GRAPH METHODS
     * ============================================
     */
    
	/**
     * Reference: {@link org.graphspace.javaclient.Graph#getGraph(RestClient restClient, int graphId)}
     */
    public Graph getGraph(int graphId) throws Exception{
    	return Graph.getGraph(restClient, graphId);
    }
    
    /**
	 * Reference: {@link org.graphspace.javaclient.Graph#getGraph(RestClient restClient, String graphName)}
	 */
    public Graph getGraph(String graphName) throws Exception{
    	return Graph.getGraph(restClient, graphName);
    }
    
	/**
	 * Reference: {@link org.graphspace.javaclient.Graph#getGraph(RestClient restClient, String graphName, String ownerEmail)}
	 */
    public Graph getGraph(String graphName, String ownerEmail) throws Exception{
    	return Graph.getGraph(restClient, graphName, ownerEmail);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#getPublicGraphs(RestClient restClient, ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getPublicGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	return Graph.getPublicGraphs(restClient, graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#getPublicGraphs(RestClient restClient, ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getPublicGraphs(int limit, int offset) throws Exception{
    	return Graph.getPublicGraphs(restClient, null, null, limit, offset);
    }
    
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#getSharedGraphs(RestClient restClient, ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getSharedGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
		return Graph.getSharedGraphs(restClient, graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#getSharedGraphs(RestClient restClient, ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getSharedGraphs(int limit, int offset) throws Exception{
		return Graph.getSharedGraphs(restClient, null, null, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#getMyGraphs(RestClient restClient, ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getMyGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	return Graph.getMyGraphs(restClient, graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#getMyGraphs(RestClient restClient, ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getMyGraphs(int limit, int offset) throws Exception{
    	return Graph.getMyGraphs(restClient, null, null, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#postGraph()}
     */
    public Response postGraph(JSONObject graphJson, JSONObject styleJson, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	Graph graph = new Graph(restClient, isGraphPublic);
    	graph.setGraphJson(graphJson);
    	graph.setStyleJson(styleJson);
    	if(tagsList!=null && !tagsList.isEmpty()) {
    		for(String tag: tagsList) {
        		graph.addTag(tag);
        	}
    	}
    	return graph.postGraph();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#updateGraph()}
     */
    public String updateGraph(String graphName, JSONObject graphJson, JSONObject styleJson, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	Graph graph = new Graph(restClient, isGraphPublic);
    	graph.setGraphJson(graphJson);
    	graph.setStyleJson(styleJson);
    	graph.setName(graphName);
    	if(tagsList!=null && !tagsList.isEmpty()) {
    		for(String tag: tagsList) {
        		graph.addTag(tag);
        	}
    	}
    	return graph.updateGraph();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#makeGraphPublic()}
     */
    public String makeGraphPublic(String graphName) throws Exception {
    	Graph graph = Graph.getGraph(restClient, graphName, restClient.getUser());
    	return graph.makeGraphPublic();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#makeGraphPrivate()}
     */
    public String makeGraphPrivate(String graphName) throws Exception {
    	Graph graph = Graph.getGraph(restClient, graphName, restClient.getUser());
    	return graph.makeGraphPrivate();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#deleteGraph(RestClient restClient, int graphId)}
     */
    public String deleteGraph(int graphId) throws Exception {
    	return Graph.deleteGraph(restClient, graphId);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graph#deleteGraph(RestClient restClient, String graphName)}
     */
    public String deleteGraph(String graphName) throws Exception {
    	return Graph.deleteGraph(restClient, graphName);
    }
    
    /**
     * ============================================
     * LAYOUT METHODS
     * ============================================
     */
	
	/**
	 * Reference: {@link org.graphspace.javaclient.Layout#getGraphLayout(RestClient restClient, int graphId, int layoutId)}
	 */
    public Layout getGraphLayout(int graphId, int layoutId) throws Exception{
    	return Layout.getGraphLayout(restClient, graphId, layoutId);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layout#getMyGraphLayouts(RestClient restClient, int graphId, int limit, int offset)}
     */
    public ArrayList<Layout> getMyGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	return Layout.getMyGraphLayouts(restClient, graphId, limit, offset);
    }

    /**
     * Reference: {@link org.graphspace.javaclient.Layout#getSharedGraphLayouts(RestClient restClient, int graphId, int limit, int offset)}
     */
    public ArrayList<Layout> getSharedGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	return Layout.getSharedGraphLayouts(restClient, graphId, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layout#postGraphLayout(int graphId, boolean isGraphShared)}
     */
	public String postGraphLayout(int graphId, String layoutName, JSONObject positionsJson, JSONObject styleJson, boolean isGraphShared) throws Exception{
		Layout layout = new Layout(restClient, layoutName, styleJson, positionsJson);
		return layout.postGraphLayout(graphId, isGraphShared);
    }
	
	/**
	 * Reference: {@link org.graphspace.javaclient.Layout#updateGraphLayout(int graphId, int layoutId, boolean isGraphShared)}
	 */
	public String updateGraphLayout(int graphId, String layoutName, JSONObject positionsJson, JSONObject styleJson, boolean isGraphShared) throws Exception{
		Layout layout = new Layout(restClient, layoutName, styleJson, positionsJson);
		return layout.updateGraphLayout(graphId, layout.getId(), isGraphShared);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layout#deleteGraphLayout(RestClient restClient, int graphId, int layoutId)}
     */
    public String deleteGraphLayout(int graphId, int layoutId) throws Exception{
    	return Layout.deleteGraphLayout(restClient, graphId, layoutId);
    }
    
    /**
     * ============================================
     * GROUP METHODS
     * ============================================
     */
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#getGroup(RestClient restClient, String groupName)}
     */
    public Group getGroup(String groupName) throws Exception {
    	return Group.getGroup(restClient, groupName);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#getMyGroups(RestClient restClient, int limit, int offset)}
     */
    public ArrayList<Group> getMyGroups(int limit, int offset) throws Exception {
    	return Group.getMyGroups(restClient, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#getAllGroups(RestClient restClient, int limit, int offset)}
     */
    public ArrayList<Group> getAllGroups(int limit, int offset) throws Exception {
    	return Group.getAllGroups(restClient, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#postGroup()}
     */
    public String postGroup(Group group) throws Exception {
    	return group.postGroup();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#updateGroup()}
     */    
    public String updateGroup(Group group) throws Exception {
    	return group.updateGroup();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#deleteGroup(RestClient restClient, Integer groupId)}
     */
    public String deleteGroup(Integer groupId) throws Exception {
    	return Group.deleteGroup(restClient, groupId);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#deleteGroup(RestClient restClient, String groupName)}
     */
    public String deleteGroup(String groupName) throws Exception {
    	return Group.deleteGroup(restClient, groupName);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#getGroupMembers()}
     */
    public ArrayList<Member> getGroupMembers(Group group) throws Exception {
    	return group.getGroupMembers();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#getGroupMember(String memberEmail)}
     */
    public Member getGroupMember(Group group, String memberEmail) throws Exception {
    	return group.getGroupMember(memberEmail);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#addGroupMember(Member member)}
     */    
    public String addGroupMember(Group group, Member member) throws Exception {
    	return group.addGroupMember(member);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#deleteGroupMember(Member member)}
     */
    public String deleteGroupMember(Group group, Member member) throws Exception {
    	return group.deleteGroupMember(member);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#getGroupGraphs()}
     */
    public ArrayList<Graph> getGroupGraphs(Group group) throws Exception {
    	return group.getGroupGraphs();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Group#shareGraph(Graph graph)}
     */
    public String shareGraph(Group group, Graph graph) throws Exception{
    	return group.shareGraph(graph);
    }

    /**
     * Reference: {@link org.graphspace.javaclient.Group#unshareGraph(Graph graph)}
     */
    public String unshareGraph(Group group, Graph graph) throws Exception{
    	return group.unshareGraph(graph);
    }
    
}