package com.silvia.cooperativa;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Environment;

public class Variables {
	public static final int PICK_FROM_GALLERY = 1;
	public static final String FOLDER_IMAGES_COOPERATIVA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/ImagesCooperativa/";
	public static final String FOLDER_INFO_VENTAS_PDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/InfoVentasCooperativa/";
	public static final String SIN_ESPECIFICAR = "Sin especificar";
	public static final int NO_ELIMINADO = 1;
	public static final int ELIMINADO = 0;
	public static final int MAQ_FUERA_DE_SERVICIO = 2;
	
	//static para manejo de los activitys
	public static final int ACCION_CARGAR_LISTA_CARGOS = 1;
	public static final int ACCION_CARGAR_LISTA_MAQ = 2;
	public static final int ACCION_CARGAR_LISTA_PERSONAL = 3;
	public static final int ACCION_CARGAR_LISTA_USUARIOS = 4;
	public static final int ACCION_CARGAR_NUEVO_CLIENTE = 5;
	public static final int ACCION_CARGAR_LISTA_CLIENTES = 6;
	public static final int ACCION_CARGAR_LISTA_PRODUCTOS = 7;
	public static final int ACCION_CARGAR_PERFIL = 8;
	public static final int ACCION_CARGAR_DETALLE_CLIENTE = 9;
	public static final int ACCION_CARGAR_DETALLE_PERSONAL = 10;
	public static final int ACCION_CARGAR_LISTA_PEDIDO = 11;
	
	//static para manejo de fragment nuevo maquinaria
	public static final int ACCION_NUEVO_MAQ = 1;
	public static final int ACCION_EDITAR_MAQ = 2;
	
	//static para manejo de fragment nuevo personal
	public static final int ACCION_NUEVO_PERSONAL = 1;
	public static final int ACCION_EDITAR_PERSONAL = 2;
	
	//static para manejo del dialogo nuevo usuario
	public static final int ACCION_NUEVO_USUARIO = 1;
	public static final int ACCION_EDITAR_USUARIO = 2;
	
	//static para manejo de fragment nuevo cliente
	public static final int ACCION_NUEVO_CLIENTE = 1;
	public static final int ACCION_EDITAR_CLIENTE = 2;
	public static final String SEXO_MASCULINO = "Masculino";
	public static final String SEXO_FEMENINO = "Femenino";
	
	//static para manejo de fragment nuevo producto
	public static final int ACCION_NUEVO_PROD = 1;
	public static final int ACCION_EDITAR_PROD = 2;
	
	public static final SimpleDateFormat FORMAT_HORA_1 = new SimpleDateFormat("'Hrs.' HH:mm:ss", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_FECHA_1 = new SimpleDateFormat("dd 'de' MMMM yyyy", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_FECHA_2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_FECHA_3 = new SimpleDateFormat("EEEE, dd  MMMM yyyy", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_FECHA_4 = new SimpleDateFormat("MMMM 'de' yyyy", Locale.getDefault());
	public static final Date FECHA_DEFAULT = Date.valueOf("1900-01-01");
	
	public static final String ID_CARGO_ADMIN = "cargo-1000";
	public static final String ID_MAQ_DEFAULT = "maq-1000";
	public static final String PLACA_MAQ_DEFAULT = "1000XXX";
	public static final String ID_PERSONAL_ADMIN = "per-1000";
	public static final String ID_USER_ADMIN = "user-1000";
	public static final String ID_CLIENTE_DEFAULT = "cli-1000";
	public static final double CAPACIDAD_DEFAULT = 1;
	public static final double COSTO_ENTREGA_DEFAULT = 0;
	
	public static final String ID_COOP = "COOP_1000";
	
	public static final int VENTA_DIRECTA = 1;
	public static final int VENTA_A_DOMICILIO = 2;
	
	public static final int OPCION_VENTA_POR_FECHA = 1;
	public static final int OPCION_VENTA_POR_MES = 2;
	
	public static final int VER_INFO_INGRESOS = 1;
	public static final int NO_VER_INFO_INGRESOS = 2;
	
	public static final int PROV_VENTA = 1;
	public static final int PROV_INFORME = 2;
	
	public static final int PEDIDO_PENDIENTE = 0;
	public static final int PEDIDO_ENTREGADO = 1;
	
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

	public static String getNuevoCodeUnico(String inicio_code){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String nuevo_code = new StringBuilder(inicio_code).append(date).toString();
		return nuevo_code;
	}
	
	public static Time getHoraActual(){
		java.util.Date date = new java.util.Date();
		Time hora_actual = new Time(date.getHours(), date.getMinutes(), date.getSeconds());
		return hora_actual;
	}
	
	public static Date getFechaActual(){
		java.util.Date date = new java.util.Date();
		Date fecha_actual = new Date(date.getTime());
		return fecha_actual;
	}
}

