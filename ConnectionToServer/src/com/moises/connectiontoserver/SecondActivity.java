package com.moises.connectiontoserver;

import com.moises.httpurlconnection.RegistrarLugar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SecondActivity extends Activity{

	protected int accion;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.second_activity);
		iniciar();
	}
	
	private void iniciar() {
		Bundle msj = getIntent().getExtras();
		accion = msj.getInt("accion");
		if(accion==Var.CARGAR_FRAG_NUEVO_LUGAR){
			RegistrarLugar reg = new RegistrarLugar();
			cargarFragment(reg, "tag_reg_lugar");
		}
	}

	public void cargarFragment(Fragment frag, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.second_container, frag);
		ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.second_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==android.R.id.home){
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount()>1){
			getFragmentManager().popBackStack();
		}else{
			this.finish();
		}
	}
	
}
