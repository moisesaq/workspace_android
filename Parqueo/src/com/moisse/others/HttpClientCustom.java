package com.moisse.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
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

import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Usuario;
import com.moisse.modelo.Vehiculo;

public class HttpClientCustom {
	
	private static final String INSERTAR = "insertar";
	private static final String ACTUALIZAR = "actualizar";
	private static final String ELIMINAR = "eliminar";
	private static final String RESTAURAR = "restaurar";
	private static final String CAMBIAR_CLAVE = "cambiar_clave";
	private static final String VERIFICAR_CI = "verificar_ci";
	private static final String VERIFICAR_PLACA = "verificar_placa";
	private static final String AUTENTICAR = "autenticar";
	private static final String CARGAR_DATOS = "cargar_datos";
	private static final String CARGAR_ENTRADAS = "cargar_entradas";
	private static final String GET_DATO = "get_dato";
	private static final String EDITAR_NOTA = "editar_nota";

	
	public HttpClientCustom(){
	}
	
	//----------------------------------------------CLIENTE--------------------------------------------
	
	public boolean verificarCICliente(String idparqueo, String ci_cliente){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", VERIFICAR_CI));
		postValores.add(new BasicNameValuePair("id_parqueo", idparqueo));
		postValores.add(new BasicNameValuePair("ci_cliente", ci_cliente));
		
		try {
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean insertarCliente(Cliente c){
		String query_insertar_cliente = "INSERT INTO cliente(idcliente, ci, nombre, apellido, celular, direccion, email, sexo, fecha_nac, " +
									"imagen, estado, tipo, fecha_contrato, idparqueo) " +
									"VALUES ('"+c.getIdcliente()+"','"+c.getCi()+"','"+c.getNombre()+"','"+c.getApellido()+"',"+c.getCelular()+"," +
									"'"+c.getDireccion()+"','"+c.getEmail()+"','"+c.getSexo()+"','"+c.getFecha_nac()+"',"+
									"'"+c.getImagen()+"',"+c.getEstado()+","+c.getTipo()+",'"+c.getFecha_contrato()+"','"+c.getIdparqueo()+"')";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", INSERTAR));
		postValores.add(new BasicNameValuePair("id_cliente", c.getIdcliente()));
		postValores.add(new BasicNameValuePair("query_insertar_cliente", query_insertar_cliente));
		
		try {
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean actualizarCliente(Cliente c){
		String query_actualizar_cliente = "UPDATE cliente SET ci='"+c.getCi()+"', nombre='"+c.getNombre()+"', apellido='"+c.getApellido()+"', celular="+c.getCelular()+", " +
				"direccion='"+c.getDireccion()+"', email='"+c.getEmail()+"', sexo='"+c.getSexo()+"', fecha_nac='"+c.getFecha_nac()+"', " +
				"imagen='"+c.getImagen()+"', estado="+c.getEstado()+", tipo="+c.getTipo()+", fecha_contrato='"+c.getFecha_contrato()+"' " +
					"WHERE idcliente='"+c.getIdcliente()+"'";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", ACTUALIZAR));
		postValores.add(new BasicNameValuePair("id_cliente", c.getIdcliente()));
		postValores.add(new BasicNameValuePair("query_actualizar_cliente", query_actualizar_cliente));
		
		try {
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean eliminarCliente(Cliente c){
		String query_eliminar_cliente = "UPDATE cliente SET estado="+c.getEstado()+" WHERE idcliente='"+c.getIdcliente()+"'";

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", ELIMINAR));
		postValores.add(new BasicNameValuePair("id_cliente", c.getIdcliente()));
		postValores.add(new BasicNameValuePair("query_eliminar_cliente", query_eliminar_cliente));
		
		try {
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean restaurarCliente(Cliente c){
		String query_restaurar_cliente = "UPDATE cliente SET estado="+c.getEstado()+" WHERE idcliente='"+c.getIdcliente()+"'";

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		
		ArrayList<NameValuePair> postValores = new ArrayList<NameValuePair>();
		postValores.add(new BasicNameValuePair("accion", RESTAURAR));
		postValores.add(new BasicNameValuePair("id_cliente", c.getIdcliente()));
		postValores.add(new BasicNameValuePair("query_restaurar_cliente", query_restaurar_cliente));
		
		try {
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValores);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public Cliente getDatosCliente(String idcliente){
		Cliente cliente = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", GET_DATO));
		postValoresVehiculo.add(new BasicNameValuePair("id_cliente", idcliente));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String json_cliente = convertStreamToString(entity.getContent());
			if(!json_cliente.equals("0")){
				cliente = getParseJSONCliente(json_cliente);
			}
			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return cliente;
	}
	
	public Cliente getDatosClientePorCI(String ci_cliente, String idparqueo){
		Cliente cliente = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", "get_dato_por_ci"));
		postValoresVehiculo.add(new BasicNameValuePair("ci_cliente", ci_cliente));
		postValoresVehiculo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String json_cliente = convertStreamToString(entity.getContent());
			if(!json_cliente.equals("0")){
				cliente = getParseJSONCliente(json_cliente);
			}
			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return cliente;
	}
	
	public Cliente getParseJSONCliente(String datos_json){
		Cliente cliente = null;
		try {
			JSONObject jsonC = new JSONObject(datos_json);
			cliente = new Cliente(jsonC.getString("idcliente"), jsonC.getString("ci"), jsonC.getString("nombre"), 
					jsonC.getString("apellido"), Integer.parseInt(jsonC.getString("celular")), jsonC.getString("direccion"), 
					jsonC.getString("email"), jsonC.getString("sexo"), Date.valueOf(jsonC.getString("fecha_nac")), 
					jsonC.getString("imagen"), Integer.parseInt(jsonC.getString("estado")), Integer.parseInt(jsonC.getString("tipo")), 
					Date.valueOf(jsonC.getString("fecha_contrato")), jsonC.getString("idparqueo"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public List<Cliente> cargarClientes(String idparqueo){
		List<Cliente> lista_clientes = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_cliente.php");
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", CARGAR_DATOS));
		postValoresVehiculo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String datos_json_clientes = convertStreamToString(entity.getContent());
			if(!datos_json_clientes.equals("0")){
				lista_clientes = parseJSONClientes(datos_json_clientes);
			}			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_clientes;
	}
	
	public List<Cliente> parseJSONClientes(String datos_json){
		List<Cliente> lista_clientes = new ArrayList<Cliente>();
		try{
			JSONArray jsonA = new JSONArray(datos_json);
			for (int i = 0; i < jsonA.length(); i++) {
				JSONObject jsonC = jsonA.getJSONObject(i);
				Cliente cliente = new Cliente(jsonC.getString("idcliente"), jsonC.getString("ci"), jsonC.getString("nombre"), 
						jsonC.getString("apellido"), Integer.parseInt(jsonC.getString("celular")), jsonC.getString("direccion"), 
						jsonC.getString("email"), jsonC.getString("sexo"), Date.valueOf(jsonC.getString("fecha_nac")), 
						jsonC.getString("imagen"), Integer.parseInt(jsonC.getString("estado")), Integer.parseInt(jsonC.getString("tipo")), 
						Date.valueOf(jsonC.getString("fecha_contrato")), jsonC.getString("idparqueo"));
				lista_clientes.add(cliente);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
		return lista_clientes;
	}
	
	//-----------------------------------------------VEHICULO------------------------------------------------
	
	public boolean verificarDisponibilidadPlacaVehiculo(String idparqueo, String placa){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", VERIFICAR_PLACA));
		postValoresVehiculo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		postValoresVehiculo.add(new BasicNameValuePair("placa_vehiculo", placa));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean insertarVehiculo(Vehiculo v){
		String query_insertar_vehiculo = "INSERT INTO vehiculo(idvehiculo, placa, marca, color, imagen, estado, idcliente) " +
				"VALUES ('"+v.getIdvehiculo()+"','"+v.getPlaca()+"','"+v.getMarca()+"'," +
				"'"+v.getColor()+"','"+v.getImagen()+"',"+v.getEstado()+",'"+v.getIdcliente()+"')";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", INSERTAR));
		postValoresVehiculo.add(new BasicNameValuePair("id_vehiculo", v.getIdvehiculo()));
		postValoresVehiculo.add(new BasicNameValuePair("query_insertar_vehiculo", query_insertar_vehiculo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean actualizarVehiculo(Vehiculo v){
		String query_actualizar_vehiculo = "UPDATE vehiculo SET placa='"+v.getPlaca()+"', marca='"+v.getMarca()+"', color='"+v.getColor()+"', " +
				"imagen='"+v.getImagen()+"', estado="+v.getEstado()+", idcliente='"+v.getIdcliente()+"' WHERE idvehiculo='"+v.getIdvehiculo()+"'";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion",ACTUALIZAR));
		postValoresVehiculo.add(new BasicNameValuePair("id_vehiculo", v.getIdvehiculo()));
		postValoresVehiculo.add(new BasicNameValuePair("query_actualizar_vehiculo", query_actualizar_vehiculo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean eliminarVehiculo(Vehiculo v){
		String query_eliminar_vehiculo = "UPDATE vehiculo SET estado="+v.getEstado()+" WHERE idvehiculo='"+v.getIdvehiculo()+"'";

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion",ELIMINAR));
		postValoresVehiculo.add(new BasicNameValuePair("id_vehiculo", v.getIdvehiculo()));
		postValoresVehiculo.add(new BasicNameValuePair("query_eliminar_vehiculo", query_eliminar_vehiculo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean restaurarVehiculo(Vehiculo v){
		String query_restaurar_vehiculo = "UPDATE vehiculo SET estado="+v.getEstado()+" WHERE idvehiculo='"+v.getIdvehiculo()+"'";

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion",RESTAURAR));
		postValoresVehiculo.add(new BasicNameValuePair("id_vehiculo", v.getIdvehiculo()));
		postValoresVehiculo.add(new BasicNameValuePair("query_restaurar_vehiculo", query_restaurar_vehiculo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String verificacion = EntityUtils.toString(entity);
			if(Integer.parseInt(verificacion)==1){
				return true;
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public Vehiculo getDatosVehiculo(String idvehiculo){
		Vehiculo vehiculo = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", GET_DATO));
		postValoresVehiculo.add(new BasicNameValuePair("id_vehiculo", idvehiculo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String json_vehiculo = convertStreamToString(entity.getContent());
			if(!json_vehiculo.equals("0")){
				vehiculo = getParseJSONVehiculo(json_vehiculo);
			}			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return vehiculo;
	}
	
	public Vehiculo getParseJSONVehiculo(String datos_json){
		Vehiculo vehiculo = null;
		try {
			JSONObject jsonV = new JSONObject(datos_json);
			vehiculo = new Vehiculo(jsonV.getString("idvehiculo"), jsonV.getString("placa"), jsonV.getString("marca"), 
					jsonV.getString("color"), jsonV.getString("imagen"), Integer.parseInt(jsonV.getString("estado")), jsonV.getString("idcliente"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vehiculo;
	}
	
	public List<Vehiculo> cargarVehiculos(String idparqueo){
		List<Vehiculo> lista_vehiculos = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_vehiculo.php");
		ArrayList<NameValuePair> postValoresVehiculo = new ArrayList<NameValuePair>();
		postValoresVehiculo.add(new BasicNameValuePair("accion", CARGAR_DATOS));
		postValoresVehiculo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresVehiculo);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String datos_json_vehiculos = convertStreamToString(entity.getContent());
			if(!datos_json_vehiculos.equals("0")){
				lista_vehiculos = parseJSONVehiculos(datos_json_vehiculos);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_vehiculos;
	}
	
	public List<Vehiculo> parseJSONVehiculos(String datos_json){
		List<Vehiculo> lista_vehiculos = new ArrayList<Vehiculo>();
		try{
			JSONArray jsonA = new JSONArray(datos_json);
			for (int i = 0; i < jsonA.length(); i++) {
				JSONObject jsonV = jsonA.getJSONObject(i);
				Vehiculo vehiculo = new Vehiculo(jsonV.getString("idvehiculo"), jsonV.getString("placa"), jsonV.getString("marca"), 
						jsonV.getString("color"), jsonV.getString("imagen"), Integer.parseInt(jsonV.getString("estado")), jsonV.getString("idcliente"));
				lista_vehiculos.add(vehiculo);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return lista_vehiculos;
	}
	
	//-------------------------------------------------PARQUEO-------------------------------------------------
	
	public boolean verificarDisponibilidadNombreParqueo(String nombre_parqueo){
		boolean control = false;
		String accion = "verificar_disponibilidad";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_parqueo.php");
		
		ArrayList<NameValuePair> postValor = new ArrayList<NameValuePair>();
		postValor.add(new BasicNameValuePair("accion", accion));
		postValor.add(new BasicNameValuePair("nombre_parqueo", nombre_parqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValor);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificar = EntityUtils.toString(entity);
			if(Integer.parseInt(verificar)==1){
				control=true;
			}
			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return control;
	}
	
	public boolean insertarParqueo(Parqueo p){
		boolean control = false;
		String query_insertar_parqueo = "INSERT INTO parqueo(idparqueo, nombre_parqueo, telf, direccion, tolerancia, capacidad, precioHoraDia, precioNoche," +
				"precioContratoNocturno, precioContratoDiurno, precioContratoDiaCompleto, inicioDia, finDia, inicioNoche, finNoche, logo)"+
				"VALUES ('"+p.getIdparqueo()+"','"+p.getNombreParqueo()+"',"+p.getTelf()+",'"+p.getDireccion()+"'" +
				","+p.getTolerancia()+","+p.getCapacidad()+","+p.getPrecioHoraDia()+","+p.getPrecioNoche()+"," +
			 	p.getPrecioContratoNocturno()+","+p.getPrecioContratoDiurno()+","+p.getPrecioContratoDiaCompleto()+"," +
			 	"'"+p.getInicioDia()+"','"+p.getFinDia()+"','"+p.getInicioNoche()+"','"+p.getFinNoche()+"','"+p.getLogo()+"')";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_parqueo.php");
		
		ArrayList<NameValuePair> postValoresParqueo = new ArrayList<NameValuePair>();
		postValoresParqueo.add(new BasicNameValuePair("accion", INSERTAR));
		postValoresParqueo.add(new BasicNameValuePair("id_parqueo", p.getIdparqueo()));
		postValoresParqueo.add(new BasicNameValuePair("query_insertar_parqueo", query_insertar_parqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresParqueo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificar = EntityUtils.toString(entity);
			if(Integer.parseInt(verificar)==1){
				control=true;
			}
			
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return control;
	}
	
	public boolean actualizarParqueo(Parqueo p){
		String query_actualizar_parqueo = "UPDATE parqueo SET nombre_parqueo='"+p.getNombreParqueo()+"', telf="+p.getTelf()+", " +
				"direccion='"+p.getDireccion()+"', tolerancia="+p.getTolerancia()+", " +
				"capacidad="+p.getCapacidad()+", precioHoraDia="+p.getPrecioHoraDia()+", precioNoche="+p.getPrecioNoche()+"," +
				"precioContratoNocturno="+p.getPrecioContratoNocturno()+", precioContratoDiurno="+p.getPrecioContratoDiurno()+", " +
				"precioContratoDiaCompleto="+p.getPrecioContratoDiaCompleto()+", inicioDia='"+p.getInicioDia()+"', finDia='"+p.getFinDia()+"', " +
				"inicioNoche='"+p.getInicioNoche()+"', finNoche='"+p.getFinNoche()+"', logo='"+p.getLogo()+"' WHERE idparqueo='"+p.getIdparqueo()+"'";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_parqueo.php");
		
		ArrayList<NameValuePair> postValoresParqueo = new ArrayList<NameValuePair>();
		postValoresParqueo.add(new BasicNameValuePair("accion", ACTUALIZAR));
		postValoresParqueo.add(new BasicNameValuePair("id_parqueo", p.getIdparqueo()));
		postValoresParqueo.add(new BasicNameValuePair("query_actualizar_parqueo", query_actualizar_parqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresParqueo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificar = EntityUtils.toString(entity);
			if(Integer.parseInt(verificar)==1){
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
	
	public boolean actualizarCapacidadParqueo(Parqueo p){
		String query_actualizar_capacidad_parqueo ="UPDATE parqueo SET capacidad="+p.getCapacidad()+" WHERE idparqueo='"+p.getIdparqueo()+"'";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_parqueo.php");		
		ArrayList<NameValuePair> postValoresParqueo = new ArrayList<NameValuePair>();
		postValoresParqueo.add(new BasicNameValuePair("accion", "actualizar_capacidad"));
		postValoresParqueo.add(new BasicNameValuePair("id_parqueo", p.getIdparqueo()));
		postValoresParqueo.add(new BasicNameValuePair("query_actualizar_capacidad_parqueo", query_actualizar_capacidad_parqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresParqueo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verificar = EntityUtils.toString(entity);
			if(Integer.parseInt(verificar)==1){
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
	
	public Parqueo cargarParqueo(String idparqueo){
		Parqueo parqueo = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_parqueo.php");
		
		ArrayList<NameValuePair> postValoresParqueo = new ArrayList<NameValuePair>();
		postValoresParqueo.add(new BasicNameValuePair("accion", CARGAR_DATOS));
		postValoresParqueo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresParqueo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String json_parqueo = convertStreamToString(entity.getContent());
			if(!json_parqueo.equals("0")){
				parqueo = getParseJSONParqueo(json_parqueo);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return parqueo;
	}
	
	public Parqueo getParseJSONParqueo(String datos_json){
		Parqueo parqueo = null;
		try{
			JSONObject jsonO = new JSONObject(datos_json);
			
			parqueo = new Parqueo(jsonO.getString("idparqueo"), jsonO.getString("nombre_parqueo"), Integer.parseInt(jsonO.getString("telf")), 
										jsonO.getString("direccion"),Integer.parseInt(jsonO.getString("tolerancia")), 
										Integer.parseInt(jsonO.getString("capacidad")), Double.valueOf(jsonO.getString("precioHoraDia")), 
										Double.valueOf(jsonO.getString("precioNoche")), Double.valueOf(jsonO.getString("precioContratoNocturno")),
										Double.valueOf(jsonO.getString("precioContratoDiurno")),Double.valueOf(jsonO.getString("precioContratoDiaCompleto")),
										Time.valueOf(jsonO.getString("inicioDia")),Time.valueOf(jsonO.getString("finDia")),
										Time.valueOf(jsonO.getString("inicioNoche")),Time.valueOf(jsonO.getString("finNoche")),
										jsonO.getString("logo"));
		} catch(JSONException e){
			e.printStackTrace();
		}
		return parqueo;
	}
	
	//---------------------------------------------------CARRIL--------------------------------------------------
	
	public boolean insertarCarril(Carril c){
		boolean control = false;
		String query_insertar_carril = "INSERT INTO carril(idcarril, num_carril, disponible, reservado, estado, idparqueo) VALUES ("+
										  "'"+c.getIdcarril()+"',"+c.getNum_carril()+",'"+c.getDisponible()+"','"+c.getReservado()+"'," +
										  c.getEstado()+",'"+c.getIdpaqueo()+"')";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", INSERTAR));
		postValoresCarril.add(new BasicNameValuePair("id_carril", c.getIdcarril()));
		postValoresCarril.add(new BasicNameValuePair("query_insertar_carril", query_insertar_carril));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				control = true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return control;
	}
	
	public boolean actualizarCarril(Carril c){
		String query_actualizar_carril = "UPDATE carril SET disponible='"+c.getDisponible()+"', reservado='"+c.getReservado()+"', estado="+c.getEstado()+" " +
											"WHERE idcarril='"+c.getIdcarril()+"'";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", ACTUALIZAR));
		postValoresCarril.add(new BasicNameValuePair("id_carril", c.getIdcarril()));
		postValoresCarril.add(new BasicNameValuePair("query_actualizar_carril", query_actualizar_carril));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean ocuparCarril(String idvehiculo, Carril c){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", "ocupar_carril"));
		postValoresCarril.add(new BasicNameValuePair("id_carril", c.getIdcarril()));
		postValoresCarril.add(new BasicNameValuePair("id_vehiculo", idvehiculo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean desocuparCarril(Carril c){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", "desocupar_carril"));
		postValoresCarril.add(new BasicNameValuePair("id_carril", c.getIdcarril()));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean habilitarCarril(Carril c){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", "habilitar_carril"));
		postValoresCarril.add(new BasicNameValuePair("id_carril", c.getIdcarril()));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deshabilitarCarril(Carril c){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", "deshabilitar_carril"));
		postValoresCarril.add(new BasicNameValuePair("id_carril", c.getIdcarril()));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Carril> cargarCarriles(String idparqueo){
		List<Carril> lista_carriles = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_carril.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", CARGAR_DATOS));
		postValoresCarril.add(new BasicNameValuePair("id_parqueo", idparqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String datos_carriles = convertStreamToString(entity.getContent());
			if(!datos_carriles.equals("0")){
				lista_carriles = parseJSONCarriles(datos_carriles);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_carriles;
	}
	
	public List<Carril> parseJSONCarriles(String datos_json){
		List<Carril> lista_carriles = new ArrayList<Carril>();
		try{
			JSONArray JsonArray = new JSONArray(datos_json);
			for (int i = 0; i < JsonArray.length(); i++) {
				JSONObject jsonCarril = JsonArray.getJSONObject(i);
				Carril carril = new Carril(jsonCarril.getString("idcarril"), Integer.parseInt(jsonCarril.getString("num_carril")), 
											jsonCarril.getString("disponible"), jsonCarril.getString("reservado"), 
											Integer.parseInt(jsonCarril.getString("estado")), jsonCarril.getString("idparqueo"));
				lista_carriles.add(carril);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
		return lista_carriles;
	}
	
	//--------------------------------------------------RESGUARDO------------------------------------------------
	
	public boolean insertarResguardo(Resguardo r){
		String query_insertar_resguardo = "INSERT INTO resguardo(idresguardo, horaE, fechaE, costoTotal, estado, nota, tipo, idcarril, idvehiculo) " +
				"VALUES ('"+r.getIdresguardo()+"','"+r.getHoraE()+"','"+r.getFechaE()+"',"+r.getCostoTotal()+","+r.getEstado()+"," +
				"'"+r.getNota()+"',"+r.getTipo()+",'"+r.getIdcarril()+"','"+r.getIdvehiculo()+"')";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", INSERTAR));
		postValoresCarril.add(new BasicNameValuePair("id_resguardo", r.getIdresguardo()));
		postValoresCarril.add(new BasicNameValuePair("query_insertar_resguardo", query_insertar_resguardo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
				return true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean actualizarResguardo(Resguardo r){
		String query_actualizar_resguardo = "UPDATE resguardo SET horaS='"+r.getHoraS()+"', fechaS='"+r.getFechaS()+"', costoDia="+r.getCostoDia()+
				", costoNoche="+r.getCostoNoche()+", costoTotal="+r.getCostoTotal()+", nota='"+r.getNota()+"', tipo="+r.getTipo()+
				" WHERE idresguardo='"+r.getIdresguardo()+"' AND idcarril='"+r.getIdcarril()+"'";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", ACTUALIZAR));
		postValoresCarril.add(new BasicNameValuePair("id_resguardo", r.getIdresguardo()));
		postValoresCarril.add(new BasicNameValuePair("query_actualizar_resguardo", query_actualizar_resguardo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
			return true;
		}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean editarNotaResguardo(Resguardo resg){
		String query_editar_nota_resguardo = "UPDATE resguardo SET nota='"+resg.getNota()+"' WHERE idresguardo='"+resg.getIdresguardo()+"'";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValoresCarril = new ArrayList<NameValuePair>();
		postValoresCarril.add(new BasicNameValuePair("accion", EDITAR_NOTA));
		postValoresCarril.add(new BasicNameValuePair("query_editar_nota_resguardo", query_editar_nota_resguardo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresCarril);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			if(Integer.parseInt(verify)==1){
			return true;
		}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Resguardo> cargarIngresosResguardos(String idparqueo){
		List<Resguardo> lista_ingresos = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValorResguardo = new ArrayList<NameValuePair>();
		postValorResguardo.add(new BasicNameValuePair("accion", CARGAR_ENTRADAS));
		postValorResguardo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorResguardo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String datos_ingresos_resguardo = convertStreamToString(entity.getContent());
			if(!datos_ingresos_resguardo.equals("0")){
				lista_ingresos = parseJSONIngresosResguardos(datos_ingresos_resguardo);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_ingresos;
	}
	
	protected List<Resguardo> parseJSONIngresosResguardos(String datos_json){
		List<Resguardo> lista_ingresos = new ArrayList<Resguardo>();
		try{
			JSONArray jsonA = new JSONArray(datos_json);
			for (int i = 0; i < jsonA.length(); i++) {
				JSONObject jsonR = jsonA.getJSONObject(i);
				Resguardo resguardo =  new Resguardo(jsonR.getString("idresguardo"), Time.valueOf(jsonR.getString("horaE")), 
												Date.valueOf(jsonR.getString("fechaE")), Double.valueOf(jsonR.getString("costoTotal")), 
												Integer.parseInt(jsonR.getString("estado")), jsonR.getString("nota"), Integer.parseInt(jsonR.getString("tipo")),
												jsonR.getString("idcarril"), jsonR.getString("idvehiculo"));
				lista_ingresos.add(resguardo);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
		return lista_ingresos;
	}
	
	public List<Resguardo> cargarResguardos(String idparqueo){
		List<Resguardo> lista_resguardo = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValorResguardo = new ArrayList<NameValuePair>();
		postValorResguardo.add(new BasicNameValuePair("accion", "ALL_RESGUARDOS"));
		postValorResguardo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorResguardo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String datos_resguardos = convertStreamToString(entity.getContent());
			if(!datos_resguardos.equals("0")){
				lista_resguardo = parseJSONResguardos(datos_resguardos);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_resguardo;
	}
	
	public List<Resguardo> cargarResguardosPorFecha(String idparqueo, Date fecha_actual, Date fecha_antes){		
		List<Resguardo> lista_resguardos = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValorResguardo = new ArrayList<NameValuePair>();
		postValorResguardo.add(new BasicNameValuePair("accion", "RESGUARDOS_FECHA"));
		postValorResguardo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		postValorResguardo.add(new BasicNameValuePair("fecha_antes", fecha_antes.toString()));
		postValorResguardo.add(new BasicNameValuePair("fecha_actual", fecha_actual.toString()));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorResguardo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String datos_resguardos = convertStreamToString(entity.getContent());
			if(!datos_resguardos.equals("0")){
				lista_resguardos = parseJSONResguardos(datos_resguardos);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_resguardos;
	}
		
	public List<Resguardo> cargarResguardosPorMes(String idparqueo, Date fecha_mes){
		List<Resguardo> lista_resguardo = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_resguardo.php");
		ArrayList<NameValuePair> postValorResguardo = new ArrayList<NameValuePair>();
		postValorResguardo.add(new BasicNameValuePair("accion", "RESGUARDOS_MES"));
		postValorResguardo.add(new BasicNameValuePair("id_parqueo", idparqueo));
		postValorResguardo.add(new BasicNameValuePair("fecha_mes", fecha_mes.toString()));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorResguardo);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String datos_resguardos = convertStreamToString(entity.getContent());
			if(!datos_resguardos.equals("0")){
				lista_resguardo = parseJSONResguardos(datos_resguardos);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e ){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_resguardo;
	}
	
	protected List<Resguardo> parseJSONResguardos(String datos_json){
		List<Resguardo> lista_resguardos = new ArrayList<Resguardo>();
		try{
			JSONArray JsonArray = new JSONArray(datos_json);
			for (int i = 0; i < JsonArray.length(); i++) {
				JSONObject jsonR = JsonArray.getJSONObject(i);
				Resguardo resg = new Resguardo(jsonR.getString("idresguardo"), Time.valueOf(jsonR.getString("horaE")), Date.valueOf(jsonR.getString("fechaE")), 
						Time.valueOf(jsonR.getString("horaS")), Date.valueOf(jsonR.getString("fechaS")), Double.valueOf(jsonR.getString("costoDia")), 
						Double.valueOf(jsonR.getString("costoNoche")),Double.valueOf(jsonR.getString("costoTotal")), Integer.parseInt(jsonR.getString("estado")),
						jsonR.getString("nota"), Integer.parseInt(jsonR.getString("tipo")), jsonR.getString("idcarril"), jsonR.getString("idvehiculo"));
				
				lista_resguardos.add(resg);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
		return lista_resguardos;
	}
	
	//---------------------------------------------------USUARIO---------------------------------------------------
	
	public boolean verificarDisponibilidadCIUsuario(String idparqueo, String ci_usuario){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValoresUsuario = new ArrayList<NameValuePair>();
		postValoresUsuario.add(new BasicNameValuePair("accion", VERIFICAR_CI));
		postValoresUsuario.add(new BasicNameValuePair("id_parqueo", idparqueo));
		postValoresUsuario.add(new BasicNameValuePair("ci_usuario", ci_usuario));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresUsuario);
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
	
	public boolean verificarDisponibilidadNombreUsuario(String nombre_usuario){
		String accion = "verificar_disponibilidad_nombre_usuario";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValoresUsuario = new ArrayList<NameValuePair>();
		postValoresUsuario.add(new BasicNameValuePair("accion", accion));
		postValoresUsuario.add(new BasicNameValuePair("nombre_usuario", nombre_usuario));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresUsuario);
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
	
	public boolean insertarUsuario(Usuario u){
		boolean control = false;
		String query_insertar_usuario = "INSERT INTO usuario(idusuario, ci, nombre, apellido, nombre_usuario, clave, celular, direccion, email, sexo, " +
										 "fecha_nac, cargo, estado, imagen, idparqueo) " +
										 "VALUES ('"+u.getIdusuario()+"',"+u.getCi()+",'"+u.getNombre()+"','"+u.getApellido()+"','"+u.getNombre_usuario()+"'" +
										 ",'"+u.getClave()+"',"+u.getCelular()+",'"+u.getDireccion()+"','"+u.getEmail()+"','"+u.getSexo()+"'" +
										 ",'"+u.getFecha_nac()+"',"+u.getCargo()+","+u.getEstado()+",'"+u.getImagen()+"','"+u.getIdparqueo()+"')";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValoresUsuario = new ArrayList<NameValuePair>();
		postValoresUsuario.add(new BasicNameValuePair("accion", INSERTAR));
		postValoresUsuario.add(new BasicNameValuePair("id_usuario", u.getIdusuario()));
		postValoresUsuario.add(new BasicNameValuePair("query_insertar_usuario", query_insertar_usuario));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresUsuario);
			httpPost.setEntity(encodeEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String verify = EntityUtils.toString(entity);
			
			if(Integer.parseInt(verify)==1){
				control = true;
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return control;
	}
	
	public boolean actualizarUsuario(Usuario u){
		String query_actualizar_usuario = "UPDATE usuario SET ci='"+u.getCi()+"', nombre='"+u.getNombre()+"', apellido='"+u.getApellido()+"', " +
											"celular="+u.getCelular()+", direccion='"+u.getDireccion()+"', email='"+u.getEmail()+"', sexo='"+u.getSexo()+"', " +
											"fecha_nac='"+u.getFecha_nac()+"', imagen='"+u.getImagen()+"' WHERE idusuario='"+u.getIdusuario()+"'";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValoresUsuario = new ArrayList<NameValuePair>();
		postValoresUsuario.add(new BasicNameValuePair("accion", ACTUALIZAR));
		postValoresUsuario.add(new BasicNameValuePair("id_usuario", u.getIdusuario()));
		postValoresUsuario.add(new BasicNameValuePair("query_actualizar_usuario", query_actualizar_usuario));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresUsuario);
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
	
	public boolean cambiarClaveUsuario(Usuario u){
		String query_nuevo_clave_usuario = "UPDATE usuario SET nombre_usuario='"+u.getNombre_usuario()+"', clave='"+u.getClave()+"' " +
												"WHERE idusuario='"+u.getIdusuario()+"'";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValoresUsuario = new ArrayList<NameValuePair>();
		postValoresUsuario.add(new BasicNameValuePair("accion", CAMBIAR_CLAVE));
		postValoresUsuario.add(new BasicNameValuePair("id_usuario", u.getIdusuario()));
		postValoresUsuario.add(new BasicNameValuePair("query_nuevo_clave_usuario", query_nuevo_clave_usuario));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresUsuario);
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
	
	public boolean habilitarDeshabilitarUsuario(Usuario u, int estado){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValoresUsuario = new ArrayList<NameValuePair>();
		postValoresUsuario.add(new BasicNameValuePair("accion", "habilitar_deshabilitar"));
		postValoresUsuario.add(new BasicNameValuePair("id_usuario", u.getIdusuario()));
		postValoresUsuario.add(new BasicNameValuePair("estado", String.valueOf(estado)));
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValoresUsuario);
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
	
	public Usuario autenticarUsuario(Usuario usuario){
		Usuario usuario_aut = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValorUsuario = new ArrayList<NameValuePair>();
		postValorUsuario.add(new BasicNameValuePair("accion", AUTENTICAR));
		postValorUsuario.add(new BasicNameValuePair("nombre_usuario", usuario.getNombre_usuario()));
		postValorUsuario.add(new BasicNameValuePair("clave", usuario.getClave()));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorUsuario);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String json_usuario = convertStreamToString(entity.getContent());
			if(!json_usuario.equals("0")){
				usuario_aut = getParseJSONUsuario(json_usuario);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return usuario_aut;
	}
	
	public Usuario getDatosUsuario(String idusuario){
		Usuario usuario = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValorUsuario = new ArrayList<NameValuePair>();
		postValorUsuario.add(new BasicNameValuePair("accion", GET_DATO));
		postValorUsuario.add(new BasicNameValuePair("id_usuario", idusuario));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorUsuario);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String json_usuario = convertStreamToString(entity.getContent());
			if(!json_usuario.equals("0")){
				usuario = getParseJSONUsuario(json_usuario);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return usuario;
	}
	
	public Usuario getParseJSONUsuario(String datos_json){
		Usuario usuario = null;
		try {
			JSONObject jsonO = new JSONObject(datos_json);
			usuario = new Usuario(jsonO.getString("idusuario"), jsonO.getString("ci"), jsonO.getString("nombre"), 
								jsonO.getString("apellido"), jsonO.getString("nombre_usuario"), jsonO.getString("clave"), 
								Integer.parseInt(jsonO.getString("celular")), jsonO.getString("direccion"), jsonO.getString("email"), jsonO.getString("sexo"),
								Date.valueOf(jsonO.getString("fecha_nac")), Integer.valueOf(jsonO.getString("cargo")), Integer.valueOf(jsonO.getString("estado")), 
								jsonO.getString("imagen"), jsonO.getString("idparqueo"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return usuario;
	}
	
	public List<Usuario> cargarUsuarios(Parqueo parqueo, Usuario usuario){
		List<Usuario> lista_usuarios = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MyVar.RUTA_SERVER+"handle_table_usuario.php");
		ArrayList<NameValuePair> postValorUsuario = new ArrayList<NameValuePair>();
		postValorUsuario.add(new BasicNameValuePair("accion", CARGAR_DATOS));
		postValorUsuario.add(new BasicNameValuePair("id_parqueo", parqueo.getIdparqueo()));
		postValorUsuario.add(new BasicNameValuePair("id_usuario", usuario.getIdusuario()));
		
		try{
			UrlEncodedFormEntity encodeEntity = new UrlEncodedFormEntity(postValorUsuario);
			httpPost.setEntity(encodeEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String datos_json_usuarios = convertStreamToString(entity.getContent());
			if(!datos_json_usuarios.equals("0")){
				lista_usuarios = parseJSONUsuarios(datos_json_usuarios);
			}
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return lista_usuarios;
	}
	
	public List<Usuario> parseJSONUsuarios(String json_usuarios){
		List<Usuario> lista_usuarios = new ArrayList<Usuario>();
		try{
			JSONArray jsonA = new JSONArray(json_usuarios);
			for (int i = 0; i < jsonA.length(); i++) {
				JSONObject JOU = jsonA.getJSONObject(i);
				Usuario usuario = new Usuario(JOU.getString("idusuario"), JOU.getString("ci"), JOU.getString("nombre"),
						JOU.getString("apellido"), JOU.getString("nombre_usuario"), JOU.getString("clave"), Integer.parseInt(JOU.getString("celular")),
						JOU.getString("direccion"), JOU.getString("email"), JOU.getString("sexo"), Date.valueOf(JOU.getString("fecha_nac")),
						Integer.parseInt(JOU.getString("cargo")), Integer.parseInt(JOU.getString("estado")), JOU.getString("imagen"), 
						JOU.getString("idparqueo"));
				lista_usuarios.add(usuario);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return lista_usuarios;
	}
	
	//-------------------------------------------------------OTROS-----------------------------------------------
	
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
