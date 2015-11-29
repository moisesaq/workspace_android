package com.silvia.adapters;

import java.util.List;


import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.android.gms.internal.ac;
import com.silvia.cooperativa.R;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListAddressAdapter extends ArrayAdapter<Address>{

	private Activity activity;
	
	public ListAddressAdapter(Activity activity, List<Address> listAddress){
		super(activity, R.layout.item_address,listAddress);
		this.activity = activity;
	}
	
	public static class ViewHolder{
		//@InjectView(R.id.tvAddressLine)
		TextView tvAddress;
		//@InjectView(R.id.lyVistaArea)
		LinearLayout lyVistaArea;
		//@InjectView(R.id.tvArea)
		TextView tvArea;
		//@InjectView(R.id.lyVistaCountry)
		LinearLayout lyVistaCountry;
		//@InjectView(R.id.tvCountry)
		TextView tvCountry;
		
//		public ViewHolder(View view){
//			ButterKnife.inject(this, view);
//		}
	}

	@Override
	public Address getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View view = convertView;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_address, null);
			holder = new ViewHolder();
			holder.tvAddress = (TextView)view.findViewById(R.id.tvAddressLine);
			holder.lyVistaArea = (LinearLayout)view.findViewById(R.id.lyVistaArea);
			holder.tvArea = (TextView)view.findViewById(R.id.tvArea);
			holder.lyVistaCountry = (LinearLayout)view.findViewById(R.id.lyVistaCountry);
			holder.tvCountry = (TextView)view.findViewById(R.id.tvCountry);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Address address = this.getItem(position);
		holder.tvAddress.setText(address.getAddressLine(0));
		if(address.getAdminArea()!=null){
			holder.lyVistaArea.setVisibility(View.VISIBLE);
			holder.tvArea.setText(address.getAdminArea());
		}
		
		if(address.getCountryName()!=null){
			holder.lyVistaCountry.setVisibility(View.VISIBLE);
			String txtAddress = address.getCountryName();
			if(address.getCountryCode()!=null){
				txtAddress += ", "+address.getCountryCode();
			}
			holder.tvCountry.setText(txtAddress);
		}
		return view;
	}
	
	
}
