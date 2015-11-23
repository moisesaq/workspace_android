package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListClienteAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogDetalleCliente;
import com.moisse.modelo.Cliente;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaClientes extends Fragment implements OnItemClickListener{

	ListView lvListaClientes;
	TextView tvAviso;
	private String idparqueo;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_clientes, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Lista de clientes");
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		lvListaClientes = (ListView)v.findViewById(R.id.lvListaClientes);
		lvListaClientes.setOnItemClickListener(this);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaClientes);
		this.idparqueo = getArguments().getString("idparqueo");
		cargarListaClientes();
	}
	
	public void cargarListaClientes() {
		DBParqueo db = new DBParqueo(getActivity());
		
		try {
			db.openSQLite();
			List<Cliente> listaC = db.getAllCliente(idparqueo, MyVar.NO_ELIMINADO);
			if(listaC.size()==0 || listaC==null){
				tvAviso.setText("No se encontro ningún cliente, lista vacia..!");
				tvAviso.setVisibility(View.VISIBLE);
				lvListaClientes.setVisibility(View.INVISIBLE);
			}else{
				tvAviso.setVisibility(View.INVISIBLE);
				lvListaClientes.setVisibility(View.VISIBLE);
				ListClienteAdapter adapter = new ListClienteAdapter(getActivity(), listaC);
				lvListaClientes.setAdapter(adapter);
			}
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cargarListaClientesPorContrato(int tipo_cliente) {
		DBParqueo db = new DBParqueo(getActivity());
		
		try {
			db.openSQLite();
			List<Cliente> listaC = db.getAllClientePorContrato(idparqueo, tipo_cliente);
			if(listaC.size()==0 || listaC==null){
				tvAviso.setText("No se encontro ningún cliente, lista vacia..!");
				tvAviso.setVisibility(View.VISIBLE);
				lvListaClientes.setVisibility(View.INVISIBLE);
			}else{
				tvAviso.setVisibility(View.INVISIBLE);
				lvListaClientes.setVisibility(View.VISIBLE);
				ListClienteAdapter adapter = new ListClienteAdapter(getActivity(), listaC);
				lvListaClientes.setAdapter(adapter);
			}
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Cliente cliente = (Cliente)adapter.getAdapter().getItem(position);
		DialogDetalleCliente dialogDetalleC = new DialogDetalleCliente(this, cliente, getActivity());
		dialogDetalleC.show(getFragmentManager(), "tagDDetalleCliente");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_refresh).setVisible(true);
		menu.findItem(R.id.action_cli_todos).setVisible(true);
		menu.findItem(R.id.action_cli_ocasionales).setVisible(true);
		menu.findItem(R.id.action_cli_nocturno).setVisible(true);
		menu.findItem(R.id.action_cli_diurno).setVisible(true);
		menu.findItem(R.id.action_cli_dia_completo).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_refresh:
			//new ActualizarListaClientes().execute();
			cargarListaClientes();
			return true;
		case R.id.action_cli_todos:
			cargarListaClientes();
			return true;
		case R.id.action_cli_ocasionales:
			cargarListaClientesPorContrato(MyVar.CLIENTE_OCASIONAL);
			return true;
		case R.id.action_cli_nocturno:
			cargarListaClientesPorContrato(MyVar.CLIENTE_CONTRATO_NOCTURNO);
			return true;
		case R.id.action_cli_diurno:
			cargarListaClientesPorContrato(MyVar.CLIENTE_CONTRATO_DIURNO);		
			return true;
		case R.id.action_cli_dia_completo:
			cargarListaClientesPorContrato(MyVar.CLIENTE_CONTRATO_DIA_COMPLETO);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//DE AQUI HACIA ABAJO YA NO SE ESTA UTILIZANDO
	public class ActualizarListaClientes extends AsyncTask<Void, Void, Boolean>{
		List<Cliente> lista_clientes = null;
		ProgressDialog pd;
		HttpClientCustom httpCustom;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando lista...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			httpCustom = new HttpClientCustom();
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
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Lista de clientes actualizado", Toast.LENGTH_SHORT).show();
				cargarListaClientes();
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
