package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Usuario;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;
import com.moisse.parqueo.MenuPrincipal;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Autenticacion extends Fragment implements OnClickListener{

	EditText etUsuario, etClave;
	Button acceder,cancelar;
	TextView tvNuevo;
	
	OnNuevoParqueoClickListener npListener;
	public HttpClientCustom httpCustom = new HttpClientCustom();
	java.util.Date date = new java.util.Date();
	Date fecha_actual = new Date(date.getTime());
	
	Usuario usuario_online;
	Parqueo parqueo_online;
	String idparqueo, idusuario;
	
	ProgressDialog pd;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.autenticacion, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		etUsuario = (EditText)v.findViewById(R.id.etUsuarioAut);
		etClave = (EditText)v.findViewById(R.id.etClaveAut);
		acceder = (Button)v.findViewById(R.id.btnAccederAut);
		acceder.setOnClickListener(this);
		cancelar = (Button)v.findViewById(R.id.btnCancelarAut);
		cancelar.setOnClickListener(this);
		tvNuevo = (TextView)v.findViewById(R.id.tvNuevoRegAut);
		tvNuevo.setOnClickListener(this);;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnAccederAut:
				if(existConnection()){
					String txtUsuario = etUsuario.getText().toString().trim();
					String txtClave = etClave.getText().toString().trim();
					if(!txtUsuario.equals("")){
						if(!txtClave.equals("")){
							Usuario user = new Usuario(txtUsuario, txtClave);
							new AutenticarUsuario().execute(user);
						}else{
							etClave.requestFocus();
							etClave.setError("Introduzca clave");
						}
					}else{
						etUsuario.requestFocus();
						etUsuario.setError("Introduzca usuario");
					}
				}else{
					Toast.makeText(getActivity(), "Sin acceso a internet", Toast.LENGTH_LONG).show();
				}
				
				break;
			case R.id.btnCancelarAut:
				getActivity().finish();
				break;
			case R.id.tvNuevoRegAut:
				npListener.onNuevoParqueoClick();
				break;
		}
	}
	
	public interface OnNuevoParqueoClickListener{
		public void onNuevoParqueoClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			npListener = (OnNuevoParqueoClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" tiene que implementar el OnNuevoParqueClickListener");
		}
	}

	public class AutenticarUsuario extends AsyncTask<Usuario, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Autenticando usuario...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Usuario... params) {
			Usuario usuario = params[0];
			usuario_online = httpCustom.autenticarUsuario(usuario);
			if(usuario_online!=null){
				if(!usuario_online.getImagen().equals(MyVar.NO_ESPECIFICADO)){
					downloadSaveImage(usuario_online.getImagen());
				}
				return true;
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				if(usuario_online.getEstado()==MyVar.NO_ELIMINADO){
					new CargarParqueo().execute(usuario_online.getIdparqueo());
				}else{
					pd.dismiss();
					DialogMensaje dialogMsj = new DialogMensaje("Tu cuenta fue bloqueado, pongase en contacto con su administrador");
					dialogMsj.show(getFragmentManager(), "msjDialogAutenicacion");
					etClave.getText().clear();
				}
			}else{
				pd.dismiss();
				Toast.makeText(getActivity(), "Usuario no registrado, verifique sus datos", Toast.LENGTH_SHORT).show();
				etClave.setText("");
			}
		}
		
		
	}
	
	private class CargarParqueo extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Cargando parqueo...");
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String idparqueo = params[0];
			parqueo_online = httpCustom.cargarParqueo(idparqueo);
			if(parqueo_online!=null){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					if(!parqueo_online.getLogo().equals(MyVar.NO_ESPECIFICADO)){
						downloadSaveImage(parqueo_online.getLogo());
					}
					if(db.verificarParqueo(parqueo_online.getIdparqueo())){
						db.actualizarParqueo(parqueo_online);
						if(db.existeUsuario(usuario_online.getIdusuario())){
							if(db.actualizarUsuario(usuario_online)){
								return true;
							}
						}else{
							if(db.insertarUsuario(usuario_online)){
								return true;
							}
						}
					}else{
						if(db.insertarParqueo(parqueo_online)){
							if(db.insertarUsuario(usuario_online)){
								return true;
							}
						}
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
				new CargarCarriles().execute();
			}else{
				pd.dismiss();
				DialogMensaje dialogMsj = new DialogMensaje("No se pudo cargar datos del usuario y el parqueo, intente mas tarde...!");
				dialogMsj.show(getFragmentManager(), "msjDialogAutenicacion");
				etClave.setText("");
			}
		}
	}
	
	public class CargarCarriles extends AsyncTask<Void, Void, Boolean>{
		List<Carril> lista_carriles = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Cargando carriles...");
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
							db.actualizarCarril(carril);
						}else{
							db.insertarCarril(carril);
						}
						
					}
					if(db.verificarCarriles(parqueo_online)){//Aqui se verifica asi xq arriba ya se actualizo parqueo
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
				Toast.makeText(getActivity(), "Usuario y parqueo cargados correctamente", Toast.LENGTH_SHORT).show();
				new CargarClientes().execute();
			}else{
				pd.dismiss();
				etClave.getText().clear();
				DialogMensaje dialogMsj = new DialogMensaje("No se pudo cargar los carriles del parqueo, intente mas tarde...!");
				dialogMsj.show(getFragmentManager(), "msjDialogAutenicacion");
			}
		}
		
	}
	
	public class CargarClientes extends AsyncTask<Void, Void, Boolean>{
		List<Cliente> lista_clientes = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Cargando clientes...");
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
				new CargarVehiculos().execute();
			}else{
				new CargarVehiculos().execute();
			}
		}
	}
	
	public class CargarVehiculos extends AsyncTask<Void, Void, Boolean>{
		List<Vehiculo> lista_vehiculos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Cargando vehiculos...");
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
						if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
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
				Toast.makeText(getActivity(), "Cliente y Vehiculos cargados", Toast.LENGTH_SHORT).show();
				new CargarIngresoVehiculos().execute();
			}else{
				pd.dismiss();
				accederParqueo();
				Toast.makeText(getActivity(), "No se encontro ningun vehiculo registrado", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public class CargarIngresoVehiculos extends AsyncTask<Void, Void, Boolean>{
		List<Resguardo> lista_ingresos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Cargando ingreso vehiculos...");
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
				Toast.makeText(getActivity(), "Ingreso de vehiculos cargados", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), "No existen ingreso de vehiculos registrados", Toast.LENGTH_SHORT).show();
			}
			new CargarResguardos().execute();
		}
	}
	
	public class CargarResguardos extends AsyncTask<Void, Void, Boolean>{
		boolean control =false;
		List<Resguardo> lista_resguardos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Cargando resguardos...");
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			//ACA: no actualizara todo solo del mes actual.... si en caso de q inicia seccion de un tiempo largo
			lista_resguardos = httpCustom.cargarResguardosPorMes(usuario_online.getIdparqueo(), fecha_actual);
			if(lista_resguardos!=null && lista_resguardos.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					for (int i = 0; i < lista_resguardos.size(); i++) {
						Resguardo resguardo = lista_resguardos.get(i);
						if(db.verificarResguardo(resguardo.getIdresguardo())){
							if(db.actualizarResguardo(resguardo)){//Esto va actualizar los resguardos que yo tenga como entrada y q ya se aya actualizado
								control = true;
							}
						}else{
							if(db.insertarResguardoCompleto(resguardo)){
								control = true;
							}
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
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Resguardos anteriores cargados", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), "No existen resguardos anteriores", Toast.LENGTH_SHORT).show();
			}
			accederParqueo();
		}
	
	}
	
	public void accederParqueo(){
		if(usuario_online==null || parqueo_online==null){
			Toast.makeText(getActivity(), "Error al autenticarse no se puede acceder", Toast.LENGTH_SHORT).show();
		}else{
			setIDShare(parqueo_online.getIdparqueo(), usuario_online.getIdusuario());
			etClave.getText().clear();
			getActivity().finish();
			Intent intent = new Intent(getActivity(), MenuPrincipal.class);
			Bundle caja = new Bundle();
			caja.putString("idparqueo", parqueo_online.getIdparqueo());
			caja.putString("idusuario", usuario_online.getIdusuario());
			intent.putExtras(caja);
			startActivity(intent);
		}
	}
	
	public void setIDShare(String idparqueo, String idusuario){
		SharedPreferences sharep = getActivity().getSharedPreferences("ShareParqueo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = sharep.edit();
		editar.putString("idparqueo", idparqueo);
		editar.putString("idusuario", idusuario);
		editar.commit();
	}
	
	public void downloadSaveImage(String name_imagen){
		File folder_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		folder_images.mkdirs();
		
		File file_image = new File(folder_images, name_imagen);
		if(!file_image.exists()){
			try {
				//TODO aqui podemos hacer que descargue las imagen con un ImageLoader
				URL url_image = new URL(new StringBuilder(MyVar.RUTA_IMAGES_SERVER).append(name_imagen).toString());
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

	public boolean existConnection(){
		ConnectivityManager connMg = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfor = connMg.getActiveNetworkInfo();
		if(networkInfor!=null && networkInfor.isConnected()){
			return true;
		}
		return false;
	}
}
