package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.PopupMenuEvent;

import org.cytoscape.application.swing.AbstractToolBarComponent;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.ToolBarComponent;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.application.swing.AbstractCyAction;

public class PostGraphToolBarComponent extends AbstractToolBarComponent implements CyAction{
	private JButton button;
	public PostGraphToolBarComponent(){
		super();
//		ImageIcon icon = createImageIcon("/graphspace.png", "Graphspace");
//		this.setIcon(icon);
//		this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("graphspace-icon.png")));
		button = new JButton();
//		button.setText("GraphSpace");
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("graphspaceicon.png"));
		button.setIcon(icon);
//		button.setPreferredSize(new Dimension(10,10));
//		button.setMargin(new Insets(0, 0, 0, 0));
//		button.setBorder(null);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				System.out.println("clicked");
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
		});
	}
	/** Returns an ImageIcon, or null if the path was invalid. */

	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
//		return null;
		return button;
	}
//	@Override
//	public float getToolBarGravity() {
//		// TODO Auto-generated method stub
//		return 1;
//	}
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