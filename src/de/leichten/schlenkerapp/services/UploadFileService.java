package de.leichten.schlenkerapp.services;

import static utils.Utils.SHARED_PREF_UPLOAD_NAME;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import de.leichten.schlenkerapp.R;

public class UploadFileService extends Service {
	
	String name;
	String username;
	String pass;
	String conn_type;
	String ftp_proxy;
	SharedPreferences settings;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		settings = getSharedPreferences(SHARED_PREF_UPLOAD_NAME,MODE_PRIVATE);
		name= settings.getString(getResources().getString(R.string.ftp_address), null);
		username = settings.getString(getResources().getString(R.string.ftp_username), null);
		pass = settings.getString(getResources().getString(R.string.ftp_password), null);
		conn_type = settings.getString(getResources().getString(R.string.ftp_conntype), null);
		ftp_proxy = settings.getString(getResources().getString(R.string.ftp_proxy), null);
		if(name == null || username == null || pass == null)
			return;
		new UploadFileTask().execute();
	}
	
	private class UploadFileTask extends AsyncTask<Void, Void, Boolean>{
		
		
		private FTPClient client;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			client = new FTPClient();
			client.setBufferSize(4*1024);
		}


		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				client.connect(name);
				client.login(username, pass);
			} catch (SocketException e) {
				try {
					client.disconnect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				try {
					client.disconnect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
			
			return null;
		}
		
		
	}
	
	

}
