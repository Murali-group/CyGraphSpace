package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.graphspace.javaclient.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class GraphsTest {
	
	/**
     * ============================================
     * GET GRAPH TESTS
     * ============================================
     */
	
//	@Test
//	public void getGraphByNameTest() throws Exception {
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		String graphName = TestConfig.GET_GRAPH_NAME;
//		Client client = new Client(host, username, password);
//		String graphNameResponse = client.getGraphByName(graphName, username).getString("name");
//		assertEquals(graphName, graphNameResponse);
//	}
//	
//	@Test
//	public void getGraphByIdTest() throws Exception {
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		int graphId = TestConfig.GET_GRAPH_ID;
//		Client client = new Client(host, username, password);
//		int graphIdResponse = client.getGraphById(graphId).getJSONObject("body").getJSONArray("array").getJSONObject(0).getInt("id");
//		assertEquals(graphId, graphIdResponse);
//	}
//	
//	@Test
//	public void getMyGraphsTest() throws Exception {
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		int totalMyGraphs = TestConfig.GET_MY_GRAPHS_TOTAL;
//		Client client = new Client(host, username, password);
//		JSONObject graphObject;
//		graphObject = client.getMyGraphs(null, null, totalMyGraphs+20, 0);
//		JSONObject body = graphObject.getJSONObject("body");
//		JSONArray array = body.getJSONArray("array");
//		int totalMyGraphsResponse = ((JSONObject) array.get(0)).getInt("total");
//		assertEquals(totalMyGraphs, totalMyGraphsResponse);
//	}
//	
//	@Test
//	public void getSharedGraphsTest() throws Exception {
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		int totalSharedGraphs = TestConfig.GET_SHARED_GRAPHS_TOTAL;
//		Client client = new Client(host, username, password);
//		JSONObject graphObject;
//		graphObject = client.getSharedGraphs(null, null, totalSharedGraphs+20, 0);
//		JSONObject body = graphObject.getJSONObject("body");
//		JSONArray array = body.getJSONArray("array");
//		int totalSharedGraphsResponse = ((JSONObject) array.get(0)).getInt("total");
//		assertEquals(totalSharedGraphs, totalSharedGraphsResponse);
//	}
//	
//	@Test
//	public void getPublicGraphsTest() throws Exception {
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		int totalPublicGraphs = TestConfig.GET_PUBLIC_GRAPHS_TOTAL;
//		Client client = new Client(host, username, password);
//		JSONObject graphObject;
//		graphObject = client.getPublicGraphs(null, null, totalPublicGraphs+20, 0);
//		JSONObject body = graphObject.getJSONObject("body");
//		JSONArray array = body.getJSONArray("array");
//		int totalPublicGraphsResponse = ((JSONObject) array.get(0)).getInt("total");
//		assertEquals(totalPublicGraphs, totalPublicGraphsResponse);
//	}
//	
//	@Test
//	public void postGraphTest() throws Exception{
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		String fileName = TestConfig.POST_GRAPH_FILENAME;
//		String styleFileName = TestConfig.POST_GRAPH_STYLE_FILENAME;
//		Client client = new Client(host, username, password);
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource(fileName).getFile());
//		InputStream is = new FileInputStream(file);
//		String graphJsonText = IOUtils.toString(is);
//		JSONObject graphJson = new JSONObject(graphJsonText);
//		file = new File(classLoader.getResource(styleFileName).getFile());
//		is = new FileInputStream(file);
//		String styleJsonText = IOUtils.toString(is);
//		JSONArray styleJSONArray = new JSONArray(styleJsonText);
//		JSONObject styleJson = styleJSONArray.getJSONObject(0);
//		JSONObject response = client.postGraph(graphJson, styleJson, false, null);
//		assertEquals(201, response.getInt("status"));
//	}
//	
//	@Test
//	public void deleteGraphTest() throws Exception{
//		String host = TestConfig.HOST;
//		String username = TestConfig.USERNAME;
//		String password = TestConfig.PASSWORD;
//		String fileName = TestConfig.POST_GRAPH_FILENAME;
//		Client client = new Client(host, username, password);
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource(fileName).getFile());
//		InputStream is = new FileInputStream(file);
//		String graphJsonText = IOUtils.toString(is);
//		JSONObject graphJson = new JSONObject(graphJsonText);
//		JSONObject data = graphJson.getJSONObject("data");
//		String graphName = data.getString("name");
//		JSONObject response = client.deleteGraph(null, graphName);
//		if (response.has("message")) {
//			System.out.println(response.getString("message"));
//		}
//		else {
//			System.out.println(response.toString());
//		}
//	}
}