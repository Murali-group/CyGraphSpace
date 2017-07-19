package org.graphspace.javaclient;

import java.util.ArrayList;

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
			return graphJSONListToMetaDataArray(client.getSharedGraphs(null, limit, offset));
		}
		else{
			return graphJSONListToMetaDataArray(client.getPublicGraphs(limit, offset));
		}
	}
	
	public ArrayList<GSGraphMetaData> searchGraphs(String searchText, boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
		ArrayList<String> searchTerms = new ArrayList<String>();
		searchTerms.add("%"+searchText+"%");
		ArrayList<GSGraphMetaData> searchResults;
		ArrayList<GSGraphMetaData> graphsByTags;
		ArrayList<GSGraphMetaData> graphsByNames;
		if (myGraphs){
			searchResults = new ArrayList<GSGraphMetaData>();
			graphsByTags = graphJSONListToMetaDataArray(client.getMyGraphs(searchTerms, limit, offset));
			graphsByNames = graphJSONListToMetaDataArray(client.getMyGraphsByNames(searchTerms, limit, offset));
			searchResults.addAll(graphsByTags);
			searchResults.addAll(graphsByNames);
			return searchResults;
		}
		else if(sharedGraphs){
			searchResults = new ArrayList<GSGraphMetaData>();
			graphsByTags = graphJSONListToMetaDataArray(client.getSharedGraphs(searchTerms, limit, offset));
			graphsByNames = graphJSONListToMetaDataArray(client.getSharedGraphsByNames(searchTerms, limit, offset));
			searchResults.addAll(graphsByTags);
			searchResults.addAll(graphsByNames);
			return searchResults;
		}
		else{
			searchResults = new ArrayList<GSGraphMetaData>();
			graphsByTags = graphJSONListToMetaDataArray(client.getPublicGraphs(searchTerms, limit, offset));
			graphsByNames = graphJSONListToMetaDataArray(client.getPublicGraphsByNames(searchTerms, limit, offset));
			searchResults.addAll(graphsByTags);
			searchResults.addAll(graphsByNames);
			return searchResults;
		}
	}
	
	public JSONObject getGraphById(String id) throws Exception{
		return client.getGraphById(id);
	}
	
	public JSONObject getGraphByName(String name) throws Exception{
		return client.getGraph(name);
	}
	
	public JSONObject postGraph(JSONObject graph, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		return client.postGraph(graph, styleJSON, isGraphPublic, tagsList);
	}
	
	public JSONObject updateGraph(String name, String ownerEmail, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		return client.updateGraph(name, ownerEmail, graphJSON, isGraphPublic, tagsList);
	}
	
	public JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		String ownerEmail = this.username;
		return client.updateGraph(name, ownerEmail, graphJSON, isGraphPublic, tagsList);
	}
	
	public JSONObject postGraphLayout(String graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{
		return client.postGraphLayout(graphId, layoutName, positionsJSON, styleJSON, isGraphShared);
	}
	
	public boolean updatePossible(String name) throws Exception{
		String ownerEmail = this.username;
		JSONObject responseJSON = client.getGraphRequest(name, ownerEmail);
		if (responseJSON==null){
			return false;
		}
		if (responseJSON.getInt("status")==201 || responseJSON.getInt("status")==200){
			return true;
		}
		else{
			return false;
		}
	}
}