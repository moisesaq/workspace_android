package com.silvia.fragmentos;

import java.util.List;

import com.silvia.adapters.ListaClienteAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.modelo.Cliente;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ListaClientes extends Fragment implements OnItemClickListener{

	ListView lvListaClientes;
	TextView tvNota;
	OnListaClienteClickListener listener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_clientes, container, false);
		inicializarComponentes(v);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_nuevo_cliente).setVisible(true);
	}
	
	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista clientes");
		lvListaClientes = (ListView)v.findViewById(R.id.lvListaClientes);
		lvListaClientes.setOnItemClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaClientes);
		cargarMostrarListaClientes();
	}
	
	private void cargarMostrarListaClientes() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Cliente> lista = db.getTodosLosClientes();
			if(lista.size()==0){
				tvNota.setText("No se encontro ningun cliente, lista vacia..!");
				tvNota.setVisibility(View.VISIBLE);
				lvListaClientes.setVisibility(View.INVISIBLE);
			}else{
				//ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(getActivity(), android.R.layout.simple_list_item_1, lista);
				ListaClienteAdapter adapter = new ListaClienteAdapter(getActivity(), lista);
				lvListaClientes.setAdapter(adapter);
			}
			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Cliente cliente = (Cliente)adapter.getAdapter().getItem(position);
		listener.onVerDetalleClienteClick(cliente.getIdcliente());
	}

	public interface OnListaClienteClickListener{
		public void onVerDetalleClienteClick(String idcliente);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnListaClienteClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnListaClienteClickListener");
		}
	}
}
