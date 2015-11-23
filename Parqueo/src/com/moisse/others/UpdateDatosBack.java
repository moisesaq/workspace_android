package com.moisse.others;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.moisse.database.DBParqueo;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateDatosBack {

	public Activity activity;
	public String idparqueo; 
	java.util.Date date = new java.util.Date();
	Date fecha_actual = new Date(date.getTime());
	HttpClientCustom httpCustom = new HttpClientCustom();
	
	public UpdateDatosBack(Activity activity , String idparqueo){
		this.activity = activity;
		this.idparqueo = idparqueo;
	}
	
	public void iniciarActualizacionMinimizado(){
		new CargarParqueo().execute();
	}
	
	private class CargarParqueo extends AsyncTask<Void, Void, Boolean>{
		Parqueo parqueo;
		@Override
		protected Boolean doInBackground(Void... params) {
			parqueo = httpCustom.cargarParqueo(idparqueo);
			if(parqueo!=null){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					if(db.actualizarParqueo(parqueo)){
						return true;
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			new CargarClientes().execute();
		}
	}
	
	public class CargarClientes extends AsyncTask<Void, Void, Boolean>{
		List<Cliente> lista_clientes = null;
		
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_clientes = httpCustom.cargarClientes(idparqueo);
			if(lista_clientes!=null && lista_clientes.size()!=0){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					for (int i = 0; i < lista_clientes.size(); i++) {
						Cliente cliente = lista_clientes.get(i);
						if(db.existeCliente(cliente.getIdcliente())){
							db.actualizarCliente(cliente);
						}else{
							db.insertarCliente(cliente);
						}
						if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							downloadSaveImage(cliente.getImagen());
						}
						
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			new CargarVehiculos().execute();
		}

	}
	
	public class CargarVehiculos extends AsyncTask<Void, Void, Boolean>{
		List<Vehiculo> lista_vehiculos = null;
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_vehiculos = httpCustom.cargarVehiculos(idparqueo);
			if(lista_vehiculos!=null && lista_vehiculos.size()!=0){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					for (int i = 0; i < lista_vehiculos.size(); i++) {
						Vehiculo vehiculo = lista_vehiculos.get(i);
						if(db.existeVehiculo(vehiculo.getIdvehiculo())){
							db.actualizarVehiculo(vehiculo);
						}else{
							db.insertarVehiculo(vehiculo);
						}
						if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){;
							downloadSaveImage(vehiculo.getImagen());
						}
					}
					db.closeSQLite();
					return true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			new ActualizarCarriles().execute();
		}
	}
	
	public class ActualizarCarriles extends AsyncTask<Void, Void, Boolean>{
		List<Carril> lista_carriles = null;
		int cont;
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_carriles = httpCustom.cargarCarriles(idparqueo);
			if(lista_carriles!=null && lista_carriles.size()!=0){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					for (int i = 0; i < lista_carriles.size(); i++) {
						Carril carril = lista_carriles.get(i);
						if(db.existeCarril(carril)){
							db.actualizarCarril(carril);
						}else{
							db.insertarCarril(carril);
						}
					}
					db.closeSQLite();
					return true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			new ActualizarIngresoVehiculos().execute();
		}
	}
	
	public class ActualizarIngresoVehiculos extends AsyncTask<Void, Void, Boolean>{
		List<Resguardo> lista_ingresos = null;
		
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_ingresos = httpCustom.cargarIngresosResguardos(idparqueo);
			if(lista_ingresos!=null && lista_ingresos.size()!=0){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					for (int i = 0; i < lista_ingresos.size(); i++) {
						Resguardo resguardo = lista_ingresos.get(i);
						if(!db.verificarResguardo(resguardo.getIdresguardo())){
							db.insertarResguardo(resguardo);
						}
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			new ActualizarResguardos().execute();
		}
		
	}
	
	public class ActualizarResguardos extends AsyncTask<Void, Void, Boolean>{
		boolean control = false;
		List<Resguardo> lista_resguardos = null;
		@Override
		protected Boolean doInBackground(Void... params) {
			Date fecha_antes = new Date(getFechaReducido(fecha_actual, -2).getTime());
			lista_resguardos = httpCustom.cargarResguardosPorFecha(idparqueo, fecha_actual, fecha_antes);
			if(lista_resguardos!=null && lista_resguardos.size()!=0){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					for (int i = 0; i < lista_resguardos.size(); i++) {
						Resguardo resguardo = lista_resguardos.get(i);
						if(db.verificarResguardo(resguardo.getIdresguardo())){
							db.actualizarResguardo(resguardo);
							control = true;
						}else{
							db.insertarResguardoCompleto(resguardo);
							control = true;
						}
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return control;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Toast.makeText(activity, "Actulizacion finalizado", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public java.util.Date getFechaReducido(java.util.Date date, int dias){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}
	
	public void downloadSaveImage(String name_image){
		File folder_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		folder_images.mkdirs();
		
		File file_image = new File(folder_images, name_image);
		if(!file_image.exists()){
			try {
				URL url_image = new URL(new StringBuilder(MyVar.RUTA_IMAGES_SERVER).append(name_image).toString());
				HttpURLConnection conn = (HttpURLConnection)url_image.openConnection();
				Bitmap myBitmap = BitmapFactory.decodeStream(conn.getInputStream());
				if(myBitmap!=null){
					FileOutputStream fos = new FileOutputStream(file_image);
					myBitmap.compress(CompressFormat.JPEG, 90, fos);
					fos.flush();
					scanImage(file_image.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public void scanImage(final String pathImage){
		new MediaScannerConnectionClient() {
			private MediaScannerConnection msc = null; {
				msc = new MediaScannerConnection(activity.getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(pathImage, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}
	
}
