package com.moisse.dialogs;
import com.example.parqueo.R;
import com.moisse.fragments.NuevoCliente;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogOptionImageCliente extends DialogFragment{

	private final String[] opciones = {"Imagen de galeria","Capturar imagen"};
	private final String[] opciones2 = {"Imagen de galeria","Capturar imagen", "Quitar imagen"};
	NuevoCliente nuevoCliente;
	
	public DialogOptionImageCliente(NuevoCliente nuevoCliente){
		this.nuevoCliente = nuevoCliente;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		
		dialog.setTitle("Completar accion con..");
		if(nuevoCliente.pathImageCliente.equals(MyVar.NO_ESPECIFICADO)){
			dialog.setItems(opciones, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0){
						nuevoCliente.selectImage();
					}else if(which==1){
						nuevoCliente.captureImage();
					}
				}
			});
		}else{
			dialog.setItems(opciones2, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0){
						nuevoCliente.selectImage();
					}else if(which==1){
						nuevoCliente.captureImage();
					}else if(which==2){
						nuevoCliente.pathImageCliente = MyVar.NO_ESPECIFICADO;
						nuevoCliente.ivImageCliente.setImageResource(R.drawable.ic_timer_auto_white_48dp);
					}
				}
			});			
							
		}
		
		
		dialog.setNeutralButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		return dialog.create();
	}
}
