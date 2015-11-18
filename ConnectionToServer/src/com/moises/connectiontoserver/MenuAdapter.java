package com.moises.connectiontoserver;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<ItemMenu>{
	Activity activity;
	
	public MenuAdapter(Activity activity, List<ItemMenu> lista_menu){
		super(activity, R.layout.item_menu, lista_menu);
		this.activity = activity;
	}
	
	public static class Holder{
		ImageView ivImagen;
		TextView tvTitulo;
	}
	
	@Override
	public ItemMenu getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	
	public View getView(int position, View viewConverter, ViewGroup parent){
		View view = viewConverter;
		final Holder holder;
		if(view==null){
			view = activity.getLayoutInflater().inflate(R.layout.item_menu, null);
			holder = new Holder();
			holder.ivImagen = (ImageView)view.findViewById(R.id.ivImageItemMenu);
			holder.tvTitulo = (TextView)view.findViewById(R.id.tvTituloItemMenu);
			view.setTag(holder);
		}else{
			holder = (Holder)view.getTag();
		}
		
		ItemMenu item = this.getItem(position);
		holder.ivImagen.setImageResource(item.getIcono());
		holder.tvTitulo.setText(item.getTitulo());
		
		return view;
	}

}
