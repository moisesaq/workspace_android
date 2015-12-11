package com.moises.appsistemaparqueo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaClientes extends Activity{
	
	ListView lvLista;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listaclientes);
		
		lvLista = (ListView) findViewById(R.id.listViewClintes);
		
		DBParqueo dbparqueo = new DBParqueo(this);
		
		boolean funciona = true;
		
		try {
			
			dbparqueo.abrir();
			final String[] listaC = dbparqueo.ListaClientes();
			dbparqueo.cerrar();
			
			final ArrayList<String> arrayLista = new ArrayList<String>();
			final ArrayAdapter<String> adaptadorArray = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, arrayLista);
			lvLista.setAdapter(adaptadorArray);
			
			for (int i = 0; i < listaC.length; i++) {
				arrayLista.add(listaC[i].toString());
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
				Toast sms = Toast.makeText(this, "Lista de clientes", Toast.LENGTH_SHORT);
				sms.show();
			}
			
		}	
	}

}
