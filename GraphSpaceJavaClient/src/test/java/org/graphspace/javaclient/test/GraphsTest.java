package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.graphspace.javaclient.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraphsTest {
	private static String host;
	private static String username;
	private static String password;
	private static String graphFileName;
	private static String styleFileName;
	private static Client client;
	private static String graphName;
	private static JSONObject graphJson;
	private static JSONObject styleJson;
	
	@BeforeClass
	public static void prepareTests() throws IOException {
		host = TestConfig.HOST;
		username = TestConfig.USERNAME;
		password = TestConfig.PASSWORD;
		graphFileName = TestConfig.POST_GRAPH_FILENAME;
		styleFileName = TestConfig.POST_GRAPH_STYLE_FILENAME;
		client = new Client(host, username, password);
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		File file = new File(classLoader.getResource(graphFileName).getFile());
		InputStream is = new FileInputStream(file);
		String graphJsonText = IOUtils.toString(is);
		graphJson = new JSONObject(graphJsonText);
		file = new File(classLoader.getResource(styleFileName).getFile());
		is = new FileInputStream(file);
		String styleJsonText = IOUtils.toString(is);
		JSONArray styleJSONArray = new JSONArray(styleJsonText);
		styleJson = styleJSONArray.getJSONObject(0);
		graphName = graphJson.getJSONObject("data").getString("name");
	}
	
	@Test
	public void a_postGraphTest() throws Exception{
		System.out.println("Running Test: postGraph");
		JSONObject response = client.postGraph(graphJson, styleJson, false, null);
		assertEquals(201, response.getInt("status"));
	}
	
	@Test
	public void b_getGraphByNameTest() throws Exception {
		System.out.println("Running Test: getGraphByName");
		String graphNameResponse = client.getGraphByName(graphName, username).getString("name");
		assertEquals(graphName, graphNameResponse);
	}
	
	@Test
	public void c_getGraphByIdTest() throws Exception {
		System.out.println("Running Test: getGraphById");
		int graphId = client.getGraphByName(graphName, username).getInt("id");
		int graphIdResponse = client.getGraphById(graphId).getJSONObject("body").getJSONArray("array").getJSONObject(0).getInt("id");
		assertEquals(graphId, graphIdResponse);
	}
	
	@Test
	public void d_getMyGraphsTest() throws Exception {
		System.out.println("Running Test: getMyGraphs");
		int totalMyGraphs = TestConfig.GET_MY_GRAPHS_TOTAL;
		JSONObject graphObject;
		graphObject = client.getMyGraphs(null, null, totalMyGraphs+20, 0);
		JSONObject body = graphObject.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int totalMyGraphsResponse = ((JSONObject) array.get(0)).getInt("total");
		assertEquals(totalMyGraphs+1, totalMyGraphsResponse);
	}
	
	@Test
	public void e_getSharedGraphsTest() throws Exception {
		System.out.println("Running Test: getSharedGraphs");
		int totalSharedGraphs = TestConfig.GET_SHARED_GRAPHS_TOTAL;
		JSONObject graphObject;
		graphObject = client.getSharedGraphs(null, null, totalSharedGraphs+20, 0);
		JSONObject body = graphObject.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int totalSharedGraphsResponse = ((JSONObject) array.get(0)).getInt("total");
		assertEquals(totalSharedGraphs, totalSharedGraphsResponse);
	}
	
	@Test
	public void f_getPublicGraphsTest() throws Exception {
		System.out.println("Running Test: getPublicGraphs");
		int totalPublicGraphs = TestConfig.GET_PUBLIC_GRAPHS_TOTAL;
		JSONObject graphObject;
		graphObject = client.getPublicGraphs(null, null, totalPublicGraphs+20, 0);
		JSONObject body = graphObject.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int totalPublicGraphsResponse = ((JSONObject) array.get(0)).getInt("total");
		assertEquals(totalPublicGraphs, totalPublicGraphsResponse);
	}
	
	
	@AfterClass
	public static void deleteGraphTest() throws Exception {
		System.out.println("Running Test: deleteGraph");
		JSONObject response = client.deleteGraph(null, graphName);
		assertEquals(200, response.getInt("status"));
	}
}