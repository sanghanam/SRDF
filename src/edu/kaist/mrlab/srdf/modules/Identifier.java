package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.HashMap;

import edu.kaist.mrlab.srdf.data.Chunk;

public class Identifier {

	private static HashMap<String, Integer> vpMap = new HashMap<String, Integer>();
	private static HashMap<String, Integer> npMap = new HashMap<String, Integer>();

	public void identifyVP(ArrayList<Chunk> vpChunks) {

		for (int i = 0; i < vpChunks.size(); i++) {

			Chunk vp = vpChunks.get(i);
			String vpLabel = vp.getChunk();

			int vpCount;

			if (vpMap.containsKey(vpLabel)) {

				vpCount = vpMap.get(vpLabel);
				vpMap.put(vpLabel, vpCount + 1);
				vp.setProvenance(String.valueOf(vpCount));

			} else {

				vpMap.put(vpLabel, 1);
				vp.setProvenance("1");

			}

		}

	}

	public void identifyNP(ArrayList<Chunk> npChunks) {

		for (int i = 0; i < npChunks.size(); i++) {

			Chunk np = npChunks.get(i);
			String npPost = np.getPostposition();

			int npCount;

			if (npPost != "") {
				if (npMap.containsKey(npPost)) {

					npCount = vpMap.get(npPost);
					vpMap.put(npPost, npCount + 1);
					np.setProvenance(String.valueOf(npCount));

				} else {

					npMap.put(npPost, 1);
					np.setProvenance("1");

				}
			}

		}

	}

}
