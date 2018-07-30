package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.util.MessageConfig;

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
    public int graphSpaceEvent(ResultPanelEvent e) {
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
        itemMap.get(e.getGraphIndex()).updateStatus(e.getGraphStatus(), e.getGraphId());
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

        private JPanel graphPanel;
        private JLabel graphNameLabel;
        private JTextField graphNameText;

        private JPanel statusPanel;
        private JLabel statusLabel;
        private JLabel statusText;

        private JPanel linkPanel;
        private JLabel linkLabel;
        private JButton linkBtn;

        public PanelItem(int index, String name, String status) {
            this.index = index;
            this.name = name;
            this.status = status;
            initPanelItem();
        }

        private void initPanelItem() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            TitledBorder taskBorder = BorderFactory.createTitledBorder("Task " + index);
            this.setBorder(taskBorder);

            graphNameLabel = new JLabel("Graph: ");

            graphNameText = new JTextField(name);
            graphNameText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            graphNameText.setEditable(false);
            graphNameText.setOpaque(false);

            graphPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            graphPanel.add(graphNameLabel);
            graphPanel.add(graphNameText);
            this.add(graphPanel);

            statusLabel = new JLabel("Status: ");
            statusText = new JLabel(status);

            statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            statusPanel.add(statusLabel);
            statusPanel.add(statusText);
            this.add(statusPanel);
        }

        public void updateStatus(String status, int graphId) {
            this.status = status;
            statusText.setText(status);

            if (graphId != -1) {
                linkBtn = new JButton("<HTML><FONT color=\"#000099\"><U>" + MessageConfig.GRAPHSPACE_LINK + graphId + "</U></FONT></HTML>");
                linkBtn.setOpaque(false);
                linkBtn.setContentAreaFilled(false);
                linkBtn.setBorderPainted(false);

                linkBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            java.awt.Desktop.getDesktop().browse(new URI(MessageConfig.GRAPHSPACE_LINK + graphId));
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                });

                linkLabel = new JLabel("Link: ");
                linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                linkPanel.add(linkLabel);
                linkPanel.add(linkBtn);
                this.add(linkPanel);
            }
        }
    }
}
