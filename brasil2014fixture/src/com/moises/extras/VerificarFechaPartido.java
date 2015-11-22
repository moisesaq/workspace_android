package com.moises.extras;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.widget.Toast;

public class VerificarFechaPartido {
	
	Calendar calendar=Calendar.getInstance();
	int a=calendar.get(Calendar.YEAR);
	int m=calendar.get(Calendar.MONTH)+1;
	int d=calendar.get(Calendar.DAY_OF_MONTH);
	
	Date fechaActual=Date.valueOf(a+"-"+m+"-"+d);
	
	Date fechaPartido;
	Time horaPartido;
	
	Calendar fecha=new GregorianCalendar();
	int horaA=fecha.get(Calendar.HOUR_OF_DAY);
	int minA=fecha.get(Calendar.MINUTE);
	
	Time horaActual=Time.valueOf(horaA+":"+minA+":00");
	
	public VerificarFechaPartido(){
		
	}
	public VerificarFechaPartido(Date fechaP,Time horaP){
		this.fechaPartido=fechaP;
		this.horaPartido=horaP;
	}

	public boolean verficar(){
		boolean control=false;
		if(fechaPartido.before(fechaActual) ){
			control=true;
		}
		if(!fechaPartido.after(fechaActual) && !fechaPartido.before(fechaActual)){
			control=true;
		}
		return control;
	}
	
	public boolean verificarPartidoHoy(){
		boolean control=false;
		if(!fechaPartido.after(fechaActual) && !fechaPartido.before(fechaActual)){
			control=true;
		}
		return control;
	}
	
	public boolean verificarHoraParaAlarma(){
		boolean control=false;
		if(horaA==(horaPartido.getHours()-1) && (60-minA)==horaPartido.getMinutes()+30){
			control=true;
		}
		return control;
	}
	
	public long diasParaPartido(){
		return (fechaPartido.getTime()-fechaActual.getTime())/(24*60*60*1000);
	}
	
	public boolean verificarHoraPartido(){
		boolean control=false;
		if(fechaActual.after(fechaPartido)){
			control = true;
		}else{
			if(horaPartido.getHours()<=horaA && horaPartido.getMinutes()<=minA){
				control=true;
			}
		}
		return control;
	}
	
	public String horasParaPartido(){
		String mensaje="";
		if(horaA-horaPartido.getHours()==1){
			mensaje=" faltan "+(60-minA)+"min para este partido";
		}else{
			mensaje=" falta "+(horaPartido.getHours()-horaA)+" hrs y "+(minA-horaPartido.getMinutes())+"min para este partido";
		}
		return mensaje;
	}
	
	public Date getFechaActual(){
		return fechaActual;
	}
	
	public Time getHoraActual(){
		return horaActual;
	}
}
