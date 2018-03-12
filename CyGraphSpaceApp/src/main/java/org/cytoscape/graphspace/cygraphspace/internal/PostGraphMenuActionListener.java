package org.cytoscape.graphspace.cygraphspace.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

    private JFrame parent;
    private JFrame loadingFrame;
    private ImageIcon loading;
    private JLabel loadingLabel;
    private AuthenticationDialog dialog;

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
        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();

        //loading frame while checking if updating the graph is possible
        loadingFrame.setLocationRelativeTo(parent);

        //if there is no network to export, display an error
        if (currentNetwork == null) {
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }

        //if there is a network and the user is currently authenticated, create a post graph dialog
        if (Server.INSTANCE.isAuthenticated()) {
            loadingFrame.setVisible(true);
            try {
                PostGraphExportUtils.populate(parent, loadingFrame);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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
