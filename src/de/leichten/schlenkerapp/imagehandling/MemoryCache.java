package de.leichten.schlenkerapp.imagehandling;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {

	private static MemoryCache instance = null;

	public static MemoryCache getInstance() {
		if (instance == null) {
			instance = new MemoryCache();
		}
		return instance;
	}

	private MemoryCache() {
		// Use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	private static final String TAG = "MemoryCache";

	// Last argument true for LRU ordering
	private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	private long size = 0;
	// Max memory in bytes
	private long limit = 1000000;

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void rename(String id, String newId){
		if (get(id) != null) {
			Bitmap temp = get(id);
			this.cache.remove(id);
			this.cache.put(newId, temp);
		}
		
		
	}
	
	public void remove(String id) {
		try {
			if (cache.containsKey(id)) {
				cache.remove(id);
				size = size - getSizeInBytes(cache.get(id));
				checkSize();
			}
		} catch (Throwable th) {
			th.printStackTrace();
		}

	}

	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// Least recently accessed item will be the first one iterated
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		try {
			cache.clear();
			size = 0;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}