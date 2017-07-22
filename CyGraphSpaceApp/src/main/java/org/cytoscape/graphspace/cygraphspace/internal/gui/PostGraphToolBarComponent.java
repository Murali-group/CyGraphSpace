package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
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
import org.cytoscape.application.swing.ToolBarComponent;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.cytoscape.application.swing.AbstractCyAction;

public class PostGraphToolBarComponent extends AbstractToolBarComponent implements CyAction{
	private JButton button;
	private JFrame loadingFrame;
	public PostGraphToolBarComponent(){
		super();
//		ImageIcon icon = createImageIcon("/graphspace.png", "Graphspace");
//		this.setIcon(icon);
//		this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("graphspace-icon.png")));
		button = new JButton();
//		button.setText("GraphSpace");
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("graphspaceicon.png"));
		button.setIcon(icon);
//		button.setPreferredSize(new Dimension(10,10));
//		button.setMargin(new Insets(0, 0, 0, 0));
//		button.setBorder(null);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.setToolTipText("Export To GraphSpace");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){

				JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();

		        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
		        
		        loadingFrame = new JFrame("Checking if update Possible");
				ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
				JLabel loadingLabel = new JLabel("Checking if you're trying to update an existing graph", loading, JLabel.CENTER);
				loadingLabel.setHorizontalTextPosition(JLabel.CENTER);
				loadingLabel.setVerticalTextPosition(JLabel.BOTTOM);
				loadingFrame.add(loadingLabel);
				loadingFrame.setSize(400, 300);
		        if( currentNetwork == null )
		        {
		            String msg = "There is no graph to export.";
		            String dialogTitle = "No Graph Found";
		            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
		            return;
		        }
		        if (Server.INSTANCE.isAuthenticated()){
					loadingFrame.setVisible(true);
		    		try {
						populate(parent);
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
	
   private void populate(Frame parent) throws Exception{
		JSONObject graphJSON = exportNetworkToJSON();
		JSONObject styleJSON = exportStyleToJSON();
		String graphName = graphJSON.getJSONObject("data").getString("name");
		System.out.println(graphName);
		boolean isGraphPublic = false;
		if(Server.INSTANCE.updatePossible(graphName)){
			loadingFrame.dispose();
			JSONObject responseFromGraphSpace = Server.INSTANCE.client.getGraphByName(graphName);
			int isPublic = responseFromGraphSpace.getInt("is_public");
			if (isPublic==1){
				isGraphPublic = true;
			}
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
		
	/** Returns an ImageIcon, or null if the path was invalid. */

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
//		return null;
		return button;
	}
//	@Override
//	public float getToolBarGravity() {
//		// TODO Auto-generated method stub
//		return 1;
//	}
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