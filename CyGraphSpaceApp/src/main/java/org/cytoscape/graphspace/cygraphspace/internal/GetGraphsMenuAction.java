package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.graphspace.cygraphspace.internal.gui.GetGraphsDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;

/**
 *
 * @author David Welker
 * Creates a new menu item in the Apps|NDex menu to find networks in the currently selected NDEx server.
 */
public class GetGraphsMenuAction extends AbstractCyAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public GetGraphsMenuAction(String menuTitle, CyApplicationManager applicationManager){
        super(menuTitle, applicationManager, null, null);
        // We want this menu item to appear under the App|NDEx menu. The actual name of the menu item is set in
        // org.cytoscape.ndex.internal.CyActivator as "Find Networks"
        setPreferredMenu("Apps.CyGraphSpace");
    }
    

    @Override
    /**
     * This method displays the find networks dialog.
     * It is called when the menu item is selected.  
     */
    public void actionPerformed(ActionEvent e)
    {
        JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();
        GetGraphsDialog dialog = new GetGraphsDialog(parent);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
}
