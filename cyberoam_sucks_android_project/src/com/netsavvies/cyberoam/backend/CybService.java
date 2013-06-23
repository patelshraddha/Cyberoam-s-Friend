package com.netsavvies.cyberoam.backend;

/*
 * The service which performs the actual tasks. Started by Control.class/CybApp.class or by the system at boot and not directly accessible by gui
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

import com.netsavvies.cyberoam.gui.InformGui;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class CybService extends Service {

	int connection;
	TimerTask task;
	Timer timer;
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String action = intent.getStringExtra("action");
        Static.isloggedIn=false;
        Static.loginId=null;
        timer=new Timer();
        task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(attemptLogin())
				{
					InformGui.Notify("Logged in by "+Static.loginId,getApplicationContext());
					InformGui.loggedIn(getApplicationContext(),Static.loginId);
				}
			}
            
        };
     // Build notification
     // Actions are just fake
     
		// Stop the activity if rebooted.
		if (action == "android.intent.action.BOOT_COMPLETED")
			InformGui.Notify("Boot completed",getApplicationContext());

		connection = Static.getConnectivityStatus(getApplicationContext());
		if (connection == 0)
			InformGui.Notify("No data connection available",getApplicationContext());
		else if (connection == 2)
		{
			InformGui.Notify("Using Data connection",getApplicationContext());
			Toast.makeText(getApplicationContext(),"Using Data connection",Toast.LENGTH_SHORT).show();// mobile connection
		}
			else {
			if (Static.isCyberoamAvailbale()) {
				if(attemptLogin())
				{
					InformGui.Notify("Logged in by "+Static.loginId,getApplicationContext());
					InformGui.loggedIn(getApplicationContext(),Static.loginId);
					timer.schedule(task,Static.loginInterval,Static.loginInterval);
				}
				else
				{
					InformGui.Notify("Logged failed",getApplicationContext());
					InformGui.loginFailed(getApplicationContext());
				}	
			} else {
				// using some other wifi or because the wifi strength is less
				InformGui.Notify("Cyberoam can't be reached",getApplicationContext());
			}
		}

		return super.onStartCommand(intent, flags, startId);

	}

	public boolean attemptLogin()
	{
		int i=1;
		String message;
		while(getloginId(i)!=null)
		{
		message=contactServer("191",getloginId(i),getloginPassword(i),getApplicationContext());
	    if(message.equals("You have successfully logged in")||message.equals("You are logged in as a clientless user"))
	    {
	    Static.isloggedIn=true;
	    Static.loginId=getloginId(i);
	    break;
	    }
	    i++;
		}
		if(Static.isloggedIn==true)
			return true;
		else
			return false;
			
   }

	public void attemptLogout(Context context, String loginid,
			String loginpassword) {
		contactServer("193", loginid, loginpassword, context);
	}

	private String contactServer(String loginmode, String loginid, String loginpassword,
			Context context) {
		String url = "http://10.100.56.55:8090/httpclient.html";
		String message="empty";
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

	private String getloginId(int i) {
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences("user_details", 0);
		String user = settings.getString("user" + i, null);
		return user;
	}

	private String getloginPassword(int i) {
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences("user_details", 0);
		String password = settings.getString("password" + i, null);
		return password;
	}

}
