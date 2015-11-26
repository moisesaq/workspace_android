package com.silvia.cooperativa;

import com.silvia.dialogos.DialogDetallePedido.OnDetallePedidoClickListener;
import com.silvia.dialogos.DialogDetalleProd.OnEditarProdClickListener;
import com.silvia.fragmentos.DetalleCliente;
import com.silvia.fragmentos.DetalleCliente.OnDetalleClienteClickListener;
import com.silvia.fragmentos.ListaClientes;
import com.silvia.fragmentos.ListaClientes.OnListaClienteClickListener;
import com.silvia.fragmentos.ListaPedidos;
import com.silvia.fragmentos.ListaPedidos.OnListaPedidosClickListener;
import com.silvia.fragmentos.ListaProductos;
import com.silvia.fragmentos.ListaUsuarios;
import com.silvia.fragmentos.MapSucre;
import com.silvia.fragmentos.NuevoCliente;
import com.silvia.fragmentos.RegistrarPedido;
import com.silvia.fragmentos.NuevoCliente.OnBackFromNuevoClienteClickListener;
import com.silvia.fragmentos.NuevoProducto;
import com.silvia.fragmentos.NuevoProducto.OnBackToListaProdClickListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TercerActivity extends Activity implements OnBackFromNuevoClienteClickListener, OnDetalleClienteClickListener,
														OnListaClienteClickListener, OnBackToListaProdClickListener, OnEditarProdClickListener,
														OnListaPedidosClickListener, OnDetallePedidoClickListener{

	public int ACCION;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tercer_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle cajon = getIntent().getExtras();
		this.ACCION = cajon.getInt("accion");
		if(ACCION==Variables.ACCION_CARGAR_NUEVO_CLIENTE){
			Bundle msjNC = new Bundle();
			msjNC.putInt("accion", Variables.ACCION_NUEVO_CLIENTE);
			NuevoCliente nuevoCliente = new NuevoCliente();
			nuevoCliente.setArguments(msjNC);
			cargarFragmento(nuevoCliente, "tagNCliente");
		}else if (ACCION==Variables.ACCION_CARGAR_LISTA_CLIENTES) {
			cargarFragmento(new MapSucre(), "tagListaC");
		}else if (ACCION==Variables.ACCION_CARGAR_LISTA_PRODUCTOS) {
			cargarFragmento(new ListaProductos(), "tagListaProd");
		}else if (ACCION==Variables.ACCION_CARGAR_LISTA_USUARIOS) {
			cargarFragmento(new ListaUsuarios(), "tagListaUsers");
		}else if(ACCION==Variables.ACCION_CARGAR_DETALLE_CLIENTE){
			String idcliente = cajon.getString("idcliente");
			onVerDetalleClienteClick(idcliente);
		}else if(ACCION==Variables.ACCION_CARGAR_LISTA_PEDIDO){
			Bundle msjIDUsuario = new Bundle();
			msjIDUsuario.putString("idusuario", cajon.getString("idusuario"));
			ListaPedidos listaPedidos = new ListaPedidos();
			listaPedidos.setArguments(msjIDUsuario);
			cargarFragmento(listaPedidos, "tagListaPedidos");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.tercer_menu, menu);
		menu.findItem(R.id.action_nuevo_cliente).setVisible(false);
		menu.findItem(R.id.action_editar_cliente).setVisible(false);
		menu.findItem(R.id.action_eliminar_cliente).setVisible(false);
		menu.findItem(R.id.action_listo_cliente).setVisible(false);
		menu.findItem(R.id.action_nuevo_prod).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_nuevo_cliente:
			Bundle msjNCliente = new Bundle();
			msjNCliente.putInt("accion", Variables.ACCION_NUEVO_CLIENTE);
			NuevoCliente nuevoC = new NuevoCliente();
			nuevoC.setArguments(msjNCliente);
			cargarFragmento(nuevoC, "tagNuevoC");
			return true;
		
		case R.id.action_listo_cliente:
			onBackPressed();
			return true;
		
		case R.id.action_nuevo_prod:
			Bundle msjNProd = new Bundle();
			msjNProd.putInt("accion", Variables.ACCION_NUEVO_PROD);
			NuevoProducto nuevoP = new NuevoProducto();
			nuevoP.setArguments(msjNProd);
			cargarFragmento(nuevoP, "tagNProd");
			return true;
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cargarFragmento(Fragment fragmento, String tag){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.myTercerContenedor, fragmento);
		ft.commit();
	}
	
	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount()>1){
			getFragmentManager().popBackStack();
		}else{
			this.finish();
		}
	}

	@Override
	public void onBackFromNuevoClienteClick() {
		onBackPressed();
	}

	@Override
	public void onBackToListaProdClick() {
		onBackPressed();
	}

	@Override
	public void onEditarProdClick(String idprod) {
		Bundle msjProd = new Bundle();
		msjProd.putInt("accion", Variables.ACCION_EDITAR_PROD);
		msjProd.putString("idprod", idprod);
		NuevoProducto nuevoProd = new NuevoProducto();
		nuevoProd.setArguments(msjProd);
		cargarFragmento(nuevoProd, "tagEditarProd");
	}
	
	//metodo de listener de detalle cliente
	@Override
	public void onEditarClienteClick(String idcliente) {
		Bundle msj = new Bundle();
		msj.putString("idcliente", idcliente);
		msj.putInt("accion", Variables.ACCION_EDITAR_CLIENTE);
		NuevoCliente nuevoC = new NuevoCliente();
		nuevoC.setArguments(msj);
		cargarFragmento(nuevoC, "tagEditarCliente");
	}
	
	@Override
	public void onBackFromDetalleClienteClick() {
		onBackPressed();
	}
	
	//metodo de listener de lista de clientes
	@Override
	public void onVerDetalleClienteClick(String idcliente) {
		Bundle msjIdCliente = new Bundle();
		msjIdCliente.putString("idcliente", idcliente);
		DetalleCliente dCliente = new DetalleCliente();
		dCliente.setArguments(msjIdCliente);
		cargarFragmento(dCliente, "tagDCliente");
	}

	@Override
	public void onNuevoPedidoClick(String idusuario) {
		Bundle msjIDU = new Bundle();
		msjIDU.putString("idusuario", idusuario);
		RegistrarPedido reg_pedido = new RegistrarPedido();
		reg_pedido.setArguments(msjIDU);
		cargarFragmento(reg_pedido, "tagRegPedido");
	}

	@Override
	public void onEditarDetallePedidoClick(int accion) {
		Toast.makeText(this, "Ir a editarrrrr", Toast.LENGTH_SHORT).show();
	}

	
}
