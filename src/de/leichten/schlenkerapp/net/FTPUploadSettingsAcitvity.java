package de.leichten.schlenkerapp.net;

import android.os.Bundle;
import static utils.Utils.SHARED_PREF_UPLOAD_NAME;
import android.preference.PreferenceActivity;
import de.leichten.schlenkerapp.R;


public class FTPUploadSettingsAcitvity extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(SHARED_PREF_UPLOAD_NAME);
		addPreferencesFromResource(R.xml.ftppreferences);
		
	}
	
	
	

}
