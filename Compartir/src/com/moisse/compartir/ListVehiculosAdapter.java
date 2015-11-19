package com.moisse.compartir;

import java.io.File;
import java.util.List;

import com.moisse.compartir.R;
import com.moisse.modelo.Vehiculo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListVehiculosAdapter extends ArrayAdapter<Vehiculo>{

	private Activity activity;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public ListVehiculosAdapter(Activity activity, List<Vehiculo> lista_vehiculos){
		super(activity, R.layout.modelo_item_vehiculo, lista_vehiculos);
		this.activity = activity;
		configureOptionDefaultImageLoader();
	}
	
	private void configureOptionDefaultImageLoader() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_car) //imagen mostrar una imagen mientras se carga la imagen
		.showImageForEmptyUri(R.drawable.ic_photo) //imagen para mostrar si la imagen URI es nula o vacia
		.showImageOnFail(R.drawable.ic_launcher) //imagen para mostrar si hay error en la decodificacion
		.cacheInMemory(true) //para cargar la imagen al cache
		.cacheOnDisk(true) //para almacenar en cache la imagen en el disco
		.build();
	}
	
	public static class ViewHolder{
		ImageView imagen;
		TextView placa;
		TextView marca;
		TextView color;
		TextView path;
	}
	@Override
	public Vehiculo getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(view==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_vehiculo, null);
			holder = new ViewHolder();
			holder.imagen = (ImageView)view.findViewById(R.id.ivImageVehiculoLV);
			holder.placa = (TextView)view.findViewById(R.id.tvPlacaLV);
			holder.marca = (TextView)view.findViewById(R.id.tvMarcaLV);
			holder.color = (TextView)view.findViewById(R.id.tvColorLV);
			holder.path = (TextView)view.findViewById(R.id.tvPathImageVehiculoLV);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Vehiculo vehiculo = this.getItem(position);
		if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			File file = new File(vehiculo.getImagen());
			String ruta_imagen = MyVar.FOLDER_IMAGES_VEHICULOS+file.getName();
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file://"+ruta_imagen, holder.imagen, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.imagen.setImageResource(R.drawable.ic_directions_car_white_36dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.imagen.setImageBitmap(loadedImage);
				}
			});
		}else{
			holder.imagen.setImageResource(R.drawable.ic_car);
		}
		
		holder.placa.setText(new StringBuilder("Placa: ").append(vehiculo.getPlaca()));
		holder.marca.setText(new StringBuilder("Marca: ").append(vehiculo.getMarca()));
		holder.color.setText(new StringBuilder("Color: ").append(vehiculo.getColor()));
		holder.path.setText(new StringBuilder("Path: ").append(vehiculo.getImagen()));
		
		return view;
	}
}
