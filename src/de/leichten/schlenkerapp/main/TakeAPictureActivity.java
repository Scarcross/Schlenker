package de.leichten.schlenkerapp.main;

import java.io.File;

import utils.BitmapHelpers;
import utils.BitmapHelpers.BitmapMemoryException;
import utils.Constants;
import utils.Dialogs;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.imagehandling.MemoryCache;
import de.leichten.schlenkerapp.provider.FileContentProvider;

public class TakeAPictureActivity extends Activity {

	private static final String CAM_CALLED = "CAM_CALLED";
	private static final int REQUEST_CODE = 100;
	private final String Tag = getClass().getName();

	private Bundle savedInstanceState;
	private ImageView imageView;

	private File lastPic;

	

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
			Toast.makeText(getBaseContext(), "Keine Kamera Anwendung gefunden!", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.i(Tag, "Receive the camera result");

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			lastPic = new File(getFilesDir(), "newImage.jpg");

			if (!lastPic.exists()) {
				Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
				return;
			}
			
			try {
				resizeImage();
			} catch (BitmapMemoryException e) {
				Dialogs.getOutOfMemory(this).show();
				e.printStackTrace();
			}
			
			if (savedInstanceState != null) {
				savedInstanceState.clear();
			}
			
			startBarcodeActivity();

		} else if (resultCode == RESULT_CANCELED) {
			deleteRecentPicture();
			backToMainScreen();
		}

	}

	private void startBarcodeActivity() {
		Intent intent = new Intent(this, TakeBarcodeActivity.class);
		getAndAddProcedure(intent);
		startActivity(intent);
		finish();
	}

	private void resizeImage() throws BitmapMemoryException {
		Bitmap decodedAndResizedFile = BitmapHelpers.decodeAndResizeFile(lastPic);
		BitmapHelpers.compressBitmap(decodedAndResizedFile, lastPic);
		MemoryCache.getInstance().put(lastPic.getAbsolutePath(), decodedAndResizedFile);
	}

	private void backToMainScreen() {
		Intent intent = new Intent(this, MainMenue.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra(Constants.MAIN_CALLED, true);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		finish();
	}



	private void getAndAddProcedure(Intent intent) {
		Intent reveicedIntent = getIntent();
		Object object = reveicedIntent.getExtras().get(Constants.PROCEDURE_PARTIE_OR_ARTICLE);
		if (object != null) {
			if (Constants.PROCEDURE_PARTIE.equals(object)) {
				intent.putExtra(Constants.PROCEDURE_PARTIE_OR_ARTICLE, Constants.PROCEDURE_PARTIE);
			}
			if (Constants.PROCEDURE_ARTICLE.equals(object)) {
				intent.putExtra(Constants.PROCEDURE_PARTIE_OR_ARTICLE, Constants.PROCEDURE_ARTICLE);
			} else {
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

	@Override
	protected void onPause() {
		super.onPause();
		imageView.setImageResource(0);
	}

}
