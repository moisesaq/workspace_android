package com.silvia.modelo;

public class Cooperativa {

	private String idcoop, nombre, telf_cel, nit, email, direccion, ciudad;
	
	public Cooperativa(String idcoop, String nombre, String nit, String telf_cel, String email, String direccion, String ciudad){
		this.idcoop = idcoop;
		this.nombre = nombre;
		this.nit = nit;
		this.telf_cel = telf_cel;
		this.email = email;
		this.direccion = direccion;
		this.ciudad = ciudad;
	}

	public String getIdcoop() {
		return idcoop;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNit() {
		return nit;
	}

	public String getEmail() {
		return email;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setIdcoop(String idcoop) {
		this.idcoop = idcoop;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getTelf_cel() {
		return telf_cel;
	}

	public void setTelf_cel(String telf_cel) {
		this.telf_cel = telf_cel;
	}
	
}
