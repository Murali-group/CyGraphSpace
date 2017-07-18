package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PostGraphDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;

import javax.swing.*;

/**
 *
 * @author David Welker
 * Creates a new menu item in the Apps|NDex menu to upload an Cytoscape network to the current NDEx server.
 */
public class PostGraphMenuAction extends AbstractCyAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PostGraphMenuAction(String menuTitle, CyApplicationManager applicationManager)
    {
        super(menuTitle, applicationManager, null, null);
        // We want this menu item to appear under the App|NDEx menu. The actual name of the menu item is set in
        // org.cytoscape.ndex.internal.CyActivator as "Upload Network"
        setPreferredMenu("File.Export");
    }

    @Override
    /**
     * This method displays the upload network dialog.
     * It is called when the menu item is selected.  
     */
    public void actionPerformed(ActionEvent e)
    {
        JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();

        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        if( currentNetwork == null )
        {
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }
        if (Server.INSTANCE.isAuthenticated()){
        	PostGraphDialog dialog = new PostGraphDialog(parent);
            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);
        }
        else{
        	AuthenticationDialog dialog = new AuthenticationDialog(parent);
            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter(){
            	@Override
            	public void windowClosed(WindowEvent e){
            		PostGraphDialog postDialog = new PostGraphDialog(parent);
                    postDialog.setLocationRelativeTo(parent);
                    postDialog.setVisible(true);
            	}
            });
        }
    }
}
