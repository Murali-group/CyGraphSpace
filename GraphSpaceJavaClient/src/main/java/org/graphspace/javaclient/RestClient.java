package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Implements the get, post, put and delete methods used by all the methods. Uses Unirest to handle requests.
 *  
 * @author rishabh
 *
 */
public class RestClient{
	private String host;
	private String username;
	private String password;
	
	private Map<String, String> headers;
	
	public RestClient(String username, String password, String host) {
		this.host = host;
		this.username = username;
		this.password = password;
		setHeaders();
	}
	
	public RestClient(String username, String password) {
		this.host = "http://www.graphspace.org";
		this.username = username;
		this.password = password;
		setHeaders();
	}
	
	public void setHeaders() {
		headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
    	headers.put("Content-Type", "application/x-www-form-urlencoded");
	}
	
	public void setProxy(String proxyHost, int proxyPort) {
		Unirest.setProxy(new HttpHost(proxyHost, proxyPort));
	}
	
    private JSONObject get(String path, Map<String, Object> urlParams) throws UnirestException{
		String queryPath = this.host+path;
		HttpResponse<JsonNode> getResponse = Unirest.get(queryPath)
				.basicAuth(User.username, User.password)
				.headers(this.headers)
				.queryString(urlParams)
				.asJson();
		JSONObject response = new JSONObject(getResponse);
		return response;
    }
    
    private JSONObject post(String path, Map<String, Object> data) throws UnirestException{
		String queryPath = this.host+path;
		JSONObject dataJson = new JSONObject(data);
			HttpResponse<JsonNode> postResponse = Unirest.post(queryPath)
					.basicAuth(User.username, User.password)
					.headers(this.headers)
					.body(dataJson)
					.asJson();
			JSONObject response = new JSONObject(postResponse);
			return response;
    }
    
    private JSONObject put(String path, Map<String, Object> data) throws UnirestException{
		String queryPath = this.host+path;
		JSONObject dataJson = new JSONObject(data);
		HttpResponse<JsonNode> putResponse = Unirest.put(queryPath)
				.basicAuth(this.username, this.password)
				.headers(this.headers)
				.body(dataJson)
				.asJson();
		JSONObject response = new JSONObject(putResponse);
		return response;
    }
    
    private JSONObject delete(String path, Map<String, Object> urlParams) throws UnirestException{
		String queryPath = this.host+path;
		HttpResponse<JsonNode> deleteResponse = Unirest.delete(queryPath)
				.basicAuth(this.username, this.password)
				.headers(this.headers)
				.queryString(urlParams)
				.asJson();
		JSONObject response = new JSONObject(deleteResponse);
		return response;
    }
    
}