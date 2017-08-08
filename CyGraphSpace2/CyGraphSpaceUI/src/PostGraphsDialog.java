import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class PostGraphsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtWithName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PostGraphsDialog dialog = new PostGraphsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PostGraphsDialog() {
		setTitle("CyGraphSpace - Upload Network to GraphSpace");
		setBounds(100, 100, 622, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JTextArea txtrAuthenticatedAsSethirishabhiitdgmailcom = new JTextArea();
			txtrAuthenticatedAsSethirishabhiitdgmailcom.setBackground(SystemColor.control);
			txtrAuthenticatedAsSethirishabhiitdgmailcom.setText("Authenticated as: sethirishabhiitd@gmail.com");
			txtrAuthenticatedAsSethirishabhiitdgmailcom.setBounds(22, 13, 356, 22);
			contentPanel.add(txtrAuthenticatedAsSethirishabhiitdgmailcom);
		}
		
		JRadioButton rdbtnUploadThisNetwork = new JRadioButton("Upload this network");
		rdbtnUploadThisNetwork.setBounds(22, 46, 173, 25);
		contentPanel.add(rdbtnUploadThisNetwork);
		
		JRadioButton rdbtnUploadAJson = new JRadioButton("Upload a json file");
		rdbtnUploadAJson.setBounds(234, 46, 173, 25);
		contentPanel.add(rdbtnUploadAJson);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Update Existing Network");
		chckbxNewCheckBox.setBounds(427, 46, 193, 25);
		contentPanel.add(chckbxNewCheckBox);
		
		txtWithName = new JTextField();
		txtWithName.setText("Enter the name of the Network");
		txtWithName.setBounds(78, 80, 259, 22);
		contentPanel.add(txtWithName);
		txtWithName.setColumns(10);
		
		JTextArea txtrName = new JTextArea();
		txtrName.setBackground(SystemColor.control);
		txtrName.setText("Name:");
		txtrName.setBounds(22, 80, 44, 22);
		contentPanel.add(txtrName);
		
		JTextArea txtrTags = new JTextArea();
		txtrTags.setBackground(SystemColor.control);
		txtrTags.setText("Tags:  GSOC 17");
		txtrTags.setBounds(22, 115, 305, 22);
		contentPanel.add(txtrTags);
		
		JButton btnaddTags = new JButton("+Add Tags");
		btnaddTags.setBounds(22, 137, 108, 25);
		contentPanel.add(btnaddTags);
		
		JTextArea txtrGroupsGsoc = new JTextArea();
		txtrGroupsGsoc.setBackground(SystemColor.control);
		txtrGroupsGsoc.setText("Groups: GSOC 17 group, CyGraphSpace group");
		txtrGroupsGsoc.setBounds(22, 175, 385, 22);
		contentPanel.add(txtrGroupsGsoc);
		
		JButton btnaddGroups = new JButton("+Add Groups");
		btnaddGroups.setBounds(22, 198, 108, 25);
		contentPanel.add(btnaddGroups);
		
		JTextArea txtrNumberOfNodes = new JTextArea();
		txtrNumberOfNodes.setBackground(SystemColor.control);
		txtrNumberOfNodes.setText("Number of Nodes:");
		txtrNumberOfNodes.setBounds(22, 236, 158, 22);
		contentPanel.add(txtrNumberOfNodes);
		
		JTextArea txtrNumberOfEdges = new JTextArea();
		txtrNumberOfEdges.setText("Number of Edges:");
		txtrNumberOfEdges.setBackground(SystemColor.menu);
		txtrNumberOfEdges.setBounds(22, 271, 158, 22);
		contentPanel.add(txtrNumberOfEdges);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			JButton btnUploadNetworkTo = new JButton("Upload Network to GraphSpace");
			buttonPane.add(btnUploadNetworkTo);
			
			JButton btnNewButton = new JButton("Cancel\r\n");
			buttonPane.add(btnNewButton);
		}
	}
}
