package org.cytoscape.graphspace.cygraphspace.internal.util;

import java.util.List;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.task.AbstractNetworkTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class CreateViewTask extends AbstractTask{

	public CreateViewTask() {
		
	}

	@Override
	public void run(TaskMonitor arg0) throws Exception {
		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
		List<CyNode> nodes = network.getNodeList();
		System.out.println(nodes.toString());
		CyNetworkView view = CyObjectManager.INSTANCE.getNetworkViewFactory().createNetworkView(network);
		for (CyNode node: nodes) {
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, 50d);
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, 50d);
		}
	}
	
}