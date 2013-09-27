package de.leichten.schlenkerapp.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.imagehandling.ImageLoader;
import de.leichten.schlenkerapp.imagehandling.MemoryCache;

public class ViewSingleImage extends Activity {

	final static String ROTATE_RIGHT = "Rechts drehen";
	final static String ROTATE_LEFT = "Links drehen";
	final static String RENAME = "Umbenennen";
	final static String DELETE = "Löschen";

	String[] d;
	String[] n;
	int position;

	ImageView imageView;
	TextView text;
	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_single_image);
		// Retrieve data from MainActivity on item click event
		Intent i = getIntent();
		// Get a single position
		position = i.getExtras().getInt("position");
		// Get the list of filepaths
		d = i.getStringArrayExtra("filepath");
		// Get the list of filenames
		n = i.getStringArrayExtra("filename");

		// Calls the ImageLoader Class
		imageLoader = new ImageLoader(getApplication());
		// Locate the ImageView in view_image.xml
		imageView = (ImageView) findViewById(R.id.full_image_view);
		// Load the image using ImageLoader class followed by the position
		imageLoader.displayImage(d[position], imageView);

		// Locate the TextView in view_image.xml
		text = (TextView) findViewById(R.id.imagetext);
		// Load the text into the TextView followed by the position
		text.setText(n[position]);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MemoryCache.getInstance().remove(d[position]);
	}

	

	

	

}
