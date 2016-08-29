package edu.kaist.mrlab.srdf.pattern;

import java.util.ArrayList;

public class PatternGenerator {
	
	public void generate(ArrayList<String> patterns){
		
		
		
		
	}

	public static void main(String[] ar){
		
		PatternGenerator pg = new PatternGenerator();
		ArrayList<String> patterns = new ArrayList<String>();
		
		patterns.add("nvvv");
		patterns.add("nvvvv");
		patterns.add("nvv");
		patterns.add("nv");
		patterns.add("nvnvnnv");
		patterns.add("nvnvnnv");
		patterns.add("nnvnnv");
		patterns.add("nnvnnv");
		patterns.add("nnvnnv");
		patterns.add("nnvnnv");
		patterns.add("nnnvnnnv");
		patterns.add("nnnvvnnnv");
		patterns.add("nnvvnnnv");
		patterns.add("nvvvnnnv");
		patterns.add("nnnvnvnv");
		patterns.add("nnnvvvnv");
		patterns.add("nnnv");
		patterns.add("nnnnv");
		patterns.add("nnnv");
		patterns.add("nnv");		
		patterns.add("nnnv");
		patterns.add("nnnv");
		patterns.add("nnnv");
		
		pg.generate(patterns);
	}
	
}
