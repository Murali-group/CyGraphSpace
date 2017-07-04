package org.cytoscape.graphspace.cygraphspace.internal.gui;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.io.IOUtils;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;

public class PostGraphDialog extends JDialog {
	private JTextField hostField;
	private JTextField usernameField;
	private JLabel usernameLabel;
	private JPanel buttonsPanel;
	private JButton postGraphButton;
	private JButton cancelButton;
	public PostGraphDialog(Frame parent) {
		this.setTitle("Export Graphs to GraphSpace");
		
		JLabel hostLabel = new JLabel("Host");
		
		hostField = new JTextField();
		hostField.setColumns(10);
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		usernameLabel = new JLabel("Username");
		
		buttonsPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(hostLabel)
								.addComponent(usernameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(hostField, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
								.addComponent(usernameField)))
						.addComponent(buttonsPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(hostField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(hostLabel))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameLabel))
					.addGap(15)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(14, Short.MAX_VALUE))
		);
		
		postGraphButton = new JButton("Export");
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
		try {
			JSONObject graphJSON = exportNetworkToJSON();
			if (updatePossible(graphJSON)){
				postGraphButton.setText("Update");
				postGraphButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateActionPerformed(e, graphJSON);
					}
				});
			}
			else{
				postGraphButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						exportActionPerformed(e, graphJSON);
					}
				});
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pack();
	}
	
	private void populateFields(){
		if (Server.INSTANCE.getUsername() != null){
			usernameField.setText(Server.INSTANCE.getUsername());
		}
		if (Server.INSTANCE.getHost() != null){
			hostField.setText(Server.INSTANCE.getHost());
		}
	}
	
	private void updateActionPerformed(ActionEvent evt, JSONObject graphJSON){
		try{
			updateGraph(graphJSON);
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not update graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		this.dispose();
	}
	
	private void exportActionPerformed(ActionEvent evt, JSONObject graphJSON){
		try{
			postGraph(graphJSON);
			}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not post graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		this.dispose();
	}
	
	private boolean updatePossible(JSONObject graphJSON) throws Exception{
		String name = graphJSON.getString("name");
		JSONObject responseJSON = Server.INSTANCE.getGraphByName(name);
		System.out.println(responseJSON);
		if (responseJSON.getInt("status")==201 || responseJSON.getInt("status")==201){
			return true;
		}
		return false;
	}
	
	private void updateGraph(JSONObject graphJSON) throws Exception{
		String name = graphJSON.getString("name");
		JSONObject responseFromGraphSpace = Server.INSTANCE.client.getGraphByName(name);
		int isPublic = responseFromGraphSpace.getJSONObject("body").getJSONObject("object").getInt("is_public");
		if (isPublic == 0){
			Server.INSTANCE.updateGraph(name, graphJSON, false);
		}
		else{
			Server.INSTANCE.updateGraph(name, graphJSON, true);
		}
	}
	
	private void postGraph(JSONObject graphJSON) throws Exception{
		Server.INSTANCE.postGraph(graphJSON);
	}
	
	private JSONObject exportNetworkToJSON() throws IOException{
		File tempFile = File.createTempFile("CyGraphSpaceExport", ".cyjs");
		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
		TaskIterator ti = CyObjectManager.INSTANCE.getExportNetworkTaskFactory().createTaskIterator(network, tempFile);
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
		InputStream is = new FileInputStream(tempFile);
		String graphJSONString = IOUtils.toString(is);
//		System.out.println(graphJSONString);
		JSONObject graphJSON = new JSONObject(graphJSONString);
		tempFile.delete();
        return graphJSON;
	}
	
	private void cancelActionPerformed(ActionEvent e){
		this.dispose();
	}
	
}