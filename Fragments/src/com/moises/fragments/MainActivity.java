package com.moises.fragments;

import com.moises.fragments.FragmentA.SeleccionarPersonaClickListener;
import com.moises.fragments.FragmentB.PersonaSeleccionadoClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements SeleccionarPersonaClickListener, PersonaSeleccionadoClickListener{

	FragmentA fragA;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//new FragmentA();
		cargarFrag(FragmentA.newInstance(getResources().getColor(R.color.turquoise), "Aca van los datos seleccionado en el fragment B"), null);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_ver_lista).setVisible(true);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_ver_lista:
			Intent intent = new Intent(this, SecondActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cargarFrag(Fragment frag, String tag){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.my_container, frag);
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.commit();
	}

	@Override
	public void SeleccionarClick() {
		cargarFrag(new FragmentB(), null);
	}



	@Override
	public void seleccionadoClick(Persona per) {
		cargarFrag(FragmentA.newInstance(getResources().getColor(R.color.spring_green), "DNI: "+per.getDni()+" Nombre: "+per.getNombre()), null);
	}
}
