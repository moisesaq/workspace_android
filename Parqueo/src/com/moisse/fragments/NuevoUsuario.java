package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogFecha;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.dialogs.DialogOptionImageUser;
import com.moisse.modelo.Usuario;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;
import com.moisse.others.UploadImage;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;;

public class NuevoUsuario extends Fragment implements OnClickListener{

	public ImageView ivImagenUsuario;
	private EditText etCI, etNombre, etApellido, etUsuario, etClave, etClaveRepetir, etCelular, etDireccion, etE_mail;
	private TextView tvFecha_nac;
	private LinearLayout lyVistaFechaNac, lyVistaSexo;
	private RadioGroup rdgSexo;
	private RadioButton rbtnMasculino, rbtnFemenino;
	private Button btnGuardar;
	private ImageButton ibtnFechaNac;
	protected ProgressBar pbVerificarCI, pbVerificarUser;
	
	OnPerfilClickListener listener;
	private int CARGO;	
	private int ACCION;
	public int OPCION_ELEGIDA = 0; 
	public String pathImageUsuario = MyVar.NO_ESPECIFICADO;
	
	private String idparqueo;
	private String idusuario;
	private Usuario user_online;
	
	public HttpClientCustom httpCustom = new HttpClientCustom();
	private Uri finalImageUri;
	private Bitmap finalImageBitmap;
	
	public boolean CI_DISPONIBLE = false;
	public boolean NOMBRE_USUARIO_DISPONIBLE = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nuevo_usuario, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(view);
		return view;
	}
	
	private void inicializarComponentes(View view) {
		ivImagenUsuario = (ImageView)view.findViewById(R.id.ivImageUsuario);
		ivImagenUsuario.setOnClickListener(this);
		pbVerificarCI = (ProgressBar)view.findViewById(R.id.pbVericarCIUsuario);
		etCI = (EditText)view.findViewById(R.id.etCIUsuario);
		etCI.addTextChangedListener(CIUsuarioWacher);
		etNombre = (EditText)view.findViewById(R.id.etNombreUsuario);
		etApellido = (EditText)view.findViewById(R.id.etApellidoUsuario);
		pbVerificarUser = (ProgressBar)view.findViewById(R.id.pbVericarNombreUsuario);
		etUsuario = (EditText)view.findViewById(R.id.etUserUsuario);
		etUsuario.addTextChangedListener(NombreUsuarioWacher);
		etClave = (EditText)view.findViewById(R.id.etClaveUsuario);
		etClaveRepetir = (EditText)view.findViewById(R.id.etClaveRepetirUsuario);
		etCelular = (EditText)view.findViewById(R.id.etCelularUsuario);
		etDireccion = (EditText)view.findViewById(R.id.etDireccionUsuario);
		etE_mail = (EditText)view.findViewById(R.id.etEmailUsuario);
		lyVistaFechaNac = (LinearLayout)view.findViewById(R.id.lyVistaFechaNacUsuario);
		tvFecha_nac = (TextView)view.findViewById(R.id.tvFechaNacUsuario);
		ibtnFechaNac = (ImageButton)view.findViewById(R.id.ibtnCalendarioUsuario);
		ibtnFechaNac.setOnClickListener(this);
		lyVistaSexo = (LinearLayout)view.findViewById(R.id.lyVistaSexoUsuario);
		rdgSexo = (RadioGroup)view.findViewById(R.id.rdgSexoUsuario);
		rbtnMasculino = (RadioButton)view.findViewById(R.id.rdbMasculinoUsuario);
		rbtnFemenino = (RadioButton)view.findViewById(R.id.rdbFemeninoUsuario);
		btnGuardar = (Button)view.findViewById(R.id.btnGuardarUsuario);
		btnGuardar.setOnClickListener(this);
		
		ActionBar actionB = getActivity().getActionBar();
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		Bundle bundle = getArguments();
		this.idparqueo = bundle.getString("idparqueo");
		this.ACCION = bundle.getInt("accion");
		
		if(ACCION == MyVar.ACCION_REGISTRAR_ADMIN){
			this.CARGO = MyVar.CARGO_ADMIN;
			actionB.setTitle("Datos administrador");
		}else if(ACCION == MyVar.ACCION_REGISTRAR_USUARIO){ 
			this.CARGO = MyVar.CARGO_USUARIO;
			btnGuardar.setText(new StringBuilder("Aceptar"));
			actionB.setTitle("Nuevo usuario");
			actionB.setDisplayHomeAsUpEnabled(true);
			actionB.setHomeButtonEnabled(true);
		}else if(ACCION == MyVar.ACCION_EDITAR_PERFIL){
			etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			this.NOMBRE_USUARIO_DISPONIBLE = true;
			cargarDatosUsuarioParaEditar(bundle);
			actionB.setTitle("Editar mi perfil");
		}
	}
	
	private void cargarDatosUsuarioParaEditar(Bundle bundle) {
		this.idusuario = bundle.getString("idusuario");
		this.user_online = getUserOnline(idusuario);
		if(!user_online.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			ivImagenUsuario.setImageBitmap(BitmapFactory.decodeFile(MyVar.FOLDER_IMAGES_PARQUEO+user_online.getImagen()));
		}else{
			ivImagenUsuario.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
		}
		this.pathImageUsuario = user_online.getImagen();
		etCI.setText(new StringBuilder().append(user_online.getCi()));
		//etCI.setEnabled(false);
		etNombre.setText(new StringBuilder(user_online.getNombre()));
		etApellido.setVisibility(View.VISIBLE);
		if(!user_online.getApellido().equals(MyVar.NO_ESPECIFICADO)){
			etApellido.setText(new StringBuilder(user_online.getApellido()));
		}
		etUsuario.setVisibility(View.GONE);
		etClave.setVisibility(View.GONE);
		etClaveRepetir.setVisibility(View.GONE);
		etCelular.setVisibility(View.VISIBLE);
		if(user_online.getCelular()!=0){
			etCelular.setText(new StringBuilder().append(user_online.getCelular()));
		}
		etDireccion.setVisibility(View.VISIBLE);
		if(!user_online.getDireccion().equals(MyVar.NO_ESPECIFICADO)){
			etDireccion.setText(new StringBuilder(user_online.getDireccion()));
		}
		etE_mail.setVisibility(View.VISIBLE);
		if(!user_online.getEmail().equals(MyVar.NO_ESPECIFICADO)){
			etE_mail.setText(new StringBuilder(user_online.getEmail()));
		}
		lyVistaFechaNac.setVisibility(View.VISIBLE);
		if(!user_online.getFecha_nac().equals(MyVar.FECHA_DEFAULT)){
			tvFecha_nac.setText(MyVar.FORMAT_FECHA_1.format(user_online.getFecha_nac()));
		}
		lyVistaSexo.setVisibility(View.VISIBLE);
		tvFecha_nac.setTag(user_online.getFecha_nac());
		rdgSexo.setVisibility(View.VISIBLE);
		if(user_online.getSexo().equals("Masculino")){
			rdgSexo.check(R.id.rdbMasculinoUsuario);
		}else if (user_online.getSexo().equals("Femenino")) {
			rdgSexo.check(R.id.rdbFemeninoUsuario);
		}
		btnGuardar.setText(new StringBuilder("Listo"));
	}
	
	public Usuario getUserOnline(String idusuario){
		DBParqueo db = new DBParqueo(getActivity());
		Usuario user_activo = null;
		try {
			db.openSQLite();
			user_activo = db.getUsuario(idusuario);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user_activo;
	}

	private final TextWatcher CIUsuarioWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			VerifyCIUsuario verify = new VerifyCIUsuario();
			if(ACCION==MyVar.ACCION_REGISTRAR_ADMIN || ACCION==MyVar.ACCION_REGISTRAR_USUARIO){
				if(s.length()==7 || s.length()==8){
					verify.execute(s.toString());
				}else{
					verify.cancel(true);
					CI_DISPONIBLE = false;
					etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}else if(ACCION==MyVar.ACCION_EDITAR_PERFIL){
				if(!user_online.getCi().equals(s.toString())){
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
	
	private final TextWatcher NombreUsuarioWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			VerifyNombreUsuario verify = new VerifyNombreUsuario();
			if(ACCION==MyVar.ACCION_REGISTRAR_ADMIN || ACCION==MyVar.ACCION_REGISTRAR_USUARIO){
				if(s.length()>=4 && s.length()<=10){
					verify.execute(s.toString());
				}else{
					verify.cancel(true);
					NOMBRE_USUARIO_DISPONIBLE = false;
					etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}
			}			
		}
	};
	
	private class VerifyCIUsuario extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pbVerificarCI.setVisibility(View.VISIBLE);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String ci_usuario = params[0];		
			if(httpCustom.verificarDisponibilidadCIUsuario(idparqueo, ci_usuario)){
				return true;
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pbVerificarCI.setVisibility(View.GONE);
			if(result){
				etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
				CI_DISPONIBLE = true;
			}else{
				etCI.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				CI_DISPONIBLE = false;
			}
		}
	}
	
	private class VerifyNombreUsuario extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pbVerificarUser.setVisibility(View.VISIBLE);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String nombre_usuario = params[0];		
			if(httpCustom.verificarDisponibilidadNombreUsuario(nombre_usuario)){
				return true;
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pbVerificarUser.setVisibility(View.GONE);
			if(result){
				etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
				NOMBRE_USUARIO_DISPONIBLE = true;
			}else{
				etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				NOMBRE_USUARIO_DISPONIBLE = false;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId()==btnGuardar.getId()) {
			if(ACCION == MyVar.ACCION_REGISTRAR_ADMIN || ACCION == MyVar.ACCION_REGISTRAR_USUARIO){
				validarIniciarRegistroUsuario();
			}else{
				validarIniciarActualizacionUsuario();
			}
		}else if (v.getId()==ivImagenUsuario.getId()) {
			DialogOptionImageUser dou = new DialogOptionImageUser(this);
			dou.show(getFragmentManager(), "tagDOptionImagen");
		}else if (v.getId()==ibtnFechaNac.getId()) {
			DialogFecha d = new DialogFecha(tvFecha_nac);
			d.show(getFragmentManager(), "tagFechaNacUsuario");
		}
	}

 	public void validarIniciarRegistroUsuario(){
		String ci = etCI.getText().toString();
		String nombre = etNombre.getText().toString();
		String apellido = MyVar.NO_ESPECIFICADO;
		String usuario = etUsuario.getText().toString().trim();
		String clave = etClave.getText().toString().trim();
		String clave2 = etClaveRepetir.getText().toString().trim();
		int celular = 0;
		String direccion = MyVar.NO_ESPECIFICADO;
		String email = MyVar.NO_ESPECIFICADO;
		Date fecha_nac = MyVar.FECHA_DEFAULT;
		String sexo = MyVar.NO_ESPECIFICADO;
		if(!pathImageUsuario.equals(MyVar.NO_ESPECIFICADO)){
			copySaveImageUsuario();
		}
	
		if(ci.length()>=7){
			if(nombre.length()>=3){
				if(usuario.length()>=4){
					if(clave.length()>=6){
						if(clave.equals(clave2)){
							if(CI_DISPONIBLE){
								if(NOMBRE_USUARIO_DISPONIBLE){
									Usuario nuevo_usuario = new Usuario(getIDUsuarioGenerado(), ci, nombre, apellido, usuario, clave, celular, 
											direccion, email, sexo, fecha_nac, this.CARGO, MyVar.NO_ELIMINADO, pathImageUsuario, this.idparqueo);
									new RegistrarUsuario().execute(nuevo_usuario);
								}else{
									etUsuario.requestFocus();
									etUsuario.setError("Usuario elegido ya existe, introduzca otro por favor");
								}
							}else{
								etCI.requestFocus();
								etCI.setError("Carnet Identidad introducido ya existe");
							}
						}else{
							etClave.requestFocus();
							etClave.setError("Claves no coenciden, intente nuevamente");
							etClave.getText().clear();
							etClaveRepetir.getText().clear();
						}					
					}else{
						etClave.requestFocus();
						etClave.setError("Claves son necesarios, mínimo debe tener 6 caracteres");
						etClave.getText().clear();
						etClaveRepetir.getText().clear();
					}
				}else{
					etUsuario.requestFocus();
					etUsuario.setError("Usuario es necesario y mínimo debe tener 4 caracteres");
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
 		
	public void validarIniciarActualizacionUsuario(){
		String ci = etCI.getText().toString().trim();
		String nombre = etNombre.getText().toString();
		String apellido = MyVar.NO_ESPECIFICADO;
		if(!etApellido.getText().toString().equals("")){ apellido = etApellido.getText().toString(); }
		int celular = 0;
		if(!etCelular.getText().toString().equals("")){ celular = Integer.parseInt(etCelular.getText().toString()); }
		String direccion = MyVar.NO_ESPECIFICADO;
		if(!etDireccion.getText().toString().equals("")){ direccion = etDireccion.getText().toString(); }
		String email = MyVar.NO_ESPECIFICADO;
		if(!etE_mail.getText().toString().equals("")){ email = etE_mail.getText().toString(); } 
		Date fecha_nac = (Date)tvFecha_nac.getTag();
		
		String sexo = MyVar.NO_ESPECIFICADO;
		if(rdgSexo.getCheckedRadioButtonId()==rbtnMasculino.getId()){ 
			sexo = MyVar.MASCULINO; 
		}else if (rdgSexo.getCheckedRadioButtonId()==rbtnFemenino.getId()) {
			sexo = MyVar.FEMENINO;
		}
		
		if(!user_online.getImagen().equals(this.pathImageUsuario) && !this.pathImageUsuario.equals(MyVar.NO_ESPECIFICADO)){
			copySaveImageUsuario();
		}
		if(nombre.length()>=4){
			if(ci.length()>=7){
				if(CI_DISPONIBLE){
					Usuario editar_usuario = new Usuario(this.user_online.getIdusuario(),ci ,nombre, apellido, celular, direccion,
							email, sexo, fecha_nac, pathImageUsuario);
					new ActualizarUsuario().execute(editar_usuario);
				}else{
					etCI.requestFocus();
					etCI.setError("Carnet Identidad introducido ya existe");
				}
			}else{
				etCI.requestFocus();
				etCI.setError("Carnet Identidad debe tener 7 u 8 digitos");
			}
		}else{
			etNombre.requestFocus();
			etNombre.setError("Nombre del usuario debe tener mínimo 3 caracteres");
		}
	}
	
	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent,MyVar.SELECT_IMAGE);
	}
	
	public void captureImage(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		ruta_images.mkdirs();
		
		Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File file_image_usuario = new File(ruta_images, getNameImageUsuario());
		finalImageUri = Uri.fromFile(file_image_usuario);
		this.pathImageUsuario = file_image_usuario.getAbsolutePath();
		intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, finalImageUri);
		startActivityForResult(intentCamera, MyVar.CAPTURE_IMAGE);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		
		if(requestCode==MyVar.SELECT_IMAGE && resultCode==getActivity().RESULT_OK){
			OPCION_ELEGIDA = MyVar.SELECT_IMAGE;
			finalImageUri = data.getData();
			this.pathImageUsuario = getRealPathFromURI(finalImageUri);
			finalImageBitmap = BitmapFactory.decodeFile(pathImageUsuario);
			performCrop();
		}else if(requestCode==MyVar.CAPTURE_IMAGE && resultCode==getActivity().RESULT_OK){
			OPCION_ELEGIDA = MyVar.CAPTURE_IMAGE;
			finalImageBitmap = BitmapFactory.decodeFile(pathImageUsuario);
			performCrop();
		}else if(requestCode==MyVar.PIC_CROP && resultCode==getActivity().RESULT_OK){
			Bundle extras = data.getExtras();
			finalImageBitmap = extras.getParcelable("data");
			ivImagenUsuario.setImageBitmap(finalImageBitmap);
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
		
	private class RegistrarUsuario extends AsyncTask<Usuario, Void, Boolean>{
		ProgressDialog pd;
		Usuario usuario;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Registrando usuario...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Usuario... params) {
			usuario = params[0];
			if(httpCustom.insertarUsuario(usuario)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.insertarUsuario(usuario)){
						return true;
					}
					db.closeSQLite();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				if(!usuario.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(usuario.getImagen()).toString());
				}
				if(ACCION == MyVar.ACCION_REGISTRAR_ADMIN){
					Toast.makeText(getActivity(), "Administrador registrado exitosamente", Toast.LENGTH_LONG).show();
					accesoIniciar();		
				}else if (ACCION == MyVar.ACCION_REGISTRAR_USUARIO) {
					Toast.makeText(getActivity(), "Usuario registrado exitosamente", Toast.LENGTH_LONG).show();
					listener.onBackFromPerfilClick();
				}	
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo registrar el usuario, intente mas tarde...!");
				mensaje.show(getFragmentManager(), "tagMsj");
			}
		}
		
		public void accesoIniciar(){
			setIDShare(usuario.getIdparqueo(), usuario.getIdusuario());
			limpiarCampos();
			getActivity().finish();
			Intent intent = new Intent("android.intent.action.MENUMAIN");
			Bundle caja = new Bundle();
			caja.putString("idparqueo", usuario.getIdparqueo());
			caja.putString("idusuario", usuario.getIdusuario());
			intent.putExtras(caja);
			startActivity(intent);
		}
	}
	
	private class ActualizarUsuario extends AsyncTask<Usuario, Void, Boolean>{
		ProgressDialog pd;
		Usuario usuario;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando usuario...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Usuario... params) {
			usuario = params[0];
			if(httpCustom.actualizarUsuario(usuario)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.actualizarUsuario(usuario)){
						return true;
					}
					db.closeSQLite();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				if(!usuario.getImagen().equals(user_online.getImagen()) && !usuario.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(usuario.getImagen()).toString());
				}
				Toast.makeText(getActivity(), "Tu perfil fue actualizado", Toast.LENGTH_LONG).show();
				listener.onActualizarPerfilClick();	
				listener.onBackFromPerfilClick();
			}else{
				listener.onBackFromPerfilClick();
			}
		}	
	}
	
	public void setIDShare(String idparqueo, String idusuario){
		SharedPreferences sharep = getActivity().getSharedPreferences("ShareParqueo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = sharep.edit();
		editar.putString("idparqueo", idparqueo);
		editar.putString("idusuario", idusuario);
		editar.commit();
	}
	
	public boolean copySaveImageUsuario(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		ruta_images.mkdirs();
		
		String name_image_usuario = null;
		if(OPCION_ELEGIDA==MyVar.SELECT_IMAGE){
			name_image_usuario = getNameImageUsuario();
		}else if(OPCION_ELEGIDA==MyVar.CAPTURE_IMAGE){
			name_image_usuario = new File(this.pathImageUsuario).getName();
		}
		File file_image_usuario = new File(ruta_images, name_image_usuario);
		//file_image_usuario.mkdirs();
		
		FileOutputStream fos;
		try{
			this.pathImageUsuario = file_image_usuario.getName();
			fos = new FileOutputStream(file_image_usuario);
			finalImageBitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			scanImage(file_image_usuario.getAbsolutePath());
			if(file_image_usuario.exists()){
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
	
	public interface OnPerfilClickListener{
		public void onActualizarPerfilClick();
		public void onBackFromPerfilClick();
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnPerfilClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implemtar OnPerfilClickListener");
		}
	}
	
	public void limpiarCampos(){ 
		etCI.getText().clear(); etNombre.getText().clear();
		etApellido.getText().clear(); etUsuario.getText().clear();
		etClave.getText().clear();
		etClaveRepetir.getText().clear();
		etCelular.getText().clear(); 
		etDireccion.getText().clear(); etE_mail.getText().clear();
		tvFecha_nac.setTag(null);
		tvFecha_nac.setText(R.string.fecha_nac); 
		ivImagenUsuario.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
		this.pathImageUsuario = MyVar.NO_ESPECIFICADO;
		if(rdgSexo.getCheckedRadioButtonId()==rbtnMasculino.getId()){
			rbtnMasculino.setChecked(false);
		}else if (rdgSexo.getCheckedRadioButtonId()==rbtnFemenino.getId()) {
			rbtnFemenino.setChecked(false);
		}
		this.OPCION_ELEGIDA = 0;
	}
	
	public String getNameImageUsuario(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "user_"+this.idparqueo+"_pic_"+date+".jpg";
		return imageCode;
	}
	
	public String getIDUsuarioGenerado(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "user_"+date+"_"+this.idparqueo;
		return imageCode;
	}
	
	public String getRealPathFromURI(Uri contentUri){
		@SuppressWarnings("unused")
		String [] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().managedQuery(contentUri, null, null, null, null);	
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
}
