package com.moises.appsistemaparqueo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DatosCliente extends Activity{
	
	TextView placa,modelo,color,nombre,apellido,ci,celular;
	ImageView image;
	Button editar;
	String buscarPlaca;
	int cont=0;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datoscliente);
		
		Bundle cajonsito = getIntent().getExtras();
		buscarPlaca = cajonsito.getString("BuscarPlaca");
		
		placa = (TextView) findViewById(R.id.tvPlacaCoche);
		modelo = (TextView) findViewById(R.id.tvModeloCoche);
		color = (TextView) findViewById(R.id.tvColorCoche);
		nombre = (TextView) findViewById(R.id.tvNombreCliente);
		apellido = (TextView) findViewById(R.id.tvApellido);
		ci = (TextView) findViewById(R.id.tvCIdentidad);
		celular = (TextView) findViewById(R.id.tvCelu);
		image = (ImageView) findViewById(R.id.imgvImagenC);
		
		placa.setText(""); modelo.setText(""); color.setText("");
		nombre.setText(""); apellido.setText(""); ci.setText(""); 
		celular.setText("");
	
		editar = (Button) findViewById(R.id.btEditar);
		editar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				DBParqueo db = new DBParqueo(DatosCliente.this);
				try {
					db.abrir();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(db.getExisteCliente(buscarPlaca)){
					Intent i = new Intent("com.moises.appsistemaparqueo.EDITARCLIENTE");
					Bundle cajonsito = new Bundle();
					cajonsito.putString("BuscarPlaca", buscarPlaca);
					i.putExtras(cajonsito);
					startActivity(i);
				}else{
					Toast toast = Toast.makeText(DatosCliente.this, "Cliente no registrado", Toast.LENGTH_SHORT);
					toast.show();
				}
				db.cerrar();
				finish();
				
			}
		});
		
		boolean funciona = true;
		
		try {
			
			
			DBParqueo db = new DBParqueo(this);
			db.abrir();
			placa.setText(buscarPlaca);
			modelo.setText(db.getModelo(buscarPlaca));
			color.setText(db.getColorCoche(buscarPlaca));
			nombre.setText(db.getNombre(buscarPlaca));
			apellido.setText(db.getApellido(buscarPlaca));
			ci.setText(String.valueOf(db.getCI(buscarPlaca)));
			celular.setText(String.valueOf(db.getMovil(buscarPlaca)));
			image.setImageResource(db.getImagen(buscarPlaca));	
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
				Toast sms = Toast.makeText(this, "Datos del cliente", Toast.LENGTH_SHORT);
				sms.show();
			}
			
		}	
	}

}
