package de.leichten.schlenkerapp.tasks;

import java.io.File;

import utils.Utils;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.ftp.FTPUploadTask;
import android.content.Context;
import android.content.SharedPreferences;

public class FileHandlingTasksStarter {

	public FileHandlingTasksStarter(Context context, File... files) {
		
		SharedPreferences sharedPref = context.getSharedPreferences(Utils.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		
		//Start SDSaving task
		if(sharedPref.getBoolean(context.getResources().getString(R.string.key_partie_copy_sd), false)){
			new SDSavingTask(context).execute(files);
		}
		
		//Start FTPUpload task
		if(sharedPref.getBoolean(context.getResources().getString(R.string.key_partie_ftp_upload), false)){
			new FTPUploadTask(context).execute(files);
		}
		
		if(sharedPref.getBoolean(context.getResources().getString(R.string.key_partie_delete_pictures), false)){
			new DeleteFileTask().execute(files);
		}
		
	}
	
}
