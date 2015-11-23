package com.silvia.fragmentos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.silvia.adapters.ListaVentaAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.PDFCustomManager;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogDetalleVenta;
import com.silvia.dialogos.DialogEnviarInformeVentas;
import com.silvia.dialogos.DialogVentaPorCliente;
import com.silvia.dialogos.DialogVentaPorFecha;
import com.silvia.modelo.Venta;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class InformeVentas extends Fragment implements OnItemClickListener, OnClickListener, OnCheckedChangeListener, OnMenuItemClickListener{

	public LinearLayout lyDatosIngresos, lyMas;
	public ListView lvInforme;
	public CheckBox cbVerIngresos;
	public Button btnInfoPdf;
	public ImageButton ibtnMas;
	public TextView tvTitulo, tvCantVDirectas, tvCantVDomicilio, tvCantVRegistrados, tvIngresoTotal, tvAviso;
	public List<Venta> lista_ventas = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.informe_ventas, container, false);
		inicializarComponentes(v);
		return v;
	}
	
	public void inicializarComponentes(View v) {
		lyDatosIngresos = (LinearLayout)v.findViewById(R.id.lyDatosInformeVentas);
		tvTitulo = (TextView)v.findViewById(R.id.tvTituloInfoInformeVentas);
		cbVerIngresos = (CheckBox)v.findViewById(R.id.cbVerIngresosInformeVentas);
		if(getShareVerInfoIngresos()==Variables.VER_INFO_INGRESOS){
			cbVerIngresos.setChecked(true);
		}else{
			cbVerIngresos.setChecked(false);
		}
		cbVerIngresos.setOnCheckedChangeListener(this);
		lyMas = (LinearLayout)v.findViewById(R.id.lyMasInformeVentas);
		btnInfoPdf = (Button)v.findViewById(R.id.btnInfoPdfInformeVentas);
		btnInfoPdf.setOnClickListener(this);
		ibtnMas = (ImageButton)v.findViewById(R.id.ibtnMasInformeVentas);
		ibtnMas.setOnClickListener(this);
		tvCantVDirectas = (TextView)v.findViewById(R.id.tvCantVDirectasInformeVentas);
		tvCantVDomicilio = (TextView)v.findViewById(R.id.tvCantVDomicilioInformeVentas);
		tvCantVRegistrados = (TextView)v.findViewById(R.id.tvCantVentasRegistradosInformeVentas);
		tvIngresoTotal = (TextView)v.findViewById(R.id.tvIngresoTotalInformeVentas);
		lvInforme = (ListView)v.findViewById(R.id.lvInformeVentas);
		lvInforme.setOnItemClickListener(this);
		tvAviso = (TextView)v.findViewById(R.id.tvNotaInformeVentas);
		lista_ventas = getAllVentas();
		cargarMostrarInformeVentas(lista_ventas);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_ventas_de_la_semana).setVisible(true);
		menu.findItem(R.id.action_ventas_mes_actual).setVisible(true);
		menu.findItem(R.id.action_ventas_fecha_especifico).setVisible(true);
		menu.findItem(R.id.action_ventas_mes_especifico).setVisible(true);
		menu.findItem(R.id.action_ventas_por_cliente).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.action_ventas_de_la_semana:
			//Toast.makeText(getActivity(), "1 semana antes", Toast.LENGTH_SHORT).show();
			tvTitulo.setText(new StringBuilder("Ventas de la Semana"));
			this.lista_ventas = getListaVentasDeLaSemana(getFechaActual());
			cargarMostrarInformeVentas(lista_ventas);
			return true;
		case R.id.action_ventas_mes_actual:
			Date fecha_actual = getFechaActual();
			this.lista_ventas = getListaVentasPorMes(fecha_actual);
			cargarMostrarInformeVentas(lista_ventas);
			tvTitulo.setText(new StringBuilder("Ventas del mes actual - ").append(Variables.FORMAT_FECHA_4.format(fecha_actual).toUpperCase()));
			return true;
		case R.id.action_ventas_fecha_especifico:
			DialogVentaPorFecha dVentasFecha = new DialogVentaPorFecha(this, Variables.OPCION_VENTA_POR_FECHA);
			dVentasFecha.show(getFragmentManager(), "tagVentasPorFecha");
			return true;
		case R.id.action_ventas_mes_especifico:
			DialogVentaPorFecha dVentasMes = new DialogVentaPorFecha(this, Variables.OPCION_VENTA_POR_MES);
			dVentasMes.show(getFragmentManager(), "tagVentasMes");
			return true;
		case R.id.action_ventas_por_cliente:
			DialogVentaPorCliente dVentasCliente = new DialogVentaPorCliente(this);
			dVentasCliente.show(getFragmentManager(), "tagVentasCliente");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cargarMostrarInformeVentas(List<Venta> lista_ventas) {
		if(lista_ventas.size()!=0 && lista_ventas!=null){
			tvAviso.setVisibility(View.INVISIBLE);
			lvInforme.setVisibility(View.VISIBLE);
			lyMas.setVisibility(View.VISIBLE);
			tvCantVDirectas.setText(new StringBuilder("Cant. V. Directas: ").append(getCantidadVentasDirectas(lista_ventas)));
			tvCantVDomicilio.setText(new StringBuilder("Cant. V. Domicilio: ").append(getCantidadVentasDomicilio(lista_ventas)));
			tvCantVRegistrados.setText(new StringBuilder("Cant. V. Registrados: ").append(lista_ventas.size()));
			tvIngresoTotal.setText(new StringBuilder("Ingreso Total:").append(getIngresoTotal(lista_ventas)).append(" Bs."));
			if(getShareVerInfoIngresos()==Variables.VER_INFO_INGRESOS){
				lyDatosIngresos.setVisibility(View.VISIBLE);
			}else{
				lyDatosIngresos.setVisibility(View.GONE);
			}
			//ArrayAdapter<Venta> adapter = new ArrayAdapter<Venta>(getActivity(), android.R.layout.simple_list_item_1, lista);
			ListaVentaAdapter adapter = new ListaVentaAdapter(getActivity(), lista_ventas);
			lvInforme.setAdapter(adapter);
		}else{
			cbVerIngresos.setChecked(false);
			tvAviso.setVisibility(View.VISIBLE);
			lvInforme.setVisibility(View.INVISIBLE);
			lyDatosIngresos.setVisibility(View.GONE);
			lyMas.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Venta venta = (Venta)adapter.getAdapter().getItem(position);
		DialogDetalleVenta dDetalleV = new DialogDetalleVenta(venta);
		dDetalleV.show(getFragmentManager(), "tagDDVenta");
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnInfoPdf.getId()){
			abrirInfoMasPdfPopupMenu();
		}else if(v.getId()==ibtnMas.getId()){
			abrirInfoMasPopupMenu();
		}
	}
	
	private void abrirInfoMasPdfPopupMenu() {
		PopupMenu pmInfoPdf = new PopupMenu(getActivity(), btnInfoPdf);
		pmInfoPdf.setOnMenuItemClickListener(this);
		pmInfoPdf.inflate(R.menu.popup_menu_info_pdf);
		pmInfoPdf.show();
	}
	
	public void abrirInfoMasPopupMenu(){
		PopupMenu pmInfoMas = new PopupMenu(getActivity(), btnInfoPdf);
		pmInfoMas.setOnMenuItemClickListener(this);
		pmInfoMas.inflate(R.menu.popup_menu_info_mas);
		pmInfoMas.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_ventas_todas:
				cargarMostrarInformeVentas(lista_ventas);
				return true;
			case R.id.action_ventas_directas:
				cargarMostrarInformeVentas(getListaVentasPorTipo(lista_ventas, Variables.VENTA_DIRECTA));
				return true;
			case R.id.action_ventas_a_domicilio:
				cargarMostrarInformeVentas(getListaVentasPorTipo(lista_ventas, Variables.VENTA_A_DOMICILIO));
				return true;
			case R.id.action_ver_imprimir_info_pdf:
				String name_informe = Variables.getNuevoCodeUnico("informe_ventas_");
				PDFCustomManager pdfCM = new PDFCustomManager(getActivity(), name_informe);
				if(pdfCM.crearInformeVentasPDF(this)){
					pdfCM.verEmprimirInfoPdf("Leendo informe de ventas en pdf");
				}else{
					Toast.makeText(getActivity(), "Error al generar o abrir el informe en pdf", Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.action_enviar_info_pdf:
				DialogEnviarInformeVentas dEnviar = new DialogEnviarInformeVentas(this);
				dEnviar.show(getFragmentManager(), "tagEnviar");
				return true;
		}
		return false;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getId()==cbVerIngresos.getId()){
			if(tvAviso.getVisibility()==View.INVISIBLE){
				if(isChecked){
					//Toast.makeText(getActivity(), "Chekeado", Toast.LENGTH_SHORT).show();
					setShareVerInfoIngresos(Variables.VER_INFO_INGRESOS);
					lyDatosIngresos.setVisibility(View.VISIBLE);
				}else{
					setShareVerInfoIngresos(Variables.NO_VER_INFO_INGRESOS);
					lyDatosIngresos.setVisibility(View.GONE);
				}
			}else{
				//cbVerIngresos.setEnabled(false);
				Toast.makeText(getActivity(), "Lista vacia no hay datos para mostrar", Toast.LENGTH_LONG).show();
				cbVerIngresos.setChecked(false);
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Toast.makeText(getActivity(), "Continuando Informe ventas", Toast.LENGTH_SHORT).show();
	}

	public void setShareVerInfoIngresos(int ver_ingresos_estado){
		SharedPreferences shareVerInfoIngresos = getActivity().getSharedPreferences("ShareVerIngresos", Context.MODE_PRIVATE);
		SharedPreferences.Editor editar = shareVerInfoIngresos.edit();
		editar.putInt("ver_ingresos", ver_ingresos_estado);
		editar.commit();
	}

	public int getShareVerInfoIngresos(){
		SharedPreferences shareVerInfoIngresos = getActivity().getSharedPreferences("ShareVerIngresos", Context.MODE_PRIVATE);
		int ver_ingresos_estado = shareVerInfoIngresos.getInt("ver_ingresos", Variables.NO_VER_INFO_INGRESOS);
		return ver_ingresos_estado;
	}

	//------------------------------------METODOS PARA TRABAJAR CON LA LISTA DE VENTAS----------------------------------------------
	
	public List<Venta> getListaVentasDeLaSemana(Date fecha){
		List<Venta> lista = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista = db.getVentasDeLaSemana(fecha);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public List<Venta> getListaVentasPorMes(Date fecha){
		List<Venta> lista = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista = db.getVentasPorMes(fecha);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public List<Venta> getAllVentas(){
		List<Venta> lista = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista = db.getTodasLasVentas();
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
		
	public int getCantidadVentasDirectas(List<Venta> lista){
		int cant = 0;
		for(int i = 0; i<lista.size(); i++){
			Venta venta = lista.get(i);
			if(venta.getTipo_venta()==Variables.VENTA_DIRECTA){
				cant++;
			}
		}
		return cant;
	}
	
	public int getCantidadVentasDomicilio(List<Venta> lista){
		int cant = 0;
		for(int i = 0; i<lista.size(); i++){
			Venta venta = lista.get(i);
			if(venta.getTipo_venta()==Variables.VENTA_A_DOMICILIO){
				cant++;
			}
		}
		return cant;
	}
	
	public List<Venta> getListaVentasPorTipo(List<Venta> lista_ventas, int tipo){
		List<Venta> lista = new ArrayList<Venta>();
		for (int i = 0; i < lista_ventas.size(); i++) {
			Venta venta = lista_ventas.get(i);
			if(venta.getTipo_venta()==tipo){
				lista.add(venta);
			}
		}
		return lista;
	}
	
	public double getIngresoTotal(List<Venta> lista){
		double ingreso = 0;
		for(int i = 0; i<lista.size(); i++){
			Venta venta = lista.get(i);
			ingreso = ingreso+venta.getCosto_total();
		}
		return ingreso;
	}
	
	public Date getFechaActual(){
		java.util.Date fecha = new java.util.Date();
		return new Date(fecha.getTime());
	}
	
	public Date getFechaHaceUnaSemana(){
		java.util.Date date = new java.util.Date();
		int dias = -6;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return new Date(calendar.getTime().getTime());
	}
	
}
