package com.silvia.dialogos;

import java.util.List;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.InformeVentas;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.Venta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogVentaPorCliente extends DialogFragment implements OnClickListener{

	InformeVentas informeVentas;
	View v;
	ImageView ivImageCliente;
	AutoCompleteTextView actvCICliente;
	TextView tvNombreCliente;
	Button btnCancelar, btnAceptar;
	Cliente cliente = null;
		
	public DialogVentaPorCliente(InformeVentas informeVentas){
		this.informeVentas = informeVentas;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Ventas por cliente"));
		v = getActivity().getLayoutInflater().inflate(R.layout.modelo_venta_por_cliente, null);
		ivImageCliente = (ImageView)v.findViewById(R.id.ivImagenClienteVentaPorCliente);
		actvCICliente = (AutoCompleteTextView)v.findViewById(R.id.actvCIClienteVentaPorCliente);
		adaptarAutoCompleteTextViewCICliente();
		actvCICliente.addTextChangedListener(CIWacher);
		tvNombreCliente = (TextView)v.findViewById(R.id.tvNombreClienteVentaPorCliente);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarVentaPorCliente);
		btnCancelar.setOnClickListener(this);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarVentaPorCliente);
		btnAceptar.setOnClickListener(this);
		dialog.setView(v);
		return dialog.create();
	}
	
	private void adaptarAutoCompleteTextViewCICliente() {
		String[] lista_ci_clientes = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Cliente> lista = db.getTodosLosClientes();
			if(lista.size()!=0 && lista!=null){
				lista_ci_clientes = new String[lista.size()];
				for (int i = 0; i < lista.size(); i++) {
					lista_ci_clientes[i] = lista.get(i).getCi();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista_ci_clientes);
				actvCICliente.setThreshold(2);
				actvCICliente.setAdapter(adapter);
			}
			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnCancelar.getId()){
			dismiss();
		}else if(v.getId()==btnAceptar.getId()){
			if(cliente.getApellido().equals(Variables.SIN_ESPECIFICAR)){
				informeVentas.tvTitulo.setText(new StringBuilder("Ventas al cliente: ").append(cliente.getNombre())
						.append(" CI: ").append(cliente.getCi()));
			}else{
				informeVentas.tvTitulo.setText(new StringBuilder("Ventas al cliente: ").append(cliente.getNombre())
						.append(" ").append(cliente.getApellido()).append(" CI: ").append(cliente.getCi()));
			}
			
			informeVentas.lista_ventas = getListaVentasPorCliente(cliente.getIdcliente());
			informeVentas.cargarMostrarInformeVentas(informeVentas.lista_ventas);
			dismiss();
		}
	}
	
	private final TextWatcher CIWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
	
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()==7 || s.length()==8 || s.length()==10){
				cliente = getClientePorCI(s.toString());
				if(cliente!=null){
					ivImageCliente.setVisibility(View.VISIBLE);
					if(!cliente.getImagen().equals(Variables.SIN_ESPECIFICAR)){
						Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(cliente.getImagen()).toString());
						if(bitmap!=null){
							ivImageCliente.setImageBitmap(bitmap);
						}else{
							ivImageCliente.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
						}
					}else{
						ivImageCliente.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
					}
					tvNombreCliente.setVisibility(View.VISIBLE);
					if(cliente.getApellido().equals(Variables.SIN_ESPECIFICAR)){
						tvNombreCliente.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
					}else{
						tvNombreCliente.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
					}
					btnAceptar.setEnabled(true);
				}else{
					btnAceptar.setEnabled(false);
					actvCICliente.requestFocus();
					actvCICliente.setError("Este CI no existe");
				}
			}else{
				btnAceptar.setEnabled(false);
				ivImageCliente.setVisibility(View.GONE);
				tvNombreCliente.setVisibility(View.GONE);
			}
		}
	};
	
	public Cliente getClientePorCI(String CI){
		Cliente cliente = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			cliente = db.getClientePorCI(CI);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public List<Venta> getListaVentasPorCliente(String idcliente){
		List<Venta> lista = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista = db.getVentasPorCliente(idcliente);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

}
