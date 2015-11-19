package com.moisse.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogSelectImage extends DialogFragment{

	private NuevoVehiculo nuevo_vehiculo;
	
	public DialogSelectImage(NuevoVehiculo nuevo_vehiculo){
		this.nuevo_vehiculo = nuevo_vehiculo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		String[] opciones = {"Imagen de galeria","Capturar imagen"};
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Elegir opcion"));
		
		dialog.setItems(opciones, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				nuevo_vehiculo.obtenerImagenVehiculo(which);
			}
		});
		
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return dialog.create();
	}
}
