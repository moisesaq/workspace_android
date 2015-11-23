package com.silvia.modelo;

import java.sql.Date;
import java.sql.Time;

public class Pedido {

	public String idpedido, idusuario, idcliente, idpersonal, direccion, nota;
	public Date fecha_pedido, fecha_entrega;
	public Time hora_pedido;
	public double costo_total;
	public int estado;
	
	public Pedido(String idpedido, String idusuario, String idcliente, Date fecha_pedido, Time hora_pedido, 
						Date fecha_entrega, String idpersonal, String direccion, double costo_total, String nota, int estado){
		this.idpedido = idpedido;
		this.idusuario = idusuario;
		this.idcliente = idcliente;
		this.fecha_pedido = fecha_pedido;
		this.hora_pedido = hora_pedido;
		this.fecha_entrega = fecha_entrega;
		this.idpersonal = idpersonal;
		this.direccion = direccion;
		this.costo_total = costo_total;
		this.nota = nota;
		this.estado = estado;
	}

	public String getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(String idpedido) {
		this.idpedido = idpedido;
	}

	public String getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

	public String getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(String idcliente) {
		this.idcliente = idcliente;
	}

	public String getIdpersonal() {
		return idpersonal;
	}

	public void setIdpersonal(String idpersonal) {
		this.idpersonal = idpersonal;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Date getFecha_pedido() {
		return fecha_pedido;
	}

	public void setFecha_pedido(Date fecha_pedido) {
		this.fecha_pedido = fecha_pedido;
	}

	public Date getFecha_entrega() {
		return fecha_entrega;
	}

	public void setFecha_entrega(Date fecha_entrega) {
		this.fecha_entrega = fecha_entrega;
	}

	public Time getHora_pedido() {
		return hora_pedido;
	}

	public void setHora_pedido(Time hora_pedido) {
		this.hora_pedido = hora_pedido;
	}

	public double getCosto_total() {
		return costo_total;
	}

	public void setCosto_total(double costo_total) {
		this.costo_total = costo_total;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

}
