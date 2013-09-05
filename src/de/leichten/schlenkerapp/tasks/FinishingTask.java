package de.leichten.schlenkerapp.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import utils.BitmapHelpers;
import utils.Constants;
import utils.UtilFile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.ftp.FTPUploadTask;
import de.leichten.schlenkerapp.main.MainMenue;
import de.leichten.schlenkerapp.main.TakeBarcodeActivity;
import de.leichten.schlenkerapp.sd.SDSavingTask;

public class FinishingTask extends AsyncTask<String, Void, Boolean> {
	
	String procedure;
	TakeBarcodeActivity barcodeActivity;
	
	String partieNummer;
	String artikelNummer;
	String verpackungsEinheit;
	String pruefZiffer;

	File lastPic;

	private final String Tag = getClass().getName();

	public FinishingTask(TakeBarcodeActivity barcodeActivity, String procedure) {
		this.procedure = procedure;
		this.barcodeActivity = barcodeActivity;
	}

	@Override
	protected Boolean doInBackground(String... barcode) {
		organizeHistory();
		
		if (parseBarcode(barcode)) {
			lastPic = new File(barcodeActivity.getFilesDir(), "newImage.jpg");
			// First shrink image to save resources
			resizeImage();
			// Rotate picture from EXIF
			rotatePictureFromEXIF();
			// Rename File depending on the Procedure
			renameFile();

			// Do the upload stuff
			startOtherFileTasks();
			return true;
		}
		else{
			return false;
		}
	}

	private void organizeHistory() {
		//TODO remove bitmap from MemoryCache
		//TODO Delete the oldest picture if the number of max history ist reached.
	}

	/**
	 * Last picture will be shrinked and compressed if possible
	 */

	private void resizeImage() {
		Bitmap decodedAndResizedFile = BitmapHelpers.decodeAndResizeFile(lastPic);
		BitmapHelpers.compressBitmap(decodedAndResizedFile, lastPic);
	}

	private void renameFile() {

		if (Constants.PROCEDURE_PARTIE.equals(this.procedure)) {
			setPartieFilename();
		} else if (Constants.PROCEDURE_ARTICLE.equals(this.procedure)) {
			setArticleFilename();
		}
		showFileList();
	}

	private boolean parseBarcode(String[] barcodes) {
		String barcode = barcodes[0];

		if (barcode.length() == 16) {
			partieNummer = barcode.substring(0, 6);
			artikelNummer = barcode.substring(6, 12);
			verpackungsEinheit = barcode.substring(12, 15);
			pruefZiffer = barcode.substring(15, 16);
			return true;
		}
		else{
			return false;
		}
	}

	private void setPartieFilename() {
		File newFile = new File(barcodeActivity.getFilesDir(), partieNummer);
		lastPic.renameTo(newFile);
		lastPic = newFile;
	}

	private void setArticleFilename() {
		File newFile = new File(barcodeActivity.getFilesDir(), artikelNummer+"_1");
		lastPic.renameTo(newFile);
		lastPic = newFile;

	}

	private void rotatePictureFromEXIF() {
		try {
			ExifInterface exif = new ExifInterface(lastPic.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			if (orientation == ExifInterface.ORIENTATION_NORMAL) {
				// Do nothing. The original image is fine.
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				BitmapHelpers.rotateImage(lastPic.getAbsoluteFile(), 90);

			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				BitmapHelpers.rotateImage(lastPic.getAbsoluteFile(), 180);

			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				BitmapHelpers.rotateImage(lastPic.getAbsoluteFile(), 270);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startOtherFileTasks() {
		//TODO Check WIFI connection
				//TODO copy the files in the pending folder --> if it is full make message!! and do something

		if (Constants.PROCEDURE_PARTIE.equals(this.procedure)) {
			decidePartieTasks();
		} else if (Constants.PROCEDURE_ARTICLE.equals(this.procedure)) {
			decideArticleTasks();
		}
	
	}

	private void decideArticleTasks() {
		SharedPreferences prefsMain = barcodeActivity.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		//Do we have to copy it on SD Card?
		if(prefsMain.getBoolean(barcodeActivity.getResources().getString(R.string.key_art_copy_sd), false)){
					new SDSavingTask(barcodeActivity, this.procedure).execute(lastPic.getAbsoluteFile());
				}
		
		//Do we have to upload it to an FTP?
		if(prefsMain.getBoolean(barcodeActivity.getResources().getString(R.string.key_art_ftp_upload), false)){
			new FTPUploadTask(barcodeActivity, this.procedure).execute(lastPic);
			
			//Do we have to delete the Files after uploading ?
			if(prefsMain.getBoolean(barcodeActivity.getResources().getString(R.string.key_art_delete_pictures), false)){
				new DeleteFileTask().execute(lastPic);
			}
		}
	}

	private void decidePartieTasks() {

		SharedPreferences prefsMain = barcodeActivity.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		//Do we have to copy it on SD Card?
		if(prefsMain.getBoolean(barcodeActivity.getResources().getString(R.string.key_partie_copy_sd), false)){
					new SDSavingTask(barcodeActivity, this.procedure).execute(lastPic.getAbsoluteFile());
				}
		
		//Do we have to upload it to an FTP?
		if(prefsMain.getBoolean(barcodeActivity.getResources().getString(R.string.key_partie_ftp_upload), false)){
			new FTPUploadTask(barcodeActivity, this.procedure).execute(lastPic);
			
			//Do we have to delete the Files after uploading ?
			if(prefsMain.getBoolean(barcodeActivity.getResources().getString(R.string.key_partie_delete_pictures), false)){
				new DeleteFileTask().execute(lastPic);
			}
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		barcodeActivity.getImageView().setImageBitmap(BitmapHelpers.decodeAndResizeFile(lastPic));
		barcodeActivity.getTextView().setText("Bild: "+ lastPic.getName());
		if(result){
			Toast.makeText(barcodeActivity, procedure+" Finishing erfolgreich", Toast.LENGTH_LONG).show();			
		}
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Intent intent = new Intent(barcodeActivity, MainMenue.class);
				barcodeActivity.startActivity(intent);
			}
		}, 3000);
		showFileList();
	}

	private void showFileList() {
		File root = barcodeActivity.getFilesDir();
		File[] fileList = root.listFiles();
		for (File file : fileList) {
			Log.d(Tag, file.getAbsolutePath());
		}
	}

}
