package org.graphspace.javaclient;

import org.json.JSONObject;

/**
 * This class defines Member Object and getters and setters for the member properties
 * @author rishabh
 *
 */
public class Member{
	
	
	private String email;
	private Integer id;
	
	/**
	 * Constructor for a Member object
	 * @param email(String) email of the member
	 */
	public Member(String email) {
		this.email = email;
	}
	
	/**
	 * Constructor for a Member object
	 * @param id(int) id of the member
	 * @param email(String) email of the member
	 */
	public Member(int id, String email) {
		this.id = id;
		this.email = email;
	}
	
	/**
	 * Constructor for a Member object
	 * @param memberJson(JSONObject) json of the member
	 */
	public Member(JSONObject memberJson) {
		this.id = memberJson.getInt("id");
		this.email = memberJson.getString("email");
	}
	
	/**
	 * @return email of the member 
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * @return id of the member
	 */
	public Integer getId() {
		return this.id;
	}
	
	/**
	 * set email of the member
	 * @param email(String) email of the member
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * set id of the member
	 * @param id(int) id of the member
	 */
	public void setId(int id) {
		this.id = id;
	}
}
