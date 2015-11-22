package com.moises.adapters;

import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.example.brasil2014fixture.R.id;
import com.example.brasil2014fixture.R.layout;
import com.moises.data.base.fixture.DBFixture;
import com.moises.modelo.Partido;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPartidos extends BaseAdapter{
	
	Activity miContexto;
	ArrayList<Partido> listaPartidos;

	public AdapterPartidos(Activity contexto,ArrayList<Partido> lista){
		this.miContexto=contexto;
		this.listaPartidos=lista;
	}

	@Override
	public int getCount() {
		return listaPartidos.size();
	}

	@Override
	public Object getItem(int posicion) {
		return listaPartidos.get(posicion);
	}

	@Override
	public long getItemId(int posicion) {
		return listaPartidos.get(posicion).getIdpartido();
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup parent) {
		View vista=convertView;
		if(convertView==null){
			LayoutInflater inflar=miContexto.getLayoutInflater();
			vista=inflar.inflate(R.layout.modelolistapartidos, null);
		}
		Partido parti = listaPartidos.get(posicion);
		ImageView bandera1=(ImageView)vista.findViewById(R.id.ivBand1lp);
		bandera1.setImageResource(getImageBandera(parti.getPais1()));
		
		TextView pais1=(TextView)vista.findViewById(R.id.tvPais1lp);
		pais1.setText(parti.getPais1());
		
		ImageView bandera2=(ImageView)vista.findViewById(R.id.ivBand2lp);
		bandera2.setImageResource(getImageBandera(parti.getPais2()));
		
		TextView pais2=(TextView)vista.findViewById(R.id.tvPais2lp);
		pais2.setText(parti.getPais2());
		
		TextView fecha=(TextView)vista.findViewById(R.id.tvFechalp);
		fecha.setText("Fecha: "+parti.getFecha());
		
		TextView hora=(TextView)vista.findViewById(R.id.tvHoralp);
		hora.setText("Hora: "+parti.getHora());
		
		if(parti.getEstado()==1){
			TextView g1=(TextView)vista.findViewById(R.id.tvGol1lp);
			g1.setText(String.valueOf(parti.getGoles1()));
			
			TextView g2=(TextView)vista.findViewById(R.id.tvGol2lp);
			g2.setText(String.valueOf(parti.getGoles2()));
		}
		
		return vista;
	}
	
	public int getImageBandera(String pais){
		int imagen=0;
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			imagen=db.getImagenBandera(pais);
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
