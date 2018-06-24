package org.cytoscape.graphspace.cygraphspace.internal.gui;

public class ResultPanelEvent {

    private int graphIndex;
    private String graphName;
    private String graphStatus;

    public ResultPanelEvent(String graphName, String graphStatus) {
        this.graphIndex = -1;
        this.graphName = graphName;
        this.graphStatus = graphStatus;
    }
    
    public ResultPanelEvent(int graphIndex, String graphName, String graphStatus) {
        this.graphIndex = graphIndex;
        this.graphName = graphName;
        this.graphStatus = graphStatus;
    }
    
    public int getGraphIndex() {
        return this.graphIndex;
    }

    public String getGraphName() {
        return this.graphName;
    }

    public String getGraphStatus() {
        return this.graphStatus;
    }
}
