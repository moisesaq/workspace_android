package com.silvia.cooperativa;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class SelectImage extends Activity{

	private ImageView imageView;
	private Activity activity;
	
	public SelectImage(ImageView imageView, Activity activity){
		this.imageView = imageView;
		this.activity = activity;
	}
	
	public void iniciarSeleccion(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		try {
			intent.putExtra("return-data", true);
			activity.getIntent();
			startActivityForResult(Intent.createChooser(intent, "Completar accion con"), Variables.PICK_FROM_GALLERY);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Dispositivo no soporta recorte de imagen", Toast.LENGTH_SHORT).show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==Variables.PICK_FROM_GALLERY && resultCode==RESULT_OK){
			Bundle extras1 = data.getExtras();
			if(extras1!=null){
				Bitmap imageBitmap = extras1.getParcelable("data");
				this.imageView.setImageBitmap(imageBitmap);
			}
		}
	}
}
