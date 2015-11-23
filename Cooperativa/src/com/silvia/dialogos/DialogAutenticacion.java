package com.silvia.dialogos;

import java.sql.Date;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.MainActivity;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Cooperativa;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogAutenticacion extends DialogFragment implements OnClickListener{

	View v;
	EditText etCi, etNombre, etUsuario, etClave, etClave2;
	Button btnAceptar, btnCancelar;
	
	MainActivity main_activity;
	
	public DialogAutenticacion(MainActivity main){
		this.main_activity = main;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDivideId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDivideId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setCancelable(false);
		v = getActivity().getLayoutInflater().inflate(R.layout.autenticacion, null);
		etCi = (EditText)v.findViewById(R.id.etCIAutenticacion);
		etNombre = (EditText)v.findViewById(R.id.etNombreAutenticacion);
		etUsuario = (EditText)v.findViewById(R.id.etUsuarioAutenticacion);
		etClave = (EditText)v.findViewById(R.id.etClaveAutenticacion);
		etClave2 = (EditText)v.findViewById(R.id.etClave2Autenticacion);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarAtenticacion);
		btnAceptar.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarAutenticacion);
		btnCancelar.setOnClickListener(this);
		if(isFirst()){
			dialog.setTitle("Datos del administrador");
		}else{
			dialog.setTitle("Autenticacion");
			etCi.setVisibility(View.GONE);
			etNombre.setVisibility(View.GONE);
			etClave2.setVisibility(View.GONE);
		}
		dialog.setView(v);
		return dialog.create();
		
	}
	
	public boolean isFirst(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.getTodosLosCargos().size()==0 && db.getTodosLasMaquinarias().size()==0 && db.getTodosLosUsuario().size()==0){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		if(btnAceptar.getId()==v.getId()){
			if(isFirst()){
				registrarAdministrador();
			}else{
				autenticarUsuario();
			}
		}else if(btnCancelar.getId()==v.getId()){
			getActivity().finish();
		}
	}
	
	public void autenticarUsuario(){
		String usuario = etUsuario.getText().toString().trim();
		String clave = etClave.getText().toString().trim();
		if(!usuario.equals("") || !clave.equals("")){
			DBDuraznillo db = new DBDuraznillo(getActivity());
			try {
				db.abrirDB();
				Usuario aut_user = db.autenticarUsuario(usuario, clave);
				if(aut_user!=null){
					Toast.makeText(getActivity(), "Bienvenido "+aut_user.getUsuario(), Toast.LENGTH_SHORT).show();
					setIDUsuarioShare(aut_user.getIdusuario());
					main_activity.inicializarTabs();
					dismiss();
				}else{
					Toast.makeText(getActivity(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
				}
				db.cerrarDB();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(getActivity(), "Introduzca usuario y clave", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void registrarAdministrador(){
		String CI = etCi.getText().toString().trim();
		String nombre = etNombre.getText().toString().trim();
		String nombre_usuario = etUsuario.getText().toString().trim();
		String clave = etClave.getText().toString().trim();
		String clave2 = etClave2.getText().toString().trim();
		
		if(CI.length()>=7){
			if(nombre.length()>=4 && nombre_usuario.length()>=4 && clave.length()>=4){
				if(clave2.equals(clave)){
					
					Cooperativa cooperativo = new Cooperativa(Variables.ID_COOP, "COOPERATIVA AGREGADOS DURAZNILLO", "5544332211", Variables.SIN_ESPECIFICAR, 
																Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR);
					Cargo cargo = new Cargo(Variables.ID_CARGO_ADMIN, "ADMINISTRADOR", 7000, Variables.SIN_ESPECIFICAR, Variables.NO_ELIMINADO);
					Maquinaria maquinaria = new Maquinaria(Variables.ID_MAQ_DEFAULT, Variables.PLACA_MAQ_DEFAULT, "Sin Maquinaria", Variables.CAPACIDAD_DEFAULT,
													Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR, Variables.NO_ELIMINADO);
					
					DBDuraznillo db = new DBDuraznillo(getActivity());
					try {
						db.abrirDB();
						if(db.insertarCargo(cargo) && db.insertarMaquinaria(maquinaria) && db.insertarCooperativa(cooperativo)){
							Personal personal = new Personal(Variables.ID_PERSONAL_ADMIN, CI, nombre, Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR, 0, 
									Variables.SIN_ESPECIFICAR, Variables.FECHA_DEFAULT, getFechaActual(), Variables.SIN_ESPECIFICAR, 
									Variables.NO_ELIMINADO, cargo.getIdcargo(), maquinaria.getIdmaquinaria());
							if(db.insertarPersonal(personal)){
								Usuario usuario = new Usuario(Variables.ID_USER_ADMIN, nombre_usuario, clave, Variables.NO_ELIMINADO, personal.getIdpersonal());
								if(db.insertarUsuario(usuario)){
									Toast.makeText(getActivity(), "Administrador registrado exitosamente", Toast.LENGTH_SHORT).show();
									mostrarRecomendacion();
									setIDUsuarioShare(usuario.getIdusuario());
									main_activity.inicializarTabs();
									dismiss();
								}else{
									Toast.makeText(getActivity(), "No se pudo registrar administrador", Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(getActivity(), "Error al registrar administrador", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(getActivity(), "Error al registrar datos necesarios principales", Toast.LENGTH_LONG).show();
						}
						db.cerrarDB();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Toast.makeText(getActivity(), "Claves no coenciden", Toast.LENGTH_SHORT).show();
					etClave.requestFocus();
					etClave.getText().clear();
					etClave2.getText().clear();
				}
			}else{
				Toast.makeText(getActivity(), "Nombre, Usuario y Clave deben contener minimo 4 caracteres", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getActivity(), "CI debe contener como minimo 7 caracteres", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void mostrarRecomendacion(){
		Dialog d = new Dialog(getActivity().getApplicationContext());
		d.setTitle(new StringBuilder("Nota"));
		TextView tv = new TextView(getActivity().getApplicationContext());
		tv.setPadding(5, 5, 5, 5);
		tv.setText(" Se recomienda registrar al menos:\n " +
					" Un producto ej. arena\n " +
					" Un cargo ej. chofer\n " +
					" Un maquinaria ej. volqueta\n " +
					" Un personal ej. pedro, dni: 12345678 ");
		d.setContentView(tv);
		d.show();
	}
	
	public Date getFechaActual(){
		java.util.Date date = new java.util.Date();
		Date fecha = new Date(date.getTime());
		return fecha;
	}
	
	public void setIDUsuarioShare(String idusuario){
		SharedPreferences shareC = getActivity().getSharedPreferences("ShareCooperativa", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = shareC.edit();
		editar.putString("idusuario", idusuario);
		editar.commit();
	}
	
}
