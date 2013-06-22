package com.netsavvies.cyberoam.backend;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*
 * Controlling switches, accessible by gui.
 * For eg. loginCyberoam(), logoutCyberoam(), startBackend() or similar methods
 */

public class Control {

	

	public static void startService(Context context,String action)
	{
		int connection = Static.getConnectivityStatus(context);
		
		if(!((Static.isloggedIn)&&(connection==1)))
		{
		Intent newintent=new Intent(context,CybService.class);
	    newintent.putExtra("action",action);
	    context.startService(newintent);
		}
	}
	
	
	
	public static boolean isServiceRunning(Context context) {
	    ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (CybService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	    
	}
}
