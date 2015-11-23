package com.silvia.fragmentos;

import java.util.ArrayList;
import java.util.List;

import com.silvia.adapters.ListaPedidoAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogDetallePedido;
import com.silvia.modelo.Pedido;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListaPedidos extends Fragment implements OnItemClickListener{

	public View v;
	public ListView lvListaPedidos;
	public TextView tvNota;
	public List<Pedido> lista_pedidos;
	public OnListaPedidosClickListener listener;
	public String idusuario;
	public ListaPedidoAdapter my_adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.lista_pedidos, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(v);
		return v;
	}
	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista de pedidos");
		Bundle bundle = getArguments();
		this.idusuario = bundle.getString("idusuario");
		lvListaPedidos =(ListView)v.findViewById(R.id.lvListaPedidos);
		lvListaPedidos.setOnItemClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaPedidos);
		lista_pedidos = getTodosLosPedidos();
		cargarMostrarListaPedidos(lista_pedidos);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_nuevo_pedido).setVisible(true);
		menu.findItem(R.id.action_pedidos_entregados).setVisible(true);
		menu.findItem(R.id.action_pedidos_pendientes).setVisible(true);
		menu.findItem(R.id.action_pedidos_todos).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case R.id.action_nuevo_pedido:
				listener.onNuevoPedidoClick(idusuario);
				return true;
			case R.id.action_pedidos_todos:
				cargarMostrarListaPedidos(getTodosLosPedidos());
				return true;
			case R.id.action_pedidos_entregados:
				cargarMostrarListaPedidos(filtrarPedidoPorEstado(lista_pedidos, Variables.PEDIDO_ENTREGADO));
				return true;
			case R.id.action_pedidos_pendientes:
				cargarMostrarListaPedidos(filtrarPedidoPorEstado(lista_pedidos, Variables.PEDIDO_PENDIENTE));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cargarMostrarListaPedidos(List<Pedido> lista){
		if(lista.size()!=0 && lista!=null){
			tvNota.setVisibility(View.GONE);
			lvListaPedidos.setVisibility(View.VISIBLE);
			my_adapter = new ListaPedidoAdapter(getActivity(), lista);
			lvListaPedidos.setAdapter(my_adapter);
		}else{
			tvNota.setVisibility(View.VISIBLE);
			lvListaPedidos.setVisibility(View.GONE);
		}
	}
	
	public List<Pedido> getTodosLosPedidos(){
		List<Pedido> lista = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista = db.getTodasLosPedidos();
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Pedido pedido = (Pedido)adapter.getAdapter().getItem(position);
		DialogDetallePedido dDetalleP = new DialogDetallePedido(pedido, this);
		dDetalleP.show(getFragmentManager(), "tagDDP");
	}
	
	public interface OnListaPedidosClickListener{
		public void onNuevoPedidoClick(String idusuario);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnListaPedidosClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar metodo de onListaPedidoClickListener");
		}
	}

	public List<Pedido> filtrarPedidoPorEstado(List<Pedido> lista_pedidos, int estado){
		List<Pedido> lista = new ArrayList<Pedido>();
		for (int i = 0; i < lista_pedidos.size(); i++) {
			Pedido pedido = lista_pedidos.get(i);
			if(pedido.getEstado()==estado){
				lista.add(pedido);
			}
		}
		return lista;
	}
}
