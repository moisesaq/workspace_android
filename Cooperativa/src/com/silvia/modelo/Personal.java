package com.silvia.modelo;

import java.sql.Date;

public class Personal {

	private String idpersonal, ci, nombre, apellido, direccion, email, imagen, idcargo, idmaquinaria;
	private Date fecha_nac, fecha_ingreso;
	private int telf, estado;
	
	public Personal(String idpersonal, String ci, String nombre, String apellido, String direccion, int telf, String email, Date fecha_nac, Date fecha_ingreso,
					String imagen, int estado, String idcargo, String idmaquinaria) {

		this.idpersonal = idpersonal;
		this.ci = ci;
		this.nombre = nombre;
		this.apellido = apellido;
		this.direccion = direccion;
		this.telf = telf;
		this.email = email;
		this.fecha_nac = fecha_nac;
		this.fecha_ingreso = fecha_ingreso;
		this.imagen = imagen;
		this.estado = estado;
		this.idcargo = idcargo;
		this.idmaquinaria = idmaquinaria;		
	}

	public String getIdpersonal() {
		return idpersonal;
	}
	
	public String getCi(){
		return ci;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getEmail() {
		return email;
	}

	public String getImagen() {
		return imagen;
	}

	public String getIdcargo() {
		return idcargo;
	}

	public String getIdmaquinaria() {
		return idmaquinaria;
	}

	public Date getFecha_nac() {
		return fecha_nac;
	}

	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}

	public int getTelf() {
		return telf;
	}

	public int getEstado() {
		return estado;
	}
	
	public String toString(){
		return this.ci+" - "+this.nombre;
	}
	
}
