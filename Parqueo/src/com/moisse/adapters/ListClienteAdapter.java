package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.modelo.Cliente;
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

public class ListClienteAdapter extends ArrayAdapter<Cliente>{

	private Activity activity;
	private ImageLoader imageLoader;
	public ListClienteAdapter(Activity activity, List<Cliente> listCliente){
		super(activity,R.layout.modelo_lista_cliente,listCliente);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImageCliente;
		TextView tvCI;
		TextView tvNombre;
		TextView tvTipo;
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
			view = activity.getLayoutInflater().inflate(R.layout.modelo_lista_cliente, null);
			holder = new ViewHolder();
			holder.ivImageCliente = (ImageView)view.findViewById(R.id.ivImageClienteLCliente);
			holder.tvCI = (TextView)view.findViewById(R.id.tvCILCliente);
			holder.tvNombre = (TextView)view.findViewById(R.id.tvNombreLCliente);
			holder.tvTipo = (TextView)view.findViewById(R.id.tvTipoLCliente);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Cliente cliente = this.getItem(position);
		imageLoader = ImageLoader.getInstance();
		if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString();
			imageLoader.displayImage("file://"+path_image, holder.ivImageCliente, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageCliente.setImageResource(R.drawable.ic_timer_auto_white_48dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageCliente.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageCliente.setImageResource(R.drawable.ic_timer_auto_white_48dp);
				}
			});
		}else{
			imageLoader.displayImage("drawable://"+R.drawable.ic_client, holder.ivImageCliente, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.ivImageCliente.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageCliente.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.ivImageCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImageCliente.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.ivImageCliente.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.ivImageCliente.setImageResource(R.drawable.ic_history_white_18dp);
				}
			});
		}
		holder.tvCI.setText(new StringBuilder("CI: ").append(cliente.getCi()));
		if(!cliente.getApellido().equals(MyVar.NO_ESPECIFICADO)){
			holder.tvNombre.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
		}else{
			holder.tvNombre.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
		}
		int tipo = cliente.getTipo();
		if(tipo==MyVar.CLIENTE_OCASIONAL){
			holder.tvTipo.setText(new StringBuilder("Tipo: Cliente ocasional"));
		}else if (tipo==MyVar.CLIENTE_CONTRATO_NOCTURNO) {
			holder.tvTipo.setText(new StringBuilder("Tipo: Cliente con contrato nocturno"));
		}else if(tipo==MyVar.CLIENTE_CONTRATO_DIURNO){
			holder.tvTipo.setText(new StringBuilder("Tipo: Cliente con contrato diurno"));
		}else if(tipo==MyVar.CLIENTE_CONTRATO_DIA_COMPLETO){
			holder.tvTipo.setText(new StringBuilder("Tipo: Cliente con contrato dia completo"));
		}
				
		return view;
	}
}
