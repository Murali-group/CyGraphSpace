package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.graphspace.javaclient.model.GSGraphMetaData;

public class MyGraphsTablePanel extends JPanel implements CytoPanelComponent {
	
	private static final long serialVersionUID = 8292806967891823933L;

	private JTable myGraphsTable;
	DefaultTableModel myGraphsTableModel;
	
	public MyGraphsTablePanel() {
		
JScrollPane publicGraphsTableScrollPane = new JScrollPane();
		
		myGraphsTable = new JTable();

		myGraphsTableModel = new DefaultTableModel(
	            new Object [][]
	            {
	                {null, null, null, null}
	            },
	            new String []
	            {
	                "Graph ID", "Graph Name", "Owned By", "Access Level"
	            }
	        );
		myGraphsTable.setModel(myGraphsTableModel);
		
        myGraphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        publicGraphsTableScrollPane.setViewportView(myGraphsTable);
		add(publicGraphsTableScrollPane);
		try {
			populateFields();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setVisible(true);
	}

	private void populateFields() throws Exception{
		Server.INSTANCE.authenticate("www.graphspace.org", "rishu.sethi2525@gmail.com", "123456789");
		ArrayList<GSGraphMetaData> graphsMetaDataList = Server.INSTANCE.client.getGraphMetaDataList(true, false, false);
		for (GSGraphMetaData gsGraphMetaData : graphsMetaDataList){
			String access = "PRIVATE";
			if (gsGraphMetaData.getAccess()==1){
				access = "PUBLIC";
			}
			Object[] row = {String.valueOf(gsGraphMetaData.getId()), gsGraphMetaData.getName(), gsGraphMetaData.getOwnedBy(), access};
			myGraphsTableModel.addRow(row);
		}
	}
	
	public Component getComponent() {
		return this;
	}


	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}


	public String getTitle() {
		return "My Graphs Table Panel";
	}


	public Icon getIcon() {
		return null;
	}
}