package de.leichten.schlenkerapp.barcode;

import com.google.zxing.client.android.Intents.Scan;

import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.R.layout;
import de.leichten.schlenkerapp.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class QRActivity extends Activity {

	TextView QrResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		
		QrResult = (TextView)findViewById(R.id.textView_QrResult);
		startQRScan();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.qr, menu);
		return true;
	}
	
	private void startQRScan() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra(Scan.FORMATS, "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
		
		startActivityForResult(intent, 0);
	}

	

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				QrResult.setText(contents);
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}
	
}
