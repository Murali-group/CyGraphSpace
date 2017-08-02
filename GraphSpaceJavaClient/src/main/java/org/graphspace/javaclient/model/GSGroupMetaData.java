package org.graphspace.javaclient.model;

public class GSGroupMetaData{
	private String name;
	private int id;
	private String ownerEmail;
	
	public GSGroupMetaData(String name, int id, String ownerEmail){
		this.name = name;
		this.id = id;
		this.ownerEmail = ownerEmail;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getOwnerEmail(){
		return this.ownerEmail;
	}
}