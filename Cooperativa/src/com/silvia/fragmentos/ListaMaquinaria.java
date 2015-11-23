package com.silvia.fragmentos;

import java.util.List;

import com.silvia.adapters.ListaMaqAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.dialogos.DialogDetalleMaquinaria;
import com.silvia.modelo.Maquinaria;

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

public class ListaMaquinaria extends Fragment implements OnItemClickListener{

	private ListView lvListaMaquinaria;
	private TextView tvNota;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_maquinaria, container, false);
		inicializarCompenentes(v);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		menu.findItem(R.id.action_nueva_maq).setVisible(true);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void inicializarCompenentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista maquinarias");
		lvListaMaquinaria = (ListView)v.findViewById(R.id.lvListaMaquinaria);
		lvListaMaquinaria.setOnItemClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaMaquinarias);
		cargarMostrarListaMaquinaria();
	}

	public void cargarMostrarListaMaquinaria() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Maquinaria> lista = db.getTodosLasMaquinarias();
			if(lista.size()==0){
				tvNota.setText("No se encontro ningun maquinaria, lista vacia..!");
				tvNota.setVisibility(View.VISIBLE);
				lvListaMaquinaria.setVisibility(View.INVISIBLE);
			}else{
				//ArrayAdapter<Maquinaria> adapter = new ArrayAdapter<Maquinaria>(getActivity(), android.R.layout.simple_list_item_1, lista);
				ListaMaqAdapter adapter = new ListaMaqAdapter(getActivity(), lista);
				lvListaMaquinaria.setAdapter(adapter);
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Maquinaria maq = (Maquinaria)adapter.getAdapter().getItem(position);
		//listener.onMostrarMaquinariaClick(maq.getIdmaquinaria());
		DialogDetalleMaquinaria dMaq = new DialogDetalleMaquinaria(maq, this);
		dMaq.show(getFragmentManager(), "tagDM");
	}

}
