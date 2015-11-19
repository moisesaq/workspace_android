package com.moisse.modelo;

public class Vehiculo {

	public String placa, marca, color, imagen;
	public int estado;
	
	public Vehiculo(String placa, String marca, String color, String imagen, int estado){
		this.placa = placa;
		this.marca = marca;
		this.color = color;
		this.imagen = imagen;
		this.estado = estado;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
}
