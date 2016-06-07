package com.moises.ormlite;

import com.moises.practices.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListContactAdapter extends ArrayAdapter<Contact>{

	private Activity activity;
	
	public ListContactAdapter(Activity activity){
		super(activity, R.layout.item_contact);
		this.activity = activity;
	}
	
	public static class ViewHolder{
		TextView tvFullName;
		TextView tvAddress;
	}
	@Override
	public Contact getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if(view==null){
			view = activity.getLayoutInflater().inflate(R.layout.item_contact, null);
			holder = new ViewHolder();
			holder.tvFullName = (TextView)view.findViewById(R.id.tvName);
			holder.tvAddress = (TextView)view.findViewById(R.id.tvAddress);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Contact contact = this.getItem(position);
		holder.tvFullName.setText("Nombre: "+contact.getFullName());
		holder.tvAddress.setText("Direccion: "+contact.getAddress());
		return view;
	}
	
	

}
