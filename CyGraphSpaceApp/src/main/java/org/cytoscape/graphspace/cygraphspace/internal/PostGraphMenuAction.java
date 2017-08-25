package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

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

public class PostGraphMenuAction extends AbstractCyAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame loadingFrame;
	public PostGraphMenuAction(String menuTitle, CyApplicationManager applicationManager) {
        super(menuTitle, applicationManager, null, null);
        // We want this menu item to appear under the File|Export menu.
        setPreferredMenu("File.Export");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();

        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        
        JFrame loadingFrame = new JFrame("Checking if update Possible");
		ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
		JLabel loadingLabel = new JLabel("", loading, JLabel.CENTER);
		loadingFrame.add(loadingLabel);
	    loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setSize(400, 300);
		loadingFrame.setLocationRelativeTo(parent);
        
		if(currentNetwork == null){
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }
        
		if (Server.INSTANCE.isAuthenticated()){
			loadingFrame.setVisible(true);
    		try {
				populate(parent, loadingFrame);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        
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
    
    private void populate(Frame parent, JFrame loadingFrame) throws Exception{
		JSONObject graphJSON = exportNetworkToJSON();
		JSONObject styleJSON = exportStyleToJSON();
		String graphName = graphJSON.getJSONObject("data").getString("name");
		boolean isGraphPublic = false;
		if(Server.INSTANCE.updatePossible(graphName)){
			loadingFrame.dispose();
			Graph graph = Server.INSTANCE.getGraphByName(graphName);
			isGraphPublic = graph.isPublic();
			UpdateGraphDialog updateDialog = new UpdateGraphDialog(parent, graphName, graphJSON, styleJSON, isGraphPublic, null);
			updateDialog.setLocationRelativeTo(parent);
			updateDialog.setVisible(true);
		}
		else{
			loadingFrame.dispose();
			PostGraphDialog postDialog = new PostGraphDialog(parent, graphName, graphJSON, styleJSON, isGraphPublic, null);
		    postDialog.setLocationRelativeTo(parent);
		    postDialog.setVisible(true);
		}
    }
    
    private JSONObject exportNetworkToJSON() throws IOException{
		File tempFile = File.createTempFile("CyGraphSpaceExport", ".cyjs");
		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
		CyNetworkView networkView = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetworkView();
		TaskIterator ti = CyObjectManager.INSTANCE.getExportNetworkTaskFactory().createTaskIterator(network, tempFile);
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
		String graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
		int count = 0;
		while(graphJSONString.isEmpty()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
			count++;
			if (count>=10){
				return null;
			}
		}
		tempFile.delete();
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"source_original\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"target_original\".*", "");
		JSONObject graphJSON = new JSONObject(graphJSONString);
        return graphJSON;
	}
	
	private JSONObject exportStyleToJSON() throws IOException{
		File tempFile = File.createTempFile("CyGraphSpaceStyleExport", ".json");
		TaskIterator ti = CyObjectManager.INSTANCE.getExportVizmapTaskFactory().createTaskIterator(tempFile);
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
		String styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
		int count = 0;
		while(styleJSONString.isEmpty()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
			count++;
			if (count>=10){
				return null;
			}
		}
		tempFile.delete();
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"source_original\".*", "");
		styleJSONString = styleJSONString.replaceAll("(?m)^*.\"target_original\".*", "");
		JSONArray styleJSONArray = new JSONArray(styleJSONString);
        return styleJSONArray.getJSONObject(0);
	}
	
}
