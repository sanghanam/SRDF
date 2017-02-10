package edu.kaist.mrlab.srdf.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TripleCounter {

	private static BufferedReader filebr;
	private static String filePath;

	private int count() {
		
		int count = 0;

		try {

			File fl = new File(Constants.wikiPathOutput);
			FolderInReader fir = new FolderInReader();
			ArrayList<String> arrFS = fir.RECURSIVE_FILE(fl);
			for (int i = 0; i < arrFS.size(); i++) {

				filePath = arrFS.get(i);

//				String fileName = null;
//				StringTokenizer st = new StringTokenizer(filePath, "\\");
//				int size = st.countTokens();
//				for (int k = 0; k < size; k++) {
//					fileName = st.nextToken();
//				}

				System.out.println(filePath);
				if (!filePath.contains(".txt")) {
					continue;
				}
				filebr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));

				String input = null;
				while ((input = filebr.readLine()) != null) {
					if (input.length() != 0) {
						// readedStc++;
						
						if(!input.contains("STC")){
							count++;
						}
						
					}
				}
				filebr.close();
				
				System.out.println(count);

				// System.out.println("readed sentence : " + readedStc);
				// System.out.println("generated triples : " +
				// generatedTriples);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(filePath);
		}

		return count;
	}

	public static void main(String[] ar) {

		TripleCounter tc = new TripleCounter();
		int totalTripleCount = tc.count();
		System.out.println(totalTripleCount);

	}
}
