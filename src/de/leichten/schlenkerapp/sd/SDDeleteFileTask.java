package de.leichten.schlenkerapp.sd;

import java.io.File;

import de.leichten.schlenkerapp.R;

import utils.Constants;
import utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class SDDeleteFileTask extends AsyncTask<File, Void, Boolean> {

	private static String SD_PARTIE_PATH = "/SchlenkerApp/Partie";
	private static String SD_ARTICLE_PATH = "/SchlenkerApp/Article";

	File path;

	String procedure;
	SharedPreferences settings;
	Activity context;

	public SDDeleteFileTask(Activity context) {
		this.context = context;

	}

	@Override
	protected Boolean doInBackground(File... files) {
		extractPreferences();

		if (!Utils.checkSDCardAvailable()) {
			Toast.makeText(context, "Keine SD Karte gefunden!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			for (File file : files) {
				parseProcedure(file.getName());
				if (Constants.PROCEDURE_PARTIE.equals(procedure)) {
					deleteSDPartie(file);
				} else if (Constants.PROCEDURE_ARTICLE.equals(procedure)) {
					deleteSDArticle(file);
				}

			}
		}
		return null;
	}

	private void parseProcedure(String file) {
		// TODO this is not really reliable
		if (file.contains("_")) {
			this.procedure = Constants.PROCEDURE_ARTICLE;
		} else {
			this.procedure = Constants.PROCEDURE_PARTIE;
		}
	}
	
	private void deleteSDPartie(File file) {
		path = new File(Environment.getExternalStorageDirectory() + File.separator + SD_PARTIE_PATH + File.separator + file.getName());
		path.getAbsoluteFile().mkdirs();
		path.delete();
	}

	private void deleteSDArticle(File file) {
		path = new File(Environment.getExternalStorageDirectory() + File.separator + SD_ARTICLE_PATH + File.separator + file.getName());
		path.getAbsoluteFile().mkdirs();
		path.delete();
	}

	private void extractPreferences() {

		settings = context.getSharedPreferences(Constants.SHARED_PREF_NAME_MAIN, Context.MODE_PRIVATE);

		SD_PARTIE_PATH = settings.getString(context.getResources().getString(R.string.sd_partie_folder_path), SD_PARTIE_PATH);
		SD_ARTICLE_PATH = settings.getString(context.getResources().getString(R.string.sd_art_folder_path), SD_ARTICLE_PATH);

	}
}
