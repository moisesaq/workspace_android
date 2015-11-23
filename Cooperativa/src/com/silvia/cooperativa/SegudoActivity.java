package com.silvia.cooperativa;

import com.silvia.dialogos.DialogDetalleMaquinaria.OnEditarMaquinariaClickListener;
import com.silvia.fragmentos.DetallePersonal;
import com.silvia.fragmentos.DetallePersonal.OnDetallePersonalClickListener;
import com.silvia.fragmentos.ListaCargos;
import com.silvia.fragmentos.ListaMaquinaria;
import com.silvia.fragmentos.ListaPersonal;
import com.silvia.fragmentos.ListaPersonal.OnListaPersonalClickListener;
import com.silvia.fragmentos.NuevaMaquinaria;
import com.silvia.fragmentos.NuevoPersonal;
import com.silvia.fragmentos.NuevaMaquinaria.OnBackToListaMaqClickListener;
import com.silvia.fragmentos.NuevoPersonal.OnBackFromNuevoPersonalClickListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SegudoActivity extends Activity implements OnEditarMaquinariaClickListener, OnBackToListaMaqClickListener, OnDetallePersonalClickListener,
														OnListaPersonalClickListener, OnBackFromNuevoPersonalClickListener{

	public int ACCION;
	public ActionBar actionBar;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.segundo_activity);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle cajon = getIntent().getExtras();
		this.ACCION = cajon.getInt("accion");
		if(ACCION==Variables.ACCION_CARGAR_LISTA_CARGOS){
			cargarFragmento(new ListaCargos(), "tagListaCargos");
		}else if (ACCION==Variables.ACCION_CARGAR_LISTA_MAQ) {
			cargarFragmento(new ListaMaquinaria(), "tagListaMaq");
		}else if (ACCION==Variables.ACCION_CARGAR_LISTA_PERSONAL) {
			Bundle msjID = new Bundle();
			msjID.putString("idusuario", cajon.getString("idusuario"));
			ListaPersonal listaP = new ListaPersonal();
			listaP.setArguments(msjID);
			cargarFragmento(listaP, "tagListaPer");
		}else if (ACCION==Variables.ACCION_CARGAR_PERFIL) {
			Bundle msjIDPersonal = new Bundle();
			msjIDPersonal.putString("idpersonal", cajon.getString("idpersonal"));
			msjIDPersonal.putInt("proviene", Variables.ACCION_CARGAR_PERFIL);
			DetallePersonal detallePerfil = new DetallePersonal();
			detallePerfil.setArguments(msjIDPersonal);
			cargarFragmento(detallePerfil, "tagDetallePerfil");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.segundo_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.action_nueva_maq:
				Bundle msjMaq = new Bundle();
				NuevaMaquinaria nuevaMaq = new NuevaMaquinaria();
				msjMaq.putInt("accion", Variables.ACCION_NUEVO_MAQ);
				nuevaMaq.setArguments(msjMaq);
				cargarFragmento(nuevaMaq, "tagNuevaMaq");
				return true;
				
			case R.id.action_nuevo_personal:
				Bundle msjPer = new Bundle();
				NuevoPersonal nuevoPer = new NuevoPersonal();
				msjPer.putInt("accion", Variables.ACCION_NUEVO_PERSONAL);
				nuevoPer.setArguments(msjPer);
				cargarFragmento(nuevoPer, "tagNuevoPer");
				return true;
			
			case R.id.action_listo_personal:
				//cargarFragmento(new ListaPersonal(), null);
				onBackPressed();
				return true;
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void cargarFragmento(Fragment fragment, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.myContenedorDos, fragment);
		ft.commit();
	}

	@Override
	public void onEditarMaquinariaClick(String idmaquinaria) {
		Bundle msj = new Bundle();
		msj.putInt("accion", Variables.ACCION_EDITAR_MAQ);
		msj.putString("idmaquinaria", idmaquinaria);
		NuevaMaquinaria nMaq = new NuevaMaquinaria();
		nMaq.setArguments(msj);
		cargarFragmento(nMaq, "tagEditMaq");
	}
	
	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount()>1){
			getFragmentManager().popBackStack();
		}else{
			this.finish();
		}
	}

	@Override
	public void onBackToListaMaqClick() {
		onBackPressed();
	}

	@Override
	public void onBackFromNuevoPersonalClick() {
		onBackPressed();
	}

	//Estos 2 metodos son de listener de detalle personal
	@Override
	public void onEditarDetallePersonalClick(String idpersonal) {
		Bundle msjIDPer = new Bundle();
		msjIDPer.putString("idpersonal", idpersonal);
		msjIDPer.putInt("accion", Variables.ACCION_EDITAR_PERSONAL);
		NuevoPersonal nuevoP = new NuevoPersonal();
		nuevoP.setArguments(msjIDPer);
		cargarFragmento(nuevoP, "tagEditarPer");
	}
	
	@Override
	public void onBackFromDetallePersonalClick() {
		onBackPressed();
	}

	//Estos 2 metodos son de listener de lista personal
	@Override
	public void onVerDetallePersonalClick(String idpersonal) {
		Bundle msjID = new Bundle();
		msjID.putString("idpersonal", idpersonal);
		msjID.putInt("proviene", Variables.ACCION_CARGAR_DETALLE_PERSONAL);
		DetallePersonal dPer = new DetallePersonal();
		dPer.setArguments(msjID);
		cargarFragmento(dPer, "tagDetallePer");
	}

	@Override
	public void onEditarListaPersonalClick(String idpersonal) {
		onEditarDetallePersonalClick(idpersonal);
	}

}
