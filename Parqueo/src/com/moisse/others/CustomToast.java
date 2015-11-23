package com.moisse.others;

import com.example.parqueo.R;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

	View view;
	Activity activity;
	String message;
	int imagen, x, y;
	
	public CustomToast(Activity activity, String message, int icActionPerson, int x, int y){
		this.activity = activity;
		this.message = message;
		this.imagen = icActionPerson;
		this.x = x;
		this.y = y;
	}
	
	public void showCustomToast(){
		LayoutInflater inflater = activity.getLayoutInflater();
		view = inflater.inflate(R.layout.layout_custom_toast, null);
		TextView mensaje = (TextView)view.findViewById(R.id.tvMessageCT);
		mensaje.setText(message);
		ImageView image = (ImageView)view.findViewById(R.id.ivImagenCT);
		image.setImageResource(imagen);
		Toast toast = new Toast(activity);
		toast.setGravity(Gravity.RIGHT | Gravity.TOP, x, y);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}
}
