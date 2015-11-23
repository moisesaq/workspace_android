package com.silvia.modelo;

public class ItemMiMenu {

	private int idmenu, icono;
	private String titulo, descripcion, icono_personalizado;
	
	public ItemMiMenu(int idmenu, int icono, String titulo, String descripcion){
		this.idmenu = idmenu;
		this.icono = icono;
		this.titulo = titulo;
		this.descripcion = descripcion;
	}
	
	public ItemMiMenu(int idmenu, String icono_personalizado, String titulo, String descripcion){
		this.idmenu = idmenu;
		this.icono_personalizado = icono_personalizado;
		this.titulo = titulo;
		this.descripcion = descripcion;
	}

	public int getIdmenu() {
		return idmenu;
	}

	public int getIcono() {
		return icono;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getIcono_personalizado() {
		return icono_personalizado;
	}
	
	public String getDescripcion(){
		return descripcion;
	}
	
}
