package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.graphspace.javaclient.GraphSpaceClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LayoutsTest{
	private static String host;
	private static String username;
	private static String password;
	private static String graphFileName;
	private static String styleFileName;
	private static String positionFileName;
	private static GraphSpaceClient client;
	private static String graphName;
	private static String layoutName;
	private static JSONObject graphJson;
	private static JSONObject styleJson;
	private static JSONObject positionJson;
	private static int layoutId;
	
	@BeforeClass
	public static void prepareTests() throws IOException {
		host = TestConfig.HOST;
		username = TestConfig.USERNAME;
		password = TestConfig.PASSWORD;
		graphFileName = TestConfig.POST_GRAPH_FILENAME;
		styleFileName = TestConfig.POST_GRAPH_STYLE_FILENAME;
		positionFileName = TestConfig.POST_LAYOUT_POSITION_FILENAME;
		
		layoutName = TestConfig.POST_LAYOUT_NAME;
		
		client = new GraphSpaceClient(host, username, password);
		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		
		File file = new File(classLoader.getResource(graphFileName).getFile());
		InputStream is = new FileInputStream(file);
		String graphJsonText = IOUtils.toString(is);
		graphJson = new JSONObject(graphJsonText);
		
		file = new File(classLoader.getResource(styleFileName).getFile());
		is = new FileInputStream(file);
		String styleJsonText = IOUtils.toString(is);
		styleJson = new JSONObject(styleJsonText);
		
		file = new File(classLoader.getResource(positionFileName).getFile());
		is = new FileInputStream(file);
		String positionJsonText = IOUtils.toString(is);
		positionJson = new JSONObject(positionJsonText);
		
		graphName = graphJson.getJSONObject("data").getString("name");
	}
	
	@Test
	public void a_postGraphTest() throws Exception{
		System.out.println("Running Test: postGraph");
		JSONObject response = client.postGraph(graphJson, null, false, null);
		assertEquals(201, response.getInt("status"));
	}
	
	@Test
	public void b_postLayoutTest() throws Exception {
		System.out.println("Running Test: postLayout");
		int graphId = client.getGraphByName(graphName, username).getInt("id");
		JSONObject response = client.postGraphLayout(graphId, layoutName, positionJson, styleJson, false);
		assertEquals(201, response.getInt("status"));
	}
	
	@Test
	public void c_getMyLayoutsTest() throws Exception {
		System.out.println("Running Test: getMyLayouts");
		int graphId = client.getGraphByName(graphName, username).getInt("id");
		JSONObject response = client.getMyGraphLayouts(graphId, 100, 0);
		assertEquals(200, response.getInt("status"));
		layoutId = response.getJSONObject("body").getJSONObject("object").getJSONArray("layouts").getJSONObject(0).getInt("id");
	}
	
	@Test
	public void d_getSharedLayoutsTest() throws Exception {
		System.out.println("Running Test: getSharedLayouts");
		int graphId = client.getGraphByName(graphName, username).getInt("id");
		JSONObject response = client.getSharedGraphLayouts(graphId, 100, 0);
		assertEquals(200, response.getInt("status"));
	}
	
	@Test
	public void e_getLayoutTest() throws Exception {
		System.out.println("Running Test: getLayout");
		int graphId = client.getGraphByName(graphName, username).getInt("id");
		JSONObject response = client.getGraphLayout(graphId, layoutId, username);
		assertEquals(200, response.getInt("status"));
	}
	
	@Test
	public void f_deleteLayoutTest() throws Exception {
		System.out.println("Running Test: deleteLayout");
		int graphId = client.getGraphByName(graphName, username).getInt("id");
		JSONObject response = client.deleteGraphLayout(graphId, layoutId);
		assertEquals(200, response.getInt("status"));
	}
	
	@AfterClass
	public static void deleteGraphTest() throws Exception {
		System.out.println("Running Test: deleteGraph");
		JSONObject response = client.deleteGraph(null, graphName);
		assertEquals(200, response.getInt("status"));
	}
}