package de.leichten.schlenkerapp.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;

public class UploadFileReceiver extends BroadcastReceiver {

	private static final String TAG = "UploadFileReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		NetworkInfo info = extras.getParcelable("networkInfo");
		State state = info.getState();
		Log.i(TAG, "Switched to " + state.toString());
		if(state == State.CONNECTED){
			Intent serviceStarer = new Intent("de.leichten.schlenkerapp.services.UploadFileService");
			serviceStarer.setClass(context, UploadFileReceiver.class);
			context.startService(serviceStarer);
			
		}else if(state == State.CONNECTING){
			
		}else if (state == State.DISCONNECTED){
			
		}
	}

}
