package org.cytoscape.graphspace.cygraphspace.internal.io.write.json.serializer;

import org.cytoscape.view.vizmap.VisualMappingFunction;

public interface VisualMappingSerializer <T extends VisualMappingFunction<?, ?>> {

	String serialize(final T mapping);
}
