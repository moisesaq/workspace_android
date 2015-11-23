package com.silvia.fragmentos;

import java.util.List;

import com.silvia.adapters.ListaUsuarioAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.modelo.Usuario;

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

//----------------------------------------ESTA LISTA YA NO SE VA UTILIZAR------------------------------------ 
public class ListaUsuarios extends Fragment implements OnItemClickListener{

	ListView lvListaUsuarios;
	TextView tvNota;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_personal, container, false);
		inicializarComponentes(v);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		//menu.findItem(R.id.action_nuevo_cliente).setVisible(true);
	}
	
	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista usuarios");
		lvListaUsuarios = (ListView)v.findViewById(R.id.lvListaPersonal);
		lvListaUsuarios.setOnItemClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaPersonal);
		cargarMostrarListaUsuarios();
	}
	
	private void cargarMostrarListaUsuarios() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Usuario> lista = db.getTodosLosUsuario();
			if(lista.size()==0){
				tvNota.setText("No se encontro ningun cargo, lista vacia..!");
				tvNota.setVisibility(View.VISIBLE);
				lvListaUsuarios.setVisibility(View.INVISIBLE);
			}else{
				ListaUsuarioAdapter adapter = new ListaUsuarioAdapter(getActivity(), lista);
				lvListaUsuarios.setAdapter(adapter);
			}
			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		
	}
}
