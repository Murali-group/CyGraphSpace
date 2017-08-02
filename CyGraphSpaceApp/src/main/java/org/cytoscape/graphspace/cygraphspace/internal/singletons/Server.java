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

import org.cytoscape.graphspace.cygraphspace.internal.util.CyGraphSpaceClient;
import org.graphspace.javaclient.User;
//import org.cytoscape.property.CyProperty;
import org.graphspace.javaclient.model.GSGraphMetaData;
import org.graphspace.javaclient.model.GSGroupMetaData;
import org.json.JSONObject;

public enum Server{
	INSTANCE;
//	public CyGraphSpaceClient client;
//	String host = User.host;
//	String username = User.username;
//	String password = User.password;
//	Properties properties= CyObjectManager.INSTANCE.getCyProperties().getProperties();
//	String username = properties.getProperty("cygraphspace.username");
//	String password = properties.getProperty("cygraphspace.password");
//	String host = properties.getProperty("cygraphspace.host");
	boolean authenticated = false;
	
	public boolean authenticate(String host, String username, String password){
//		this.host = host;
//		this.username = username;
//		this.password = password;
		if (authenticationValid(host, username, password)){
//			properties.setProperty("cygraphspace.host", host);
//			properties.setProperty("cygraphspace.username", username);
//			properties.setProperty("cygraphspace.password", password);
//			this.host = properties.getProperty("cygraphspace.host");
//			this.username = properties.getProperty("cygraphspace.username");
//			this.password = properties.getProperty("cygraphspace.password");
//			System.out.println("server: "+this.host+" : "+this.username+" : "+ this.password);
//			writeToPropertiesFile(host, username, password);
			User.host = host;
			User.username = username;
			User.password = password;
//			this.client = new CyGraphSpaceClient(this.host, this.username, this.password);
			this.authenticated = true;
			return true;
		}
		return false;
	}
	
	public boolean isAuthenticated(){
		return this.authenticated;
	}
	
	public boolean authenticationValid(String host, String username, String password){
		JSONObject graph;
		try {
//			CyGraphSpaceClient cl = new CyGraphSpaceClient(host, username, password);
			graph = CyGraphSpaceClient.getGraphById(21752);
			System.out.println(graph.toString());
			if (graph.getInt("status")!= 401){
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String getUsername(){
		return User.username;
	}
	
	public String getPassword(){
		return User.password;
	}
	
	public void setHost(String host){
		User.host = host;
	}
	
	public String getHost(){
		return User.host;
	}
	
	public void logout(){
		this.authenticated = false;
//		properties.setProperty("cygraphspace.host", "www.graphspace.org");
//		properties.setProperty("cygraphspace.username", "");
//		properties.setProperty("cygraphspace.password", "");
//		this.host = properties.getProperty("cygraphspace.host");
//		this.username = properties.getProperty("cygraphspace.username");
//		this.password = properties.getProperty("cygraphspace.password");
		User.host = "www.graphspace.org";
		User.username = null;
		User.password = null;
//		this.username = properties.getProperty("cygraphspace.username");;
//		this.password = properties.getProperty("cygraphspace.password");;
//		writeToPropertiesFile(this.host, this.username, this.password);
//		this.client = null;
	}
	
	public ArrayList<GSGraphMetaData> getGraphsMetaData(boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
		return CyGraphSpaceClient.getGraphMetaDataList(myGraphs, sharedGraphs, publicGraphs, limit, offset);
	}
	
	public JSONObject getGraphById(int id) throws Exception{
		return CyGraphSpaceClient.getGraphById(id);
	}
	
	public JSONObject getGraphByName(String name) throws Exception{
		return CyGraphSpaceClient.getGraphByName(name);
	}
	
	public JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		return CyGraphSpaceClient.updateGraph(name, graphJSON, isGraphPublic, tagsList);
	}
	
	public JSONObject postGraph(JSONObject graph, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		return CyGraphSpaceClient.postGraph(graph, styleJSON, isGraphPublic, tagsList);
	}
	
	public boolean updatePossible(String name) throws Exception{
		return CyGraphSpaceClient.updatePossible(name);
	}
	
	public ArrayList<GSGraphMetaData> searchGraphs(String searchTerm, boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
		return CyGraphSpaceClient.searchGraphs(searchTerm, myGraphs, sharedGraphs, publicGraphs, limit, offset);
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