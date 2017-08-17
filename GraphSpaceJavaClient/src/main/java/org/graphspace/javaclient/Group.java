package org.graphspace.javaclient;

import org.json.JSONObject;

public class Group extends Resource {
	private String description;
	private int totalGraphs;
	private int totalMembers;
	private String inviteCode;
	private String createdAt;
	private String updatedAt;
	private String url;
	private String inviteLink;
	
	public JSONObject groupJson;
	
	public Group(RestClient restClient) {
		super(restClient);
	}
	
	public Group(RestClient restClient, JSONObject groupJson) {
		super(restClient);
	}
}
