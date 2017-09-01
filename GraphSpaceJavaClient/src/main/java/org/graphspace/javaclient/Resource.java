package org.graphspace.javaclient;

import org.json.JSONObject;

/**
 * Base class for all Objects (Graph, Group, Layout) used by GraphSpace
 * @author rishabh
 *
 */
public abstract class Resource {
	
	protected Integer id;
	protected String name;
	protected String ownerEmail;
	protected JSONObject json;
	protected JSONObject responseBody;
	protected RestClient restClient;
	
	/**
	 * Constructor for Resource
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 */
	public Resource (RestClient restClient) {
		this.restClient = restClient;
	}
	
	/**
	 * Constructor for Resource
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 * @param json(JSONObject) response body retrieved from GraphSpace
	 */
	public Resource(RestClient restClient, JSONObject json) {
		this.restClient = restClient;
		this.json = json;
		if (this.json!=null) {
			setAttrs();
		}
	}
	
	/**
	 * set id of the resource
	 * @param id(int) id of the resource
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * set name of the resource
	 * @param name(String) name of the resource
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * set owner of the resource
	 * @param ownerEmail(String) email of the owner of the resource
	 */
	public void setOwner(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	/**
	 * set json of the resource
	 * @param json(JSONObject) json of the resource
	 */
	public void setJson(JSONObject json) {
		this.json = json;
	}
	
	/**
	 * set RestClient for the resource
	 * @param restClient(RestClient) defines the methods and other connection variables used by the Rest API
	 */
	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}
	
	/**
	 * get id of the resource
	 * @return id of the resource
	 */
	public Integer getId() {
		return this.id;
	}
	
	/**
	 * get name of the resource
	 * @return name of the resource
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * get email of the owner of the resource
	 * @return email of the owner of the resource
	 */
	public String getOwner() {
		return this.ownerEmail;
	}
	
	/**
	 * get json of the resource
	 * @return json of the resource
	 */
	public JSONObject getJson() {
		return this.json;
	}
	
	/**
	 * get restClient associated with a resource
	 * @return restClient of the resource
	 */
	public RestClient getRestClient() {
		return this.restClient;
	}
	
	//utility method to set resource properties
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
