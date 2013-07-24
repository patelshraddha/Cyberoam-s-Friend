package com.netsavvies.cyberoam.gui.dragNdrop;

import java.util.ArrayList;

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

import com.netsavvies.cyberoam.backend.DatabaseHandler;

public final class DragNDropAdapter extends BaseAdapter implements DropListener {

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

	public int getCount() {
		return mContent.size();
	}

	public String getItem(int position) {
		return mContent.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final int priority = position + 1;
		if (convertView == null) {
			convertView = mInflater.inflate(mLayouts[0], null);
			holder = new ViewHolder();
			holder.delete = (Button) convertView.findViewById(mdelete[0]);
			holder.text = (TextView) convertView.findViewById(mIds[0]);
			holder.checkbox = (CheckBox) convertView.findViewById(mcheckbox[0]);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
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
										mContent.remove(priority - 1);
										mcheckboxContent.remove(priority - 1);
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