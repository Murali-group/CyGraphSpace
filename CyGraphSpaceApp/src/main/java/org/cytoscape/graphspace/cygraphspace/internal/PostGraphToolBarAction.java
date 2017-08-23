package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PostGraphToolBarComponent;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;

public class PostGraphToolBarAction extends AbstractCyAction{
    
	private static final long serialVersionUID = 1L;
	public PostGraphToolBarAction(String menuTitle, CyApplicationManager applicationManager){
        super(menuTitle, applicationManager, null, null);
    	PostGraphToolBarComponent toolBarComponent = new PostGraphToolBarComponent();
    	CyObjectManager.INSTANCE.getCySwingApplition().getJToolBar().add(toolBarComponent);
    	CyObjectManager.INSTANCE.getCySwingApplition().getJToolBar().setVisible(true);
    	setMenuGravity(getMenuGravity());
    }

    @Override
    public void actionPerformed(ActionEvent e) {	
    	PostGraphToolBarComponent toolBarComponent = new PostGraphToolBarComponent();
    	CyObjectManager.INSTANCE.getCySwingApplition().getJToolBar().add(toolBarComponent);
    	CyObjectManager.INSTANCE.getCySwingApplition().getJToolBar().setVisible(true);
    }
}
