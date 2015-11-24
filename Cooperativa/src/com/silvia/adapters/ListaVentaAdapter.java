package com.silvia.adapters;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.Venta;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaVentaAdapter extends ArrayAdapter<Venta>{

	private Activity activity;
	private ImageLoader imageLoader;
	
	public ListaVentaAdapter(Activity activity, List<Venta> lista_venta){
		super(activity, R.layout.modelo_item_venta, lista_venta);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageCliente;
		TextView tvNombreCliente;
		TextView tvCICliente;
		TextView tvFechaVenta;;
		TextView tvCostoTotal;
	}
	
	@Override
	public Venta getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_venta, null);
			holder = new ViewHolder();
			holder.ivImageCliente = (ImageView)view.findViewById(R.id.ivImageClienteLVenta);
			holder.tvNombreCliente = (TextView)view.findViewById(R.id.tvNombreClienteLVenta);
			holder.tvCICliente = (TextView)view.findViewById(R.id.tvCIClienteLVenta);
			holder.tvFechaVenta = (TextView)view.findViewById(R.id.tvFechaVentaLVenta);
			holder.tvCostoTotal = (TextView)view.findViewById(R.id.tvCostoTotalLVenta);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Venta venta = this.getItem(position);
		Cliente cliente = getCliente(venta.getIdcliente());
		imageLoader = ImageLoader.getInstance();
		if(!cliente.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(cliente.getImagen()).toString();
			imageLoader.displayImage("file://"+pathImagen, holder.ivImageCliente, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageCliente.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageCliente.setImageBitmap(loadedImage);
					holder.ivImageCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImageCliente.setPadding(0, 0, 0, 0);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageCliente.setImageResource(R.drawable.ic_client_128);
				}
			});
		}else{
			imageLoader.displayImage("drawable://"+R.drawable.ic_client_128, holder.ivImageCliente, new SimpleImageLoadingListener(){
				
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageCliente.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageCliente.setImageBitmap(loadedImage);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason){
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageCliente.setImageResource(R.drawable.ic_client_128);
				}
			});
		}
		holder.tvNombreCliente.setText(cliente.getNombre());
		if(!cliente.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			holder.tvNombreCliente.setText(new StringBuilder().append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
		}
		holder.tvCICliente.setText(cliente.getCi());
		holder.tvFechaVenta.setText(new StringBuilder().append(Variables.FORMAT_FECHA_2.format(venta.getFecha_venta())));
		holder.tvCostoTotal.setText(new StringBuilder().append(venta.getCosto_total()).append(" Bs."));
		return view;
	}
	
	public Cliente getCliente(String idcliente){
		Cliente cliente = null;
		DBDuraznillo db = new DBDuraznillo(getContext());
		try {
			db.abrirDB();
			cliente = db.getCliente(idcliente);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
}