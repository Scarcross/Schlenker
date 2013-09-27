package de.leichten.schlenkerapp.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import utils.BitmapHelpers;
import utils.BitmapHelpers.BitmapMemoryException;
import utils.Constants;
import utils.Dialogs;
import utils.UtilFile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.widget.Toast;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.imagehandling.MemoryCache;
import de.leichten.schlenkerapp.main.MainMenue;
import de.leichten.schlenkerapp.main.TakeBarcodeActivity;

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
		lastPic = new File(barcodeActivity.getFilesDir(), "newImage.jpg");

		if (parseBarcode(barcode)) {

			try {
				rotatePictureFromEXIF();
			} catch (BitmapMemoryException e) {
				Dialogs.getOutOfMemory(barcodeActivity).show();
				e.printStackTrace();
			}
			renameFile();

			new FileHandlingStartDecideTask(lastPic, procedure, barcodeActivity, false, true).execute();
			return true;
		} else {
			return false;
		}
	}

	private void organizeHistory() {
		while (getCountMaxHistoryPreferences() < UtilFile.getCountHistoryDir(barcodeActivity)) {
			removeOldestEntry();
		}
	}

	private void removeOldestEntry() {
		File oldestFile = UtilFile.getOldestFile(barcodeActivity);
		if (oldestFile != null) {
			MemoryCache memoryCache = MemoryCache.getInstance();
			memoryCache.remove(oldestFile.getAbsolutePath());
			oldestFile.delete();
		}
	}

	private int getCountMaxHistoryPreferences() {
		SharedPreferences settings = barcodeActivity.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);
		 int parseInt = Integer.parseInt(settings.getString(barcodeActivity.getResources().getString(R.string.maxHistory), null));
		 return parseInt;
	}

	

	private void renameFile() {
		String oldName = lastPic.getAbsolutePath();
		
		if (Constants.PROCEDURE_PARTIE.equals(this.procedure)) {
			setPartieFilename();
		} else if (Constants.PROCEDURE_ARTICLE.equals(this.procedure)) {
			setArticleFilename();
		}
		MemoryCache.getInstance().rename(oldName, lastPic.getAbsolutePath());
	}

	private boolean parseBarcode(String[] barcodes) {
		String barcode = barcodes[0];
		if (barcode.length() == 16) {
			partieNummer = barcode.substring(0, 6);
			artikelNummer = barcode.substring(6, 12);
			verpackungsEinheit = barcode.substring(12, 15);
			pruefZiffer = barcode.substring(15, 16);
			return true;
		} else {
			return false;
		}
	}

	private void setPartieFilename() {
		File newFile = new File(barcodeActivity.getFilesDir(), partieNummer);
		lastPic.renameTo(newFile);
		lastPic = newFile;
	}

	private void setArticleFilename() {
		File[] artFiles = barcodeActivity.getFilesDir().listFiles();
		int increasedNumber = 1;

		for (File file : artFiles) {
			if (file.isFile()) {
				String filename = file.getName();

				int upperIndex = filename.indexOf("_");
				if (upperIndex == -1)
					continue;

				String artikelNummerTemp = filename.substring(0, upperIndex);

				if (artikelNummer.equals(artikelNummerTemp)) {
					char charAt = filename.charAt(filename.length() - 1);
					int parseInt = Integer.parseInt(charAt + "");
					parseInt++;
					if (parseInt > increasedNumber)
						increasedNumber = parseInt;
				}
			}
		}

		File newFile = new File(barcodeActivity.getFilesDir(), artikelNummer + "_" + String.valueOf(increasedNumber));

		lastPic.renameTo(newFile);
		lastPic = newFile;

	}

	private void rotatePictureFromEXIF() throws BitmapMemoryException {
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

	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			barcodeActivity.getImageView().setImageBitmap(MemoryCache.getInstance().get(lastPic.getAbsolutePath()));
			barcodeActivity.getTextView().setText("Bild: " + lastPic.getName());			
			Toast.makeText(barcodeActivity, "Finishing erfolgreich", Toast.LENGTH_LONG).show();
		}
		else {
			barcodeActivity.getTextView().setText("...");			
			Toast.makeText(barcodeActivity, "Finishing Fehler vermutlich invalider Barcode.. wird gelöscht", Toast.LENGTH_LONG).show();
			lastPic.delete();
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Intent intent = new Intent(barcodeActivity, MainMenue.class);
			    intent.addCategory(Intent.CATEGORY_HOME);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    intent.putExtra(Constants.MAIN_CALLED, true);
			    barcodeActivity.startActivity(intent);
				barcodeActivity.finish();
			}
		}, 3000);
		UtilFile.getCountHistoryDir(barcodeActivity);
	}

	

}
