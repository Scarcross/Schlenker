package de.leichten.schlenkerapp.tasks;

import java.io.File;

import de.leichten.schlenkerapp.ftp.FTPUploadTask;
import de.leichten.schlenkerapp.preferences.FTPUploadSettingsAcitvity;

import utils.Constants;
import utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class StartupPendingTask extends AsyncTask<Void, Void, Void> {

	Activity context;

	public StartupPendingTask(Activity context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... params) {


		if (Utils.checkWifiConnectivity(context)) {
			startPendingUpload();

		} else {
			// TODO If there is no WIFI connection and the max pending space is
			// reached make dialog and quit APP!

		}

		return null;
	}

	private void startPendingUpload() {
		File[] articlePending = new File(context.getFilesDir()+Constants.ARTICLE_PENDING).listFiles();
		if(articlePending != null){
			new FTPUploadTask(context, Constants.PROCEDURE_ARTICLE).execute(articlePending);
		}
		File[] partiePending = new File(context.getFilesDir()+Constants.PARTIE_PENDING).listFiles();
		if(articlePending != null){
			new FTPUploadTask(context, Constants.PARTIE_PENDING).execute(partiePending);
		}

	}
	
	
}
