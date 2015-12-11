package com.moises.appsistemaparqueo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	
	Context micontexto;
	Integer[] Imagenes;
	

	
	public ImageAdapter(Context context, Integer[] imagenes){
		micontexto = context;
		Imagenes = imagenes;
	}

	@Override
	public int getCount() {
		return Imagenes.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ImageView imageView;
		if(convertView==null){
			imageView = new ImageView(micontexto);
			imageView.setLayoutParams(new GridView.LayoutParams(200,150));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(10, 10, 10, 10);
		}else{
			imageView = (ImageView)convertView;
		}
		
		imageView.setImageResource(Imagenes[position]);
		return imageView;
	}

}
