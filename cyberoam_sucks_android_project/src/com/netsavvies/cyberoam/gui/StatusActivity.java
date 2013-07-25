package com.netsavvies.cyberoam.gui;

import com.netsavvies.cyberoam.backend.Vars;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StatusActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout mainLayout=new LinearLayout(this);	
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		ImageView image=new ImageView(this);
		image.setImageResource(Vars.imageid);
		//image.setLayoutParams(new LayoutParams(,0));
		
		
		setContentView(mainLayout);
	}

}
