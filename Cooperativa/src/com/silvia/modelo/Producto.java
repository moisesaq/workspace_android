package com.silvia.modelo;

public class Producto {

	private String idprod, nombre_prod, unidad, descripcion, imagen;
	private double precio;
	private int estado;
	
	public Producto(String idprod, String nombre_prod, double precio, String unidad, String descripcion, String imagen, int estado){
		this.idprod = idprod;
		this.nombre_prod = nombre_prod;
		this.precio = precio;
		this.unidad = unidad;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.estado = estado;
	}

	public String getIdprod() {
		return idprod;
	}

	public String getNombre_prod() {
		return nombre_prod;
	}

	public String getUnidad() {
		return unidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public double getPrecio() {
		return precio;
	}

	public int getEstado() {
		return estado;
	}
	
	public String toString(){
		return this.nombre_prod+" - "+this.precio;
	}
}
