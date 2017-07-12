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
import java.awt.event.ActionEvent;

public class PostGraphDialog extends JDialog {
	private JLabel hostValueLabel;
	private JLabel usernameValueLabel;
	private JLabel usernameLabel;
	private JPanel buttonsPanel;
	private JButton postGraphButton;
	private JButton cancelButton;
	public PostGraphDialog(Frame parent) {
		this.setTitle("Export Graphs to GraphSpace");
		
		JLabel hostLabel = new JLabel("Host");
		
		hostValueLabel = new JLabel("www.graphspace.org");
		
		usernameValueLabel = new JLabel("Anonymous");
		
		
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
								.addComponent(hostValueLabel, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
								.addComponent(usernameValueLabel)))
						.addComponent(buttonsPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(hostValueLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(hostLabel))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(usernameValueLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameLabel))
					.addGap(15)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(14, Short.MAX_VALUE))
		);
		
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
			System.out.println(graphJSON.toString());
//			if (graphJSON == null){
//				this.dispose();
//				JOptionPane.showMessageDialog(new JFrame(), "Could not export your network to JSON.", "Dialog", JOptionPane.ERROR_MESSAGE);
//			}
			String graphName = graphJSON.getJSONObject("data").getString("name");
			System.out.println(graphName);
			if(Server.INSTANCE.updatePossible(graphName)){
				postGraphButton.setText("Update");
				postGraphButton.setEnabled(true);
				postGraphButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						updateActionPerformed(evt, graphJSON);
					}
				});
			}
			else{
				postGraphButton.setText("Export");
				postGraphButton.setEnabled(true);
				postGraphButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						exportActionPerformed(evt, graphJSON);
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
	
	private void updateActionPerformed(ActionEvent evt, JSONObject graphJSON){
		try{
			this.dispose();
			updateGraph(graphJSON);
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not update graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}
	
	private void exportActionPerformed(ActionEvent evt, JSONObject graphJSON){
		try{
			this.dispose();
			postGraph(graphJSON);
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not post graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}
	
	private void updateGraph(JSONObject graphJSON) throws Exception{
		String name = graphJSON.getJSONObject("data").getString("name");
		JSONObject responseFromGraphSpace = Server.INSTANCE.client.getGraphByName(name);
//		System.out.println(responseFromGraphSpace);
		int isPublic = responseFromGraphSpace.getInt("is_public");
		if (isPublic == 0){
			JSONObject response = Server.INSTANCE.updateGraph(name, graphJSON, false);
			System.out.println(response);
		}
		else{
			JSONObject response = Server.INSTANCE.updateGraph(name, graphJSON, true);
			System.out.println(response);
		}
	}
	
	private void postGraph(JSONObject graphJSON) throws Exception{
		Server.INSTANCE.postGraph(graphJSON);
	}
	
	private JSONObject exportNetworkToJSON() throws IOException{
		File tempFile = File.createTempFile("CyGraphSpaceExport", ".cyjs");
		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
		TaskIterator ti = CyObjectManager.INSTANCE.getExportNetworkTaskFactory().createTaskIterator(network, tempFile);
//		System.out.println("network: " + network.toString());
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
//		CyObjectManager.INSTANCE.getTaskManager().
//		System.out.println("tempfile: " + tempFile.toString());
		String graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
//		InputStream is = new FileInputStream(tempFile);
//		String graphJSONString = IOUtils.toString(is);
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
//		System.out.println("graphString: "+graphJSONString);
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
//		System.out.println("graphString: "+graphJSONString);
		JSONObject graphJSON = new JSONObject(graphJSONString);
//		System.out.println(graphJSON.toString());
        return graphJSON;
	}
	
	private void cancelActionPerformed(ActionEvent e){
		this.dispose();
	}
	
}