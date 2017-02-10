package edu.kaist.mrlab.srdf.data;

public class GraphEdge {

	private String target;
	private String source;
	private String id;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public GraphEdge(String target, String source){
		this.target = target;
		this.source = source;
		this.id = target + "to" + source;
	}

}
