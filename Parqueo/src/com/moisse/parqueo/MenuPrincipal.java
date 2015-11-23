package com.moisse.parqueo;

import java.util.ArrayList;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.NavDrawerListAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogDetalleCliente;
import com.moisse.dialogs.DialogDetalleCliente.OnEditarClienteClickListener;
import com.moisse.dialogs.DialogDetalleVehiculo;
import com.moisse.dialogs.DialogDetalleVehiculo.OnEditarVehiculoClickListener;
import com.moisse.dialogs.DialogInfoParqueo;
import com.moisse.dialogs.DialogInfoParqueo.OnEditarParqueoClickListener;
import com.moisse.dialogs.DialogListas;
import com.moisse.dialogs.DialogListasEliminados;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.dialogs.DialogPerfil;
import com.moisse.dialogs.DialogPerfil.OnEditarPerfilClickListener;
import com.moisse.fragments.NuevoCliente;
import com.moisse.fragments.NuevoCliente.OnBackFromNuevoClienteClickListener;
import com.moisse.fragments.ListaResguardo;
import com.moisse.fragments.NuevoParqueo;
import com.moisse.fragments.NuevoParqueo.OnParqueoClickListener;
import com.moisse.fragments.NuevoUsuario;
import com.moisse.fragments.NuevoUsuario.OnPerfilClickListener;
import com.moisse.fragments.NuevoVehiculo;
import com.moisse.fragments.NuevoVehiculo.OnBackFromNuevoVehiculoClickListener;
import com.moisse.fragments.Parking;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.NavDrawerItem;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Usuario;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.MyVar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPrincipal extends Activity implements OnItemClickListener,	OnEditarClienteClickListener, OnEditarVehiculoClickListener, 
														OnEditarPerfilClickListener, OnEditarParqueoClickListener, OnParqueoClickListener, 
														OnPerfilClickListener, OnQueryTextListener,
														OnBackFromNuevoClienteClickListener, OnBackFromNuevoVehiculoClickListener{
	
	private DrawerLayout menu_lateral;
	private ActionBarDrawerToggle toogle;
	private ListView menu;
	private String idparqueo;
	private String idusuario;
	private Usuario user_online;
	private Parqueo parq_online;
	
	private SearchView mySearchView;
	
	static final List<NavDrawerItem> listaMenu = new ArrayList<NavDrawerItem>();
	String nombre_frag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);
		Bundle mensaje = getIntent().getExtras();
		this.idparqueo = mensaje.getString("idparqueo");
		this.idusuario = mensaje.getString("idusuario");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		generarVistaMenuLateral();
		inicializarVistaParking("tagParking");
	}
	
	public void inicializarVistaParking(String tagParking){
		Bundle mandar = new Bundle();
		mandar.putString("idparqueo", idparqueo);
		Parking parking = new Parking();
		parking.setArguments(mandar);
		establecerVistaFragment(parking, tagParking);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mySearchView = (SearchView)searchItem.getActionView();
		mySearchView.setQueryHint("Placa o CI");
		mySearchView.setOnQueryTextListener(this);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(toogle.onOptionsItemSelected(item)){
			return true;
		}
		switch (item.getItemId()) {
//			case R.id.action_parking:
//				inicializarVistaParking("tagParking");
//				return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		DBParqueo db = new DBParqueo(this);
		if(isNumeric(query)){
			try {
				db.openSQLite();
				Cliente cliente = db.buscarClientePorCI(query, this.idparqueo);
				if(cliente!=null){
					DialogDetalleCliente dialogDCliente = new DialogDetalleCliente(cliente, this);
					dialogDCliente.show(getFragmentManager(), "tagDDC");
				}else{
					Toast.makeText(this, "Cliente no encotrado con ese CI", Toast.LENGTH_LONG).show();
				}
				db.closeSQLite();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				db.openSQLite();
				Vehiculo vehiculo = db.buscarVehiculoPorPlaca(query.toUpperCase(), this.idparqueo);
				if(vehiculo!=null){
					DialogDetalleVehiculo dialogDVehiculo = new DialogDetalleVehiculo(vehiculo, this);
					dialogDVehiculo.show(getFragmentManager(), "tagDDV");
				}else{
					Toast.makeText(this, "Vehiculo no encotrado con esa placa", Toast.LENGTH_LONG).show();
				}
				db.closeSQLite();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isNumeric(String dato){
		try{
			Integer.parseInt(dato);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public void establecerVistaFragment(Fragment fragment, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.mainContenedor, fragment);
		ft.commit();
	}
	
	public void generarVistaMenuLateral(){
		this.parq_online = getParqueoOnline();
		this.user_online = getUserOnline(this.idusuario);
		Toast.makeText(this, user_online.getNombre_usuario()+" bienvenido", Toast.LENGTH_SHORT).show();
		menu_lateral = (DrawerLayout)findViewById(R.id.drawer_layout);
		menu = (ListView)findViewById(R.id.left_drawer);
		menu.setAdapter(new NavDrawerListAdapter(this, getMenuGenerado()));
		//menu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, valores));
		
		menu.setOnItemClickListener(this);
		menu_lateral.setDrawerShadow(R.drawable.drawer_shadow, 	GravityCompat.START);
		toogle = new ActionBarDrawerToggle(this, 
										   menu_lateral, 
										   R.drawable.ic_drawer,
										   R.string.app_name, 
										   R.string.hello_world){

											@Override
											public void onDrawerClosed(
													View drawerView) {
												//getActionBar().setTitle(getResources().getString(R.string.app_name));
												//getActionBar().setTitle(getActionBar().getTitle());
												//getActionBar().setTitle(new StringBuilder("Parqueo ").append(parq_online.getNombre_parqueo()));
												invalidateOptionsMenu();
											}

											@Override
											public void onDrawerOpened(View drawerView) {
												//getActionBar().setTitle("Menu");
												invalidateOptionsMenu();
											}
			
		};
		menu_lateral.setDrawerListener(toogle);
	}
	
	public void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		toogle.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		if(listaMenu.get(position).getIdItem()==1000){
			DialogPerfil dialogPerfil = new DialogPerfil(user_online);
			dialogPerfil.show(getFragmentManager(), "tagDPerfil");
		}
		switch (listaMenu.get(position).getIdItem()) {
			case 1:
				inicializarVistaParking("tagParking");
				break;
			case 2:
				NuevoCliente nuevoCliente = new NuevoCliente();
				Bundle msj2 = new Bundle();
				msj2.putInt("accion", MyVar.ACCION_REGISTRAR_CLIENTE);
				msj2.putString("idparqueo", this.idparqueo);
				nuevoCliente.setArguments(msj2);
				establecerVistaFragment(nuevoCliente, "tagNC");
				break;
			case 3:
				NuevoVehiculo nv = new NuevoVehiculo();
				Bundle msj3 = new Bundle();
				msj3.putInt("accion", MyVar.ACCION_REGISTRAR_VEHICULO);
				msj3.putString("idparqueo", this.idparqueo);
				nv.setArguments(msj3);
				establecerVistaFragment(nv, "tagNV");
				break;
			case 4:
				nuevoUsuario();
				break;
			case 5:
				DialogListas dl = new DialogListas(this, this.idparqueo, this.idusuario);
				dl.show(getFragmentManager(), "tagDialogListas");
				break;
			case 6:
				ListaResguardo listR = new ListaResguardo();
				Bundle msj6 = new Bundle();
				msj6.putString("idparqueo", this.idparqueo);
				msj6.putString("idusuario", this.idusuario);
				listR.setArguments(msj6);
				establecerVistaFragment(listR, "tagLR");
				break;
			case 2000:
				parq_online = getParqueoOnline(); 
				DialogInfoParqueo dialogInfoParqueo = new DialogInfoParqueo(parq_online, user_online);
				dialogInfoParqueo.show(getFragmentManager(), "tagDInfoParqueo");
				break;
				
			case 8:
				DialogListasEliminados dialogLEliminados = new DialogListasEliminados(this, idparqueo);
				dialogLEliminados.show(getFragmentManager(), "tagDLEliminados");
				break;
			case 9:
				confirmarCerrarSesion();
				break;
		}
		menu_lateral.closeDrawers();
	}
	
	public void confirmarCerrarSesion(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(new StringBuilder("¿Desea cerrar sesión?"));
		dialog.setNeutralButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearIDShare();
				MenuPrincipal.this.finish();
				Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
				startActivity(intent);
			}
		});
		dialog.create().show();
	}

	private void nuevoUsuario() {
		if(user_online.getCargo()==MyVar.CARGO_ADMIN){
			NuevoUsuario nu = new NuevoUsuario();
			Bundle msj4 = new Bundle();
			msj4.putInt("accion", MyVar.ACCION_REGISTRAR_USUARIO);
			msj4.putString("idparqueo", idparqueo);
			nu.setArguments(msj4);
			establecerVistaFragment(nu, "tagNU");
		}else{
			DialogMensaje dmsj = new DialogMensaje("No estas autorizado para registrar un nuevo usuario");
			dmsj.show(getFragmentManager(), "dialogMsjNuevoUsuario");
		}
	}

	public List<NavDrawerItem> getMenuGenerado(){
		listaMenu.clear();
		NavDrawerItem itemPerfil = new NavDrawerItem(1000, user_online.getImagen(),
				new StringBuilder("Perfil ").append(user_online.getNombre_usuario()).toString());
		NavDrawerItem item1 = new NavDrawerItem(1, R.drawable.ic_parking_select, "Parquear Vehículo");
		NavDrawerItem item2 = new NavDrawerItem(2, R.drawable.ic_person_add_white_48dp, "Registrar Nuevo Cliente");
		NavDrawerItem item3 = new NavDrawerItem(3, R.drawable.ic_directions_car_white_48dp, "Registrar Nuevo vehículo");
		NavDrawerItem item4 = new NavDrawerItem(4, R.drawable.ic_add_user, "Registrar Nuevo Usuario");
		NavDrawerItem item5 = new NavDrawerItem(5, R.drawable.ic_format_list_numbered_white_48dp, "Ver Listas");
		NavDrawerItem item6 = new NavDrawerItem(6, R.drawable.ic_assignment_white_48dp, "Registro de Ingreso/Salida");
		NavDrawerItem itemParqueo = new NavDrawerItem(2000, parq_online.getLogo(),"Info. Parqueo");
		NavDrawerItem item8 = new NavDrawerItem(8, R.drawable.ic_delete_white_36dp, "Eliminados");
		NavDrawerItem item9 = new NavDrawerItem(9, R.drawable.ic_exit_to_app_white_48dp, "Cerrar Sesión");
		
		listaMenu.add(itemPerfil); listaMenu.add(item1); listaMenu.add(item2); listaMenu.add(item3); listaMenu.add(item4); 
		listaMenu.add(item5); listaMenu.add(item6); listaMenu.add(itemParqueo); listaMenu.add(item8); listaMenu.add(item9);
		
		return listaMenu;
	}
	
	public Parqueo getParqueoOnline(){
		Parqueo p = null;
		DBParqueo db = new DBParqueo(this);
		try {
			db.openSQLite();
			p = db.getParqueo(this.idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			mensajeError(e.toString(), "obtener parqueo");
		}
		return p;
	}

	public Usuario getUserOnline(String id_user_online){
		DBParqueo db = new DBParqueo(this);
		Usuario user_activo = null;
		try {
			db.openSQLite();
			user_activo = db.getUsuario(id_user_online);
			db.closeSQLite();
		} catch (Exception e) {
			mensajeError(e.toString(), "obtener usuario");
		}
		return user_activo;
	}
	
	@Override
	public void onEditarClienteClick(String idcliente) {
		NuevoCliente nc = new NuevoCliente();
		Bundle msj1 = new Bundle();
		msj1.putInt("accion", MyVar.ACCION_EDITAR_CLIENTE);
		msj1.putString("idcliente", idcliente);
		msj1.putString("idparqueo", this.idparqueo);
		nc.setArguments(msj1);
		establecerVistaFragment(nc, "tagEditarCliente");
	}
	
	@Override
	public void onEditarVehiculoClick(String idvehiculo) {
		Bundle msj2 = new Bundle();
		msj2.putInt("accion", MyVar.ACCION_EDITAR_VEHICULO);
		msj2.putString("id_vehiculo", idvehiculo);
		msj2.putString("idparqueo", this.idparqueo);
		NuevoVehiculo nv = new NuevoVehiculo();
		nv.setArguments(msj2);
		establecerVistaFragment(nv, "tagEditarVehiculo");
	}
	
	@Override
	public void onEditarPerfilClick(String idusuario) {
		Bundle cajon = new Bundle();
		cajon.putString("idparqueo", idparqueo);
		cajon.putInt("accion", MyVar.ACCION_EDITAR_PERFIL);
		cajon.putString("idusuario", idusuario);
		NuevoUsuario nu = new NuevoUsuario();
		nu.setArguments(cajon);
		establecerVistaFragment(nu, "tagEditarPerfil");
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(this);
		mensaje.setTitle("Error "+titulo);
		TextView texto=new TextView(this);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	@Override
	public void onEditarParqueoClick(String idparqueo) {
		Bundle cajon = new Bundle();
		cajon.putString("idparqueo", idparqueo);
		NuevoParqueo nuevoParqueo = new NuevoParqueo();
		nuevoParqueo.setArguments(cajon);
		establecerVistaFragment(nuevoParqueo, "tagEditarParqueo");
	}

	public void clearIDShare(){
		SharedPreferences sharep = getSharedPreferences("ShareParqueo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = sharep.edit();
		editar.putString("idparqueo", "");
		editar.putString("idusuario", "");
		editar.commit();
	}

	@Override
	public void onBackPressed() {
		//Aqui pongo 1 porque kiero q se quede en vista parking y no volver mas atras
		if(getFragmentManager().getBackStackEntryCount()>1){
			getFragmentManager().popBackStack();
		}else{
			confirmarCerrarSesion();
		}
	}
	
	@Override
	public void onBackFromNuevoClienteClick() {
		onBackPressed();
	}

	@Override
	public void onBackFromNuevoVehiculoClick() {
		onBackPressed();
	}

	@Override
	public void onContinuarParqueoClick(String idParqueo) {
		//generarVistaMenuLateral();
	}
	
	@Override
	public void onBackFromParqueoClick() {
		onBackPressed();
	}

	@Override
	public void onActualizarPerfilClick() {
		generarVistaMenuLateral();
	}

	@Override
	public void onBackFromPerfilClick() {
		onBackPressed();
	}
	
}
