package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogAsignarPropietario;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.dialogs.DialogOptionImageVehiculo;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;
import com.moisse.others.UploadImage;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class NuevoVehiculo extends Fragment implements OnClickListener{

	protected EditText etPlaca, etMarca, etColor;
	public ImageView ivImageVehiculo, ivImagePropietario;
	protected Button btnListo;
	public AutoCompleteTextView actvCIPropietario;
	public TextView tvNombreProp;
	public ProgressBar pbVerifyPlaca;
	public String pathImageVehiculo = MyVar.NO_ESPECIFICADO;
	private int ACCION;
	
	private String idvehiculo;
	private Vehiculo vehiculo_edit;
	private Cliente cliente;
	
	public int OPCION_ELEGIDA = 0;
	private String idparqueo;
	protected HttpClientCustom httpCustom = new HttpClientCustom();
	private String id_client_default;
	
	private Uri finalImageUri;
	private Bitmap finalImageBitmap; 
	OnBackFromNuevoVehiculoClickListener listener;
	
	public boolean PLACA_DISPONIBLE = false;
	public int DIGITOS_PARA_VALIDACION;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.nuevo_vehiculo, container, false);
		inicializarComponentes(v);
		return v;
	}
	
	private void inicializarComponentes(View v) {
		ivImageVehiculo = (ImageView)v.findViewById(R.id.ivImageVehiculo);
		ivImageVehiculo.setOnClickListener(this);
		pbVerifyPlaca = (ProgressBar)v.findViewById(R.id.pbVericarPlacaVehiculo);
		etPlaca = (EditText)v.findViewById(R.id.etPlacaVehiculo);
		etPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
		etPlaca.addTextChangedListener(PlacaVehiculoWacher);
		etMarca = (EditText)v.findViewById(R.id.etMarcaVehiculo);
		etColor = (EditText)v.findViewById(R.id.etColorVehiculo);
		ivImagePropietario = (ImageView)v.findViewById(R.id.ivImagePropietarioVehiculo);
		ivImagePropietario.setOnClickListener(this);
		actvCIPropietario = (AutoCompleteTextView)v.findViewById(R.id.actvCIPropietarioVehiculo);
		actvCIPropietario.setEnabled(false);
		tvNombreProp = (TextView)v.findViewById(R.id.tvNombrePropVehiculo);		
		btnListo = (Button)v.findViewById(R.id.btnListoVehiculo);
		btnListo.setOnClickListener(this);
		
		ActionBar actionB = getActivity().getActionBar();
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		Bundle input = getArguments();
		this.ACCION = input.getInt("accion");
		this.idparqueo = input.getString("idparqueo");
		id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
		if(this.ACCION==MyVar.ACCION_REGISTRAR_VEHICULO){
			DIGITOS_PARA_VALIDACION = 0;
			btnListo.setText(new StringBuilder("Aceptar"));
			actionB.setTitle("Nuevo vehiculo");
			actvCIPropietario.setTag(id_client_default);
		}else if(this.ACCION==MyVar.ACCION_EDITAR_VEHICULO){
			this.idvehiculo = input.getString("id_vehiculo");
			cargarDatosVehiculoParaEditar();
			actionB.setTitle("Editar vehiculo");
		}

	}
	
	private final TextWatcher PlacaVehiculoWacher = new TextWatcher() {
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
			VerifyPlacaVehiculo verify = new VerifyPlacaVehiculo();
			if(ACCION==MyVar.ACCION_REGISTRAR_VEHICULO){
				if(DIGITOS_PARA_VALIDACION==s.length()){
					if(MyVar.isPlaca(s.toString())){
						verify.execute(s.toString());
					}else{
						etPlaca.requestFocus();
						etPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
						PLACA_DISPONIBLE = false;
					}
				}else{
					verify.cancel(true);
					PLACA_DISPONIBLE = false;
					etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(ACCION==MyVar.ACCION_EDITAR_VEHICULO){
				if(!vehiculo_edit.getPlaca().equals(s.toString().toUpperCase())){
					if(DIGITOS_PARA_VALIDACION==s.length()){
						if(MyVar.isPlaca(s.toString())){
							verify.execute(s.toString());
						}else{
							etPlaca.requestFocus();
							etPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
							PLACA_DISPONIBLE = false;
						}
					}else{
						verify.cancel(true);
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
	
	public static boolean isNumeric(String dato){
		try{
			Integer.parseInt(dato);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	private class VerifyPlacaVehiculo extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pbVerifyPlaca.setVisibility(View.VISIBLE);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String placa = params[0];
			if(httpCustom.verificarDisponibilidadPlacaVehiculo(idparqueo, placa)){
				return true;
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pbVerifyPlaca.setVisibility(View.GONE);
			if(result){
				etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
				PLACA_DISPONIBLE = true;
			}else{
				etPlaca.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				PLACA_DISPONIBLE = false;
			}
		}
	}

	/*private final TextWatcher CIPropWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(count==0){
				actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				ivImagePropietario.setImageResource(R.drawable.ic_person_white_36dp);
				tvNombreProp.setVisibility(View.GONE);
			}
		}
				
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()==7 && s.length()==8){
				Cliente cliente = getClientePorCI(actvCIPropietario.getText().toString());
				if(cliente!=null){
					actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
					tvNombreProp.setVisibility(View.VISIBLE);
					tvNombreProp.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
					if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
						Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
						if(bitmap!=null){
							ivImagePropietario.setImageBitmap(bitmap);
						}else{
							ivImagePropietario.setImageResource(R.drawable.ic_client);
						}
					}else{
						ivImagePropietario.setImageResource(R.drawable.ic_client);
					}
				}else{
					actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(s.length()<6 && s.length()>0){
				actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				ivImagePropietario.setImageResource(R.drawable.ic_person_white_36dp);
				tvNombreProp.setVisibility(View.GONE);
			}else if(s.length()==0){
				actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				ivImagePropietario.setImageResource(R.drawable.ic_person_white_36dp);
				tvNombreProp.setVisibility(View.GONE);
			}
		}
	}; */

	private void cargarDatosVehiculoParaEditar() {
		vehiculo_edit = getVehiculo(this.idvehiculo);
		if(!vehiculo_edit.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap myBitmap = BitmapFactory.decodeFile(MyVar.FOLDER_IMAGES_PARQUEO+vehiculo_edit.getImagen());
			if(myBitmap!=null){
				ivImageVehiculo.setImageBitmap(myBitmap);
			}else{
				ivImageVehiculo.setImageResource(R.drawable.ic_car);
			}
		}else{
			ivImageVehiculo.setImageResource(R.drawable.ic_car);
		}
		this.pathImageVehiculo = vehiculo_edit.getImagen();
		etPlaca.setText(new StringBuilder(vehiculo_edit.getPlaca()));
		DIGITOS_PARA_VALIDACION = etPlaca.getText().toString().length();
		
		if(!vehiculo_edit.getMarca().equals(MyVar.NO_ESPECIFICADO)){
			etMarca.setText(new StringBuilder(vehiculo_edit.getMarca()));
		}
		if(!vehiculo_edit.getColor().equals(MyVar.NO_ESPECIFICADO)){
			etColor.setText(new StringBuilder(vehiculo_edit.getColor()));
		}
		
		if(!vehiculo_edit.getIdcliente().equals(id_client_default)){
			cliente = getCliente(vehiculo_edit.getIdcliente());
			tvNombreProp.setVisibility(View.VISIBLE);
			if(cliente.getApellido().equals(MyVar.NO_ESPECIFICADO)){
				tvNombreProp.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
			}else{
				tvNombreProp.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
			}
			if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
				Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
				if(bitmap!=null){
					ivImagePropietario.setImageBitmap(bitmap);
				}
				else{
					ivImagePropietario.setImageResource(R.drawable.ic_client);
				}
			}else{
				ivImagePropietario.setImageResource(R.drawable.ic_client);
			}
			actvCIPropietario.setText(new StringBuilder().append(cliente.getCi()));
		}
		actvCIPropietario.setTag(vehiculo_edit.getIdcliente());
	}
	
	public Vehiculo getVehiculo(String idvehiculo){
		DBParqueo db = new DBParqueo(getActivity());
		Vehiculo vehiculo=null;
		try {
			db.openSQLite();
			vehiculo = db.getVehiculo(idvehiculo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehiculo;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==ivImageVehiculo.getId()){
			DialogOptionImageVehiculo dv = new DialogOptionImageVehiculo(this);
			dv.show(getFragmentManager(), "tagDialogOptionVehiculo");
		}else if(v.getId()==btnListo.getId()){
			validarDatosOperar();			
		}else if(v.getId()==ivImagePropietario.getId()){
			DialogAsignarPropietario dAP = new DialogAsignarPropietario(this, this.idparqueo);
			dAP.show(getFragmentManager(), "tagDAP");
		}
	}
	
	private void validarDatosOperar() {
		String placa = etPlaca.getText().toString().trim().toUpperCase();
		String marca = etMarca.getText().toString();
		if(marca.equals("")){ 
			marca = MyVar.NO_ESPECIFICADO; 
		}
		String color = etColor.getText().toString();
		if(color.equals("")){ 
			color = MyVar.NO_ESPECIFICADO; 
		}
		
		String idcliente = (String)actvCIPropietario.getTag();
		
		if(!placa.equals("") && placa.length()>=6){
			if(placa.length()==DIGITOS_PARA_VALIDACION){
				if(MyVar.isPlaca(placa)){
					if(PLACA_DISPONIBLE){
						if(ACCION==MyVar.ACCION_REGISTRAR_VEHICULO){
							if(!pathImageVehiculo.equals(MyVar.NO_ESPECIFICADO)){
								copySaveImageVehiculo();
							}
							Vehiculo nuevo_vehiculo = new Vehiculo(getIDVehiculoGenerado(), placa, marca,
														color, this.pathImageVehiculo, MyVar.NO_ELIMINADO, idcliente);
							new RegistrarVehiculo().execute(nuevo_vehiculo);
							
						}else if(ACCION==MyVar.ACCION_EDITAR_VEHICULO){
							if(!vehiculo_edit.getImagen().equals(pathImageVehiculo) && !this.pathImageVehiculo.equals(MyVar.NO_ESPECIFICADO)){
								copySaveImageVehiculo();
							}
							Vehiculo vehiculo = new Vehiculo(this.vehiculo_edit.getIdvehiculo(), placa, marca,
									color, this.pathImageVehiculo, MyVar.NO_ELIMINADO, idcliente);
							new ActualizarVehiculo().execute(vehiculo);		
						}
					}else{
						etPlaca.requestFocus();
						etPlaca.setError("Placa introducida ya existe");
					}	
				}else{
					etPlaca.requestFocus();
					etPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
				}
			}else{
				etPlaca.requestFocus();
				etPlaca.setError("Placa incorrecta falta un dígito");
			}
		}else{
			etPlaca.requestFocus();
			etPlaca.setError("Placa del vehículo debe tener 6 o 7 caracteres");
		}
	}
	
	public Cliente getCliente(String idcliente){
		Cliente cliente = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			cliente = db.getCliente(idcliente);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public void SelectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, MyVar.SELECT_IMAGE);	
	}
		
	public void CaptureImage(){
		File folder_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		folder_images.mkdir();
		
		Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File file_image_vehiculo = new File(folder_images, getNameImageVehiculo());
		//Importante sin esto no guarda imagen si no solo como archivo
		finalImageUri = Uri.fromFile(file_image_vehiculo);
		this.pathImageVehiculo = file_image_vehiculo.getAbsolutePath();
		intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, finalImageUri);
		startActivityForResult(intentCamera, MyVar.CAPTURE_IMAGE);
	}
	
	@SuppressWarnings("static-access")
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==MyVar.SELECT_IMAGE && resultCode==getActivity().RESULT_OK){
			OPCION_ELEGIDA = MyVar.SELECT_IMAGE;
			finalImageUri = data.getData();
			this.pathImageVehiculo = getRealPathFromURI(finalImageUri);
			this.finalImageBitmap = BitmapFactory.decodeFile(pathImageVehiculo);
			performCrop();
		}else if(requestCode == MyVar.CAPTURE_IMAGE && resultCode==getActivity().RESULT_OK){
			OPCION_ELEGIDA = MyVar.CAPTURE_IMAGE;
			this.finalImageBitmap = BitmapFactory.decodeFile(pathImageVehiculo);
			performCrop();
		}else if(requestCode ==MyVar.PIC_CROP && resultCode==getActivity().RESULT_OK){
			Bundle extra = data.getExtras();
			this.finalImageBitmap = extra.getParcelable("data");
			ivImageVehiculo.setImageBitmap(finalImageBitmap);
		}
	}
	
	private void performCrop(){
		int valueX = 0;
		int valueY = 0;
		if(finalImageBitmap.getWidth()>=MyVar.PIC_VALUE_MAX || finalImageBitmap.getHeight()>=MyVar.PIC_VALUE_MAX){
			valueX = MyVar.PIC_VALUE_MAX;
			valueY = MyVar.PIC_VALUE_MAX-250;
		}else {
			valueX = MyVar.PIC_VALUE_MAX-300;
			valueY = MyVar.PIC_VALUE_MAX-400;
		}
		try{
			Intent intentCrop = new Intent("com.android.camera.action.CROP");
			intentCrop.setDataAndType(finalImageUri, "image/*");
			//intentCrop.setData(finalImageUri);
			//intentCrop.putExtra("scale", true);
			intentCrop.putExtra("crop", true);
			intentCrop.putExtra("aspectX", 4);
			intentCrop.putExtra("aspectY", 2);
			intentCrop.putExtra("outputX", valueX);
			intentCrop.putExtra("outputY", valueY);
			intentCrop.putExtra("return-data", true);
			//intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intentCrop, MyVar.PIC_CROP);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
			Toast.makeText(getActivity(), "Dispositivo no soporta recorte de imagen", Toast.LENGTH_SHORT).show();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class RegistrarVehiculo extends AsyncTask<Vehiculo, Void, Boolean>{
		ProgressDialog pd;
		Vehiculo vehiculo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Registrando vehiculo...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Vehiculo... params) {
			vehiculo = params[0];
			if(httpCustom.insertarVehiculo(this.vehiculo)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.insertarVehiculo(this.vehiculo)){						
						return true;
					}
					db.closeSQLite();			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehiculo.getImagen()).toString());
				}
				Toast.makeText(getActivity(), "Vehiculo registrado exitosamente", Toast.LENGTH_LONG).show();
				listener.onBackFromNuevoVehiculoClick();
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo resgistrar vehiculo, intente mas tarde..!");
				mensaje.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	private class ActualizarVehiculo extends AsyncTask<Vehiculo, Void, Boolean>{
		ProgressDialog pd;
		Vehiculo vehiculo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando vehiculo...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Vehiculo... params) {
			vehiculo = params[0];
			if(httpCustom.actualizarVehiculo(this.vehiculo)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.actualizarVehiculo(this.vehiculo)){
						return true;
					}
					db.closeSQLite();			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				if(!vehiculo.getImagen().equals(vehiculo_edit.getImagen()) && !vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehiculo.getImagen()).toString());
				}
				Toast.makeText(getActivity(), "Datos del vehiculo actualizado", Toast.LENGTH_LONG).show();
				listener.onBackFromNuevoVehiculoClick();
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo actualizar datos del vehiculo, intente mas tarde..!");
				mensaje.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	public boolean copySaveImageVehiculo(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		ruta_images.mkdirs();
		
		String name_image_vehiculo = null;
		if(OPCION_ELEGIDA==MyVar.SELECT_IMAGE){
			name_image_vehiculo = getNameImageVehiculo();
		}else if(OPCION_ELEGIDA==MyVar.CAPTURE_IMAGE){
			name_image_vehiculo = new File(this.pathImageVehiculo).getName();
		}
		File file_image_vehiculo = new File(ruta_images, name_image_vehiculo);
//		if(file_image_vehiculo.exists()){
//			file_image_vehiculo.delete();
//		}
		FileOutputStream fos;
		try{
			//Le sacamos el nombre de la imagen para que guarde solo el nombre y no asi toda la ruta
			this.pathImageVehiculo = file_image_vehiculo.getName();
			fos = new FileOutputStream(file_image_vehiculo);
			finalImageBitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			scanImage(file_image_vehiculo.getAbsolutePath());
			if(file_image_vehiculo.exists()){
				return true;
			}
		}catch(IOException e){
			e.printStackTrace();
		}	
		return false;
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
	
	public String getNameImageVehiculo(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new Date());
		String imageCode = "vehi_"+this.idparqueo+"_pic_"+date+".jpg";
		return imageCode;
	}
	
	public String getIDVehiculoGenerado(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "vehi_"+date+"_"+this.idparqueo;
		return imageCode;
	}
	
	@SuppressWarnings("unused")
	public String getRealPathFromURI(Uri contentUri){
		String [] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().managedQuery(contentUri, null, null, null, null);	
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public void limpiarCampos(){
		etPlaca.getText().clear();
		etMarca.getText().clear();
		etColor.getText().clear();
		ivImagePropietario.setImageResource(R.drawable.ic_person_white_36dp);
		actvCIPropietario.getText().clear();
		actvCIPropietario.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		ivImageVehiculo.setImageResource(R.drawable.ic_directions_car_white_48dp);
		etPlaca.requestFocus();
		this.pathImageVehiculo = MyVar.NO_ESPECIFICADO;
		OPCION_ELEGIDA = 0;
	}	
	
	public interface OnBackFromNuevoVehiculoClickListener{
		public void onBackFromNuevoVehiculoClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnBackFromNuevoVehiculoClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnBackFromNuevoNuevoClickListener");
		}
	}
}
