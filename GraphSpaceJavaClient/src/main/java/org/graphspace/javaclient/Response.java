package org.graphspace.javaclient;

public class Response {
	private int status;
	private String message;
	private ArrayList<Graph> graphs;
	
	public Response(int status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public String toString() {
		return this.status + " : " + this.message;
	}
	
}
