package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogDetalleResguardo extends DialogFragment implements OnClickListener{

	private Resguardo resguardo;
	private View v;
	private Button btnVerPropietario, btnOk;
	private Vehiculo vehiculo;
	public String id_client_default;
	
	public DialogDetalleResguardo(Resguardo resguardo, String id_cliente_default){
		this.resguardo = resguardo;
		this.id_client_default = id_cliente_default;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		this.vehiculo = getVehiculo();
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Datos del resguardo"));
		
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_resguardo, null);
		ImageView imageVehiculo = (ImageView)v.findViewById(R.id.ivVehiculoDetalleResguardo);
		if(!vehiculo.getImagen().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(vehiculo.getImagen()).toString());
			if(bitmap!=null){
				imageVehiculo.setImageBitmap(bitmap);
			}else{
				imageVehiculo.setImageResource(R.drawable.ic_car);
			}	
		}else{
			imageVehiculo.setImageResource(R.drawable.ic_car);
		}
		
		TextView num_carril = (TextView)v.findViewById(R.id.tvNumCarrilDetalleResguardo);
		num_carril.setText(new StringBuffer("Carril No. ").append(getNumCarril(resguardo.getIdcarril())));
		TextView placa = (TextView)v.findViewById(R.id.tvPlacaDetalleResguardo);
		placa.setText(new StringBuffer("Placa: ").append(vehiculo.getPlaca()));
		
		
		TextView costoDia = (TextView)v.findViewById(R.id.tvCostoDiaDetalleResguardo);
		costoDia.setText(new StringBuffer().append(resguardo.getCostoDia()).append(" Bs."));
		TextView costoNoche = (TextView)v.findViewById(R.id.tvCostoNocheDetalleResguardo);
		costoNoche.setText(new StringBuffer().append(resguardo.getCostoNoche()).append(" Bs."));
		TextView costoTotal = (TextView)v.findViewById(R.id.tvCostoTotalDetalleResguardo);
		costoTotal.setText(new StringBuffer().append(resguardo.getCostoTotal()).append(" Bs."));
		
		TextView horaE = (TextView)v.findViewById(R.id.tvHoraEntradaDetalleResguardo);
		horaE.setText(new StringBuffer().append(resguardo.getHoraE()));
		TextView horaS = (TextView)v.findViewById(R.id.tvHoraSalidaDetalleResguardo);
		horaS.setText(new StringBuffer().append(resguardo.getHoraS()));
		
		TextView fechaE = (TextView)v.findViewById(R.id.tvFechaEntradaDetalleResguardo);
		fechaE.setText(new StringBuffer(MyVar.FORMAT_FECHA_1.format(resguardo.getFechaE())));
		TextView fechaS = (TextView)v.findViewById(R.id.tvFechaSalidaDetalleResguardo);
		fechaS.setText(new StringBuffer(MyVar.FORMAT_FECHA_1.format(resguardo.getFechaS())));
		btnVerPropietario = (Button)v.findViewById(R.id.btnVerPropDetalleResguardo);
		btnVerPropietario.setOnClickListener(this);
		if(!vehiculo.getIdcliente().equals(id_client_default)){
			btnVerPropietario.setVisibility(View.VISIBLE);			
		}
		btnOk = (Button)v.findViewById(R.id.btnOkDetalleResguardo);
		btnOk.setOnClickListener(this);
		
		dialog.setView(v);
		return dialog.create();
	}
	
	private Vehiculo getVehiculo(){
		DBParqueo db = new DBParqueo(getActivity());
		Vehiculo vehi = null;
		try {
			db.openSQLite();
			vehi = db.getVehiculo(this.resguardo.getIdvehiculo());
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehi;
	}
	
	private int getNumCarril(String idcarril){
		DBParqueo db = new DBParqueo(getActivity());
		int carril = 0;
		try {
			db.openSQLite();
			carril = db.getCarril(idcarril).getNum_carril();
			db.closeSQLite();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return carril;
	}
	
	private Cliente getPropietarioCliente(){
		Cliente cliente = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			cliente = db.getCliente(vehiculo.getIdcliente());
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnVerPropietario.getId()){
			Cliente cliente = getPropietarioCliente();
			if(cliente!=null){
				this.dismiss();
				DialogDetalleCliente dDCliente = new DialogDetalleCliente(cliente, getActivity());
				dDCliente.show(getFragmentManager(), "tagDDC");
			}
		}else if(v.getId()==btnOk.getId()){
			this.dismiss();
		}
	}
}
