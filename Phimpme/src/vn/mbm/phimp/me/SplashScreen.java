package vn.mbm.phimp.me;

import vn.mbm.phimp.me.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity {

	protected int _splashTime = 1000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = new Intent();
		intent.setClass(SplashScreen.this, PhimpMe.class);
		if (_splashTime > 0) {
			setContentView(R.layout.splash);
			// thread for displaying the SplashScreen
			new Thread() {
				@Override
				public void run() {
					try {
						synchronized (this) {
							wait(_splashTime);
						}
					} catch (InterruptedException ignored) {
					} finally {
						finish();
						startActivity(intent);
						// stop();
					}
				}
			}.start();
		} else {
			startActivity(intent);
		}
	}

}
