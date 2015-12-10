package com.silvia.basedatos;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpURLCustom {

	final static String API_CARGO = "handle_cargo.php";
	final static String API_MAQUINARIA = "handle_maquinaria.php";
	final static String API_PERSONAL = "handle_personal.php";
	final static String API_USUARIO = "handle_usuario.php";
	final static String METHOD_POST = "POST";
	final static String CONTENT_TYPE = "Content-Type";
	final static String  VALUE_ENCODED = "application/x-www-form-urlencoded";
	
	final static String ACCION_SET = "set";
	final static String ACCION_GET_ALL = "get_all";
	
	Activity activity;
	HttpURLConnection con = null;
	
	public HttpURLCustom(Activity activity){
		this.activity = activity;
	}
	
	public boolean exitsConnection(){
		ConnectivityManager connMg = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfor = connMg.getActiveNetworkInfo();
		if(networkInfor!=null && networkInfor.isConnected()){
			return true;
		}
		return false;
	}
	
	public boolean regitrarCargo(Cargo cargo){
		try {
			URL url = new URL(Variables.RUTA_SERVER+API_CARGO);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod(METHOD_POST);
			con.setReadTimeout(10000); //Tiempo de espera para leer
			con.setConnectTimeout(10000);//Tiempo de espera para conectar
			con.setDoOutput(true);
			con.setRequestProperty(CONTENT_TYPE, VALUE_ENCODED);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("accion", ACCION_SET));
			params.add(new BasicNameValuePair("idcargo", cargo.getIdcargo()));
			params.add(new BasicNameValuePair("ocupacion", cargo.getOcupacion()));
			params.add(new BasicNameValuePair("salario", String.valueOf(cargo.getSalario())));
			params.add(new BasicNameValuePair("descripcion", cargo.getDescripcion()));
			params.add(new BasicNameValuePair("estado", String.valueOf(cargo.getEstado())));
			
			OutputStreamWriter request = new OutputStreamWriter(con.getOutputStream());
			request.write(getQuery(params));
			request.flush();
			request.close();
			if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean regitrarMaquinaria(Maquinaria maq){
		try {
			URL url = new URL(Variables.RUTA_SERVER+API_MAQUINARIA);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod(METHOD_POST);
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			con.setDoOutput(true);
			con.setRequestProperty(CONTENT_TYPE, VALUE_ENCODED);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("accion", ACCION_SET));
			params.add(new BasicNameValuePair("idmaquinaria", maq.getIdmaquinaria()));
			params.add(new BasicNameValuePair("placa", maq.getPlaca()));
			params.add(new BasicNameValuePair("descripcion", maq.getDescripcion()));
			params.add(new BasicNameValuePair("capacidad", String.valueOf(maq.getCapacidad())));
			params.add(new BasicNameValuePair("marca", maq.getMarca()));
			params.add(new BasicNameValuePair("color", maq.getColor()));
			params.add(new BasicNameValuePair("imagen", maq.getImagen()));
			params.add(new BasicNameValuePair("estado", String.valueOf(maq.getEstado())));
			
			OutputStreamWriter request = new OutputStreamWriter(con.getOutputStream());
			Log.d("tagMaq2",getQuery(params));
			request.write(getQuery(params));
			request.flush();
			request.close();
			if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean regitrarPersonal(Personal per){		
		try {
			URL url = new URL(Variables.RUTA_SERVER+API_PERSONAL);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod(METHOD_POST);
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			con.setDoOutput(true);
			con.setRequestProperty(CONTENT_TYPE, VALUE_ENCODED);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("accion", ACCION_SET));
			params.add(new BasicNameValuePair("idpersonal", per.getIdpersonal()));
			params.add(new BasicNameValuePair("ci", per.getCi()));
			params.add(new BasicNameValuePair("nombre", per.getNombre()));
			params.add(new BasicNameValuePair("apellido", per.getApellido()));
			params.add(new BasicNameValuePair("direccion", per.getDireccion()));
			params.add(new BasicNameValuePair("telefono", String.valueOf(per.getTelf())));
			params.add(new BasicNameValuePair("email", per.getEmail()));
			params.add(new BasicNameValuePair("fecha_nac", String.valueOf(per.getFecha_nac())));
			params.add(new BasicNameValuePair("fecha_ingreso", String.valueOf(per.getFecha_ingreso())));
			params.add(new BasicNameValuePair("imagen", per.getImagen()));
			params.add(new BasicNameValuePair("estado", String.valueOf(per.getEstado())));
			params.add(new BasicNameValuePair("idcargo", per.getIdcargo()));
			params.add(new BasicNameValuePair("idmaquinaria", per.getIdmaquinaria()));
			
			OutputStreamWriter request = new OutputStreamWriter(con.getOutputStream());
			Log.d("tagPer2",getQuery(params));
			request.write(getQuery(params));
			request.flush();
			request.close();
			if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean regitrarUsuario(Usuario usuario){		
		try {
			URL url = new URL(Variables.RUTA_SERVER+API_USUARIO);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod(METHOD_POST);
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			con.setDoOutput(true);
			con.setRequestProperty(CONTENT_TYPE, VALUE_ENCODED);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("accion", ACCION_SET));
			params.add(new BasicNameValuePair("idusuario", usuario.getIdusuario()));
			params.add(new BasicNameValuePair("usuario", usuario.getUsuario()));
			params.add(new BasicNameValuePair("clave", usuario.getClave()));
			params.add(new BasicNameValuePair("estado", String.valueOf(usuario.getEstado())));
			params.add(new BasicNameValuePair("idpersonal", usuario.getIdpersonal()));
			
			OutputStreamWriter request = new OutputStreamWriter(con.getOutputStream());
			Log.d("tagPer2",getQuery(params));
			request.write(getQuery(params));
			request.flush();
			request.close();
			if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public String getQuery(List<NameValuePair> params) throws IOException{
		StringBuilder resultado = new StringBuilder();
		boolean first = true;
		for(NameValuePair pair: params){
			if(first)
				first = false;
			else
				resultado.append("&");
			resultado.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			resultado.append("=");
			resultado.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}
		
		return resultado.toString();
	}
	
}
