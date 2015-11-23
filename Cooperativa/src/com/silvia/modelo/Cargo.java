package com.silvia.modelo;

public class Cargo {

	private String idcargo, ocupacion, descripcion;
	private double salario;
	private int estado;
	
	public Cargo(String idcargo, String ocupacion, double salario, String descripcion, int estado){
		this.idcargo = idcargo;
		this.ocupacion = ocupacion;
		this.salario = salario;
		this.descripcion = descripcion;
		this.estado = estado;
	}
	
	public String getIdcargo(){
		return idcargo;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public double getSalario() {
		return salario;
	}

	public int getEstado() {
		return estado;
	}
	
	public String toString(){
		return this.ocupacion;
	}
}
