package com.moises.broadcastreceiver;

import java.util.ArrayList;
import java.util.List;

import com.moises.broadcastreceiver.NewContact.SelectAddressClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lista extends Fragment implements OnItemClickListener{

	private View view;
	private ListView lvListOptions;
	private List<String> list_test;
	public OptionClickListener callBack;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.lista, container, false);
		initComponents(view);
		return view;
	}
	
	private void initComponents(View v) {
		list_test = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			list_test.add("Address"+(i+1));
		}
		lvListOptions = (ListView)v.findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_test);
		lvListOptions.setAdapter(adapter);
		lvListOptions.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View adapter, int position, long arg3) {
		String address = (String)parent.getAdapter().getItem(position);
		callBack.OptionClick(address);
	}

	public interface OptionClickListener{
		public void OptionClick(String address);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callBack = (OptionClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" Implemented OptionClickListener");
		}
	}
	
}
