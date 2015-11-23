package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogFecha;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.dialogs.DialogOptionImageCliente;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class NuevoCliente extends Fragment implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener{
	
	public ImageView ivImageCliente;
	private ProgressBar pbVerificarCI;
	private EditText etCI, etNombre, etApellido, etCelular, etDireccion, etEmail;
	private TextView tvFecha_nac, tvFecha_contrato;
	private RadioGroup rdgSexo;
	private RadioButton rbtnMasculino, rbtnFemenino;
	private ImageButton ibtnCalendarFechaNac, ibtnCalendarFechaContrato;
	private Spinner spinnerTipo;
	private LinearLayout lyFechaContrato;
	private Button btnListo;
	
	OnBackFromNuevoClienteClickListener listener;
	
	public int OPCION_ELEGIDA = 0;
	public String pathImageCliente = MyVar.NO_ESPECIFICADO;
	private String sexo = MyVar.NO_ESPECIFICADO;

	private String idparqueo, idcliente;
	private int ACCION;
	protected Cliente cliente_edit;
	protected HttpClientCustom httpCustom = new HttpClientCustom();
	
	List<String> tipos = new ArrayList<String>();
	private int tipo_cliente = MyVar.CLIENTE_OCASIONAL;
	
	private Uri finalImageUri;
	private Bitmap finalImageBitmap;
	public boolean CI_DISPONIBLE = false;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.nuevo_cliente, container, false);
		inicializarComponentes(view);
		return view;
	}
	
	private void inicializarComponentes(View view) {
		ivImageCliente = (ImageView)view.findViewById(R.id.ivImageCliente);
		ivImageCliente.setOnClickListener(this);
		pbVerificarCI = (ProgressBar)view.findViewById(R.id.pbVericarCICliente);
		etCI = (EditText)view.findViewById(R.id.etCICliente);
		etCI.addTextChangedListener(CIClienteWacher);
		etNombre = (EditText)view.findViewById(R.id.etNombreCliente);
		etApellido = (EditText)view.findViewById(R.id.etApellidoCliente);
		etCelular = (EditText)view.findViewById(R.id.etCelularCliente);
		etDireccion = (EditText)view.findViewById(R.id.etDireccionCliente);
		etEmail = (EditText)view.findViewById(R.id.etEmailCliente);
		rdgSexo = (RadioGroup)view.findViewById(R.id.rdgSexoCliente);
		rdgSexo.setOnCheckedChangeListener(this);
		rbtnMasculino = (RadioButton)view.findViewById(R.id.rdbMasculinoCliente);
		rbtnFemenino = (RadioButton)view.findViewById(R.id.rdbFemeninoCliente);
		tvFecha_nac = (TextView)view.findViewById(R.id.tvFechaNacCliente);
		
		ibtnCalendarFechaNac = (ImageButton)view.findViewById(R.id.ibtnCalendarioFechaNacCliente);
		ibtnCalendarFechaNac.setOnClickListener(this);
		ibtnCalendarFechaContrato = (ImageButton)view.findViewById(R.id.ibtnCalendarioFechaContratoCliente);
		ibtnCalendarFechaContrato.setOnClickListener(this);
		spinnerTipo = (Spinner)view.findViewById(R.id.spinnerTipoCliente);
		spinnerTipo.setOnItemSelectedListener(this);
		lyFechaContrato = (LinearLayout)view.findViewById(R.id.lyFechaContratoCliente);
		tvFecha_contrato = (TextView)view.findViewById(R.id.tvFechaContratoCliente);
		btnListo = (Button)view.findViewById(R.id.btnListoCliente);
		btnListo.setOnClickListener(this);
		
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Nuevo cliente");
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		Bundle recepcion = getArguments();
		this.idparqueo = recepcion.getString("idparqueo");
		this.ACCION = recepcion.getInt("accion");
		cargarSpinnerTipoCliente();
		if(ACCION==MyVar.ACCION_REGISTRAR_CLIENTE){
			tvFecha_nac.setTag(MyVar.FECHA_DEFAULT);
			btnListo.setText("Aceptar");
		}else if(ACCION==MyVar.ACCION_EDITAR_CLIENTE){
			etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			this.CI_DISPONIBLE = true;
			this.idcliente = recepcion.getString("idcliente");
			actionB.setTitle("Editar cliente");
			cargarDatosClienteParaEditar();
		}

	}
	
	public void cargarSpinnerTipoCliente(){
		Parqueo paqueo = getParqueoOnline();
		tipos.clear();
		tipos.add("Ocasional");
		if(paqueo.getPrecioContratoNocturno()!=0){
			tipos.add("Contrato nocturno");
		}
		if (paqueo.getPrecioContratoDiurno()!=0) {
			tipos.add("Contrato diurno");
		}
		if(paqueo.getPrecioContratoDiaCompleto()!=0){
			tipos.add("Contrato dia completo");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tipos);
		spinnerTipo.setAdapter(adapter);
	}
	
	private void cargarDatosClienteParaEditar() {
		cliente_edit = getCliente(this.idcliente);
		if(!cliente_edit.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap myBitmap = BitmapFactory.decodeFile(MyVar.FOLDER_IMAGES_PARQUEO+cliente_edit.getImagen());
			if(myBitmap!=null){
				ivImageCliente.setImageBitmap(myBitmap);
			}
		}else{
			ivImageCliente.setImageResource(R.drawable.ic_client);
		}
		this.pathImageCliente = cliente_edit.getImagen();
		etCI.setText(new StringBuilder().append(cliente_edit.getCi()));
		//etCI.setEnabled(false);
		etNombre.setText(new StringBuilder(cliente_edit.getNombre()));
		if(!cliente_edit.getApellido().equals(MyVar.NO_ESPECIFICADO)){
			etApellido.setText(new StringBuilder(cliente_edit.getApellido()));
		}
		if(cliente_edit.getCelular()!=0){
			etCelular.setText(new StringBuilder().append(cliente_edit.getCelular()));
		}
		if(!cliente_edit.getDireccion().equals(MyVar.NO_ESPECIFICADO)){
			etDireccion.setText(new StringBuilder(cliente_edit.getDireccion()));
		}
		if(!cliente_edit.getEmail().equals(MyVar.NO_ESPECIFICADO)){
			etEmail.setText(new StringBuilder(cliente_edit.getEmail()));
		}
		if(cliente_edit.getSexo().equals(MyVar.MASCULINO)){
			rdgSexo.check(R.id.rdbMasculinoCliente);
		}else if (cliente_edit.getSexo().equals(MyVar.FEMENINO)) {
			rdgSexo.check(R.id.rdbFemeninoCliente);
		}
		if(!cliente_edit.getFecha_nac().equals(MyVar.FECHA_DEFAULT)){
			tvFecha_nac.setText(MyVar.FORMAT_FECHA_1.format(cliente_edit.getFecha_nac()));
		}
		tvFecha_nac.setTag(cliente_edit.getFecha_nac());
		if(cliente_edit.getTipo()!=MyVar.CLIENTE_OCASIONAL){
			int tipo = cliente_edit.getTipo();
			lyFechaContrato.setVisibility(View.VISIBLE);
			if(tipo==MyVar.CLIENTE_CONTRATO_NOCTURNO){
				spinnerTipo.setSelection(MyVar.CLIENTE_CONTRATO_NOCTURNO-1);
				tipo_cliente = MyVar.CLIENTE_CONTRATO_NOCTURNO;
			}else if (tipo==MyVar.CLIENTE_CONTRATO_DIURNO) {
				spinnerTipo.setSelection(MyVar.CLIENTE_CONTRATO_DIURNO-1);
				tipo_cliente = MyVar.CLIENTE_CONTRATO_DIURNO;
			}else if (tipo==MyVar.CLIENTE_CONTRATO_DIA_COMPLETO) {
				spinnerTipo.setSelection(MyVar.CLIENTE_CONTRATO_DIA_COMPLETO-1);
				tipo_cliente = MyVar.CLIENTE_CONTRATO_DIA_COMPLETO;
			}
			tvFecha_contrato.setText(MyVar.FORMAT_FECHA_1.format(cliente_edit.getFecha_contrato()));
			tvFecha_contrato.setTag(cliente_edit.getFecha_contrato());
		}else{
			tipo_cliente = MyVar.CLIENTE_OCASIONAL;
		}
		
	}
	
	private Cliente getCliente(String idcliente){ 
		DBParqueo db = new DBParqueo(getActivity());
		Cliente cliente = null;
		try {
			db.openSQLite();
			cliente = db.getCliente(idcliente);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	private final TextWatcher CIClienteWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
	
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			VerifyCICliente verify = new VerifyCICliente();
			if(ACCION==MyVar.ACCION_REGISTRAR_CLIENTE){
				if(s.length()==7 || s.length()==8){
					verify.execute(s.toString());
				}else{
					verify.cancel(true);
					CI_DISPONIBLE = false;
					etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(ACCION==MyVar.ACCION_EDITAR_CLIENTE){
				if(!cliente_edit.getCi().equals(s.toString())){
					if(s.length()==7 || s.length()==8){
						verify.execute(s.toString());
					}else{
						verify.cancel(true);
						CI_DISPONIBLE = false;
						etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
					}
				}else{
					etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					CI_DISPONIBLE = true;
				}
			}
		}
	};
	
	private class VerifyCICliente extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pbVerificarCI.setVisibility(View.VISIBLE);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String ci_cliente = params[0];
			if(httpCustom.verificarCICliente(idparqueo, ci_cliente)){
				return true;
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean verificacion){
			super.onPostExecute(verificacion);
			pbVerificarCI.setVisibility(View.GONE);
			if(verificacion){
				etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
				CI_DISPONIBLE = true;
			}else{
				etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				CI_DISPONIBLE = false;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivImageCliente:
			DialogOptionImageCliente dialogImage = new DialogOptionImageCliente(this);
			dialogImage.show(getFragmentManager(), "tagDialogOptionImage");
			break;
		case R.id.ibtnCalendarioFechaNacCliente:
			DialogFecha dialog = new DialogFecha(tvFecha_nac);
			dialog.show(getFragmentManager(), "tagFechaNacCliente");
			break;
		case R.id.ibtnCalendarioFechaContratoCliente:
			DialogFecha dialog2 = new DialogFecha(tvFecha_contrato);
			dialog2.show(getFragmentManager(), "tagFechaNacCliente");
			break;
		case R.id.btnListoCliente:
			validarDatosOperar();
			break;
		}
	}
	
	private void validarDatosOperar() {
		String ci = etCI.getText().toString().trim();
		String nombre = MyVar.NO_ESPECIFICADO;
		if(!etNombre.getText().toString().equals("")){ nombre = etNombre.getText().toString().trim(); }
		String apellido = MyVar.NO_ESPECIFICADO;
		if(!etApellido.getText().toString().equals("")){ apellido = etApellido.getText().toString(); }
		int celular = 0;
		if(!etCelular.getText().toString().equals("")){ celular = Integer.parseInt(etCelular.getText().toString()); }
		String direccion = MyVar.NO_ESPECIFICADO;
		if(!etDireccion.getText().toString().equals("")){ direccion = etDireccion.getText().toString(); }
		String email = MyVar.NO_ESPECIFICADO;
		if(!etEmail.getText().toString().equals("")){ email = etEmail.getText().toString(); } 
		Date fecha_nac =  (Date)tvFecha_nac.getTag();
		Date fecha_contrato = (Date)tvFecha_contrato.getTag();
		
		if(ci.length()>=7){
			if(!nombre.equals(MyVar.NO_ESPECIFICADO) && nombre.length()>=3){
				if(CI_DISPONIBLE){
					if(ACCION==MyVar.ACCION_REGISTRAR_CLIENTE){
						if(!pathImageCliente.equals(MyVar.NO_ESPECIFICADO)){
							copySaveImageCliente();
						}
						Cliente nuevo_cliente = new Cliente(getIDClienteGenerado(), ci, nombre, apellido, celular, direccion, 
								  email, sexo, fecha_nac, this.pathImageCliente, MyVar.NO_ELIMINADO, tipo_cliente, fecha_contrato, this.idparqueo);
						new RegistrarCliente().execute(nuevo_cliente);
					}else if(ACCION==MyVar.ACCION_EDITAR_CLIENTE){
						if(!cliente_edit.getImagen().equals(this.pathImageCliente) && !this.pathImageCliente.equals(MyVar.NO_ESPECIFICADO)){
							copySaveImageCliente();
						}
						Cliente cliente = new Cliente(cliente_edit.getIdcliente(), ci, nombre, apellido, celular, direccion, 
								  email, sexo, fecha_nac, this.pathImageCliente, MyVar.NO_ELIMINADO, tipo_cliente, fecha_contrato, cliente_edit.getIdparqueo());
						new ActualizarCliente().execute(cliente);
					}
				}else{
					etCI.requestFocus();
					etCI.setError("Carnet Identidad introducido ya existe");
				}	
			}else{
				etNombre.requestFocus();
				etNombre.setError("Nombre es necesario y mínimo debe tener 3 caracteres");
			}
		}else{
			etCI.requestFocus();
			etCI.setError("Carnet Identidad es necesario y debe tener 7 u 8 digitos");
		}
	}	
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(checkedId==R.id.rdbMasculinoCliente){
			sexo = MyVar.MASCULINO;
		}else if (checkedId == R.id.rdbFemeninoCliente) {
			sexo = MyVar.FEMENINO;
		}
	}
	
	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, MyVar.SELECT_IMAGE);
	}
	
	public void captureImage(){
		try{
			File ruta_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
			ruta_images.mkdirs();
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			
			File file_image_cliente = new File(ruta_images, getNameImageCliente());
			//Este uri es el que guarda la imagen en el dispositivo
			finalImageUri = Uri.fromFile(file_image_cliente);
			this.pathImageCliente = file_image_cliente.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, finalImageUri);
			startActivityForResult(intent, MyVar.CAPTURE_IMAGE);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
		
	}
		
	@SuppressWarnings("static-access")
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==MyVar.SELECT_IMAGE && resultCode==getActivity().RESULT_OK){
			OPCION_ELEGIDA = MyVar.SELECT_IMAGE;
			finalImageUri = data.getData();
			this.pathImageCliente = getRealPathFromURI(finalImageUri);
			this.finalImageBitmap = BitmapFactory.decodeFile(this.pathImageCliente);
			performCrop();
		}else if(requestCode==MyVar.CAPTURE_IMAGE && resultCode==getActivity().RESULT_OK){
			//Toast.makeText(getActivity(), "Imagen capturada y guardado", Toast.LENGTH_SHORT).show();
			OPCION_ELEGIDA = MyVar.CAPTURE_IMAGE;
			this.finalImageBitmap = BitmapFactory.decodeFile(this.pathImageCliente);
			performCrop();
		}else if(requestCode==MyVar.PIC_CROP && resultCode==getActivity().RESULT_OK){
			Bundle extra = data.getExtras();
			finalImageBitmap = extra.getParcelable("data");
			ivImageCliente.setImageBitmap(finalImageBitmap);
		}
	}
	
	private void performCrop(){
		int value = 0;
		if(finalImageBitmap.getWidth()>=MyVar.PIC_VALUE_MAX && finalImageBitmap.getHeight()>=MyVar.PIC_VALUE_MAX){
			value = MyVar.PIC_VALUE_MAX;
		}else if (finalImageBitmap.getHeight()<MyVar.PIC_VALUE_MAX && finalImageBitmap.getHeight()<=finalImageBitmap.getWidth()) {
			value = finalImageBitmap.getHeight();
		}else if (finalImageBitmap.getWidth()<MyVar.PIC_VALUE_MAX && finalImageBitmap.getWidth()<=finalImageBitmap.getHeight()) {
			value = finalImageBitmap.getWidth();
		}
		try{
			Intent intentCrop = new Intent("com.android.camera.action.CROP");
			intentCrop.setDataAndType(finalImageUri, "image/*");
			//intentCrop.setData(finalImageUri);
			//intentCrop.putExtra("scale", true);
			intentCrop.putExtra("crop", true);
			intentCrop.putExtra("aspectX", 1);
			intentCrop.putExtra("aspectY", 1);
			intentCrop.putExtra("outputX", value);
			intentCrop.putExtra("outputY", value);
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

	private class RegistrarCliente extends AsyncTask<Cliente, Void, Boolean>{
		ProgressDialog pd;
		Cliente cliente;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Registrando cliente...");
			pd.setCancelable(false);
			pd.show();
			
		}
		@Override
		protected Boolean doInBackground(Cliente... params) {
			cliente = params[0];
			if(httpCustom.insertarCliente(this.cliente)){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					if(db.insertarCliente(this.cliente)){
						return true;
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean verificacion){
			super.onPostExecute(verificacion);
			pd.dismiss();
			if(verificacion){
				if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
				}
				Toast.makeText(getActivity(), "Cliente registrado exitosamente", Toast.LENGTH_LONG).show();
				listener.onBackFromNuevoClienteClick();
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se pudo registrar el cliente, intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
		
	private class ActualizarCliente extends AsyncTask<Cliente, Void, Boolean>{
		ProgressDialog pd;
		Cliente cliente;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Actualizando cliente...");
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Cliente... params) {
			cliente = params[0];
			if(httpCustom.actualizarCliente(this.cliente)){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					if(db.actualizarCliente(this.cliente)){
						return true;
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean verificacion){
			super.onPostExecute(verificacion);
			pd.dismiss();
			if(verificacion){
				if(!cliente.getImagen().equals(cliente_edit.getImagen()) && !cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString());
				}
				Toast.makeText(getActivity(), "Datos del cliente actualizados", Toast.LENGTH_LONG).show();
				listener.onBackFromNuevoClienteClick();
			}else{
				listener.onBackFromNuevoClienteClick();
			}
		}
	}
		
	public Parqueo getParqueoOnline(){
		Parqueo parqueo = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			parqueo = db.getParqueo(this.idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parqueo;
	}
	
	public boolean copySaveImageCliente(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		ruta_images.mkdirs();
		
		String name_image_cliente = null;
		if(OPCION_ELEGIDA==MyVar.SELECT_IMAGE){
			name_image_cliente = getNameImageCliente();
		}else if(OPCION_ELEGIDA==MyVar.CAPTURE_IMAGE){
			name_image_cliente = new File(this.pathImageCliente).getName();
		}

		File file_image_cliente = new File(ruta_images, name_image_cliente);
		
		FileOutputStream fos;
		try{
			//Le sacamos el nombre de la imagen para que guarde solo el nombre y no asi toda la ruta
			this.pathImageCliente = file_image_cliente.getName();
			fos = new FileOutputStream(file_image_cliente);
			finalImageBitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			scanImage(file_image_cliente.getAbsolutePath());
			if(file_image_cliente.exists()){
				return true;
			}
		}catch(IOException e){
			e.printStackTrace();
		}	
		return false;
	}
		
	public interface OnBackFromNuevoClienteClickListener{
		public void onBackFromNuevoClienteClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnBackFromNuevoClienteClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnBackFromNuevoClienteClickListener");
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
		
	public String getNameImageCliente(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "client_"+this.idparqueo+"_pic_"+date+".jpg";
		return imageCode;
	}
	
	public String getIDClienteGenerado(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "client_"+date+"_"+this.idparqueo;
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
		etCI.getText().clear();
		etNombre.getText().clear();
		etApellido.getText().clear();
		etCelular.getText().clear();
		etDireccion.getText().clear();
		etEmail.getText().clear();
		tvFecha_nac.setText(R.string.fecha_nac);
		tvFecha_nac.setTag(MyVar.FECHA_DEFAULT);
		ivImageCliente.setImageResource(R.drawable.ic_timer_auto_white_48dp);
		if(rdgSexo.getCheckedRadioButtonId()==rbtnMasculino.getId()){
			rbtnMasculino.setChecked(false);
		}else if (rdgSexo.getCheckedRadioButtonId()==rbtnFemenino.getId()) {
			rbtnFemenino.setChecked(false);
		}
		spinnerTipo.setSelection(0);
		tvFecha_contrato.setText(R.string.fecha_contrato);
		tvFecha_contrato.setTag(getFechaActual());
		this.OPCION_ELEGIDA = 0;
		this.pathImageCliente = MyVar.NO_ESPECIFICADO;
		this.sexo = MyVar.NO_ESPECIFICADO;
		etCI.requestFocus();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long parent) {
		Date fecha_actual =getFechaActual();
		if((position+1)==MyVar.CLIENTE_OCASIONAL){
			lyFechaContrato.setVisibility(View.GONE);
			tipo_cliente = MyVar.CLIENTE_OCASIONAL;
			tvFecha_contrato.setTag(MyVar.FECHA_DEFAULT);
			tvFecha_contrato.setText(R.string.fecha_contrato);
		}else if ((position+1)==MyVar.CLIENTE_CONTRATO_NOCTURNO) {
			if(ACCION==MyVar.ACCION_REGISTRAR_CLIENTE){
				lyFechaContrato.setVisibility(View.VISIBLE);
				tipo_cliente = MyVar.CLIENTE_CONTRATO_NOCTURNO;
				tvFecha_contrato.setTag(fecha_actual);
				tvFecha_contrato.setText(MyVar.FORMAT_FECHA_1.format(fecha_actual));
			}
		}else if((position+1)==MyVar.CLIENTE_CONTRATO_DIURNO){
			if(ACCION==MyVar.ACCION_REGISTRAR_CLIENTE){
				lyFechaContrato.setVisibility(View.VISIBLE);
				tipo_cliente = MyVar.CLIENTE_CONTRATO_DIURNO;
				tvFecha_contrato.setTag(fecha_actual);
				tvFecha_contrato.setText(MyVar.FORMAT_FECHA_1.format(fecha_actual));
			}
		}else if((position+1)==MyVar.CLIENTE_CONTRATO_DIA_COMPLETO){
			if(ACCION==MyVar.ACCION_REGISTRAR_CLIENTE){
				lyFechaContrato.setVisibility(View.VISIBLE);
				tipo_cliente = MyVar.CLIENTE_CONTRATO_DIA_COMPLETO;
				tvFecha_contrato.setTag(fecha_actual);
				tvFecha_contrato.setText(MyVar.FORMAT_FECHA_1.format(fecha_actual));
			}
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	public Date getFechaActual(){
		java.util.Date date = new java.util.Date();
		java.sql.Date fecha = new java.sql.Date(date.getTime());
		return fecha;
	}
}
