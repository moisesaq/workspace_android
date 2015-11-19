package com.moises.fragments;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Persona implements Serializable{

	public int dni;
	public String nombre;
	public Persona(int dni, String nombre){
		this.dni = dni;
		this.nombre = nombre;
	}
	
	public int getDni() {
		return dni;
	}
	public void setDni(int dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "DNI: "+this.dni+" Nombre: "+this.nombre;
	}
	
	
}
