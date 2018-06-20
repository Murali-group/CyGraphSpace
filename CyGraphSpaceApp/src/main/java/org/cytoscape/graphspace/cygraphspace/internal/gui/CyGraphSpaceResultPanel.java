package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

@SuppressWarnings("serial")
public class CyGraphSpaceResultPanel extends JPanel implements CytoPanelComponent {

	private String title;
	private JPanel mainList;

	public CyGraphSpaceResultPanel(String title) {
		this.title = title;
		initializePanel();
	}

	private void initializePanel() {
		setLayout(new BorderLayout());

		// based on https://stackoverflow.com/questions/14615888/list-of-jpanels-that-eventually-uses-a-scrollbar
		this.mainList = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		mainList.add(new JPanel(), gbc);

		add(new JScrollPane(mainList));
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
