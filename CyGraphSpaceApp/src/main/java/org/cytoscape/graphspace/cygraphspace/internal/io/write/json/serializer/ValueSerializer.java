package org.cytoscape.graphspace.cygraphspace.internal.io.write.json.serializer;

public interface ValueSerializer<T> {
	
	String serialize(final T value);

}
