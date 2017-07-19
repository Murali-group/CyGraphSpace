package org.cytoscape.graphspace.cygraphspace.internal.singletons;

import java.util.ArrayList;
import java.util.List;

import org.graphspace.javaclient.CyGraphSpaceClient;
import org.graphspace.javaclient.model.GSGraphMetaData;
import org.json.JSONObject;

public enum Server{
	INSTANCE;
	public CyGraphSpaceClient client;
	String username;
	String password;
	String host = "www.graphspace.org";
	boolean authenticated = false;
	
	public boolean authenticate(String host, String username, String password){
//		this.host = host;
//		this.username = username;
//		this.password = password;
		if (authenticationValid(host, username, password)){
			this.host = host;
			this.username = username;
			this.password = password;
			this.client = new CyGraphSpaceClient(this.host, this.username, this.password);
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
			CyGraphSpaceClient cl = new CyGraphSpaceClient(host, username, password);
			graph = cl.getGraphById("21752");
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
		this.host = "www.graphspace.org";
		this.username = null;
		this.password = null;
		this.client = null;
		this.authenticated = false;
	}
	
	public ArrayList<GSGraphMetaData> getGraphsMetaData(boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
		return this.client.getGraphMetaDataList(myGraphs, sharedGraphs, publicGraphs, limit, offset);
	}
	
	public JSONObject getGraphById(String id) throws Exception{
		return this.client.getGraphById(id);
	}
	
	public JSONObject getGraphByName(String name) throws Exception{
		return this.client.getGraphByName(name);
	}
	
	public JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		return this.client.updateGraph(name, graphJSON, isGraphPublic, tagsList);
	}
	
	public JSONObject postGraph(JSONObject graph, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		return this.client.postGraph(graph, styleJSON, isGraphPublic, tagsList);
	}
	
	public boolean updatePossible(String name) throws Exception{
		return this.client.updatePossible(name);
	}
	
	public ArrayList<GSGraphMetaData> searchGraphs(String searchTerm, boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
		return this.client.searchGraphs(searchTerm, myGraphs, sharedGraphs, publicGraphs, limit, offset);
	}
}