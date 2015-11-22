package com.moises.modelo;

public class Equipo {
	int id,imagen,pg,pe,pp,gf,gc,pts;
	String pais,grupo;
	
	public Equipo(int id, String pais,int pg, int pe, int pp, int gf, int gc,int pts,int imagen,String grupo){
		this.id=id;
		this.pais=pais;
		this.pg=pg;
		this.pe=pe;
		this.pp=pp;
		this.gf=gf;
		this.gc=gc;
		this.pts=pts;
		this.imagen=imagen;
		this.grupo=grupo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
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

	public int getPg() {
		return pg;
	}

	public void setPg(int pg) {
		this.pg = pg;
	}

	public int getPe() {
		return pe;
	}

	public void setPe(int pe) {
		this.pe = pe;
	}

	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getGf() {
		return gf;
	}

	public void setGf(int gf) {
		this.gf = gf;
	}

	public int getGc() {
		return gc;
	}

	public void setGc(int gc) {
		this.gc = gc;
	}

	public int getPts() {
		return pts;
	}

	public void setPts(int pts) {
		this.pts = pts;
	}
	
}
