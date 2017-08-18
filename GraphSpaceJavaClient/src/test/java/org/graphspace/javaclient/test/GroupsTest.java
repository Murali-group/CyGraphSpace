package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.graphspace.javaclient.GraphSpaceClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupsTest{
	
//	private static String host;
//	private static String username;
//	private static String password;
//	private static GraphSpaceClient client;
//	private static String getGroupName;
//	private static String postGroupName;
//	private static String groupDescription;
//	
//	@BeforeClass
//	public static void prepareTests() throws IOException {
//		host = TestConfig.HOST;
//		username = TestConfig.USERNAME;
//		password = TestConfig.PASSWORD;
//		client = new GraphSpaceClient(host, username, password);
//		getGroupName = TestConfig.GET_GROUP_NAME;
//		postGroupName = TestConfig.POST_GROUP_NAME;
//		groupDescription = "Test group to test GraphSpace java client library";
//	}
//	
////	@Test
////	public void a_postGroupTest() throws Exception {
////		GSGroup group = new GSGroup();
////		group.setName(groupName2);
////		group.setDescription(groupDescription);
////		JSONObject response = client.postGroup(group);
////		System.out.println(response.toString());
////		assertEquals(200, response.getInt("status"));
////	}
//	
//	@Test
//	public void b_getGroupTest() throws Exception {
//		System.out.println("Running Test: getGroup");
//		String groupNameResponse = client.getGroup(getGroupName).getString("name");
//		assertEquals(getGroupName, groupNameResponse);
//	}
//	
//	@Test
//	public void c_getMyGroupsTest() throws Exception {
//		System.out.println("Running Test: getMyGroups");
//		int myGroupsCount = TestConfig.GET_MY_GROUPS_TOTAL;
//		JSONArray myGroups = client.getMyGroups(myGroupsCount+20, 0);
//		assertEquals(myGroupsCount, myGroups.length());
//	}
//	
//	@Test
//	public void d_getAllGroupsTest() throws Exception {
//		System.out.println("Running Test: getAllGroups");
//		int allGroupsCount = TestConfig.GET_ALL_GROUPS_TOTAL;
//		JSONArray allGroups = client.getAllGroups(allGroupsCount+20, 0);
//		assertEquals(allGroupsCount, allGroups.length());
//	}
//	
//	@Test
//	public void e_getGroupMembersTest() throws Exception {
//		System.out.println("Running Test: postGroupMembers");
//		System.out.println(client.getGroupMembers(getGroupName, null, null));
//	}
	
//	@AfterClass
//	public static void deleteGroupTest() throws Exception {
//		JSONObject response = client.deleteGroup(groupName2, null);
//		assertEquals(200, response.getInt("status"));
//	}
}