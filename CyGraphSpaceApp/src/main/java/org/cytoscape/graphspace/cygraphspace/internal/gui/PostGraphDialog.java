package org.cytoscape.graphspace.cygraphspace.internal.gui;

//importing swing components
import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.JButton;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.json.JSONObject;
import java.util.ArrayList;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class defines the UI for the PostGraph dialog
 * @author rishabh
 *
 */
public class PostGraphDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 15L;
	
	//UI component variables
	private JLabel hostValueLabel;
	private JLabel usernameValueLabel;
	private JLabel usernameLabel;
	private JPanel buttonsPanel;
	private JButton postGraphButton;
	private JButton cancelButton;
	private GroupLayout groupLayout;
	private JLabel graphNameLabel;
	private JLabel graphNameValue;
	
	
	public PostGraphDialog(Frame parent, String graphName, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tags) {
		
		this.setTitle("Export Graphs to GraphSpace");
		this.setAlwaysOnTop(true);
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
	}
	
	//populate dialog with user values
	private void populateFields(String graphName){
		if (Server.INSTANCE.getUsername() != null){
			usernameValueLabel.setText(Server.INSTANCE.getUsername());
		}
		if (Server.INSTANCE.getHost() != null){
			hostValueLabel.setText(Server.INSTANCE.getHost());
		}
		graphNameValue.setText(graphName);
	}
	
	//called when export button clicked
	private void exportActionPerformed(ActionEvent evt, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic){
		try{
			this.dispose();
			postGraph(graphJSON, styleJSON, isGraphPublic, null);
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not post graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	}
	
	//post the current network to GraphSpace
	private void postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
		Server.INSTANCE.postGraph(graphJSON, styleJSON, isGraphPublic, tagsList);
	}
	
	//close the dialog box on cancel clicked
	private void cancelActionPerformed(ActionEvent e){
		this.dispose();
	}
}