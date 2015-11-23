package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListVehiculoAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogDetalleVehiculo;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaVehiculos extends Fragment implements OnItemClickListener{

	private ListView lvListaVehiculos;
	private TextView tvAviso;
	private String idparqueo;
	
	ProgressDialog pd;
	HttpClientCustom httpCustom = new HttpClientCustom();
	String id_client_default;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_vehiculos, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Lista de vehiculos");
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		lvListaVehiculos = (ListView)v.findViewById(R.id.lvListaVehiculos);
		lvListaVehiculos.setOnItemClickListener(this);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaVehiculos);
		this.idparqueo = getArguments().getString("idparqueo");
		id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
		cargarListaVehiculos();
	}

	public void cargarListaVehiculos() {
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			List<Vehiculo> listaV = db.getAllVehiculo(this.idparqueo, MyVar.NO_ELIMINADO);
			if(listaV.size()==0 || listaV==null){
				tvAviso.setText("No se encontro ningún vehículo, lista vacia..!");
				tvAviso.setVisibility(View.VISIBLE);
				lvListaVehiculos.setVisibility(View.INVISIBLE);
			}else{
				ListVehiculoAdapter adapter = new ListVehiculoAdapter(getActivity(), listaV, id_client_default);
				lvListaVehiculos.setAdapter(adapter);
			}
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
			mensajeError(e.toString(), "cargar lista vehiculos");
		}
	}
	
	public void cargarListaVehiculosFiltrados(List<Vehiculo> lista) {
		if(lista.size()==0 || lista==null){
			tvAviso.setText("No se encontro ningún vehículo, lista vacia..!");
			tvAviso.setVisibility(View.VISIBLE);
			lvListaVehiculos.setVisibility(View.INVISIBLE);
		}else{
			tvAviso.setVisibility(View.INVISIBLE);
			lvListaVehiculos.setVisibility(View.VISIBLE);
			ListVehiculoAdapter adapter = new ListVehiculoAdapter(getActivity(), lista, id_client_default);
			lvListaVehiculos.setAdapter(adapter);
		}
	}
	
	public void filtrarVehiculos(){
		String[] opciones = {"Todos","Ocasionales", "Con contrato nocturno", "Con contrato diurno", "Con contrato dia completo"}; 
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Seleccionar vehículos"));
		dialog.setItems(opciones, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0){
					cargarListaVehiculos();
				}else if(which==1){
					cargarListaVehiculosFiltrados(getVehiculosOcasionales());
				}else if(which==2){
					cargarListaVehiculosFiltrados(getVehiculosConContrato(MyVar.CLIENTE_CONTRATO_NOCTURNO));
				}else if(which==3){
					cargarListaVehiculosFiltrados(getVehiculosConContrato(MyVar.CLIENTE_CONTRATO_DIURNO));
				}else if(which==4){
					cargarListaVehiculosFiltrados(getVehiculosConContrato(MyVar.CLIENTE_CONTRATO_DIA_COMPLETO));
				}
			}
		});
		dialog.setNeutralButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}
	
	public List<Vehiculo> getVehiculosOcasionales() {
		List<Vehiculo> listaV = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			listaV = db.getAllVehiculosOcasionales(idparqueo, id_client_default);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
			mensajeError(e.toString(), "cargar lista vehiculos");
		}
		return listaV;
	}
	
	public List<Vehiculo> getVehiculosConContrato(int tipo) {
		List<Vehiculo> listaV = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			listaV = db.getAllVehiculoConPropConContrato(idparqueo, id_client_default, tipo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
			mensajeError(e.toString(), "cargar lista vehiculos");
		}
		return listaV;
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error al "+titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}

	@Override
	public void onItemClick(AdapterView<?> a, View view, int position, long parent) {
		Vehiculo vehiculo = (Vehiculo)a.getAdapter().getItem(position);
		DialogDetalleVehiculo dialogDetalleVehiculo = new DialogDetalleVehiculo(this,vehiculo, getActivity());
		dialogDetalleVehiculo.show(getFragmentManager(), "tagDialogDVehiculo");
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_refresh).setVisible(true);
		menu.findItem(R.id.action_filter_vehiculo).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.action_refresh:
				//new CargarClientes().execute();
				cargarListaVehiculos();
				return true;
			case R.id.action_filter_vehiculo:
				filtrarVehiculos();
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	//DE AQUI YA NO SE VA UTILIZAR PERO X AHORA NO FUNCIONA LO DESHABILITE
	public class CargarClientes extends AsyncTask<Void, Void, Boolean>{
		List<Cliente> lista_clientes = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando clientes nuevos...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_clientes = httpCustom.cargarClientes(idparqueo);
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
			pd.setMessage("Actualizando lista vehiculos...");
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			lista_vehiculos = httpCustom.cargarVehiculos(idparqueo);
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
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Lista de vehiculos actualizado", Toast.LENGTH_SHORT).show();
				cargarListaVehiculos();
			}else{
				Toast.makeText(getActivity(), "No se pudo actualizar lista de vehiculos", Toast.LENGTH_SHORT).show();
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
