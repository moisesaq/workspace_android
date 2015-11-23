package com.silvia.fragmentos;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cliente;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleCliente extends Fragment{

	private String idcliente;
	private Cliente cliente;;
	
	private ImageView ivImagenCliente;
	private TextView tvCI, tvNombre, tvApellido, tvDireccion, tvTelf, tvEmail, tvSexo, tvFechaNac, tvFechaReg;
	
	OnDetalleClienteClickListener listener;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.detalle_cliente, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(new StringBuilder("Datos del cliente"));
		this.idcliente = getArguments().getString("idcliente");
		cargarcliente();
		ivImagenCliente = (ImageView)v.findViewById(R.id.ivImagenDetalleCliente);
		tvCI = (TextView)v.findViewById(R.id.tvCIDetalleCliente);
		tvNombre = (TextView)v.findViewById(R.id.tvNombreDetalleCliente);
		tvApellido = (TextView)v.findViewById(R.id.tvApellidoDetalleCliente);
		tvDireccion = (TextView)v.findViewById(R.id.tvDireccionDetalleCliente);
		tvTelf = (TextView)v.findViewById(R.id.tvTelfDetalleCliente);
		tvEmail = (TextView)v.findViewById(R.id.tvEmailDetalleCliente);
		tvSexo = (TextView)v.findViewById(R.id.tvSexoDetalleCliente);
		tvFechaNac = (TextView)v.findViewById(R.id.tvFechaNacDetallaCliente);
		tvFechaReg = (TextView)v.findViewById(R.id.tvFechaRegDetalleCliente);
		
		cargarMostrarDatosPersonal();
	}

	private void cargarMostrarDatosPersonal() {
		if(!cliente.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(cliente.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
			if(bitmap!=null){
				ivImagenCliente.setImageBitmap(bitmap);
				ivImagenCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagenCliente.setPadding(0, 0, 0, 0);
			}else{
				ivImagenCliente.setImageResource(R.drawable.ic_client_128);
			}
		}else{
			ivImagenCliente.setImageResource(R.drawable.ic_client_128);
		}
		tvCI.setText(cliente.getCi());
		tvNombre.setText(cliente.getNombre());
		tvApellido.setText(cliente.getApellido());
		tvDireccion.setText(cliente.getDireccion());
		if(cliente.getTelf()==0){
			tvTelf.setText(Variables.SIN_ESPECIFICAR);
		}else{
			tvTelf.setText(String.valueOf(cliente.getTelf()));
		}
		tvEmail.setText(cliente.getEmail());
		tvSexo.setText(cliente.getSexo());
		if(cliente.getFechaNac().equals(Variables.FECHA_DEFAULT)){
			tvFechaNac.setText(Variables.SIN_ESPECIFICAR);
		}else{
			tvFechaNac.setText(Variables.FORMAT_FECHA_1.format(cliente.getFechaNac()));
		}
		tvFechaReg.setText(Variables.FORMAT_FECHA_1.format(cliente.getFechaReg()));

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_editar_cliente).setVisible(true);
		menu.findItem(R.id.action_eliminar_cliente).setVisible(true);
		menu.findItem(R.id.action_listo_cliente).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_editar_cliente){
			listener.onEditarClienteClick(this.cliente.getIdcliente());
		}else if(item.getItemId()==R.id.action_eliminar_cliente){
			confirmarEliminarCliente();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void confirmarEliminarCliente() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Eliminar?");
		dialog.setMessage("Recuerde que una vez elimine cliente no podra restaurarlo");
		dialog.setPositiveButton(R.string.aceptar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.eliminarCliente(cliente)){
						Toast.makeText(getActivity(), "Cliente eliminado", Toast.LENGTH_SHORT).show();
						listener.onBackFromDetalleClienteClick();
						dialog.dismiss();
						
					}else{
						Toast.makeText(getActivity(), "No se pudo eliminar cliente, intente mas tarde..!", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		dialog.setNeutralButton(R.string.cancelar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog d = dialog.show();
		
		int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(getResources().getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public interface OnDetalleClienteClickListener{
		public void onEditarClienteClick(String idcliente);
		public void onBackFromDetalleClienteClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnDetalleClienteClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnDetalleClienteClickListener");
		}
	}
	
	private void cargarcliente() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			cliente = db.getCliente(this.idcliente);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
