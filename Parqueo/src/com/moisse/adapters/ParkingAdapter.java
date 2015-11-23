package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.MyVar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingAdapter extends BaseAdapter{
	
	Context miContext;
	List<Carril> carriles;
	Activity activity;
	ProgressDialog pd;
	private ImageLoader imageLoader;

	public ParkingAdapter(Context context, List<Carril> carriles, Activity acticity){
		this.miContext=context;
		this.carriles=carriles;
		this.activity = acticity;
	}
	
	public class ViewHolder{
		TextView tv_placa_carril;
		ImageView imgv_vehiculo_carril;
	}
	
	@Override
	public int getCount() {
		return carriles.size();
	}

	@Override
	public Object getItem(int position) {
		return carriles.get(position);
	}

	@Override
	public long getItemId(int position) {
		//Se esta retornando el numero carril x que el id es string
		return carriles.get(position).getNum_carril();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_parking, null);
			holder = new ViewHolder();
			holder.tv_placa_carril = (TextView)view.findViewById(R.id.tvPlacaCarril);
			holder.imgv_vehiculo_carril = (ImageView)view.findViewById(R.id.ivVehiculoCarril);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		
		Carril carril = carriles.get(position);
		imageLoader = ImageLoader.getInstance();
		if(carril.getDisponible().equals(MyVar.SI)){
			holder.tv_placa_carril.setText(new StringBuilder("Carril ").append(carril.getNum_carril()));
			imageLoader.displayImage("drawable://"+R.drawable.ic_carril_parking, holder.imgv_vehiculo_carril, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.imgv_vehiculo_carril.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.imgv_vehiculo_carril.setImageResource(R.drawable.ic_insert_emoticon_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.imgv_vehiculo_carril.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.imgv_vehiculo_carril.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.imgv_vehiculo_carril.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.imgv_vehiculo_carril.setImageResource(R.drawable.ic_insert_emoticon_white_18dp);
				}
			});
			
		}else{
			Vehiculo vehi = getVehiculo(carril.getDisponible());
			if(vehi!=null){
				holder.tv_placa_carril.setText(new StringBuilder(vehi.getPlaca()));
				if(!vehi.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehi.getImagen()).toString();
					imageLoader.displayImage("file://"+path_image, holder.imgv_vehiculo_carril, new SimpleImageLoadingListener(){
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							super.onLoadingStarted(imageUri, view);
							holder.imgv_vehiculo_carril.setImageResource(R.drawable.ic_car);
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							holder.imgv_vehiculo_carril.setImageBitmap(loadedImage);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							super.onLoadingFailed(imageUri, view, failReason);
							holder.imgv_vehiculo_carril.setImageResource(R.drawable.ic_car);
						}
					});
				}else{
					holder.imgv_vehiculo_carril.setImageResource(R.drawable.ic_car);
				}
			}else{
				Toast.makeText(activity, "Error no se encontro vehiculo", Toast.LENGTH_SHORT).show();
			}
			
		}
		return view;
	}
	
	public Vehiculo getVehiculo(String idvehiculo){
		DBParqueo db = new DBParqueo(miContext);
		Vehiculo vehiculo = null;
		try {
			db.openSQLite();
			vehiculo = db.getVehiculo(idvehiculo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehiculo;
	}

}
