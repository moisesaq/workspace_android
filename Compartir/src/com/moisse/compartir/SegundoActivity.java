package com.moisse.compartir;

import com.moisse.fragments.ImageCamera;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class SegundoActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.segundo_activity);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		establerFragmentEnVista(new ImageCamera());
	}
	
	public void establerFragmentEnVista(Fragment fragment){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.segundo_contenedor, fragment);
		//fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
}
