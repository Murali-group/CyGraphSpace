package org.cytoscape.graphspace.cygraphspace.internal.singletons;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.graphspace.javaclient.Graph;
import org.graphspace.javaclient.GraphSpaceClient;
import org.json.JSONException;
//import org.cytoscape.property.CyProperty;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public enum Server{
	INSTANCE;
	boolean authenticated = false;
	public GraphSpaceClient client;
	private String host = "http://www.graphspace.org";
	private String username;
	private String password;
	
	public boolean authenticate(String host, String username, String password) throws Exception{
		client = new GraphSpaceClient(host, username, password);
		if (client.isAuthenticated()) {
			this.host = host;
			this.username = username;
			this.password = password;
			this.authenticated = true;
			return true;
		}
		return false;
	}
	
	public boolean isAuthenticated(){
		return this.authenticated;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public String getHost(){
		return this.host;
	}
	
	public void logout(){
		this.authenticated = false;
		this.host = "http://www.graphspace.org";
		this.username = null;
		this.password = null;
		this.client = null;
//		this.username = properties.getProperty("cygraphspace.username");;
//		this.password = properties.getProperty("cygraphspace.password");;
//		writeToPropertiesFile(this.host, this.username, this.password);
	}
	
	public ArrayList<Graph> getMyGraphs(int limit, int offset) throws Exception{
		return client.getMyGraphs(limit, offset);
	}
	
	public ArrayList<Graph> getSharedGraphs(int limit, int offset) throws Exception{
		return client.getSharedGraphs(limit, offset);
	}
	
	public ArrayList<Graph> getPublicGraphs(int limit, int offset) throws Exception{
		return client.getPublicGraphs(limit, offset);
	}
	
	public Graph getGraphById(int id) throws Exception{
		return client.getGraph(id);
	}
	
	public Graph getGraphByName(String name) throws Exception{
		return client.getGraph(name, this.username);
	}
	
	public void updateGraph(String name, JSONObject graphJson, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		Graph graph = client.getGraph(name, this.username);
		client.updateGraph(name, graphJson, graph.getStyleJson(), isGraphPublic, tagsList);
	}
	
	public void postGraph(JSONObject graphJson, JSONObject styleJson, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		client.postGraph(graphJson, styleJson, isGraphPublic, tagsList);
	}
	
	public boolean updatePossible(String name) throws Exception{
		Graph graph = client.getGraph(name, this.username);
		if (graph.getGraphJson() != null) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Graph> searchMyGraphs(String searchTerm, int limit, int offset) throws Exception{
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		ArrayList<String> graphNames = new ArrayList<String>();
		if(searchTerm != null) {
			graphNames.add(searchTerm);
		}
		ArrayList<Graph> nameSearchGraphs = client.getMyGraphs(graphNames, null, limit, offset);
		ArrayList<Graph> tagSearchGraphs = client.getMyGraphs(null, graphNames, limit, offset);
		graphs.addAll(nameSearchGraphs);
		graphs.addAll(tagSearchGraphs);
		return graphs;
	}
	
	public ArrayList<Graph> searchSharedGraphs(String searchTerm, int limit, int offset) throws Exception{
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		ArrayList<String> graphNames = new ArrayList<String>();
		if(searchTerm != null) {
			graphNames.add(searchTerm);
		}
		ArrayList<Graph> nameSearchGraphs = client.getSharedGraphs(graphNames, null, limit, offset);
		ArrayList<Graph> tagSearchGraphs = client.getSharedGraphs(null, graphNames, limit, offset);
		graphs.addAll(nameSearchGraphs);
		graphs.addAll(tagSearchGraphs);
		return graphs;
	}
	
	public ArrayList<Graph> searchPublicGraphs(String searchTerm, int limit, int offset) throws Exception{
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		ArrayList<String> graphNames = new ArrayList<String>();
		if(searchTerm != null) {
			graphNames.add(searchTerm);
		}
		ArrayList<Graph> nameSearchGraphs = client.getPublicGraphs(graphNames, null, limit, offset);
		ArrayList<Graph> tagSearchGraphs = client.getPublicGraphs(null, graphNames, limit, offset);
		graphs.addAll(nameSearchGraphs);
		graphs.addAll(tagSearchGraphs);
		return graphs;
	}
	
//	public ArrayList<GSGroupMetaData> getMyGroups(int limit, int offset) throws Exception{
//		return this.client.getMyGroups(limit, offset);
//	}
//	
//	public JSONObject addGroupGraph(String graphId, String name, String groupId) throws Exception{
//		return this.client.addGroupGraph(graphId, name, groupId);
//	}
	
	public void writeToPropertiesFile(String host, String username, String password) {
		List<String> lines = Arrays.asList("cygraphspace.host="+host, "cygraphspace.username="+username, "cygraphspace.password="+password);
		Path file = Paths.get(this.getClass().getClassLoader().getResource("properties").getFile());
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}