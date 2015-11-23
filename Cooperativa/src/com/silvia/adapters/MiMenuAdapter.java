package com.silvia.adapters;

import java.util.List;

import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.ItemMiMenu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MiMenuAdapter extends ArrayAdapter<ItemMiMenu>{

	private Activity activity;
	
	public MiMenuAdapter(Activity activity, List<ItemMiMenu> lista_menu){
		super(activity, R.layout.modelo_mi_menu_item, lista_menu);
		this.activity = activity;
	}
	
	public class ViewHolder{
		ImageView ivImage;
		TextView tvTitulo;
		TextView tvDescripcion;
	}
	
	@Override
	public ItemMiMenu getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_mi_menu_item, null);
			holder = new ViewHolder();
			holder.ivImage = (ImageView)view.findViewById(R.id.imageMiMenu);
			holder.tvTitulo = (TextView)view.findViewById(R.id.titleMiMenu);
			holder.tvDescripcion = (TextView)view.findViewById(R.id.descriptionMiMenu);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		ItemMiMenu miMenu = this.getItem(position);
		if(miMenu.getIdmenu()==100){
			if(!miMenu.getIcono_personalizado().equals(Variables.SIN_ESPECIFICAR)){
				Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(miMenu.getIcono_personalizado()).toString());
				if(bitmap!=null){
					holder.ivImage.setImageBitmap(bitmap);
					holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
					holder.ivImage.setPadding(0, 0, 0, 0);
				}else{
					holder.ivImage.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
					holder.ivImage.setPadding(5, 5, 5, 5);
				}
			}else{
				holder.ivImage.setImageResource(R.drawable.ic_supervisor_account_white_48dp);
				holder.ivImage.setPadding(5, 5, 5, 5);
			}
		}else if(miMenu.getIdmenu()==101){
			holder.ivImage.setImageResource(miMenu.getIcono());
			holder.ivImage.setPadding(0, 0, 0, 0);
		}else{
			holder.ivImage.setImageResource(miMenu.getIcono());
			holder.ivImage.setPadding(5, 5, 5, 5);
		}
		holder.tvTitulo.setText(miMenu.getTitulo());
		holder.tvDescripcion.setText(miMenu.getDescripcion());
		return view;
	}
}
