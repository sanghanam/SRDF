package edu.kaist.mrlab.srdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import edu.kaist.mrlab.srdf.data.Triple;
import edu.kaist.mrlab.srdf.modules.Chunker;
import edu.kaist.mrlab.srdf.modules.Preprocessor;
import edu.kaist.mrlab.srdf.modules.SentenceSplitter;
import edu.kaist.mrlab.srdf.modules.TripleGenerator;
import edu.kaist.mrlab.srdf.tools.Constants;
import edu.kaist.mrlab.srdf.tools.FolderInReader;

public class SRDF {

	private Scanner scan;
	private static BufferedReader filebr;
	private static BufferedWriter filebw;
	private static String filePath;

	private static int readedSTC = 0;
	private static int generatedTriples = 0;

	public String inputSentence() {

		String sentence;

		scan = new Scanner(System.in);

		System.out.println("문장을 입력하세요:");

		sentence = scan.nextLine();

		return sentence;
	}

	public static void writeTriples(ArrayList<Triple> triples, Chunker c) throws Exception {

		filebw.write("STC : " + c.getText().get("text") + "\n");
		System.out.println("STC : " + c.getText().get("text"));
		readedSTC++;

		for (int j = 0; j < triples.size(); j++) {
			Triple t = triples.get(j);
			filebw.write(t.getSubject() + "\t" + t.getPredicate() + "\t" + t.getObject() + "\n");
		}
		generatedTriples += triples.size();

		filebw.write("\n\n");

	}

	public String formatTime(long lTime) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(lTime);
		return (c.get(Calendar.HOUR_OF_DAY) + "시 " + c.get(Calendar.MINUTE) + "분 " + c.get(Calendar.SECOND) + "."
				+ c.get(Calendar.MILLISECOND) + "초");
	}

	public String doOneSentence(KoSeCT kosect, Preprocessor p, SentenceSplitter ss, String input) {
		String qTriples = "";
		try {

			ArrayList<String> splittedSTC = ss.splitSentence(input);

			for (int j = 0; j < splittedSTC.size(); j++) {

				// System.out.println(splittedSTC.get(j));

				input = kosect.changeSymbol(splittedSTC.get(j));
				input = kosect.removeUTF8BOM(input);
				input = p.removeBracket(input);

				ArrayList<Chunker> chunkers = kosect.doPreprocessWithoutSplitting(input);

				if (!p.isValidChunk(chunkers) || chunkers.isEmpty()) {
					continue;
				}

				for (Chunker c : chunkers) {
					TripleGenerator tg = new TripleGenerator(c.getNPChunks(), c.getVPChunks());
					tg.generate();
					ArrayList<Triple> triples = tg.getTriples();
					for (int k = 0; k < triples.size(); k++) {
						Triple t = triples.get(k);
						System.out.print(t.getSubject() + "\t" + t.getPredicate() + "\t" + t.getObject() + "\n");
						qTriples += t.getSubject() + "\t" + t.getPredicate() + "\t" + t.getObject() + "\n";
					}
				}
			}

		} catch (Exception e) {

		}

		return qTriples;
	}

	public void doSampleFile(KoSeCT kosect, Preprocessor p, SentenceSplitter ss) {
		try {

			filebr = new BufferedReader(new InputStreamReader(new FileInputStream("data/test/sample4.txt"), "UTF8"));

			String input = null;
			while ((input = filebr.readLine()) != null) {
				if (input.length() != 0) {
					// readedStc++;

					doOneSentence(kosect, p, ss, input);

				}
			}
			filebr.close();

		} catch (Exception e) {

		}
	}

	public void doFeaturedArticle(KoSeCT kosect, Preprocessor p, SentenceSplitter ss) {
		try {

			String sbj = "역사";
			filebr = new BufferedReader(new InputStreamReader(
					new FileInputStream("data\\gold_standard\\gold_standard_" + sbj + ".txt"), "UTF8"));
			filebw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("data\\gold_standard_triple\\gold_standard_" + sbj + "_triples.txt"), "UTF8"));

			String input = null;
			while ((input = filebr.readLine()) != null) {
				if (input.length() != 0) {
					readedSTC++;

					doOneSentence(kosect, p, ss, input);

				}
			}
			filebr.close();
			filebw.close();

		} catch (Exception e) {

		}
	}

	public void doWikiDump(KoSeCT kosect, Preprocessor p, SentenceSplitter ss) {

		try {

			File fl = new File(Constants.wikiPathInput);
			FolderInReader fir = new FolderInReader();
			ArrayList<String> arrFS = fir.RECURSIVE_FILE(fl);
			for (int i = 0; i < arrFS.size(); i++) {

				filePath = arrFS.get(i);

				String fileName = null;
				StringTokenizer st = new StringTokenizer(filePath, "\\");
				int size = st.countTokens();
				for (int k = 0; k < size; k++) {
					fileName = st.nextToken();
				}

				System.out.println(filePath);
				if (!filePath.contains(".txt")) {
					continue;
				}
				filebr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
				filebw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(Constants.wikiPathOutput + fileName), "UTF8"));

				String input = null;
				while ((input = filebr.readLine()) != null) {
					if (input.length() != 0 && !p.filterOut(input)) {
						// readedStc++;

						doOneSentence(kosect, p, ss, input);

					}
				}
				filebr.close();
				filebw.close();
				File f = new File(filePath);
				f.delete();

				// System.out.println("readed sentence : " + readedStc);
				// System.out.println("generated triples : " +
				// generatedTriples);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(filePath);
		}

	}

	public static void main(String[] ar) throws NoSuchElementException {

		long startTime = System.currentTimeMillis();

		KoSeCT kosect = new KoSeCT();
		Preprocessor p = new Preprocessor();
		SentenceSplitter ss = new SentenceSplitter();
		SRDF srdf = new SRDF();

		String input = srdf.inputSentence();
		srdf.doOneSentence(kosect, p, ss, input);
		// srdf.doWikiDump(kosect, p, ss);
		// srdf.doFeaturedArticle(kosect, p, ss);
		// srdf.doSampleFile(kosect, p, ss);

		// 종료 시간
		long endTime = System.currentTimeMillis();
		// 시간 출력
		System.out.println("##  시작시간 : " + new SRDF().formatTime(startTime));
		System.out.println("##  종료시간 : " + new SRDF().formatTime(endTime));
		System.out.println("##  소요시간(초.0f) : " + (endTime - startTime) / 1000.0f + "초");

		System.out.println("## 입력 문장 수 : " + readedSTC);
		System.out.println("## 출력 트리플 수 : " + generatedTriples);

	}
}
