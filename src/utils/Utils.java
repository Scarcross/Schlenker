package utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;

public class Utils {

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	// Check WIFI connectivity
	public static boolean checkWifiConnectivity(Context context) {

		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!mWifi.isConnected()) {
			return false;
		}
		return true;
	}

	public static boolean checkSDCardAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static double getFreeHeapMemory() {
		double max = Runtime.getRuntime().maxMemory()/1024; 
		double heapSize = Runtime.getRuntime().totalMemory()/1024;
		double heapRemaining = Runtime.getRuntime().freeMemory()/1024;	
		double nativeUsage = Debug.getNativeHeapAllocatedSize()/1024;

		// heapSize - heapRemaining = heapUsed + nativeUsage = totalUsage
		return  max - (heapSize - heapRemaining + nativeUsage);
	}
	public static double checkFreeAppSpace() {
		StatFs stats = new StatFs("/data");
		int availableBlocks = stats.getAvailableBlocks();
		int blockSizeInMBytes = stats.getBlockSize()/1024;
		int freeSpaceInMBytes = availableBlocks * blockSizeInMBytes;
		return freeSpaceInMBytes;
	}
	
	
}
