package org.graphspace.javaclient;

import java.util.ArrayList;

import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.graphspace.javaclient.exceptions.GroupException;
import org.graphspace.javaclient.exceptions.LayoutException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Response {
	private int status;
	private String statusText;
	private JSONObject json;
	private RestClient restClient;
	private Graph graph;
	private Layout layout;
	private Group group;
	private Member member;
	private ArrayList<Graph> graphs;
	private ArrayList<Layout> layouts;
	private ArrayList<Group> groups;
	private ArrayList<Member> members;

	public Response(JSONObject json) {
		this.json = json;
		this.status = json.getInt("status");
		this.statusText = json.getString("statusText");
	}

	public String getResponseStatus() {
		return this.status + " : " + this.statusText;
	}

	public JSONObject getJsonResponse() {
		return this.json;
	}

	public ArrayList<Graph> getGraphs() {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		graphs = new ArrayList<Graph>();
		if (responseBody.has("graph_json")) {
			graph = new Graph(restClient, responseBody);
			graphs.add(graph);
			return graphs;
		}
		JSONArray graphsArr = responseBody.getJSONArray("graphs");
		for (int i = 0; i < graphsArr.length(); i++) {
			JSONObject graphObj = graphsArr.getJSONObject(i);
			graph = new Graph(restClient, graphObj);
			graphs.add(graph);
		}
		return graphs;
	}

	public ArrayList<Layout> getLayouts() {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		layouts = new ArrayList<Layout>();
		if (responseBody.has("style_json")) {
			layout = new Layout(restClient, responseBody);
			layouts.add(layout);
			return layouts;
		}
		JSONArray layoutsArr = responseBody.getJSONArray("layouts");
		for (int i = 0; i < layoutsArr.length(); i++) {
			JSONObject layoutObj = layoutsArr.getJSONObject(i);
			layout = new Layout(restClient, layoutObj);
			layouts.add(layout);
		}
		return layouts;
	}

	public ArrayList<Group> getGroups() {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		groups = new ArrayList<Group>();
		if (responseBody.has("name")) {
			group = new Group(restClient, responseBody);
			groups.add(group);
			return groups;
		}
		JSONArray groupsArr = responseBody.getJSONArray("groups");
		for (int i = 0; i < groupsArr.length(); i++) {
			JSONObject groupObj = groupsArr.getJSONObject(i);
			group = new Group(restClient, groupObj);
			groups.add(group);
		}
		return groups;
	}

	public ArrayList<Member> getMembers() throws GroupException {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		members = new ArrayList<Member>();
		
		if (responseBody.has("email")) {
			member = new Member(responseBody);
			members.add(member);
			return members;
		}
		
		if (responseBody.has("members")) {
			JSONArray membersArr = responseBody.getJSONArray("members");
			for (int i = 0; i < membersArr.length(); i++) {
				JSONObject memberObj = membersArr.getJSONObject(i);
				member = new Member(memberObj);
				members.add(member);
			}
			return members;
		}
		throw new GroupException(ExceptionCode.MEMBER_NOT_FOUND_EXCEPTION, ExceptionMessage.MEMBER_NOT_FOUND_EXCEPTION,
				"Members not found.");
		
	}

	public Graph getGraph() throws GraphException {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		if (responseBody.has("graph_json")) {
			graph = new Graph(restClient, responseBody);
			return graph;
		}
		if (responseBody.has("graphs")) {
			JSONArray graphsArr = responseBody.getJSONArray("graphs");
			if (graphsArr.length()>1) {
				throw new GraphException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
						"More than 1 graph are returned. Please use getGraphs method instead to get an ArrayList of Graphs");
			}
			JSONObject graphObj = graphsArr.getJSONObject(0);
			graph = new Graph(restClient, graphObj);
			return graph;
		}
		throw new GraphException(ExceptionCode.GRAPH_NOT_FOUND_EXCEPTION, ExceptionMessage.GRAPH_NOT_FOUND_EXCEPTION,
				"Could not find graph.");
	}

	public Layout getLayout() throws LayoutException {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		if (responseBody.has("style_json")) {
			layout = new Layout(restClient, responseBody);
			return layout;
		}
		if (responseBody.has("layouts")) {
			JSONArray layoutsArr = responseBody.getJSONArray("layouts");
			if (layoutsArr.length()>1) {
				throw new LayoutException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
						"More than 1 layout are returned. Please use getLayouts method instead to get an ArrayList of Layouts");
			}
			JSONObject layoutObj = layoutsArr.getJSONObject(0);
			layout = new Layout(restClient, layoutObj);
			return layout;
		}

		throw new LayoutException(ExceptionCode.LAYOUT_NOT_FOUND_EXCEPTION, ExceptionMessage.LAYOUT_NOT_FOUND_EXCEPTION,
				"Could not find layout.");
	}

	public Group getGroup() throws GroupException {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		if (responseBody.has("name")) {
			group = new Group(restClient, responseBody);
			return group;
		}
		if (responseBody.has("groups")) {
			JSONArray groupsArr = responseBody.getJSONArray("groups");
			if (groupsArr.length()>1) {
				throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
						"More than 1 group are returned. Please use getGroups method instead to get an ArrayList of Groups");
			}
			JSONObject groupObj = groupsArr.getJSONObject(0);
			group = new Group(restClient, groupObj);
			return group;
		}

		throw new GroupException(ExceptionCode.GROUP_NOT_FOUND_EXCEPTION, ExceptionMessage.GROUP_NOT_FOUND_EXCEPTION,
				"Could not find group.");
	}

	public Member getMember() throws GroupException {
		JSONObject responseBody = this.json.getJSONObject("body").getJSONObject("object");
		if (responseBody.has("email")) {
			member = new Member(responseBody);
			return member;
		}
		if (responseBody.has("members")) {
			JSONArray membersArr = responseBody.getJSONArray("members");
			if (membersArr.length()>1) {
				throw new GroupException(ExceptionCode.BAD_REQUEST_FORMAT, ExceptionMessage.BAD_REQUEST_FORMAT_EXCEPTION,
						"More than 1 member are returned. Please use getMembers method instead to get an ArrayList of Members");
			}
			JSONObject memberObj = membersArr.getJSONObject(0);
			member = new Member(memberObj);
			return member;
		}

		throw new GroupException(ExceptionCode.MEMBER_NOT_FOUND_EXCEPTION, ExceptionMessage.MEMBER_NOT_FOUND_EXCEPTION,
				"Could not find member.");
	}
}
