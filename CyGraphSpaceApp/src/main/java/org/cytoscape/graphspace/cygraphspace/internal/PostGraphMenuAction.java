package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;
import org.graphspace.javaclient.Graph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PostGraphDialog;
import org.cytoscape.graphspace.cygraphspace.internal.gui.UpdateGraphDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;

import javax.swing.*;

/**
 * This class defines the action performed on exporting a graph to GraphSpace from Cytoscape from the menu File>Export>Network To GraphSpace
 * @author rishabh
 *
 */
public class PostGraphMenuAction extends AbstractCyAction {
	
	private static final long serialVersionUID = 1L;
	
	public PostGraphMenuAction(String menuTitle, CyApplicationManager applicationManager) {
        
		super(menuTitle, applicationManager, null, null);
        
        // Menu under File>Export
        setPreferredMenu("File.Export");
    }
	
	//called when export menu is clicked by the user
    @Override
    public void actionPerformed(ActionEvent e) {
    	
        JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();

        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        
        //loading frame while checking if updating the graph is possible
        JFrame loadingFrame = new JFrame("Checking if update Possible");
		ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
		JLabel loadingLabel = new JLabel("", loading, JLabel.CENTER);
		loadingFrame.add(loadingLabel);
	    loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setSize(400, 300);
		loadingFrame.setLocationRelativeTo(parent);
        
		//if there is no network to export, display an error
		if(currentNetwork == null){
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }
        
		//if there is a network and the user is currently authenticated, create a post graph dialog
		if (Server.INSTANCE.isAuthenticated()){
			loadingFrame.setVisible(true);
    		try {
				populate(parent, loadingFrame);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        
		//if there is a network but the user is not authenticated, open the login dialog for the user to log in. Once logged in, open the post graph dialog
		else{
        	AuthenticationDialog dialog = new AuthenticationDialog(parent);
            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter(){
            	@Override
            	public void windowClosed(WindowEvent e){
					loadingFrame.setVisible(true);
            		try {
						populate(parent, loadingFrame);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            });
        }
    }
    
    //populate the values in the post graph dialog
    private void populate(Frame parent, JFrame loadingFrame) throws Exception{
		JSONObject graphJson = exportNetworkToJSON();
		JSONObject styleJson = exportStyleToJSON();
		String graphName = graphJson.getJSONObject("data").getString("name");
		boolean isGraphPublic = false;
		
		//if updating the graph is possible, open the update graph dialog.
		if(Server.INSTANCE.updatePossible(graphName)){
			loadingFrame.dispose();
			Graph graph = Server.INSTANCE.getGraphByName(graphName);
			isGraphPublic = graph.isPublic();
			UpdateGraphDialog updateDialog = new UpdateGraphDialog(parent, graphName, graphJson, styleJson, isGraphPublic, null);
			updateDialog.setLocationRelativeTo(parent);
			updateDialog.setVisible(true);
		}
		
		//if updating the graph is not possible, open the post graph dialog
		else{
			loadingFrame.dispose();
			PostGraphDialog postDialog = new PostGraphDialog(parent, graphName, graphJson, styleJson, isGraphPublic, null);
		    postDialog.setLocationRelativeTo(parent);
		    postDialog.setVisible(true);
		}
    }
    
    //Utility method to export the current network to a json object to be exported to GraphSpace
    private JSONObject exportNetworkToJSON() throws IOException{
    	
    	//create a temporary json file for the network of cyjs format
		File tempFile = File.createTempFile("CyGraphSpaceExport", ".cyjs");
		
		//read the network
		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
		
		//export the network to the temporary cyjs file
		TaskIterator ti = CyObjectManager.INSTANCE.getExportNetworkTaskFactory().createTaskIterator(network, tempFile);
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
		
		//read the file contents to a string
		String graphJsonString = FileUtils.readFileToString(tempFile, "UTF-8");
		
		//ugly way to wait for the parallel process of reading to be completed
		int count = 0;
		while(graphJsonString.isEmpty()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			graphJsonString = FileUtils.readFileToString(tempFile, "UTF-8");
			count++;
			if (count>=10){
				return null;
			}
		}
		
		//delete the temporary file
		tempFile.delete();
		
		/**
		 * There is a design flaw in Cytoscape that results in a few parameters to be added to the graphs automatically on import.
		 * This is because cytoscape doesn't restrict apps to use any arbitary parameters to save network data, hence, they add
		 * attributes on importing the graph that was earlier created using GraphSpace and instead of replacing the values for conflicting attributes,
		 * those values are stored under different attribute names.
		 * This creates duplicate attribute values which results in an error while exporting the String to a json object.
		 * Hence, the attributes which are not required by GraphSpace and Cytoscape are deleted before exporting to resolve this conflict.
		 * This might be problematic in case the user needs to use the graph with other apps which uses conflicting attributes.
		 */
		graphJsonString = graphJsonString.replaceAll("(?m)^*.\"shared_name\".*", "");
		graphJsonString = graphJsonString.replaceAll("(?m)^*.\"id_original\".*", "");
		graphJsonString = graphJsonString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
		graphJsonString = graphJsonString.replaceAll("(?m)^*.\"source_original\".*", "");
		graphJsonString = graphJsonString.replaceAll("(?m)^*.\"target_original\".*", "");
		
		//graph string converted to graphJson
		JSONObject graphJson = new JSONObject(graphJsonString);
		
		JSONObject elements = graphJson.getJSONObject("elements");
		JSONArray nodes = elements.getJSONArray("nodes");
		JSONArray edges = elements.getJSONArray("edges");
		
		Map<String, String> nodeId2Name = new HashMap<String, String>();
		Map<String, String> edgeId2Name = new HashMap<String, String>();
		
		for(int i=0; i<nodes.length(); i++) {
			JSONObject node = nodes.getJSONObject(i);
			String id = node.getJSONObject("data").getString("id");
			String name = node.getJSONObject("data").getString("name");
			nodeId2Name.put(id, name);
			node.getJSONObject("data").put("id", (String)nodeId2Name.get(id));
		}
		
		for(int i=0; i<edges.length(); i++) {
			JSONObject edge = edges.getJSONObject(i);
			String id = edge.getJSONObject("data").getString("id");
			String name = edge.getJSONObject("data").getString("name");
			edgeId2Name.put(id, name);
			edge.getJSONObject("data").put("source", nodeId2Name.get(edge.getJSONObject("data").getString("source")));
			edge.getJSONObject("data").put("target", nodeId2Name.get(edge.getJSONObject("data").getString("target")));
		}
        return graphJson;
	}
    
    //Utility method to export the style of the current network to a json object to be exported to GraphSpace
	private JSONObject exportStyleToJSON() throws IOException{
		
		//create a temporary json file for the network of json format
		File tempFile = File.createTempFile("CyGraphSpaceStyleExport", ".json");
		
		//export the style json to the temporary json file
		TaskIterator ti = CyObjectManager.INSTANCE.getExportVizmapTaskFactory().createTaskIterator(tempFile);
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
		
		//read the file contents to a string
		String styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
		
		//ugly way to wait for the parallel process of reading to be completed
		int count = 0;
		while(styleJSONString.isEmpty()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
			count++;
			if (count>=10){
				return null;
			}
		}
		
		//delete the temporary files
		tempFile.delete();
		
		/**
		 * There is a design flaw in Cytoscape that results in a few parameters to be added to the graphs automatically on import.
		 * This is because cytoscape doesn't restrict apps to use any arbitary parameters to save network data, hence, they add
		 * attributes on importing the graph that was earlier created using GraphSpace and instead of replacing the values for conflicting attributes,
		 * those values are stored under different attribute names.
		 * This creates duplicate attribute values which results in an error while exporting the String to a json object.
		 * Hence, the attributes which are not required by GraphSpace and Cytoscape are deleted before exporting to resolve this conflict.
		 * This might be problematic in case the user needs to use the graph with other apps which uses conflicting attributes.
		 */
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"source_original\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"target_original\".*", "");
		
		//style string converted to graphJson
		JSONArray styleJSONArray = new JSONArray(styleJSONString);
        return styleJSONArray.getJSONObject(0);
	}
	
}
