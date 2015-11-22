package com.moises.modelo;

import java.sql.Date;
import java.sql.Time;

public class Partido {

	String pais1,pais2,grupo;
	int idpartido,goles1,goles2,estado,recordar;
	Date fecha;
	Time hora;
	
	public Partido(int idpartido,String pais1, String pais2,int goles1,int goles2,Time hora,Date fecha, String grupo, int estado,int recordar){
		this.idpartido=idpartido;
		this.pais1=pais1;
		this.pais2=pais2;
		this.goles1=goles1;
		this.goles2=goles2;
		this.hora=hora;
		this.fecha=fecha;
		this.grupo=grupo;
		this.estado=estado;
		this.recordar=recordar;
	}

	public String getPais1() {
		return pais1;
	}

	public void setPais1(String pais1) {
		this.pais1 = pais1;
	}

	public String getPais2() {
		return pais2;
	}

	public void setPais2(String pais2) {
		this.pais2 = pais2;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public int getIdpartido() {
		return idpartido;
	}

	public void setIdpartido(int idpartido) {
		this.idpartido = idpartido;
	}

	public int getGoles1() {
		return goles1;
	}

	public void setGoles1(int goles1) {
		this.goles1 = goles1;
	}

	public int getGoles2() {
		return goles2;
	}

	public void setGoles2(int goles2) {
		this.goles2 = goles2;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getRecordar() {
		return recordar;
	}

	public void setRecordar(int recordar) {
		this.recordar = recordar;
	}
	
	
}
