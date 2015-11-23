package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ParkingAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogEntradaVehiculo;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.dialogs.DialogSalidaVehiculo;
import com.moisse.dialogs.DialogUpdate;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class Parking extends Fragment implements OnItemClickListener, OnClickListener{
	
	private GridView gvParking;
	public AutoCompleteTextView actvPlaca;
	private ImageButton ibtnOKSalida;
	private Parqueo parqueo_online;
	private String idparqueo;

	private HttpClientCustom httpCustom;
	ProgressDialog pd;
	
	public int DIGITOS_PARA_VALIDACION = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View viewRoot = inflater.inflate(R.layout.parking, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(viewRoot);
		return viewRoot;
	}
	
	public void inicializarComponentes(View view){
		ActionBar actionB = getActivity().getActionBar();
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionB.setTitle(new StringBuilder("Parking"));
		httpCustom = new HttpClientCustom();
		Bundle mensaje = getArguments();
		this.idparqueo = mensaje.getString("idparqueo");
		this.parqueo_online = getParqueoOnline(this.idparqueo);
		actvPlaca = (AutoCompleteTextView)view.findViewById(R.id.actvPlacaSalidaRapidaParking);
		actvPlaca.addTextChangedListener(placaWacher);
		ibtnOKSalida = (ImageButton)view.findViewById(R.id.ibtnOKSalidaRapidaParking);
		ibtnOKSalida.setOnClickListener(this);
		gvParking = (GridView)view.findViewById(R.id.gridViewParking);
		gvParking.setOnItemClickListener(this);
		cargarCarriles();
	}
	
	private final TextWatcher placaWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(s.length()>=0 && s.length()<=2){
				actvPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
			}else if(s.length()==3){
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				//actvPlaca.setFilters(new InputFilter[]{MyVar.filterNumLetter});
			}else if(s.length()==4){
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				if(isNumeric(s.toString())){
					DIGITOS_PARA_VALIDACION = 7;
					actvPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
				}else{
					DIGITOS_PARA_VALIDACION = 6;
					actvPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
				}
			}else if (s.length()>=5) {
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				//actvPlaca.setFilters(new InputFilter[]{MyVar.filterLetter, new InputFilter.LengthFilter(DIGITOS_PARA_VALIDACION)});
			}
		}
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	public static boolean isNumeric(String dato){
		try{
			Integer.parseInt(dato);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public void cargarCarriles(){
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			List<Carril> listaCarril = db.getCarrilesParaParking(this.parqueo_online);
			ParkingAdapter adapter = new ParkingAdapter(getActivity().getApplicationContext(), listaCarril, getActivity());
			gvParking.setAdapter(adapter);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_download).setVisible(true);
		menu.findItem(R.id.action_refresh).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_refresh){
			cargarCarriles();
			return true;
		}else if (item.getItemId()==R.id.action_download) {
			DialogUpdate dialogUpdate = new DialogUpdate(parqueo_online, this);
			dialogUpdate.show(getFragmentManager(), "tagDialogUpdate");
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Carril carril = (Carril)parent.getAdapter().getItem(position);
		if(carril.getDisponible().equals(MyVar.SI)){
			DialogEntradaVehiculo dev = new DialogEntradaVehiculo(this, carril, getActivity());
			dev.show(getFragmentManager(), "tagDEntradaVehiculo");
		}else{
			DialogSalidaVehiculo dsv = new DialogSalidaVehiculo(this, carril, this.parqueo_online, getActivity());
			dsv.show(getFragmentManager(), "tagDSalidaVehiculo");
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==ibtnOKSalida.getId()){
			registrarSalidaRapida(actvPlaca.getText().toString().trim().toUpperCase());
		}
	}
	
	public void registrarSalidaRapida(String placa){
		DBParqueo db = new DBParqueo(getActivity());
		try {
			if(!placa.equals("")){
				if(placa.length()>=6){
					if(placa.length()==DIGITOS_PARA_VALIDACION){
						if(MyVar.isPlaca(placa)){
							db.openSQLite();
							Vehiculo vehi = db.buscarVehiculoPorPlaca(placa, this.idparqueo);
							if(vehi!=null){
								Carril carril = db.getCarrilConVehiculo(vehi.getIdvehiculo(), this.idparqueo);
								if(carril!=null){
									DialogSalidaVehiculo dsv1 = new DialogSalidaVehiculo(this, carril, this.parqueo_online, getActivity());
									dsv1.show(getFragmentManager(), "tagDSalidaVehiculo1");
								}else{
									Toast.makeText(getActivity(), "Vehiculo no se encuentra en el parqueo", Toast.LENGTH_LONG).show();
								}
							}else{
								actvPlaca.requestFocus();
								actvPlaca.setError("Esta placa no esta registrado");
								Toast.makeText(getActivity(), "Vehiculo no se encuentra en el parqueo", Toast.LENGTH_LONG).show();
							}
							db.closeSQLite();
						}else{
							actvPlaca.requestFocus();
							actvPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
						}
					}else{
						actvPlaca.requestFocus();
						actvPlaca.setError("Placa incorrecta falta un dígito");
					}
				}else{
					actvPlaca.requestFocus();
					actvPlaca.setError("Placa del vehiculo debe tener 6 o 7 dígito");
				}
			}else{
				actvPlaca.requestFocus();
				actvPlaca.setError("Introduzca placa");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Parqueo getParqueoOnline(String idparqueo){
		Parqueo parqueo = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			parqueo = db.getParqueo(idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parqueo;
	}
	
	public List<Carril> getListaCarril(){
		List<Carril> lista = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			lista = db.getAllCarril(this.parqueo_online);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	//DE AQUI PA ABAJO ES PA ACTUALIZAR SOLO CARRILES PERO CON LA OPCION DE ADICION DE CARRILES NO SE ESTA UTILIZANDO
	public class ActualizarCarriles extends AsyncTask<List<Carril>, Void, Integer>{
		List<Carril> lista_carriles_obtenidos = null;
		List<Carril> lista_actual;
		int cont = 0;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando carriles...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Integer doInBackground(List<Carril>... params) {
			lista_actual = params[0];
			lista_carriles_obtenidos = httpCustom.cargarCarriles(idparqueo);
			if(lista_carriles_obtenidos!=null && lista_carriles_obtenidos.size()!=0){
				for (int i = 0; i < lista_actual.size(); i++) {
					Carril carril_actual = lista_actual.get(i);
					Carril carril_obtenido = lista_carriles_obtenidos.get(i);
					
					if(carril_obtenido.getDisponible().equals(carril_actual.getDisponible())){
						if(!carril_obtenido.getDisponible().equals(MyVar.SI)){
							verificarRegistrarDatosClienteVehiculo(carril_obtenido);
						}
						cont++;
					}else{
						if(!carril_obtenido.getDisponible().equals(MyVar.SI) && carril_actual.getDisponible().equals(MyVar.SI)){
							if(verificarRegistrarDatosClienteVehiculo(carril_obtenido)){
								actualizarCarril(carril_obtenido);
								cont++;
							}
						}else {
							if(carril_obtenido.getDisponible().equals(MyVar.SI) && !carril_actual.getDisponible().equals(MyVar.SI)){
								actualizarCarril(carril_obtenido);
								cont++;
							}
						}
						
					}
				}
			}
			return cont;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(result!=0){
				new CargarIngresoVehiculos().execute();
				cargarCarriles();
				Toast.makeText(getActivity(), "Carriles actualizados", Toast.LENGTH_SHORT).show();
			}else{
				pd.dismiss();
				DialogMensaje dmsj = new DialogMensaje("No se actualizaron correctamente los carriles, intente mas tarde..!");
				dmsj.show(getFragmentManager(), "tagDM");
			}
		}
		
		private void actualizarCarril(Carril carril_obtenido) {
			DBParqueo db = new DBParqueo(getActivity());
			try {
				db.openSQLite();
				db.actualizarCarril(carril_obtenido);
				db.closeSQLite();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		public boolean verificarRegistrarDatosClienteVehiculo(Carril carril_obtenido){
			String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
			String idvehiculo = carril_obtenido.getDisponible();
			Vehiculo vehiculo = httpCustom.getDatosVehiculo(idvehiculo);
			
			DBParqueo db = new DBParqueo(getActivity());
			if(!vehiculo.getIdcliente().equals(id_client_default)){
				Cliente cliente = httpCustom.getDatosCliente(vehiculo.getIdcliente());
				if(cliente!=null){
					try {
						db.openSQLite();
						if(!cliente.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							downloadSaveImage(cliente.getImagen());
						}
						if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							downloadSaveImage(vehiculo.getImagen());
						}
						
						if(db.existeVehiculo(idvehiculo)){
							if(db.actualizarCliente(cliente) && db.actualizarVehiculo(vehiculo)){							
								return true;
							}
						}else{
							if(db.insertarCliente(cliente)){
								if(db.insertarVehiculo(vehiculo)){
									return true;
								}
							}
						}
						db.closeSQLite();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					db.openSQLite();
					if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
						downloadSaveImage(vehiculo.getImagen());
					}
					if(db.existeVehiculo(idvehiculo)){
						if(db.actualizarVehiculo(vehiculo)){							
							return true;
						}
					}else{
						if(db.insertarVehiculo(vehiculo)){
							return true;
						}
					}
					db.closeSQLite();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
	}

	public class CargarIngresoVehiculos extends AsyncTask<Void, Void, Void>{
		List<Resguardo> lista_ingresos = null;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd.setMessage("Actualizando ingresos...");
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			lista_ingresos = httpCustom.cargarIngresosResguardos(idparqueo);
			if(lista_ingresos!=null && lista_ingresos.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					for (int i = 0; i < lista_ingresos.size(); i++) {
						Resguardo resguardo = lista_ingresos.get(i);
						if(!db.verificarResguardo(resguardo.getIdresguardo())){
							db.insertarResguardo(resguardo);
						}else{
							db.editarNotaResguardo(resguardo);
						}
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pd.dismiss();
			Toast.makeText(getActivity(), "Ingresos actualizados", Toast.LENGTH_SHORT).show();
			//new CargarResguardos().execute();
		}
	}
	
	public java.util.Date getFechaReducido(java.util.Date date, int dias){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}
	
	public void downloadSaveImage(String name_image){
		File folder_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		folder_images.mkdirs();
		
		File file_image = new File(folder_images, name_image);
		if(!file_image.exists()){
			try {
				URL url_image = new URL(new StringBuilder(MyVar.RUTA_IMAGES_SERVER).append(name_image).toString());
				HttpURLConnection conn = (HttpURLConnection)url_image.openConnection();
				Bitmap myBitmap = BitmapFactory.decodeStream(conn.getInputStream());
				if(myBitmap!=null){
					FileOutputStream fos = new FileOutputStream(file_image);
					myBitmap.compress(CompressFormat.JPEG, 90, fos);
					fos.flush();
					scanImage(file_image.getAbsolutePath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void scanImage(final String pathImage){
		new MediaScannerConnectionClient() {
			private MediaScannerConnection msc = null; {
				msc = new MediaScannerConnection(getActivity().getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(pathImage, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}

}
