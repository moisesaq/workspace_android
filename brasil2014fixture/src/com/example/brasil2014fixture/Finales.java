package com.example.brasil2014fixture;

import java.util.ArrayList;

import com.moises.adapters.AdapterPartidoFinales;
import com.moises.adapters.AdapterPartidoSemis;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoFinal;
import com.moises.dialogos.DialogoSemis;
import com.moises.modelo.PartidoFinales;
import com.moises.modelo.PartidoSemis;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Finales extends Activity implements OnItemClickListener{

	ListView lvPartidosFinales;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finales);
		

		lvPartidosFinales=(ListView)findViewById(R.id.lvPartidosFinales);
		lvPartidosFinales.setOnItemClickListener(this);
		//listar();
		adaptarListViewPartidosFinales();
	}
	
	public void adaptarListViewPartidosFinales(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			ArrayList<PartidoFinales> lista=db.getAdaptadorPartidosFinales();
			AdapterPartidoFinales adapter=new AdapterPartidoFinales(this, lista);
			lvPartidosFinales.setAdapter(adapter);
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
		PartidoFinales partidoF=(PartidoFinales)a.getAdapter().getItem(posicion);
		DialogoFinal dialogo=new DialogoFinal(this, partidoF, this);
		dialogo.show(getFragmentManager(), "tagfinal");
	}
}
