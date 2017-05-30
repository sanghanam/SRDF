package edu.kaist.mrlab.srdf.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class WikiTextRefiner {

	private static BufferedReader filebr;
	private static BufferedWriter filebw;

	private static String filePath;
	
	public static void main(String[] ar) {
		
		try {

			File fl = new File("data/wiki/wiki/");
			FolderInReader fir = new FolderInReader();
			ArrayList<String> arrFS = fir.RECURSIVE_FILE(fl);
			for (int i = 0; i < arrFS.size(); i++) {

				filePath = arrFS.get(i);

				String fileName = null;
				StringTokenizer st = new StringTokenizer(filePath, "\\");
				int size = st.countTokens();
				for (int k = 0; k < size; k++) {
					fileName = st.nextToken();
					Constants.fileName = fileName.replace(".wiki", ".txt");
				}

				System.out.println(filePath);
//				if (!filePath.contains(".txt")) {
//					continue;
//				}
				filebr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
				filebw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("data/wiki/txt/" + Constants.fileName), "UTF8"));

				String input = null;
				while ((input = filebr.readLine()) != null) {
					if (input.length() != 0) {
						// readedStc++;

						if (input.startsWith("{{")) {
							continue;
						}
						if (input.startsWith("{")) {
							continue;
						}
						if (input.startsWith("}}")) {
							continue;
						}
						if (input.startsWith("==")) {
							continue;
						}
						if (input.startsWith("[[")) {
							continue;
						}
						if (input.startsWith("]]")) {
							continue;
						}
						if (input.startsWith("|")) {
							continue;
						}
						if (input.startsWith("*")) {
							continue;
						}
						if (input.startsWith("<ref")) {
							continue;
						}
						if (input.startsWith("</ref")) {
							continue;
						}
						if (input.startsWith(";")) {
							continue;
						}
						if (input.startsWith(":")) {
							continue;
						}
						if (input.startsWith("---")) {
							continue;
						}
						if (input.startsWith("파일:")) {
							continue;
						}
						if (input.startsWith("<gallery")) {
							continue;
						}
						if (input.startsWith("</gallery")) {
							continue;
						}
						if (input.startsWith("<span")) {
							continue;
						}
						if (input.startsWith("</span")) {
							continue;
						}
						if (input.startsWith("<timeline")) {
							continue;
						}
						if (input.startsWith("</timeline")) {
							continue;
						}
						if (input.startsWith("<center")) {
							continue;
						}
						if (input.startsWith("</center")) {
							continue;
						}
						if (input.startsWith(" |")) {
							continue;
						}
						if (input.startsWith("  |")) {
							continue;
						}
						if (input.startsWith("   |")) {
							continue;
						}
						if (input.startsWith("!")) {
							continue;
						}
						if (input.startsWith("#")) {
							continue;
						}

						input = input.replaceAll("'''", "");
						input = input.replaceAll("&nbsp;", "");
						// input = input.replace("[[", "");
						// input = input.replace("]]", "");

						int oneRefStart = input.indexOf("<ref ");
						int oneRefEnd = input.indexOf(" />");

						String prePart = null;
						String postPart = null;

						if (oneRefStart >= 0 && oneRefEnd >= 0) {

							prePart = input.substring(0, oneRefStart);
							postPart = input.substring(oneRefEnd + 3, input.length());

							input = prePart + postPart;

						}

						int refStart = input.indexOf("<ref");
						int refEnd = input.indexOf("</ref>");

						boolean printable = true;

						while (refStart >= 0 || refEnd >= 0) {

							if (refEnd < 0 && refStart >= 0) {

								input = input.substring(0, refStart);

							} else if (refStart < 0 && refEnd >= 0) {

								printable = false;
								break;

							} else if (refStart >= 0 && refEnd >= 0 && refStart < refEnd) {

								prePart = input.substring(0, refStart);
								postPart = input.substring(refEnd + 6, input.length());

								input = prePart + postPart;

							}

							refStart = input.indexOf("<ref");
							refEnd = input.indexOf("</ref>");

						}
						
						int divStart = input.indexOf("<div");
						int divEnd = input.indexOf("</div>");

						while (divStart >= 0 || divEnd >= 0) {

							if (divEnd < 0 && divStart >= 0) {

								input = input.substring(0, divStart);

							} else if (divStart < 0 && divEnd >= 0) {

								printable = false;
								break;

							} else if (divStart >= 0 && divEnd >= 0 && divStart < divEnd) {

								prePart = input.substring(0, divStart);
								postPart = input.substring(divEnd + 6, input.length());

								input = prePart + postPart;

							}

							divStart = input.indexOf("<div");
							divEnd = input.indexOf("</div>");

						}
						
						
						int anonStart = input.indexOf("<!--");
						int anonEnd = input.indexOf("-->");

						while (anonStart >= 0 || anonEnd >= 0) {

							if (anonEnd < 0 && anonStart >= 0) {

								input = input.substring(0, anonStart);

							} else if (anonStart < 0 && anonEnd >= 0) {

								printable = false;
								break;

							} else if (anonStart >= 0 && anonEnd >= 0 && anonStart < anonEnd) {

								prePart = input.substring(0, anonStart);
								postPart = input.substring(anonEnd + 3, input.length());

								input = prePart + postPart;

							}

							anonStart = input.indexOf("<div");
							anonEnd = input.indexOf("</div>");

						}
						
						int pareStart = input.indexOf("{{");
						int pareEnd = input.indexOf("}}");

						while (pareStart >= 0 || pareEnd >= 0) {

							if (pareEnd < 0 && pareStart >= 0) {

								input = input.substring(0, pareStart);

							} else if (pareStart < 0 && pareEnd >= 0) {

								printable = false;
								break;

							} else if (pareStart >= 0 && pareEnd >= 0 && pareStart < pareEnd) {

								prePart = input.substring(0, pareStart);
								postPart = input.substring(pareEnd + 2, input.length());

								input = prePart + postPart;

							} else if (pareStart > pareEnd){
								
								prePart = input.substring(0, pareEnd);
								postPart = input.substring(pareEnd + 2, input.length());

								input = prePart + postPart;
								
							}

							pareStart = input.indexOf("{{");
							pareEnd = input.indexOf("}}");

						}
						
						int smallStart = input.indexOf("<small>");
						int smallEnd = input.indexOf("</small>");
						
						while (smallStart >= 0 || smallEnd >= 0) {

							if(smallStart >= 0 && smallEnd >= 0){
								
								prePart = input.substring(0, smallStart);
								postPart = input.substring(smallEnd + 8, input.length());

								input = prePart + postPart;
								
							}

							smallStart = input.indexOf("<small>");
							smallEnd = input.indexOf("</small>");

						}
						
						if (printable) {

							int parStart = input.indexOf("[[");
							int parEnd = input.indexOf("]]");
							int orPosition = input.indexOf("|");

							while (parStart >= 0 && parEnd >= 0) {

								if (orPosition < parEnd && orPosition > parStart) {

									String label = input.substring(parStart + 2, orPosition);
									prePart = input.substring(0, parStart);
									postPart = input.substring(parEnd + 2, input.length());
									input = prePart + label + postPart;

								} else {

									String label = input.substring(parStart + 2, parEnd);
									prePart = input.substring(0, parStart);
									postPart = input.substring(parEnd + 2, input.length());
									input = prePart + label + postPart;

								}

								parStart = input.indexOf("[[");
								parEnd = input.indexOf("]]");
								orPosition = input.indexOf("|");

							}

							System.out.println(input);
							filebw.write(input + "\n");

						}
						
					}
				}
				filebr.close();
				filebw.close();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(filePath);
		}

	}
}
