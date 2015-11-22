package com.example.brasil2014fixture;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class DBFixture {

	private static final String N_DB="dbfixture";
	private static final int VERSION_DB=1;
	
	private DBHelper miHelper;
	private Context miContexto;
	private SQLiteDatabase miDB;
	
	//Esta parte sirve para abri y cerrar la base de datos
	
	public DBFixture(Context c){
		miContexto=c;
	}
	
	public DBFixture AbrirDB() throws Exception{
		miHelper=new DBHelper(miContexto);
		miDB=miHelper.getWritableDatabase();
		return this;
	}
	
	public void cerrarDB(){
		miDB.close();
	}
	
	/*---------------------------------Manipulacion tabla Grupo-------------------------------*/
	public void registrarGrupo(String grupo){
			String sql="INSERT INTO grupo (idgrupo) VALUES('"+grupo+"')";
			miDB.execSQL(sql);
	}
	
	public boolean verificarGrupo(String grupo){
		boolean control=false;
			Cursor c = miDB.rawQuery("SELECT idgrupo FROM grupo WHERE idgrupo='"+grupo+"'", null);
			if(c!=null){
				if(c.moveToFirst())
					control=true;
			}
		return control;
	}
	
	public String getGrupo(String grupo){
		String grup="";
		Cursor c = miDB.rawQuery("SELECT idgrupo,primero,segundo FROM grupo WHERE idgrupo='"+grupo+"'", null);

		if(c!=null){
			c.moveToFirst();
			grup=c.getString(0)+" - "+c.getInt(1)+" - "+c.getInt(2);
			return grup;
		}	
		return grup;
	}
	
	public String[] getListaGrupos(){
		int cont1=0; int cont2=0;
		String sql="SELECT idgrupo FROM grupo";
		String sql2="SELECT COUNT(idgrupo) FROM grupo";
		Cursor c2=miDB.rawQuery(sql2, null);
		if(c2.moveToFirst()){
			cont1=c2.getInt(0);
		}
		
		String[] grupo=new String[cont1];
		Cursor c=miDB.rawQuery(sql, null);
		for (c.moveToFirst();!c.isAfterLast(); c.moveToNext()) {
			grupo[cont2]=c.getString(0);
			cont2++;
		}
		return grupo;
	}
	
	
	/*---------------------------------Manipulacion tabla equipo-------------------------------*/
	
	public void registrarEquipo(String pais, int imagen, String grupo){
		String sql="INSERT INTO equipo (pais,imagen,idgrupo) VALUES ('"+pais+"',"+imagen+",'"+grupo+"')";
		miDB.execSQL(sql);
	}
	
	public boolean verificarEquipo(String pais){
		boolean control=false;
		String SQL="SELECT idequipo FROM equipo WHERE pais='"+pais+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		if(c!=null){
			if(c.moveToFirst())
				control=true;
		}
		return control;
	}
	
	public String getEquipo(String pais){
		String detalleEquipo="";
		String SQL="SELECT pais,pg,pe,pp,gf,gc,idgrupo FROM equipo WHERE pais='"+pais+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		if(c!=null){
			c.moveToFirst();
			detalleEquipo=c.getString(0)+" - "+c.getInt(1)+" - "+c.getInt(2)+" - "+c.getInt(3)+" - "+c.getInt(4)+" - "+c.getInt(5)+" - "+c.getString(6);
			return detalleEquipo;
		}
		return detalleEquipo;
	}
	
	public Integer getImagenBandera(String pais){
		Integer imagen=0;
		String SQL="SELECT imagen FROM equipo WHERE pais='"+pais+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		if(c!=null){
			c.moveToFirst();
			imagen=c.getInt(0);
		}
		return imagen;
	}
	
	//Creacion de la clase sqliteopenhelper que crea la base de datos
	public static class DBHelper extends SQLiteOpenHelper{
		
		private static final String equipo="create table equipo("+
														"idequipo Integer primary key autoincrement not null,"+
														"pais varchar(30) not null,"+
														"pg Integer,"+
														"pe Integer,"+
														"pp Integer,"+
														"gf Integer,"+
														"gc Integer,"+
														"imagen Integer,"+
														"idgrupo varchar(1) not null"+
														");";
		private static final String grupo="create table grupo("+
														"idgrupo varchar(1) primary key not null,"+
														"primero Integer,"+
														"segundo Integer"+
														");";
		
		private static final String partido="create table partido("+
														  "idpartido Integer primary key autoincrement not null,"+
														  "equipo1 Integer not null,"+
														  "equipo2 Integer not null,"+
														  "goles1 Integer,"+
														  "goles2 Integer,"+
														  "hora time,"+
														  "fecha date,"+
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
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS equipo");
			db.execSQL("DROP TABLE IF EXISTS grupo");
			db.execSQL("DROP TABLE IF EXISTS partido");
			
			db.execSQL(equipo);
			db.execSQL(grupo);
			db.execSQL(partido);
			
		}
	}
	
	
}
