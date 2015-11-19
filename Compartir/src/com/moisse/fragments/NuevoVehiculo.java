package com.moisse.fragments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.moisse.compartir.MyVar;
import com.moisse.compartir.R;
import com.moisse.database.DBVehiculo;
import com.moisse.database.HttpClientCustom;
import com.moisse.modelo.Vehiculo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NuevoVehiculo extends Fragment implements OnClickListener{
	
	ImageView ivImageVehiculo;
	EditText etPlaca, etMarca, etColor;
	Button btnListo;
	
	HttpClientCustom httpCustom;
	
	private int OPCION_SELECCIONADO = 0;
	private String pathImageVehiculo = MyVar.NO_ESPECIFICADO;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.nuevo_vehiculo,container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setTitle("Nuevo vehiculo");
		
		ivImageVehiculo = (ImageView)v.findViewById(R.id.ivImagenVehiculo);
		ivImageVehiculo.setOnClickListener(this);
		etPlaca = (EditText)v.findViewById(R.id.etPlaca);
		//etPlaca.setText(getMiShare());
		etMarca = (EditText)v.findViewById(R.id.etMarca);
		etColor = (EditText)v.findViewById(R.id.etColor);
		
		btnListo = (Button)v.findViewById(R.id.btnListo);
		btnListo.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		if(ivImageVehiculo.getId() == v.getId()){
			DialogSelectImage dSelectImage = new DialogSelectImage(this);
			dSelectImage.show(getFragmentManager(), "tagDSI");
			//Toast.makeText(getActivity(), "Placa share: "+getMiShare(), Toast.LENGTH_SHORT).show();
		}else if(btnListo.getId() == v.getId()){
			validarDatos();
			//almacenarShare(etPlaca.getText().toString());
		}
	}
	
	@SuppressLint("DefaultLocale")
	private void validarDatos() {
		String placa = etPlaca.getText().toString().trim().toUpperCase();
		
		String marca = etMarca.getText().toString().trim(); 
		if(marca.equals("")){ marca = MyVar.NO_ESPECIFICADO; }
		
		String color = etColor.getText().toString().trim();
		if(color.equals("")){ color = MyVar.NO_ESPECIFICADO; }
		
		if(OPCION_SELECCIONADO == MyVar.SELECT_IMAGE && !this.pathImageVehiculo.equals(MyVar.NO_ESPECIFICADO)){
			copySaveImage();
		}
		if(!placa.equals("")){
			Vehiculo vehiculo = new Vehiculo(placa, marca, color, pathImageVehiculo, 1);
			new VerifyPlaca().execute(vehiculo);
		}else{
			DialogMensaje dMsj = new DialogMensaje("La placa es obligatorio y debe contener como minimo 6 caracteres");
			dMsj.show(getFragmentManager(), "tagDM");
		}
	}

	public void obtenerImagenVehiculo(int opcion){
		if(opcion==0){
			selectImage();
			OPCION_SELECCIONADO = MyVar.SELECT_IMAGE;
		}else if(opcion==1){
			captureImage();
			OPCION_SELECCIONADO = MyVar.CAPTURE_IMAGE;
		}
	}
	
	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, MyVar.SELECT_IMAGE);
	}
	
	public void captureImage(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_VEHICULOS);
		ruta_images.mkdirs();
		
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		String nameImage = getNameImageVehiculo()+".jpg";
		File fileImage = new File(ruta_images, nameImage);
		Uri uriSaveImage = Uri.fromFile(fileImage);
		this.pathImageVehiculo = fileImage.getAbsolutePath();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSaveImage);
		startActivityForResult(intent, MyVar.CAPTURE_IMAGE);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == MyVar.SELECT_IMAGE){
			if(resultCode==getActivity().RESULT_OK){
				Uri imageSelected = data.getData();
				try{
					InputStream iStream = getActivity().getContentResolver().openInputStream(imageSelected);
					BufferedInputStream bis = new BufferedInputStream(iStream);
					Bitmap finalImageBitmap = BitmapFactory.decodeStream(bis);
					if(finalImageBitmap!=null){
						ivImageVehiculo.setImageBitmap(finalImageBitmap);
						this.pathImageVehiculo = getRealPathFromURI(imageSelected);
						Toast.makeText(getActivity(), "Imagen seleccionado "+pathImageVehiculo, Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getActivity(), "Ocurrio algun error al seleccionar imagen ", Toast.LENGTH_SHORT).show();
					}
				} catch(FileNotFoundException e){
					e.printStackTrace();
				}
			}
		}else if(requestCode == MyVar.CAPTURE_IMAGE && resultCode==getActivity().RESULT_OK){
			Bitmap myBitmap = BitmapFactory.decodeFile(this.pathImageVehiculo);
			if(myBitmap!=null){
				ivImageVehiculo.setImageBitmap(myBitmap);
				scanImage(this.pathImageVehiculo);
				Toast.makeText(getActivity(), "Imagen capturado y guardado en "+pathImageVehiculo, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), "Ocurrio algun error no se capturo correctamente la imagen ", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getNameImageVehiculo(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String imageCode = "pic_vehiculo_"+date;
		return imageCode;
	}
	
	public String getRealPathFromURI(Uri contentUri){
		@SuppressWarnings("unused")
		String [] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().managedQuery(contentUri, null, null, null, null);	
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public void limpiarCampos(){
		etPlaca.getText().clear();
		etMarca.getText().clear();
		etColor.getText().clear();
		ivImageVehiculo.setImageResource(R.drawable.ic_car);
		this.pathImageVehiculo = MyVar.NO_ESPECIFICADO;
		OPCION_SELECCIONADO = 0;
	}
	
	public class VerifyPlaca extends AsyncTask<Vehiculo, Void, Boolean>{
		ProgressDialog pd;
		Vehiculo vehiculo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();	
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Verificando placa..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Vehiculo... params) {
			vehiculo = params[0];
			httpCustom = new HttpClientCustom();
			if(httpCustom.verifyPlacaDisponible(vehiculo)){
				return true;
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				//Toast.makeText(getActivity(), "Placa esta disponible", Toast.LENGTH_SHORT).show();
				new RegistrarVehiculo().execute(this.vehiculo);
			}else{
				DialogMensaje dMsj = new DialogMensaje("La placa introducida ya esta registrado por favor verifique bien sus datos");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	public class RegistrarVehiculo extends AsyncTask<Vehiculo, Void, Boolean>{
		ProgressDialog pd;
		Vehiculo vehiculo;
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Registrando vehiculo..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Vehiculo... params){
			vehiculo = params[0];
			httpCustom = new HttpClientCustom();
			DBVehiculo db = new DBVehiculo(getActivity());
			if(httpCustom.registrarVehiculo(this.vehiculo)){
				if(!pathImageVehiculo.equals(MyVar.NO_ESPECIFICADO)){
					uploadImage();
				}
				try{
					db.openDB();
					if(db.insertarVehiculo(this.vehiculo)){
						return true;
					}
					db.closeDB();
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Vehiculo registrado exitosamente", Toast.LENGTH_SHORT).show();
				limpiarCampos();
			}else{
				DialogMensaje dMsj = new DialogMensaje("Vehiculo no se pudo registrar intenten mas tarde...! ");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	public Boolean copySaveImage(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_VEHICULOS);
		if(!ruta_images.exists()){
			ruta_images.mkdir();
		}
		
		String name_image = new File(pathImageVehiculo).getName();
		File fileImageVehiculo = new File(ruta_images, name_image);
		Bitmap myBitmap = BitmapFactory.decodeFile(this.pathImageVehiculo);
		if(fileImageVehiculo.exists()){
			this.pathImageVehiculo = fileImageVehiculo.getAbsolutePath();
			return true;
		}else{
			FileOutputStream fos;
			try{
				fos = new FileOutputStream(fileImageVehiculo);
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
				fos.flush();
				scanImage(fileImageVehiculo.getAbsolutePath());
				//Toast.makeText(getActivity(), "Imagen guardado", Toast.LENGTH_SHORT).show();
				this.pathImageVehiculo = fileImageVehiculo.getAbsolutePath();
				return true;
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public Boolean uploadImage(){
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"subir_imagen.php");
		try{
			MultipartEntity mpEntity = new MultipartEntity();
			File finalImageFile = new File(this.pathImageVehiculo);
			ContentBody cbody = new FileBody(finalImageFile, "image/jpeg");
			mpEntity.addPart("imageUp", cbody);
			httpPost.setEntity(mpEntity);
			HttpResponse response = httpClient.execute(httpPost);
			httpClient.getConnectionManager().shutdown();
			HttpEntity entity = response.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void scanImage(final String imagen){
		new MediaScannerConnectionClient() {
			private MediaScannerConnection msc = null; {
				msc = new MediaScannerConnection(getActivity().getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(imagen, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}
}
