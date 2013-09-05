package de.leichten.schlenkerapp.preferences;

import utils.Constants;
import utils.Utils;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import de.leichten.schlenkerapp.R;


public class FTPUploadSettingsAcitvity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	private Preference pref;
	String prefixStr;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Constants.SHARED_PREF_NAME_FTP);
		addPreferencesFromResource(R.xml.ftp_preferences);
		
		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
		sharedPref.registerOnSharedPreferenceChangeListener(this);
		
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Get the current summary
		pref = findPreference(key);
		
		// Get the user input data
		prefixStr = sharedPreferences.getString(key, "");

		// Update the summary with user input data
		pref.setSummary(prefixStr);
	}

	

}
