package org.graphspace.javaclient;

import org.json.JSONObject;

public class Response {
	private int status;
	private String statusText;
	private JSONObject json;
	
	public Response(JSONObject json) {
		this.json = json;
		this.status = json.getInt("status");
		this.statusText = json.getString("statusText");
	}
	
	public String toString() {
		return this.status + " : " + this.statusText;
	}
	
	public JSONObject getJsonResponse() {
		return this.json;
	}

}
