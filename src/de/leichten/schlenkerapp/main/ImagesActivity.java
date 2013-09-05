package de.leichten.schlenkerapp.main;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import utils.Constants;
import utils.UtilFile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.ftp.FTPUploadTask;
import de.leichten.schlenkerapp.imagehandling.ImageAdapter;
import de.leichten.schlenkerapp.sd.SDSavingTask;
import de.leichten.schlenkerapp.sd.SDUtil;
import de.leichten.schlenkerapp.tasks.ImageRotationTask;

public class ImagesActivity extends Activity {

	private String[] filePathStrings;
	private String[] fileNameStrings;
	private File[] listFile;
	int position;

	final static String ROTATE_RIGHT = "Rechts drehen";
	final static String ROTATE_LEFT = "Links drehen";
	final static String RENAME = "Umbenennen";
	final static String DELETE = "Löschen";

	public GridView grid;
	public ImageAdapter adapter;
	public File file;

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("IMAGES", "onResume");
		loadPictures();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images);
		Log.d("IMAGES", "onCreate");
	}

	public void loadPictures() {
		// Locate the image in the app files dir
		file = getFilesDir();
		file.mkdirs();

		if (file.isDirectory()) {
			listFile = file.listFiles();

			// sort images by date
			Arrays.sort(listFile, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			// List file path for Images
			filePathStrings = new String[listFile.length];
			// List file path for Image file names
			fileNameStrings = new String[listFile.length];

			for (int i = 0; i < listFile.length; i++) {
				// Get the path
				filePathStrings[i] = listFile[i].getAbsolutePath();

				// Get the name
				fileNameStrings[i] = listFile[i].getName();
			}
		}

		grid = (GridView) findViewById(R.id.gridview);
		// Pass results to LazyAdapter Class
		adapter = new ImageAdapter(this, filePathStrings, fileNameStrings);
		// Binds the Adapter to the GridView
		grid.setAdapter(adapter);

		// Capture button clicks on gridview items
		setOnClickListener();
		setOnLongClickListener();
	}

	private void setOnLongClickListener() {
		grid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final CharSequence[] items = { ROTATE_RIGHT, ROTATE_LEFT, RENAME, DELETE };

				ImagesActivity.this.position = position;

				AlertDialog.Builder builder = new AlertDialog.Builder(ImagesActivity.this);
				builder.setTitle("Menü");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals(ROTATE_RIGHT)) {
							triggerRotationRight();
						}
						if (items[item].equals(ROTATE_LEFT)) {
							triggerRotationLeft();
						}
						if (items[item].equals(RENAME)) {
							triggerRename();
						}
						if (items[item].equals(DELETE)) {
							triggerDelete();
						}

						dialog.dismiss();
					}

				});
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
		});
	}

	private void triggerDelete() {
		File file = new File(filePathStrings[position]);
		file.delete();
	}

	private void triggerRename() {
		// TODO Auto-generated method stub

	}

	private void triggerRotationLeft() {
		File file = new File(filePathStrings[this.position]);
		new ImageRotationTask(-90, this).execute(file);
	}

	private void triggerRotationRight() {
		File file = new File(filePathStrings[this.position]);
		new ImageRotationTask(90, this).execute(file);
	}

	private void setOnClickListener() {
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent i = new Intent(getApplicationContext(), ViewSingleImage.class);
				i.putExtra("filepath", filePathStrings);
				i.putExtra("filename", fileNameStrings);
				i.putExtra("position", position);
				// Open ViewImage.java Activity
				startActivity(i);

			}
		});
	}

	private void doUploadStuff(File file) {
		String procedure = decideProcedure(file);

		new FTPUploadTask(this, procedure).execute(file);

		if (SDUtil.checkFileExists(file)) {
			new SDSavingTask(this, procedure).execute(file);
		}

	}

	private String decideProcedure(File file) {
		if (file.getName().contains("_")) {
			return Constants.PROCEDURE_PARTIE;
		} else {
			return Constants.PROCEDURE_ARTICLE;
		}
	}

}