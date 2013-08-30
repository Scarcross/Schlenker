package de.leichten.schlenkerapp.main;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.imagehandling.ImageAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImagesActivity extends Activity {

	private String[] FilePathStrings;
	private String[] FileNameStrings;
	private File[] listFile;
	GridView grid;
	ImageAdapter adapter;
	File file;

	@Override
	protected void onResume() {
		super.onResume();

		Log.d("IMAGES", "onResume");


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images);

		Log.d("IMAGES", "onCreate");
		
		loadPictures();

	}

	private void loadPictures() {
		// Locate the image in the app files dir
		file = getFilesDir();
		// Create a new folder if no folder named Tutorial exist
		file.mkdirs();

		if (file.isDirectory()) {
			listFile = file.listFiles();

			// sort images by date
			Arrays.sort(listFile, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(
							f2.lastModified());
				}
			});

			
			// List file path for Images
			FilePathStrings = new String[listFile.length];
			// List file path for Image file names
			FileNameStrings = new String[listFile.length];

			for (int i = 0; i < listFile.length; i++) {
				// Get the path
				FilePathStrings[i] = listFile[i].getAbsolutePath();
				// Get the name
				FileNameStrings[i] = listFile[i].getName();
			}
		}

		// Locate the GridView in main.xml
		grid = (GridView) findViewById(R.id.gridview);
		// Pass results to LazyAdapter Class
		adapter = new ImageAdapter(this, FilePathStrings, FileNameStrings);
		// Binds the Adapter to the GridView
		grid.setAdapter(adapter);

		// Capture button clicks on gridview items
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(),
						ViewSingleImage.class);
				// Pass all the filepaths
				i.putExtra("filepath", FilePathStrings);
				// Pass all the filenames
				i.putExtra("filename", FileNameStrings);
				// Pass a single position
				i.putExtra("position", position);
				// Open ViewImage.java Activity
				startActivity(i);

			}
		});
	}

}
