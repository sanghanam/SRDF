package edu.kaist.mrlab.srdf.tools;

public class StringEdit {
	public static String replaceLast(String text, String regex,
			String replacement) {
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")",
				replacement);
	}
}
