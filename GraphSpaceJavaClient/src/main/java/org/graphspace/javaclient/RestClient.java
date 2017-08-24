package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.graphspace.javaclient.util.Config;
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
	
	//variables used for connection to GraphSpace
	private String host;
	private String username;
	private String password;
	private Map<String, String> headers;

	/**
	 * get Email Id of the user connected to GraphSpace in the current session
	 * @return Email ID of the user connected to GraphSpace
	 */
	public String getUser() {
		return this.username;
	}
	
	/**
	 * get password of the user connected to GraphSpace in the current session
	 * @return password of the user connected to GraphSpace
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * get host of the graphs (default: http://www.graphspace.org)
	 * @return host of the graphs
	 */
	public String getHost() {
		return this.host;
	}
	
	/**
	 * Constructor for the RestClient
	 * @param host(String) host where graphs are located (default: http://www.graphspace.org)
	 * @param username(String) email Id of the user to be connected
	 * @param password(String) password to the user to be connected
	 */
	public RestClient(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		setHeaders();
	}
	
	/**
	 * Constructor for the RestClient with default host (http://www.graphspace.org)
	 * @param username(String) email Id of the user to be connected
	 * @param password(String) password to the user to be connected
	 */
	public RestClient(String username, String password) {
		this.host = Config.HOST;
		this.username = username;
		this.password = password;
		setHeaders();
	}
	
	//set headers for connections to GraphSpace
	private void setHeaders() {
		headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
    	headers.put("Content-Type", "application/x-www-form-urlencoded");
	}
	
	/**
	 * Connect to GraphSpace behind an HTTP/HTTPS proxy
	 * @param proxyHost(String) host of the http/https proxy
	 * @param proxyPort(int) port of the http/https proxy
	 */
	public void setProxy(String proxyHost, int proxyPort) {
		Unirest.setProxy(new HttpHost(proxyHost, proxyPort));
	}
	
	/**
	 * get request to GraphSpace
	 * @param path(String) path where request needs to be made
	 * @param urlParams(Map&lt;String,Object&gt;) extra parameters to send with the request
	 * @return json of the response received from GraphSpace
	 * @throws UnirestException
	 */
    public JSONObject get(String path, Map<String, Object> urlParams) throws UnirestException{
		String queryPath = this.host+path;
		HttpResponse<JsonNode> getResponse;
		if(urlParams == null) {
			getResponse = Unirest.get(queryPath)
					.basicAuth(this.username, this.password)
					.headers(this.headers)
					.asJson();
		}
		else {
			getResponse = Unirest.get(queryPath)
					.basicAuth(this.username, this.password)
					.headers(this.headers)
					.queryString(urlParams)
					.asJson();
		}
		JSONObject response = new JSONObject(getResponse);
		return response;
    }
    
	/**
	 * post request to GraphSpace
	 * @param path(String) path where request needs to be made
	 * @param data(Map&lt;String,Object&gt;) payload to send with the request
	 * @return json of the response received from GraphSpace
	 * @throws UnirestException
	 */
    public JSONObject post(String path, Map<String, Object> data) throws UnirestException{
    	String queryPath = this.host+path;
		JSONObject dataJson = new JSONObject(data);
		HttpResponse<JsonNode> postResponse = Unirest.post(queryPath)
				.basicAuth(this.username, this.password)
				.headers(this.headers)
				.body(dataJson)
				.asJson();
		JSONObject response = new JSONObject(postResponse);
		return response;
    }

	/**
	 * put request to GraphSpace
	 * @param path(String) path where request needs to be made
	 * @param data(Map&lt;String,Object&gt;) payload to send with the request
	 * @return json of the response received from GraphSpace
	 * @throws UnirestException
	 */
    public JSONObject put(String path, Map<String, Object> data) throws UnirestException{
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
    
	/**
	 * delete request to GraphSpace
	 * @param path(String) path where request needs to be made
	 * @return json of the response received from GraphSpace
	 * @throws UnirestException
	 */
    public JSONObject delete(String path) throws UnirestException{
		String queryPath = this.host+path;
		HttpResponse<JsonNode> deleteResponse = Unirest.delete(queryPath)
				.basicAuth(this.username, this.password)
				.headers(this.headers)
				.asJson();
		JSONObject response = new JSONObject(deleteResponse);
		return response;
    }
}
