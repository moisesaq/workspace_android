package com.moises.dialogos;

import com.example.brasil2014fixture.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogoAlerta extends DialogFragment{
	
	String mensaje;
	String titulo;
	Context miContexto;

	public DialogoAlerta(String msj, String titulo, Context cont){
		this.mensaje=msj;
		this.titulo=titulo;
		this.miContexto=cont;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder=new AlertDialog.Builder(miContexto);
		
		builder.setMessage(mensaje);
		builder.setTitle(titulo);
		
		builder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
		builder.setIcon(getResources().getDrawable(R.drawable.fuleco));
		builder.setNeutralButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		return builder.create();
	}
}
