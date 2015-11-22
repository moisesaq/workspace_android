package com.example.brasil2014fixture;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Campeon extends Activity{

	ImageView bandera;
	TextView pais;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campeon);
		
		bandera=(ImageView)findViewById(R.id.ivCampeon);
		pais=(TextView)findViewById(R.id.tvCampeon);
		
		Bundle encomienda=getIntent().getExtras();
		bandera.setImageResource(encomienda.getInt("bandera"));
		pais.setText(encomienda.getString("pais"));
	}
}
