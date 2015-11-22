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

public class AdapterPosiciones extends BaseAdapter{
	
	Activity miContexto;
	ArrayList<Equipo> listaEquipos;
	
	public AdapterPosiciones(Activity contexto, ArrayList<Equipo> lista){
		this.miContexto=contexto;
		this.listaEquipos=lista;
	}

	@Override
	public int getCount() {
		return listaEquipos.size();
	}

	@Override
	public Object getItem(int posicion) {
		return listaEquipos.get(posicion);
	}

	@Override
	public long getItemId(int posicion) {
		return listaEquipos.get(posicion).getId();
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup parent) {
		View vista=convertView;
		if(convertView==null){
			LayoutInflater inflar=miContexto.getLayoutInflater();
			vista=inflar.inflate(R.layout.modelolistaposiciones, null);
		}
		Equipo equi=listaEquipos.get(posicion);
		ImageView bandera=(ImageView)vista.findViewById(R.id.ivBandlpos);
		bandera.setImageResource(equi.getImagen());
		
		TextView equipo = (TextView)vista.findViewById(R.id.tvEquipolpos);
		equipo.setText(equi.getPais());
		
		TextView pts=(TextView)vista.findViewById(R.id.tvPTS);
		pts.setText("PTS. "+String.valueOf(equi.getPts()));
		
		TextView pg=(TextView)vista.findViewById(R.id.tvPG);
		pg.setText("PG. "+String.valueOf(equi.getPg()));
		
		TextView pe=(TextView)vista.findViewById(R.id.tvPE);
		pe.setText("PE. "+equi.getPe());
		
		TextView pp=(TextView)vista.findViewById(R.id.tvPP);
		pp.setText("PP. "+equi.getPp());
		
		TextView gf=(TextView)vista.findViewById(R.id.tvGF);
		gf.setText("GF. "+equi.getGf());
		
		TextView gc=(TextView)vista.findViewById(R.id.tvGC);
		gc.setText("GC. "+equi.getGc());
		return vista;
	}

}
