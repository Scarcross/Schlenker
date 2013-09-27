package de.leichten.schlenkerapp.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import utils.Constants;
import utils.Utils;
import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import de.leichten.schlenkerapp.ftp.FTPDeleteTask;
import de.leichten.schlenkerapp.ftp.FTPUpload;

public class StartupPendingTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog myPd_ring;
	Activity context;

	File[] articlePending;
	File[] partiePending;
	ArrayList<String> deletePending;

	public StartupPendingTask(Activity context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		myPd_ring = new ProgressDialog(context);
		myPd_ring.setMessage("Pending verarbeiten...");
		myPd_ring.setIcon(R.drawable.ic_dialog_info);
		myPd_ring.show();

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (Utils.checkWifiConnectivity(context)) {
			synchronizePending();
			startPendingUpload();
			startPendingDeletions();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		myPd_ring.dismiss();
		super.onPostExecute(result);
	}

	private void startPendingDeletions() {
		if (deletePending != null) {
			new FTPDeleteTask(context, true).execute(deletePending.toArray(new String[deletePending.size()]));
		}
	}

	private void synchronizePending() {
		ArrayList<File> temp;

		getPendingUploadLists();
		getPendingDeletionList();

		if (deletePending != null) {
			if (articlePending != null) {
				temp = new ArrayList<File>();

				for (File element : articlePending) {
					if (!deletePending.contains(element.getName())) {
						temp.add(element);
					}
					else {
						deletePending.remove(element.getName());
						element.delete();
					}
				}
				articlePending = temp.toArray(new File[temp.size()]);
			}

			if (partiePending != null) {
				temp = new ArrayList<File>();

				for (File element : partiePending) {
					if (!deletePending.contains(element.getName())) {
						temp.add(element);
					}
					else {
						deletePending.remove(element.getName());
						element.delete();
					}
				}
				partiePending = temp.toArray(new File[temp.size()]);
			}

		}
	}

	private void startPendingUpload() {
		if (articlePending != null && articlePending.length >= 1) {
			new FTPUpload(context, Constants.PROCEDURE_ARTICLE, true, true).uploadFiles(articlePending);
		} 
		if (partiePending != null && partiePending.length >= 1) {
			new FTPUpload(context, Constants.PROCEDURE_PARTIE, true, true).uploadFiles(partiePending);
		}
	}

	private void getPendingUploadLists() {
		articlePending = new File(context.getFilesDir() + Constants.ARTICLE_UPLOAD_PENDING).listFiles();
		partiePending = new File(context.getFilesDir() + Constants.PARTIE_UPLOAD_PENDING).listFiles();
	}

	private void getPendingDeletionList() {
		try {
			File file = new File(context.getFilesDir() + Constants.DELETE_PENDING, "pending.txt");
			if (file.exists()) {
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				String line;
				this.deletePending = new ArrayList<String>();

				while ((line = bufferedReader.readLine()) != null) {
					deletePending.add(line);
				}
				fileReader.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
