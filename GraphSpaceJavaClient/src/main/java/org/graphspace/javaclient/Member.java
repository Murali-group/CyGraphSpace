package org.graphspace.javaclient;

import org.json.JSONObject;

public class Member{
	
	private String email;
	private Integer id;
	
	public Member(String email) {
		this.email = email;
	}
	
	public Member(int id, String email) {
		this.id = id;
		this.email = email;
	}
	
	public Member(JSONObject memberJson) {
		this.email = memberJson.getString("email");
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
}
