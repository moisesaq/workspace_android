package com.example.customtabs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {

	public TabHost tabs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Resources res = getResources();
		tabs = (TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		
//		setupTab(new TextView(this), "TAB 1");
//		setupTab(new TextView(this), "TAB 2");
		
		TabHost.TabSpec spec1 = tabs.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("TAB 1");
		tabs.addTab(spec1);
		
		spec1 = tabs.newTabSpec("Tab 2");
		spec1.setContent(R.id.tab2);
		spec1.setIndicator("TAB 2", res.getDrawable(R.drawable.ic_launcher));
		tabs.addTab(spec1);
		
		tabs.setCurrentTab(0);
	}
	
//	private void setupTab(final View view, final String tag){
//		View tabview = createTabView(tabs.getContext(), tag);
//	    TabSpec setContent = tabs.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
//
//			public View createTabContent(String tag) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		});
//	    tabs.addTab(setContent);
//	}
//	
//	private static View createTabView(final Context context, final String text){
//		 View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
//		    TextView tv = (TextView) view.findViewById(R.id.tabsText);
//		    tv.setText(text);
//		    return view;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
