package com.moises.ormlite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPageAdapter extends FragmentPagerAdapter{

	public TabPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0: return new NewContactFragment();
		case 1: return new ListContactFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	

}
