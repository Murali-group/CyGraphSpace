package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.graphspace.javaclient.Graph;
import org.graphspace.javaclient.GraphSpaceClient;
import org.graphspace.javaclient.Group;
import org.graphspace.javaclient.Member;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupsTest{
//
//	private static String host;
//	private static String username;
//	private static String password;
//	private static GraphSpaceClient client;
//	private static String groupName;
//	private static String groupDescription;
//	private static int numberOfMyGroups;
//	private static int numberOfAllGroups;
//	private static String memberEmail;
//	private static JSONObject graphJson;
//	private static JSONObject styleJson;
//	private static String graphName;
//	private static String graphFileName;
//	private static String styleFileName;
//	@BeforeClass
//	public static void prepareTests() throws IOException {
//		host = TestConfig.HOST;
//		username = TestConfig.USERNAME;
//		password = TestConfig.PASSWORD;
//		client = new GraphSpaceClient(host, username, password);
//		groupName = TestConfig.GROUP_NAME;
//		groupDescription = "Test group to test GraphSpace java client library";
//		memberEmail = TestConfig.MEMBER_EMAIL;
//		graphFileName = TestConfig.POST_GRAPH_FILENAME;
//		styleFileName = TestConfig.POST_GRAPH_STYLE_FILENAME;
//		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//		File file = new File(classLoader.getResource(graphFileName).getFile());
//		InputStream is = new FileInputStream(file);
//		String graphJsonText = IOUtils.toString(is);
//		graphJson = new JSONObject(graphJsonText);
//		file = new File(classLoader.getResource(styleFileName).getFile());
//		is = new FileInputStream(file);
//		String styleJsonText = IOUtils.toString(is);
//		styleJson = new JSONObject(styleJsonText);
//		graphName = graphJson.getJSONObject("data").getString("name");
//	}
//
//	@Test
//	public void a_getMyGroupsTest() throws Exception {
//		System.out.println("Running Test: getMyGroups");
//		ArrayList<Group> groups = client.getMyGroups(Integer.MAX_VALUE, 0);
//		numberOfMyGroups = groups.size();
//		System.out.println("Number of my groups initially: " + numberOfMyGroups);
//	}
//
//	@Test
//	public void b_getAllGroupsTest() throws Exception {
//		System.out.println("Running Test: getAllGroups");
//		ArrayList<Group> groups = client.getMyGroups(Integer.MAX_VALUE, 0);
//		numberOfAllGroups = groups.size();
//		System.out.println("Number of all groups initially: " + numberOfAllGroups);
//	}
//
//	@Test
//	public void d_getGroupTest() throws Exception {
//		System.out.println("Running Test: getGroup");
//		Group group = client.getGroup(groupName);
//		System.out.println("Group Name: " + group.getName());
//	}
}
