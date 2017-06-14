package org.graphspace.javaclient;

import java.util.ArrayList;

import org.json.JSONObject;

public class Graph{
	String name;
	String id;
	JSONObject graph;
	int numberOfNodes;
	int numberOfEdges;
	String username;
	String group;
	ArrayList<String> tags;
	String lastModified;
	
	public Graph(JSONObject graph){
		this.graph = graph;
	}
	
	public String getName(){
		return this.name;
	}
	
	public JSONObject computeGraphJSON(){
		return new JSONObject();
	}
	
	public JSONObject getStyleJSON(){
		return new JSONObject();
	}
	
	public JSONObject getGraphJSON(){
		return new JSONObject();
	}
	
	public String getId(){
		return this.id;
	}
	
	public int numberOfNodes(){
		return this.numberOfNodes;
	}
	
	public int numberOfEdges(){
		return this.numberOfEdges;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getGroup(){
		return this.group;
	}
	
	public ArrayList<String> getTags(){
		return this.tags;
	}
	
	public String getLastModified(){
		return this.lastModified;
	}
	
}