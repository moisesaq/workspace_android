package com.moisse.database;

import java.util.ArrayList;
import java.util.List;

import com.moisse.modelo.Vehiculo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBVehiculo {

	private DBHelper miHelper;
	private SQLiteDatabase miDB;
	private Context contexto;
	
	public DBVehiculo(Context contexto){
		this.contexto = contexto;
	}
	
	public DBVehiculo openDB(){
		try{
			miHelper = new DBHelper(contexto);
			miDB = miHelper.getWritableDatabase();
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public void closeDB(){
		miDB.close();
	}
	
	//----------------------------------MANIPULANDO TABLA VEHICULO----------------------------------
	
	public boolean insertarVehiculo(Vehiculo v){
		String query = "INSERT INTO vehiculo(placa, marca, color, imagen, estado) " +
				"VALUES ('"+v.getPlaca()+"','"+v.getMarca()+"','"+v.getColor()+"','"+v.getImagen()+"',"+v.getEstado()+")";
		miDB.execSQL(query);
		return verificarVehiculo(v);
	}
	
	public boolean actualizarVehiculo(Vehiculo v){
		String query = "UPDATE vehiculo SET marca='"+v.getMarca()+"', color='"+v.getColor()+"', imagen='"+v.getImagen()+"' " +
				"WHERE placa='"+v.getPlaca()+"'";
		miDB.execSQL(query);
		return verificarVehiculo(v);
	}
	
	public boolean verificarVehiculo(Vehiculo v){
		String query = "SELECT * FROM vehiculo WHERE placa='"+v.getPlaca()+"'";
		Cursor c = miDB.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Vehiculo buscarVehiculo(String placa){
		Vehiculo vehiculo = null;
		Cursor c = miDB.rawQuery("SELECT * FROM vehiculo WHERE placa='"+placa+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				vehiculo = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4));
			}
		}
		return vehiculo;
	}
	
	public List<Vehiculo> getAllVehiculo(){
		List<Vehiculo> lista_vehiculos = new ArrayList<Vehiculo>();
		Cursor c = miDB.rawQuery("SELECT * FROM vehiculo", null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			Vehiculo vehiculo = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4));
			lista_vehiculos.add(vehiculo);
		}
		return lista_vehiculos;
	}
}
