package com.moisse.dialogs;

import java.sql.Date;
import java.sql.Time;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.Parking;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.GenerarDatosSalida;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogSalidaVehiculo extends DialogFragment implements android.view.View.OnClickListener{
	
	private View v;
	private LinearLayout lyCostos; //lyPermanencia;
	private ImageView ivImageV, ivEditarNota;
	private TextView tvPlaca, tvNombreProp, tvCIProp, tvTipoProp, tvHoraE, tvFechaE, tvHoraS, tvFechaS, tvTiempoDia, tvCantNoche, tvTiempo, 
					 tvCostoDia, tvCostoNoche, tvCostoTotal, tvNota;
	private Button btnOK, btnCancelar;
	
	private Parking parking;
	private Carril carril;
	private Parqueo parq_online;
	private Resguardo ingreso_resguardo, salida_resguardo;
	private Vehiculo vehiculo;
	private GenerarDatosSalida datos;
	
	public final String SI = "SI"; 
	public String id_client_default;
	private HttpClientCustom httpCustom = new HttpClientCustom();
	public Activity activity;
	java.util.Date fecha_salida_actual = new java.util.Date();
	private Time horaS = new Time(fecha_salida_actual.getHours(), fecha_salida_actual.getMinutes(), fecha_salida_actual.getSeconds());		
	private Date fechaS = new Date(fecha_salida_actual.getTime());	
	
	ProgressDialog pd;
		
	public DialogSalidaVehiculo(Parking parking, Carril carril, Parqueo parq_online, Activity activity){
		this.parking = parking;
		this.carril = carril;
		this.parq_online = parq_online;
		this.activity = activity;
		id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(parq_online.getIdparqueo()).toString();
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		v = inflater.inflate(R.layout.modelo_salida_vehiculo, null);
		lyCostos = (LinearLayout)v.findViewById(R.id.lyCostosSalidaVehiculo);
		//lyPermanencia = (LinearLayout)v.findViewById(R.id.lyPermanenciaSalidaVehiculo);
		ivImageV = (ImageView)v.findViewById(R.id.ivSalidaVehiculo);
		tvPlaca = (TextView)v.findViewById(R.id.tvPlacaSalidaVehiculo);
		tvNombreProp = (TextView)v.findViewById(R.id.tvNombrePropSalidaVehiculo);
		tvCIProp = (TextView)v.findViewById(R.id.tvCIPropSalidaVehiculo);
		tvTipoProp = (TextView)v.findViewById(R.id.tvTipoPropSalidaVehiculo);
		tvHoraE = (TextView)v.findViewById(R.id.tvHoraIngresoSalidaVehiculo);
		tvFechaE = (TextView)v.findViewById(R.id.tvFechaIngresoSalidaVehiculo);
		tvHoraS = (TextView)v.findViewById(R.id.tvHoraSalidaVehiculo);
		tvFechaS = (TextView)v.findViewById(R.id.tvFechaSalidaVehiculo);
		tvTiempoDia = (TextView)v.findViewById(R.id.tvTiempoPermanenciaDia);
		tvCantNoche = (TextView)v.findViewById(R.id.tvCantPermanenciaNoche);
		tvTiempo = (TextView)v.findViewById(R.id.tvTiempoParking);
		tvCostoDia = (TextView)v.findViewById(R.id.tvCostoPermanenciaDia);
		tvCostoNoche = (TextView)v.findViewById(R.id.tvCostoPermanenciaNoche);
		tvCostoTotal = (TextView)v.findViewById(R.id.tvCostoParking);
		ivEditarNota = (ImageView)v.findViewById(R.id.ivEditarNotaSalidaVehiculo);
		ivEditarNota.setOnClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaSalidaVehiculo);
		btnOK = (Button)v.findViewById(R.id.btnOKSalidaVehiculo);
		btnOK.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarSalidaVehiculo);
		btnCancelar.setOnClickListener(this);
		
		dialog.setTitle("Carril No."+this.carril.getNum_carril()+" ¿Desocupar?");
		this.vehiculo = getVehiculo(this.carril.getDisponible());
		if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehiculo.getImagen()).toString());
			if(bitmap!=null){
				ivImageV.setImageBitmap(bitmap);
			}else{
				ivImageV.setImageResource(R.drawable.ic_car);
			}
		}else{
			ivImageV.setImageResource(R.drawable.ic_car);
		}
		
		tvPlaca.setText("Placa: "+this.vehiculo.getPlaca());
		this.ingreso_resguardo = getResguardo();
		if(ingreso_resguardo!=null){
			tvNota.setText(ingreso_resguardo.getNota());
			this.datos = new GenerarDatosSalida(this.parq_online, ingreso_resguardo.getHoraE(), ingreso_resguardo.getFechaE());
			
			if(datos.getDiferenciaDias()==0){
				tvFechaE.setText(new StringBuilder("Hoy,").append(MyVar.FORMAT_FECHA_2.format(datos.getFechaEntrada())));
			}else if (datos.getDiferenciaDias()==1) {
				tvFechaE.setText(new StringBuilder("Ayer,").append(MyVar.FORMAT_FECHA_2.format(datos.getFechaEntrada())));
			}else if (datos.getDiferenciaDias()>1) {
				tvFechaE.setText(new StringBuilder(MyVar.FORMAT_FECHA_2.format(datos.getFechaEntrada())));
			}
			
			tvHoraE.setText(new StringBuilder("Hrs. ").append(datos.getHoraEntrada()));
			tvFechaS.setText(new StringBuffer("Hoy,").append(MyVar.FORMAT_FECHA_2.format(fecha_salida_actual)));
			tvHoraS.setText(new StringBuilder(MyVar.FORMAT_HORA_1.format(fecha_salida_actual)));
			
			if(this.vehiculo.getIdcliente().equals(id_client_default)){
				tvNombreProp.setText(new StringBuilder("Prop.: ").append(MyVar.SIN_PROPIETARIO));
				tvCIProp.setVisibility(View.GONE);
				tvTipoProp.setText(new StringBuilder("Vehiculo Ocacional"));
				cargarDatosVehiculoClienteOcasional();
			}else{
				Cliente cliente = getCliente(this.vehiculo.getIdcliente());
				if(cliente!=null){
					if(cliente.getApellido().equals(MyVar.NO_ESPECIFICADO)){
						tvNombreProp.setText(new StringBuilder("Prop.: ").append(cliente.getNombre()));
					}else{
						tvNombreProp.setText(new StringBuilder("Prop.: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido()));
					}
					tvCIProp.setText(new StringBuilder("CI: ").append(cliente.getCi()));
					int tipo_cliente = cliente.getTipo();
					if(tipo_cliente==MyVar.CLIENTE_OCASIONAL){
						tvTipoProp.setText(new StringBuilder("Cliente ocasional"));
						cargarDatosVehiculoClienteOcasional();
					}else if (tipo_cliente == MyVar.CLIENTE_CONTRATO_NOCTURNO) {
						tvTipoProp.setText(new StringBuilder("Cliente con contrato nocturno"));
						cargarDatosClienteContratoNocturno();
					}else if (tipo_cliente == MyVar.CLIENTE_CONTRATO_DIURNO) {
						tvTipoProp.setText(new StringBuilder("Cliente con contrato diurno"));
						cargarDatosClienteContratoDiurno();
					}else if (tipo_cliente == MyVar.CLIENTE_CONTRATO_DIA_COMPLETO) {
						tvTipoProp.setText(new StringBuilder("Cliente con contrato dia completo"));
						cargarDatosClienteContratoDiaCompleto();
					}
				}else{
					Toast.makeText(getActivity(), "Error al cargar salida, actualice sus datos", Toast.LENGTH_SHORT).show();
					btnOK.setEnabled(false);
				}
			}			
		}else{
			Toast.makeText(getActivity(), "Error al cargar salida, actualice sus datos", Toast.LENGTH_SHORT).show();
			btnOK.setEnabled(false);
		}
		dialog.setView(v);
		return dialog.create();
	}

	public void cargarDatosVehiculoClienteOcasional(){
		tvTiempoDia.setText(new StringBuilder("Dia: ").append(datos.getTiempoDia()).append(" Hrs."));
		tvTiempo.setText(datos.getVistaTiempo());
		tvCostoDia.setText(new StringBuilder("Dia: ").append(datos.getCostoDia()).append(" Bs."));
		if(datos.getCantNoche()!=0){
			tvCantNoche.setVisibility(View.VISIBLE);
			tvCantNoche.setText(new StringBuilder("Noches: ").append(datos.getCantNoche()));
			tvCostoNoche.setVisibility(View.VISIBLE);
			tvCostoNoche.setText(new StringBuilder("Noche: ").append(datos.getCostoNoche()).append(" Bs."));
			tvCostoTotal.setVisibility(View.VISIBLE);
			tvCostoTotal.setText(new StringBuilder("Total: ").append(datos.getCostoTotal()).append(" Bs."));
		}
		salida_resguardo = new Resguardo(ingreso_resguardo.getIdresguardo(), horaS, fechaS, datos.getCostoDia(), datos.getCostoNoche(),
											datos.getCostoTotal(), tvNota.getText().toString(), MyVar.TIPO_RESGUARDO_OCASIONAL, this.carril.getIdcarril());
	}
	
	public void cargarDatosClienteContratoNocturno(){
		if(datos.getCantNoche()!=0){
			tvCantNoche.setVisibility(View.VISIBLE);
			tvCantNoche.setText(new StringBuilder("Noche: ").append(datos.getCantNoche()));
		}
		int tiempo_adicional_dia = datos.getTiempoDia();
		if(tiempo_adicional_dia!=0){
			double costo_adicional_dia = datos.getCostoDia();
			tvTiempoDia.setText(new StringBuilder("Dia: ").append(tiempo_adicional_dia).append(" Hrs."));
			tvTiempo.setText(datos.getVistaTiempo());
			tvCostoDia.setText(new StringBuilder("Dia: ").append(costo_adicional_dia).append(" Bs."));
			salida_resguardo = new Resguardo(ingreso_resguardo.getIdresguardo(), horaS, fechaS, costo_adicional_dia, MyVar.COSTO_DEFAULT_CERO,
												costo_adicional_dia, tvNota.getText().toString(), MyVar.TIPO_RESGUARDO_CONTRATO, this.carril.getIdcarril());
		}else{
			lyCostos.setVisibility(View.GONE);
			salida_resguardo = new Resguardo(ingreso_resguardo.getIdresguardo(), horaS, fechaS, MyVar.COSTO_DEFAULT_CERO, MyVar.COSTO_DEFAULT_CERO,
										MyVar.COSTO_DEFAULT_CERO, tvNota.getText().toString(), MyVar.TIPO_RESGUARDO_CONTRATO, this.carril.getIdcarril());
		}
	}
	
	public void cargarDatosClienteContratoDiurno(){
		tvTiempoDia.setText(new StringBuilder("Dia: ").append(datos.getTiempoDia()).append(" Hrs."));
		tvTiempo.setText(datos.getVistaTiempo());
		int cant_noches = datos.getCantNoche();
		if(cant_noches!=0){
			double costo_adicional_noche = datos.getCostoNoche();
			tvCantNoche.setVisibility(View.VISIBLE);
			tvCantNoche.setText(new StringBuilder("Noche: ").append(cant_noches));
			tvCostoDia.setVisibility(View.GONE);
			tvCostoNoche.setVisibility(View.VISIBLE);
			tvCostoNoche.setText(new StringBuilder("Noche: ").append(costo_adicional_noche).append(" Bs."));
			salida_resguardo = new Resguardo(ingreso_resguardo.getIdresguardo(), horaS, fechaS, MyVar.COSTO_DEFAULT_CERO, costo_adicional_noche,
												costo_adicional_noche, tvNota.getText().toString(), MyVar.TIPO_RESGUARDO_CONTRATO, this.carril.getIdcarril());
		}else{
			lyCostos.setVisibility(View.GONE);
			salida_resguardo = new Resguardo(ingreso_resguardo.getIdresguardo(), horaS, fechaS, MyVar.COSTO_DEFAULT_CERO, MyVar.COSTO_DEFAULT_CERO,
										MyVar.COSTO_DEFAULT_CERO, tvNota.getText().toString(), MyVar.TIPO_RESGUARDO_CONTRATO, this.carril.getIdcarril());
		}
	}
	
	public void cargarDatosClienteContratoDiaCompleto(){
		tvTiempoDia.setVisibility(View.GONE);
		tvTiempo.setText(datos.getVistaTiempo());
		lyCostos.setVisibility(View.GONE);
		salida_resguardo = new Resguardo(ingreso_resguardo.getIdresguardo(), horaS, fechaS, MyVar.COSTO_DEFAULT_CERO, MyVar.COSTO_DEFAULT_CERO,
									MyVar.COSTO_DEFAULT_CERO, tvNota.getText().toString(), MyVar.TIPO_RESGUARDO_CONTRATO, this.carril.getIdcarril());
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnOK.getId()){
			confirmarSalida(this);
		}else if(v.getId()==btnCancelar.getId()){
			dismiss();
		}else if (v.getId()==ivEditarNota.getId()) {
			editarNota();
		}
	}
	
	public void confirmarSalida(final DialogSalidaVehiculo dialogSalidaVehiculo){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setMessage(new StringBuilder("¿Desea desocupar carril?"));
		dialog.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DesocuparCarril().execute();
			}
		});
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}
	
	public void editarNota(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Editar nota"));
		View v = getActivity().getLayoutInflater().inflate(R.layout.modelo_editar_nota, null);
		final EditText etEditarNota = (EditText)v.findViewById(R.id.etEditarNota);
		if(!ingreso_resguardo.getNota().equals(MyVar.NO_ESPECIFICADO)){
			etEditarNota.setText(ingreso_resguardo.getNota());
		}
		dialog.setView(v);
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!etEditarNota.getText().toString().equals("")){
					Resguardo resg = new Resguardo(ingreso_resguardo.getIdresguardo(), etEditarNota.getText().toString());
					new EditarNota().execute(resg);
				}else{
					Toast.makeText(getActivity(), "Campo nota esta vacio", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.create().show();
	}
	
	public Resguardo getResguardo(){
		DBParqueo db1 = new DBParqueo(getActivity());
		Resguardo res = null;
		try {
			db1.openSQLite();
			res = db1.getEntradaResguardo(carril.getIdcarril(), carril.getDisponible());
			db1.closeSQLite();
		} catch (Exception e) {
			mensajeError(e.toString(), "obtener resguardo");
		}
		return res;
	}
	
	public Vehiculo getVehiculo(String idvehiculo){
		DBParqueo db = new DBParqueo(getActivity());
		Vehiculo vehi = null;
		try {
			db.openSQLite();
			vehi = db.getVehiculo(idvehiculo);
			db.closeSQLite();
		} catch (Exception e) {
			mensajeError(e.toString(), "obtener vehiculo");
		}
		return vehi;
	}
	
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
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(getActivity());
		mensaje.setTitle("Error al " + titulo);
		TextView texto=new TextView(getActivity());
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	private class EditarNota extends AsyncTask<Resguardo, Void, Boolean>{
		Resguardo resguardo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Editando nota...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Resguardo... params) {
			resguardo = params[0];
			if(httpCustom.editarNotaResguardo(resguardo)){
				DBParqueo db = new DBParqueo(getActivity());
				try{
					db.openSQLite();
					if(db.editarNotaResguardo(resguardo)){
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
				Toast.makeText(getActivity(), "Nota editado", Toast.LENGTH_SHORT).show();
				tvNota.setText(resguardo.getNota());
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se pudo editar nota, intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	private class DesocuparCarril extends AsyncTask<Void, Void, Boolean>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Desocupando carril...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			if(httpCustom.desocuparCarril(carril)){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					if(db.setDesocuparCarril(carril)){
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
			if(result){//x alguna razon se genera error o no funciona getActivity() x eso se agreso un activity al constructor
				new SalidaVehiculo().execute();
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se pudo desocupar el carril, actualice los datos o intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
		
	private class SalidaVehiculo extends AsyncTask<Void, Void, Boolean>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Registrando salida de vehiculo...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			if(httpCustom.actualizarResguardo(salida_resguardo)){
				DBParqueo db = new DBParqueo(activity);
				try{
					db.openSQLite();
					if(db.actualizarResguardo(salida_resguardo)){
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
				DialogSalidaVehiculo.this.dismiss();
				Toast.makeText(getActivity(), "Salida de vehiculo registrado, carril "+carril.getNum_carril()+" disponible", Toast.LENGTH_LONG).show();
				parking.cargarCarriles();
				parking.actvPlaca.getText().clear();
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se pudo registrar salida de vehiculo, intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	

}
