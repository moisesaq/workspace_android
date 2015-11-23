package com.moisse.others;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;

public class MyVar{
	public static final String mi_nombre = "Moises Apaza Q.";
	
	public static final int SELECT_IMAGE = 1;
	public static final int CAPTURE_IMAGE = 2;
	public static final int PIC_CROP = 3;
	public static final int PIC_VALUE_MAX = 500;
	public static final String FOLDER_IMAGES_PARQUEO = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/ImagesParqueo/";

	//public static final String RUTA_SERVER = "http://192.168.1.100/parqueo/";
	public static final String RUTA_SERVER = "http://aqmoises.zoedev.com/parqueo/";
	//public static final String RUTA_IMAGES_SERVER = "http://192.168.1.100/parqueo/images_parqueo/";
	public static final String RUTA_IMAGES_SERVER = "http://aqmoises.zoedev.com/parqueo/images_parqueo/";
	
	public static final String NO_ESPECIFICADO = "No especificado";
	public static final String SIN_PROPIETARIO = "Sin propietario";
	public static final String SI = "SI";
	public static final String NO = "NO";
	public static final String MASCULINO = "Masculino";
	public static final String FEMENINO = "Femenino";
	public static final String ID_CLIENT_DEFAULT = "client_default_";
	public static final String CI_CLIENT_DEFAULT = "87654321";
	
	public static final int ELIMINADO = 0;
	public static final int NO_ELIMINADO = 1;
	
	public static final SimpleDateFormat FORMAT_HORA_1 = new SimpleDateFormat("'Hrs.' HH:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_FECHA_1 = new SimpleDateFormat("dd 'de' MMMM yyyy", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_FECHA_2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	public static final Date FECHA_DEFAULT = Date.valueOf("1900-01-01");
	
	public static final int CLIENTE_OCASIONAL = 1;
	public static final int CLIENTE_CONTRATO_NOCTURNO = 2;
	public static final int CLIENTE_CONTRATO_DIURNO = 3;
	public static final int CLIENTE_CONTRATO_DIA_COMPLETO = 4;
	
	public static final int ACCION_REGISTRAR_CLIENTE = 1;
	public static final int ACCION_EDITAR_CLIENTE = 2;
	
	public static final int ACCION_REGISTRAR_VEHICULO = 1;
	public static final int ACCION_EDITAR_VEHICULO = 2;
	
	public static final int CARGO_ADMIN = 1;
	public static final int CARGO_USUARIO = 2;
	
	public static final int ACCION_REGISTRAR_ADMIN = 1;
	public static final int ACCION_REGISTRAR_USUARIO = 2;
	public static final int ACCION_EDITAR_PERFIL = 3;
	
	public static final double COSTO_DEFAULT_CERO = 0;
	public static final int TIPO_RESGUARDO_DEFAULT = 0;
	public static final int TIPO_RESGUARDO_OCASIONAL = 1;
	public static final int TIPO_RESGUARDO_CONTRATO = 2;
	
	//Variables para actualizar la lista resguardos
	public static final int ACT_ULTIMOS_RESG = 0;
	public static final int ACT_RESG_HACE_3DIAS = 1;
	public static final int ACT_RESG_HACE_1SEMANA = 2;
	public static final int ACT_RESG_MES_ACTUAL = 3;
	public static final int ACT_RESG_FECHA_ESPECIFICO = 4;
	public static final int ACT_RESG_MES_ESPECIFICO = 5;
	public static final int ACT_RESG_POR_PLACA = 6;
	
	public static final int DEFINIR_HORARIO_DIURNO = 1;
	public static final int DEFINIR_HORARIO_NOCTURNO = 2;
	public static final Time INICIO_DIA_DEFAULT = new Time(8, 0, 0);
	public static final Time FIN_DIA_DEFAULT = new Time(19, 0, 0);
	public static final Time INICIO_NOCHE_DEFAULT = new Time(19, 1, 0);
	public static final Time FIN_NOCHE_DEFAULT = new Time(7, 59, 0);
	
	public static final InputFilter filterNumLetter = new InputFilter() { 
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) { 
	              if (!Character.isLetterOrDigit(source.charAt(i))) { 
	                  return ""; 
	              }
	          }
			return null;
		} 
	};

	public static final InputFilter filterLetter = new InputFilter() { 
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) { 
	              if (!Character.isLetter(source.charAt(i))) { 
	                  return ""; 
	              }
	          }
			return null;
		} 
	};
	
	public static boolean isLetter(Character c){
		if(Character.isLetter(c)){
			return true;
		}
		return false;
	}
	
	public static boolean isNumeric(String dato){
		try{
			Integer.parseInt(dato);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public static boolean isPlaca(String placa){
		int tamanio = placa.length();
		int numeros = 0; int letras = 3;
		if(tamanio==7){
			numeros = 4;
		}else{
			numeros = 3;
		}
		int auxNum = 0;
		int auxLetra = 0;
		for (int i = 0; i < placa.length(); i++) {
			Character c = placa.charAt(i);
			if((i+1)<=numeros){
				if(isNumeric(c.toString())){
					auxNum++;
				}
			}else{
				if(isLetter(c)){
					auxLetra++;
				}
			}
		}
		if(auxLetra==letras && auxNum==numeros){
			return true;
		}
		return false;
	}
	
}
