package de.leichten.schlenkerapp.preferences;

import java.util.Map;
import java.util.Set;

import utils.Constants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.ListView;
import de.leichten.schlenkerapp.R;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {


	private Preference pref;
	String prefixStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getPreferenceManager().setSharedPreferencesName(Constants.SHARED_PREF_NAME_MAIN);
		addPreferencesFromResource(R.xml.preferences);

		ListView list = (ListView) findViewById(android.R.id.list);

		// Change divider of standard gui
		list.setDivider(new ColorDrawable(Color.GRAY));
		list.setDividerHeight(4);

		Preference ftp_settings = findPreference(getResources().getString(R.string.key_ftp_settings));
		ftp_settings.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getBaseContext(), FTPUploadSettingsAcitvity.class);
				preference.setIntent(i);
				return false;
			}
		});
	
		

		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
		loadSummaries(sharedPref);
		sharedPref.registerOnSharedPreferenceChangeListener(this);
	}

	private void loadSummaries(SharedPreferences sharedPref) {
		Map<String, ?> all = sharedPref.getAll();
		Set<String> keySet = all.keySet();
		for (String key : keySet) {
			pref = findPreference(key);
			if (pref != null) {
				try {
					String value = sharedPref.getString(key, "Summary");
					pref.setSummary(value);					
				} catch (ClassCastException e) {
					// TODO: handle exception
				}
			}

		}
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Get the current summary
		pref = findPreference(key);
		// Get the user input data
		try {
			prefixStr = sharedPreferences.getString(key, "");
			// Update the summary with user input data
			pref.setSummary(prefixStr);
			
		} catch (ClassCastException e) {
			// TODO: handle exception
		}
	}

}
