package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.gui.GetGraphDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;

public class GetGraphMenuAction extends AbstractCyAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public GetGraphMenuAction(String menuTitle, CyApplicationManager applicationManager){
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
    	if (!Server.INSTANCE.isAuthenticated()){
    		AuthenticationDialog authDialog = new AuthenticationDialog(parent);
    		authDialog.setLocationRelativeTo(parent);
            authDialog.setVisible(true);
    	}
    	else{
	        GetGraphDialog dialog = new GetGraphDialog(parent);
	        dialog.setLocationRelativeTo(parent);
	        dialog.setVisible(true);
    	}
    }
    
}
