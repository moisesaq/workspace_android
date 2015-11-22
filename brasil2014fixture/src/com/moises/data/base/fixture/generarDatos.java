package com.moises.data.base.fixture;

import java.sql.Date;
import java.sql.Time;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.example.brasil2014fixture.R;
import com.example.brasil2014fixture.R.drawable;

public class generarDatos {

	Context miContexto;
	
	Integer[] banderasGA={R.drawable.brasil, R.drawable.croacia, R.drawable.mexico, R.drawable.camerun};
	Integer[] banderasGB={R.drawable.espania, R.drawable.holanda, R.drawable.chile, R.drawable.australia};
	Integer[] banderasGC={R.drawable.colombia, R.drawable.grecia, R.drawable.costa_marfil, R.drawable.japon};
	Integer[] banderasGD={R.drawable.uruguay, R.drawable.costa_rica, R.drawable.inglaterra, R.drawable.italia};
	Integer[] banderasGE={R.drawable.suiza, R.drawable.ecuador, R.drawable.francia, R.drawable.honduras};
	Integer[] banderasGF={R.drawable.argentina, R.drawable.bosnia, R.drawable.iran, R.drawable.nigeria};
	Integer[] banderasGG={R.drawable.alemania, R.drawable.portugal, R.drawable.ghana, R.drawable.eeuu};
	Integer[] banderasGH={R.drawable.belgica, R.drawable.argelia, R.drawable.rusia, R.drawable.corea_sur};
	
	Integer BANDERA=R.drawable.bandera;
	public generarDatos(Context contexto){
		this.miContexto=contexto;
	}
	
	public void generarGrupos(){
		DBFixture dbfixtu=new DBFixture(miContexto);
		try {
			dbfixtu.AbrirDB();
			dbfixtu.registrarGrupo("A");
			dbfixtu.registrarGrupo("B");
			dbfixtu.registrarGrupo("C");
			dbfixtu.registrarGrupo("D");
			dbfixtu.registrarGrupo("E");
			dbfixtu.registrarGrupo("F");
			dbfixtu.registrarGrupo("G");
			dbfixtu.registrarGrupo("H");
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"generar grupos");
		}
	}
	
	public void generarEquipos(){
		DBFixture dbfE=new DBFixture(miContexto);
		try {
			dbfE.AbrirDB();
			dbfE.registrarEquipo("BRASIL", banderasGA[0], "A");
			dbfE.registrarEquipo("CROACIA", banderasGA[1], "A");
			dbfE.registrarEquipo("MEXICO", banderasGA[2], "A");
			dbfE.registrarEquipo("CAMERUN", banderasGA[3], "A");
			
			dbfE.registrarEquipo("ESPAÑA", banderasGB[0], "B");
			dbfE.registrarEquipo("HOLANDA", banderasGB[1], "B");
			dbfE.registrarEquipo("CHILE", banderasGB[2], "B");
			dbfE.registrarEquipo("AUSTRALIA", banderasGB[3], "B");
			
			dbfE.registrarEquipo("COLOMBIA", banderasGC[0], "C");
			dbfE.registrarEquipo("GRECIA", banderasGC[1], "C");
			dbfE.registrarEquipo("C. DE MARFIL", banderasGC[2], "C");
			dbfE.registrarEquipo("JAPON", banderasGC[3], "C");
			
			dbfE.registrarEquipo("URUGUAY", banderasGD[0], "D");
			dbfE.registrarEquipo("COSTA RICA", banderasGD[1], "D");
			dbfE.registrarEquipo("INGLATERRA", banderasGD[2], "D");
			dbfE.registrarEquipo("ITALIA", banderasGD[3], "D");
			
			dbfE.registrarEquipo("SUIZA", banderasGE[0], "E");
			dbfE.registrarEquipo("ECUADOR", banderasGE[1], "E");
			dbfE.registrarEquipo("FRANCIA", banderasGE[2], "E");
			dbfE.registrarEquipo("HONDURAS", banderasGE[3], "E");
			
			dbfE.registrarEquipo("ARGENTINA", banderasGF[0], "F");
			dbfE.registrarEquipo("BOSNIA", banderasGF[1], "F");
			dbfE.registrarEquipo("IRAN", banderasGF[2], "F");
			dbfE.registrarEquipo("NIGERIA", banderasGF[3], "F");
			
			dbfE.registrarEquipo("ALEMANIA", banderasGG[0], "G");
			dbfE.registrarEquipo("PORTUGAL", banderasGG[1], "G");
			dbfE.registrarEquipo("GHANA", banderasGG[2], "G");
			dbfE.registrarEquipo("EE.UU.", banderasGG[3], "G");
			
			dbfE.registrarEquipo("BELGICA", banderasGH[0], "H");
			dbfE.registrarEquipo("ARGELIA", banderasGH[1], "H");
			dbfE.registrarEquipo("RUSIA", banderasGH[2], "H");
			dbfE.registrarEquipo("C. DEL SUR", banderasGH[3], "H");
			dbfE.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "generar equipos");
		}
	}
	
	public void generarPartidos(){
		DBFixture dbfP=new DBFixture(miContexto);
		try {
			dbfP.AbrirDB();
			dbfP.registrarPartido("BRASIL", "CROACIA", Time.valueOf("16:00:00"), Date.valueOf("2014-6-12"), "A");
			dbfP.registrarPartido("MEXICO", "CAMERUN", Time.valueOf("12:00:00"), Date.valueOf("2014-6-13"), "A");
			dbfP.registrarPartido("BRASIL", "MEXICO", Time.valueOf("15:00:00"), Date.valueOf("2014-6-17"), "A");
			dbfP.registrarPartido("CAMERUN", "CROACIA", Time.valueOf("18:00:00"), Date.valueOf("2014-6-18"), "A");
			dbfP.registrarPartido("CAMERUN", "BRASIL", Time.valueOf("16:00:00"), Date.valueOf("2014-6-23"), "A");
			dbfP.registrarPartido("CROACIA", "MEXICO", Time.valueOf("16:00:00"), Date.valueOf("2014-6-23"), "A");
			
			dbfP.registrarPartido("ESPAÑA", "HOLANDA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-13"), "B");
			dbfP.registrarPartido("CHILE", "AUSTRALIA", Time.valueOf("18:00:00"), Date.valueOf("2014-6-13"), "B");
			dbfP.registrarPartido("ESPAÑA", "CHILE", Time.valueOf("15:00:00"), Date.valueOf("2014-6-18"), "B");
			dbfP.registrarPartido("AUSTRALIA", "HOLANDA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-18"), "B");
			dbfP.registrarPartido("AUSTRALIA", "ESPAÑA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-23"), "B");
			dbfP.registrarPartido("HOLANDA", "CHILE", Time.valueOf("12:00:00"), Date.valueOf("2014-6-23"), "B");
			
			dbfP.registrarPartido("COLOMBIA", "GRECIA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-14"), "C");
			dbfP.registrarPartido("C. DE MARFIL", "JAPON", Time.valueOf("21:00:00"), Date.valueOf("2014-6-14"), "C");
			dbfP.registrarPartido("COLOMBIA", "C. DE MARFIL", Time.valueOf("12:00:00"), Date.valueOf("2014-6-19"), "C");
			dbfP.registrarPartido("JAPON", "GRECIA", Time.valueOf("18:00:00"), Date.valueOf("2014-6-19"), "C");
			dbfP.registrarPartido("JAPON", "COLOMBIA", Time.valueOf("16:00:00"), Date.valueOf("2014-6-24"), "C");
			dbfP.registrarPartido("GRECIA", "C. DE MARFIL", Time.valueOf("16:00:00"), Date.valueOf("2014-6-24"), "C");
			
			dbfP.registrarPartido("URUGUAY", "COSTA RICA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-14"), "D");
			dbfP.registrarPartido("INGLATERRA", "ITALIA", Time.valueOf("18:00:00"), Date.valueOf("2014-6-14"), "D");
			dbfP.registrarPartido("URUGUAY", "INGLATERRA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-19"), "D");
			dbfP.registrarPartido("ITALIA", "COSTA RICA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-20"), "D");
			dbfP.registrarPartido("ITALIA", "URUGUAY", Time.valueOf("12:00:00"), Date.valueOf("2014-6-24"), "D");
			dbfP.registrarPartido("COSTA RICA", "INGLATERRA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-24"), "D");
			
			
			dbfP.registrarPartido("SUIZA", "ECUADOR", Time.valueOf("1:00:00"), Date.valueOf("2014-6-15"), "E");
			dbfP.registrarPartido("FRANCIA", "HONDURAS", Time.valueOf("15:00:00"), Date.valueOf("2014-6-15"), "E");
			dbfP.registrarPartido("SUIZA", "FRANCIA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-20"), "E");
			dbfP.registrarPartido("HONDURAS", "ECUADOR", Time.valueOf("18:00:00"), Date.valueOf("2014-6-20"), "E");
			dbfP.registrarPartido("HONDURAS", "SUIZA", Time.valueOf("16:00:00"), Date.valueOf("2014-6-25"), "E");
			dbfP.registrarPartido("ECUADOR", "FRANCIA", Time.valueOf("16:00:00"), Date.valueOf("2014-6-25"), "E");
			
			dbfP.registrarPartido("ARGENTINA", "BOSNIA", Time.valueOf("18:00:00"), Date.valueOf("2014-6-15"), "F");
			dbfP.registrarPartido("IRAN", "NIGERIA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-16"), "F");
			dbfP.registrarPartido("ARGENTINA", "IRAN", Time.valueOf("12:00:00"), Date.valueOf("2014-6-21"), "F");
			dbfP.registrarPartido("NIGERIA", "BOSNIA", Time.valueOf("18:00:00"), Date.valueOf("2014-6-21"), "F");
			dbfP.registrarPartido("NIGERIA", "ARGENTINA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-25"), "F");
			dbfP.registrarPartido("BOSNIA", "IRAN", Time.valueOf("12:00:00"), Date.valueOf("2014-6-25"), "F");
			
			dbfP.registrarPartido("ALEMANIA", "PORTUGAL", Time.valueOf("12:00:00"), Date.valueOf("2014-6-16"), "G");
			dbfP.registrarPartido("GHANA", "EE.UU.", Time.valueOf("18:00:00"), Date.valueOf("2014-6-16"), "G");
			dbfP.registrarPartido("ALEMANIA", "GHANA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-21"), "G");
			dbfP.registrarPartido("EE.UU.", "PORTUGAL", Time.valueOf("18:00:00"), Date.valueOf("2014-6-22"), "G");
			dbfP.registrarPartido("EE.UU.", "ALEMANIA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-26"), "G");
			dbfP.registrarPartido("PORTUGAL", "GHANA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-26"), "G");
			
			dbfP.registrarPartido("BELGICA", "ARGELIA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-17"), "H");
			dbfP.registrarPartido("RUSIA", "C. DEL SUR", Time.valueOf("18:00:00"), Date.valueOf("2014-6-17"), "H");
			dbfP.registrarPartido("BELGICA", "RUSIA", Time.valueOf("12:00:00"), Date.valueOf("2014-6-22"), "H");
			dbfP.registrarPartido("C. DEL SUR", "ARGELIA", Time.valueOf("15:00:00"), Date.valueOf("2014-6-22"), "H");
			dbfP.registrarPartido("C. DEL SUR", "BELGICA", Time.valueOf("16:00:00"), Date.valueOf("2014-6-26"), "H");
			dbfP.registrarPartido("ARGELIA", "RUSIA", Time.valueOf("16:00:00"), Date.valueOf("2014-6-26"), "H");
			
			dbfP.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "generar partidos");
		}
	}
	
	public void generarEquipoOctavos(){
		DBFixture dbfixtu=new DBFixture(miContexto);
		try {
			dbfixtu.AbrirDB();
			dbfixtu.registrarOctavos("1A", "PRIMERO A", BANDERA, "A");
			dbfixtu.registrarOctavos("2A", "SEGUNDO A", BANDERA, "A");
			dbfixtu.registrarOctavos("1B", "PRIMERO B", BANDERA, "B");
			dbfixtu.registrarOctavos("2B", "SEGUNDO B", BANDERA, "B");
			dbfixtu.registrarOctavos("1C", "PRIMERO C", BANDERA, "C");
			dbfixtu.registrarOctavos("2C", "SEGUNDO C", BANDERA, "C");
			dbfixtu.registrarOctavos("1D", "PRIMERO D", BANDERA, "D");
			dbfixtu.registrarOctavos("2D", "SEGUNDO D", BANDERA, "D");
			dbfixtu.registrarOctavos("1E", "PRIMERO E", BANDERA, "E");
			dbfixtu.registrarOctavos("2E", "SEGUNDO E", BANDERA, "E");
			dbfixtu.registrarOctavos("1F", "PRIMERO F", BANDERA, "F");
			dbfixtu.registrarOctavos("2F", "SEGUNDO F", BANDERA, "F");
			dbfixtu.registrarOctavos("1G", "PRIMERO G", BANDERA, "G");
			dbfixtu.registrarOctavos("2G", "SEGUNDO G", BANDERA, "G");
			dbfixtu.registrarOctavos("1H", "PRIMERO H", BANDERA, "H");
			dbfixtu.registrarOctavos("2H", "SEGUNDO H", BANDERA, "H");
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"generar equipos octavos");
		}
	}
	
	public void generarPartidosOctavos(){
		DBFixture dbfixtu=new DBFixture(miContexto);
		try {
			dbfixtu.AbrirDB();
			dbfixtu.registrarPartidosOctavos("A", "1A", "2B", Date.valueOf("2014-6-28"), Time.valueOf("12:00:00"));
			dbfixtu.registrarPartidosOctavos("B", "1C", "2D", Date.valueOf("2014-6-28"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosOctavos("C", "1B", "2A", Date.valueOf("2014-6-29"), Time.valueOf("12:00:00"));
			dbfixtu.registrarPartidosOctavos("D", "1D", "2C", Date.valueOf("2014-6-29"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosOctavos("E", "1E", "2F", Date.valueOf("2014-6-30"), Time.valueOf("12:00:00"));
			dbfixtu.registrarPartidosOctavos("F", "1G", "2H", Date.valueOf("2014-6-30"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosOctavos("G", "1F", "2E", Date.valueOf("2014-7-1"), Time.valueOf("12:00:00"));
			dbfixtu.registrarPartidosOctavos("H", "1H", "2G", Date.valueOf("2014-7-1"), Time.valueOf("16:00:00"));
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"generar equipos octavos");
		}
	}
	
	public void generarPartidosCuartos(){
		DBFixture dbfixtu=new DBFixture(miContexto);
		try {
			dbfixtu.AbrirDB();
			dbfixtu.registrarPartidosCuartos("I", "GA",BANDERA,"GANADOR A",BANDERA,"GANADOR B", "GB", Date.valueOf("2014-7-4"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosCuartos("J", "GE",BANDERA,"GANADOR E",BANDERA,"GANADOR F", "GF", Date.valueOf("2014-7-4"), Time.valueOf("12:00:00"));
			dbfixtu.registrarPartidosCuartos("K", "GC",BANDERA,"GANADOR C",BANDERA,"GANADOR D", "GD", Date.valueOf("2014-7-5"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosCuartos("L", "GG",BANDERA,"GANADOR G",BANDERA,"GANADOR H", "GH", Date.valueOf("2014-7-5"), Time.valueOf("12:00:00"));
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"generar equipos cuartos");
		}
	}
	
	public void generarPartidosSemis(){
		DBFixture dbfixtu=new DBFixture(miContexto);
		try {
			dbfixtu.AbrirDB();
			dbfixtu.registrarPartidosSemis("M", "GI",BANDERA,"GANADOR I",BANDERA,"GANADOR J", "GJ", Date.valueOf("2014-7-8"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosSemis("N", "GK",BANDERA,"GANADOR K",BANDERA,"GANADOR L", "GL", Date.valueOf("2014-7-9"), Time.valueOf("16:00:00"));
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"generar equipos semis");
		}
	}
	
	public void generarPartidosFinales(){
		DBFixture dbfixtu=new DBFixture(miContexto);
		try {
			dbfixtu.AbrirDB();
			dbfixtu.registrarPartidosFinales("FP", "PM",BANDERA,"PERDEDOR M",BANDERA,"PERDEDOR N", "PN", Date.valueOf("2014-7-12"), Time.valueOf("16:00:00"));
			dbfixtu.registrarPartidosFinales("FG", "GM",BANDERA,"GANADOR M",BANDERA,"GANADOR N", "GN", Date.valueOf("2014-7-13"), Time.valueOf("15:00:00"));
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error,"generar equipos finales");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(miContexto);
		mensaje.setTitle("Error al "+titulo);
		TextView texto=new TextView(miContexto);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
}
