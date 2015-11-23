package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.example.parqueo.R;
import com.moisse.adapters.ListResguardoAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogBuscarResguardoFecha;
import com.moisse.dialogs.DialogBuscarResguardoPorPlaca;
import com.moisse.dialogs.DialogDetalleResguardo;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Usuario;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.OnNavigationListener;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListaResguardo extends Fragment implements OnItemClickListener, OnNavigationListener{

	private ListView lvListaResguardo;
	private TextView tvNota;
	private SlidingDrawer sdGanancias;
	public TextView tvCuando ,tvIngresosDia, tvIngresosNoche, tvCantidad,tvIngresoTotal;
	private String idparqueo, id_client_default;
	java.util.Date date = new java.util.Date();
	Date fecha_actual = new Date(date.getTime());
	private int lista_por_fecha = 1;
	private int lista_por_mes = 2;
	private Usuario usuario_online;
	private HttpClientCustom httpCustom;
	public int OPCION_ACT;
	
	@Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lista_resguardo, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(view);
		return view;
	}
	
	private void inicializarComponentes(View view) {
		ActionBar actionB = getActivity().getActionBar();
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionB.setTitle("Registro");
		httpCustom = new HttpClientCustom();
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), 
													R.array.resguardos_por_fechas, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionB.setListNavigationCallbacks(adapter, this);
		
		this.idparqueo = getArguments().getString("idparqueo");
		id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
		String idusuario = getArguments().getString("idusuario");
		this.usuario_online = getUserOnline(idusuario);
		
		lvListaResguardo = (ListView)view.findViewById(R.id.lvListaResguardo);
		lvListaResguardo.setOnItemClickListener(this);
		tvNota = (TextView)view.findViewById(R.id.tvNotaListaResguardo);
		sdGanancias = (SlidingDrawer)view.findViewById(R.id.sdGananciasListaResguardo);
		tvCuando = (TextView)view.findViewById(R.id.tvCuandoListaResguardos);
		tvIngresosDia = (TextView)view.findViewById(R.id.tvIngresosDia);
		tvIngresosNoche = (TextView)view.findViewById(R.id.tvIngresosNoche);
		tvCantidad = (TextView)view.findViewById(R.id.tvCantidadResguardos);
		tvIngresoTotal = (TextView)view.findViewById(R.id.tvIngresoTotal);
		
		cargarListaResguardos(getUltimosResguardos());
		OPCION_ACT = MyVar.ACT_ULTIMOS_RESG;
	}
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition) {
		case 0:
			OPCION_ACT = MyVar.ACT_ULTIMOS_RESG;
			cargarListaResguardos(getUltimosResguardos());
			break;
		case 1:
			OPCION_ACT = MyVar.ACT_RESG_HACE_3DIAS;
			Date fecha_reducida1 = new Date(getFechaReducido(date, -2).getTime());
			cargarListaResguardos(getListaResguardosPorFecha(fecha_reducida1, fecha_actual));
			break;
		case 2:
			OPCION_ACT = MyVar.ACT_RESG_HACE_1SEMANA;
			Date fecha_reducida2 = new Date(getFechaReducido(date, -6).getTime());
			cargarListaResguardos(getListaResguardosPorFecha(fecha_reducida2, fecha_actual));
			break;
			
		case 3:
			OPCION_ACT = MyVar.ACT_RESG_MES_ACTUAL;
			cargarListaResguardos(getListaResguardosMes(fecha_actual));
			break;
			
		}
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_search_date).setVisible(true);
		menu.findItem(R.id.action_search_month).setVisible(true);
		menu.findItem(R.id.action_search_placa).setVisible(true);
		menu.findItem(R.id.action_search).setVisible(false);
		menu.findItem(R.id.action_refresh).setVisible(true);
		menu.findItem(R.id.action_download).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_search_date:
			DialogBuscarResguardoFecha dBuscarResguardo = new DialogBuscarResguardoFecha(this, lista_por_fecha, tvNota);
			dBuscarResguardo.show(getFragmentManager(), "tagdBuscarResguardo");
			return true;

		case R.id.action_search_month:
			DialogBuscarResguardoFecha dBuscarResguardoMes = new DialogBuscarResguardoFecha(this, lista_por_mes, tvNota);
			dBuscarResguardoMes.show(getFragmentManager(), "tagdBuscarResguardoMes");
			return true;
		
		case R.id.action_search_placa:
			DialogBuscarResguardoPorPlaca dBuscarResguardoPlaca = new DialogBuscarResguardoPorPlaca(this, this.idparqueo);
			dBuscarResguardoPlaca.show(getFragmentManager(), "tagDBRP");
			return true;
		
		case R.id.action_refresh:
			cargarListaResguardos(getUltimosResguardos());
			return true;
		case R.id.action_download:
			//POR EL MOMENTO NO SE VA VER ESTO....
			new ActualizarResguardos().execute();
			return true;
		}
		
			
		return super.onOptionsItemSelected(item);
	}

	public void cargarListaResguardos(List<Resguardo> listR) {
		if(listR.size()==0 || listR==null){
			tvNota.setText("No se encontraro ningún registro, lista vacia...!");
			tvNota.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
			tvNota.setVisibility(View.VISIBLE);
			lvListaResguardo.setVisibility(View.INVISIBLE);
			sdGanancias.setVisibility(View.INVISIBLE);
		}else{
			if(usuario_online.getCargo()==MyVar.CARGO_ADMIN){
				sdGanancias.setVisibility(View.VISIBLE);
				if(OPCION_ACT==MyVar.ACT_ULTIMOS_RESG){
					tvCuando.setText(new StringBuilder("Ingresos de los ultimos 25 registros"));
				}else if(OPCION_ACT==MyVar.ACT_RESG_HACE_3DIAS){
					tvCuando.setText(new StringBuilder("Ingresos de hace 3 dias"));
				}else if(OPCION_ACT==MyVar.ACT_RESG_HACE_1SEMANA){
					tvCuando.setText(new StringBuilder("Ingresos de hace 1 semana"));
				}else if(OPCION_ACT==MyVar.ACT_RESG_MES_ACTUAL){
					tvCuando.setText(new StringBuilder("Ingresos del mes actual"));
				}
				tvIngresosDia.setText(new StringBuilder("Ingresos de dia: ").append(getIngresosDia(listR)).append(" Bs."));
				tvIngresosNoche.setText(new StringBuilder("Ingresos de noche: ").append(getIngresosNoche(listR)).append(" Bs."));
				tvCantidad.setText(new StringBuilder("Cantidad registrados: ").append(listR.size()));
				tvIngresoTotal.setText(new StringBuilder("Ingreso Total: ").append(getIngresoTotal(listR)).append(" Bs."));
			}
			tvNota.setVisibility(View.INVISIBLE);
			lvListaResguardo.setVisibility(View.VISIBLE);
			ListResguardoAdapter adapter = new ListResguardoAdapter(getActivity(), listR);
			lvListaResguardo.setAdapter(adapter);
		}	
	}
	
	public double getIngresoTotal(List<Resguardo> lista){
		double total = 0;
			for (int i = 0; i < lista.size(); i++) {
				total = total+lista.get(i).getCostoTotal();
			}
		return total;
	}
	
	public double getIngresosDia(List<Resguardo> lista){
		double ingresos_dia = 0;
		for (int i = 0; i < lista.size(); i++) {
			ingresos_dia = ingresos_dia + lista.get(i).getCostoDia();
		}
		return ingresos_dia;
	}
	
	public double getIngresosNoche(List<Resguardo> lista){
		double ingresos_noche = 0;
		for (int i = 0; i < lista.size(); i++) {
			ingresos_noche = ingresos_noche + lista.get(i).getCostoNoche();
		}
		return ingresos_noche;
	}
	
	public List<Resguardo> getUltimosResguardos(){
		DBParqueo db = new DBParqueo(getActivity());
		List<Resguardo> listR = null;
		try {
			db.openSQLite();
			listR = db.getUltimoResguardos(this.idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listR;
	}
	
	public List<Resguardo> getListaResguardosPorFecha(Date fecha_antes, Date fecha_actual){
		DBParqueo db = new DBParqueo(getActivity());
		List<Resguardo> listR = null;
		try {
			db.openSQLite();
			listR = db.getResguardoPorFecha(this.idparqueo, fecha_antes, fecha_actual);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listR;
	}
	
	public List<Resguardo> getListaResguardosMes(Date fecha){
		DBParqueo db = new DBParqueo(getActivity());
		List<Resguardo> listR = null;
		try {
			db.openSQLite();
			listR = db.getResguardoPorMes(this.idparqueo, fecha);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listR;
	}

	@Override
	public void onItemClick(AdapterView<?> a, View view, int position, long parent) {
		Resguardo resguardo = (Resguardo)a.getAdapter().getItem(position);
		DialogDetalleResguardo ddr = new DialogDetalleResguardo(resguardo, id_client_default);
		ddr.show(getFragmentManager(), "tagDDResguardo");
	}
	
	public Usuario getUserOnline(String idusuario){
		Usuario usuario = null;
		DBParqueo db  = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			usuario = db.getUsuario(idusuario);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
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

	//ASYNCTASK PARA UTILIZAR LUEGO.... ;)
	public class ActualizarResguardos extends AsyncTask<Void, Void, Boolean>{
		ProgressDialog pd;
		boolean control = false;
		List<Resguardo> lista_resguardos = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando lista...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
			
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			if(OPCION_ACT==MyVar.ACT_ULTIMOS_RESG){
				Date fecha_1dias_antes = new Date(getFechaReducido(date, -1).getTime());
				lista_resguardos = httpCustom.cargarResguardosPorFecha(idparqueo, fecha_actual, fecha_1dias_antes);
			}else if (OPCION_ACT==MyVar.ACT_RESG_HACE_3DIAS) {
				Date fecha_3dias_antes = new Date(getFechaReducido(date, -2).getTime());
				lista_resguardos = httpCustom.cargarResguardosPorFecha(idparqueo, fecha_actual, fecha_3dias_antes);
			}else if (OPCION_ACT==MyVar.ACT_RESG_HACE_1SEMANA) {
				Date fecha_1semana_antes = new Date(getFechaReducido(date, -6).getTime());
				lista_resguardos = httpCustom.cargarResguardosPorFecha(idparqueo, fecha_actual, fecha_1semana_antes);
			}else if (OPCION_ACT==MyVar.ACT_RESG_MES_ACTUAL || OPCION_ACT==MyVar.ACT_RESG_POR_PLACA) {
				//Aca le puse asi paq busque en el mes los ingresos de ese vehiculo al estacionamiento
				lista_resguardos = httpCustom.cargarResguardosPorMes(idparqueo, fecha_actual);
			}else if(OPCION_ACT==MyVar.ACT_RESG_FECHA_ESPECIFICO){
				if(tvNota.getTag()!=null){
					Date fecha = (Date)tvNota.getTag();
					lista_resguardos = httpCustom.cargarResguardosPorFecha(idparqueo, fecha, fecha);
				}
			}else if (OPCION_ACT==MyVar.ACT_RESG_MES_ESPECIFICO) {
				if(tvNota.getTag()!=null){
					Date fecha = (Date)tvNota.getTag();
					lista_resguardos = httpCustom.cargarResguardosPorMes(idparqueo, fecha);
				}
			}
			if(lista_resguardos!=null && lista_resguardos.size()!=0){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					for (int i = 0; i < lista_resguardos.size(); i++) {
						Resguardo resguardo = lista_resguardos.get(i);
						if(db.verificarResguardo(resguardo.getIdresguardo())){
							//TODO aqui acortar un poco la actualizacion....
							if(db.actualizarResguardo(resguardo)){
							control = true;	
							}
						}else{
							if(controlarVehiculoCliente(resguardo)){
								db.insertarResguardoCompleto(resguardo);
								control = true;
							}
						}
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return control;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Lista de resguardos actualizado "+lista_resguardos.size(), Toast.LENGTH_SHORT).show();
				cargarListaResguardos(lista_resguardos);
			}else{
				Toast.makeText(getActivity(), "No se encontraron mas resguardos", Toast.LENGTH_SHORT).show();
			}
		}
		
		public boolean controlarVehiculoCliente(Resguardo resg){
			String idvehiculo = resg.getIdvehiculo();
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
	
}
