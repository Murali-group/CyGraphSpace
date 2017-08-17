package org.graphspace.javaclient;

public class Resource {
	private int id;
	private String name;
	private RestClient restClient;
	
	public Resource(RestClient restClient) {
		this.restClient = restClient;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setRestClient(RestClient restClient) {
		this.restClient = restClient;
	}
	
	public RestClient getRestClient() {
		return this.restClient;
	}
}
