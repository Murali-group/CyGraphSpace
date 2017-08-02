package org.cytoscape.graphspace.cygraphspace.internal;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;

public class GraphSpaceNetworkAboutToBeDestroyedListener implements NetworkAboutToBeDestroyedListener {

	@Override
	public void handleEvent(NetworkAboutToBeDestroyedEvent arg0) {
		CyNetwork cyNetwork = arg0.getNetwork();
		System.out.println( "Network to be destroyed: " + cyNetwork.getSUID());
	}

}