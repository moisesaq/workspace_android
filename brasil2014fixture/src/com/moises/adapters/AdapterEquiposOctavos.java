package com.moises.adapters;

import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.moises.modelo.EquipoOctavos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterEquiposOctavos extends BaseAdapter{
	
	Activity miActivity;
	ArrayList<EquipoOctavos> miLista;
	
	public AdapterEquiposOctavos(Activity activity, ArrayList<EquipoOctavos> listaOctavos){
		this.miActivity=activity;
		this.miLista=listaOctavos;
	}

	@Override
	public int getCount() {
		return miLista.size();
	}

	@Override
	public Object getItem(int posicion) {
		return miLista.get(posicion);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup parent) {
		View vista=convertView;
		if(convertView==null){
			LayoutInflater infla=miActivity.getLayoutInflater();
			vista=infla.inflate(R.layout.modeloequipooctavos, null);
		}
		
		EquipoOctavos equioct=miLista.get(posicion);
		TextView tvidOct=(TextView)vista.findViewById(R.id.tvIdOctavos);
		tvidOct.setText(equioct.getIdoctavos());
		
		ImageView ivband=(ImageView)vista.findViewById(R.id.ivBandPaisOctavos);
		ivband.setImageResource(equioct.getImagen());
		
		TextView tvpais=(TextView)vista.findViewById(R.id.tvPaisOctavos);
		tvpais.setText(equioct.getPais());
		
		TextView tvgrupo=(TextView)vista.findViewById(R.id.tvGrupoOctavos);
		tvgrupo.setText(equioct.getGrupo());
		return vista;
	}

}
