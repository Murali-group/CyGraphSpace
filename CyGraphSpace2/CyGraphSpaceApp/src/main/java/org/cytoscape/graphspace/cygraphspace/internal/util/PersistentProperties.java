package org.cytoscape.graphspace.cygraphspace.internal.util;

import org.cytoscape.property.AbstractConfigDirPropsReader;
import org.cytoscape.property.CyProperty;

public class PersistentProperties extends AbstractConfigDirPropsReader {
	public PersistentProperties(String name, String fileName) {
		super(name, fileName, CyProperty.SavePolicy.CONFIG_DIR);
	}
}