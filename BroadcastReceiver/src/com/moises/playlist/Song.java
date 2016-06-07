package com.moises.playlist;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Song implements Serializable{

	private String titulo, pathFile;

	public String getTitulo() {
		return titulo;
	}

	public Song setTitulo(String titulo) {
		this.titulo = titulo;
		return this;
	}

	public String getPathFile() {
		return pathFile;
	}

	public Song setPathFile(String pathFile) {
		this.pathFile = pathFile;
		return this;
	}
	
	
}
