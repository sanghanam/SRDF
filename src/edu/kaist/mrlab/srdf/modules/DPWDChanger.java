package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;

public class DPWDChanger {

	JSONArray morpArr;
	JSONArray depenArr;
	JSONArray wordArr;

	int firstSBJID;

	@SuppressWarnings({ "unused", "unchecked" })
	public String change(String input) {

		String output = "";

		try {

			JSONParser jsonParser = new JSONParser();
			// JSONObject reader = (JSONObject) jsonParser.parse(new FileReader(
			// "data\\corextractor\\test11.json"));

			JSONObject reader = (JSONObject) jsonParser.parse(input);

			JSONArray stn = (JSONArray) reader.get("sentence");

			Iterator<?> s = stn.iterator();

			while (s.hasNext()) {

				JSONObject text = (JSONObject) s.next();
				JSONObject morp = (JSONObject) s.next();
				JSONObject depen = (JSONObject) s.next();
				JSONObject word = (JSONObject) s.next();

				morpArr = (JSONArray) morp.get("morp");
				depenArr = (JSONArray) depen.get("dependency");
				wordArr = (JSONArray) word.get("word");

				/**
				 * 
				 * 
				 * 
				 */

				JSONObject lastDepen = (JSONObject) depenArr.get(depenArr.size() - 1);
				String lastDepenLabel = (String) lastDepen.get("label");
				if (lastDepenLabel.startsWith("NP") || lastDepenLabel.contains("MOD") || lastDepenLabel.startsWith("AP")
						|| lastDepenLabel.startsWith("DP")|| lastDepenLabel.startsWith("X")) {

					return input;

				}

				/**
				 * 
				 */

				Iterator<?> dt = depenArr.iterator();
				boolean stop = true;
				while (dt.hasNext()) {
					JSONObject innerDepen = (JSONObject) dt.next();
					int depenID = (int) (long) innerDepen.get("id");
					String depenLabel = (String) innerDepen.get("label");
					if (depenLabel.equals("NP_SBJ") && stop) {
						firstSBJID = depenID;
						stop = false;
					}
				}

				ListIterator<?> d = depenArr.listIterator(depenArr.size());

				if (d.hasPrevious()) {
					JSONObject innerDepen = (JSONObject) d.previous();
					String depenText = (String) innerDepen.get("text");
					if (depenText.contains("반면") || depenText.contains("일변")) {

						innerDepen.replace("label", "VP");

					}
				}

				d = depenArr.listIterator();

				while (d.hasNext()) {

					JSONObject innerDepen = (JSONObject) d.next();
					int depenID = (int) (long) innerDepen.get("id");
					int depenHead = (int) (long) innerDepen.get("head");
					String depenText = (String) innerDepen.get("text");
					String depenLabel = (String) innerDepen.get("label");
					JSONArray depenMod = (JSONArray) innerDepen.get("mod");
					ArrayList<Integer> depenModArr = new ArrayList<Integer>();
					Iterator<?> dm = depenMod.iterator();
					while (dm.hasNext()) {
						depenModArr.add((int) (long) dm.next());
					}

					// if(depenText.contains("한다는")){
					// System.out.println();
					// }

					if (depenLabel.contains("VP") || depenLabel.contains("VNP") || depenText.equals("수")) {

						// if (depenLabel.contains("VNP")) {
						// System.out.println(depenText);
						// }

						Iterator<?> w = wordArr.iterator();

						// System.out.println(depenID + "\t" + depenText + "\t"
						// + depenLabel);

						for (int i = 0; i < depenID; i++) {
							w.next();
						}

						JSONObject innerWord = (JSONObject) w.next();
						String wordText = (String) innerWord.get("text");
						long wordID = (long) innerWord.get("id");
						long wordBegin = (long) innerWord.get("begin");
						long wordEnd = (long) innerWord.get("end");

						// System.out.println(wordID + "\t" + wordText + "\t"
						// + wordBegin + "\t" + wordEnd);

						Iterator<?> m = morpArr.iterator();

						for (int j = 0; j < wordBegin; j++) {
							m.next();
						}

						String prevMorpType = null;

						for (int k = 0; k <= wordEnd - wordBegin; k++) {

							JSONObject innerMorp = (JSONObject) m.next();
							long morpID = (long) innerMorp.get("id");

							String morpType = (String) innerMorp.get("type");
							String morpLemma = (String) innerMorp.get("lemma");

							Iterator<?> pm = morpArr.iterator();

							for (int i = 0; i < (int) morpID - 1; i++) {
								pm.next();
							}
							JSONObject prevMorp = (JSONObject) pm.next();
							prevMorpType = (String) prevMorp.get("type");

							if ((wordEnd - wordBegin) == 0) {
								if (morpType.equals("NNG")) {
									innerDepen.replace("label", depenLabel, "NP");
								}
							}
							// || morpType.equals("ETN")
							// || (!prevMorpType.equals("XSV") &&
							// morpType.equals("EC") && morpLemma.equals("게"))
							// 없었을것이다.
							if ((morpType.equals("ETM") && depenHead != -1)
									|| (morpType.equals("MM") && depenHead != -1)
									|| (depenLabel.equals("VNP_CMP")
											&& (morpType.equals("EC") || morpType.equals("JKQ")))
									|| (morpLemma.equals("수") && depenLabel.equals("NP_SBJ")
											&& prevMorpType.equals("ETM"))) {
								// System.out.println(depenID + "\t" + depenText
								// + "\t" + depenLabel + "\t" + wordBegin
								// + "\t" + wordEnd);

								innerDepen.replace("label", depenLabel, "NP");

								// VP가 NP로 바뀐 경우에는 해당 VP가 받고 있던 화살표 중 VP만을 본인이
								// 화살표를 주고 있던 가장 가까운 VP로 넘겨줘야 한다.

								if (depenModArr.size() > 0) {

									for (int i = 0; i < depenModArr.size(); i++) {
										Iterator<?> dma = depenArr.iterator();
										for (int j = 0; j < depenModArr.get(i); j++) {
											dma.next();
										}

										JSONObject VP2NP = (JSONObject) dma.next();
										int dmaID = (int) (long) VP2NP.get("id");
										String dmaLabel = (String) VP2NP.get("label");
										if (dmaLabel.contains("VP")) {

											int newHead = getNewHead(depenHead);
											VP2NP.replace("head", newHead);
											changeModOfNewHead(newHead, dmaID);
											depenMod.remove((Object) (long) dmaID);

										} else if (dmaLabel.contains("NP_SBJ") && dmaID == firstSBJID) {

											int newHead = getNewHead(depenHead);
											VP2NP.replace("head", newHead);
											changeModOfNewHead(newHead, dmaID);
											depenMod.remove((Object) (long) dmaID);

										}
									}
								}
							}
						}
					}
				}

				// 문장의 마지막 부분에 위치한 VNP 혹은 VP 중 VCP를 포함한 것은 NP와 VP로 나누어 주는 모듈을 개발

				if (depenArr.size() > 1) {
					lastDepen = (JSONObject) depenArr.get(depenArr.size() - 1);
					JSONObject lastSecDepen = (JSONObject) depenArr.get(depenArr.size() - 2);

					long lastDepenHead = (long) lastDepen.get("head");
					JSONArray lastDepenMod = (JSONArray) lastDepen.get("mod");
					long lastDepenID = (long) lastDepen.get("id");
					String lastDepenText = (String) lastDepen.get("text");
					lastDepenLabel = (String) lastDepen.get("label");

					// 뒤에서 두 번째 Word의 정보

					long lastSecDepenHead = (long) lastSecDepen.get("head");
					JSONArray lastSecDepenMod = (JSONArray) lastSecDepen.get("mod");
					long lastSecDepenID = (long) lastSecDepen.get("id");
					String lastSecDepenText = (String) lastSecDepen.get("text");
					String lastSecDepenLabel = (String) lastSecDepen.get("label");

					if (lastSecDepenLabel.contains("VNP")
							|| lastSecDepenLabel.contains("VP") && lastDepenLabel.contains("VNP")
							|| lastDepenLabel.contains("VP")) {
						// System.out.println(lastSecDepenText);

						splitVNP(lastSecDepen, lastSecDepenHead, lastSecDepenMod, lastSecDepenID, lastSecDepenText,
								lastSecDepenLabel, 2);
					}

					if (lastDepenLabel.contains("VNP") || lastDepenLabel.contains("VP")) {

						splitVNP(lastDepen, lastDepenHead, lastDepenMod, lastDepenID, lastDepenText, lastDepenLabel, 1);

					}

					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					output = gson.toJson(reader);
					// System.out.println(output);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	public void splitVNP(JSONObject targetDepen, long targetDepenHead, JSONArray targetDepenMod, long targetDepenID,
			String targetDepenText, String targetDepenLabel, int index) {

		boolean splitON = false;

		JSONObject targetWord = (JSONObject) wordArr.get(wordArr.size() - index);

		String targetWordText = (String) targetWord.get("text");
		long targetWordID = (long) targetWord.get("id");
		long targetWordBegin = (long) targetWord.get("begin");
		long targetWordEnd = (long) targetWord.get("end");

		Iterator<?> m = morpArr.iterator();

		for (int j = 0; j < targetWordBegin; j++) {
			m.next();
		}

		// 분할 문제
		// NP와 VP를 나누는 기준은 일단 word를 중간 다리로 놓고 morp list를 가져온다.
		// morp list에서 lemma와 type을 보고 "VCP(이)+EF(다)+SF(.)" 혹은
		// "EF(다)+SF(.)" 를 VP로 취급
		// 앞부분에서 남는 모든 것을 NP로 취급

		String NP = "";
		String VP = "";

		boolean VPFlag = false;

		int wordEndCount = 0;

		ArrayList<String> morpTypeArr = new ArrayList<String>();
		for (int k = 0; k <= targetWordEnd - targetWordBegin; k++) {
			JSONObject innerMorp = (JSONObject) m.next();
			String morpType = (String) innerMorp.get("type");
			morpTypeArr.add(morpType);
		}

		if (morpTypeArr.contains("VCP")) {
			m = morpArr.iterator();

			for (int j = 0; j < targetWordBegin; j++) {
				m.next();
			}
			for (int k = 0; k <= targetWordEnd - targetWordBegin; k++) {
				JSONObject innerMorp = (JSONObject) m.next();
//				long morpID = (long) innerMorp.get("id");
				String morpType = (String) innerMorp.get("type");
				String morpLemma = (String) innerMorp.get("lemma");

				if (morpType.equals("VCP") || morpType.equals("EF")) {

					VPFlag = true;

				} else {
					if (!VPFlag) {
						wordEndCount++;
					}

				}

				if (VPFlag) {
					VP += morpLemma;
				} else {
					splitON = true;
					NP += morpLemma;
				}
			}

			// 링크 문제
			// NP로 취급된 부분은 "id"만 동일
			// "head"는 id+1 부여
			// "text"는 NP로 취급된 부분의 morp.lemma 부여
			// "label"는 NP 부여
			// "mod"는 기존의 link를 따라간 다음 해당 dependency entity가 NP_SBJ인
			// 링크만
			// 제거

			if (!splitON) {
				return;
			}
			targetDepen.replace("head", targetDepenHead, targetDepenID + 1);
			targetDepen.replace("text", targetDepenText, NP);
			targetDepen.replace("label", targetDepenLabel, "NP");

			// JSONArray newDepenMod1 = new JSONArray();
			// if(index == 1){
			// targetDepen.replace("mod", newDepenMod1);
			// }

			int newLastWordBegin;
			targetWord.replace("text", targetWordText, NP);
			targetWord.replace("end", targetWordEnd, newLastWordBegin = (int) (targetWordBegin + wordEndCount - 1));

			ArrayList<Integer> removeID = new ArrayList<Integer>();
			Iterator<?> dm = targetDepenMod.iterator();

			int sbjID = -1;

			while (dm.hasNext()) {
				Iterator<?> da = depenArr.iterator();
				long id = (long) dm.next();
				for (int i = 0; i < id; i++) {
					da.next();
				}
				JSONObject tempDP = (JSONObject) da.next();
				String depenLabel = (String) tempDP.get("label");
				if (depenLabel.equals("NP_SBJ")) {
					removeID.add((int) id);
					sbjID = (int) id;
				} else if (depenLabel.equals("VP")) {
					removeID.add((int) id);
				}
			}

			JSONArray newDepenMod = new JSONArray();

			for (int i = 0; i < removeID.size(); i++) {
				targetDepenMod.remove((long) removeID.get(i));
				newDepenMod.add((long) removeID.get(i));
			}

			// System.out.println(NP + "\t" + VP);

			// VP로 취급된 부분은 "head"만 동일
			// "id"는 id+1 부여
			// "text"는 VP로 취급된 부분의 morp.lemma 부여
			// "label"는 VP 부여
			// "head"값을 -1로 변경
			// "mod"는 위에서 제거된 링크 번호 및 본인의 기존 id 추가 & 추가된 dependency
			// entity의

			if (sbjID != -1) {
				Iterator<?> tempD = depenArr.iterator();
				for (int i = 0; i < sbjID; i++) {
					tempD.next();
				}
				JSONObject SBJDepen = (JSONObject) tempD.next();

				// ////// &****************************
				// ///////////////

				SBJDepen.replace("head", targetDepenID + 1);
				// System.out.println(SBJDepen);
			}

			JSONObject newDepen = new JSONObject();

			newDepen.put("id", targetDepenID + 1);
			newDepen.put("text", VP);
			newDepen.put("label", "VP");
			if (index == 2) {
				newDepen.put("head", targetDepenHead + 1);
			} else if (index == 1) {
				newDepen.put("head", -1);
			}

			newDepenMod.add(targetDepenID);
			newDepen.put("mod", newDepenMod);

			JSONObject newTargetWord = new JSONObject();
			newTargetWord.put("end", targetWordEnd);
			newTargetWord.put("id", targetWordID + 1);
			newTargetWord.put("text", VP);
			newTargetWord.put("type", "");
			newTargetWord.put("begin", newLastWordBegin + 1);

			if (index == 2) {

				JSONObject lastTempDepen = (JSONObject) depenArr.remove(depenArr.size() - 1);
				lastTempDepen.replace("id", targetDepenHead + 1);
				JSONArray lastTempDepenMod = new JSONArray();
				lastTempDepenMod.add(targetDepenID + 1);
				lastTempDepen.replace("mod", lastTempDepenMod);
				depenArr.add(newDepen);
				depenArr.add(lastTempDepen);

				JSONObject lastTempWord = (JSONObject) wordArr.remove(wordArr.size() - 1);
				lastTempWord.replace("id", targetDepenHead + 1);
				wordArr.add(newTargetWord);
				wordArr.add(lastTempWord);

				JSONObject lastSECDepen = (JSONObject) depenArr.get(depenArr.size() - 2);
				lastSECDepen.replace("head", targetDepenHead + 2);

			} else if (index == 1) {
				depenArr.add(newDepen);
				wordArr.add(newTargetWord);
			}

		}
	}

	public int getNewHead(int oldHead) {

		Iterator<?> d = depenArr.iterator();
		for (int i = 0; i < oldHead; i++) {
			d.next();
		}
		JSONObject tempDepen = (JSONObject) d.next();

		int tempHead = Integer.parseInt(tempDepen.get("head").toString());
		int tempID = (int) (long) tempDepen.get("id");
		String tempLabel = (String) tempDepen.get("label");
//		String tempText = (String) tempDepen.get("text");

		// System.out.println("*getNewHead* " + tempText);
		// System.out.println("*getNewHead* " + tempHead);

		/*
		 * 기존 방식 --> 수정일 20160216
		 * 
		 * if (tempLabel.contains("VP") || tempLabel.contains("VNP")) { return
		 * tempID; } else { return getNewHead(tempHead); }
		 * 
		 */

		if (tempLabel.contains("VP") || tempLabel.contains("VNP")) {
			return tempID;
		} else {
			return getNewHead(tempHead);
		}
	}

	@SuppressWarnings("unchecked")
	public void changeModOfNewHead(int newMod, int addMod) {
		Iterator<?> d = depenArr.iterator();
		for (int i = 0; i < newMod; i++) {
			d.next();
		}
		JSONObject tempDepen = (JSONObject) d.next();
		JSONArray tempMod = (JSONArray) tempDepen.get("mod");
		tempMod.add((Object) (long) addMod);
	}

	public static void main(String[] ar) {
		CoreExtractor parser = new CoreExtractor();
		KoreanAnalyzer ex = new KoreanAnalyzer();
		DPWDChanger dtc = new DPWDChanger();
		try {
			String output1 = ex.getResult("2008년 6월 17일, 프로젝트가 진행된 지 15년째에 Wine의 첫 번째 안정 버전인 1.0이 출시됐다, .");
			String output2 = parser.parse(output1);
			// System.out.println(output2);

			String result = dtc.change(output2);
			System.out.println(result);
			// BufferedWriter filebw = new BufferedWriter(new
			// OutputStreamWriter(
			// new FileOutputStream("data\\dpwdchanger\\test12.json"),
			// "UTF8"));
			// filebw.write(result);
			// filebw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
