package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkEvent;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.application.swing.AbstractToolBarComponent;
import org.cytoscape.graphspace.cygraphspace.internal.PostGraphMenuActionListener;

/**
 * Toolbar component on Cytoscape toolbar to post and update graphs to GraphSpace
 * @author rishabh
 *
 */
public class PostGraphToolBarComponent extends AbstractToolBarComponent implements SetCurrentNetworkListener {

    private JButton button;
    private PostGraphMenuActionListener actionListener;

	public PostGraphToolBarComponent() {
		super();
		button = new JButton();

		//imageicon used as for the toolbar menu
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("graphspaceicon.png"));
		button.setIcon(icon);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(true);
		button.setToolTipText("Export To GraphSpace"); //set tooltip to notify users about the functionality of the button
		button.setEnabled(false);
		
		//action attached to the button
		actionListener = new PostGraphMenuActionListener();
		button.addActionListener(actionListener);
	}
		
	/** Returns an ImageIcon, or null if the path was invalid. */

	@Override
	public Component getComponent() {
		//returns the button component to be visible on the toolbar
		return button;
	}

    @Override
    public void handleEvent(SetCurrentNetworkEvent e) {
        button.setEnabled(e.getNetwork() != null);
    }
}
