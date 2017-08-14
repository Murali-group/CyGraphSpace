package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.graphspace.javaclient.Client;
import org.graphspace.javaclient.model.GSGroup;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupsTest{
	
	private static String host;
	private static String username;
	private static String password;
	private static Client client;
	private static String groupName;
	private static String groupName2;
	private static String groupDescription;
	
	@BeforeClass
	public static void prepareTests() throws IOException {
		host = TestConfig.HOST;
		username = TestConfig.USERNAME;
		password = TestConfig.PASSWORD;
		client = new Client(host, username, password);
		groupName = "testgroup";
		groupName2 = "GraphSpaceJavaClientTestGroup";
		groupDescription = "Test group to test GraphSpace java client library";
	}
	
	@Test
	public void a_postGroupTest() throws Exception {
		GSGroup group = new GSGroup();
		group.setName(groupName2);
		group.setDescription(groupDescription);
		JSONObject response = client.postGroup(group);
		System.out.println(response.toString());
		assertEquals(200, response.getInt("status"));
	}
	
	@Test
	public void b_getGroupTest() throws Exception {
		String groupNameResponse = client.getGroup(groupName).getString("name");
		assertEquals(groupName, groupNameResponse);
	}
	
	@Test
	public void c_getMyGroupsTest() throws Exception {
		int myGroupsCount = TestConfig.GET_MY_GROUPS_TOTAL;
		JSONArray myGroups = client.getMyGroups(myGroupsCount+20, 0);
		assertEquals(myGroupsCount, myGroups.length());
	}
	
	@Test
	public void d_getAllGroupsTest() throws Exception {
		int allGroupsCount = TestConfig.GET_ALL_GROUPS_TOTAL;
		JSONArray allGroups = client.getAllGroups(allGroupsCount+20, 0);
		assertEquals(allGroupsCount, allGroups.length());
	}
	
	@Test
	public void e_getGroupMembersTest() throws Exception {
		System.out.println(client.getGroupMembers(groupName, null, null));
	}
	
	@AfterClass
	public static void deleteGroupTest() throws Exception {
		JSONObject response = client.deleteGroup(groupName2, null);
		assertEquals(200, response.getInt("status"));
	}
}