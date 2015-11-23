package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Cliente;
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

public class ListVehiculoAdapter extends ArrayAdapter<Vehiculo>{

	private Activity activity;
	private ImageLoader imageLoader;
	private String id_client_default;
	public ListVehiculoAdapter(Activity activity, List<Vehiculo> listVehiculo, String id_client_default){
		super(activity, R.layout.modelo_lista_vehiculo, listVehiculo);
		this.activity = activity;
		this.id_client_default = id_client_default;
	}
	
	public class ViewHolder{
		ImageView ivImageVehiculo;
		TextView tvPlaca;
		TextView tvProp;
		TextView tvTipoProp;
	}
	@Override
	public Vehiculo getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_lista_vehiculo, null);
			holder = new ViewHolder();
			holder.ivImageVehiculo = (ImageView)view.findViewById(R.id.ivImageVehiculoLVehiculo);
			holder.tvPlaca = (TextView)view.findViewById(R.id.tvPlacaLVehiculo);
			holder.tvProp = (TextView)view.findViewById(R.id.tvPropLVehiculo);
			holder.tvTipoProp = (TextView)view.findViewById(R.id.tvTipoPropLVehiculo);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Vehiculo vehi = this.getItem(position);
		
		imageLoader = ImageLoader.getInstance();
		if(!vehi.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehi.getImagen()).toString();
			imageLoader.displayImage("file://"+path_image, holder.ivImageVehiculo, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageVehiculo.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageVehiculo.setImageResource(R.drawable.ic_directions_car_white_48dp);
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
		holder.tvPlaca.setText(new StringBuilder("Placa: ").append(vehi.getPlaca()));
		if(!vehi.getIdcliente().equals(id_client_default)){
			Cliente cli = getCliente(vehi.getIdcliente());
			if(cli.getApellido().equals(MyVar.NO_ESPECIFICADO)){
				holder.tvProp.setText(new StringBuilder("Prop.: ").append(cli.getNombre()));
			}else{
				holder.tvProp.setText(new StringBuilder("Prop.: ").append(cli.getNombre()).append(" ").append(cli.getApellido()));
			}
			int tipo = cli.getTipo();
			holder.tvTipoProp.setVisibility(View.VISIBLE);
			if(tipo==MyVar.CLIENTE_OCASIONAL){
				holder.tvTipoProp.setText(new StringBuilder("Tipo: Cliente ocasional"));
			}else if (tipo==MyVar.CLIENTE_CONTRATO_NOCTURNO) {
				holder.tvTipoProp.setText(new StringBuilder("Tipo: Cliente con contrato nocturno"));
			}else if(cli.getTipo()==MyVar.CLIENTE_CONTRATO_DIURNO){
				holder.tvTipoProp.setText(new StringBuilder("Tipo: Cliente con contrato diurno"));
			}else if(cli.getTipo()==MyVar.CLIENTE_CONTRATO_DIA_COMPLETO){
				holder.tvTipoProp.setText(new StringBuilder("Tipo: Cliente con contrato dia completo"));
			}
		}else{
			//holder.tvTipoProp.setVisibility(View.GONE);
			holder.tvTipoProp.setText(new StringBuilder("Vehiculo Ocasional"));
			holder.tvProp.setText(new StringBuilder("Prop.: ").append(MyVar.SIN_PROPIETARIO));
		}
		
		return view;
	}
	
	public Cliente getCliente(String idcliente){
		Cliente cliente = null;
		DBParqueo db = new DBParqueo(activity);
		try {
			db.openSQLite();
			cliente = db.getCliente(idcliente);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
}
