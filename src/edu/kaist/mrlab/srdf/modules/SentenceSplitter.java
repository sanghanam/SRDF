package edu.kaist.mrlab.srdf.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;

public class SentenceSplitter {

	String sbj = "다큐멘터리 Live / Play: 살며, 게임하며는 ";
	KoreanAnalyzer ka = new KoreanAnalyzer();


	public ArrayList<String> splitSentence(String input) {

		ArrayList<String> results = new ArrayList<String>();

		try {

			String resultOfKA = ka.getResult(input);

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(resultOfKA);

			JSONArray sentArr = (JSONArray) jsonObject.get("sentence");

			Iterator<?> s = sentArr.iterator();

			while (s.hasNext()) {
				
				boolean contSBJ = false;

				JSONObject innerOBJ = (JSONObject) s.next();
				String text = (String) innerOBJ.get("text");

				if(text.contains("[") && text.contains("]")){
					text = text.substring(0, text.indexOf("[")) + text.substring(text.indexOf("]"), text.length());
				}

				JSONArray dependency = (JSONArray) innerOBJ.get("dependency");
				Iterator<?> di = dependency.iterator();
				while (di.hasNext()) {
					JSONObject depen = (JSONObject) di.next();
					String label = depen.get("label").toString();
					if (label.equals("NP_SBJ")) {
						
						contSBJ = true;
						
					}

				}
				
				if(!contSBJ){
					text = sbj + text;
				}

				results.add(text.trim());

			}

		} catch (Exception e) {

		}

		return results;
	}

	public static void main(String[] ar) throws Exception {

		SentenceSplitter ss = new SentenceSplitter();

		BufferedReader filebr = new BufferedReader(new InputStreamReader(new FileInputStream("data/리그 오브 레전드.txt"), "UTF8"));
		BufferedWriter filebw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("data/리그 오브 레전드_spt.txt"), "UTF8"));

		String input;
		while ((input = filebr.readLine()) != null) {
			ArrayList<String> results = ss.splitSentence(input);
			for (int i = 0; i < results.size(); i++) {
				filebw.write(results.get(i) + "\n");
			}
		}
		filebr.close();
		filebw.close();

	}
}
