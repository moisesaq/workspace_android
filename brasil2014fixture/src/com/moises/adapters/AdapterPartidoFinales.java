package com.moises.adapters;

import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.moises.modelo.PartidoFinales;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPartidoFinales extends BaseAdapter{

	Activity miActivity;
	ArrayList<PartidoFinales> listaFinales;
	
	public AdapterPartidoFinales(Activity activity, ArrayList<PartidoFinales> listaFinales){
		this.miActivity=activity;
		this.listaFinales=listaFinales;
	}
	
	@Override
	public int getCount() {
		return listaFinales.size();
	}

	@Override
	public Object getItem(int posicion) {
		return listaFinales.get(posicion);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup arg2) {
		View vista=convertView;
		if(convertView==null){
			LayoutInflater inflar=miActivity.getLayoutInflater();
			vista=inflar.inflate(R.layout.modelopartidofinales, null);
		}
		PartidoFinales parti=listaFinales.get(posicion);
		
		TextView idganador1=(TextView)vista.findViewById(R.id.tvIdGanador1F);
		idganador1.setText(parti.getIdganador1());
		
		ImageView band1=(ImageView)vista.findViewById(R.id.ivBandGanado1F);
		band1.setImageResource(parti.getBandera1());
		
		TextView ganador1=(TextView)vista.findViewById(R.id.tvPaisGanador1F);
		ganador1.setText(parti.getPaisganador1());
		
		ImageView band2=(ImageView)vista.findViewById(R.id.ivBandGanador2F);
		band2.setImageResource(parti.getBandera2());
		
		TextView ganador2=(TextView)vista.findViewById(R.id.tvPaisGanador2F);
		ganador2.setText(parti.getPaisganador2());
		
		TextView idganador2=(TextView)vista.findViewById(R.id.tvIdGanador2F);
		idganador2.setText(parti.getIdganador2());
		
		TextView fecha=(TextView)vista.findViewById(R.id.tvFechaF);
		fecha.setText("Fecha: "+parti.getFecha());
		
		TextView hora=(TextView)vista.findViewById(R.id.tvHoraF);
		hora.setText("Hora: "+parti.getHora());
		
		if(parti.getEstado()==1){
			TextView g1=(TextView)vista.findViewById(R.id.tvGol1Finales);
			g1.setText(String.valueOf(parti.getGol1()));
			
			TextView g2=(TextView)vista.findViewById(R.id.tvGol2Finales);
			g2.setText(String.valueOf(parti.getGol2()));
		}
		
		return vista;
	}

}
