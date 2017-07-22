package org.graphspace.javaclient.model;

import java.util.ArrayList;

public class GSGraphMetaData{
	private String name;
	private int id;
	private String owner;
	private ArrayList<String> tags;
	
	public GSGraphMetaData(String name, int id, String owner, ArrayList<String> tags){
		this.name = name;
		this.id = id;
		this.owner = owner;
		this.tags = tags;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getOwner(){
		return this.owner;
	}
	
	public ArrayList<String> getTags(){
		return this.tags;
	}
}