package util;

public class Config {
	public static String VERSION = "v1";
	public static String HOST = "http://www.graphspace.org";
	public static String GROUPS_PATH = String.format("/api/%s/groups/", VERSION);
	public static String GRAPHS_PATH = String.format("/api/%s/graphs/", VERSION);
}
