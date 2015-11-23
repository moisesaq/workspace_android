package com.silvia.adapters;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.RegistrarVenta;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Producto;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ListaDetalleVentaAdapter extends ArrayAdapter<DetalleVenta> implements OnMenuItemClickListener{

	private Activity activity;
	private ImageLoader imageLoader;
	private ImageButton ibtnMas;
	private RegistrarVenta reg_venta;
	private int proviene;
	private DetalleVenta detalle_seleccionado;
	//private LayoutInflater inflater;
	//private View dialogView;
	public AlertDialog.Builder dialogEditar;
	public TextView tvNombreProd;
	public EditText etCantidad, etCostoEntrega, etCosto;
	
	public ListaDetalleVentaAdapter(Activity activity, List<DetalleVenta> lista_detalleV, int proviene){
		super(activity, R.layout.modelo_item_detalle_venta, lista_detalleV);
		this.activity = activity;
		this.proviene = proviene;
	}
	
	public ListaDetalleVentaAdapter(Activity activity, List<DetalleVenta> lista_detalleV, RegistrarVenta reg_venta, int proviene){
		super(activity, R.layout.modelo_item_detalle_venta, lista_detalleV);
		this.activity = activity;
		this.reg_venta = reg_venta;
		this.proviene = proviene;
	}
	
	public class ViewHolder{
		ImageView ivImageProd;
		TextView tvNombreProd;
		TextView tvPrecio;
		TextView tvCant;
		TextView tvCostoEntrega;
		TextView tvCosto;
	}
	
	@Override
	public DetalleVenta getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_detalle_venta, null);
			holder = new ViewHolder();
			holder.ivImageProd = (ImageView)view.findViewById(R.id.ivImageProdLDetalleVenta);
			holder.tvNombreProd = (TextView)view.findViewById(R.id.tvNombreProdLDetalleVenta);
			holder.tvPrecio = (TextView)view.findViewById(R.id.tvPrecioProdLDetalleVenta);
			holder.tvCant = (TextView)view.findViewById(R.id.tvCantLDetalleVenta);
			holder.tvCostoEntrega =(TextView)view.findViewById(R.id.tvCostoEntregaLDetalleVenta);
			holder.tvCosto = (TextView)view.findViewById(R.id.tvCostoLDetalleVenta);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		final DetalleVenta detalleV = this.getItem(position);
		Producto prod = getProducto(detalleV.getIdproducto());
		if(!prod.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(prod.getImagen()).toString();
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file://"+pathImagen, holder.ivImageProd, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageProd.setImageResource(R.drawable.ic_product_plant_128);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageProd.setImageBitmap(loadedImage);
					holder.ivImageProd.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImageProd.setPadding(0, 0, 0, 0);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageProd.setImageResource(R.drawable.ic_product_plant_128);
				}
			});
		}else{
			holder.ivImageProd.setImageResource(R.drawable.ic_product_plant_128);
		}
		holder.tvNombreProd.setText(prod.getNombre_prod());
		holder.tvPrecio.setText(prod.getPrecio()+" Bs.");
		holder.tvCant.setText(String.valueOf(detalleV.getCantidad()));
		holder.tvCostoEntrega.setText(detalleV.getCosto_entrega()+" Bs.");
		holder.tvCosto.setText(new StringBuilder().append(detalleV.getCosto()).append(" Bs."));
		
		if(proviene==Variables.PROV_VENTA){
			ibtnMas = (ImageButton)view.findViewById(R.id.ibtnMasDetalleVenta);
			ibtnMas.setVisibility(View.VISIBLE);
			ibtnMas.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					abrirPopupMenu(detalleV);
				}
			});
		}		
		return view;
	}
	
	public void abrirPopupMenu(DetalleVenta detalle){
		detalle_seleccionado = detalle;
		PopupMenu popupMenu = new PopupMenu(activity, ibtnMas);
		popupMenu.setOnMenuItemClickListener(this);
		popupMenu.inflate(R.menu.popup_menu_detalle_venta);
		popupMenu.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_editar_detalle_venta:
			//detalle_seleccionado.setCantidad(5);
			//this.notifyDataSetChanged();
			abrirEditarDetalleVenta();
			return true;

		case R.id.action_eliminar_detalle_venta:
			//Toast.makeText(activity, "Eliminar "+detalle_seleccionado.getIdproducto(), Toast.LENGTH_SHORT).show();
			double costo_reducido_actual = reg_venta.nueva_venta.getCosto_total()-detalle_seleccionado.getCosto();
			reg_venta.nueva_venta.setCosto_total(costo_reducido_actual);
			reg_venta.tvCostoTotal.setText(new StringBuilder().append(costo_reducido_actual).append(" Bs."));
			this.remove(detalle_seleccionado);
			this.notifyDataSetChanged();
			return true;
		}
		return false;
	}
	
	private final TextWatcher CalcVentaDirectaWatcher = new TextWatcher() {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int cont, int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}		
		
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>0){
				int cantidad = Integer.parseInt(etCantidad.getText().toString().trim());
				Producto prod = (Producto)tvNombreProd.getTag();
				etCosto.setText(String.valueOf(prod.getPrecio()*cantidad));
			}else{
				etCosto.getText().clear();
			}
		}
	};
	
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
				Producto prod = (Producto)tvNombreProd.getTag();
				String txtCantidad = etCantidad.getText().toString().trim();
				if(!txtCantidad.equals("")){
					int cantidad = Integer.parseInt(etCantidad.getText().toString().trim());
					double costo_entrega = Double.valueOf(etCostoEntrega.getText().toString().trim());
					double costo = (prod.getPrecio()*cantidad)+costo_entrega;
					etCosto.setText(String.valueOf(costo));
				}else{
					etCantidad.requestFocus();
					etCantidad.setError("Introduzca cantidad de cubos");
					etCostoEntrega.getText().clear();
				}
			}else{
				etCosto.getText().clear();
			}
		}
	};
	
	public void abrirEditarDetalleVenta(){
		//inflater = (LayoutInflater)activity.getBaseContext().getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		dialogEditar = new AlertDialog.Builder(activity, R.style.Theme_CAT_Alert_Dialog);
		View dialogView = activity.getLayoutInflater().inflate(R.layout.modelo_editar_detalle_venta, null);
		//popupWindowEditar = new PopupWindow(dialogView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		Producto prod = getProducto(detalle_seleccionado.getIdproducto());
		ImageView imageProd = (ImageView)dialogView.findViewById(R.id.ivImagenProdEditarDetalleVenta);
		
		if(!prod.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(prod.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
			if(bitmap!=null){
				imageProd.setImageBitmap(bitmap);
			}else{
				imageProd.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
			}
		}else{
			imageProd.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
		}
		
		tvNombreProd = (TextView)dialogView.findViewById(R.id.tvNombreProdEditarDetalleVenta);
		tvNombreProd.setText(new StringBuilder(prod.getNombre_prod()));
		tvNombreProd.setTag(prod);
		TextView tvPrecioProd = (TextView)dialogView.findViewById(R.id.tvPrecioProdEditarDetalleVenta);
		tvPrecioProd.setText(new StringBuilder().append(prod.getPrecio()).append("Bs."));
		etCantidad = (EditText)dialogView.findViewById(R.id.etCantidadEditarDetalleVenta);
		etCantidad.setText(String.valueOf(detalle_seleccionado.getCantidad()));
		LinearLayout lyCostoEntrega = (LinearLayout)dialogView.findViewById(R.id.lyCostoEntregaEditarDetalleVenta);
		etCostoEntrega = (EditText)dialogView.findViewById(R.id.etCostoEntregaEditarDetalleVenta);
		if(reg_venta.nueva_venta.getTipo_venta()==Variables.VENTA_DIRECTA){
			etCantidad.addTextChangedListener(CalcVentaDirectaWatcher);
		}else if(reg_venta.nueva_venta.getTipo_venta()==Variables.VENTA_A_DOMICILIO){
			lyCostoEntrega.setVisibility(View.VISIBLE);
			etCostoEntrega.setText(String.valueOf(detalle_seleccionado.getCosto_entrega()));
			etCostoEntrega.addTextChangedListener(CalcVentaDomicilioWatcher);
		}
		etCosto = (EditText)dialogView.findViewById(R.id.etCostoEditarDetalleVenta);
		etCosto.setText(String.valueOf(detalle_seleccionado.getCosto()));
		dialogEditar.setNegativeButton(R.string.cancelar,new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialogEditar.setPositiveButton(R.string.aceptar, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String txtCantidad = etCantidad.getText().toString().trim();
				String txtCosto =etCosto.getText().toString().trim();
				if(reg_venta.nueva_venta.getTipo_venta()==Variables.VENTA_DIRECTA){
					if(!txtCosto.equals("") && !txtCantidad.equals("")){
						double costo_anterior_venta = reg_venta.nueva_venta.getCosto_total() - detalle_seleccionado.getCosto();
						int cant = Integer.valueOf(txtCantidad);
						double costo = Double.parseDouble(txtCosto);
						detalle_seleccionado.setCantidad(cant);
						detalle_seleccionado.setCosto(costo);
						reg_venta.nueva_venta.setCosto_total(costo_anterior_venta+costo);
						reg_venta.tvCostoTotal.setText(new StringBuilder().append(reg_venta.nueva_venta.getCosto_total()).append(" Bs."));
						reg_venta.tvCostoTotal.setTag(reg_venta.nueva_venta.getCosto_total());
						ListaDetalleVentaAdapter.super.notifyDataSetChanged();
						Toast.makeText(activity, "Editado", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(activity, "Sin editar por falta de datos", Toast.LENGTH_SHORT).show();
					}
				}else if(reg_venta.nueva_venta.getTipo_venta()==Variables.VENTA_A_DOMICILIO){
					String txtCostoEntrega = etCostoEntrega.getText().toString().trim();
					if(!txtCosto.equals("") && !txtCantidad.equals("") && !txtCostoEntrega.equals("")){
						double costo_anterior_venta = reg_venta.nueva_venta.getCosto_total() - detalle_seleccionado.getCosto();
						int cant = Integer.valueOf(txtCantidad);
						double costo_entrega = Double.parseDouble(txtCostoEntrega);
						double costo = Double.parseDouble(txtCosto);
						detalle_seleccionado.setCantidad(cant);
						detalle_seleccionado.setCosto_entrega(costo_entrega);
						detalle_seleccionado.setCosto(costo);
						reg_venta.nueva_venta.setCosto_total(costo_anterior_venta+costo);
						reg_venta.tvCostoTotal.setText(new StringBuilder().append(reg_venta.nueva_venta.getCosto_total()).append(" Bs."));
						reg_venta.tvCostoTotal.setTag(reg_venta.nueva_venta.getCosto_total());
						ListaDetalleVentaAdapter.super.notifyDataSetChanged();
						Toast.makeText(activity, "Editado", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(activity, "Sin editar por falta de datos", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		dialogEditar.setView(dialogView);
		//dialogEditar.create().show();
		
		AlertDialog d = dialogEditar.show();
		int titleDividerId = activity.getResources().getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(activity.getResources().getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
 	public Producto getProducto(String idprod){
		Producto prod = null;
		DBDuraznillo db = new DBDuraznillo(getContext());
		try {
			db.abrirDB();
			prod = db.getProducto(idprod);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prod;
	}

}
