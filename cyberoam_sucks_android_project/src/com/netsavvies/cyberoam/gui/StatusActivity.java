package com.netsavvies.cyberoam.gui;

import com.netsavvies.cyberoam.R;
import com.netsavvies.cyberoam.backend.Const;

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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity {

	private RelativeLayout mainLayout;
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
	private int windowwidth;
	private int windowheight;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		windowheight = getWindowManager().getDefaultDisplay().getHeight();
		// mainLayout = new LinearLayout(this);
		mainLayout = new RelativeLayout(this);
		mainLayout.setBackgroundColor(Color.rgb(61, 0, 61));
		// mainLayout.setOrientation(LinearLayout.VERTICAL);
		image = new ImageView(this);

		messageView = new TextView(this);
		messageView.setTextColor(Color.WHITE);
		messageView.setTextSize(30f);
        
		button = new Button(this);
        button.setTextSize(20f);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(windowwidth/2,windowheight/4);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin=(windowheight/10);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params2.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(windowwidth/2,windowheight/8);
        params3.bottomMargin=(windowheight/5);
		params3.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		image.setLayoutParams(params);
		messageView.setLayoutParams(params2);
		button.setLayoutParams(params3);
		button.setBackgroundColor(Color.WHITE);
		button.setTextColor(Color.BLACK);
		/*subLayout = new LinearLayout(this);
		subLayout.setOrientation(LinearLayout.HORIZONTAL);
		subLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, (float) 0.2));*/

		// messageView.setLayoutParams(new
		// LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, (float) 0.7));

		/*button.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, (float) 0.3));
		

		subLayout.addView(messageView);
		subLayout.addView(button);

		scrollView = new ScrollView(this);
		scrollView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, (float) 0.5));*/

		updateScreen();
		setBtnListeners(this);
		mainLayout.addView(image);

		mainLayout.addView(messageView);
		mainLayout.addView(button);
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
					Methods.sendBroadcast(loggedOut.toString(), context);
					break;
				case loggedOut:
					Methods.sendBroadcast(enable.toString(), context);
					break;
				case stop:
					Methods.sendBroadcast(restart.toString(), context);
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
			switch (constMsg) {
			case updateGui:
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

	@Override
	protected void onRestart() {
		super.onRestart();
		updateScreen();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.status_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.changeprefrences:
			Intent intent = new Intent("com.netsavvies.cyberoam.gui.dragNdrop.DRAGNDROPLISTACTIVITY");
			context.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
