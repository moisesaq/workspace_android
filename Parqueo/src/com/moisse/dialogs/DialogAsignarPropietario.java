package com.moisse.dialogs;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListClienteAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.NuevoVehiculo;
import com.moisse.modelo.Cliente;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class DialogAsignarPropietario extends DialogFragment implements OnItemClickListener{

	ListView lvListaClientes;
	List<Cliente> lista_clientes;
	TextView tvAviso;
	View v;
	NuevoVehiculo nuevo_vehiculo;
	String idparqueo;
	public DialogAsignarPropietario(NuevoVehiculo nuevo_vehiculo, String idparqueo){
		this.nuevo_vehiculo = nuevo_vehiculo;
		this.idparqueo = idparqueo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Seleccione cliente"));
		v = getActivity().getLayoutInflater().inflate(R.layout.lista_clientes, null);
		lvListaClientes = (ListView)v.findViewById(R.id.lvListaClientes);
		lvListaClientes.setOnItemClickListener(this);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaClientes);
		cargarListaClientes();
		dialog.setView(v);
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int wich) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}
	
	private void cargarListaClientes(){
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			List<Cliente> lista_clientes = db.getAllCliente(this.idparqueo, MyVar.NO_ELIMINADO);
			if(lista_clientes.size()!=0){
				ListClienteAdapter adapter = new ListClienteAdapter(getActivity(), lista_clientes);
				lvListaClientes.setAdapter(adapter);
			}else{
				tvAviso.setVisibility(View.VISIBLE);
				tvAviso.setText(new StringBuilder("No encotraron clientes registrado...!"));
				lvListaClientes.setVisibility(View.INVISIBLE);
			}
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Cliente cliente = (Cliente)adapter.getAdapter().getItem(position);
		nuevo_vehiculo.actvCIPropietario.setText(cliente.getCi());
		nuevo_vehiculo.actvCIPropietario.setTag(cliente.getIdcliente());
		nuevo_vehiculo.actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
		nuevo_vehiculo.tvNombreProp.setVisibility(View.VISIBLE);
		if(cliente.getApellido().equals(MyVar.NO_ESPECIFICADO)){
			nuevo_vehiculo.tvNombreProp.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
		}else{
			nuevo_vehiculo.tvNombreProp.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
		}
		
		if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
			if(bitmap!=null){
				nuevo_vehiculo.ivImagePropietario.setImageBitmap(bitmap);
			}else{
				nuevo_vehiculo.ivImagePropietario.setImageResource(R.drawable.ic_client);
			}
		}else{
			nuevo_vehiculo.ivImagePropietario.setImageResource(R.drawable.ic_client);
		}
		this.dismiss();
	}
}
