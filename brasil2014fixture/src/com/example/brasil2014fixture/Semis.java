package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterPartidoCuartos;
import com.moises.adapters.AdapterPartidoSemis;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoCuartos;
import com.moises.dialogos.DialogoSemis;
import com.moises.modelo.PartidoCuartos;
import com.moises.modelo.PartidoSemis;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Semis extends Activity implements OnItemClickListener{
	
	ListView lvPartidosSemis;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.semis);
		
		lvPartidosSemis=(ListView)findViewById(R.id.lvPartidosSemis);
		lvPartidosSemis.setOnItemClickListener(this);
		//listar();
		adaptarListViewPartidosSemis();
	}
	
	public void listar(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			String[] lista = db.getPartidosSemis();
			db.cerrarDB();
			
			ArrayList<String> arrayLista = new ArrayList<String>();
			ArrayAdapter<String> adaptadorArray = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, arrayLista);
			lvPartidosSemis.setAdapter(adaptadorArray);
			
			for (int i = 0; i < lista.length; i++) {
				arrayLista.add(lista[i].toString());
				adaptadorArray.notifyDataSetChanged();
			}
		} catch (Exception e) {
			String error = e.toString();
			Dialog mensaje = new Dialog(this);
			mensaje.setTitle("Error!!");
			TextView texto = new TextView(this);
			texto.setText(error);
			mensaje.setContentView(texto);
			mensaje.show();
		}
	}
	
	public void adaptarListViewPartidosSemis(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<PartidoSemis> lista= db.getAdaptadorPartidosSemis();
			AdapterPartidoSemis adapter= new AdapterPartidoSemis(this, lista);
			lvPartidosSemis.setAdapter(adapter);
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
		PartidoSemis partidoSemis=(PartidoSemis)a.getAdapter().getItem(posicion);
		DialogoSemis dialogo=new DialogoSemis(this, partidoSemis, this);
		dialogo.show(getFragmentManager(), "tagSemis");
		
	}
}
