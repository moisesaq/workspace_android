package com.moises.appsistemaparqueo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EntradaCoches extends Activity implements android.widget.AdapterView.OnItemClickListener, OnClickListener{

	
	GridView carriles;
	SlidingDrawer sd;
	EditText placaRegistro;
	Button ok;
	TextView carril;
	DatePicker fecha;
	TimePicker hora;
	
	String fechaE="23/11/2013";
	String horaE="19:23:12";
	String estado="entrada";
	
		public Integer[] IDpistas = {
			R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,
			R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,
			R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,
			R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril,R.drawable.carril
			};
	
	int posCarril=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entradacoche);
		
		carriles = (GridView) findViewById(R.id.gvParqueo);
		updateParqueo();
		carriles.setOnItemClickListener(this);
		
		sd = (SlidingDrawer) findViewById(R.id.slidingDrawerRegister);
		carril = (TextView) findViewById(R.id.tvNumCarril);
		placaRegistro = (EditText) findViewById(R.id.etPlacaDato);
		ok = (Button) findViewById(R.id.btOK);
		ok.setOnClickListener(this);
		
		fecha = (DatePicker) findViewById(R.id.datePickerEntrada);
		hora = (TimePicker) findViewById(R.id.timePickerEntrada);
		hora.setIs24HourView(true);
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
		carril.setText("");
		boolean funciona = true;
		try {
			DBParqueo dbparqueo = new DBParqueo(this);
			dbparqueo.abrir();
			if(dbparqueo.getDisponible(position+1)){
				carril.setText("Carril # "+(position+1));
				posCarril = position+1;
				sd.open();
				Toast nota = Toast.makeText(this, "Carril disponible", Toast.LENGTH_SHORT);
				nota.show();
			}else{
				String placaO = dbparqueo.getPlacaRegistro(position+1);
				String nombre = dbparqueo.getNombre(placaO);
				Dialog mensaje = new Dialog(this);
				mensaje.setTitle("Carril no disponible");
				TextView texto = new TextView(this);
				texto.setText("El carril esta ocupado por: \n"+nombre+"\n"+" Placa : "+placaO);
				mensaje.setContentView(texto);
				mensaje.show();
			}
			dbparqueo.cerrar();
		} catch (Exception e) {
			funciona = false;
			String error = e.toString();
			Dialog mensaje = new Dialog(this);
			mensaje.setTitle("Error!!");
			TextView texto = new TextView(this);
			texto.setText(error);
			mensaje.setContentView(texto);
			mensaje.show();
		}		
	}
	@Override
	public void onClick(View arg0) {
		fechaE = fecha.getMonth()+"/"+fecha.getDayOfMonth()+"/"+fecha.getYear();
		horaE = hora.getCurrentHour()+":"+hora.getCurrentMinute()+":00";
		boolean funciona = true;
		try{
			DBParqueo db = new DBParqueo(this);
			db.abrir();
			String placa = placaRegistro.getText().toString();
			if(db.getExisteCliente(placa)){
				
				db.registrarEntradaSalida(fechaE,horaE,estado,placa,posCarril);
				db.ActualizarCarril(posCarril,"no");
				
				
			}else{
				funciona = false;
				Dialog mensaje = new Dialog(this);
				mensaje.setTitle("Placa no registrada");
				TextView texto = new TextView(this);
				texto.setText("Realice un nuevo registro");
				mensaje.setContentView(texto);
				mensaje.show();
			}
			db.cerrar();
			sd.close();
		}catch(Exception e){
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
				updateParqueo();
//				Toast nota = Toast.makeText(this, "Coche parqueado carril ocupado", Toast.LENGTH_SHORT);
//				nota.show();
				placaRegistro.setText("");
				Dialog mensaje = new Dialog(this);
				mensaje.setTitle("Registro correcto");
				TextView texto = new TextView(this);
				texto.setText("El coche se posiciono en el carril");
				mensaje.setContentView(texto);
				mensaje.show();
			}
		}
	}
	
	public void updateParqueo(){
		
		String prueba="";
		boolean funciona = true;
		try{
			DBParqueo dbpar = new DBParqueo(this);
			dbpar.abrir();
			Integer[] carrilesOcupados;
			carrilesOcupados = dbpar.getCarrilesOcupados();
			String[] placasClientes = new String[carrilesOcupados.length];
			Integer[] imageClientes = new Integer[carrilesOcupados.length];
			
			for (int j = 0; j < placasClientes.length; j++) {
				placasClientes[j]=dbpar.getPlacaRegistro(carrilesOcupados[j]);
			}
			
			for (int k = 0; k < imageClientes.length; k++) {
				imageClientes[k]=dbpar.getImagen(placasClientes[k]);
			}
			
			for (int i = 0; i < imageClientes.length; i++) {
				ActualizarImgs(carrilesOcupados[i], imageClientes[i]);
			}
			
			carriles.setAdapter(new ImageAdapter(this,IDpistas));
			dbpar.cerrar();
			
			
		}catch(Exception e){
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
				Toast nota = Toast.makeText(this, "Parque actulizado!!", Toast.LENGTH_SHORT);
				nota.show();
			}
		}	
	}
	
	public void ActualizarImgs(int pos, int val){
		int posOriginal =pos-1;
		IDpistas[posOriginal]=val;
	}

}
