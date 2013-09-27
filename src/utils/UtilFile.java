package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class UtilFile {

	public static boolean saveBitmapToFile(Bitmap bitmap, File file) {

		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			BitmapHelpers.decodeAndResizeFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static void copyFile(File src, File dest) throws IOException {

		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();

	}
	
	public static File[] getOnlyFiles(File[] fileList){
		
		ArrayList<File> temp = new ArrayList<File>();
		for (int i = 0; i < fileList.length; i++) {
			File array_element = fileList[i];
			if (array_element.isFile()) {
				temp.add(array_element);
			}
		}
		return temp.toArray(new File[temp.size()]);
	}
	
	
	public static int getCountHistoryDir(Context context) {
		File root = context.getFilesDir();
		File[] fileList = root.listFiles();

		fileList = getOnlyFiles(fileList);
		
		for (File file : fileList) {
			Log.d("Util", file.getAbsolutePath());
		}
		return fileList.length;
	}

	public static File getOldestFile(Context context) {
		File oldestFile = null;
		File root = context.getFilesDir();
		File[] fileList = root.listFiles();

		fileList = getOnlyFiles(fileList);

		for (File file : fileList) {
			if (oldestFile == null) {
				oldestFile = file;
				continue;
			}
			if(oldestFile.lastModified()> file.lastModified()){
				oldestFile = file;
			}
		}
		return oldestFile;
	}
	

}
