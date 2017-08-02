package org.graphspace.javaclient.exceptions;

public class LayoutException extends Exception{
	
	public LayoutException(int exceptionCode, String exceptionMessage, String additionalMessage){
		super("["+String.valueOf(exceptionCode)+"] "+exceptionMessage + " : " + additionalMessage);
	}
}