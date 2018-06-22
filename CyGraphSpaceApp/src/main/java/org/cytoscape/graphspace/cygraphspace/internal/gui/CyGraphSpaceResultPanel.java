package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

@SuppressWarnings("serial")
public class CyGraphSpaceResultPanel extends JPanel implements CytoPanelComponent, ResultPanelEventListener {

	private String title;
	private JPanel mainList;
	private int count;
	private Map<Integer, PanelItem> map;

	public CyGraphSpaceResultPanel(String title) {
		this.title = title;
		this.count = 0;
		this.map = new HashMap<>();
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

	private class PanelItem extends JPanel {
		private int index;
		private String name;
		private String status;

		public PanelItem(int index, String name, String status) {
			this.index = index;
			this.name = name;
			this.status = status;
			initPanelItem();
		}

		private void initPanelItem() {
			this.add(new JLabel(name));
			this.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
		}
	}

	@Override
	public void postGraphEvent() {
		PanelItem item = new PanelItem(count, "test", "test");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainList.add(item, gbc, 0);

		validate();
		repaint();
	}

	@Override
	public void updateGraphEvent() {
		// TODO Auto-generated method stub
	}
}
