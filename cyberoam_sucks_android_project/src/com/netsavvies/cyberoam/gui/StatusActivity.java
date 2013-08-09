package com.netsavvies.cyberoam.gui;

import com.netsavvies.cyberoam.backend.Const;
import com.netsavvies.cyberoam.backend.Control;
import com.netsavvies.cyberoam.backend.Methods;

import com.netsavvies.cyberoam.backend.Vars;
import com.netsavvies.cyberoam.gui.dragNdrop.DragNDropListActivity;

import static com.netsavvies.cyberoam.backend.Const.*;
import static com.netsavvies.cyberoam.backend.Methods.*;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity {

	private LinearLayout mainLayout;
	private LinearLayout subLayout;
	private int icon;
	private String message;
	private String description;
	private boolean scrollViewVisible;
	private String buttonString;
	private static ImageView image;
	private static Button button;
	private static TextView messageView;
	private static ScrollView scrollView;
	private final String update = "UPDATE_SCREEN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		image = new ImageView(this);
		
		image.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 0, (float) 0.3));

		subLayout = new LinearLayout(this);
		subLayout.setOrientation(LinearLayout.HORIZONTAL);
		subLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, (float) 0.2));

		messageView = new TextView(this);
		messageView.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, (float) 0.7));
		// messageView.setText("Success");

		button = new Button(this);
		button.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, (float) 0.3));
		// button.setText("On");

		subLayout.addView(messageView);
		subLayout.addView(button);

		scrollView = new ScrollView(this);
		scrollView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, (float) 0.5));

		updateScreen();
		setBtnListeners(this);
		mainLayout.addView(image);
		mainLayout.addView(subLayout);
		mainLayout.addView(scrollView);
		setContentView(mainLayout);
		
	}

	private static void updateScreen() {
		image.setImageResource(Vars.currIcon);
		messageView.setText(Vars.currMsg);
		
		if (getButton(Vars.currStatusKey).equals("NA")) {
			button.setVisibility(View.GONE);
		} else {
			button.setVisibility(View.VISIBLE);
			button.setText(getButton(Vars.currStatusKey));
		}
	}

	private static void updateScrollView() {
		switch (Vars.currStatusKey) {

		}
	}

	private static void setBtnListeners(final Context context) {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i;
				switch (Vars.currStatusKey) {
				case wifiDisconnected:
					execute(wifi, true, context);
					break;
				case loggedIn:
				case stop:
					Methods.sendBroadcast(restart.toString(),context);
					break;
				case noUser:
				case loginFailed:
					i = new Intent(context, DragNDropListActivity.class);
					context.startActivity(i);
					break;
				}

			}
		});
	}

	private BroadcastReceiver guiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Const constMsg = Const.valueOf(intent.getAction());
			switch(constMsg){
				case updateGui :  
					updateScreen();
					break;
				default:		
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(guiReceiver, new IntentFilter(updateGui.toString()));
	}

	@Override
	protected void onPause() {
		unregisterReceiver(guiReceiver);
		super.onPause();
	}

}
