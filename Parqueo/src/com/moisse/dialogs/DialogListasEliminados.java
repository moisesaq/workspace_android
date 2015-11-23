package com.moisse.dialogs;

import com.moisse.fragments.ListaClientesEliminados;
import com.moisse.fragments.ListaVehiculosEliminados;
import com.moisse.parqueo.MenuPrincipal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogListasEliminados extends DialogFragment{
	
	private MenuPrincipal menu_principal;
	private String idparqueo;

	public DialogListasEliminados(MenuPrincipal menu_principal, String idparqueo){
		this.menu_principal = menu_principal;
		this.idparqueo = idparqueo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		String[] eliminados = {"Clientes eliminados","Vehiculo eliminados"};
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Seleccione.."));
		dialog.setItems(eliminados, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Bundle cajon = new Bundle();
				cajon.putString("idparqueo", idparqueo);
				if(which==0){
					ListaClientesEliminados clientes_eliminados = new ListaClientesEliminados();
					clientes_eliminados.setArguments(cajon);
					menu_principal.establecerVistaFragment(clientes_eliminados, "tagLCE");
				}else if(which==1){
					ListaVehiculosEliminados vehiculos_eliminados = new ListaVehiculosEliminados();
					vehiculos_eliminados.setArguments(cajon);
					menu_principal.establecerVistaFragment(vehiculos_eliminados, "tagLVE");
				}
			}
		});
		dialog.setNeutralButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		return dialog.create();
	}
}
