package com.netsavvies.cyberoam.backend;

/*
 * The service which performs the actual tasks. Started by Control.class/CybApp.class or by the system at boot and not directly accessible by gui
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.netsavvies.cyberoam.gui.InformGui;

public class CybService extends Service {

	int connection;

	private BroadcastReceiver receiversForCybService;
	// all events
	// all states
	private Hashtable<Const, Boolean> bool_hs;
	private Handler handler;
	private Hashtable<Const, Runnable> run_hs;
	private Hashtable<Const, Boolean> runExist_hs;

	private Hashtable<Const, BroadcastReceiver> bcr_hs;
	private Hashtable<Const, Boolean> bcrExist_hs;

	// all commands

	private void init() {

		bool_hs = new Hashtable<Const, Boolean>();
		run_hs = new Hashtable<Const, Runnable>();
		runExist_hs = new Hashtable<Const, Boolean>();
		bcr_hs = new Hashtable<Const, BroadcastReceiver>();
		bcrExist_hs = new Hashtable<Const, Boolean>();

		// set bools
		set(Const.c, false);
		set(Const.l, false);
		set(Const.str, false);
		set(Const.wifi, false);
		set(Const.isStopped, false);
		set(Const.cyberLess, false);

		// set handler and tasks
		run_hs.put(Const.c, new Runnable() {

			@Override
			public void run() {
				Log.wtf("hadler cybcheck", runExist_hs.put(Const.c, false) + "");
				runExist_hs.put(Const.c, false);
				timer(Const.c, true);
				dispatch(Const.top, Const.cybCheck);
			}

		});
		run_hs.put(Const.l, new Runnable() {

			@Override
			public void run() {
				Log.wtf("hadler logincheck", runExist_hs.put(Const.c, false) + "");
				if (runExist_hs.get(Const.l)) {
					runExist_hs.put(Const.l, false);
					timer(Const.l, true);
				}
				if (runExist_hs.get(Const.lF)) {
					runExist_hs.put(Const.lF, false);
					timer(Const.lF, true);
				}

				dispatch(Const.top, Const.reLogin);
			}

		});

		run_hs.put(Const.net, new Runnable() {

			@Override
			public void run() {
				Log.wtf("hadler netcheck", runExist_hs.put(Const.c, false) + "");
				runExist_hs.put(Const.net, false);
				timer(Const.net, true);
				dispatch(Const.top, Const.netCheck);

			}
		});

		runExist_hs.put(Const.c, false);
		runExist_hs.put(Const.l, false);
		runExist_hs.put(Const.lF, false);
		runExist_hs.put(Const.net, false);

		handler = new Handler();

		// receivers
		bcr_hs.put(Const.wifi, new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
					/*if (intent.getBooleanExtra(
							WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
						dispatch(Const.top, Const.wifiConnected);
					} else {
						dispatch(Const.top, Const.wifiDisconnected);
					}*/
				
				if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					if (Methods.isWifiConnected(context)) {
					
						if(!get(Const.wifi)){
							set(Const.wifi,true);
							dispatch(Const.top, Const.wifiConnected);
						}
					
					} else if (get(Const.wifi)){
						set(Const.wifi,false);
						dispatch(Const.top, Const.wifiDisconnected);
					}
				}
			}
		});
		bcr_hs.put(Const.wifiLocha, new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					if (Methods.isWifiConnected(context)   ){
						if(!get(Const.wifiForLocha)){
							set(Const.wifiForLocha,true);
							dispatch(Const.top, Const.wifiKaLochaTheekKaro);
						}
						
					}
				}
			}
		});
		bcr_hs.put(Const.str, new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
					// TODO: use extra
					if (check(Const.str)) {
						if (!get(Const.str)) {
							set(Const.str, true);
							dispatch(Const.top, Const.strChange);
						}
					} else {
						if (get(Const.str)) {
							set(Const.str, false);
							dispatch(Const.top, Const.strChange);
						}
					}
				}
			}
		});

		bcrExist_hs.put(Const.wifi, false);
		bcrExist_hs.put(Const.wifiLocha, false);
		bcrExist_hs.put(Const.str, false);

	}

	private void execute(Const key, boolean bool) {
		Log.wtf("Execute", key.name() + " " + bool);
		switch (key) {
		case wifi:
			Methods.turnWifi(this, bool);

		}
	}

	private void set(Const key, Boolean bool) {
		Log.wtf("set", key.name() + " " + bool);
		bool_hs.put(key, bool);
		
	}

	private boolean get(Const key) {

		if (bool_hs.containsKey(key)) {
			Log.wtf("get", key.name() + " " + bool_hs.get(key).toString());
			return bool_hs.get(key);
		} else {
			bool_hs.put(key, false);
			Log.wtf("get", key.name() + " false");
			return false;
		}

	}

	private boolean check(Const key) {
		boolean result = false;
		switch (key) {
		case wifi:
			result =  Methods.isWifiConnected(getApplicationContext());
			break;
		case c:
			result = Methods.isCyberoamAvailbale(getApplicationContext());
			break;
		case l:
			result= get(key);
		case str:
			result= Methods.isStrengthEnough(getApplicationContext());
         }
		Log.wtf("check", key.name() + " " + result);
		return result;
		
	}

	private void timer(Const key, boolean bool) {
		Log.wtf("timer", key.name() + " " + bool);

		switch (key) {
		case l:
			if (bool) {

				if (!(runExist_hs.get(Const.l) || runExist_hs.get(Const.lF))) {
					handler.postDelayed(run_hs.get(Const.l), Vars.loginInterval);
					runExist_hs.put(Const.l, true);

				}

			} else {

				handler.removeCallbacks(run_hs.get(Const.l));
				runExist_hs.put(Const.l, false);
			}

			break;

		case lF:
			if (bool) {

				if (runExist_hs.get(Const.l)) {
					timer(Const.l, false);

				} else if (!runExist_hs.get(Const.lF)) {
					handler.postDelayed(run_hs.get(Const.l),Vars.loginTrialInterval);
					runExist_hs.put(Const.lF, true);
				}

			} else {

				handler.removeCallbacks(run_hs.get(Const.l));
				runExist_hs.put(Const.l, false);
			}

			break;
		case c:
			if (bool) {
				if (!(runExist_hs.get(Const.c))) {
					handler.postDelayed(run_hs.get(Const.c),
							Vars.cybCheckInterval);
					runExist_hs.put(Const.c, true);
				}

			} else {
				handler.removeCallbacks(run_hs.get(Const.c));
				runExist_hs.put(Const.c, false);
			}
			break;
		case net:
			if (bool) {
				if (!(runExist_hs.get(Const.net))) {
					handler.postDelayed(run_hs.get(Const.net),
							Vars.netCheckInterval);
					runExist_hs.put(Const.net, true);
				}

			} else {

				handler.removeCallbacks(run_hs.get(Const.net));
				runExist_hs.put(Const.net, false);
			}
			break;
		}
	}

	private void receiver(Const key, boolean bool) {
		Log.wtf("reciever", key.name() + " " + bool);

		switch (key) {
		case wifi:
			if (bool && !bcrExist_hs.get(Const.wifi)) {
				registerReceiver(bcr_hs.get(Const.wifi), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
				bcrExist_hs.put(Const.wifi, true);
			} else if (bcrExist_hs.get(Const.wifi)) {
				unregisterReceiver(bcr_hs.get(Const.wifi));
				bcrExist_hs.put(Const.wifi, false);
			}
			break;

		case wifiLocha:
			if (bool && !bcrExist_hs.get(Const.wifiLocha)) {
				registerReceiver(bcr_hs.get(Const.wifiLocha), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
				bcrExist_hs.put(Const.wifiLocha, true);
			} else if (bcrExist_hs.get(Const.wifiLocha)) {
				unregisterReceiver(bcr_hs.get(Const.wifiLocha));
				bcrExist_hs.put(Const.wifiLocha, false);
			}
			break;

		case str:
			if (bool && !bcrExist_hs.get(Const.str)) {
				registerReceiver(bcr_hs.get(Const.str), new IntentFilter(
						WifiManager.RSSI_CHANGED_ACTION));
				bcrExist_hs.put(Const.str, true);
			} else if (bcrExist_hs.get(Const.str)) {
				unregisterReceiver(bcr_hs.get(Const.str));
				bcrExist_hs.put(Const.str, false);
			}
			break;

		default:
			// locho

		}
	}

	private void dispatch(Const level, Const command) {
		Log.wtf("dispatch", level.name() + " " + command.name());

		switch (level) {
		case top:
			switch (command) {
			case stop:
			case wrongIdPwd:
				if (!get(Const.isStopped)) {
					set(Const.isStopped, true);
					dispatch(Const.wifi, command);
				}
				break;
			case start:
				Log.wtf("blah", "b");
				if (!get(Const.isStopped)) {
					dispatch(Const.wifi, command);
				}

				break;
			case restart:
				dispatch(Const.top, Const.stop);
				dispatch(Const.top, Const.start);

				break;
			default:
				if (!get(Const.isStopped)) {
					dispatch(Const.wifi, command);
				}
				break;
			}

			break; // Top Over
			
		case wifi: {
			// after getting ip address
			switch (command){
			case wifiConnected:
			case wifiDisconnected:
			case stop:
				dispatch(Const.net, command);
				break;
			case start:
				if (check(Const.wifi)) {
					set(Const.wifi,true);
					receiver(Const.wifi, true);
					dispatch(Const.net, command);
				} else
					receiver(Const.wifi, true);
				
				break;
			case wifiKaLochaAaya:
				receiver(Const.wifi, false);
				receiver(Const.wifiLocha, true);
				execute(Const.wifi, true);
				break;
			case wifiKaLochaTheekKaro:
				dispatch(Const.net, Const.wifiKaLochaTheekKaro);
				receiver(Const.wifiLocha, false);
				execute(Const.wifi,false);
				set(Const.wifiForLocha,true);
				receiver(Const.wifi, true);
				break;
			case wifiKaBahotBadaLocha:
				// don't know
				break;
			default:
				dispatch(Const.net, command);
				break;
			}
		}
			break; // Wifi over

		case net: {
			switch (command) {
			case wifiKaLochaTheekKaro:
			case strChange:
			case reLogin:
			case cybCheck:
				dispatch(Const.str, command);
				break;
			case wifiDisconnected:
			case wrongIdPwd:
			case stop:
				timer(Const.net, false);
				set(Const.net, false);
				dispatch(Const.str, command);
				break;
			case wifiConnected:
			case start:
				timer(Const.net, true);
				if (Methods.isConnectionAlive(getApplicationContext()) == 1) {
					set(Const.net, true);
					set(Const.cyberLess, true);
					// inform that cyberless login or another wifi
				} else {
					dispatch(Const.str, command);
				}
				break;
			case netCheck:
				int retValue = Methods
						.isConnectionAlive(getApplicationContext());
				if (retValue == 1)
					set(Const.net, true);
				else {
					set(Const.net, false);
					switch (retValue) {
					case 0:
						dispatch(Const.net, Const.notLoggedIn);
						break;
					case 2:
						dispatch(Const.str, Const.noNet);
						break;
					}
				}
				break;
			case netRecheck:
				if (Methods.isConnectionAlive(getApplicationContext()) == 1)
					set(Const.net, true);
				else {
					set(Const.net, false);
					// notify logged in but no net
				}
				break;

			}

		}
			break; // Net over

		case str:
			switch (command) {
			case noNet:
			case notLoggedIn:
				set(Const.str, check(Const.str));
				break;

			case wifiKaLochaTheekKaro:
				dispatch(Const.c, command);
				return;
			case wifiConnected:
			case start:
				if (check(Const.str)) {
					set(Const.str, true);
					receiver(Const.str, true);
					dispatch(Const.c, command);
				} else {
					receiver(Const.str, true);
				}
				return;
			case wifiDisconnected:
			case wrongIdPwd:
			case stop:
				receiver(Const.str, false);
				dispatch(Const.c, command);
				return;
			case strChange:
				if (get(Const.str)) {
					dispatch(Const.c, Const.highSTR);
				} else {
					dispatch(Const.c, Const.lowSTR);
				}
				return;
			}

			if (get(Const.str)) {
				switch (command) {
				case cybCheck:
				case reLogin:
				case noNet:
				case notLoggedIn:
					dispatch(Const.c, command);
					break;
				default:
					// locha
				}
			}
			break;

		case c: {
			if (check(Const.c)) {
				timer(Const.c, false);

				if (!get(Const.c))
					set(Const.c, true);

				switch (command) {
				case stop:
					set(Const.c, false);
					dispatch(Const.l, command);
					break;
				case wifiKaLochaTheekKaro:
					dispatch(Const.l, Const.wifiKaLochaTheekKaro);
					set(Const.c, false);
					break;
				case wrongIdPwd:
					break;
				default:
					dispatch(Const.l, command);
					break;
				}
			} else {

				switch (command) {
				case wifiDisconnected:
				case stop:
					set(Const.c, false);
					timer(Const.c, false);
					dispatch(Const.l, command);
					break;

				case noNet:
				case cybCheck:
				case wifiConnected:
				case start:
				case highSTR:
				case reLogin:
					timer(Const.c, true);
					break;

				case wifiKaLochaTheekKaro:
					dispatch(Const.top, Const.wifiKaBahotBadaLocha);
					timer(Const.c, false);
					break;

				default:
					timer(Const.c, false);
					break;
				}

				if (get(Const.c)) {
					dispatch(Const.l, Const.noCyb);
					set(Const.c, false);
				}

			}
		}
			break; // C over

		case l: {
			timer(Const.l, false);
			switch (command) {
			case noCyb:
				if (get(Const.l))
					// big locha
					set(Const.l, false);
				break;
			case lowSTR:
				if (get(Const.l)) {
					attemptLogout(getApplicationContext(), Vars.loginId,
							Vars.password);
					set(Const.l, false);
				}
				break;

			case notLoggedIn:
				set(Const.l, false);
			case highSTR:
			case cybCheck:
			case reLogin:
			case wifiConnected:
			case noNet:
			case start:
				if ((get(Const.l)) && (command != Const.reLogin)) {
					if (command == Const.noNet) {
						// inform that logged in but still no net
					}
				} else {
					Const reason = attemptLogin();
					if (reason == Const.loggedIn) {
						set(Const.l, true);
						timer(Const.l, true);
						dispatch(Const.top, Const.netRecheck);
					} else {
						set(Const.l, false);
						switch (reason) {
						case maxLogin:
							timer(Const.lF, true);
							break;
						case wrongIdPwd:
							dispatch(Const.top, Const.wrongIdPwd);
							break;
						default:
							// todo stop timer and receivers until user id
							// received
						}
					}
				}

				break;

			case wifiKaLochaTheekKaro:
			case stop:
				if (get(Const.l))
					attemptLogout(getApplicationContext(), Vars.loginId,
							Vars.password);
				set(Const.l, false);
				break;

			case wifiDisconnected:
				if (get(Const.l))
					dispatch(Const.top, Const.wifiKaLochaAaya);
				break;
			default:
				break;
			}
		}
		default:
			break;
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		init();
		dispatch(Const.top, Const.start);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dispatch(Const.top, Const.stop);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// String action = intent.getStringExtra("action");
		Vars.isloggedIn = false;
		Vars.loginId = null;
		
		// Build notification
		// Actions are just fake

		// Stop the activity if rebooted.
		/*
		 * if (action == "android.intent.action.BOOT_COMPLETED")
		 * InformGui.Notify("Boot completed", getApplicationContext());
		 */
		/*
		 * connection = Static.getConnectivityStatus(getApplicationContext());
		 * if (connection == 0) InformGui.Notify("No data connection available",
		 * getApplicationContext()); else if (connection == 2) {
		 * InformGui.Notify("Using Data connection", getApplicationContext());
		 * Toast.makeText(getApplicationContext(), "Using Data connection",
		 * Toast.LENGTH_SHORT).show();// mobile connection } else { if
		 * (Static.isCyberoamAvailbale()) { if (attemptLogin()) {
		 * InformGui.Notify("Logged in by " + Static.loginId,
		 * getApplicationContext()); InformGui.loggedIn(getApplicationContext(),
		 * Static.loginId); timer.schedule(task, Static.loginInterval,
		 * Static.loginInterval); } else { InformGui.Notify("Logged failed",
		 * getApplicationContext());
		 * InformGui.loginFailed(getApplicationContext()); } } else { // using
		 * some other wifi or because the wifi strength is less
		 * InformGui.Notify("Supreme can't be reached",
		 * getApplicationContext()); } }
		 */
		// Static.receiverContext=getApplicationContext();
		return super.onStartCommand(intent, flags, startId);

	}

	public Const attemptLogin() {
		int i = 1;
		int maxLogin = 0;

		String message;
		// while (getloginId(i) != null) {
		message = contactServer("191", getloginId(i), getloginPassword(i),
				getApplicationContext());
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

	public void attemptLogout(Context context, String loginid,	String loginpassword) {
		Log.wtf("logoutMsg", contactServer("193", loginid, loginpassword, context));

	}

	private String contactServer(String loginmode, String loginid,
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

	private String getloginId(int i) {
		/*SharedPreferences settings = getApplicationContext()
				.getSharedPreferences("user_details", 0);
		String user = settings.getString("user" + i, null);
		return user;*/
		return "200901146";
	}

	private String getloginPassword(int i) {
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences("user_details", 0);
		String password = settings.getString("password" + i, null);
		return "reset123";
	}

}
