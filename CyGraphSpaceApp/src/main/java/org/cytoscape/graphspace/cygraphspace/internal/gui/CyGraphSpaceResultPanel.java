package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

@SuppressWarnings("serial")
public class CyGraphSpaceResultPanel extends JPanel implements CytoPanelComponent {

	private String title;

	public CyGraphSpaceResultPanel(String title) {
		this.title = title;
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		return title;
	}
}
