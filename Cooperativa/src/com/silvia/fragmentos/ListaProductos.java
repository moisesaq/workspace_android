package com.silvia.fragmentos;

import java.util.List;

import com.silvia.adapters.ListaProdAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.dialogos.DialogDetalleProd;
import com.silvia.modelo.Producto;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListaProductos extends Fragment implements OnItemClickListener{

	private ListView lvListaProductos;
	private TextView tvNota;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_productos, container, false);
		inicializarCompenentes(v);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		menu.findItem(R.id.action_nuevo_prod).setVisible(true);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void inicializarCompenentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista productos");
		lvListaProductos = (ListView)v.findViewById(R.id.lvListaProductos);
		lvListaProductos.setOnItemClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaProductos);
		cargarMostrarListaProductos();
	}

	public void cargarMostrarListaProductos() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Producto> lista = db.getTodosLosProductos();
			if(lista.size()==0){
				tvNota.setText("No se encontro ningun producto, lista vacia..!");
				tvNota.setVisibility(View.VISIBLE);
				lvListaProductos.setVisibility(View.INVISIBLE);
			}else{
				//ArrayAdapter<Producto> adapter = new ArrayAdapter<Producto>(getActivity(), android.R.layout.simple_list_item_1, lista);
				ListaProdAdapter adapter = new ListaProdAdapter(getActivity(), lista);
				lvListaProductos.setAdapter(adapter);
			}
			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Producto prod = (Producto)adapter.getAdapter().getItem(position);
		DialogDetalleProd dialogDProd = new DialogDetalleProd(this, prod);
		dialogDProd.show(getFragmentManager(), "tagDDProd");
	}
	
}
