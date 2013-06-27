package com.netsavvies.cyberoam.gui.dragNdrop;

import java.util.ArrayList;
import java.util.List;


import com.netsavvies.cyberoam.R;
import com.netsavvies.cyberoam.backend.*;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DatabaseHandler db =new DatabaseHandler(this);
        TextView display=(TextView)findViewById(R.id.demot);
		
		
		/**
		 * CRUD Operations
		 * */
		// Inserting Contacts
		/*Log.d("Insert: ", "Inserting ..");
		UserDetails user1=new UserDetails(1,"201101123","agytagga",1);
        UserDetails user2=new UserDetails(2,"201101124","agytagga",1);
        UserDetails user3=new UserDetails(3,"201101125","agytagga",1);
        UserDetails user4=new UserDetails(4,"201101126","agytagga",1);
        UserDetails user5=new UserDetails(5,"201101126","agytagga",1);
        UserDetails user6=new UserDetails(6,"2011011sj26","agytagga",1);
        UserDetails user7=new UserDetails(7,"20110112zzjbh6","agytagga",1);
        
        Log.d("Reading: ", "Reading all contacts..");
        
        db.addUser(user1);
        db.addUser(user2);
        db.addUser(user3);
        db.addUser(user4);
        db.addUser(user5);
        db.addUser(user6);
        db.addUser(user7);
      
        
        
        UserDetails user15=new UserDetails(15,"201101123","agytagga",1);
        
        
        
       //db.updateContact(user15,4);
        db.deleteContact(1);
        
        display.setText(""+db.getContactsCount());
        
       // display.setText(db.getUser(1).getId());
        Log.d("Reading: ", "here");

		// Reading all contacts
		Log.d("Reading: ", "Reading all contacts..");
		ArrayList<UserDetails> contacts = db.getAllUsers();

		String id="\n";
		for (UserDetails cn : contacts) {
			String log = "Id: " + cn.getId();
			id=id+cn.getPriority();
			id=id+"\n";
			// Writing Contacts to log
			Log.d("Name: ", log);

		}
		
		id=id+db.getUser(7).getId();
		
		
		db.deleteAllUsers();
		DatabaseHandler db1 =new DatabaseHandler(this);
		id=""+db1.getContactsCount();
		display.setText(id);
        */
        display.setText(Static.iswifiConnected(this)+"");
        
	}

}
