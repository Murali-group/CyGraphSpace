/**
 * 
 */
package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.exceptions.GraphNotFoundException;
import org.graphspace.javaclient.exceptions.RequestTypeNotDefinedException;
import org.graphspace.javaclient.model.GSGraph;
import org.apache.http.HttpHost;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * 
 * @author rishabh
 *
 */
public class Client {

	String host;
	String username;
	String password;
	final String defaultHost = "www.graphspace.org";
    public void authenticate(String host, String username, String password){
    	this.host = host;
    	this.username = username;
    	this.password = password;
    }
    
    public void authenticate(String username, String password){
    	this.host = this.defaultHost;
    	this.username = username;
    	this.password = password;
    }
    
//    private String getFormattedString(String path, JSONObject urlParams){
//    	byte[] pathB = path.getBytes(ISO_8859_1); 
//		path = new String(pathB, UTF_8); 
//		for (String key: urlParams.keySet()){
//			String value = urlParams.getString(key);
//			
//		}
//		Object[] params = new Object[]{this.host, path, };
//		String msg = MessageFormat.format("http://{0}{1}", params);
//    }
    
    /**
     * 
     * @param method
     * @param path
     * @param urlParams
     * @param data
     * @return
     * @throws Exception
     */
    private JSONObject makeRequest(String method, String path, Map<String, Object> urlParams, Map<String, Object> data) throws Exception{
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Accept", "application/json");
    	headers.put("Content-Type", "application/json");
    	return makeRequest(method, path, urlParams, data, headers);
    }
    
    /**
     * 
     * @param path
     * @param urlParams
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject getRequest(String path, Map<String, Object> urlParams, Map<String, String> headers){
    	
		String queryPath = "http://"+this.host+path;
		try{
//			Unirest.setProxy(new HttpHost("proxy61.iitd.ernet.in", 3128));
			HttpResponse<JsonNode> getResponse = Unirest.get(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
					.queryString(urlParams)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @param path
     * @param data
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject postRequest(String path, Map<String, Object> data, Map<String, String> headers){
    	
		String queryPath = "http://"+this.host+path+"/";
		JSONObject headersJson = new JSONObject(headers);
		JSONObject dataJson = new JSONObject(data);
		try{
			HttpResponse<JsonNode> getResponse = Unirest.post(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
//					.fields(data)
					.body(dataJson)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @param path
     * @param data
     * @param headers
     * @return
     * @throws GraphNotFoundException 
     * @throws Exception
     */
    private JSONObject putRequest(String path, Map<String, Object> data, Map<String, String> headers){
		String queryPath = "http://"+this.host+path;
		JSONObject dataJson = new JSONObject(data);
		try{
			HttpResponse<JsonNode> getResponse = Unirest.put(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
//					.fields(data)
					.body(dataJson)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @param path
     * @param urlParams
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject deleteRequest(String path, Map<String, Object> urlParams, Map<String, String> headers){
    	
		String queryPath = "http://"+this.host+path;
		try{
			HttpResponse<JsonNode> getResponse = Unirest.delete(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
					.queryString(urlParams)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @param method
     * @param path
     * @param urlParams
     * @param data
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject makeRequest(String method, String path, Map<String, Object> urlParams, Map<String, Object> data, Map<String, String> headers) throws Exception{
    	
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
    		throw new RequestTypeNotDefinedException();
    	}
    }
    
    /**
     * Posts NetworkX graph to the requesting users account on GraphSpace.
     * @param graph
     * @return
     * @throws Exception 
     */
//    public JSONObject postGraph(JSONObject graph, ArrayList<String> tagsList) throws Exception{
//    	return postGraph(graph, null, false, tagsList);
//    }
//    
//    public JSONObject postGraph(JSONObject graph, JSONObject styleJSON, ArrayList<String> tagsList) throws Exception{
//    	return postGraph(graph, styleJSON, false, tagsList);
//    }
//    
    //TODO: Change return type
    /**
     * Posts NetworkX graph to the requesting users account on GraphSpace.
     * @param graph
     * @param isGraphPublic
     * @return
     * @throws Exception 
     */
	public JSONObject postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	int isPublic;
    	if (isGraphPublic){
    		isPublic = 1;
    	}
    	else{
    		isPublic = 0;
    	}
    	Map<String, Object> data = new HashMap<String, Object>();
    	GSGraph graph = new GSGraph(graphJSON);
    	graph.setStyleJSON(styleJSON);
    	data.put("name", graph.getName());
    	data.put("is_public", isPublic);
        data.put("owner_email", this.username);
        data.put("graph_json", graph.computeGraphJSON());
        data.put("style_json", graph.getStyleJSON());
        System.out.println("tags list: " + tagsList.toString());
        data.put("tags[]", tagsList.get(0));
//        if (!tagsList.isEmpty()){
//        	for (int i=0; i<tagsList.size(); i++){
//        		data.put("tags[]", new StringBody(tagsList.get(i)));
//        	}
//        }
//        if (!tagsList.isEmpty()){
////    		String[] tags = new String[tagsList.size()];
////    		tags = tagsList.toArray(tags);
//        	JSONArray tags = new JSONArray(tagsList);
//    		data.put("tags", tags);
//    	}
        
        System.out.println("\n\n\n");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("name: " + graph.getName() + " is public: " + isPublic + " ownerEmail: " + this.username);
        System.out.println("graph_json: " + graph.computeGraphJSON());
        System.out.println("style_json: " + graph.getStyleJSON());
    	return makeRequest("POST", "/api/v1/graphs", null, data);
    	//TODO: return relevant string from the json
    }
    
    /**
     * Get a graph owned by requesting user with the given name.
     * @param name
     * @return Graph Object
     * @throws Exception 
     */
    public JSONObject getGraph(String name) throws Exception{
    	String ownerEmail = this.username;
    	return getGraph(name, ownerEmail);
    }
    
    //TODO: Change return type to graph
    /**
     * Get a graph owned by requesting user with the given name.
     * @param name
     * @param ownerEmail
     * @return JSONObject
     * @throws Exception 
     */
    public JSONObject getGraph(String name, String ownerEmail) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", name);
    	JSONObject response = makeRequest("GET", "/api/v1/graphs/", urlParams, null);
		JSONObject body = response.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int total = ((JSONObject) array.get(0)).getInt("total");
    	if (total > 0){
    		return ((JSONArray) ((JSONObject)((JSONArray)((JSONObject)response.getJSONObject("body")).getJSONArray("array")).get(0)).getJSONArray("graphs")).getJSONObject(0);
    	}
    	else{
    		return null;
    	}
    }
    
    public JSONObject getGraphRequest(String name, String ownerEmail) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("owner_email", ownerEmail);
    	urlParams.put("names[]", name);
    	JSONObject response = makeRequest("GET", "/api/v1/graphs/", urlParams, null);
		JSONObject body = response.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int total = ((JSONObject) array.get(0)).getInt("total");
    	if (total > 0){
    		return response;
    	}
    	else{
    		return null;
    	}
    }
    
    //TODO: Change return type
    /**
     * Get a graph by id.
     * @param graphId
     * @return
     * @throws Exception 
     */
    public JSONObject getGraphById(String graphId) throws Exception{
    	String path = "/api/v1/graphs/"+graphId;
    	return makeRequest("GET", path, null, null);
    }
    
    //TODO: For default values of limit and offset
    /**
     * Get public graphs.
     * @param tagsList
     * @param limit
     * @param offset
     * @return
     * @throws Exception 
     */
    public JSONObject getPublicGraphs(ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("is_public", 1);
    	query.put("limit", limit);
    	query.put("offset", offset);
    	if (!tagsList.isEmpty()){
//    		JSONArray tags = new JSONArray(tagsList);
    		String[] tags = new String[tagsList.size()];
    		tags = tagsList.toArray(tags);
    		query.put("tags[]", tags.toString());
    	}
    	return makeRequest("GET", "/api/v1/graphs", query, null);
    }
    
    public JSONObject getPublicGraphsByNames(ArrayList<String> graphNames, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("is_public", 1);
    	query.put("limit", limit);
    	query.put("offset", offset);
    	if (!graphNames.isEmpty()){
    		String[] names = new String[graphNames.size()];
    		names = graphNames.toArray(names);
    		query.put("names[]", names.toString());
    	}
    	return makeRequest("GET", "/api/v1/graphs", query, null);
    }
    
    /**
     * 
     * @param limit
     * @param offset
     * @return
     * @throws Exception
     */
    public JSONObject getPublicGraphs(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("is_public", 1);
    	query.put("limit", limit);
    	query.put("offset", offset);
//    	Map<String, Object> data = new HashMap<String, Object>();
    	return makeRequest("GET", "/api/v1/graphs", query, null);
    }
    
    //TODO: For default values of limit and offset
    /**
     * Get graphs shared with the groups where requesting user is a member.
     * @param tagsList
     * @param limit
     * @param offset
     * @return
     * @throws Exception 
     */
    public JSONObject getSharedGraphs(ArrayList<String> tagsList, int limit, int offset) throws Exception{
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("member_email", this.username);
		query.put("limit", limit);
    	query.put("offset", offset);
    	if (tagsList!=null){
//    		JSONArray tags = new JSONArray(tagsList);
    		String[] tags = new String[tagsList.size()];
    		tags = tagsList.toArray(tags);
    		query.put("tags[]", tags.toString());
    	}
    	System.out.println(query.toString());
    	return makeRequest("GET", "/api/v1/graphs", query, null);
    }
    
    public JSONObject getSharedGraphsByNames(ArrayList<String> graphNames, int limit, int offset) throws Exception{
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("member_email", this.username);
		query.put("limit", limit);
    	query.put("offset", offset);
    	if (!graphNames.isEmpty()){
    		String[] names = new String[graphNames.size()];
    		names = graphNames.toArray(names);
    		query.put("names[]", names.toString());
    	}
    	System.out.println(query.toString());
    	return makeRequest("GET", "/api/v1/graphs", query, null);
    }
    
    
    //TODO: For default values of limit and offset    
    /**
     * Get graphs created by the requesting user.
     * @param tagsList
     * @param limit
     * @param offset
     * @return
     */
    public JSONObject getMyGraphs(ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
		query.put("owner_email", this.username);
		query.put("limit", limit);
    	query.put("offset", offset);
    	if (!tagsList.isEmpty()){
//    		JSONArray tags = new JSONArray(tagsList);
    		String[] tags = new String[tagsList.size()];
    		tags = tagsList.toArray(tags);
    		query.put("tags[]", tags.toString());
    	}
		return makeRequest("GET", "/api/v1/graphs/", query, null);
    }
    
    public JSONObject getMyGraphsByNames(ArrayList<String> graphNames, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
		query.put("owner_email", this.username);
		query.put("limit", limit);
    	query.put("offset", offset);
    	if (!graphNames.isEmpty()){
    		String[] names = new String[graphNames.size()];
    		names = graphNames.toArray(names);
    		query.put("names[]", names.toString());
    	}
		return makeRequest("GET", "/api/v1/graphs/", query, null);
    }
    
    public JSONObject getMyGraphs(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
		query.put("owner_email", this.username);
		query.put("limit", limit);
    	query.put("offset", offset);
		return makeRequest("GET", "/api/v1/graphs/", query, null);
    }
    
    //ASK: how to handle parameters that are not required in certain requests. Currently empty objects
    //TODO: change return type
    /**
     * 
     * @param name
     * @return
     * @throws Exception 
     */
    public JSONObject deleteGraph(String name) throws Exception{
    	JSONObject graphJSON = getGraph(name);
    	if (graphJSON == null){
    		throw new GraphNotFoundException(name, username);
    	}
    	else{
    		GSGraph graph = new GSGraph(graphJSON);
    		return makeRequest("DELETE", "/api/v1/graphs/" + graph.getId(), null, null);
    	}
    }
    
    //TODO: change return type
    /**
     * 
     * @param name
     * @param ownerEmail
     * @param graph
     * @param isGraphPublic
     * @return
     * @throws Exception 
     */
    public JSONObject updateGraph(String name, String ownerEmail, JSONObject graphJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	Map<String, Object> data = new HashMap<String, Object>();
    	int isPublic;
    	if (isGraphPublic){
    		isPublic = 1;
    	}
    	else{
    		isPublic = 0;
    	}
//    	int isPublic = (isGraphPublic) ? 1 : 0;
    	JSONObject graphResponse = getGraphRequest(name, ownerEmail);
    	String id = String.valueOf(graphResponse.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getInt("id"));
    	JSONObject styleJSON = graphResponse.getJSONObject("body").getJSONObject("object").getJSONArray("graphs").getJSONObject(0).getJSONObject("style_json");
    	GSGraph graph = new GSGraph(graphJSON);
    	graph.setId(id);
    	graph.setStyleJSON(styleJSON);
    	if (graphJSON!=null){
    		data.put("name", graph.getName());
    		data.put("is_public", isPublic);
    		data.put("owner_email", this.username);
    		data.put("graph_json", graph.computeGraphJSON());
    		data.put("style_json", graph.getStyleJSON());
//    		if (!tagsList.isEmpty()){
//            	for (int i=0; i<tagsList.size(); i++){
//            		data.put("tags[]", tagsList.get(i));
//            	}
//            }
    		data.put("tags[]", tagsList.get(0));
//    		System.out.println("update tags list: " + tagsList.toString());
//    		if (!tagsList.isEmpty()){
////        		String[] tags = new String[tagsList.size()];
////        		tags = tagsList.toArray(tags);
//    			JSONArray tags = new JSONArray(tagsList);
//        		data.put("tags", tags);
//        	}
    		return makeRequest("PUT", "/api/v1/graphs/" + graph.getId(), null, data);
    	}
    	else{
    		return null;
    	}
    	
    }
    
//    public JSONObject updateGraph(String name, JSONObject graphJSON, boolean isGraphPublic) throws Exception{
//    	String ownerEmail = this.username;
//    	return updateGraph(name, ownerEmail, graphJSON, isGraphPublic);
//    }
//    
//    /**
//     * 
//     * @param name
//     * @return
//     * @throws Exception 
//     */
//    public JSONObject makeGraphPublic(String name) throws Exception{
//    	return updateGraph(name, username, null, true);
//    }
//    
//    /**
//     * 
//     * @param name
//     * @return
//     * @throws Exception 
//     */
//    public JSONObject makeGraphPrivate(String name) throws Exception{
//    	return updateGraph(name, username, null, false);
//    }
    
    //TODO: change return type
    /**
     * 
     * @param graphId
     * @param layoutName
     * @param positionsJSON
     * @param styleJSON
     * @param isGraphShared
     * @return
     * @throws Exception 
     */
    public JSONObject postGraphLayout(String graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{
    	/**
    	Sample style_json::
		{
		    "style": [
		        {
		            "selector": "node[name='P4314611']",
		            "style": {
		                "border-color": "#888",
		                "text-halign": "center",
		                "text-valign": "center",
		                "border-width": "2px",
		                "height": "50px",
		                "width": "50px",
		                "shape": "ellipse",
		                "background-blacken": "0.1",
		                "background-color": "yellow"
		            }
		        },
		        {
		            "selector": "node[name='P0810711']",
		            "style": {
		                "text-halign": "center",
		                "text-valign": "center",
		                "text-outline-color": "#888",
		                "text-outline-width": "2px",
		                "border-color": "black",
		                "border-width": "5px",
		                "height": "150px",
		                "shape": "ellipse",
		                "color": "black",
		                "border-style": "double",
		                "text-wrap": "wrap",
		                "background-blacken": "0",
		                "width": "150px",
		                "background-color": "red"
		            }
		        },
		        {
		            "selector": "edge[name='P4314611-P0810711']",
		            "style": {
		                "curve-style": "bezier",
		                "line-style": "dotted",
		                "width": "12px",
		                "line-color": "blue",
		                "source-arrow-color": "yellow",
		                "target-arrow-shape": "triangle"
		            }
		        }
		    ]
		}

		Sample positions_json::
	
			{
			    "P4314611": {
			        "y": 87,
			        "x": 35
			    },
			    "P0810711": {
			        "y": 87.89306358381505,
			        "x": 208.18593448940268
			    }
			}
		**/

    	int isShared = (isGraphShared) ? 1 : 0;
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", layoutName);
    	data.put("graph_id", graphId);
    	data.put("is_shared", isShared);
    	data.put("owner_email", this.username);
    	if (positionsJSON != null){
    		data.put("positions_json", positionsJSON);
    	}
    	else{
    		positionsJSON = new JSONObject();
    		data.put("positions_json", positionsJSON);
    	}
    	if (styleJSON != null){
    		data.put("style_json", styleJSON);
    	}
    	else{
    		styleJSON = new JSONObject();
    		styleJSON.append("style", new JSONArray());
    		data.put("positions_json", styleJSON);
    	}
    	String path = String.format("/api/v1/graphs/%s/layouts/", graphId);
    	return makeRequest("POST", path, null, data);
    }
    
    //TODO: change return type    
    /**
     * 
     * @param graphId
     * @param layoutId
     * @param layoutName
     * @param positionsJSON
     * @param styleJSON
     * @param isShared
     * @return
     * @throws Exception 
     */
    public JSONObject updateGraphLayout(String graphId, String layoutId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isShared) throws Exception{
    	/**
    	Sample style_json::

		{
		    "style": [
		        {
		            "selector": "node[name='P4314611']",
		            "style": {
		                "border-color": "#888",
		                "text-halign": "center",
		                "text-valign": "center",
		                "border-width": "2px",
		                "height": "50px",
		                "width": "50px",
		                "shape": "ellipse",
		                "background-blacken": "0.1",
		                "background-color": "yellow"
		            }
		        },
		        {
		            "selector": "node[name='P0810711']",
		            "style": {
		                "text-halign": "center",
		                "text-valign": "center",
		                "text-outline-color": "#888",
		                "text-outline-width": "2px",
		                "border-color": "black",
		                "border-width": "5px",
		                "height": "150px",
		                "shape": "ellipse",
		                "color": "black",
		                "border-style": "double",
		                "text-wrap": "wrap",
		                "background-blacken": "0",
		                "width": "150px",
		                "background-color": "red"
		            }
		        },
		        {
		            "selector": "edge[name='P4314611-P0810711']",
		            "style": {
		                "curve-style": "bezier",
		                "line-style": "dotted",
		                "width": "12px",
		                "line-color": "blue",
		                "source-arrow-color": "yellow",
		                "target-arrow-shape": "triangle"
		            }
		        }
		    ]
		}

		Sample positions_json::
	
			{
			    "P4314611": {
			        "y": 87,
			        "x": 35
			    },
			    "P0810711": {
			        "y": 87.89306358381505,
			        "x": 208.18593448940268
			    }
			}
		**/
    	Map<String, Object> data = new HashMap<String, Object>();
		if (layoutName != null){
			data.put("name", layoutName);
		}
		
		data.put("is_shared", isShared);
		
		if (positionsJSON != null){
			data.put("positions_json", positionsJSON);
		}
		if (styleJSON != null){
			data.put("style_json", styleJSON);
		}
		
		String path = String.format("/api/v1/graphs/%s/layouts/%s", graphId, layoutId);
		return makeRequest("PUT", path, null, data);
    }

    //TODO: change return type
    //TODO: Handle Exception
    /**
     * 
     * @param graphId
     * @param layoutId
     * @return
     * @throws Exception 
     */
    public JSONObject deleteGraphLayout(String graphId, String layoutId) throws Exception{
    	String path = String.format("/api/v1/graphs/%s/layouts/%s", graphId, layoutId);
    	return makeRequest("DELETE", path, null, null);
    }

    //TODO: change return type
    //TODO: Handle Exception
    /**
     * 
     * @param graphId
     * @param layoutId
     * @return
     * @throws Exception 
     */
    public JSONObject getGraphLayout(String graphId, String layoutId) throws Exception{
    	String path = String.format("/api/v1/graphs/%s/layouts/%s", graphId, layoutId);
    	JSONObject response = makeRequest("GET", path, null, null);
    	if (response.has("id")){
    		return response;
    	}
    	else{
    		return null;
    	}
    }
    
    //TODO: Handle Exception
    /**
     * 
     * @param graphId
     * @param limit
     * @param offset
     * @return
     * @throws Exception 
     */
    public JSONObject getMyGraphLayouts(String graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", this.username);
    	String path = String.format("/api/v1/graphs/%s/layouts/", graphId);
    	return makeRequest("GET", path, query, null);
    }

    //TODO: Handle Exception
    /**
     * 
     * @param graphId
     * @param limit
     * @param offset
     * @return
     * @throws Exception 
     */
    public JSONObject getSharedGraphLayouts(String graphId, int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("is_shared", 1);
    	String path = String.format("/api/v1/graphs/%s/layouts/", graphId);
    	return makeRequest("GET", path, query, null);
    }
    
    
    
    /**
     * GROUPS
     * @throws Exception 
     */
    
    public JSONObject getGroup(String name) throws Exception{
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	urlParams.put("member_email", this.username);
    	urlParams.put("name", name);
    	JSONObject response = makeRequest("GET", "/api/v1/groups", urlParams, null);
    	JSONObject body = response.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
    	int total = ((JSONObject) array.get(0)).getInt("total");
    	if(total>0){
    		return response.getJSONArray("groups").getJSONObject(0);
    	}
    	return null;
    }
    
    public JSONObject getMyGroups(int limit, int offset) throws Exception{
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("owner_email", this.username);
    	return makeRequest("GET", "/api/v1/groups/", query, null);
    }
    
    public JSONObject addGroupGraph(String graphId, String name, String groupId) throws Exception{
    	if (groupId != null){
    		Map<String, Object> data = new HashMap<String, Object>();
    		data.put("graph_id", graphId);
    		return makeRequest("POST", "/api/v1/groups/"+groupId+"/graphs", null, data);
    	}
    	if (name != null){
    		JSONObject response = getGraph(name);
    		if (response!=null){
    			int id = response.getInt("id");
    			Map<String, Object> data = new HashMap<String, Object>();
    			data.put("graph_id", graphId);
    			return makeRequest("POST", "/api/v1/groups/"+String.valueOf(groupId)+"/graphs", null, data);
    		}
    		else{
    			return null;
    		}
    	}
    	else{
    		return null;
    	}
    }
}