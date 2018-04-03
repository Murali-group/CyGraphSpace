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
 * Task class that posts graph to the GraphSpace and update related status
 */
public class PostGraphTask extends AbstractTask {

    private ActionEvent evt;
    private JSONObject graphJSON;
    private JSONObject styleJSON; 
    private boolean isGraphPublic;
    private TaskMonitor taskMonitor;

    public PostGraphTask(ActionEvent evt, JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic) {
        this.evt = evt;
        this.graphJSON = graphJSON;
        this.styleJSON = styleJSON;
        this.isGraphPublic = isGraphPublic;
    }

    //post the current network to GraphSpace
    private void postGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic, ArrayList<String> tagsList) throws Exception{
        Server.INSTANCE.postGraph(graphJSON, styleJSON, isGraphPublic, tagsList);
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {

        this.taskMonitor = taskMonitor;
        taskMonitor.setTitle("Export graph to GraphSpace");
        taskMonitor.setStatusMessage("Exporting graph to GraphSpace. Please wait...");

        // run task in background
        new Thread() {
            public void run() {
                postGraph();
            }
        }.start();
    }

    private void postGraph() {
        try {
            postGraph(graphJSON, styleJSON, isGraphPublic, null);
        } catch (Exception e1) {
            taskMonitor.setStatusMessage("Post graph fails.");
            JOptionPane.showMessageDialog((Component)evt.getSource(), "Could not post graph", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        taskMonitor.setStatusMessage("Post graph successful.");
        JOptionPane.showMessageDialog((Component)evt.getSource(), "Post graph successful.", 
                "Message", JOptionPane.INFORMATION_MESSAGE);
    }
}
