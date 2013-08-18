package de.leichten.schlenkerapp.test;

import android.test.ActivityInstrumentationTestCase2;
import de.leichten.schlenkerapp.main.MainMenue;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainMenue> {

	private MainMenue mActivity;
	
	public MainActivityTest() {
		super("de.leichten.schlenkerapp.main",MainMenue.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		
	}
	
	public void testResources(){
		int id = mActivity.getResources().getIdentifier("button1", "button", mActivity.getPackageName());
		assertTrue(id > 0);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
	
	

}
