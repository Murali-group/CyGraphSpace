package org.cytoscape.graphspace.cygraphspace.internal.gui;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.graphspace.javaclient.CyGraphSpaceClient;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class AuthenticationDialog extends JDialog {
	private JTextField hostField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton signInButton;
	JButton cancelButton;
	public AuthenticationDialog(Frame parent) {
		setTitle("Log in to the Server");
		JLabel hostLabel = new JLabel("Host");
		
		hostField = new JTextField();
		hostField.setColumns(10);
		
		JLabel usernameLabel = new JLabel("Username");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		
		passwordField = new JPasswordField();
		
		JPanel buttonsPanel = new JPanel();
		
		signInButton = new JButton("Log In");
		
		signInButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt)
            {
                try {
					signInActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		buttonsPanel.add(signInButton);

		
		cancelButton = new JButton("Cancel");
		
		cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                cancelActionPerformed(evt);
            }
        });
		buttonsPanel.add(cancelButton);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(hostLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addGap(75)
							.addComponent(hostField, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addGap(21)
							.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addGap(21)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(13, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(293, Short.MAX_VALUE)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(hostLabel))
						.addComponent(hostField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(usernameLabel)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(passwordLabel))
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(11, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
		populateFields();
		pack();
	}
	
	private void signInActionPerformed(ActionEvent evt) throws Exception{
		signInButton.setText("Checking");
		signInButton.setEnabled(false);
		cancelButton.setEnabled(false);
    	String hostText = hostField.getText();
    	String usernameText = usernameField.getText();
    	String passwordText = new String(passwordField.getPassword());
    	System.out.println(hostText + " : " + usernameText + " : " + passwordText);
    	if (hostText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty()){
    		JOptionPane.showMessageDialog((Component)evt.getSource(), "Please enter all the values", "Error", JOptionPane.ERROR_MESSAGE);
    		signInButton.setText("Log In");
    		signInButton.setEnabled(true);
    		cancelButton.setEnabled(true);
    	}
    	else if (!Server.INSTANCE.authenticate(hostText, usernameText, passwordText)){
    		JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not authenticate you. Please ensure the username and password are correct and that you are connected to the internet.", "Error", JOptionPane.ERROR_MESSAGE);
    		signInButton.setText("Log In");
    		signInButton.setEnabled(true);
    		cancelButton.setEnabled(true);
    	}
    	else{
	    	this.dispose();
    	}
    }
	
	private void populateFields(){
		if (Server.INSTANCE.getHost()==null){
			hostField.setText("www.graphspace.org");
		}
		else{
			hostField.setText(Server.INSTANCE.getHost());
		}
		if (Server.INSTANCE.getUsername()!=null){
			usernameField.setText(Server.INSTANCE.getUsername());
		}
		if (Server.INSTANCE.getPassword()!=null){
			passwordField.setText(Server.INSTANCE.getPassword());
		}
	}
	
	private void cancelActionPerformed(ActionEvent evt) {
        this.dispose();
    }
}