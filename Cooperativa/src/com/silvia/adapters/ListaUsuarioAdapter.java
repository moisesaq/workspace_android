package com.silvia.adapters;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

public class ListaUsuarioAdapter extends ArrayAdapter<Usuario>{

	private Activity activity;
	private ImageLoader imageLoader;
	
	public ListaUsuarioAdapter(Activity activity, List<Usuario> lista_user){
		super(activity, R.layout.modelo_item_lista_usuario, lista_user);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageUser;
		TextView tvUsuario;
		TextView tvNombrePer;
	}
	
	@Override
	public Usuario getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_lista_usuario, null);
			holder = new ViewHolder();
			holder.ivImageUser = (ImageView)view.findViewById(R.id.ivImageLUser);
			holder.tvUsuario = (TextView)view.findViewById(R.id.tvUsuarioLUser);
			holder.tvNombrePer = (TextView)view.findViewById(R.id.tvNombrePersonalLUser);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Usuario user = this.getItem(position);
		Personal per = getUserPersonal(user.getIdpersonal());
		if(!per.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file://"+per.getImagen(), holder.ivImageUser, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageUser.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageUser.setImageBitmap(loadedImage);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageUser.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
				}
			});
		}else{
			holder.ivImageUser.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
		}
		holder.tvUsuario.setText("Usuario: "+user.getUsuario());
		holder.tvNombrePer.setText("Nombre: "+per.getNombre());
		return view;
	}
	
	public Personal getUserPersonal(String idpersonal){
		Personal per = null;
		DBDuraznillo db = new DBDuraznillo(activity);
		try {
			db.abrirDB();
			per = db.getPersonal(idpersonal);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return per;
	}
	
}
