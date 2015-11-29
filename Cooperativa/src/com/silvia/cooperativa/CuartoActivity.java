package com.silvia.cooperativa;

import com.silvia.fragmentos.ListaPedidos;
import com.silvia.fragmentos.ListaPedidos.OnListaPedidosClickListener;
import com.silvia.fragmentos.MapSucre;
import com.silvia.fragmentos.MapSucre.OnMapSucreClickListener;
import com.silvia.fragmentos.RegistrarPedido;
import com.silvia.fragmentos.RegistrarPedido.OnRegistrarPedidoClickListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CuartoActivity extends Activity implements OnListaPedidosClickListener, OnRegistrarPedidoClickListener, OnMapSucreClickListener{

	private int ACCION;
	private RegistrarPedido registrarPedido;
	//private MapSucre mapSucre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cuarto_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle cajon = getIntent().getExtras();
		this.ACCION = cajon.getInt("accion");
		switch (ACCION) {
			case Variables.ACCION_CARGAR_LISTA_PEDIDO:
				Bundle msjIDUsuario = new Bundle();
				msjIDUsuario.putString("idusuario", cajon.getString("idusuario"));
				ListaPedidos listaPedidos = new ListaPedidos();
				listaPedidos.setArguments(msjIDUsuario);
				cargarFragmento(listaPedidos, "tagListaPedidos");
				break;
		}
	}
	
	public void cargarFragmento(Fragment fragmento, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.myCuartoContenedor, fragmento);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cuarto_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onNuevoPedidoClick(String idusuario) {
		Bundle msjIDU = new Bundle();
		msjIDU.putString("idusuario", idusuario);
		registrarPedido = new RegistrarPedido();
		registrarPedido.setArguments(msjIDU);
		cargarFragmento(registrarPedido, "tagRegPedido");
	}	

	@Override
	public void selectAddressOnMap() {
		MapSucre mapSucre = new MapSucre();
		cargarFragmento(mapSucre, null);
	}

	@Override
	public void setAddressPedido(double latitude, double longitude) {
		if(registrarPedido==null){
			registrarPedido = new RegistrarPedido();
		}
		Bundle bundle = new Bundle();
		bundle.putDouble("latitude", latitude);
		bundle.putDouble("longitude", longitude);
		registrarPedido.setArguments(bundle);
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
