package com.netsavvies.cyberoam.backend;

import static com.netsavvies.cyberoam.backend.Const.cyberLess;
import static com.netsavvies.cyberoam.backend.Const.loggedIn;
import static com.netsavvies.cyberoam.backend.Const.maxLogin;
import static com.netsavvies.cyberoam.backend.Const.noUser;
import static com.netsavvies.cyberoam.backend.Const.notChecked;
import static com.netsavvies.cyberoam.backend.Const.wrongIdPwd;
import static com.netsavvies.cyberoam.backend.Vars.bool_hs;
import static com.netsavvies.cyberoam.backend.Vars.buttons;
import static com.netsavvies.cyberoam.backend.Vars.cyberlessMessage;
import static com.netsavvies.cyberoam.backend.Vars.icons;
import static com.netsavvies.cyberoam.backend.Vars.loginMessage;
import static com.netsavvies.cyberoam.backend.Vars.logoutMessage;
import static com.netsavvies.cyberoam.backend.Vars.maxloginMessage;
import static com.netsavvies.cyberoam.backend.Vars.strings;
import static com.netsavvies.cyberoam.backend.Vars.wrongIdpwdmessage;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.netsavvies.cyberoam.R;

public class Methods {
	private static ArrayList<UserDetails> data;
	private static int position;

	public static void execute(Const key, boolean bool, Context ctx) {
		Log.wtf("Execute", key.name() + " " + bool);
		switch (key) {
		case wifi:
			turnWifi(ctx, bool);
		}
	}

	static void set(Const key, Boolean bool) {
		Log.wtf("set", key.name() + " " + bool);
		bool_hs.put(key, bool);
	}

	public static boolean get(Const key) {
		if (getNoLog(key)) {
			Log.wtf("get", key.name() + " true");
			return true;
		} else {
			Log.wtf("get", key.name() + " false");
			return false;
		}
	}

	public static String getMessage(Const key) {
		// try {
		if (strings.containsKey(key))
			return strings.get(key);
		else
			return null;
		// } catch (NullPointerException e) {
		// return null;
		// }
	}

	public static int getIcon(Const key) {
		try {
			if (icons.containsKey(key))
				return icons.get(key);
			else
				return R.drawable.ic_launcher;
		} catch (NullPointerException e) {
			return R.drawable.ic_launcher;
		}

	}

	public static String getButton(Const key) {
		try {
			if (buttons.containsKey(key))
				return buttons.get(key);
			else
				return "NA";
		} catch (NullPointerException e) {
			return "NA";
		}

	}

	public static boolean getNoLog(Const key) {
		if (bool_hs.containsKey(key)) {
			return bool_hs.get(key);
		} else {
			bool_hs.put(key, false);
			return false;
		}

	}

	public static char getTF(Const key) {
		if (getNoLog(key))
			return 'T';
		else
			return 'F';
	}

	public static char gettf(Const key) {
		if (getNoLog(key))
			return 't';
		else
			return 'f';
	}

	static boolean isCyberoamAvailbale(Context context) {
		if (!isWifiConnected(context))
			return false;
		HttpURLConnection urlc;
		

		try {
			urlc = (HttpURLConnection) (new URL(Vars.url)
					.openConnection());
			urlc.setConnectTimeout(4000);
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
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		Log.wtf("isWifiConnected", mWifi.isConnected() + "");

		return mWifi.isConnected();
	}

	static int isConnectionAlive(Context context) {
		Toast.makeText(context,"Here",Toast.LENGTH_SHORT).show();
		if (!isWifiConnected(context))
			return 2;
		 HttpParams httpParameters = new BasicHttpParams();
		 
	        HttpClient httpClient = new DefaultHttpClient(); 
	    
	        try {
				HttpGet method = new HttpGet(new URI(Vars.netCheckurl));
				HttpResponse response = httpClient.execute(method);
				return parse(response,"title");
		    } catch (URISyntaxException e) {
				return 2;
			} catch (ClientProtocolException e) {
				return 2;
			} catch (IOException e) {
				return 2;
			}
	}

	static boolean isStrengthEnough(Context context) {
		// TODO Auto-generated method stub
		if (!isWifiConnected(context))
			return false;

		WifiManager mywifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		int level = calculateSignalLevel((mywifiManager.getConnectionInfo()
				.getRssi()));
		Log.wtf("WifiStrength", level + "");
		if (level >= Vars.thresholdStrength)
			return true;
		else
			return false;

	}

	static void turnWifi(Context context, boolean bool) {
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.setWifiEnabled(bool);
	}

	static int calculateSignalLevel(int rssi) {
		// TODO Auto-generated method stub
		int MIN_RSSI = -100;
		int MAX_RSSI = -55;
		int numLevels = 100;
		if (rssi <= MIN_RSSI) {
			return 0;
		} else if (rssi >= MAX_RSSI) {
			return numLevels - 1;
		} else {
			float inputRange = (MAX_RSSI - MIN_RSSI);
			float outputRange = (numLevels - 1);
			return (int) ((float) (rssi - MIN_RSSI) * outputRange / inputRange);
		}
	}

	public static void attemptLogout(Context context, String loginid,
			String loginpassword) {

		while (!contactServer("193", loginid, loginpassword, context).equals(
				logoutMessage)) {
			Log.wtf("logoutMsg", "Not logged out");
		}

		Vars.isloggedIn = false;
		Vars.loginId = null;
	}

	private static String getloginId(int i) {
		if (i > data.size() - 1)
			return null;
		else
			return data.get(i).getId();
	}

	private static void setStatus(int i, Const status) {
		if (!(i > data.size() - 1))
			data.get(i).setStatus(status);
	}

	private static String getloginPassword(int i) {
		return data.get(i).getPassword();
	}

	public static Const attemptLogin(Context context) {
		int i = 0;
		int r = 0;
		position = 0;
		String message;
		// get all the users in one go and store them.
		DatabaseHandler db = new DatabaseHandler(context);
		data = db.getAllUsers();
		db.close();
		if (data.size() == 0)
			return noUser;
		
		while (getloginId(i) != null) {
			position++;
			if (getChecked(i)) {

				message = contactServer("191", getloginId(i),
						getloginPassword(i), context);
				
				Log.wtf("loginMsg", message);
				if (message.equals(loginMessage)) {
					Vars.isloggedIn = true;
					Vars.loginId = getloginId(i);
					Vars.password = getloginPassword(i);
					setStatus(i, loggedIn);

					return loggedIn;

				} else if (message.equals(cyberlessMessage))
					return cyberLess;
				else if (message.equals(wrongIdpwdmessage))
					setStatus(i, wrongIdPwd);
				else if (message.equals(maxloginMessage)) {
					setStatus(i, maxLogin);
					r++;
				}
			} else
				setStatus(i, notChecked);
			i++;
		}
		if (r != 0)
			return maxLogin;
		else
			return wrongIdPwd;

	}

	private static boolean getChecked(int i) {
		if (data.get(i).getChecked() == 1)
			return true;
		else
			return false;
	}

	public static String contactServer(String loginmode, String loginid,
 			String loginpassword, Context context) {
 		String url = Vars.url;
 		String message = "empty";
 	    DefaultHttpClient httpclient=getNewHttpClient();
 		HttpPost client = new HttpPost(url);
 		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
 		nvps.add(new BasicNameValuePair("mode", loginmode));
 		nvps.add(new BasicNameValuePair("username", loginid));
 		nvps.add(new BasicNameValuePair("password", loginpassword));
 		try {
 			client.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
 		} catch (UnsupportedEncodingException e) {
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
 		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
 		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
			
		} catch (SAXException e) {

			e.printStackTrace();
			
 		}
 		return message;
 	}

	public static Message constructHandlerMessage(Const cnt) {
		Bundle bundle = new Bundle();
		bundle.putString("Const", cnt.name());

		Message msg = new Message();
		msg.setData(bundle);
		return msg;

	}

	public static boolean checkCyberLess(Context context) {
		if (contactServer("191", "200901567", "A", context).equals(
				Vars.cyberlessMessage))
			return true;
		else
			return false;
	}

	private static int parse(HttpResponse response, String tag) {
		if(response==null)
    		return 2;
    	 HttpEntity r_entity = response.getEntity();
         String xmlString = "xmlString";
		try {
			xmlString = EntityUtils.toString(r_entity);
		} catch (ParseException e) {
			return 2;
		} catch (IOException e) {
			return 2;
		}
		
		int length=tag.length()+2;
		int start=xmlString.lastIndexOf("<"+tag+">");
        int end=xmlString.lastIndexOf("</"+tag+">");
        
        if((start==-1)&&(end==-1))
        {
        	 String atag="SCRIPT";
    		 length=atag.length()+2;
    		 start=xmlString.lastIndexOf("<"+atag+">");
             end=xmlString.lastIndexOf("</"+atag+">");
        }
        if(xmlString.substring(start+length, end).equals(Vars.netPageTitle))
         return 1;
        else
        return 0;
		
		
		
		
	}

	
	
	
	public static void sendBroadcast(String str, Context context) {
		context.sendBroadcast(new Intent(str));
	}
	
	private static DefaultHttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
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
