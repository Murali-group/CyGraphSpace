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
	String host;
	
	public void authenticate(String host, String username, String password){
		this.host = host;
		this.username = username;
		this.password = password;
		client = new CyGraphSpaceClient(host, username, password);
	}
	
	public boolean isAuthenticated(){
		return !(this.client==null);
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
	
	public JSONObject getGraph(String id) throws Exception{
		return this.client.getGraph(id);
	}
	
	public void postGraph(JSONObject graph) throws Exception{
		this.client.postGraph(graph);
	}
}