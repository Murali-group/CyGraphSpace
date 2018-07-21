package org.cytoscape.graphspace.cygraphspace.internal.task;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.cytoscape.graphspace.cygraphspace.internal.gui.ResultPanelEvent;
import org.cytoscape.graphspace.cygraphspace.internal.gui.ResultPanelEventListener;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.MessageConfig;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.graphspace.javaclient.Response;
import org.json.JSONObject;

/**
 * Task class that update graph to the GraphSpace and update related status
 */
public class UpdateGraphTask extends AbstractTask {
    private JSONObject graphJSON;
    private JSONObject styleJSON; 
    private boolean isGraphPublic;
    private TaskMonitor taskMonitor;
    private ResultPanelEventListener listener;

    public UpdateGraphTask(JSONObject graphJSON, JSONObject styleJSON, boolean isGraphPublic) {
        this.graphJSON = graphJSON;
        this.styleJSON = styleJSON;
        this.isGraphPublic = isGraphPublic;
        this.listener = null;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {

        this.taskMonitor = taskMonitor;
        taskMonitor.setTitle(MessageConfig.UPDATE_GRAPH_TASK_TITLE);
        taskMonitor.setStatusMessage(MessageConfig.UPDATE_GRAPH_TASK_STATUS_IN_PROGRESS);

        // run task in background
        new Thread() {
            public void run() {
                updateGraph();
            }
        }.start();
    }

    private void updateGraph() {
        int panelIndex = -1, graphId = -1;
        try {
            if (listener != null)
                panelIndex = listener.graphSpaceEvent(
                        new ResultPanelEvent(graphJSON.getJSONObject("data").getString("name"),MessageConfig.TASK_IN_PROGRESS));

            graphId = updateGraph(graphJSON, styleJSON, isGraphPublic, null).getGraph().getId();
        } catch (Exception e1) {
            taskMonitor.setStatusMessage(MessageConfig.UPDATE_GRAPH_TASK_STATUS_FAIL);
            JOptionPane.showMessageDialog(null, 
                    MessageConfig.UPDATE_GRAPH_TASK_DIALOG_FAIL, "Error", JOptionPane.ERROR_MESSAGE);

            if (listener != null)
                listener.updateGraphStatusEvent(
                        new ResultPanelEvent(panelIndex, graphId, "", MessageConfig.TASK_FAIL));

            return;
        }

        if (listener != null)
            listener.updateGraphStatusEvent(
                    new ResultPanelEvent(panelIndex, graphId, "", MessageConfig.TASK_COMPLETE));

        taskMonitor.setStatusMessage(MessageConfig.UPDATE_GRAPH_TASK_STATUS_SUCCESS);
        JOptionPane.showMessageDialog(null, 
                MessageConfig.UPDATE_GRAPH_TASK_DIALOG_SUCCESS, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    //post the current network to GraphSpace
    private Response updateGraph(JSONObject graphJSON, JSONObject styleJSON, boolean isPublic, ArrayList<String> tagsList) throws Exception{
        String name = graphJSON.getJSONObject("data").getString("name");
        return Server.INSTANCE.updateGraph(name, graphJSON, isPublic, tagsList);
    }

    public void setResultPanelEventListener(ResultPanelEventListener listener) {
        this.listener = listener;
    }
}
