package main;

import utils.SaveFile;
import utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import provider.FileContentProvider;
import android.graphics.BitmapFactory;

import de.leichten.schlenkerapp.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class PartieActivity extends Activity {

	private static final int REQUEST_CODE = 100;

	private ImageView imageView;
	private Uri fileUri;
	private final String Tag = getClass().getName();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.partie, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partie);
		imageView = (ImageView) findViewById(R.id.imageView1);
		takeAPicture();
	}

	private void takeAPicture() {

		PackageManager pm = getPackageManager();

		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

			Intent i = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(MediaStore.EXTRA_OUTPUT, FileContentProvider.CONTENT_URI);
			startActivityForResult(i, REQUEST_CODE);

		} else {
			Toast.makeText(getBaseContext(), "Camera is not available",	Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.i(Tag, "Receive the camera result");

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

			File out = new File(getFilesDir(), "newImage.jpg");

			if (!out.exists()) {
				Toast.makeText(getBaseContext(),"Error while capturing image", Toast.LENGTH_LONG).show();
				return;
			}
			Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
			imageView.setImageBitmap(mBitmap);

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		imageView = null;

	}

}
