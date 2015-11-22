package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterEquipos;
import com.moises.data.base.fixture.DBFixture;
import com.moises.modelo.Equipo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListaEquipos extends Activity{
	
	ListView lvListaEquipos;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listaequipos);
		
		lvListaEquipos=(ListView)findViewById(R.id.lvListaEquipos);
		adaptarListView();
	}
	
	public void adaptarListView(){
		DBFixture dbfixtu=new DBFixture(this);
		try {
			dbfixtu.AbrirDB();
			ArrayList<Equipo> listaEquipos=dbfixtu.getAdapterEquipo();
			AdapterEquipos adapter=new AdapterEquipos(this, listaEquipos);
			lvListaEquipos.setAdapter(adapter);
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			Dialog mensaje=new Dialog(this);
			mensaje.setTitle("Error al cargar lista");
			TextView texto=new TextView(this);
			texto.setText(error);
			mensaje.setContentView(texto);
			mensaje.show();
		}
	}
}
