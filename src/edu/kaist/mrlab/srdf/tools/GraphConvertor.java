package edu.kaist.mrlab.srdf.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.kaist.mrlab.srdf.data.GraphEdge;

public class GraphConvertor {

	private static BufferedReader filebr;
	private static BufferedWriter filebw;

	private HashMap<String, String> nodeMap = new HashMap<String, String>();
	private HashSet<GraphEdge> edgeSet = new HashSet<GraphEdge>();

	private static int nodeIDs = 1;

	public JSONObject convert() throws IOException {

		JSONObject result = new JSONObject();
		JSONArray nodes = new JSONArray();
		JSONArray edges = new JSONArray();

		String input = null;
		while ((input = filebr.readLine()) != null) {

			if (input.length() != 0 && !input.contains("STC:")) {
				
				StringTokenizer st = new StringTokenizer(input, "\t");

				String sbj = st.nextToken();
				String rel = st.nextToken();
				String obj = st.nextToken();

				if (!nodeMap.containsKey(sbj)) {
					nodeMap.put(sbj, String.valueOf(nodeIDs++));
				}
				if (!nodeMap.containsKey(rel)) {
					nodeMap.put(rel, String.valueOf(nodeIDs++));
				}
				if (!nodeMap.containsKey(obj) && !obj.equals("ANONYMOUS")) {
					nodeMap.put(obj, String.valueOf(nodeIDs++));
				}

				edgeSet.add(new GraphEdge(String.valueOf(nodeMap.get(sbj)), String.valueOf(nodeMap.get(rel))));

				if (!obj.equals("ANONYMOUS")) {
					edgeSet.add(new GraphEdge(String.valueOf(nodeMap.get(rel)), String.valueOf(nodeMap.get(obj))));
				}

			}
		}

		Iterator<String> ni = nodeMap.keySet().iterator();
		while (ni.hasNext()) {
			JSONObject node = new JSONObject();
			String label = ni.next();
			node.put("label", label);
			node.put("id", nodeMap.get(label));
			nodes.put(node);
		}
		
		Iterator<GraphEdge> ei = edgeSet.iterator();
		while (ei.hasNext()) {
			GraphEdge gEdge = ei.next();
			
			if(gEdge.getTarget() == gEdge.getSource()){
				edgeSet.remove(gEdge);
			}
		}

		ei = edgeSet.iterator();
		while (ei.hasNext()) {
			JSONObject edge = new JSONObject();
			GraphEdge gEdge = ei.next();
			edge.put("id", gEdge.getId());
			edge.put("target", gEdge.getTarget());
			edge.put("source", gEdge.getSource());
			edges.put(edge);
		}

		result.put("nodes", nodes);
		result.put("edges", edges);
		return result;
	}

	public static void main(String[] ar) throws Exception {

		filebr = new BufferedReader(new InputStreamReader(new FileInputStream("data/이은재.srdf"), "UTF8"));
		filebw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("data/이은재.graph"), "UTF8"));

		GraphConvertor gc = new GraphConvertor();
		String jsonFormatGraph = gc.convert().toString();
		String webFormatGraph = jsonFormatGraph.replace("\"nodes\"", "nodes");
		webFormatGraph = webFormatGraph.replace("\"label\"", "label");
		webFormatGraph = webFormatGraph.replace("\"id\"", "id");
		webFormatGraph = webFormatGraph.replace("\"edges\"", "edges");
		webFormatGraph = webFormatGraph.replace("\"source\"", "source");
		webFormatGraph = webFormatGraph.replace("\"target\"", "target");
		filebw.write(webFormatGraph);
		filebw.close();

	}
}
