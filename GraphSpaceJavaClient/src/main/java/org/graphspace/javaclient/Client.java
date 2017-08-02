/**
 * 
 */
package org.graphspace.javaclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphspace.javaclient.Config;
import org.graphspace.javaclient.exceptions.ExceptionCode;
import org.graphspace.javaclient.exceptions.ExceptionMessage;
import org.graphspace.javaclient.exceptions.GraphException;
import org.graphspace.javaclient.exceptions.LayoutException;
import org.graphspace.javaclient.model.GSGraph;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author rishabh
 *
 */
public class Client {
	
	/**
	 * Authenticate user with username and password. The user can set a different host by setting it in Config class
	 * 
	 * @param username(String) email of the user
	 * @param password(String) password of the user
	 */
    public void authenticate(String username, String password){
    	User.host = Config.HOST;
    	User.username = username;
    	User.password = password;
    }
    
    /**
     * Authenticate user with username and password. The user can set a different host by setting it in Config class
     * 
     * @param host(String) URL of the server where graphs are hosted. User can also use this library with self hosted graphs
     * @param username(String) email of the user
     * @param password(String) password of the user
     */
    public void authenticate(String host, String username, String password){
    	User.host = host;
    	User.username = username;
    	User.password = password;
    }
    
    /**
     * ============================================
     * GRAPH METHODS
     * ============================================
     */
    
	/**
     * Reference: {@link org.graphspace.javaclient.Graphs#getGraphById(int graphId)}
     */
    public static JSONObject getGraphById(int graphId) throws Exception{
    	return Graphs.getGraphById(graphId);
    }
    
	/**
	 * Reference: {@link org.graphspace.javaclient.Graphs#getGraphByName(String graphName, String ownerEmail)}
	 */
    public static JSONObject getGraphByName(String graphName, String ownerEmail) throws Exception{
    	return Graphs.getGraphByName(graphName, ownerEmail);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getGraphResponse(String graphName, String ownerEmail)}
     */
    public static JSONObject getGraphResponse(String graphName, String ownerEmail) throws Exception{
    	return Graphs.getGraphResponse(graphName, ownerEmail);
    }
    
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getPublicGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public static JSONObject getPublicGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	return Graphs.getPublicGraphs(graphNames, tagsList, limit, offset);
    }
    
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getSharedGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public static JSONObject getSharedGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
		return Graphs.getSharedGraphs(graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#getMyGraphs(ArrayList graphNames, ArrayList tagsList, int limit, int offset)}
     */
    public static JSONObject getMyGraphs(ArrayList<String> graphNames, ArrayList<String> tagsList, int limit, int offset) throws Exception{
    	return Graphs.getMyGraphs(graphNames, tagsList, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList tagsList)}
     */
    public static JSONObject postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	return Graphs.postGraph(graphJSON, styleJSON, isGraphPublic, tagsList);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#updateGraph(String graphName, String ownerEmail, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList tagsList)}
     */
    public static JSONObject updateGraph(String graphName, String ownerEmail, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
    	return Graphs.updateGraph(graphName, ownerEmail, graphJSON, styleJSON, isGraphPublic, tagsList);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Graphs#deleteGraph(Integer id, String graphName)}
     */
    public static String deleteGraph(Integer id, String graphName) throws Exception{
    	return Graphs.deleteGraph(id, graphName);
    }
    
    /**
     * ============================================
     * LAYOUT METHODS
     * ============================================
     */
	
	/**
	 * Reference: {@link org.graphspace.javaclient.Layouts#getGraphLayout(int graphId, int layoutId, String ownerEmail)}
	 */
    public static JSONObject getGraphLayout(int graphId, int layoutId, String ownerEmail) throws Exception{
    	return Layouts.getGraphLayout(graphId, layoutId, ownerEmail);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#getMyGraphLayouts(int graphId, int limit, int offset)}
     */
    public static JSONObject getMyGraphLayouts(int graphId, int limit, int offset) throws Exception{
    	return Layouts.getMyGraphLayouts(graphId, limit, offset);
    }

    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#getSharedGraphLayouts(String graphId, int limit, int offset)}
     */
    public static JSONObject getSharedGraphLayouts(String graphId, int limit, int offset) throws Exception{
    	return Layouts.getSharedGraphLayouts(graphId, limit, offset);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#postGraphLayout(int graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared)}
     */
	public static JSONObject postGraphLayout(int graphId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{
		return Layouts.postGraphLayout(graphId, layoutName, positionsJSON, styleJSON, isGraphShared);
    }
	
	/**
	 * Reference: {@link org.graphspace.javaclient.Layouts#updateGraphLayout(int graphId, int layoutId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared)}
	 */
    public static JSONObject updateGraphLayout(int graphId, int layoutId, String layoutName, JSONObject positionsJSON, JSONObject styleJSON, boolean isGraphShared) throws Exception{
    	return Layouts.updateGraphLayout(graphId, layoutId, layoutName, positionsJSON, styleJSON, isGraphShared);
    }
    
    /**
     * Reference: {@link org.graphspace.javaclient.Layouts#deleteGraphLayout(int graphId, String layoutId)}
     */
    public static String deleteGraphLayout(int graphId, String layoutId) throws Exception{
    	return Layouts.deleteGraphLayout(graphId, layoutId);
    }
}