package edu.kaist.mrlab.srdf.modules;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;

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
				
				JSONObject textOBJ = new JSONObject();
				JSONObject morpOBJ = new JSONObject();
				JSONObject dependencyOBJ = new JSONObject();
				JSONObject wordOBJ = new JSONObject();
				textOBJ.put("text", text);
				tempOUT.add(textOBJ);
				morpOBJ.put("morp", morp);
				tempOUT.add(morpOBJ);
				dependencyOBJ.put("dependency", dependency);
				tempOUT.add(dependencyOBJ);
				wordOBJ.put("word", word);
				tempOUT.add(wordOBJ);
				
				output.put("sentence", tempOUT);

				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				resultOfCE = gson.toJson(output);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultOfCE;
	}

	public static void main(String[] ar) {
		
		CoreExtractor ce = new CoreExtractor();
		KoreanAnalyzer ka = new KoreanAnalyzer();

		try {
			String resultOfKA = ka
					.getResult("�삤�뱶由� �뿵踰덉� 踰④린�뿉�뿉�꽌 �깭�뼱�궃 �쁺援��쓽 諛곗슦�씠�옄 �씤�룄二쇱쓽�옄�씠�떎.");
			String resultOfCE = ce.parse(resultOfKA);
			System.out.println(resultOfCE);
			
//			BufferedWriter filebw = new BufferedWriter(
//					new OutputStreamWriter(
//							new FileOutputStream(
//									"data\\coreextracted\\test8.json"),
//							"UTF8"));
//			filebw.write(resultOfCE);
//			filebw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
