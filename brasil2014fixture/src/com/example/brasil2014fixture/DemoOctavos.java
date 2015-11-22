package com.example.brasil2014fixture;

import java.sql.Date;
import java.sql.Time;

import com.moises.data.base.fixture.DBFixture;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DemoOctavos extends Activity implements OnClickListener{
	
	EditText etIdOctavos,etPaisOct,etGrupoOct;
	Button btRegistrarOct,btBuscarOct;
	
	EditText etIdRegPartOct,etIdOct1,etPais1PartOct, etPais2PartOct, etIdOct2;
	Button btRegistrarPartOct;
	
	TextView tvResultadoOct;
	
	Integer bandera=R.drawable.bandera;
	Integer bandera2=R.drawable.time;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demooctavos);
		
		etIdOctavos=(EditText)findViewById(R.id.etIdOctavos);
		etPaisOct=(EditText)findViewById(R.id.etPaisOct);
		etGrupoOct=(EditText)findViewById(R.id.etGrupoOct);
		btRegistrarOct=(Button)findViewById(R.id.btRegistrarOct);
		btRegistrarOct.setOnClickListener(this);
		
		etIdRegPartOct=(EditText)findViewById(R.id.etIdRegPartOct);
		etIdOct1=(EditText)findViewById(R.id.etIdOct1);
		etPais1PartOct=(EditText)findViewById(R.id.etPais1PartOct);
		etPais2PartOct=(EditText)findViewById(R.id.etPais2PartOct);
		etIdOct2=(EditText)findViewById(R.id.etIdOct2);
		
		btRegistrarPartOct=(Button)findViewById(R.id.btRegistrarPartOct);
		btRegistrarPartOct.setOnClickListener(this);
		
		btBuscarOct=(Button)findViewById(R.id.btBuscarOct);
		btBuscarOct.setOnClickListener(this);
		tvResultadoOct=(TextView)findViewById(R.id.tvResultadoOct);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btRegistrarOct:
			nuevoRegistroOctavos();
			break;
		
		case R.id.btBuscarOct:
			//buscarOctavos();
			actualizarEquiposOctavos();
			break;
			
		case R.id.btRegistrarPartOct:
			nuevoRegistroPartidoOctavos();
			break;
		default:
			break;
		}
	}
	
	public void nuevoRegistroOctavos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			db.registrarOctavos(etIdOctavos.getText().toString(), etPaisOct.getText().toString(), bandera, etGrupoOct.getText().toString());
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "registrar octavos");
		}
	}
	
	public void actualizarEquiposOctavos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			db.actualizarOctavos(etIdOctavos.getText().toString(), etPaisOct.getText().toString(), bandera2, etGrupoOct.getText().toString());
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "registrar octavos");
		}
	}
	
	public void nuevoRegistroPartidoOctavos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			db.registrarPartidosOctavos(etIdRegPartOct.getText().toString(), etIdOct1.getText().toString(), etIdOct2.getText().toString(),Date.valueOf("2014-06-11"),Time.valueOf("17:00:00"));
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "registrar partido octavos");
		}
	}
	
	public void buscarOctavos(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			String pais=db.buscarPaisOctavos(etIdOctavos.getText().toString());
			tvResultadoOct.setText(pais);
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "buscar octavos");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(this);
		mensaje.setTitle("Error al "+titulo);
		TextView texto=new TextView(this);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
}
