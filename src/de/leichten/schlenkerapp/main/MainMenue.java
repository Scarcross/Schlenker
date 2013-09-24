package de.leichten.schlenkerapp.main;

import utils.Constants;
import utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.preferences.SettingsActivity;
import de.leichten.schlenkerapp.tasks.StartupPendingTask;
import de.leichten.schlenkerapp.tasks.TestConnectionTask;

public class MainMenue extends Activity {

	private static final String TAG = "MainMenue";
	private static final String MAIN_CALLED = "MAIN_CALLED";

	private boolean wasCalled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menue);

		PreferenceManager.setDefaultValues(this, Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE, R.xml.preferences, false);
		PreferenceManager.setDefaultValues(this, Constants.SHARED_PREF_NAME_FTP, Context.MODE_PRIVATE, R.xml.ftp_preferences, false);


		int orientation = getScreenOrientation();
		if (isHorizontalOriented(orientation)) {
			startAnimation();
		}
	}

	@Override
	protected void onResume() {
		if (!wasCalled) {
			new TestConnectionTask(this).execute();
			new StartupPendingTask(this).execute();
			wasCalled = true;

		}
		
		int orientation = getScreenOrientation();
		if (isHorizontalOriented(orientation))
			startAnimation();
		super.onResume();
	}
	@Override
	protected void onPause() {
		this.wasCalled = true;
		super.onPause();
	}
	
	private boolean isHorizontalOriented(int orientation) {
		return (orientation == 1 || orientation == 9);
	}

	private int getScreenOrientation() {
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
				return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			} else {
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			}
		}
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
				return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			} else {
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			}
		}
		return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menue, menu);
		return true;
	}

	public void buttonClicked(final View view) {
		Intent intent = null;
		int id = view.getId();

		switch (id) {
		case R.id.button_partie:
			intent = new Intent(this, TakeAPictureActivity.class);
			intent.putExtra(Constants.PROCEDURE_PARTIE_OR_ARTICLE, Constants.PROCEDURE_PARTIE);
			break;
		case R.id.button_artikel:
			intent = new Intent(this, TakeAPictureActivity.class);
			intent.putExtra(Constants.PROCEDURE_PARTIE_OR_ARTICLE, Constants.PROCEDURE_ARTICLE);
			break;

		case R.id.button_letzteVorgaenge:
			intent = new Intent(this, ImagesActivity.class);

			break;
		case R.id.button_einstellungen:
			intent = new Intent(this, SettingsActivity.class);
			break;
		case R.id.button_beenden:
			finish();
			break;
		default:
			break;
		}
		if (intent != null)
			startActivity(intent);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return super.onRetainNonConfigurationInstance();
	}

	private void startAnimation() {

		View main_back = findViewById(R.id.main_menu_back);
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		main_back.startAnimation(fadeIn);
	}

}
