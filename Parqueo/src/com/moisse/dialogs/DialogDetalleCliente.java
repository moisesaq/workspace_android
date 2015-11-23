package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.ListaClientes;
import com.moisse.modelo.Cliente;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetalleCliente extends DialogFragment implements OnClickListener{

	private ListaClientes listaClientes;
	private Cliente cliente;
	private View view;
	private LinearLayout lyFechaContrato;
	private Button btnEliminar, btnEditar, btnOk;
	private Activity activity;
	
	OnEditarClienteClickListener listener;
	protected HttpClientCustom httpCustom;
	
	public DialogDetalleCliente(ListaClientes listaClientes, Cliente cliente, Activity activity){
		this.listaClientes = listaClientes;
		this.cliente = cliente;
		this.activity = activity;
	}
	
	public DialogDetalleCliente(Cliente cliente, Activity activity){
		this.cliente = cliente;
		this.activity = activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuffer("Datos cliente"));
		view = getActivity().getLayoutInflater().inflate(R.layout.detalle_cliente, null);
		
		ImageView image = (ImageView)view.findViewById(R.id.ivImageDetalleCliente);
		if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
			if(bitmap!=null){
				image.setImageBitmap(bitmap);
			}else{
				image.setImageResource(R.drawable.ic_client);
			}	
		}else{
			image.setImageResource(R.drawable.ic_client);
		}
		TextView ci = (TextView)view.findViewById(R.id.tvCIDetalleCliente);
		ci.setText(new StringBuilder(cliente.getCi()));
		TextView nombre = (TextView)view.findViewById(R.id.tvNombreDetalleCliente);
		nombre.setText(new StringBuilder(cliente.getNombre()));
		TextView apellido = (TextView)view.findViewById(R.id.tvApellidoDetalleCliente);
		apellido.setText(new StringBuilder(cliente.getApellido()));
		TextView celular = (TextView)view.findViewById(R.id.tvCelularDetalleCliente);
		if(cliente.getCelular()!=0){
			celular.setText(new StringBuilder().append(cliente.getCelular()));
		}else{
			celular.setText(new StringBuilder(MyVar.NO_ESPECIFICADO));
		}
		TextView direccion = (TextView)view.findViewById(R.id.tvDireccionDetalleCliente);
		direccion.setText(new StringBuilder(cliente.getDireccion()));
		TextView email = (TextView)view.findViewById(R.id.tvEmailDetalleCliente);
		email.setText(new StringBuilder(cliente.getEmail()));
		TextView sexo = (TextView)view.findViewById(R.id.tvSexoDetalleCliente);
		sexo.setText(new StringBuilder(cliente.getSexo()));
		TextView fecha_nac = (TextView)view.findViewById(R.id.tvFechaNacDetalleCliente);
		if(!cliente.getFecha_nac().equals(MyVar.FECHA_DEFAULT)){
			fecha_nac.setText(new StringBuilder(MyVar.FORMAT_FECHA_1.format(cliente.getFecha_nac())));
		}else{
			fecha_nac.setText(new StringBuilder(MyVar.NO_ESPECIFICADO));
		}
		TextView tipo_cliente = (TextView)view.findViewById(R.id.tvTipoClienteDetalleCliente);
		TextView fecha_contrato = (TextView)view.findViewById(R.id.tvFechaContratoDetalleCliente);
		lyFechaContrato = (LinearLayout)view.findViewById(R.id.lyFechaContratoDetalleCliente);
		if(cliente.getTipo()!=MyVar.CLIENTE_OCASIONAL){
			lyFechaContrato.setVisibility(View.VISIBLE);
			int tipo = cliente.getTipo();
			if(tipo==MyVar.CLIENTE_CONTRATO_NOCTURNO){
				tipo_cliente.setText(new StringBuilder("Por contrato nocturno"));
			}else if (tipo==MyVar.CLIENTE_CONTRATO_DIURNO) {
				tipo_cliente.setText(new StringBuilder("Por contrato diurno"));
			}else if (tipo==MyVar.CLIENTE_CONTRATO_DIA_COMPLETO) {
				tipo_cliente.setText(new StringBuilder("Por contrato dia completo"));
			}
			fecha_contrato.setText(MyVar.FORMAT_FECHA_1.format(cliente.getFecha_contrato()));
		}
		btnEliminar = (Button)view.findViewById(R.id.btnDarBajaDetalleCliente);
		btnEliminar.setOnClickListener(this);
		btnEditar = (Button)view.findViewById(R.id.btnEditarDetalleCliente);
		btnEditar.setOnClickListener(this);
		btnOk = (Button)view.findViewById(R.id.btnOKDetalleCliente);
		btnOk.setOnClickListener(this);
		
		dialog.setView(view);
		
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnEditar.getId()){
			listener.onEditarClienteClick(cliente.getIdcliente());
			this.dismiss();
		}else if(v.getId()==btnOk.getId()){
			this.dismiss();
		}else if(v.getId()==btnEliminar.getId()){
			mensajeConfirmacion(this);
		}
	}
	
	private void mensajeConfirmacion(final DialogDetalleCliente dialogDetalleCliente) {
		AlertDialog.Builder dialogConfi = new AlertDialog.Builder(getActivity());
		dialogConfi.setTitle("Confimar");
		dialogConfi.setMessage(new StringBuilder("¿Desea eliminar cliente?"));
		dialogConfi.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Cliente cli = new Cliente(cliente.getIdcliente(), MyVar.ELIMINADO);
				EliminarCliente eliminar = new EliminarCliente();
				eliminar.execute(cli);
				dialogDetalleCliente.dismiss();
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
	
	public class EliminarCliente extends AsyncTask<Cliente, Void, Boolean>{
		ProgressDialog pd;
		Cliente cliente_delete;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Eliminando cliente..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Cliente... params) {
			cliente_delete = params[0];
			httpCustom = new HttpClientCustom();
			if(httpCustom.eliminarCliente(this.cliente_delete)){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					if(db.eliminarCliente(this.cliente_delete.getIdcliente())){
						return true;
					}
					db.closeSQLite();	
				} catch (Exception e) {
					e.printStackTrace();
					mensajeError(e.toString(), "eliminar cliente");
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(activity,"Cliente eliminado",Toast.LENGTH_LONG).show();
				if(listaClientes!=null){
					listaClientes.cargarListaClientes();
				}
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo eliminar el cliente, intente mas tarde..!");
				mensaje.show(activity.getFragmentManager(), "tagMsj");
			}
		}
	}
		
	public interface OnEditarClienteClickListener{
		public void onEditarClienteClick(String idcliente);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnEditarClienteClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnEditarClienteClickListener");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(activity);
		mensaje.setTitle("Error "+titulo);
		TextView texto=new TextView(activity);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
}
