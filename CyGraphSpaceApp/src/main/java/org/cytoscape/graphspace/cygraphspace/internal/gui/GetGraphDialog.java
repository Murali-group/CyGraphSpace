package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.cytoscape.work.TaskIterator;
import org.graphspace.javaclient.model.GSGraphMetaData;
import org.json.JSONObject;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.*;

import org.apache.commons.io.IOUtils;

public class GetGraphDialog extends JDialog{
	private JTextField searchField;
	private JCheckBox publicGraphsCheckBox;
	private JCheckBox myGraphsCheckBox;
	private JTable graphsTable;
	private JCheckBox sharedGraphsCheckBox;
	private Frame parent;
	DefaultTableModel graphsTableModel;
	TableRowSorter<TableModel> graphsTableSorter;
	public GetGraphDialog(Frame parent){
		
		this.parent = parent;
		this.setTitle("Get graph from GraphSpace");
		
		JPanel infoPanel = new JPanel();

//		searchField.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				searchActionPerformed(e);
//			}
//		});
		JLabel searchLabel = new JLabel("Search");
		
		JPanel checkBoxesPanel = new JPanel();
		
		JScrollPane graphsTableScrollPane = new JScrollPane();
		
		graphsTable = new JTable();
		graphsTable.setBounds(28, 152, 695, 311);
		graphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owned By", "Access Level"
	            }
	        );
		graphsTable.setModel(graphsTableModel);
		
        graphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        graphsTableScrollPane.setViewportView(graphsTable);
        
        
		searchField = new JTextField();
		searchField.setColumns(10);
		
		graphsTableSorter = new TableRowSorter<TableModel>(graphsTableModel);
		graphsTable.setRowSorter(graphsTableSorter);

		searchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String searchText = searchField.getText();

                if (searchText.trim().length() == 0) {
                    graphsTableSorter.setRowFilter(null);
                } else {
                    graphsTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    graphsTableSorter.setRowFilter(null);
                } else {
                    graphsTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
		
		JPanel buttonsPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(graphsTableScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
						.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(checkBoxesPanel, GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(searchLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
							.addComponent(searchField, GroupLayout.PREFERRED_SIZE, 583, GroupLayout.PREFERRED_SIZE))
						.addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(searchLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(checkBoxesPanel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(graphsTableScrollPane, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5))
		);
		
		publicGraphsCheckBox = new JCheckBox("Public Graphs");
		
		publicGraphsCheckBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				try {
					checkBoxClicked(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		sharedGraphsCheckBox = new JCheckBox("Shared Graphs");
		
		myGraphsCheckBox = new JCheckBox("My Graphs");
		myGraphsCheckBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				try {
					checkBoxClicked(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GroupLayout gl_checkBoxesPanel = new GroupLayout(checkBoxesPanel);
		gl_checkBoxesPanel.setHorizontalGroup(
			gl_checkBoxesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_checkBoxesPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(publicGraphsCheckBox)
					.addGap(197)
					.addComponent(myGraphsCheckBox, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
					.addComponent(sharedGraphsCheckBox))
		);
		gl_checkBoxesPanel.setVerticalGroup(
			gl_checkBoxesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_checkBoxesPanel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_checkBoxesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(publicGraphsCheckBox)
						.addComponent(myGraphsCheckBox)
						.addComponent(sharedGraphsCheckBox)))
		);
		checkBoxesPanel.setLayout(gl_checkBoxesPanel);
		
		JButton getGraphButton = new JButton("Get graph");
		getGraphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getGraphActionPerformed(e);
			}
		});
		buttonsPanel.add(getGraphButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				cancelActionPerformed(evt);
			}
		});
		buttonsPanel.add(cancelButton);
		
		JLabel hostLabel = new JLabel("Host");
		
		JLabel usernameLabel = new JLabel("User");
		
		JLabel hostValueLabel = new JLabel("None");
		if (Server.INSTANCE.getHost()!=null){
			hostValueLabel.setText(Server.INSTANCE.getHost());
		}
			
		//Set font bold
		Font font = hostValueLabel.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		hostValueLabel.setFont(boldFont);
		
		JLabel usernameValueLabel = new JLabel("None");
		if (Server.INSTANCE.getUsername()!=null){
			usernameValueLabel.setText(Server.INSTANCE.getUsername());
		}
		//Set font bold
		font = usernameValueLabel.getFont();
		boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		usernameValueLabel.setFont(boldFont);
		
		GroupLayout gl_infoPanel = new GroupLayout(infoPanel);
		gl_infoPanel.setHorizontalGroup(
			gl_infoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(hostLabel)
						.addComponent(usernameLabel))
					.addGap(140)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(usernameValueLabel)
						.addComponent(hostValueLabel))
					.addContainerGap(478, Short.MAX_VALUE))
		);
		
		gl_infoPanel.setVerticalGroup(
			gl_infoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_infoPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(hostLabel)
						.addComponent(hostValueLabel))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_infoPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(usernameLabel)
						.addComponent(usernameValueLabel))
					.addContainerGap())
		);
		infoPanel.setLayout(gl_infoPanel);
		getContentPane().setLayout(groupLayout);
		pack();
	}
	
	private void cancelActionPerformed(ActionEvent evt) {
		System.out.println("cancel action performed");
		this.dispose();
//        setVisible(false);
    }
	
	private void getGraphActionPerformed(ActionEvent e){
		System.out.println("get graph action performed");
		if (graphsTable.getSelectedRow()<0){
			JOptionPane.showMessageDialog((Component)e.getSource(), "No graph is selected", "Error", JOptionPane.ERROR_MESSAGE);
		}
//		System.out.println("hum yaha hain: "+graphsTable.getValueAt(graphsTable.getSelectedRow(), 0));
		String id = graphsTable.getValueAt(graphsTable.getSelectedRow(), 0).toString();
		try {
			JSONObject graphJSON = Server.INSTANCE.client.getGraph(id).getJSONObject("body").getJSONObject("object").getJSONObject("graph_json");
//			System.out.println(graphJSON.toString());
			String name = graphJSON.getJSONObject("data").getString("name");
			String str = graphJSON.toString();
//			String str = "{ \"format_version\" : \"1.0\", \"generated_by\" : \"cytoscape-3.5.1\", \"target_cytoscapejs_version\" : \"~2.1\", \"data\" : { \"shared_name\" : \"test3\", \"name\" : \"test3\", \"SUID\" : 52, \"__Annotations\" : [ ], \"selected\" : true }, \"elements\" : { \"nodes\" : [ { \"data\" : { \"id\" : \"68\", \"shared_name\" : \"Node 3\", \"name\" : \"Node 3\", \"SUID\" : 68, \"selected\" : false }, \"position\" : { \"x\" : -15.0, \"y\" : 18.0 }, \"selected\" : false }, { \"data\" : { \"id\" : \"66\", \"shared_name\" : \"Node 2\", \"name\" : \"Node 2\", \"SUID\" : 66, \"selected\" : false }, \"position\" : { \"x\" : -117.0, \"y\" : -93.0 }, \"selected\" : false }, { \"data\" : { \"id\" : \"64\", \"shared_name\" : \"Node 1\", \"name\" : \"Node 1\", \"SUID\" : 64, \"selected\" : false }, \"position\" : { \"x\" : -196.0, \"y\" : 36.0 }, \"selected\" : false } ], \"edges\" : [ { \"data\" : { \"id\" : \"72\", \"source\" : \"66\", \"target\" : \"68\", \"shared_name\" : \"Node 2 (interacts with) Node 3\", \"name\" : \"Node 2 (interacts with) Node 3\", \"interaction\" : \"interacts with\", \"SUID\" : 72, \"shared_interaction\" : \"interacts with\", \"selected\" : false }, \"selected\" : false }, { \"data\" : { \"id\" : \"70\", \"source\" : \"64\", \"target\" : \"66\", \"shared_name\" : \"Node 1 (interacts with) Node 2\", \"name\" : \"Node 1 (interacts with) Node 2\", \"interaction\" : \"interacts with\", \"SUID\" : 70, \"shared_interaction\" : \"interacts with\", \"selected\" : false }, \"selected\" : false } ] } }";
//			System.out.println(str);
			InputStream is = new ByteArrayInputStream(str.getBytes());
//			Path tempDir = Files.createTempDirectory("tempCyGraphSpaceDirectory");
			File tempFile = File.createTempFile("CyGraphSpace", ".cyjs");
//			tempFile.deleteOnExit();
			try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(is, out);
	        }
			InputStream is2 = new FileInputStream(tempFile);
			String fileContents = IOUtils.toString(is2, "UTF-8");
			System.out.println(fileContents);
			TaskIterator ti = CyObjectManager.INSTANCE.getLoadNetworkFileTaskFactory().createTaskIterator(tempFile);
			CyObjectManager.INSTANCE.getTaskManager().execute(ti);
			tempFile.delete();
//			createNetwork(null, "test", is);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog((Component)e.getSource(), "Could not get graph", "Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
			e1.printStackTrace();
		}
		this.dispose();
	}
	
	private void checkBoxClicked(ActionEvent e) throws Exception{
		populateTable(myGraphsCheckBox.isSelected(), publicGraphsCheckBox.isSelected(), false);
	}
	
	private void populateTable(boolean myGraphsSelected, boolean publicGraphsSelected, boolean sharedGraphsSelected) throws Exception{
		System.out.println("populate table action performed");
		graphsTableModel.setRowCount(0);
		if (!Server.INSTANCE.isAuthenticated()){
			new AuthenticationDialog(this.parent);
		}
		else{
			ArrayList<GSGraphMetaData> graphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(myGraphsSelected, publicGraphsSelected, sharedGraphsSelected);
			for (GSGraphMetaData gsGraphMetaData : graphsMetaDataList){
				String access = "PRIVATE";
				if (gsGraphMetaData.getAccess()==1){
					access = "PUBLIC";
				}
				Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwnedBy(), access};
				graphsTableModel.addRow(row);
			}
		}

	}
	
//	private void searchActionPerformed(ActionEvent evt) {  
//		System.out.println("search action performed");
//	    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(graphsTable.getModel()); 
//	    sorter.setRowFilter(RowFilter.regexFilter(searchField.getText()));
//	    graphsTable.setRowSorter(sorter);
//	}
	
//	private void loadGraph(InputStream is) throws Exception {
//		CyApplicationManager appManager = CyObjectManager.INSTANCE.getApplicationManager();
//		CyNetworkFactory networkFactory = CyObjectManager.INSTANCE.getNetworkFactory();
//		CyNetworkManager networkManager = CyObjectManager.INSTANCE.getNetworkManager();
//		
//		CyRootNetworkManager rootNetworkManager = CyObjectManager.INSTANCE.getRootNetworkManager();
//		CytoscapeJsNetworkReader reader = new CytoscapeJsNetworkReader(null, is, appManager, networkFactory,
//				networkManager, rootNetworkManager);
//		CyObjectManager.INSTANCE.adapter.getTaskManager().execute(new TaskIterator(reader));
//		is.close();
//	}
	
//	public void createNetwork(String collection, String title, final InputStream is) {
//		CyObjectManager.INSTANCE.getApplicationManager().setCurrentNetworkView(null);
//		CyObjectManager.INSTANCE.getApplicationManager().setCurrentNetwork(null);
//	
//		String collectionName = null;
//		if (collection != null) {
//			collectionName = collection;
//		}
//		final TaskIterator it;
//		InputStreamTaskFactory cytoscapeJsReaderFactory = CyObjectManager.INSTANCE.getCytoscapeJsReaderFactory();
//		it = cytoscapeJsReaderFactory.createTaskIterator(is, collection);
//		CyNetworkReader reader = (CyNetworkReader) it.next();
//		try {
//			reader.run(new HeadlessTaskMonitor());
////			CyObjectManager.INSTANCE.adapter.getTaskManager().execute(new TaskIterator(reader));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		CyNetwork[] networks = reader.getNetworks();
//		CyNetwork newNetwork = networks[0];
//		if(title!= null && title.isEmpty() == false) {
//			try {
//				newNetwork.getTable(CyNetwork.class, CyNetwork.LOCAL_ATTRS).getRow(newNetwork.getSUID()).set(CyNetwork.NAME, title);
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//		addNetwork(networks, reader, collectionName);
//		try {
//			is.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//	private void addNetwork(final CyNetwork[] networks, final CyNetworkReader reader, final String collectionName) {
//		addNetwork(networks, reader, collectionName, false);
//	}
//	
//	private void addNetwork(final CyNetwork[] networks, final CyNetworkReader reader, final String collectionName,
//		final Boolean applyStyle) {
//		
//		final List<CyNetworkView> results = new ArrayList<CyNetworkView>();
//		final Map<CyNetworkView, VisualStyle> styleMap = new HashMap<>();
//
//		final Set<CyRootNetwork> rootNetworks = new HashSet<CyRootNetwork>();
//
//		for (final CyNetwork net : CyObjectManager.INSTANCE.getNetworkManager().getNetworkSet()) {
//			final CyRootNetwork rootNet = CyObjectManager.INSTANCE.getRootNetworkManager().getRootNetwork(net);
//			rootNetworks.add(rootNet);
//		}
//		
//		for (final CyNetwork network : networks) {
//			String networkName = network.getRow(network).get(CyNetwork.NAME, String.class);
//			if (networkName == null || networkName.trim().length() == 0) {
//				if (networkName == null)
//					networkName = collectionName;
//				network.getRow(network).set(CyNetwork.NAME, networkName);
//			}
//			CyObjectManager.INSTANCE.getNetworkManager().addNetwork(network);
//			final int numGraphObjects = network.getNodeCount() + network.getEdgeCount();
//			int viewThreshold = 200000;
//			if (numGraphObjects < viewThreshold) {
//
//				final CyNetworkView view = reader.buildCyNetworkView(network);
//				VisualStyle style = CyObjectManager.INSTANCE.getVisualMappingManager().getVisualStyle(view);
//				if (style == null) {
//					style = CyObjectManager.INSTANCE.getVisualMappingManager().getDefaultVisualStyle();
//				}
//				styleMap.put(view, style);
//				CyObjectManager.INSTANCE.getNetworkViewManager().addNetworkView(view);
//				results.add(view);
//			}
//			
//
//		}
//		// If this is a subnetwork, and there is only one subnetwork in the
//		// root, check the name of the root network
//		// If there is no name yet for the root network, set it the same as its
//		// base subnetwork
//		if (networks.length == 1) {
//			if (networks[0] instanceof CySubNetwork) {
//				CySubNetwork subnet = (CySubNetwork) networks[0];
//				final CyRootNetwork rootNet = subnet.getRootNetwork();
//				String rootNetName = rootNet.getRow(rootNet).get(CyNetwork.NAME, String.class);
//				rootNet.getRow(rootNet).set(CyNetwork.NAME, collectionName);
//				if (rootNetName == null || rootNetName.trim().length() == 0) {
//					// The root network does not have a name yet, set it the same
//					// as the base subnetwork
//					rootNet.getRow(rootNet).set(CyNetwork.NAME, collectionName);
//				}
//			}
//		}
//	}

}