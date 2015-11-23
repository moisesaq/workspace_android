package com.moisse.dialogs;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
public class DialogDefinirHora extends DialogFragment implements OnClickListener{

	View view;
	Button btnCancelar, btnAceptar;
	TimePicker timePicker;
	TextView tvInicio, tvFin, tvHoraIngreso;
	int OPCION;
	int horaTimePicker;
	int minTimePicker;
	
	public DialogDefinirHora(TextView tvInicio, TextView tvFin, int opcion, int horaTimePicker, int minTimePicker){
		this.tvInicio = tvInicio;
		this.tvFin = tvFin;
		this.OPCION = opcion;
		this.horaTimePicker = horaTimePicker;
		this.minTimePicker = minTimePicker;
	}
	
	public DialogDefinirHora(TextView tvHoraIngreso, int horaTimePicker, int minTimePicker){
		this.tvHoraIngreso = tvHoraIngreso;
		this.horaTimePicker = horaTimePicker;
		this.minTimePicker = minTimePicker;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstateState){

		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("Establecer Hora");
		view = getActivity().getLayoutInflater().inflate(R.layout.modelo_timepicker, null);
		timePicker = (TimePicker)view.findViewById(R.id.timePickerDefinirHora);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(horaTimePicker);
		timePicker.setCurrentMinute(minTimePicker);
		btnCancelar = (Button)view.findViewById(R.id.btnCancelarTimePicker);
		btnCancelar.setOnClickListener(this);
		btnAceptar = (Button)view.findViewById(R.id.btnAceptarTimePicker);
		btnAceptar.setOnClickListener(this);
		dialog.setView(view);
		
		return dialog.create();
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==btnCancelar.getId()){
			dismiss();
		}else if(v.getId()==btnAceptar.getId()){
			int hora = timePicker.getCurrentHour();
			int min = timePicker.getCurrentMinute();
			Time hora_definido = Time.valueOf(new StringBuilder().append(hora).append(":").append(min).append(":").append(0).toString());
			if(tvHoraIngreso==null){
				tvInicio.setText(hora_definido.toString());
				tvInicio.setTag(hora_definido);
				if(OPCION==MyVar.DEFINIR_HORARIO_DIURNO){
					Time hora_fin = getTimeModificado(hora_definido, -1);
					tvFin.setText(hora_fin.toString());
					tvFin.setTag(hora_fin);
				}else if (OPCION==MyVar.DEFINIR_HORARIO_NOCTURNO) {
					Time hora_fin = getTimeModificado(hora_definido, 1);
					tvFin.setText(hora_fin.toString());
					tvFin.setTag(hora_fin);
				}
			}else{
				java.util.Date date = new Date();
				Date auxDate = new Date(date.getYear(), date.getMonth(), date.getDay(), hora, min, 0);
				tvHoraIngreso.setText(MyVar.FORMAT_HORA_1.format(auxDate));
				tvHoraIngreso.setTag(hora_definido);
			}
			
			
			dismiss();
		}
	}
	
	public Time getTimeModificado(Time time, int min){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, time.getHours(), time.getMinutes(), time.getSeconds());
		calendar.add(Calendar.MINUTE, min);
		Time time_modificado = new Time(calendar.getTimeInMillis());
		return time_modificado;
	}
	
}
