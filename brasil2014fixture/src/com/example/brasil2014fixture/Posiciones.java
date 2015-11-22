package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterPartidos;
import com.moises.adapters.AdapterPosiciones;
import com.moises.data.base.fixture.DBFixture;
import com.moises.modelo.Equipo;
import com.moises.modelo.Partido;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Posiciones extends Activity{

	
	ListView lvPosiciones;
	String buscarGrupo;
	public void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.posiciones);
				
		//Bundle recojerCajon=getIntent().getExtras();
		//buscarGrupo=recojerCajon.getString("grupo");
		
		lvPosiciones=(ListView)findViewById(R.id.lvPosiciones);
		adaptarListViewPosicioness("A");
	}
	
	public void adaptarListViewPosicioness(String buscar){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<Equipo> listaEquipoPos=db.getAdapterPosiciones(buscar);
			AdapterPosiciones adapterPos=new AdapterPosiciones(this, listaEquipoPos);
			lvPosiciones.setAdapter(adapterPos);
			db.cerrarDB();
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
