package com.moises.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brasil2014fixture.R;
import com.moises.modelo.PartidoSemis;

public class AdapterPartidoSemis extends BaseAdapter{

	Activity miActivity;
	ArrayList<PartidoSemis> listaSemis;
	
	public AdapterPartidoSemis(Activity activity, ArrayList<PartidoSemis> listaSemis){
		this.miActivity=activity;
		this.listaSemis=listaSemis;
	}

	@Override
	public int getCount() {
		return listaSemis.size();
	}

	@Override
	public Object getItem(int posicion) {
		return listaSemis.get(posicion);
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
			vista=inflar.inflate(R.layout.modelopartidosemis, null);
		}
		PartidoSemis parti=listaSemis.get(posicion);
		
		TextView ids=(TextView)vista.findViewById(R.id.tvIDS);
		ids.setText(parti.getIdpartidosemis());
		
		TextView idganador1=(TextView)vista.findViewById(R.id.tvIdGanador1S);
		idganador1.setText(parti.getIdganador1());
		
		ImageView band1=(ImageView)vista.findViewById(R.id.ivBandGanado1S);
		band1.setImageResource(parti.getBandera1());
		
		TextView ganador1=(TextView)vista.findViewById(R.id.tvPaisGanador1S);
		ganador1.setText(parti.getPaisganador1());
		
		ImageView band2=(ImageView)vista.findViewById(R.id.ivBandGanador2S);
		band2.setImageResource(parti.getBandera2());
		
		TextView ganador2=(TextView)vista.findViewById(R.id.tvPaisGanador2S);
		ganador2.setText(parti.getPaisganador2());
		
		TextView idganador2=(TextView)vista.findViewById(R.id.tvIdGanador2S);
		idganador2.setText(parti.getIdganador2());
		
		TextView fecha=(TextView)vista.findViewById(R.id.tvFechaS);
		fecha.setText("Fecha: "+parti.getFecha());
		
		TextView hora=(TextView)vista.findViewById(R.id.tvHoraS);
		hora.setText("Hora: "+parti.getHora());
		
		if(parti.getEstado()==1){
			TextView g1=(TextView)vista.findViewById(R.id.tvGol1Semis);
			g1.setText(String.valueOf(parti.getGol1()));
			
			TextView g2=(TextView)vista.findViewById(R.id.tvGol2Semis);
			g2.setText(String.valueOf(parti.getGol2()));
		}
		
		return vista;
	}
	
}
