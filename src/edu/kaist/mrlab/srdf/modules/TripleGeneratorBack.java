package edu.kaist.mrlab.srdf.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.kaist.mrlab.srdf.data.Chunk;
import edu.kaist.mrlab.srdf.data.Triple;
import edu.kaist.mrlab.srdf.tools.Constants;

public class TripleGeneratorBack {

	ArrayList<Chunk> NPChunks;
	ArrayList<Chunk> VPChunks;
	ArrayList<Triple> triples = new ArrayList<Triple>();

	public ArrayList<Triple> getTriples() {
		return triples;
	}

	public void generate() {

		Chunk SBJChk = null;
		Chunk SBJRELChk = null;

		String sbj = "";
		String rel = "";
		String obj = "";

		int SBJID = -1;

		boolean isNoSBJ = true;

		// NPChunks와 VPChunks 정렬

		Collections.sort(NPChunks, new Comparator<Chunk>() {
			public int compare(Chunk obj1, Chunk obj2) {
				// TODO Auto-generated method stub
				return (obj1.getID() < obj2.getID()) ? -1 : (obj1.getID() > obj2.getID()) ? 1 : 0;
			}
		});

		Collections.sort(VPChunks, new Comparator<Chunk>() {
			public int compare(Chunk obj1, Chunk obj2) {
				// TODO Auto-generated method stub
				return (obj1.getID() < obj2.getID()) ? -1 : (obj1.getID() > obj2.getID()) ? 1 : 0;
			}
		});

		if (NPChunks.size() == 0 || VPChunks.size() == 0) {
			return;
		}

		// NP 중에서 주어를 발견한다.
		// 주어 발견 조건:
		// 1. NP_SBJ이여야 함
		// 2. NP_SBJ가 2개 이상일 때는 ID값이 작은 것. 즉, 왼쪽에 있는 것

		for (int i = 0; i < NPChunks.size(); i++) {
			Chunk tempChk;
			if ((tempChk = NPChunks.get(i)).getLabel().equals("NP_SBJ")) {
				SBJChk = tempChk;
				isNoSBJ = false;
				break;
			}
		}

		int fNPID = NPChunks.get(0).getID();
		int fVPID = VPChunks.get(0).getID();

		int fID = -1;

		if (fNPID <= fVPID) {
			fID = fNPID;
		} else {
			fID = fVPID;
		}

		if (isNoSBJ) {
			return;
		}

		sbj = SBJChk.getChunk();
		SBJID = SBJChk.getID();

		// VP 중에서 가장 뒤를 가져온다.
		// 이것이 주어와 가장 먼저 짝을 이룰 relation이다.

		SBJRELChk = VPChunks.get(VPChunks.size() - 1);

		rel = SBJRELChk.getChunk() + "#" + Constants.fileName + "#" + Constants.lineNumber + "#"
				+ SBJRELChk.getProvenance();

		Set<Integer> modArr = new HashSet<Integer>(SBJRELChk.getMod());

		if (modArr.size() == 0) {
			return;
		}

		int maxMod = Collections.max(modArr);
		Chunk tempChk = getChunkByMod(maxMod);

		String before = "SBJ";

		while (modArr.size() > 0 || maxMod == fID) {
			if (tempChk.getLabel().contains("VP")) {

				if (before.equals("SBJ")) {
					obj = "ANONYMOUS";
					// System.out.println(sbj + ", " + rel + ", " + obj);
					if (sbj.contains("#")) {

						if (rel.contains("#")) {

							triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
									"<" + Constants.propertyPrefix + rel + ">",
									"<" + Constants.entityPrefix + obj + ">"));
						} else {

							triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
									"<" + Constants.josaPrefix + rel + ">", "<" + Constants.entityPrefix + obj + ">"));
						}

					} else {
						triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
								"<" + Constants.propertyPrefix + rel + ">", "<" + Constants.entityPrefix + obj + ">"));
					}

					if (rel.contains("#")) {
						triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">", "<" + Constants.sp + ">",
								"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
					}

					sbj = rel;

				} else if (before.equals("VP")) {

					sbj = rel;
					rel = tempChk.getChunk() + "#" + Constants.fileName + "#" + Constants.lineNumber + "#"
							+ tempChk.getProvenance();

				} else {
					if (modArr.size() == 1) {
						sbj = rel;
						rel = tempChk.getChunk() + "#" + Constants.fileName + "#" + Constants.lineNumber + "#"
								+ tempChk.getProvenance();
						obj = "ANONYMOUS";
						// System.out.println(sbj + ", " + rel + ", " + obj);
						if (sbj.contains("#")) {
							if (rel.contains("#")) {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							} else {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.josaPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
						} else {
							triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
									"<" + Constants.propertyPrefix + rel + ">",
									"<" + Constants.entityPrefix + obj + ">"));
						}
						if (rel.contains("#")) {
							triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">", "<" + Constants.sp + ">",
									"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
						}
					} else {
						rel = tempChk.getChunk() + "#" + Constants.fileName + "#" + Constants.lineNumber + "#"
								+ tempChk.getProvenance();
					}
				}

				if (modArr.size() == 0) {
					break;
				}

				maxMod = Collections.max(modArr);
				modArr.remove((Object) maxMod);
				tempChk = getChunkByMod(maxMod);
				if (tempChk.getLabel().contains("VP")) {
					modArr.addAll(tempChk.getMod());
				}
				before = "VP";

			} else if (tempChk.getLabel().contains("NP")) {

				if (before.contains("SBJ")) {

					if (rel.substring(0, rel.indexOf("#")).equals("이")) {

						obj = tempChk.getChunk();
						// System.out.println(sbj + ", " + rel + ", " + obj);
						if (sbj.contains("#")) {
							if (rel.contains("#")) {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							} else {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.josaPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
						} else {
							triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
									"<" + Constants.propertyPrefix + rel + ">",
									"<" + Constants.entityPrefix + obj + ">"));
						}
						if (rel.contains("#")) {
							triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">", "<" + Constants.sp + ">",
									"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
						}
						modArr.remove((Object) maxMod);

					} else {
						if (tempChk.getPostposition().equals("을") || tempChk.getPostposition().equals("를")) {
							obj = tempChk.getChunk();
							// System.out.println(sbj + ", " + rel + ", " +
							// obj);
							if (sbj.contains("#")) {
								if (rel.contains("#")) {

									triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
											"<" + Constants.propertyPrefix + rel + ">",
											"<" + Constants.entityPrefix + obj + ">"));
								} else {

									triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
											"<" + Constants.josaPrefix + rel + ">",
											"<" + Constants.entityPrefix + obj + ">"));
								}
							} else {
								triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
							if (rel.contains("#")) {
								triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.sp + ">",
										"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
							}
							modArr.remove((Object) maxMod);
							sbj = rel;

						} else {
							obj = "ANONYMOUS";
							// System.out.println(sbj + ", " + rel + ", " +
							// obj);
							if (sbj.contains("#")) {
								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							} else {
								triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
							if (rel.contains("#")) {
								triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.sp + ">",
										"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
							}
							sbj = rel;
						}
					}

				} else {
					if (maxMod == SBJID) {
						if (maxMod == 0 || modArr.size() == 0) {
							break;
						}

						maxMod = Collections.max(modArr);
						modArr.remove((Object) maxMod);
						tempChk = getChunkByMod(maxMod);
						if (tempChk.getLabel().contains("VP")) {
							modArr.addAll(tempChk.getMod());
						}
						continue;
					}
					if (tempChk.getPostposition().equals("을") || tempChk.getPostposition().equals("를")) {
						obj = tempChk.getChunk();
						// System.out.println(sbj + ", " + rel + ", " + obj);
						if (sbj.contains("#")) {
							if (rel.contains("#")) {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							} else {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.josaPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
						} else {
							triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
									"<" + Constants.propertyPrefix + rel + ">",
									"<" + Constants.entityPrefix + obj + ">"));
						}
						if (rel.contains("#")) {
							triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">", "<" + Constants.sp + ">",
									"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
						}

						if (triples.size() > 2) {
							sbj = rel;
						}

					} else {
						if (before.contains("VP")) {
							obj = "ANONYMOUS";
							// System.out.println(sbj + ", " + rel + ", " +
							// obj);
							if (sbj.contains("#")) {
								if (rel.contains("#")) {

									triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
											"<" + Constants.propertyPrefix + rel + ">",
											"<" + Constants.entityPrefix + obj + ">"));
								} else {

									triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
											"<" + Constants.josaPrefix + rel + ">",
											"<" + Constants.entityPrefix + obj + ">"));
								}
							} else {
								triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
							if (rel.contains("#")) {
								triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.sp + ">",
										"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
							}
							sbj = rel;
						}
						rel = tempChk.getPostposition();
						if (rel.equals("")) {
							rel = "JOSA";
						}
						obj = tempChk.getChunk();
						// System.out.println(sbj + ", " + rel + ", " + obj);
						if (sbj.contains("#")) {
							if (rel.contains("#")) {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.propertyPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							} else {

								triples.add(new Triple("<" + Constants.propertyPrefix + sbj + ">",
										"<" + Constants.josaPrefix + rel + ">",
										"<" + Constants.entityPrefix + obj + ">"));
							}
						} else {
							triples.add(new Triple("<" + Constants.entityPrefix + sbj + ">",
									"<" + Constants.propertyPrefix + rel + ">",
									"<" + Constants.entityPrefix + obj + ">"));
						}
						if (rel.contains("#")) {
							triples.add(new Triple("<" + Constants.propertyPrefix + rel + ">", "<" + Constants.sp + ">",
									"<" + Constants.propertyPrefix + rel.substring(0, rel.indexOf("#")) + ">"));
						}
					}
				}

				if (modArr.size() == 0) {
					break;
				}

				maxMod = Collections.max(modArr);
				modArr.remove((Object) maxMod);
				tempChk = getChunkByMod(maxMod);
				if (tempChk.getLabel().contains("VP")) {
					modArr.addAll(tempChk.getMod());
				}
				before = "NP";

			}
		}

	}

	public Chunk getChunkByMod(int mod) {
		Chunk c = null;

		for (int i = 0; i < NPChunks.size(); i++) {
			if ((c = NPChunks.get(i)).getID() == mod) {
				return c;
			}
		}

		for (int i = 0; i < VPChunks.size(); i++) {
			if ((c = VPChunks.get(i)).getID() == mod) {
				return c;
			}
		}
		return c;
	}

	public TripleGeneratorBack(ArrayList<Chunk> NPChunks, ArrayList<Chunk> VPChunks) {
		this.NPChunks = NPChunks;
		this.VPChunks = VPChunks;
	}

}
