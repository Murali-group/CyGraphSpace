package org.cytoscape.graphspace.cygraphspace.internal.io.read.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.io.read.AbstractCyNetworkReader;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.util.ListSingleSelection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomCytoscapeJsNetworkReader extends AbstractCyNetworkReader {

	private final CustomCytoscapejsMapper mapper;

	// Supports only one CyNetwork per file.
	private CyNetwork network = null;

	private final InputStream is;
	private final String networkCollectionName;

	public CustomCytoscapeJsNetworkReader(final String networkCollectionName,
									final InputStream is,
									final CyApplicationManager cyApplicationManager,
									final CyNetworkFactory cyNetworkFactory,
									final CyNetworkManager cyNetworkManager,
									final CyRootNetworkManager cyRootNetworkManager) {
		super(is, cyApplicationManager, cyNetworkFactory, cyNetworkManager, cyRootNetworkManager);
//		String styleJSONStringTest;
//		try {
//			styleJSONStringTest = IOUtils.toString(is, "UTF-8");
//			System.out.println("is: ===========" + styleJSONStringTest);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("p1");
		this.networkCollectionName = networkCollectionName;
		System.out.println("p2");
		if (is == null) {
			throw new NullPointerException("Input Stream cannot be null.");
		}
		System.out.println("p3");
		this.mapper = new CustomCytoscapejsMapper();
		System.out.println("p4");
		this.is = is;
		System.out.println("p5");
	}

	@Override
	public CyNetwork[] getNetworks() {
		final CyNetwork[] result = new CyNetwork[1];
		result[0] = network;
		return result;
	}

	@Override
	public CyNetworkView buildCyNetworkView(CyNetwork network) {
		final CyNetworkView view = getNetworkViewFactory().createNetworkView(network);
		final Map<CyNode, Double[]> positionMap = mapper.getNodePosition();
		System.out.println("p4");
		for (final CyNode node : positionMap.keySet()) {
			final Double[] position = positionMap.get(node);
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, position[0]);
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, position[1]);
		}
		System.out.println("p5");
		return view;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		System.out.println("p6");
		final ObjectMapper objMapper = new ObjectMapper();
		System.out.println("p7");
		final JsonNode rootNode = objMapper.readValue(is, JsonNode.class);
		System.out.println("rootNode==========="+rootNode.toString());
		System.out.println("p8");
		// Select the root collection name from the list.
		if(networkCollectionName != null) {
			ListSingleSelection<String> rootList = getRootNetworkList();
			if(rootList.getPossibleValues().contains(networkCollectionName)) {
				// Collection already exists.
				rootList.setSelectedValue(networkCollectionName);
			}
		}
		
		CyRootNetwork rootNetwork = getRootNetwork();
		// Select Network Collection
		// 1. Check from Tunable
		// 2. If not available, use optional parameter
		CySubNetwork subNetwork;
		String collectionName = null;
		
		if (rootNetwork != null) {
			// Root network exists
			subNetwork = rootNetwork.addSubNetwork();
//			this.network = this.mapper.createNetwork(rootNode, subNetwork, null);
		} else {
			// Need to create new network with new root.
			subNetwork = (CySubNetwork) cyNetworkFactory.createNetwork();
			collectionName = networkCollectionName;
//			this.network = this.mapper.createNetwork(rootNode, subNetwork, networkCollectionName);
		}
		
		// Check this is an element list or full network
		if(rootNode.isArray()) {
			
			this.network = this.mapper.createNetworkFromElementList(rootNode, subNetwork, collectionName);
		} else {
			this.network = this.mapper.createNetwork(rootNode, subNetwork, collectionName);
		}
	}
}