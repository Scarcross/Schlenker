package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Debug;

public class BitmapHelpers {

	final static int REQUIRED_SIZE = 1024;
	final static int REQUIRED_SIZE_THUMBNAIL = 256;

	public static Bitmap decodeAndResizeFile(File originalImage) throws BitmapMemoryException {
		return decodeAndResizeOriginal(originalImage, REQUIRED_SIZE);
	}

//	public static Bitmap getThumbnail(File originalImage) {
//		return decodeAndResizeOriginal(originalImage, REQUIRED_SIZE_THUMBNAIL);
//	}

	public static void compressBitmap(Bitmap bitmap, File file) {
		System.gc();

		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// shrink the image to
	private static Bitmap decodeAndResizeOriginal(File file, int size) throws BitmapMemoryException{
		System.gc();
		try {
			// Decode image size
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(file);
			BitmapFactory.decodeStream(stream1, null, options);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			// Recommended Size 512

			int width_tmp = options.outWidth;
			int height_tmp = options.outHeight;
			int scale = 1;

			while (true) {
				if (width_tmp / 2 < size || height_tmp / 2 < size){
					scale = scale * 2;					
					break;
				}else{
					width_tmp = width_tmp / 2;
					height_tmp = height_tmp / 2;
					scale = scale * 2;					
				}
				
			}
			
			if (!checkBitmapFitsInMemory(width_tmp, height_tmp, 2)) {
				throw new BitmapMemoryException("Memory is full!");
			}
			
			// Decode with inSampleSize
			options = new BitmapFactory.Options();
			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			FileInputStream stream2 = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, options);
			stream2.close();

			return bitmap;

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void rotateImage(File file, int degrees) throws BitmapMemoryException {
		System.gc();

		Bitmap decodeFile = BitmapHelpers.decodeAndResizeFile(file);
		Matrix affine = new Matrix();
		affine.preRotate(degrees);
		decodeFile = Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), affine, false);
		UtilFile.saveBitmapToFile(decodeFile, file);

	}
	
	/**
	 * Checks if a bitmap with the specified size fits in memory
	 * @param bmpwidth Bitmap width
	 * @param bmpheight Bitmap height
	 * @param bmpdensity Bitmap bpp (use 2 as default)
	 * @return true if the bitmap fits in memory false otherwise
	 */
	public static boolean checkBitmapFitsInMemory(long bmpwidth,long bmpheight, int bmpdensity ){
	    long reqsize=bmpwidth*bmpheight*bmpdensity;
	    long allocNativeHeap = Debug.getNativeHeapAllocatedSize();

	    final long heapPad=(long) Math.max(4*1024*1024,Runtime.getRuntime().maxMemory()*0.1);
	    if ((reqsize + allocNativeHeap + heapPad) >= Runtime.getRuntime().maxMemory())
	    {
	        return false;
	    }
	    return true;

	}

	public static class BitmapMemoryException extends Exception{
		
		public BitmapMemoryException(String message) {
		super(message);
		}
		
	}
	
}
