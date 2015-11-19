package com.moisse.fragments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImageCamera extends Fragment implements OnClickListener{

	ImageView ivVistaImagen;
	EditText etBuscado;
	CheckBox cboxServidor;
	Button btnBuscar, btnGuardar, btnSubir, btnCapturar, btnCompartir;
	ProgressBar pbEspera;
	
	private static final String RUTA_IMAGES_DESCARGAS_MOISSE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/ImagesMoisse/";
	
	Bitmap finalImageBitmap;
	File finalImageFile;
	
	private Uri picUri;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.images_camera, container, false);
		setHasOptionsMenu(true);
//		ActionBar actionBar = getActivity().getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ivVistaImagen = (ImageView)v.findViewById(R.id.ivVistaImagen);
		ivVistaImagen.setOnClickListener(this);
		pbEspera = (ProgressBar)v.findViewById(R.id.pbEspera);
		pbEspera.setVisibility(View.INVISIBLE);
		etBuscado = (EditText)v.findViewById(R.id.etBusquedaIC);
		cboxServidor = (CheckBox)v.findViewById(R.id.cboxServidorIC);
		cboxServidor.setChecked(true);
		btnBuscar = (Button)v.findViewById(R.id.btnBuscarImagenIC);
		btnBuscar.setOnClickListener(this);
		btnGuardar = (Button)v.findViewById(R.id.btnGuardarImagenIC);
		btnGuardar.setOnClickListener(this);
		btnSubir = (Button)v.findViewById(R.id.btnSubirImagenIC);
		btnSubir.setOnClickListener(this);
		btnCapturar = (Button)v.findViewById(R.id.btnCapturarImagenIC);
		btnCapturar.setOnClickListener(this);
		btnCompartir = (Button)v.findViewById(R.id.btnCompartirImagenIC);
		btnCompartir.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(btnBuscar.getId()==v.getId()){
			String nombre_imagen = etBuscado.getText().toString().trim();
			if(cboxServidor.isChecked()){
				new CargarImagen().execute(nombre_imagen);
			}			
		}else if (btnGuardar.getId()==v.getId()) {
			if(finalImageBitmap!=null){
				saveImage();
			}else{
				Toast.makeText(getActivity(), "Primero busque una imagen", Toast.LENGTH_SHORT).show();
			}
		}else if(ivVistaImagen.getId()==v.getId()) {
			selectImage();
		}else if(btnSubir.getId()==v.getId()){
			if(finalImageFile!=null){
				new SubirImagen().execute();
			}else{
				Toast.makeText(getActivity(), "Primero selecciones una imagen", Toast.LENGTH_SHORT).show();
			}
		}else if (btnCapturar.getId()==v.getId()) {
			captureImage();
		}
	}
	
	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, MyVar.SELECT_IMAGE);
	}
	
	public void captureImage(){
		File ruta_images = new File(RUTA_IMAGES_DESCARGAS_MOISSE);
		ruta_images.mkdirs();
		try {
			Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String nameImage = etBuscado.getText().toString().trim()+".jpg";
			File fileImage = new File(ruta_images, nameImage);
			//Uri uriSaveImage = Uri.fromFile(fileImage);
			picUri = Uri.fromFile(fileImage);
			captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
			//fileImage.delete();
			startActivityForResult(captureIntent, MyVar.CAPTURE_IMAGE);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			String mensaje = "Lo siento, el dispositivo no soporta captura de imagen";
			Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==MyVar.SELECT_IMAGE && resultCode==getActivity().RESULT_OK){
			//Uri imageSelected = data.getData();
			picUri = data.getData();
			finalImageBitmap = BitmapFactory.decodeFile(getRealPathFromURI(picUri));
			if(finalImageBitmap.getWidth()>640 && finalImageBitmap.getHeight()>640){
				performCrop();
			}else{
				ivVistaImagen.setImageBitmap(finalImageBitmap);
			}
			
//			try{
//				InputStream istream = getActivity().getContentResolver().openInputStream(imageSelected);
//				BufferedInputStream bis = new BufferedInputStream(istream);
//				Bitmap myBitmap = BitmapFactory.decodeStream(bis);
//				if(myBitmap!=null){
//					ivVistaImagen.setImageBitmap(myBitmap);
//					String pathImage = getRealPathFromURI(imageSelected);
//					Toast.makeText(getActivity(), "Imagen seleccionado "+pathImage, Toast.LENGTH_SHORT).show();
//					finalImageFile = new File(pathImage);
//					btnSubir.setEnabled(true);
//				}else{
//					Toast.makeText(getActivity(), "Ocurrio error al seleccionar imagen ", Toast.LENGTH_SHORT).show();
//				}
//			}catch(FileNotFoundException e){
//				e.printStackTrace();
//			}
		}else if (requestCode==MyVar.CAPTURE_IMAGE && resultCode==getActivity().RESULT_OK) {
			//picUri = data.getData();
			ivVistaImagen.setImageURI(picUri);
			performCrop();
			//scanImage(getRealPathFromURI(picUri));
		}else if(requestCode==MyVar.PIC_CROP && resultCode==getActivity().RESULT_OK){
			Bundle extras = data.getExtras();
			//Bitmap thePic = extras.getParcelable("data");
			finalImageBitmap = extras.getParcelable("data");
			ivVistaImagen.setImageBitmap(finalImageBitmap);
		}
	}
	
	private void performCrop(){
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			cropIntent.setDataAndType(picUri, "image/*");
			cropIntent.putExtra("crop", true);
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			cropIntent.putExtra("return-data", true);
			startActivityForResult(cropIntent, MyVar.PIC_CROP);
		} catch (ActivityNotFoundException e) {
			String mensaje = "Lo siento, el dispositivo no soporta recorte de imagen";
			Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
		}
	}
	
	public class CargarImagen extends AsyncTask<String , Void, Bitmap>{
		//String dir_imagen = "http://aqmoises.zoedev.com/android/images_subidas/";//"http://192.168.1.100/android/imagenes/";
		URL url_image = null;
		Bitmap myBitmap = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pbEspera.setVisibility(View.VISIBLE);
			ivVistaImagen.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String nombre_imagen = params[0];
			try{
				url_image = new URL(MyVar.RUTA_IMAGES_SERVER+nombre_imagen);
				HttpURLConnection conn = (HttpURLConnection)url_image.openConnection();
				conn.connect();
				myBitmap = BitmapFactory.decodeStream(conn.getInputStream());
			}catch(IOException e){
				e.printStackTrace();
			}
			return myBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result!=null){
				pbEspera.setVisibility(View.INVISIBLE);
				ivVistaImagen.setVisibility(View.VISIBLE);
				ivVistaImagen.setImageBitmap(myBitmap);
				finalImageBitmap = myBitmap;
				btnGuardar.setEnabled(true);
			}else{
				pbEspera.setVisibility(View.INVISIBLE);
				ivVistaImagen.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No se encontro la imagen en el servidor", Toast.LENGTH_SHORT).show();
			}
		}		
	}
	
	public void saveImage(){
		File ruta_images = new File(RUTA_IMAGES_DESCARGAS_MOISSE);
		if(!ruta_images.exists()){
			ruta_images.mkdirs();
		}
		String nameImage = etBuscado.getText().toString().trim()+".jpg";
		File fileImage = new File(ruta_images, nameImage);
		if(fileImage.exists()){
			fileImage.mkdirs();
		}
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(fileImage);
			finalImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.flush();
			scanImage(fileImage.getAbsolutePath());
			Toast.makeText(getActivity(), "Imagen guardado "+fileImage.getAbsolutePath(), Toast.LENGTH_SHORT).show();
			//btnGuardar.setEnabled(false);
			finalImageBitmap = null;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
//		if(fileImage.exists()){
//			Toast.makeText(getActivity(), "Este imagen ya existe "+fileImage.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//			//btnGuardar.setEnabled(false);
//			finalImageBitmap = null;
//		}else{
//			FileOutputStream fos = null;
//			try{
//				fos = new FileOutputStream(fileImage);
//				finalImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//				fos.flush();
//				scanImage(fileImage.getAbsolutePath());
//				Toast.makeText(getActivity(), "Imagen guardado "+fileImage.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//				//btnGuardar.setEnabled(false);
//				finalImageBitmap = null;
//			}catch(FileNotFoundException e){
//				e.printStackTrace();
//			}catch(IOException e){
//				e.printStackTrace();
//			}
//		}	
	}
	
	public class SubirImagen extends AsyncTask<Void, Void, Boolean>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Subiendo imagen..");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Void... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"subir_imagen.php");
			try{
				MultipartEntity mpEntity = new MultipartEntity();
				ContentBody cbody = new FileBody(finalImageFile, "image/jgp");
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
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Foto subido correctamente", Toast.LENGTH_SHORT).show();
				//btnSubir.setEnabled(false);
				finalImageFile = null;
			}else{
				Toast.makeText(getActivity(), "No se pudo subir imagen", Toast.LENGTH_SHORT).show();
			}
		}
		
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
	
	public String getRealPathFromURI(Uri contentUri){
		@SuppressWarnings("unused")
		String [] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().managedQuery(contentUri, null, null, null, null);	
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
		
}
