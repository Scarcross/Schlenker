package main;

import de.leichten.schlenkerapp.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//HEllo THIS IS PATRICK
		return true;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startAnimation();

	}

	private void startAnimation() {
		
		ImageView myImageView = (ImageView) findViewById(R.id.SchlenkerLogo);
		Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		fadeIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				Toast toast = Toast.makeText(MainActivity.this,	"Animation startet", Toast.LENGTH_SHORT);
				toast.show();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				//No repeat
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent mainMenue = new Intent(MainActivity.this, MainMenue.class);
				startActivity(mainMenue);
			}
		});
		myImageView.startAnimation(fadeIn);
	}

}
