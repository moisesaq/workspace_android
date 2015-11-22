package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterPartidos;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoEditarPartido;
import com.moises.modelo.Partido;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Partidos extends Activity implements OnItemClickListener{
	
	ListView lvPartidos;
	String buscarGrupo;
	public TextView tvGrupoPartidos;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partidos);
		
		tvGrupoPartidos=(TextView)findViewById(R.id.tvGrupoPartidos);
		
		Bundle recojerCajon=getIntent().getExtras();
		buscarGrupo=recojerCajon.getString("grupo");
		tvGrupoPartidos.setText(buscarGrupo);
		
		lvPartidos=(ListView)findViewById(R.id.lvPartidos);
		lvPartidos.setOnItemClickListener(this);
		adaptarListViewPartidos(buscarGrupo);
	}
	
	public void adaptarListViewPartidos(String buscar){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<Partido> listaPartidos=db.getAdapterPatidosGrupo(buscar);
			AdapterPartidos adapter=new AdapterPartidos(this, listaPartidos);
			lvPartidos.setAdapter(adapter);
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
	public void onItemClick(AdapterView<?> a, View v, int posicion, long parent) {
		Partido partido=(Partido)a.getAdapter().getItem(posicion);
		//DialogoEditarPartido dialogo=new DialogoEditarPartido(this, partido,this);
		//dialogo.show(getFragmentManager(), "tagEditar");
	}
}
