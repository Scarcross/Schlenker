package de.leichten.schlenkerapp.main;

import java.io.File;

import utils.BitmapHelpers;
import utils.Constants;
import utils.Utils;

import android.graphics.BitmapFactory;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.provider.FileContentProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class TakeAPictureActivity extends Activity {

	private static final String CAM_CALLED = "CAM_CALLED";
	private static final int REQUEST_CODE = 100;
	private final String Tag = getClass().getName();

	private Bundle savedInstanceState;
	private ImageView imageView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.partie, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);

		imageView = (ImageView) findViewById(R.id.imageView1);

		if (savedInstanceState != null) {
			if (!savedInstanceState.getBoolean(CAM_CALLED)) {
				takeAPicture();
			}

		} else {
			takeAPicture();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(CAM_CALLED, true);
	}

	private void takeAPicture() {

		PackageManager pm = getPackageManager();

		
		
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(MediaStore.EXTRA_OUTPUT, FileContentProvider.CONTENT_URI);
			startActivityForResult(i, REQUEST_CODE);

		} else {
			Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.i(Tag, "Receive the camera result");

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

			File out = new File(getFilesDir(), "newImage.jpg");

			if (!out.exists()) {
				Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
				return;
			}
			//setze verkleinerte variante in imageview (könnte man eigentlich auch gleich verarbeiten)
			Bitmap mBitmap = BitmapHelpers.decodeAndResizeFile(out);
			imageView.setImageBitmap(mBitmap);

		} else if (resultCode == RESULT_CANCELED) {
			backToMainScreen();
		}

	}

	private void backToMainScreen() {
		Intent intent = new Intent(this, MainMenue.class);
		startActivity(intent);
		finish();
	}

	public void buttonClicked(final View view) {
		if(savedInstanceState != null){
			savedInstanceState.clear();
		}
		Intent intent = null;

		int id = view.getId();

		switch (id) {
		case R.id.btn_pictureOk:
			intent = new Intent(this, TakeBarcodeActivity.class);
			getAndAddProcedure(intent);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_pictureBad:
			if (deleteRecentPicture()) {
				Toast.makeText(this, "Bild verworfen", Toast.LENGTH_SHORT).show();
			}
			finish();
			backToMainScreen();
			break;
		default:
			break;
		}

	}

	private void getAndAddProcedure(Intent intent) {
		Intent reveicedIntent = getIntent();
		Object object = reveicedIntent.getExtras().get(Constants.PROCEDURE_PARTIE_OR_ARTICLE);
		if(object != null){
			if (Constants.PROCEDURE_PARTIE.equals(object)){
				intent.putExtra(Constants.PROCEDURE_PARTIE_OR_ARTICLE, Constants.PROCEDURE_PARTIE);
			}
			if (Constants.PROCEDURE_ARTICLE.equals(object)){
				intent.putExtra(Constants.PROCEDURE_PARTIE_OR_ARTICLE, Constants.PROCEDURE_ARTICLE);
			}
			else{
				backToMainScreen();
				finish();
			}
		}
	}

	private boolean deleteRecentPicture() {
		File out = new File(getFilesDir(), "newImage.jpg");
		return out.delete();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		imageView = null;

	}

}
