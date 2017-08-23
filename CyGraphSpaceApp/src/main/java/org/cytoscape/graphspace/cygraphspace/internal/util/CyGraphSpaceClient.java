//package org.cytoscape.graphspace.cygraphspace.internal.util;
//
//import java.util.ArrayList;
//
//import org.graphspace.javaclient.Client;
//import org.graphspace.javaclient.User;
//import org.graphspace.javaclient.model.GSGraphMetaData;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//public class CyGraphSpaceClient{
//	
////	String host;
////	String username;
////	String password;
////	Client client;
////	final String defaultHost = "www.graphspace.org";
//	
////	public CyGraphSpaceClient(String host, String username, String password){
////		this.host = host;
////		this.username = username;
////		this.password = password;
////		client = new Client();
////		client.authenticate(this.host, this.username, this.password);
////	}
////	
////	public CyGraphSpaceClient(String username, String password){
////		this.host = this.defaultHost;
////		this.username = username;
////		this.password = password;
////		client = new Client();
////		client.authenticate(this.host, this.username, this.password);
////	}
//	
//	public static ArrayList<GSGraphMetaData> graphJSONListToMetaDataArray(JSONObject graphsJSONObject){
//		ArrayList<GSGraphMetaData> graphMetaDataList = new ArrayList<GSGraphMetaData>();
//		JSONArray graphsArray = graphsJSONObject.getJSONObject("body").getJSONArray("array").getJSONObject(0).getJSONArray("graphs");
//		for (Object graphObject : graphsArray){
//			JSONObject graph = (JSONObject) graphObject;
//			String name = graph.getString("name");
//			int id = graph.getInt("id");
//			String ownedBy = graph.getString("owner_email");
//			ArrayList<String> tags = new ArrayList<String>();
//			JSONArray tagsJSONArray = graph.getJSONArray("tags");
//			for (int i=0; i<tagsJSONArray.length(); i++){
//				tags.add(tagsJSONArray.getString(i));
//			}
//			GSGraphMetaData graphMetaData = new GSGraphMetaData(name, id, ownedBy, tags);
//			graphMetaDataList.add(graphMetaData);
//		}
//		return graphMetaDataList;
//	}
//	
//	public static ArrayList<GSGraphMetaData> getGraphMetaDataList(boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
//		if (myGraphs){
//			return graphJSONListToMetaDataArray(Client.getMyGraphs(null, null, limit, offset));
//		}
//		else if(sharedGraphs){
//			return graphJSONListToMetaDataArray(Client.getSharedGraphs(null, null, limit, offset));
//		}
//		else{
//			return graphJSONListToMetaDataArray(Client.getPublicGraphs(null, null, limit, offset));
//		}
//	}
//	
//	public static ArrayList<GSGraphMetaData> searchGraphs(String searchText, boolean myGraphs, boolean sharedGraphs, boolean publicGraphs, int limit, int offset) throws Exception{
//		ArrayList<String> searchTerms = new ArrayList<String>();
//		searchTerms.add("%"+searchText+"%");
//		ArrayList<GSGraphMetaData> searchResults;
//		ArrayList<GSGraphMetaData> graphsByTags;
//		ArrayList<GSGraphMetaData> graphsByNames;
//		if (myGraphs){
//			searchResults = new ArrayList<GSGraphMetaData>();
//			graphsByTags = graphJSONListToMetaDataArray(Client.getMyGraphs(null, searchTerms, limit, offset));
//			graphsByNames = graphJSONListToMetaDataArray(Client.getMyGraphs(searchTerms, null, limit, offset));
//			searchResults.addAll(graphsByTags);
//			searchResults.addAll(graphsByNames);
//			return searchResults;
//		}
//		else if(sharedGraphs){
//			searchResults = new ArrayList<GSGraphMetaData>();
//			graphsByTags = graphJSONListToMetaDataArray(Client.getSharedGraphs(null, searchTerms, limit, offset));
//			graphsByNames = graphJSONListToMetaDataArray(Client.getSharedGraphs(searchTerms, null, limit, offset));
//			searchResults.addAll(graphsByTags);
//			searchResults.addAll(graphsByNames);
//			return searchResults;
//		}
//		else{
//			searchResults = new ArrayList<GSGraphMetaData>();
//			graphsByTags = graphJSONListToMetaDataArray(Client.getPublicGraphs(null, searchTerms, limit, offset));
//			graphsByNames = graphJSONListToMetaDataArray(Client.getPublicGraphs(searchTerms, null, limit, offset));
//			searchResults.addAll(graphsByTags);
//			searchResults.addAll(graphsByNames);
//			return searchResults;
//		}
//	}
//	
//	public static JSONObject getGraphById(int id) throws Exception{
//		return Client.getGraphById(id);
//	}
//	
//	public static JSONObject getGraphByName(String name) throws Exception{
//		return Client.getGraphByName(name, null);
//	}
//	
//	public static JSONObject postGraph(JSONObject graph, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
//		return Client.postGraph(graph, styleJSON, isGraphPublic, tagsList);
//	}
//	
//	public static JSONObject updateGraph(String name, String ownerEmail, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
//		return Client.updateGraph(name, ownerEmail, graphJSON, null, isGraphPublic, tagsList);
//	}
//	
//	public static JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
////		String ownerEmail = this.username;
//		String ownerEmail = User.username;
//		return Client.updateGraph(name, ownerEmail, graphJSON, null, isGraphPublic, tagsList);
//	}
//	
//	public static JSONObject postGraphLayout(int graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{
//		return Client.postGraphLayout(graphId, layoutName, positionsJSON, styleJSON, isGraphShared);
//	}
//	
//	public static boolean updatePossible(String name) throws Exception{
////		String ownerEmail = this.username;
//		String ownerEmail = User.username;
//		JSONObject responseJSON = Client.getGraphResponse(name, ownerEmail);
//		if (responseJSON==null){
//			return false;
//		}
//		if (responseJSON.getInt("status")==201 || responseJSON.getInt("status")==200){
//			return true;
//		}
//		else{
//			return false;
//		}
//	}
//	
////	public ArrayList<GSGroupMetaData> getMyGroups(int limit, int offset) throws Exception{
////		ArrayList<GSGroupMetaData> myGroups = new ArrayList<GSGroupMetaData>();
////		JSONArray groups = client.getMyGroups(limit, offset).getJSONArray("groups");
////		for (int i=0; i<groups.length(); i++){
////			JSONObject groupJSON = groups.getJSONObject(i);
////			String name = groupJSON.getString("name");
////			String ownerEmail = groupJSON.getString("owner_email");
////			int id = groupJSON.getInt("id");
////			GSGroupMetaData groupMetaData = new GSGroupMetaData(name, id, ownerEmail);
////			myGroups.add(groupMetaData);
////		}
////		return myGroups;
////	}
//	
////	public JSONObject addGroupGraph(String graphId, String name, String groupId) throws Exception{
////		return addGroupGraph(graphId, name, groupId);
////	}
//}