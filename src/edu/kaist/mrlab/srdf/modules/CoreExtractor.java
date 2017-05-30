package edu.kaist.mrlab.srdf.modules;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;
import edu.kaist.mrlab.srdf.tools.KoreanAnalyzerREST;

public class CoreExtractor {
	
	@SuppressWarnings("unchecked")
	public String parse(String resultOfKA) {
		String resultOfCE = null;
		try {

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(resultOfKA);

			JSONArray sentArr = (JSONArray) jsonObject.get("sentence");

			Iterator<?> s = sentArr.iterator();
			
			JSONArray tempOUT = new JSONArray();
			JSONObject output = new JSONObject();

			while (s.hasNext()) {

				JSONObject innerOBJ = (JSONObject) s.next();
				String text = (String) innerOBJ.get("text");
				JSONArray morp = (JSONArray) innerOBJ.get("morp");
				JSONArray dependency = (JSONArray) innerOBJ.get("dependency");
				JSONArray word = (JSONArray) innerOBJ.get("word");
				JSONArray wsd = (JSONArray) innerOBJ.get("WSD");
				
				JSONObject textOBJ = new JSONObject();
				JSONObject morpOBJ = new JSONObject();
				JSONObject dependencyOBJ = new JSONObject();
				JSONObject wordOBJ = new JSONObject();
				JSONObject wsdOBJ = new JSONObject();
				textOBJ.put("text", text);
				tempOUT.add(textOBJ);
				morpOBJ.put("morp", morp);
				tempOUT.add(morpOBJ);
				dependencyOBJ.put("dependency", dependency);
				tempOUT.add(dependencyOBJ);
				wordOBJ.put("word", word);
				tempOUT.add(wordOBJ);
				wsdOBJ.put("WSD", wsd);
				tempOUT.add(wsdOBJ);
				
				output.put("sentence", tempOUT);
				
				resultOfCE = output.toString();

//				Gson gson = new GsonBuilder().setPrettyPrinting().create();
//				resultOfCE = gson.toJson(output);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultOfCE;
	}

	public static void main(String[] ar) {
		
		CoreExtractor ce = new CoreExtractor();
		KoreanAnalyzer ka = new KoreanAnalyzer();
		KoreanAnalyzerREST kare = new KoreanAnalyzerREST();

		try {
//			String resultOfKARE = kare.callETRI("Antoine-Laurent de Lavoisier는 새로운 연소 이론을 주장하여 플로지스톤설을 폐기하고 화학을 발전시켰다. 그리고 사망하였다.");
			String resultOfKA = ka
					.getResult("Antoine-Laurent de Lavoisier는 새로운 연소 이론을 주장하여 플로지스톤설을 폐기하고 화학을 발전시켰다.");
			String resultOfCE = ce.parse(resultOfKA);
			System.out.println(resultOfCE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
