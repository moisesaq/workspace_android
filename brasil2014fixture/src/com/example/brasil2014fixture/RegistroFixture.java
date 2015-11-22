package com.example.brasil2014fixture;

import com.moises.adapters.ImageAdapter;
import com.moises.data.base.fixture.DBFixture;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroFixture extends Activity implements OnClickListener,OnItemSelectedListener,OnItemClickListener{
	
	TabHost thRegistro;
	Button btAceptar,btBuscarG, btRegistrarE,btBuscarE;
	EditText etGrupo,etPais;
	Spinner spnGrupo;
	TextView tvRespuesta,tvResultado;
	ImageView imgvBandera;
	SlidingDrawer slidingDrawBanderas;
	GridView gvBanderas;
	
	String idgrupo;
	
	Integer[] IDBanderas={R.drawable.brasil,R.drawable.chile};
	
	Integer imagenBandera=R.drawable.bandera;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrofixture);
		
		thRegistro =(TabHost) findViewById(R.id.thRegistro);
		configurarTabHost();
		
		//-----------------Reconocimiento de tab2 registro de grupos----------------
		
		btAceptar =(Button) findViewById(R.id.btAceptar);
		btAceptar.setOnClickListener(this);
		etGrupo = (EditText)findViewById(R.id.etGrupo);
		btBuscarG=(Button)findViewById(R.id.btBuscarGrupo);
		btBuscarG.setOnClickListener(this);
		tvRespuesta=(TextView)findViewById(R.id.tvRespuesta);
		
		//-----------------Reconocimiento de tab1 registro de equipos----------------
	
		etPais=(EditText) findViewById(R.id.etPais);
		btRegistrarE=(Button) findViewById(R.id.btRegistrarEquipo);
		btRegistrarE.setOnClickListener(this);
		btBuscarE=(Button)findViewById(R.id.btBuscarEquipo);
		btBuscarE.setOnClickListener(this);
		tvResultado=(TextView)findViewById(R.id.tvResultado);
		
		spnGrupo=(Spinner)findViewById(R.id.spinnerGrupo);
		spnGrupo.setAdapter(adaptarSpinner());
		spnGrupo.setOnItemSelectedListener(this);
		
		imgvBandera=(ImageView)findViewById(R.id.imgvBandera);
		imgvBandera.setOnClickListener(this);
		
		slidingDrawBanderas=(SlidingDrawer) findViewById(R.id.slidingDrawerBanderas);
		gvBanderas=(GridView)findViewById(R.id.gvBanderas);
		gvBanderas.setAdapter(new ImageAdapter(this, IDBanderas));
		gvBanderas.setOnItemClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btAceptar:
			registrarNuevoGrupo();
			break;
		case R.id.btBuscarGrupo:
			buscarGrupo();
			break;
		
		case R.id.btRegistrarEquipo:
			registrarNuevoEquipo();
			break;
			
		case R.id.imgvBandera:
			slidingDrawBanderas.open();
			break;
		
		case R.id.btBuscarEquipo:
			buscarEquipo();
			break;
		default:
			break;
		}
		
	}
	
	public ArrayAdapter adaptarSpinner(){
		
		ArrayAdapter adaptador = null;
		
		DBFixture dbfixtu=new DBFixture(this);
		try {
			dbfixtu.AbrirDB();
			String[] grupo=dbfixtu.getListaGrupos();
			adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,grupo);
			return adaptador;
		} catch (Exception e) {
			String error =e.toString();
			mensajeError(error,"cargar spinner");
		}
		dbfixtu.cerrarDB();
		return adaptador;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		int posicion=spnGrupo.getSelectedItemPosition();
		
		idgrupo=spnGrupo.getSelectedItem().toString();
		Toast toast=Toast.makeText(this, idgrupo, Toast.LENGTH_SHORT);
		toast.show();	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void configurarTabHost(){
		thRegistro.setup();
		TabSpec spec = thRegistro.newTabSpec("tag1");
		spec.setIndicator("Equipos");
		spec.setContent(R.id.tab1);
		thRegistro.addTab(spec);
		
		spec=thRegistro.newTabSpec("tag2");
		spec.setIndicator("Grupos");
		spec.setContent(R.id.tab2);
		thRegistro.addTab(spec);
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(this);
		mensaje.setTitle("Error al "+titulo);
		TextView texto=new TextView(this);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int posicion, long arg3) {
		imgvBandera.setImageResource(IDBanderas[posicion]);
		imagenBandera=IDBanderas[posicion];
		Toast toast=Toast.makeText(this, "Imagen seleccionado", Toast.LENGTH_SHORT);
		toast.show();
		slidingDrawBanderas.close();	
	}
	
	public void registrarNuevoGrupo(){
		DBFixture dbfix= new DBFixture(this);
		try {
			dbfix.AbrirDB();
			if(!dbfix.verificarGrupo(etGrupo.getText().toString())){
				
				dbfix.registrarGrupo(etGrupo.getText().toString());
				if(dbfix.verificarGrupo(etGrupo.getText().toString())){
					spnGrupo.setAdapter(adaptarSpinner());
					Toast toast=Toast.makeText(this, "Registro correcto", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast=Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT);
					toast.show();
				}
			}else{
				Toast toast=Toast.makeText(this, "Ese grupo ya existe", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"registrar");
			
		}
		dbfix.cerrarDB();
	}
	
	public void buscarGrupo(){
		DBFixture dbfixt= new DBFixture(this);
		try {
			dbfixt.AbrirDB();
			if(dbfixt.verificarGrupo(etGrupo.getText().toString())){
				String res=dbfixt.getGrupo(etGrupo.getText().toString());
				tvRespuesta.setText(res);
			}else{
				Toast toast=Toast.makeText(this, "No existe ese grupo", Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (Exception e) {
			String error =e.toString();
			mensajeError(error,"buscar");
		}
		dbfixt.cerrarDB();
	}
	
	public void registrarNuevoEquipo(){
		DBFixture dbfixtu= new DBFixture(this);
		try {
			dbfixtu.AbrirDB();
			if(!dbfixtu.verificarEquipo(etPais.getText().toString())){
				dbfixtu.registrarEquipo(etPais.getText().toString(), imagenBandera, idgrupo);
				if(dbfixtu.verificarEquipo(etPais.getText().toString())){
					Toast toast=Toast.makeText(this, "Pais registrado correctamente", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast=Toast.makeText(this, "No se pudo registrar pais", Toast.LENGTH_SHORT);
					toast.show();
				}
			}else{
				Toast toast=Toast.makeText(this, "Ya existe ese pais", Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (Exception e) {
			String error =e.toString();
			mensajeError(error,"buscar");
		}
		dbfixtu.cerrarDB();
	}
	
	public void buscarEquipo(){
		DBFixture dbfixt= new DBFixture(this);
		try {
			dbfixt.AbrirDB();
			String pais=etPais.getText().toString();
			if(dbfixt.verificarEquipo(pais)){
				String res=dbfixt.getEquipo(pais);
				Integer imagen=dbfixt.getImagenBandera(pais);
				tvResultado.setText(res);
				imgvBandera.setImageResource(imagen);
			}else{
				Toast toast=Toast.makeText(this, "No existe ese equipo", Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (Exception e) {
			String error =e.toString();
			mensajeError(error,"buscar");
		}
		dbfixt.cerrarDB();
	}
}
