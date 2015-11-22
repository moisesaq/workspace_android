package com.moises.modelo;

public class EquipoOctavos {

	String idoctavos,pais,grupo;
	int imagen;
	
	public EquipoOctavos(String idoctavos, String pais, int imagen,String grupo){
		this.idoctavos=idoctavos;
		this.pais=pais;
		this.imagen=imagen;
		this.grupo=grupo;
	}

	public String getIdoctavos() {
		return idoctavos;
	}

	public void setIdoctavos(String idoctavos) {
		this.idoctavos = idoctavos;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

}
