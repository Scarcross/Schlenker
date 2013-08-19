package de.leichten.schlenkerapp.net;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import de.leichten.schlenkerapp.R;

public class FTPUploadChooseAcitvity extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.ftppreferences);
		
	}
	
	
	

}
