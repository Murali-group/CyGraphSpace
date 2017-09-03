package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.PopupMenuEvent;

import org.cytoscape.application.swing.AbstractToolBarComponent;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;
import org.graphspace.javaclient.Graph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

/**
 * Toolbar component on Cytoscape toolbar to post and update graphs to GraphSpace
 * @author rishabh
 *
 */
public class PostGraphToolBarComponent extends AbstractToolBarComponent implements CyAction{
	
	//UI elements
	private JButton button;
	private JFrame loadingFrame;
	
	public PostGraphToolBarComponent(){
		super();
		button = new JButton();
		
		//imageicon used as for the toolbar menu
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("graphspaceicon.png"));
		button.setIcon(icon);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.setToolTipText("Export To GraphSpace"); //set tooltip to notify users about the functionality of the button
		
		//action attached to the button
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){

				JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();
		        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
		        
		        //loading frame while checking if updating the graph is possible
		        loadingFrame = new JFrame("Checking if update Possible");
				ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
				JLabel loadingLabel = new JLabel("", loading, JLabel.CENTER);
				loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				loadingFrame.setSize(400, 300);
				loadingFrame.add(loadingLabel);
				loadingFrame.setLocationRelativeTo(parent);
				
				//if there is no network to export, display an error
		        if( currentNetwork == null ){
		            String msg = "There is no graph to export.";
		            String dialogTitle = "No Graph Found";
		            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
		            return;
		        }
		        
		        //if there is a network and the user is currently authenticated, create a post graph dialog
		        if (Server.INSTANCE.isAuthenticated()){
					loadingFrame.setVisible(true);
		    		try {
						populate(parent);
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
								populate(parent);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		            	}
		            });
		        }
			}
		});
	}
	
	//populate the values in the post graph dialog
   private void populate(Frame parent) throws Exception{
		JSONObject graphJSON = exportNetworkToJSON();
		JSONObject styleJSON = exportStyleToJSON();
		String graphName = graphJSON.getJSONObject("data").getString("name");
		boolean isGraphPublic = false;
		
		//if updating the graph is possible, open the update graph dialog.
		if(Server.INSTANCE.updatePossible(graphName)){
			loadingFrame.dispose();
			Graph graph = Server.INSTANCE.getGraphByName(graphName);
			isGraphPublic = graph.isPublic();
			UpdateGraphDialog updateDialog = new UpdateGraphDialog(parent, graphName, graphJSON, styleJSON, isGraphPublic, null);
			updateDialog.setLocationRelativeTo(parent);
			updateDialog.setVisible(true);
		}
		
		//if updating the graph is not possible, open the post graph dialog
		else{
			loadingFrame.dispose();
			PostGraphDialog postDialog = new PostGraphDialog(parent, graphName, graphJSON, styleJSON, isGraphPublic, null);
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
		
	/** Returns an ImageIcon, or null if the path was invalid. */

	@Override
	public Component getComponent() {
		//returns the button component to be visible on the toolbar
		return button;
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public KeyStroke getAcceleratorKeyStroke() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public float getMenuGravity() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPreferredMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public float getToolbarGravity() {
//		// TODO Auto-generated method stub
//		return 1;
//	}
	@Override
	public boolean insertSeparatorAfter() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean insertSeparatorBefore() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isInMenuBar() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isInToolBar() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void updateEnableState() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean useCheckBoxMenuItem() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public float getToolbarGravity() {
		// TODO Auto-generated method stub
		return 0;
	}
}