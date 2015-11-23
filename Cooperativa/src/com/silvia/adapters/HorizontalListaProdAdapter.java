package com.silvia.adapters;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Producto;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListaProdAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	public List<Producto> lista_prod;
	
	public HorizontalListaProdAdapter(Context context, List<Producto> lista_prod){
		this.inflater = LayoutInflater.from(context);
		this.lista_prod = lista_prod;
	}
	
	@Override
	public int getCount() {
		return lista_prod.size();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	@Override
	public Object getItem(int position) {
		return lista_prod.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = inflater.inflate(R.layout.modelo_item_prod_horizontal, null);
			holder = new ViewHolder();
			holder.ivImageProd = (ImageView)view.findViewById(R.id.ivImageProdHorizontalLProd);
			holder.tvNombreProd = (TextView)view.findViewById(R.id.tvNombreHorizontalLProd);
			holder.tvPrecio = (TextView)view.findViewById(R.id.tvPrecioHorizontalLProd);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Producto prod = lista_prod.get(position);
		if(!prod.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file://"+prod.getImagen(), holder.ivImageProd, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageProd.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageProd.setImageBitmap(loadedImage);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageProd.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
				}
			});
		}else{
			holder.ivImageProd.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
		}
		holder.tvNombreProd.setText(prod.getNombre_prod());
		holder.tvPrecio.setText("Precio: "+prod.getPrecio()+" Bs.");
		return view;
	}
	
	public class ViewHolder{
		ImageView ivImageProd;
		TextView tvNombreProd;
		TextView tvPrecio;
	}
	
}
