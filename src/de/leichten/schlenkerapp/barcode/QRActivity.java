package de.leichten.schlenkerapp.barcode;

import com.google.zxing.client.android.Intents.Scan;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.R.layout;
import de.leichten.schlenkerapp.R.menu;
import de.leichten.schlenkerapp.tasks.FinishingTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class QRActivity extends Activity {

	TextView qrResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);

		qrResult = (TextView) findViewById(R.id.textView_QrResult);
		startQRScan();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.qr, menu);
		return true;
	}

	private void startQRScan() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		
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

}
