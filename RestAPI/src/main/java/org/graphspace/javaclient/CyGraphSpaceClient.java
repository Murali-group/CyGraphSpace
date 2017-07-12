package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.model.GSGraphMetaData;
import org.json.JSONArray;
import org.json.JSONObject;

public class CyGraphSpaceClient{
	
	String host;
	String username;
	String password;
	Client client;
	final String defaultHost = "www.graphspace.org";
	
	public CyGraphSpaceClient(String host, String username, String password){
		this.host = host;
		this.username = username;
		this.password = password;
		client = new Client();
		client.authenticate(this.host, this.username, this.password);
	}
	
	public CyGraphSpaceClient(String username, String password){
		this.host = this.defaultHost;
		this.username = username;
		this.password = password;
		client = new Client();
		client.authenticate(this.host, this.username, this.password);
	}
	
	public ArrayList<GSGraphMetaData> graphJSONListToMetaDataArray(JSONObject graphsJSONObject){
		ArrayList<GSGraphMetaData> graphMetaDataList = new ArrayList<GSGraphMetaData>();
		JSONArray graphsArray = graphsJSONObject.getJSONObject("body").getJSONArray("array").getJSONObject(0).getJSONArray("graphs");
		for (Object graphObject : graphsArray){
			JSONObject graph = (JSONObject) graphObject;
			String name = graph.getString("name");
			int id = graph.getInt("id");
			String ownedBy = graph.getString("owner_email");
			int access = graph.getInt("is_public");
			GSGraphMetaData graphMetaData = new GSGraphMetaData(name, id, ownedBy, access);
			graphMetaDataList.add(graphMetaData);
		}
		return graphMetaDataList;
	}
	
	public ArrayList<GSGraphMetaData> getGraphMetaDataList(boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
		if (myGraphs){
			return graphJSONListToMetaDataArray(client.getMyGraphs(limit, offset));
		}
		else if(sharedGraphs){
			System.out.println(graphJSONListToMetaDataArray(client.getSharedGraphs(null, limit, offset)).toString());
			return graphJSONListToMetaDataArray(client.getSharedGraphs(null, limit, offset));
//			graphList.addAll(graphJSONListToMetaDataArray(client.getSharedGraphs(limit, offset)));
		}
		else{
			return graphJSONListToMetaDataArray(client.getPublicGraphs(limit, offset));
		}
	}
	
	//TODO: handle shared graphs
//	public ArrayList<GSGraphMetaData> getGraphMetaDataList(boolean myGraphs, boolean sharedGraphs, boolean publicGraphs) throws Exception{
//		
//		ArrayList<GSGraphMetaData> graphList = new ArrayList<GSGraphMetaData>();
//		
//		if (myGraphs){
//			int limit = 20;
//			int offset = 0;
//			graphList.addAll(graphJSONListToMetaDataArray(client.getMyGraphs(limit, offset)));
//		}
//		
//		if (publicGraphs){
//			int limit = 20;
//			int offset = 0;
//			graphList.addAll(graphJSONListToMetaDataArray(client.getPublicGraphs(limit, offset)));
//		}	
//		return graphList;
//	}
	
	public JSONObject getGraphById(String id) throws Exception{
		return client.getGraphById(id);
	}
	
	public JSONObject getGraphByName(String name) throws Exception{
		return client.getGraph(name);
	}
	
	public void postGraph(JSONObject graph) throws Exception{
		client.postGraph(graph).toString();
	}
	
	public JSONObject updateGraph(String name, String ownerEmail, JSONObject graphJSON, boolean isGraphPublic) throws Exception{
		return client.updateGraph(name, ownerEmail, graphJSON, isGraphPublic);
	}
	
	public JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic) throws Exception{
		String ownerEmail = this.username;
		return client.updateGraph(name, ownerEmail, graphJSON, isGraphPublic);
	}
	
	public boolean updatePossible(String name) throws Exception{
		String ownerEmail = this.username;
		JSONObject responseJSON = client.getGraphRequest(name, ownerEmail);
//		System.out.println("status: " + responseJSON.getInt("status"));
		if (responseJSON.getInt("status")==201 || responseJSON.getInt("status")==200){
			return true;
		}
		else{
			return false;
		}
	}
}