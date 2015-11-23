package com.silvia.dialogos;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.InformeVentas;
import com.silvia.modelo.Venta;

@SuppressLint("ValidFragment")
public class DialogVentaPorFecha extends DialogFragment implements OnClickListener{

	InformeVentas informeVentas;
	View v;
	DatePicker datePicker;
	Button btnCancelar, btnAceptar;
	int OPCION;
	
	public DialogVentaPorFecha(InformeVentas informeVentas, int OPCION){
		this.informeVentas = informeVentas;
		this.OPCION = OPCION;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
//		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//		lp.copyFrom(d.getWindow().getAttributes());
//		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		d.getWindow().setAttributes(lp);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		v = getActivity().getLayoutInflater().inflate(R.layout.modelo_venta_por_fecha, null);
		datePicker = (DatePicker)v.findViewById(R.id.dpVentaPorFecha);
		if(OPCION==Variables.OPCION_VENTA_POR_FECHA){
			dialog.setTitle(new StringBuilder("Ventas por fecha"));
		}else if (OPCION==Variables.OPCION_VENTA_POR_MES) {
			dialog.setTitle(new StringBuilder("Ventas por mes"));
			ocultarDia();
		}
		datePicker.init(year, month, day, null);
		datePicker.setCalendarViewShown(false);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarVentaPorFecha);
		btnCancelar.setOnClickListener(this);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarVentaPorFecha);
		btnAceptar.setOnClickListener(this);
		dialog.setView(v);
		return dialog.create();
	}
	
	public void ocultarDia(){
		try {
			Field f[] = datePicker.getClass().getDeclaredFields();
			for (Field field: f) {
				if(field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")){
					field.setAccessible(true);
					Object dayPicker = new Object();
					dayPicker = field.get(datePicker);
					((View)dayPicker).setVisibility(View.GONE);
				}
			}
			if(android.os.Build.VERSION.SDK_INT==21){
				int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
				if(daySpinnerId!=0){
					View daySpinner = datePicker.findViewById(daySpinnerId);
					if(daySpinner!=null){
						daySpinner.setVisibility(View.GONE);
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if(btnCancelar.getId()==v.getId()){
			dismiss();
		}else if (btnAceptar.getId()==v.getId()) {
			int year = datePicker.getYear();
			int month = datePicker.getMonth()+1;
			int day = datePicker.getDayOfMonth();
			java.sql.Date fecha_de_busqueda = Date.valueOf(new StringBuilder().append(year).append("-").append(month).append("-").append(day).toString());
			if(OPCION==Variables.OPCION_VENTA_POR_FECHA){
				informeVentas.tvTitulo.setText(new StringBuilder("Ventas de la fecha: ").append(Variables.FORMAT_FECHA_1.format(fecha_de_busqueda)));
				informeVentas.lista_ventas = getListaVentasPorFecha(fecha_de_busqueda);
				informeVentas.cargarMostrarInformeVentas(informeVentas.lista_ventas);
			}else if (OPCION==Variables.OPCION_VENTA_POR_MES) {
				informeVentas.tvTitulo.setText(new StringBuilder("Ventas del mes: ").append(Variables.FORMAT_FECHA_4.format(fecha_de_busqueda)));
				informeVentas.lista_ventas = informeVentas.getListaVentasPorMes(fecha_de_busqueda);
				informeVentas.cargarMostrarInformeVentas(informeVentas.lista_ventas);
			}
			dismiss();
		}
	}
	
	public List<Venta> getListaVentasPorFecha(Date fecha){
		List<Venta> lista = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista = db.getVentasPorFecha(fecha);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
}
