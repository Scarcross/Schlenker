package de.leichten.schlenkerapp.ftp;

import de.leichten.schlenkerapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import utils.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUploadTask extends AsyncTask<File, Void, Void> {

	String host;
	String username;
	String password;
	String conn_type;
	Integer port;

	private FTPClient client;

	SharedPreferences settings;
	Context context;

	public FTPUploadTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		client = new FTPClient();
		client.setBufferSize(4 * 1024);
	}

	@Override
	protected Void doInBackground(File... files) {

		extractPreferences();
		if (FTPUtil.ftpConnect(host, username, password, port)){
			for (File file : files) {
				FTPUtil.ftpGetCurrentWorkingDirectory();
				FTPUtil.ftpChangeDirectory("SchlenkerApp");
			}
		}
		
		FTPUtil.ftpDisconnect();
		
		// //TODO check remote existing File and increase Filename +1
		// FileInputStream fs = new FileInputStream(file);
		// String fileName = file.getName();
		// Boolean result = client.storeFile(ftp_proxy + fileName, fs);

		return null;
	}

	private void extractPreferences() {

		settings = context.getSharedPreferences(Utils.SHARED_PREF_NAME,	Context.MODE_PRIVATE);
		
		username = settings.getString(context.getResources().getString(R.string.ftp_username), null);
		password = settings.getString(context.getResources().getString(R.string.ftp_password), null);
		host = settings.getString(context.getResources().getString(R.string.ftp_address), null);
		port = Integer.parseInt(settings.getString(context.getResources().getString(R.string.ftp_port), null));
	}

}
