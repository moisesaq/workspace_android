package com.example.brasil2014fixture;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import com.moises.adapters.AdapterPartidos;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoAlerta;
import com.moises.dialogos.DialogoListaPartidos;
import com.moises.modelo.Partido;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

public class RegistroPartidos extends Activity implements OnClickListener{
	
	Button btRegPartido,btBuscarPartido;
	EditText etFecha,etHora;
	AutoCompleteTextView actvEquipo1,actvEquipo2;
	TextView tvTest;
	ImageView ivHora,ivFecha;
	
	int horas,minutos,anio,mes,dia;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registropartidos);
		
		btRegPartido=(Button)findViewById(R.id.btRegistrarPartido);
		btRegPartido.setOnClickListener(this);
		btBuscarPartido=(Button) findViewById(R.id.btBuscarPartidos);
		btBuscarPartido.setOnClickListener(this);
		etFecha=(EditText)findViewById(R.id.etFecha);
		etHora=(EditText)findViewById(R.id.etHora);
		tvTest=(TextView)findViewById(R.id.tvTest);
		actvEquipo1=(AutoCompleteTextView)findViewById(R.id.actvEquipo1);
		actvEquipo2=(AutoCompleteTextView)findViewById(R.id.actvEquipo2);
		adaptarAutoCompleteTV();
		
		ivHora=(ImageView)findViewById(R.id.ivHora);
		ivHora.setOnClickListener(this);
		ivFecha=(ImageView)findViewById(R.id.ivFecha);
		ivFecha.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btRegistrarPartido:
			nuevoRegistroPartido();
			break;
		case R.id.btBuscarPartidos:
			buscarPartido();
			break;
		case R.id.ivHora:
			OnTimeSetListener controlTimePicker = new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					horas=hourOfDay;
					minutos=minute;
					if(minutos<10)
						etHora.setText(horas+":0"+minutos+":00");
					else
						etHora.setText(horas+":"+minutos+":00");
				}
			};
			
			TimePickerDialog miTimeP = new TimePickerDialog(this, controlTimePicker, horas, minutos, true);
			miTimeP.show();
			break;
		case R.id.ivFecha:
			Calendar calendario = Calendar.getInstance();
			anio=calendario.get(Calendar.YEAR);
			mes=calendario.get(Calendar.MONTH);
			dia=calendario.get(Calendar.DAY_OF_MONTH);
			
			OnDateSetListener controlDatePicker=new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					anio=year;
					mes=monthOfYear;
					dia=dayOfMonth;
					int uno=1;
					etFecha.setText(anio+"-"+(mes+uno)+"-"+dia);
				}
			};
			DatePickerDialog miDateP = new DatePickerDialog(this, controlDatePicker, anio, mes, dia);
			miDateP.show();
			break;
		default:
			break;
		}
		
	}
	
	public void adaptarAutoCompleteTV(){
		DBFixture dbfixtu=new DBFixture(this);
		try {
			dbfixtu.AbrirDB();
			String[] paises=dbfixtu.getListaPaises();
			ArrayAdapter<String> aaEquipos=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,paises);
			actvEquipo1.setAdapter(aaEquipos);
			actvEquipo2.setAdapter(aaEquipos);
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al cargar AutoCompleteTextView");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(this);
		mensaje.setTitle("Error "+titulo);
		TextView texto=new TextView(this);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	public void nuevoRegistroPartido(){
		Time horaP=Time.valueOf(etHora.getText().toString());
		Date fechaP=Date.valueOf(etFecha.getText().toString());
		
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			String pais1=actvEquipo1.getText().toString();
			String pais2=actvEquipo2.getText().toString();
			if(db.verificarEquipo(pais1) && db.verificarEquipo(pais2)){
				
				if(db.getGrupoEquipo(pais1).equals(db.getGrupoEquipo(pais2))){
					
					if(!db.verificarPartido(pais1, pais2)){
						db.registrarPartido(pais1, pais2, horaP, fechaP,db.getGrupoEquipo(pais1));
						if(db.verificarPartido(pais1, pais2)){
							Toast toast=Toast.makeText(this, "Partido registrado correctamente", Toast.LENGTH_SHORT);
							toast.show();
						}else{
							Toast toast=Toast.makeText(this, "No se pudo registrar partido", Toast.LENGTH_SHORT);
							toast.show();
						}
					}else{
						Toast toast=Toast.makeText(this, "Este partido ya esta registrado", Toast.LENGTH_SHORT);
						toast.show();
					}
					
				}else{
					Toast toast=Toast.makeText(this, "Equipos no son del mismo grupo", Toast.LENGTH_SHORT);
					toast.show();
				}
				
				
			}else{
				Toast toast=Toast.makeText(this, "Uno de los equipos no existe", Toast.LENGTH_SHORT);
				toast.show();
			}	
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al registrar partido");
		}
	}
	
	public void buscarPartido(){
		Date fecha = Date.valueOf(etFecha.getText().toString());
		DBFixture dbf=new DBFixture(this);
		try {
			dbf.AbrirDB();
			//String[] partidos = dbf.getPartidoFecha(fecha);
			ArrayList<Partido> lista=dbf.getAdapterPatidosFecha(fecha);
			if(lista.size()==0){
				DialogoAlerta dial=new DialogoAlerta("No hay partidos para esta fecha", "Aviso", this);
				dial.show(getFragmentManager(), "tagAviso");
			}else{
				AdapterPartidos adapter = new AdapterPartidos(this, lista);
				
				DialogoListaPartidos dialogo=new DialogoListaPartidos(this, "Lista partidos", adapter);
				dialogo.show(getFragmentManager(), "tagAlerta");
			}
			
//			DialogoAlerta dialogo=new DialogoAlerta(partido, "Partidos", this);
//			dialogo.show(getFragmentManager(), "tagAlerta");
			
			/*String pais1=actvEquipo1.getText().toString();
			String pais2=actvEquipo2.getText().toString();
			if(dbf.verificarEquipo(pais1) && dbf.verificarEquipo(pais2)){
				tvTest.setText(dbf.getPartido(pais1, pais2));
			}else{
				Toast toast=Toast.makeText(this, "Uno de los equipos no existe", Toast.LENGTH_SHORT);
				toast.show();
			}*/
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al registrar partido");
		}
		
	}
}
