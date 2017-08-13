package org.graphspace.javaclient.model;

import java.util.ArrayList;

import org.json.JSONObject;

public class GSGraph{
	
	String name;
	int id;
	JSONObject graph;
	int numberOfNodes;
	int numberOfEdges;
	String username;
	String group;
	ArrayList<String> tags;
	String lastModified;
	JSONObject styleJSON;
	
	public GSGraph(JSONObject graph){
		this.graph = graph;
		this.name = graph.getString("name");
		this.id = graph.getInt("id");
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public JSONObject computeGraphJSON(){
		return this.graph;
	}
	
	public void setStyleJSON(JSONObject styleJSON){
		this.styleJSON = styleJSON;
	}
	
	public JSONObject getStyleJSON(){
		return this.styleJSON;
	}
	
	public JSONObject getGraphJSON(){
		return this.graph;
	}
	
	public int getId(){
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