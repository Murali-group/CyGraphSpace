package org.graphspace.javaclient.exceptions;

public class GraphException extends Exception{
	
	public GraphException(int exceptionCode, String exceptionMessage, String additionalMessage){
		super("["+String.valueOf(exceptionCode)+"] "+exceptionMessage + " : " + additionalMessage);
	}
}