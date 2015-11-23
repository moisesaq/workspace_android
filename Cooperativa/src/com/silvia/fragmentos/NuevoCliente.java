package com.silvia.fragmentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogFecha;
import com.silvia.modelo.Cliente;

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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoCliente extends Fragment implements OnClickListener, OnCheckedChangeListener{

	ImageView ivImagenCliente, ivFechaNac, ivFechaReg;
	EditText etCI, etNombre, etApellido, etDireccion, etTelf, etEmail;
	RadioGroup rdgSexo;
	RadioButton rdbtnMasculino, rdbtnFemenino;
	TextView tvFechaNac, tvFechaReg;
	Button btnAceptar;
	
	public int ACCION;
	public String path_imagen = Variables.SIN_ESPECIFICAR;
	public String sexo = Variables.SIN_ESPECIFICAR;
	
	OnBackFromNuevoClienteClickListener listener;
	public boolean CI_DISPONIBLE = false;
	
	public String idcliente;
	public Cliente editar_cliente;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v= inflater.inflate(R.layout.nuevo_cliente, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		ivImagenCliente = (ImageView)v.findViewById(R.id.ivImagenNuevoCliente);
		ivImagenCliente.setOnClickListener(this);
		etCI = (EditText)v.findViewById(R.id.etCINuevoCliente);
		etCI.addTextChangedListener(CIWacher);
		etNombre = (EditText)v.findViewById(R.id.etNombreNuevoCliente);
		etApellido = (EditText)v.findViewById(R.id.etApellidoNuevoCliente);
		etDireccion = (EditText)v.findViewById(R.id.etDireccionNuevoCliente);
		etTelf = (EditText)v.findViewById(R.id.etTelfNuevoCliente);
		etEmail = (EditText)v.findViewById(R.id.etEmailNuevoCliente);
		rdgSexo = (RadioGroup)v.findViewById(R.id.rdgSexoNuevoCliente);
		rdgSexo.setOnCheckedChangeListener(this);
		rdbtnMasculino = (RadioButton)v.findViewById(R.id.rdbMasculinoNuevoCliente);
		rdbtnFemenino = (RadioButton)v.findViewById(R.id.rdbFemeninoNuevoCliente);
		tvFechaNac = (TextView)v.findViewById(R.id.tvFechaNacNuevoCliente);
		ivFechaNac = (ImageView)v.findViewById(R.id.ivFechaNacNuevoCliente);
		ivFechaNac.setOnClickListener(this);
		tvFechaReg = (TextView)v.findViewById(R.id.tvFechaRegNuevoCliente);
		ivFechaReg = (ImageView)v.findViewById(R.id.ivFechaRegNuevoCliente);
		ivFechaReg.setOnClickListener(this);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarNuevoCliente);
		btnAceptar.setOnClickListener(this);
		Bundle cajon = getArguments();
		this.ACCION=cajon.getInt("accion");
		if(ACCION==Variables.ACCION_NUEVO_CLIENTE){
			actionBar.setTitle("Nuevo cliente");
			tvFechaNac.setTag(Variables.FECHA_DEFAULT);
			java.sql.Date fecha_ingreso = getFechaActual();
			tvFechaReg.setTag(fecha_ingreso);
			tvFechaReg.setText(Variables.FORMAT_FECHA_1.format(fecha_ingreso));
		}else if(ACCION==Variables.ACCION_EDITAR_CLIENTE){
			actionBar.setTitle("Modificar cliente");
			this.idcliente = cajon.getString("idcliente");
			btnAceptar.setText("Listo");
			cargarDatosClienteParaEditar();
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
			if(ACCION==Variables.ACCION_NUEVO_CLIENTE){
				if(s.length()==7 || s.length()==8 || s.length()==10){
					if(verificarDisponibilidadCI(s.toString())){
						CI_DISPONIBLE = true;
						etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
					}else{
						CI_DISPONIBLE = false;
						etCI.requestFocus();
						etCI.setError("Este CI ya existe");
						etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
					}
				}else{
					CI_DISPONIBLE = false;
					etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(ACCION==Variables.ACCION_EDITAR_CLIENTE){
				if(!editar_cliente.getCi().equals(s.toString())){
					if(s.length()==7 || s.length()==8 || s.length()==10){
						if(verificarDisponibilidadCI(s.toString())){
							CI_DISPONIBLE = true;
							etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
						}else{
							CI_DISPONIBLE = false;
							etCI.requestFocus();
							etCI.setError("Este CI ya existe");
							etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
						}
					}else{
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
	
	public boolean verificarDisponibilidadCI(String ci){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(!db.existeCICliente(ci)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void cargarDatosClienteParaEditar() {
		cargarCliente();
		if(!editar_cliente.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(editar_cliente.getImagen()).toString();
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
		this.path_imagen = editar_cliente.getImagen();
		etCI.setText(editar_cliente.getCi());
		etNombre.setText(editar_cliente.getNombre());
		if(!editar_cliente.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			etApellido.setText(editar_cliente.getApellido());
		}
		if(!editar_cliente.getDireccion().equals(Variables.SIN_ESPECIFICAR)){
			etDireccion.setText(editar_cliente.getDireccion());
		}
		if(editar_cliente.getTelf()!=0){
			etTelf.setText(String.valueOf(editar_cliente.getTelf()));
		}
		if(!editar_cliente.getEmail().equals(Variables.SIN_ESPECIFICAR)){
			etEmail.setText(editar_cliente.getEmail());
		}
		
		if(editar_cliente.getSexo().equals(Variables.SEXO_MASCULINO)){
			rdbtnMasculino.setChecked(true);
		}else if (editar_cliente.getSexo().equals(Variables.SEXO_FEMENINO)) {
			rdbtnFemenino.setChecked(true);
		}
		this.sexo = editar_cliente.getSexo();
		if(!editar_cliente.getFechaNac().equals(Variables.FECHA_DEFAULT)){
			tvFechaNac.setText(Variables.FORMAT_FECHA_1.format(editar_cliente.getFechaNac()));
			tvFechaNac.setTag(editar_cliente.getFechaNac());
		}else{
			tvFechaNac.setTag(Variables.FECHA_DEFAULT);
		}
		tvFechaReg.setText(Variables.FORMAT_FECHA_1.format(editar_cliente.getFechaReg()));
		tvFechaReg.setTag(editar_cliente.getFechaReg());
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==ivImagenCliente.getId()){
			selectImage();
		}else if (v.getId()==ivFechaNac.getId()) {
			DialogFecha dFecha1 = new DialogFecha(tvFechaNac);
			dFecha1.show(getFragmentManager(), "tagDF1");
		}else if (v.getId()==ivFechaReg.getId()) {
			DialogFecha dFecha2 = new DialogFecha(tvFechaReg);
			dFecha2.show(getFragmentManager(), "tagDF2");
		}else if (v.getId()==btnAceptar.getId()) {
			if(ACCION==Variables.ACCION_NUEVO_CLIENTE){
				registrarCliente();
			}else if (ACCION==Variables.ACCION_EDITAR_CLIENTE) {
				modificarCliente();
			}
		}
	}
	
	public void registrarCliente(){
		String ci = etCI.getText().toString().trim();
		String nombre = etNombre.getText().toString().trim();
		String apellido = etApellido.getText().toString().trim();
		if(apellido.equals("")){
			apellido = Variables.SIN_ESPECIFICAR;
		}
		String direccion = etDireccion.getText().toString().trim();
		if(direccion.equals("")){
			direccion = Variables.SIN_ESPECIFICAR;
		}
		int telf = 0;
		if(!etTelf.getText().toString().equals("")){
			telf = Integer.parseInt(etTelf.getText().toString().trim());
		}
		String email = etEmail.getText().toString().trim();
		if(email.equals("")){
			email = Variables.SIN_ESPECIFICAR;
		}
		Date fecha_nac = (Date)tvFechaNac.getTag();
		Date fecha_reg = (Date)tvFechaReg.getTag();
		
		if(ci.length()==7 || ci.length()==8 || ci.length()==10){
			if(nombre.length()>=3){
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(CI_DISPONIBLE){
						String idcliente = generarIdCliente();
						if(ivImagenCliente.getTag()!=null){
							String name_image_cliente = new StringBuilder("pic-").append(idcliente).append(".jpg").toString();
							savedImage(name_image_cliente);
						}
						Cliente nuevo_cliente = new Cliente(idcliente, ci, nombre, apellido, direccion, telf, email, this.sexo, 
															fecha_nac, fecha_reg, path_imagen, Variables.NO_ELIMINADO);
						
						if(db.insertarCliente(nuevo_cliente)){
							Toast.makeText(getActivity(), "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
							listener.onBackFromNuevoClienteClick();
						}else{
							Toast.makeText(getActivity(), "No se pudo registrar cliente", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getActivity(), "Carnet de Identidad no disponible", Toast.LENGTH_SHORT).show();
						etCI.requestFocus();
						etCI.setError("CI ya existe, introduzca otro");
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else{
				etNombre.requestFocus();
				etNombre.setError("Introduzca nombre, minimo 3 caracteres");
			}
		}else{
			etCI.requestFocus();
			etCI.setError("CI o NIT debe tener 7, 8 o 10 caracteres");
		}
	}
	
	public void modificarCliente(){
		String ci = etCI.getText().toString().trim();
		String nombre = etNombre.getText().toString().trim();
		String apellido = etApellido.getText().toString().trim();
		if(apellido.equals("")){
			apellido = Variables.SIN_ESPECIFICAR;
		}
		String direccion = etDireccion.getText().toString().trim();
		if(direccion.equals("")){
			direccion = Variables.SIN_ESPECIFICAR;
		}
		int telf = 0;
		if(!etTelf.getText().toString().equals("")){
			telf = Integer.parseInt(etTelf.getText().toString().trim());
		}
		String email = etEmail.getText().toString().trim();
		if(email.equals("")){
			email = Variables.SIN_ESPECIFICAR;
		}
		Date fecha_nac = (Date)tvFechaNac.getTag();
		Date fecha_reg = (Date)tvFechaReg.getTag();
		
		if(ci.length()==7 || ci.length()==8 || ci.length()==10){
			if(nombre.length()>=3){
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(CI_DISPONIBLE){
						if(ivImagenCliente.getTag()!=null){
							String name_image_cliente = new StringBuilder("pic-").append(generarIdCliente()).append(".jpg").toString();
							savedImage(name_image_cliente);
						}
						Cliente cliente = new Cliente(editar_cliente.getIdcliente(), ci, nombre, apellido, direccion, telf, email, this.sexo, 
															fecha_nac, fecha_reg, path_imagen, Variables.NO_ELIMINADO);
						
						if(db.modificarCliente(cliente)){
							Toast.makeText(getActivity(), "Cliente modificado exitosamente", Toast.LENGTH_SHORT).show();
							listener.onBackFromNuevoClienteClick();
						}else{
							Toast.makeText(getActivity(), "No se pudo modificar cliente", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getActivity(), "Carnet de Identidad no disponible", Toast.LENGTH_SHORT).show();
						etCI.requestFocus();
						etCI.setText("CI ya existe, introduzca otro");
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else{
				etNombre.requestFocus();
				etNombre.setText("Introduzca nombre, minimo 3 caracteres");
			}
		}else{
			etCI.requestFocus();
			etCI.setError("CI o NIT debe tener 7, 8 o 10 caracteres");
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkId) {
		if(group.getId()==rdgSexo.getId()){
			if(checkId==rdbtnMasculino.getId()){
				sexo = Variables.SEXO_MASCULINO;
			}else if (checkId==rdbtnFemenino.getId()) {
				sexo = Variables.SEXO_FEMENINO;
			}
		}
	}
	
	public void limpiarCampos(){
		ivImagenCliente.setImageResource(R.drawable.ic_person_white_48dp);
		this.path_imagen = Variables.SIN_ESPECIFICAR;
		etCI.getText().clear();
		etNombre.getText().clear();
		etApellido.getText().clear();
		etDireccion.getText().clear();
		etTelf.getText().clear();
		etEmail.getText().clear();
		tvFechaNac.setTag(Variables.FECHA_DEFAULT);
		tvFechaNac.setText(Variables.SIN_ESPECIFICAR);
		tvFechaReg.setTag(getFechaActual());
		tvFechaReg.setText(Variables.FORMAT_FECHA_1.format(getFechaActual()));
		if(rdgSexo.getCheckedRadioButtonId()==rdbtnMasculino.getId()){
			rdbtnMasculino.setChecked(false);
		}else if (rdgSexo.getCheckedRadioButtonId()==rdbtnFemenino.getId()) {
			rdbtnFemenino.setChecked(false);
		}
	}
	
	public Date getFechaActual(){
		java.util.Date date = new java.util.Date();
		Date fecha = new Date(date.getTime());
		return fecha;
	}
	
	public String generarIdCliente(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "cli-"+date;
		return imageCode;
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
	
	public void cargarCliente(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			editar_cliente = db.getCliente(this.idcliente);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
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
				this.ivImagenCliente.setImageBitmap(imageBitmap);
				this.ivImagenCliente.setTag(imageBitmap);
				this.ivImagenCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
				this.ivImagenCliente.setPadding(0, 0, 0, 0);
			}
		}
	}
	
	public void savedImage(String name_image){
		File ruta_images = new File(Variables.FOLDER_IMAGES_COOPERATIVA);
		ruta_images.mkdirs();
		
		File file_image = new File(ruta_images, name_image);
		
		try {
			FileOutputStream fos = new FileOutputStream(file_image);
			Bitmap bitmap = (Bitmap)ivImagenCliente.getTag();
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
