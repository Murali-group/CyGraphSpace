package org.cytoscape.graphspace.cygraphspace.internal.util;

import org.cytoscape.property.AbstractConfigDirPropsReader;

public class PropertyReader extends AbstractConfigDirPropsReader{
	public PropertyReader(String name, String propFileName) {
		super(name, propFileName, SavePolicy.CONFIG_DIR);
	}
}