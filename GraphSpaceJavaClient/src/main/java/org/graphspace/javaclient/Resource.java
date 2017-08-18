package org.graphspace.javaclient;

import org.json.JSONObject;

public abstract class Resource {
	protected Integer id;
	protected String name;
	protected String ownerEmail;
	protected JSONObject json;
	protected RestClient restClient;
	
	public Resource (RestClient restClient) {
		this.restClient = restClient;
		if (this.json!=null) {
			setAttrs();
		}
	}
	
	public Resource(RestClient restClient, JSONObject json) {
		this.restClient = restClient;
		this.json = json;
		if (this.json!=null) {
			setAttrs();
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOwner(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
	
	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getOwner() {
		return this.ownerEmail;
	}
	
	public JSONObject getJson() {
		return this.json;
	}
	
	public RestClient getRestClient() {
		return this.restClient;
	}
	
	private void setAttrs() {
		if (this.json.has("id")) {
			this.id = json.getInt("id");
		}
		if (this.json.has("name")) {
			this.name = json.getString("name");
		}
		if (this.json.has("data")) {
			this.name = json.getJSONObject("data").getString("name");
		}
		if (this.json.has("owner_email")) {
			this.ownerEmail = json.getString("owner_email");
		}
	}
}
