package edu.kaist.mrlab.srdf;

import java.util.NoSuchElementException;

import edu.kaist.mrlab.srdf.modules.EntityLinker;
import edu.kaist.mrlab.srdf.modules.Preprocessor;
import edu.kaist.mrlab.srdf.modules.SentenceSplitter;

public class ELSRDF {
	public static void main(String[] ar) throws NoSuchElementException {

		KoSeCT kosect = new KoSeCT();
		Preprocessor p = new Preprocessor();
		SentenceSplitter ss = new SentenceSplitter();
		SRDF srdf = new SRDF();
		EntityLinker el = new EntityLinker();

		String input = srdf.inputSentence();
		String output = srdf.doOneSentence(kosect, p, ss, input);
		System.out.println(el.pipeEL(output, input));
		

	}
}
