package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.HashMap;

import edu.kaist.mrlab.srdf.data.Chunk;

public class Identifier {

	private static HashMap<String, Integer> entityMap = new HashMap<String, Integer>();

	public void identify(ArrayList<Chunk> vpChunks) {

		for (int i = 0; i < vpChunks.size(); i++) {

			Chunk vp = vpChunks.get(i);
			String vpLabel = vp.getChunk();

			int vpCount;

			if (entityMap.containsKey(vpLabel)) {

				vpCount = entityMap.get(vpLabel);
				entityMap.put(vpLabel, vpCount + 1);
				vp.setProvenance(String.valueOf(vpCount));

			} else {

				entityMap.put(vpLabel, 1);
				vp.setProvenance("1");

			}

		}

	}

}
