package org.graphspace.javaclient.exceptions;

public class GraphNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GraphNotFoundException(String name, String username){
		System.out.println("Graph with name " + name + " doesn't exist for user " + username);
	}
	
	public GraphNotFoundException(){
		System.out.println("Graph not found Exception");
	}
}
