package com.silvia.cooperativa;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.dialogos.DialogAutenticacion;
import com.silvia.fragmentos.InformeVentas;
import com.silvia.fragmentos.MiMenu;
import com.silvia.fragmentos.RegistrarVenta;
import com.silvia.modelo.Cliente;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends Activity{

	public String idusuario;
	private SearchView mySearchView;
	private ActionBar actionBar;
	private Bundle bundle1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(getIdUsuarioSharePreferences().equals("")){
			DialogAutenticacion dAut = new DialogAutenticacion(this);
			dAut.show(getFragmentManager(), "tagAut");
		}else{
			inicializarTabs();
		}
	}
	
	public void inicializarTabs(){
		idusuario = getIdUsuarioSharePreferences();
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab tab1 = actionBar.newTab().setIcon(R.drawable.ic_menu_black_32).setText(" MENU");
		ActionBar.Tab tab2 = actionBar.newTab().setIcon(R.drawable.ic_sell_black_32).setText("VENTAS");
		ActionBar.Tab tab3 = actionBar.newTab().setIcon(R.drawable.ic_report_black_32).setText("REPORTES");
		bundle1 = new Bundle();
		bundle1.putString("idusuario", idusuario);
		
		MiMenu miMenu = new MiMenu();
		miMenu.setArguments(bundle1);
		
		RegistrarVenta regVenta = new RegistrarVenta();
		regVenta.setArguments(bundle1);
		
		Fragment fragTab1 = miMenu;
		Fragment fragTab2 = regVenta;
		Fragment fragTab3 = new InformeVentas();
		
		tab1.setTabListener(new MyTabListener(fragTab1));
		tab2.setTabListener(new MyTabListener(fragTab2));
		tab3.setTabListener(new MyTabListener(fragTab3));
		
		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mySearchView = (SearchView)searchItem.getActionView();
		mySearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
		//mySearchView.setBackgroundColor(getResources().getColor(R.color.AMARILLO_GOLD));
		mySearchView.setQueryHint("Carnet Identidad");
		mySearchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				if(query.length()==7 || query.length()==8 || query.length()==10){
					DBDuraznillo db = new DBDuraznillo(getApplicationContext());
					try {
						db.abrirDB();
						Cliente cli = db.getClientePorCI(query);
						if(cli!=null){
							Bundle bundle = new Bundle();
							Intent intent = new Intent(MainActivity.this, TercerActivity.class);
							bundle.putInt("accion", Variables.ACCION_CARGAR_DETALLE_CLIENTE);
							bundle.putString("idcliente", cli.getIdcliente());
							intent.putExtras(bundle);
							startActivity(intent);
						}else{
							Toast toast = Toast.makeText(getApplicationContext(), "Este CI/NIT no existe", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
							toast.show();
						}
						db.cerrarDB();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Toast toast = Toast.makeText(getApplicationContext(), "CI/NIT debe tener 7, 8 o 10 caracteres", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
				}
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				
				return false;
			}
		});
		return true;
	}
	
	public String getIdUsuarioSharePreferences(){
		SharedPreferences shareC = getSharedPreferences("ShareCooperativa", Context.MODE_PRIVATE);
		String shareCooperativa = shareC.getString("idusuario", "");
		return shareCooperativa;
	}
	
	public class MyTabListener implements ActionBar.TabListener{
	
		Fragment myFragmento;
		public MyTabListener(Fragment fragment){
			this.myFragmento = fragment;
		}
	
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.myContenedor, myFragmento);
		}
	
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.myContenedor, myFragmento);
		
		}
	
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(myFragmento);
		}
	}
	
}
