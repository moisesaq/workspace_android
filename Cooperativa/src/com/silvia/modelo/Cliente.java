package com.silvia.modelo;

import java.sql.Date;

public class Cliente {

	private String idcliente, ci, nombre, apellido, direccion, email, sexo, imagen;
	private int telf, estado;
	private Date fechaNac, fechaReg;
	
	public Cliente(String idcliente, String ci, String nombre, String apellido, String direccion, int telf, String email, 
							String sexo, Date fechaNac, Date fechaReg, String imagen, int estado){
		this.idcliente = idcliente;
		this.ci = ci;
		this.nombre = nombre;
		this.apellido = apellido;
		this.direccion = direccion;
		this.telf = telf;
		this.email = email;
		this.sexo = sexo;
		this.fechaNac = fechaNac;
		this.fechaReg = fechaReg;
		this.imagen = imagen;
		this.estado = estado;
	}

	public String getIdcliente() {
		return idcliente;
	}

	public String getCi() {
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

	public String getSexo() {
		return sexo;
	}

	public String getImagen() {
		return imagen;
	}

	public int getTelf() {
		return telf;
	}

	public int getEstado() {
		return estado;
	}

	public Date getFechaNac() {
		return fechaNac;
	}

	public Date getFechaReg() {
		return fechaReg;
	}
	
	public String toString(){
		return this.ci+" - "+this.nombre;
	}
	
}

