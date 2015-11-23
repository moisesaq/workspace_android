package com.silvia.modelo;

public class DetalleVenta {

	String iddetalle, idventa, idproducto;
	int cantidad;
	double costo_entrega, costo;
	
	public DetalleVenta(String iddetalle, String idventa, String idproducto, int cantidad, double costo_entrega, double costo){
		this.iddetalle = iddetalle;
		this.idventa = idventa;
		this.idproducto = idproducto;
		this.cantidad = cantidad;
		this.costo_entrega = costo_entrega;
		this.costo = costo;
	}

	public String getIddetalle() {
		return iddetalle;
	}

	public String getIdventa() {
		return idventa;
	}

	public String getIdproducto() {
		return idproducto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public double getCosto_entrega() {
		return costo_entrega;
	}

	public double getCosto() {
		return costo;
	}
	
	public void setIddetalle(String iddetalle) {
		this.iddetalle = iddetalle;
	}

	public void setIdventa(String idventa) {
		this.idventa = idventa;
	}

	public void setIdproducto(String idproducto) {
		this.idproducto = idproducto;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public void setCosto_entrega(double costo_entrega) {
		this.costo_entrega = costo_entrega;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public String toString(){
		return this.idproducto+" - "+this.cantidad+" - "+this.costo_entrega+" - "+this.costo;
	}
}
