package com.moises.practices;

import com.moises.countdowntimer.ListCountDownTimer;
import com.moises.ormlite.ContactActivity;
import com.moises.pinnedlistview.ListHeaderCustom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

	private ListHeaderCustom listHeaderCustom;
	private MainMenu mainMenu;
	private ListCountDownTimer listCountDownTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(mainMenu == null) mainMenu = new MainMenu();
		showFragment(mainMenu, MainMenu.TAG);
	}
	
	public void showFragment(Fragment fragment, String tag){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft =  fm.beginTransaction();
		ft.replace(R.id.container, fragment);
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.action_show_pinned_section_list_view:
				if(listHeaderCustom == null) listHeaderCustom = new ListHeaderCustom();
				showFragment(listHeaderCustom, ListHeaderCustom.TAG);
				return true;
	
			case R.id.action_show_test_ormlite:
				Intent intent = new Intent(this, ContactActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_show_count_down_timer:
				if(listCountDownTimer == null) listCountDownTimer = new ListCountDownTimer();
				showFragment(listCountDownTimer, ListCountDownTimer.TAG);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
