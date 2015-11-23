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
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Maquinaria;

public class ListaMaqAdapter extends ArrayAdapter<Maquinaria>{

	private Activity activity;
	private ImageLoader imageLoader;
	
	public ListaMaqAdapter(Activity activity, List<Maquinaria> lista_maq){
		super(activity, R.layout.modelo_item_lista_maq, lista_maq);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageMaq;
		TextView tvPlaca;
		TextView tvDescripcion;
		TextView tvCapacidad;
	}
	
	@Override
	public Maquinaria getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_lista_maq, null);
			holder = new ViewHolder();
			holder.ivImageMaq = (ImageView)view.findViewById(R.id.ivImageLMaq);
			holder.tvPlaca = (TextView)view.findViewById(R.id.tvPlacaLMaq);
			holder.tvDescripcion = (TextView)view.findViewById(R.id.tvDescripcionLMaq);
			holder.tvCapacidad = (TextView)view.findViewById(R.id.tvCapacidadLMaq);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Maquinaria maq = this.getItem(position);
		if(!maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString();
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file://"+pathImagen, holder.ivImageMaq, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageMaq.setImageResource(R.drawable.ic_excavator_128);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageMaq.setImageBitmap(loadedImage);
					holder.ivImageMaq.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImageMaq.setPadding(0, 0, 0, 0);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageMaq.setImageResource(R.drawable.ic_excavator_128);
				}
			});
		}else{
			holder.ivImageMaq.setImageResource(R.drawable.ic_excavator_128);
		}
		holder.tvPlaca.setText(maq.getPlaca());
		holder.tvDescripcion.setText(maq.getDescripcion());
		holder.tvCapacidad.setText(new StringBuilder().append(maq.getCapacidad()).append(" Cubos"));
		return view;
	}
	
}
