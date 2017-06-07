import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class GetGraphsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	/**
	 * @wbp.nonvisual location=61,14
	 */
	private final JTextField CyGraphSpace = new JTextField();
	private final JTextArea txtrAuthenticatedAsSethirishabhiitdgmailcom = new JTextArea();
	private JTextField txtHomoSapien;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GetGraphsDialog dialog = new GetGraphsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GetGraphsDialog() {
		CyGraphSpace.setText("Get networks from GraphSpace");
		CyGraphSpace.setColumns(10);
		setBounds(100, 100, 943, 678);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Network Name", "User", "Accissibility", "Group", "Tags", "Number of Nodes", "Number of Edges", "Last Modified"},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Network Name", "User", "Accessibility", "Group", "Tags", "Number of Nodes", "Number of Edges", "Last Modified"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setBounds(27, 165, 863, 416);
		contentPanel.add(table);
		txtrAuthenticatedAsSethirishabhiitdgmailcom.setBounds(27, 13, 356, 22);
		txtrAuthenticatedAsSethirishabhiitdgmailcom.setWrapStyleWord(true);
		txtrAuthenticatedAsSethirishabhiitdgmailcom.setBackground(SystemColor.control);
		txtrAuthenticatedAsSethirishabhiitdgmailcom.setEditable(false);
		txtrAuthenticatedAsSethirishabhiitdgmailcom.setText("Authenticated as: sethirishabhiitd@gmail.com");
		
		contentPanel.add(txtrAuthenticatedAsSethirishabhiitdgmailcom);
		
		txtHomoSapien = new JTextField();
		txtHomoSapien.setText("homo sapien");
		txtHomoSapien.setBounds(27, 48, 754, 22);
		contentPanel.add(txtHomoSapien);
		txtHomoSapien.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(793, 47, 97, 25);
		contentPanel.add(btnSearch);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"View Graphs by Tag", "View Graphs by Group"}));
		comboBox.setBounds(27, 83, 356, 22);
		contentPanel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Tag: GSOC 2017"}));
		comboBox_1.setBounds(457, 83, 324, 22);
		contentPanel.add(comboBox_1);
		
		JButton btnGetGraphs = new JButton("Get Graphs\r\n");
		btnGetGraphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnGetGraphs.setBounds(793, 82, 97, 25);
		contentPanel.add(btnGetGraphs);
		
		JTextArea txtrView = new JTextArea();
		txtrView.setWrapStyleWord(true);
		txtrView.setText("View");
		txtrView.setEditable(false);
		txtrView.setBackground(SystemColor.menu);
		txtrView.setBounds(27, 118, 36, 22);
		contentPanel.add(txtrView);
		
		JCheckBox chckbxAllGraphs = new JCheckBox("All Graphs");
		chckbxAllGraphs.setSelected(false);
		chckbxAllGraphs.setBounds(81, 117, 97, 25);
		contentPanel.add(chckbxAllGraphs);
		
		JCheckBox chckbxPublicGraphs = new JCheckBox("Public Graphs");
		chckbxPublicGraphs.setSelected(false);
		chckbxPublicGraphs.setBounds(193, 117, 128, 25);
		contentPanel.add(chckbxPublicGraphs);
		
		JCheckBox chckbxSharedGraphs = new JCheckBox("Private Graphs");
		chckbxSharedGraphs.setSelected(false);
		chckbxSharedGraphs.setBounds(325, 117, 128, 25);
		contentPanel.add(chckbxSharedGraphs);
		
		JCheckBox checkBox = new JCheckBox("Shared Graphs");
		checkBox.setSelected(false);
		checkBox.setBounds(467, 117, 128, 25);
		contentPanel.add(checkBox);
		
		JButton btnLoadNetworks = new JButton("Load Networks");
		btnLoadNetworks.setBounds(27, 594, 172, 25);
		contentPanel.add(btnLoadNetworks);
		
		JButton btnDoneLoadingNetworks = new JButton("Done Loading Networks");
		btnDoneLoadingNetworks.setBounds(718, 594, 172, 25);
		contentPanel.add(btnDoneLoadingNetworks);
	}
}
