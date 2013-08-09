package com.netsavvies.cyberoam.gui.dragNdrop;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.netsavvies.cyberoam.R;
import com.netsavvies.cyberoam.backend.Const;
import com.netsavvies.cyberoam.backend.Control;
import com.netsavvies.cyberoam.backend.DatabaseHandler;
import com.netsavvies.cyberoam.backend.Methods;
import com.netsavvies.cyberoam.backend.UserDetails;

public class DragNDropListActivity extends ListActivity {

	private ArrayList<UserDetails> users;
	private ArrayList<String> content;
	private ArrayList<Boolean> checkboxcontent;
	private DragNDropAdapter adapter;
    static boolean change;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dragndroplistview);
        change=false;
		content = new ArrayList<String>();
		checkboxcontent = new ArrayList<Boolean>();
		construct();
		adapter = new DragNDropAdapter(this, new int[] { R.layout.dragitem },
				new int[] { R.id.TextView01 }, new int[] { R.id.checkBox },
				new int[] { R.id.deleteuser }, content, checkboxcontent);
		setListAdapter(adapter);
		ListView listView = getListView();

		if (listView instanceof DragNDropListView) {
			((DragNDropListView) listView).setDropListener(mDropListener);
			((DragNDropListView) listView).setRemoveListener(mRemoveListener);
			((DragNDropListView) listView).setDragListener(mDragListener);
		}
	}

	private DropListener mDropListener = new DropListener() {
		public void onDrop(int from, int to) {
			ListAdapter adapter = getListAdapter();
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onDrop(from, to);
				getListView().invalidateViews();
			}
		}
	};

	private RemoveListener mRemoveListener = new RemoveListener() {
		public void onRemove(int which) {
			ListAdapter adapter = getListAdapter();
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onRemove(which);
				getListView().invalidateViews();
			}
		}
	};

	private DragListener mDragListener = new DragListener() {

		int backgroundColor = 0xe0103010;
		int defaultBackgroundColor;

		public void onDrag(int x, int y, ListView listView) {
		}

		public void onStartDrag(View itemView) {
			itemView.setVisibility(View.INVISIBLE);
			defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
			itemView.setBackgroundColor(backgroundColor);
			ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
			if (iv != null)
				iv.setVisibility(View.INVISIBLE);
		}

		public void onStopDrag(View itemView) {
			itemView.setVisibility(View.VISIBLE);
			itemView.setBackgroundColor(defaultBackgroundColor);
			ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
			if (iv != null)
				iv.setVisibility(View.VISIBLE);
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			addUser();
			return true;
		case R.id.deletesettings:
			deleteallUsers();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void deleteallUsers() {
		final Context context = this;
		AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
		String message = "Are you sure you want to delete all the users?";
		alertbuilder
				.setTitle("OOPS!!!")
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								DatabaseHandler db = new DatabaseHandler(
										context);
								db.deleteAllUsers();
								db.close();
								content.clear();
								checkboxcontent.clear();
								adapter.notifyDataSetChanged();
								dialog.cancel();
								change=true;
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});
		AlertDialog alert = alertbuilder.create();
		alert.show();

	}

	private void addUser() {
		final Context context = this;
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.adduserdialog);
		dialog.setTitle("Add Another User");
		dialog.show();
		final Button save = (Button) dialog.findViewById(R.id.save);
		final Button cancel = (Button) dialog.findViewById(R.id.cancel);
		final EditText userId = (EditText) dialog.findViewById(R.id.userid);
		final EditText userPassword = (EditText) dialog
				.findViewById(R.id.userpassword);

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if ((userPassword.getText().length() == 0)
						|| (userId.getText().length() == 0)) {
					String message = null;

					if ((userPassword.getText().length() == 0)
							&& (userId.getText().length() == 0))
						message = "Empty password and empty UserId";
					else if (userPassword.getText().length() != 0)
						message = "Empty User Id";
					else
						message = "Empty password";
					AlertDialog.Builder alertbuilder = new AlertDialog.Builder(
							context);
					alertbuilder
							.setTitle("OOPS!!!")
							.setMessage(message)
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = alertbuilder.create();
					alert.show();

				} else {
					
					DatabaseHandler db = new DatabaseHandler(context);
					db.addUser(new UserDetails(1000, userId.getText()
							.toString(), userPassword.getText().toString(), 1));
					content.add(db.getUser(db.getUsersCount()).getId());
					Boolean checked;
					if (db.getUser(db.getUsersCount()).getChecked() == 1)
						checked = true;
					else
						checked = false;
					checkboxcontent.add(checked);
					
					
					db.close();

					adapter.notifyDataSetChanged();
					dialog.dismiss();
					change=true;
				}
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(change)
			Methods.sendBroadcast(Const.restart.toString(),this);
		
	}

	private void construct() {
		DatabaseHandler db = new DatabaseHandler(this);
		users = db.getAllUsers();
		for (int i = 0; i < users.size(); i++) {
			content.add(users.get(i).getId());
			Boolean checked;
			if (users.get(i).getChecked() == 1)
				checked = true;
			else
				checked = false;
			checkboxcontent.add(checked);
		}

		db.close();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(change)
			Methods.sendBroadcast(Const.restart.toString(),this);
	}
	
	

}