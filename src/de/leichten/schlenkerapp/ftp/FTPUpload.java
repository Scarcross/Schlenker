package de.leichten.schlenkerapp.ftp;

import java.io.File;
import java.io.IOException;

import utils.Constants;
import utils.UtilFile;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.sd.SDDeleteFileTask;
import de.leichten.schlenkerapp.sd.SDSaving;

public class FTPUpload {

	private static String FTP_PARTIE_PATH = "/SchlenkerApp/Partie";
	private static String FTP_ARTICLE_PATH = "/SchlenkerApp/Article";

	String host;
	String username;
	String password;
	String conn_type;
	Integer port;
	
	boolean fromPending;
	boolean increasing;

	String procedure;
	SharedPreferences settings;
	Activity context;

	public FTPUpload(Activity context, String procedure, boolean fromPending, boolean increasing) {
		this.context = context;
		this.procedure = procedure;
		this.fromPending = fromPending;
		this.increasing = increasing;

	}

	public boolean uploadFiles(File... files) {
		extractPreferences();
		if (FTPUtil.ftpConnect(host, username, password, port)) {
			try {
				decideAndUploadCopy(files);
				removeFromSD(files);
			} catch (IOException e) {
				startPendingProcedure(files);
				e.printStackTrace();
			}
		} else {
			startPendingProcedure(files);
			return false;

		}
		FTPUtil.ftpDisconnect();

		return true;
	}

	private void removeFromSD(File... files) {
		SharedPreferences prefsMain = context.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);
		if (prefsMain.getBoolean(context.getResources().getString(R.string.key_partie_delete_pictures), false)) {
			new SDDeleteFileTask(context).execute(files);
		}
	}

	private void decideAndUploadCopy(File... files) throws IOException {
		for (File file : files) {
			if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
				uploadPartie(file);
			} else if (Constants.PROCEDURE_ARTICLE.equals(procedure)) {
				uploadArticle(file);
			}
				decideSDCopy(file);
			if (fromPending) {
				deletePending(file);
			}
		}
	}

	private void decideSDCopy(File file) {
		SharedPreferences prefsMain = context.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		if (prefsMain.getBoolean(context.getResources().getString(R.string.key_art_copy_sd), false)) {
			new SDSaving(this.context, this.procedure).saveSD(file.getAbsoluteFile());
		}
	}

	private void deletePending(File file) {
		file.delete();
	}

	private void startPendingProcedure(File[] files) {
		for (File file : files) {
			File pending;

			if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
				pending = new File(context.getFilesDir() + Constants.PARTIE_UPLOAD_PENDING, file.getName());
			} else {
				pending = new File(context.getFilesDir() + Constants.ARTICLE_UPLOAD_PENDING, file.getName());
			}
			try {
				pending.getParentFile().mkdirs();
				UtilFile.copyFile(file, pending);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void uploadArticle(File file) throws IOException {
		try {
			FTPUtil.ftpCreateDirectoryTree(FTP_ARTICLE_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (increasing) {

			while (FTPUtil.ftpCheckFileExists(FTP_ARTICLE_PATH, file.getName())) {
				String increasedFilename = increaseNumber(file);
				String filePath = extractPath(file);
				File renamed = new File(filePath, increasedFilename);
				file.renameTo(renamed);
				file = renamed;
			}
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

	private void uploadPartie(File file) throws IOException {
		try {
			FTPUtil.ftpCreateDirectoryTree(FTP_PARTIE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FTPUtil.ftpUpload(file.getAbsolutePath(), file.getName(), FTP_PARTIE_PATH);
	}

	private void extractPreferences() {
		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_FTP, Context.MODE_PRIVATE);
		username = settings.getString(context.getResources().getString(R.string.ftp_username), null);
		password = settings.getString(context.getResources().getString(R.string.ftp_password), null);
		host = settings.getString(context.getResources().getString(R.string.ftp_address), null);
		port = Integer.parseInt(settings.getString(context.getResources().getString(R.string.ftp_port), "21"));

		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		FTP_PARTIE_PATH = settings.getString(context.getResources().getString(R.string.ftp_partie_folder_path), FTP_PARTIE_PATH);
		FTP_ARTICLE_PATH = settings.getString(context.getResources().getString(R.string.ftp_art_folder_path), FTP_ARTICLE_PATH);

	}

}
