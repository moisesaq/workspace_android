package com.moisse.dialogs;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.parqueo.R;
import com.moisse.fragments.ListaResguardo;
import com.moisse.modelo.Resguardo;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogBuscarResguardoFecha extends DialogFragment implements OnDateSetListener, OnClickListener{
	
	//OnResguardoPorFechaClickListener mListener;
	private ListaResguardo lista_resguardo;
	private View view;
	private DatePicker datePicker;
	private Button btnAceptar, btnCancelar;
	private int opcion;
	private TextView viewTag;
	
	public DialogBuscarResguardoFecha(ListaResguardo listaResguardo, int opcion, TextView viewTag){
		this.lista_resguardo = listaResguardo;
		this.opcion = opcion;
		this.viewTag = viewTag;
	}
	
	public Dialog onCreateDialog(Bundle savedInstaceState){
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Filtrar por fecha");
		view = getActivity().getLayoutInflater().inflate(R.layout.modelo_datepicker, null);
		datePicker = (DatePicker)view.findViewById(R.id.datepickerFecha);
		if(opcion==2){
			dialog.setTitle("Filtrar por mes");
			ocultarDia();
		}
		datePicker.init(year, month, day, null);
		datePicker.setCalendarViewShown(false);
		btnAceptar = (Button)view.findViewById(R.id.btnAceptarDatePicker);
		btnAceptar.setOnClickListener(this);
		btnCancelar =(Button)view.findViewById(R.id.btnCancelarDatePicker);
		btnCancelar.setOnClickListener(this);
		dialog.setView(view);
		
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnAceptar.getId()){
			int year = datePicker.getYear();
			int month = datePicker.getMonth();
			int day = datePicker.getDayOfMonth();
			Date fecha_de_busqueda = Date.valueOf(new StringBuilder().append(year).append("-").append(month+1).append("-").append(day).toString());
			viewTag.setTag(fecha_de_busqueda);
			if(opcion==1){
				List<Resguardo> lista_buscado = lista_resguardo.getListaResguardosPorFecha(fecha_de_busqueda, fecha_de_busqueda);
				lista_resguardo.cargarListaResguardos(lista_buscado);
				lista_resguardo.OPCION_ACT = MyVar.ACT_RESG_FECHA_ESPECIFICO;
				lista_resguardo.tvCuando.setText(new StringBuilder("Ingresos de ").append(MyVar.FORMAT_FECHA_1.format(fecha_de_busqueda)));
				Toast.makeText(getActivity(), MyVar.FORMAT_FECHA_1.format(fecha_de_busqueda), Toast.LENGTH_LONG).show();
			}else if(opcion==2){
				SimpleDateFormat formatDate = new SimpleDateFormat("MMMM 'de' yyyy", Locale.getDefault());
				List<Resguardo> lista_buscado = lista_resguardo.getListaResguardosMes(fecha_de_busqueda);
				lista_resguardo.cargarListaResguardos(lista_buscado);
				lista_resguardo.OPCION_ACT = MyVar.ACT_RESG_MES_ESPECIFICO;
				lista_resguardo.tvCuando.setText(new StringBuilder("Ingresos del ").append(formatDate.format(fecha_de_busqueda)));
				Toast.makeText(getActivity(), formatDate.format(fecha_de_busqueda), Toast.LENGTH_LONG).show();
			}
			
			dismiss();
		}else if(v.getId()==btnCancelar.getId()){
			dismiss();
		}
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
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}
	
	public boolean verifyVersion(){
		//Otra manera para compara Build.VERSION_CODES.HONEYCOMB, y verificar bien con de esta manera para que funcione bien
		if(Build.VERSION.SDK_INT < 5.0){
			return true;
		}
		return false;
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Date fecha_de_busqueda = Date.valueOf(year+"-"+(month+1)+"-"+day);
		List<Resguardo> lista_buscado = lista_resguardo.getListaResguardosPorFecha(fecha_de_busqueda, fecha_de_busqueda);
		lista_resguardo.cargarListaResguardos(lista_buscado);
	}
	
}
