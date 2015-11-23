package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
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

public class ListResguardoAdapter extends ArrayAdapter<Resguardo>{

	private Activity miActivity;
	private List<Resguardo> lista;
	private ImageLoader imageLoader;
	public ListResguardoAdapter(Activity activity, List<Resguardo> objects) {
		super(activity, R.layout.modelo_lista_resguardo, objects);
		this.miActivity = activity;
		this.lista= objects;
	}
	
	public class ViewHolder{
		ImageView ivImageVehiculo;
		TextView tvNum_Carril;
		TextView tvPlaca;
		TextView tvCostoTotal;
		TextView tvHoraFechaE;
		TextView tvHoraFechaS;
	}
	
	@Override
	public Resguardo getItem(int position) {
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = miActivity.getLayoutInflater().inflate(R.layout.modelo_lista_resguardo, null);
			holder = new ViewHolder();
			holder.ivImageVehiculo = (ImageView)view.findViewById(R.id.ivVehiculoLResguardo);
			holder.tvNum_Carril = (TextView)view.findViewById(R.id.tvNumCarrilLResguardo);
			holder.tvPlaca = (TextView)view.findViewById(R.id.tvPlacaLResguardo);
			holder.tvCostoTotal = (TextView)view.findViewById(R.id.tvCostoTotalLResguardo);
			holder.tvHoraFechaE = (TextView)view.findViewById(R.id.tvHoraFechaEntradaLResguardo);
			holder.tvHoraFechaS = (TextView)view.findViewById(R.id.tvHoraFechaSalidaLResguardo);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Resguardo resg = lista.get(position);
		imageLoader = ImageLoader.getInstance();
		Vehiculo vehi = getVehiculo(resg.getIdvehiculo());
		if(!vehi.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehi.getImagen()).toString();
			imageLoader.displayImage("file://"+path_image, holder.ivImageVehiculo, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageVehiculo.setImageResource(R.drawable.ic_directions_car_white_48dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageVehiculo.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageVehiculo.setImageResource(R.drawable.ic_directions_car_white_48dp);
				}
			});
		}else{
			imageLoader.displayImage("drawable://"+R.drawable.ic_car, holder.ivImageVehiculo, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageVehiculo.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageVehiculo.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageVehiculo.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImageVehiculo.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageVehiculo.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageVehiculo.setImageResource(R.drawable.ic_history_white_18dp);
				}
			});
		}
		holder.tvNum_Carril.setText(new StringBuffer().append("No. carril: ").append(getNumCarril(resg.getIdcarril())));
		holder.tvPlaca.setText(new StringBuffer().append("Placa: ").append(vehi.getPlaca()));
		holder.tvCostoTotal.setText(new StringBuffer().append("Costo total: ").append(resg.getCostoTotal()));
		holder.tvHoraFechaE.setText(new StringBuffer().append("Ingreso: ").append(resg.getHoraE()).append(" ")
														.append(MyVar.FORMAT_FECHA_2.format(resg.getFechaE())));
		holder.tvHoraFechaS.setText(new StringBuffer().append("Salida: ").append(resg.getHoraS()).append(" ")
														.append(MyVar.FORMAT_FECHA_2.format(resg.getFechaS())));
		return view;
	}

	private Vehiculo getVehiculo(String idvehiculo){
		DBParqueo db = new DBParqueo(miActivity);
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
	
	private int getNumCarril(String idcarril){
		DBParqueo db = new DBParqueo(miActivity);
		int carril = 0;
		try {
			db.openSQLite();
			carril = db.getCarril(idcarril).getNum_carril();
			db.closeSQLite();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return carril;
	}
}
