package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.kaist.mrlab.srdf.tools.KoreanAnalyzer;

public class SentenceSplitter {
	
	KoreanAnalyzer ka = new KoreanAnalyzer();
	
	public ArrayList<String> splitSentence(String input){
		
		ArrayList<String> results = new ArrayList<String>();
		
		try {
			
			String resultOfKA = ka.getResult(input);

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(resultOfKA);

			JSONArray sentArr = (JSONArray) jsonObject.get("sentence");

			Iterator<?> s = sentArr.iterator();
			
			while (s.hasNext()) {
			
				JSONObject innerOBJ = (JSONObject) s.next();
				String text = (String) innerOBJ.get("text");
				results.add(text);
				
			}
			
			
		} catch (Exception e){
			
		}
		
		
		return results;
	}
	
	
	
	public static void main(String[] ar){
		
		SentenceSplitter ss = new SentenceSplitter();
		
		ArrayList<String> results = ss.splitSentence("공간에 대한 연구는 기하학에서 시작되었고, 특히 유클리드 기하학에서 비롯되었다. 삼각법은 공간과 수들을 결합하였고, 잘 알려진 피타고라스의 정리를 포함한다. 현대에 와서 공간에 대한 연구는, 이러한 개념들은 더 높은 차원의 기하학을 다루기 위해 비유클리드 기하학(상대성이론에서 핵심적인 역할을 함)과 위상수학으로 일반화되었다. 수론과 공간에 대한 이해는 모두 해석 기하학, 미분기하학, 대수기하학에 중요한 역할을 한다. 리 군도 공간과 구조, 변화를 다루는데 사용된다. 위상수학은 20세기 수학의 다양한 지류속에서 괄목할만한 성장을 한 분야이며, 푸앵카레 추측과 인간에 의해서 증명되지 못하고 오직 컴퓨터로만 증명된 4색정리를 포함한다.");
		for(int i = 0; i < results.size(); i++){
			System.out.println(results.get(i));
		}
		
	}
}
