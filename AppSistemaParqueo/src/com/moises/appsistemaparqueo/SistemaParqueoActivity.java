package com.moises.appsistemaparqueo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SistemaParqueoActivity extends Activity implements OnClickListener{
	
	ImageView entrar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sistema_parqueo);
		
		entrar = (ImageView)findViewById(R.id.imgvEnter);
		entrar.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sistema_parqueo, menu);
		return true;		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.imgvEnter){
			Intent i = new Intent("com.moises.appsistemaparqueo.MENU");
			startActivity(i);
		}
		
	}
}
