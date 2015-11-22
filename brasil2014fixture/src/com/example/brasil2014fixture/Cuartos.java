package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterPartidoCuartos;
import com.moises.adapters.AdapterPartidosOctavos;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoCuartos;
import com.moises.modelo.PartidoCuartos;
import com.moises.modelo.PartidoOctavos;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Cuartos extends Activity implements OnItemClickListener{
	
	ListView lvPartidosCuartos;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cuartos);
		
		lvPartidosCuartos=(ListView)findViewById(R.id.lvPartidosCuartos);
		adaptarListViewPartidosCuartos();
		lvPartidosCuartos.setOnItemClickListener(this);
	}
	
	public void adaptarListViewPartidosCuartos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<PartidoCuartos> lista= db.getAdaptadorPartidosCuartos();
			AdapterPartidoCuartos adapter= new AdapterPartidoCuartos(this, lista);
			lvPartidosCuartos.setAdapter(adapter);
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
	public void onItemClick(AdapterView<?> a, View vista, int posicion, long arg3) {
		PartidoCuartos partidoC=(PartidoCuartos)a.getAdapter().getItem(posicion);
		DialogoCuartos dialogo=new DialogoCuartos(this, partidoC, this);
		dialogo.show(getFragmentManager(), "tagCuartos");
	}
}
