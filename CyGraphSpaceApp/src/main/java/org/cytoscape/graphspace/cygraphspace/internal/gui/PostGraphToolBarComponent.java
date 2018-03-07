package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.PopupMenuEvent;

import org.cytoscape.application.swing.AbstractToolBarComponent;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.PostGraphExportUtils;
import org.cytoscape.model.CyNetwork;

/**
 * Toolbar component on Cytoscape toolbar to post and update graphs to GraphSpace
 * @author rishabh
 *
 */
public class PostGraphToolBarComponent extends AbstractToolBarComponent implements CyAction{
	
	//UI elements
	private JButton button;
	private JFrame loadingFrame;
	
	public PostGraphToolBarComponent(){
		super();
		button = new JButton();
		//imageicon used as for the toolbar menu
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("graphspaceicon.png"));
		button.setIcon(icon);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.setToolTipText("Export To GraphSpace"); //set tooltip to notify users about the functionality of the button
		
		//action attached to the button
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){

				JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();
		        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
		        
		        //loading frame while checking if updating the graph is possible
		        loadingFrame = new JFrame("Checking if update Possible");
				ImageIcon loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
				JLabel loadingLabel = new JLabel("", loading, JLabel.CENTER);
				loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				loadingFrame.setSize(400, 300);
				loadingFrame.add(loadingLabel);
				loadingFrame.setLocationRelativeTo(parent);
				
				//if there is no network to export, display an error
		        if( currentNetwork == null ){
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
		});
	}
		
	/** Returns an ImageIcon, or null if the path was invalid. */

	@Override
	public Component getComponent() {
		//returns the button component to be visible on the toolbar
		return button;
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public KeyStroke getAcceleratorKeyStroke() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public float getMenuGravity() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPreferredMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public float getToolbarGravity() {
//		// TODO Auto-generated method stub
//		return 1;
//	}
	@Override
	public boolean insertSeparatorAfter() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean insertSeparatorBefore() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isInMenuBar() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isInToolBar() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void updateEnableState() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean useCheckBoxMenuItem() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public float getToolbarGravity() {
		// TODO Auto-generated method stub
		return 0;
	}
}