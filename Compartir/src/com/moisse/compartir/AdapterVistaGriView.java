package com.moisse.compartir;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.moisse.modelo.Vehiculo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AdapterVistaGriView extends BaseAdapter{

	private Context context;
	public List<Vehiculo> lista_vehiculos;
	public Activity activity;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public AdapterVistaGriView(Context context, List<Vehiculo> lista, Activity activity){
		this.context = context;
		this.lista_vehiculos = lista;
		this.activity = activity;
		configureOptionDefaultImageLoader();	
		//imageLoader = ImageLoader.getInstance();
	}
	
	private void configureOptionDefaultImageLoader() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_car) //imagen mostrar una imagen mientras se carga la imagen
		.showImageForEmptyUri(R.drawable.ic_photo) //imagen para mostrar si la imagen URI es nula o vacia
		.showImageOnFail(R.drawable.ic_launcher) //imagen para mostrar si hay error en la decodificacion
		.cacheInMemory(true) //para cargar la imagen al cache
		.cacheOnDisk(true) //para almacenar en cache la imagen en el disco
		//.imageScaleType(ImageScaleType.EXACTLY)
		//.considerExifParams(true) //si imageloader debe considerar loa paramentros EXIF de la imagenes JPEG
		//.bitmapConfig(Bitmap.Config.RGB_565) //establece el Bitmap.Config a RGB_565 donde cada píxel se almacena en 2 bytes 
											//y sólo los canales RGB están codificados (menor uso de memoria)
		//.displayer(new RoundedBitmapDisplayer(2)) //<--- X alguna razon con esto no me funciona
		.build();
	}
	
	public class ViewHolder{
		TextView placa;
		ImageView imagen;
		ProgressBar cargar;
	}
	
	@Override
	public int getCount() {
		return lista_vehiculos.size();
	}

	@Override
	public Object getItem(int position) {
		return lista_vehiculos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if(view==null){
			view = activity.getLayoutInflater().inflate(R.layout.item_gridview, null);
			holder = new ViewHolder();
			holder.placa = (TextView)view.findViewById(R.id.tvPlacaVehiculoGV);
			holder.imagen = (ImageView)view.findViewById(R.id.ivImagenVehiculoGV);
			//holder.imagen.setVisibility(View.VISIBLE);
			holder.cargar = (ProgressBar)view.findViewById(R.id.pbCargarGV);
			//holder.cargar.setVisibility(View.INVISIBLE);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Vehiculo vehiculo = lista_vehiculos.get(position);
		holder.placa.setText(new StringBuilder("Placa: ").append(vehiculo.getPlaca()));//;new File(vehiculo.getImagen()).getName())
		
		if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			File fileImage = new File(vehiculo.getImagen());
			String ruta_image = MyVar.RUTA_IMAGES_SERVER+fileImage.getName();
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(ruta_image, holder.imagen, options, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.cargar.setVisibility(View.VISIBLE);
					holder.imagen.setVisibility(View.INVISIBLE);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					//for para probar  //// en esta parte implementar para que se descargue solo desde el servidor
					int cont = 0;
					for (int i = 0; i < 1000000; i++) {
						cont++;
					}
					holder.cargar.setVisibility(View.INVISIBLE);
					holder.imagen.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
				}
				
			});
			//PARA CARGAR IMAGEN DESDE SD LOCAL
//			Bitmap bitmap = BitmapFactory.decodeFile(vehiculo.getImagen());
//			if(bitmap!=null){
//				holder.imagen.setImageBitmap(bitmap);
//			}
		}

		return view;
	}	
	
	//Esto no funciona xq tiene q ejecutarse en un 2do hilo
	public Bitmap getImagen(String name_imagen){
		String ruta_imagen = "http://192.168.1.100/android/images_subidas/";
		URL url_image;
		Bitmap bitmap_image = null;
		try{
			url_image = new URL(ruta_imagen+name_imagen);
			HttpURLConnection conn = (HttpURLConnection)url_image.openConnection();
			conn.connect();
			//if(conn!=null){
				bitmap_image = BitmapFactory.decodeStream(conn.getInputStream());
			//}
		}catch(IOException e){
			e.printStackTrace();
		}
		return bitmap_image;
	}

	//Esto tampoco funciona xq no es l forma correcta mejor es utilizar universal-image-loader
	public class CargarImagen extends AsyncTask<Vehiculo, Void, Bitmap>{
		String ruta_imagen = "http://192.168.1.100/android/images_subidas/";
		ViewHolder holder;
		Vehiculo vehiculo;
		URL url_image;
		Bitmap bitmap_image;
		
		public void setViewHolder(ViewHolder viewHolder){
			this.holder = viewHolder;  
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			holder.cargar.setVisibility(View.VISIBLE);
			holder.imagen.setVisibility(View.INVISIBLE);
		}
		@Override
		protected Bitmap doInBackground(Vehiculo... params) {
			vehiculo = params[0];
			String nombre_imagen = new File(vehiculo.getImagen()).getName();
			try{
				url_image = new URL(ruta_imagen+nombre_imagen);
				HttpURLConnection conn = (HttpURLConnection)url_image.openConnection();
				conn.connect();
				//if(conn!=null){
					bitmap_image = BitmapFactory.decodeStream(conn.getInputStream());
				//}
				
			}catch(IOException e){
				e.printStackTrace();
			}
			return bitmap_image;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result!=null){
				holder.cargar.setVisibility(View.INVISIBLE);
				holder.imagen.setImageBitmap(result);
				holder.imagen.setVisibility(View.VISIBLE);
			}else{
//				holder.cargar.setVisibility(View.INVISIBLE);
//				holder.imagen.setImageResource(R.drawable.ic_car);
//				holder.imagen.setVisibility(View.VISIBLE);
			}
		}
	}
}
