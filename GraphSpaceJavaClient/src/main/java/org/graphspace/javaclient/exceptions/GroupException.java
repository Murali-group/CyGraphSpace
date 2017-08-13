package org.graphspace.javaclient.exceptions;

public class GroupException extends Exception{
	public GroupException(int exceptionCode, String exceptionMessage, String additionalMessage){
		super("["+String.valueOf(exceptionCode)+"] "+exceptionMessage + " : " + additionalMessage);
	}
}