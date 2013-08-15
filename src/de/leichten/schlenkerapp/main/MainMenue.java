package de.leichten.schlenkerapp.main;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.R.layout;
import de.leichten.schlenkerapp.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainMenue extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menue);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menue, menu);
		return true;
	}

	public void buttonClicked(View view) {
		Intent intent = null;
		
		switch (view.getId()) {
		case R.id.button_partie:
			intent = new Intent(this, PartieActivity.class);
			break;
		case R.id.button_artikel:
			break;
		case R.id.button_letzteVorgaenge:
			break;
		case R.id.button_einstellungen:
			break;
		case R.id.button_beenden:
			break;
		default:
			break;
		}
		if (intent != null)
			startActivity(intent); 
	}
}
