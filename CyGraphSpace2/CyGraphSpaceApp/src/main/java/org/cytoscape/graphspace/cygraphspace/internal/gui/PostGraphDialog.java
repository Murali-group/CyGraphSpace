package org.cytoscape.graphspace.cygraphspace.internal.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
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

public class PostGraphDialog extends JDialog {
	
	private JLabel hostValueLabel;
	private JLabel usernameValueLabel;
	private JLabel usernameLabel;
	private JPanel buttonsPanel;
	private JButton postGraphButton;
	private JButton cancelButton;
	private JPanel loadingPanel;
	private GroupLayout groupLayout;
	private JLabel graphNameLabel;
	private JLabel graphNameValue;
	
	public PostGraphDialog(Frame parent, String graphName, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tags) {
		this.setTitle("Export Graphs to GraphSpace");
		
		JLabel hostLabel = new JLabel("Host");
		
		hostValueLabel = new JLabel("www.graphspace.org");
		
		usernameValueLabel = new JLabel("Anonymous");
		
		
		usernameLabel = new JLabel("Username");
		
		buttonsPanel = new JPanel();
		
		graphNameLabel = new JLabel("Graph Name");
		
		graphNameValue = new JLabel("");
		
		groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(hostLabel)
								.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(usernameValueLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(hostValueLabel, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)))
						.addComponent(buttonsPanel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(graphNameLabel, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(graphNameValue, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE)))
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
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(graphNameValue)
							.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
							.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
						.addComponent(graphNameLabel))
					.addContainerGap())
		);
		
		postGraphButton = new JButton("Export");
		postGraphButton.setEnabled(true);
		postGraphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportActionPerformed(e, graphJSON, styleJSON, isGraphPublic);
			}
		});
		buttonsPanel.add(postGraphButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelActionPerformed(e);
			}
		});
		buttonsPanel.add(cancelButton);
		getContentPane().setLayout(groupLayout);
		populateFields(graphName);
		pack();
//		check();
	}

//	public void check(){
//		try {
//			System.out.println("host: "+Server.INSTANCE.getHost() + ", username: "+Server.INSTANCE.getUsername()+ ",password: "+Server.INSTANCE.getPassword());
//				if(Server.INSTANCE.updatePossible(graphName)){
//				getContentPane().remove(loadingPanel);
//				loadingPanel.setVisible(false);
//				getContentPane().setLayout(groupLayout);
//				postGraphButton.setText("Update");
//				postGraphButton.setEnabled(true);
//				JSONObject responseFromGraphSpace = Server.INSTANCE.client.getGraphByName(graphName);
//				int isPublic = responseFromGraphSpace.getInt("is_public");
//				postGraphButton.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent evt) {
//						boolean isGraphPublic = false;
//						if (isPublic == 1){
//							isGraphPublic = true;
//						}
//						updateActionPerformed(evt, graphJSON, styleJSON, isGraphPublic);
//					}
//				});
//			}
//			else{
//				getContentPane().remove(loadingPanel);
//				loadingPanel.setVisible(false);
//				getContentPane().setLayout(groupLayout);
//				postGraphButton.setText("Export");
//				postGraphButton.setEnabled(true);
////				ArrayList<GSGroupMetaData> groups = Server.INSTANCE.getMyGroups(20, 0);
////				for (GSGroupMetaData group: groups){
////				}
////				postGraphButton.addActionListener(new ExportActionListener(graphJSON, styleJSON, isGraphPublic, tagsList));
//				postGraphButton.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent evt) {
//						boolean isGraphPublic = false;
//						exportActionPerformed(evt, graphJSON, styleJSON, isGraphPublic);
//					}
//				});
//			}
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			dispose();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			dispose();
//		}
//	}
	
	private void populateFields(String graphName){
		if (Server.INSTANCE.getUsername() != null){
			usernameValueLabel.setText(Server.INSTANCE.getUsername());
		}
		if (Server.INSTANCE.getHost() != null){
			hostValueLabel.setText(Server.INSTANCE.getHost());
		}
		graphNameValue.setText(graphName);
	}
	
	private void exportActionPerformed(ActionEvent evt, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic){
		try{
			this.dispose();
			postGraph(graphJSON, styleJSON, isGraphPublic, null);
//			String name = graphJSON.getJSONObject("data").getString("name");
//			JSONObject graph = Server.INSTANCE.getGraphByName(name);
//			System.out.println(graph.toString());
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not post graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}
//	
//	private void updateGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isPublic, ArrayList<String> tagsList) throws Exception{
//		String name = graphJSON.getJSONObject("data").getString("name");
//		Server.INSTANCE.updateGraph(name, graphJSON, isPublic, tagsList);
//	}
	
	private void postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		System.out.println(Server.INSTANCE.postGraph(graphJSON, styleJSON, isGraphPublic, tagsList).toString());
	}
	
	
	private void cancelActionPerformed(ActionEvent e){
		this.dispose();
	}
}