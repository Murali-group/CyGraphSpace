package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.PostGraphExportUtils;

import javax.swing.*;

/**
 * This class defines the action performed on exporting a graph to GraphSpace from Cytoscape from the menu File>Export>Network To GraphSpace
 * @author rishabh
 *
 */
public class PostGraphMenuAction extends AbstractCyAction {
	
	private static final long serialVersionUID = 1L;
	
	public PostGraphMenuAction(String menuTitle, CyApplicationManager applicationManager) {
        
		super(menuTitle, applicationManager, null, null);
        
        // Menu under File>Export
        setPreferredMenu("File.Export");
    }
	
	//called when export menu is clicked by the user
    @Override
    public void actionPerformed(ActionEvent e) {
    	
        JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();

        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        
        //loading frame while checking if updating the graph is possible
        JFrame loadingFrame = new JFrame("Checking if update Possible");
		ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
		JLabel loadingLabel = new JLabel("", loading, JLabel.CENTER);
		loadingFrame.add(loadingLabel);
	    loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setSize(400, 300);
		loadingFrame.setLocationRelativeTo(parent);
        
		//if there is no network to export, display an error
		if(currentNetwork == null){
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }
        
		//if there is a network and the user is currently authenticated, create a post graph dialog
		if (Server.INSTANCE.isAuthenticated()){
			loadingFrame.setVisible(true);
    		try {
    		    PostGraphExportUtils.populate(parent, loadingFrame);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        
		//if there is a network but the user is not authenticated, open the login dialog for the user to log in. Once logged in, open the post graph dialog
		else{
        	AuthenticationDialog dialog = new AuthenticationDialog(parent);
            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter(){
            	@Override
            	public void windowClosed(WindowEvent e){
					loadingFrame.setVisible(true);
            		try {
            		    PostGraphExportUtils.populate(parent, loadingFrame);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            });
        }
    }
}
