package org.cytoscape.graphspace.cygraphspace.io.write.json.serializer;

public interface ValueSerializer<T> {
	
	String serialize(final T value);

}
