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

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

class Methods {

	static boolean isCyberoamAvailbale(Context context) {
		if (!isWifiConnected(context))
			return false;
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

	static boolean isWifiConnected(Context context) {
		
		// TODO Auto-generated method stub
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		Log.wtf("isWifiConnected",mWifi.isConnected()+"");
		
		return mWifi.isConnected();
	}

	static int isConnectionAlive(Context context) {
		if (!isWifiConnected(context))
			return 0;
		

		String message = "";
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
		} catch (ClientProtocolException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		} catch (SAXException e) {
			
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

	static boolean isStrengthEnough(Context context) {
		// TODO Auto-generated method stub
		if(!isWifiConnected(context))
			return false;
		
		WifiManager mywifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int level = calculateSignalLevel((mywifiManager.getConnectionInfo().getRssi()));
		Log.wtf("WifiStrength",level+"");
	    if(level >= Vars.thresholdStrength)
	    	return true;
	    else
	    	return false;
	
	}
	
	static void turnWifi(Context context,boolean bool){
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(bool); 
	}

	static int calculateSignalLevel(int rssi) {
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
	
	public static void attemptLogout(Context context, String loginid,	String loginpassword) {
		Log.wtf("logoutMsg", contactServer("193", loginid, loginpassword, context));

	}
	private static String getloginId(int i) {
		/*SharedPreferences settings = getApplicationContext()
				.getSharedPreferences("user_details", 0);
		String user = settings.getString("user" + i, null);
		return user;*/
		return "200901146";
	}
	private static String getloginPassword(int i) {
		/*SharedPreferences settings = getApplicationContext()
				.getSharedPreferences("user_details", 0);
		String password = settings.getString("password" + i, null);*/
		return "reset123";
	}
	public static Const attemptLogin(Context context) {
		int i = 1;
		int maxLogin = 0;

		String message;
		// while (getloginId(i) != null) {
		message = contactServer("191", getloginId(i), getloginPassword(i),
				context);
		Log.wtf("loginMsg", message);
		if (message.equals("You have successfully logged in")
				|| message
						.equals("You are already logged in as a clientless user")) {
			// Static.isloggedIn = true;
			Vars.loginId = getloginId(i);
			Vars.password = getloginPassword(i);
			return Const.loggedIn;
			// break;
		}
		/*	else if(message.equals("You have reached the maximum login limit"))
				maxLogin+=1;
			else if()
			i++;*/
		else
			return Const.maxLogin;

	}
	
	
	public static String contactServer(String loginmode, String loginid,
			String loginpassword, Context context) {
		String url = Vars.url;
		String message = "empty";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost client = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mode", loginmode));
		nvps.add(new BasicNameValuePair("username", loginid));
		nvps.add(new BasicNameValuePair("password", loginpassword));
		try {
			client.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) { // TODO Auto-generated catch
													// block
			e.printStackTrace();
		}
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
			NodeList nl = doc.getElementsByTagName("message");
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
		return message;
	}
	
}
