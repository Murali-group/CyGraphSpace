package org.graphspace.javaclient.util;

public class Config {
	public static String VERSION = "v1";
	public static String HOST = "http://www.graphspace.org";
	public static String GROUPS_PATH = String.format("/api/%s/groups/", VERSION);
	public static String GRAPHS_PATH = String.format("/api/%s/graphs/", VERSION);
	
	public static String getLayoutsPath(int graphId) {
		return Config.GRAPHS_PATH + graphId + "/layouts/";
	}
	
	public static String getLayoutPath(int graphId, int layoutId) {
		return String.format(Config.GRAPHS_PATH+"%s/layouts/%s", graphId, layoutId);
	}
	
	public static String getGroupPath(int groupId) {
		return Config.GROUPS_PATH + groupId;
	}
	
	public static String getMembersPath(int groupId) {
		return String.format(Config.GROUPS_PATH + "%s/members/", groupId);
	}
	
	public static String getMemberPath(int groupId, int memberId) {
		return String.format(Config.GROUPS_PATH+"%s/members/%s", groupId, memberId);
	}
	
	public static String getGroupGraphsPath(int groupId) {
		return Config.GROUPS_PATH+groupId+"/graphs";
	}
	
	public static String getGroupGraphPath(int groupId, int graphId) {
		return String.format(Config.GROUPS_PATH+"%s/graphs%s", groupId, graphId);
	}
}
