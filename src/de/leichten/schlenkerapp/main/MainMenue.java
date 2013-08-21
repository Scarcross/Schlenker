package de.leichten.schlenkerapp.main;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.R.layout;
import de.leichten.schlenkerapp.R.menu;
import de.leichten.schlenkerapp.barcode.QRActivity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import de.leichten.schlenkerapp.R;

public class MainMenue extends Activity {

	private static final String TAG = "MainMenue";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menue);
		int orientation = getScreenOrientation();
		if (isHorizontalOriented(orientation))
			startAnimation();
	}

	@Override
	protected void onResume() {
		int orientation = getScreenOrientation();
		if (isHorizontalOriented(orientation))
			startAnimation();
		super.onResume();
	}

	private boolean isHorizontalOriented(int orientation) {
		return (orientation == 1 || orientation == 9);
	}

	private int getScreenOrientation() {
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (rotation == Surface.ROTATION_0
					|| rotation == Surface.ROTATION_270) {
				return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			} else {
				return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			}
		}
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (rotation == Surface.ROTATION_0
					|| rotation == Surface.ROTATION_90) {
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
			intent = new Intent(this, PartieActivity.class);
			break;
		case R.id.button_artikel:
			break;
		case R.id.button_letzteVorgaenge:
			intent = new Intent(this, QRActivity.class);
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
