package edu.kaist.mrlab.srdf.data;

public class Entity {

	private String text;
	private String uri;
	private int start;
	private int end;
	
	public Entity(){
		
	}
	
	public Entity(String text, String uri, int start, int end){
		this.text = text;
		this.uri = uri;
		this.start = start;
		this.end = end;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

}
