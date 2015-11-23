package com.moisse.fragments;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListClientesEliminadosAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Cliente;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;

public class ListaClientesEliminados extends Fragment{

	private ListView lvListaClientesEliminados;
	private TextView tvAviso;
	private static final int inhabilitado=0;
	private String idparqueo;
	Button  botonPrecionado;
	TextView tvPrecionado;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//se utiliza el mismo layout xq no afecta solo es para vista
		View v = inflater.inflate(R.layout.lista_clientes, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(v);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_search).setVisible(false);
	}

	private void inicializarComponentes(View v) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionB.setTitle("Clientes eliminados");
		lvListaClientesEliminados = (ListView)v.findViewById(R.id.lvListaClientes);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaClientes);
		this.idparqueo = getArguments().getString("idparqueo");
		cargarListaClientesEliminados();
	}

	public void cargarListaClientesEliminados() {
		DBParqueo db = new DBParqueo(getActivity());
		try{
			db.openSQLite();
			List<Cliente> listClientesEliminados = db.getAllCliente(idparqueo, inhabilitado);
			if(listClientesEliminados.size()==0 || listClientesEliminados==null){
				tvAviso.setText("No se encontraron clientes eliminados");
				tvAviso.setVisibility(View.VISIBLE);
				lvListaClientesEliminados.setVisibility(View.INVISIBLE);
			}else{
				ListClientesEliminadosAdapter adapter = new ListClientesEliminadosAdapter(getActivity(), listClientesEliminados, this);
				lvListaClientesEliminados.setAdapter(adapter);
			}
			db.closeSQLite();
		}catch(Exception e){
			mensajeError(e.toString(), "cargar cliente eliminados");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error al "+titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
}
