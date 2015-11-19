package com.moisse.compartir;

import com.moisse.fragments.ListaVehiculos;
import com.moisse.fragments.NuevoVehiculo;
import com.moisse.fragments.VistaGridView;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	ActionBar actionB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionB = getActionBar();
//		actionB.setHomeButtonEnabled(true);
//		actionB.setDisplayHomeAsUpEnabled(true);
		iniciarTabs();
	}
	
	public void iniciarTabs(){
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//actionB.setDisplayShowTitleEnabled(false);
		ActionBar.Tab tab1 = actionB.newTab().setIcon(R.drawable.ic_directions_car_white_36dp);
		ActionBar.Tab tab2 = actionB.newTab().setIcon(R.drawable.ic_format_list_bulleted_white_36dp);
		ActionBar.Tab tab3 = actionB.newTab().setIcon(R.drawable.ic_apps_white_36dp);
		
		Fragment tab1Frag = new NuevoVehiculo();
		Fragment tab2Frag = new ListaVehiculos();
		Fragment tab3Frag = new VistaGridView();
		
		tab1.setTabListener(new MiTabListener(tab1Frag));
		tab2.setTabListener(new MiTabListener(tab2Frag));
		tab3.setTabListener(new MiTabListener(tab3Frag));
		
		actionB.addTab(tab1);
		actionB.addTab(tab2);
		actionB.addTab(tab3);
	}
	
	public int getMiShare(){
		SharedPreferences sharep = getSharedPreferences("VistaVehiculoPreferences", Context.MODE_PRIVATE);
		int vista = sharep.getInt("vista", 0);
		return vista;
	}
	
	public void setMiShare(int vista){
		SharedPreferences sharep = getSharedPreferences("VistaVehiculoPreferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = sharep.edit();
		editar.putInt("vista", vista);
		editar.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.actin_update).setVisible(false);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_images:
//				Fragment fragImageCamera = new ImageCamera();
//				establerFragmentEnVista(fragImageCamera);
				Intent intent = new Intent(this, SegundoActivity.class);
				startActivity(intent);
				//MiTabListener mitab = new MiTabListener();
				//Toast.makeText(getApplicationContext(), "Tab seleccionado "+mitab.getFragment().toString(), Toast.LENGTH_SHORT).show();
				return true;
		
			case R.id.action_dialog_saludo:
				saludar();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saludar() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppTheme);
		dialog.setTitle("Hola Mundo Android");
		dialog.setMessage("Hola esto es un saludo");
		dialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int witch) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}

	public void establerFragmentEnVista(Fragment fragment){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.myContenedor, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	public class MiTabListener implements ActionBar.TabListener{
		private Fragment fragment;
		
		public MiTabListener(){}
		
		public MiTabListener(Fragment fragment){
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Log.i("ActionBar", tab.getText()+" reseleccionado");
			ft.replace(R.id.myContenedor, fragment);
			//ft.addToBackStack(null);
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Log.i("ActionBar",tab.getText()+" seleccionado");
			ft.replace(R.id.myContenedor, fragment);
			//ft.addToBackStack(null);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Log.i("ActionBar",tab.getText()+" deseleccionado");
			ft.remove(fragment);
			//ft.addToBackStack(null);
		}
		
		public Fragment getFragment(){
			return this.fragment;
		}
		
	}

}
