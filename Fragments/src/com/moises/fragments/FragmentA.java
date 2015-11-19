package com.moises.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentA extends Fragment{

	private View v;
	private TextView tvDatos;
	private Button btnTraer;
	private int color = -1;
	private String titulo;
	
	SeleccionarPersonaClickListener mCallBack;
	
	public static FragmentA newInstance(int color, String titulo){
		FragmentA frag = new FragmentA();
		Bundle bundle = new Bundle();
		bundle.putInt("color", color);
		bundle.putString("titulo", titulo);
		frag.setArguments(bundle);
		frag.setRetainInstance(true);
		return frag;
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getArguments()!=null){
			this.color = getArguments().getInt("color");
			this.titulo = getArguments().getString("titulo");
		}
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.fragment_a, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		tvDatos = (TextView)v.findViewById(R.id.tvDatos);
		if(titulo!=null){
			tvDatos.setText(titulo);
		}
		if(color != -1){
			v.setBackgroundColor(this.color);
		}
		btnTraer = (Button)v.findViewById(R.id.btnTraerDato);
		btnTraer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(getActivity().getApplicationContext(), SecondActivity.class);
				
				mCallBack.SeleccionarClick();
				try {
					FragmentA.this.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public interface SeleccionarPersonaClickListener{
		public void SeleccionarClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			mCallBack = (SeleccionarPersonaClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(e.toString()+" Debe implementar SeleccionarPersonaClickListener");
		}
	}
	
}
