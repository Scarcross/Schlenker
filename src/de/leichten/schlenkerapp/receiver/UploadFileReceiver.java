package de.leichten.schlenkerapp.receiver;

import de.leichten.schlenkerapp.R;
import android.content.BroadcastReceiver;
import static utils.Utils.SHARED_PREF_UPLOAD_NAME;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class UploadFileReceiver extends BroadcastReceiver {

	private static final String TAG = "UploadFileReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isactive = context.getSharedPreferences(SHARED_PREF_UPLOAD_NAME, 0).getBoolean(context.getResources().getString(R.string.ftp_activate), false);
		if (isactive) {
		
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			Log.i(TAG, "Switched to " + mWifi.toString());
			if (mWifi.isConnected()) {
				Intent serviceStarter = new Intent("de.leichten.schlenkerapp.services.UploadFileService");
				serviceStarter.setClass(context, UploadFileReceiver.class);
				context.startService(serviceStarter);
			} else if (false) {

			}
		}
	}

}
