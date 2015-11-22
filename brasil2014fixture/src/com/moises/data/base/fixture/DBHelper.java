package com.moises.data.base.fixture;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String N_DB="dbfixture";
	private static final int VERSION_DB=1;

	private static final String equipo="create table equipo("+
													"idequipo Integer primary key autoincrement not null,"+
													"pais varchar(30) not null,"+
													"pg Integer,"+
													"pe Integer,"+
													"pp Integer,"+
													"gf Integer,"+
													"gc Integer,"+
													"pts Integer,"+
													"imagen Integer,"+
													"idgrupo varchar(1) not null"+
													");";
	private static final String grupo="create table grupo("+
													"idgrupo varchar(1) primary key not null,"+
													"primero varchar(30),"+
													"segundo varchar(30)"+
													");";
	
	private static final String partido="create table partido("+
													  "idpartido Integer primary key autoincrement not null,"+
													  "pais1 varchar(30) not null,"+
													  "pais2 varchar(30) not null,"+
													  "goles1 Integer,"+
													  "goles2 Integer,"+
													  "hora Time,"+
													  "fecha Date,"+
													  "grupo varchar(1) not null,"+
													  "estado Integer,"+
													  "recordar Integer"+
													  ");";
	
	private static final String usuario="create table usuario("+
													  "idusuario Integer primary key autoincrement not null,"+
													  "nombre varchar(30) not null"+
													  ");";
	private static final String octavos="create table octavos("+
													  "idoctavos varchar(3) primary key not null,"+
													  "pais varchar(30) not null,"+
													  "imagen Integer,"+
													  "grupo varchar(1) not null"+
													  ");";
	
	private static final String partidooctavos="create table partidooctavos("+
															  "idpartidooctavos varchar(3) primary key not null,"+
															  "idoctavos1 varchar(3) not null,"+
															  "pais1 varchar(30) not null,"+
															  "gol1 Integer,"+
															  "gol2 Integer,"+
															  "pais2 varchar(30) not null,"+
															  "idoctavos2 varchar(3) not null,"+
															  "fecha Date,"+
															  "hora Time,"+															  
															  "estado Integer"+
															  ");";
	
	private static final String partidocuartos="create table partidocuartos("+
															  "idpartidocuartos varchar(3) primary key not null,"+
															  "idganador1 varchar(3) not null,"+
															  "bandera1 Integer,"+
															  "paisganador1 varchar(30) not null,"+
															  "gol1 Integer,"+
															  "gol2 Integer,"+
															  "bandera2 Integer,"+
															  "paisganador2 varchar(30) not null,"+
															  "idganador2 varchar(3) not null,"+
															  "fecha Date,"+
															  "hora Time,"+															  
															  "estado Integer"+
															  ");";
	
	private static final String partidosemis="create table partidosemis("+
															  "idpartidosemis varchar(3) primary key not null,"+
															  "idganador1 varchar(3) not null,"+
															  "bandera1 Integer,"+
															  "paisganador1 varchar(30) not null,"+
															  "gol1 Integer,"+
															  "gol2 Integer,"+
															  "bandera2 Integer,"+
															  "paisganador2 varchar(30) not null,"+
															  "idganador2 varchar(3) not null,"+
															  "fecha Date,"+
															  "hora Time,"+															  
															  "estado Integer"+
															  ");";
	
	private static final String partidofinales="create table partidofinales("+
															  "idpartidofinales varchar(3) primary key not null,"+
															  "idganador1 varchar(3) not null,"+
															  "bandera1 Integer,"+
															  "paisganador1 varchar(30) not null,"+
															  "gol1 Integer,"+
															  "gol2 Integer,"+
															  "bandera2 Integer,"+
															  "paisganador2 varchar(30) not null,"+
															  "idganador2 varchar(3) not null,"+
															  "fecha Date,"+
															  "hora Time,"+															  
															  "estado Integer"+
															  ");";
	
	public DBHelper(Context contexto){
	super(contexto,N_DB,null,VERSION_DB);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	db.execSQL(equipo);
	db.execSQL(grupo);
	db.execSQL(partido);
	db.execSQL(usuario);
	db.execSQL(octavos);
	db.execSQL(partidooctavos);
	db.execSQL(partidocuartos);
	db.execSQL(partidosemis);
	db.execSQL(partidofinales);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS equipo");
	db.execSQL("DROP TABLE IF EXISTS grupo");
	db.execSQL("DROP TABLE IF EXISTS partido");
	db.execSQL("DROP TABLE IF EXISTS usuario");
	db.execSQL("DROP TABLE IF EXISTS octavos");
	db.execSQL("DROP TABLE IF EXISTS partidooctavos");
	db.execSQL("DROP TABLE IF EXISTS partidocuartos");
	db.execSQL("DROP TABLE IF EXISTS partidosemis");
	db.execSQL("DROP TABLE IF EXISTS partidofinales");
	
	db.execSQL(equipo);
	db.execSQL(grupo);
	db.execSQL(partido);
	db.execSQL(usuario);
	db.execSQL(octavos);
	db.execSQL(partidooctavos);
	db.execSQL(partidocuartos);
	db.execSQL(partidosemis);
	db.execSQL(partidofinales);
	
	}
}
