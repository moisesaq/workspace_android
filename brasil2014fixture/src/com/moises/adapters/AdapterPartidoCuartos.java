package com.moises.adapters;

import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.moises.modelo.PartidoCuartos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPartidoCuartos extends BaseAdapter{

	Activity miContexto;
	ArrayList<PartidoCuartos> listaCuartos;
	
	public AdapterPartidoCuartos(Activity contexto, ArrayList<PartidoCuartos> lista){
		this.miContexto=contexto;
		this.listaCuartos=lista;
	}

	@Override
	public int getCount() {
		return listaCuartos.size();
	}

	@Override
	public Object getItem(int posicion) {
		return listaCuartos.get(posicion);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup arg2) {
		View vista=convertView;
		if(convertView==null){
			LayoutInflater inflar=miContexto.getLayoutInflater();
			vista=inflar.inflate(R.layout.modelopartidocuartos, null);
		}
		PartidoCuartos parti=listaCuartos.get(posicion);
		
		TextView idc=(TextView)vista.findViewById(R.id.tvIDC);
		idc.setText(parti.getIdpartidocuartos());
		
		TextView idganador1=(TextView)vista.findViewById(R.id.tvIdGanador1C);
		idganador1.setText(parti.getIdganador1());
		
		ImageView band1=(ImageView)vista.findViewById(R.id.ivBandGanado1C);
		band1.setImageResource(parti.getBandera1());
		
		TextView ganador1=(TextView)vista.findViewById(R.id.tvPaisGanador1C);
		ganador1.setText(parti.getPaisganador1());
		
		ImageView band2=(ImageView)vista.findViewById(R.id.ivBandGanador2C);
		band2.setImageResource(parti.getBandera2());
		
		TextView ganador2=(TextView)vista.findViewById(R.id.tvPaisGanador2C);
		ganador2.setText(parti.getPaisganador2());
		
		TextView idganador2=(TextView)vista.findViewById(R.id.tvIdGanador2C);
		idganador2.setText(parti.getIdganador2());
		
		TextView fecha=(TextView)vista.findViewById(R.id.tvFechaC);
		fecha.setText("Fecha: "+parti.getFecha());
		
		TextView hora=(TextView)vista.findViewById(R.id.tvHoraC);
		hora.setText("Hora: "+parti.getHora());
		
		if(parti.getEstado()==1){
			TextView g1=(TextView)vista.findViewById(R.id.tvGol1Cuartos);
			g1.setText(String.valueOf(parti.getGol1()));
			
			TextView g2=(TextView)vista.findViewById(R.id.tvGol2Cuartos);
			g2.setText(String.valueOf(parti.getGol2()));
		}
		
		return vista;
	}
}
