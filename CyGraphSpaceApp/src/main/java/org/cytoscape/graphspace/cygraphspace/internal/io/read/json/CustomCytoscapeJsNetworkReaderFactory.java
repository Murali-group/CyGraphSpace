package org.cytoscape.graphspace.cygraphspace.internal.io.read.json;

import java.io.InputStream;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.read.AbstractInputStreamTaskFactory;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.work.TaskIterator;

public class CustomCytoscapeJsNetworkReaderFactory extends AbstractInputStreamTaskFactory {

	private final CyApplicationManager cyApplicationManager;
	protected final CyNetworkFactory cyNetworkFactory;
	private final CyNetworkManager cyNetworkManager;
	private final CyRootNetworkManager cyRootNetworkManager;

	public CustomCytoscapeJsNetworkReaderFactory(final CyFileFilter filter,
										   final CyApplicationManager cyApplicationManager,
										   final CyNetworkFactory cyNetworkFactory,
										   final CyNetworkManager cyNetworkManager,
										   final CyRootNetworkManager cyRootNetworkManager) {
		super(filter);
		this.cyApplicationManager = cyApplicationManager;
		this.cyNetworkFactory = cyNetworkFactory;
		this.cyNetworkManager = cyNetworkManager;
		this.cyRootNetworkManager = cyRootNetworkManager;
	}

	@Override
	public TaskIterator createTaskIterator(InputStream is, String collectionName) {
		System.out.println("p1");
		return new TaskIterator(new CustomCytoscapeJsNetworkReader(collectionName, is, cyApplicationManager, cyNetworkFactory,
				cyNetworkManager, cyRootNetworkManager));
	}
}
