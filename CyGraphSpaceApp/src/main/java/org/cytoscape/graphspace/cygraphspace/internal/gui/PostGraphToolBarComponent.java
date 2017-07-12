package org.cytoscape.graphspace.cygraphspace.internal.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.cytoscape.application.swing.ToolBarComponent;


public class PostGraphToolBarComponent extends JButton implements ToolBarComponent{
	public PostGraphToolBarComponent(){
		super();
//		ImageIcon icon = createImageIcon("/graphspace.png", "Graphspace");
//		this.setIcon(icon);
//		this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("graphspace-icon.png")));
		ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("graphspace.png").getFile());
		this.setIcon(icon);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				new PostGraphDialog(new JFrame());
			}
		});
	}
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
		return new ImageIcon(PostGraphToolBarComponent.class.getClassLoader().getResource(path));
//	    java.net.URL imgURL = getClass().getResource(path);
//	    if (imgURL != null) {
//	        return new ImageIcon(imgURL, description);
//	    } else {
//	        System.err.println("Couldn't find file: " + path);
//	        return null;
//	    }
	}
	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public float getToolBarGravity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
//	@Override
//	public Component getComponent() {
//		// TODO Auto-generated method stub
//		return this.graphspaceToolBarButton;
//	}
}