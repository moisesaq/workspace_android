package com.moises.googlemapstest;

import java.util.List;

import butterknife.InjectView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class ListAddressDialog extends DialogFragment implements OnItemClickListener{

	private View v;
	private ListView lvAddresses;
	private List<Address> list_address;
	public MainActivity mainActivity;
	
	public ListAddressDialog(List<Address> list_address, MainActivity mainActivity){
		this.list_address = list_address;
		this.mainActivity = mainActivity;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Quiso decir");
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.list_address, null);
		lvAddresses = (ListView)v.findViewById(R.id.lvAddresses);
		lvAddresses.setOnItemClickListener(this);
		ArrayAdapter<Address> adapter = new ArrayAdapter<Address>(getActivity(), android.R.layout.simple_list_item_1, this.list_address);
		lvAddresses.setAdapter(adapter);
		
		dialog.setNeutralButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setView(v);
		return dialog.create();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Address address = (Address)adapter.getAdapter().getItem(position);
		mainActivity.setLocation(address);
		dismiss();
	}
}
