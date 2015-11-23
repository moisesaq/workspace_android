package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.ListaUsuarios;
import com.moisse.modelo.Usuario;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetalleUsuario extends DialogFragment implements OnClickListener{
	
	private ListaUsuarios listUsuarios;
	private Usuario user, user_online;
	private View v;
	private Button btnDarBaja, btnOk;
	
	HttpClientCustom httpCustom;
	
	public DialogDetalleUsuario(ListaUsuarios listUsuarios, Usuario usuario, Usuario user_activo){
		this.listUsuarios = listUsuarios;
		this.user = usuario;
		this.user_online = user_activo;
	}
	
	public DialogDetalleUsuario(Usuario usuario, Usuario user_activo){
		this.user = usuario;
		this.user_online = user_activo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Datos usuario"));
		
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_usuario, null);
		ImageView imagen = (ImageView)v.findViewById(R.id.ivImageUserDetalleUsuario);
		if(!user.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(user.getImagen()).toString());
			if(bitmap!=null){
				imagen.setImageBitmap(bitmap);
			}else{
				imagen.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
			}
		}else{
			imagen.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
		}
		TextView nombre_usuario = (TextView)v.findViewById(R.id.tvUsuarioDetalleUsuario);
		nombre_usuario.setText(new StringBuilder("Usuario: ").append(user.getNombre_usuario()));
		TextView ci = (TextView)v.findViewById(R.id.tvCIDetalleUsuario);
		ci.setText(new StringBuilder(user.getCi()));
		TextView nombre = (TextView)v.findViewById(R.id.tvNombreDetalleUsuario);
		nombre.setText(new StringBuilder(user.getNombre()));
		TextView apellido = (TextView)v.findViewById(R.id.tvApellidoDetalleUsuario);
		apellido.setText(new StringBuilder(user.getApellido()));
		TextView celular = (TextView)v.findViewById(R.id.tvCelularDetalleUsuario);
		if(user.getCelular()!=0){
			celular.setText(new StringBuilder().append(user.getCelular()));
		}else{
			celular.setText(new StringBuilder().append(MyVar.NO_ESPECIFICADO));
		}

		TextView direccion = (TextView)v.findViewById(R.id.tvDireccionDetalleUsuario);
		direccion.setText(new StringBuilder(user.getDireccion()));
		TextView email = (TextView)v.findViewById(R.id.tvEmailDetalleUsuario);
		email.setText(new StringBuilder(user.getEmail()));
		TextView sexo = (TextView)v.findViewById(R.id.tvSexoDetalleUsuario);
		sexo.setText(new StringBuilder(user.getSexo()));
		TextView fecha_nac = (TextView)v.findViewById(R.id.tvFechaNacDetalleUsuario);
		if(!user.getFecha_nac().equals(MyVar.FECHA_DEFAULT)){
			fecha_nac.setText(new StringBuilder(MyVar.FORMAT_FECHA_1.format(user.getFecha_nac())));
		}else{
			fecha_nac.setText(new StringBuilder(MyVar.NO_ESPECIFICADO));
		}
		btnDarBaja = (Button)v.findViewById(R.id.btnDarBajaDetalleUsuario);
		btnDarBaja.setOnClickListener(this);
		btnOk = (Button)v.findViewById(R.id.btnOKDetalleUsuario);
		btnOk.setOnClickListener(this);
		
		if(user_online.getCargo()==MyVar.CARGO_ADMIN){
			if(user.getEstado()==MyVar.NO_ELIMINADO){
				btnDarBaja.setText(new StringBuilder("Inhabilitar"));
			}else if(user.getEstado()==MyVar.ELIMINADO){
				btnDarBaja.setText(new StringBuilder("Habilitar"));
			}
		}else if(user_online.getCargo()==MyVar.CARGO_USUARIO){
			btnDarBaja.setVisibility(View.GONE);
		}
		
		dialog.setView(v);
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnDarBaja.getId()){
			new HabilitarDeshabilitarUsuario().execute(user);
		}else if(v.getId()==btnOk.getId()){
			this.dismiss();
		}
	}
	
	public void cambiarEstadoUsuario(int estado){
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			if(listUsuarios!=null){
				if(!db.setEstadoUsuario(user, estado)){
					Toast.makeText(getActivity(),"Usuario Inhabilitado",Toast.LENGTH_LONG).show();
					this.dismiss();
					listUsuarios.cargarListaUsuarios();
				}else{
					Toast.makeText(getActivity(),"Usuario Habilitado",Toast.LENGTH_LONG).show();
					this.dismiss();
					listUsuarios.cargarListaUsuarios();
				}
			}else{
				if(!db.setEstadoUsuario(user, estado)){
					Toast.makeText(getActivity(),"Usuario Inhabilitado",Toast.LENGTH_LONG).show();
					this.dismiss();
				}else{
					Toast.makeText(getActivity(),"Usuario Habilitado",Toast.LENGTH_LONG).show();
					this.dismiss();
				}
			}
			db.closeSQLite();
		} catch (Exception e) {
			mensajeError(e.toString(), "cambiar estado usuario");
		}
	}
	
	public class HabilitarDeshabilitarUsuario extends AsyncTask<Usuario, Void, Boolean>{
		ProgressDialog pd;
		Usuario usuario;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Deshabilitando usuario..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Usuario... params){
			usuario = params[0];
			httpCustom = new HttpClientCustom();
			DBParqueo db = new DBParqueo(getActivity());
			if(usuario.getEstado()==MyVar.NO_ELIMINADO){
				if(!httpCustom.habilitarDeshabilitarUsuario(usuario, MyVar.ELIMINADO)){
					try{
						db.openSQLite();
						if(!db.setEstadoUsuario(usuario, MyVar.ELIMINADO)){
							return false;
						}
						db.closeSQLite();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else if (usuario.getEstado()==MyVar.ELIMINADO) {
				if(httpCustom.habilitarDeshabilitarUsuario(usuario, MyVar.NO_ELIMINADO)){
					try{
						db.openSQLite();
						if(db.setEstadoUsuario(usuario, MyVar.NO_ELIMINADO)){
							return true;
						}
						db.closeSQLite();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result.equals(true)){
				Toast.makeText(getActivity(),"Usuario Habilitado",Toast.LENGTH_LONG).show();
				dismiss();
				if(listUsuarios!=null){
					listUsuarios.cargarListaUsuarios();
				}
			}else if (result.equals(false)) {
				Toast.makeText(getActivity(),"Usuario Deshabilitado",Toast.LENGTH_LONG).show();
				dismiss();
				if(listUsuarios!=null){
					listUsuarios.cargarListaUsuarios();
				}				
			}else if (result.equals(null)) {
				DialogMensaje dMsj = new DialogMensaje("No se pudo habilitar o desabilitar usuario, intente mas tarde...!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error "+titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
}
