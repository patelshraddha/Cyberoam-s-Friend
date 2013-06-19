package com.netsavvies.cyberoam.gui;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.netsavvies.cyberoam.R;
import com.netsavvies.cyberoam.backend.Control;
import com.netsavvies.cyberoam.backend.CybService;

import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private String url;
	private TextView display;
	private final String loginid = "200901031";
	private final String loginpassword = "divya";
	private final String loginmode = "191";
	private HttpResponse response;
	BroadcastReceiver reg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		display=(TextView)findViewById(R.id.textview);

		int percentage = 0;

		WifiManager mywifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		int rssi = mywifiManager.getConnectionInfo().getRssi();
		percentage = WifiManager.calculateSignalLevel(rssi, 100);

		display.setText("" + percentage);

		reg = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
					int mWifiRssi = intent.getIntExtra(
							WifiManager.EXTRA_NEW_RSSI, -200);
					Log.d("Sucks", "" + mWifiRssi);
					int mWifiLevel = this.calculateSignalLevel(mWifiRssi);
					display.setText("" + mWifiLevel);
				}

			}

			int calculateSignalLevel(int rssi) {
				// TODO Auto-generated method stub
				int MIN_RSSI = -100;
				int MAX_RSSI = -55;
				int numLevels = 100;
				if (rssi <= MIN_RSSI) {
					return 0;
				} else if (rssi >= MAX_RSSI) {
					return numLevels-1;
				} else {
					float inputRange = (MAX_RSSI - MIN_RSSI);
					float outputRange = (numLevels-1);
					return (int) ((float) (rssi - MIN_RSSI) * outputRange / inputRange);
				}
			}
		};

		registerReceiver(reg, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
         
		
		
		
	/*	if(Control.isServiceRunning(this)==false)
		{
		Toast.makeText(this,"HERE",Toast.LENGTH_SHORT).show();
		Intent startServiceIntent = new Intent(this, com.netsavvies.cyberoam.backend.CybService.class);
        startService(startServiceIntent);
        
        Toast.makeText(this,"Service started by login activity",Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(this,"Service already running",Toast.LENGTH_SHORT).show();*/
		}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//unregisterReceiver(reg);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//registerReceiver(reg, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
