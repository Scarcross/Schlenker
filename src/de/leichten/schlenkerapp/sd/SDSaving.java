package de.leichten.schlenkerapp.sd;

import java.io.File;
import java.io.IOException;

import utils.Constants;
import utils.UtilFile;
import utils.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;
import de.leichten.schlenkerapp.R;

public class SDSaving {

	private static String SD_PARTIE_PATH = "/SchlenkerApp/Partie";
	private static String SD_ARTICLE_PATH = "/SchlenkerApp/Article";

	File path;
	Context context;
	String procedure;

	SharedPreferences settings;

	public SDSaving(Context context, String procedure) {
		this.context = context;
		this.procedure = procedure;

	}

	public boolean saveSD(File... files) {
		extractPreferences();

		// Check for SD Card
		if (!Utils.checkSDCardAvailable()) {
			Toast.makeText(context, "Keine SD Karte gefunden!", Toast.LENGTH_SHORT).show();
		} else {
			for (File file : files) {
				if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
					copyPartie(file);
				} else if (Constants.PROCEDURE_ARTICLE.equals(procedure)) {
					copyArticle(file);
				}

			}

		}
		return true;
	}

	private void copyArticle(File file) {
		// Locate the image folder in your SD Card
		path = new File(Environment.getExternalStorageDirectory() + File.separator + SD_ARTICLE_PATH);
		// Create a new folder if no folder named SchlenkerImages exist
		path.mkdirs();
		copyFile(file);

	}

	private void copyPartie(File file) {
		path = new File(Environment.getExternalStorageDirectory() + File.separator + SD_PARTIE_PATH);
		// Create a new folder if no folder named SchlenkerImages exist
		path.mkdirs();
		copyFile(file);

	}

	private void copyFile(File file) {
		try {
			UtilFile.copyFile(file, new File(path, file.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractPreferences() {
		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_FTP, Context.MODE_PRIVATE);

		SD_PARTIE_PATH = settings.getString(context.getResources().getString(R.string.sd_partie_folder_path), SD_PARTIE_PATH);
		SD_ARTICLE_PATH = settings.getString(context.getResources().getString(R.string.sd_art_folder_path), SD_ARTICLE_PATH);

	}

}
