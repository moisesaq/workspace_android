package com.moisse.modelo;

import java.sql.Date;

public class Usuario {
	
	private int cargo, estado, celular;
	private String idusuario, ci, nombre, apellido, nombre_usuario, clave, direccion, email, sexo, imagen,  idparqueo;
	private Date fecha_nac;
	
	public Usuario(String idusuario, String ci, String nombre, String apellido, String nombre_usuario, String clave, int celular, String direccion, 
				   	String email, String sexo, Date fecha_nac, int cargo, int estado, String imagen, String idparqueo){
		this.idusuario = idusuario;
		this.ci=ci;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nombre_usuario = nombre_usuario;
		this.clave = clave;
		this.celular = celular;
		this.direccion = direccion;
		this.email = email;
		this.sexo = sexo;
		this.fecha_nac = fecha_nac;
		this.cargo = cargo;
		this.estado = estado;
		this.imagen = imagen;
		this.idparqueo = idparqueo;
	}
	
	//Constructor para editar datos del usuario
	public Usuario(String idusuario, String ci, String nombre, String apellido, int celular, String direccion, 
			   						String email, String sexo, Date fecha_nac, String imagen){
		this.idusuario = idusuario;
		this.ci = ci;
		this.nombre = nombre;
		this.apellido = apellido;
		this.celular = celular;
		this.direccion = direccion;
		this.email = email;
		this.sexo = sexo;
		this.fecha_nac = fecha_nac;
		this.imagen = imagen;
	}
	
	//Constructor para cambiar clave del usuario
	public Usuario(String idusuario , String nombre_usuario, String clave){
		this.idusuario = idusuario;
		this.nombre_usuario = nombre_usuario;
		this.clave = clave;
		}

	public Usuario(String usuario, String clave){
		this.nombre_usuario = usuario;
		this.clave = clave;
	}
	
	
		
	public int getCargo() {
		return cargo;
	}

	public int getEstado() {
		return estado;
	}

	public int getCelular() {
		return celular;
	}

	public String getIdusuario() {
		return idusuario;
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

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public String getClave() {
		return clave;
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

	public String toString(){
		return this.nombre_usuario+" - "+this.ci;
	}
}
