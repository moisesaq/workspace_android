package com.moises.animations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListViewCustom extends Fragment{

	public View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.list_view_custom, container, false);
		initComponents(view);
		return view;
	}
	
	//TODO aca falta personalizar sobre agregado un nuevo campo
	public void initComponents(View v){
		
	}

	
}
