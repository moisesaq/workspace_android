package com.moises.adapters;

import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.moises.modelo.Equipo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterEquipos extends BaseAdapter{
	
	Activity miContexto;
	ArrayList<Equipo> lista;
	
	public AdapterEquipos(Activity contexto, ArrayList<Equipo> listaEquipos){
		this.miContexto=contexto;
		this.lista=listaEquipos;
	}

	@Override
	public int getCount() {
		return lista.size();
	}

	@Override
	public Object getItem(int posicion) {
		return lista.get(posicion);
	}

	@Override
	public long getItemId(int posicion) {
		return lista.get(posicion).getId();
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup parent) {
		View vista=convertView;
		if(convertView==null){
			LayoutInflater infla=miContexto.getLayoutInflater();
			vista=infla.inflate(R.layout.modeloequipolista, null);
		}
		
		Equipo equipo = lista.get(posicion);
		ImageView imageBandera=(ImageView) vista.findViewById(R.id.ivEquipoBandera);
		imageBandera.setImageResource(equipo.getImagen());
		
		TextView pais=(TextView)vista.findViewById(R.id.tvPais1);
		pais.setText(equipo.getPais());
		
		TextView grupo=(TextView)vista.findViewById(R.id.tvGrupo1);
		grupo.setText("Grupo "+equipo.getGrupo());
		return vista;
	}

}
