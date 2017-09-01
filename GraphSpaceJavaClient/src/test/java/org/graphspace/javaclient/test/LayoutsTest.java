package org.graphspace.javaclient.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.graphspace.javaclient.GraphSpaceClient;
import org.graphspace.javaclient.Layout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LayoutsTest{
//	private static String host;
//	private static String username;
//	private static String password;
//	private static String graphFileName;
//	private static String styleFileName;
//	private static String positionFileName;
//	private static GraphSpaceClient client;
//	private static String graphName;
//	private static String layoutName;
//	private static JSONObject graphJson;
//	private static JSONObject styleJson;
//	private static JSONObject positionJson;
//	private static int layoutId;
//	private static int numberOfMyLayouts;
//	private static int numberOfSharedLayouts;
//	
//	@BeforeClass
//	public static void prepareTests() throws IOException {
//		host = TestConfig.HOST;
//		username = TestConfig.USERNAME;
//		password = TestConfig.PASSWORD;
//		graphFileName = TestConfig.POST_GRAPH_FILENAME;
//		styleFileName = TestConfig.POST_GRAPH_STYLE_FILENAME;
//		positionFileName = TestConfig.POST_LAYOUT_POSITION_FILENAME;
//		
//		layoutName = TestConfig.POST_LAYOUT_NAME;
//		
//		client = new GraphSpaceClient(host, username, password);
//		
//		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
//		
//		File file = new File(classLoader.getResource(graphFileName).getFile());
//		InputStream is = new FileInputStream(file);
//		String graphJsonText = IOUtils.toString(is);
//		graphJson = new JSONObject(graphJsonText);
//		
//		file = new File(classLoader.getResource(styleFileName).getFile());
//		is = new FileInputStream(file);
//		String styleJsonText = IOUtils.toString(is);
//		styleJson = new JSONObject(styleJsonText);
//		
//		file = new File(classLoader.getResource(positionFileName).getFile());
//		is = new FileInputStream(file);
//		String positionJsonText = IOUtils.toString(is);
//		positionJson = new JSONObject(positionJsonText);
//		
//		graphName = graphJson.getJSONObject("data").getString("name");
//	}
//	
//	@Test
//	public void b_postGraphTest() throws Exception{
//		System.out.println("Running Test: postGraph");
//		System.out.println(client.postGraph(graphJson, styleJson, false, null));
//	}
//	
//	@Test
//	public void c_postLayoutTest() throws Exception {
//		System.out.println("Running Test: postLayout");
//		int graphId = client.getGraph(graphName, username).getId();
//		System.out.println(client.postGraphLayout(graphId, layoutName, positionJson, styleJson, false));
//	}
//	
//	@Test
//	public void d_getMyLayoutsTest() throws Exception {
//		System.out.println("Running Test: getMyLayouts");
//		int graphId = client.getGraph(graphName, username).getId();
//		ArrayList<Layout> layouts = client.getMyGraphLayouts(graphId, Integer.MAX_VALUE, 0);
//		layoutId = layouts.get(0).getId();
//		int numberOfMyLayoutsFinal = layouts.size();
//		System.out.println("Total Number of personal layouts: " + numberOfMyLayoutsFinal);
//		assertEquals(numberOfMyLayouts+1, numberOfMyLayoutsFinal);
//	}
//	
//	@Test
//	public void e_getLayoutTest() throws Exception {
//		System.out.println("Running Test: getLayout");
//		int graphId = client.getGraph(graphName, username).getId();
//		System.out.println(client.getGraphLayout(graphId, layoutId).getStyleJson());
//	}
//	
//	@Test
//	public void f_getSharedLayoutsTest() throws Exception {
//		System.out.println("Running Test: getSharedLayouts");
//		int graphId = client.getGraph(graphName, username).getId();
//		ArrayList<Layout> layouts = client.getSharedGraphLayouts(graphId, Integer.MAX_VALUE, 0);
//		int numberOfSharedLayouts = layouts.size();
//		System.out.println("Total Number of personal layouts: " + numberOfSharedLayouts);
//	}
//	
//	@Test
//	public void g_deleteLayoutTest() throws Exception {
//		System.out.println("Running Test: deleteLayout");
//		int graphId = client.getGraph(graphName, username).getId();
//		System.out.println(client.deleteGraphLayout(graphId, layoutId));
//	}
//	
//	@AfterClass
//	public static void deleteGraphTest() throws Exception {
//		System.out.println("Running Test: deleteGraph");
//		System.out.println(client.deleteGraph(graphName));
//	}
}