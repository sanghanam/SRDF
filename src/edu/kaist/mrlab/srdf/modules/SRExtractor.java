package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;

import edu.kaist.mrlab.srdf.KoSeCT;
import edu.kaist.mrlab.srdf.SRDF;
import edu.kaist.mrlab.srdf.data.Chunk;

public class SRExtractor {

	public String makeSRTriple(ArrayList<Chunk> NPChunks, ArrayList<Chunk> VPChunks) {

		String results = "";
		Chunk sbjChk = null;
		String sbj = null;

		for (int i = 0; i < NPChunks.size(); i++) {

			if (NPChunks.get(i).getLabel().equals("NP_SBJ")) {

				sbjChk = NPChunks.get(i);
				sbj = sbjChk.getChunk();
				break;
			}

		}
		

		for (int i = 0; i < NPChunks.size(); i++) {

			if (NPChunks.get(i).equals(sbjChk)) {

				continue;

			}

			Chunk obj = NPChunks.get(i);
			int objID = obj.getID();

			String verbLemma = null;
			for (int k = VPChunks.size() - 1; k >= 0; k--) {

				if (VPChunks.get(k).getID() > objID){
					verbLemma = VPChunks.get(k).getChunk();
					break;
				}

			}

			String triple = sbj + "\t" + "srdf:SemanticRelation#" + verbLemma + "\t" + NPChunks.get(i).getChunk();
			results += triple + "\n";

		}

		return results;

	}

	public static void main(String[] ar) {

		SRExtractor sre = new SRExtractor();

		KoSeCT kosect = new KoSeCT();
		Preprocessor p = new Preprocessor();
		SRDF srdf = new SRDF();
		Identifier iden = new Identifier();

		String input = srdf.inputSentence();
		input = p.changeSymbol(input);
		input = kosect.removeUTF8BOM(input);
		input = p.removeBracket(input);

		ArrayList<Chunker> chunkers = kosect.doPreprocessWithoutSplitting(input);

		if (!p.isValidChunk(chunkers) || chunkers.isEmpty()) {
			System.exit(0);
			;
		}

		for (Chunker c : chunkers) {
			iden.identifyVP(c.getVPChunks());

			ArrayList<Chunk> NPChunks = c.getNPChunks();
			ArrayList<Chunk> VPChunks = c.getVPChunks();

			String results = sre.makeSRTriple(NPChunks, VPChunks);
			System.out.println(results);

		}

	}

}
