package com.moisse.database;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Usuario;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.MyVar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBParqueo {

	private DBHelper helper;
	private Context contexto;
	private SQLiteDatabase sqlitedb;

	private static final int todos = 2;
	
	public DBParqueo(Context contexto){
		this.contexto=contexto;
	}
	
	public DBParqueo openSQLite(){
		helper = new DBHelper(contexto);
		sqlitedb = helper.getWritableDatabase();
		return this;
	}
	
	public void closeSQLite(){
		sqlitedb.close();
	}
	
	//-----------------------------------------TABLA PARQUEO-------------------------------
	
	public boolean insertarParqueo(Parqueo p){
		String sql = "INSERT INTO parqueo(idparqueo, nombre_parqueo, telf, direccion, tolerancia, capacidad, precioHoraDia, precioNoche," +
						"precioContratoNocturno, precioContratoDiurno, precioContratoDiaCompleto, inicioDia, finDia, inicioNoche, finNoche, logo)"+
						"VALUES ('"+p.getIdparqueo()+"','"+p.getNombreParqueo()+"',"+p.getTelf()+",'"+p.getDireccion()+"'" +
						","+p.getTolerancia()+","+p.getCapacidad()+","+p.getPrecioHoraDia()+","+p.getPrecioNoche()+"," +
					 	p.getPrecioContratoNocturno()+","+p.getPrecioContratoDiurno()+","+p.getPrecioContratoDiaCompleto()+"," +
					 	"'"+p.getInicioDia()+"','"+p.getFinDia()+"','"+p.getInicioNoche()+"','"+p.getFinNoche()+"','"+p.getLogo()+"')";
		sqlitedb.execSQL(sql);
		return verificarParqueo(p.getIdparqueo());		
	}
	
	public boolean actualizarParqueo(Parqueo p){
		String query = "UPDATE parqueo SET nombre_parqueo='"+p.getNombreParqueo()+"', telf="+p.getTelf()+", direccion='"+p.getDireccion()+"', " +
				"tolerancia="+p.getTolerancia()+", capacidad="+p.getCapacidad()+", precioHoraDia="+p.getPrecioHoraDia()+", precioNoche="+p.getPrecioNoche()+"," +
				"precioContratoNocturno="+p.getPrecioContratoNocturno()+", precioContratoDiurno="+p.getPrecioContratoDiurno()+", " +
				"precioContratoDiaCompleto="+p.getPrecioContratoDiaCompleto()+", inicioDia='"+p.getInicioDia()+"', finDia='"+p.getFinDia()+"', " +
				"inicioNoche='"+p.getInicioNoche()+"', finNoche='"+p.getFinNoche()+"', logo='"+p.getLogo()+"' WHERE idparqueo='"+p.getIdparqueo()+"'";
		
		sqlitedb.execSQL(query);
		return verificarParqueo(p.getIdparqueo());		
	}
	
	public boolean actualizarCapacidadParqueo(Parqueo p){
		String query = "UPDATE parqueo SET capacidad="+p.getCapacidad()+" WHERE idparqueo='"+p.getIdparqueo()+"'";
		sqlitedb.execSQL(query);
		return verificarParqueo(p.getIdparqueo());		
	}
	
	public boolean verificarParqueo(String idparqueo){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM parqueo WHERE idparqueo='"+idparqueo+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Parqueo getParqueo(String idParqueo){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM parqueo WHERE idparqueo='"+idParqueo+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				Parqueo parqueo = new Parqueo(c.getString(0), c.getString(1), c.getInt(2), c.getString(3), c.getInt(4), c.getInt(5), c.getDouble(6), 
												c.getDouble(7), c.getDouble(8), c.getDouble(9), c.getDouble(10),Time.valueOf(c.getString(11)),
												Time.valueOf(c.getString(12)), Time.valueOf(c.getString(13)), Time.valueOf(c.getString(14)), c.getString(15));
				return parqueo;
			}
		}
		return null;
	}
	
	public List<Parqueo> getAllParqueo(){
		List<Parqueo> listParqueo = new ArrayList<Parqueo>();
		Cursor c = sqlitedb.rawQuery("SELECT * FROM parqueo", null);
		for (c.moveToFirst();!c.isAfterLast(); c.moveToNext()) {
			Parqueo p = new Parqueo(c.getString(0), c.getString(1), c.getInt(2), c.getString(3), c.getInt(4), c.getInt(5), c.getDouble(6), 
									c.getDouble(7), c.getDouble(8), c.getDouble(9), c.getDouble(10),Time.valueOf(c.getString(11)),
									Time.valueOf(c.getString(12)), Time.valueOf(c.getString(13)), Time.valueOf(c.getString(14)), c.getString(15));
			listParqueo.add(p);
		}
		return listParqueo;
	}
	
	/*------------------------------------------TABLA CARRIL---------------------------------------------
	Si no esta disponible el carril entonces la placa del vehiculo va estar ahi asi se sobre entiendo que no esta
	disponible de igual manera para el campo de reservado*/
	
	public boolean insertarCarril(Carril c){
		String sql = "INSERT INTO carril(idcarril, num_carril, disponible, reservado, estado, idparqueo) VALUES ("+
					  "'"+c.getIdcarril()+"',"+c.getNum_carril()+",'"+c.getDisponible()+"','"+c.getReservado()+"'," +
					  c.getEstado()+",'"+c.getIdpaqueo()+"')";
		sqlitedb.execSQL(sql);
		return existeCarril(c);
	}
	
	public boolean actualizarCarril(Carril c){
		String sql = "UPDATE carril SET disponible='"+c.getDisponible()+"', reservado='"+c.getReservado()+"', estado="+c.getEstado()+" " +
										"WHERE idcarril='"+c.getIdcarril()+"'";
		sqlitedb.execSQL(sql);
		return existeCarril(c);
	}
	
	public boolean verificarCarriles(Parqueo p){
		Cursor cu = sqlitedb.rawQuery("SELECT COUNT(idparqueo) FROM carril WHERE idparqueo='"+p.getIdparqueo()+"'", null);
		if(cu!=null){
			if(cu.moveToFirst()){
				if(p.getCapacidad()==cu.getInt(0)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean verificarCarril(Carril c){
		Cursor cu = sqlitedb.rawQuery("SELECT * FROM carril WHERE idcarril='"+c.getIdcarril()+"' AND estado="+MyVar.NO_ELIMINADO, null);
		if(cu!=null){
			if(cu.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean existeCarril(Carril c){
		Cursor cu = sqlitedb.rawQuery("SELECT * FROM carril WHERE idcarril='"+c.getIdcarril()+"'", null);
		if(cu!=null){
			if(cu.moveToFirst()){
				return true;
			}
		}
		return false;
	}	
	
	public List<Carril> getAllCarril(Parqueo p){
		List<Carril> listCarril = new ArrayList<Carril>();
		Cursor c = sqlitedb.rawQuery("SELECT * FROM carril WHERE idparqueo='"+p.getIdparqueo()+"' ORDER BY num_carril ASC", null);
		for (c.moveToFirst();!c.isAfterLast(); c.moveToNext()) {
			Carril ca = new Carril(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5));
			listCarril.add(ca);
		}
		return listCarril;
	}
	
	public List<Carril> getCarrilesParaParking(Parqueo p){
		List<Carril> listCarril = new ArrayList<Carril>();
		Cursor c = sqlitedb.rawQuery("SELECT * FROM carril WHERE idparqueo='"+p.getIdparqueo()+"' AND estado="+MyVar.NO_ELIMINADO+" ORDER BY num_carril ASC", null);
		for (c.moveToFirst();!c.isAfterLast(); c.moveToNext()) {
			Carril ca = new Carril(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5));
			listCarril.add(ca);
		}
		return listCarril;
	}

	public Carril getCarril(String idcarril){
		Carril ca = null;
		Cursor c = sqlitedb.rawQuery("SELECT * FROM carril WHERE idcarril='"+idcarril+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				ca = new Carril(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5));
				return ca;
			}
		}
		return ca;
	}
	
	public boolean setOcuparCarril(String idvehiculo, Carril c){
		String sql = "UPDATE carril SET disponible='"+idvehiculo+"' WHERE idcarril='"+c.getIdcarril()+"'";
		sqlitedb.execSQL(sql);
		return verificarPlacaEnCarril(idvehiculo, c);
	}
	
	public boolean setDesocuparCarril(Carril c){
		String sql = "UPDATE carril SET disponible='"+MyVar.SI+"' WHERE idcarril='"+c.getIdcarril()+"'";
		sqlitedb.execSQL(sql);
		return verificarPlacaEnCarril(MyVar.SI, c);
	}
	
	public boolean habilitarCarril(Carril c){
		String sql = "UPDATE carril SET estado="+MyVar.NO_ELIMINADO+" WHERE idcarril='"+c.getIdcarril()+"'";
		sqlitedb.execSQL(sql);
		return verificarCarril(c);
	}
	
	public boolean deshabilitarCarril(Carril c){
		String sql = "UPDATE carril SET estado="+MyVar.ELIMINADO+" WHERE idcarril='"+c.getIdcarril()+"'";
		sqlitedb.execSQL(sql);
		if(verificarCarril(c)){
			return false;
		}else{
			return true;
		}
	}

	public boolean verificarPlacaEnCarril(String var, Carril carril){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM carril WHERE disponible='"+var+"' AND idparqueo='"+carril.getIdpaqueo()+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Carril getCarrilConVehiculo(String idvehiculo, String idparqueo){
		Carril ca = null;
		Cursor c = sqlitedb.rawQuery("SELECT * FROM carril WHERE disponible='"+idvehiculo+"' AND idparqueo='"+idparqueo+"'", null);
		if(c!=null){
			if(c.moveToLast()){
				ca = new Carril(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5));
				return ca;
			}
		}
		return ca;
	}
	
	//----------------------------------------TABLA USUARIO--------------------------------------
	
	public boolean insertarUsuario(Usuario u){
		String sql = "INSERT INTO usuario(idusuario, ci, nombre, apellido, nombre_usuario, clave, celular, direccion, email, sexo, " +
					 "fecha_nac, cargo, estado, imagen, idparqueo) " +
					 "VALUES ('"+u.getIdusuario()+"','"+u.getCi()+"','"+u.getNombre()+"','"+u.getApellido()+"','"+u.getNombre_usuario()+"'" +
					 ",'"+u.getClave()+"',"+u.getCelular()+",'"+u.getDireccion()+"','"+u.getEmail()+"','"+u.getSexo()+"'" +
					 ",'"+u.getFecha_nac()+"',"+u.getCargo()+","+u.getEstado()+",'"+u.getImagen()+"','"+u.getIdparqueo()+"')";
		sqlitedb.execSQL(sql);
		return verificarUsuario(u.getIdusuario());
	}
	
	public boolean actualizarUsuario(Usuario u){
		String sql = "UPDATE usuario SET ci='"+u.getCi()+"', nombre='"+u.getNombre()+"', apellido='"+u.getApellido()+"', celular="+u.getCelular()+", " +
						"direccion='"+u.getDireccion()+"', email='"+u.getEmail()+"', sexo='"+u.getSexo()+"', fecha_nac='"+u.getFecha_nac()+"', " +
							"imagen='"+u.getImagen()+"' WHERE idusuario='"+u.getIdusuario()+"'";
		sqlitedb.execSQL(sql);
		return verificarUsuario(u.getIdusuario());
	}
	
	public boolean cambiarClaveUsuario(Usuario u){
		String sql = "UPDATE usuario SET nombre_usuario='"+u.getNombre_usuario()+"', clave='"+u.getClave()+"' WHERE idusuario='"+u.getIdusuario()+"'";
		sqlitedb.execSQL(sql);
		return verificarUsuario(u.getIdusuario());
	}
	
	public boolean verificarUsuario(String idusuario){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM usuario WHERE idusuario='"+idusuario+"' AND estado="+MyVar.NO_ELIMINADO, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean existeUsuario(String idusuario){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM usuario WHERE idusuario='"+idusuario+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean verificarDisponibilidadNombreUsuario(Usuario u){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM usuario WHERE nombre_usuario='"+u.getNombre_usuario()+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Usuario getUsuario(String idusuario){
		Usuario user = null;
		Cursor c = sqlitedb.rawQuery("SELECT * FROM usuario WHERE idusuario='"+idusuario+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				user = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5),
						c.getInt(6), c.getString(7), c.getString(8), c.getString(9), Date.valueOf(c.getString(10)), 
						c.getInt(11), c.getInt(12), c.getString(13), c.getString(14));
			}
		}
		return user;
	}
	
	public boolean setEstadoUsuario(Usuario u, int estado){
		String sql = "UPDATE usuario SET estado="+estado+" WHERE idusuario='"+u.getIdusuario()+"'";
		sqlitedb.execSQL(sql);
		return verificarUsuario(u.getIdusuario());
	}
	
	public Usuario getUsuarioAutenticacion(Usuario usuario){
		Usuario user = null;
		Cursor c = sqlitedb.rawQuery("SELECT * FROM usuario WHERE nombre_usuario='"+usuario.getNombre_usuario()+"' AND clave='"+usuario.getClave()+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				user = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5),
						c.getInt(6), c.getString(7), c.getString(8), c.getString(9), Date.valueOf(c.getString(10)), 
						c.getInt(11), c.getInt(12), c.getString(13), c.getString(14));
			}
		}
		return user;
	}
	
	public List<Usuario> getAllUsuario(String idparqueo){
		List<Usuario> listUsuario = new ArrayList<Usuario>();
		Cursor c = sqlitedb.rawQuery("SELECT * FROM usuario WHERE idparqueo='"+idparqueo+"' ORDER BY nombre_usuario ASC", null);
		for (c.moveToFirst();!c.isAfterLast(); c.moveToNext()) {
			Usuario u = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5),
						c.getInt(6), c.getString(7), c.getString(8), c.getString(9), Date.valueOf(c.getString(10)), 
						c.getInt(11), c.getInt(12), c.getString(13), c.getString(14));
			listUsuario.add(u);
		}
		return listUsuario;
	}
	
	//------------------------------------------TABLA CLIENTE--------------------------------------------
	
	public boolean insertarCliente(Cliente c){
		String sql = "INSERT INTO cliente(idcliente, ci, nombre, apellido, celular, direccion, email, sexo, fecha_nac, " +
						"imagen, estado, tipo, fecha_contrato, idparqueo) " +
						"VALUES ('"+c.getIdcliente()+"','"+c.getCi()+"','"+c.getNombre()+"','"+c.getApellido()+"',"+c.getCelular()+"," +
						"'"+c.getDireccion()+"','"+c.getEmail()+"','"+c.getSexo()+"','"+c.getFecha_nac()+"',"+
						"'"+c.getImagen()+"',"+c.getEstado()+","+c.getTipo()+",'"+c.getFecha_contrato()+"','"+c.getIdparqueo()+"')";
		sqlitedb.execSQL(sql);
		return verificarCliente(c.getIdcliente());
	}
	
	public boolean actualizarCliente(Cliente c){
		String sql = "UPDATE cliente SET ci='"+c.getCi()+"', nombre='"+c.getNombre()+"', apellido='"+c.getApellido()+"', celular="+c.getCelular()+", " +
						"direccion='"+c.getDireccion()+"', email='"+c.getEmail()+"', sexo='"+c.getSexo()+"', fecha_nac='"+c.getFecha_nac()+"', " +
							"imagen='"+c.getImagen()+"', estado="+c.getEstado()+", tipo="+c.getTipo()+", fecha_contrato='"+c.getFecha_contrato()+"' " +
								"WHERE idcliente='"+c.getIdcliente()+"'";
		sqlitedb.execSQL(sql);
		return verificarCliente(c.getIdcliente());
	}
	
	public boolean eliminarCliente(String idcliente){		
		String query1 = "UPDATE cliente SET estado="+MyVar.ELIMINADO+" WHERE idcliente='"+idcliente+"'";
		sqlitedb.execSQL(query1);
		if(!verificarCliente(idcliente)){
			return true;
		}else{
			return false;
		}
	}
		
	public boolean restaurarCliente(String idcliente){
		String sql = "UPDATE cliente SET estado="+MyVar.NO_ELIMINADO+" WHERE idcliente='"+idcliente+"'";
		sqlitedb.execSQL(sql);
		return verificarCliente(idcliente);
	}
	
	public boolean verificarCliente(String idcliente) {
		Cursor c = sqlitedb.rawQuery("SELECT * FROM cliente WHERE idcliente='"+idcliente+"' AND estado="+MyVar.NO_ELIMINADO, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean existeCliente(String idcliente) {
		Cursor c = sqlitedb.rawQuery("SELECT * FROM cliente WHERE idcliente='"+idcliente+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Cliente getCliente(String idcliente) {
		Cliente cli = null;
		Cursor c = sqlitedb.rawQuery("SELECT * FROM cliente WHERE idcliente='"+idcliente+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				cli = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), 
									c.getString(6), c.getString(7), Date.valueOf(c.getString(8)), c.getString(9), 
									c.getInt(10), c.getInt(11), Date.valueOf(c.getString(12)),c.getString(13));
				return cli;
			}
		}
		return cli;
	}
	
	public Cliente buscarClientePorCI(String ci, String idparqueo) {
		Cliente cli = null;
		Cursor c = sqlitedb.rawQuery("SELECT * FROM cliente WHERE ci='"+ci+"' AND idparqueo='"+idparqueo+"' AND estado="+MyVar.NO_ELIMINADO, null);
		if(c!=null){
			if(c.moveToFirst()){
				cli = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), 
							c.getString(6), c.getString(7), Date.valueOf(c.getString(8)), c.getString(9), 
							c.getInt(10), c.getInt(11), Date.valueOf(c.getString(12)),c.getString(13));
				return cli;
			}
		}
		return cli;
	}
	
	public List<Cliente> getAllCliente(String idparqueo, int estado){
		String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
		List<Cliente> listCliente = new ArrayList<Cliente>();
		String consulta = "SELECT * FROM cliente WHERE idparqueo='"+idparqueo+"' AND estado="+estado+
							" AND idcliente!='"+id_client_default+"' ORDER BY nombre ASC";
		
		if(estado==todos){
			consulta = "SELECT * FROM cliente WHERE idparqueo='"+idparqueo+"' AND idcliente!='"+id_client_default+"' ORDER BY apellido ASC";
		}
		Cursor c = sqlitedb.rawQuery(consulta, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			Cliente cliente = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), 
									c.getString(6), c.getString(7), Date.valueOf(c.getString(8)), c.getString(9), 
									c.getInt(10), c.getInt(11), Date.valueOf(c.getString(12)),c.getString(13));
			listCliente.add(cliente);
		}
		return listCliente;
	}
	
	public List<Cliente> getAllClientePorContrato(String idparqueo, int tipo_cliente){
		String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
		List<Cliente> listCliente = new ArrayList<Cliente>();
		String consulta = "SELECT * FROM cliente WHERE idparqueo='"+idparqueo+"' AND tipo="+tipo_cliente+
				" AND estado="+MyVar.NO_ELIMINADO+" AND idcliente!='"+id_client_default+"' ORDER BY nombre ASC";

		Cursor c = sqlitedb.rawQuery(consulta, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			Cliente cliente = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), 
									c.getString(6), c.getString(7), Date.valueOf(c.getString(8)), c.getString(9), 
									c.getInt(10), c.getInt(11), Date.valueOf(c.getString(12)),c.getString(13));
			listCliente.add(cliente);
		}
		return listCliente;
	}
	
	//-------------------------------------------TABLA VEHICULO-----------------------------------------------
	
	public boolean insertarVehiculo(Vehiculo v){
		String sql = "INSERT INTO vehiculo(idvehiculo, placa, marca, color, imagen, estado, idcliente) " +
						"VALUES ('"+v.getIdvehiculo()+"','"+v.getPlaca()+"','"+v.getMarca()+"'," +
							"'"+v.getColor()+"','"+v.getImagen()+"',"+v.getEstado()+",'"+v.getIdcliente()+"')";
		sqlitedb.execSQL(sql);
		return verificarVehiculo(v.getIdvehiculo());
	}
	
	public boolean actualizarVehiculo(Vehiculo v){
		String sql3 = "UPDATE vehiculo SET placa='"+v.getPlaca()+"', marca='"+v.getMarca()+"', color='"+v.getColor()+"', imagen='"+v.getImagen()+"', " +
						"estado="+v.getEstado()+", idcliente='"+v.getIdcliente()+"' WHERE idvehiculo='"+v.getIdvehiculo()+"'";
		sqlitedb.execSQL(sql3);
		return verificarVehiculo(v.getIdvehiculo());
	}

	public boolean eliminarVehiculo(String idvehiculo){
		String sql = "UPDATE vehiculo SET estado="+MyVar.ELIMINADO+" WHERE idvehiculo='"+idvehiculo+"'";
		sqlitedb.execSQL(sql);
		if(verificarVehiculo(idvehiculo)){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean restaurarVehiculo(String idvehiculo){		
		String query = "UPDATE vehiculo SET estado="+MyVar.NO_ELIMINADO+" WHERE idvehiculo='"+idvehiculo+"'";
		sqlitedb.execSQL(query);
		if(verificarVehiculo(idvehiculo)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean verificarVehiculo(String idvehiculo) {
		String consulta = "SELECT * FROM vehiculo WHERE idvehiculo='"+idvehiculo+"' AND estado="+MyVar.NO_ELIMINADO;
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean existeVehiculo(String idvehiculo) {
		String consulta = "SELECT * FROM vehiculo WHERE idvehiculo='"+idvehiculo+"'";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Vehiculo getVehiculo(String idvehiculo){
		Vehiculo v = null;
		String consulta = "SELECT * FROM vehiculo WHERE idvehiculo='"+idvehiculo+"'";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.moveToFirst()){
				v = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), 
									c.getInt(5), c.getString(6));
				return v;
			}
		}
		return v;
	}
	
	public Vehiculo buscarVehiculoPorPlaca(String placa, String idparqueo){
		Vehiculo v = null;
		String consulta = "SELECT * FROM vehiculo V INNER JOIN cliente C ON V.idcliente=C.idcliente " +
							"WHERE V.placa='"+placa+"' AND C.idparqueo='"+idparqueo+"' AND V.estado="+MyVar.NO_ELIMINADO;
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.moveToFirst()){
				v = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), 
									c.getInt(5), c.getString(6));
				return v;
			}
		}
		return v;
	}
	
	public List<Vehiculo> getAllVehiculo(String idparqueo, int estado){
		List<Vehiculo> listVehiculo = new ArrayList<Vehiculo>();
		String query = "SELECT V.idvehiculo, V.placa, V.marca, V.color, V.imagen, V.estado, V.idcliente FROM vehiculo V INNER JOIN cliente C " +
						"ON V.idcliente=C.idcliente WHERE C.idparqueo='"+idparqueo+"' AND V.estado="+estado+" ORDER BY V.placa ASC";
		
		Cursor c = sqlitedb.rawQuery(query, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			Vehiculo vehiculo = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), 
												c.getInt(5), c.getString(6));
			listVehiculo.add(vehiculo);
		}
		return listVehiculo;
	}
	
	public List<Vehiculo> getAllVehiculosOcasionales(String idparqueo, String id_client_default){
		//String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(idparqueo).toString();
		List<Vehiculo> listVehiculo = new ArrayList<Vehiculo>();
		String query = "SELECT V.idvehiculo, V.placa, V.marca, V.color, V.imagen, V.estado, V.idcliente FROM vehiculo V INNER JOIN cliente C " +
				"ON V.idcliente=C.idcliente WHERE C.idparqueo='"+idparqueo+"' AND V.estado="+MyVar.NO_ELIMINADO+" AND " +
				"V.idcliente='"+id_client_default+"' OR C.tipo="+MyVar.CLIENTE_OCASIONAL+" ORDER BY V.placa ASC";
		Cursor c = sqlitedb.rawQuery(query, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			Vehiculo vehiculo = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), 
												c.getInt(5), c.getString(6));
			listVehiculo.add(vehiculo);
		}
		return listVehiculo;
	}
	
	public List<Vehiculo> getAllVehiculoConPropConContrato(String idparqueo, String id_client_default, int tipo){
		List<Vehiculo> listVehiculo = new ArrayList<Vehiculo>();
		String query = "SELECT V.idvehiculo, V.placa, V.marca, V.color, V.imagen, V.estado, V.idcliente FROM vehiculo V INNER JOIN cliente C " +
				"ON V.idcliente=C.idcliente WHERE C.idparqueo='"+idparqueo+"' AND V.idcliente!='"+id_client_default+
				"' AND C.tipo="+tipo+" AND V.estado="+MyVar.NO_ELIMINADO+" ORDER BY V.placa ASC";
		
		Cursor c = sqlitedb.rawQuery(query, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			Vehiculo vehiculo = new Vehiculo(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), 
												c.getInt(5), c.getString(6));
			listVehiculo.add(vehiculo);
		}
		return listVehiculo;
	}

	//-----------------------------------------TABLA RESGUARDO-----------------------------------------------
	
	public boolean insertarResguardoCompleto(Resguardo r){
		String sql = "INSERT INTO resguardo(idresguardo, horaE, fechaE, horaS, fechaS, costoDia, costoNoche, " +
				"costoTotal, estado, nota, tipo ,idcarril, idvehiculo) " +
				"VALUES ('"+r.getIdresguardo()+"','"+r.getHoraE()+"','"+r.getFechaE()+"','"+r.getHoraS()+"','"+r.getFechaS()+"',"+r.getCostoDia()+","+
				 r.getCostoNoche()+","+r.getCostoTotal()+","+r.getEstado()+",'"+r.getNota()+"',"+r.getTipo()+",'"+r.getIdcarril()+"','"+r.getIdvehiculo()+"')";
		sqlitedb.execSQL(sql);
	return verificarResguardo(r.getIdresguardo());
	}
	
	public boolean insertarResguardo(Resguardo r){
		String sql = "INSERT INTO resguardo(idresguardo, horaE, fechaE, costoTotal, estado, nota, tipo, idcarril, idvehiculo) " +
						"VALUES ('"+r.getIdresguardo()+"','"+r.getHoraE()+"','"+r.getFechaE()+"',"+r.getCostoTotal()+","+r.getEstado()+"," +
							"'"+r.getNota()+"',"+r.getTipo()+",'"+r.getIdcarril()+"','"+r.getIdvehiculo()+"')";
		sqlitedb.execSQL(sql);
		return verificarResguardo(r.getIdresguardo());
	}
	
	public boolean actualizarResguardo(Resguardo r){
		String sql = "UPDATE resguardo SET horaS='"+r.getHoraS()+"', fechaS='"+r.getFechaS()+"', costoDia="+r.getCostoDia()+
							", costoNoche="+r.getCostoNoche()+", costoTotal="+r.getCostoTotal()+", nota='"+r.getNota()+"', tipo="+r.getTipo()+
							" WHERE idresguardo='"+r.getIdresguardo()+"' AND idcarril='"+r.getIdcarril()+"'";
		sqlitedb.execSQL(sql);
		return verificarActualizacion(r);
	}
	
	public boolean editarNotaResguardo(Resguardo resg){
		String query = "UPDATE resguardo SET nota='"+resg.getNota()+"' WHERE idresguardo='"+resg.getIdresguardo()+"'";
		sqlitedb.execSQL(query);
		return true;
	}
		
	public boolean verificarActualizacion(Resguardo r){
		String consulta = "SELECT * FROM resguardo WHERE idresguardo='"+r.getIdresguardo()+"' AND tipo != 0 AND idcarril='"+r.getIdcarril()+"'";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Resguardo getEntradaResguardo(String idcarril, String idvehiculo){
		Resguardo r = null;
		String consulta = "SELECT idresguardo, horaE, fechaE, costoTotal, estado, nota, tipo, idcarril, idvehiculo FROM resguardo " +
							"WHERE idcarril='"+idcarril+"' AND idvehiculo='"+idvehiculo+"' AND tipo=0";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.moveToFirst()){
				r = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
									c.getDouble(3), c.getInt(4), c.getString(5), c.getInt(6),c.getString(7), c.getString(8));
				return r;
			}
		}
		return r;
	}
	
	public boolean verificarResguardo(String idresguardo){
		Cursor c = sqlitedb.rawQuery("SELECT * FROM resguardo WHERE idresguardo='"+idresguardo+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public List<Resguardo> getAllEntradaResguardo(String idparqueo){
		List<Resguardo> listR = new ArrayList<Resguardo>();
		String consulta = "SELECT R.idresguardo, R.horaE, R.fechaE, R.costoTotal, R.estado, R.nota, R.tipo, R.idcarril, R.idvehiculo FROM resguardo R " +
						"INNER JOIN carril C ON R.idcarril = C.idcarril WHERE C.idparqueo='"+idparqueo+"' AND R.tipo=0";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				Resguardo re = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
										c.getDouble(3), c.getInt(4), c.getString(5), c.getInt(6), c.getString(7), c.getString(8));
				listR.add(re);
			}
		}
		
		return listR;
	}
	
	public List<Resguardo> getUltimoResguardos(String idparqueo){
		List<Resguardo> listResguardo = new ArrayList<Resguardo>();
		String consulta = "SELECT R.idresguardo, R.horaE, R.fechaE, R.horaS, R.fechaS, R.costoDia, R.costoNoche, R.costoTotal, " +
							"R.estado, R.nota, R.tipo, R.idcarril, R.idvehiculo " +
							"FROM resguardo R INNER JOIN carril C ON R.idcarril = C.idcarril " +
								"WHERE C.idparqueo='"+idparqueo+"' AND R.tipo!=0 ORDER BY R.fechaS, R.horaS ASC";//DESC LIMIT 25";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			if(c.getCount()>24){
				int desde = c.getCount()-24;//Aqui pongo desde 24 xq kiero los 25 ultimos
				for(c.move(desde);!c.isAfterLast();c.moveToNext()){
					Resguardo r = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
													Time.valueOf(c.getString(3)), Date.valueOf(c.getString(4)), c.getDouble(5), c.getDouble(6), 
													c.getDouble(7), c.getInt(8), c.getString(9), c.getInt(10), c.getString(11), c.getString(12));
					listResguardo.add(r);
				}
			}else{
				for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
					Resguardo r = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
													Time.valueOf(c.getString(3)), Date.valueOf(c.getString(4)), c.getDouble(5), c.getDouble(6), 
													c.getDouble(7), c.getInt(8), c.getString(9), c.getInt(10), c.getString(11), c.getString(12));
					listResguardo.add(r);
				}
			}
			
		}
		
		return listResguardo;
	}
	
	public List<Resguardo> getResguardoPorFecha(String idparqueo, Date fecha_antes, Date fecha_actual){
		List<Resguardo> listResguardo = new ArrayList<Resguardo>();
		String consulta = "SELECT R.idresguardo, R.horaE, R.fechaE, R.horaS, R.fechaS, R.costoDia, R.costoNoche, R.costoTotal, " +
							"R.estado, R.nota, R.tipo, R.idcarril, R.idvehiculo " +
							"FROM resguardo R INNER JOIN carril C ON R.idcarril = C.idcarril " +
								"WHERE C.idparqueo='"+idparqueo+"' AND R.tipo!=0 AND R.fechaE BETWEEN '"+fecha_antes+"' AND '"+fecha_actual+"' "+
									" ORDER BY R.fechaS, R.horaS  ASC";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Resguardo r = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
								Time.valueOf(c.getString(3)), Date.valueOf(c.getString(4)), c.getDouble(5), c.getDouble(6), 
								c.getDouble(7), c.getInt(8), c.getString(9), c.getInt(10), c.getString(11), c.getString(12));
			listResguardo.add(r);
			}
		}
		
		return listResguardo;
	}
	
	public List<Resguardo> getResguardoPorMes(String idparqueo, Date fecha){
		List<Resguardo> listResguardo = new ArrayList<Resguardo>();
		String consulta = "SELECT R.idresguardo, R.horaE, R.fechaE, R.horaS, R.fechaS, R.costoDia, R.costoNoche, R.costoTotal, " +
							"R.estado, R.nota, R.tipo, R.idcarril, R.idvehiculo " +
							"FROM resguardo R INNER JOIN carril C ON R.idcarril = C.idcarril " +
								"WHERE C.idparqueo='"+idparqueo+"' AND R.tipo!=0 AND " +
									"strftime('%m',R.fechaE) = strftime('%m','"+fecha+"') AND strftime('%Y',R.fechaE) = strftime('%Y','"+fecha+"')"+
										"ORDER BY R.fechaS, R.horaS ASC";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Resguardo r = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
								Time.valueOf(c.getString(3)), Date.valueOf(c.getString(4)), c.getDouble(5), c.getDouble(6), 
								c.getDouble(7), c.getInt(8), c.getString(9), c.getInt(10), c.getString(11), c.getString(12));
			listResguardo.add(r);
			}
		}
		
		return listResguardo;
	}
	
	public List<Resguardo> getResguardoPorVehiculo(String idparqueo, String idvehiculo){
		List<Resguardo> listResguardo = new ArrayList<Resguardo>();
		String consulta = "SELECT R.idresguardo, R.horaE, R.fechaE, R.horaS, R.fechaS, R.costoDia, R.costoNoche, R.costoTotal, " +
							"R.estado, R.nota, R.tipo, R.idcarril, R.idvehiculo " +
							"FROM resguardo R INNER JOIN carril C ON R.idcarril = C.idcarril " +
								"WHERE C.idparqueo='"+idparqueo+"' AND R.tipo!=0 AND R.idvehiculo='"+idvehiculo+"' ORDER BY R.fechaS, R.horaS ASC";
		Cursor c = sqlitedb.rawQuery(consulta, null);
		if(c!=null){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			Resguardo r = new Resguardo(c.getString(0), Time.valueOf(c.getString(1)), Date.valueOf(c.getString(2)), 
								Time.valueOf(c.getString(3)), Date.valueOf(c.getString(4)), c.getDouble(5), c.getDouble(6), 
								c.getDouble(7), c.getInt(8), c.getString(9), c.getInt(10), c.getString(11), c.getString(12));
			listResguardo.add(r);
			}
		}
		
		return listResguardo;
	}
		
}
