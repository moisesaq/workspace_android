package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterEquiposOctavos;
import com.moises.adapters.AdapterPartidos;
import com.moises.adapters.AdapterPartidosOctavos;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoOctavos;
import com.moises.modelo.EquipoOctavos;
import com.moises.modelo.Partido;
import com.moises.modelo.PartidoOctavos;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.GridLayout.Spec;
import android.widget.TabHost.TabSpec;

public class Octavos extends Activity implements OnItemClickListener{
	
	TabHost tabhostOctavos;
	ListView lvPartidosOctavos, lvEquiposOctavos;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.octavos);
		
		tabhostOctavos=(TabHost)findViewById(R.id.tabhostOctavos);
		configTabHostOctavos();
		
		lvPartidosOctavos=(ListView)findViewById(R.id.lvPartidosOctavos);
		lvEquiposOctavos=(ListView)findViewById(R.id.lvEquiposOctavos);
		//listar();
		adaptarListViewPartidosOctavos();
		adaptarListViewEquiposOctavos();
		lvPartidosOctavos.setOnItemClickListener(this);
	}
	
	public void configTabHostOctavos(){
		tabhostOctavos.setup();
		TabSpec spec=tabhostOctavos.newTabSpec("tagPartidosOctavos");
		spec.setIndicator("Partidos");
		spec.setContent(R.id.tabPartidosOctavos);
		tabhostOctavos.addTab(spec);
		
		spec=tabhostOctavos.newTabSpec("tagEquiposOctavos");
		spec.setIndicator("Equipos");
		spec.setContent(R.id.tabEquiposOctavos);
		tabhostOctavos.addTab(spec);
	}
	
	public void adaptarListViewEquiposOctavos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<EquipoOctavos> listaOctavos=db.getAdaptadorEquiposOctavos();
			AdapterEquiposOctavos adapter=new AdapterEquiposOctavos(this, listaOctavos);
			lvEquiposOctavos.setAdapter(adapter);
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
	
	public void adaptarListViewPartidosOctavos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<PartidoOctavos> listaPartidosOctavos= db.getAdaptadorPartidosOctavos();
			AdapterPartidosOctavos adapter=new AdapterPartidosOctavos(this, listaPartidosOctavos);
			lvPartidosOctavos.setAdapter(adapter);
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

	@Override
	public void onItemClick(AdapterView<?> a, View view, int posicion, long arg3) {
		PartidoOctavos partOctavos=(PartidoOctavos)a.getAdapter().getItem(posicion);
		DialogoOctavos dialogo=new DialogoOctavos(this, partOctavos, this);
		dialogo.show(getFragmentManager(), "tagOctavos");
	}
}
