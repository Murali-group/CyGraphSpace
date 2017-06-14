package org.graphspace.javaclient.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.graphspace.javaclient.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class Tests{

	@Test
	public void getMyGraphsTest() throws Exception {
		Client client = new Client();
		JSONObject graphObject;
		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
		graphObject = client.getMyGraphs(new ArrayList<String>(), 20, 0);
		JSONObject body = graphObject.getJSONObject("body");
		JSONArray array = body.getJSONArray("array");
		int total = ((JSONObject) array.get(0)).getInt("total");
		assertEquals(2, total);
	}
	
	@Test
	public void getGraphTest() throws Exception {
		String name = "network1";
		Client client = new Client();
		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
		String graphName = client.getGraph(name).getString("name");
		assertEquals(name, graphName);
	}
	
	@Test
	public void getGraphByIdTest() throws Exception {
		String graphId = "21474";
		Client client = new Client();
		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
		int id = client.getGraphById(graphId).getJSONObject("body").getJSONArray("array").getJSONObject(0).getInt("id");
		assertEquals(id, Integer.parseInt(graphId));
	}
	
	@Test
	public void getPublicGraphsTest() throws Exception {
		Client client = new Client();
		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
		client.getPublicGraphs(1, 0);
	}
	
//	@Test
//	public void getGraphLayoutTest() throws Exception {
//		String graphId = "network1";
//		Client client = new Client();
//		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
//		String graphName = client.getGraph(name).getString("name");
//		assertEquals(name, graphName);
//	}
//	
//	@Test
//	public void getMyGraphLayoutsTest() throws Exception {
//		String name = "network1";
//		Client client = new Client();
//		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
//		String graphName = client.getGraph(name).getString("name");
//		assertEquals(name, graphName);
//	}
//	
//	@Test
//	public void getSharedGraphLayoutsTest() throws Exception {
//		String name = "network1";
//		Client client = new Client();
//		client.authenticate("rishu.sethi2525@gmail.com", "123456789");
//		String graphName = client.getGraph(name).getString("name");
//		assertEquals(name, graphName);
//	}

}