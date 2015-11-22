package com.moises.modelo;

import java.sql.Date;
import java.sql.Time;

public class PartidoCuartos {

	String idpartidocuartos,idganador1,paisganador1,paisganador2,idganador2;
	Date fecha;
	Time hora;
	int gol1,gol2,estado, bandera1, bandera2;
	
	public PartidoCuartos(String idpartidocuartos,String idganador1,int bandera1, String paisganador1,int gol1, int gol2,int bandera2, String paisganador2, String idganador2, Date fecha, Time hora, int estado){
		this.idpartidocuartos=idpartidocuartos;
		this.idganador1=idganador1;
		this.bandera1=bandera1;
		this.paisganador1=paisganador1;
		this.gol1=gol1;
		this.gol2=gol2;
		this.bandera2=bandera2;
		this.paisganador2=paisganador2;
		this.idganador2=idganador2;
		this.fecha=fecha;
		this.hora=hora;
		this.estado=estado;
	}

	public String getIdpartidocuartos() {
		return idpartidocuartos;
	}

	public void setIdpartidocuartos(String idpartidocuartos) {
		this.idpartidocuartos = idpartidocuartos;
	}

	public String getIdganador1() {
		return idganador1;
	}

	public void setIdganador1(String idganador1) {
		this.idganador1 = idganador1;
	}

	public String getPaisganador1() {
		return paisganador1;
	}

	public void setPaisganador1(String paisganador1) {
		this.paisganador1 = paisganador1;
	}

	public String getPaisganador2() {
		return paisganador2;
	}

	public void setPaisganador2(String paisganador2) {
		this.paisganador2 = paisganador2;
	}

	public String getIdganador2() {
		return idganador2;
	}

	public void setIdganador2(String idganador2) {
		this.idganador2 = idganador2;
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

	public int getBandera1() {
		return bandera1;
	}

	public void setBandera1(int bandera1) {
		this.bandera1 = bandera1;
	}

	public int getBandera2() {
		return bandera2;
	}

	public void setBandera2(int bandera2) {
		this.bandera2 = bandera2;
	}
	
	
}
