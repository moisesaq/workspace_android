package com.moisse.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogMensaje extends DialogFragment{

	private String mensaje;
	public DialogMensaje(String mensaje){
		this.mensaje = mensaje;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Nota");
		dialog.setMessage(mensaje);
		dialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
			}
		});
		return dialog.create();
	}
}
