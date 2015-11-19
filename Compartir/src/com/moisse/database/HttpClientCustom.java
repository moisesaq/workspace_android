package com.moisse.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moisse.compartir.MyVar;
import com.moisse.modelo.Vehiculo;

public class HttpClientCustom {
	
	private static final String VERIFICAR_PLACA = "verificar_placa";
	private static final String INSERTAR = "insertar";
	private static final String ACTUALIZAR = "actualizar";
	private static final String CARGAR_DATOS = "cargar_datos";
	
	//private static final String RUTA_SERVER = "http://192.168.1.100/android/";//"http://aqmoises.zoedev.com/android/" ;
	
	public HttpClientCustom(){
		
	}
	
	public boolean verifyPlacaDisponible(Vehiculo v){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"manipular_tabla_vehiculo.php");
		ArrayList<NameValuePair> postValor = new ArrayList<NameValuePair>();
		postValor.add(new BasicNameValuePair("accion", VERIFICAR_PLACA));
		postValor.add(new BasicNameValuePair("placa", v.getPlaca()));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValor);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean registrarVehiculo(Vehiculo v){
		String query_insertar_vehiculo = "INSERT INTO vehiculo(placa, marca, color, imagen, estado) " +
				"VALUES ('"+v.getPlaca()+"','"+v.getMarca()+"','"+v.getColor()+"','"+v.getImagen()+"',"+v.getEstado()+")";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"manipular_tabla_vehiculo.php");
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", INSERTAR));
		postValores.add(new BasicNameValuePair("placa",v.getPlaca()));
		postValores.add(new BasicNameValuePair("query_insertar_vehiculo", query_insertar_vehiculo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean actualizarVehiculo(Vehiculo v){
		String query_vehiculo_actualizar = "UPDATE vehiculo SET marca='"+v.getMarca()+"', color='"+v.getColor()+"', imagen='"+v.getImagen()+"' " +
				"WHERE placa='"+v.getPlaca()+"'";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"manipular_tabla_vehiculo.php");
		
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", ACTUALIZAR));
		postValores.add(new BasicNameValuePair("placa",v.getPlaca()));
		postValores.add(new BasicNameValuePair("query_vehiculo_actualizar", query_vehiculo_actualizar));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Vehiculo> cargarVehiculos(){
		List<Vehiculo> lista_vehiculos = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"manipular_tabla_vehiculo.php");
		
		ArrayList<NameValuePair> postValor = new ArrayList<NameValuePair>();
		postValor.add(new BasicNameValuePair("accion", CARGAR_DATOS));
		
		UrlEncodedFormEntity encodeEntity;
		try {
			encodeEntity = new UrlEncodedFormEntity(postValor);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String json_datos_vehiculos = convertStreamToString(entity.getContent());
			if(!json_datos_vehiculos.equals("0")){
				lista_vehiculos = parseJSONVehiculo(json_datos_vehiculos);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return lista_vehiculos;
	}
	
	public List<Vehiculo> parseJSONVehiculo(String json_datos){
		List<Vehiculo> lista = new ArrayList<Vehiculo>();
		try {
			JSONArray jsonA = new JSONArray(json_datos);
			for (int i = 0; i < jsonA.length(); i++) {
				JSONObject JOV = jsonA.getJSONObject(i);
				Vehiculo vehi = new Vehiculo(JOV.getString("placa"), JOV.getString("marca"), JOV.getString("color"), 
												JOV.getString("imagen"), Integer.parseInt(JOV.getString("estado")));
				lista.add(vehi);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public String convertStreamToString(InputStream istream){
		String resultado = "";
		BufferedReader bReader = new BufferedReader(new InputStreamReader(istream));
		StringBuffer sb = new StringBuffer();
		
		String linea = "";
		
		try {
			while((linea=bReader.readLine())!=null){
				sb.append(linea);
			}
			resultado = sb.toString();
			return resultado;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
}
