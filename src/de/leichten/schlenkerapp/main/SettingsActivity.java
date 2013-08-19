package de.leichten.schlenkerapp.main;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.net.FTPUploadChooseAcitvity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SettingsActivity extends PreferenceActivity {

	
	private static Preference partie_ftp_up;
	private static Preference partie_delete_sd;
	private static Preference partie_upload_network;
	private static Preference partie_copy_sd;
	private static Preference art_ftp_up;
	private static Preference art_delete_sd;
	private static Preference art_upload_network;
	private static Preference art_copy_sd;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.preferenceactivity);
		ListView list = (ListView) findViewById(android.R.id.list);
		// Change GUI
		list.setDivider(new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.limegreen),Color.WHITE,getResources().getColor(R.color.limegreen)}));
		list.setDividerHeight(4);
		
		partie_ftp_up = findPreference(getResources().getString(R.string.key_partie_ftp_upload));
		partie_ftp_up.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getBaseContext(), FTPUploadChooseAcitvity.class);
				preference.setIntent(i);
				return false;
			}
		});
		partie_delete_sd = findPreference(getResources().getString(R.string.key_partie_delete_pictures));
		partie_delete_sd.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				preference.setIntent(new Intent(getBaseContext(),FTPUploadChooseAcitvity.class));
				return false;
			}
		});
		
		partie_copy_sd = findPreference(getResources().getString(R.string.key_partie_copy_sd));
		
		partie_upload_network = findPreference(getResources().getString(R.string.key_partie_upload_network));
		
		art_ftp_up = findPreference(getResources().getString(R.string.key_art_ftp_upload));
		
		art_upload_network = findPreference(getResources().getString(R.string.key_art_upload_network));
		
		art_delete_sd = findPreference(getResources().getString(R.string.key_art_delete_pictures));
		
		art_copy_sd = findPreference(getResources().getString(R.string.key_art_copy_sd));
		
	}



}
