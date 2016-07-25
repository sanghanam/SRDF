package edu.kaist.mrlab.srdf.data;

import java.util.ArrayList;

public class Chunk {
	private String chunk;
	private String postposition;
	private int ID;
	private ArrayList<Integer> APID;
	private String label;
	private ArrayList<Integer> mod;

	public Chunk(String chunk, String postposition) {
		this.chunk = chunk;
		this.postposition = postposition;
	}

	public Chunk(String chunk, String postposition, int ID) {
		this.chunk = chunk;
		this.postposition = postposition;
		this.ID = ID;
	}

	public Chunk(String chunk, String postposition, int ID, String label) {
		this.chunk = chunk;
		this.postposition = postposition;
		this.ID = ID;
		this.label = label;
	}

	public Chunk(String chunk, String postposition, ArrayList<Integer> mod) {
		this.chunk = chunk;
		this.postposition = postposition;
		this.mod = mod;
	}

	public Chunk(String chunk, String postposition, int ID,
			ArrayList<Integer> mod) {
		this.chunk = chunk;
		this.postposition = postposition;
		this.ID = ID;
		this.mod = mod;
	}

	public Chunk(String chunk, String postposition, int ID, String label,
			ArrayList<Integer> mod) {
		this.chunk = chunk;
		this.postposition = postposition;
		this.ID = ID;
		this.label = label;
		this.mod = mod;
	}

	public String getChunk() {
		return chunk;
	}

	public void setChunk(String chunk) {
		this.chunk = chunk;
	}

	public String getPostposition() {
		return postposition;
	}

	public void setPostposition(String postposition) {
		this.postposition = postposition;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<Integer> getMod() {
		return mod;
	}

	public void setMod(ArrayList<Integer> mod) {
		this.mod = mod;
	}

	public ArrayList<Integer> getAPID() {
		return APID;
	}

	public void setAPID(ArrayList<Integer> aPID) {
		this.APID = aPID;
	}

	public String print() {
		String result = chunk + " / " + postposition;
		return result;
	}

}
