package de.leichten.schlenkerapp.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import utils.BitmapHelpers;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;
import de.leichten.schlenkerapp.main.MainMenue;

public class FinishingTask extends AsyncTask<String, Void, Boolean> {

	private Context context;
	File lastPic;
	
	private final String Tag = getClass().getName();

	public FinishingTask(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... filename) {

		if (filename.length >= 1) {
			File lastPic = new File(context.getFilesDir(), "newImage.jpg");
			if (lastPic.renameTo(new File(context.getFilesDir(), filename[0]))) {
				BitmapHelpers.decodeAndResizeFile(lastPic);
				//rotatePicture();
				startOtherFileTasks();
			}
		}

		return false;
	}

	
	
	private void rotatePicture() {
		try {
			ExifInterface exif = new ExifInterface(lastPic.getAbsolutePath());
			int attributeInt = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Log.d(Tag, "EXIF says:" + attributeInt);
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startOtherFileTasks() {
		new FileHandlingTasksStarter(context);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Intent intent = new Intent(context, MainMenue.class);
				context.startActivity(intent);
			}
		}, 3000);
		showFileList();
	}

	private void showFileList() {
		File root = context.getFilesDir();
		File[] fileList = root.listFiles();
		for (File file : fileList) {
			Log.d(Tag, file.getAbsolutePath());
		}
	}

}
