package com.moisse.dialogs;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.Parking;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;
import com.moisse.others.UpdateDatosBack;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogUpdate extends DialogFragment implements OnClickListener{
	protected Button btnIniciar, btnFinalizar;
	protected TextView tvTexto;
	protected ProgressBar pbActualizacion;
	protected CheckBox cbMinimizado;
	protected View v;
	protected Parqueo parqueo_online;
	protected Parking parking;
	protected HttpClientCustom httpCustom = new HttpClientCustom();
	
	java.util.Date date = new java.util.Date();
	Date fecha_actual = new Date(date.getTime());
	
	public DialogUpdate(Parqueo parqueo_online, Parking parking){
		this.parqueo_online = parqueo_online;
		this.parking = parking;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setCancelable(false);
		dialog.setTitle(new StringBuilder("Actualizar datos"));
		v = getActivity().getLayoutInflater().inflate(R.layout.modelo_update_information, null);
		pbActualizacion = (ProgressBar)v.findViewById(R.id.pbActualizacion);
		tvTexto = (TextView)v.findViewById(R.id.tvTextoActualizacion);
		cbMinimizado = (CheckBox)v.findViewById(R.id.cbMinimizadoActualizacion);
		cbMinimizado.setChecked(false);
		btnIniciar = (Button)v.findViewById(R.id.btnIniciarActualizacion);
		btnIniciar.setOnClickListener(this);
		btnFinalizar = (Button)v.findViewById(R.id.btnFinalizarActualizacion);
		btnFinalizar.setOnClickListener(this);
		dialog.setView(v);
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnIniciar.getId()){
			if(cbMinimizado.isChecked()){
				UpdateDatosBack update = new UpdateDatosBack(getActivity(), parqueo_online.getIdparqueo());
				update.iniciarActualizacionMinimizado();
				Toast.makeText(getActivity(), "Actualizando datos...", Toast.LENGTH_SHORT).show();
				this.dismiss();
			}else{
				//Toast.makeText(getActivity(), "Actualizando datos a la vista..", Toast.LENGTH_SHORT).show();
				new CargarParqueo().execute();
			}
		}else if(v.getId()==btnFinalizar.getId()){
			parking.cargarCarriles();
			this.dismiss();
		}
	}
	
	private class CargarParqueo extends AsyncTask<Void, Void, Boolean>{
		Parqueo parqueo;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pbActualizacion.setVisibility(View.VISIBLE);
			tvTexto.setText("Actualizando parqueo...");
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			parqueo = httpCustom.cargarParqueo(parqueo_online.getIdparqueo());
			if(parqueo!=null){
				DBParqueo db = new DBParqueo(getActivity());
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
			if(result){
				Toast.makeText(getActivity(), "Parqueo actualizado", Toast.LENGTH_SHORT).show();
				new CargarClientes().execute();
			}else{
				new CargarClientes().execute();
			}
		}
	}
	
	public class CargarClientes extends AsyncTask<Void, Void, Boolean>{
		List<Cliente> lista_clientes = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvTexto.setText("Actualizando clientes...");
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_clientes = httpCustom.cargarClientes(parqueo_online.getIdparqueo());
			if(lista_clientes!=null && lista_clientes.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
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
			if(result){
				Toast.makeText(getActivity(), "Clientes actualizados", Toast.LENGTH_SHORT).show();
				new CargarVehiculos().execute();
			}else{
				Toast.makeText(getActivity(), "No se encontraron clientes registrados", Toast.LENGTH_SHORT).show();
				new CargarVehiculos().execute();
			}
		}

	}
	
	public class CargarVehiculos extends AsyncTask<Void, Void, Boolean>{
		List<Vehiculo> lista_vehiculos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvTexto.setText("Actualizando vehiculos...");
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_vehiculos = httpCustom.cargarVehiculos(parqueo_online.getIdparqueo());
			if(lista_vehiculos!=null && lista_vehiculos.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
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
			if(result){
				Toast.makeText(getActivity(), "Vehiculos actualizados", Toast.LENGTH_SHORT).show();
				new ActualizarCarriles().execute();
			}else{
				pbActualizacion.setVisibility(View.GONE);
				tvTexto.setText("Datos actualizados exitosamente");
				btnIniciar.setVisibility(View.GONE);
				btnFinalizar.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public class ActualizarCarriles extends AsyncTask<Void, Void, Boolean>{
		List<Carril> lista_carriles = null;
		int cont;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvTexto.setText("Actualizando carriles...");
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_carriles = httpCustom.cargarCarriles(parqueo_online.getIdparqueo());
			if(lista_carriles!=null && lista_carriles.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					for (int i = 0; i < lista_carriles.size(); i++) {
						Carril carril = lista_carriles.get(i);
						if(db.existeCarril(carril)){
							if(db.actualizarCarril(carril)){
								cont++;
							}
						}else{
							if(db.insertarCarril(carril)){
								cont++;
							}
						}
					}
					if(cont==lista_carriles.size()){
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
			if(result){
				Toast.makeText(getActivity(), "Carriles del parqueo actualizados", Toast.LENGTH_SHORT).show();
				new ActualizarIngresoVehiculos().execute();
			}else{
				pbActualizacion.setVisibility(View.GONE);
				tvTexto.setText("No se pudo actualizar los carriles del parqueo, intente mas tarde..!");
				btnIniciar.setVisibility(View.GONE);
				btnFinalizar.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public class ActualizarIngresoVehiculos extends AsyncTask<Void, Void, Boolean>{
		List<Resguardo> lista_ingresos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvTexto.setText("Actualizando ingreso de vehiculos...");
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_ingresos = httpCustom.cargarIngresosResguardos(parqueo_online.getIdparqueo());
			if(lista_ingresos!=null && lista_ingresos.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					for (int i = 0; i < lista_ingresos.size(); i++) {
						Resguardo resguardo = lista_ingresos.get(i);
						//db.insertarResguardo(resguardo);
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
			if(result){
				Toast.makeText(getActivity(), "Ingreso de vehiculos actualizados", Toast.LENGTH_SHORT).show();
				new ActualizarResguardos().execute();
			}else{
				Toast.makeText(getActivity(), "No se encontraron registro de ingresos", Toast.LENGTH_SHORT).show();
				new ActualizarResguardos().execute();
			}
		}
		
	}
	
	public class ActualizarResguardos extends AsyncTask<Void, Void, Boolean>{
		boolean control = false;
		List<Resguardo> lista_resguardos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvTexto.setText("Actualizando resguardos...");
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			Date fecha_antes = new Date(getFechaReducido(fecha_actual, -5).getTime());
			lista_resguardos = httpCustom.cargarResguardosPorFecha(parqueo_online.getIdparqueo(), fecha_actual, fecha_antes);
			if(lista_resguardos!=null && lista_resguardos.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
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
			if(result){
				Toast.makeText(getActivity(), "Resguardos anteriores cargados", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), "No se encontraron mas resguardos", Toast.LENGTH_SHORT).show();
			}
			if(cbMinimizado.isChecked()){
				Toast.makeText(getActivity(), "Datos actualizos", Toast.LENGTH_SHORT).show();
			}
			pbActualizacion.setVisibility(View.GONE);
			tvTexto.setText("Datos actualizados exitosamente");
			btnIniciar.setVisibility(View.GONE);
			btnFinalizar.setVisibility(View.VISIBLE);
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
				msc = new MediaScannerConnection(getActivity().getApplicationContext(), this);msc.connect();
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
