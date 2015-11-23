package com.silvia.dialogos;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.ListaProductos;
import com.silvia.modelo.Producto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetalleProd extends DialogFragment implements OnClickListener{

	public ListaProductos lista_prod;
	public Producto prod;
	
	private ImageView ivImagenProd; 
	private Button btnEliminar, btnEditar, btnOk;
	private TextView tvNombreProd, tvPrecio, tvUnidad, tvDescripcion;
	private View v;
	
	OnEditarProdClickListener listener;
	
	public DialogDetalleProd(ListaProductos lista_prod, Producto prod){
		this.lista_prod = lista_prod;
		this.prod = prod;
	}
		
	@Override
	public void onStart() {
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//		lp.copyFrom(d.getWindow().getAttributes());
//		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		d.getWindow().setAttributes(lp);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		Rect displayRectangle = new Rect();
		Window window = getActivity().getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("Datos producto");
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_producto, null);
		v.setMinimumWidth((int)(displayRectangle.width()*1.0f));
		v.setMinimumHeight(LayoutParams.WRAP_CONTENT);
		//v.setMinimumHeight((int)(displayRectangle.height()*0.9f));
		
		ivImagenProd = (ImageView)v.findViewById(R.id.ivImagenDetalleProd);
		tvNombreProd = (TextView)v.findViewById(R.id.tvNombreProdDetalleProd);
		tvPrecio = (TextView)v.findViewById(R.id.tvPrecioDetalleProd);
		tvUnidad = (TextView)v.findViewById(R.id.tvUnidadDetalleProd);
		tvDescripcion = (TextView)v.findViewById(R.id.tvDescripcionDetalleProd);
		btnEliminar = (Button)v.findViewById(R.id.btnEliminarDetalleProd);
		btnEliminar.setOnClickListener(this);
		btnEditar = (Button)v.findViewById(R.id.btnEditarDetalleProd);
		btnEditar.setOnClickListener(this);
		btnOk = (Button)v.findViewById(R.id.btnOkDetalleProd);
		btnOk.setOnClickListener(this);
		mostrarDatosProducto();
		
		dialog.setView(v);
		return dialog.create();
	}
	
	public void mostrarDatosProducto(){
		if(!prod.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(prod.getImagen()).toString();
			Bitmap bitmap_imagen = BitmapFactory.decodeFile(pathImagen);
			if(bitmap_imagen!=null){
				ivImagenProd.setImageBitmap(bitmap_imagen);
				ivImagenProd.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagenProd.setPadding(0, 0, 0, 0);
			}else{
				ivImagenProd.setImageResource(R.drawable.ic_product_plant_128);
			}
		}else{
			ivImagenProd.setImageResource(R.drawable.ic_product_plant_128);
		}
		tvNombreProd.setText(prod.getNombre_prod());
		tvPrecio.setText(String.valueOf(prod.getPrecio()));
		tvUnidad.setText(prod.getUnidad());
		tvDescripcion.setText(prod.getDescripcion());
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnEliminar.getId()){
			confimarEliminarProd();
		}else if (v.getId()==btnEditar.getId()) {
			listener.onEditarProdClick(this.prod.getIdprod());
			dismiss();
		}else if (v.getId()==btnOk.getId()) {
			dismiss();
		}
	}
	
	public void confimarEliminarProd(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Eliminar?");
		dialog.setMessage("Recuerde que una vez elimine producto no podra restaurarlo");
		dialog.setPositiveButton(R.string.aceptar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.eliminarProducto(prod)){
						Toast.makeText(getActivity(), "Producto eliminado", Toast.LENGTH_SHORT).show();
						lista_prod.cargarMostrarListaProductos();
						DialogDetalleProd.this.dismiss();
						dialog.dismiss();
						
					}else{
						Toast.makeText(getActivity(), "No se pudo eliminar producto, intente mas tarde..!", Toast.LENGTH_SHORT).show();
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
	
	public interface OnEditarProdClickListener{
		public void onEditarProdClick(String idprod);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (OnEditarProdClickListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+" debe implementar OnEditarProdClickListener");
		}
	}
}
