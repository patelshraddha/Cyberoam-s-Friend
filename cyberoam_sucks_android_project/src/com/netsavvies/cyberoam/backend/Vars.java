package com.netsavvies.cyberoam.backend;

import java.util.Hashtable;

import com.netsavvies.cyberoam.R;

import android.widget.Button;



import static com.netsavvies.cyberoam.backend.Const.*;

//TODO remove comment

/*
 * Parameters/Vars on which the service runs. Not accessible directly by gui
 */

public class Vars {
	
	static final public Hashtable<Const, Boolean> bool_hs;
	
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
	static final int thresholdStrength = 30;
	static  String url = "https://10.100.56.55:8090/httpclient.html";
	
	
	static String loginMessage = "You have successfully logged in";
	
	static final String logoutMessage ="You have successfully logged off" ;
	static final String wrongIdpwdmessage = "The system could not log you on. Make sure your password is correct";
	static final String cyberlessMessage = "You are already logged in as a clientless user";
	static final String maxloginMessage = "You have reached Maximum Login Limit.";
	
	static final String netCheckurl = "http://www.whatismyip.org/";
	static final String netPageTitle="What is my ip? - WhatIsMyIp.org";
	static final String cybPageTitle="";
	static final int timeoutConnection=5000;
	static final int timeoutSocket = 5000;
	//strings/messages
	
	static final public Hashtable<Const,String> strings;
	static final public Hashtable<Const,Integer> icons;
	static final public Hashtable<Const,String> buttons;
	// user data
	public static String loginId;
	public static boolean isloggedIn;
	public static String password;


	//current status message
	public static  int currIcon;
	public static Const currStatusKey;
	public static String currMsg;

	
	
	//Constants
	public static final short notiId = 5461; //shrapa lpgic :/

	static {
		bool_hs = new Hashtable<Const, Boolean>();
		strings = new Hashtable<Const, String>();
		icons=new Hashtable<Const,Integer>();
		buttons=new Hashtable<Const,String>();
		
		//putting messages
		
		strings.put(init,"Starting service...");
		icons.put(init,R.drawable.start);
		buttons.put(init,"NA");
		
		
		strings.put(wifiDisconnected,"Wifi disconnected");
		icons.put(wifiDisconnected,R.drawable.wifidisconnected);
		buttons.put(wifiDisconnected,"Turn on wifi");
		
		strings.put(cyberLess,"You are already logged in a cyberless client.");
		icons.put(cyberLess,R.drawable.cyberless);
		buttons.put(cyberLess,"NA");
		
		strings.put(otherWifi,"Connected via another wifi");
		icons.put(otherWifi,R.drawable.another_wifi);
		buttons.put(otherWifi,"NA");
		
		strings.put(noNet,"Logged in but no net");
		icons.put(noNet,R.drawable.nonet);
		buttons.put(noNet,"NA");
		
		strings.put(lowSTR,"Not able to login due to less strength");
		icons.put(lowSTR,R.drawable.lowwifi);
		buttons.put(lowSTR,"NA");
		
		strings.put(loggedOutLessStrength,"Logged out due to less strength.");
		icons.put(loggedOutLessStrength,R.drawable.lowwifi);
		buttons.put(loggedOutLessStrength,"NA");
		
		
		strings.put(wifiKaBahotBadaLocha,"Unable to log out since cyberoam not available.");
		icons.put(wifiKaBahotBadaLocha,R.drawable.nocyb);
		buttons.put(wifiKaBahotBadaLocha,"NA");
		
		strings.put(noCyb,"Not able to login because cyberoam not available");
		icons.put(noCyb,R.drawable.nocyb);
		buttons.put(noCyb,"NA");
		
		strings.put(loggedIn,"Logged in by id ");
		icons.put(loggedIn,R.drawable.login);
		buttons.put(loggedIn,"Logout");
		
		strings.put(loggedOut,"Logged out by id ");
		icons.put(loggedOut,R.drawable.loggedout);
		buttons.put(loggedOut,"ReLogin");
		
		
		strings.put(noUser,"No user added.");
		icons.put(noUser,R.drawable.nouser);
		buttons.put(noUser,"Add users");
		
		strings.put(loginFailed,"Login failed.");
		icons.put(loginFailed,R.drawable.loginfailed);
		buttons.put(loginFailed,"Change Prefrences");
		
		strings.put(stop,"Service stopped.");
		icons.put(stop,R.drawable.stop);
		buttons.put(stop,"Start Again.");
		
		strings.put(wifiKaLochaAaya,"Wifi turned off without logging out.");
		icons.put(wifiKaLochaAaya,R.drawable.wifilocha);
		buttons.put(wifiKaLochaAaya,"NA");
				
	}
}
