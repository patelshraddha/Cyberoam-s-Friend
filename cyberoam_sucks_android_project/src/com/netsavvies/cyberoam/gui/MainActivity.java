package com.netsavvies.cyberoam.gui;

import com.netsavvies.cyberoam.R;
import com.netsavvies.cyberoam.R.layout;
import com.netsavvies.cyberoam.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Thread thread = new Thread() {
			private Intent intent;

			@Override
			public void run() {
				try {

					sleep(2000);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					intent = new Intent(
							"com.netsavvies.cyberoam.gui.STATUSACTIVITY");
					startActivity(intent);
					finish();
				}
			}
		};

		thread.start();

	}

}
