package org.cytoscape.graphspace.cygraphspace.internal.gui;

public class ResultPanelEvent {

    private int graphIndex;
    private int graphId;
    private String graphName;
    private String graphStatus;

    public ResultPanelEvent(String graphName, String graphStatus) {
        this.graphIndex = -1;
        this.graphId = -1;
        this.graphName = graphName;
        this.graphStatus = graphStatus;
    }

    public ResultPanelEvent(int graphIndex, int graphId, String graphName, String graphStatus) {
        this.graphIndex = graphIndex;
        this.graphId = graphId;
        this.graphName = graphName;
        this.graphStatus = graphStatus;
    }

    public int getGraphIndex() {
        return this.graphIndex;
    }

    public int getGraphId() {
        return this.graphId;
    }

    public String getGraphName() {
        return this.graphName;
    }


    public String getGraphStatus() {
        return this.graphStatus;
    }
}
