package com.moisse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	
	final static String NOMBRE_DB = "db_parqueo";
	final static int VERSION = 2;
	
	private static final String tabla_parqueo = "CREATE TABLE parqueo("+
															  "idparqueo Text primary key not null,"+
															  "nombre_parqueo Text not null,"+
															  "telf Integer,"+
															  "direccion Text,"+
															  "tolerancia Integer not null,"+
															  "capacidad Integer not null," +
															  "precioHoraDia Double not null," +
															  "precioNoche Double not null," +
															  "precioContratoNocturno Double," +
															  "precioContratoDiurno Double," +
															  "precioContratoDiaCompleto Double," +
															  "inicioDia Time," +
															  "finDia Time," +
															  "inicioNoche Time," +
															  "finNoche Time," +
															  "logo Text);";
	
	private static final String tabla_usuario = "CREATE TABLE usuario("+
															  "idusuario Text primary key not null,"+
															  "ci Text not null,"+
															  "nombre Text not null,"+
															  "apellido Text,"+
															  "nombre_usuario Text not null,"+
															  "clave Text not null,"+
															  "celular Integer,"+
															  "direccion Text,"+
															  "email Text,"+
															  "sexo Text,"+
															  "fecha_nac Date,"+
															  "cargo Integer not null,"+
															  "estado Integer not null,"+
															  "imagen Text,"+
															  "idparqueo Text not null,"+
															  "FOREIGN KEY (idparqueo) REFERENCES parqueo(idparqueo));";
	
	private static final String tabla_carril = "CREATE TABLE carril("+
															  "idcarril Text primary key not null,"+
															  "num_carril Integer not null,"+
															  "disponible Text not null,"+
															  "reservado Text not null,"+
															  "estado Integer not null,"+
															  "idparqueo Text not null,"+
															  "FOREIGN KEY (idparqueo) REFERENCES parqueo(idparqueo));";
	
	private static final String tabla_cliente = "CREATE TABLE cliente("+
															  "idcliente Text primary key not null,"+
															  "ci Text not null,"+
															  "nombre Text not null,"+
															  "apellido Text,"+
															  "celular Integer,"+
															  "direccion Text,"+
															  "email Text,"+
															  "sexo Text,"+
															  "fecha_nac Date,"+
															  "imagen Text,"+
															  "estado Integer not null,"+
															  "tipo Integer not null,"+
															  "fecha_contrato Date,"+
															  "idparqueo Text not null,"+
															  "FOREIGN KEY (idparqueo) REFERENCES parqueo(idparqueo));";
	
	private static final String tabla_vehiculo = "CREATE TABLE vehiculo("+
															  "idvehiculo Text primary key not null,"+
															  "placa Text not null,"+
															  "marca Text,"+
															  "color Text,"+
															  "imagen Text,"+
															  "estado Integer,"+
															  "idcliente Text not null,"+
															  "FOREIGN KEY (idcliente) REFERENCES cliente(idcliente));";
	
	private static final String tabla_resguardo = "CREATE TABLE resguardo(" +
																"idresguardo Text primary key not null,"+
																"horaE Time not null," +
																"fechaE Date not null,"+
																"horaS Time," +
																"fechaS Date,"+
																"costoDia Double,"+
																"costoNoche Double,"+
																"costoTotal Double,"+
																"estado Integer," +
																"nota Text,"+
																"tipo Integer,"+
																"idcarril Text not null,"+
																"idvehiculo Text not null,"+
																"FOREIGN KEY (idcarril) REFERENCES carril(idcarril),"+
																"FOREIGN KEY (idvehiculo) REFERENCES vehiculo(idvehiculo));";
	
	public DBHelper(Context context) {
		super(context, NOMBRE_DB, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tabla_parqueo);
		db.execSQL(tabla_usuario);
		db.execSQL(tabla_carril);
		db.execSQL(tabla_cliente);
		db.execSQL(tabla_vehiculo);
		db.execSQL(tabla_resguardo);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS parqueo");
		db.execSQL(tabla_parqueo);
		db.execSQL("DROP TABLE IF EXISTS usuario");
		db.execSQL(tabla_usuario);
		db.execSQL("DROP TABLE IF EXISTS carril");
		db.execSQL(tabla_carril);
		db.execSQL("DROP TABLE IF EXISTS cliente");
		db.execSQL(tabla_cliente);
		db.execSQL("DROP TABLE IF EXISTS vehiculo");
		db.execSQL(tabla_vehiculo);
		db.execSQL("DROP TABLE IF EXISTS resguardo");
		db.execSQL(tabla_resguardo);
	}
	
}
