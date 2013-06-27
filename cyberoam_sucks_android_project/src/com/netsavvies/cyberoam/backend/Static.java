package com.netsavvies.cyberoam.backend;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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
import com.netsavvies.cyberoam.gui.LoginActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/*
 * Parameters/Vars on which the service runs. Not accessible directly by gui
 */

public class Static {
	// Intervals
	static final int loginInterval = 2 * 60 * 60*1000; // if already loggend in, then
													// automatically
	// attempt to login again after this interval.
	// Generally kept to be 2 -3 hrs
	static final int loginTrialInterval=30*1000; // if a login attempt fails, then try to
									// login again after this interval (5 -10
									// secs)
	static final int netCheckInterval=3*60*1000; // check if we are connected to the interval.
									// Have to give a thought on its value

	static String url = "http://10.100.56.55:8090/httpclient.html";
	// user data
	static String loginId;
	static boolean isloggedIn;
	static String password;

	// bools
	static boolean isAutoLoginAfterIntervalOn;
	static boolean isAutoCheckOn;
	static boolean isAutoTryOn;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return 1;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return 2;
		}
		return 0;
	}

	public static boolean isCyberoamAvailbale() {
		HttpURLConnection urlc;

		try {
			urlc = (HttpURLConnection) (new URL(
					"http://10.100.56.55:8090/httpclient.html")
					.openConnection());

			urlc.connect();
			if (urlc.getResponseCode() == 200)
				return true;
			else
				return false;

		} catch (IOException e) {
			Log.e("Problem", "Error checking internet connection", e);
			return false;
		}
	}

	public static boolean iswifiConnected(Context context) {
		// TODO Auto-generated method stub
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected())
			return true;
		else
			return false;

	}

	public static int isConnectionAlive(Context context) {
		// TODO Auto-generated method stub
		/*if (!iswifiConnected(context))
			return false;*/
		

		String message = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost client = new HttpPost("http://playpowerlabs.org/");

		try {
			HttpResponse response = httpclient.execute(client);
			HttpEntity r_entity = response.getEntity();
			String xmlString = EntityUtils.toString(r_entity);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlString));
			Document doc = db.parse(inStream);
			NodeList nl = doc.getElementsByTagName("body");
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					org.w3c.dom.Element nameElement = (org.w3c.dom.Element) nl
							.item(i);
					message = nameElement.getFirstChild().getNodeValue().trim();
				}

			}
		} catch (ClientProtocolException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		if (message.equals("Playpower Labs"))
			return 1;
		else
		{
			HttpURLConnection urlc;

			try {
				urlc = (HttpURLConnection) (new URL("http://playpowerlabs.org/")
						.openConnection());

				urlc.connect();
				if (!(urlc.getResponseCode() == 200))
					return 0;
				else
					return 2;

			} catch (IOException e) {
				Log.e("Problem", "Error checking internet connection", e);
				return 2;
			}
		}

	}

	public static boolean getWifiStrength(Context context) {
		// TODO Auto-generated method stub
		if(!Static.iswifiConnected(context))
		return false;
		WifiManager mywifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int level = Static.calculateSignalLevel((mywifiManager.getConnectionInfo().getRssi()));
	    if(level>=30)
	    	return true;
	    else
	    	return false;
	
	}

	
	
	
	public static int calculateSignalLevel(int rssi) {
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
}
