package com.moisse.modelo;

import java.sql.Date;

public class Cliente {
	
	private int celular, estado, tipo;
	private String idcliente, ci, nombre, apellido, direccion, email, sexo, imagen, idparqueo;
	private Date fecha_nac, fecha_contrato;
	
	public Cliente(String idcliente, String ci, String nombre, String apellido, int celular, String direccion, String email, 
					String sexo, Date fecha_nac, String imagen, int estado, int tipo, Date fecha_contrato, String idparqueo){
		this.idcliente = idcliente;
		this.ci = ci;
		this.nombre = nombre;
		this.apellido = apellido;
		this.celular = celular;
		this.direccion = direccion;
		this.email = email;
		this.sexo = sexo;
		this.fecha_nac = fecha_nac;
		this.imagen = imagen;
		this.estado = estado;
		this.tipo = tipo;
		this.fecha_contrato = fecha_contrato;
		this.idparqueo = idparqueo;
	}
	
	//Constructor para dar de baja al cliente
	public Cliente(String idcliente, int estado){
		this.idcliente = idcliente;
		this.estado = estado;
	}

	public String getCi() {
		return ci;
	}

	public int getCelular() {
		return celular;
	}

	public int getEstado() {
		return estado;
	}

	public int getTipo() {
		return tipo;
	}

	public String getIdcliente() {
		return idcliente;
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

	public String getIdparqueo() {
		return idparqueo;
	}

	public Date getFecha_nac() {
		return fecha_nac;
	}

	public Date getFecha_contrato() {
		return fecha_contrato;
	}

	public String toString(){
		return this.ci+" - "+this.nombre;
	}
}
