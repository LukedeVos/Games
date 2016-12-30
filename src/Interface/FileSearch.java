package Interface;

import java.io.*;

public class FileSearch{   
	public String find(File dir, String pattern){
		
		File listFile[] = dir.listFiles();
		if(listFile != null){
			System.out.println("not empty");
			for(int i=0; i<listFile.length; i++){
				if (listFile[i].isDirectory()){
					find(listFile[i], pattern);
				} else { 
					if(listFile[i].getName().endsWith(pattern)){
//						System.out.println(listFile[i].getPath());
						pattern = listFile[i].getPath();
					}
				}
			}
		} else {
			System.out.println("empty");
		}
		return pattern;
	}
}