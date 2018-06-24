package org.cytoscape.graphspace.cygraphspace.internal.util;

/**
 * Util class for message configuration
 */
public class MessageConfig {

    // menu title for CyGraphSpaceMenuAction
    public final static String MenuActionTitle = "Network to GraphSpace...";

    // app description
    public final static String APP_DESCRIPTION = "<html>" + "The CyGraphSpace App is used to upload graphs to "
            + "<a href=\"http://www.grapshace.org\">GraphSpace</a>. ";

    // error messages related to authentication dialog
    public final static String AUTH_NOT_FILL_ERROR_MSG = "Please enter all the values.";
    public final static String AUTH_INVALID_URL = "IP address of the entered host could not be determined.\n"
            + "Please try again or enter a new host.";
    public final static String AUTH_MALFORMED_URL = "Malformed URL entered for host.\nPlease enter a new host.";
    public final static String AUTH_FAIL_MSG = "Could not authenticate you. "
            + "Please ensure the username and password are correct and that you are connected to the internet.";

    // Update graph task status message, shown in the status bar of the Cytoscape
    public final static String UPDATE_GRAPH_TASK_TITLE = "Update Status";
    public final static String UPDATE_GRAPH_TASK_STATUS_IN_PROGRESS = "Updating graph on GraphSpace. Please wait...";
    public final static String UPDATE_GRAPH_TASK_STATUS_FAIL = "CyGraphSpace failed to update the graph.";
    public final static String UPDATE_GRAPH_TASK_STATUS_SUCCESS = "CyGraphSpace updated the graph successfully.";

    // Update graph task dialog message, shown in the pop up window after update graph task is done
    public final static String UPDATE_GRAPH_TASK_DIALOG_SUCCESS = "You have updated the graph successfully.";
    public final static String UPDATE_GRAPH_TASK_DIALOG_FAIL = "CyGraphSpace failed to update the graph.";

    // Post graph task status message, shown in the status bar of the Cytoscape
    public final static String POST_GRAPH_TASK_TITLE = "Upload Status";
    public final static String POST_GRAPH_TASK_STATUS_IN_PROGRESS = "Uploading graph to GraphSpace. Please wait...";
    public final static String POST_GRAPH_TASK_STATUS_FAIL = "CyGraphSpace failed to upload the graph.";
    public final static String POST_GRAPH_TASK_STATUS_SUCCESS = "CyGraphSpace uploaded the graph successfully.";

    // Post graph task dialog message, shown in the pop up window after update graph task is done
    public final static String POST_GRAPH_TASK_DIALOG_SUCCESS = "You have uploaded the graph successfully.";
    public final static String POST_GRAPH_TASK_DIALOG_FAIL = "CyGraphSpace failed to upload the graph.";

    // Error message for event where user tries to upload large network
    public final static String NETWORK_TOO_LARGE_MSG = "Currently GraphSpace cannot handle networks with more than 400 nodes and/or 1000 edges.\n"
            + "Please select a smaller network or subnetwork to upload.";
    
    // Result panel status messages
    public static final String TASK_COMPLETE = "Complete";
    public static final String TASK_IN_PROGRESS = "In progress";
    public static final String TASK_FAIL = "Fail";
}
