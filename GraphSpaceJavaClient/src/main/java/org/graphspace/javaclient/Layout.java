package org.graphspace.javaclient;

import org.json.JSONObject;

public class Layout extends Resource {

	public Layout(RestClient restClient) {
		super(restClient);
	}
	
	public Layout(RestClient restClient, JSONObject layoutJson) {
		super(restClient);
	}

}
