package org.cytoscape.graphspace.cygraphspace.internal.gui;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;

import org.graphspace.javaclient.*;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class AuthenticationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Frame parent;
	private JButton save;
	private JButton cancel;
    private JLabel hostLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPasswordField password;
    private JTextField host;
    private JTextField username;
    private JTextPane message;
	/**
	 * Create the dialog.
	 */
	public AuthenticationDialog(Frame parent) {
		super(parent, true);
        this.parent = parent;
        initComponents();
        prepComponents();
	}
	
    private void populateFieldsWithSelectedServer() {
       
    }
    
    private void prepComponents() {
    	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(save);    
    }
    
    private void initComponents() {
    	setTitle("Sign in to GraphSpace");
        host = new JTextField("www.graphspace.org");
        username = new JTextField();
        hostLabel = new JLabel();
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        message = new JTextPane();
        message.setText("");
        message.setEditable(false);
        save = new JButton();
        cancel = new JButton();
        password = new JPasswordField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        hostLabel.setText("Host:");
        usernameLabel.setText("Username:");
        passwordLabel.setText("Password:");
        save.setText("Save");
        
        
        save.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                try {
					saveActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("exception aa gya");
					e.printStackTrace();
				}
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(hostLabel)
                            .addComponent(usernameLabel)
                            .addComponent(passwordLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(host)
                            .addComponent(username)
                            .addComponent(password)))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(message))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 221, Short.MAX_VALUE)
                        .addComponent(cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(save)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
            	.addContainerGap()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(host, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hostLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(message))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(cancel))
                .addContainerGap())
        );

        pack();
    }

    private void urlActionPerformed(ActionEvent evt) {
    	
    }
    
    private void saveActionPerformed(ActionEvent evt) throws Exception{
    	System.out.println("hehehe");
    	if (Server.INSTANCE.client == null){
    		System.out.println("1");
    		Server.INSTANCE.client = new Client(); 
    	}
    	System.out.println("2");
    	String hostText = host.getText();
    	String usernameText = username.getText();
    	String passwordText = password.getPassword().toString();
    	if (hostText == "" || usernameText == "" || passwordText == ""){
    		System.out.println("3");
    		message.setText("Please enter the values");
    	}
    	System.out.println("4");
    	Server.INSTANCE.client.authenticate(usernameText, passwordText, hostText);
    	System.out.println("5");
    	System.out.println(Server.INSTANCE.client.getMyGraphs(new ArrayList<String>(), 20, 0));
    	System.out.println("6");
    	System.out.println("7");
    }
    
    private void cancelActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

}
