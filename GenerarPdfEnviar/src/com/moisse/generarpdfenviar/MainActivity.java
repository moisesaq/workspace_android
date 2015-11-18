package com.moisse.generarpdfenviar;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentMyMenu myMenu = new FragmentMyMenu();
		establecerFragment(myMenu, "tagMyMenu");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void establecerFragment(Fragment fragment, String tagFragment){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tagFragment!=null){
			ft.addToBackStack(tagFragment);
		}
		ft.replace(R.id.myContenedor, fragment);
		ft.commit();
	}

}
