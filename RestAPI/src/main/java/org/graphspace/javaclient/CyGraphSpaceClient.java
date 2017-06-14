package org.graphspace.javaclient;

import java.util.HashMap;
import java.util.Map;

public class CyGraphSpaceClient{
	
	String host;
	String username;
	String password;
	Client client;
	final String defaultHost = "www.graphspace.org";
	
	public CyGraphSpaceClient(String host, String username, String password){
		this.host = host;
		this.username = username;
		this.password = password;
		client = new Client();
		client.authenticate(this.host, this.username, this.password);
	}
	
	public CyGraphSpaceClient(String username, String password){
		this.host = this.defaultHost;
		this.username = username;
		this.password = password;
		client = new Client();
		client.authenticate(this.host, this.username, this.password);
	}
	
	public Map<String, String> getMyGraphs(){
		return new HashMap<String, String>();
	}
}