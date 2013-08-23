package de.leichten.schlenkerapp.barcode;

import de.leichten.schlenkerapp.R;

import de.leichten.schlenkerapp.tasks.FinishingTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class QRActivity extends Activity {

	private static final String BC_CALLED = "BC_CALLED";
	
	TextView qrResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		
		qrResult = (TextView) findViewById(R.id.textView_QrResult);
		
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
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				qrResult.setText(contents);
				triggerFinishing(contents);
			}
		} else {
			// TODO implement
		}
	}

	private void triggerFinishing(String barcode) {
		FinishingTask finishingTask = new FinishingTask(this);
		finishingTask.execute(barcode);
	}

	public void buttonClicked(final View view) {
		int id = view.getId();

		switch (id) {
		case R.id.btn_start_scan:
			startQRScan();
			break;
		default:
			break;
		}
		
	}
}
