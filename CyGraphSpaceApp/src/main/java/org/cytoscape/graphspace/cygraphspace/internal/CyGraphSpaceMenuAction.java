package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class CyGraphSpaceMenuAction extends AbstractCyAction {

    private static final long serialVersionUID = 1L;
    private PostGraphMenuActionListener actionListener;

    public CyGraphSpaceMenuAction(String menuTitle, CyApplicationManager applicationManager) {

        super(menuTitle, applicationManager, null, null);

        // Menu under File>Export
        setPreferredMenu("File.Export");

        actionListener = new PostGraphMenuActionListener();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        actionListener.actionPerformed(arg0);
    }
}
