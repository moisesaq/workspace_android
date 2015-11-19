package com.moisse.fragments;

import java.util.List;

import com.moisse.compartir.AdapterVistaGriView;
import com.moisse.compartir.R;
import com.moisse.database.DBVehiculo;
import com.moisse.modelo.Vehiculo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class VistaGridView extends Fragment implements OnItemClickListener{

	GridView gvVistaCuadros;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.vista_gridview, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		gvVistaCuadros = (GridView)v.findViewById(R.id.gvVistaCoches);
		gvVistaCuadros.setOnItemClickListener(this);
		DBVehiculo db = new DBVehiculo(getActivity());
		try{
			db.openDB();
			List<Vehiculo> lista_vehiculos = db.getAllVehiculo();
			AdapterVistaGriView adapter = new AdapterVistaGriView(getActivity().getApplicationContext(), lista_vehiculos, getActivity());
			gvVistaCuadros.setAdapter(adapter);
			db.closeDB();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		
	}
}
