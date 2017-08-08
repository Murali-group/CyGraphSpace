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
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.IOUtils;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.CreateViewTask;
import org.cytoscape.graphspace.cygraphspace.internal.util.CreateViewTaskFactory;
import org.cytoscape.graphspace.cygraphspace.internal.util.ResultTask;
import org.cytoscape.io.webservice.NetworkImportWebServiceClient;
import org.cytoscape.io.webservice.SearchWebServiceClient;
import org.cytoscape.io.webservice.swing.AbstractWebServiceGUIClient;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.graphspace.javaclient.CyGraphSpaceClient;
import org.graphspace.javaclient.model.GSGraphMetaData;
import org.json.JSONObject;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTabbedPane;

public class GetGraphsPanel extends AbstractWebServiceGUIClient
		implements NetworkImportWebServiceClient{
	static final String APP_DESCRIPTION = "<html>" + "CyGraphSpace App is used to import and export graphs from "
			+ "<a href=\"http://www.grapshace.org\">GraphSpace</a> website. ";

//	final TaskManager taskManager;
	CyGraphSpaceClient client;
	OpenBrowser openBrowser;
	LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
	LoadVizmapFileTaskFactory loadVizmapFileTaskFactory;
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
	private JButton searchButton;
	DefaultTableModel myGraphsTableModel;
	TableRowSorter<TableModel> myGraphsTableSorter;
	DefaultTableModel sharedGraphsTableModel;
	TableRowSorter<TableModel> sharedGraphsTableSorter;
	DefaultTableModel publicGraphsTableModel;
	TableRowSorter<TableModel> publicGraphsTableSorter;
//	DefaultTableModel searchResultsTableModel;
//	TableRowSorter<TableModel> searchResultsTableSorter;
	private JButton importButton;
	private JButton openInBrowserButton;
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
	private JButton myGraphsNextButton;
	private JButton myGraphsPreviousButton;
	private JButton sharedGraphsPreviousButton;
	private JButton sharedGraphsNextButton;
	private JButton publicGraphsNextButton;
	private String searchTerm;
	private JButton publicGraphsPreviousButton;
	private JPanel myGraphsLoadingFrame;
	private JPanel sharedGraphsLoadingFrame;
	private JPanel publicGraphsLoadingFrame;
	private JButton clearSearchButton;
	CreateViewTaskFactory createViewTaskFactory;
	@SuppressWarnings({ "serial", "static-access" })
	public GetGraphsPanel(TaskManager taskManager, OpenBrowser openBrowser) {
		super("http://www.graphspace.org", "GraphSpace", APP_DESCRIPTION);
		this.taskManager = taskManager;
//		this.taskManager = CyObjectManager.INSTANCE.getTaskManager();
		this.client = Server.INSTANCE.client;
		this.openBrowser = openBrowser;
		this.loadNetworkFileTaskFactory = CyObjectManager.INSTANCE.getLoadNetworkFileTaskFactory();
		this.loadVizmapFileTaskFactory = CyObjectManager.INSTANCE.getLoadVizmapFileTaskFactory();
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
		
		myGraphsPreviousButton = new JButton("< Previous Page");
		myGraphsPreviousButton.addActionListener(new MyGraphsPreviousButtonActionListener());
		
		myGraphsNextButton = new JButton("Next Page >");
		myGraphsNextButton.addActionListener(new MyGraphsNextButtonActionListener());
		
		GroupLayout gl_myGraphsPaginationPanel = new GroupLayout(myGraphsPaginationPanel);
		gl_myGraphsPaginationPanel.setHorizontalGroup(
			gl_myGraphsPaginationPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_myGraphsPaginationPanel.createSequentialGroup()
					.addComponent(myGraphsPreviousButton)
					.addPreferredGap(ComponentPlacement.RELATED, 692, Short.MAX_VALUE)
					.addComponent(myGraphsNextButton))
		);
		gl_myGraphsPaginationPanel.setVerticalGroup(
			gl_myGraphsPaginationPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_myGraphsPaginationPanel.createSequentialGroup()
					.addGroup(gl_myGraphsPaginationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(myGraphsNextButton)
						.addComponent(myGraphsPreviousButton))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		myGraphsPaginationPanel.setLayout(gl_myGraphsPaginationPanel);
		
		myGraphsTable = new JTable();
		myGraphsScrollPane.setRowHeaderView(myGraphsTable);
		myGraphsPanel.setLayout(gl_myGraphsPanel);
		myGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owner", "Tags"
	            }
	        ){
			@Override
			public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		myGraphsTable.setModel(myGraphsTableModel);
        myGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myGraphsTable.getColumn("Graph ID").setPreferredWidth(Math.round((myGraphsTable.getPreferredSize().width)* 0.15f));
        myGraphsTable.getColumn("Graph Name").setPreferredWidth(Math.round((myGraphsTable.getPreferredSize().width)* 0.25f));
        myGraphsTable.getColumn("Owner").setPreferredWidth(Math.round((myGraphsTable.getPreferredSize().width)* 0.25f));
        myGraphsTable.getColumn("Tags").setPreferredWidth(Math.round((myGraphsTable.getPreferredSize().width)* 0.35f));
        myGraphsScrollPane.setViewportView(myGraphsTable);
//        myGraphsScrollPane.setViewportView((Component)loadingFrame);
        
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
		
		sharedGraphsPreviousButton = new JButton("< Previous Page");
		sharedGraphsPreviousButton.addActionListener(new SharedGraphsPreviousButtonActionListener());
		sharedGraphsNextButton = new JButton("Next Page >");
		sharedGraphsNextButton.addActionListener(new SharedGraphsNextButtonActionListener());
		
		GroupLayout gl_sharedGraphsPaginationPanel = new GroupLayout(sharedGraphsPaginationPanel);
		gl_sharedGraphsPaginationPanel.setHorizontalGroup(
			gl_sharedGraphsPaginationPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_sharedGraphsPaginationPanel.createSequentialGroup()
					.addComponent(sharedGraphsPreviousButton)
					.addPreferredGap(ComponentPlacement.RELATED, 692, Short.MAX_VALUE)
					.addComponent(sharedGraphsNextButton))
		);
		gl_sharedGraphsPaginationPanel.setVerticalGroup(
			gl_sharedGraphsPaginationPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_sharedGraphsPaginationPanel.createSequentialGroup()
					.addGroup(gl_sharedGraphsPaginationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(sharedGraphsNextButton)
						.addComponent(sharedGraphsPreviousButton))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		sharedGraphsPaginationPanel.setLayout(gl_sharedGraphsPaginationPanel);
		
		sharedGraphsTable = new JTable();
		sharedGraphsScrollPane.setRowHeaderView(sharedGraphsTable);
		sharedGraphsPanel.setLayout(gl_sharedGraphsPanel);
		sharedGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owner", "Tags"
	            }
	        ){
			@Override
			public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		sharedGraphsTable.setModel(sharedGraphsTableModel);
		sharedGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
        sharedGraphsTable.getColumn("Graph ID").setPreferredWidth(Math.round((sharedGraphsTable.getPreferredSize().width)* 0.15f));
        sharedGraphsTable.getColumn("Graph Name").setPreferredWidth(Math.round((sharedGraphsTable.getPreferredSize().width)* 0.25f));
        sharedGraphsTable.getColumn("Owner").setPreferredWidth(Math.round((sharedGraphsTable.getPreferredSize().width)* 0.25f));
        sharedGraphsTable.getColumn("Tags").setPreferredWidth(Math.round((sharedGraphsTable.getPreferredSize().width)* 0.35f));

        sharedGraphsScrollPane.setViewportView(sharedGraphsTable);
        
		publicGraphsPreviousButton = new JButton("< Previous Page");
		publicGraphsPreviousButton.addActionListener(new PublicGraphsPreviousButtonActionListener());
		
		publicGraphsNextButton = new JButton("Next Page >");
		publicGraphsNextButton.addActionListener(new PublicGraphsNextButtonActionListener());
        
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
				.addGroup(gl_publicGraphsPaginationPanel.createSequentialGroup()
					.addComponent(publicGraphsPreviousButton)
					.addPreferredGap(ComponentPlacement.RELATED, 692, Short.MAX_VALUE)
					.addComponent(publicGraphsNextButton))
		);
		gl_publicGraphsPaginationPanel.setVerticalGroup(
			gl_publicGraphsPaginationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_publicGraphsPaginationPanel.createSequentialGroup()
					.addGroup(gl_publicGraphsPaginationPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(publicGraphsPreviousButton)
						.addComponent(publicGraphsNextButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		publicGraphsPaginationPanel.setLayout(gl_publicGraphsPaginationPanel);
		
		publicGraphsTable = new JTable();
		publicGraphsScrollPane.setRowHeaderView(publicGraphsTable);
		publicGraphsPanel.setLayout(gl_publicGraphsPanel);
		publicGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owner", "Tags"
	            }
	        ){
			@Override
			public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		publicGraphsTable.setModel(publicGraphsTableModel);
		publicGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
        publicGraphsTable.getColumn("Graph ID").setPreferredWidth(Math.round((publicGraphsTable.getPreferredSize().width)* 0.15f));
        publicGraphsTable.getColumn("Graph Name").setPreferredWidth(Math.round((publicGraphsTable.getPreferredSize().width)* 0.25f));
        publicGraphsTable.getColumn("Owner").setPreferredWidth(Math.round((publicGraphsTable.getPreferredSize().width)* 0.25f));
        publicGraphsTable.getColumn("Tags").setPreferredWidth(Math.round((publicGraphsTable.getPreferredSize().width)* 0.35f));
        
		publicGraphsScrollPane.setViewportView(publicGraphsTable);
		
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
		
		
//		searchResultsTable.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2){
//					String id = searchResultsTable.getValueAt(searchResultsTable.getSelectedRow(), 0).toString();
//					getGraphActionPerformed(e, id);
//				}
//			}
//		});
		importButton = new JButton("Import to Cytoscape");
		importButton.setEnabled(false);
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
		
		openInBrowserButton = new JButton("Open in GraphSpace");
		openInBrowserButton.setEnabled(false);
		openInBrowserButton.addActionListener(new ActionListener() {
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
				openInBrowser(id);
			}
		});
		
		myGraphsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1){
					importButton.setEnabled(true);
					openInBrowserButton.setEnabled(true);
				}
				if (e.getClickCount() == 2){
					String id = myGraphsTable.getValueAt(myGraphsTable.getSelectedRow(), 0).toString();
					getGraphActionPerformed(e, id);
				}
			}
		});
		sharedGraphsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1){
					importButton.setEnabled(true);
					openInBrowserButton.setEnabled(true);
				}
				if (e.getClickCount() == 2){
					String id = sharedGraphsTable.getValueAt(sharedGraphsTable.getSelectedRow(), 0).toString();
					getGraphActionPerformed(e, id);
				}
			}
		});
		publicGraphsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1){
					importButton.setEnabled(true);
					openInBrowserButton.setEnabled(true);
				}
				if (e.getClickCount() == 2){
					String id = publicGraphsTable.getValueAt(publicGraphsTable.getSelectedRow(), 0).toString();
					getGraphActionPerformed(e, id);
				}
			}
		});
		
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

		searchButton = new JButton("Search");
		searchButton.setEnabled(false);
		
		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String searchText = searchField.getText();

                if (searchText.trim().length() == 0) {
                    searchButton.setEnabled(false);
                } else {
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String searchText = searchField.getText();

                if (searchText.trim().length() == 0) {
                    searchButton.setEnabled(false);
                } else {
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            	String searchText = searchField.getText();
            	if (searchText.trim().length() == 0) {
                    searchButton.setEnabled(false);
                } else {
                    searchButton.setEnabled(true);
                }
            }

        });
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				searchPerformed();
			}
		});
		
		clearSearchButton = new JButton("Clear Search");
		clearSearchButton.setEnabled(false);
		clearSearchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				clearSearch();
			}
			
		});
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(searchLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(searchField, GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clearSearchButton)
					.addContainerGap())
		);
		gl_searchPanel.setVerticalGroup(
			gl_searchPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_searchPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(searchLabel)
						.addComponent(clearSearchButton)
						.addComponent(searchButton))
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
					.addComponent(hostTextField, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(usernameLabel)
					.addGap(4)
					.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_loginPanel.setVerticalGroup(
			gl_loginPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_loginPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_loginPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(hostLabel)
						.addComponent(loginButton)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordLabel)
						.addComponent(usernameLabel))
					.addGap(10))
		);
		
		myGraphsLoadingFrame = new JPanel();
		ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
		myGraphsLoadingFrame.add(new JLabel("Importing Graphs", loading, JLabel.CENTER));
		myGraphsLoadingFrame.setSize(myGraphsPanel.WIDTH, myGraphsPanel.HEIGHT);
		myGraphsLoadingFrame.setVisible(true);
		
		sharedGraphsLoadingFrame = new JPanel();
		sharedGraphsLoadingFrame.add(new JLabel("Importing Graphs", loading, JLabel.CENTER));
		sharedGraphsLoadingFrame.setSize(sharedGraphsPanel.WIDTH, sharedGraphsPanel.HEIGHT);
		sharedGraphsLoadingFrame.setVisible(true);
		
		publicGraphsLoadingFrame = new JPanel();
		publicGraphsLoadingFrame.add(new JLabel("Importing Graphs", loading, JLabel.CENTER));
		publicGraphsLoadingFrame.setSize(publicGraphsPanel.WIDTH, publicGraphsPanel.HEIGHT);
		publicGraphsLoadingFrame.setVisible(true);
		
		
		loginPanel.setLayout(gl_loginPanel);
		parentPanel.setLayout(gl_panel);
		myGraphsNextButton.setEnabled(false);
		sharedGraphsNextButton.setEnabled(false);
		publicGraphsNextButton.setEnabled(false);
		myGraphsPreviousButton.setEnabled(false);
		sharedGraphsPreviousButton.setEnabled(false);
		publicGraphsPreviousButton.setEnabled(false);
		populate();
	}

	private void populate(){
		if (Server.INSTANCE.isAuthenticated()){
			try {
				this.loggedIn = true;
				loginButton.setText("Log Out");
				hostTextField.setText(Server.INSTANCE.getHost());
				usernameTextField.setText(Server.INSTANCE.getUsername());
				passwordField.setText(Server.INSTANCE.getPassword());
				hostTextField.setEnabled(false);
				usernameTextField.setEnabled(false);
				passwordField.setEnabled(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			loginButton.setText("Log In");
		}
	}
	
	private void clearSearch(){
		importGraphListActionPerformed();
		clearSearchButton.setEnabled(false);
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
	    			this.loggedIn = true;
	    			loginButton.setText("Log Out");
		    		loginButton.setEnabled(true);
		    		hostTextField.setEnabled(false);
		    		usernameTextField.setEnabled(false);
		    		passwordField.setEnabled(false);
		    		importGraphListActionPerformed();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		}
		else{
			Server.INSTANCE.logout();
			this.loggedIn = false;
			hostTextField.setEnabled(true);
			usernameTextField.setEnabled(true);
			passwordField.setEnabled(true);
			myGraphsTableModel.setRowCount(0);
			sharedGraphsTableModel.setRowCount(0);
			publicGraphsTableModel.setRowCount(0);
			importButton.setEnabled(false);
			openInBrowserButton.setEnabled(false);
			loginButton.setText("Log In");
			hostTextField.setText("www.graphspace.org");
			usernameTextField.setText("");
			passwordField.setText("");
			myGraphsNextButton.setEnabled(false);
			sharedGraphsNextButton.setEnabled(false);
			publicGraphsNextButton.setEnabled(false);
			myGraphsPreviousButton.setEnabled(false);
			sharedGraphsPreviousButton.setEnabled(false);
			publicGraphsPreviousButton.setEnabled(false);
			myGraphsLoadingFrame.setVisible(false);
			sharedGraphsLoadingFrame.setVisible(false);
			publicGraphsLoadingFrame.setVisible(false);
		}
	}
	
	private void importGraphListActionPerformed(){
		try {
			this.searchTerm = null;
//			JDialog waitDialog = new JDialog();
//			JLabel waitLabel = new JLabel("Graphs are currently being imported from GraphSpace. Please Wait...");
//			waitDialog.setLocationRelativeTo(null);
//			waitDialog.setTitle("Please Wait...");
//			waitDialog.getContentPane().add(waitLabel);
//			waitDialog.pack();
//			waitDialog.setVisible(true);
			myGraphsLoadingFrame.setVisible(true);
			sharedGraphsLoadingFrame.setVisible(true);
			publicGraphsLoadingFrame.setVisible(true);
			myGraphsScrollPane.setViewportView(myGraphsLoadingFrame);
			sharedGraphsScrollPane.setViewportView(sharedGraphsLoadingFrame);
			publicGraphsScrollPane.setViewportView(publicGraphsLoadingFrame);
			SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

	            @Override
	            protected Integer doInBackground() throws Exception{
	            	{
	        			populateMyGraphs(null, limit, myGraphsOffSet);
	        			populateSharedGraphs(null, limit, sharedGraphsOffSet);
	        			populatePublicGraphs(null, limit, publicGraphsOffSet);
	        			myGraphsScrollPane.setViewportView(myGraphsTable);
	        			sharedGraphsScrollPane.setViewportView(sharedGraphsTable);
	        			publicGraphsScrollPane.setViewportView(publicGraphsTable);
	        			myGraphsLoadingFrame.setVisible(false);
	        			sharedGraphsLoadingFrame.setVisible(false);
	        			publicGraphsLoadingFrame.setVisible(false);
	        		}
	                return 1;
	            }
			};
			
			worker.execute();
			myGraphsNextButton.setEnabled(true);
			sharedGraphsNextButton.setEnabled(true);
			publicGraphsNextButton.setEnabled(true);
			myGraphsPreviousButton.setEnabled(true);
			sharedGraphsPreviousButton.setEnabled(true);
			publicGraphsPreviousButton.setEnabled(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void searchPerformed(){
		clearSearchButton.setEnabled(true);
		this.searchTerm = searchField.getText();
		System.out.println(searchTerm);
		try {
			myGraphsTableModel.setRowCount(0);
			sharedGraphsTableModel.setRowCount(0);
			publicGraphsTableModel.setRowCount(0);
			myGraphsLoadingFrame.setVisible(true);
			sharedGraphsLoadingFrame.setVisible(true);
			publicGraphsLoadingFrame.setVisible(true);
			myGraphsScrollPane.setViewportView(myGraphsLoadingFrame);
			sharedGraphsScrollPane.setViewportView(sharedGraphsLoadingFrame);
			publicGraphsScrollPane.setViewportView(publicGraphsLoadingFrame);
			myGraphsOffSet = 0;
			sharedGraphsOffSet = 0;
			publicGraphsOffSet = 0;
			SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

	            @Override
	            protected Integer doInBackground() throws Exception{
	            	{
	            		ArrayList<GSGraphMetaData> myGraphsSearchResults = Server.INSTANCE.searchGraphs(searchTerm, true, false, false, limit, myGraphsOffSet);
	        			ArrayList<GSGraphMetaData> sharedGraphsSearchResults = Server.INSTANCE.searchGraphs(searchTerm, false, true, false, limit, sharedGraphsOffSet);
	        			ArrayList<GSGraphMetaData> publicGraphsSearchResults = Server.INSTANCE.searchGraphs(searchTerm, false, false, true, limit, publicGraphsOffSet);
	        			for (GSGraphMetaData gsGraphMetaData : myGraphsSearchResults){
	        				String tags = "";
	        				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
	        					tags += gsGraphMetaData.getTags().get(i)+", ";
	        				}
	        				if(tags.length()>0){
	        					tags = tags.substring(0, tags.length()-2);
	        				}
	        				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
	        				myGraphsTableModel.addRow(row);
	        			}
	        			for (GSGraphMetaData gsGraphMetaData : sharedGraphsSearchResults){
	        				String tags = "";
	        				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
	        					tags += gsGraphMetaData.getTags().get(i)+", ";
	        				}
	        				if(tags.length()>0){
	        					tags = tags.substring(0, tags.length()-2);
	        				}
	        				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
	        				sharedGraphsTableModel.addRow(row);
	        			}
	        			for (GSGraphMetaData gsGraphMetaData : publicGraphsSearchResults){
	        				String tags = "";
	        				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
	        					tags += gsGraphMetaData.getTags().get(i)+", ";
	        				}
	        				if(tags.length()>0){
	        					tags = tags.substring(0, tags.length()-2);
	        				}
	        				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
	        				publicGraphsTableModel.addRow(row);
	        			}
	        			myGraphsScrollPane.setViewportView(myGraphsTable);
	        			sharedGraphsScrollPane.setViewportView(sharedGraphsTable);
	        			publicGraphsScrollPane.setViewportView(publicGraphsTable);
	        			myGraphsLoadingFrame.setVisible(false);
	        			sharedGraphsLoadingFrame.setVisible(false);
	        			publicGraphsLoadingFrame.setVisible(false);
	        		}
	                return 1;
	            }
			};
			
			worker.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void populateMyGraphs(String searchTerm, int limit, int offset) throws Exception{
		if (searchTerm == null){
			myGraphsTableModel.setRowCount(0);
			ArrayList<GSGraphMetaData> myGraphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(true, false, false, limit, offset);
			for (GSGraphMetaData gsGraphMetaData : myGraphsMetaDataList){
				String tags = "";
				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
					tags += gsGraphMetaData.getTags().get(i)+", ";
				}
				if(tags.length()>0){
					tags = tags.substring(0, tags.length()-2);
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
				myGraphsTableModel.addRow(row);
			}
		}
		else{
			myGraphsTableModel.setRowCount(0);
			ArrayList<GSGraphMetaData> myGraphsSearchResults = Server.INSTANCE.searchGraphs(searchTerm, true, false, false, limit, offset);
			for (GSGraphMetaData gsGraphMetaData : myGraphsSearchResults){
				String tags = "";
				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
					tags += gsGraphMetaData.getTags().get(i)+", ";
				}
				if(tags.length()>0){
					tags = tags.substring(0, tags.length()-2);
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
				myGraphsTableModel.addRow(row);
			}
		}
	}
	
	private void populatePublicGraphs(String searchTerm, int limit, int offset) throws Exception{
		if(searchTerm==null){
			System.out.println("populate public graphs table action performed");
			publicGraphsTableModel.setRowCount(0);
			ArrayList<GSGraphMetaData> publicGraphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(false, false, true, limit, offset);
			for (GSGraphMetaData gsGraphMetaData : publicGraphsMetaDataList){
				String tags = "";
				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
					tags += gsGraphMetaData.getTags().get(i)+", ";
				}
				if(tags.length()>0){
					tags = tags.substring(0, tags.length()-2);
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
				publicGraphsTableModel.addRow(row);
			}
		}
		else{
			System.out.println("populate public graphs table action performed");
			publicGraphsTableModel.setRowCount(0);
			ArrayList<GSGraphMetaData> publicGraphsSearchResults = Server.INSTANCE.searchGraphs(searchTerm, false, false, true, limit, offset);
			for (GSGraphMetaData gsGraphMetaData : publicGraphsSearchResults){
				String tags = "";
				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
					tags += gsGraphMetaData.getTags().get(i)+", ";
				}
				if(tags.length()>0){
					tags = tags.substring(0, tags.length()-2);
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
				publicGraphsTableModel.addRow(row);
			}
		}
	}
	
	private void populateSharedGraphs(String searchTerm, int limit, int offset) throws Exception{
		if(searchTerm==null){
			System.out.println("populate shared graphs table action performed");
			sharedGraphsTableModel.setRowCount(0);
			ArrayList<GSGraphMetaData> sharedGraphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(false, true, false, limit, offset);
			for (GSGraphMetaData gsGraphMetaData : sharedGraphsMetaDataList){
				String tags = "";
				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
					tags += gsGraphMetaData.getTags().get(i)+", ";
				}
				if(tags.length()>0){
					tags = tags.substring(0, tags.length()-2);
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
				sharedGraphsTableModel.addRow(row);
			}
		}
		else{
			System.out.println("populate shared graphs table action performed");
			sharedGraphsTableModel.setRowCount(0);
			ArrayList<GSGraphMetaData> sharedGraphsSearchResults = Server.INSTANCE.searchGraphs(searchTerm, false, true, false, limit, offset);
			for (GSGraphMetaData gsGraphMetaData : sharedGraphsSearchResults){
				String tags = "";
				for (int i=0; i<gsGraphMetaData.getTags().size(); i++){
					tags += gsGraphMetaData.getTags().get(i)+", ";
				}
				if(tags.length()>0){
					tags = tags.substring(0, tags.length()-2);
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwner(), tags};
				sharedGraphsTableModel.addRow(row);
			}
		}
	}
	
	private void getGraphActionPerformed(ActionEvent e, String id){
		System.out.println("get graph action performed");
		try {
			JSONObject response = Server.INSTANCE.client.getGraphById(id);
			JSONObject graphJSON = response.getJSONObject("body").getJSONObject("object").getJSONObject("graph_json");
			JSONObject styleJSON = response.getJSONObject("body").getJSONObject("object").getJSONObject("style_json");
			String graphJSONString = graphJSON.toString();
			String styleJSONString = styleJSON.toString();
			InputStream graphJSONInputStream = new ByteArrayInputStream(graphJSONString.getBytes());
//			InputStream styleJSONInputStream = new ByteArrayInputStream(styleJSONString.getBytes());
			File tempFile = File.createTempFile("CyGraphSpaceImport", ".cyjs");
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(graphJSONInputStream, out);
	        }
			TaskIterator ti = loadNetworkFileTaskFactory.createTaskIterator(tempFile);
			
			tempFile.delete();
//			CreateViewTask createViewTask = new CreateViewTask();
//			ti.append(createViewTask);
//			CreateViewTaskFactory createViewTaskFactory = new CreateViewTaskFactory();
//			ti.append(createViewTaskFactory.createTaskIterator());
			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
//			CyNetworkView networkView = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetworkView();
//			int count = 0;
//			while (networkView == null) {
//				Thread.sleep(1000);
//				count++;
//				networkView = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetworkView();
//				System.out.println(count);
//			}
//	    	Collection<View<CyNode>> cyNodeViews = networkView.getNodeViews();
//	    	for (View<CyNode> cyNodeView : cyNodeViews) {
//	    		cyNodeView.setVisualProperty(BasicVisualLexicon.NODE_HEIGHT, 100);
//	    	}
//			ti = 
//			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
			
//			CyObjectManager.INSTANCE.getNetworkViewFactory().createNetworkView(arg0);
//			tempFile = File.createTempFile("CyGraphSpaceStyleImport", ".json");
//			try (FileOutputStream out = new FileOutputStream(tempFile)) {
//	            IOUtils.copy(styleJSONInputStream, out);
//	        }
//			ti = loadVizmapFileTaskFactory.createTaskIterator(tempFile);
//			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
////			System.out.println(str);
//			tempFile.delete();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog((Component)e.getSource(), "Could not get graph", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	public void openInBrowser(String id){
		if (id==null){
			JOptionPane.showMessageDialog(new JDialog(), "Please select a graph", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		openBrowser.openURL(Server.INSTANCE.getHost()+"/graphs/"+id);
	}
	
	private void getGraphActionPerformed(MouseEvent e, String id){
		System.out.println("get graph action performed");
		try {
			JSONObject graphJSON = Server.INSTANCE.client.getGraphById(id).getJSONObject("body").getJSONObject("object").getJSONObject("graph_json");
			String str = graphJSON.toString();
			InputStream is = new ByteArrayInputStream(str.getBytes());
			File tempFile = File.createTempFile("CyGraphSpaceImport", ".cyjs");
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(is, out);
	        }
			TaskIterator ti = loadNetworkFileTaskFactory.createTaskIterator(tempFile);
			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
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
	    public MyGraphsNextButtonActionListener() {
	    	super();
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
	        	int offset = myGraphsOffSet+20;
	        	setMyGraphsOffSet(offset);
	        	myGraphsTableModel.setRowCount(0);
				myGraphsLoadingFrame.setVisible(true);
				myGraphsScrollPane.setViewportView(myGraphsLoadingFrame);
				SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

		            @Override
		            protected Integer doInBackground() throws Exception{
		            	{
		            		populateMyGraphs(searchTerm, limit, offset);
		            		myGraphsScrollPane.setViewportView(myGraphsTable);
		        			myGraphsLoadingFrame.setVisible(false);
		        			
		        		}
		                return 1;
		            }
				};
				
				worker.execute();
//				populateMyGraphs(searchTerm, limit, offset);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class MyGraphsPreviousButtonActionListener implements ActionListener{
	    public MyGraphsPreviousButtonActionListener() {
	    	super();
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
	        	int offset = myGraphsOffSet-20;
	        	setMyGraphsOffSet(offset);
	        	myGraphsTableModel.setRowCount(0);
				myGraphsLoadingFrame.setVisible(true);
				myGraphsScrollPane.setViewportView(myGraphsLoadingFrame);
				SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

		            @Override
		            protected Integer doInBackground() throws Exception{
		            	{
		            		populateMyGraphs(searchTerm, limit, offset);
		            		myGraphsScrollPane.setViewportView(myGraphsTable);
		        			myGraphsLoadingFrame.setVisible(false);
		        			
		        		}
		                return 1;
		            }
				};
				worker.execute();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class SharedGraphsNextButtonActionListener implements ActionListener{
	    public SharedGraphsNextButtonActionListener() {
	    	super();
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
	        	int offset = sharedGraphsOffSet+20;
	        	setSharedGraphsOffSet(offset);
	        	sharedGraphsTableModel.setRowCount(0);
				sharedGraphsLoadingFrame.setVisible(true);
				sharedGraphsScrollPane.setViewportView(sharedGraphsLoadingFrame);
				SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

		            @Override
		            protected Integer doInBackground() throws Exception{
		            	{
		            		populateSharedGraphs(searchTerm, limit, offset);
		            		sharedGraphsScrollPane.setViewportView(sharedGraphsTable);
		        			sharedGraphsLoadingFrame.setVisible(false);
		        			
		        		}
		                return 1;
		            }
				};
				
				worker.execute();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class SharedGraphsPreviousButtonActionListener implements ActionListener{
	    public SharedGraphsPreviousButtonActionListener() {
	    	super();
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
	        	int offset = sharedGraphsOffSet-20;
	        	setSharedGraphsOffSet(offset);
	        	sharedGraphsTableModel.setRowCount(0);
				sharedGraphsLoadingFrame.setVisible(true);
				sharedGraphsScrollPane.setViewportView(sharedGraphsLoadingFrame);
				SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

		            @Override
		            protected Integer doInBackground() throws Exception{
		            	{
		            		populateMyGraphs(searchTerm, limit, offset);
		            		sharedGraphsScrollPane.setViewportView(sharedGraphsTable);
		        			sharedGraphsLoadingFrame.setVisible(false);

		        		}
		                return 1;
		            }
				};
				worker.execute();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	
	class PublicGraphsNextButtonActionListener implements ActionListener{
	    public PublicGraphsNextButtonActionListener() {
	    	super();
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
	        	int offset = publicGraphsOffSet+20;
	        	setPublicGraphsOffSet(offset);
	        	publicGraphsTableModel.setRowCount(0);
				publicGraphsLoadingFrame.setVisible(true);
				publicGraphsScrollPane.setViewportView(publicGraphsLoadingFrame);
				SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

		            @Override
		            protected Integer doInBackground() throws Exception{
		            	{
		            		populatePublicGraphs(searchTerm, limit, offset);
		            		publicGraphsScrollPane.setViewportView(publicGraphsTable);
		        			publicGraphsLoadingFrame.setVisible(false);
		        			
		        		}
		                return 1;
		            }
				};
				worker.execute();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}

	class PublicGraphsPreviousButtonActionListener implements ActionListener{
	    public PublicGraphsPreviousButtonActionListener() {
	    	super();
	    }

	    public void actionPerformed(ActionEvent e) {
	        try {
	        	int offset = publicGraphsOffSet-20;
	        	setPublicGraphsOffSet(offset);
	        	publicGraphsTableModel.setRowCount(0);
				publicGraphsLoadingFrame.setVisible(true);
				publicGraphsScrollPane.setViewportView(publicGraphsLoadingFrame);
				SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>(){

		            @Override
		            protected Integer doInBackground() throws Exception{
		            	{
		            		populatePublicGraphs(searchTerm, limit, offset);
		            		publicGraphsScrollPane.setViewportView(publicGraphsTable);
		        			publicGraphsLoadingFrame.setVisible(false);
		        			
		        		}
		                return 1;
		            }
				};
				
				worker.execute();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
//	private static void setWidthAsPercentages(JTable table,
//	        double... percentages) {
//	    final double factor = 10000;
//	 
//	    TableModel model = table.getModel();
//	    for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
//	        TableColumn column = model.getColumn(columnIndex);
//	        column.setPreferredWidth((int) (percentages[columnIndex] * factor));
//	    }
//	}
}