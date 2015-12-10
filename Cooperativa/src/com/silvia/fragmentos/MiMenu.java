package com.silvia.fragmentos;

import java.util.ArrayList;
import java.util.List;

import com.silvia.adapters.MiMenuAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.CuartoActivity;
import com.silvia.cooperativa.SegudoActivity;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.TercerActivity;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogDatosCooperativa;
import com.silvia.modelo.ItemMiMenu;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MiMenu extends Fragment implements OnItemClickListener{
	
	private ListView lvMenu;
	private List<ItemMiMenu> miMenu = new ArrayList<ItemMiMenu>();
	private String idusuario;
	private Personal userPersonal;
	private Usuario userOnline; 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.mi_menu, container, false);
		inicializarComponentes(v);
		return v;
	}
	
	public void inicializarComponentes(View v){
		setHasOptionsMenu(true);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Coop. SUCRE");
		lvMenu = (ListView)v.findViewById(R.id.lvMiMenu);
		lvMenu.setOnItemClickListener(this);
		this.idusuario = getArguments().getString("idusuario");
		cargarMenu();
	}
	
	public void cargarMenu(){
		generarMiMenu();
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, miMenu);
		MiMenuAdapter adapter = new MiMenuAdapter(getActivity(), miMenu);
		lvMenu.setAdapter(adapter);
	}
	
	public void generarMiMenu(){
		cargarDatosUsuarioPersonal();
		miMenu.clear();
		miMenu.add(new ItemMiMenu(100, userPersonal.getImagen(), "Perfil "+userOnline.getUsuario(), "Permite ver y editar los datos del usuario"));
		miMenu.add(new ItemMiMenu(9, R.drawable.ic_order_service_128, "Pedidos", "Permite ver todos los pedidos entregados y pendientes"));
		miMenu.add(new ItemMiMenu(1, R.drawable.ic_person_add_white_36dp, "Nuevo cliente", "Permite registrar un nuevo cliente"));
		miMenu.add(new ItemMiMenu(2, R.drawable.ic_list_white_36dp, "Lista clientes", "Permite ver, editar y registrar clientes"));
		miMenu.add(new ItemMiMenu(3, R.drawable.ic_product_plant_128, "Productos", "Permite ver, editar y registrar productos"));
		miMenu.add(new ItemMiMenu(4, R.drawable.ic_employees_128, "Personal", "Permite ver, editar y registrar personal"));
		miMenu.add(new ItemMiMenu(5, R.drawable.ic_ocupation_128, "Cargos", "Permite ver, editar y registrar cargos"));
		miMenu.add(new ItemMiMenu(6, R.drawable.ic_excavator_128, "Maquinarias", "Permite ver y registrar cargo"));
		miMenu.add(new ItemMiMenu(101, R.drawable.ic_icon_coop_256, "Datos Cooperativa", "Permite ver y editar los datos generales de la cooperativa"));
		miMenu.add(new ItemMiMenu(10, R.drawable.ic_backup_128, "Backup", "Permite sacar BACKUP de todos los datos"));
		miMenu.add(new ItemMiMenu(8, R.drawable.ic_close_white_36dp, "Cerrar Sesión", "Cierra sesión del usuario actual"));
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		ItemMiMenu itemMiMenu = (ItemMiMenu)adapter.getAdapter().getItem(position);
		switch (itemMiMenu.getIdmenu()) {
			case 100:
				Bundle msjPerfil = new Bundle();
				msjPerfil.putInt("accion", Variables.ACCION_CARGAR_PERFIL);
				msjPerfil.putString("idpersonal", userOnline.getIdpersonal());
				Intent intentPerfil = new Intent(getActivity(), SegudoActivity.class);
				intentPerfil.putExtras(msjPerfil);
				startActivity(intentPerfil);
				break;
			case 1:
				Bundle msjNCliente = new Bundle();
				Intent intentNCliente = new Intent(getActivity(), TercerActivity.class);
				msjNCliente.putInt("accion", Variables.ACCION_CARGAR_NUEVO_CLIENTE);
				intentNCliente.putExtras(msjNCliente);
				startActivity(intentNCliente);
				break;
			case 2:
				Bundle msjListaC = new Bundle();
				Intent intentListaC = new Intent(getActivity(), TercerActivity.class);
				msjListaC.putInt("accion", Variables.ACCION_CARGAR_LISTA_CLIENTES);
				intentListaC.putExtras(msjListaC);
				startActivity(intentListaC);
				break;
			
			case 3:
				Bundle msjLProd = new Bundle();
				Intent intentLProd = new Intent(getActivity(), TercerActivity.class);
				msjLProd.putInt("accion", Variables.ACCION_CARGAR_LISTA_PRODUCTOS);
				intentLProd.putExtras(msjLProd);
				startActivity(intentLProd);
				break;
			
			case 4:
				Bundle msjPer = new Bundle();
				Intent intentPersonal = new Intent(getActivity(), SegudoActivity.class);
				msjPer.putInt("accion", Variables.ACCION_CARGAR_LISTA_PERSONAL);
				msjPer.putString("idusuario", this.idusuario);
				intentPersonal.putExtras(msjPer);
				startActivity(intentPersonal);
				break;
				
			case 5:
				Bundle msjCargo = new Bundle();
				Intent intentCargos = new Intent(getActivity(), SegudoActivity.class);
				msjCargo.putInt("accion", Variables.ACCION_CARGAR_LISTA_CARGOS);
				intentCargos.putExtras(msjCargo);
				startActivity(intentCargos);
				break;
			
			case 6:
				Bundle msjMaq = new Bundle();
				Intent intentMaq = new Intent(getActivity(), SegudoActivity.class);
				msjMaq.putInt("accion", Variables.ACCION_CARGAR_LISTA_MAQ);
				intentMaq.putExtras(msjMaq);
				startActivity(intentMaq);
				break;
			case 101:
				DialogDatosCooperativa dDatosCoop = new DialogDatosCooperativa();
				dDatosCoop.show(getFragmentManager(), "tagDatosCoop");
				break;
			case 8:
				cerrarSesion();
				break;
			case 9:
				Bundle msjPedidos = new Bundle();
				msjPedidos.putInt("accion", Variables.ACCION_CARGAR_LISTA_PEDIDO);
				msjPedidos.putString("idusuario", idusuario);
				Intent intentPedidos = new Intent(getActivity(), CuartoActivity.class);
				intentPedidos.putExtras(msjPedidos);
				startActivity(intentPedidos);
				break;
			case 10:
				Bundle msjBackup = new Bundle();
				Intent intentBackup = new Intent(getActivity(), CuartoActivity.class);
				msjBackup.putInt("accion", Variables.ACCION_CARGAR_BACKUP);
				intentBackup.putExtras(msjBackup);
				startActivity(intentBackup);
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//Toast.makeText(getActivity(), "Continuando Mi Menu", Toast.LENGTH_SHORT).show();
	}
	
	private void cerrarSesion() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setMessage("¿Desea cerrar sesión?");
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		dialog.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearIDUsuarioShare();
				getActivity().finish();
			}
		});
		dialog.create().show();
	}
	
	public void clearIDUsuarioShare(){
		SharedPreferences shareC = getActivity().getSharedPreferences("ShareCooperativa", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = shareC.edit();
		editar.putString("idusuario", "");
		editar.commit();
	}
	
	public void cargarDatosUsuarioPersonal(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			this.userOnline = db.getUsuario(this.idusuario);
			this.userPersonal = db.getPersonal(userOnline.getIdpersonal());
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
