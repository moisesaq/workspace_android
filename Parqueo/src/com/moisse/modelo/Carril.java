package com.moisse.modelo;

public class Carril {

	private int num_carril, estado;
	private String idcarril, disponible, reservado, idpaqueo;
	
	public Carril(String idcarril, int num_carril, String disponible, String reservado, int estado, String idparqueo){
		this.idcarril = idcarril;
		this.num_carril = num_carril;
		this.disponible = disponible;
		this.reservado = reservado;
		this.estado = estado;
		this.idpaqueo = idparqueo;
	}
	
	public String getIdcarril() {
		return idcarril;
	}
	
	public void setIdcarril(String idcarril) {
		this.idcarril = idcarril;
	}

	public int getNum_carril() {
		return num_carril;
	}

	public void setNum_carril(int num_carril) {
		this.num_carril = num_carril;
	}

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String disponible) {
		this.disponible = disponible;
	}

	public String getReservado() {
		return reservado;
	}

	public void setReservado(String reservado) {
		this.reservado = reservado;
	}

	public String getIdpaqueo() {
		return idpaqueo;
	}

	public void setIdpaqueo(String idpaqueo) {
		this.idpaqueo = idpaqueo;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public String toString(){
		return this.num_carril+" - "+this.disponible;
	}
	
}
