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
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Personal;

public class ListaPersonalAdapter extends ArrayAdapter<Personal>{

	private Activity activity;
	private ImageLoader imageLoader;
	
	public ListaPersonalAdapter(Activity activity, List<Personal> lista_personal){
		super(activity, R.layout.modelo_item_lista_personal, lista_personal);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImage;
		TextView tvCI;
		TextView tvNombre;
		TextView tvCargo;
		TextView tvUsuario;
	}
	
	@Override
	public Personal getItem(int position){
		return super.getItem(position);
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_lista_personal, null);
			holder = new ViewHolder();
			holder.ivImage = (ImageView)view.findViewById(R.id.ivImageLPersonal);
			holder.tvCI = (TextView)view.findViewById(R.id.tvCILPersonal);
			holder.tvNombre = (TextView)view.findViewById(R.id.tvNombreLPersonal);
			holder.tvCargo = (TextView)view.findViewById(R.id.tvCargoLPersonal);
			holder.tvUsuario = (TextView)view.findViewById(R.id.tvEsUsuarioLPersonal);
			view.setTag(holder);
		}else{
			holder =(ViewHolder)view.getTag();
		}
		Personal personal = this.getItem(position);
		imageLoader= ImageLoader.getInstance();
		if(!personal.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(personal.getImagen()).toString();
			imageLoader.displayImage("file://"+pathImagen, holder.ivImage, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImage.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImage.setImageBitmap(loadedImage);
					holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImage.setPadding(0, 0, 0, 0);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImage.setImageResource(R.drawable.ic_employees_128);
				}
			});
		}else{
			imageLoader.displayImage("drawable://"+R.drawable.ic_employees_128, holder.ivImage, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImage.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImage.setImageBitmap(loadedImage);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImage.setImageResource(R.drawable.ic_employees_128);
				}
			});
		}
		holder.tvCI.setText(personal.getCi());
		if(isUser(personal.getIdpersonal())){
			holder.tvUsuario.setVisibility(View.VISIBLE);
		}else{
			holder.tvUsuario.setVisibility(View.INVISIBLE);
		}

		if(personal.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			holder.tvNombre.setText(personal.getNombre());
		}else{
			holder.tvNombre.setText(new StringBuilder(personal.getNombre()).append(" ").append(personal.getApellido()));
		}
		holder.tvCargo.setText(new StringBuilder(getCargo(personal.getIdcargo()).getOcupacion()));
		
		return view;
	}
	
	public Cargo getCargo(String idcargo){
		Cargo cargo = null;
		DBDuraznillo db = new DBDuraznillo(activity);
		try {
			db.abrirDB();
			cargo = db.getCargo(idcargo);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cargo;
	}
	
	public boolean isUser(String idpersonal){
		DBDuraznillo db = new DBDuraznillo(activity);
		try {
			db.abrirDB();
			if(db.personalEsUsuario(idpersonal)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
