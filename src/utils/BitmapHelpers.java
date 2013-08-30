package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapHelpers {
	
	
	final static int REQUIRED_SIZE = 1024;	
	final static int REQUIRED_SIZE_THUMBNAIL = 256;
	
	public static Bitmap decodeAndResizeFile(File originalImage){
		return decodeAndResizeOriginal(originalImage, REQUIRED_SIZE);
	}
	
	public static Bitmap getThumbnail(File originalImage){
		return decodeAndResizeOriginal(originalImage, REQUIRED_SIZE_THUMBNAIL);
	}
	
	//shrink the image to 
	private static Bitmap decodeAndResizeOriginal(File file, int size) {
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
                if (width_tmp / 2 < size  || height_tmp / 2 < size)
                    break;
                width_tmp = width_tmp / 2;
                height_tmp = height_tmp / 2;
                scale = scale * 2;
            }
 
            // Decode with inSampleSize
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
	
	public static Bitmap rotateImage(File file, int degrees){
		System.gc();
		
		Bitmap decodeFile = BitmapHelpers.decodeAndResizeFile(file);
		Matrix affine = new Matrix();
		affine.preRotate(degrees);
		decodeFile = Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), affine,false);
		
		return decodeFile;
		
	}
	
}
