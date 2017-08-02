package org.cytoscape.graphspace.cygraphspace.internal.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;
import org.graphspace.javaclient.model.GSGroupMetaData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JComboBox;

public class PostGraphDialog2 extends JDialog {
	private JLabel hostValueLabel;
	private JLabel usernameValueLabel;
	private JLabel usernameLabel;
	private JPanel buttonsPanel;
	private JButton postGraphButton;
	private JButton cancelButton;
	private JLabel tagsLabel;
	private JTextField tagsTextField;
	private JLabel tagsMessageLabel;
	private JLabel groupsLabel;
	private JCheckBox makeGraphPublicCheckBox;
	private JComboBox groupsComboBox;
	public PostGraphDialog2(Frame parent) {
		this.setTitle("Export Graphs to GraphSpace");
		
		JLabel hostLabel = new JLabel("Host");
		
		hostValueLabel = new JLabel("www.graphspace.org");
		
		usernameValueLabel = new JLabel("Anonymous");
		
		
		usernameLabel = new JLabel("Username");
		
		buttonsPanel = new JPanel();
		
		JPanel tagsPanel = new JPanel();
		
		makeGraphPublicCheckBox = new JCheckBox("Make Graph Public");
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(makeGraphPublicCheckBox)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(hostLabel)
								.addComponent(usernameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(48)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(usernameValueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(hostValueLabel, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)))
						.addComponent(buttonsPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
								.addComponent(tagsPanel, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE))
							.addGap(10)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(hostValueLabel)
						.addComponent(hostLabel))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(usernameLabel)
						.addComponent(usernameValueLabel))
					.addGap(18)
					.addComponent(tagsPanel, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(makeGraphPublicCheckBox)
					.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		groupsLabel = new JLabel("Share With");
		
		groupsComboBox = new JComboBox();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(groupsLabel, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(groupsComboBox, 0, 450, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(groupsLabel)
						.addComponent(groupsComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		tagsLabel = new JLabel("Tags");
		
		tagsTextField = new JTextField();
		tagsTextField.setColumns(10);
		
		tagsMessageLabel = new JLabel("For multiple tags, separate the tags using commas");
		GroupLayout gl_tagsPanel = new GroupLayout(tagsPanel);
		gl_tagsPanel.setHorizontalGroup(
			gl_tagsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_tagsPanel.createSequentialGroup()
					.addComponent(tagsLabel, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addGap(71)
					.addGroup(gl_tagsPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(tagsMessageLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
						.addComponent(tagsTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_tagsPanel.setVerticalGroup(
			gl_tagsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tagsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tagsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tagsLabel)
						.addComponent(tagsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tagsMessageLabel)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		tagsPanel.setLayout(gl_tagsPanel);
		
		postGraphButton = new JButton("Checking...");
		postGraphButton.setEnabled(false);
		buttonsPanel.add(postGraphButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelActionPerformed(e);
			}
		});
		buttonsPanel.add(cancelButton);
		getContentPane().setLayout(groupLayout);
		populateFields();
		check();
		pack();

	}

	public void check(){
		try {
			System.out.println("host: "+Server.INSTANCE.getHost() + ", username: "+Server.INSTANCE.getUsername()+ ",password: "+Server.INSTANCE.getPassword());
			JSONObject graphJSON = exportNetworkToJSON();
			JSONObject styleJSON = exportStyleToJSON();
//			if (graphJSON == null){
//				this.dispose();
//				JOptionPane.showMessageDialog(new JFrame(), "Could not export your network to JSON.", "Dialog", JOptionPane.ERROR_MESSAGE);
//			}
			String graphName = graphJSON.getJSONObject("data").getString("name");
			System.out.println(graphName);
			if(Server.INSTANCE.updatePossible(graphName)){
				postGraphButton.setText("Update");
				postGraphButton.setEnabled(true);
				JSONObject responseFromGraphSpace = Server.INSTANCE.client.getGraphByName(graphName);
				int isPublic = responseFromGraphSpace.getInt("is_public");
				if (isPublic == 1){
					makeGraphPublicCheckBox.setSelected(true);
				}
				JSONArray tagsArr = responseFromGraphSpace.getJSONArray("tags");
				String tagsListText = "";
				for (int i=0; i<tagsArr.length(); i++){
					tagsListText += tagsArr.getString(i)+", ";
				}
				if (tagsListText.length()>2){
					tagsListText = tagsListText.substring(0, tagsListText.length()-2);
				}
				tagsTextField.setText(tagsListText);
				ArrayList<GSGroupMetaData> groups = Server.INSTANCE.getMyGroups(20, 0);
				for (GSGroupMetaData group: groups){
					groupsComboBox.addItem(group.getName());
				}
				postGraphButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						boolean isGraphPublic;
						ArrayList<String> tagsList;
						if (makeGraphPublicCheckBox.isSelected()){
							isGraphPublic = true;
						}
						else{
							isGraphPublic = false;
						}
						String tagsListText = tagsTextField.getText();
						tagsList = new ArrayList<String>(Arrays.asList(tagsListText.split("\\s*,\\s*")));
						System.out.println(tagsList.toString());
						String group = (String) groupsComboBox.getSelectedItem();
						updateActionPerformed(evt, graphJSON, styleJSON, isGraphPublic, tagsList, group);
					}
				});
			}
			else{
				postGraphButton.setText("Export");
				postGraphButton.setEnabled(true);
				ArrayList<GSGroupMetaData> groups = Server.INSTANCE.getMyGroups(20, 0);
				for (GSGroupMetaData group: groups){
					groupsComboBox.addItem(group.getName());
				}
//				postGraphButton.addActionListener(new ExportActionListener(graphJSON, styleJSON, isGraphPublic, tagsList));
				postGraphButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						boolean isGraphPublic;
						ArrayList<String> tagsList;
						if (makeGraphPublicCheckBox.isSelected()){
							isGraphPublic = true;
						}
						else{
							isGraphPublic = false;
						}
						String tagsListText = tagsTextField.getText();
						tagsList = new ArrayList<String>(Arrays.asList(tagsListText.split("\\s*,\\s*")));
						System.out.println(tagsList.toString());
						String group = (String) groupsComboBox.getSelectedItem();
						exportActionPerformed(evt, graphJSON, styleJSON, isGraphPublic, (ArrayList<String>)tagsList, group);
					}
				});
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dispose();
		}
	}
	
//	private boolean updatePossible(String graphName){
//		JSONObject responseJSON;
//		try {
//			
//			responseJSON = Server.INSTANCE.getGraphByName(graphName);
//			System.out.println(responseJSON);
//			if (responseJSON.getInt("status")==201 || responseJSON.getInt("status")==200){
//				return true;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	private void populateFields(){
		if (Server.INSTANCE.getUsername() != null){
			usernameValueLabel.setText(Server.INSTANCE.getUsername());
		}
		if (Server.INSTANCE.getHost() != null){
			hostValueLabel.setText(Server.INSTANCE.getHost());
		}
	}
	
	private void updateActionPerformed(ActionEvent evt, JSONObject graphJSON, JSONObject styleJSON, boolean isPublic, ArrayList<String> tagsList, String group){
		try{
			this.dispose();
			updateGraph(graphJSON, styleJSON, isPublic, tagsList);
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not update graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}
	
	private void exportActionPerformed(ActionEvent evt, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList, String group){
		try{
			this.dispose();
			postGraph(graphJSON, styleJSON, isGraphPublic, tagsList);
			String name = graphJSON.getJSONObject("data").getString("name");
			JSONObject graph = Server.INSTANCE.getGraphByName(name);
			System.out.println(graph.toString());
//			if (group!=null){
//				String name = graphJSON.getJSONObject("data").getString("name");
//				JSONObject graph = Server.INSTANCE.getGraphByName(name);
//				System.out.println(graph.toString());
////				Server.INSTANCE.addGroupGraph(null, group, )
//			}
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not post graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}
	
	private void updateGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isPublic, ArrayList<String> tagsList) throws Exception{
		String name = graphJSON.getJSONObject("data").getString("name");
		JSONObject responseFromGraphSpace = Server.INSTANCE.client.getGraphByName(name);
		Server.INSTANCE.updateGraph(name, graphJSON, isPublic, tagsList);
//		System.out.println(responseFromGraphSpace);
//		int isPublic = responseFromGraphSpace.getInt("is_public");
//		if (isPublic == 0){
//			JSONObject response = Server.INSTANCE.updateGraph(name, graphJSON, false);
//			System.out.println(response);
//		}
//		else{
//			JSONObject response = Server.INSTANCE.updateGraph(name, graphJSON, true);
//			System.out.println(response);
//		}
	}
	
	private void postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		System.out.println(Server.INSTANCE.postGraph(graphJSON, styleJSON, isGraphPublic, tagsList).toString());
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
//		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
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
	
	private void cancelActionPerformed(ActionEvent e){
		this.dispose();
	}
	
//	private class ExportActionListener implements ActionListener{
//		private JSONObject graphJSON;
//		private JSONObject styleJSON;
//		private boolean isGraphPublic;
//		private ArrayList<String> tagsList;
//		
//		public ExportActionListener(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList){
//			this.graphJSON = graphJSON;
//			this.styleJSON = styleJSON;
//			this.isGraphPublic = isGraphPublic;
//			this.tagsList = tagsList;
//		}
//		
//		public void actionPerformed(ActionEvent e){
//			try {
//				exportActionPerformed(e, graphJSON, styleJSON, isGraphPublic, tagsList);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//	}
}