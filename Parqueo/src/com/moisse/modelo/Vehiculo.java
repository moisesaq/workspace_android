package com.moisse.modelo;

public class Vehiculo {
	private String idvehiculo, placa, marca, color, imagen, idcliente;
	private int estado;
	
	public Vehiculo(String idvehiculo, String placa, String marca, String color, String imagen, int estado, String idcliente){
		this.idvehiculo = idvehiculo;
		this.placa = placa;
		this.marca = marca;
		this.color = color;
		this.imagen = imagen;
		this.estado = estado;
		this.idcliente = idcliente;
	}
	//Constructor para dar de baja al vehiculo
	public Vehiculo(String idvehiculo, int estado){
		this.idvehiculo = idvehiculo;
		this.estado = estado;
	}

	public String getIdvehiculo() {
		return idvehiculo;
	}
	public String getPlaca() {
		return placa;
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
	public String toString(){
		return this.placa;
	}
	public String getIdcliente() {
		return idcliente;
	}
}
