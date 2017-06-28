package org.graphspace.javaclient.model;

public class GSGraphMetaData{
	private String name;
	private int id;
	private String ownedBy;
	private int access;
	
	public GSGraphMetaData(String name, int id, String ownedBy, int access){
		this.name = name;
		this.id = id;
		this.ownedBy = ownedBy;
		this.access = access;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getOwnedBy(){
		return this.ownedBy;
	}
	
	public int getAccess(){
		return this.access;
	}
}