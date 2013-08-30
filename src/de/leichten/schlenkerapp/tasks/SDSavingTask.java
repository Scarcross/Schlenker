package de.leichten.schlenkerapp.tasks;

import java.io.File;

import utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class SDSavingTask extends AsyncTask<File, Void, Void> {

	File file;
	Context context;

	public SDSavingTask(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(File... params) {

		// Check for SD Card
		if (!Utils.checkSDCardAvailable()) {
			Toast.makeText(context, "Error! No SDCARD Found!",Toast.LENGTH_LONG).show();
		} else {
			// Locate the image folder in your SD Card
			file = new File(Environment.getExternalStorageDirectory() + File.separator + "SchlenkerImages");
			// Create a new folder if no folder named SchlenkerImages exist
			file.mkdirs();

		}
		return null;
	}
}
