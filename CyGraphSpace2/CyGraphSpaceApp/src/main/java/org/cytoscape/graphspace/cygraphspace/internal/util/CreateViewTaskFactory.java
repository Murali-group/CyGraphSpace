package org.cytoscape.graphspace.cygraphspace.internal.util;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class CreateViewTaskFactory extends AbstractTaskFactory{

	@Override
	public TaskIterator createTaskIterator() {
		// TODO Auto-generated method stub
		return new TaskIterator(new CreateViewTask());
	}
	
}