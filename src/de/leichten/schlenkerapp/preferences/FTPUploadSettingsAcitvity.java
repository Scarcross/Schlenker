package de.leichten.schlenkerapp.preferences;

import java.util.Map;
import java.util.Set;

import utils.Constants;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.tasks.TestConnectionTask;

public class FTPUploadSettingsAcitvity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	public Preference pref;
	
	public String prefixStr;
	public String host;
	public String username;
	public String password;
	public String conn_type;
	public int port;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(Constants.SHARED_PREF_NAME_FTP);
		addPreferencesFromResource(R.xml.ftp_preferences);

		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
		loadSummaries(sharedPref);
		sharedPref.registerOnSharedPreferenceChangeListener(this);

		new TestConnectionTask(this).execute();
	}

	private void loadSummaries(SharedPreferences sharedPref) {
		Map<String, ?> all = sharedPref.getAll();
		Set<String> keySet = all.keySet();
		for (String key : keySet) {
			pref = findPreference(key);
			if (pref != null) {
				String value = sharedPref.getString(key, "Summary");
				pref.setSummary(value);
			}

		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Get the current summary
		pref = findPreference(key);
		// Get the user input data
		prefixStr = sharedPreferences.getString(key, "");
		// Update the summary with user input data
		pref.setSummary(prefixStr);
		new TestConnectionTask(this).execute();
	}

	
}
