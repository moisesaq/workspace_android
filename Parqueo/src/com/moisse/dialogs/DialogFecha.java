package com.moisse.dialogs;

import java.sql.Date;
import java.util.Calendar;

import com.example.parqueo.R;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogFecha extends DialogFragment implements OnClickListener{

	Date fecha_nac;
	TextView textView;
	View view;
	DatePicker datePicker;
	Button btnAceptar, btnCancelar;
	
	public DialogFecha(TextView textView){
		this.textView = textView;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Elegir Fecha");
		view = getActivity().getLayoutInflater().inflate(R.layout.modelo_datepicker, null);
		datePicker = (DatePicker)view.findViewById(R.id.datepickerFecha);
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
			String fecha = new StringBuilder().append(year).append("-").append(month+1).append("-").append(day).toString();
			fecha_nac = Date.valueOf(fecha);
			textView.setText(MyVar.FORMAT_FECHA_1.format(fecha_nac));
			textView.setTag(fecha_nac);
			dismiss();
		}else if (v.getId()==btnCancelar.getId()) {
			dismiss();
		}
	}
}
