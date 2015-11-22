package com.moises.modelo;

import java.sql.Date;
import java.sql.Time;

public class PartidoOctavos {

	String idpartidooctavos, idoctavos1, pais1, pais2, idoctavos2;
	int gol1, gol2,estado;
	Date fecha;
	Time hora;
	
	public PartidoOctavos(String idpartidooctavos, String idoctavos1, String pais1, int gol1, int gol2, String pais2, String idoctavos2, Date fecha, Time hora, int estado){
		this.idpartidooctavos=idpartidooctavos;
		this.idoctavos1=idoctavos1;
		this.pais1=pais1;
		this.gol1=gol1;
		this.gol2=gol2;
		this.pais2=pais2;
		this.idoctavos2=idoctavos2;
		this.fecha=fecha;
		this.hora=hora;
		this.estado=estado;
	}

	public String getIdpartidooctavos() {
		return idpartidooctavos;
	}

	public void setIdpartidooctavos(String idpartidooctavos) {
		this.idpartidooctavos = idpartidooctavos;
	}

	public String getIdoctavos1() {
		return idoctavos1;
	}

	public void setIdoctavos1(String idoctavos1) {
		this.idoctavos1 = idoctavos1;
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

	public String getIdoctavos2() {
		return idoctavos2;
	}

	public void setIdoctavos2(String idoctavos2) {
		this.idoctavos2 = idoctavos2;
	}

	public int getGol1() {
		return gol1;
	}

	public void setGol1(int gol1) {
		this.gol1 = gol1;
	}

	public int getGol2() {
		return gol2;
	}

	public void setGol2(int gol2) {
		this.gol2 = gol2;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}
	
	
}
