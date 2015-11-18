package com.moises.connectiontoserver;

public class ItemMenu {

	int codigo, icono;
	String titulo;
	
	public ItemMenu(int codigo, int icono, String titulo){
		this.codigo = codigo;
		this.icono = icono;
		this.titulo = titulo;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
