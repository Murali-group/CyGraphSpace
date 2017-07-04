package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;

public class PublicGraphsTablePanelAction extends AbstractCyAction {

	private static final long serialVersionUID = 1L;
	private CySwingApplication desktopApp;
	private final CytoPanel cytoPanelEast;
	private PublicGraphsTablePanel publicGraphsTablePanel;
	
	public PublicGraphsTablePanelAction(CySwingApplication desktopApp,
			PublicGraphsTablePanel publicGraphsTablePanel){
		// Add a menu item -- Apps->sample02
		super("Control Panel");
//		setPreferredMenu("Apps.Samples");

		this.desktopApp = desktopApp;
		
		//Note: myControlPanel is bean we defined and registered as a service
		this.cytoPanelEast = this.desktopApp.getCytoPanel(CytoPanelName.EAST);
		this.publicGraphsTablePanel = publicGraphsTablePanel;
	}
	
	/**
	 *  DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void actionPerformed(ActionEvent e) {
		// If the state of the cytoPanelWest is HIDE, show it
		if (cytoPanelEast.getState() == CytoPanelState.HIDE) {
			cytoPanelEast.setState(CytoPanelState.DOCK);
		}	

		// Select my panel
		int index = cytoPanelEast.indexOfComponent(publicGraphsTablePanel);
		if (index == -1) {
			return;
		}
		cytoPanelEast.setSelectedIndex(index);
	}

}