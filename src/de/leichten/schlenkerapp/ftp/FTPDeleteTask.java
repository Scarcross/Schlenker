package de.leichten.schlenkerapp.ftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import utils.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import de.leichten.schlenkerapp.R;

public class FTPDeleteTask extends AsyncTask<String, Void, Boolean> {

	private static final String PENDING_DELETE_FILE = "pending.txt";
	private static String FTP_PARTIE_PATH = "/SchlenkerApp/Partie";
	private static String FTP_ARTICLE_PATH = "/SchlenkerApp/Article";

	String host;
	String username;
	String password;
	String conn_type;
	Integer port;

	boolean fromPending;

	String procedure;
	SharedPreferences settings;
	Activity context;

	public FTPDeleteTask(Activity context, boolean fromPending) {
		this.context = context;
		this.fromPending = fromPending;

	}

	@Override
	protected Boolean doInBackground(String... files) {
		extractPreferences();

		if (FTPUtil.ftpConnect(host, username, password, port)) {
			for (String file : files) {
				parseProcedure(file);
				if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
					try {
						deletePartie(file);
					} catch (IOException e) {
						startPendingProcedure(files);
						e.printStackTrace();
					}
				} else if (Constants.PROCEDURE_ARTICLE.equals(procedure)) {
					try {
						deleteArticle(file);
					} catch (IOException e) {
						startPendingProcedure(files);
						e.printStackTrace();
					}
				}
			}
			FTPUtil.ftpDisconnect();
			return true;
		} else {
			startPendingProcedure(files);
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (result) {
			if (fromPending) {
				File file = new File(context.getFilesDir() + Constants.DELETE_PENDING, PENDING_DELETE_FILE);
				file.delete();
			}
		} else {
			Toast.makeText(context, "FTP Delete verschiebe in pending...", Toast.LENGTH_SHORT).show();
		}
	}

	private void startPendingProcedure(String[] files) {
		File file = new File(context.getFilesDir() + Constants.DELETE_PENDING, PENDING_DELETE_FILE);
		file.getParentFile().mkdirs();

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		for (String iterable_element : files) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(iterable_element);
				bw.newLine();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseProcedure(String file) {
		// TODO this is not really reliable

		if (file.contains("_")) {
			this.procedure = Constants.PROCEDURE_ARTICLE;
		} else {
			this.procedure = Constants.PROCEDURE_PARTIE;
		}

	}

	private void deleteArticle(String file) throws IOException {
		FTPUtil.ftpRemoveFile(FTP_ARTICLE_PATH + File.separator + file);
	}

	private void deletePartie(String file) throws IOException {
		FTPUtil.ftpRemoveFile(FTP_PARTIE_PATH + File.separator + file);
	}

	private void extractPreferences() {

		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_FTP, Context.MODE_PRIVATE);
		username = settings.getString(context.getResources().getString(R.string.ftp_username), null);
		password = settings.getString(context.getResources().getString(R.string.ftp_password), null);
		host = settings.getString(context.getResources().getString(R.string.ftp_address), null);
		port = Integer.parseInt(settings.getString(context.getResources().getString(R.string.ftp_port), null));

		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		FTP_PARTIE_PATH = settings.getString(context.getResources().getString(R.string.ftp_partie_folder_path), FTP_PARTIE_PATH);
		FTP_ARTICLE_PATH = settings.getString(context.getResources().getString(R.string.ftp_art_folder_path), FTP_ARTICLE_PATH);

	}

}
