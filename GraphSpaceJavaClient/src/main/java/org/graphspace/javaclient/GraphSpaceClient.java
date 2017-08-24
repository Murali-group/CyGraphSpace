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
	 * @param host
	 * @param username
	 * @param password
	 */
	public GraphSpaceClient(String host, String username, String password) {
		restClient = new RestClient(host, username, password);
	}

	/**
	 * Create client object with username and password. The user can set the default host in Config.java.
	 * By default, Config.HOST = http://graphspace.org
	 * @param username
	 * @param password
	 */
	public GraphSpaceClient(String username, String password) {
		restClient = new RestClient(Config.HOST, username, password);
	}
	
    public void setProxy(String proxyHost, int proxyPort) {
    	restClient.setProxy(proxyHost, proxyPort);
    }
    
    public RestClient getRestClient() {
    	return this.restClient;
    }
    
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
     * Reference: {@link org.graphspace.javaclient.Graphs#getGraphById(int graphId)}
     */
    public Graph getGraph(int graphId) throws Exception{
    	return Graph.getGraph(restClient, graphId);
    }
    
	/**
	 * Reference: {@link org.graphspace.javaclient.Graphs#getGraphByName(String graphName, String ownerEmail)}
	 */
    public Graph getGraph(String graphName, String ownerEmail) throws Exception{
    	return Graph.getGraph(restClient, graphName, ownerEmail);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getGraphResponse(String graphName, String ownerEmail)}
     */
//    public Graph getGraphResponse(String graphName, String ownerEmail) throws Exception{
//    	return Graphs.getGraphResponse(graphName, ownerEmail);
//    }
    
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getPublicGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getPublicGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	return Graph.getPublicGraphs(restClient, graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getPublicGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getPublicGraphs(int limit, int offset) throws Exception{
    	return Graph.getPublicGraphs(restClient, null, null, limit, offset);
    }
    
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getSharedGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getSharedGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
		return Graph.getSharedGraphs(restClient, graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getSharedGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getSharedGraphs(int limit, int offset) throws Exception{
		return Graph.getSharedGraphs(restClient, null, null, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getMyGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getMyGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	return Graph.getMyGraphs(restClient, graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getMyGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public ArrayList<Graph> getMyGraphs(int limit, int offset) throws Exception{
    	return Graph.getMyGraphs(restClient, null, null, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList tagsList)}
     */
    public String postGraph(JSONObject graphJson, JSONObject styleJson, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
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
     * Reference: {@link org.graphspace.javaclient.Graphs#updateGraph(String graphName, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList tagsList)}
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
     * Reference: {@link org.graphspace.javaclient.Graphs#makeGraphPublic(String graphName)}
     */
    public String makeGraphPublic(String graphName) throws Exception {
    	Graph graph = Graph.getGraph(restClient, graphName, restClient.getUser());
    	return graph.makeGraphPublic();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#makeGraphPrivate(String graphName)}
     */
    public String makeGraphPrivate(String graphName) throws Exception {
    	Graph graph = Graph.getGraph(restClient, graphName, restClient.getUser());
    	return graph.makeGraphPrivate();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#deleteGraph(Integer id, String graphName)}
     */
    public String deleteGraph(int graphId) throws Exception {
    	return Graph.deleteGraph(restClient, graphId);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#deleteGraph(Integer id, String graphName)}
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
	 * Reference: {@link org.graphspace.javaclient.Layouts#getGraphLayout(int graphId, int layoutId, String ownerEmail)}
	 */
    public Layout getGraphLayout(int graphId, int layoutId) throws Exception{
    	return Layout.getGraphLayout(restClient, graphId, layoutId);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#getMyGraphLayouts(int graphId, int limit, int offset)}
     */
    public ArrayList<Layout> getMyGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	return Layout.getMyGraphLayouts(restClient, graphId, limit, offset);
    }

    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#getSharedGraphLayouts(String graphId, int limit, int offset)}
     */
    public ArrayList<Layout> getSharedGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	return Layout.getSharedGraphLayouts(restClient, graphId, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#postGraphLayout(int graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared)}
     */
	public String postGraphLayout(int graphId, String layoutName, JSONObject positionsJson, JSONObject styleJson, boolean isGraphShared) throws Exception{
		Layout layout = new Layout(restClient, layoutName, styleJson, positionsJson);
		return layout.postGraphLayout(graphId, isGraphShared);
    }
	
	/**
	 * Reference: {@link org.graphspace.javaclient.Layouts#updateGraphLayout(int graphId, int layoutId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared)}
	 */
	public String updateGraphLayout(int graphId, String layoutName, JSONObject positionsJson, JSONObject styleJson, boolean isGraphShared) throws Exception{
		Layout layout = new Layout(restClient, layoutName, styleJson, positionsJson);
		return layout.updateGraphLayout(graphId, layout.getId(), isGraphShared);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#deleteGraphLayout(int graphId, String layoutId)}
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
     * Reference: {@link org.graphspace.javaclient.Groups#getGroup(String groupName)}
     */
    public Group getGroup(String groupName) throws Exception {
    	return Group.getGroup(restClient, groupName);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#getMyGroups(int limit, int offset)}
     */
    public ArrayList<Group> getMyGroups(int limit, int offset) throws Exception {
    	return Group.getMyGroups(restClient, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#getAllGroups(int limit, int offset)}
     */
    public ArrayList<Group> getAllGroups(int limit, int offset) throws Exception {
    	return Group.getAllGroups(restClient, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#postGroup(GSGroup group)}
     */
    public String postGroup(Group group) throws Exception {
    	return group.postGroup();
    }
    
    public String updateGroup(Group group) throws Exception {
    	return group.updateGroup();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#deleteGroup(String groupName, Integer groupId)}
     */
    public String deleteGroup(Integer groupId) throws Exception {
    	return Group.deleteGroup(restClient, groupId);
    }
    
    public String deleteGroup(String groupName) throws Exception {
    	return Group.deleteGroup(restClient, groupName);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#getGroupMembers(String groupName, Integer groupId, GSGroup group)}
     */
    public ArrayList<Member> getGroupMembers(Group group) throws Exception {
    	return group.getGroupMembers();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#addGroupMember(String memberEmail, String groupName, Integer groupId, GSGroup group)}
     */    
    public String addGroupMember(Group group, Member member) throws Exception {
    	return group.addGroupMember(member);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#deleteGroupMember(Integer memberId, GSMember member, String groupName, Integer groupId, GSGroup group)}
     */
    public String deleteGroupMember(Group group, Member member) throws Exception {
    	return group.deleteGroupMember(member);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#getGroupGraphs(String groupName, Integer groupId, GSGroup group)}
     */
    public ArrayList<Graph> getGroupGraphs(Group group) throws Exception {
    	return group.getGroupGraphs();
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Groups#shareGraph(String graphName, Integer graphId, GSGraph graph, String groupName, Integer groupId, GSGroup group)}
     */
    public String shareGraph(Group group, Graph graph) throws Exception{
    	return group.shareGraph(graph);
    }

    /**
     * Reference: {@link org.graphspace.javaclient.Groups#unshareGraph(String graphName, Integer graphId, GSGraph graph, String groupName, Integer groupId, GSGroup group)}
     */
    public String unshareGraph(Group group, Graph graph) throws Exception{
    	return group.unshareGraph(graph);
    }
    
}