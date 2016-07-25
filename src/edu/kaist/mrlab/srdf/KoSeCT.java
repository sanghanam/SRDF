package edu.kaist.mrlab.srdf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import edu.kaist.mrlab.srdf.data.Chunk;
import edu.kaist.mrlab.srdf.data.Sentence;
import edu.kaist.mrlab.srdf.data.TypicalPattern;
import edu.kaist.mrlab.srdf.modules.Chunker;
import edu.kaist.mrlab.srdf.modules.CoreExtractor;
import edu.kaist.mrlab.srdf.modules.DPWDChanger;
import edu.kaist.mrlab.srdf.modules.Preprocessor;
import edu.kaist.mrlab.srdf.modules.StmtSegmter;
import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;

public class KoSeCT {

	private static BufferedReader filebr;
	// private static BufferedWriter filebw;

	CoreExtractor parser = new CoreExtractor();
	KoreanAnalyzer ex = new KoreanAnalyzer();
	DPWDChanger dtc = new DPWDChanger();
	StmtSegmter ss = new StmtSegmter();
	Chunker chunker = null;
	TypicalPattern tp = new TypicalPattern();
	Preprocessor p = new Preprocessor();

	protected static int seperatedSentence = 0;
	private static final String UTF8_BOM = "\uFEFF";
	private Scanner scan;

	static int p1 = 0;
	static int p2 = 0;
	static int p3 = 0;
	static int p4 = 0;

	public String inputSentence() {

		String sentence;

		scan = new Scanner(System.in);

		System.out.println("문장을 입력하세요:");

		sentence = scan.nextLine();

		return sentence;
	}

	public ArrayList<Chunker> doPreprocessWithSplitting(String input) {
		ArrayList<Chunker> chunkers = new ArrayList<Chunker>();

		try {
			String prevSBJ = "";
			String output1 = ex.getResult(input);
			String output2 = parser.parse(output1);

			ArrayList<Integer> segPoint = ss.findSegPoint(output2);
			ArrayList<Sentence> segSentence = ss.getSegmentedSentence(segPoint);
			for (int i = 0; i < segSentence.size(); i++) {

				Sentence tempSTC = segSentence.get(i);

				chunker = new Chunker();

				if (tempSTC.isContainsSBJ()) {
					String text = tempSTC.getTextOfSentence();
					// String text = input;
					String output3 = ex.getResult(text);
					
					if(p.passOrNot(output3)){
						return chunkers;
					}
					
					String output4 = parser.parse(output3);
					String result = dtc.change(output4);

					chunker.chunk(result);

					// chunker.printChunks2File(filebw);
//					chunker.printSTC2Console();

					// String pattern = getPattern(chunker);
					// System.out.println(pattern);
					//
					// if (tp.isMatchedType1(pattern)) {
					// p1++;
					// } else if (tp.isMatchedType2(pattern)) {
					// p2++;
					// } else if (tp.isMatchedType3(pattern)) {
					// p3++;
					// } else if (tp.isMatchedType4(pattern)) {
					// p4++;
					// }

					seperatedSentence++;

					boolean SBJflag = false;

					ArrayList<Chunk> npChunks = chunker.getNPChunks();
					for (int j = 0; j < npChunks.size(); j++) {
						if (npChunks.get(j).getLabel().contains("SBJ") && !SBJflag) {
							prevSBJ = npChunks.get(j).getChunk() + npChunks.get(j).getPostposition();
							SBJflag = true;
						}
					}

				} else {

					String beAttachedSBJ = prevSBJ + " " + tempSTC.getTextOfSentence();

					String output3 = ex.getResult(beAttachedSBJ);
					
					if(p.passOrNot(output3)){
						return chunkers;
					}
					
					
					
					String output4 = parser.parse(output3);
					String result = dtc.change(output4);

					if(result.length() == 0){
						
						continue;
					}
					
					chunker.chunk(result);

//					chunker.printSTC2Console();
					seperatedSentence++;

				}

				chunkers.add(chunker);

				// for(int n = 0; n < chunker.getNPChunks().size(); n++){
				// nv.add(chunker.getNPChunks().get(n));
				// }
				//
				// for(int v = 0; v < chunker.getVPChunks().size(); v++){
				// nv.add(chunker.getVPChunks().get(v));
				// }
				//
				// for(int n = 0; n < nv.size(); n++){
				// System.out.println(nv.get(n).getID());
				// }

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chunkers;
	}

	public ArrayList<Chunker> doPreprocessWithoutSplitting(String input) {
		ArrayList<Chunker> chunkers = new ArrayList<Chunker>();

		try {
			chunker = new Chunker();

			String text = input;
			String output3 = ex.getResult(text);
			
			if(p.passOrNot(output3)){
				return chunkers;
			}
			
			String output4 = parser.parse(output3);
			String result = dtc.change(output4);

			chunker.chunk(result);

//			chunker.printChunks2Console();
			chunkers.add(chunker);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chunkers;
	}

	protected String getPattern(Chunker c) {
		String result = "";

		ArrayList<Chunk> vpTmp = c.getVPChunks();
		ArrayList<Chunk> npTmp = c.getNPChunks();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for (int i = 0; i < vpTmp.size(); i++) {
			idList.add(vpTmp.get(i).getID());
			idList.addAll(vpTmp.get(i).getMod());

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

			for (int j = 0; j < npTmp.size(); j++) {

				if (npTmp.get(j).getID() == tmp) {
					result += "n";
				}

			}

			for (int k = 0; k < vpTmp.size(); k++) {

				if (vpTmp.get(k).getID() == tmp) {
					result += "v";
				}

			}
		}

		return result;
	}

	protected String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}

	protected String changeSymbol(String input) {
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
		
//		input = input.replace("을것이다", "을 것이다");
//		input = input.replace("오르가논입니다", "오르가논 입니다");
		
		return input;
	}
	
	public static void main(String[] ar) {

		KoSeCT kosect = new KoSeCT();
		Preprocessor p = new Preprocessor();

		try {

			// String sentence = kosect.inputSentence();
			// sentence = kosect.changeSymbol(sentence);
			// sentence = kosect.removeUTF8BOM(sentence);
			// kosect.doPreprocess(sentence);
			//
			// System.out.println("\n\n number of sentences : " + sentence);
			// System.out.println(" number of seperated sentences: " +
			// seperatedSentence);

			// String sbj = "물리천문학";
			//
			// filebr = new BufferedReader(new InputStreamReader(
			// new FileInputStream("data\\gold_standard\\gold_standard_" + sbj +
			// ".txt"), "UTF8"));

			filebr = new BufferedReader(new InputStreamReader(new FileInputStream("data\\test\\sample4.txt"), "UTF8"));
			String input = null;
			while ((input = filebr.readLine()) != null) {
				if (input.length() != 0) {
					input = kosect.changeSymbol(input);
					input = kosect.removeUTF8BOM(input);
					input = p.removeBracket(input);
					// System.out.println("============= " + sentence + "
					// =============");
					// System.out.println("original sentence: " + input);
					kosect.doPreprocessWithoutSplitting(input);

				}

			}

			filebr.close();
			// System.out.println(p1 + "\t" + p2 + "\t" + p3 + "\t" + p4);
			// System.out.println("\n\n number of sentences : " + sentence);
			// System.out.println(" number of seperated sentences: " +
			// seperatedSentence);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
