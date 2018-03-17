package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.PostGraphExportUtils;

import javax.swing.*;

/**
 * This class defines the action performed on exporting a graph to GraphSpace from Cytoscape
 * @author rishabh
 *
 */
public class PostGraphMenuActionListener implements ActionListener {

    private static JFrame parent;
    private static JFrame loadingFrame;
    private static ImageIcon loading;
    private static JLabel loadingLabel;
    private static AuthenticationDialog dialog;

    public PostGraphMenuActionListener() {
        loadingFrame = new JFrame("Checking if update Possible");
        loading = new ImageIcon(this.getClass().getClassLoader().getResource("loading.gif"));
        loadingLabel = new JLabel("", loading, JLabel.CENTER);
        loadingFrame.add(loadingLabel);
        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingFrame.setSize(400, 300);
    }

    //called when export menu is clicked by the user
    @Override
    public void actionPerformed(ActionEvent e) {

        parent = CyObjectManager.INSTANCE.getApplicationFrame();
        loadingFrame.setLocationRelativeTo(parent);

        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();

        //if there is no network to export, display an error
        if (currentNetwork == null) {
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }

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

            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);
        }
    }
}
