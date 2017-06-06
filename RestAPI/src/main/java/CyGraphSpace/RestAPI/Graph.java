package CyGraphSpace.RestAPI;

import org.json.JSONObject;

public class Graph{
	String name;
	String id;
	
	public String getName(){
		return this.name;
	}
	
	public JSONObject computeGraphJSON(){
		return new JSONObject();
	}
	
	public JSONObject getStyleJSON(){
		return new JSONObject();
	}
	
	public String getId(){
		return this.id;
	}
}