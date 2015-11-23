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
import com.silvia.modelo.Maquinaria;

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
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NuevaMaquinaria extends Fragment implements OnClickListener{

	ImageView ivImagen;
	EditText etPlaca, etDescripcion, etCapacidad, etMarca, etColor;
	Button btnAceptar;
	String path_imagen = Variables.SIN_ESPECIFICAR;
	String idmaquinaria;
	Maquinaria editar_maq;
	private int ACCION;
	private int DIGITOS_PARA_VALIDACION;
	private boolean PLACA_DISPONIBLE = false;
	
	OnBackToListaMaqClickListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.nueva_maquinaria, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		ivImagen = (ImageView)v.findViewById(R.id.ivImagenNuevaMaquinaria);
		ivImagen.setOnClickListener(this);
		etPlaca = (EditText)v.findViewById(R.id.etPlacaNuevaMaquinaria);
		etPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
		etPlaca.addTextChangedListener(PlacaMaquinaWacher);
		etDescripcion = (EditText)v.findViewById(R.id.etDescripcionNuevaMaquinaria);
		etCapacidad = (EditText)v.findViewById(R.id.etCapacidadNuevaMaquinaria);
		etMarca = (EditText)v.findViewById(R.id.etMarcaNuevaMaquinaria);
		etColor = (EditText)v.findViewById(R.id.etColorNuevaMaquinaria);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarNuevaMaquinaria);
		btnAceptar.setOnClickListener(this);
		
		Bundle cajon = getArguments();
		this.ACCION = cajon.getInt("accion");
		if(ACCION==Variables.ACCION_NUEVO_MAQ){
			actionBar.setTitle("Nueva maquinaria");
			this.DIGITOS_PARA_VALIDACION = 6;
		}else if(ACCION==Variables.ACCION_EDITAR_MAQ){
			actionBar.setTitle("Modificar maquinaria");
			this.idmaquinaria = cajon.getString("idmaquinaria");
			btnAceptar.setText("Listo");
			cargarDatosMaquinariaParaEditar();
		}
	}
	
	private final TextWatcher PlacaMaquinaWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(s.length()>=0 && s.length()<=2){
				etPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
			}else if(s.length()==3){
				etPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				//etPlaca.setFilters(new InputFilter[]{MyVar.filterNumLetter});
			}else if(s.length()==4){
				etPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				if(isNumeric(s.toString())){
					DIGITOS_PARA_VALIDACION = 7;
					etPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
				}else{
					DIGITOS_PARA_VALIDACION = 6;
					etPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
				}
			}else if (s.length()>=5) {
				etPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				//etPlaca.setFilters(new InputFilter[]{MyVar.filterLetter, new InputFilter.LengthFilter(DIGITOS_PARA_VALIDACION)});
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(ACCION==Variables.ACCION_NUEVO_MAQ){
				if(DIGITOS_PARA_VALIDACION==s.length()){
					if(Variables.isPlaca(s.toString())){
						if(verificarPlaca(s.toString().toUpperCase())){
							PLACA_DISPONIBLE = true;
							etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
						}else{
							etPlaca.requestFocus();
							etPlaca.setError("Placa ya existe");
							etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
							PLACA_DISPONIBLE = false;
						}
					}else{
						etPlaca.requestFocus();
						etPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
						PLACA_DISPONIBLE = false;
					}
				}else{
					PLACA_DISPONIBLE = false;
					etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(ACCION==Variables.ACCION_EDITAR_MAQ){
				if(!editar_maq.getPlaca().equals(s.toString().toUpperCase())){
					if(DIGITOS_PARA_VALIDACION==s.length()){
						if(Variables.isPlaca(s.toString())){
							if(verificarPlaca(s.toString().toUpperCase())){
								PLACA_DISPONIBLE = true;
								etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
							}else{
								etPlaca.requestFocus();
								etPlaca.setError("Placa ya existe");
								etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
								PLACA_DISPONIBLE = false;
							}
						}else{
							etPlaca.requestFocus();
							etPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
							PLACA_DISPONIBLE = false;
						}
					}else{
						PLACA_DISPONIBLE = false;
						etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
					}
				}else{
					etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					PLACA_DISPONIBLE = true;
				}
			}
		}
	};
	
	public boolean verificarPlaca(String placa){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(!db.existePlaca(placa)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isNumeric(String dato){
		try{
			Integer.parseInt(dato);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	private void cargarDatosMaquinariaParaEditar() {
		cargarMaquinaria();
		if(!editar_maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(editar_maq.getImagen()).toString();
			Bitmap bitmap_imagen = BitmapFactory.decodeFile(pathImagen);
			if(bitmap_imagen!=null){
				ivImagen.setImageBitmap(bitmap_imagen);
				ivImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagen.setPadding(0, 0, 0, 0);
			}else{
				ivImagen.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
		}else{
			ivImagen.setImageResource(R.drawable.ic_local_shipping_white_48dp);
		}
		
		this.path_imagen = editar_maq.getImagen();
		etPlaca.setText(editar_maq.getPlaca());
		this.DIGITOS_PARA_VALIDACION = etPlaca.getText().toString().length();
		etDescripcion.setText(editar_maq.getDescripcion());
		etCapacidad.setText(String.valueOf(editar_maq.getCapacidad()));		
		
		if(!editar_maq.getMarca().equals(Variables.SIN_ESPECIFICAR)){
			etMarca.setText(editar_maq.getMarca());
		}
		if(!editar_maq.getColor().equals(Variables.SIN_ESPECIFICAR)){
			etColor.setText(editar_maq.getColor());
		}
		
	}

	@Override
	public void onClick(View v){
		if(ivImagen.getId()==v.getId()){
			//Toast.makeText(getActivity(), "Seleccionar imagen", Toast.LENGTH_SHORT).show();
			selectImage();
		}else if (btnAceptar.getId()==v.getId()) {
			if(ACCION==Variables.ACCION_NUEVO_MAQ){
				registrarMaquinaria();
			}else{
				actualizarMaquinaria();
			}
			
		}
	}
	
	public void registrarMaquinaria(){
		String placa = etPlaca.getText().toString().trim().replace(" ", "").toUpperCase();
		String descripcion = etDescripcion.getText().toString();
		double capacidad = 0;
		if(!etCapacidad.getText().toString().equals("")){
			capacidad = Double.parseDouble(etCapacidad.getText().toString().trim());
		}
		String marca = etMarca.getText().toString();
		if(marca.equals("")){ 
			marca = Variables.SIN_ESPECIFICAR; 
		}
		String color = etColor.getText().toString();
		if(color.equals("")){
			color = Variables.SIN_ESPECIFICAR;
		}
		
		
		DBDuraznillo db = new DBDuraznillo(getActivity());
		if(placa.length()>=DIGITOS_PARA_VALIDACION){
			if(descripcion.length()>=5){
				if(capacidad!=0){
					try {
						db.abrirDB();
						String idmaq = generarIdMaquinaria();
						if(ivImagen.getTag()!=null){
							String name_image_maq = new StringBuilder("pic-").append(idmaq).append(".jpg").toString();
							savedImage(name_image_maq);
						}
						Maquinaria maq = new Maquinaria(idmaq, placa, descripcion, capacidad, marca, color, path_imagen, Variables.NO_ELIMINADO);
						if(PLACA_DISPONIBLE){
							if(db.insertarMaquinaria(maq)){
								Toast.makeText(getActivity(), "Maquinaria registrado exitosamente", Toast.LENGTH_SHORT).show();
								listener.onBackToListaMaqClick();
							}else{
								Toast.makeText(getActivity(), "No se pudo registrar maquinaria", Toast.LENGTH_SHORT).show();
							}
						}else{
							etPlaca.requestFocus();
							etPlaca.setError("Esta placa ya existe");
						}
						
						db.cerrarDB();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					etCapacidad.requestFocus();
					etCapacidad.setError("Introduzca capacidad de la maquina");
				}
			}else{
				etDescripcion.requestFocus();
				etDescripcion.setError("Introduzca descripcion, minimo 5 caracteres");
			}
		}else{
			etPlaca.requestFocus();
			etPlaca.setError("Introduzca placa, debe tener 6 o 7 caracteres");
		}
	}
	
	public void actualizarMaquinaria(){
		String placa = etPlaca.getText().toString().trim().replace(" ", "").toUpperCase();
		String descripcion = etDescripcion.getText().toString();
		double capacidad = 0;
		if(!etCapacidad.getText().toString().equals("")){
			capacidad = Double.parseDouble(etCapacidad.getText().toString().trim());
		}
		String marca = etMarca.getText().toString();
		if(marca.equals("")){ 
			marca = Variables.SIN_ESPECIFICAR; 
		}
		String color = etColor.getText().toString();
		if(color.equals("")){
			color = Variables.SIN_ESPECIFICAR;
		}
		DBDuraznillo db = new DBDuraznillo(getActivity());
		if(placa.length()>=DIGITOS_PARA_VALIDACION){
			if(descripcion.length()>=5){
				if(capacidad!=0){
					try {
						db.abrirDB();
						if(ivImagen.getTag()!=null){
							String name_image_maq = new StringBuilder("pic-").append(generarIdMaquinaria()).append(".jpg").toString();
							savedImage(name_image_maq);
						}
						Maquinaria maq1 = new Maquinaria(editar_maq.getIdmaquinaria(), placa, descripcion, capacidad, 
								marca, color, path_imagen, Variables.NO_ELIMINADO);
						if(PLACA_DISPONIBLE){
							if(db.modificarMaq(maq1)){
								Toast.makeText(getActivity(), "Maquinaria modificada", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(getActivity(), "No se pudo modificar maquinaria", Toast.LENGTH_SHORT).show();
							}
							listener.onBackToListaMaqClick();
						}else{
							etPlaca.requestFocus();
							etPlaca.setError("Esta placa ya existe");
						}
						db.cerrarDB();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					etCapacidad.requestFocus();
					etCapacidad.setError("Introduzca capacidad de la maquina");
				}
			}else{
				etDescripcion.requestFocus();
				etDescripcion.setError("Introduzca descripcion, minimo 5 caracteres");
			}
		}else{
			etPlaca.requestFocus();
			etPlaca.setError("Introduzca placa, debe tener minimo 6 caracteres");
		}
	}
	
	public void cargarMaquinaria(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			editar_maq = db.getMaquinaria(this.idmaquinaria);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String generarIdMaquinaria(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new Date());
		String imageCode = "maq-"+date;
		return imageCode;
	}
		
	public void limpiarCampos(){
		ivImagen.setImageResource(R.drawable.ic_launcher);
		path_imagen = Variables.SIN_ESPECIFICAR;
		etPlaca.getText().clear();
		etDescripcion.getText().clear();
		etMarca.getText().clear();
		etColor.getText().clear();
	}
	
	public interface OnBackToListaMaqClickListener{
		public void onBackToListaMaqClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnBackToListaMaqClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+"debe implementar OnBackToListaMaqClickListener");
		}
	}
	
	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 2);
		intent.putExtra("outputX", 460);
		intent.putExtra("outputY", 230);
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
				ivImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagen.setPadding(0, 0, 0, 0);
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
