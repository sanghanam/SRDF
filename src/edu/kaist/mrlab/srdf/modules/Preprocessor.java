package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.Collections;

import edu.kaist.mrlab.srdf.data.Chunk;

public class Preprocessor {

	public String changeSymbol(String input) {
		input = input.replace("“", "'");
		input = input.replace("”", "'");

		input = input.replace("\"", "'");
		input = input.replace("\"", "'");

		input = input.replace("《", "'");
		input = input.replace("》", "'");

		input = input.replace("‘", "'");
		input = input.replace("’", "'");

		input = input.replace("〈", "'");
		input = input.replace("〉", "'");

		// input = input.replace("을것이다", "을 것이다");
		// input = input.replace("오르가논입니다", "오르가논 입니다");

		return input;
	}

	public String removeBracket(String input) {

		String result = "";

		boolean deal = false;
		boolean openned = false;
		int val = 0;
		int open = -1;
		int close = -1;
		char[] inputArr = input.toCharArray();

		for (int i = 0; i < inputArr.length; i++) {

			if (inputArr[i] == '(') {
				if (openned) {

				} else {
					open = i;
					openned = true;
				}
				val++;
			} else if (inputArr[i] == ')') {
				close = i;
				val--;
				deal = false;
			}

			if (i != 0 && val == 0 && open != -1 && !deal) {

				deal = true;

				for (int j = open; j <= close; j++) {

					inputArr[j] = '-';

				}
				openned = false;
			}
		}

		for (int i = 0; i < inputArr.length; i++) {
			if (inputArr[i] != '-')
				result += inputArr[i];
		}

		return result;

	}

	public String attachSubject(String input) {

		return input;

	}

	public boolean passOrNot(String input) {

		if (input.contains("IP")) {
			return true;
		}
		return false;

	}

	public boolean isValidChunk(ArrayList<Chunker> chunkers) {

		ArrayList<Integer> IDs = new ArrayList<Integer>();
		ArrayList<Integer> mods = new ArrayList<Integer>();

		for (int i = 0; i < chunkers.size(); i++) {

			ArrayList<Chunk> npChunks = chunkers.get(i).getNPChunks();
			ArrayList<Chunk> vpChunks = chunkers.get(i).getVPChunks();

			for (int j = 0; j < npChunks.size(); j++) {

				IDs.add(npChunks.get(j).getID());

			}

			for (int j = 0; j < vpChunks.size(); j++) {

				IDs.add(vpChunks.get(j).getID());
				mods.addAll(vpChunks.get(j).getMod());
			}

		}

		Collections.sort(IDs);
		Collections.sort(mods);

		if (IDs.size() > 0) {

			if (IDs.containsAll(mods)) {
				return true;
			}

		}

		return false;
	}

	public boolean filterOut(String input) {

		if (input.contains(":") || input.contains("?")) {
			return true;
		}

		return false;

	}

	public static void main(String[] ar) {

		Preprocessor p = new Preprocessor();

		String input = "오디세우스(: Ὀδυσσεύς ; : Ulixes )는 그리스 신화상의 영웅으로, 이타카(Ithaca)의 영주, 트로이 전쟁의 영웅, 트로이 목마의 고안자이다.";
		System.out.println(p.removeBracket(input));
	}

}
