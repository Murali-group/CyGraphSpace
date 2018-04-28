package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;

import javax.swing.event.MenuEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;

public class CyGraphSpaceMenuAction extends AbstractCyAction {

    private static final long serialVersionUID = 1L;
    private PostGraphMenuActionListener actionListener;

    public CyGraphSpaceMenuAction(String menuTitle) {

        super(menuTitle, CyObjectManager.INSTANCE.getApplicationManager(), null, null);

        // Menu under File>Export
        setPreferredMenu("File.Export");

        actionListener = new PostGraphMenuActionListener();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        actionListener.actionPerformed(arg0);
    }

    @Override
    public void menuSelected(MenuEvent e) {
        setEnabled(CyObjectManager.INSTANCE.getCurrentNetwork() != null);
    }
}
