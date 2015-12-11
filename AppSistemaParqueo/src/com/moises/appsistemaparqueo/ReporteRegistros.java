package com.moises.appsistemaparqueo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReporteRegistros extends Activity{
	
	ListView reporte;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reporteregistros);
		
		reporte = (ListView) findViewById(R.id.listViewReport);
		
		DBParqueo dbparqueo = new DBParqueo(this);
		
		boolean funciona = true;
		
		try {
			
			dbparqueo.abrir();
			final String[] listaR = dbparqueo.getReporte();
			dbparqueo.cerrar();
			
			final ArrayList<String> arrayLista = new ArrayList<String>();
			final ArrayAdapter<String> adaptadorArray = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, arrayLista);
			reporte.setAdapter(adaptadorArray);
			
			for (int i = 0; i < listaR.length; i++) {
				arrayLista.add(listaR[i].toString());
				adaptadorArray.notifyDataSetChanged();
			}
						
		} catch (Exception e) {
			funciona = false;
			String error = e.toString();
			Dialog mensaje = new Dialog(this);
			mensaje.setTitle("Error!!");
			TextView texto = new TextView(this);
			texto.setText(error);
			mensaje.setContentView(texto);
			mensaje.show();
			
		}finally{
			if(funciona){
				Toast sms = Toast.makeText(this, "Lista de entrada y salidas", Toast.LENGTH_SHORT);
				sms.show();
			}
			
		}	
	}
}
