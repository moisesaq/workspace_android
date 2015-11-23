package com.moisse.dialogs;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.Parking;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogEntradaVehiculo extends DialogFragment implements android.view.View.OnClickListener{

	private Parking parking;
	private Carril carril;
	private LinearLayout lyPropVehiculo;
	private View v;
	private Button btnOK, btnCancelar;
	private AutoCompleteTextView actvPlaca;
	private EditText etNota;
	private TextView tvHora, tvFecha, tvNombreProp, tvTipoProp;
	private ImageView ivDefinirHoraIngreso, ivImageVehiculo;
	
	private HttpClientCustom httpCustom = new HttpClientCustom();
	private Activity activity;
	
	ProgressDialog pd;
	ProgressDialog pd2;
	
	public int DIGITOS_PARA_VALIDACION = 0;
	
	public DialogEntradaVehiculo(Parking parking, Carril carril, Activity activity){
		this.parking = parking;
		this.carril = carril;
		this.activity = activity;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog.setTitle("Carril No."+carril.getNum_carril());
		v = inflater.inflate(R.layout.modelo_entrada_vehiculo, null);
		ivImageVehiculo = (ImageView)v.findViewById(R.id.ivImageVehiculoIngresoVehiculo);
		actvPlaca = (AutoCompleteTextView)v.findViewById(R.id.actvPlacaIngresoVehiculo);
		actvPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
		actvPlaca.addTextChangedListener(placaWacher);
		adapterAutoCompleteTextViewPlaca();
		lyPropVehiculo = (LinearLayout)v.findViewById(R.id.lyPropVehiculoIngresoVehiculo);
		tvNombreProp = (TextView)v.findViewById(R.id.tvNombrePropIngresoVehiculo);
		tvTipoProp = (TextView)v.findViewById(R.id.tvTipoPropIngresoVehiculo);
		etNota = (EditText)v.findViewById(R.id.etNotaIngresoVehiculo);
		ivDefinirHoraIngreso = (ImageView)v.findViewById(R.id.ivDefinirHoraIngresoVehiculo);
		ivDefinirHoraIngreso.setOnClickListener(this);
		tvHora = (TextView)v.findViewById(R.id.tvHoraIngresoVehiculo);
		tvFecha = (TextView)v.findViewById(R.id.tvFechaIngresoVehiculo);
		btnOK = (Button)v.findViewById(R.id.btnOKIngresoVehiculo);
		btnOK.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarIngresoVehiculo);
		btnCancelar.setOnClickListener(this);
		
		putHoraFecha();
		dialog.setView(v);
		return dialog.create();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnOK.getId()){
			registrarResguardo();
		}else if (v.getId()==btnCancelar.getId()) {
			dismiss();
		}else if (v.getId()==ivDefinirHoraIngreso.getId()) {
			Time hora_actual = (Time)tvHora.getTag();
			DialogDefinirHora dDefinir = new DialogDefinirHora(tvHora, hora_actual.getHours(), hora_actual.getMinutes());
			dDefinir.show(getFragmentManager(), "tagDHoraIngreso");
		}
	}
	
	public void adapterAutoCompleteTextViewPlaca(){
		List<Vehiculo> lista = null;
		String[] lista_placas = null;
		DBParqueo db = new DBParqueo(activity);
		try {
			db.openSQLite();
			lista = db.getAllVehiculo(carril.getIdpaqueo(), MyVar.NO_ELIMINADO);
			if(lista.size()!=0 && lista!=null){
				lista_placas = new String[lista.size()];
				for (int i = 0; i < lista_placas.length; i++) {
					lista_placas[i]=lista.get(i).getPlaca();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, lista_placas);
				actvPlaca.setThreshold(2);
				actvPlaca.setAdapter(adapter);
			}
			
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				if(MyVar.isNumeric(s.toString())){
					DIGITOS_PARA_VALIDACION = 7;
					actvPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
				}else{
					DIGITOS_PARA_VALIDACION = 6;
					actvPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
				}
			}else if (s.length()==5) {
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
			}else if (s.length()==6) {
				if(DIGITOS_PARA_VALIDACION==0){
					DIGITOS_PARA_VALIDACION = 6;
					actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				}
			}else if (s.length()==7) {
				if(DIGITOS_PARA_VALIDACION==0){
					DIGITOS_PARA_VALIDACION = 7;
					actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				}
			}
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()==6 || s.length()==7){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					Vehiculo vehi = db.buscarVehiculoPorPlaca(s.toString().toUpperCase(), carril.getIdpaqueo());
					if(vehi!=null){
						String id_default_client = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(carril.getIdpaqueo()).toString();
						ivImageVehiculo.setVisibility(View.VISIBLE);
						if(!vehi.getImagen().equals(MyVar.NO_ESPECIFICADO)){
							Bitmap bitmap = BitmapFactory.decodeFile(MyVar.FOLDER_IMAGES_PARQUEO+vehi.getImagen());
							if(bitmap!=null){
								ivImageVehiculo.setImageBitmap(bitmap);
							}else{
								ivImageVehiculo.setImageResource(R.drawable.ic_car);
							}
						}else{
							ivImageVehiculo.setImageResource(R.drawable.ic_car);
						}
						lyPropVehiculo.setVisibility(View.VISIBLE);
						if(!vehi.getIdcliente().equals(id_default_client)){
							Cliente cliente = getCliente(vehi.getIdcliente());
							if(cliente.getApellido().equals(MyVar.NO_ESPECIFICADO)){
								tvNombreProp.setText(cliente.getNombre());
							}else{
								tvNombreProp.setText(new StringBuilder(cliente.getNombre()).append(" ").append(cliente.getApellido()));
							}
							putTipoCliente(cliente);
						}else{
							tvNombreProp.setText(MyVar.SIN_PROPIETARIO);
							tvTipoProp.setText("Vehiculo ocasional");
						}
					}
					db.closeSQLite();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				ivImageVehiculo.setVisibility(View.GONE);
				lyPropVehiculo.setVisibility(View.GONE);
			}
		}
	};
	
	public Cliente getCliente(String idcliente){
		DBParqueo db = new DBParqueo(getActivity());
		Cliente cliente = null;
		try {
			db.openSQLite();
			cliente = db.getCliente(idcliente);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public void putTipoCliente(Cliente cliente){
		int tipo_cliente = cliente.getTipo();
		if(tipo_cliente==MyVar.CLIENTE_OCASIONAL){
			tvTipoProp.setText(new StringBuilder("Cliente ocasional"));
		}else if (tipo_cliente == MyVar.CLIENTE_CONTRATO_NOCTURNO) {
			tvTipoProp.setText(new StringBuilder("Cliente con contrato nocturno"));
		}else if (tipo_cliente == MyVar.CLIENTE_CONTRATO_DIURNO) {
			tvTipoProp.setText(new StringBuilder("Cliente con contrato diurno"));
		}else if (tipo_cliente == MyVar.CLIENTE_CONTRATO_DIA_COMPLETO) {
			tvTipoProp.setText(new StringBuilder("Cliente con contrato dia completo"));
		}
	}
		
	public void registrarResguardo(){
		String placa = actvPlaca.getText().toString().trim().toUpperCase();
		String nota = etNota.getText().toString();
		if(nota.equals("")){
			nota = MyVar.NO_ESPECIFICADO;
		}
		Time horaE = (Time)tvHora.getTag();
		Date fechaE = (Date)tvFecha.getTag();
		try {
			if(!placa.equals("")){
				if(placa.length()>=6){
					if(placa.length()==DIGITOS_PARA_VALIDACION){
						if(MyVar.isPlaca(placa)){
							Vehiculo vehiculo = getVehiculoPorPlaca(placa);
							if(vehiculo!=null){
								if(!verifyPlacaEnCarril(vehiculo.getIdvehiculo())){
									String idresguardo = getGenerateIdResguardo();
									Resguardo resguardo = new Resguardo(idresguardo, horaE, fechaE, MyVar.COSTO_DEFAULT_CERO, MyVar.NO_ELIMINADO,
																			nota, MyVar.TIPO_RESGUARDO_DEFAULT, this.carril.getIdcarril(), vehiculo.getIdvehiculo());
									new OcuparCarril().execute(resguardo);
								}else{
									Toast.makeText(getActivity(), "Vehículo ya esta ubicado en el estacionamiento", Toast.LENGTH_LONG).show();
								}
							}else{
								confirmarRegistroRapidoVehiculo(placa);
							}
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
					actvPlaca.setError("Placa del vehículo debe tener 6 o 7 dígitos");
				}
			}else{
				actvPlaca.requestFocus();
				actvPlaca.setError("Introduzca placa");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void confirmarRegistroRapidoVehiculo(final String placa){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Registrar vehiculo");
		dialog.setMessage("Este vehiculo no esta registrado ¿Desea hacer un registro rapido del vehículo?");
		dialog.setNegativeButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new VerificarIDVehiculo().execute(placa);
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}
	
	public Vehiculo getVehiculoPorPlaca(String placa){
		Vehiculo vehi = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			vehi = db.buscarVehiculoPorPlaca(placa, carril.getIdpaqueo());
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehi;
	}
	
	public boolean verifyPlacaEnCarril(String idvehiculo){
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			if(db.verificarPlacaEnCarril(idvehiculo, this.carril)){
				return true;
			}
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void putHoraFecha(){
		java.util.Date date1 = new java.util.Date();
		Time horaIngreso = new Time(date1.getHours(), date1.getMinutes(), date1.getSeconds());		
		Date fechaIngreso = new Date(date1.getTime());
	
		tvHora.setText(MyVar.FORMAT_HORA_1.format(date1));
		tvHora.setTag(horaIngreso);
		tvFecha.setText(MyVar.FORMAT_FECHA_1.format(date1));
		tvFecha.setTag(fechaIngreso);
	}
	
	public String getGenerateIdResguardo(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String idresguardo = "resg_"+date+"_"+carril.getIdpaqueo();
		return idresguardo;
	}
		
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error al " + titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	private class OcuparCarril extends AsyncTask<Resguardo, Void, Boolean>{
		Resguardo resg;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Asignando carril...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Resguardo... params) {
			resg = params[0];
			if(httpCustom.ocuparCarril(resg.getIdvehiculo(), carril)){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					if(db.setOcuparCarril(resg.getIdvehiculo(), carril)){
						return true;	
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				new IngresoVehiculo().execute(resg);
//				pd.dismiss();
//				Toast.makeText(getActivity(), "Carril asignado por "+resg.getIdvehiculo(), Toast.LENGTH_SHORT).show();
			}else{
				pd.dismiss();
				DialogMensaje dMsj = new DialogMensaje("No se pudo ocupar carril, actualice los datos o intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	private class IngresoVehiculo extends AsyncTask<Resguardo, Void, Boolean>{
		Resguardo resguardo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd.setMessage("Registrando ingreso de vehiculo...");
		}
		@Override
		protected Boolean doInBackground(Resguardo... params) {
			resguardo = params[0];
			DBParqueo db = new DBParqueo(getActivity());
			if(httpCustom.insertarResguardo(resguardo)){
				try{
					db.openSQLite();
					if(db.insertarResguardo(resguardo)){
						return true;
					}
					db.closeSQLite();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Ingreso de vehiculo registrado", Toast.LENGTH_SHORT).show();
				dismiss();
				parking.cargarCarriles();
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se pudo registrar el ingreso de vehiculo, intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	private class VerificarIDVehiculo extends AsyncTask<String, Void, Boolean>{
		String placa;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd2 = new ProgressDialog(getActivity());
			pd2.setMessage("Verificando placa vehiculo...");
			pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd2.setCancelable(true);
			pd2.show();
		}
		@Override
		protected Boolean doInBackground(String... params) {
			placa = params[0];
			if(httpCustom.verificarDisponibilidadPlacaVehiculo(carril.getIdpaqueo(), placa)){
				return true;
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(result){
				new RegistrarVehiculo().execute(placa);
				//Toast.makeText(getActivity(), "Placa disponible", Toast.LENGTH_LONG).show();
			}else{
				pd2.dismiss();
				DialogMensaje mensaje = new DialogMensaje("Placa introducido apararecio ya en uso, por favor actualice sus datos o la lista de vehiculos");
				mensaje.show(getFragmentManager(), "tagMsj");
			}
		}
		
	}
	
	private class RegistrarVehiculo extends AsyncTask<String, Void, Boolean>{
		Vehiculo vehiculo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd2.setMessage("Registrando vehiculo...");
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String placa = params[0];
			String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(carril.getIdpaqueo()).toString();
			vehiculo = new Vehiculo(getIDVehiculoGenerado(), placa, MyVar.NO_ESPECIFICADO, MyVar.NO_ESPECIFICADO, MyVar.NO_ESPECIFICADO, 
					MyVar.NO_ELIMINADO, id_client_default);
			if(httpCustom.insertarVehiculo(this.vehiculo)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.insertarVehiculo(this.vehiculo)){
						return true;
					}
					db.closeSQLite();			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd2.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Vehiculo registrado exitosamente", Toast.LENGTH_LONG).show();
				actvPlaca.setText(vehiculo.getPlaca());
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo resgistrar vehiculo, intente mas tarde..!");
				mensaje.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	public String getIDVehiculoGenerado(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "vehi_"+date+"_"+this.carril.getIdpaqueo();
		return imageCode;
	}
	
}
