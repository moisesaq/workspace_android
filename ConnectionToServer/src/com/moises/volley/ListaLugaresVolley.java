package com.moises.volley;

import com.moises.connectiontoserver.R;
import com.moises.connectiontoserver.R.id;
import com.moises.connectiontoserver.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ListaLugaresVolley extends Fragment{

	View view;
	protected ListView lvListaLugares;
	protected TextView tvAviso;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.lista_lugares, container, false);
		inicializarComponentes(view);
		return view;
	}
	
	private void inicializarComponentes(View v) {
		lvListaLugares = (ListView)v.findViewById(R.id.lvListaLugares);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaLugares);
		tvAviso.setVisibility(View.VISIBLE);
		tvAviso.setText("Lista de lugares vacio (Volley)");
	}
}
