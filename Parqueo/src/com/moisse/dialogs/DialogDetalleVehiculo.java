package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.ListaVehiculos;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetalleVehiculo extends DialogFragment implements OnClickListener{
	
	private ListaVehiculos listVehiculos;
	private Vehiculo vehiculo;
	private Cliente cliente;
	private View v;
	private Button btnEliminar, btnEditar, btnOk;
	private LinearLayout lyPropietario;
	
	OnEditarVehiculoClickListener listener;
	protected HttpClientCustom httpCustom;
	protected Activity activity;
	ImageView ivLookCliente;
	
	public DialogDetalleVehiculo(ListaVehiculos listVehiculos, Vehiculo vehiculo, Activity activity){
		this.listVehiculos = listVehiculos;
		this.vehiculo = vehiculo;
		this.activity = activity;
	}
	
	public DialogDetalleVehiculo(Vehiculo vehiculo, Activity activity){
		this.vehiculo = vehiculo;
		this.activity = activity;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Datos vehiculo"));
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_vehiculo, null);
		
		ImageView imageVehiculo = (ImageView)v.findViewById(R.id.ivImageVehiculoDetalleVehiculo);
		if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehiculo.getImagen()).toString());
			if(bitmap!=null){
				imageVehiculo.setImageBitmap(bitmap);
			}else{
				imageVehiculo.setImageResource(R.drawable.ic_car);
			}
		}else{
			imageVehiculo.setImageResource(R.drawable.ic_car);
		}
		TextView placa = (TextView)v.findViewById(R.id.tvPlacaDetalleVehiculo);
		placa.setText(new StringBuilder(vehiculo.getPlaca()));
		TextView marca = (TextView)v.findViewById(R.id.tvMarcaDetalleVehiculo);
		marca.setText(new StringBuilder(vehiculo.getMarca()));
		TextView color = (TextView)v.findViewById(R.id.tvColorDetalleVehiculo);
		color.setText(new StringBuilder(vehiculo.getColor()));
		lyPropietario = (LinearLayout)v.findViewById(R.id.lyPropietarioDetalleVehiculo);
		this.cliente = getCliente(vehiculo.getIdcliente());
		String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(this.cliente.getIdparqueo()).toString();
		if(!vehiculo.getIdcliente().equals(id_client_default)){
			ivLookCliente = (ImageView)v.findViewById(R.id.ivVerClienteDetalleVehiculo);
			ivLookCliente.setOnClickListener(this);
			ImageView imageCliente = (ImageView)v.findViewById(R.id.ivImageClienteDetalleVehiculo);
			if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
				Bitmap myBitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
				if(myBitmap!=null){
					imageCliente.setImageBitmap(myBitmap);
				}
			}else{
				imageCliente.setImageResource(R.drawable.ic_client);
			}
			TextView ci = (TextView)v.findViewById(R.id.tvCIDetalleVehiculo);
			ci.setText(new StringBuilder("CI: ").append(cliente.getCi()));
			TextView nombre = (TextView)v.findViewById(R.id.tvNombreDetalleVehiculo);
			nombre.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
			TextView apellido = (TextView)v.findViewById(R.id.tvApellidoDetalleVehiculo);
			apellido.setText(new StringBuilder("Apellido: ").append(cliente.getApellido()));
			TextView tipo_cliente = (TextView)v.findViewById(R.id.tvTipoClienteDetalleVehiculo);
			int tipo = cliente.getTipo();
			if(tipo==MyVar.CLIENTE_OCASIONAL){
				tipo_cliente.setText(new StringBuilder("Cliente ocasional"));
			}else if(tipo==MyVar.CLIENTE_CONTRATO_NOCTURNO){
				tipo_cliente.setText(new StringBuilder("Cliente con contrato nocturno"));
			}else if (tipo==MyVar.CLIENTE_CONTRATO_DIURNO) {
				tipo_cliente.setText(new StringBuilder("Cliente con contrato diurno"));
			}else if (tipo==MyVar.CLIENTE_CONTRATO_DIA_COMPLETO) {
				tipo_cliente.setText(new StringBuilder("Cliente con contrato dia completo"));
			}			
		}else{
			lyPropietario.setVisibility(View.GONE);
			TextView tvNota = (TextView)v.findViewById(R.id.tvNotaDetalleVehiculo);
			tvNota.setText(new StringBuilder("Propietario no especificado"));
			LinearLayout.LayoutParams params = (LayoutParams)tvNota.getLayoutParams();
			params.gravity = Gravity.CENTER_HORIZONTAL;
			tvNota.setLayoutParams(params);
		}
	
		btnEliminar = (Button)v.findViewById(R.id.btnDarBajaDetalleVehiculo);
		btnEliminar.setOnClickListener(this);
		btnEditar = (Button)v.findViewById(R.id.btnEditarDetalleVehiculo);
		btnEditar.setOnClickListener(this);
		btnOk = (Button)v.findViewById(R.id.btnOKDetalleVehiculo);
		btnOk.setOnClickListener(this);
		
		dialog.setView(v);
		
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnEditar.getId()){
			listener.onEditarVehiculoClick(vehiculo.getIdvehiculo());
			this.dismiss();
		}else if(v.getId()==btnOk.getId()){
			this.dismiss();
		}else if(v.getId()==btnEliminar.getId()){
			mensajeConfirmacion(this);
		}else if (cliente!=null) {
			if(v.getId()==ivLookCliente.getId()){
				this.dismiss();
				DialogDetalleCliente dDCliente = new DialogDetalleCliente(cliente, activity);
				dDCliente.show(getFragmentManager(), "tagDCliente");
			}
		}
	}
	
	private void mensajeConfirmacion(final DialogDetalleVehiculo dialogDetalleVehiculo) {		
		AlertDialog.Builder dialogConfi = new AlertDialog.Builder(getActivity());
		dialogConfi.setTitle(new StringBuilder("Confirmar"));
		dialogConfi.setMessage(new StringBuilder("¿Esta seguro de eliminar el vehículo?"));
		dialogConfi.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Vehiculo vehi = new Vehiculo(vehiculo.getIdvehiculo(), MyVar.ELIMINADO);
				new EliminarVehiculo().execute(vehi);
				dialogDetalleVehiculo.dismiss();
			}
		});
		
		dialogConfi.setNegativeButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialogConfi.create().show();
	}

	public class EliminarVehiculo extends AsyncTask<Vehiculo, Void, Boolean>{
		ProgressDialog pd;
		Vehiculo vehiculo_delete;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Eliminando cliente...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Vehiculo... params) {
			vehiculo_delete = params[0];
			httpCustom = new HttpClientCustom();
			if(httpCustom.eliminarVehiculo(this.vehiculo_delete)){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					if(db.eliminarVehiculo(this.vehiculo_delete.getIdvehiculo())){
						return true;
					}
					db.closeSQLite();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(activity,"Vehiculo eliminado",Toast.LENGTH_LONG).show();
				if(listVehiculos!=null){
					listVehiculos.cargarListaVehiculos();
				}
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo eliminar vehiculo, intente mas tarde..!");
				mensaje.show(activity.getFragmentManager(), "tagMsj");
			}
		}	
	}
	
	public interface OnEditarVehiculoClickListener{
		public void onEditarVehiculoClick(String idvehiculo);
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener =(OnEditarVehiculoClickListener)activity;
		} catch (ClassCastException e) {
			mensajeError(e.toString(), "debe implementar OnEditarVehiculoClickListener");
		}
	}
	
	private Cliente getCliente(String idcliente){
		DBParqueo db = new DBParqueo(getActivity());
		Cliente cliente = null;
		try{
			db.openSQLite();
			cliente = db.getCliente(idcliente);
			db.closeSQLite();
		}catch(Exception e){
			mensajeError(e.toString(), "obteniendo cliente");
		}
		return cliente;
	}

	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error: " + titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}

}
