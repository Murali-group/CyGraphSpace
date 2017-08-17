package org.graphspace.javaclient;

import org.json.JSONObject;

public class Response {
	private int status;
	private String message;
	private JSONObject json;
	
	public Response(JSONObject json) {
		this.json = json;
		this.status = json.getInt("status");
		this.message = json.getString("message");
	}
	
	public String toString() {
		return this.status + " : " + this.message;
	}
	
	public JSONObject getJsonResponse() {
		return this.json;
	}

}
