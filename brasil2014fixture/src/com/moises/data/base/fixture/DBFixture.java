package com.moises.data.base.fixture;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import com.example.brasil2014fixture.R;
import com.moises.modelo.Equipo;
import com.moises.modelo.EquipoOctavos;
import com.moises.modelo.Partido;
import com.moises.modelo.PartidoCuartos;
import com.moises.modelo.PartidoFinales;
import com.moises.modelo.PartidoOctavos;
import com.moises.modelo.PartidoSemis;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

public class DBFixture {

	private DBHelper miHelper;
	private Context miContexto;
	private SQLiteDatabase miDB;
	
	//--------------------Esta parte sirve para abrir y cerrar la base de datos-----------
	
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
	
	/*///////////////--------------------------------TABLA GRUPOS-------------------------------//////////////////*/
	
	public void registrarGrupo(String grupo){
			String sql="INSERT INTO grupo (idgrupo) VALUES('"+grupo+"')";
			miDB.execSQL(sql);
	}
	
	public int getCantidadGrupo(){
		int cont=0;
		String sql="SELECT COUNT(idgrupo) FROM grupo";
		Cursor c = miDB.rawQuery(sql, null);
		if(c.moveToFirst()){
			cont=c.getInt(0);
		}
		return cont;
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
			grup=c.getString(0)+" - "+c.getString(1)+" - "+c.getString(2);
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
	
	
	/*///////////////////////---------------------------------TABLA EQUIPO-------------------------------////////////////////////*/
	
	public void registrarEquipo(String pais, int imagen, String grupo){
		String sql="INSERT INTO equipo (pais,pg,pe,pp,gf,gc,pts,imagen,idgrupo) VALUES "
				+ "('"+pais+"',"+0+","+0+","+0+","+0+","+0+","+0+","+imagen+",'"+grupo+"')";
		miDB.execSQL(sql);
	}
	
	public void actualizarEquipo(String p, int pg1, int pe1, int pp1, int gf1, int gc1, int pts1){
//		Toast toast=Toast.makeText(miContexto,"Llego: "+p+"-"+pg1+"-"+pe1+"-"+pp1+"-"+gf1+"-"+gc1+"-"+pts1, Toast.LENGTH_LONG);
//		toast.show();
		
		String sql1="UPDATE equipo SET pg=pg+"+pg1+", pe=pe+"+pe1+", pp=pp+"+pp1+", gf=gf+"+gf1+", gc=gc+"+gc1+", pts=pts+"+pts1+" WHERE pais='"+p+"'";
		miDB.execSQL(sql1);
		
	}
	public void updateEquipo(String pais1, String pais2, int gol1, int gol2){
		
		if(gol1>gol2){
			Toast toast1=Toast.makeText(miContexto,"Gano :"+pais1, Toast.LENGTH_LONG);
			toast1.show();
			actualizarEquipo(pais1, 1, 0, 0, gol1, gol2, 3);
			actualizarEquipo(pais2, 0, 0, 1, gol2, gol1, 0);
		}else if (gol2>gol1) {
			Toast toast1=Toast.makeText(miContexto,"Gano :"+pais2, Toast.LENGTH_LONG);
			toast1.show();
			actualizarEquipo(pais2, 1, 0, 0, gol2, gol1, 3);
			actualizarEquipo(pais1, 0, 0, 1, gol1, gol2, 0);
		}else {
			Toast toast1=Toast.makeText(miContexto,"Empate "+pais1+" contra "+pais2, Toast.LENGTH_LONG);
			toast1.show();
			actualizarEquipo(pais2, 0, 1, 0, gol2, gol1, 1);
			actualizarEquipo(pais1, 0, 1, 0, gol1, gol2, 1);
		}
		
	}
	
	public void resetEquipos(String g){
		String sql1="UPDATE equipo SET pg=0, pe=0, pp=0, gf=0, gc=0, pts=0 WHERE idgrupo='"+g+"'";
		miDB.execSQL(sql1);
		
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
	
	public String[] getListaPaises(){
		int cont1=0; int cont2=0;
		String SQL1="SELECT pais FROM equipo";
		String SQL2="SELECT COUNT(pais) FROM equipo";
		
		Cursor c2=miDB.rawQuery(SQL2, null);
		if(c2.moveToFirst()){
			cont2=c2.getInt(0);
		}
		
		String[] listaPaises=new String[cont2];
		Cursor c1=miDB.rawQuery(SQL1, null);
		for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
			listaPaises[cont1]=c1.getString(0);
			cont1++;
		}
		return listaPaises;
	}
	public int getIDEquipo(String pais){
		int id=0;
		String sql="SELECT idequipo FROM equipo WHERE pais='"+pais+"'";
		Cursor c=miDB.rawQuery(sql, null);
		if(c.moveToFirst()){
			id=c.getInt(0);
		}
		return id;
	}
	
	public String getGrupoEquipo(String pais){
		String grupo ="";
		String sql="SELECT idgrupo FROM equipo WHERE pais='"+pais+"'";
		Cursor c=miDB.rawQuery(sql, null);
		if(c.moveToFirst()){
			grupo=c.getString(0);
		}
		return grupo;
	}
	
	public ArrayList<Equipo> getAdapterEquipo(){
		Equipo equipo;
		ArrayList<Equipo> arrayEquipo = new ArrayList<Equipo>();
		String SQL="SELECT idequipo,pais,pg,pe,pp,gf,gc,pts,imagen,idgrupo FROM equipo";
		Cursor c=miDB.rawQuery(SQL, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			equipo=new Equipo(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getString(9));
			arrayEquipo.add(equipo);
		}
		return arrayEquipo;
	}
	
	//-----------------------------------------------SACANDO POSICIONES-----------------------------------
	public void sacarPosicionesGrupos(String g){
		String primeroEquipo=""; String segundoEquipo=""; int cont=0; String pase1=""; String pase2="";
		String SQL="SELECT pais FROM equipo WHERE idgrupo='"+g+"' ORDER BY pts DESC, gf DESC";
		Cursor c=miDB.rawQuery(SQL, null);
		if(contarEstados(g)>=2){
			for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
				if(cont==0){
					primeroEquipo=c.getString(0);
					pase1="1"+g;
					cont++;
				}else if (cont==1) {
					segundoEquipo=c.getString(0);
					pase2="2"+g;
					cont++;
				}
			}
			String sql="UPDATE grupo SET primero='"+primeroEquipo+"', segundo='"+segundoEquipo+"' WHERE idgrupo='"+g+"'";
			miDB.execSQL(sql);
			actualizarOctavos(pase1, primeroEquipo, getImagenBandera(primeroEquipo), g);
			actualizarOctavos(pase2, segundoEquipo, getImagenBandera(segundoEquipo), g);
		}else{
			Toast toast=Toast.makeText(miContexto,"no hay puestos por que hay"+contarEstados(g)+" jugados", Toast.LENGTH_LONG);
			toast.show();
		}
		
	}
	
	public ArrayList<Equipo> getAdapterPosiciones(String g){
		Equipo equipo;
		ArrayList<Equipo> arrayEquipo = new ArrayList<Equipo>();
		String SQL="SELECT idequipo,pais,pg,pe,pp,gf,gc,pts,imagen,idgrupo FROM equipo WHERE idgrupo='"+g+"' ORDER BY pts DESC, gf DESC";
		Cursor c=miDB.rawQuery(SQL, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			
			equipo=new Equipo(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getString(9));
			arrayEquipo.add(equipo);
		}
		return arrayEquipo;
	}

	//-----------------------------------------ACTUALIZAR POSICIONES Y SACAR CLASIFICADOS DE LOS GRUPOS---------------------------
	public void ActualizarPosiciones(String g){
		resetEquipos(g);
		String SQL="SELECT pais1,pais2,goles1,goles2 FROM partido WHERE estado=1 AND grupo='"+g+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			updateEquipo(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3));
		}
		sacarPosicionesGrupos(g);
	}
	
	
	public int contarEstados(String g){
		int contar=0;
		String sql="SELECT COUNT(idpartido) FROM partido WHERE estado=1 AND grupo='"+g+"'";
		Cursor c=miDB.rawQuery(sql, null);
		if(c.moveToFirst()){
			contar=c.getInt(0);
		}
		return contar;
	}
	
	/*-------------------------------------------------TABLA PARTIDOS---------------------------------------*/
	
	public void registrarPartido(String pais1, String pais2, Time hora, Date fecha, String grupo){
		String sql="INSERT INTO partido(pais1, pais2,hora,fecha,grupo,estado) VALUES ('"+pais1+"','"+pais2+"','"+hora+"','"+fecha+"','"+grupo+"',"+0+")";
		miDB.execSQL(sql);
		
	}
	
	public void actualizarPartido(int idpartido,String pais1, String pais2, int gol1, int gol2){

		String SQL="UPDATE partido SET goles1="+gol1+", goles2="+gol2+", estado=1 WHERE idpartido="+idpartido;
		miDB.execSQL(SQL);
		
	}
	
	public boolean verificarPartido(String pais1, String pais2){
		boolean control=false;
		String SQL="SELECT idpartido FROM partido WHERE pais1='"+pais1+"' AND pais2='"+pais2+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		if(c!=null){
			if(c.moveToFirst())
				control=true;
		}
		return control;
	}
	
	public String getPartido(String pais1, String pais2){
		String detallePartido="";
		String SQL="SELECT idpartido,pais1,pais2,goles1,goles2,hora,fecha,grupo,estado FROM partido WHERE pais1='"+pais1+"' AND pais2='"+pais2+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		if(c!=null){
			c.moveToFirst();
			detallePartido=c.getInt(0)+" - "+c.getString(1)+" - "+c.getString(2)+" - "+c.getInt(3)+" - "+c.getInt(4)+" - "+c.getString(5)+" - "+c.getString(6)+" - "+c.getString(7)+" - "+c.getInt(8);
			return detallePartido;
		}
		return detallePartido;
	}
	
	public String[] getPartidoFecha(Date fecha){
		int cont=0; int cont1=0;
		String sql1="SELECT COUNT(idpartido) FROM partido WHERE fecha='"+fecha+"'";
		Cursor c1=miDB.rawQuery(sql1, null);
		if(c1.moveToFirst()){
			cont=c1.getInt(0);
		}
		String[] listaPartidos = new String[cont];
		String SQL="SELECT pais1,pais2,hora FROM partido WHERE fecha='"+fecha+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			listaPartidos[cont1]=c.getString(0)+" vs "+c.getString(1)+" - "+c.getString(2);
			cont1++;
			
		}
		return listaPartidos;
	}
	
	public ArrayList<Partido> getAdapterPatidosGrupo(String g){
		ActualizarPosiciones(g);
		Partido partido;
		ArrayList<Partido> lista=new ArrayList<Partido>();
		Toast toast=Toast.makeText(miContexto, "Grupo: "+g, Toast.LENGTH_SHORT);
		toast.show();
		String SQL="SELECT idpartido,pais1,pais2,goles1,goles2,hora,fecha,grupo,estado,recordar FROM partido WHERE grupo='"+g+"'";
		Cursor c=miDB.rawQuery(SQL, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {	
			partido=new Partido(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getInt(4),Time.valueOf(c.getString(5)),Date.valueOf(c.getString(6)),c.getString(7),c.getInt(8),c.getInt(9));
			lista.add(partido);
		}	
		return lista;
	}
	
	public ArrayList<Partido> getAdapterPatidosFecha(Date f){
		Partido partido;
		ArrayList<Partido> lista=new ArrayList<Partido>();
		String SQL="SELECT idpartido,pais1,pais2,goles1,goles2,hora,fecha,grupo,estado,recordar FROM partido WHERE fecha='"+f+"' AND estado=0";
		Cursor c=miDB.rawQuery(SQL, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			partido=new Partido(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getInt(4),Time.valueOf(c.getString(5)),Date.valueOf(c.getString(6)),c.getString(7),c.getInt(8),c.getInt(9));
			lista.add(partido);
		}	
		return lista;
	}
	
	
	//-----------------------------------------------------EQUIPOS OCTAVOS-----------------------------------
	
	public void registrarOctavos(String idoctavos, String pais, int imagen, String grupo){
		String sql="INSERT INTO octavos(idoctavos,pais,imagen,grupo) VALUES ('"+idoctavos+"','"+pais+"',"+imagen+",'"+grupo+"')";
		miDB.execSQL(sql);
	}
	
	public void actualizarOctavos(String idoctavos, String pais, int imagen, String g){
		actualizarPais1Partido(idoctavos, pais);
		actualizarPais2Partido(idoctavos, pais);
		String SQL="UPDATE octavos SET pais='"+pais+"', imagen="+imagen+" WHERE grupo='"+g+"' AND idoctavos='"+idoctavos+"'";
		miDB.execSQL(SQL);
	}
	
	public String buscarPaisOctavos(String idoct){
		String octavo="";
		String sql="SELECT pais FROM octavos WHERE idoctavos='"+idoct+"'";
		Cursor c=miDB.rawQuery(sql, null);
		if(c.moveToFirst()){
			octavo=c.getString(0);
		}
		return octavo;
	}
	
	public int buscarBanderaPaisOctavos(String idoct){
		int band=0;
		String sql="SELECT imagen FROM octavos WHERE idoctavos='"+idoct+"'";
		Cursor c=miDB.rawQuery(sql, null);
		if(c.moveToFirst()){
			band=c.getInt(0);
		}
		return band;
	}
	

	public String[] getEquiposOctavos(){
		int cont2=0;int cont=0;
		String sql2="SELECT COUNT(idoctavos) FROM octavos";
		String sql="SELECT idoctavos,pais,imagen,grupo FROM octavos";
		Cursor c2=miDB.rawQuery(sql2, null);
		if(c2.moveToFirst()){
			cont2=c2.getInt(0);
		}
		
		String[] listaoctavos= new String[cont2];
		Cursor c=miDB.rawQuery(sql, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			listaoctavos[cont]=c.getString(0)+"-"+c.getString(1)+"-"+c.getInt(2)+"-"+c.getString(3);
			cont++;
		}
		return listaoctavos;
	}
	
	public ArrayList<EquipoOctavos> getAdaptadorEquiposOctavos(){
		EquipoOctavos equipoO;
		ArrayList<EquipoOctavos> lista= new ArrayList<EquipoOctavos>();
		String sql="SELECT idoctavos,pais,imagen,grupo FROM octavos";
		Cursor c=miDB.rawQuery(sql, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			equipoO=new EquipoOctavos(c.getString(0), c.getString(1), c.getInt(2), c.getString(3));
			lista.add(equipoO);
		}
		return lista;
	}
	
	//--------------------------------------------------------PARTIDOS OCTAVOS-------------------------------------------------
	
	public void registrarPartidosOctavos(String idpartidooctavos, String idoct1,String idoct2, Date fecha, Time hora){
		String pais1=buscarPaisOctavos(idoct1);
		String pais2=buscarPaisOctavos(idoct2);
		String sql="INSERT INTO partidooctavos(idpartidooctavos,idoctavos1,pais1,pais2,idoctavos2,fecha,hora) VALUES"
					+ "('"+idpartidooctavos+"','"+idoct1+"','"+pais1+"','"+pais2+"','"+idoct2+"','"+fecha+"','"+hora+"')";
		miDB.execSQL(sql);
	}
	
	public String[] getPartidosOctavos(){
		int cont2=0;int cont=0;
		String sql2="SELECT COUNT(idpartidooctavos) FROM partidooctavos";
		String sql="SELECT idpartidooctavos,idoctavos1,pais1,gol1,gol2,pais2,idoctavos2,fecha,hora,estado FROM partidooctavos";
		Cursor c2=miDB.rawQuery(sql2, null);
		if(c2.moveToFirst()){
			cont2=c2.getInt(0);
		}
		
		String[] lista= new String[cont2];
		Cursor c=miDB.rawQuery(sql, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			lista[cont]=c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2)+"-"+c.getInt(3)+"-"+c.getInt(4)+"-"+c.getString(5)+"-"+c.getString(6)+"-"+c.getString(7)+"-"+c.getString(8)+"-"+c.getInt(9);
			cont++;
		}
		return lista;
	}
	
	public ArrayList<PartidoOctavos> getAdaptadorPartidosOctavos(){
		PartidoOctavos partidoO;
		ArrayList<PartidoOctavos> listaPartidosOctavos=new ArrayList<PartidoOctavos>();
		String sql2="SELECT COUNT(idpartidooctavos) FROM partidooctavos";
		String sql="SELECT idpartidooctavos,idoctavos1,pais1,gol1,gol2,pais2,idoctavos2,fecha,hora,estado FROM partidooctavos";
		
		Cursor c=miDB.rawQuery(sql, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			partidoO = new PartidoOctavos(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5), c.getString(6), Date.valueOf(c.getString(7)), Time.valueOf(c.getString(8)), c.getInt(9));
			listaPartidosOctavos.add(partidoO);
		}
		return listaPartidosOctavos;
	}
	
	public void actualizarPais1Partido(String idoct, String p1){
		String sql="UPDATE partidooctavos SET pais1='"+p1+"' WHERE idoctavos1='"+idoct+"'";
		miDB.execSQL(sql);
	}
	
	public void actualizarPais2Partido(String idoct, String p2){
		String sql="UPDATE partidooctavos SET pais2='"+p2+"' WHERE idoctavos2='"+idoct+"'";
		miDB.execSQL(sql);
	}
	
	public void actualizarPartidosOctavosGoles(String idpartidooctavos, int gol1, int gol2){
		String SQL="UPDATE partidooctavos SET gol1="+gol1+", gol2="+gol2+", estado=1 WHERE idpartidooctavos='"+idpartidooctavos+"'";
		miDB.execSQL(SQL);
		sacarEquiposParaCuartos();
	}
	
	public void sacarEquiposParaCuartos(){
		String sql="SELECT idpartidooctavos,pais1,gol1,gol2,pais2 FROM partidooctavos WHERE estado=1";
		Cursor c=miDB.rawQuery(sql, null);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			posicionarEquipoACuartos(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4));
		}
	}
		
	//------------------------------------------------------------PARTIDOS CUARTOS---------------------------------
	
		public void registrarPartidosCuartos(String idpartidocuartos, String idganador1,int bandera1,String paisganador1, int bandera2, String paisganador2,String idganador2, Date fecha, Time hora){

			String sql="INSERT INTO partidocuartos(idpartidocuartos,idganador1,bandera1,paisganador1,bandera2,paisganador2,idganador2,fecha,hora) VALUES"
						+ "('"+idpartidocuartos+"','"+idganador1+"',"+bandera1+",'"+paisganador1+"',"+bandera2+",'"+paisganador2+"','"+idganador2+"','"+fecha+"','"+hora+"')";
			miDB.execSQL(sql);
		}
		
		public void actualizarPartidoCuartos(String idganador, int bandera,String paisganador){
			String sql="UPDATE partidocuartos SET bandera1="+bandera+", paisganador1='"+paisganador+"' WHERE idganador1='"+idganador+"'";
			miDB.execSQL(sql);
			String sql1="UPDATE partidocuartos SET bandera2="+bandera+", paisganador2='"+paisganador+"' WHERE idganador2='"+idganador+"'";
			miDB.execSQL(sql1);
		}
		
		public void posicionarEquipoACuartos(String idpartidooctavos, String pais1,int gol1,int gol2,String pais2){
			int bandera1=getImagenBandera(pais1);
			int bandera2=getImagenBandera(pais2);
			String pase="G"+idpartidooctavos;
			if(gol1>gol2){
				Toast toast=Toast.makeText(miContexto, pais1+" paso a cuartos", Toast.LENGTH_LONG);
				toast.show();
				actualizarPartidoCuartos(pase, bandera1, pais1);
			}else if (gol2>gol1) {
				Toast toast=Toast.makeText(miContexto, pais2+" paso a cuartos", Toast.LENGTH_LONG);
				toast.show();
				actualizarPartidoCuartos(pase, bandera2, pais2);
			}else{
				Toast toast=Toast.makeText(miContexto, "Empate definicion por penales", Toast.LENGTH_LONG);
				toast.show();
			}
		}
		
		public String[] getPartidosCuartos(){
			int cont2=0;int cont=0;
			String sql2="SELECT COUNT(idpartidocuartos) FROM partidocuartos";
			String sql="SELECT idpartidocuartos,idganador1,paisganador1,gol1,gol2,paisganador2,idganador2,fecha,hora,estado FROM partidocuartos";
			Cursor c2=miDB.rawQuery(sql2, null);
			if(c2.moveToFirst()){
				cont2=c2.getInt(0);
			}
			
			String[] lista= new String[cont2];
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				lista[cont]=c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2)+"-"+c.getInt(3)+"-"+c.getInt(4)+"-"+c.getString(5)+"-"+c.getString(6)+"-"+c.getString(7)+"-"+c.getString(8)+"-"+c.getInt(9);
				cont++;
			}
			return lista;
		}
		
		public ArrayList<PartidoCuartos> getAdaptadorPartidosCuartos(){
			PartidoCuartos partidoC;
			ArrayList<PartidoCuartos> lista=new ArrayList<PartidoCuartos>();
			String sql="SELECT idpartidocuartos,idganador1,bandera1,paisganador1,gol1,gol2,bandera2,paisganador2,idganador2,fecha,hora,estado FROM partidocuartos";
			
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				partidoC = new PartidoCuartos(c.getString(0), c.getString(1),c.getInt(2), c.getString(3), c.getInt(4), c.getInt(5),c.getInt(6), c.getString(7), c.getString(8), Date.valueOf(c.getString(9)), Time.valueOf(c.getString(10)), c.getInt(11));
				lista.add(partidoC);
			}
			return lista;
		}
		
		public void actualizarPartidosCuartosGoles(String idpartidocuartos, int gol1, int gol2){
			String SQL="UPDATE partidocuartos SET gol1="+gol1+", gol2="+gol2+", estado=1 WHERE idpartidocuartos='"+idpartidocuartos+"'";
			miDB.execSQL(SQL);
			sacarEquiposParaSemis();
		}
		
		public void sacarEquiposParaSemis(){
			String sql="SELECT idpartidocuartos,paisganador1,gol1,gol2,paisganador2 FROM partidocuartos WHERE estado=1";
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				posicionarEquipoASemis(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4));
			}
		}
		
		//--------------------------------------------------PARTIDO SEMIS----------------------------------------------
		
		public void registrarPartidosSemis(String idpartidosemis, String idganador1,int bandera1,String paisganador1, int bandera2, String paisganador2,String idganador2, Date fecha, Time hora){

			String sql="INSERT INTO partidosemis(idpartidosemis,idganador1,bandera1,paisganador1,bandera2,paisganador2,idganador2,fecha,hora) VALUES"
						+ "('"+idpartidosemis+"','"+idganador1+"',"+bandera1+",'"+paisganador1+"',"+bandera2+",'"+paisganador2+"','"+idganador2+"','"+fecha+"','"+hora+"')";
			miDB.execSQL(sql);
		}
		
		public void actualizarPartidoSemis(String idganador, int bandera,String paisganador){
			String sql="UPDATE partidosemis SET bandera1="+bandera+", paisganador1='"+paisganador+"' WHERE idganador1='"+idganador+"'";
			miDB.execSQL(sql);
			String sql1="UPDATE partidosemis SET bandera2="+bandera+", paisganador2='"+paisganador+"' WHERE idganador2='"+idganador+"'";
			miDB.execSQL(sql1);
		}
		
		public void posicionarEquipoASemis(String idpartidosemis, String pais1,int gol1,int gol2,String pais2){
			int bandera1=getImagenBandera(pais1);
			int bandera2=getImagenBandera(pais2);
			String pase="G"+idpartidosemis;
			if(gol1>gol2){
				Toast toast=Toast.makeText(miContexto, pais1+" paso a semis", Toast.LENGTH_LONG);
				toast.show();
				actualizarPartidoSemis(pase, bandera1, pais1);
			}else if (gol2>gol1) {
				Toast toast=Toast.makeText(miContexto, pais2+" paso a semis", Toast.LENGTH_LONG);
				toast.show();
				actualizarPartidoSemis(pase, bandera2, pais2);
			}else{
				Toast toast=Toast.makeText(miContexto, "Empate definicion por penales", Toast.LENGTH_LONG);
				toast.show();
			}
		}
		
		public String[] getPartidosSemis(){
			int cont2=0;int cont=0;
			String sql2="SELECT COUNT(idpartidosemis) FROM partidosemis";
			String sql="SELECT idpartidosemis,idganador1,paisganador1,gol1,gol2,paisganador2,idganador2,fecha,hora,estado FROM partidosemis";
			Cursor c2=miDB.rawQuery(sql2, null);
			if(c2.moveToFirst()){
				cont2=c2.getInt(0);
			}
			
			String[] lista= new String[cont2];
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				lista[cont]=c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2)+"-"+c.getInt(3)+"-"+c.getInt(4)+"-"+c.getString(5)+"-"+c.getString(6)+"-"+c.getString(7)+"-"+c.getString(8)+"-"+c.getInt(9);
				cont++;
			}
			return lista;
		}
		
		public ArrayList<PartidoSemis> getAdaptadorPartidosSemis(){
			PartidoSemis partidoS;
			ArrayList<PartidoSemis> lista=new ArrayList<PartidoSemis>();
			String sql="SELECT idpartidosemis,idganador1,bandera1,paisganador1,gol1,gol2,bandera2,paisganador2,idganador2,fecha,hora,estado FROM partidosemis";
			
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				partidoS = new PartidoSemis(c.getString(0), c.getString(1),c.getInt(2), c.getString(3), c.getInt(4), c.getInt(5),c.getInt(6), c.getString(7), c.getString(8), Date.valueOf(c.getString(9)), Time.valueOf(c.getString(10)), c.getInt(11));
				lista.add(partidoS);
			}
			return lista;
		}
		
		public void actualizarPartidosSemisGoles(String idpartidosemis, int gol1, int gol2){
			String SQL="UPDATE partidosemis SET gol1="+gol1+", gol2="+gol2+", estado=1 WHERE idpartidosemis='"+idpartidosemis+"'";
			miDB.execSQL(SQL);
			sacarEquiposParaFinales();
		}
		
		public void sacarEquiposParaFinales(){
			String sql="SELECT idpartidosemis,paisganador1,gol1,gol2,paisganador2 FROM partidosemis WHERE estado=1";
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				posicionarEquipoAFinales(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4));
			}
		}
		
		//-----------------------------------------------------PARTIDO FINALES---------------------------------------------------
		
		public void registrarPartidosFinales(String idpartidofinales, String idganador1,int bandera1,String paisganador1, int bandera2, String paisganador2,String idganador2, Date fecha, Time hora){

			String sql="INSERT INTO partidofinales(idpartidofinales,idganador1,bandera1,paisganador1,bandera2,paisganador2,idganador2,fecha,hora) VALUES"
						+ "('"+idpartidofinales+"','"+idganador1+"',"+bandera1+",'"+paisganador1+"',"+bandera2+",'"+paisganador2+"','"+idganador2+"','"+fecha+"','"+hora+"')";
			miDB.execSQL(sql);
		}
		
		public void actualizarPartidoFinales(String idganador, int bandera,String paisganador){
			String sql="UPDATE partidofinales SET bandera1="+bandera+", paisganador1='"+paisganador+"' WHERE idganador1='"+idganador+"'";
			miDB.execSQL(sql);
			String sql1="UPDATE partidofinales SET bandera2="+bandera+", paisganador2='"+paisganador+"' WHERE idganador2='"+idganador+"'";
			miDB.execSQL(sql1);
		}
		
		public void posicionarEquipoAFinales(String idpartidosemis, String pais1,int gol1,int gol2,String pais2){
			int bandera1=getImagenBandera(pais1);
			int bandera2=getImagenBandera(pais2);
			String paseGanador="G"+idpartidosemis;
			String pasePerdedor="P"+idpartidosemis;
			if(gol1>gol2){
				Toast toast=Toast.makeText(miContexto, pais1+" paso a la final", Toast.LENGTH_SHORT);
				toast.show();
				actualizarPartidoFinales(paseGanador, bandera1, pais1);
				actualizarPartidoFinales(pasePerdedor, bandera2, pais2);
			}else if (gol2>gol1) {
				Toast toast=Toast.makeText(miContexto, pais2+" paso a la final", Toast.LENGTH_SHORT);
				toast.show();
				actualizarPartidoFinales(pasePerdedor, bandera1, pais1);
				actualizarPartidoFinales(paseGanador, bandera2, pais2);
			}else{
				Toast toast=Toast.makeText(miContexto, "Empate definicion por penales", Toast.LENGTH_LONG);
				toast.show();
			}
		}
		
		public ArrayList<PartidoFinales> getAdaptadorPartidosFinales(){
			PartidoFinales partidoF;
			ArrayList<PartidoFinales> lista=new ArrayList<PartidoFinales>();
			String sql="SELECT idpartidofinales,idganador1,bandera1,paisganador1,gol1,gol2,bandera2,paisganador2,idganador2,fecha,hora,estado FROM partidofinales";
			
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				partidoF = new PartidoFinales(c.getString(0), c.getString(1),c.getInt(2), c.getString(3), c.getInt(4), c.getInt(5),c.getInt(6), c.getString(7), c.getString(8), Date.valueOf(c.getString(9)), Time.valueOf(c.getString(10)), c.getInt(11));
				lista.add(partidoF);
			}
			return lista;
		}
		
		public void actualizarFinalesGoles(String idfinal,int gol1, int gol2){
			String sql="UPDATE partidofinales SET gol1="+gol1+", gol2="+gol2+", estado=1 WHERE idpartidofinales='"+idfinal+"'";
			miDB.execSQL(sql);
			sacarPuestoCampeon();
		}
		
		public void sacarPuestoCampeon(){
			String sql="SELECT idpartidofinales,paisganador1,gol1,gol2,paisganador2 FROM partidofinales WHERE estado=1";
			Cursor c=miDB.rawQuery(sql, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				posicionarCampeon(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4));
			}
		}
		
		public void posicionarCampeon(String idfinal, String pais1, int gol1, int gol2, String pais2){
			if(idfinal.equals("FG")){
				if(gol1>gol2){
					Toast toast=Toast.makeText(miContexto, pais1+" Campeon Brasil 2014", Toast.LENGTH_LONG);
					toast.show();
				}else if(gol2>gol1){
					Toast toast=Toast.makeText(miContexto, pais2+" Campeon Brasil 2014", Toast.LENGTH_LONG);
					toast.show();
				}else{
					Toast toast=Toast.makeText(miContexto, "Empate definicion por penales para campeon", Toast.LENGTH_LONG);
					toast.show();
				}
			}else if (idfinal.equals("FP")) {
				if(gol1>gol2){
					Toast toast=Toast.makeText(miContexto, "3ro: "+pais1+" y 4to: "+pais2, Toast.LENGTH_LONG);
					toast.show();
				}else if(gol2>gol1){
					Toast toast=Toast.makeText(miContexto, "3ro: "+pais2+" y 4to: "+pais1, Toast.LENGTH_LONG);
					toast.show();
				}else{
					Toast toast=Toast.makeText(miContexto, "Empate definicion por penales para campeon", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		}
		
		//------------------------------------------------manipulacion tabla usuario---------------------------------------------
		public void registrarUsuario(String nom){
			String sql="INSERT INTO usuario(nombre) VALUES ('"+nom+"')";
			miDB.execSQL(sql);
		}
		
		public int getCantidadUsuario(){
			int cont=0;
			String sql="SELECT COUNT(idusuario) FROM usuario";
			Cursor c=miDB.rawQuery(sql, null);
			if(c.moveToFirst()){
				cont=c.getInt(0);
			}
			return cont;
		}
		
		public String getNombre(){
			String nombre="";
			String sql="SELECT nombre FROM usuario";
			Cursor c=miDB.rawQuery(sql, null);
			if(c.moveToFirst()){
				nombre=c.getString(0);
			}
			return nombre;
		}		
		
}
