package edu.kaist.mrlab.srdf.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SubjectNullAttacher {

	private static BufferedReader filebr;
	private static BufferedWriter filebw;

	private static String filePath;

	public static void main(String[] ar) {

		try {

			File fl = new File("data/wiki/txt/");
			FolderInReader fir = new FolderInReader();
			ArrayList<String> arrFS = fir.RECURSIVE_FILE(fl);
			for (int z = 0; z < arrFS.size(); z++) {

				filePath = arrFS.get(z);

				String fileName = null;
				StringTokenizer st = new StringTokenizer(filePath, "\\");
				int size = st.countTokens();
				for (int k = 0; k < size; k++) {
					fileName = st.nextToken();
					Constants.fileName = fileName.replace(".txt", ".txt");
				}

				System.out.println(filePath);
				// if (!filePath.contains(".txt")) {
				// continue;
				// }
				filebr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
				filebw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("data/wiki/sbj/" + Constants.fileName), "UTF8"));

				String input = null;
				while ((input = filebr.readLine()) != null) {
					if (input.length() != 0) {

						KoreanAnalyzerREST karest = new KoreanAnalyzerREST();
						input = input.replaceAll("\"", "'");
						// System.out.println(input);
						String etriOutput = karest.callETRI(input);

						JSONParser jsonParser = new JSONParser();

						JSONArray sentArr = (JSONArray) jsonParser.parse(etriOutput);

						Iterator<?> s = sentArr.iterator();

						boolean attachable = true;

						while (s.hasNext()) {

							JSONObject innerOBJ = (JSONObject) s.next();
							JSONArray dependency = (JSONArray) innerOBJ.get("dependency");
							String text = (String) innerOBJ.get("text");

							for (int i = 0; i < dependency.size(); i++) {

								JSONObject depenObject = (JSONObject) dependency.get(i);
								if (depenObject.get("label").equals("NP_SBJ")) {

									attachable = false;
									break;

								}

							}

							if (attachable) {

								text = "null은 " + text;

							}

							text = text.trim();

							System.out.println(text);
							filebw.write(text + "\n");

						}

					}

				}
				filebr.close();
				filebw.close();
				
				File f = new File(filePath);
				f.delete();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
