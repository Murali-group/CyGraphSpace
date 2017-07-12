package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.IOUtils;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.ResultTask;
import org.cytoscape.io.webservice.NetworkImportWebServiceClient;
import org.cytoscape.io.webservice.SearchWebServiceClient;
import org.cytoscape.io.webservice.swing.AbstractWebServiceGUIClient;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.graphspace.javaclient.CyGraphSpaceClient;
import org.graphspace.javaclient.model.GSGraphMetaData;
import org.json.JSONObject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;

public class GetGraphsPanel extends AbstractWebServiceGUIClient
		implements NetworkImportWebServiceClient, SearchWebServiceClient {
	static final String APP_DESCRIPTION = "<html>" + "CyGraphSpace App is used to import and export graphs from "
			+ "<a href=\"http://www.grapshace.org\">GraphSpace</a> website. ";

//	final TaskManager taskManager;
	CyGraphSpaceClient client;
	OpenBrowser openBrowser;
	LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
	TaskManager taskManager;
	
//	final JButton searchButton = new JButton(new ImageIcon(getClass().getResource("/search-icon.png")));
//	final JTable resultsTable = new JTable();
//	final JLabel noResultsLabel = new JLabel();
	private JTextField usernameTextField;
	private JTextField hostTextField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JTextField searchField;
	private JPanel loginPanel = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	DefaultTableModel myGraphsTableModel;
	TableRowSorter<TableModel> myGraphsTableSorter;
	DefaultTableModel sharedGraphsTableModel;
	TableRowSorter<TableModel> sharedGraphsTableSorter;
	DefaultTableModel publicGraphsTableModel;
	TableRowSorter<TableModel> publicGraphsTableSorter;
//	DefaultTableModel searchResultsTableModel;
//	TableRowSorter<TableModel> searchResultsTableSorter;
	private JButton importButton;
	private JPanel parentPanel;
	private JPanel sharedGraphsPanel = new JPanel();
	private JPanel myGraphsPanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel publicGraphsPanel = new JPanel();
//	private JPanel searchResultsPanel = new JPanel();
	private JTable myGraphsTable;
	private JTable sharedGraphsTable;
	private JTable publicGraphsTable;
//	private JTable searchResultsTable;
	private JPanel myGraphsPaginationPanel = new JPanel();
	private JPanel sharedGraphsPaginationPanel = new JPanel();
	private JPanel publicGraphsPaginationPanel = new JPanel();
//	private JPanel searchResultsPaginationPanel = new JPanel();
	JScrollPane myGraphsScrollPane = new JScrollPane();
	JScrollPane sharedGraphsScrollPane = new JScrollPane();
	JScrollPane publicGraphsScrollPane = new JScrollPane();
//	JScrollPane searchResultsScrollPane = new JScrollPane();
	private int limit = 20;
	private int myGraphsOffSet = 0;
	private int sharedGraphsOffSet = 0;
	private int publicGraphsOffSet = 0;
//	private int searchResultsOffSet = 0;
	private boolean loggedIn = false;
	public GetGraphsPanel(TaskManager taskManager, OpenBrowser openBrowser) {
		super("http://www.graphspace.org", "GraphSpace", APP_DESCRIPTION);
		this.taskManager = taskManager;
//		this.taskManager = CyObjectManager.INSTANCE.getTaskManager();
		this.client = Server.INSTANCE.client;
		this.openBrowser = openBrowser;
		this.loadNetworkFileTaskFactory = CyObjectManager.INSTANCE.getLoadNetworkFileTaskFactory();
		
		parentPanel = new JPanel();
		super.gui = parentPanel;
		
		JPanel resultsPanel = new JPanel();
		GroupLayout gl_panel = new GroupLayout(parentPanel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(resultsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
						.addComponent(buttonPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
						.addComponent(searchPanel, GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
						.addComponent(loginPanel, GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(resultsPanel, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		tabbedPane.addTab("My Graphs", myGraphsPanel);
		tabbedPane.addTab("Shared Graphs", sharedGraphsPanel);
		tabbedPane.addTab("Public Graphs", publicGraphsPanel);
//		tabbedPane.addTab("Search Results", searchResultsPanel);
		
		GroupLayout gl_myGraphsPanel = new GroupLayout(myGraphsPanel);
		gl_myGraphsPanel.setHorizontalGroup(
			gl_myGraphsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(myGraphsScrollPane, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
				.addComponent(myGraphsPaginationPanel, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
		);
		gl_myGraphsPanel.setVerticalGroup(
			gl_myGraphsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_myGraphsPanel.createSequentialGroup()
					.addComponent(myGraphsScrollPane, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(myGraphsPaginationPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
		);
		
		JButton myGraphsPreviousButton = new JButton("< Previous");
		myGraphsPreviousButton.addActionListener(new MyGraphsPreviousButtonActionListener(this.limit, this.myGraphsOffSet-20));
		
		JButton myGraphsNextButton = new JButton("Next >");
		myGraphsNextButton.addActionListener(new MyGraphsNextButtonActionListener(this.limit, this.myGraphsOffSet+20));
		
		GroupLayout gl_myGraphsPaginationPanel = new GroupLayout(myGraphsPaginationPanel);
		gl_myGraphsPaginationPanel.setHorizontalGroup(
			gl_myGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_myGraphsPaginationPanel.createSequentialGroup()
					.addComponent(myGraphsPreviousButton)
					.addPreferredGap(ComponentPlacement.RELATED, 711, Short.MAX_VALUE)
					.addComponent(myGraphsNextButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
		);
		gl_myGraphsPaginationPanel.setVerticalGroup(
			gl_myGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_myGraphsPaginationPanel.createSequentialGroup()
					.addGroup(gl_myGraphsPaginationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(myGraphsPreviousButton)
						.addComponent(myGraphsNextButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		myGraphsPaginationPanel.setLayout(gl_myGraphsPaginationPanel);
		
		myGraphsTable = new JTable();
		myGraphsScrollPane.setRowHeaderView(myGraphsTable);
		myGraphsPanel.setLayout(gl_myGraphsPanel);
		myGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owned By"
	            }
	        );
		myGraphsTable.setModel(myGraphsTableModel);
        myGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myGraphsScrollPane.setViewportView(myGraphsTable);
        
        
		GroupLayout gl_sharedGraphsPanel = new GroupLayout(sharedGraphsPanel);
		gl_sharedGraphsPanel.setHorizontalGroup(
			gl_sharedGraphsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(sharedGraphsScrollPane, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
				.addComponent(sharedGraphsPaginationPanel, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
		);
		gl_sharedGraphsPanel.setVerticalGroup(
			gl_sharedGraphsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_sharedGraphsPanel.createSequentialGroup()
					.addComponent(sharedGraphsScrollPane, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sharedGraphsPaginationPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
		);
		
		JButton sharedGraphsPreviousButton = new JButton("< Previous");
		sharedGraphsPreviousButton.addActionListener(new SharedGraphsNextButtonActionListener(this.limit, this.sharedGraphsOffSet-20));
		JButton sharedGraphsNextButton = new JButton("Next >");
		sharedGraphsNextButton.addActionListener(new SharedGraphsNextButtonActionListener(this.limit, this.sharedGraphsOffSet+20));
		
		GroupLayout gl_sharedGraphsPaginationPanel = new GroupLayout(sharedGraphsPaginationPanel);
		gl_sharedGraphsPaginationPanel.setHorizontalGroup(
			gl_sharedGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_sharedGraphsPaginationPanel.createSequentialGroup()
					.addComponent(sharedGraphsPreviousButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 719, Short.MAX_VALUE)
					.addComponent(sharedGraphsNextButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
		);
		gl_sharedGraphsPaginationPanel.setVerticalGroup(
			gl_sharedGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sharedGraphsPaginationPanel.createSequentialGroup()
					.addGroup(gl_sharedGraphsPaginationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(sharedGraphsNextButton)
						.addComponent(sharedGraphsPreviousButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		sharedGraphsPaginationPanel.setLayout(gl_sharedGraphsPaginationPanel);
		
		sharedGraphsTable = new JTable();
		sharedGraphsScrollPane.setRowHeaderView(sharedGraphsTable);
		sharedGraphsPanel.setLayout(gl_sharedGraphsPanel);
		sharedGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owned By"
	            }
	        );
		sharedGraphsTable.setModel(sharedGraphsTableModel);
		sharedGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sharedGraphsScrollPane.setViewportView(sharedGraphsTable);
        
		JButton publicGraphsPreviousButton = new JButton("< Previous");
		publicGraphsPreviousButton.addActionListener(new PublicGraphsPreviousButtonActionListener(this.limit, this.publicGraphsOffSet-20));
		
		JButton publicGraphsNextButton = new JButton("Next >");
		publicGraphsNextButton.addActionListener(new PublicGraphsNextButtonActionListener(this.limit, this.publicGraphsOffSet+20));
        
		GroupLayout gl_publicGraphsPanel = new GroupLayout(publicGraphsPanel);
		gl_publicGraphsPanel.setHorizontalGroup(
			gl_publicGraphsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(publicGraphsScrollPane, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
				.addComponent(publicGraphsPaginationPanel, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
		);
		gl_publicGraphsPanel.setVerticalGroup(
			gl_publicGraphsPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_publicGraphsPanel.createSequentialGroup()
					.addComponent(publicGraphsScrollPane, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(publicGraphsPaginationPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
		);
		GroupLayout gl_publicGraphsPaginationPanel = new GroupLayout(publicGraphsPaginationPanel);
		gl_publicGraphsPaginationPanel.setHorizontalGroup(
			gl_publicGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_publicGraphsPaginationPanel.createSequentialGroup()
					.addComponent(publicGraphsPreviousButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 719, Short.MAX_VALUE)
					.addComponent(publicGraphsNextButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_publicGraphsPaginationPanel.setVerticalGroup(
			gl_publicGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_publicGraphsPaginationPanel.createSequentialGroup()
					.addGroup(gl_publicGraphsPaginationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(publicGraphsNextButton)
						.addComponent(publicGraphsPreviousButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		publicGraphsPaginationPanel.setLayout(gl_publicGraphsPaginationPanel);
		
		publicGraphsTable = new JTable();
		publicGraphsScrollPane.setRowHeaderView(publicGraphsTable);
		publicGraphsPanel.setLayout(gl_publicGraphsPanel);
		publicGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owned By"
	            }
	        );
		publicGraphsTable.setModel(publicGraphsTableModel);
		publicGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		publicGraphsScrollPane.setViewportView(publicGraphsTable);
        
//		JButton searchResultsPreviousButton = new JButton("< Previous");
//		searchResultsPreviousButton.addActionListener(new SearchResultsPreviousButtonActionListener(this.limit, this.searchResultsOffSet-20));
		
//		JButton searchResultsNextButton = new JButton("Next >");
//		searchResultsNextButton.addActionListener(new SearchResultsNextButtonActionListener(this.limit, this.myGraphsOffSet+20));
        
//		GroupLayout gl_searchResultsPanel = new GroupLayout(searchResultsPanel);
//		gl_searchResultsPanel.setHorizontalGroup(
//			gl_searchResultsPanel.createParallelGroup(Alignment.LEADING)
//				.addComponent(searchResultsScrollPane, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
//				.addComponent(searchResultsPaginationPanel, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
//		);
//		gl_searchResultsPanel.setVerticalGroup(
//			gl_searchResultsPanel.createParallelGroup(Alignment.TRAILING)
//				.addGroup(gl_searchResultsPanel.createSequentialGroup()
//					.addComponent(searchResultsScrollPane, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
//					.addPreferredGap(ComponentPlacement.RELATED)
//					.addComponent(searchResultsPaginationPanel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
//		);
//		GroupLayout gl_searchResultsPaginationPanel = new GroupLayout(searchResultsPaginationPanel);
//		gl_searchResultsPaginationPanel.setHorizontalGroup(
//			gl_searchResultsPaginationPanel.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_searchResultsPaginationPanel.createSequentialGroup()
//					.addComponent(searchResultsPreviousButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
//					.addGap(719)
//					.addComponent(searchResultsNextButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
//					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//		);
//		gl_searchResultsPaginationPanel.setVerticalGroup(
//			gl_searchResultsPaginationPanel.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_searchResultsPaginationPanel.createSequentialGroup()
//					.addGroup(gl_searchResultsPaginationPanel.createParallelGroup(Alignment.BASELINE)
//						.addComponent(searchResultsNextButton)
//						.addComponent(searchResultsPreviousButton))
//					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//		);
//		searchResultsPaginationPanel.setLayout(gl_searchResultsPaginationPanel);
//		
//		searchResultsTable = new JTable();
//		searchResultsScrollPane.setRowHeaderView(searchResultsTable);
//		searchResultsPanel.setLayout(gl_searchResultsPanel);
//		searchResultsTableModel = new DefaultTableModel(
//	            new Object [][]
//	            {
//	                {null, null, null, null}
//	            },
//	            new String []
//	            {
//	                "Graph ID", "Graph Name", "Owned By"
//	            }
//	        );
//		searchResultsTable.setModel(searchResultsTableModel);
//		searchResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		searchResultsScrollPane.setViewportView(searchResultsTable);
		
		
		GroupLayout gl_resultsPanel = new GroupLayout(resultsPanel);
		gl_resultsPanel.setHorizontalGroup(
			gl_resultsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 926, Short.MAX_VALUE)
		);
		gl_resultsPanel.setVerticalGroup(
			gl_resultsPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
		);
		resultsPanel.setLayout(gl_resultsPanel);
		
		
		myGraphsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					String id = myGraphsTable.getValueAt(myGraphsTable.getSelectedRow(), 0).toString();
					getGraphActionPerformed(e, id);
				}
			}
		});
		sharedGraphsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					String id = sharedGraphsTable.getValueAt(sharedGraphsTable.getSelectedRow(), 0).toString();
					getGraphActionPerformed(e, id);
				}
			}
		});
		publicGraphsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
					String id = publicGraphsTable.getValueAt(publicGraphsTable.getSelectedRow(), 0).toString();
					getGraphActionPerformed(e, id);
				}
			}
		});
//		searchResultsTable.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2){
//					String id = searchResultsTable.getValueAt(searchResultsTable.getSelectedRow(), 0).toString();
//					getGraphActionPerformed(e, id);
//				}
//			}
//		});
		importButton = new JButton("Import to Cytoscape");
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = "";
				int selectedTable = tabbedPane.getSelectedIndex();
				if (selectedTable==0){
					if (myGraphsTable.getSelectedRow()<=0){
						id = null;
					}
					else{
						id = myGraphsTable.getValueAt(myGraphsTable.getSelectedRow(), 0).toString();
					}
				}
				else if (selectedTable==1){
					if (sharedGraphsTable.getSelectedRow()<=0){
						id = null;
					}
					else{
						id = sharedGraphsTable.getValueAt(sharedGraphsTable.getSelectedRow(), 0).toString();
					}
				}
				else if(selectedTable==2){
					if (publicGraphsTable.getSelectedRow()<=0){
						id = null;
					}
					else{
						id = publicGraphsTable.getValueAt(publicGraphsTable.getSelectedRow(), 0).toString();
					}
				}
//				else if (selectedTable==3){
//					if (searchResultsTable.getSelectedRow()<=0){
//						id = null;
//					}
//					else{
//						id = searchResultsTable.getValueAt(searchResultsTable.getSelectedRow(), 0).toString();
//					}
//				}
				getGraphActionPerformed(e, id);
			}
		});
		
		JButton openInBrowserButton = new JButton("Open in GraphSpace");
		
		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_buttonPanel.createSequentialGroup()
					.addContainerGap(528, Short.MAX_VALUE)
					.addComponent(importButton)
					.addGap(18)
					.addComponent(openInBrowserButton, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_buttonPanel.setVerticalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(openInBrowserButton)
						.addComponent(importButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		buttonPanel.setLayout(gl_buttonPanel);
		
		JLabel searchLabel = new JLabel("Search");
		
		searchField = new JTextField();
		searchField.setColumns(10);
		
		JButton searchButton = new JButton("Search");
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(searchLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, 706, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
					.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_searchPanel.setVerticalGroup(
			gl_searchPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_searchPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(searchButton)
						.addComponent(searchLabel))
					.addContainerGap())
		);
		searchPanel.setLayout(gl_searchPanel);
		
		JLabel hostLabel = new JLabel("Host");
		
		JLabel usernameLabel = new JLabel("Username");
		
		usernameTextField = new JTextField();
		usernameTextField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		
		hostTextField = new JTextField();
		hostTextField.setText("www.graphspace.org");
		hostTextField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		
		loginButton = new JButton("Log In");
		loginButton.setToolTipText("Log in to the server");
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				loginActionPerformed(e);
			}
		});
		GroupLayout gl_loginPanel = new GroupLayout(loginPanel);
		gl_loginPanel.setHorizontalGroup(
			gl_loginPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_loginPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(hostLabel)
					.addGap(3)
					.addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(usernameLabel)
					.addGap(3)
					.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
					.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_loginPanel.setVerticalGroup(
			gl_loginPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_loginPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameLabel)
						.addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(hostLabel)
						.addComponent(loginButton))
					.addContainerGap())
		);
		loginPanel.setLayout(gl_loginPanel);
		parentPanel.setLayout(gl_panel);
		populate();
	}

	private void populate(){
		if (Server.INSTANCE.isAuthenticated()){
			try {
				loginButton.setText("Log Out");
				hostTextField.setText(Server.INSTANCE.getHost());
				usernameTextField.setText(Server.INSTANCE.getUsername());
				passwordField.setText(Server.INSTANCE.getPassword());
				hostTextField.setEnabled(false);
				usernameTextField.setEnabled(false);
				passwordField.setEnabled(false);
				populateMyGraphs(this.limit, this.myGraphsOffSet);
				populatePublicGraphs(this.limit, this.publicGraphsOffSet);
				populateSharedGraphs(this.limit, this.sharedGraphsOffSet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			loginButton.setText("Log In");
		}
	}
	
	private void loginActionPerformed(ActionEvent evt){
		if (!this.loggedIn){
//			loginButton.setEnabled(false);
	    	String hostText = hostTextField.getText();
	    	String usernameText = usernameTextField.getText();
	    	String passwordText = new String(passwordField.getPassword());
	    	System.out.println(hostText + " : " + usernameText + " : " + passwordText);
	    	if (hostText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty()){    		
	    		JOptionPane.showMessageDialog((Component)evt.getSource(), "Please enter all the values", "Error", JOptionPane.ERROR_MESSAGE);
	    		loginButton.setText("Log In");
	    		loginButton.setEnabled(true);
	    	}
	    	else if (!Server.INSTANCE.authenticate(hostText, usernameText, passwordText)){
	    		JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not authenticate you. Please ensure the username and password are correct.", "Error", JOptionPane.ERROR_MESSAGE);
	    		loginButton.setText("Log In");
	    		loginButton.setEnabled(true);
	    	}
	    	else{
	    		try {
					populateMyGraphs(this.limit, this.myGraphsOffSet);
					populatePublicGraphs(this.limit, this.publicGraphsOffSet);
					populateSharedGraphs(this.limit, this.sharedGraphsOffSet);
					this.loggedIn = true;
		    		loginButton.setText("Log Out");
		    		loginButton.setEnabled(true);
		    		hostTextField.setEnabled(false);
		    		usernameTextField.setEnabled(false);
		    		passwordField.setEnabled(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		}
		else{
			Server.INSTANCE.logout();
			hostTextField.setEnabled(true);
			usernameTextField.setEnabled(true);
			passwordField.setEnabled(true);
			hostTextField.setText("www.graphspace.org");
			usernameTextField.setText("");
			passwordField.setText("");
		}
	}
	
	public TaskIterator createTaskIterator(Object query) {
		return new TaskIterator();
	}
	
//	public void populateTables() throws Exception{
//		System.out.println("populate table action performed");
//		myGraphsTableModel.setRowCount(0);
//		sharedGraphsTableModel.setRowCount(0);
//		publicGraphsTableModel.setRowCount(0);
////		searchResultsTableModel.setRowCount(0);
//		if (Server.INSTANCE.isAuthenticated()){
//			populateMyGraphs(this.limit, this.myGraphsOffSet);
//			populatePublicGraphs(this.limit, this.publicGraphsOffSet);			
//		}
//	}
	
	private void populateMyGraphs(int limit, int offset) throws Exception{
		System.out.println("populate my graphs table action performed");
		myGraphsTableModel.setRowCount(0);
		ArrayList<GSGraphMetaData> myGraphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(true, false, false, limit, offset);
		for (GSGraphMetaData gsGraphMetaData : myGraphsMetaDataList){
//			String access = "PRIVATE";
//			if (gsGraphMetaData.getAccess()==1){
//				access = "PUBLIC";
//			}
			Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwnedBy()};
			myGraphsTableModel.addRow(row);
		}
	}
	
	private void populatePublicGraphs(int limit, int offset) throws Exception{
		System.out.println("populate public graphs table action performed");
		publicGraphsTableModel.setRowCount(0);
		ArrayList<GSGraphMetaData> publicGraphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(false, false, true, limit, offset);
		for (GSGraphMetaData gsGraphMetaData : publicGraphsMetaDataList){
//			String access = "PRIVATE";
//			if (gsGraphMetaData.getAccess()==1){
//				access = "PUBLIC";
//			}
			Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwnedBy()};
			publicGraphsTableModel.addRow(row);
		}
	}
	
	private void populateSharedGraphs(int limit, int offset) throws Exception{
		System.out.println("populate shared graphs table action performed");
		sharedGraphsTableModel.setRowCount(0);
		ArrayList<GSGraphMetaData> sharedGraphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(false, true, false, limit, offset);
		for (GSGraphMetaData gsGraphMetaData : sharedGraphsMetaDataList){
//			String access = "PRIVATE";
//			if (gsGraphMetaData.getAccess()==1){
//				access = "PUBLIC";
//			}
			Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwnedBy()};
			sharedGraphsTableModel.addRow(row);
		}
	}
	
//	void performSearch(final String query){
//		// searchField.setEnabled(false);
//		// searchButton.setEnabled(false);
//
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				final ResultTask<List<WPPathway>> searchTask = client.newFreeTextSearchTask(query, species);
//				taskManager.execute(new TaskIterator(searchTask, new AbstractTask() {
//					public void run(final TaskMonitor monitor) {
//						final List<WPPathway> results = searchTask.get();
//						if (results.isEmpty()) {
//							noResultsLabel.setText(String.format("<html><b>No results for \'%s\'.</b></html>", query));
//							noResultsLabel.setVisible(true);
//						} else
//							noResultsLabel.setVisible(false);
//
//						setPathwaysInResultsTable(results);
//					}
//				}), new TaskObserver() {
//					public void taskFinished(ObservableTask t) {
//					}
//
//					public void allFinished(FinishStatus status) {
//						searchField.setEnabled(true);
//						searchButton.setEnabled(true);
//					}
//				});
//			}
//		});
//	}
	
	private void getGraphActionPerformed(ActionEvent e, String id){
		System.out.println("get graph action performed");
		try {
			JSONObject graphJSON = Server.INSTANCE.client.getGraphById(id).getJSONObject("body").getJSONObject("object").getJSONObject("graph_json");
			String str = graphJSON.toString();
//			String str = "{ \"format_version\" : \"1.0\", \"generated_by\" : \"cytoscape-3.5.1\", \"target_cytoscapejs_version\" : \"~2.1\", \"data\" : { \"shared_name\" : \"test3\", \"name\" : \"test3\", \"SUID\" : 52, \"__Annotations\" : [ ], \"selected\" : true }, \"elements\" : { \"nodes\" : [ { \"data\" : { \"id\" : \"68\", \"shared_name\" : \"Node 3\", \"name\" : \"Node 3\", \"SUID\" : 68, \"selected\" : false }, \"position\" : { \"x\" : -15.0, \"y\" : 18.0 }, \"selected\" : false }, { \"data\" : { \"id\" : \"66\", \"shared_name\" : \"Node 2\", \"name\" : \"Node 2\", \"SUID\" : 66, \"selected\" : false }, \"position\" : { \"x\" : -117.0, \"y\" : -93.0 }, \"selected\" : false }, { \"data\" : { \"id\" : \"64\", \"shared_name\" : \"Node 1\", \"name\" : \"Node 1\", \"SUID\" : 64, \"selected\" : false }, \"position\" : { \"x\" : -196.0, \"y\" : 36.0 }, \"selected\" : false } ], \"edges\" : [ { \"data\" : { \"id\" : \"72\", \"source\" : \"66\", \"target\" : \"68\", \"shared_name\" : \"Node 2 (interacts with) Node 3\", \"name\" : \"Node 2 (interacts with) Node 3\", \"interaction\" : \"interacts with\", \"SUID\" : 72, \"shared_interaction\" : \"interacts with\", \"selected\" : false }, \"selected\" : false }, { \"data\" : { \"id\" : \"70\", \"source\" : \"64\", \"target\" : \"66\", \"shared_name\" : \"Node 1 (interacts with) Node 2\", \"name\" : \"Node 1 (interacts with) Node 2\", \"interaction\" : \"interacts with\", \"SUID\" : 70, \"shared_interaction\" : \"interacts with\", \"selected\" : false }, \"selected\" : false } ] } }";
			InputStream is = new ByteArrayInputStream(str.getBytes());
			File tempFile = File.createTempFile("CyGraphSpaceImport", ".cyjs");
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(is, out);
	        }
			TaskIterator ti = loadNetworkFileTaskFactory.createTaskIterator(tempFile);
			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
			System.out.println(str);
			tempFile.delete();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog((Component)e.getSource(), "Could not get graph", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	public void openInBrowser(String id){
		openBrowser.openURL(Server.INSTANCE.getHost()+"/graphs/"+id);
	}
	private void getGraphActionPerformed(MouseEvent e, String id){
		System.out.println("get graph action performed");
//		if (resultsTable.getSelectedRow()<0){
//			JOptionPane.showMessageDialog((Component)e.getSource(), "No graph is selected", "Error", JOptionPane.ERROR_MESSAGE);
//		}
//		String id = resultsTable.getValueAt(resultsTable.getSelectedRow(), 0).toString();
		try {
			JSONObject graphJSON = Server.INSTANCE.client.getGraphById(id).getJSONObject("body").getJSONObject("object").getJSONObject("graph_json");
			String str = graphJSON.toString();
//			String str = "{ \"format_version\" : \"1.0\", \"generated_by\" : \"cytoscape-3.5.1\", \"target_cytoscapejs_version\" : \"~2.1\", \"data\" : { \"shared_name\" : \"test3\", \"name\" : \"test3\", \"SUID\" : 52, \"__Annotations\" : [ ], \"selected\" : true }, \"elements\" : { \"nodes\" : [ { \"data\" : { \"id\" : \"68\", \"shared_name\" : \"Node 3\", \"name\" : \"Node 3\", \"SUID\" : 68, \"selected\" : false }, \"position\" : { \"x\" : -15.0, \"y\" : 18.0 }, \"selected\" : false }, { \"data\" : { \"id\" : \"66\", \"shared_name\" : \"Node 2\", \"name\" : \"Node 2\", \"SUID\" : 66, \"selected\" : false }, \"position\" : { \"x\" : -117.0, \"y\" : -93.0 }, \"selected\" : false }, { \"data\" : { \"id\" : \"64\", \"shared_name\" : \"Node 1\", \"name\" : \"Node 1\", \"SUID\" : 64, \"selected\" : false }, \"position\" : { \"x\" : -196.0, \"y\" : 36.0 }, \"selected\" : false } ], \"edges\" : [ { \"data\" : { \"id\" : \"72\", \"source\" : \"66\", \"target\" : \"68\", \"shared_name\" : \"Node 2 (interacts with) Node 3\", \"name\" : \"Node 2 (interacts with) Node 3\", \"interaction\" : \"interacts with\", \"SUID\" : 72, \"shared_interaction\" : \"interacts with\", \"selected\" : false }, \"selected\" : false }, { \"data\" : { \"id\" : \"70\", \"source\" : \"64\", \"target\" : \"66\", \"shared_name\" : \"Node 1 (interacts with) Node 2\", \"name\" : \"Node 1 (interacts with) Node 2\", \"interaction\" : \"interacts with\", \"SUID\" : 70, \"shared_interaction\" : \"interacts with\", \"selected\" : false }, \"selected\" : false } ] } }";
			InputStream is = new ByteArrayInputStream(str.getBytes());
			File tempFile = File.createTempFile("CyGraphSpaceImport", ".cyjs");
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(is, out);
	        }
			TaskIterator ti = loadNetworkFileTaskFactory.createTaskIterator(tempFile);
			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
			System.out.println(str);
			tempFile.delete();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog((Component)e.getSource(), "Could not get graph", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	public void setMyGraphsOffSet(int offset){
		this.myGraphsOffSet = offset;
	}
	
	public void setSharedGraphsOffSet(int offset){
		this.sharedGraphsOffSet = offset;
	}
	
	public void setPublicGraphsOffSet(int offset){
		this.publicGraphsOffSet = offset;
	}

	class MyGraphsNextButtonActionListener implements ActionListener{
		private int offset;
		private int limit;
	    public MyGraphsNextButtonActionListener(int limit, int offset) {
	    	this.limit = limit;
	        this.offset = offset;
	        setMyGraphsOffSet(offset);
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
				populateMyGraphs(limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class MyGraphsPreviousButtonActionListener implements ActionListener{
		private int offset;
		private int limit;
	    public MyGraphsPreviousButtonActionListener(int limit, int offset) {
	    	this.limit = limit;
	        this.offset = offset;
	        setMyGraphsOffSet(offset);
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
				populateMyGraphs(limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class SharedGraphsNextButtonActionListener implements ActionListener{
		private int offset;
		private int limit;
	    public SharedGraphsNextButtonActionListener(int limit, int offset) {
	    	this.limit = limit;
	        this.offset = offset;
	        setSharedGraphsOffSet(offset);
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
				populateSharedGraphs(limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class SharedGraphsPreviousButtonActionListener implements ActionListener{
		private int offset;
		private int limit;
	    public SharedGraphsPreviousButtonActionListener(int limit, int offset) {
	    	this.limit = limit;
	        this.offset = offset;
	        setSharedGraphsOffSet(offset);
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
				populateSharedGraphs(limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class PublicGraphsNextButtonActionListener implements ActionListener{
		private int offset;
		private int limit;
	    public PublicGraphsNextButtonActionListener(int limit, int offset) {
	    	this.limit = limit;
	        this.offset = offset;
	        setPublicGraphsOffSet(this.offset);
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
				populatePublicGraphs(limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}

	class PublicGraphsPreviousButtonActionListener implements ActionListener{
		private int offset;
		private int limit;
	    public PublicGraphsPreviousButtonActionListener(int limit, int offset) {
	    	this.limit = limit;
	        this.offset = offset;
	        setPublicGraphsOffSet(offset);
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
				populatePublicGraphs(limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}

/*	
//	class SearchResultsNextButtonActionListener implements ActionListener{
//		private int offset;
//		private int limit;
//	    public SearchResultsNextButtonActionListener(int limit, int offset) {
//	    	this.limit = limit;
//	        this.offset = offset;
//	        setSearchResultsOffSet(offset);
//	    }
//
//	    public void actionPerformed(ActionEvent e) {
//	        try {
//				populateSearchResults(limit, offset);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	    }
//	}
//	
//	
//	class SearchResultsPreviousButtonActionListener implements ActionListener{
//		private int offset;
//		private int limit;
//	    public SearchResultsPreviousButtonActionListener(int limit, int offset) {
//	    	this.limit = limit;
//	        this.offset = offset;
//	        setSearchResultsOffSet(offset);
//	    }
//
//	    public void actionPerformed(ActionEvent e) {
//	        try {
//				populateSearchResults(limit, offset);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	    }
//	}
	
*/
}