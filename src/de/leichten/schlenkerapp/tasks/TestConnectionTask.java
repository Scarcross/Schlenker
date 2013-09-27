package de.leichten.schlenkerapp.tasks;

import utils.Constants;
import utils.Dialogs;
import utils.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.ftp.FTPUtil;

public class TestConnectionTask extends AsyncTask<Void, Void, String> {
	private static String SUCCESS = "SUCCESS";
	private static String NO_WIFI = "NO_WIFI";
	private static String NO_FTP = "NO_FTP";

	public String prefixStr;
	public String host;
	public String username;
	public String password;
	public String conn_type;
	public int port;

	ProgressDialog myPd_ring;
	Activity context;

	public TestConnectionTask(Activity context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		myPd_ring = new ProgressDialog(this.context);
		myPd_ring.setMessage("Testen...");
		myPd_ring.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		return testConnection();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		myPd_ring.dismiss();

		if (SUCCESS.equals(result)) {
			Dialogs.getConnectSuccesDialog(context).show();
		} else if (NO_FTP.equals(result)) {
			Dialogs.getFTPConnectFailedDialog(context).show();
		} else if (NO_WIFI.equals(result)) {
			Dialogs.getWIFIConnectFailedDialog(context).show();
		}

	}

	public String testConnection() {
		extractPreferences();
		if (!Utils.checkWifiConnectivity(this.context)) {
			return NO_WIFI;
		} 
		else {
			if (FTPUtil.ftpConnect(this.host, this.username, this.password, this.port)) {
				return SUCCESS;
			} else {
				return NO_FTP;
			}
		}
	}

	private void extractPreferences() {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME_FTP, Context.MODE_PRIVATE);
		this.username = sharedPreferences.getString(context.getResources().getString(R.string.ftp_username), null);
		this.password = sharedPreferences.getString(context.getResources().getString(R.string.ftp_password), null);
		this.host = sharedPreferences.getString(context.getResources().getString(R.string.ftp_address), null);
		this.port = Integer.parseInt(sharedPreferences.getString(context.getResources().getString(R.string.ftp_port), "21"));
	}
}