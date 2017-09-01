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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraphsTest {
//	private static String host;
//	private static String username;
//	private static String password;
//	private static String graphFileName;
//	private static String styleFileName;
//	private static GraphSpaceClient client;
//	private static String graphName;
//	private static JSONObject graphJson;
//	private static JSONObject styleJson;
//	private static int numberOfPublicGraphs;
//	private static int numberOfMyGraphs;
//	private static int numberOfSharedGraphs;
//	
//	@BeforeClass
//	public static void prepareTests() throws IOException {
//		host = TestConfig.HOST;
//		username = TestConfig.USERNAME;
//		password = TestConfig.PASSWORD;
//		graphFileName = TestConfig.POST_GRAPH_FILENAME;
//		styleFileName = TestConfig.POST_GRAPH_STYLE_FILENAME;
//		client = new GraphSpaceClient(host, username, password);
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
//	public void a_getMyGraphsTest() throws Exception {
//		System.out.println("Running Test: getMyGraphs");
//		ArrayList<Graph> graphs = client.getMyGraphs(Integer.MAX_VALUE, 0);
//		numberOfMyGraphs = graphs.size();
//		System.out.println("Total Number of personal graphs initially: " + numberOfMyGraphs);
//	}
//	
//	@Test
//	public void b_getSharedGraphsTest() throws Exception {
//		System.out.println("Running Test: getSharedGraphs");
//		ArrayList<Graph> graphs = client.getSharedGraphs(Integer.MAX_VALUE, 0);
//		numberOfSharedGraphs = graphs.size();
//		System.out.println("Total Number of shared graphs initially: " + numberOfSharedGraphs);
//	}
//	
//	@Test
//	public void c_getPublicGraphsTest() throws Exception {
//		System.out.println("Running Test: getPublicGraphs");
//		ArrayList<Graph> graphs = client.getPublicGraphs(Integer.MAX_VALUE, 0);
//		numberOfPublicGraphs = graphs.size();
//		System.out.println("Total Number of public graphs initially: " + numberOfPublicGraphs);
//	}
//	
//	@Test
//	public void d_postGraphTest() throws Exception{
//		System.out.println("Running Test: postGraph");
//		System.out.println(client.postGraph(graphJson, styleJson, false, null));
//	}
//	
//	@Test
//	public void e_getMyGraphsTest() throws Exception {
//		System.out.println("Running Test: getMyGraphs");
//		ArrayList<Graph> graphs = client.getMyGraphs(Integer.MAX_VALUE, 0);
//		int numberOfMyGraphsFinal = graphs.size();
//		System.out.println("Number of graphs after posting one graph: " + numberOfMyGraphsFinal);
//		assertEquals(numberOfMyGraphs+1, numberOfMyGraphsFinal);
//	}
//	
//	@Test
//	public void f_getGraphByNameTest() throws Exception {
//		System.out.println("Running Test: getGraphByName");
//		System.out.println(client.getGraph(graphName, username).getGraphJson());
//	}
//	
//	@Test
//	public void g_getGraphByIdTest() throws Exception {
//		System.out.println("Running Test: getGraphById");
//		int graphId = client.getGraph(graphName, username).getId();
//		System.out.println(client.getGraph(graphId).getGraphJson());
//	}
//		
//	@AfterClass
//	public static void deleteGraphTest() throws Exception {
//		System.out.println("Running Test: deleteGraph");
//		System.out.println(client.deleteGraph(graphName));
//	}
}