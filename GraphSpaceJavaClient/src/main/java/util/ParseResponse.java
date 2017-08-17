package util;

import java.util.ArrayList;

import org.graphspace.javaclient.Graph;
import org.graphspace.javaclient.Layout;
import org.graphspace.javaclient.Response;
import org.graphspace.javaclient.RestClient;
import org.graphspace.javaclient.Group;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParseResponse {
	JSONObject response;
	RestClient restClient;
	
	public ParseResponse(RestClient restClient, JSONObject response) {
		this.response = response;
		this.restClient = restClient;
	}
	
	public ArrayList<Graph> getGraphs() {
		JSONObject responseBody = response.getJSONObject("body").getJSONObject("object");
		JSONArray graphsArr = responseBody.getJSONArray("graphs");
		Graph graph;
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		for (int i = 0; i < graphsArr.length(); i++) {
			JSONObject graphObj = graphsArr.getJSONObject(i);
			graph = new Graph(restClient, graphObj);
			graphs.add(graph);
		}
		return graphs;
	}
	
	public ArrayList<Layout> getLayouts() {
		JSONObject responseBody = response.getJSONObject("body").getJSONObject("object");
		JSONArray layoutsArr = responseBody.getJSONArray("layouts");
		Layout layout;
		ArrayList<Layout> layouts = new ArrayList<Layout>();
		for (int i = 0; i < layoutsArr.length(); i++) {
			JSONObject layoutObj = layoutsArr.getJSONObject(i);
			layout = new Layout(restClient, layoutObj);
			layouts.add(layout);
		}
		return layouts;
	}
	
	public ArrayList<Group> getGroups() {
		JSONObject responseBody = response.getJSONObject("body").getJSONObject("object");
		JSONArray groupsArr = responseBody.getJSONArray("groups");
		Group group;
		ArrayList<Group> groups = new ArrayList<Group>();
		for (int i = 0; i < groupsArr.length(); i++) {
			JSONObject groupObj = groupsArr.getJSONObject(i);
			group = new Group(restClient, groupObj);
			groups.add(group);
		}
		return groups;
	}

//	public ArrayList<Group> getMembers() {
//		JSONObject responseBody = response.getJSONObject("body").getJSONObject("object");
//		JSONArray graphsArr = responseBody.getJSONArray("graphs");
//		Graph graph;
//		ArrayList<Graph> graphs = new ArrayList<Graph>();
//		for (int i = 0; i < graphsArr.length(); i++) {
//			JSONObject graphObj = graphsArr.getJSONObject(i);
//			graph = new Graph(restClient, graphObj);
//			graphs.add(graph);
//		}	
//	}
	
	public Response getResponse() {
		return new Response(response);
	}
}
