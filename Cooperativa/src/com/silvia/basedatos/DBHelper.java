package com.silvia.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private static final String NOMBRE_DB = "db_duraznillo";
	private static final int VERSION_DB = 1;
	
	private static final String TABLE_CARGO = "CREATE TABLE cargo("+
															"idcargo Text primary key not null,"+
															"ocupacion Text not null,"+
															"salario Double not null,"+
															"descripcion Text,"+
															"estado Integer);";
	
	private static final String TABLE_MAQUINARIA = "CREATE TABLE maquinaria("+
															"idmaquinaria Text primarykey not null,"+
															"placa Text not null,"+
															"descripcion Text,"+
															"capacidad Double not null,"+
															"marca Text,"+
															"color Text,"+
															"imagen Text,"+
															"estado Integer);";
	
	private static final String TABLE_PERSONAL = "CREATE TABLE personal("+
															"idpersonal Text primary key not null,"+
															"ci Text not null,"+
															"nombre Text not null,"+
															"apellido Text,"+
															"direccion Text,"+
															"telefono Integer,"+
															"email Text,"+
															"fecha_nac Date,"+
															"fecha_ingreso Date,"+
															"imagen Text,"+
															"estado Integer,"+
															"idcargo Text not null,"+
															"idmaquinaria Text not null,"+
															"FOREIGN KEY (idcargo) REFERENCES cargo(idcargo),"+
															"FOREIGN KEY (idmaquinaria) REFERENCES maquinaria(idmaquinaria));";
	
	private static final String TABLE_USUARIO = "CREATE TABLE usuario("+
															"idusuario Text primary key not null,"+
															"usuario Text not null,"+
															"clave Text not null,"+
															"estado Integer not null,"+
															"idpersonal Text not null,"+
															"FOREIGN KEY (idpersonal) REFERENCES personal(idpersonal));";
	
	private static final String TABLE_CLIENTE = "CREATE TABLE cliente("+
															"idcliente Text primary key not null,"+
															"ci Text not null,"+
															"nombre Text not null,"+
															"apellido Text,"+
															"direccion Text,"+
															"telefono Integer,"+
															"email Text,"+
															"sexo Text,"+
															"fecha_nac Date,"+
															"fecha_reg Date,"+
															"imagen Text,"+
															"estado Integer);";
	
	private static final String TABLE_PRODUCTO = "CREATE TABLE producto("+
															"idprod Text primary key not null,"+
															"nombre_prod Text not null,"+
															"precio Double not null,"+
															"unidad Text,"+
															"descripcion Text,"+
															"imagen Text,"+
															"estado Integer);";
	
	private static final String TABLE_VENTA = "CREATE TABLE venta ("+
														    "idventa Text primary key not null,"+
														    "tipo_venta Integer,"+
														    "idusuario Text not null,"+
														    "idcliente Text not null,"+
														    "fecha_venta Date not null,"+
														    "hora_venta Time not null,"+
														    "idpersonal Text not null,"+
														    "direccion Text,"+
														    "costo_total Double not null,"+
														    "nota Text,"+
														    "FOREIGN KEY (idusuario) REFERENCES usuario(idusuario),"+
														    "FOREIGN KEY (idcliente) REFERENCES cliente(idcliente),"+
														    "FOREIGN KEY (idpersonal) REFERENCES personal(idpersonal));";
	
	private static final String TABLE_DETALLE_VENTA ="CREATE TABLE detalle_venta("+
															"iddetalle Text primary key not null,"+ 
															"idventa Text not null,"+ 
															"idproducto Text not null,"+ 
															"cantidad Integer,"+
															"costo_entrega Double,"+ 
															"costo Double,"+
															"FOREIGN KEY (idventa) REFERENCES venta(idventa),"+ 
															"FOREIGN KEY (idproducto) REFERENCES producto(idprod));";
	
	private static final String TABLE_PEDIDO = "CREATE TABLE pedido ("+
														    "idpedido Text primary key not null,"+
														    "idusuario Text not null,"+
														    "idcliente Text not null,"+
														    "fecha_pedido Date not null,"+
														    "hora_pedido Time not null,"+
														    "fecha_entrega Date not null,"+
														    "idpersonal Text not null,"+
														    "direccion Text,"+
														    "costo_total Double not null,"+
														    "nota Text,"+
														    "estado int,"+
														    "FOREIGN KEY (idusuario) REFERENCES usuario(idusuario),"+
														    "FOREIGN KEY (idcliente) REFERENCES cliente(idcliente),"+
														    "FOREIGN KEY (idpersonal) REFERENCES personal(idpersonal));";
												
	private static final String TABLE_DETALLE_PEDIDO ="CREATE TABLE detalle_pedido("+
															"iddetalle_pedido Text primary key not null,"+ 
															"idpedido Text not null,"+ 
															"idproducto Text not null,"+ 
															"cantidad Integer,"+
															"costo_entrega Double,"+ 
															"costo Double,"+
															"FOREIGN KEY (idpedido) REFERENCES pedido(idpedido),"+ 
															"FOREIGN KEY (idproducto) REFERENCES producto(idprod));";
	
	private static final String TABLE_COOPERATIVA = "CREATE TABLE cooperativa(" +
															"idcoop Text primary key not null," +
															"nombre Text not null," +
															"nit Text not null," +
															"telf_cel Text," +
															"email Text," +
															"direccion Text," +
															"ciudad Text);";
	
	public DBHelper(Context context) {
		super(context, NOMBRE_DB, null, VERSION_DB);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CARGO);
		db.execSQL(TABLE_MAQUINARIA);
		db.execSQL(TABLE_PERSONAL);
		db.execSQL(TABLE_USUARIO);
		db.execSQL(TABLE_CLIENTE);
		db.execSQL(TABLE_PRODUCTO);
		db.execSQL(TABLE_VENTA);
		db.execSQL(TABLE_DETALLE_VENTA);
		db.execSQL(TABLE_COOPERATIVA);
		db.execSQL(TABLE_PEDIDO);
		db.execSQL(TABLE_DETALLE_PEDIDO);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS cargo");
		db.execSQL(TABLE_CARGO);
		db.execSQL("DROP TABLE IF EXISTS maquinaria");
		db.execSQL(TABLE_MAQUINARIA);
		db.execSQL("DROP TABLE IF EXISTS personal");
		db.execSQL(TABLE_PERSONAL);
		db.execSQL("DROP TABLE IF EXISTS usuario");
		db.execSQL(TABLE_USUARIO);
		db.execSQL("DROP TABLE IF EXISTS cliente");
		db.execSQL(TABLE_CLIENTE);
		db.execSQL("DROP TABLE IF EXISTS producto");
		db.execSQL(TABLE_PRODUCTO);
		db.execSQL("DROP TABLE IF EXISTS venta");
		db.execSQL(TABLE_VENTA);
		db.execSQL("DROP TABLE IF EXISTS detalle_venta");
		db.execSQL(TABLE_DETALLE_VENTA);
		db.execSQL("DROP TABLE IF EXISTS cooperativa");
		db.execSQL(TABLE_COOPERATIVA);
		db.execSQL("DROP TABLE IF EXISTS pedido");
		db.execSQL(TABLE_PEDIDO);
		db.execSQL("DROP TABLE IF EXISTS detalle_pedido");
		db.execSQL(TABLE_DETALLE_PEDIDO);
	}

}
