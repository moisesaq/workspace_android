package com.moisse.compartir;

import android.os.Environment;

public class MyVar {
	public static final int SELECT_IMAGE = 1;
	public static final int CAPTURE_IMAGE = 2;
	public static final int PIC_CROP = 3;
	public static final String FOLDER_IMAGES_VEHICULOS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/ImagesVehiculos/";
	public static final String NO_ESPECIFICADO = "No especificado";
	//public static final String RUTA_SERVER = "http://10.0.3.2/android/";
	public static final String RUTA_SERVER = "http://aqmoises.zoedev.com/pruebas/";
	//public static final String RUTA_IMAGES_SERVER = "http://10.0.3.2/android/images_subidas/";
	public static final String RUTA_IMAGES_SERVER = "http://aqmoises.zoedev.com/pruebas/images_subidas/";
}
