package org.cytoscape.graphspace.cygraphspace.internal.singletons;

import org.graphspace.javaclient.Client;

public enum Server{
	INSTANCE;
	public Client client;
	String username;
	String password;
	String host;
	
	public void authenticate(String username, String password, String host){
		this.username = username;
		this.password = password;
		this.host = host;
		client.authenticate(username, password, host);
	}
		
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public String getHost(){
		return this.host;
	}
	
	//TODO: authenticated method in rest api
	public boolean isAuthenticated(){
		return true;
	}
}