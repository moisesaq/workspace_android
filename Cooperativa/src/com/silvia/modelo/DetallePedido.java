package com.silvia.modelo;

public class DetallePedido {

	public String iddetalle_pedido, idpedido, idproducto; 
	public int cantidad;
	public double costo_entrega, costo;
	
	public DetallePedido(String iddetalle_pedido, String idpedido, String idproducto, int cantidad, double costo_entrega, double costo){
		this.iddetalle_pedido = iddetalle_pedido;
		this.idpedido = idpedido;
		this.idproducto = idproducto;
		this.cantidad = cantidad;
		this.costo_entrega = costo_entrega;
		this.costo = costo;
	}

	public String getIddetalle_pedido() {
		return iddetalle_pedido;
	}

	public void setIddetalle_pedido(String iddetalle_pedido) {
		this.iddetalle_pedido = iddetalle_pedido;
	}

	public String getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(String idpedido) {
		this.idpedido = idpedido;
	}

	public String getIdproducto() {
		return idproducto;
	}

	public void setIdproducto(String idproducto) {
		this.idproducto = idproducto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getCosto_entrega() {
		return costo_entrega;
	}

	public void setCosto_entrega(double costo_entrega) {
		this.costo_entrega = costo_entrega;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	
}
