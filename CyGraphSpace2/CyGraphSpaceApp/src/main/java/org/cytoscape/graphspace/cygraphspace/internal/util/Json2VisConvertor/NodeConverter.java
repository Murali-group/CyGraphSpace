package org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

import java.awt.Color;

import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;

import static org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor.CytoscapeJsToken.*;
import org.json.JSONObject;

public class NodeConverter{
	
	JSONObject nodeStyle;
	View<CyNode> nodeView;
	
//	private Map<CytoscapeJsToken, VisualProperty<?>> tag2nodeVp = new HashMap<CytoscapeJsToken, VisualProperty<?>>();
	
	public NodeConverter(JSONObject nodeStyle, View<CyNode> nodeView) {
		this.nodeStyle = nodeStyle;
		this.nodeView = nodeView;
	}
	
	public void convert() {
		if(nodeStyle.has(COLOR.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_LABEL_COLOR, Color.decode(nodeStyle.getString(COLOR.getTag())));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(COLOR.getTag()) + " can't be set as node label color");
			}
		}
		if(nodeStyle.has(BACKGROUND_COLOR.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_PAINT, Color.decode(nodeStyle.getString(BACKGROUND_COLOR.getTag())));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(BACKGROUND_COLOR.getTag()) + " can't be set as node paint");
			}
		}
		if(nodeStyle.has(WIDTH.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_WIDTH, Double.parseDouble(nodeStyle.getString(WIDTH.getTag()).replace("px", "")));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(WIDTH.getTag()) + " can't be set as width");
			}
		}
		if(nodeStyle.has(HEIGHT.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_HEIGHT, Double.parseDouble(nodeStyle.getString(HEIGHT.getTag()).replace("px", "")));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(HEIGHT.getTag()) + " can't be set as height");
			}
		}
		if(nodeStyle.has(SHAPE.getTag())) {
			try {
				convertNodeShape(nodeStyle.getString(SHAPE.getTag()));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(SHAPE.getTag()) + " can't be set as node shape");
			}
			
		}
		if(nodeStyle.has(LABEL.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_LABEL, nodeStyle.getString(LABEL.getTag()));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(LABEL.getTag()) + " can't be set as node label");
			}
		}
		if(nodeStyle.has(CONTENT.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_LABEL, nodeStyle.getString(CONTENT.getTag()));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(CONTENT.getTag()) + " can't be set as node label");
			}
		}
		if(nodeStyle.has(BORDER_WIDTH.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_BORDER_WIDTH, Double.parseDouble(nodeStyle.getString(BORDER_WIDTH.getTag()).replace("px", "")));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(BORDER_WIDTH.getTag()) + " can't be set as node border width");
			}
		}
		if(nodeStyle.has(OPACITY.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_TRANSPARENCY, Integer.parseInt(nodeStyle.getString(OPACITY.getTag())));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(OPACITY.getTag()) + " can't be set as node transparency");
			}
		}
		if(nodeStyle.has(BACKGROUND_OPACITY.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_TRANSPARENCY, Integer.parseInt(nodeStyle.getString(BACKGROUND_OPACITY.getTag())));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(BACKGROUND_OPACITY.getTag()) + " can't be set as node transparency");
			}
		}
		if(nodeStyle.has(BORDER_OPACITY.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_BORDER_TRANSPARENCY, Integer.parseInt(nodeStyle.getString(BORDER_OPACITY.getTag())));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(BORDER_OPACITY.getTag()) + " can't be set as node border transparency");
			}
		}
		if(nodeStyle.has(TEXT_OPACITY.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_LABEL_TRANSPARENCY, Integer.parseInt(nodeStyle.getString(TEXT_OPACITY.getTag())));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(TEXT_OPACITY.getTag()) + " can't be set as node label transparency");
			}
		}
		if(nodeStyle.has(FONT_SIZE.getTag())) {
			try {
				nodeView.setVisualProperty(NODE_LABEL_FONT_SIZE, Integer.parseInt(nodeStyle.getString(FONT_SIZE.getTag()).replace("px", "")));
			}
			catch(Exception e) {
				System.out.println(nodeStyle.getString(FONT_SIZE.getTag()) + " can't be set as node label font size");
			}
		}
	}
	
	private void convertNodeShape(String shape) {
		switch(shape) {
		case "diamond":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.DIAMOND);
			break;
		case "ellipse":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.ELLIPSE);
			break;
		case "hexagon":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.HEXAGON);
			break;
		case "octagon":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.OCTAGON);
			break;
		case "parallelogram":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.PARALLELOGRAM);
			break;
		case "rectangle":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.RECTANGLE);
			break;
		case "roundrectangle":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.ROUND_RECTANGLE);
			break;
		case "triangle":
			nodeView.setVisualProperty(NODE_SHAPE, NodeShapeVisualProperty.TRIANGLE);
			break;
		}
	}
}