package com.moises.ormlite;

import com.moises.practices.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;

public class ContactActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener{

	private ActionBar actionBar;
	private ViewPager viewPager;
	private TabPageAdapter tabPageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		setupTab();
	}

	private void setupTab() {
		viewPager = (ViewPager)findViewById(R.id.pager);
		actionBar = getActionBar();
		tabPageAdapter = new TabPageAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(tabPageAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab tab1 = actionBar.newTab().setText("New");
		ActionBar.Tab tab2 = actionBar.newTab().setText("List");
		tab1.setTabListener(this);
		tab2.setTabListener(this);
		
		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		
		viewPager.setOnPageChangeListener(this);
		/*Fragment frag1 = new NewContactFragment();
		Fragment frag2 = new ListContactFragment();*/
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//-------------------METODOS TAB LISTENER---------------------
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition()); 
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	//-------------------METODOS TAB LISTENER---------------------
	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {
	}

	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
		
	}

	
}
