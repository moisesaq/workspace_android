package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.modelo.NavDrawerItem;
import com.moisse.others.MyVar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem>{

	Activity activity;
	List<NavDrawerItem> listaMenu;
	private ImageLoader imageLoader;
	
	public NavDrawerListAdapter(Activity activity, List<NavDrawerItem> listaMenu){
		super(activity,R.layout.nav_drawer_item,listaMenu);
		this.activity=activity;
		this.listaMenu=listaMenu;
	}
	
	public class ViewHolder{
		ImageView icono;
		TextView title;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.nav_drawer_item, parent, false); 
			holder = new ViewHolder();
			holder.icono = (ImageView)view.findViewById(R.id.drawer_image);
			holder.title = (TextView)view.findViewById(R.id.title_option);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		imageLoader = ImageLoader.getInstance();
		NavDrawerItem drawerItem = listaMenu.get(position);
		if(drawerItem.getIdItem()>100){
			if(!drawerItem.getIcono_p().equals(MyVar.NO_ESPECIFICADO)){
				String path_image = new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(drawerItem.getIcono_p()).toString();
				imageLoader.displayImage("file://"+path_image, holder.icono, new SimpleImageLoadingListener(){
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						super.onLoadingStarted(imageUri, view);
						holder.icono.setImageResource(R.drawable.ic_insert_emoticon_white_18dp);
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						holder.icono.setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
						holder.icono.setImageResource(R.drawable.ic_insert_emoticon_white_18dp);
					}
				});
			}else{
				holder.icono.setImageResource(R.drawable.ic_insert_emoticon_white_18dp);
			}
		}else{
			holder.icono.setImageResource(drawerItem.getIcono());
			imageLoader.displayImage("drawable://"+drawerItem.getIcono(), holder.icono, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					super.onLoadingStarted(imageUri, view);
					holder.icono.setImageResource(R.drawable.ic_tag_faces_white_36dp);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.icono.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					super.onLoadingFailed(imageUri, view, failReason);
					holder.icono.setImageResource(R.drawable.ic_tag_faces_white_36dp);
				}
			});
		}
		
		holder.title.setText(new StringBuilder(drawerItem.getTitulo()));
		return view;
	}
}
