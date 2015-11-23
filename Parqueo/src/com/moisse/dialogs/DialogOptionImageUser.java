package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.fragments.NuevoUsuario;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogOptionImageUser extends DialogFragment{

	private final String[] options = {"Imagen de galeria","Capturar imagen"};
	private final String[] options2 = {"Imagen de galeria","Capturar imagen", "Quitar imagen"};
	private NuevoUsuario nuevoUsuario;
	
	public DialogOptionImageUser(){}
	
	public DialogOptionImageUser(NuevoUsuario nuevoUsuario){
		this.nuevoUsuario = nuevoUsuario;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Completar accion con..");
		if(nuevoUsuario.pathImageUsuario.equals(MyVar.NO_ESPECIFICADO)){
			dialog.setItems(options, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0){
						nuevoUsuario.selectImage();
					}else if(which==1){
						nuevoUsuario.captureImage();
					}	
				}
			});
		}else{
			dialog.setItems(options2, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0){
						nuevoUsuario.selectImage();
					}else if(which==1){
						nuevoUsuario.captureImage();
					}else if (which==2) {
						nuevoUsuario.pathImageUsuario = MyVar.NO_ESPECIFICADO;
						nuevoUsuario.ivImagenUsuario.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
					}
				}
			});
		}
		
		dialog.setNeutralButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
			}
		});
		
		return dialog.create();
	}
}
