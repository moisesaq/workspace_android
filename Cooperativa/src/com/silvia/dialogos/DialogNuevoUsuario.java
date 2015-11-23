package com.silvia.dialogos;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogNuevoUsuario extends DialogFragment implements OnClickListener{

	View v;
	ImageView ivImagenPersonal;
	EditText etCIPersonal, etUsuario, etClaveActual, etClave, etRepetirClave;
	Button btnAceptar, btnCancelar;
	boolean DISPONIBLE_USUARIO = false;
	public int ACCION;
	
	private Personal personal;
	private Usuario usuario;
	
	public DialogNuevoUsuario(Personal personal, int ACCION){
		this.personal = personal;
		this.ACCION = ACCION;
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
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		v = getActivity().getLayoutInflater().inflate(R.layout.nuevo_usuario, null);
		ivImagenPersonal = (ImageView)v.findViewById(R.id.ivImagenNuevoUsuario);
		etCIPersonal = (EditText)v.findViewById(R.id.etCIPersonalNuevoUsuario);
		etUsuario = (EditText)v.findViewById(R.id.etNombreUserNuevoUsuario);
		etUsuario.addTextChangedListener(UserWatcher);
		etClaveActual = (EditText)v.findViewById(R.id.etClaveActualNuevoUsuario);
		etClave = (EditText)v.findViewById(R.id.etClaveNuevoUsuario);
		etRepetirClave = (EditText)v.findViewById(R.id.etRepetirClaveNuevoUsuario);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarNuevoUsuario);
		btnAceptar.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarNuevoUsuario);
		btnCancelar.setOnClickListener(this);
		
		if(this.ACCION==Variables.ACCION_NUEVO_USUARIO){
			dialog.setTitle("Nuevo usuario");
		}else if (this.ACCION==Variables.ACCION_EDITAR_USUARIO) {
			dialog.setTitle("Editar usuario");
			this.usuario = getUsuario(personal.getIdpersonal());
			etUsuario.setText(usuario.getUsuario());
			etClaveActual.setVisibility(View.VISIBLE);
			etClave.setHint("Nueva clave");
			etRepetirClave.setHint("Repetir nueva clave");
		}
		cargarDatosPersonal();
		dialog.setView(v);
		
		return dialog.create();
	}
	
	public final TextWatcher UserWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>4){
				if(ACCION==Variables.ACCION_NUEVO_USUARIO){
					if(!existeUsuario(s.toString())){
						DISPONIBLE_USUARIO = true;
						etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
					}else{
						DISPONIBLE_USUARIO = false;
						etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
						etUsuario.requestFocus();
						etUsuario.setError(new StringBuilder("Este usuario ya existe"));
					}
				}else if(ACCION==Variables.ACCION_EDITAR_USUARIO){
					if(!usuario.getUsuario().equals(s.toString())){
						if(!existeUsuario(s.toString())){
							DISPONIBLE_USUARIO = true;
							etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
						}else{
							DISPONIBLE_USUARIO = false;
							etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
							etUsuario.requestFocus();
							etUsuario.setError(new StringBuilder("Este usuario ya existe"));
						}
					}else{
						etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
						DISPONIBLE_USUARIO = true;
					}
				}
			}else{
				etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				DISPONIBLE_USUARIO = false;
			}
			
		}
	};
	
	public void cargarDatosPersonal(){
		if(!personal.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(personal.getImagen()).toString());
			if(bitmap!=null){
				ivImagenPersonal.setImageBitmap(bitmap);
			}else{
				ivImagenPersonal.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
			}
		}else{
			ivImagenPersonal.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
		}
		etCIPersonal.setText(personal.getCi());
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnAceptar.getId()){
			validarRegistrarEditarUsuario();
		}else if (v.getId()==btnCancelar.getId()) {
			dismiss();
		}
	}
	
	public void validarRegistrarEditarUsuario(){
		String nombre_usuario = etUsuario.getText().toString().trim();
		String clave_actual = etClaveActual.getText().toString().trim();
		String clave = etClave.getText().toString().trim();
		String clave2 = etRepetirClave.getText().toString().trim();
		
		if(!nombre_usuario.equals("") && !clave.equals("")){
			if(nombre_usuario.length()>=4 && clave.length()>=4){
				if(DISPONIBLE_USUARIO){
					if(clave.equals(clave2)){
						if(this.ACCION==Variables.ACCION_NUEVO_USUARIO){
							Usuario user = new Usuario(generarIdUsuario(), nombre_usuario, clave, Variables.NO_ELIMINADO, personal.getIdpersonal());
							if(registrarUsuario(user)){
								Toast.makeText(getActivity(), "Personal definido como usuario", Toast.LENGTH_SHORT).show();
								dismiss();
							}else{
								Toast.makeText(getActivity(), "No se pudo definir personal como usuario", Toast.LENGTH_SHORT).show();
							}
						}else if(this.ACCION==Variables.ACCION_EDITAR_USUARIO){
							if(clave_actual.equals(this.usuario.getClave())){
								Usuario user = new Usuario(this.usuario.getIdusuario(), nombre_usuario, clave, Variables.NO_ELIMINADO, personal.getIdpersonal());
								if(modificarUsuario(user)){
									Toast.makeText(getActivity(), "Usuario modificado exitosamente", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(getActivity(), "No se pudo modificar usuario", Toast.LENGTH_SHORT).show();
								}
								dismiss();
							}else{
								etClaveActual.requestFocus();
								etClaveActual.setError(new StringBuilder("Clave actual incorrecto"));
								etClaveActual.getText().clear();
								limpiarClaves();
							}
						}
					}else{
						etClave.requestFocus();
						etClave.setError(new StringBuilder("Claves no coenciden, intente de nuevo"));
						etClaveActual.getText().clear();
						limpiarClaves();
					}
				}else{
					etUsuario.requestFocus();
					etUsuario.setError(new StringBuilder("Este usuario ya existe"));
					etUsuario.getText().clear();
					limpiarClaves();
				}
			}else{
				Toast.makeText(getActivity(), "Usuario min. 4 caracteres y clave min. 4 caracteres", Toast.LENGTH_SHORT).show();
				limpiarClaves();
			}
		}else{
			Toast.makeText(getActivity(), "Introduzca usuario y clave", Toast.LENGTH_LONG).show();
		}
	}
	
	public void limpiarClaves(){
		etClave.getText().clear();
		etRepetirClave.getText().clear();
	}
	
	public String generarIdUsuario(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "user-"+date;
		return imageCode;
	}
	
	public boolean existeUsuario(String usuario){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.existeUsuario(usuario)){
				return true;
			}				
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean registrarUsuario(Usuario usuario){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.insertarUsuario(usuario)){
				return true;
			}				
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean modificarUsuario(Usuario usuario){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.modificarUsuario(usuario)){
				return true;
			}				
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Usuario getUsuario(String idpersonal){
		Usuario usuario = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			usuario = db.getUsuarioPersonal(idpersonal);			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}
	
}
