package com.moisse.parqueo;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.fragments.Autenticacion;
import com.moisse.fragments.NuevoParqueo;
import com.moisse.fragments.Autenticacion.OnNuevoParqueoClickListener;
import com.moisse.fragments.NuevoParqueo.OnParqueoClickListener;
import com.moisse.fragments.NuevoUsuario;
import com.moisse.fragments.NuevoUsuario.OnPerfilClickListener;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Usuario;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class MainActivity extends Activity implements OnParqueoClickListener, OnNuevoParqueoClickListener, OnPerfilClickListener{

	NuevoParqueo nuevo_parqueo;
	NuevoUsuario nuevo_usuario;
	Autenticacion autenticacion;
	public String idparqueo, idusuario;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(new StringBuilder("Easy Parking"));
		setContentView(R.layout.activity_main);
//		autenticacion = new Autenticacion();
//		establecesVistaFragment(autenticacion, "tag");
		getIDShare();
		if(!idparqueo.equals("") && !idusuario.equals("")){
			new IniciarComponentes().execute();
		}else{
			autenticacion = new Autenticacion();
			establecesVistaFragment(autenticacion, "tagAut");
		}
	}
	
	public void getIDShare(){
		SharedPreferences sharep = getSharedPreferences("ShareParqueo", Context.MODE_PRIVATE);
		idparqueo = sharep.getString("idparqueo", "");
		idusuario = sharep.getString("idusuario", "");
	}
	
	public void accederParqueo(){
		Intent intent = new Intent(this, MenuPrincipal.class);
		Bundle caja = new Bundle();
		caja.putString("idparqueo", idparqueo);
		caja.putString("idusuario", idusuario);
		intent.putExtras(caja);
		startActivity(intent);
	}
	
	public void establecesVistaFragment(Fragment fragment, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.contenedorAcceso, fragment);
		ft.commit();
	}

	public class IniciarComponentes extends AsyncTask<Void, Void, Boolean>{
		Usuario usuario_online;
		Parqueo parqueo_online;
		ProgressDialog pd;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(MainActivity.this);
			pd.setMessage("Iniciando aplicacion...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params){
			DBParqueo db = new DBParqueo(MainActivity.this);
			HttpClientCustom httpCustom = new HttpClientCustom();
			parqueo_online = httpCustom.cargarParqueo(idparqueo);
			usuario_online = httpCustom.getDatosUsuario(idusuario);
			
			if(parqueo_online!=null && usuario_online!=null){
				try {
					db.openSQLite();
					if(db.actualizarParqueo(parqueo_online) && db.actualizarUsuario(usuario_online)){
						if(!parqueo_online.getLogo().equals(MyVar.NO_ESPECIFICADO)){
							downloadSaveImage(parqueo_online.getLogo());
						}
						if(!usuario_online.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							downloadSaveImage(usuario_online.getImagen());
						}
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
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				if(usuario_online.getEstado()==MyVar.NO_ELIMINADO){
					accederParqueo();
				}else{
					DialogMensaje dialogMsj = new DialogMensaje("Tu cuenta fue bloqueado, pongase en contacto con su administrador");
					dialogMsj.show(getFragmentManager(), "msjDialogAutenicacion");
					Autenticacion autenticacion = new Autenticacion();
					establecesVistaFragment(autenticacion, "tagAut");
				}
			}else{
				Toast.makeText(MainActivity.this, "App no inicio correctamente, puede que no funcione algunas funcionalidades", Toast.LENGTH_LONG).show();
				accederParqueo();
			}
			
		}
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
				msc = new MediaScannerConnection(getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(pathImage, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}
	
	@Override
	public void onBackPressed(){
		if(getFragmentManager().getBackStackEntryCount()>1){
			getFragmentManager().popBackStack();
		}else{
			MainActivity.this.finish();
		}
	}
	
	@Override
	public void onContinuarParqueoClick(String idParqueo) {
		nuevo_usuario = new NuevoUsuario();
		Bundle mensaje = new Bundle();
		mensaje.putInt("accion", MyVar.ACCION_REGISTRAR_ADMIN);
		mensaje.putString("idparqueo", idParqueo);
		nuevo_usuario.setArguments(mensaje);
		establecesVistaFragment(nuevo_usuario, "tagNuevoU");
	}
	
	@Override
	public void onBackFromParqueoClick() {}

	@Override
	public void onNuevoParqueoClick() {
		Bundle cajon = new Bundle();
		cajon.putString("idparqueo", MyVar.NO_ESPECIFICADO);
		nuevo_parqueo = new NuevoParqueo();
		nuevo_parqueo.setArguments(cajon);
		establecesVistaFragment(nuevo_parqueo, "tagNuevoParqueo");
	}
	
	@Override
	public void onActualizarPerfilClick() {}

	@Override
	public void onBackFromPerfilClick() {}
	
	
}
