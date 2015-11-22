package com.moises.dialogos;

import com.example.brasil2014fixture.MainActivity;
import com.example.brasil2014fixture.R;
import com.moises.data.base.fixture.DBFixture;
import com.moises.data.base.fixture.generarDatos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogoUsuario extends DialogFragment{
	
	Context miContexto;
	View vista;
	EditText etNombre;
	String nombre;
	MainActivity main;
	

	public DialogoUsuario(Context contexto, MainActivity main){
		this.miContexto=contexto;
		this.main=main;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder dialogo=new AlertDialog.Builder(miContexto);
		
		LayoutInflater infla=getActivity().getLayoutInflater();
		vista=infla.inflate(R.layout.modeloregistrousuario, null);
		
		dialogo.setTitle("Nombre");
		dialogo.setView(vista);
		etNombre=(EditText)vista.findViewById(R.id.etNombre);
		dialogo.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				nombre=etNombre.getText().toString();
				registrarNuevoUsuario(nombre);
				generarNuevosDatos();
				main.fijarNombre();
				Toast toast=Toast.makeText(miContexto, "Nombre :"+nombre, Toast.LENGTH_LONG);
				toast.show();
			}
		});
		dialogo.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return dialogo.create();
	}
	
	public void registrarNuevoUsuario(String n){
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			db.registrarUsuario(n);			
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			Dialog mensaje=new Dialog(miContexto);
			mensaje.setTitle("Error al ");
			TextView texto=new TextView(miContexto);
			texto.setText(error);
			mensaje.setContentView(texto);
			mensaje.show();
		}
	}
	
	public void generarNuevosDatos(){
		generarDatos generar=new generarDatos(miContexto);
		generar.generarGrupos();
		generar.generarEquipos();
		generar.generarPartidos();
		generar.generarEquipoOctavos();
		generar.generarPartidosOctavos();
		generar.generarPartidosCuartos();
		generar.generarPartidosSemis();
		generar.generarPartidosFinales();
	}
}
