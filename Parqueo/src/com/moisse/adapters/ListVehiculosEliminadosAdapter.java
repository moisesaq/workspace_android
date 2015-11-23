package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.fragments.ListaVehiculosEliminados;
import com.moisse.modelo.Vehiculo;
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

public class ListVehiculosEliminadosAdapter extends ArrayAdapter<Vehiculo> implements OnClickListener{

	private Activity activity;
	private Button btnRestaurar;
	private Vehiculo vehi;
	protected ListaVehiculosEliminados listaVehiculosEliminados;
	protected HttpClientCustom httpCustom;
	private ImageLoader imageLoader;
	
	public ListVehiculosEliminadosAdapter(Activity activity, List<Vehiculo> listaEliminados, 
											ListaVehiculosEliminados listaVehiculosEliminados){
		super(activity, R.layout.modelo_lista_vehiculo_eliminado, listaEliminados);
		this.activity = activity;
		this.listaVehiculosEliminados = listaVehiculosEliminados;
	}
	
	public class ViewHolder{
		ImageView ivImageVehiculo;
		TextView tvPlaca;
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
			view = activity.getLayoutInflater().inflate(R.layout.modelo_lista_vehiculo_eliminado, null);
			holder = new ViewHolder();
			holder.ivImageVehiculo = (ImageView)view.findViewById(R.id.ivImageVehiculoLVehiculoEliminado);
			holder.tvPlaca = (TextView)view.findViewById(R.id.tvPlacaLVehiculoEliminado);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		vehi = this.getItem(position);
		imageLoader = ImageLoader.getInstance();
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
		holder.tvPlaca.setText(new StringBuilder("Placa: ").append(vehi.getPlaca()));
		btnRestaurar = (Button)view.findViewById(R.id.btnRestaurarLVehiculoEliminado);
		btnRestaurar.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnRestaurar.getId()){
			Vehiculo vehiculo = new Vehiculo(vehi.getIdvehiculo(), MyVar.NO_ELIMINADO);
			new RestaurarVehiculo().execute(vehiculo);
		}
	}
	
	public class RestaurarVehiculo extends AsyncTask<Vehiculo, Void, Boolean>{
		ProgressDialog pd;
		Vehiculo vehiculo_res;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Restaurando vehiculo..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Vehiculo... params) {
			vehiculo_res = params[0];
			httpCustom = new HttpClientCustom();
			if(httpCustom.restaurarVehiculo(this.vehiculo_res)){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					if(db.restaurarVehiculo(this.vehiculo_res.getIdvehiculo())){
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
				Toast.makeText(activity,"Vehiculo restaurado",Toast.LENGTH_LONG).show();
				listaVehiculosEliminados.cargarListaVehiculosEliminados();
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo restaurar el vehiculo, intente mas tarde.");
				mensaje.show(activity.getFragmentManager(), "tagMsj");
			}
		}	
	}

}
