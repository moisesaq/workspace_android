package com.silvia.fragmentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogAsignarMaq;
import com.silvia.dialogos.DialogFecha;
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoPersonal extends Fragment implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener{
	
	LinearLayout lyVistaCargo, lyVistaMaq;
	CheckBox cbMaquinaria;
	ImageView ivImagenPersonal, ivFechaNac, ivFechaIngreso, ivImagenMaq, ivEditarCargo;
	EditText etCI, etNombre, etApellido, etDireccion, etTelf, etEmail;
	TextView tvFechaNac, tvFechaIngreso, tvPlacaMaq, tvDescripcionMaq, tvCargo;
	Spinner spCargo;
	Button btnAceptar;
	
	private String path_imagen = Variables.SIN_ESPECIFICAR;
	private String idcargo = Variables.SIN_ESPECIFICAR;
	
	private int ACCION;
	String idpersonal;
	private Personal editar_personal;
	
	OnBackFromNuevoPersonalClickListener listener;
	public boolean CI_DISPONIBLE = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.nuevo_personal, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		ivImagenPersonal = (ImageView)v.findViewById(R.id.ivImagenNuevoPersonal);
		ivImagenPersonal.setOnClickListener(this);
		etCI = (EditText)v.findViewById(R.id.etCINuevoPersonal);
		etCI.addTextChangedListener(CIWacher);
		etNombre = (EditText)v.findViewById(R.id.etNombreNuevoPersonal);
		etApellido = (EditText)v.findViewById(R.id.etApellidoNuevoPersonal);
		etDireccion = (EditText)v.findViewById(R.id.etDireccionNuevoPersonal);
		etTelf = (EditText)v.findViewById(R.id.etTelfNuevoPersonal);
		etEmail = (EditText)v.findViewById(R.id.etEmailNuevoPersonal);
		tvFechaNac = (TextView)v.findViewById(R.id.tvFechaNanNuevoPersonal);
		ivFechaNac = (ImageView)v.findViewById(R.id.ivFechaNacNuevoPersonal);
		ivFechaNac.setOnClickListener(this);
		tvFechaIngreso = (TextView)v.findViewById(R.id.tvFechaIngresoNuevoPersonal);
		ivFechaIngreso = (ImageView)v.findViewById(R.id.ivFechaIngresoNuevoPersonal);
		ivFechaIngreso.setOnClickListener(this);
		lyVistaCargo = (LinearLayout)v.findViewById(R.id.lyVistaCargoNuevoPersonal);
		tvCargo = (TextView)v.findViewById(R.id.tvOcupacionCargoNuevoPersonal);
		ivEditarCargo = (ImageView)v.findViewById(R.id.ivEditarCargoNuevoPersonal);
		ivEditarCargo.setOnClickListener(this);
		spCargo = (Spinner)v.findViewById(R.id.spCargoNuevoPersonal);
		spCargo.setOnItemSelectedListener(this);
		cbMaquinaria = (CheckBox)v.findViewById(R.id.cbMaquinariaNuevoPersonal);
		cbMaquinaria.setOnCheckedChangeListener(this);
		lyVistaMaq = (LinearLayout)v.findViewById(R.id.lyVistaMaqNuevoPersonal);
		ivImagenMaq = (ImageView)v.findViewById(R.id.ivMaquinariaNuevoPersonal);
		ivImagenMaq.setOnClickListener(this);
		tvPlacaMaq = (TextView)v.findViewById(R.id.tvPlacaMaqNuevoPersonal);
		tvDescripcionMaq = (TextView)v.findViewById(R.id.tvDescripcionMaqNuevoPersonal);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarNuevoPersonal);
		btnAceptar.setOnClickListener(this);
		cargarSpinnerCargo();
		Bundle cajon = getArguments();
		this.ACCION = cajon.getInt("accion");
		if(ACCION==Variables.ACCION_NUEVO_PERSONAL){
			actionBar.setTitle("Nuevo personal");
			tvFechaNac.setTag(Variables.FECHA_DEFAULT);
			java.sql.Date fecha_ingreso = getFechaActual();
			tvFechaIngreso.setTag(fecha_ingreso);
			tvFechaIngreso.setText(Variables.FORMAT_FECHA_1.format(fecha_ingreso));
			tvPlacaMaq.setTag(Variables.ID_MAQ_DEFAULT);
		}else if(ACCION==Variables.ACCION_EDITAR_PERSONAL){
			actionBar.setTitle("Modificar personal");
			this.idpersonal = cajon.getString("idpersonal");
			btnAceptar.setText("Listo");
			cargarDatosPersonalParaEditar();
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
			if(ACCION==Variables.ACCION_NUEVO_PERSONAL){
				if(s.length()==7 || s.length()==8){
					if(verificarDisponibilidadCI(s.toString())){
						CI_DISPONIBLE = true;
						etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
					}else{
						CI_DISPONIBLE = false;
						etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
					}
				}else{
					CI_DISPONIBLE = false;
					etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(ACCION==Variables.ACCION_EDITAR_PERSONAL){
				if(!editar_personal.getCi().equals(s.toString())){
					if(s.length()==7 || s.length()==8){
						if(verificarDisponibilidadCI(s.toString())){
							CI_DISPONIBLE = true;
							etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
						}else{
							CI_DISPONIBLE = false;
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
			if(!db.existeCIPersonal(ci)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void cargarDatosPersonalParaEditar() {
		cargarPersonal();
		if(!editar_personal.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(editar_personal.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
			if(bitmap!=null){
				ivImagenPersonal.setImageBitmap(bitmap);
				ivImagenPersonal.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagenPersonal.setPadding(0, 0, 0, 0);
			}else{
				ivImagenPersonal.setImageResource(R.drawable.ic_person_white_48dp);
			}
		}else{
			ivImagenPersonal.setImageResource(R.drawable.ic_person_white_48dp);
		}
		this.path_imagen = editar_personal.getImagen();
		etCI.setText(editar_personal.getCi());
		etNombre.setText(editar_personal.getNombre());
		if(!editar_personal.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			etApellido.setText(editar_personal.getApellido());
		}
		if(!editar_personal.getDireccion().equals(Variables.SIN_ESPECIFICAR)){
			etDireccion.setText(editar_personal.getDireccion());
		}
		if(editar_personal.getTelf()!=0){
			etTelf.setText(String.valueOf(editar_personal.getTelf()));
		}
		if(!editar_personal.getEmail().equals(Variables.SIN_ESPECIFICAR)){
			etEmail.setText(editar_personal.getEmail());
		}
		if(!editar_personal.getFecha_nac().equals(Variables.FECHA_DEFAULT)){
			tvFechaNac.setText(Variables.FORMAT_FECHA_1.format(editar_personal.getFecha_nac()));
			tvFechaNac.setTag(editar_personal.getFecha_nac());
		}else{
			tvFechaNac.setTag(Variables.FECHA_DEFAULT);
		}
		tvFechaIngreso.setText(Variables.FORMAT_FECHA_1.format(editar_personal.getFecha_ingreso()));
		tvFechaIngreso.setTag(editar_personal.getFecha_ingreso());
		
		lyVistaCargo.setVisibility(View.VISIBLE);
		spCargo.setVisibility(View.GONE);
		tvCargo.setText(getCargo(editar_personal.getIdcargo()).getOcupacion());
		this.idcargo = editar_personal.getIdcargo();
		
		Maquinaria maq = getMaquinaria(editar_personal.getIdmaquinaria());
		if(maq.getIdmaquinaria().equals(Variables.ID_MAQ_DEFAULT)){
			tvPlacaMaq.setTag(maq.getIdmaquinaria()); //o personal.getIdMaquinaria();
		}else{
			cbMaquinaria.setChecked(true);
			if(!maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
				String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString();
				Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
				if(bitmap!=null){
					ivImagenMaq.setImageBitmap(bitmap);
					ivImagenMaq.setScaleType(ImageView.ScaleType.CENTER_CROP);
					ivImagenMaq.setPadding(0, 0, 0, 0);
				}else{
					ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
				}
			}else{
				ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
			tvPlacaMaq.setText(maq.getPlaca());
			tvPlacaMaq.setTag(maq.getIdmaquinaria());
			tvDescripcionMaq.setVisibility(View.VISIBLE);
			tvDescripcionMaq.setText(maq.getDescripcion());
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==ivImagenPersonal.getId()){
			selectImage();
		}else if (v.getId()==ivFechaNac.getId()) {
			DialogFecha dFecha1 = new DialogFecha(tvFechaNac);
			dFecha1.show(getFragmentManager(), "tagDF1");
		}else if (v.getId()==ivFechaIngreso.getId()) {
			DialogFecha dFecha2 = new DialogFecha(tvFechaIngreso);
			dFecha2.show(getFragmentManager(), "tagDF2");
		}else if (v.getId()==ivEditarCargo.getId()) {
			if(spCargo.getVisibility()==View.GONE){
				spCargo.setVisibility(View.VISIBLE);
			}else if(spCargo.getVisibility()==View.VISIBLE){
				spCargo.setVisibility(View.GONE);
			}
		}else if (v.getId()==ivImagenMaq.getId()) {
			DialogAsignarMaq dAsignarMaq = new DialogAsignarMaq(ivImagenMaq, tvPlacaMaq, tvDescripcionMaq);
			dAsignarMaq.show(getFragmentManager(), "tagDAMaq");
		}else if (v.getId()==btnAceptar.getId()) {
			if(ACCION==Variables.ACCION_EDITAR_PERSONAL){
				modificarPersonal();
			}else{
				registrarPersonal();
			}
			
		}
	}
	
	public void cargarSpinnerCargo(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Cargo> lista = db.getTodosLosCargos();
			if(lista.size()!=0 && lista!=null){
				ArrayAdapter<Cargo> adapter = new ArrayAdapter<Cargo>(getActivity(), android.R.layout.simple_spinner_item, lista);
				spCargo.setAdapter(adapter);
			}else{
				//listener.onBackFromNuevoPersonalClick();
				Toast toast = Toast.makeText(getActivity(), "No se encontro ningun cargo por favor registre por lo menos uno", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
				spCargo.setVisibility(View.GONE);
				btnAceptar.setVisibility(View.GONE);
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void registrarPersonal(){
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
		Date fecha_ingreso = (Date)tvFechaIngreso.getTag();
		String idmaquinaria = (String)tvPlacaMaq.getTag();
		
		if(ci.length()>=7){
			if(nombre.length()>=3){
				if(cbMaquinaria.isChecked() && !idmaquinaria.equals(Variables.ID_MAQ_DEFAULT) || 
						!cbMaquinaria.isChecked() && idmaquinaria.equals(Variables.ID_MAQ_DEFAULT)){
					if(!idcargo.equals(Variables.SIN_ESPECIFICAR)){
						DBDuraznillo db = new DBDuraznillo(getActivity());
						try {
							db.abrirDB();
							//Toast.makeText(getActivity(), "IDMaq-"+idmaquinaria+" IDCargo-"+this.idcargo, Toast.LENGTH_SHORT).show();
							if(CI_DISPONIBLE){
								String idpersonal = generarIdPersonal();
								if(ivImagenPersonal.getTag()!=null){
									String name_image_personal = new StringBuilder("pic-").append(idpersonal).append(".jpg").toString();
									savedImage(name_image_personal);
								}
								Personal pers = new Personal(idpersonal, ci, nombre, apellido, direccion, telf, email, fecha_nac, fecha_ingreso, 
																	path_imagen, Variables.NO_ELIMINADO, idcargo, idmaquinaria);
								if(db.insertarPersonal(pers)){
									Toast.makeText(getActivity(), "Personal registrado exitosamente", Toast.LENGTH_SHORT).show();
									//limpiarCampos();
									listener.onBackFromNuevoPersonalClick();
								}else{
									Toast.makeText(getActivity(), "No se pudo registrar personal", Toast.LENGTH_SHORT).show();
								}
							}else{
								etCI.requestFocus();
								etCI.setError("CI ya existe, introduzca otro");
								etCI.getText().clear();
							}
							db.cerrarDB();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						Toast.makeText(getActivity(), "Seleccione un cargo por favor", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), "Seleccione una maquinaria porfavor", Toast.LENGTH_SHORT).show();
				}
			}else{
				etNombre.requestFocus();
				etNombre.setError("Nombre es necesario debe tener minimo 3 caracteres");
			}
		}else{
			etCI.requestFocus();
			etCI.setError("CI debe tener minimo 7 digitos");
		}
	}
	
	public void modificarPersonal(){
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
		Date fecha_ingreso = (Date)tvFechaIngreso.getTag();
		String idmaquinaria = (String)tvPlacaMaq.getTag();
		
		if(ci.length()>=7){
			if(nombre.length()>=3){
				if(cbMaquinaria.isChecked() && !idmaquinaria.equals(Variables.ID_MAQ_DEFAULT) || 
						!cbMaquinaria.isChecked() && idmaquinaria.equals(Variables.ID_MAQ_DEFAULT)){
					DBDuraznillo db = new DBDuraznillo(getActivity());
					try {
						db.abrirDB();
						if(CI_DISPONIBLE){
							if(ivImagenPersonal.getTag()!=null){
								String name_image_personal = new StringBuilder("pic-").append(generarIdPersonal()).append(".jpg").toString();
								savedImage(name_image_personal);
							}
							Personal pers = new Personal(editar_personal.getIdpersonal(), ci, nombre, apellido, direccion, telf, email, fecha_nac, 
															fecha_ingreso,	path_imagen, Variables.NO_ELIMINADO, idcargo, idmaquinaria);
							
							if(db.modificarPersonal(pers)){
								Toast.makeText(getActivity(), "Personal modificado exitosamente", Toast.LENGTH_SHORT).show();
								//limpiarCampos();
								listener.onBackFromNuevoPersonalClick();
							}else{
								Toast.makeText(getActivity(), "No se pudo modificar personal", Toast.LENGTH_SHORT).show();
							}
						}else{
							etCI.requestFocus();
							etCI.setError("CI ya existe, introduzca otro");
							etCI.getText().clear();
						}
						db.cerrarDB();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Toast.makeText(getActivity(), "Seleccione una maquinaria porfavor", Toast.LENGTH_SHORT).show();
				}
			}else{
				etNombre.requestFocus();
				etNombre.setError("Nombre es necesario debe tener minimo 3 caracteres");
			}
		}else{
			etCI.requestFocus();
			etCI.setError("CI debe tener minimo 7 digitos");
		}
	}
	
	public String generarIdPersonal(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "per-"+date;
		return imageCode;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long parent) {
		Cargo cargo = (Cargo)adapter.getAdapter().getItem(position);
		this.idcargo = cargo.getIdcargo();
		//Toast.makeText(getActivity(), "Cargo-"+cargo.getOcupacion(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	public Date getFechaActual(){
		java.util.Date date = new java.util.Date();
		Date fecha = new Date(date.getTime());
		return fecha;
	}
	
	public void cargarPersonal(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			editar_personal = db.getPersonal(idpersonal);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cargo getCargo(String idcargo){
		Cargo cargo = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			cargo = db.getCargo(idcargo);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cargo;
	}
	
	public Maquinaria getMaquinaria(String idmaq){
		Maquinaria maq = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			maq = db.getMaquinaria(idmaq);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maq;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getId()==cbMaquinaria.getId()){
			if(cbMaquinaria.isChecked()){
				lyVistaMaq.setVisibility(View.VISIBLE);
			}else{
				lyVistaMaq.setVisibility(View.GONE);
				tvPlacaMaq.setTag(Variables.ID_MAQ_DEFAULT);
				tvPlacaMaq.setText("Seleccione vehiculo");
				tvDescripcionMaq.setVisibility(View.GONE);
				ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
		}
	}
	
	public void limpiarCampos(){
		ivImagenPersonal.setImageResource(R.drawable.ic_person_white_48dp);
		this.path_imagen = Variables.SIN_ESPECIFICAR;
		etCI.getText().clear();
		etNombre.getText().clear();
		etApellido.getText().clear();
		etDireccion.getText().clear();
		etTelf.getText().clear();
		etEmail.getText().clear();
		tvFechaNac.setTag(Variables.FECHA_DEFAULT);
		tvFechaNac.setText("Fecha de nacimiento");
		tvFechaIngreso.setTag(getFechaActual());
		tvFechaIngreso.setText(Variables.FORMAT_FECHA_1.format(getFechaActual()));
		spCargo.setSelection(0);
		cbMaquinaria.setChecked(false);
	}
	
	public interface OnBackFromNuevoPersonalClickListener{
		public void onBackFromNuevoPersonalClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnBackFromNuevoPersonalClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnBackFromNuevoPersonalClickListener");
		}
	}

	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		//intent.setAction(Intent.ACTION_GET_CONTENT);
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
				this.ivImagenPersonal.setImageBitmap(imageBitmap);
				this.ivImagenPersonal.setTag(imageBitmap);
				this.ivImagenPersonal.setScaleType(ImageView.ScaleType.CENTER_CROP);
				this.ivImagenPersonal.setPadding(0, 0, 0, 0);
			}
		}
	}
	
	public void savedImage(String name_image){
		File ruta_images = new File(Variables.FOLDER_IMAGES_COOPERATIVA);
		ruta_images.mkdirs();
		
		File file_image = new File(ruta_images, name_image);
		
		try {
			FileOutputStream fos = new FileOutputStream(file_image);
			Bitmap bitmap = (Bitmap)ivImagenPersonal.getTag();
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
