package com.silvia.modelo;

public class Maquinaria {

	private String idmaquinaria, placa, descripcion, marca, color, imagen;
	private int estado;
	private double capacidad;
	
	public Maquinaria(String idmaquinaria, String placa, String descripcion, double capacidad, String marca, String color, String imagen, int estado) {
		super();
		this.idmaquinaria = idmaquinaria;
		this.placa = placa;
		this.descripcion = descripcion;
		this.capacidad = capacidad;
		this.marca = marca;
		this.color = color;
		this.imagen = imagen;
		this.estado = estado;
	}

	public String getIdmaquinaria() {
		return idmaquinaria;
	}

	public String getPlaca() {
		return placa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getMarca() {
		return marca;
	}

	public String getColor() {
		return color;
	}

	public String getImagen() {
		return imagen;
	}

	public int getEstado() {
		return estado;
	}
	
	public double getCapacidad() {
		return capacidad;
	}

	public String toString(){
		return this.placa+"-"+this.descripcion;
				
	}
}
