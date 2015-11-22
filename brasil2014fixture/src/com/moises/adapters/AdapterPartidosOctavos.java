package com.moises.adapters;

import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.moises.data.base.fixture.DBFixture;
import com.moises.modelo.PartidoOctavos;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPartidosOctavos extends BaseAdapter{
	
	Activity miContexto;
	ArrayList<PartidoOctavos> listaPartidosOct;
	
	public AdapterPartidosOctavos(Activity context, ArrayList<PartidoOctavos> lista){
		this.miContexto=context;
		this.listaPartidosOct=lista;
	}

	@Override
	public int getCount() {
		return listaPartidosOct.size();
	}

	@Override
	public Object getItem(int posicion) {
		return listaPartidosOct.get(posicion);
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
			vista=inflar.inflate(R.layout.modelopartidooctavos, null);
		}
		
		PartidoOctavos parti = listaPartidosOct.get(posicion);
		
		TextView ido=(TextView)vista.findViewById(R.id.tvIDO);
		ido.setText(parti.getIdpartidooctavos());
		
		TextView idoctavos1=(TextView)vista.findViewById(R.id.tvIdOct1);
		idoctavos1.setText(parti.getIdoctavos1());
		
		ImageView bandera1=(ImageView)vista.findViewById(R.id.ivBand1Oct);
		bandera1.setImageResource(getImageBandera(parti.getIdoctavos1()));
		
		TextView pais1=(TextView)vista.findViewById(R.id.tvPais1Oct);
		pais1.setText(parti.getPais1());
		
		ImageView bandera2=(ImageView)vista.findViewById(R.id.ivBand2Oct);
		bandera2.setImageResource(getImageBandera(parti.getIdoctavos2()));
		
		TextView pais2=(TextView)vista.findViewById(R.id.tvPaisOct);
		pais2.setText(parti.getPais2());
		
		TextView idoctavos2=(TextView)vista.findViewById(R.id.tvIdOct2);
		idoctavos2.setText(parti.getIdoctavos2());
		
		TextView fecha=(TextView)vista.findViewById(R.id.tvFechaOct);
		fecha.setText(parti.getFecha().toString());
		
		TextView hora=(TextView)vista.findViewById(R.id.tvHoraOct);
		hora.setText(parti.getHora().toString());
		
		if(parti.getEstado()==1){
			TextView g1=(TextView)vista.findViewById(R.id.tvGol1Oct);
			g1.setText(String.valueOf(parti.getGol1()));
			
			TextView g2=(TextView)vista.findViewById(R.id.tvGol2Oct);
			g2.setText(String.valueOf(parti.getGol2()));
		}
		
		return vista;
	}
	
	public int getImageBandera(String idoctavos){
		int imagen=0;
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			imagen=db.buscarBanderaPaisOctavos(idoctavos);
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			Dialog mensaje=new Dialog(miContexto);
			mensaje.setTitle("Error al cargar lista");
			TextView texto=new TextView(miContexto);
			texto.setText(error);
			mensaje.setContentView(texto);
			mensaje.show();
		}
		return imagen;
	}

}
