package de.leichten.schlenkerapp.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.R.id;
import de.leichten.schlenkerapp.R.layout;
import de.leichten.schlenkerapp.R.menu;
import de.leichten.schlenkerapp.imagehandling.ImageLoader;

import utils.BitmapHelpers;
import utils.Utils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
		initDialogListener(imageView);
		// Load the image using ImageLoader class followed by the position
		imageLoader.displayImage(d[position], imageView);

		// Locate the TextView in view_image.xml
		text = (TextView) findViewById(R.id.imagetext);
		// Load the text into the TextView followed by the position
		text.setText(n[position]);
	}

	private void initDialogListener(ImageView imageView) {
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CharSequence[] items = { ROTATE_RIGHT, ROTATE_LEFT,
						RENAME, DELETE };

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ViewSingleImage.this);
				builder.setTitle("Menü");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals(ROTATE_RIGHT)) {
							triggerRotationRight();
						}
						if (items[item].equals(ROTATE_LEFT)) {
							triggerRotationLeft();
							finish();
						}
						if (items[item].equals(RENAME)) {
							triggerRename();
							finish();
						}
						if (items[item].equals(DELETE)) {
							triggerDelete();
						}
						dialog.dismiss();
					}

				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	private void triggerDelete() {
		// TODO Auto-generated method stub

	}

	private void triggerRename() {
		// TODO Auto-generated method stub

	}

	private void triggerRotationLeft() {
		File file = new File(d[position]);
		new ImageRotationTask(-90).execute(file);
	}

	private void triggerRotationRight() {
		File file = new File(d[position]);
		new ImageRotationTask(90).execute(file);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_single_image, menu);
		return true;
	}

	public class ImageRotationTask extends AsyncTask<File, Void, Void> {

		int degrees = 0;

		public ImageRotationTask(int degrees) {
			this.degrees = degrees;
		}

		@Override
		protected Void doInBackground(File... src) {
			
			for (File file : src) {
				
				Bitmap rotatetImage = BitmapHelpers.rotateImage(file, degrees);
				//write Bitmap to file path
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				rotatetImage.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
				
				byte[] bitmapdata = bos.toByteArray();
				FileOutputStream fos;
				
				try {
					fos = new FileOutputStream(file);
					fos.write(bitmapdata);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			imageLoader.clearCache();
			imageLoader.displayImage(d[position], imageView);

		}

	}

}
