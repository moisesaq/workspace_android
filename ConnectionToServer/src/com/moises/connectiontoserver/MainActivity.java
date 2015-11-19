package com.moises.connectiontoserver;

import java.util.ArrayList;
import java.util.List;

import com.moises.httpurlconnection.DatosLugar;
import com.moises.httpurlconnection.ListaLugares;
import com.moises.httpurlconnection.ListaLugares.OnLugarSeleccionadoClickListener;
import com.moises.httpurlconnection.Lugar;
import com.moises.volley.ListaLugaresVolley;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener, OnLugarSeleccionadoClickListener{

	ActionBarDrawerToggle drawerToogle;
	DrawerLayout menu_izq;
	ListView lv_menu;
	String titleApp = "";
	String itemTitle = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		titleApp = getResources().getString(R.string.app_name);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		iniciarMenuIzq();
		cargarFrament(new ListaLugares(), "tagLista1");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(drawerToogle.onOptionsItemSelected(item)){
			return true;
		}
		switch (item.getItemId()) {
			case R.id.action_nuevo_lugar:
				Intent intent_Reg_Lugar = new Intent(this, SecondActivity.class);
				intent_Reg_Lugar.putExtra("accion", Var.CARGAR_FRAG_NUEVO_LUGAR);
				startActivity(intent_Reg_Lugar);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cargarFrament(Fragment frag, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.miContenedor, frag);
		ft.commit();
	}
	
	public void iniciarMenuIzq(){
		menu_izq = (DrawerLayout)findViewById(R.id.menu_izquierdo);
		lv_menu = (ListView)findViewById(R.id.lv_menu);
		MenuAdapter adapter = new MenuAdapter(this, getListaMenu());
		lv_menu.setAdapter(adapter);
		lv_menu.setOnItemClickListener(this);
		menu_izq.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		drawerToogle = new ActionBarDrawerToggle(this, 
											menu_izq, 
											R.drawable.ic_drawer,
											R.string.app_name, 
											R.string.hello_world){
			
											@Override
											public void onDrawerClosed(View view){
												getActionBar().setTitle(itemTitle);
											}
											
											@Override
											public void onDrawerOpened(View drawerView){
												if(!itemTitle.equals("")){
													getActionBar().setTitle(titleApp);	
												}
											}
		};
		menu_izq.setDrawerListener(drawerToogle);
		
	}
	
	public List<ItemMenu> getListaMenu(){
		List<ItemMenu> lista = new ArrayList<ItemMenu>();
		lista.add(new ItemMenu(1,R.drawable.ic_launcher, "Pruebas con HttpUrl"));
		lista.add(new ItemMenu(2,R.drawable.ic_launcher, "Pruebas con Volley"));
		return lista;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		ItemMenu item = (ItemMenu)adapter.getAdapter().getItem(position);
		itemTitle = item.getTitulo();
		Toast.makeText(getApplicationContext(), "Preccionaste "+item.getCodigo(), Toast.LENGTH_SHORT).show();
		switch (item.getCodigo()) {
			case 1:
				ListaLugares lista_http = new ListaLugares();
				cargarFrament(lista_http, "tagL1");
				break;
			case 2:
				ListaLugaresVolley lista_volley = new ListaLugaresVolley();
				cargarFrament(lista_volley, "tagL2");
				break;
		}
		menu_izq.closeDrawers();
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		drawerToogle.syncState();
	}

	@Override
	public void onLugarSeleccionadoClick(Lugar lugar) {
		DatosLugar datos = new DatosLugar();
		DatosLugar.newInstance(lugar);
		cargarFrament(DatosLugar.newInstance(lugar), "tagDatosLugar");
	}

	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount()>1){
			getFragmentManager().popBackStack();
		}else{
			finish();
		}
	}

}
