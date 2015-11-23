package com.moisse.modelo;

public class NavDrawerItem {

	private int idItem;
	private int icono;
	private String titulo, icono_p;
	
	public NavDrawerItem(int idItem, int icono, String titulo){
		this.idItem = idItem;
		this.icono=icono;
		this.titulo=titulo;
	}
	
	public NavDrawerItem(int idItem, String icono_p, String titulo){
		this.idItem = idItem;
		this.icono_p = icono_p;
		this.titulo = titulo;
	}

	public int getIdItem() {
		return idItem;
	}

	public int getIcono() {
		return icono;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getIcono_p() {
		return icono_p;
	}

	
}
