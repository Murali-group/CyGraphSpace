package org.cytoscape.graphspace.cygraphspace.io.write.json;

import java.io.OutputStream;

import org.cytoscape.io.CyFileFilter;
import org.cytoscape.io.write.CyNetworkViewWriterFactory;
import org.cytoscape.io.write.CyWriter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CytoscapeJsNetworkWriterFactory implements CyNetworkViewWriterFactory {
	
	private final CyFileFilter filter;
	private final ObjectMapper mapper;

	public CytoscapeJsNetworkWriterFactory(final CyFileFilter filter, final ObjectMapper mapper) {
		this.filter = filter;
		this.mapper = mapper;
	}

	@Override
	public CyWriter createWriter(OutputStream outputStream, CyNetworkView view) {
		return new JSONNetworkViewWriter(outputStream, view, mapper);
	}

	@Override
	public CyWriter createWriter(OutputStream outputStream, CyNetwork network) {
		return new JSONNetworkWriter(outputStream, network, mapper);
	}

	@Override
	public CyFileFilter getFileFilter() {
		return filter;
	}
}