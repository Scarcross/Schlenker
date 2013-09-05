package de.leichten.schlenkerapp.sd;

import java.io.File;

import android.os.Environment;

public class SDUtil {
	private static String SD_PARTIE_PATH = "/SchlenkerApp/Partie";
	private static String SD_ARTICLE_PATH = "/SchlenkerApp/Article";
	
	public static boolean checkFileExists(File file) {
		File path = new File(Environment.getExternalStorageDirectory() + File.separator + SD_ARTICLE_PATH);
		File path2 = new File(Environment.getExternalStorageDirectory() + File.separator + SD_PARTIE_PATH);

		File[] listFiles1 = path.listFiles();
		File[] listFiles2 = path2.listFiles();
		
		for (File file1 : listFiles1) {
			if(file.getName().equals(file1.getName())){
				return true;
			}
		}
		for (File file2 : listFiles2) {
			if(file.getName().equals(file2.getName())){
				return true;
			}
		}				
		return false;

	}
}
