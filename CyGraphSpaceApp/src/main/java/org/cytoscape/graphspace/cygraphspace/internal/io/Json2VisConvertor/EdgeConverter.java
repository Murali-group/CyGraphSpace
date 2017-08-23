package org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor;

import static org.cytoscape.view.presentation.property.BasicVisualLexicon.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.model.CyEdge;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.LineTypeVisualProperty;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;

import static org.cytoscape.graphspace.cygraphspace.internal.util.Json2VisConvertor.CytoscapeJsToken.*;
import org.json.JSONObject;

public class EdgeConverter{
	
	JSONObject edgeStyle;
	View<CyEdge> edgeView;
	
	public EdgeConverter(JSONObject edgeStyle, View<CyEdge> edgeView) {
		this.edgeStyle = edgeStyle;
		this.edgeView = edgeView;
	}
	
	public void convert() {
		
		if(edgeStyle.has(COLOR.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_LABEL_COLOR, Color.decode(edgeStyle.getString(COLOR.getTag())));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(COLOR.getTag()) + " can't be set as edge label color");
			}
		}
		if(edgeStyle.has(LINE_COLOR.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_UNSELECTED_PAINT, Color.decode(edgeStyle.getString(LINE_COLOR.getTag())));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(LINE_COLOR.getTag()) + " can't be set as edge unselected paint");
			}
		}
		if(edgeStyle.has(WIDTH.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_WIDTH, Double.parseDouble(edgeStyle.getString(WIDTH.getTag()).replace("px", "")));
			}
			catch (Exception e) {
				System.out.println(edgeStyle.getString(WIDTH.getTag()) + " can't be set as edge width");
			}
		}
		if(edgeStyle.has(LABEL.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_LABEL, edgeStyle.getString(LABEL.getTag()));
			}
			catch (Exception e) {
				System.out.println(edgeStyle.getString(LABEL.getTag()) + " can't be set as edge label");
			}
		}
		if(edgeStyle.has(CONTENT.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_LABEL, edgeStyle.getString(CONTENT.getTag()));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(CONTENT.getTag()) + " can't be set as edge label");
			}
		}
		if(edgeStyle.has(LINE_STYLE.getTag())) {
			try {
				convertLineStyle(edgeStyle.getString(LINE_STYLE.getTag()));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(LINE_STYLE.getTag()) + " can't be set as edge line type");
			}
		}
		if(edgeStyle.has(FONT_SIZE.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_LABEL_FONT_SIZE, Integer.parseInt(edgeStyle.getString(FONT_SIZE.getTag()).replace("px", "")));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(FONT_SIZE.getTag()) + " can't be set as edge label font size");
			}
		}
		if(edgeStyle.has(TEXT_OPACITY.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_LABEL_TRANSPARENCY, Integer.parseInt(edgeStyle.getString(TEXT_OPACITY.getTag())));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(TEXT_OPACITY.getTag()) + " can't be set as edge label transparency");
			}
		}
		if(edgeStyle.has(OPACITY.getTag())) {
			try {
				edgeView.setVisualProperty(EDGE_TRANSPARENCY, Integer.parseInt(edgeStyle.getString(OPACITY.getTag())));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(OPACITY.getTag()) + " can't be set as edge transparency");
			}
		}
		if(edgeStyle.has(SOURCE_ARROW_SHAPE.getTag())) {
			try {
				convertSourceArrowShape(edgeStyle.getString(SOURCE_ARROW_SHAPE.getTag()));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(SOURCE_ARROW_SHAPE.getTag()) + " can't be set as edge source arrow shape");
			}
		}
		if(edgeStyle.has(TARGET_ARROW_SHAPE.getTag())) {
			try {
				convertTargetArrowShape(edgeStyle.getString(TARGET_ARROW_SHAPE.getTag()));
			}
			catch(Exception e) {
				System.out.println(edgeStyle.getString(TARGET_ARROW_SHAPE.getTag()) + " can't be set as edge target arrow shape");
			}
		}
	}
	
	private void convertSourceArrowShape(String shape) {
		switch(shape) {
		case "triangle":
			edgeView.setVisualProperty(EDGE_SOURCE_ARROW_SHAPE, ArrowShapeVisualProperty.DELTA);
			break;
		case "circle":
			edgeView.setVisualProperty(EDGE_SOURCE_ARROW_SHAPE, ArrowShapeVisualProperty.CIRCLE);
			break;
		case "tee":
			edgeView.setVisualProperty(EDGE_SOURCE_ARROW_SHAPE, ArrowShapeVisualProperty.T);
			break;
		case "diamond":
			edgeView.setVisualProperty(EDGE_SOURCE_ARROW_SHAPE, ArrowShapeVisualProperty.DIAMOND);
			break;
		case "none":
			edgeView.setVisualProperty(EDGE_SOURCE_ARROW_SHAPE, ArrowShapeVisualProperty.NONE);
			break;
		default:
			System.out.println("Shape not supported: " + shape);
			edgeView.setVisualProperty(EDGE_SOURCE_ARROW_SHAPE, ArrowShapeVisualProperty.NONE);
		}
	}
	
	private void convertTargetArrowShape(String shape) {
		switch(shape) {
		case "triangle":
			edgeView.setVisualProperty(EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.DELTA);
			break;
		case "circle":
			edgeView.setVisualProperty(EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.CIRCLE);
			break;
		case "tee":
			edgeView.setVisualProperty(EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.T);
			break;
		case "diamond":
			edgeView.setVisualProperty(EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.DIAMOND);
			break;
		case "none":
			edgeView.setVisualProperty(EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.NONE);
			break;
		default:
			System.out.println("Shape not supported: " + shape);
			edgeView.setVisualProperty(EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.NONE);
		}
	}
	
	private void convertLineStyle(String lineStyle) {
		switch(lineStyle) {
		case "solid":
			edgeView.setVisualProperty(EDGE_LINE_TYPE, LineTypeVisualProperty.SOLID);
			break;
		case "dotted":
			edgeView.setVisualProperty(EDGE_LINE_TYPE, LineTypeVisualProperty.DOT);
			break;
		case "dashed":
			edgeView.setVisualProperty(EDGE_LINE_TYPE, LineTypeVisualProperty.EQUAL_DASH);
			break;
		default:
			System.out.println("Line style not supported: " + lineStyle);
			edgeView.setVisualProperty(EDGE_LINE_TYPE, LineTypeVisualProperty.SOLID);
		}
	}
}