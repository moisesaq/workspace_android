package com.example.customtabs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {

	public TabHost tabs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Resources res = getResources();
		tabs = (TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		setNewTab(this, tabs, "tab1", R.string.tab1, R.drawable.ic_adjust_white_18dp, R.id.tab1);
		setNewTab(this, tabs, "tab2", R.string.tab2, R.drawable.ic_assistant_photo_white_18dp, R.id.tab2);
		
//		TabHost.TabSpec spec1 = tabs.newTabSpec("Tab 1");
//		spec1.setContent(R.id.tab1);
//		spec1.setIndicator("TAB 1");
//		tabs.addTab(spec1);
//		
//		spec1 = tabs.newTabSpec("Tab 2");
//		spec1.setContent(R.id.tab2);
//		spec1.setIndicator("TAB 2", res.getDrawable(R.drawable.ic_launcher));
//		tabs.addTab(spec1);
//		
//		tabs.setCurrentTab(0);
	}
	
	 private void setNewTab(Context context, TabHost tabHost, String tag, int title, int icon, int contentID){
		 TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
		 tabSpec.setIndicator(getTabIndicator(context, title, icon));
		 tabSpec.setContent(contentID);
		 tabHost.addTab(tabSpec);
		 
	 }
	 
	 private View getTabIndicator(Context context, int title, int icon){
		 View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
		 ImageView iv = (ImageView)view.findViewById(R.id.imageView);
		 iv.setImageResource(icon);
		 TextView tv = (TextView)view.findViewById(R.id.textView);
		 tv.setText(title);
		 return view;
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
