package com.silvia.adapters;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cliente;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaClienteAdapter extends ArrayAdapter<Cliente>{

	private Activity activity;
	private ImageLoader imageLoader;
	public ListaClienteAdapter(Activity activity, List<Cliente> lista_clientes){
		super(activity, R.layout.modelo_item_lista_cliente, lista_clientes);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageCliente;
		TextView tvCI;
		TextView tvNombre;
		TextView tvApellido;
	}
	
	@Override
	public Cliente getItem(int position){
		return super.getItem(position);
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_lista_cliente, null);
			holder = new ViewHolder();
			holder.ivImageCliente = (ImageView)view.findViewById(R.id.ivImageClienteLCliente);
			holder.tvCI = (TextView)view.findViewById(R.id.tvCILCliente);
			holder.tvNombre = (TextView)view.findViewById(R.id.tvNombreLCliente);
			holder.tvApellido = (TextView)view.findViewById(R.id.tvApellidoLCliente);
			view.setTag(holder);
		}else{
			holder =(ViewHolder)view.getTag();
		}
		Cliente cliente = this.getItem(position);
		String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(cliente.getImagen()).toString();
		imageLoader= ImageLoader.getInstance();
		if(!cliente.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			
			imageLoader.displayImage("file://"+pathImagen, holder.ivImageCliente, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view){
					super.onLoadingStarted(imageUri, view);
					holder.ivImageCliente.setImageResource(R.drawable.ic_client_128);
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
		holder.tvCI.setText(cliente.getCi());
		holder.tvNombre.setText(cliente.getNombre());
		holder.tvApellido.setText(cliente.getApellido());
		
		return view;
	}
}
