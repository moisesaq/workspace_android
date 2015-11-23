package com.silvia.modelo;

import java.sql.Date;
import java.sql.Time;

public class Venta {

	public String idventa, idusuario, idcliente, idpersonal, direccion, nota;
	public int tipo_venta;
	public Date fecha_venta;
	public Time hora_venta;
	public double costo_total;
	
	public Venta(String idventa, int tipo_venta, String idusuario, String idcliente, Date fecha_venta, 
								Time hora_venta, String idpersonal, String direccion, double costo_total, String nota){
		this.idventa = idventa;
		this.tipo_venta = tipo_venta;
		this.idusuario = idusuario;
		this.idcliente = idcliente;
		this.fecha_venta = fecha_venta;
		this.hora_venta = hora_venta;
		this.idpersonal = idpersonal;
		this.direccion = direccion;
		this.costo_total = costo_total;
		this.nota = nota;
	}

	public String getIdventa() {
		return idventa;
	}

	public String getIdusuario() {
		return idusuario;
	}

	public String getIdcliente() {
		return idcliente;
	}

	public String getIdpersonal() {
		return idpersonal;
	}

	public String getDireccion() {
		return direccion;
	}

	public int getTipo_venta() {
		return tipo_venta;
	}

	public Date getFecha_venta() {
		return fecha_venta;
	}

	public double getCosto_total() {
		return costo_total;
	}
		
	public void setCosto_total(double costo_total) {
		this.costo_total = costo_total;
	}
	
	public void setIdventa(String idventa) {
		this.idventa = idventa;
	}

	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

	public void setIdcliente(String idcliente) {
		this.idcliente = idcliente;
	}

	public void setIdpersonal(String idpersonal) {
		this.idpersonal = idpersonal;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTipo_venta(int tipo_venta) {
		this.tipo_venta = tipo_venta;
	}

	public void setFecha_venta(Date fecha_venta) {
		this.fecha_venta = fecha_venta;
	}
	
	public Time getHora_venta() {
		return hora_venta;
	}

	public void setHora_venta(Time hora_venta) {
		this.hora_venta = hora_venta;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String toString(){
		return this.idcliente+"-"+this.costo_total;
	}
}
