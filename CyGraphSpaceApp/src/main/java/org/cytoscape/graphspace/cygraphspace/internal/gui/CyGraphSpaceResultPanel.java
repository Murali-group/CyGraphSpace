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

import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;

@SuppressWarnings("serial")
public class CyGraphSpaceResultPanel extends JPanel implements CytoPanelComponent, ResultPanelEventListener {

    private String title;
    private JPanel mainList;
    private int itemCount;
    private Map<Integer, PanelItem> itemMap;
    private CytoPanel cytoPanel;

    public CyGraphSpaceResultPanel(String title) {
        this.title = title;
        this.itemCount = 1;
        this.itemMap = new HashMap<>();
        this.cytoPanel = CyObjectManager.INSTANCE.getCySwingApplition().getCytoPanel(getCytoPanelName());
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

    @Override
    public int postGraphEvent(ResultPanelEvent e) {
        PanelItem item = new PanelItem(itemCount, e.getGraphName(), e.getGraphStatus());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainList.add(item, gbc, 0);

        itemMap.put(itemCount, item);

        validate();
        repaint();
        showPanel();

        return itemCount++;
    }

    @Override
    public void updateGraphStatusEvent(ResultPanelEvent e) {
        itemMap.get(e.getGraphIndex()).updateStatus(e.getGraphStatus());
        validate();
        repaint();
        showPanel();
    }

    private void showPanel() {
        if (cytoPanel.getState() == CytoPanelState.HIDE)
            cytoPanel.setState(CytoPanelState.DOCK);

        // set visible and selected
        this.setVisible(true);
        cytoPanel.setSelectedIndex(cytoPanel.indexOfComponent(this.getComponent()));
    }

    private class PanelItem extends JPanel {
        private int index;
        private String name;
        private String status;

        private JLabel taskLabel;
        private JLabel graphNameLabel;
        private JLabel statusLabel;

        public PanelItem(int index, String name, String status) {
            this.index = index;
            this.name = name;
            this.status = status;
            initPanelItem();
        }

        private void initPanelItem() {
            taskLabel = new JLabel("Task: " + index);
            graphNameLabel = new JLabel("Graph: " + name);
            statusLabel = new JLabel("Status: " + status);

            this.add(taskLabel);
            this.add(graphNameLabel);
            this.add(statusLabel);
            this.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        }

        public void updateStatus(String status) {
            this.status = status;
            statusLabel.setText("Status: " + status);
        }
    }
}
