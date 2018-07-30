package org.cytoscape.graphspace.cygraphspace.internal.gui;

//importing swing components
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.MessageConfig;
import org.cytoscape.graphspace.cygraphspace.internal.util.PostGraphExportUtils;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

/**
 * This class defines the UI for Authentication dialog
 * @author rishabh
 *
 */
@SuppressWarnings("serial")
public class AuthenticationDialog extends JDialog {
	
	//UI component variables
	private JTextField hostField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	JButton cancelButton;
	private JFrame loadingFrame;
	
	private CyGraphSpaceResultPanel resultPanel;

	public AuthenticationDialog(JFrame loadingFrame, CyGraphSpaceResultPanel resultPanel) {
	    super(CyObjectManager.INSTANCE.getApplicationFrame(), "Log in to the Server", ModalityType.APPLICATION_MODAL);

	    this.resultPanel = resultPanel;

	    JLabel hostLabel = new JLabel("Host");

	    AuthTextFieldListener textFieldListener = new AuthTextFieldListener();

	    hostField = new JTextField();
	    hostField.setColumns(10);
	    hostField.getDocument().addDocumentListener(textFieldListener);

	    JLabel usernameLabel = new JLabel("Username");
	    usernameField = new JTextField();
	    usernameField.setColumns(10);
	    usernameField.getDocument().addDocumentListener(textFieldListener);

	    JLabel passwordLabel = new JLabel("Password");
	    passwordField = new JPasswordField();
	    passwordField.getDocument().addDocumentListener(textFieldListener);

	    JPanel buttonsPanel = new JPanel();
	    loginButton = new JButton("Log In");

	    this.loadingFrame = loadingFrame;

	    //action listener for login button
	    loginButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent evt)
	        {
	            try {
	                loginActionPerformed(evt);
	            } catch (UnirestException e) {
	                if (e.getCause().getClass() == UnknownHostException.class) {
	                    JOptionPane.showMessageDialog(null, MessageConfig.AUTH_INVALID_URL, "Error", JOptionPane.ERROR_MESSAGE);
	                    loginButton.setText("Log In");
	                    cancelButton.setEnabled(true);
	                }
	            } catch (Exception e) {
	                if (e.getCause().getClass() == MalformedURLException.class) {
	                    JOptionPane.showMessageDialog(null, MessageConfig.AUTH_MALFORMED_URL, "Error", JOptionPane.ERROR_MESSAGE);
	                    loginButton.setText("Log In");
	                    cancelButton.setEnabled(true);
	                }else {
	                    e.printStackTrace();
	                }
	            }
	        }
	    });
		loginButton.setEnabled(false);
		
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

    	//throws error if cannot authenticate the user
    	if (!Server.INSTANCE.authenticate(hostText, usernameText, passwordText)){
    		JOptionPane.showMessageDialog(this, MessageConfig.AUTH_FAIL_MSG, "Error", JOptionPane.ERROR_MESSAGE);
    		loginButton.setText("Log In");
    		cancelButton.setEnabled(true);
    	}

    	//logs in the user
    	else{
            this.dispose();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    loadingFrame.setVisible(true);
                }
            });

            new Thread() {
                public void run() {
                    try {
                        PostGraphExportUtils.populate(CyObjectManager.INSTANCE.getApplicationFrame(), loadingFrame, resultPanel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
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
	    SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadingFrame.dispose();
            }
        });

        this.dispose();
    }

	/**
	 * Helper method to check if login button should be enabled or not
	 * login button is enable only when all three fields (host, username, and password) are filled.
	 */
	private void enableLoginButton() {
	    if (hostField.getText().isEmpty() || usernameField.getText().isEmpty() 
	            || new String(passwordField.getPassword()).isEmpty()) {
	        loginButton.setEnabled(false);
	    }
	    else {
	        loginButton.setEnabled(true);
	    }
	}

	/**
	 * Listener for the host, username, and password text field
	 *     use for enable/disable login button
	 */
	private class AuthTextFieldListener implements DocumentListener {
	    @Override
	    public void changedUpdate(DocumentEvent e) {
	        enableLoginButton();
	    }
	    @Override
	    public void insertUpdate(DocumentEvent e) {
	        enableLoginButton();
	    }
	    @Override
	    public void removeUpdate(DocumentEvent e) {
	        enableLoginButton();
	    }
	}
}