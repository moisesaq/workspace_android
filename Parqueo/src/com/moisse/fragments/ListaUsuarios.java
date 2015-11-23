package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListUsuarioAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogDetalleUsuario;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Usuario;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.app.ActionBar;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListaUsuarios extends Fragment implements OnItemClickListener{
	
	ListView lvListaUsuarios;
	private String idparqueo;
	private String idusuario;
	private Usuario user_online;
	private Parqueo parq_online;
	
	private HttpClientCustom httpCustom;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.lista_usuarios, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Lista de usuarios");
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		lvListaUsuarios = (ListView)v.findViewById(R.id.lvListaUsuarios);
		lvListaUsuarios.setOnItemClickListener(this);
		this.idparqueo = getArguments().getString("idparqueo");
		this.idusuario = getArguments().getString("idusuario");
		this.user_online = getUserOnline(this.idusuario);
		this.parq_online = getParqueoOnline(this.idparqueo);
		httpCustom = new HttpClientCustom();
		cargarListaUsuarios();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_download).setVisible(true);
		menu.findItem(R.id.action_search).setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_download){
			new ActualizarUsuarios().execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void cargarListaUsuarios() {
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			List<Usuario> listaU = db.getAllUsuario(this.idparqueo);
			//ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(getActivity(), android.R.layout.simple_list_item_1, listaU);
			ListUsuarioAdapter adapter = new ListUsuarioAdapter(getActivity(), listaU);
			lvListaUsuarios.setAdapter(adapter);
			db.closeSQLite();			
		} catch (Exception e) {
			mensajeError(e.toString(), "al cargar lista Usuarios");
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> a, View view, int position, long parent) {
		Usuario usuario = (Usuario)a.getAdapter().getItem(position);
		if(this.user_online.getCi().equals(usuario.getCi())){
			DialogMensaje dmsj = new DialogMensaje("Este usuario es usted!, para ver sus datos vea su perfil");
			dmsj.show(getFragmentManager(), "dialogMsjListUsuario");
		}else{
			DialogDetalleUsuario dialogDUsuario = new DialogDetalleUsuario(this, usuario, this.user_online);
			dialogDUsuario.show(getFragmentManager(), "tagDialogDUsuario");
		}
	}
	
	public Usuario getUserOnline(String id_user_online){
		DBParqueo db = new DBParqueo(getActivity());
		Usuario user_activo = null;
		try {
			db.openSQLite();
			user_activo = db.getUsuario(id_user_online);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
			mensajeError(e.toString(), "obtener usuario");
		}
		return user_activo;
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error "+titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	public Parqueo getParqueoOnline(String idparqueo){
		Parqueo parqueo = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			parqueo = db.getParqueo(idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parqueo;
	}
	
	public class ActualizarUsuarios extends AsyncTask<Void, Void, Boolean>{
		ProgressDialog pd;
		List<Usuario> lista_usuarios = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando usuarios...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_usuarios = httpCustom.cargarUsuarios(parq_online, user_online);
			DBParqueo db = new DBParqueo(getActivity());
			if(lista_usuarios!=null && lista_usuarios.size()!=0){
				try{
					db.openSQLite();
					for (int i = 0; i < lista_usuarios.size(); i++) {
						Usuario usuario = lista_usuarios.get(i);
						if(db.existeUsuario(usuario.getIdusuario())){
							db.actualizarUsuario(usuario);
						}else{
							db.insertarUsuario(usuario);
						}
						if(!usuario.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							downloadSaveImage(usuario.getImagen());
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
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Lista de usuarios actualizado", Toast.LENGTH_SHORT).show();
				cargarListaUsuarios();
			}else{
				Toast.makeText(getActivity(), "No se pudo actualizar lista", Toast.LENGTH_SHORT).show();
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
