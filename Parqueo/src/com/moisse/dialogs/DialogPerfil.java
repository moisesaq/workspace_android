package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.modelo.Usuario;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogPerfil extends DialogFragment implements OnClickListener{

	private Usuario user_online;
	private View v;
	private Button btnCambiarClave, btnEditarPerfil, btnOk;
	OnEditarPerfilClickListener listener;
	
	public DialogPerfil(Usuario user_activo){
		this.user_online = user_activo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Mi perfil ").append(user_online.getNombre_usuario()));
		
		v = getActivity().getLayoutInflater().inflate(R.layout.perfil, null);
		ImageView imagen = (ImageView)v.findViewById(R.id.ivImageUserPerfil);
		if(!user_online.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(user_online.getImagen()).toString());
			if(bitmap!=null){
				imagen.setImageBitmap(bitmap);
			}else{
				imagen.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
			}
		}else{
			imagen.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
		}
		TextView nombre_usuario = (TextView)v.findViewById(R.id.tvUsuarioPerfil);
		nombre_usuario.setText(new StringBuilder(user_online.getNombre_usuario()));
		TextView ci = (TextView)v.findViewById(R.id.tvCIPerfil);
		ci.setText(new StringBuilder(user_online.getCi()));
		TextView nombre = (TextView)v.findViewById(R.id.tvNombrePerfil);
		nombre.setText(new StringBuilder(user_online.getNombre()));
		TextView apellido = (TextView)v.findViewById(R.id.tvApellidoPerfil);
		apellido.setText(new StringBuilder(user_online.getApellido()));
		TextView celular = (TextView)v.findViewById(R.id.tvCelularPerfil);
		if(user_online.getCelular()!=0){
			celular.setText(new StringBuilder().append(user_online.getCelular()));
		}else{
			celular.setText(new StringBuilder().append(MyVar.NO_ESPECIFICADO));
		}
		TextView direccion = (TextView)v.findViewById(R.id.tvDireccionPerfil);
		direccion.setText(new StringBuilder(user_online.getDireccion()));
		TextView email = (TextView)v.findViewById(R.id.tvEmailPerfil);
		email.setText(new StringBuilder(user_online.getEmail()));
		TextView sexo = (TextView)v.findViewById(R.id.tvSexoPerfil);
		sexo.setText(new StringBuilder(user_online.getSexo()));
		TextView fecha_nac = (TextView)v.findViewById(R.id.tvFechaNacPerfil);
		if(!user_online.getFecha_nac().equals(MyVar.FECHA_DEFAULT)){
			fecha_nac.setText(new StringBuilder(MyVar.FORMAT_FECHA_1.format(user_online.getFecha_nac())));
		}else{
			fecha_nac.setText(new StringBuilder(MyVar.NO_ESPECIFICADO));
		}
		
		btnCambiarClave = (Button)v.findViewById(R.id.btnCambiarClavePerfil);
		btnCambiarClave.setOnClickListener(this);
		btnEditarPerfil = (Button)v.findViewById(R.id.btnEditarPerfil);
		btnEditarPerfil.setOnClickListener(this);
		btnOk = (Button)v.findViewById(R.id.btnOKPerfil);
		btnOk.setOnClickListener(this);

		dialog.setView(v);
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnCambiarClave.getId()){
			DialogCambiarClave dialogCambiarClave = new DialogCambiarClave(user_online);
			dialogCambiarClave.show(getFragmentManager(), "tagDCambiaClave");
			this.dismiss();
		}else if (v.getId()==btnEditarPerfil.getId()) {
			listener.onEditarPerfilClick(user_online.getIdusuario());
			this.dismiss();
		}else if (v.getId()==btnOk.getId()) {
			this.dismiss();
		}
	}
	
	public interface OnEditarPerfilClickListener{
		public void onEditarPerfilClick(String idusuario);
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnEditarPerfilClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+"debe implementar onEditarPerfilClickListener");
		}
	}
}
