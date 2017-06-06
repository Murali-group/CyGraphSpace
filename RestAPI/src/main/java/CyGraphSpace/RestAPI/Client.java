/**
 * 
 */
package CyGraphSpace.RestAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import CyGraphSpace.RestAPI.Exceptions.GraphNotFoundException;
import CyGraphSpace.RestAPI.Exceptions.RequestTypeNotDefinedException;

import static java.nio.charset.StandardCharsets.*;
import java.net.URLEncoder;
/**
 * 
 * @author rishabh
 *
 */
public class Client {
	String APIHOST = "www.graphspace.org";
	String username;
	String password;
	
    public void authenticate(String username, String password){
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
//		Object[] params = new Object[]{this.APIHOST, path, };
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
    private JSONObject getRequest(String path, Map<String, Object> urlParams, Map<String, String> headers) throws Exception{
    	
		String queryPath = "http://"+this.APIHOST+path;
		try{
			HttpResponse<JsonNode> getResponse = Unirest.get(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
					.queryString(urlParams)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			throw new GraphNotFoundException();
		}
    }
    
    /**
     * 
     * @param path
     * @param data
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject postRequest(String path, Map<String, Object> data, Map<String, String> headers) throws Exception{
    	
		String queryPath = "http://"+this.APIHOST+path;
		try{
			HttpResponse<JsonNode> getResponse = Unirest.post(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
					.fields(data)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			throw new GraphNotFoundException();
		}
    }
    
    /**
     * 
     * @param path
     * @param data
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject putRequest(String path, Map<String, Object> data, Map<String, String> headers) throws Exception{
    	
		String queryPath = "http://"+this.APIHOST+path;
		try{
			HttpResponse<JsonNode> getResponse = Unirest.put(queryPath)
					.basicAuth(this.username, this.password)
					.headers(headers)
					.fields(data)
					.asJson();
			JSONObject response = new JSONObject(getResponse);
			return response;
		}
		catch(Exception e){
			throw new GraphNotFoundException();
		}
    }
    
    /**
     * 
     * @param path
     * @param urlParams
     * @param headers
     * @return
     * @throws Exception
     */
    private JSONObject deleteRequest(String path, Map<String, Object> urlParams, Map<String, String> headers) throws Exception{
    	
		String queryPath = "http://"+this.APIHOST+path;
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
			throw new GraphNotFoundException();
		}
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
     */
    public String postGraph(Graph graph){
    	return postGraph(graph, false);
    }
    
    //TODO: Change return type
    /**
     * Posts NetworkX graph to the requesting users account on GraphSpace.
     * @param graph
     * @param isGraphPublic
     * @return
     */
    public String postGraph(Graph graph, boolean isGraphPublic){
    	int isPublic = (isGraphPublic) ? 1 : 0;
    	Map<String, Object> urlParams = new HashMap<String, Object>();
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("name", graph.getName());
    	data.put("is_public", isPublic);
        data.put("owner_email", username);
        data.put("graph_json", graph.computeGraphJSON());
        data.put("style_json", graph.getStyleJSON());
    	return makeRequest("POST", "/api/v1/graphs", urlParams, data);
    	//TODO: return relevant string from the json
    }
    
    /**
     * Get a graph owned by requesting user with the given name.
     * @param name
     * @return Graph Object
     */
    public Graph getGraph(String name){
    	String ownerEmail = this.username;
    	return getGraph(name, ownerEmail);
    }
    
    //TODO: Change return type to graph
    /**
     * Get a graph owned by requesting user with the given name.
     * @param name
     * @param ownerEmail
     * @return Graph Object
     */
    public JSONObject getGraph(String name, String ownerEmail){
    	JSONObject data = new JSONObject();
    	JSONObject urlParams = new JSONObject();
    	urlParams.append("owner_email", ownerEmail);
    	urlParams.append("names[]", name);
    	JSONObject response = makeRequest("GET", "/api/v1/graphs/", urlParams, data);
    	if (response.getInt("total") > 0){
    		return response.getJSONArray("graphs").getJSONObject(0);
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
     */
    public Graph getGraphById(String graphId){
    	JSONObject data = new JSONObject();
    	JSONObject urlParams = new JSONObject();
    	String path = "/api/v1/graphs/"+graphId;
    	return makeRequest("GET", path, urlParams, data);
    }
    
    //TODO: For default values of limit and offset
    /**
     * Get public graphs.
     * @param tagsList
     * @param limit
     * @param offset
     * @return
     */
    public JSONObject getPublicGraphs(ArrayList<String> tagsList, int limit, int offset){
    	JSONObject query = new JSONObject();
    	query.append("is_public", 1);
    	query.append("limit", limit);
    	query.append("offset", offset);
    	if (!tagsList.isEmpty()){
    		JSONArray tags = new JSONArray(tagsList);
    		query.append("tags[]", tags);
    	}
    	JSONObject data = new JSONObject();
    	return makeRequest("GET", "/api/v1/graphs", query, data);
    }
    
    //TODO: For default values of limit and offset
    /**
     * Get graphs shared with the groups where requesting user is a member.
     * @param tagsList
     * @param limit
     * @param offset
     * @return
     */
    public JSONObject getSharedGraphs(ArrayList<String> tagsList, int limit, int offset){
		JSONObject query = new JSONObject();
		query.append("member_email", this.username);
		query.append("limit", limit);
    	query.append("offset", offset);
    	if (!tagsList.isEmpty()){
    		JSONArray tags = new JSONArray(tagsList);
    		query.append("tags[]", tags);
    	}
    	JSONObject data = new JSONObject(); 
    	return makeRequest("GET", "/api/v1/graphs/", query, data);
    }
    
    //TODO: For default values of limit and offset    
    /**
     * Get graphs created by the requesting user.
     * @param tagsList
     * @param limit
     * @param offset
     * @return
     */
    public JSONObject getMyGraphs(ArrayList<String> tagsList, int limit, int offset){
    	JSONObject query = new JSONObject();
		query.append("owner_email", this.username);
		query.append("limit", limit);
    	query.append("offset", offset);
    	if (!tagsList.isEmpty()){
    		JSONArray tags = new JSONArray(tagsList);
    		query.append("tags[]", tags);
    	}
		return makeRequest("GET", "/api/v1/graphs/", query, null);
    }
    
    //ASK: how to handle parameters that are not required in certain requests. Currently empty objects
    //TODO: change return type
    /**
     * 
     * @param name
     * @return
     */
    public String deleteGraph(String name){
    	Graph graph = getGraph(name);
    	if (graph == null){
    		throw new GraphNotFoundException(name, username);
    	}
    	else{
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
     */
    public String updateGraph(String name, String ownerEmail, Graph graph, boolean isGraphPublic){
    	JSONObject data = new JSONObject();
    	int isPublic = (isGraphPublic) ? 1 : 0;
    	if (graph!=null){
    		data.append("name", graph.getName());
    		data.append("is_public", isPublic);
    		data.append("graph_json", graph.computeGraphJSON());
    		data.append("style_json", graph.getStyleJSON());
    	}
    	else{
    		data.append("is_public", isPublic);
    	}
    	graph = getGraph(name, ownerEmail);
    	if (graph == null){
    		throw new GraphNotFoundException(name, username);
    	}
    	else{
    		return makeRequest("DELETE", "/api/v1/graphs/" + graph.getId(), data, null);
    	}
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public String makeGraphPublic(String name){
    	return updateGraph(name, username, null, true);
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public String makeGraphPrivate(String name){
    	return updateGraph(name, username, null, false);
    }
    
    //TODO: change return type
    /**
     * 
     * @param graphId
     * @param layoutName
     * @param positionsJSON
     * @param styleJSON
     * @param isGraphShared
     * @return
     */
    public String postGraphLayout(String graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared){
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
    	JSONObject data = new JSONObject();
    	data.append("name", layoutName);
    	data.append("graph_id", graphId);
    	data.append("is_shared", isShared);
    	data.append("owner_email", this.username);
    	if (positionsJSON != null){
    		data.append("positions_json", positionsJSON);
    	}
    	else{
    		positionsJSON = new JSONObject();
    		data.append("positions_json", positionsJSON);
    	}
    	if (styleJSON != null){
    		data.append("style_json", styleJSON);
    	}
    	else{
    		styleJSON = new JSONObject();
    		styleJSON.append("style", new JSONArray());
    		data.append("positions_json", styleJSON);
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
     */
    public String updateGraphLayout(String graphId, String layoutId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, ArrayList isShared){
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
    	JSONObject data = new JSONObject();
		if (layoutName != null){
			data.append("name", layoutName);
		}
		if (isShared != null){
			data.append("is_shared", isShared);
		}
		if (positionsJSON != null){
			data.append("positions_json", positionsJSON);
		}
		if (styleJSON != null){
			data.append("style_json", styleJSON);
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
     */
    public String deleteGraphLayout(String graphId, String layoutId){
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
     */
    public String getGraphLayout(String graphId, String layoutId){
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
     */
    public JSONObject get_my_graph_layouts(String graphId, int limit, int offset){
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
     */
    public JSONObject getsharedGraphLayouts(String graphId, int limit, int offset){
    	Map<String, Object> query = new HashMap<String, Object>();
    	query.put("limit", limit);
    	query.put("offset", offset);
    	query.put("is_shared", 1);
    	String path = String.format("/api/v1/graphs/%s/layouts/", graphId);
    	return makeRequest("GET", path, query, null);
    }
}