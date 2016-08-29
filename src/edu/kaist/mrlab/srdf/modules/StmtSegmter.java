package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.kaist.mrlab.srdf.data.Sentence;
import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;

public class StmtSegmter {

	JSONArray morpArr = null;
	JSONArray depenArr = null;
	JSONArray wordArr = null;

	public ArrayList<Integer> findSegPoint(String sentence) {
		ArrayList<Integer> segPoint = new ArrayList<Integer>();
		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject reader = (JSONObject) jsonParser.parse(sentence);

			JSONArray stn = (JSONArray) reader.get("sentence");

			Iterator<?> s = stn.iterator();

			while (s.hasNext()) {

				s.next();
				JSONObject morp = (JSONObject) s.next();
				JSONObject depen = (JSONObject) s.next();
				JSONObject word = (JSONObject) s.next();

				morpArr = (JSONArray) morp.get("morp");
				depenArr = (JSONArray) depen.get("dependency");
				wordArr = (JSONArray) word.get("word");

				// morp 연결 어미 --> EC
				// 연결 어미를 포함한 DP --> VP
				// VP에 연결어미 EC를 포함한 word 중 lemma가 "고, (으)며, (으)나, 지마는, 라도" 이러한
				// 것들을 기준으로 separate

				// 알고리즘 개요
				// 나뉘어져야 할 포인트 선정 (DP나 Word의 ID 값)
				// 해당 포인트를 포함한 문장의 앞부분을 단순히 잘라내기
				// 해당 포인트를 제외한 문장의 뒷부분을 단순히 잘라내기
				// 잘린 뒷부분에서 NP_SBJ가 있는지 확인
				// 있다면 pass, 없다면 잘린 앞부분의 NP_SBJ를 그대로 가져와서 잘린 뒷부분의 맨 앞에 부착
				// 부착과정에서 잘린 뒷부분의 DP들을 모두 수정할 필요

				// separate 지점 찾기

				Iterator<?> d = depenArr.iterator();

				while (d.hasNext()) {

					JSONObject innerDepen = (JSONObject) d.next();
					int depenID = Integer.parseInt(innerDepen.get("id").toString());
					// String depenText = (String) innerDepen.get("text");
					String depenLabel = (String) innerDepen.get("label");

					boolean NPflag = false;
					if (depenLabel.contains("VP") || (NPflag = depenLabel.contains("NP"))) {

						Iterator<?> w = wordArr.iterator();

						// System.out.println(depenID + "\t" + depenText + "\t"
						// + depenLabel);

						for (int i = 0; i < depenID; i++) {
							w.next();
						}

						JSONObject innerWord = (JSONObject) w.next();
						// String wordText = (String) innerWord.get("text");
						// long wordID = (long) innerWord.get("id");
						int wordBegin = Integer.parseInt(innerWord.get("begin").toString());
						int wordEnd = Integer.parseInt(innerWord.get("end").toString());

						// System.out.println(wordID + "\t" + wordText + "\t"
						// + wordBegin + "\t" + wordEnd);

						Iterator<?> m = morpArr.iterator();

						for (int j = 0; j < wordBegin; j++) {
							m.next();
						}

						boolean segPointFlag = false;

						for (int k = 0; k <= wordEnd - wordBegin; k++) {
							JSONObject innerMorp = (JSONObject) m.next();
							// long morpID = (long) innerMorp.get("id");
							String morpType = (String) innerMorp.get("type");
							String morpLemma = (String) innerMorp.get("lemma");

							if (morpType.equals("EC") || (NPflag && morpType.equals("NNG"))) {

								if (morpLemma.equals("고") || morpLemma.equals("며") || morpLemma.equals("으며")
										|| morpLemma.equals("거나") || morpLemma.equals("나") || morpLemma.equals("으나")
										|| morpLemma.equals("지만") || morpLemma.equals("지마는") || morpLemma.equals("는데")
										|| morpLemma.equals("ㄴ데") || morpLemma.equals("데") || morpLemma.equals("아도")
										|| morpLemma.equals("어도") || morpLemma.equals("라도") || morpLemma.equals("면서")
										|| (NPflag && morpLemma.equals("반면")) || (NPflag && morpLemma.equals("일변"))) {

									// System.out.println(morpLemma);

									if ((depenArr.size() - 1 - depenID) >= 2) {

										if (segPoint.size() == 0) {
											segPointFlag = true;
										} else {
											for (int i = 0; i < segPoint.size(); i++) {
												if ((depenID - segPoint.get(i)) > 3) {
													segPointFlag = true;
												} else {
													segPoint.remove(segPoint.size() - 1);

													if (!segPoint.contains((int) depenID)) {
														segPoint.add((int) depenID);
													}
												}
											}
										}

										if (segPointFlag) {
											if (!segPoint.contains((int) depenID)) {
												segPoint.add((int) depenID);
											}

										}
									}
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		segPoint.add(depenArr.size() - 1);
		return segPoint;
	}

	public ArrayList<Sentence> getSegmentedSentence(ArrayList<Integer> segPoint) {
		ArrayList<Sentence> sentenceArr = new ArrayList<Sentence>();
		// System.out.println("seg point: " + segPoint);
		String segmentedSentence = "";
		int k = 0;
		int j = 0;
		boolean isSBJ = false;
		String subject = "";
		for (int i = 0; i < segPoint.size(); i++) {
			for (j = k; j <= segPoint.get(i); j++) {

				JSONObject innerDepen = (JSONObject) depenArr.get(j);

				String depenText = (String) innerDepen.get("text");
				String depenLabel = (String) innerDepen.get("label");

				if (depenLabel.contains("SBJ") && !depenText.equals("수")) {
					isSBJ = true;
					subject = depenText;
				}

				segmentedSentence += depenText + " ";
			}
			sentenceArr.add(new Sentence(segmentedSentence, isSBJ, subject));
			subject = "";
			isSBJ = false;
			segmentedSentence = "";
			k = j;
		}

		return sentenceArr;

	}

	public Sentence attachSubject(Sentence input) {
		Sentence output = null;

		return output;
	}

	public static void main(String[] ar) {
		CoreExtractor parser = new CoreExtractor();
		KoreanAnalyzer ex = new KoreanAnalyzer();
		StmtSegmter ss = new StmtSegmter();
		try {
			String output1 = ex
					.getResult("특히 유럽인들이 세계 각지로 진출한 이후 감자, 토마토, 후추, 옥수수 등의 다양한 식재료가 유입되면서 이탈리아 요리는 큰 변화를 겪었다.");

			String result = parser.parse(output1);

			ArrayList<Integer> segPoint = ss.findSegPoint(result);
			ArrayList<Sentence> segSentence = ss.getSegmentedSentence(segPoint);
			for (int i = 0; i < segSentence.size(); i++) {
				System.out.println(segSentence.get(i).print());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
