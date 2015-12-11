package com.moises.appsistemaparqueo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoCliente extends Activity implements OnClickListener, android.widget.AdapterView.OnItemClickListener,
	android.widget.AdapterView.OnItemLongClickListener{
	
	EditText placa,modelo,color,nombre,apellidos,ci,celular;
	ImageView imagenCliente;
	Button guardar,verCliente;
	GridView imagenes;
	SlidingDrawer sd;
	String datoBusqueda = "asd123";
	
	Integer[] IDImagenes = {
			R.drawable.coche1,
			R.drawable.coche2,
			R.drawable.coche3,
			R.drawable.coche4,
			R.drawable.coche5,
			R.drawable.coche6
			};
	int idimg = R.drawable.ic_launcher;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevocliente);
		
		sd = (SlidingDrawer) findViewById(R.id.slidingDrawerImages);
		imagenes = (GridView) findViewById(R.id.gvImagenesClientes);
		imagenes.setAdapter(new ImageAdapter(this, IDImagenes));
		imagenes.setOnItemClickListener(this);
		imagenes.setOnItemLongClickListener(this);
		
		placa = (EditText) findViewById(R.id.etPlaca);
		modelo = (EditText) findViewById(R.id.etModelo);
		color = (EditText) findViewById(R.id.etColor);
		nombre = (EditText) findViewById(R.id.etNombre);
		apellidos = (EditText) findViewById(R.id.etApellidos);
		ci = (EditText) findViewById(R.id.etCI);
		celular = (EditText) findViewById(R.id.etCelular);
		imagenCliente = (ImageView) findViewById(R.id.imageCliente);
		guardar = (Button) findViewById(R.id.btGuardar);
		verCliente = (Button) findViewById(R.id.btVerCliente);
		
		guardar.setOnClickListener(this);
		verCliente.setOnClickListener(this);
		imagenCliente.setOnClickListener(this);
	}

	@Override
	public void onClick(View oc) {
		switch(oc.getId()){
		case R.id.btGuardar:
			
			boolean funciona = true;
			
			try{
				DBParqueo dbparqueo = new DBParqueo(this);
				dbparqueo.abrir();
				String Placa = placa.getText().toString();
				if(dbparqueo.getExisteCliente(Placa)){
					funciona = false;
					Toast toast = Toast.makeText(this, "Esta placa ya esta registrado", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					String Modelo = modelo.getText().toString();
					String Color = color.getText().toString();
					String Nombre = nombre.getText().toString();
					String Apellidos = apellidos.getText().toString();
					int CI = Integer.parseInt(ci.getText().toString());
					int Celular = Integer.parseInt(celular.getText().toString());
					String Imagen = String.valueOf(idimg);
					
					dbparqueo.NuevoCliente(Placa, Modelo, Color, Nombre, Apellidos, CI, Celular, Imagen);
					dbparqueo.cerrar();
				}

				modelo.setText("");
				color.setText("");
				nombre.setText("");
				apellidos.setText("");
				ci.setText("");
				celular.setText("");
				imagenCliente.setImageResource(idimg);
				
				
			}catch(Exception e){
				funciona = false;
				String error = e.toString();
				Dialog mensaje = new Dialog(this);
				mensaje.setTitle("Error");
				TextView texto = new TextView(this);
				texto.setText(error);
				mensaje.setContentView(texto);
				mensaje.show();
				
			}finally{
				if(funciona){
//					Toast sms = Toast.makeText(this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT);
//					sms.show();
					
					Dialog mensaje = new Dialog(this);
					mensaje.setTitle("Correcto!!");
					TextView texto = new TextView(this);
					texto.setText("Cliente registrado");
					mensaje.setContentView(texto);
					mensaje.show();
					
					verCliente.setEnabled(true);
					datoBusqueda = placa.getText().toString();
				}
				
			}
			break;
			
		case R.id.btVerCliente:
			//String datoB = placa.getText().toString();
			DBParqueo db = new DBParqueo(this);
			try {
				db.abrir();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(db.getExisteCliente(datoBusqueda)){
				Intent inte = new Intent("com.moises.appsistemaparqueo.DATOSCLIENTE");
				Bundle cajonsito = new Bundle();
				cajonsito.putString("BuscarPlaca", datoBusqueda);
				inte.putExtras(cajonsito);
				startActivity(inte);
				finish();
			}else{
				Toast toast = Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT);
				toast.show();
			}
			db.cerrar();
			break;
			
		case R.id.imageCliente:
			sd.open();
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
		Dialog dial = new Dialog(this);
		dial.setTitle("Presionar 2 seg para elegir");
		ImageView imv = new ImageView(this);
		imv.setImageResource(IDImagenes[position]);
		dial.setContentView(imv);
		dial.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position,
			long arg3) {
		imagenCliente.setImageResource(IDImagenes[position]);
		idimg = IDImagenes[position];
		Toast msj = Toast.makeText(this, "Imagen eligida", Toast.LENGTH_SHORT);
		msj.show();
		sd.close();
		return false;
	}
}
