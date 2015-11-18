package com.moises.httpurlconnection;

import java.util.List;

import com.moises.connectiontoserver.R;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListaLugares extends Fragment{

	View view;
	protected ListView lvListaLugares;
	protected LinearLayout lyVistaAviso;
	protected ProgressBar pbCargar;
	protected TextView tvAviso;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.lista_lugares, container, false);
		inicializarComponentes(view);
		return view;
	}
	
	private void inicializarComponentes(View v) {
		lvListaLugares = (ListView)v.findViewById(R.id.lvListaLugares);
		lyVistaAviso = (LinearLayout)v.findViewById(R.id.lyVistaAviso);
		pbCargar = (ProgressBar)v.findViewById(R.id.pbCargarLugares);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaLugares);
		CustomHttpUrl cus = new CustomHttpUrl(getActivity());
		if(cus.exitsConnection()){
			//new ProbarConexion().execute();
			new CargarLugares().execute();
		}else{
			tvAviso.setVisibility(View.VISIBLE);
			tvAviso.setText("Lista de lugares vacio (HttpUrlConnection)");
		}
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_nuevo_lugar).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_nuevo_lugar){
			Toast.makeText(getActivity(), "Agregar nuevo lugar", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class ProbarConexion extends AsyncTask<Void, Void, String>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			lyVistaAviso.setVisibility(View.VISIBLE);
			pbCargar.setVisibility(View.VISIBLE);
			tvAviso.setVisibility(View.VISIBLE);
			tvAviso.setText("Probando conexion...");
		}

		@Override
		protected String doInBackground(Void... params) {
			CustomHttpUrl cus = new CustomHttpUrl(getActivity());
			if(cus.probarConexion()){
				return "Conexion establecido correctamente";
			}else{
				return "No se establecio una conexion";
			}
			
		}
		
		@Override
		protected void onPostExecute(String mensaje){
			super.onPostExecute(mensaje);
			pbCargar.setVisibility(View.GONE);
			tvAviso.setText(mensaje);
		}
	}
	
	public class CargarLugares extends AsyncTask<Void, Void, List<Lugar>>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			lyVistaAviso.setVisibility(View.VISIBLE);
			pbCargar.setVisibility(View.VISIBLE);
			tvAviso.setVisibility(View.VISIBLE);
			tvAviso.setText("Cargando...");
		}

		@Override
		protected List<Lugar> doInBackground(Void... params) {
			CustomHttpUrl cus = new CustomHttpUrl(getActivity());
			List<Lugar> my_list = cus.getAllLugares();
			return my_list;
		}
		
		@Override
		protected void onPostExecute(List<Lugar> lista){
			super.onPostExecute(lista);
			pbCargar.setVisibility(View.GONE);
			if(lista!=null && lista.size()!=0){
				tvAviso.setVisibility(View.GONE);
				ArrayAdapter<Lugar> adapter = new ArrayAdapter<Lugar>(getActivity(), android.R.layout.simple_list_item_1, lista);
				lvListaLugares.setAdapter(adapter);
			}else{
				lvListaLugares.setVisibility(View.GONE);
				tvAviso.setText("No se encontro ningun lugar!");
			}
		}
	}
	
}
