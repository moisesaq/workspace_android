package com.moisse.fragments;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListVehiculosEliminadosAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.MyVar;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ListaVehiculosEliminados extends Fragment{
	
	private ListView lvListaVehiculoEliminados;
	private TextView tvAviso;
	private String idparqueo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.lista_vehiculos, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(v);
		return v;
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_search).setVisible(false);
	}
	
	private void inicializarComponentes(View v) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Vehiculos eliminados");
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		lvListaVehiculoEliminados = (ListView)v.findViewById(R.id.lvListaVehiculos);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaVehiculos);
		this.idparqueo = getArguments().getString("idparqueo");
		cargarListaVehiculosEliminados();
	}
	public void cargarListaVehiculosEliminados() {
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			List<Vehiculo> listVehiculosEliminados = db.getAllVehiculo(idparqueo, MyVar.ELIMINADO);
			if(listVehiculosEliminados.size()==0 || listVehiculosEliminados==null){
				tvAviso.setText("No se encontraron vehículos eliminados");
				tvAviso.setVisibility(View.VISIBLE);
				lvListaVehiculoEliminados.setVisibility(View.INVISIBLE);
			}else{
				ListVehiculosEliminadosAdapter adapter = new ListVehiculosEliminadosAdapter(getActivity(), listVehiculosEliminados, this);
				lvListaVehiculoEliminados.setAdapter(adapter);
			}
			db.closeSQLite();
		} catch (Exception e) {
			mensajeError(e.toString(), "cargar vehiculos eliminados");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error al "+titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
}
