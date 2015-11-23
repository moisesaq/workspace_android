package com.moisse.modelo;

import java.sql.Time;

public class Parqueo {
	
	private String idparqueo, nombreParqueo, direccion, logo;
	private int telf, capacidad, tolerancia;
	private double precioHoraDia, precioNoche, precioContratoNocturno, precioContratoDiurno, precioContratoDiaCompleto;
	private Time inicioDia, finDia, inicioNoche, finNoche;
	
	public Parqueo(String idparqueo, String nombreParqueo,int telf, String direccion, int tolerancia, int capacidad,
						double precioHoraDia, double precioNoche, double precioContratoNocturno, double precioContratoDiurno, 
						double precioContratoDiaCompleto, Time inicioDia, Time finDia, Time inicioNoche, Time finNoche, String logo){
		this.idparqueo = idparqueo;
		this.nombreParqueo = nombreParqueo;
		this.telf = telf;
		this.direccion = direccion;
		this.tolerancia = tolerancia;
		this.capacidad = capacidad;
		this.precioHoraDia = precioHoraDia;
		this.precioNoche = precioNoche;
		this.precioContratoNocturno = precioContratoNocturno;
		this.precioContratoDiurno = precioContratoDiurno;
		this.precioContratoDiaCompleto = precioContratoDiaCompleto;
		this.inicioDia = inicioDia;
		this.finDia = finDia;
		this.inicioNoche = inicioNoche;
		this.finNoche = finNoche;
		this.logo = logo;
	}
	
	public Parqueo(String idparqueo, int capacidad){
		this.idparqueo = idparqueo;
		this.capacidad = capacidad;
	}

	public String getIdparqueo() {
		return idparqueo;
	}

	public String getNombreParqueo() {
		return nombreParqueo;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getLogo() {
		return logo;
	}

	public int getTelf() {
		return telf;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public int getTolerancia() {
		return tolerancia;
	}

	public double getPrecioHoraDia() {
		return precioHoraDia;
	}

	public double getPrecioNoche() {
		return precioNoche;
	}

	public double getPrecioContratoNocturno() {
		return precioContratoNocturno;
	}

	public double getPrecioContratoDiurno() {
		return precioContratoDiurno;
	}

	public double getPrecioContratoDiaCompleto() {
		return precioContratoDiaCompleto;
	}

	public Time getInicioDia() {
		return inicioDia;
	}

	public Time getFinDia() {
		return finDia;
	}

	public Time getInicioNoche() {
		return inicioNoche;
	}

	public Time getFinNoche() {
		return finNoche;
	}

		
}
