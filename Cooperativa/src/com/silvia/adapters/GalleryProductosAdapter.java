package com.silvia.adapters;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Producto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryProductosAdapter extends ArrayAdapter<Producto>{

	private Activity activity;
	private ImageLoader imageLoader;
	
	public GalleryProductosAdapter(Activity activity, List<Producto> lista_prod){
		super(activity, R.layout.modelo_item_prod_horizontal, lista_prod);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageProd;
		TextView tvNombreProd;
		TextView tvPrecio;
	}
	
	@Override
	public Producto getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_prod_horizontal, null);
			holder = new ViewHolder();
			holder.ivImageProd = (ImageView)view.findViewById(R.id.ivImageProdHorizontalLProd);
			holder.tvNombreProd = (TextView)view.findViewById(R.id.tvNombreHorizontalLProd);
			holder.tvPrecio = (TextView)view.findViewById(R.id.tvPrecioHorizontalLProd);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Producto prod = this.getItem(position);
		if(!prod.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(prod.getImagen()).toString();
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file://"+pathImagen, holder.ivImageProd, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageProd.setImageResource(R.drawable.ic_product_plant_128);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageProd.setImageBitmap(loadedImage);
					holder.ivImageProd.setPadding(0, 0, 0, 0);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageProd.setImageResource(R.drawable.ic_product_plant_128);
				}
			});
		}else{
			holder.ivImageProd.setImageResource(R.drawable.ic_product_plant_128);
		}
		holder.tvNombreProd.setText(prod.getNombre_prod());
		holder.tvPrecio.setText("Precio: "+prod.getPrecio()+" Bs.");
		return view;
	}
}
