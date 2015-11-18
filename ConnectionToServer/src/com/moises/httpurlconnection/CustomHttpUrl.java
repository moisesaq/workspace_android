package com.moises.httpurlconnection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moises.connectiontoserver.Var;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CustomHttpUrl {
	Activity activity;
	HttpURLConnection con = null;
	public CustomHttpUrl(Activity activity){
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
	
	public boolean probarConexion(){
		URL url;		
		try {
			url = new URL(Var.RUTA_SERVER+"get_all_lugar.php");
			con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(15000);
			con.setReadTimeout(10000);
			int statusCode = con.getResponseCode();
			if(statusCode==HttpURLConnection.HTTP_OK){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			con.disconnect();
		}
		return false;
	}
	
	public List<Lugar> getAllLugares(){
		List<Lugar> lista = null;
		URL url;
		try {
			url = new URL(Var.RUTA_SERVER+"get_all_lugar.php");
			con = (HttpURLConnection)url.openConnection();
			int statusCode = con.getResponseCode();
			if(statusCode==HttpURLConnection.HTTP_OK){
				InputStream istream = new BufferedInputStream(con.getInputStream());
				//lista = parseJsonToLugares(convertStreamToString(istream));
				//ParseJSONReaderLugar parse = new ParseJSONReaderLugar(istream);
				//lista = parse.readJsonStream();
				GsonParseLugar parse = new GsonParseLugar(istream);
				lista = parse.getLugares();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			con.disconnect();
		}
		
		return lista;
	}
	
	public boolean registrarLugar(Lugar lugar){
		URL url;
		try{
			url = new URL(new StringBuilder(Var.RUTA_SERVER).append("insert_lugar.php").toString());
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idlugar", lugar.getIdlugar()));
			params.add(new BasicNameValuePair("nombre_lugar", lugar.getNombre_lugar()));
			params.add(new BasicNameValuePair("direccion", lugar.getDireccion()));
			params.add(new BasicNameValuePair("descripcion", lugar.getDescripcion()));
			
			OutputStreamWriter request = new OutputStreamWriter(con.getOutputStream());
//			String test = "idlugar="+lugar.getIdlugar()+"&nombre_lugar="+lugar.getNombre_lugar()+
//								"&direccion="+lugar.getDireccion()+"&descripcion="+lugar.getDescripcion();
			request.write(getQuery(params));
			request.flush();
			request.close();
			if(HttpURLConnection.HTTP_OK==con.getResponseCode()){
				return true;
			}
		}catch(Exception e){
			
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
	
	public List<Lugar> parseJsonToLugares(String JSONLugares){
		List<Lugar> lista = new ArrayList<Lugar>();
		try {
			JSONArray jsonA = new JSONArray(JSONLugares);
			for (int i = 0; i < jsonA.length(); i++) {
				JSONObject jsonL = jsonA.getJSONObject(i);
				Lugar lugar = new Lugar(jsonL.getString("idlugar"), jsonL.getString("nombre_lugar"), jsonL.getString("direccion"), jsonL.getString("descripcion"));
				lista.add(lugar);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public String convertStreamToString(InputStream is){
		String resultado = "";
		BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
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
