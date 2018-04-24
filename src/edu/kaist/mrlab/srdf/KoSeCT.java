package edu.kaist.mrlab.srdf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import edu.kaist.mrlab.srdf.data.Chunk;
import edu.kaist.mrlab.srdf.data.Globals;
import edu.kaist.mrlab.srdf.data.Sentence;
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
	Preprocessor p = new Preprocessor();

	protected static int seperatedSentence = 0;
	
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

					seperatedSentence++;

				}

				chunkers.add(chunker);

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

			chunker.printChunks2Console();
			chunkers.add(chunker);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chunkers;
	}


	protected String removeUTF8BOM(String s) {
		if (s.startsWith(Globals.UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
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

			filebr = new BufferedReader(new InputStreamReader(new FileInputStream("data/test/sample4.txt"), "UTF8"));
			String input = null;
			while ((input = filebr.readLine()) != null) {
				if (input.length() != 0) {
					input = p.changeSymbol(input);
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
