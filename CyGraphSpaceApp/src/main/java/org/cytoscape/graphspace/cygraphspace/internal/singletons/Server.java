package org.cytoscape.graphspace.cygraphspace.internal.singletons;

import java.util.ArrayList;

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
	
	public void authenticate(String host, String username, String password){
		this.host = host;
		this.username = username;
		this.password = password;
		client = new CyGraphSpaceClient(host, username, password);
	}
	
	public boolean isAuthenticated(){
		return !(this.client==null);
	}
	
	public boolean authenticationValid(){
		JSONObject graph;
		try {
			graph = getGraphById("21752");
			System.out.println(graph.toString());
			if (graph.getInt("status")!= 401){
				this.authenticated = true;
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
	
	public ArrayList<GSGraphMetaData> getGraphsMetaData(boolean myGraphs, boolean publicGraphs, boolean sharedGraphs) throws Exception{
		return this.client.getGraphMetaDataList(myGraphs, publicGraphs, sharedGraphs);
	}
	
	public JSONObject getGraphById(String id) throws Exception{
		return this.client.getGraphById(id);
	}
	
	public JSONObject getGraphByName(String name) throws Exception{
		return this.client.getGraphByName(name);
	}
	
	public JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic) throws Exception{
		return this.client.updateGraph(name, graphJSON, isGraphPublic);
	}
	
	public void postGraph(JSONObject graph) throws Exception{
		this.client.postGraph(graph);
	}
}