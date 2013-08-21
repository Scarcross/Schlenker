package de.leichten.schlenkerapp.test;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import de.leichten.schlenkerapp.receiver.UploadFileReceiver;

public class UploadReceiverTest extends AndroidTestCase {
	
	private UploadFileReceiver mReceiver;
	private NetworkInfo info;
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        mReceiver = new UploadFileReceiver();
        ConnectivityManager man = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        this.info = man.getActiveNetworkInfo();
    }
    
    @Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConnectedReceiver()
    {
        Intent intent = new Intent(getContext(),UploadFileReceiver.class);
        intent.putExtra("networkInfo",info);
        mReceiver.onReceive(getContext(), intent);        
        assertNull(mReceiver.getResultData());

    }
	
	
	

}
