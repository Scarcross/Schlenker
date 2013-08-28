package utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class Utils {
	
	public static final String SHARED_PREF_UPLOAD_NAME = "SCHLENKERAPP_UPLOAD_SETTINGS";
	
	public static final String SHARED_PREF_NAME = "SCHLENKERAPP_SETTINGS";
		
	
	public static boolean isIntentAvailable(Context context, String action){
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =  packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	

	
}
