package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.modelo.Usuario;
import com.moisse.others.MyVar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListUsuarioAdapter extends ArrayAdapter<Usuario>{

	private Activity activity;
	private ImageLoader imageLoader;
	public ListUsuarioAdapter(Activity activity, List<Usuario> listUsuario){
		super(activity,R.layout.modelo_lista_usuario,listUsuario);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageUsuario;
		TextView tvUsuario;
		TextView tvEstado;
	}
	@Override
	public Usuario getItem(int position){
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_lista_usuario, null);
			holder = new ViewHolder();
			holder.ivImageUsuario = (ImageView)view.findViewById(R.id.ivImageUserLUsuario);
			holder.tvUsuario = (TextView)view.findViewById(R.id.tvUserLUsuario);
			holder.tvEstado = (TextView)view.findViewById(R.id.tvEstadoLUsuario);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Usuario u = this.getItem(position);
		imageLoader = ImageLoader.getInstance();
		if(!u.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(u.getImagen()).toString();
			
			imageLoader.displayImage("file://"+path_image, holder.ivImageUsuario, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageUsuario.setImageResource(R.drawable.ic_timer_auto_white_48dp);
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageUsuario.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageUsuario.setImageResource(R.drawable.ic_supervisor_account_white_36dp);
				}
			});
		}else{
			imageLoader.displayImage("drawable://"+R.drawable.ic_supervisor_account_white_48dp, holder.ivImageUsuario, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageUsuario.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageUsuario.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImageUsuario.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageUsuario.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageUsuario.setImageResource(R.drawable.ic_history_white_18dp);
				}
			});
		}
		holder.tvUsuario.setText(new StringBuilder("Usuario: ").append(u.getNombre_usuario()));
		if(u.getCargo()==MyVar.CARGO_ADMIN){
			holder.tvEstado.setText(new StringBuilder("Administrador"));
		}else if(u.getCargo()==MyVar.CARGO_USUARIO){
			if(u.getEstado()==MyVar.NO_ELIMINADO){
				holder.tvEstado.setText(new StringBuilder("Habilitado"));
			}else{
				holder.tvEstado.setText(new StringBuilder("Inhabilitado"));
				//holder.tvEstado.setTextColor(holder.tvEstado.getContext().getResources().getColor(R.color.ROJO));
			}
		}
		return view;
	}
	
}
