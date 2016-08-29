package edu.kaist.mrlab.srdf.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kaist.mrlab.srdf.data.Chunk;
import edu.kaist.mrlab.srdf.modules.Chunker;

public class TypicalPattern {
	
	public boolean isMatchedType1(String input) {
		boolean result = false;

		Pattern p = Pattern.compile("nv+");
		Matcher m = p.matcher(input);
		result = m.matches();

		return result;
	}

	public boolean isMatchedType2(String input) {
		boolean result = false;

		Pattern p = Pattern.compile("n+v");
		Matcher m = p.matcher(input);
		result = m.matches();

		return result;
	}

	public boolean isMatchedType3(String input) {
		boolean result = false;

		Pattern p = Pattern.compile("n+v+");
		Matcher m = p.matcher(input);
		result = m.matches();

		return result;
	}

	public boolean isMatchedType4(String input) {
		boolean result = false;

		Pattern p = Pattern.compile("n+v+n+v+(n+v+)*");
		Matcher m = p.matcher(input);
		result = m.matches();

		return result;
	}

	protected String getPattern(Chunker c) {
		String result = "";

		ArrayList<Chunk> vpList = c.getVPChunks();
		ArrayList<Chunk> npList = c.getNPChunks();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; i < vpList.size(); i++) {
			idList.add(vpList.get(i).getID());
			idList.addAll(vpList.get(i).getMod());

			// for(int j = 0; j < mod.size(); j++){
			// if(id < mod.get(j)){
			// System.out.println("UNEXPECTED HEADING !!");
			// }
			// }

		}

		Set<Integer> idSet = new HashSet<>(idList);
		ArrayList<Integer> uniqueIdList = new ArrayList<Integer>(idSet);
		Collections.sort(uniqueIdList);

		for (int i = 0; i < uniqueIdList.size(); i++) {

			int tmp = uniqueIdList.get(i);

			for (int j = 0; j < npList.size(); j++) {

				if (npList.get(j).getID() == tmp) {
					result += "n";
				}

			}

			for (int k = 0; k < vpList.size(); k++) {

				if (vpList.get(k).getID() == tmp) {
					result += "v";
				}

			}
		}

		return result;
	}
}
