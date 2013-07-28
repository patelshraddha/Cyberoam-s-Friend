package com.netsavvies.cyberoam.gui;

import com.netsavvies.cyberoam.backend.Vars;
import static com.netsavvies.cyberoam.backend.Const.*;
import static com.netsavvies.cyberoam.backend.Methods.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StatusActivity extends Activity{

	private int icon;
	private String message;
	private String description;
	private boolean scrollViewVisible;
	private String buttonString;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		icon=getIcon(current);
		message=getMessage(current);
		buttonString=getButton(current);
		
		
		
		LinearLayout mainLayout=new LinearLayout(this);	
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
	
		
		
		
		ImageView image=new ImageView(this);
		image.setImageResource(icon);
		image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,(float) 0.3));
		
		mainLayout.addView(image);
		setContentView(mainLayout);
	}

}
