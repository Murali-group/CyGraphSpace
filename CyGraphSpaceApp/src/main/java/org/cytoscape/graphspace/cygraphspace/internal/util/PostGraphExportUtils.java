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

    private static boolean isStringNetwork;

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
        CyObjectManager.INSTANCE.getTaskManager().execute(ti);

        //read the file contents to a string
        String graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");

        //ugly way to wait for the parallel process of reading to be completed
        int count = 0;
        while(graphJSONString.isEmpty()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
            count++;
            if (count>=10){
                return null;
            }
        }

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

        // check if graph comes from StringNetwork
        isStringNetwork = false;
        if (graphJSON.has("data")) {
            JSONObject dataJson = graphJSON.getJSONObject("data");
            if (dataJson.has("database") && dataJson.has("name")) {
                isStringNetwork = dataJson.getString("database").equals("string") 
                        && dataJson.getString("name").contains("String Network");
                graphJSON = handleStringNetwork(graphJSONString);
            }
        }

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
        CyObjectManager.INSTANCE.getTaskManager().execute(ti);

        //read the file contents to a string
        String styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");

        //ugly way to wait for the parallel process of reading to be completed
        int count = 0;
        while(styleJSONString.isEmpty()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            styleJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
            count++;
            if (count>=10){
                return null;
            }
        }

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

        if (isStringNetwork) {
            JSONObject styleJSON = styleJSONArray.getJSONObject(0);
            styleJSON.getJSONArray("style").getJSONObject(0).getJSONObject("css").put("background-image", "data(STRING_style)");
            //styleJSON.getJSONArray("style").getJSONObject(0).getJSONObject("css").put("background-image-opacity", 0.5);
            return styleJSON;
        }

        return styleJSONArray.getJSONObject(0);
    }

    private static JSONObject handleStringNetwork(String graphJsonString) {
        // By capture more characters it avoids creating empty field for nodes without background image
        graphJsonString = graphJsonString.replaceAll("\"STRING_style\" : \"string:data:image/png;", "PlaceHolder\n\"STRING_style\" : \"data:image/png;");

        String glassStyle = "\"GLASS_style\": \"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGL"
                + "TgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4"
                + "xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+Cjxz"
                + "dmcgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsa"
                + "W5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBwcmVzZXJ2ZUFzcGVjdFJhdGlvPSJ4TWlkWU"
                + "1pZCBtZWV0IiB2aWV3Qm94PSIwIDAgNjQwIDY0MCIgd2lkdGg9IjY0MCIgaGVpZ2h0PSI2NDAiPjxkZWZ"
                + "zPjxwYXRoIGQ9Ik00MjEuNTMgMzA5LjEzQzQyMS41MyAzNjUuODQgMzc0LjI2IDQxMS44OCAzMTYuMDMg"
                + "NDExLjg4QzI1Ny44IDQxMS44OCAyMTAuNTMgMzY1Ljg0IDIxMC41MyAzMDkuMTNDMjEwLjUzIDI1Mi40M"
                + "iAyNTcuOCAyMDYuMzggMzE2LjAzIDIwNi4zOEMzNzQuMjYgMjA2LjM4IDQyMS41MyAyNTIuNDIgNDIxLj"
                + "UzIDMwOS4xM1oiIGlkPSJtMkxQQUFkZklFIj48L3BhdGg+PGxpbmVhckdyYWRpZW50IGlkPSJncmFkaWV"
                + "udGI3QkxSV1FCTyIgZ3JhZGllbnRVbml0cz0idXNlclNwYWNlT25Vc2UiIHgxPSIzMTYuNDciIHkxPSIx"
                + "MTUuNTUiIHgyPSIzMTYuNDciIHkyPSI0MTEuODgiPjxzdG9wIHN0eWxlPSJzdG9wLWNvbG9yOiAjMDEwM"
                + "DAwO3N0b3Atb3BhY2l0eTogMSIgb2Zmc2V0PSIwJSI+PC9zdG9wPjxzdG9wIHN0eWxlPSJzdG9wLWNvbG"
                + "9yOiAjZmZmZmZmO3N0b3Atb3BhY2l0eTogMSIgb2Zmc2V0PSIxMDAlIj48L3N0b3A+PC9saW5lYXJHcmF"
                + "kaWVudD48cGF0aCBkPSJNMjQ5LjUyIDI1MC44MUMyNDkuNzYgMjcwLjMxIDI3OS43NiAyODUuNzggMzE2"
                + "LjQ3IDI4NS4zM0MzNTMuMTggMjg0Ljg3IDM4Mi43OSAyNjguNjcgMzgyLjU0IDI0OS4xNkMzODIuMyAyM"
                + "jkuNjYgMzUyLjMgMjE0LjE5IDMxNS41OSAyMTQuNjRDMjc4Ljg4IDIxNS4xIDI0OS4yOCAyMzEuMyAyND"
                + "kuNTIgMjUwLjgxWiIgaWQ9ImNUR1ZyNFVZeCI+PC9wYXRoPjxyYWRpYWxHcmFkaWVudCBpZD0iZ3JhZGl"
                + "lbnRjMllqT3JIMHBYIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgY3g9IjMxNS4xNSIgY3k9"
                + "IjMxNS41NiIgcj0iODUuMzYiPjxzdG9wIHN0eWxlPSJzdG9wLWNvbG9yOiAjMDEwMDAwO3N0b3Atb3BhY"
                + "2l0eTogMSIgb2Zmc2V0PSIwJSI+PC9zdG9wPjxzdG9wIHN0eWxlPSJzdG9wLWNvbG9yOiAjYjhiOGI4O3"
                + "N0b3Atb3BhY2l0eTogMSIgb2Zmc2V0PSI3Mi4xMzQ0MTc1MTA5MzA4MyUiPjwvc3RvcD48c3RvcCBzdHl"
                + "sZT0ic3RvcC1jb2xvcjogI2ZmZmZmZjtzdG9wLW9wYWNpdHk6IDEiIG9mZnNldD0iMTAwJSI+PC9zdG9w"
                + "PjwvcmFkaWFsR3JhZGllbnQ+PC9kZWZzPjxnPjxnPjxnPjxnPjxmaWx0ZXIgaWQ9InNoYWRvdzEyMTE3"
                + "MTQ4IiB4PSIxODkuNTMiIHk9IjE4NS4zOCIgd2lkdGg9IjI1NC4wMSIgaGVpZ2h0PSIyNjAuNSIgZmls"
                + "dGVyVW5pdHM9InVzZXJTcGFjZU9uVXNlIiBwcmltaXRpdmVVbml0cz0idXNlclNwYWNlT25Vc2UiPjxm"
                + "ZUZsb29kPjwvZmVGbG9vZD48ZmVDb21wb3NpdGUgaW4yPSJTb3VyY2VBbHBoYSIgb3BlcmF0b3I9Imlu"
                + "Ij48L2ZlQ29tcG9zaXRlPjxmZUdhdXNzaWFuQmx1ciBzdGREZXZpYXRpb249IjEiPjwvZmVHYXVzc2lh"
                + "bkJsdXI+PGZlT2Zmc2V0IGR4PSIxIiBkeT0iMTMiIHJlc3VsdD0iYWZ0ZXJPZmZzZXQiPjwvZmVPZmZz"
                + "ZXQ+PGZlRmxvb2QgZmxvb2QtY29sb3I9IiMwMDAwMDAiIGZsb29kLW9wYWNpdHk9IjAuNSI+PC9mZUZs"
                + "b29kPjxmZUNvbXBvc2l0ZSBpbjI9ImFmdGVyT2Zmc2V0IiBvcGVyYXRvcj0iaW4iPjwvZmVDb21wb3Npd"
                + "GU+PGZlTW9ycGhvbG9neSBvcGVyYXRvcj0iZGlsYXRlIiByYWRpdXM9IjEiPjwvZmVNb3JwaG9sb2d5Pjx"
                + "mZUNvbXBvc2l0ZSBpbjI9IlNvdXJjZUFscGhhIiBvcGVyYXRvcj0ib3V0Ij48L2ZlQ29tcG9zaXRlPjwvZm"
                + "lsdGVyPjxwYXRoIGQ9Ik00MjEuNTMgMzA5LjEzQzQyMS41MyAzNjUuODQgMzc0LjI2IDQxMS44OCAzMTYuMDM"
                + "gNDExLjg4QzI1Ny44IDQxMS44OCAyMTAuNTMgMzY1Ljg0IDIxMC41MyAzMDkuMTNDMjEwLjUzIDI1Mi40MiAyNTcu"
                + "OCAyMDYuMzggMzE2LjAzIDIwNi4zOEMzNzQuMjYgMjA2LjM4IDQyMS41MyAyNTIuNDIgNDIxLjUzIDMwOS4xM1"
                + "oiIGlkPSJoNGdKUWJPSXZHIiBmaWxsPSJ3aGl0ZSIgZmlsbC1vcGFjaXR5PSIxIiBmaWx0ZXI9InVybCgjc2"
                + "hhZG93MTIxMTcxNDgpIj48L3BhdGg+PC9nPjx1c2UgeGxpbms6aHJlZj0iI20yTFBBQWRmSUUiIG9wYWNpd"
                + "Hk9IjEiIGZpbGw9InVybCgjZ3JhZGllbnRiN0JMUldRQk8pIj48L3VzZT48Zz48dXNlIHhsaW5rOmhyZWY9IiN"
                + "tMkxQQUFkZklFIiBvcGFjaXR5PSIxIiBmaWxsLW9wYWNpdHk9IjAiIHN0cm9rZT0iIzE2MTUxNSIgc3Ryb2tl"
                + "LXdpZHRoPSIyIiBzdHJva2Utb3BhY2l0eT0iMSI+PC91c2U+PC9nPjwvZz48Zz48dXNlIHhsaW5rOmhyZWY9Ii"
                + "NjVEdWcjRVWXgiIG9wYWNpdHk9IjEiIGZpbGw9InVybCgjZ3JhZGllbnRjMllqT3JIMHBYKSI+PC91c2U+PGc+PH"
                + "VzZSB4bGluazpocmVmPSIjY1RHVnI0VVl4IiBvcGFjaXR5PSIxIiBmaWxsLW9wYWNpdHk9IjAiIHN0cm9rZT0iIz"
                + "E0MTMxMyIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2Utb3BhY2l0eT0iMSI+PC91c2U+PC9nPjwvZz48L2c+PC9nPjwvc3ZnPg==\",";

        graphJsonString = graphJsonString.replaceAll("PlaceHolder", glassStyle);

        return new JSONObject(graphJsonString);
    }
}
