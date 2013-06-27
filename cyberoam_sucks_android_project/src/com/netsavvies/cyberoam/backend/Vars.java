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

public class Vars {
	// Intervals
	static final int loginInterval = 2 * 60 * 60*1000; // if already loggend in, then automatically
	// attempt to login again after this interval.
	// Generally kept to be 2 -3 hrs
	static final int loginTrialInterval=30*1000; // if a login attempt fails, then try to
									// login again after this interval (5 -10
									// secs)
	static final int netCheckInterval=3*60*1000; // check if we are connected to the interval.
									// Have to give a thought on its value
	static final int cybCheckInterval=30*1000;
	static int thresholdStrength = 30;
	static String url = "http://10.100.56.55:8090/httpclient.html";
	
	// user data
	static String loginId;
	static boolean isloggedIn;
	static String password;

	// bools
	/*
	static boolean isAutoLoginAfterIntervalOn;
	static boolean isAutoCheckOn;
	static boolean isAutoTryOn;
	*/

}
