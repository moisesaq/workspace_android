package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Usuario;
import com.moisse.others.HttpClientCustom;
import com.moisse.parqueo.MainActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogCambiarClave extends DialogFragment implements OnClickListener{

	private Usuario user_online;
	private View v;
	private EditText etUsuario, etClaveActual, etNuevaClave, etNuevaClaveRepetir;
	private Button btnConfirmar, btnCancelar;
	private HttpClientCustom httpCustom = new HttpClientCustom();
	private ProgressBar pbVerificarUser;
	public boolean NOMBRE_USUARIO_DISPONIBLE = true;
	
	public DialogCambiarClave(Usuario user_online){
		this.user_online = user_online;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Cambiar clave"));
		
		v = getActivity().getLayoutInflater().inflate(R.layout.cambiar_clave, null);
		pbVerificarUser = (ProgressBar)v.findViewById(R.id.pbVericarNombreUsuarioCambiarClave);
		etUsuario = (EditText)v.findViewById(R.id.etUsuarioCambiarClave);
		etUsuario.addTextChangedListener(NombreUsuarioWacher);
		etUsuario.setText(new StringBuilder(user_online.getNombre_usuario()));
		etClaveActual = (EditText)v.findViewById(R.id.etClaveActualCambiarClave);
		etNuevaClave = (EditText)v.findViewById(R.id.etClaveNuevaCambiarClave);
		etNuevaClaveRepetir = (EditText)v.findViewById(R.id.etClaveNuevaRepetirCambiarClave);
		btnConfirmar = (Button)v.findViewById(R.id.btnConfirmarCambiarClave);
		btnConfirmar.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarCambiarClave);
		btnCancelar.setOnClickListener(this);
		
		dialog.setView(v);
		return dialog.create();
	}
	
	private final TextWatcher NombreUsuarioWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			VerifyNombreUsuario verify = new VerifyNombreUsuario();
			if(!user_online.getNombre_usuario().equals(s.toString())){
				if(s.length()>=4 && s.length()<=10){
					verify.execute(s.toString());
				}else{
					verify.cancel(true);
					NOMBRE_USUARIO_DISPONIBLE = false;
					etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}	
			}else{
				etUsuario.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				NOMBRE_USUARIO_DISPONIBLE = true;
			}
				
		}
	};
	
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
		if(btnConfirmar.getId()==v.getId()){
			cambiarClaveDelUsuario();
		}else if(btnCancelar.getId()==v.getId()){
			this.dismiss();
			DialogPerfil dialogPerfil = new DialogPerfil(user_online);
			dialogPerfil.show(getFragmentManager(), "tagDPerfil1");
		}
	}

	private void cambiarClaveDelUsuario() {
		String usuario = etUsuario.getText().toString().trim();
		String clave_actual = etClaveActual.getText().toString().trim();
		String nueva_clave = etNuevaClave.getText().toString().trim();
		String nueva_clave_repetir = etNuevaClaveRepetir.getText().toString().trim();
		
		if(usuario.length()>=4){
			if(!clave_actual.equals("")){
				if(clave_actual.equals(user_online.getClave())){
					if(nueva_clave.length()>=6 && nueva_clave_repetir.length()>=6){
						if(nueva_clave.equals(nueva_clave_repetir)){
							if(NOMBRE_USUARIO_DISPONIBLE){
								Usuario user_cambiar = new Usuario(user_online.getIdusuario(), usuario, nueva_clave);
								new ChangePassword().execute(user_cambiar);
							}else{
								etUsuario.requestFocus();
								etUsuario.setError("Usuario ya esta en uso, elija otro por favor");
							}
						}else{
							etNuevaClave.requestFocus();
							etNuevaClave.setError("Las claves no coenciden intente nuevamente!");
							limpiarCampos();
						}	
					}else{
						etNuevaClave.requestFocus();
						etNuevaClave.setError("Nuevas claves son necesarios y minimo debe tener 6 caracteres");
						limpiarCampos();
					}
				}else{
					etClaveActual.setError("Clave actual incorrecta");
					limpiarCampos();
				}
			}else{
				etClaveActual.requestFocus();
				etClaveActual.setError("Introduzca su clave actual");
				limpiarCampos();
			}
		}else{
			etUsuario.requestFocus();
			etUsuario.setError("Usuario mínimo debe tener 4 caracteres");
		}
	}
	
	public void limpiarCampos(){
		etClaveActual.getText().clear();
		etNuevaClave.getText().clear();
		etNuevaClaveRepetir.getText().clear();
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error al " +titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	private class ChangePassword extends AsyncTask<Usuario, Void, Boolean>{
		ProgressDialog pd;
		Usuario usuario;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Cambiando clave del usuario..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Usuario... params) {
			usuario = params[0];
			if(httpCustom.cambiarClaveUsuario(usuario)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.cambiarClaveUsuario(usuario)){
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
				clearIDShare();
				Toast.makeText(getActivity(),"Clave cambiada, favor de loguearse nuevamente",Toast.LENGTH_LONG).show();
				dismiss();
				getActivity().finish();
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(getActivity(),"No se pudo cambiar clave de usuario intente mas tarde..!",Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void clearIDShare(){
		SharedPreferences sharep = getActivity().getSharedPreferences("ShareParqueo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = sharep.edit();
		editar.putString("idparqueo", "");
		editar.putString("idusuario", "");
		editar.commit();
	}
}
