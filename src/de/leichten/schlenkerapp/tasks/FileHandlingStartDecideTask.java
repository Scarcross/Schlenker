package de.leichten.schlenkerapp.tasks;

import java.io.File;

import utils.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.ftp.FTPUpload;
import de.leichten.schlenkerapp.sd.SDSaving;

public class FileHandlingStartDecideTask extends AsyncTask<Void, Void, Void> {

	String procedure;
	Activity activity;
	File picture;
	boolean fromPending;
	boolean increasing;

	public FileHandlingStartDecideTask(File picture, String procedure, Activity activity, boolean fromPending, boolean increasing) {
		this.procedure = procedure;
		this.activity = activity;
		this.picture = picture;
		this.fromPending = fromPending;
		this.increasing = increasing;

	}

	private void startOtherFileTasks() {

		if (Constants.PROCEDURE_PARTIE.equals(this.procedure)) {
			decidePartieTasks();
		} else if (Constants.PROCEDURE_ARTICLE.equals(this.procedure)) {
			decideArticleTasks();
		}
	}

	private void decideArticleTasks() {
		SharedPreferences prefsMain = activity.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		// Do we have to upload it to an FTP?
		if (prefsMain.getBoolean(activity.getResources().getString(R.string.key_art_ftp_upload), false)) {
			new FTPUpload(activity, this.procedure, this.fromPending, this.increasing).uploadFiles(picture);
		}
		else if(prefsMain.getBoolean(activity.getResources().getString(R.string.key_art_copy_sd), false)) {
			new SDSaving(this.activity, this.procedure).saveSD(picture.getAbsoluteFile());
		}
		
	}

	private void decidePartieTasks() {

		SharedPreferences prefsMain = activity.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		// Do we have to upload it to an FTP?
		if (prefsMain.getBoolean(activity.getResources().getString(R.string.key_partie_ftp_upload), false)) {
			new FTPUpload(activity, this.procedure, this.fromPending, this.increasing).uploadFiles(picture);
		}
		else if(prefsMain.getBoolean(activity.getResources().getString(R.string.key_art_copy_sd), false)) {
			new SDSaving(this.activity, this.procedure).saveSD(picture.getAbsoluteFile());
		}
			
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		startOtherFileTasks();
		return null;
	}
}
