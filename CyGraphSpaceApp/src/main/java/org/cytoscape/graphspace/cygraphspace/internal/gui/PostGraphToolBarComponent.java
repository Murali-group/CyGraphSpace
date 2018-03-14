package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.PopupMenuEvent;

import org.cytoscape.application.swing.AbstractToolBarComponent;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.graphspace.cygraphspace.internal.PostGraphMenuActionListener;

/**
 * Toolbar component on Cytoscape toolbar to post and update graphs to GraphSpace
 * @author rishabh
 *
 */
public class PostGraphToolBarComponent extends AbstractToolBarComponent implements CyAction {
	
	//UI elements
	private JButton button;
	
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
		button.addActionListener(new PostGraphMenuActionListener());
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