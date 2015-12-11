package com.moises.appsistemaparqueo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Clientes extends Activity implements OnClickListener{
	
	ImageView buscar,nuevo,listaclientes;
	EditText datobusqueda;
	DBParqueo db = new DBParqueo(this);
	
	Integer IDpistas[];
//	Integer[] IDpistas = {
//			R.drawable.p1,R.drawable.p1,R.drawable.p1,R.drawable.p1,R.drawable.p1,
//			R.drawable.p1,R.drawable.p1,R.drawable.p1,R.drawable.p1,R.drawable.p1
//			};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientes);
		datobusqueda = (EditText) findViewById(R.id.etBuscar);
		buscar = (ImageView) findViewById(R.id.imgvBuscacliente);
		buscar.setOnClickListener(this);
		listaclientes = (ImageView) findViewById(R.id.imgvListaclientes);
		listaclientes.setOnClickListener(this);
		nuevo = (ImageView) findViewById(R.id.imgvNuevocliente);
		nuevo.setOnClickListener(this);
	}
	
	public void onClick(View ac){
		switch(ac.getId()){
		case R.id.imgvBuscacliente:
			
			String datoB = datobusqueda.getText().toString(); 
			
			try {
				db.abrir();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(db.getExisteCliente(datoB)){
				Intent inte = new Intent("com.moises.appsistemaparqueo.DATOSCLIENTE");
				Bundle cajonsito = new Bundle();
				cajonsito.putString("BuscarPlaca", datoB);
				inte.putExtras(cajonsito);
				startActivity(inte);
			}else{
				Dialog mensaje = new Dialog(this);
				mensaje.setTitle("Nota!!");
				TextView texto = new TextView(this);
				texto.setText("El cliente no esta registrado");
				mensaje.setContentView(texto);
				mensaje.show();
			}
			db.cerrar();
				
			break;
		
		case R.id.imgvListaclientes:
			Intent intento = new Intent("com.moises.appsistemaparqueo.LISTACLIENTES");
			startActivity(intento);

			break;
		
		case R.id.imgvNuevocliente:
			Intent in = new Intent("com.moises.appsistemaparqueo.NUEVOCLIENTE");
			startActivity(in);
			break;
		}
	}
	
	public void ActImg(int pos, int val){
		int posOriginal =pos-1;
		IDpistas[posOriginal]=val;
	}
	
	public void contruirPistas(){
		boolean funciona=true;
		try {
			db.abrir();
			int cantidad = db.getCantidadCarriles();
			IDpistas = new Integer[cantidad];
			for (int i = 0; i < IDpistas.length; i++) {
				ActImg(i+1, R.drawable.carril);
			}
			db.cerrar();
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
				Toast nota = Toast.makeText(this, "Carriles del parque construidos", Toast.LENGTH_SHORT);
				nota.show();
			}
		}
		
	}

}
