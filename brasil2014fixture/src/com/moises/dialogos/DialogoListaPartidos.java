package com.moises.dialogos;

import java.util.ArrayList;
import java.util.List;

import com.example.brasil2014fixture.R;
import com.moises.adapters.AdapterPartidos;
import com.moises.modelo.Partido;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogoListaPartidos extends DialogFragment{
	
	Context miContexto;
	String titulo;
	AdapterPartidos miAdapter;
	String[] miLista;

	public DialogoListaPartidos(Context contexto, String titulo,AdapterPartidos adapter){
		this.miContexto=contexto;
		this.titulo=titulo;
		this.miAdapter=adapter;
		//this.miLista=lista;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder dialogo = new AlertDialog.Builder(miContexto);
		dialogo.setTitle(titulo);
		dialogo.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_info));
		
		final List mItemSeleccionados = new ArrayList();
		dialogo.setIcon(getResources().getDrawable(R.drawable.fuleco));
		dialogo.setAdapter(miAdapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}});
		
		dialogo.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for (int i = 0; i < mItemSeleccionados.size(); i++) {
					dialog.cancel();
				}
				
			}
		});
		return dialogo.create();
	}
	
	
}
