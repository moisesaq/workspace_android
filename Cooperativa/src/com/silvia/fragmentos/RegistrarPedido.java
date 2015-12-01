package com.silvia.fragmentos;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.silvia.adapters.ListaDetallePedidoAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogAgregarProdPedido;
import com.silvia.dialogos.DialogAsignarPersonalEntrega;
import com.silvia.dialogos.DialogFecha;
import com.silvia.dialogos.DialogMapSucre;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.DetallePedido;
import com.silvia.modelo.Pedido;
import com.silvia.modelo.Personal;

public class RegistrarPedido extends Fragment implements OnClickListener{
	
	public String idusuario;
	public ScrollView scrollParent;
	public ImageView ivImageCliente, ivImagePersonal, ivImageMaq, ivFechaEntrega, ivSeeGoogleMaps;
	public AutoCompleteTextView actvCICliente;
	public TextView tvNombreCliente, tvFechaPedido, tvHoraPedido, tvCIPersonal, tvNombrePersonal, tvPlacaMaq, tvCapacidadMaq, tvAviso, 
						tvCostoTotal, tvFechaEntrega;
	public EditText etDireccionEntrega, etNota;
	public LinearLayout lyVistaMaq, lyVistaCostoTotal;
	public Button btnAgregarProd, btnRegistrar, btnCancelar;
	public ListView lvListaProd;
	
	public Pedido nuevoPedido;
	public List<DetallePedido> lista_detalle_pedido;
	public Cliente cliente;
	public Personal personal;
	public ListaDetallePedidoAdapter my_adapter;
	
	OnRegistrarPedidoClickListener callback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.registrar_pedido, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Registrar nuevo pedido");
		
		Bundle bundle = getArguments();
		this.idusuario = bundle.getString("idusuario");
		Toast.makeText(getActivity(), "Id usuario "+idusuario, Toast.LENGTH_SHORT).show();
		scrollParent = (ScrollView)v.findViewById(R.id.ScrollViewParent);
		scrollParent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				scrollParent.getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});
		
		ivImageCliente = (ImageView)v.findViewById(R.id.ivImagenClientePedido);
		actvCICliente = (AutoCompleteTextView)v.findViewById(R.id.actvCIClientePedido);
		adaptarAutoCompleteTextViewCINitCliente();
		actvCICliente.addTextChangedListener(CIWacher);
		tvNombreCliente = (TextView)v.findViewById(R.id.tvNombreClientePedido);
		tvFechaPedido = (TextView)v.findViewById(R.id.tvFechaPedido);
		tvHoraPedido = (TextView)v.findViewById(R.id.tvHoraPedido);
		tvFechaPedido.setText(Variables.FORMAT_FECHA_3.format(Variables.getFechaActual()));
		tvHoraPedido.setText(new StringBuilder("Hrs. ").append(Variables.getHoraActual().toString()));
		tvFechaEntrega = (TextView)v.findViewById(R.id.tvFechaEntregaPedido);
		tvFechaEntrega.setText(Variables.FORMAT_FECHA_3.format(Variables.getFechaActual()));
		tvFechaEntrega.setTag(Variables.getFechaActual());
		ivFechaEntrega = (ImageView)v.findViewById(R.id.ivFechaEntregaPedido);
		ivFechaEntrega.setOnClickListener(this);
		etDireccionEntrega = (EditText)v.findViewById(R.id.etDireccionEntregaPedido);
		ivSeeGoogleMaps = (ImageView)v.findViewById(R.id.ivSeeGoogleMaps);
		ivSeeGoogleMaps.setOnClickListener(this);
		ivImagePersonal = (ImageView)v.findViewById(R.id.ivSeleccionarPersonalPedido);
		ivImagePersonal.setOnClickListener(this);
		tvCIPersonal = (TextView)v.findViewById(R.id.tvCIPersonalPedido);
		tvNombrePersonal = (TextView)v.findViewById(R.id.tvNombrePersonalPedido);
		lyVistaMaq = (LinearLayout)v.findViewById(R.id.lyVistaMaqPedido);
		ivImageMaq = (ImageView)v.findViewById(R.id.ivImagenMaqPedido);
		tvPlacaMaq = (TextView)v.findViewById(R.id.tvPlacaMaqPedido);
		tvCapacidadMaq = (TextView)v.findViewById(R.id.tvCapacidadMaqPedido);
		btnAgregarProd = (Button)v.findViewById(R.id.btnSeleccionarProdPedido);
		btnAgregarProd.setOnClickListener(this);
		lvListaProd = (ListView)v.findViewById(R.id.lvListaProdPedido);
		lvListaProd.setOnTouchListener(new  OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				view.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaProdPedido);
		etNota = (EditText)v.findViewById(R.id.etNotaPedido);
		lyVistaCostoTotal = (LinearLayout)v.findViewById(R.id.lyVistaCostoTotalPedido);
		tvCostoTotal = (TextView)v.findViewById(R.id.tvCostoTotalPedido);
		btnRegistrar = (Button)v.findViewById(R.id.btnRegistrarPedido);
		btnRegistrar.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarPedido);
		btnCancelar.setOnClickListener(this);
		
	}

	private final TextWatcher CIWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
	
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()==7 || s.length()==8 || s.length()==10){
				Cliente cli = getClientePorCI(s.toString());
				if(cli!=null){
					ivImageCliente.setVisibility(View.VISIBLE);
					if(!cli.getImagen().equals(Variables.SIN_ESPECIFICAR)){
						Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(cli.getImagen()).toString());
						if(bitmap!=null){
							ivImageCliente.setImageBitmap(bitmap);
						}else{
							ivImageCliente.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
						}
					}else{
						ivImageCliente.setImageResource(R.drawable.ic_insert_emoticon_white_48dp);
					}
					tvNombreCliente.setVisibility(View.VISIBLE);
					if(cli.getApellido().equals(Variables.SIN_ESPECIFICAR)){
						tvNombreCliente.setText(new StringBuilder("Nombre: ").append(cli.getNombre()));
					}else{
						tvNombreCliente.setText(new StringBuilder("Nombre: ").append(cli.getNombre()).append(" ").append(cli.getApellido()));
					}
				}
//				else{
//					mensajeConfirmarRegistroRapidoCliente(s.toString());
//				}
			}else{
				ivImageCliente.setVisibility(View.GONE);
				tvNombreCliente.setVisibility(View.GONE);
			}
		}
	};
	
	@Override
	public void onClick(View v){
		switch (v.getId()) {
			case R.id.btnSeleccionarProdPedido:
				iniciarAgregarProductosParaPedido();
				break;
			case R.id.ivSeleccionarPersonalPedido:
				DialogAsignarPersonalEntrega dAsignar = new DialogAsignarPersonalEntrega(ivImagePersonal, tvCIPersonal, tvNombrePersonal, 
						lyVistaMaq, ivImageMaq, tvPlacaMaq, tvCapacidadMaq);
				dAsignar.show(getFragmentManager(), "tagDAsignarPersonal");
				break;
			case R.id.btnRegistrarPedido:
				if(this.nuevoPedido!=null && this.tvCostoTotal.getTag()!=null){
					String ci_cliente = actvCICliente.getText().toString().trim();
					if(!ci_cliente.equals("")){
						iniciarValidacionParaRegistroVenta(ci_cliente);
					}else{
						actvCICliente.requestFocus();
						actvCICliente.setError("Introduzca CI del cliente");
					}
				}else{
					Toast.makeText(getActivity(), "Faltan datos para el pedido, verifique por favor", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.btnCancelarPedido:
				limpiarCampos();
				break;
			case R.id.ivFechaEntregaPedido:
				DialogFecha df = new DialogFecha(tvFechaEntrega);
				df.show(getFragmentManager(), "tagDFE");
				break;
			case R.id.ivSeeGoogleMaps:
				//callback.selectAddressOnMap();
				DialogMapSucre dialogMap = new DialogMapSucre(etDireccionEntrega, ivSeeGoogleMaps, getActivity());
				dialogMap.show(getFragmentManager(), "tagDMS");
				Toast.makeText(getActivity(), "Seleccione direccion en la mapa", Toast.LENGTH_LONG).show();
				break;
		}
	}
	
	private void iniciarAgregarProductosParaPedido() {
		Date fecha = Variables.getFechaActual();
		Time hora = Variables.getHoraActual();
		tvFechaPedido.setText(Variables.FORMAT_FECHA_3.format(fecha));
		tvHoraPedido.setText(new StringBuilder("Hrs. ").append(hora));
		if(nuevoPedido==null){
			Toast.makeText(getActivity(), "Iniciando nuevo pedido", Toast.LENGTH_SHORT).show();
			lista_detalle_pedido = new ArrayList<DetallePedido>();
			nuevoPedido = new Pedido(generarIdPedido(), this.idusuario, Variables.ID_CLIENTE_DEFAULT, fecha, 
										hora, fecha, Variables.ID_USER_ADMIN,
										Variables.SIN_ESPECIFICAR, Variables.LATITUDE_DEFAULT, Variables.LONGITUDE_DEFAULT,
										Variables.COSTO_TOTAL_DEFAULT, Variables.SIN_ESPECIFICAR, Variables.PEDIDO_PENDIENTE);
			my_adapter = new ListaDetallePedidoAdapter(getActivity(), lista_detalle_pedido, this);
			lvListaProd.setAdapter(my_adapter);
		}
		DialogAgregarProdPedido dAgregar = new DialogAgregarProdPedido(this);
		dAgregar.show(getFragmentManager(), "tagDAgregarProdPedido");
	}

	public void iniciarValidacionParaRegistroVenta(String ci_cliente){
		this.cliente = getClientePorCI(ci_cliente);
		Time hora = Variables.getHoraActual();
		tvHoraPedido.setText(new StringBuilder("Hrs. ").append(hora));
		String txtNota = etNota.getText().toString();
		if(txtNota.equals("")){
			txtNota = Variables.SIN_ESPECIFICAR;
		}
		if(this.cliente!=null){
			if(tvNombrePersonal.getTag()!=null){
				String direccionEntrega = etDireccionEntrega.getText().toString();
				if(!direccionEntrega.equals("")){
					if(etDireccionEntrega.getTag()!=null){
						double latitude = (Double)etDireccionEntrega.getTag();
						double longitude = (Double)ivSeeGoogleMaps.getTag();
						nuevoPedido.setLatitude(latitude);
						nuevoPedido.setLongitude(longitude);
					}
					Date fechaEntrega = (Date)tvFechaEntrega.getTag();
					if(fechaEntrega!=null){
						this.personal = (Personal)tvNombrePersonal.getTag();
						nuevoPedido.setHora_pedido(hora);
						nuevoPedido.setFecha_entrega(fechaEntrega);
						nuevoPedido.setIdcliente(this.cliente.getIdcliente());
						nuevoPedido.setIdpersonal(this.personal.getIdpersonal());
						nuevoPedido.setDireccion(direccionEntrega);
						nuevoPedido.setCosto_total((Double)tvCostoTotal.getTag());
						nuevoPedido.setNota(txtNota);
						registrarPedido();
					}else{
						Toast.makeText(getActivity(), "Elija una fecha de entrega", Toast.LENGTH_LONG).show();
					}
				}else{
					etDireccionEntrega.requestFocus();
					etDireccionEntrega.setError("Introduzca dirección de entrega");
				}
			}else{
				Toast.makeText(getActivity(), "Seleccione un personal para la entrega del pedido", Toast.LENGTH_LONG).show();
			}
		}else{
			if(ci_cliente.length()==7 || ci_cliente.length()==8 || ci_cliente.length()==10){
				mensajeConfirmarRegistroRapidoCliente(ci_cliente);
			}else{
				actvCICliente.requestFocus();
				actvCICliente.setError("CI o NIT debe tener 7, 8 o 10 digitos");
			}
		}
	}
	
	public void registrarPedido(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.insertarPedido(nuevoPedido)){
				boolean control = false;
				for (int i = 0; i < my_adapter.getCount(); i++) {
					if(db.insertarDetallePedido(my_adapter.getItem(i))){
						control=true;
					}else{
						control = false;
					}
				}
				if(control){
					//mensajePedidoRegistrado();
					Toast.makeText(getActivity(), "Pedido registrado correctamente", Toast.LENGTH_LONG).show();
					limpiarCampos();
				}else{
					Toast.makeText(getActivity(), "Pedido no se registro correctamente", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getActivity(), "No se pudo registrar pedido", Toast.LENGTH_LONG).show();
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mensajePedidoRegistrado(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Pedido registrado correctamente"));
		dialog.setMessage(new StringBuilder("¿Que desea hacer con la boleta detalle de pedido?"));
		
		dialog.setNegativeButton(R.string.ver_imprimir_info, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mostrarInfoDetalleVentaPdf();
			}
		});
		
		dialog.setPositiveButton(R.string.nada, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				limpiarCampos();
			}
		});
		
		AlertDialog d = dialog.show();
		int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(getResources().getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public void mostrarInfoDetalleVentaPdf(){
//		PDFCustomManager pdfManager = new PDFCustomManager(getActivity(), new StringBuilder("info_").append(nuevo_pedido.getIdpedido()).toString());
//		if(pdfManager.existeInfoPDF()){
//			pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
//			limpiarCampos();
//		}else{
//			if(nuevo_pedido!=null && cliente!=null && lista_detalle_pedido!=null){
//				if(pdfManager.crearInfoDetalleVentaPDF(nuevo_pedido, cliente, personal, lista_detalle_pedido)){
//					pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
//					limpiarCampos();
//				}else{
//					Toast.makeText(getActivity(), "Error al generar o abrir el detalle en PDF",  Toast.LENGTH_LONG).show();
//				}				
//			}else{
//				Toast.makeText(getActivity(), "Ocurrio un error",  Toast.LENGTH_SHORT).show();
//			}
//			
//		}
	}
	
	public void mensajeConfirmarRegistroRapidoCliente(final String CICliente){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("Nota");
		View view = getActivity().getLayoutInflater().inflate(R.layout.modelo_registro_rapido_cliente, null);
		final EditText etNombre = (EditText)view.findViewById(R.id.etNombreRegRapidoCliente);
		TextView tvNota = (TextView)view.findViewById(R.id.tvNotaRegRapidoCliente);
		tvNota.setText(new StringBuilder("El CI: ").append(CICliente).append(" no esta registrado, para realizar un registro rapido introduzca el nombre"));
		dialog.setNeutralButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String nombre = etNombre.getText().toString().trim();
				if(!nombre.equals("")){
					registroRapidoCliente(CICliente, nombre);
					dialog.dismiss();
				}else{
					Toast.makeText(getActivity(), "No se registro cliente por falta de nombre", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setView(view);
		AlertDialog d = dialog.show();
		int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(getResources().getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public void registroRapidoCliente(String CI, String nombre){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			Cliente cliente = new Cliente(generarIdCliente(), CI, nombre, Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR, 0, 
											Variables.SIN_ESPECIFICAR, Variables.SIN_ESPECIFICAR, Variables.FECHA_DEFAULT, Variables.getFechaActual(), 
											Variables.SIN_ESPECIFICAR, Variables.NO_ELIMINADO);
			if(db.insertarCliente(cliente)){
				Toast.makeText(getActivity(), "Cliente registrado", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getActivity(), "No se pudo registrar cliente", Toast.LENGTH_SHORT).show();
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void adaptarAutoCompleteTextViewCINitCliente() {
		String[] lista_ci_clientes = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Cliente> lista = db.getTodosLosClientes();
			if(lista.size()!=0 && lista!=null){
				lista_ci_clientes = new String[lista.size()];
				for (int i = 0; i < lista.size(); i++) {
					lista_ci_clientes[i] = lista.get(i).getCi();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista_ci_clientes);
				actvCICliente.setThreshold(2);
				actvCICliente.setAdapter(adapter);
			}
			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		tvFechaPedido.setText(Variables.FORMAT_FECHA_3.format(Variables.getFechaActual()));
		tvHoraPedido.setText(new StringBuilder("Hrs. ").append(Variables.getHoraActual().toString()));
		//Toast.makeText(getActivity(), "Continuando Resgistrar Venta", Toast.LENGTH_SHORT).show();
		if(my_adapter!=null){
			if(my_adapter.getCount()!=0){
				tvAviso.setVisibility(View.INVISIBLE);
				lvListaProd.setVisibility(View.VISIBLE);
				lvListaProd.setAdapter(my_adapter);
				if(nuevoPedido.getCosto_total()!=0){
					lyVistaCostoTotal.setVisibility(View.VISIBLE);
					tvCostoTotal.setText(new StringBuilder().append(nuevoPedido.getCosto_total()).append(" Bs."));
					tvCostoTotal.setTag(nuevoPedido.getCosto_total());
				}
			}
		}
	}
	
	public String generarIdPedido(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String ventaCode = "Pedido-"+date;
		return ventaCode;
	}
	
	public String generarIdCliente(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "cli-"+date;
		return imageCode;
	}
		
	public Cliente getClientePorCI(String CI){
		Cliente cliente = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			cliente = db.getClientePorCI(CI);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
	
	public void limpiarCampos(){
		actvCICliente.getText().clear();
		this.cliente = null;
		this.personal = null;
		tvFechaEntrega.setText(Variables.FORMAT_FECHA_3.format(Variables.getFechaActual()));
		etDireccionEntrega.getText().clear();
		lyVistaMaq.setVisibility(View.GONE);
		tvCIPersonal.setVisibility(View.GONE);
		tvNombrePersonal.setText(new StringBuilder("Seleccione personal"));
		lista_detalle_pedido = null;
		nuevoPedido = null;
		my_adapter = null;
		lvListaProd.setVisibility(View.INVISIBLE);
		tvAviso.setVisibility(View.VISIBLE);
		tvCostoTotal.setTag(null);
		lyVistaCostoTotal.setVisibility(View.GONE);
		etNota.getText().clear();
	}
	
	public interface OnRegistrarPedidoClickListener{
		public void selectAddressOnMap();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			callback = (OnRegistrarPedidoClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar metodo de OnRegistrarPedidoClickListener");
		}
	}
	
}
