package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.moisse.compartir.ListVehiculosAdapter;
import com.moisse.compartir.MyVar;
import com.moisse.compartir.R;
import com.moisse.database.DBVehiculo;
import com.moisse.database.HttpClientCustom;
import com.moisse.modelo.Vehiculo;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ListaVehiculos extends Fragment{
	
	ListView lvListaVehiculos;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.lista_vehiculos, container, false);
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Lista vehiculos");
		setHasOptionsMenu(true);
		lvListaVehiculos = (ListView)v.findViewById(R.id.lvListaVehiculos);
		cargarListaVehiculos();
		return v;
	}

	private void cargarListaVehiculos() {
		DBVehiculo db = new DBVehiculo(getActivity());
		try{
			db.openDB();
			List<Vehiculo> list_vehiculo = db.getAllVehiculo();
			ListVehiculosAdapter adapter = new ListVehiculosAdapter(getActivity(), list_vehiculo);
			lvListaVehiculos.setAdapter(adapter);
			db.closeDB();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.actin_update).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.actin_update){
			new ActualizarDatos().execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class ActualizarDatos extends AsyncTask<Void, Void, Boolean>{
		ProgressDialog pd;
		List<Vehiculo> lista_vehiculos = null;
		HttpClientCustom httpCustom;
		
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando datos..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		
		@Override
		public Boolean doInBackground(Void... params){
			httpCustom = new HttpClientCustom();
			lista_vehiculos = httpCustom.cargarVehiculos();
			
			if(lista_vehiculos!=null && lista_vehiculos.size()!=0){
				DBVehiculo db = new DBVehiculo(getActivity());
				try {
					db.openDB();
					for (int i = 0; i < lista_vehiculos.size(); i++) {
						Vehiculo vehiculo = lista_vehiculos.get(i);
						if(db.verificarVehiculo(vehiculo)){
							db.actualizarVehiculo(vehiculo);
						}else{
							db.insertarVehiculo(vehiculo);
						}
						if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							File fileImageVehiculo = new File(vehiculo.getImagen());
							if(!fileImageVehiculo.exists()){
								downloadSaveImage(fileImageVehiculo);
							}
						}
					}
					db.closeDB();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
		
		@Override
		public void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Datos actualizados exitosamente "+lista_vehiculos.size(), Toast.LENGTH_SHORT).show();
				cargarListaVehiculos();
			}else{
				Toast.makeText(getActivity(), "No se pudo actualizar datos", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void downloadSaveImage(File fileImage){
		File folder_images = new File(MyVar.FOLDER_IMAGES_VEHICULOS);
		folder_images.mkdirs();
		try {
			URL url_image = new URL(MyVar.RUTA_IMAGES_SERVER+fileImage.getName());
			HttpURLConnection conn = (HttpURLConnection)url_image.openConnection();
			Bitmap myBitmap = BitmapFactory.decodeStream(conn.getInputStream());
			if(myBitmap!=null){
				File file = new File(folder_images, fileImage.getName());
				FileOutputStream fos = new FileOutputStream(file);
				myBitmap.compress(CompressFormat.JPEG, 90, fos);
				fos.flush();
				scanImage(file.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void scanImage(final String imagen){
		new MediaScannerConnectionClient() {
			private MediaScannerConnection msc = null; {
				msc = new MediaScannerConnection(getActivity().getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(imagen, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}
	
}
