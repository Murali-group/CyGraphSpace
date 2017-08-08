package org.cytoscape.graphspace.cygraphspace.internal;

import static org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor.CytoscapeJsToken.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.graphspace.cygraphspace.internal.gui.AuthenticationDialog;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.CyObjectManager;
import org.cytoscape.graphspace.cygraphspace.internal.singletons.Server;
import org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor.EdgeConverter;
import org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor.NodeConverter;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskIterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoadStyleAction extends AbstractCyAction{
	private static final long serialVersionUID = 1L;
	private NodeConverter nodeConverter;
	private EdgeConverter edgeConverter;
	public LoadStyleAction(final String menuTitle, CyApplicationManager cyApplicationManager) {
        super(menuTitle, cyApplicationManager, null, null);
        setPreferredMenu("Apps.CyGraphSpace");
    }

    @Override
	public void actionPerformed(ActionEvent e) {
    	CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
    	if( currentNetwork == null ){
            String msg = "There is no graph to export.";
            String dialogTitle = "No Graph Found";
            JOptionPane.showMessageDialog(new Frame(), msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }
    	if (Server.INSTANCE.isAuthenticated()){
    		JSONObject graphJSON;
			try {
				graphJSON = exportNetworkToJSON();
//				String graphName = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork().NAME;
				String graphName = graphJSON.getJSONObject("data").getString("name");
				System.out.println(graphName);
				if (Server.INSTANCE.updatePossible(graphName)) {
					JSONObject response = Server.INSTANCE.client.getGraphRequest(graphName);
					JSONObject styleJSON = response.getJSONObject("body").getJSONObject("object").getJSONArray("graphs")
											.getJSONObject(0).getJSONObject("style_json");
					JSONArray styleArray = styleJSON.getJSONArray("style");
					CyNetworkView networkView = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetworkView();
					
					CyTable nodeTable = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork().getDefaultNodeTable();
					List<CyRow> nodeRows = nodeTable.getAllRows();
					List<CyNode> nodes = currentNetwork.getNodeList();
					for (int i=0; i<styleArray.length();i++) {
						String selectorAttr = ((JSONObject)styleArray.getJSONObject(i)).getString("selector");
						if (selectorAttr.equals("node")) {
							for (CyNode node: nodes) {
								View<CyNode> nodeView = networkView.getNodeView(node);
								if(styleArray.getJSONObject(i).has("css")) {
									nodeConverter = new NodeConverter(styleArray.getJSONObject(i).getJSONObject("css"), nodeView);
									nodeConverter.convert();
								}
								else if(styleArray.getJSONObject(i).has("style")) {
									nodeConverter = new NodeConverter(styleArray.getJSONObject(i).getJSONObject("style"), nodeView);
									nodeConverter.convert();
								}
							}
						}
						if (selectorAttr.equals("node:selected")) {
							if(styleArray.getJSONObject(i).has("css")) {
								networkView.setVisualProperty(BasicVisualLexicon.NODE_SELECTED_PAINT, 
										Color.decode(styleArray.getJSONObject(i).getJSONObject("css").getString(BACKGROUND_COLOR.toString())));
							}
							else if(styleArray.getJSONObject(i).has("style")) {
								networkView.setVisualProperty(BasicVisualLexicon.NODE_SELECTED_PAINT, 
										Color.decode(styleArray.getJSONObject(i).getJSONObject("style").getString(BACKGROUND_COLOR.toString())));
							}
						}
					}
			    	
					for(CyRow row: nodeRows) {
						String nodeName = row.get("name", String.class);
						Long suid = row.get("SUID", Long.class);
						for (int i=0; i<styleArray.length();i++) {
							String selectorAttr = ((JSONObject)styleArray.get(i)).getString("selector");
							if (selectorAttr.contains("node[name=")){
								String selectorName = selectorAttr.substring(selectorAttr.indexOf("=")+2, selectorAttr.length()-2);
								if (selectorName.equals(nodeName)) {
									View<CyNode> nodeView = networkView.getNodeView(currentNetwork.getNode(suid));
									if (styleArray.getJSONObject(i).has("css")) {
										nodeConverter = new NodeConverter(styleArray.getJSONObject(i).getJSONObject("css"), nodeView);
										nodeConverter.convert();
									}
									else if(styleArray.getJSONObject(i).has("style")) {
										nodeConverter = new NodeConverter(styleArray.getJSONObject(i).getJSONObject("style"), nodeView);
										nodeConverter.convert();
									}
									else {
										System.out.println("ye kaise ho gya");
									}
								}
							}
						}
					}
					
					CyTable edgeTable = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork().getDefaultEdgeTable();
					List<CyRow> edgeRows = edgeTable.getAllRows();
					List<CyEdge> edges = currentNetwork.getEdgeList();
					for (int i=0; i<styleArray.length();i++) {
						String selectorAttr = ((JSONObject)styleArray.getJSONObject(i)).getString("selector");
						if (selectorAttr.equals("edge")) {
							for (CyEdge edge: edges) {
								View<CyEdge> edgeView = networkView.getEdgeView(edge);
								if(styleArray.getJSONObject(i).has("css")) {
									edgeConverter = new EdgeConverter(styleArray.getJSONObject(i).getJSONObject("css"), edgeView);
									edgeConverter.convert();
								}
								else if(styleArray.getJSONObject(i).has("style")) {
									edgeConverter = new EdgeConverter(styleArray.getJSONObject(i).getJSONObject("style"), edgeView);
									edgeConverter.convert();
								}
							}
						}
						if (selectorAttr.equals("edge:selected")) {
							if(styleArray.getJSONObject(i).has("css")) {
								networkView.setVisualProperty(BasicVisualLexicon.EDGE_SELECTED_PAINT, 
										Color.decode(styleArray.getJSONObject(i).getJSONObject("css").getString(LINE_COLOR.toString())));
							}
							else if(styleArray.getJSONObject(i).has("style")) {
								networkView.setVisualProperty(BasicVisualLexicon.EDGE_SELECTED_PAINT, 
										Color.decode(styleArray.getJSONObject(i).getJSONObject("style").getString(LINE_COLOR.toString())));
							}

						}
					}
			    	
					for(CyRow row: edgeRows) {
						String edgeName = row.get("name", String.class);
						Long suid = row.get("SUID", Long.class);
						for (int i=0; i<styleArray.length();i++) {
							String selectorAttr = ((JSONObject)styleArray.get(i)).getString("selector");
							if (selectorAttr.contains("edge[name=")){
								String selectorName = selectorAttr.substring(selectorAttr.indexOf("=")+2, selectorAttr.length()-2);
								if (selectorName.equals(edgeName)) {
									View<CyEdge> edgeView = networkView.getEdgeView(currentNetwork.getEdge(suid));
									if (styleArray.getJSONObject(i).has("css")) {
										edgeConverter = new EdgeConverter(styleArray.getJSONObject(i).getJSONObject("css"), edgeView);
										edgeConverter.convert();
									}
									else if(styleArray.getJSONObject(i).has("style")) {
										edgeConverter = new EdgeConverter(styleArray.getJSONObject(i).getJSONObject("style"), edgeView);
										edgeConverter.convert();
									}
									else {
										System.out.println("ye kaise ho gya");
									}
								}
							}
						}
					}
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
        }
    	
//    	System.out.println(networkView.getAllViews().toString());
//    	EdgeConverter edgeConverter = new EdgeConverter();
//    	NodeConverter nodeConverter = new NodeConverter();
    	
    }
    
    private JSONObject exportNetworkToJSON() throws IOException{
		File tempFile = File.createTempFile("CyGraphSpaceExport", ".cyjs");
		CyNetwork network = CyObjectManager.INSTANCE.getApplicationManager().getCurrentNetwork();
		TaskIterator ti = CyObjectManager.INSTANCE.getExportNetworkTaskFactory().createTaskIterator(network, tempFile);
		CyObjectManager.INSTANCE.getTaskManager().execute(ti);
		String graphJSONString = FileUtils.readFileToString(tempFile, "UTF-8");
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
		tempFile.delete();
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_name\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"id_original\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"shared_interaction\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"source_original\".*", "");
		graphJSONString = graphJSONString.replaceAll("(?m)^*.\"target_original\".*", "");
		JSONObject graphJSON = new JSONObject(graphJSONString);
        return graphJSON;
	}
}