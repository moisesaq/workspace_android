package com.moises.httpurlconnection;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Lugar implements Serializable{

	String idlugar, nombre_lugar, direccion, descripcion;
	
	public Lugar(String idlugar, String nombre_lugar, String direccion, String descripcion){
		this.idlugar = idlugar;
		this.nombre_lugar = nombre_lugar;
		this.direccion = direccion;
		this.descripcion = descripcion;
	}

	public String getIdlugar() {
		return idlugar;
	}

	public void setIdlugar(String idlugar) {
		this.idlugar = idlugar;
	}

	public String getNombre_lugar() {
		return nombre_lugar;
	}

	public void setNombre_lugar(String nombre_lugar) {
		this.nombre_lugar = nombre_lugar;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return this.nombre_lugar+" --> "+this.direccion;
	}
	
	
	
}
