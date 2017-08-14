package org.graphspace.javaclient.model;

public class GSMember{
	private int id;
	private String email;
	
	public GSMember(int id, String email) {
		this.id = id;
		this.email = email;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}