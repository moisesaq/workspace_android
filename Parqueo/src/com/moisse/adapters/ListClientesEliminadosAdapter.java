package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.fragments.ListaClientesEliminados;
import com.moisse.modelo.Cliente;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListClientesEliminadosAdapter extends ArrayAdapter<Cliente>{

	private Activity activity;
	private Button btnRestaurar;
	protected ListaClientesEliminados listaClientesEliminados;
	protected HttpClientCustom httpCustom;
	private ImageLoader imageLoader;
	
	public ListClientesEliminadosAdapter(Activity activity, List<Cliente> ListaEliminados, 
											ListaClientesEliminados listClienteEliminados){
		super(activity, R.layout.modelo_lista_cliente_eliminado, ListaEliminados);
		this.activity = activity;
		this.listaClientesEliminados = listClienteEliminados;
	}
	
	public static class ViewHolder{
		ImageView imagen;
		TextView ci;
		TextView nombre_ape;
	}

	@Override
	public Cliente getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if(view==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_lista_cliente_eliminado, null);
			holder = new ViewHolder();
			holder.imagen = (ImageView)view.findViewById(R.id.ivImageClienteLClienteEliminado);
			holder.ci = (TextView)view.findViewById(R.id.tvCILClienteEliminado);
			holder.nombre_ape = (TextView)view.findViewById(R.id.tvNombreApeLClienteEliminado);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		
		final Cliente cliente = this.getItem(position);
		imageLoader = ImageLoader.getInstance();
		if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(cliente.getImagen()).toString();
			imageLoader.displayImage("file://"+path_image, holder.imagen, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.imagen.setImageResource(R.drawable.ic_timer_auto_white_48dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.imagen.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.imagen.setImageResource(R.drawable.ic_timer_auto_white_48dp);
				}
			});
		}else{
			imageLoader.displayImage("drawable://"+R.drawable.ic_client, holder.imagen, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.imagen.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.imagen.setImageResource(R.drawable.ic_history_white_18dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.imagen.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.imagen.setScaleType(ImageView.ScaleType.FIT_CENTER);
					holder.imagen.setImageResource(R.drawable.ic_history_white_18dp);
				}
			});
		}
		holder.ci.setText(new StringBuilder("CI: ").append(cliente.getCi()));
		if(!cliente.getApellido().equals(MyVar.NO_ESPECIFICADO)){
			holder.nombre_ape.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
		}else{
			holder.nombre_ape.setText(new StringBuilder("Nombre: ").append(cliente.getNombre()));
		}
		btnRestaurar = (Button)view.findViewById(R.id.btnRestaurarLClienteEliminado);
		btnRestaurar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Cliente cli = new Cliente(cliente.getIdcliente(), MyVar.NO_ELIMINADO);
				new RestaurarCliente().execute(cli);
			}
		});
		
		return view;
	}

	public class RestaurarCliente extends AsyncTask<Cliente, Void, Boolean>{
		ProgressDialog pd;
		Cliente cliente_res;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Restaurando cliente..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Cliente... params) {
			cliente_res = params[0];
			httpCustom = new HttpClientCustom();
			if(httpCustom.restaurarCliente(this.cliente_res)){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					if(db.restaurarCliente(this.cliente_res.getIdcliente())){
						return true;
					}
					db.closeSQLite();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(activity,"Cliente restaurado",Toast.LENGTH_SHORT).show();
				listaClientesEliminados.cargarListaClientesEliminados();
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo restaurar el cliente, intente mas tarde..!");
				mensaje.show(activity.getFragmentManager(), "tagMsj");
			}
		}		
	}
}
