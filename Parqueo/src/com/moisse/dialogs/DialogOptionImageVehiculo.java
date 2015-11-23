package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.fragments.NuevoVehiculo;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogOptionImageVehiculo extends DialogFragment{

	private final String[] options = {"Imagen de galeria","Capturar imagen"};
	private final String[] options2 = {"Imagen de galeria","Capturar imagen", "Quitar imagen"};
	
	NuevoVehiculo nuevoVehiculo;
	
	public DialogOptionImageVehiculo(){
		
	}
	
	public DialogOptionImageVehiculo(NuevoVehiculo nuevoVehiculo){
		this.nuevoVehiculo = nuevoVehiculo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Completar accion con..");
		if(nuevoVehiculo.pathImageVehiculo.equals(MyVar.NO_ESPECIFICADO)){
			dialog.setItems(options, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0){
						nuevoVehiculo.SelectImage();
					}else if (which==1) {
						nuevoVehiculo.CaptureImage();
					}				
				}
			});
		}else{
			dialog.setItems(options2, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0){
						nuevoVehiculo.SelectImage();
					}else if (which==1) {
						nuevoVehiculo.CaptureImage();
					}else if (which==2) {
						nuevoVehiculo.pathImageVehiculo = MyVar.NO_ESPECIFICADO;
						nuevoVehiculo.ivImageVehiculo.setImageResource(R.drawable.ic_car);
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
