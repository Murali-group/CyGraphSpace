package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.MessageConfig;
import org.cytoscape.graphspace.cygraphspace.internal.util.PostGraphExportUtils;

import javax.swing.*;

/**
 * This class defines the action performed on exporting a graph to GraphSpace from Cytoscape
 * @author rishabh
 *
 */
public class PostGraphMenuActionListener implements ActionListener {

    private static JFrame loadingFrame;
    private static ImageIcon loading;
    private static JLabel loadingLabel;
    private static AuthenticationDialog dialog;

    public PostGraphMenuActionListener() {
        loadingFrame = new JFrame("Checking if the graph already exists");
        loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
        loadingLabel = new JLabel("", loading, JLabel.CENTER);
        loadingFrame.add(loadingLabel);
        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingFrame.setSize(400, 300);
    }

    //called when export menu is clicked by the user
    @Override
    public void actionPerformed(ActionEvent e) {

        // temporary hack for the issue that CyGraphSpace fails on uploading large network
        // create a hard check now allowing user to upload network with more than 400 nodes or 1000 edges
        // remove in the future when the issue has been fixed
        if (CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork().getNodeCount() > 400
                || CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork().getEdgeCount() > 1000) {
            JOptionPane.showMessageDialog(null, 
                    MessageConfig.NETWORK_TOO_LARGE_MSG, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadingFrame.setLocationRelativeTo(CyObjectManager.INSTANCE.getApplicationFrame());

        //if there is a network and the user is currently authenticated, create a post graph dialog
        if (Server.INSTANCE.isAuthenticated()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    loadingFrame.setVisible(true);
                }
            });

            new Thread() {
                public void run() {
                    try {
                        PostGraphExportUtils.populate(CyObjectManager.INSTANCE.getApplicationFrame(), loadingFrame);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        //if there is a network but the user is not authenticated, open the login dialog for the user to log in. Once logged in, open the post graph dialog
        else {
            if (dialog == null)
                dialog = new AuthenticationDialog(loadingFrame);

            dialog.setLocationRelativeTo(CyObjectManager.INSTANCE.getApplicationFrame());
            dialog.setVisible(true);
        }
    }
}
