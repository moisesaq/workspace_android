package com.moisse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final int VERSION_DATA_BASE = 1;
	private static final String NOMBRE_DATA_BASE = "DB_VEHICULOS";
	
	private static final String tabla_vehiculo = "create table vehiculo("+
															   "placa Text primary key not null,"+
															   "marca Text not null,"+
															   "color Text not null,"+
															   "imagen Text not null,"+
															   "estado Integer not null);";
	
	public DBHelper(Context contexto){
		super(contexto, NOMBRE_DATA_BASE, null, VERSION_DATA_BASE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(tabla_vehiculo);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS vehiculo");
		db.execSQL(tabla_vehiculo);
	}

}
