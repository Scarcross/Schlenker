package de.leichten.schlenkerapp.main;

import utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.tasks.FinishingTask;

public class TakeBarcodeActivity extends Activity {

	private static final String BC_CALLED = "BC_CALLED";
	
	private TextView qrResult;
	private ImageView imageView;
	
	public TextView getTextView() {
		return qrResult;
	}

	public ImageView getImageView() {
		return imageView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_barcode);
		
		qrResult = (TextView) findViewById(R.id.textView_QrResult);
		imageView = (ImageView) findViewById(R.id.imageView_QrResult);
		
		if (savedInstanceState != null) {
			if (!savedInstanceState.getBoolean(BC_CALLED)) {
				startQRScan();
			}
		}else {
			startQRScan();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.qr, menu);
		return true;
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(BC_CALLED, true);
	}

	private void startQRScan() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE","QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				qrResult.setText("Warte auf Finish...");
				triggerFinishing(contents);
			}
		} else {
			// TODO implement
		}
	}

	private void triggerFinishing(String barcode) {
		FinishingTask finishingTask;
		
		Intent reveicedIntent = getIntent();
		Object object = reveicedIntent.getExtras().get(Constants.PROCEDURE_PARTIE_OR_ARTICLE);
		
		if(object != null){
			if (Constants.PROCEDURE_PARTIE.equals(object)){
				finishingTask = new FinishingTask(this,Constants.PROCEDURE_PARTIE);
				finishingTask.execute(barcode);
			}
			else if (Constants.PROCEDURE_ARTICLE.equals(object)){
				finishingTask = new FinishingTask(this,Constants.PROCEDURE_ARTICLE);
				finishingTask.execute(barcode);
			}
			else{
				backToMainScreen();
				finish();
			}
		}
	}

	private void backToMainScreen() {
		Intent intent = new Intent(this, MainMenue.class);
		startActivity(intent);
		finish();
	}

	
	

	
}
