package de.leichten.schlenkerapp.main;

import java.util.List;

import utils.Utils;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.R.layout;
import de.leichten.schlenkerapp.R.menu;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;

public class PartieActivity extends Activity {

	static int ACTION_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partie);

		takeAPicture();
	}

	private void takeAPicture() {
		if (Utils.isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)){
			
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, ACTION_CODE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partie, menu);
		return true;
	}

}
