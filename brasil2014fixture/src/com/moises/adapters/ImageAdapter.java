package com.moises.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	
	Integer[] IDImagenes;
	Context miContexto;

	public ImageAdapter(Context contexto, Integer[] imagenes){
		this.miContexto=contexto;
		this.IDImagenes=imagenes;
	}
	@Override
	public int getCount() {
		return IDImagenes.length;
	}

	@Override
	public Object getItem(int posicion) {
		return posicion;
	}

	@Override
	public long getItemId(int posicion) {
		return posicion;
	}

	@Override
	public View getView(int posicion, View convertView, ViewGroup arg2) {
		ImageView imagen;
		if(convertView==null){
			imagen=new ImageView(miContexto);
			imagen.setLayoutParams(new GridView.LayoutParams(85,85));
			imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imagen.setPadding(10, 10, 10, 10);
		}else{
			imagen=(ImageView)convertView;
		}
		imagen.setImageResource(IDImagenes[posicion]);
		return imagen;
	}

}
