package com.moises.appsistemaparqueo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TimePicker;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPrincipal extends Activity implements OnClickListener{
	
	DBParqueo dbpark = new DBParqueo(this);
	ImageView imgvOpc1,imgvOpc2,imgvOpc3,imgvOpc4, OKSalida;
	EditText placaSalida;
	SlidingDrawer sdS;
	
	DatePicker fecha;
	TimePicker hora;
	
	String fechaS="23/11/2013";
	String horaS="19:23:12";
	String estado="salida";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuprincipal);
		
		sdS = (SlidingDrawer) findViewById(R.id.slidingDrawerSalida);
		placaSalida = (EditText) findViewById(R.id.etPlacaSalida);
		imgvOpc1 = (ImageView) findViewById(R.id.imgvOpc1);
		imgvOpc1.setOnClickListener(this);
		imgvOpc2 = (ImageView) findViewById(R.id.imgvOpc2);
		imgvOpc2.setOnClickListener(this);
		imgvOpc3 = (ImageView) findViewById(R.id.imgvOpc3);
		imgvOpc3.setOnClickListener(this);
		imgvOpc4 = (ImageView) findViewById(R.id.imgvOpc4);
		imgvOpc4.setOnClickListener(this);
		OKSalida = (ImageView) findViewById(R.id.ivOkSalida);
		OKSalida.setOnClickListener(this);
		
		fecha = (DatePicker) findViewById(R.id.datePickerSalida);
		hora = (TimePicker) findViewById(R.id.timePickerSalida);
		hora.setIs24HourView(true);
	}

	@Override
	public void onClick(View ec) {
		switch(ec.getId()){
		case R.id.imgvOpc1:
			sdS.open();
			break;
		case R.id.imgvOpc2:
			try {
				dbpark.abrir();
				if(dbpark.getCantidadCarriles()==0){
					for (int i = 0; i < 20; i++) {
						dbpark.InsertarCarril(i+1, "si");
					}
				}
				dbpark.cerrar();
				Intent inte = new Intent("com.moises.appsistemaparqueo.ENTRADACOCHES");
				startActivity(inte);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		case R.id.imgvOpc3:
			
			Intent intentRe = new Intent("com.moises.appsistemaparqueo.REPORTEREGISTROS");
			startActivity(intentRe);
			
			break;
		case R.id.imgvOpc4:
			Intent intent = new Intent("com.moises.appsistemaparqueo.CLIENTES");
			startActivity(intent);
			break;
		case R.id.ivOkSalida:
			fechaS = fecha.getMonth()+"/"+fecha.getDayOfMonth()+"/"+fecha.getYear();
			horaS = hora.getCurrentHour()+":"+hora.getCurrentMinute()+":00";
			boolean funciona = true;
			try{
				String placaS = placaSalida.getText().toString();
				EntradaCoches entra = new EntradaCoches();
				DBParqueo db = new DBParqueo(this);
				db.abrir();
				int carrilOcupaba = db.getCarrilOcupaba(placaS);
				if(db.getExisteCliente(placaS)){
					if(carrilOcupaba!=0){
						db.ActualizarCarril(carrilOcupaba, "si");
						db.registrarEntradaSalida(fechaS, horaS, estado, placaS, carrilOcupaba);
						entra.ActualizarImgs(carrilOcupaba, R.drawable.carril);
						
						placaSalida.setText("");
						Dialog mensaje = new Dialog(this);
						mensaje.setTitle("Correcto!!");
						TextView texto = new TextView(this);
						texto.setText("El carril "+carrilOcupaba+" ya esta disponible");
						mensaje.setContentView(texto);
						mensaje.show();
					}else{
						Toast t = Toast.makeText(this, "La "+placaS+" no esta parqueado", Toast.LENGTH_SHORT);
						t.show();
						
						placaSalida.setText("");
						Dialog mensaje = new Dialog(this);
						mensaje.setTitle("Aviso!!");
						TextView texto = new TextView(this);
						texto.setText("La "+placaS+" no esta parqueado");
						mensaje.setContentView(texto);
						mensaje.show();
					}
				}else{
					placaSalida.setText("");
					Dialog mensaje = new Dialog(this);
					mensaje.setTitle("Aviso!!");
					TextView texto = new TextView(this);
					texto.setText("La "+placaS+" no esta registrado");
					mensaje.setContentView(texto);
					mensaje.show();
				}
				
				
				db.cerrar();
				//sdS.close();
			}catch(Exception e){
				funciona = false;
				String error = e.toString();
				Dialog mensaje = new Dialog(this);
				mensaje.setTitle("Error!!");
				TextView texto = new TextView(this);
				texto.setText(error);
				mensaje.setContentView(texto);
				mensaje.show();
			}
			break;
		}
		
	}

}
