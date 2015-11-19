package com.moises.httpurlconnection;

import com.moises.connectiontoserver.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DatosLugar extends Fragment{

	public View v;
	private TextView tvNombreLugar, tvDireccion, tvDescripcion;
	private Lugar lugar;
	
	public static DatosLugar newInstance(Lugar lugar){
		DatosLugar datos_lugar = new DatosLugar();
		Bundle bundle = new Bundle();
		bundle.putSerializable("lugar", lugar);
		datos_lugar.setArguments(bundle);
		return datos_lugar;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.lugar = (Lugar)getArguments().getSerializable("lugar");
	}



	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.datos_lugar, container, false);
		iniciarComponetes(v);
		return v;
	}
	private void iniciarComponetes(View v2) {
		tvNombreLugar = (TextView)v2.findViewById(R.id.tvNombreDatosLugar);
		tvDireccion = (TextView)v2.findViewById(R.id.tvDireccionDatosLugar);
		tvDescripcion = (TextView)v2.findViewById(R.id.tvDescripcionDatosLugar);
		tvNombreLugar.setText("Lugar: "+lugar.getNombre_lugar());
		tvDireccion.setText("Direccion: "+lugar.getDireccion());
		tvDescripcion.setText("Descripcion: "+lugar.getDescripcion());
	}
}
