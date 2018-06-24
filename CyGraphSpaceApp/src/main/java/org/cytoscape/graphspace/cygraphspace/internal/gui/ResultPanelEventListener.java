package org.cytoscape.graphspace.cygraphspace.internal.gui;

public interface ResultPanelEventListener {
    public int postGraphEvent(ResultPanelEvent e);
    public void updateGraphStatusEvent(ResultPanelEvent e);
}