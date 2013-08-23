package de.leichten.schlenkerapp.tasks;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FinishingTask extends AsyncTask<String, Void, Boolean> {

	private Context context;

	public FinishingTask(Context context) {
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		if (params.length>=1){
			File out = new File(context.getFilesDir(), "newImage.jpg");
			if (out.renameTo(new File(context.getFilesDir(), params[0]))) {
				return true;
			}
		}
		return false;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		Toast.makeText(context, "File renamed", Toast.LENGTH_LONG).show();
	}
	
}
