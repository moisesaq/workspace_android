package com.moisse.dialogs;

import java.sql.Date;
import java.sql.Time;

import com.example.parqueo.R;
import com.moisse.modelo.Parqueo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogTest extends DialogFragment implements OnClickListener{

	EditText etHoraE, etFechaE, etHoraS, etFechaS;
	TextView tvMensaje;
	Button btnProbar;
	View view;
	
	Parqueo parqueo;
	
	public DialogTest(Parqueo parqueo){
		this.parqueo = parqueo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		view = getActivity().getLayoutInflater().inflate(R.layout.modelo_test, null);
		etHoraE = (EditText)view.findViewById(R.id.etHoraEntradaTest);
		etFechaE = (EditText)view.findViewById(R.id.etFechaEntradaTest);
		etHoraS = (EditText)view.findViewById(R.id.etHoraSalidaTest);
		etFechaS = (EditText)view.findViewById(R.id.etFechaSalidaTest);
		tvMensaje = (TextView)view.findViewById(R.id.tvMensajeTest);
		btnProbar = (Button)view.findViewById(R.id.btnProbarTest);
		btnProbar.setOnClickListener(this);
		
		dialog.setView(view);
		
		return dialog.create();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnProbar.getId()){
			if(!etHoraE.getText().toString().equals("") && !etFechaE.getText().toString().equals("")){
				probarYa();
			}else{
				Toast.makeText(getActivity(), "Campos estan vacios", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void probarYa(){
		Time horaE = Time.valueOf(etHoraE.getText().toString());
		Date fechaE = Date.valueOf(etFechaE.getText().toString());
		Time horaS = Time.valueOf(etHoraS.getText().toString());
		Date fechaS = Date.valueOf(etFechaS.getText().toString());
//		GenerarDatosSalida gDato = new GenerarDatosSalida(parqueo, horaE, fechaE, horaS, fechaS);
////		tvMensaje.setText(new StringBuilder(gDato.getMensaje()).append("\n Horas dia por cobrar: ").append(gDato.getTiempoDia())
////				.append("\n").append("Noches: ").append(gDato.getCantNoche()));
//		
//		tvMensaje.setText(new StringBuilder(gDato.getVistaTiempo()).append("\n Horas dia por cobrar: ").append(gDato.getTiempoDia())
//				.append("\n").append("Noches: ").append(gDato.getCantNoche()));
		
//		tvMensaje.setText(new StringBuilder(gDato.getVistaTDia()).append("\n").append(gDato.getVistaTNoche())
//				.append("\n").append(gDato.getVistaTiempo()));
//				//append(gDato.getMensaje2()).append("\n Tiempo adicional: ").append(gDato.getTiempoAdicional()));
	}
	
}
