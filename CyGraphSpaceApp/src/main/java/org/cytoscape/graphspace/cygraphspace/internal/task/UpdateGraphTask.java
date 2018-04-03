package org.cytoscape.graphspace.cygraphspace.internal.task;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.json.JSONObject;

/**
 * Task class that update graph to the GraphSpace and update related status
 */
public class UpdateGraphTask extends AbstractTask {

    private ActionEvent evt;
    private JSONObject graphJSON;
    private JSONObject styleJSON; 
    private boolean isGraphPublic;
    private TaskMonitor taskMonitor;

    public UpdateGraphTask(ActionEvent evt, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic) {
        this.evt = evt;
        this.graphJSON = graphJSON;
        this.styleJSON = styleJSON;
        this.isGraphPublic = isGraphPublic;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {

        this.taskMonitor = taskMonitor;
        taskMonitor.setTitle("Update graph to GraphSpace");
        taskMonitor.setStatusMessage("Updating graph to GraphSpace. Please wait...");

        // run task in background
        new Thread() {
            public void run() {
                postGraph();
            }
        }.start();
    }

    private void postGraph() {
        try {
            updateGraph(graphJSON, styleJSON, isGraphPublic, null);
        } catch (Exception e1) {
            taskMonitor.setStatusMessage("Update graph fails.");
            JOptionPane.showMessageDialog((Component)evt.getSource(), 
                    "Could not update graph", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        taskMonitor.setStatusMessage("Update graph successful.");
        JOptionPane.showMessageDialog((Component)evt.getSource(), 
                "Update graph successful.", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    //post the current network to GraphSpace
    private void updateGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isPublic, ArrayList<String> tagsList) throws Exception{
        String name = graphJSON.getJSONObject("data").getString("name");
        Server.INSTANCE.updateGraph(name, graphJSON, isPublic, tagsList);
    }
}
