package org.cytoscape.graphspace.cygraphspace.internal.util;

/**
 * Util class for message configuration
 */
public class MessageConfig {

    // menu title for CyGraphSpaceMenuAction
    public final static String MenuActionTitle = "Network to GraphSpace";

    // app description
    public final static String APP_DESCRIPTION = "<html>" + "CyGraphSpace App is used to import and export graphs from "
            + "<a href=\"http://www.grapshace.org\">GraphSpace</a> website. ";

    // error messages related to authentication dialog
    public final static String AUTH_NOT_FILL_ERROR_MSG = "Please enter all the values";
    public final static String AUTH_FAIL_MSG = "Could not authenticate you. "
            + "Please ensure the username and password are correct and that you are connected to the internet.";

    // Update graph task status message, shown in the status bar of the Cytoscape
    public final static String UPDATE_GRAPH_TASK_TITLE = "Update graph to GraphSpace";
    public final static String UPDATE_GRAPH_TASK_STATUS_IN_PROGRESS = "Updating graph to GraphSpace. Please wait...";
    public final static String UPDATE_GRAPH_TASK_STATUS_FAIL = "Update graph fails.";
    public final static String UPDATE_GRAPH_TASK_STATUS_SUCCESS = "Update graph successful.";

    // Update graph task dialog message, shown in the pop up window after update graph task is done
    public final static String UPDATE_GRAPH_TASK_DIALOG_SUCCESS = "Update graph successful.";
    public final static String UPDATE_GRAPH_TASK_DIALOG_FAIL = "Could not update graph.";

    // Post graph task status message, shown in the status bar of the Cytoscape
    public final static String POST_GRAPH_TASK_TITLE = "Export graph to GraphSpace";
    public final static String POST_GRAPH_TASK_STATUS_IN_PROGRESS = "Exporting graph to GraphSpace. Please wait...";
    public final static String POST_GRAPH_TASK_STATUS_FAIL = "Post graph fails.";
    public final static String POST_GRAPH_TASK_STATUS_SUCCESS = "Post graph successful.";

    // Post graph task dialog message, shown in the pop up window after update graph task is done
    public final static String POST_GRAPH_TASK_DIALOG_SUCCESS = "Post graph successful.";
    public final static String POST_GRAPH_TASK_DIALOG_FAIL = "Could not post graph.";
}
