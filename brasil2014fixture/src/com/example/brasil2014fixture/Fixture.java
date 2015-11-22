package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterPartidos;
import com.moises.adapters.AdapterPosiciones;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoEditarPartido;
import com.moises.modelo.Equipo;
import com.moises.modelo.Partido;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class Fixture extends Activity implements OnItemClickListener{
	
	TabHost tabhostFixture;
	ListView lvPartFixtu,lvPosFixtu;
	TextView tvGrupoF;
	
	String buscarG;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fixture);
		
		tvGrupoF=(TextView)findViewById(R.id.tvGrupoF);
		tabhostFixture=(TabHost)findViewById(R.id.tabhostFixture);
		configurarTabHost();
		
		Bundle encomienda=getIntent().getExtras();
		buscarG=encomienda.getString("grupo");
		tvGrupoF.setText(buscarG);
		
		lvPosFixtu=(ListView)findViewById(R.id.lvPosicionesFixtu);
		lvPartFixtu=(ListView)findViewById(R.id.lvPartidosFixtu);
		lvPartFixtu.setOnItemClickListener(this);
		adaptarListViewPartidosFixtu(buscarG);
		adaptarListViewPosicionesFixtu(buscarG);
	}
	
	public void configurarTabHost(){
		tabhostFixture.setup();
		TabSpec spec=tabhostFixture.newTabSpec("tagPartidos");
		spec.setIndicator("Partidos");
		spec.setContent(R.id.tabPartidos);
		tabhostFixture.addTab(spec);
		
		spec=tabhostFixture.newTabSpec("tagPosiciones");
		spec.setIndicator("Posiciones");
		spec.setContent(R.id.tabPosiciones);
		tabhostFixture.addTab(spec);
	}
	
	public void adaptarListViewPartidosFixtu(String buscar){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<Partido> listaPartidos=db.getAdapterPatidosGrupo(buscar);
			AdapterPartidos adapter=new AdapterPartidos(this, listaPartidos);
			lvPartFixtu.setAdapter(adapter);
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
	
	public void adaptarListViewPosicionesFixtu(String buscar){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<Equipo> listaEquipoPos=db.getAdapterPosiciones(buscar);
			AdapterPosiciones adapterPos=new AdapterPosiciones(this, listaEquipoPos);
			lvPosFixtu.setAdapter(adapterPos);
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
	public void onItemClick(AdapterView<?> a, View view, int posicion, long parent) {
		Partido partido=(Partido)a.getAdapter().getItem(posicion);
		DialogoEditarPartido dialog=new DialogoEditarPartido(this, partido, this);
		dialog.show(getFragmentManager(), "tagFix");
		
	}
}
