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
import android.os.Handler;
import android.os.IBinder;
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

	// all commands

	private void init() {
		// set bools
		set(Const.c, false);
		set(Const.l, false);
		set(Const.str, false);
		set(Const.wifi, false);
		set(Const.isStopped,false);
		set(Const.cyberLess,false);
		
		
		// set handler and tasks
		run_hs.put(Const.c, new Runnable() {

			@Override
			public void run() {
				dispatch(Const.top, Const.cybCheck);
			}

		});
		run_hs.put(Const.l, new Runnable() {

			@Override
			public void run() {
				runExist_hs.put(Const.l, false);
				runExist_hs.put(Const.lF, false);
				dispatch(Const.top, Const.reLogin);
			}

		});

		run_hs.put(Const.net, new Runnable() {

			@Override
			public void run() {
				runExist_hs.put(Const.net, false);
				dispatch(Const.top, Const.netCheck);
			}
		});

		runExist_hs.put(Const.c, false);
		runExist_hs.put(Const.l, false);
		runExist_hs.put(Const.lF, false);
		runExist_hs.put(Const.net, false);

		handler = new Handler();
		
		//receivers
		bcr_hs.put(Const.wifi, new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
					if(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
						dispatch(Const.top, Const.wifiConnected);
					}
					else{
						dispatch(Const.top, Const.wifiDisconnected);
					}
				}	
			}
		});
		bcr_hs.put(Const.wifiLocha, new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
					if(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
						dispatch(Const.top, Const.wifiKaLochaTheekKaro);
					}
				}	
			}
		});
		bcr_hs.put(Const.str, new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
					/*
					 *	check(STR_C >= 30)
						{+
							get(str)
							{+
							}+
							{-
								set(STR+)
								dispatch(top,strChange)
							}-
							
						}+
						{-
							
							get(str)
							{+
								set(STR-)
								dispatch(top,strChange)
							}+
							{-
							
							}-
							
						}-
					 */
				}
			}
		});

	}

	private void set(Const key, Boolean bool) {
		bool_hs.put(key, bool);
	}

	private boolean get(Const key) {
		if (bool_hs.containsKey(key)) {
			return bool_hs.get(key);
		} else{
			bool_hs.put(key, false);
			return false;
		}
			
	}

	private boolean check(Const key) {
		switch (key) {
		case wifi:
			get(key);
			break;
		case c:
		case l:
			get(key);
			break;
		case str:
			
			break;

		default:
			// locho
		}
	}

	private void timer(Const key,boolean bool){
		switch(key){
		case l:
			if(bool){
				
				if( ! (runExist_hs.get(Const.l) || runExist_hs.get(Const.lF)) ){
					handler.postDelayed(run_hs.get(Const.l), Static.loginInterval);
					runExist_hs.put(Const.l, true);
				}
			
			}
			else {

				handler.removeCallbacks(run_hs.get(Const.l));
				runExist_hs.put(Const.l, false);
			}
			
			break;
			
		case lF:
			if(bool){
				
				if(runExist_hs.get(Const.l) ){
					handler.removeCallbacks(run_hs.get(Const.l));
					runExist_hs.put(Const.l, false);
					
				}
				else if (!runExist_hs.get(Const.lF)){
					handler.postDelayed(run_hs.get(Const.l), Static.loginTrialInterval);
					runExist_hs.put(Const.lF, true);
				}
			
			}
			else {

				handler.removeCallbacks(run_hs.get(Const.l));
				runExist_hs.put(Const.l, false);
			}
			
			break;
		case c:
			/*if(option)
				timerC.schedule(taskC,10,Static);
			else
				timerC.cancel();
				*/
			break;
		case net:
			break;
		}
	}
	
	private void receiver(Const key, boolean bool){
		switch(key){
		case wifi:
			if(bool){
				registerReceiver(bcr_hs.get(Const.wifi), new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
			} else {
				unregisterReceiver(bcr_hs.get(Const.wifi));
			}	
		break;
		
		case wifiLocha:
			if(bool){
				registerReceiver(bcr_hs.get(Const.wifiLocha), new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
			} else {
				unregisterReceiver(bcr_hs.get(Const.wifiLocha));
			}
		break;
		
		case str:
			if(bool){
				registerReceiver(bcr_hs.get(Const.str), new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
			} else {
				unregisterReceiver(bcr_hs.get(Const.str));
			}
		break;
		
		default:
			//locho
		
		
		}
	}
	
	private void dispatch(Const level, Const command) {
		switch (level) {
		case wifi: {
			switch (command) {
			case stop:
			case wrongIdPwd:
				if (!get(Const.isStopped)) {
					set(Const.isStopped,true);
					dispatch(Const.wifi, command);

				}
				break;
			case start:
				if (get(Const.isStopped)) {
					set(Const.isStopped,false);
					dispatch(Const.wifi, command);
				}

				break;
			case restart:
				dispatch(Const.top, Const.stop);
				dispatch(Const.top, start);

				break;
			default:
				if (!isStopped) {
					dispatch(WIFI, command);
				}

				break;
			}
		}
			break;
		case WIFI: {
			// after getting ip address
			switch (command) {
			case wifiConnected:
			case wifiDisconnected:
			case stop:
				dispatch(NET, command);
				break;
			case start:
				if (Static.iswifiConnected(getApplicationContext())) {
					dispatch(NET, command);
				} else {
					while (!Static.iswifiConnected(getApplicationContext()))
						;
					dispatch(NET, command);
				}
				break;
			case wifiKaLochaAaya:
				wifikalocha();
				break;
			case wifiKaLochaTheekKaro:
				wifikalochatheekkaro();
				break;
			case wifiKaBahotBadaLocha:
				// don't know
				break;
			default:
				dispatch(NET, command);
				break;
			}
		}
			break;
		case NET: {
			switch (command) {
			case wifiKaLochaTheekKaro:
			case strChange:
			case reLogin:
			case cybCheck:
				dispatch(STR, command);
				break;
			case wifiDisconnected:
			case wrongIdPwd:
			case stop:
				timer(NET, false,0);
				NET_P = false;
				dispatch(STR, command);
				break;
			case wifiConnected:
			case start:
				timer(NET, true,0);
				if (Static.isConnectionAlive(getApplicationContext()) == 1) {
					NET_P = true;
					cyberless = true;
					// inform that cyberless login or another wifi
				} else {
					dispatch(STR, command);
				}
				break;
			case netCheck:
				int reason = Static.isConnectionAlive(getApplicationContext());
				if (reason == 1)
					NET_P = true;
				else {
					NET_P = false;
					switch (reason) {
					case 0:
						dispatch(STR, notLoggedIn);
						break;
					case 2:
						dispatch(STR, noNet);
						break;
					}
				}
				break;
			case netRecheck:
				if (Static.isConnectionAlive(getApplicationContext()) == 1)
					NET_P = true;
				else {
					NET_P = false;
					// notify logged in but no net
				}
				break;

			}

		}
			break;
		case STR: {
			switch (command) {

			case wifiKaLochaTheekKaro:
				dispatch(C, command);
				return;
			case wifiConnected:
			case start:
				receiver(STR, true);
				if (Static.getWifiStrength(getApplicationContext())) {
					STR_P = true;
					dispatch(C, command);
				}
				return;
			case wifiDisconnected:
			case wrongIdPwd:
			case stop:
				receiver(STR, false);
				dispatch(C, command);
				return;
			case strChange:
				if (Static.getWifiStrength(getApplicationContext())) {
					if (!STR_P) {
						STR_P = true;
						dispatch(C, highSTR);
					}
				} else {
					if (STR_P) {
						STR_P = false;
						dispatch(C, lowSTR);
						
					}
				}
				return;
			}
		}
			break;
		case C: {
			if (Static.isCyberoamAvailbale()) {
				timer(C, false,0);
				if (!C_P)
					C_P = true;
				switch (command) {
				case wifiDisconnected:
				case stop:
					dispatch(L, wifiDisconnected);
					C_P = false;
					break;
				case wifiKaLochaTheekKaro:
					dispatch(L, wifiKaLochaTheekKaro);
					C_P = false;
					break;
				case lowSTR:
					dispatch(L, lowSTR);
					break;
				case wrongIdPwd:
					break;
				default:
					dispatch(L, command);
					break;
				}
			} else {
				switch (command) {
				case noNet:
				case cybCheck:
				case wifiConnected:
				case start:
				case highSTR:
				case reLogin:
					timer(C, true,0);
					break;

				case wifiKaLochaTheekKaro: {
					dispatch(Const.top, wifiKaBahotBadaLocha);
					timer(C, false,0);
				}
					break;
				default:
					timer(C, false,0);
					break;
				}

				if (C_P) {
					dispatch(L, noCyb);
					C_P = false;
				}

			}
		}
			break;
		case L: {
			timer(L, false,1);
			switch (command) {
			case noCyb:
				if (L_P)
					// big locha
					L_P = false;
				break;
			case lowSTR:
				if (L_P) {
					// attemptLogout();
					L_P = false;
				}
				break;

			case highSTR:
			case cybCheck:
			case reLogin:
			case wifiConnected:
			case noNet:
			case notLoggedIn:
			case start: {
				if (command == notLoggedIn)
					L_P = false;
				if ((L_P) && (command != reLogin)) {
					if (command == noNet) {
						// inform that logged in but still no net
					}
				} else {
					int reason = attemptLogin();
					if (reason == 1) {
						L_P = true;
						timer(L, true,1);
						dispatch(Const.top, netRecheck);
					}
					{
						L_P = false;
						switch (reason) {
						case maxLogin:
							timer(L, true,0);
							break;
						case wrongIdPwd:
							dispatch(Const.top, wrongIdPwd);
							break;
						default:
							// todo stop timer and receivers until user id
							// received
						}
					}
				}

			}
				break;

			case wifiKaLochaTheekKaro:
			case stop: {
				// attemptLogout();
			}
				break;

			case wifiDisconnected: {
				if (L_P)
					dispatch(Const.top, wifiKaLochaAaya);
			}
				break;
			}
		}
			break;
		}
	}

	private void receiver(int wIFI2, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// set Timers
		init();
		

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(this.receiversForCybService);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String action = intent.getStringExtra("action");
		Static.isloggedIn = false;
		Static.loginId = null;

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

	public int attemptLogin() {
		int i = 1;
		String message;
		while (getloginId(i) != null) {
			message = contactServer("191", getloginId(i), getloginPassword(i),
					getApplicationContext());
			if (message.equals("You have successfully logged in")
					|| message
							.equals("You are already logged in as a clientless user")) {
				Static.isloggedIn = true;
				Static.loginId = getloginId(i);
				break;
			}
			i++;
		}
		/*	if (Static.isloggedIn)
				return true;
			else
				return false;
		*/
		return 0;
	}

	public void attemptLogout(Context context, String loginid,
			String loginpassword) {
		contactServer("193", loginid, loginpassword, context);

	}

	private String contactServer(String loginmode, String loginid,
			String loginpassword, Context context) {
		String url = Static.url;
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
