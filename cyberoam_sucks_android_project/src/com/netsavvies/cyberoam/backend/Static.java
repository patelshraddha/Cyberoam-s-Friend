package com.netsavvies.cyberoam.backend;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/*
 * Parameters/Vars on which the service runs. Not accessible directly by gui
 */

class Static {
	// Intervals
	static final int loginInterval=2*60*60; // if already loggend in, then automatically
								// attempt to login again after this interval.
								// Generally kept to be 2 -3 hrs
	static int loginTrialInterval; // if a login attempt fails, then try to
									// login again after this interval (5 -10
									// secs)
	static int loginCheckInterval; // check if we are connected to the interval.
									// Have to give a thought on its value

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

	

}
