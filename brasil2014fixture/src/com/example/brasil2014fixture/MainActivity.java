package com.example.brasil2014fixture;

import java.sql.Time;

import com.moises.data.base.fixture.DBFixture;
import com.moises.data.base.fixture.generarDatos;
import com.moises.dialogos.DialogoAlerta;
import com.moises.dialogos.DialogoUsuario;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	ImageView ivEnter;
	TextView tvNombre;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ivEnter=(ImageView)findViewById(R.id.ivEnter);
		ivEnter.setOnClickListener(this);
		tvNombre=(TextView)findViewById(R.id.tvNombre);
		if(verificar()!=0){
			fijarNombre();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void fijarNombre(){
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			tvNombre.setText(db.getNombre());			
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "registrar usuario");
		}
	}
	public int verificar(){
		int control=0;
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			control=db.getCantidadUsuario();			
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "registrar usuario");
		}
		return control;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ivEnter){
			if(verificar()==0){
				DialogoUsuario dialogo=new DialogoUsuario(this,this);
				dialogo.show(getFragmentManager(), "tagDialogoUser");
			}else{
				Intent intento = new Intent("android.intent.action.MENU");
				startActivity(intento);
				
			}
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
