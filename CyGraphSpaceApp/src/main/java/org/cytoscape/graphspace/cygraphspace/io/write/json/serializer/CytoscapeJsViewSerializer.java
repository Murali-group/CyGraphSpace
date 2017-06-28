package org.cytoscape.graphspace.cygraphspace.io.write.json.serializer;

import static org.cytoscape.graphspace.cygraphspace.io.write.json.serializer.CytoscapeJsToken.*;

import java.io.IOException;
import java.util.List;

import org.cytoscape.application.CyVersion;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CytoscapeJsViewSerializer extends JsonSerializer<CyNetworkView> {
	
	private final String version;

	public CytoscapeJsViewSerializer(final CyVersion cyVersion) {
		this.version = cyVersion.getVersion();
	}
	
	@Override
	public void serialize(CyNetworkView networkView, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.useDefaultPrettyPrinter();

		jgen.writeStartObject();
		jgen.writeStringField(CytoscapeJsNetworkModule.FORMAT_VERSION_TAG, CytoscapeJsNetworkModule.FORMAT_VERSION);
		jgen.writeStringField(CytoscapeJsNetworkModule.GENERATED_BY_TAG, "cytoscape-" + version);
		jgen.writeStringField(CytoscapeJsNetworkModule.TARGET_CYJS_VERSION_TAG, CytoscapeJsNetworkModule.CYTOSCAPEJS_VERSION);

		final CyNetwork network = networkView.getModel();
		
		// Serialize network data table
		jgen.writeObjectFieldStart(DATA.getTag());
		jgen.writeObject(network.getRow(network));
		jgen.writeEndObject();
		
		jgen.writeObjectFieldStart(ELEMENTS.getTag());

		// Write array
		final List<CyNode> nodes = network.getNodeList();
		final List<CyEdge> edges = network.getEdgeList();


		jgen.writeArrayFieldStart(NODES.getTag());
		for (final CyNode node : nodes) {
			jgen.writeStartObject();

			// Data field
			jgen.writeObjectFieldStart(DATA.getTag());
			jgen.writeStringField(ID.getTag(), node.getSUID().toString());
			// Write CyRow in "data" field
			jgen.writeObject(network.getRow(node));
			jgen.writeEndObject();

			// Position and other visual props
			jgen.writeObject(networkView.getNodeView(node));

			// Special case for cytoscape.js format:
			// - Selected
			jgen.writeBooleanField(CyNetwork.SELECTED, network.getRow(node).get(CyNetwork.SELECTED, Boolean.class));

			jgen.writeEndObject();
		}
		jgen.writeEndArray();

		jgen.writeArrayFieldStart(EDGES.getTag());
		for (final CyEdge edge : edges) {
			jgen.writeStartObject();

			jgen.writeObjectFieldStart(DATA.getTag());
			jgen.writeStringField(ID.getTag(), edge.getSUID().toString());
			jgen.writeStringField(SOURCE.getTag(), edge.getSource().getSUID().toString());
			jgen.writeStringField(TARGET.getTag(), edge.getTarget().getSUID().toString());

			// Write CyRow in "data" field
			jgen.writeObject(network.getRow(edge));

			jgen.writeEndObject();

			// Special case for cytoscape.js format:
			// - Selected
			jgen.writeBooleanField(CyNetwork.SELECTED, network.getRow(edge).get(CyNetwork.SELECTED, Boolean.class));

			jgen.writeEndObject();

		}
		jgen.writeEndArray();

		jgen.writeEndObject();

	}

	@Override
	public Class<CyNetworkView> handledType() {
		return CyNetworkView.class;
	}
}