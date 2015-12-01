package com.silvia.basedatos;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.Cooperativa;
import com.silvia.modelo.DetallePedido;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Pedido;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Producto;
import com.silvia.modelo.Usuario;
import com.silvia.modelo.Venta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBDuraznillo {

	private DBHelper dbHelper;
	private SQLiteDatabase dbSQlite;
	private Context contexto;
	
	public DBDuraznillo(Context context){
		this.contexto = context;
	}
	
	public DBDuraznillo abrirDB(){
		try {
			dbHelper = new DBHelper(contexto);
			dbSQlite = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void cerrarDB(){
		dbSQlite.close();
	}
	
	//---------------------------------------------MANIPULACION TABLA COOPERATIVA---------------------------------------------------
	
	public boolean insertarCooperativa(Cooperativa coop){
		String sql = "INSERT INTO cooperativa(idcoop, nombre, nit, telf_cel, email, direccion, ciudad) " +
						"VALUES('"+coop.getIdcoop()+"','"+coop.getNombre()+"','"+coop.getNit()+"','"+coop.getTelf_cel()+"','"+coop.getEmail()+"'," +
								"'"+coop.getDireccion()+"','"+coop.getCiudad()+"')";
		dbSQlite.execSQL(sql);
		return verificarCooperativa(coop);
	}
	
	public boolean verificarCooperativa(Cooperativa coop){
		Cursor c = dbSQlite.rawQuery("SELECT * FROM cooperativa WHERE idcoop='"+coop.getIdcoop()+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public Cooperativa getCooperativa(String id_coop){
		Cooperativa coop = null;
		Cursor c = dbSQlite.rawQuery("SELECT * FROM cooperativa WHERE idcoop='"+id_coop+"'", null);
		if(c!=null){
			if(c.moveToFirst()){
				coop = new Cooperativa(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6));
			}
		}
		return coop;
	}
	
	public boolean actualizarCooperativa(Cooperativa coop){
		String sql = "UPDATE cooperativa SET nombre='"+coop.getNombre()+"', nit='"+coop.getNit()+"', telf_cel='"+coop.getTelf_cel()+"', " +
						"email='"+coop.getEmail()+"', direccion='"+coop.getDireccion()+"', ciudad='"+coop.getCiudad()+"' WHERE idcoop='"+coop.getIdcoop()+"'";
		dbSQlite.execSQL(sql);
		return verificarCooperativa(coop);
	}
	
	//----------------------------------------------MANIPULACION TABLA CARGO-------------------------------------------------
	
	public boolean insertarCargo(Cargo c){
		String query = "INSERT INTO cargo(idcargo, ocupacion, salario, descripcion, estado) " +
						"VALUES ('"+c.getIdcargo()+"','"+c.getOcupacion()+"',"+c.getSalario()+",'"+c.getDescripcion()+"',"+c.getEstado()+")";
		dbSQlite.execSQL(query);
		return verificarCargo(c);
	}
	
	public boolean verificarCargo(Cargo c){
		String sql = "SELECT * FROM cargo WHERE idcargo='"+c.getIdcargo()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor cu = dbSQlite.rawQuery(sql, null);
		if(cu!=null){
			if(cu.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean modificarCargo(Cargo c){
		String query = "UPDATE cargo SET ocupacion='"+c.getOcupacion()+"', salario="+c.getSalario()+", descripcion='"+c.getDescripcion()+"' " +
				"WHERE idcargo='"+c.getIdcargo()+"'";
		dbSQlite.execSQL(query);
		return verificarCargo(c);
	}
	
	public boolean eliminarCargo(Cargo c){
		String query = "UPDATE cargo SET estado="+Variables.ELIMINADO+" WHERE idcargo='"+c.getIdcargo()+"'";
		dbSQlite.execSQL(query);
		if(verificarCargo(c)){
			return false;
		}else{
			return true;
		}
	}
	
	public Cargo getCargo(String idcargo){
		Cargo cargo = null;
		String query = "SELECT * FROM cargo WHERE idcargo='"+idcargo+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				cargo = new Cargo(c.getString(0), c.getString(1), c.getDouble(2), c.getString(3), c.getInt(4));
			}
		}
		return cargo;
	}
	//Para obtener todos los cargos
	public List<Cargo> getTodosLosCargos(){
		List<Cargo> lista_cargo = new ArrayList<Cargo>();
		String query = "SELECT * FROM cargo WHERE estado="+Variables.NO_ELIMINADO+" AND idcargo!='"+Variables.ID_CARGO_ADMIN+"' ORDER BY ocupacion ASC";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Cargo cargo = new Cargo(c.getString(0), c.getString(1), c.getDouble(2), c.getString(3), c.getInt(4));
				lista_cargo.add(cargo);
			}
		}
		return lista_cargo;
	}
	
	//----------------------------------------------MANIPULACION TABLA MAQUINARIA-------------------------------------------------
	
	public boolean insertarMaquinaria(Maquinaria m){
		String query = "INSERT INTO maquinaria(idmaquinaria, placa, descripcion, capacidad, marca, color, imagen, estado) " +
						"VALUES ('"+m.getIdmaquinaria()+"','"+m.getPlaca()+"','"+m.getDescripcion()+"',"+m.getCapacidad()+"," +
						"'"+m.getMarca()+"','"+m.getColor()+"','"+m.getImagen()+"',"+m.getEstado()+")";
		dbSQlite.execSQL(query);
		return verificarMaquinaria(m);
	}
	
	public boolean modificarMaq(Maquinaria maq){
		String query = "UPDATE maquinaria SET placa='"+maq.getPlaca()+"', descripcion='"+maq.getDescripcion()+"', capacidad="+maq.getCapacidad()+", " +
				"marca='"+maq.getMarca()+"', color='"+maq.getColor()+"', imagen='"+maq.getImagen()+"' WHERE idmaquinaria='"+maq.getIdmaquinaria()+"'";
		dbSQlite.execSQL(query);
		return verificarMaquinaria(maq);
	}
	
	public boolean verificarMaquinaria(Maquinaria m){
		String query = "SELECT * FROM maquinaria WHERE idmaquinaria='"+m.getIdmaquinaria()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean existePlaca(String placa){
		String query = "SELECT * FROM maquinaria WHERE placa='"+placa+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean eliminarMaquinaria(Maquinaria maq){
		String query = "UPDATE maquinaria SET estado="+Variables.ELIMINADO+" WHERE idmaquinaria='"+maq.getIdmaquinaria()+"'";
		dbSQlite.execSQL(query);
		if(verificarMaquinaria(maq)){
			return false;
		}else{
			return true;
		}
	}
	
	public Maquinaria getMaquinaria(String idmaquinaria){
		Maquinaria maq = null;
		String query = "SELECT * FROM maquinaria WHERE idmaquinaria='"+idmaquinaria+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				maq = new Maquinaria(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3),c.getString(4), 
										c.getString(5), c.getString(6), c.getInt(7));
			}
		}
		return maq;
	}
	
	public List<Maquinaria> getTodosLasMaquinarias(){
		List<Maquinaria> lista_maquinaria = new ArrayList<Maquinaria>();
		String query = "SELECT * FROM maquinaria WHERE estado="+Variables.NO_ELIMINADO+" AND idmaquinaria!='"+Variables.ID_MAQ_DEFAULT+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Maquinaria maq = new Maquinaria(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3),c.getString(4), 
													c.getString(5), c.getString(6), c.getInt(7));
				lista_maquinaria.add(maq);
			}
		}
		return lista_maquinaria;
	}
	
	//----------------------------------------------MANIPULACION TABLA PERSONAL------------------------------------------------
	
	public boolean insertarPersonal(Personal p){
		String query = "INSERT INTO personal(idpersonal, ci, nombre, apellido, direccion, telefono, email, fecha_nac, fecha_ingreso, " +
				"imagen, estado, idcargo, idmaquinaria) VALUES ('"+p.getIdpersonal()+"','"+p.getCi()+"','"+p.getNombre()+"','"+p.getApellido()+"'," +
				"'"+p.getDireccion()+"',"+p.getTelf()+",'"+p.getEmail()+"','"+p.getFecha_nac()+"','"+p.getFecha_ingreso()+"','"+p.getImagen()+"'," +
				p.getEstado()+",'"+p.getIdcargo()+"','"+p.getIdmaquinaria()+"')";
		dbSQlite.execSQL(query);
		return verificarPersonal(p);
	}
	
	public boolean verificarPersonal(Personal p){
		String query = "SELECT * FROM personal WHERE idpersonal='"+p.getIdpersonal()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean modificarPersonal(Personal p){
		String query = "UPDATE personal SET ci='"+p.getCi()+"', nombre='"+p.getNombre()+"', apellido='"+p.getApellido()+"', " +
						"direccion='"+p.getDireccion()+"', telefono="+p.getTelf()+", email='"+p.getEmail()+"', fecha_nac='"+p.getFecha_nac()+"', " +
						"fecha_ingreso='"+p.getFecha_ingreso()+"', imagen='"+p.getImagen()+"', idcargo='"+p.getIdcargo()+"', " +
						"idmaquinaria='"+p.getIdmaquinaria()+"' WHERE idpersonal='"+p.getIdpersonal()+"'";
		dbSQlite.execSQL(query);
		return verificarPersonal(p);
	}
	
	public boolean existeCIPersonal(String ci){
		String query = "SELECT * FROM personal WHERE ci='"+ci+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean eliminarPersonal(Personal p){
		String query = "UPDATE personal SET estado="+Variables.ELIMINADO+" WHERE idpersonal='"+p.getIdpersonal()+"'";
		dbSQlite.execSQL(query);
		if(verificarPersonal(p)){
			return false;
		}else{
			return true;
		}
	}
	
	public Personal getPersonal(String idpersonal){
		Personal personal = null;
		String query = "SELECT * FROM personal WHERE idpersonal='"+idpersonal+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()) {
				personal = new Personal(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), 
											c.getString(6), Date.valueOf(c.getString(7)), Date.valueOf(c.getString(8)), c.getString(9), c.getInt(10), 
											c.getString(11), c.getString(12));
			}
		}
		return personal;
	}
	
	public List<Personal> getTodosLosPersonales(){
		List<Personal> lista_per = new ArrayList<Personal>();
		String query = "SELECT * FROM personal WHERE estado="+Variables.NO_ELIMINADO+" ORDER BY apellido ASC";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Personal per = new Personal(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), 
											c.getString(6), Date.valueOf(c.getString(7)), Date.valueOf(c.getString(8)), c.getString(9), c.getInt(10), 
											c.getString(11), c.getString(12));
				lista_per.add(per);
			}
		}
		return lista_per;
	}
	
	public List<Personal> getTodosLosPersonales(String excepto_idpersonal){
		List<Personal> lista_per = new ArrayList<Personal>();
		String query = "SELECT * FROM personal WHERE estado="+Variables.NO_ELIMINADO+" AND idpersonal!='"+excepto_idpersonal+"'" +
															" AND idpersonal!='"+Variables.ID_PERSONAL_ADMIN+"' ORDER BY apellido ASC";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Personal per = new Personal(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), 
											c.getString(6), Date.valueOf(c.getString(7)), Date.valueOf(c.getString(8)), c.getString(9), c.getInt(10), 
											c.getString(11), c.getString(12));
				lista_per.add(per);
			}
		}
		return lista_per;
	}
	
	public List<Personal> getTodosLosPersonalesConMaquinaria(){
		List<Personal> lista_per = new ArrayList<Personal>();
		String query = "SELECT * FROM personal WHERE estado="+Variables.NO_ELIMINADO+" AND idmaquinaria!='"+Variables.ID_MAQ_DEFAULT+"' " +
																														"ORDER BY apellido ASC";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Personal per = new Personal(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), 
											c.getString(6), Date.valueOf(c.getString(7)), Date.valueOf(c.getString(8)), c.getString(9), c.getInt(10), 
											c.getString(11), c.getString(12));
				lista_per.add(per);
			}
		}
		return lista_per;
	}
	
	//-----------------------------------------------MANIPULACION TABLA USUARIO------------------------------------------------
	
	public boolean insertarUsuario(Usuario u){
		String query = "INSERT INTO usuario(idusuario, usuario, clave, estado, idpersonal) VALUES ('"+u.getIdusuario()+"','"+u.getUsuario()+"'," +
				"'"+u.getClave()+"',"+u.getEstado()+",'"+u.getIdpersonal()+"')";
		dbSQlite.execSQL(query);
		return verificarUsuario(u);
	}

	public boolean verificarUsuario(Usuario u) {
		String query = "SELECT * FROM usuario WHERE idusuario='"+u.getIdusuario()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean modificarUsuario(Usuario u){
		String query = "UPDATE usuario SET usuario='"+u.getUsuario()+"', clave='"+u.getClave()+"' WHERE idusuario='"+u.getIdusuario()+"'";
		dbSQlite.execSQL(query);
		return verificarUsuario(u);
	}
	
	public boolean existeUsuario(String usuario){
		String query = "SELECT * FROM usuario WHERE usuario='"+usuario+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean esPersonalUsuario(Personal p){
		String query = "SELECT * FROM usuario WHERE idpersonal='"+p.getIdpersonal()+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean esPersonalUsuarioHabiltado(Personal p){
		String query = "SELECT * FROM usuario WHERE idpersonal='"+p.getIdpersonal()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean deshabilitarUsuario(Personal p){
		String query = "UPDATE usuario SET estado="+Variables.ELIMINADO+" WHERE idpersonal='"+p.getIdpersonal()+"'";
		dbSQlite.execSQL(query);
		if(esPersonalUsuarioHabiltado(p)){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean habilitarUsuario(Personal p){
		String query = "UPDATE usuario SET estado="+Variables.NO_ELIMINADO+" WHERE idpersonal='"+p.getIdpersonal()+"'";
		dbSQlite.execSQL(query);
		if(esPersonalUsuario(p)){
			return true;
		}else{
			return false;
		}
	}
	
	public Usuario getUsuario(String idusuario){
		Usuario usuario = null;
		String query = "SELECT * FROM usuario WHERE idusuario='"+idusuario+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()) {
				usuario = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4));
				
			}
		}
		return usuario;
	}
	
	public Usuario getUsuarioPersonal(String idpersonal){
		Usuario usuario = null;
		String query = "SELECT * FROM usuario WHERE idpersonal='"+idpersonal+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()) {
				usuario = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4));
				
			}
		}
		return usuario;
	}
	
	public List<Usuario> getTodosLosUsuario(){
		List<Usuario> lista_usuarios = new ArrayList<Usuario>();
		String query = "SELECT * FROM usuario";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Usuario user = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4));
				lista_usuarios.add(user);
			}
		}
		return lista_usuarios;
	}

	public Usuario autenticarUsuario(String usuario, String clave){
		Usuario user = null;
		String query = "SELECT * FROM usuario WHERE usuario='"+usuario+"' AND clave='"+clave+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()) {
				user = new Usuario(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4));
				
			}
		}
		return user;
	}

	public boolean personalEsUsuario(String idpersonal){
		Cursor c = dbSQlite.rawQuery("SELECT * FROM usuario WHERE idpersonal='"+idpersonal+"' AND estado="+Variables.NO_ELIMINADO, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	//------------------------------------------------MANIPULACION TABLA CLIENTE----------------------------------------------
	
	public boolean insertarCliente(Cliente c){
		String query = "INSERT INTO cliente(idcliente, ci, nombre, apellido, direccion, telefono, email, sexo, fecha_nac, fecha_reg, imagen, estado) " +
						"VALUES ('"+c.getIdcliente()+"','"+c.getCi()+"','"+c.getNombre()+"','"+c.getApellido()+"','"+c.getDireccion()+"',"+c.getTelf()+"," +
						"'"+c.getEmail()+"','"+c.getSexo()+"','"+c.getFechaNac()+"','"+c.getFechaReg()+"','"+c.getImagen()+"',"+c.getEstado()+")";
		dbSQlite.execSQL(query);
		return verificarCliente(c);
	}
	
	public boolean verificarCliente(Cliente c) {
		String query = "SELECT * FROM cliente WHERE idcliente='"+c.getIdcliente()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor cur = dbSQlite.rawQuery(query, null);
		if(cur!=null){
			if(cur.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean modificarCliente(Cliente c){
		String query = "UPDATE cliente SET ci='"+c.getCi()+"', nombre='"+c.getNombre()+"', apellido='"+c.getApellido()+"', direccion='"+c.getDireccion()+"', " +
						"telefono="+c.getTelf()+", email='"+c.getEmail()+"', sexo='"+c.getSexo()+"', fecha_nac='"+c.getFechaNac()+"', " +
						"fecha_reg='"+c.getFechaReg()+"', imagen='"+c.getImagen()+"' WHERE idcliente='"+c.getIdcliente()+"'";
		dbSQlite.execSQL(query);
		return verificarCliente(c);
	}
	
	public boolean existeCICliente(String ci){
		String query = "SELECT * FROM cliente WHERE ci='"+ci+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor cur = dbSQlite.rawQuery(query, null);
		if(cur!=null){
			if(cur.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean eliminarCliente(Cliente c){
		String query = "UPDATE cliente SET estado="+Variables.ELIMINADO+" WHERE idcliente='"+c.getIdcliente()+"'";
		dbSQlite.execSQL(query);
		if(verificarCliente(c)){
			return false;
		}else{
			return true;
		}
	}
	
	public Cliente getCliente(String idcliente){
		Cliente cliente = null;
		String query = "SELECT * FROM cliente WHERE idcliente='"+idcliente+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				cliente = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getString(6), 
								c.getString(7), Date.valueOf(c.getString(8)), Date.valueOf(c.getString(9)), c.getString(10), c.getInt(11));
			}
		}
		return cliente;
	}
	
	public Cliente getClientePorCI(String ci){
		Cliente cliente = null;
		String query = "SELECT * FROM cliente WHERE ci='"+ci+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				cliente = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getString(6), 
								c.getString(7), Date.valueOf(c.getString(8)), Date.valueOf(c.getString(9)), c.getString(10), c.getInt(11));
			}
		}
		return cliente;
	}
	
	public List<Cliente> getTodosLosClientes(){
		List<Cliente> lista_clientes = new ArrayList<Cliente>();
		String query = "SELECT * FROM cliente WHERE estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Cliente cliente = new Cliente(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getString(6), 
								c.getString(7), Date.valueOf(c.getString(8)), Date.valueOf(c.getString(9)), c.getString(10), c.getInt(11));
				lista_clientes.add(cliente);
			}
		}
		return lista_clientes;
	}
	
	//-----------------------------------------------MANIPULACION TABLA PRODUCTO-------------------------------------------------
	
	public boolean insertarProducto(Producto p){
		String query = "INSERT INTO producto(idprod, nombre_prod, precio, unidad, descripcion, imagen, estado) " +
						"VALUES ('"+p.getIdprod()+"','"+p.getNombre_prod()+"',"+p.getPrecio()+",'"+p.getUnidad()+"','"+p.getDescripcion()+"'," +
						"'"+p.getImagen()+"',"+p.getEstado()+")";
		dbSQlite.execSQL(query);
		return verificarProducto(p);
	}
	
	public boolean verificarProducto(Producto p) {
		String query = "SELECT * FROM producto WHERE idprod='"+p.getIdprod()+"' AND estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public boolean modificarProducto(Producto p){
		String query = "UPDATE producto SET nombre_prod='"+p.getNombre_prod()+"', precio="+p.getPrecio()+", unidad='"+p.getUnidad()+"', " +
						"descripcion='"+p.getDescripcion()+"', imagen='"+p.getImagen()+"' WHERE idprod='"+p.getIdprod()+"'";
		dbSQlite.execSQL(query);
		return verificarProducto(p);
	}
	
	public boolean eliminarProducto(Producto p){
		String query = "UPDATE producto SET estado="+Variables.ELIMINADO+" WHERE idprod='"+p.getIdprod()+"'";
		dbSQlite.execSQL(query);
		if(verificarProducto(p)){
			return false;
		}else{
			return true;
		}
	}
	
	public Producto getProducto(String idprod){
		Producto producto = null;
		String query = "SELECT * FROM producto WHERE idprod='"+idprod+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				producto = new Producto(c.getString(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6));
			}
		}
		return producto;
	}
	
	public List<Producto> getTodosLosProductos(){
		List<Producto> lista_prod = new ArrayList<Producto>();
		String query = "SELECT * FROM producto WHERE estado="+Variables.NO_ELIMINADO;
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Producto producto = new Producto(c.getString(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6));
				lista_prod.add(producto);
			}
		}
		return lista_prod;
	}
	
	//---------------------------------------------MANIPULACION TABLA VENTA--------------------------------------------------------
	
	public boolean insertarVenta(Venta v){
		String query = "INSERT INTO venta(idventa, tipo_venta, idusuario, idcliente, fecha_venta, hora_venta, idpersonal, " +
				"direccion, latitude, longitude, costo_total, nota) " +
				"VALUES('"+v.getIdventa()+"',"+v.getTipo_venta()+",'"+v.getIdusuario()+"','"+v.getIdcliente()+"','"+v.getFecha_venta()+"','" +
					v.getHora_venta()+"','"+v.getIdpersonal()+"','"+v.getDireccion()+"',"+v.getLatitude()+","+v.getLongitude()+", "+
					v.getCosto_total()+",'"+v.getNota()+"')";
		dbSQlite.execSQL(query);
		return verificarVenta(v);
	}
	
	public boolean verificarVenta(Venta v) {
		String query = "SELECT * FROM venta WHERE idventa='"+v.getIdventa()+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public List<Venta> getTodasLasVentas(){
		List<Venta> lista_venta = new ArrayList<Venta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM venta ORDER BY fecha_venta ASC", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Venta venta = new Venta(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), Date.valueOf(c.getString(4)), 
											Time.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
											c.getDouble(10), c.getString(11));
				lista_venta.add(venta);
			}
		}
		return lista_venta;
	}
	
	public List<Venta> getVentasDeLaSemana(Date fecha){
		List<Venta> lista = new ArrayList<Venta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM venta WHERE strftime('%W',fecha_venta) = strftime('%W','"+fecha+"') ORDER BY fecha_venta ASC", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Venta venta = new Venta(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), Date.valueOf(c.getString(4)), 
											Time.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
											c.getDouble(10), c.getString(11));
				lista.add(venta);
			}
		}
		return lista;
	}
	
	public List<Venta> getVentasPorMes(Date fecha){
		List<Venta> lista = new ArrayList<Venta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM venta WHERE strftime('%m',fecha_venta) = strftime('%m','"+fecha+"') ORDER BY fecha_venta ASC", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Venta venta = new Venta(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), Date.valueOf(c.getString(4)), 
											Time.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
											c.getDouble(10), c.getString(11));
				lista.add(venta);
			}
		}
		return lista;
	}

	public List<Venta> getVentasPorFecha(Date fecha){
		List<Venta> lista = new ArrayList<Venta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM venta WHERE strftime('%Y-%m-%d',fecha_venta) = strftime('%Y-%m-%d','"+fecha+"') " +
										"ORDER BY fecha_venta ASC", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Venta venta = new Venta(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), Date.valueOf(c.getString(4)), 
									Time.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
									c.getDouble(10), c.getString(11));
				lista.add(venta);
			}
		}
		return lista;
	}
	
	public List<Venta> getVentasPorCliente(String idcliente){
		List<Venta> lista = new ArrayList<Venta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM venta WHERE idcliente = '"+idcliente+"' ORDER BY fecha_venta ASC", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Venta venta = new Venta(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), Date.valueOf(c.getString(4)), 
						Time.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
						c.getDouble(10), c.getString(11));
				lista.add(venta);
			}
		}
		return lista;
	}
	
	public List<Venta> getVentasPorTipo(int tipo){
		List<Venta> lista = new ArrayList<Venta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM venta WHERE tipo_venta="+tipo+" ORDER BY fecha_venta ASC", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				Venta venta = new Venta(c.getString(0), c.getInt(1), c.getString(2), c.getString(3), Date.valueOf(c.getString(4)), 
						Time.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
						c.getDouble(10), c.getString(11));
				lista.add(venta);
			}
		}
		return lista;
	}
	
	//---------------------------------------------MANIPULACION TABLA DETALLE VENTA---------------------------------------------------
	
	public boolean insertarDetalleVenta(DetalleVenta dv){
		String query = "INSERT INTO detalle_venta(iddetalle, idventa, idproducto, cantidad, costo_entrega, costo) "+
						"VALUES('"+dv.getIddetalle()+"','"+dv.getIdventa()+"','"+dv.getIdproducto()+"',"+dv.getCantidad()+","+
						dv.getCosto_entrega()+","+dv.getCosto()+")";
		dbSQlite.execSQL(query);
		return verificarDetalleVenta(dv);
	}
	
	public boolean verificarDetalleVenta(DetalleVenta dv) {
		String query = "SELECT * FROM detalle_venta WHERE iddetalle='"+dv.getIddetalle()+"'";
		Cursor c = dbSQlite.rawQuery(query, null);
		if(c!=null){
			if(c.moveToFirst()){
				return true;
			}
		}
		return false;
	}
	
	public List<DetalleVenta> getDetalleVenta(String idventa){
		List<DetalleVenta> lista_detalle_venta = new ArrayList<DetalleVenta>();
		Cursor c = dbSQlite.rawQuery("SELECT * FROM detalle_venta WHERE idventa='"+idventa+"'", null);
		if(c!=null){
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				DetalleVenta detalleV = new DetalleVenta(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getDouble(4), c.getDouble(5));
				lista_detalle_venta.add(detalleV);
			}
		}
		return lista_detalle_venta;
	}
	
	//---------------------------------------------MANIPULACION TABLA PEDIDO--------------------------------------------------------
	
		public boolean insertarPedido(Pedido p){
			String query = "INSERT INTO pedido(idpedido, idusuario, idcliente, fecha_pedido, hora_pedido, fecha_entrega, idpersonal, " +
					"direccion, latitude, longitude, costo_total, nota, estado) "+
					"VALUES('"+p.getIdpedido()+"','"+p.getIdusuario()+"','"+p.getIdcliente()+"','"+p.getFecha_pedido()+"','"+p.getHora_pedido()+"','" +
						p.getFecha_entrega()+"','"+p.getIdpersonal()+"','"+p.getDireccion()+"',"+p.getLatitude()+", "+p.getLongitude()+", "+
						p.getCosto_total()+",'"+p.getNota()+"',"+p.getEstado()+")";
			dbSQlite.execSQL(query);
			return verificarPedido(p);
		}
		
		public boolean modificarDatosPedido(Pedido p){
			String query = "UPDATE pedido SET fecha_entrega='"+p.getFecha_entrega()+"', idpersonal='"+p.getIdpersonal()+"', direccion='"+p.getDireccion()+"', " +
								"latitude="+p.getLatitude()+", longitude="+p.getLongitude()+", "+
								"costo_total="+p.getCosto_total()+", nota='"+p.getNota()+"', estado="+p.getEstado()+" WHERE idpedido='"+p.getIdpedido()+"'";
			dbSQlite.execSQL(query);
			return true;
		}
		
		public boolean eliminarPedido(Pedido p){
			String query ="DELETE FROM pedido WHERE idpedido='"+p.getIdpedido()+"'";
			if(eliminarDetallePedidoMediantePedido(p)){
				dbSQlite.execSQL(query);
				if(!verificarPedido(p)){
					return true;
				}
			}
			return false;
		}
		
		public boolean verificarPedido(Pedido p) {
			String query = "SELECT * FROM pedido WHERE idpedido='"+p.getIdpedido()+"'";
			Cursor c = dbSQlite.rawQuery(query, null);
			if(c!=null){
				if(c.moveToFirst()){
					return true;
				}
			}
			return false;
		}
		
		public List<Pedido> getTodasLosPedidos(){
			List<Pedido> lista_pedido = new ArrayList<Pedido>();
			Cursor c = dbSQlite.rawQuery("SELECT * FROM pedido ORDER BY fecha_pedido ASC", null);
			if(c!=null){
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					Pedido pedido= new Pedido(c.getString(0), c.getString(1), c.getString(2), Date.valueOf(c.getString(3)), Time.valueOf(c.getString(4)), 
												Date.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
												c.getDouble(10), c.getString(11), c.getInt(12));
					lista_pedido.add(pedido);
				}
			}
			return lista_pedido;
		}
		
		public List<Pedido> getPedidosDeLaSemana(Date fecha){
			List<Pedido> lista = new ArrayList<Pedido>();
			Cursor c = dbSQlite.rawQuery("SELECT * FROM pedido WHERE strftime('%W',fecha_pedido) = strftime('%W','"+fecha+"') ORDER BY fecha_pedido ASC", null);
			if(c!=null){
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					Pedido pedido= new Pedido(c.getString(0), c.getString(1), c.getString(2), Date.valueOf(c.getString(3)), Time.valueOf(c.getString(4)), 
							Date.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
							c.getDouble(10), c.getString(11), c.getInt(12));
					lista.add(pedido);
				}
			}
			return lista;
		}
		
		public List<Pedido> getPedidosPorMes(Date fecha){
			List<Pedido> lista = new ArrayList<Pedido>();
			Cursor c = dbSQlite.rawQuery("SELECT * FROM pedido WHERE strftime('%m',fecha_pedido) = strftime('%m','"+fecha+"') ORDER BY fecha_pedido ASC", null);
			if(c!=null){
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					Pedido pedido= new Pedido(c.getString(0), c.getString(1), c.getString(2), Date.valueOf(c.getString(3)), Time.valueOf(c.getString(4)), 
							Date.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
							c.getDouble(10), c.getString(11), c.getInt(12));
					lista.add(pedido);
				}
			}
			return lista;
		}

		public List<Pedido> getPedidosPorFecha(Date fecha){
			List<Pedido> lista = new ArrayList<Pedido>();
			Cursor c = dbSQlite.rawQuery("SELECT * FROM pedido WHERE strftime('%Y-%m-%d',fecha_pedido) = strftime('%Y-%m-%d','"+fecha+"') " +
											"ORDER BY fecha_pedido ASC", null);
			if(c!=null){
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					Pedido pedido= new Pedido(c.getString(0), c.getString(1), c.getString(2), Date.valueOf(c.getString(3)), Time.valueOf(c.getString(4)), 
							Date.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
							c.getDouble(10), c.getString(11), c.getInt(12));
					lista.add(pedido);
				}
			}
			return lista;
		}
		
		public List<Pedido> getPedidosPorCliente(String idcliente){
			List<Pedido> lista = new ArrayList<Pedido>();
			Cursor c = dbSQlite.rawQuery("SELECT * FROM pedido WHERE idcliente = '"+idcliente+"' ORDER BY fecha_pedido ASC", null);
			if(c!=null){
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					Pedido pedido= new Pedido(c.getString(0), c.getString(1), c.getString(2), Date.valueOf(c.getString(3)), Time.valueOf(c.getString(4)), 
							Date.valueOf(c.getString(5)), c.getString(6), c.getString(7), c.getDouble(8), c.getDouble(9), 
							c.getDouble(10), c.getString(11), c.getInt(12));
					lista.add(pedido);
				}
			}
			return lista;
		}
		
		//---------------------------------------------MANIPULACION TABLA DETALLE PEDIDO---------------------------------------------------
		
		public boolean insertarDetallePedido(DetallePedido dp){
			String query = "INSERT INTO detalle_pedido(iddetalle_pedido, idpedido, idproducto, cantidad, costo_entrega, costo) "+
							"VALUES('"+dp.getIddetalle_pedido()+"','"+dp.getIdpedido()+"','"+dp.getIdproducto()+"',"+dp.getCantidad()+","+
							dp.getCosto_entrega()+","+dp.getCosto()+")";
			dbSQlite.execSQL(query);
			return verificarDetallePedido(dp);
		}
		
		public boolean verificarDetallePedido(DetallePedido dp) {
			String query = "SELECT * FROM detalle_pedido WHERE iddetalle_pedido='"+dp.getIddetalle_pedido()+"'";
			Cursor c = dbSQlite.rawQuery(query, null);
			if(c!=null){
				if(c.moveToFirst()){
					return true;
				}
			}
			return false;
		}
		
		public boolean modificarDatosDetallePedido(DetallePedido dp){
			String sql = "UPDATE detalle_pedido SET cantidad="+dp.getCantidad()+", costo_entrega="+dp.getCosto_entrega()+", costo="+dp.getCosto()+" " +
							"WHERE iddetalle_pedido='"+dp.getIddetalle_pedido()+"'";
			dbSQlite.execSQL(sql);
			return true;
		}
		
		public boolean eliminarDetallePedido(DetallePedido dp){ 
			String query = "DELETE FROM detalle_pedido WHERE iddetalle_pedido='"+dp.getIddetalle_pedido()+"'";
			dbSQlite.execSQL(query);
			if(!verificarDetallePedido(dp)){
				return true;
			}
			return false;
		}
		
		public List<DetallePedido> getListaDetallePedido(String idpedido){
			List<DetallePedido> lista_detalle_pedido = new ArrayList<DetallePedido>();
			Cursor c = dbSQlite.rawQuery("SELECT * FROM detalle_pedido WHERE idpedido='"+idpedido+"'", null);
			if(c!=null){
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
					DetallePedido detalleP = new DetallePedido(c.getString(0), c.getString(1), c.getString(2), c.getInt(3), c.getDouble(4), c.getDouble(5));
					lista_detalle_pedido.add(detalleP);
				}
			}
			return lista_detalle_pedido;
		}
		
		public boolean eliminarDetallePedidoMediantePedido(Pedido p){
			String query ="DELETE FROM detalle_pedido WHERE idpedido='"+p.getIdpedido()+"'";
			dbSQlite.execSQL(query);
			return true;
		}
}
