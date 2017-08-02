package org.graphspace.javaclient;

public class User{
	public static String host = "http://www.graphspace.org";
	public static String username;
	public static String password;
	
	public void setHost(String host){
		User.host = host;
	}
	
	public void setUsername(String username) {
		User.username = username;
	}
	
	public void setPassword(String password) {
		User.password = password;
	}
}