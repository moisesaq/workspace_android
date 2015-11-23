package com.silvia.modelo;

public class Usuario {

	private String idusuario, usuario, clave, idpersonal;
	private int estado;
	
	public Usuario(String idusuario, String usuario, String clave, int estado, String idpersonal){
		this.idusuario = idusuario;
		this.usuario = usuario;
		this.clave = clave;
		this.estado = estado;
		this.idpersonal = idpersonal;
	}

	public String getIdusuario() {
		return idusuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getClave() {
		return clave;
	}

	public String getIdpersonal() {
		return idpersonal;
	}

	public int getEstado() {
		return estado;
	}
	
	public String toString(){
		return this.usuario;
	}
}
