package com.moisse.modelo;

import java.sql.Date;
import java.sql.Time;


public class Resguardo {

	private int estado, tipo;
	private Date fechaE, fechaS;
	private Time horaE, horaS;
	private double costoDia, costoNoche, costoTotal;
	private String idcarril, idvehiculo, idresguardo, nota;
	
	public Resguardo(String idresguardo, Time horaE, Date fechaE, Time horaS, Date fechaS, 
						double costoDia, double costoNoche, double costoTotal, int estado, String nota, int tipo, String idcarril, String idvehiculo){
		this.idresguardo = idresguardo;
		this.horaE = horaE;
		this.fechaE = fechaE;
		this.horaS = horaS;
		this.fechaS = fechaS;
		this.costoDia = costoDia;
		this.costoNoche = costoNoche;
		this.costoTotal = costoTotal;
		this.estado = estado;
		this.nota = nota;
		this.tipo = tipo;
		this.idcarril = idcarril;
		this.idvehiculo = idvehiculo;
	}
	
	//CONSTRUCTOR PARA SACAR TODOS LOS INGRESOS DE LOS VEHICULOS Y PARA REGISTRAR EL INGRESO DEL VEHICULO
	public Resguardo(String idresguardo, Time horaE, Date fechaE, double costoTotal, int estado, String nota, int tipo, String idcarril, String idvehiculo){
		this.idresguardo = idresguardo;
		this.horaE = horaE;
		this.fechaE = fechaE;
		this.costoTotal = costoTotal;
		this.estado = estado;
		this.nota = nota;
		this.tipo = tipo;
		this.idcarril = idcarril;
		this.idvehiculo = idvehiculo;
	}
	
	//CONSTRUCTOR PARA ACTUALIZAR O REGISTRAR LA SALIDA DEL VEHICULO
	public Resguardo(String idresguardo, Time horaS, Date fechaS, double costoDia, double costoNoche, double costoTotal, String nota, 
						int tipo, String idcarril){
		this.idresguardo = idresguardo;
		this.horaS = horaS;
		this.fechaS = fechaS;
		this.costoDia = costoDia;
		this.costoNoche = costoNoche;
		this.costoTotal = costoTotal;
		this.nota = nota;
		this.tipo = tipo;
		this.idcarril = idcarril;
	}
	
	public Resguardo(String idresguardo, String nota){
		this.idresguardo = idresguardo;
		this.nota = nota;
	}
	
	public String toString(){
		return this.idcarril +" - "+this.idvehiculo+" - "+this.horaE.toString()+" | "+this.horaS.toString()+" Bs."+this.costoTotal;
	}

	public int getEstado() {
		return estado;
	}

	public Date getFechaE() {
		return fechaE;
	}

	public Date getFechaS() {
		return fechaS;
	}

	public Time getHoraE() {
		return horaE;
	}

	public Time getHoraS() {
		return horaS;
	}

	public double getCostoDia() {
		return costoDia;
	}

	public double getCostoNoche() {
		return costoNoche;
	}

	public double getCostoTotal() {
		return costoTotal;
	}

	public String getIdcarril() {
		return idcarril;
	}

	public String getIdvehiculo() {
		return idvehiculo;
	}

	public String getIdresguardo() {
		return idresguardo;
	}

	public String getNota() {
		return nota;
	}
	
	public int getTipo(){
		return tipo;
	}
}
