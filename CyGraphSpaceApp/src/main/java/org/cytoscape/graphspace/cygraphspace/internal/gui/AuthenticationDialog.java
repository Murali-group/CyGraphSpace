package org.cytoscape.graphspace.cygraphspace.internal.gui;

//importing swing components
import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;

import java.awt.Component;
import java.awt.Frame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class defines the UI for Authentication dialog
 * @author rishabh
 *
 */
public class AuthenticationDialog extends JDialog {
	
	//UI component variables
	private JTextField hostField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
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
		loginButton = new JButton("Log In");
		
		//action listener for login button
		loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt)
            {
                try {
					loginActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		buttonsPanel.add(loginButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
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
	
	//called when login button clicked
	private void loginActionPerformed(ActionEvent evt) throws Exception{
		loginButton.setText("Checking");
		loginButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		//sets user authentication variables from the entered values
    	String hostText = hostField.getText();
    	String usernameText = usernameField.getText();
    	String passwordText = new String(passwordField.getPassword());
    	
    	//throws error if values not filled
    	if (hostText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty()){
    		JOptionPane.showMessageDialog((Component)evt.getSource(), "Please enter all the values", "Error", JOptionPane.ERROR_MESSAGE);
    		loginButton.setText("Log In");
    		loginButton.setEnabled(true);
    		cancelButton.setEnabled(true);
    	}
    	
    	//throws error if cannot authenticate the user
    	else if (!Server.INSTANCE.authenticate(hostText, usernameText, passwordText)){
    		JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not authenticate you. Please ensure the username and password are correct and that you are connected to the internet.", "Error", JOptionPane.ERROR_MESSAGE);
    		loginButton.setText("Log In");
    		loginButton.setEnabled(true);
    		cancelButton.setEnabled(true);
    	}
    	
    	//logs in the user
    	else{
    		System.out.println(hostText + " : " + usernameText + " : " + passwordText);
    		System.out.println(Server.INSTANCE.getHost()+Server.INSTANCE.getUsername()+Server.INSTANCE.getPassword());
	    	this.dispose();
    	}
    }
	
	//populate user values in the authentication dialog
	private void populateFields(){
		hostField.setText(Server.INSTANCE.getHost());
		if (Server.INSTANCE.getUsername()!=null){
			usernameField.setText(Server.INSTANCE.getUsername());
		}
		if (Server.INSTANCE.getPassword()!=null){
			passwordField.setText(Server.INSTANCE.getPassword());
		}
	}
	
	//closes the dialog when cancel button clicked
	private void cancelActionPerformed(ActionEvent evt) {
        this.dispose();
    }
}