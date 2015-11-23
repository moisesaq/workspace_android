package com.moisse.dialogs;

import com.moisse.fragments.ListaClientes;
import com.moisse.fragments.ListaUsuarios;
import com.moisse.fragments.ListaVehiculos;
import com.moisse.parqueo.MenuPrincipal;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogListas extends DialogFragment{

	private int lista_posicion;
	private MenuPrincipal menup;
	private String idparqueo;
	private String idusuario;
	
	public DialogListas(MenuPrincipal menup, String idparqueo, String idusuario){
		this.menup = menup;
		this.idparqueo = idparqueo;
		this.idusuario = idusuario;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String[] listas = {"Lista de Clientes","Lista de Vehiculos", "Lista de Usuarios"};
		AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
		
		dialogo.setTitle("Seleccione lista");
		dialogo.setSingleChoiceItems(listas, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				lista_posicion = position;
			}
		});
		
		dialogo.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
			}
		});
		
		dialogo.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int position) {
				switch (lista_posicion) {
				case 0:
					Bundle msj1 = new Bundle();
					msj1.putString("idparqueo", idparqueo);
					ListaClientes listC = new ListaClientes();
					listC.setArguments(msj1);
					menup.establecerVistaFragment(listC, "tagLC");
					break;	
				case 1:
					Bundle msj2 = new Bundle();
					msj2.putString("idparqueo", idparqueo);
					ListaVehiculos listV = new ListaVehiculos();
					listV.setArguments(msj2);
					menup.establecerVistaFragment(listV, "tagLV");
					break;
				case 2:
					Bundle msj3 = new Bundle();
					msj3.putString("idparqueo", idparqueo);
					msj3.putString("idusuario", idusuario);
					ListaUsuarios listU = new ListaUsuarios();
					listU.setArguments(msj3);
					menup.establecerVistaFragment(listU, "tagLU");
					break;
				}
			}
		});
		return dialogo.create();
	}

	
}
