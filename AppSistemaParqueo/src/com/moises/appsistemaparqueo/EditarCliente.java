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

public class EditarCliente extends Activity implements OnClickListener, android.widget.AdapterView.OnItemClickListener,
android.widget.AdapterView.OnItemLongClickListener{
	
	EditText placa,modelo,color,nombre,apellidos,ci,celular;
	ImageView imagenCliente;
	Button editar,eliminar;
	GridView imagenes;
	SlidingDrawer sd;
	String buscarPlaca;
	
	Integer[] IDImagenes = {
			R.drawable.coche1,
			R.drawable.coche2,
			R.drawable.coche3,
			R.drawable.coche4,
			R.drawable.coche5,
			R.drawable.coche6
			};
	int idimg = R.drawable.ic_launcher;
	
	DBParqueo dbparqueo = new DBParqueo(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editarcliente);
		
		Bundle cajonsito = getIntent().getExtras();
		buscarPlaca = cajonsito.getString("BuscarPlaca");
		
		sd = (SlidingDrawer) findViewById(R.id.slidingDrawerImagesEditar);
		imagenes = (GridView) findViewById(R.id.gvImagenesEditar);
		imagenes.setAdapter(new ImageAdapter(this, IDImagenes));
		imagenes.setOnItemClickListener(this);
		imagenes.setOnItemLongClickListener(this);
		
		placa = (EditText) findViewById(R.id.etPlacaE);
		modelo = (EditText) findViewById(R.id.etModeloE);
		color = (EditText) findViewById(R.id.etColorE);
		nombre = (EditText) findViewById(R.id.etNombreE);
		apellidos = (EditText) findViewById(R.id.etApellidosE);
		ci = (EditText) findViewById(R.id.etCIE);
		celular = (EditText) findViewById(R.id.etCelularE);
		imagenCliente = (ImageView) findViewById(R.id.imageClienteE);
		editar = (Button) findViewById(R.id.btEditarCliente);
		eliminar = (Button) findViewById(R.id.btEliminar);
		
		editar.setOnClickListener(this);
		imagenCliente.setOnClickListener(this);
		eliminar.setOnClickListener(this);
		
		
		
		try {
			dbparqueo.abrir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		placa.setText(buscarPlaca);
		modelo.setText(dbparqueo.getModelo(buscarPlaca));
		color.setText(dbparqueo.getColorCoche(buscarPlaca));
		nombre.setText(dbparqueo.getNombre(buscarPlaca));
		apellidos.setText(dbparqueo.getApellido(buscarPlaca));
		ci.setText(dbparqueo.getCI(buscarPlaca)+"");
		celular.setText(dbparqueo.getMovil(buscarPlaca)+"");
		imagenCliente.setImageResource(dbparqueo.getImagen(buscarPlaca));
		dbparqueo.cerrar();
		
		
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
	public void onClick(View oc) {
		switch(oc.getId()){
		case R.id.btEditarCliente:
			
			boolean funciona = true;
			
			try{
				
				dbparqueo.abrir();
				
				String Placa = placa.getText().toString();
				String Modelo = modelo.getText().toString();
				String Color = color.getText().toString();
				String Nombre = nombre.getText().toString();
				String Apellidos = apellidos.getText().toString();
				int CI = Integer.parseInt(ci.getText().toString());
				int Celular = Integer.parseInt(celular.getText().toString());
				String Imagen = String.valueOf(idimg);
				
				dbparqueo.EditarCliente(Placa, Modelo, Color, Nombre, Apellidos, CI, Celular, Imagen);
				dbparqueo.cerrar();
				
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
//					Toast sms = Toast.makeText(this, "Cliente editado y guardado", Toast.LENGTH_SHORT);
//					sms.show();
					Dialog mensaje = new Dialog(this);
					mensaje.setTitle("Correcto!!");
					TextView texto = new TextView(this);
					texto.setText("Cliente editado y guardo");
					mensaje.setContentView(texto);
					mensaje.show();
				}
				
			}
			break;
			
		case R.id.imageClienteE:
			sd.open();
			break;
		
		case R.id.btEliminar:
			boolean func = true;
			
			try{
				
				
				dbparqueo.abrir();
				if(dbparqueo.getCarrilOcupaba(buscarPlaca)==0){
					Dialog mensaje = new Dialog(this);
					mensaje.setTitle("Nota!!");
					TextView texto = new TextView(this);
					texto.setText("El cliente no se puede eliminar afectario los reportes");
					mensaje.setContentView(texto);
					mensaje.show();
				}else{
					dbparqueo.eliminarCliente(buscarPlaca);
					
					
					placa.setText("");
					modelo.setText("");
					color.setText("");
					nombre.setText("");
					apellidos.setText("");
					ci.setText("");
					celular.setText("");
					imagenCliente.setImageResource(idimg);
				}
		
				dbparqueo.cerrar();
				
					
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
				if(func){
//					Toast sms = Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT);
//					sms.show();
					Dialog mensaje = new Dialog(this);
					mensaje.setTitle("Correcto!!");
					TextView texto = new TextView(this);
					texto.setText("Cliente eliminado");
					mensaje.setContentView(texto);
					mensaje.show();
				}
				
			}
			break;
		
		}
	}
}
