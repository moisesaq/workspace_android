package com.silvia.fragmentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Producto;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NuevoProducto extends Fragment implements OnClickListener{

	ImageView ivImagen;
	EditText etNombreProd, etPrecio, etUnidad, etDescripcion;
	Button btnAceptar;
	
	String path_imagen = Variables.SIN_ESPECIFICAR;
	String idprod;
	Producto editar_prod;
	
	public int ACCION;
	
	OnBackToListaProdClickListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.nuevo_producto, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		ivImagen = (ImageView)v.findViewById(R.id.ivImagenNuevoProd);
		ivImagen.setOnClickListener(this);
		etNombreProd = (EditText)v.findViewById(R.id.etNombreProdNuevoProd);
		etPrecio = (EditText)v.findViewById(R.id.etPrecioNuevoProd);
		etUnidad = (EditText)v.findViewById(R.id.etUnidadNuevoProd);
		etDescripcion = (EditText)v.findViewById(R.id.etDescripcionNuevoProd);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarNuevoProd);
		btnAceptar.setOnClickListener(this);
		
		Bundle cajon = getArguments();
		this.ACCION = cajon.getInt("accion");
		if(ACCION==Variables.ACCION_NUEVO_PROD){
			actionBar.setTitle("Nuevo producto");
		}else if (ACCION==Variables.ACCION_EDITAR_PROD) {
			actionBar.setTitle("Modificar producto");
			this.idprod = cajon.getString("idprod");
			this.btnAceptar.setText("listo");
			cargarDatosProductoParaEditar();
		}
	}
	
	private void cargarDatosProductoParaEditar() {
		cargarProducto();
		if(!editar_prod.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(editar_prod.getImagen()).toString();
			Bitmap bitmap_imagen = BitmapFactory.decodeFile(pathImagen);
			if(bitmap_imagen!=null){
				ivImagen.setImageBitmap(bitmap_imagen);
				ivImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagen.setPadding(0, 0, 0, 0);
			}else{
				ivImagen.setImageResource(R.drawable.ic_product_plant_128);
			}
		}else{
			ivImagen.setImageResource(R.drawable.ic_product_plant_128);
		}
		this.path_imagen = editar_prod.getImagen();
		etNombreProd.setText(editar_prod.getNombre_prod());
		etPrecio.setText(String.valueOf(editar_prod.getPrecio()));
		if(!editar_prod.getUnidad().equals(Variables.SIN_ESPECIFICAR)){
			etUnidad.setText(editar_prod.getUnidad());
		}
		if(!editar_prod.getDescripcion().equals(Variables.SIN_ESPECIFICAR)){
			etDescripcion.setText(editar_prod.getDescripcion());
		}
	}

	@Override
	public void onClick(View v){
		if(v.getId()==ivImagen.getId()){
			selectImage();
		}else if (v.getId()==btnAceptar.getId()) {
			if(ACCION==Variables.ACCION_NUEVO_PROD){
				registrarProducto();
			}else if (ACCION==Variables.ACCION_EDITAR_PROD) {
				modificarProducto();
			}
		}
	}
	
	public void registrarProducto(){
		String nombre_prod = etNombreProd.getText().toString().trim().toUpperCase();
		double precio = 0;
		if(!etPrecio.getText().toString().equals("")){
			precio = Double.parseDouble(etPrecio.getText().toString());
		}
		String unidad = etUnidad.getText().toString().trim().toUpperCase();
		if(unidad.equals("")){
			unidad = Variables.SIN_ESPECIFICAR;
		}
		String descripcion = etDescripcion.getText().toString();
		if(descripcion.equals("")){
			descripcion = Variables.SIN_ESPECIFICAR;
		}
		
		if(nombre_prod.length()>=4){
			if(precio!=0){
				String idprod = generarIdProducto();
				if(ivImagen.getTag()!=null){
					String name_image_prod = new StringBuilder("pic-").append(idprod).append(".jpg").toString();
					savedImage(name_image_prod);
				}
				Producto prod = new Producto(idprod, nombre_prod, precio, unidad, descripcion, this.path_imagen, Variables.NO_ELIMINADO);
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.insertarProducto(prod)){
						Toast.makeText(getActivity(), "Producto registrado exitosamente", Toast.LENGTH_SHORT).show();
						//limpiarCampos();
						listener.onBackToListaProdClick();
					}else{
						Toast.makeText(getActivity(), "No se pudo registrar producto", Toast.LENGTH_SHORT).show();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				etPrecio.requestFocus();
				etPrecio.setError("Introduzca precio del producto");
			}
		}else{
			etNombreProd.requestFocus();
			etNombreProd.setError("Nombre del producto, minimo debe tener 4 caracteres");
		}
	}
	
	public void modificarProducto(){
		String nombre_prod = etNombreProd.getText().toString().trim().toUpperCase();
		double precio = 0;
		if(!etPrecio.getText().toString().equals("")){
			precio = Double.parseDouble(etPrecio.getText().toString());
		}
		String unidad = etUnidad.getText().toString().trim().toUpperCase();
		if(unidad.equals("")){
			unidad = Variables.SIN_ESPECIFICAR;
		}
		String descripcion = etDescripcion.getText().toString();
		if(descripcion.equals("")){
			descripcion = Variables.SIN_ESPECIFICAR;
		}
		
		if(nombre_prod.length()>=4){
			if(precio!=0){
				if(ivImagen.getTag()!=null){
					String name_image_prod = new StringBuilder("pic-").append(generarIdProducto()).append(".jpg").toString();
					savedImage(name_image_prod);
				}
				Producto prod = new Producto(editar_prod.getIdprod(), nombre_prod, precio, unidad, descripcion, this.path_imagen, Variables.NO_ELIMINADO);
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.modificarProducto(prod)){
						Toast.makeText(getActivity(), "Producto modificado exitosamente", Toast.LENGTH_SHORT).show();
						//limpiarCampos();
						listener.onBackToListaProdClick();
					}else{
						Toast.makeText(getActivity(), "No se pudo modificar producto", Toast.LENGTH_SHORT).show();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				etPrecio.requestFocus();
				etPrecio.setError("Introduzca precio del producto");
			}
		}else{
			etNombreProd.requestFocus();
			etNombreProd.setError("Nombre del producto, minimo debe tener 4 caracteres");
		}
	}

	public String generarIdProducto(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new Date());
		String imageCode = "prod-"+date;
		return imageCode;
	}
		
	public void limpiarCampos(){
		ivImagen.setImageResource(R.drawable.ic_launcher);
		path_imagen = Variables.SIN_ESPECIFICAR;
		etNombreProd.getText().clear();
		etPrecio.getText().clear();
		etUnidad.getText().clear();
		etDescripcion.getText().clear();
	}
	
	public void cargarProducto(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			editar_prod = db.getProducto(this.idprod);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public interface OnBackToListaProdClickListener{
		public void onBackToListaProdClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnBackToListaProdClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+"debe implementar OnBackToListaProdClickListener");
		}
	}

	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		try {
			intent.putExtra("return-data", true);
			startActivityForResult(Intent.createChooser(intent, "Completar accion con"), Variables.PICK_FROM_GALLERY);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "Dispositivo no soporta recorte de imagen", Toast.LENGTH_SHORT).show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		getActivity();
		if(requestCode==Variables.PICK_FROM_GALLERY && resultCode==Activity.RESULT_OK){
			Bundle extras1 = data.getExtras();
			if(extras1!=null){
				Bitmap imageBitmap = extras1.getParcelable("data");
				this.ivImagen.setImageBitmap(imageBitmap);
				this.ivImagen.setTag(imageBitmap);
			}
		}
	}
	
	public void savedImage(String name_image){
		File ruta_images = new File(Variables.FOLDER_IMAGES_COOPERATIVA);
		ruta_images.mkdirs();
		
		File file_image = new File(ruta_images, name_image);
		
		try {
			FileOutputStream fos = new FileOutputStream(file_image);
			Bitmap bitmap = (Bitmap)ivImagen.getTag();
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			scanImage(file_image.getAbsolutePath());
			if(file_image.exists()){
				this.path_imagen = file_image.getName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void scanImage(final String pathImage){
		new MediaScannerConnectionClient() {
			private MediaScannerConnection msc = null; {
				msc = new MediaScannerConnection(getActivity().getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(pathImage, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}
}
