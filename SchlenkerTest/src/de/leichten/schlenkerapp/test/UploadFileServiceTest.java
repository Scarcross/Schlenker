package de.leichten.schlenkerapp.test;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.test.ServiceTestCase;
import android.test.mock.MockContext;
import de.leichten.schlenkerapp.R;
import de.leichten.schlenkerapp.services.UploadFileService;
import static utils.Utils.SHARED_PREF_UPLOAD_NAME;

public class UploadFileServiceTest extends ServiceTestCase<UploadFileService> {

	private UploadFileService service;
	private Context c;
	private SharedPreferences prefs;

	public UploadFileServiceTest() {
		super(UploadFileService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		c = new ContextTest(getContext());
		this.setContext(c);
	}

	public void testUpload() {

		Intent serviceStarer = new Intent(
				"de.leichten.schlenkerapp.services.UploadFileService");
		serviceStarer.setClass(c, UploadFileServiceTest.class);
		this.startService(serviceStarer);
		prefs = getService().getSharedPreferences(SHARED_PREF_UPLOAD_NAME, 0);

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class ContextTest extends MockContext {

		private Context context;
		private SharedPreferences prefs;

		public ContextTest(Context context) {
			this.context = context;
		}

		@Override
		public ComponentName startService(Intent service) {
			return this.context.startService(service);
		}

		@Override
		public String getPackageName() {
			return "de.leichten.schlenkerapp";
		}

		@Override
		public ApplicationInfo getApplicationInfo() {
			return context.getApplicationInfo();
		}

		@Override
		public SharedPreferences getSharedPreferences(String name, int mode) {
			prefs.edit().putString(getResources().getString(R.string.ftp_address),
					"");
			prefs.edit().putString(
					getResources().getString(R.string.ftp_username), "");
			prefs.edit().putString(
					getResources().getString(R.string.ftp_password), "");
			prefs.edit().putString(
					getResources().getString(R.string.ftp_conntype), "");
			prefs.edit().putString(
					getResources().getString(R.string.ftp_proxy), "");
			return prefs;
		}

	}

}
