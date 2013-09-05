package de.leichten.schlenkerapp.tasks;

import java.io.File;

import de.leichten.schlenkerapp.imagehandling.MemoryCache;
import de.leichten.schlenkerapp.main.ImagesActivity;


import utils.BitmapHelpers;
import android.os.AsyncTask;

public class ImageRotationTask extends AsyncTask<File, Void, File> {
	ImagesActivity imagesActivity;
	int degrees = 0;

	public ImageRotationTask(int degrees) {
		this.degrees = degrees;
	}

	public ImageRotationTask(int degrees, ImagesActivity imagesActivity) {
		this.degrees = degrees;
		this.imagesActivity = imagesActivity;
	}

	@Override
	protected File doInBackground(File... src) {

		for (File file : src) {
			BitmapHelpers.rotateImage(file, degrees);
		}
		if (src.length == 1) {
			return src[0];
		} else {
			return null;
		}
	}

	@Override
	protected void onPostExecute(File result) {
		super.onPostExecute(result);
		if (imagesActivity != null) {
		    MemoryCache memoryCache = MemoryCache.getInstance();
		    memoryCache.remove(result.getAbsolutePath());
		    imagesActivity.loadPictures();
		}
	}

}
