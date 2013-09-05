package de.leichten.schlenkerapp.ftp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import utils.Constants;
import utils.UtilFile;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import de.leichten.schlenkerapp.R;

public class FTPUploadTask extends AsyncTask<File, Void, Boolean> {

	private static String FTP_PARTIE_PATH = "/SchlenkerApp/Partie";
	private static String FTP_ARTICLE_PATH = "/SchlenkerApp/Article";


	String host;
	String username;
	String password;
	String conn_type;
	Integer port;

	private FTPClient client;

	String procedure;
	SharedPreferences settings;
	Activity context;

	public FTPUploadTask(Activity context, String procedure) {
		this.context = context;
		this.procedure = procedure;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		client = new FTPClient();
		client.setBufferSize(4 * 1024);
	}

	@Override
	protected Boolean doInBackground(File... files) {
		extractPreferences();

		if (FTPUtil.ftpConnect(host, username, password, port)) {
			for (File file : files) {
				if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
					uploadPartie(file);
				} else if (Constants.PROCEDURE_ARTICLE.equals(procedure)) {
					uploadArticle(file);
				}

			}
		} else {
			for (File file : files) {
				startPendingProcedure(file);
				return false;
			}
		}

		FTPUtil.ftpDisconnect();

		return null;
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			
		}
		else{
			Toast.makeText(context, "Fehler FTP upload verschiebe in pending", Toast.LENGTH_SHORT).show();
		}
	}
	private void startPendingProcedure(File file) {
		File pending;

		if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
			pending = new File(context.getFilesDir() + Constants.PARTIE_PENDING, file.getName());
		}else
		{
			pending = new File(context.getFilesDir() + Constants.ARTICLE_PENDING, file.getName());
		}
		try {
			UtilFile.copyFile(file, pending);
		} catch (IOException e) {
			//TODO FATAL HANDLING
			e.printStackTrace();
		}
		
		
	}

	private void uploadArticle(File file) {
		FTPUtil.ftpMakeDirectory(FTP_ARTICLE_PATH);

		while (FTPUtil.ftpCheckFileExists(FTP_ARTICLE_PATH, file.getName())) {

			String increasedFilename = increaseNumber(file);
			String filePath = extractPath(file);

			File renamed = new File(filePath, increasedFilename);
			file.renameTo(renamed);
			file = renamed;

		}
		FTPUtil.ftpUpload(file.getAbsolutePath(), file.getName(), FTP_ARTICLE_PATH);

	}

	private String extractPath(File file) {
		String absolutePath = file.getAbsolutePath();
		String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		return filePath;
	}

	private String increaseNumber(File file) {
		char lastChar = file.getName().charAt(file.getName().length() - 1);
		int lastNumber = Integer.parseInt(String.valueOf(lastChar));
		lastNumber++;

		String original = file.getName();
		StringBuilder increased = new StringBuilder(original);
		increased.setCharAt(original.length() - 1, String.valueOf(lastNumber).charAt(0));

		original = increased.toString();
		return original;
	}

	private void uploadPartie(File file) {
		FTPUtil.ftpMakeDirectory(FTP_PARTIE_PATH);
		FTPUtil.ftpUpload(file.getAbsolutePath(), file.getName(), FTP_PARTIE_PATH);
	}

	private void extractPreferences() {
		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_FTP, Context.MODE_PRIVATE);
		username = settings.getString(context.getResources().getString(R.string.ftp_username), null);
		password = settings.getString(context.getResources().getString(R.string.ftp_password), null);
		host = settings.getString(context.getResources().getString(R.string.ftp_address), null);
		port = Integer.parseInt(settings.getString(context.getResources().getString(R.string.ftp_port), null));

		FTP_PARTIE_PATH = settings.getString(context.getResources().getString(R.string.ftp_partie_folder_path), FTP_PARTIE_PATH);
		FTP_ARTICLE_PATH = settings.getString(context.getResources().getString(R.string.ftp_art_folder_path), FTP_ARTICLE_PATH);

	}

}
