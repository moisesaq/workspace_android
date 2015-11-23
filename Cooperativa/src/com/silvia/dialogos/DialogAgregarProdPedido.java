package com.silvia.dialogos;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.silvia.adapters.GalleryProductosAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.fragmentos.RegistrarPedido;
import com.silvia.modelo.DetallePedido;
import com.silvia.modelo.Producto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogAgregarProdPedido extends DialogFragment implements OnItemClickListener, OnClickListener{

	View v;
	LinearLayout lyVistaCostoEntrega, lyVistaInput;
	EditText etCantidad, etCostoEntrega, etCosto;
	Button btnFinalizar, btnAgregar;
	Gallery gProductos;
	TextView tvAviso;
	View anterior_view;
	
	public RegistrarPedido registrar_pedido;
	
	public DialogAgregarProdPedido(RegistrarPedido registrar_pedido){
		this.registrar_pedido = registrar_pedido;
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
	public Dialog onCreateDialog(Bundle saveInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("Pedido");
		v = getActivity().getLayoutInflater().inflate(R.layout.modelo_agregar_producto_venta, null);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoAgregarProd);
		gProductos = (Gallery)v.findViewById(R.id.gProductosAgregarProd);
		gProductos.setOnItemClickListener(this);
		etCantidad = (EditText)v.findViewById(R.id.etCantidadAgregarProd);
		lyVistaCostoEntrega = (LinearLayout)v.findViewById(R.id.lyCostoEntregaAgregarProd);
		lyVistaInput = (LinearLayout)v.findViewById(R.id.lyInputAgregarProd);
		etCostoEntrega = (EditText)v.findViewById(R.id.etCostoEntregaAgregarProd);
		lyVistaCostoEntrega.setVisibility(View.VISIBLE);
		etCostoEntrega.addTextChangedListener(CalcVentaDomicilioWatcher);
		
		etCosto = (EditText)v.findViewById(R.id.etCostoAgregarProd);
		etCosto.setEnabled(false);
		btnFinalizar = (Button)v.findViewById(R.id.btnFinalizarAgregarProd);
		btnFinalizar.setOnClickListener(this);
		btnAgregar = (Button)v.findViewById(R.id.btnAceptarAgregarProd);
		btnAgregar.setOnClickListener(this);
		
		cargarGaleriaProductos();
		dialog.setView(v);
		return dialog.create();
		
	}

	private void cargarGaleriaProductos() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Producto> lista_prod = db.getTodosLosProductos();
			if(lista_prod.size()!=0 && lista_prod!=null){
				GalleryProductosAdapter adapter = new GalleryProductosAdapter(getActivity(), lista_prod);
				gProductos.setAdapter(adapter);
			}else{
				gProductos.setVisibility(View.GONE);
				btnAgregar.setVisibility(View.GONE);
				lyVistaInput.setVisibility(View.GONE);
				tvAviso.setVisibility(View.VISIBLE);
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final TextWatcher CalcVentaDomicilioWatcher = new TextWatcher() {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int cont, int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}		
		
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>0){
				Producto prod = (Producto)etCantidad.getTag();
				String txtCantidad = etCantidad.getText().toString().trim();
				if(!txtCantidad.equals("")){
					int cantidad = Integer.parseInt(etCantidad.getText().toString().trim());
					double costo_entrega = Double.valueOf(etCostoEntrega.getText().toString().trim());
					double costo = (prod.getPrecio()*cantidad)+costo_entrega;
					etCosto.setText(String.valueOf(costo));
					btnAgregar.setVisibility(View.VISIBLE);
				}else{
					etCantidad.requestFocus();
					etCantidad.setError("Introduzca cantidad de cubos");
					etCostoEntrega.getText().clear();
				}
			}else{
				btnAgregar.setVisibility(View.GONE);
				etCosto.getText().clear();
			}
		}
	};
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Producto prod = (Producto)adapter.getAdapter().getItem(position);
		//View view = (View)adapter.getAdapter().
		if(anterior_view!=null){
			anterior_view.setBackgroundColor(getResources().getColor(R.color.GRIS_BLANCO));
			anterior_view.setPadding(3, 3, 3, 3);
			anterior_view = view;
		}else{
			anterior_view = view;
		}
		
		view.setBackgroundDrawable((getResources().getDrawable(R.drawable.custom_view_button)));
		etCantidad.setTag(prod);
		etCantidad.setEnabled(true);
		etCostoEntrega.setEnabled(true);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnFinalizar.getId()){
			dismiss();
		}else if (v.getId()==btnAgregar.getId()) {
			agregarDetalleVenta();
		}
	}

	private void agregarDetalleVenta() {
		DetallePedido detalleP = null;
		Producto producto = (Producto)etCantidad.getTag();
		String txtCantidad = etCantidad.getText().toString().trim();
		if(!txtCantidad.equals("")){
			String txtCostoEntrega = etCostoEntrega.getText().toString().trim();
			if(!txtCostoEntrega.equals("")){
				int cantidad = Integer.parseInt(txtCantidad);
				double costo = Double.valueOf(etCosto.getText().toString().trim());
				double costo_entrega = Double.valueOf(txtCostoEntrega);
				
				detalleP = new DetallePedido(generarIdDetallePedido(), registrar_pedido.nuevo_pedido.getIdpedido(), producto.getIdprod(), 
												cantidad, costo_entrega, costo);
				double costo_total_anterior = registrar_pedido.nuevo_pedido.getCosto_total();
				registrar_pedido.nuevo_pedido.setCosto_total(costo_total_anterior+costo);
			}else{
				Toast.makeText(getActivity(), "Introduzca costo de entrega", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(getActivity(), "Introduzca cantidad de cubos", Toast.LENGTH_SHORT).show();
		}
		
		if(detalleP!=null){
			registrar_pedido.my_adapter.add(detalleP);
			registrar_pedido.my_adapter.notifyDataSetChanged();
			if(registrar_pedido.lvListaProd.getVisibility()==View.INVISIBLE){
				registrar_pedido.lvListaProd.setVisibility(View.VISIBLE);
			}
			if(registrar_pedido.tvAviso.getVisibility()==View.VISIBLE){
				registrar_pedido.tvAviso.setVisibility(View.INVISIBLE);
			}
			if(registrar_pedido.lyVistaCostoTotal.getVisibility()==View.GONE){
				registrar_pedido.lyVistaCostoTotal.setVisibility(View.VISIBLE);
			}
			registrar_pedido.tvCostoTotal.setText(new StringBuilder().append(registrar_pedido.nuevo_pedido.getCosto_total()).append(" Bs."));
			registrar_pedido.tvCostoTotal.setTag(registrar_pedido.nuevo_pedido.getCosto_total());
			Toast.makeText(getActivity(), "Producto agregado a la lista de pedidos", Toast.LENGTH_SHORT).show();
			limpiarCampos();
			etCantidad.requestFocus();
		}		
	}
	
	public String generarIdDetallePedido(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String detalleCode = "detalle_p-"+date;
		return detalleCode;
	}
	
	public void limpiarCampos(){
		anterior_view.setBackgroundColor(getResources().getColor(R.color.GRIS_BLANCO));
		anterior_view = null;
		etCantidad.setTag(null);
		etCantidad.getText().clear();
		etCostoEntrega.getText().clear();
		etCantidad.setEnabled(false);
		etCostoEntrega.setEnabled(false);
		etCosto.getText().clear();
		btnAgregar.setVisibility(View.GONE);
	}
	
}
