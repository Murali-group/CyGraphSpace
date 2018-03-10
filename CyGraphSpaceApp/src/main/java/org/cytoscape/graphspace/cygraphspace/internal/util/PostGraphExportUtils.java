package org.cytoscape.graphspace.cygraphspace.internal.util;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.cytoscape.graphspace.cygraphspace.internal.gui.PostGraphDialog;
import org.cytoscape.graphspace.cygraphspace.internal.gui.UpdateGraphDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;
import org.graphspace.javaclient.Graph;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class that handles the logic on exporting the network to GraphSpace
 */
public class PostGraphExportUtils {

    /**
     * populate the values in the post graph dialog
     * @param parent the parent frame
     * @param loadingFrame the loading frame
     * @throws Exception
     */
    public static void populate(Frame parent, JFrame loadingFrame) throws Exception {
        JSONObject graphJSON = exportNetworkToJSON();
        JSONObject styleJSON = exportStyleToJSON();
        String graphName = graphJSON.getJSONObject("data").getString("name");
        boolean isGraphPublic = false;

        //if updating the graph is possible, open the update graph dialog.
        if(Server.INSTANCE.updatePossible(graphName)){
            loadingFrame.dispose();
            Graph graph = Server.INSTANCE.getGraphByName(graphName);
            isGraphPublic = graph.isPublic();
            UpdateGraphDialog updateDialog = new UpdateGraphDialog(parent, graphName, graphJSON, styleJSON, isGraphPublic, null);
            updateDialog.setLocationRelativeTo(parent);
            updateDialog.setVisible(true);
        }

        //if updating the graph is not possible, open the post graph dialog
        else{
            loadingFrame.dispose();
            PostGraphDialog postDialog = new PostGraphDialog(parent, graphName, graphJSON, styleJSON, isGraphPublic, null);
            postDialog.setLocationRelativeTo(parent);
            postDialog.setVisible(true);
        }
    }

    /**
     * Utility method to export the current network to a json object to be exported to GraphSpace
     * @return graphJSON representing the network in JSON format
     * @throws IOException
     */
    private static JSONObject exportNetworkToJSON() throws IOException{

        //create a temporary json file for the network of cyjs format
        File tempFile = File.createTempFile("CyGraphSpaceExport", ".cyjs");

        //read the network
        CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();

        //export the network to the temporary cyjs file
        TaskIterator ti = CyObjectManager.INSTANCE.getExportNetworkTaskFactory().createTaskIterator(network, tempFile);
        CyObjectManager.INSTANCE.getSynchrounousTaskManager().execute(ti);

        //read the file contents to a string
        String graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");

        //delete the temporary file
        tempFile.delete();

        /**
         * There is a design flaw in Cytoscape that results in a few parameters to be added to the graphs automatically on import.
         * This is because cytoscape doesn't restrict apps to use any arbitary parameters to save network data, hence, they add
         * attributes on importing the graph that was earlier created using GraphSpace and instead of replacing the values for conflicting attributes,
         * those values are stored under different attribute names.
         * This creates duplicate attribute values which results in an error while exporting the String to a json object.
         * Hence, the attributes which are not required by GraphSpace and Cytoscape are deleted before exporting to resolve this conflict.
         * This might be problematic in case the user needs to use the graph with other apps which uses conflicting attributes.
         */
        graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
        graphJSONString = graphJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
        graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
        graphJSONString = graphJSONString.replaceAll("(?m)^*.\"source_original\".*", "");
        graphJSONString = graphJSONString.replaceAll("(?m)^*.\"target_original\".*", "");

        //graph string converted to graphJson
        JSONObject graphJSON = new JSONObject(graphJSONString);
        return graphJSON;
    }

    //
    /**
     * Utility method to export the style of the current network to a json object to be exported to GraphSpace
     * @return style json representing the network style
     * @throws IOException
     */
    private static JSONObject exportStyleToJSON() throws IOException{

        //create a temporary json file for the network of json format
        File tempFile = File.createTempFile("CyGraphSpaceStyleExport", ".json");

        //export the style json to the temporary json file
        TaskIterator ti = CyObjectManager.INSTANCE.getExportVizmapTaskFactory().createTaskIterator(tempFile);
        CyObjectManager.INSTANCE.getSynchrounousTaskManager().execute(ti);

        //read the file contents to a string
        String styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");

        //delete the temporary files
        tempFile.delete();

        /**
         * There is a design flaw in Cytoscape that results in a few parameters to be added to the graphs automatically on import.
         * This is because cytoscape doesn't restrict apps to use any arbitary parameters to save network data, hence, they add
         * attributes on importing the graph that was earlier created using GraphSpace and instead of replacing the values for conflicting attributes,
         * those values are stored under different attribute names.
         * This creates duplicate attribute values which results in an error while exporting the String to a json object.
         * Hence, the attributes which are not required by GraphSpace and Cytoscape are deleted before exporting to resolve this conflict.
         * This might be problematic in case the user needs to use the graph with other apps which uses conflicting attributes.
         */
        styleJSONString = styleJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
        styleJSONString = styleJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
        styleJSONString = styleJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
        styleJSONString = styleJSONString.replaceAll("(?m)^*.\"source_original\".*", "");
        styleJSONString = styleJSONString.replaceAll("(?m)^*.\"target_original\".*", "");

        //style string converted to graphJson
        JSONArray styleJSONArray = new JSONArray(styleJSONString);
        return styleJSONArray.getJSONObject(0);
    }
}
