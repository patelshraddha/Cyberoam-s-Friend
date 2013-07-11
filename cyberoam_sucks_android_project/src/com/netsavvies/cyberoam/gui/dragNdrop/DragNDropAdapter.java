/*
 * Copyright (C) 2010 Eric Harlow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netsavvies.cyberoam.gui.dragNdrop;

import java.util.ArrayList;

import com.netsavvies.cyberoam.backend.DatabaseHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public final class DragNDropAdapter extends BaseAdapter implements
		RemoveListener, DropListener {

	private int[] mIds;
	private int[] mLayouts;
	private int[] mcheckbox;
	private int[] mdelete;
	private LayoutInflater mInflater;
	private ArrayList<String> mContent;
	private ArrayList<Boolean> mcheckboxContent;
	private Context mcontext;

	public DragNDropAdapter(Context context, ArrayList<String> content,
			ArrayList<Boolean> checkboxcontent) {
		init(context, new int[] { android.R.layout.simple_list_item_1 },
				new int[] { android.R.id.text1 },
				new int[] { android.R.id.checkbox },
				new int[] { android.R.id.button1 }, content, checkboxcontent);
	}

	public DragNDropAdapter(Context context, int[] itemLayouts, int[] itemIDs,
			int[] checkbox, int[] delete, ArrayList<String> content,
			ArrayList<Boolean> checkboxcontent) {
		init(context, itemLayouts, itemIDs, checkbox, delete, content,
				checkboxcontent);
	}

	private void init(Context context, int[] layouts, int[] ids,
			int[] checkbox, int[] delete, ArrayList<String> content,
			ArrayList<Boolean> checkboxcontent) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mcontext = context;
		mInflater = LayoutInflater.from(context);
		mIds = ids;
		mcheckbox = checkbox;
		mLayouts = layouts;
		mContent = content;
		mcheckboxContent = checkboxcontent;
		mdelete = delete;
	}

	/**
	 * The number of items in the list
	 * 
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		return mContent.size();
	}

	/**
	 * Since the data comes from an array, just returning the index is
	 * sufficient to get at the data. If we were using a more complex data
	 * structure, we would return whatever object represents one row in the
	 * list.
	 * 
	 * @see android.widget.ListAdapter#getItem(int)
	 */
	public String getItem(int position) {
		return mContent.get(position);
	}

	/**
	 * Use the array index as a unique id.
	 * 
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Make a view to hold each row.
	 * 
	 * @see android.widget.ListAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls
		// to findViewById() on each row.
		final ViewHolder holder;
		final int priority = position + 1;
		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(mLayouts[0], null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.delete = (Button) convertView.findViewById(mdelete[0]);
			holder.text = (TextView) convertView.findViewById(mIds[0]);
			holder.checkbox = (CheckBox) convertView.findViewById(mcheckbox[0]);

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		holder.text.setText(mContent.get(position));
		holder.checkbox.setChecked(mcheckboxContent.get(position));

		holder.checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						DatabaseHandler db = new DatabaseHandler(mcontext);
						if (holder.checkbox.isChecked())
							db.updateChecked(db.getUser(priority), 1);
						else
							db.updateChecked(db.getUser(priority), 0);
						db.close();
					}
				});

		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Context context = mcontext;
				AlertDialog.Builder alertbuilder = new AlertDialog.Builder(
						context);
				String message = "Are you sure you want to delete all this user?";
				alertbuilder
						.setTitle("OOPS!!!")
						.setMessage(message)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										DatabaseHandler db = new DatabaseHandler(
												context);
										db.deleteUser(priority);
										db.close();
										mContent.remove(priority-1);
										mcheckboxContent.remove(priority-1);
										notifyDataSetChanged();
										dialog.cancel();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
                                        dialog.cancel();
									}
								});
				AlertDialog alert = alertbuilder.create();
				alert.show();

			}
		});

		return convertView;
	}

	static class ViewHolder {
		Button delete;
		TextView text;
		CheckBox checkbox;
	}

	public void onRemove(int which) {
		if (which < 0 || which > mContent.size())
			return;
		mContent.remove(which);
	}

	public void onDrop(int from, int to) {
		DatabaseHandler db = new DatabaseHandler(mcontext);
		db.updatePriority(db.getUser(from + 1), 1000);
		db.updatePriority(db.getUser(to + 1), from + 1);
		db.updatePriority(db.getUser(1000), to + 1);
		db.close();
		String temp = mContent.get(from);
		mContent.remove(from);
		mContent.add(to, temp);
	}
}