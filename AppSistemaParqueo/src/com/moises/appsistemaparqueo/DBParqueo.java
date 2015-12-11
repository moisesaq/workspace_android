package com.moises.appsistemaparqueo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBParqueo {

	private static final String N_DB = "DBSistemaParqueo";
	private static final int VERSION_DB = 2;
	
	private DBHelper miHelper;
	private final Context miContexto;
	private SQLiteDatabase miDB;
	
	public static class DBHelper extends SQLiteOpenHelper{
		
		String tablaClientes = "CREATE TABLE CLIENTE ("+
										"placa VARCHAR(10) NOT NULL ,"+
										"modelo VARCHAR(35) NULL ,"+
										"color VARCHAR(30) NULL ,"+
										"nombre VARCHAR(20) NOT NULL ,"+
										"apellidos VARCHAR(25) NOT NULL ,"+
										"ci INTEGER NOT NULL ,"+
										"celular INTEGER NOT NULL ,"+
										"imagen VARCHAR(35) NULL ,"+
										"PRIMARY KEY(placa)"+
										");";
		
		String tablaCarriles = "CREATE TABLE CARRILES ("+
								   		"numerocarril INTEGER NOT NULL ,"+
								   		"disponible VARCHAR(5) NOT NULL ,"+
								   		"PRIMARY KEY(numerocarril)"+
								   		");";
		
		String tablaRegistro = "CREATE TABLE REGISTRARENTRADASALIDA ("+
								   		"idregistro INTEGER PRIMARY KEY AUTOINCREMENT,"+
								   		"fecha VARCHAR(20) NOT NULL ,"+
								   		"hora VARCHAR(20) NOT NULL ,"+
								   		"estado VARCHAR(15) NOT NULL ,"+
								   		"cliente_placa VARCHAR(10) NOT NULL ,"+
								   		"carriles_numerocarril INTEGER NOT NULL ,"+
								   		"FOREIGN KEY (cliente_placa) REFERENCES REGISTRARENTRADASALIDA(cliente_placa),"+
								   		"FOREIGN KEY (carriles_numerocarril) REFERENCES REGISTRARENTRADASALIDA(carriles_numerocarril)"+
										");";

		public DBHelper(Context context) {
			super(context, N_DB, null, VERSION_DB);
		}			

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(tablaClientes);
			db.execSQL(tablaCarriles);
			db.execSQL(tablaRegistro);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("DROP TABLE IF EXISTS CLIENTE");
			db.execSQL("DROP TABLE IF EXISTS CARRILES");
			db.execSQL("DROP TABLE IF EXISTS REGISTRARENTRADASALIDA");
			
			db.execSQL(tablaClientes);
			db.execSQL(tablaCarriles);
			db.execSQL(tablaRegistro);
			
		}
	}
	
	public DBParqueo(Context c){
		miContexto=c;
	}
	
	public DBParqueo abrir() throws Exception{
		miHelper = new DBHelper(miContexto);
		miDB = miHelper.getWritableDatabase();
		return this;
	}

	public void cerrar() {
		miDB.close();
	}
	
	public long NuevoCliente(String placa, String modelo, String color,
			String nombre, String apellidos, int cI, int celular, String imagen) {
		ContentValues cv = new ContentValues();
		
		cv.put("placa", placa);
		cv.put("modelo", modelo);
		cv.put("color", color);
		cv.put("nombre", nombre);
		cv.put("apellidos", apellidos);
		cv.put("ci", cI);
		cv.put("celular", celular);
		cv.put("imagen", imagen);
		return miDB.insert("CLIENTE", null, cv);
		
	}
	
	public String DatosCliente() {
		String[] columnas = new String[]{"placa","nombre","apellidos","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, null, null, null, null, null);
		String result = "";
		
		int iPlaca = c.getColumnIndex("placa");
		int iNombre = c.getColumnIndex("nombre");
		int iApellidos = c.getColumnIndex("apellidos");
		int iImagen = c.getColumnIndex("imagen");
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iPlaca)+"  "+c.getString(iNombre)+"  "+c.getString(iApellidos)+"  "+c.getString(iImagen)+"\n";
		}
		
		return result;
	}
	
	public String[] ListaClientes() {
		
		int cont=0; int cont2=0;
		
		String[] columnas = new String[]{"placa","nombre","apellidos"};
		Cursor c = miDB.query("CLIENTE", columnas, null, null, null, null, null);
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			cont2++;
		}
		String[] Resultado = new String[cont2];
		
		
		int iPlaca = c.getColumnIndex("placa");
		int iNombre = c.getColumnIndex("nombre");
		int iApellidos = c.getColumnIndex("apellidos");
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Resultado[cont] = c.getString(iPlaca)+"  "+c.getString(iNombre)+"  "+c.getString(iApellidos);
			cont=cont+1;
		}
		return Resultado;
	}

	public long InsertarCarril(int carri, String opc) {
		ContentValues cv1 = new ContentValues();
		cv1.put("numerocarril", carri);
		cv1.put("disponible", opc);
		
		return miDB.insert("CARRILES", null, cv1);
	}

	public String[] ListaCarriles() {
		int cont = 0; int cantCarriles = 0;
		
		String[] columnas1 = new String[]{"numerocarril","disponible"};
		Cursor cu = miDB.query("CARRILES", columnas1, null, null, null, null, null);
		
		for(cu.moveToFirst();!cu.isAfterLast();cu.moveToNext()){
			cantCarriles++;
		}
		String[] Result=new String[cantCarriles];
		
		int iCarril = cu.getColumnIndex("numerocarril");
		int iDisponible = cu.getColumnIndex("disponible");
		
		for(cu.moveToFirst();!cu.isAfterLast();cu.moveToNext()){
			Result[cont] = cu.getString(iCarril)+"  "+cu.getString(iDisponible);
			cont++;
		}
		return Result;
	}

	public String getModelo(String buscarPlaca) {
		String[] columnas2 = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas2, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c!=null){
			c.moveToFirst();
			String mod = c.getString(1);
			return mod;
		}
		
		return null;
	}

	public String getColorCoche(String buscarPlaca) {
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String color = c.getString(2);
			return color;
		}
		return null;
	}

	public String getNombre(String buscarPlaca) {
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String nom = c.getString(3);
			return nom;
		}
		return "no";
	}

	public String getApellido(String buscarPlaca) {
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String ape = c.getString(4);
			return ape;
		}
		return null;
	}

	public int getCI(String buscarPlaca) {
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c != null){
			c.moveToFirst();
			int cIdent = Integer.parseInt(c.getString(5));
			return cIdent;
		}
		return 0;
	}

	public int getMovil(String buscarPlaca) {
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c != null){
			c.moveToFirst();
			int movil = Integer.parseInt(c.getString(6));
			return movil;
		}
		return 0;
	}

	public int getImagen(String buscarPlaca) {
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+buscarPlaca+"'", null, null, null, null);
		if(c != null){
			c.moveToFirst();
			int image = Integer.parseInt(c.getString(7));
			return image;
		}
		return 0;
	}

	public boolean getExisteCliente(String datoB) {
		int cont=0;
		String[] columnas = new String[]{"placa","modelo","color","nombre","apellidos","ci","celular","imagen"};
		Cursor c = miDB.query("CLIENTE", columnas, "placa"+"='"+datoB+"'", null, null, null, null);

		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			cont++;
		}
		if(cont==0){
			return false;
		}else{
			return true;
		}
		
		
	}

	public void ActualizarCarril(int posCarril, String disp) {
		ContentValues cvAct = new ContentValues();
		cvAct.put("numerocarril", posCarril);
		cvAct.put("disponible",disp);
		miDB.update("CARRILES", cvAct, "numerocarril ="+posCarril, null);
	}

	public boolean getDisponible(int i) {
		String[] columnas = new String[]{"numerocarril","disponible"};
		Cursor c = miDB.query("CARRILES", columnas, "numerocarril = "+i, null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String disp = c.getString(1);
			if(disp.equals("si"))
				return true;
			else
				return false;
		}
		return false;
	}

	public long registrarEntradaSalida(String fecha, String hora,
			String estado, String placa, int posCarril) {
		
		ContentValues cvR = new ContentValues();
		cvR.put("fecha", fecha);
		cvR.put("hora", hora);
		cvR.put("estado",estado);
		cvR.put("cliente_placa",placa);
		cvR.put("carriles_numerocarril", posCarril);
		
		return miDB.insert("REGISTRARENTRADASALIDA", null,cvR);		
	}

	public String[] getReporte() {
		
		int cont=0; int cont2=0;
		
		String[] columnas = new String[]{"idregistro","fecha","hora","estado","cliente_placa","carriles_numerocarril"};
		Cursor c = miDB.query("REGISTRARENTRADASALIDA", columnas, null, null, null, null, null);
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			cont2++;
		}
		String[] reporte= new String[cont2];
		
		int iID = c.getColumnIndex("idregistro");
		int iFecha = c.getColumnIndex("fecha");
		int iHora = c.getColumnIndex("hora");
		int iEstado = c.getColumnIndex("estado");
		int iPlaca = c.getColumnIndex("cliente_placa");
		int iCarril = c.getColumnIndex("carriles_numerocarril");
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			reporte[cont] = c.getString(iID)+"  "+c.getString(iFecha)+"  "+c.getString(iHora)+"  "+c.getString(iEstado)+
					  "  "+c.getString(iPlaca)+"  "+c.getString(iCarril);
			cont++;
		}
		return reporte;
	}

	public Integer[] getCarrilesOcupados() {
		
		 int cont=0; int cantOcupados=0;
		String[] columnas = new String[]{"numerocarril","disponible"};
		Cursor c = miDB.query("CARRILES", columnas, "disponible = 'no'", null, null, null, null);
		
		int iNumCarril = c.getColumnIndex("numerocarril");
		int iDisp = c.getColumnIndex("disponible");
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			cantOcupados++;
		}
		
		Integer[] COcupados = new Integer[cantOcupados];
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			COcupados[cont]=Integer.parseInt(c.getString(iNumCarril));
			cont++;
		}
		return COcupados;
	}

	public String getPlacaRegistro(Integer numcarril) {
		String[] columnas = new String[]{"idregistro","fecha","hora","estado","cliente_placa","carriles_numerocarril"};
		Cursor c = miDB.query("REGISTRARENTRADASALIDA", columnas, "carriles_numerocarril = "+numcarril, null, null, null, null);
		if(c!=null){
			c.moveToLast();
			String placa = c.getString(4);
			return placa;
		}
		return null;
	}

	
	public int getCarrilOcupaba(String placaS) {
		String[] columnas = new String[]{"idregistro","fecha","hora","estado","cliente_placa","carriles_numerocarril"};
		Cursor c = miDB.query("REGISTRARENTRADASALIDA", columnas, "cliente_placa = '"+placaS+"' AND estado = 'entrada'", null, null, null, null);
		if(c!=null){
			c.moveToLast();
			int carril = c.getInt(5);
			return carril;
		}
		return 0;
	}

	public int getCantidadCarriles(){
		
		int cantCarriles = 0;
		String[] columnas1 = new String[]{"numerocarril","disponible"};
		Cursor cu = miDB.query("CARRILES", columnas1, null, null, null, null, null);
		
		for(cu.moveToFirst();!cu.isAfterLast();cu.moveToNext()){
			cantCarriles++;
		}
		return cantCarriles;
	}

	public void EditarCliente(String placa, String modelo, String color,
			String nombre, String apellidos, int cI, int celular, String imagen) {
		ContentValues cv = new ContentValues();
		
		cv.put("placa", placa);
		cv.put("modelo", modelo);
		cv.put("color", color);
		cv.put("nombre", nombre);
		cv.put("apellidos", apellidos);
		cv.put("ci", cI);
		cv.put("celular", celular);
		cv.put("imagen", imagen);
		miDB.update("CLIENTE", cv, "placa = '"+placa+"'", null);
	}

	public void eliminarCliente(String buscarPlaca) {
		miDB.delete("CLIENTE", "placa = '"+buscarPlaca+"'", null);
		
	}
	


}
