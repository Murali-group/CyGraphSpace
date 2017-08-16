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
public class Requests{
	
    private static JSONObject getRequest(String path, Map<String, Object> urlParams, Map<String, String> headers) throws UnirestException{
    	if (UserConfig.PROXY_HOST != null && UserConfig.PROXY_PORT != null) {
    		Unirest.setProxy(new HttpHost(UserConfig.PROXY_HOST, UserConfig.PROXY_PORT));
    	}
    	
		String queryPath = User.host+path;
		HttpResponse<JsonNode> getResponse = Unirest.get(queryPath)
				.basicAuth(User.username, User.password)
				.headers(headers)
				.queryString(urlParams)
				.asJson();
		JSONObject response = new JSONObject(getResponse);
		return response;
    }
    
    private static JSONObject postRequest(String path, Map<String, Object> data, Map<String, String> headers){
    	if (UserConfig.PROXY_HOST != null && UserConfig.PROXY_PORT != null) {
    		Unirest.setProxy(new HttpHost(UserConfig.PROXY_HOST, UserConfig.PROXY_PORT));
    	}
		String queryPath = User.host+path;
		JSONObject dataJson = new JSONObject(data);
		try {
			HttpResponse<JsonNode> postResponse = Unirest.post(queryPath)
					.basicAuth(User.username, User.password)
					.headers(headers)
//					.fields(data)
					.body(dataJson)
					.asJson();
			JSONObject response = new JSONObject(postResponse);
			return response;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new JSONObject();
    }    
    private static JSONObject putRequest(String path, Map<String, Object> data, Map<String, String> headers) throws UnirestException{
    	if (UserConfig.PROXY_HOST != null && UserConfig.PROXY_PORT != null) {
    		Unirest.setProxy(new HttpHost(UserConfig.PROXY_HOST, UserConfig.PROXY_PORT));
    	}
		String queryPath = User.host+path;
		JSONObject dataJson = new JSONObject(data);
		HttpResponse<JsonNode> putResponse = Unirest.put(queryPath)
				.basicAuth(User.username, User.password)
				.headers(headers)
//					.fields(data)
				.body(dataJson)
				.asJson();
		JSONObject response = new JSONObject(putResponse);
		return response;
    }
    
    private static JSONObject deleteRequest(String path, Map<String, Object> urlParams, Map<String, String> headers) throws UnirestException{
    	if (UserConfig.PROXY_HOST != null && UserConfig.PROXY_PORT != null) {
    		Unirest.setProxy(new HttpHost(UserConfig.PROXY_HOST, UserConfig.PROXY_PORT));
    	}
		String queryPath = User.host+path;
		HttpResponse<JsonNode> deleteResponse = Unirest.delete(queryPath)
				.basicAuth(User.username, User.password)
				.headers(headers)
				.queryString(urlParams)
				.asJson();
		JSONObject response = new JSONObject(deleteResponse);
		return response;
		
    }
    
    public static JSONObject makeRequest(String method, String path, Map<String, Object> urlParams, Map<String, Object> data) throws Exception{
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Accept", "application/json");
    	headers.put("Content-Type", "application/x-www-form-urlencoded");
    	return makeRequest(method, path, urlParams, data, headers);
    }
    
    public static JSONObject makeRequest(String method, String path, Map<String, Object> urlParams, Map<String, Object> data, Map<String, String> headers) throws Exception{
    	
    	if (method == "GET"){
    		return getRequest(path, urlParams, headers);
    	}
    	
    	else if (method == "POST"){
    		return postRequest(path, data, headers);
    	}
    	
    	else if (method == "PUT"){
    		return putRequest(path, data, headers);
    	}
    	
    	else if (method == "DELETE"){
    		return deleteRequest(path, urlParams, headers);
    	}
    	else {
    		throw new Exception("Request method should be among GET, POST, PUT and DELETE only.");
    	}
    }
}