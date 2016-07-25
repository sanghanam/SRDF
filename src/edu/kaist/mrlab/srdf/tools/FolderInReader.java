package edu.kaist.mrlab.srdf.tools;

import java.io.File;
import java.util.ArrayList;

public class FolderInReader {
	
	ArrayList<String> allFileList = new ArrayList<String>();

	public ArrayList<String> RECURSIVE_FILE(File p_file) {

		File[] arrFS = p_file.listFiles();
		for (int i = 0; i < arrFS.length; i++) {
			if (arrFS[i].isDirectory()) {
				// System.out.println(arrFS[i].getName());
				RECURSIVE_FILE(arrFS[i]);
			}
			allFileList.add(arrFS[i].getPath());
			// if()
		}
		
		return allFileList;
	}

}