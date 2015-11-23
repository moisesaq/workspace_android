package com.silvia.fragmentos;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.silvia.adapters.ListaDetalleVentaAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.PDFCustomManager;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogAgregarProducto;
import com.silvia.dialogos.DialogAsignarPersonalEntrega;
import com.silvia.dialogos.DialogEnviarInformeVentas;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Venta;

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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class RegistrarVenta extends Fragment implements OnClickListener, OnItemSelectedListener{

	public ScrollView scrollParent;
	public Spinner spTipoVenta;
	public AutoCompleteTextView actvCICliente;
	public ImageView ivImagePersonal, ivImageMaq, ivImageCliente;
	public EditText etDireccionEntrega, etNota;
	public TextView tvFechaVenta, tvHoraVenta, tvCIPersonal, tvNombrePersonal, tvPlacaMaq, tvCapacidadMaq, tvAviso, tvCostoTotal, tvNombreCliente;
	public ListView lvListaProd;
	public Button btnAgregarProd, btnRegistrar, btnCancelar;
	public LinearLayout lyVistaPersonalDeEntrega, lyVistaMaq, lyVistaCostoTotal;
	
	public int TIPO_VENTA;
	public String idusuario;
	
	public Venta nueva_venta;
	public Cliente cliente;
	public Personal personal;
	
	public List<DetalleVenta> lista_detalle_venta;
	public ListaDetalleVentaAdapter my_adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.registrar_venta, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		this.idusuario = getArguments().getString("idusuario");
		scrollParent = (ScrollView)v.findViewById(R.id.ScrollViewParent);
		scrollParent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				scrollParent.getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});
		spTipoVenta = (Spinner)v.findViewById(R.id.spinnerTipoVenta);
		cargarAdaptarSpinnerTipoVenta();
		spTipoVenta.setOnItemSelectedListener(this);
		ivImageCliente = (ImageView)v.findViewById(R.id.ivImagenClienteVenta);
		actvCICliente = (AutoCompleteTextView)v.findViewById(R.id.actvCIClienteVenta);
		adaptarAutoCompleteTextViewCINitCliente();
		actvCICliente.addTextChangedListener(CIWacher);
		tvNombreCliente = (TextView)v.findViewById(R.id.tvNombreClienteVenta);
		tvFechaVenta = (TextView)v.findViewById(R.id.tvFechaVenta);
		tvHoraVenta = (TextView)v.findViewById(R.id.tvHoraVenta);
		tvFechaVenta.setText(Variables.FORMAT_FECHA_3.format(Variables.getFechaActual()));
		tvHoraVenta.setText(new StringBuilder("Hrs. ").append(Variables.getHoraActual().toString()));
		lyVistaPersonalDeEntrega = (LinearLayout)v.findViewById(R.id.lyVistaPersonalDeEntregaVenta);
		etDireccionEntrega = (EditText)v.findViewById(R.id.etDireccionEntregaVenta);
		ivImagePersonal = (ImageView)v.findViewById(R.id.ivSeleccionarPersonalVenta);
		ivImagePersonal.setOnClickListener(this);
		tvCIPersonal = (TextView)v.findViewById(R.id.tvCIPersonalVenta);
		tvNombrePersonal = (TextView)v.findViewById(R.id.tvNombrePersonalVenta);
		lyVistaMaq = (LinearLayout)v.findViewById(R.id.lyVistaMaqVenta);
		ivImageMaq = (ImageView)v.findViewById(R.id.ivImagenMaqVenta);
		tvPlacaMaq = (TextView)v.findViewById(R.id.tvPlacaMaqVenta);
		tvCapacidadMaq = (TextView)v.findViewById(R.id.tvCapacidadMaqVenta);
		btnAgregarProd = (Button)v.findViewById(R.id.btnSeleccionarProdVenta);
		btnAgregarProd.setOnClickListener(this);
		lvListaProd = (ListView)v.findViewById(R.id.lvListaProdVenta);
		lvListaProd.setOnTouchListener(new  OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				view.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoListaProdVenta);
		lyVistaCostoTotal = (LinearLayout)v.findViewById(R.id.lyVistaCostoTotalVenta);
		tvCostoTotal = (TextView)v.findViewById(R.id.tvCostoTotalVenta);
		etNota = (EditText)v.findViewById(R.id.etNotaVenta);
		btnRegistrar = (Button)v.findViewById(R.id.btnRegistrarVenta);
		btnRegistrar.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarVenta);
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
		if(v.getId()==btnAgregarProd.getId()){
			Date fecha = Variables.getFechaActual();
			Time hora = Variables.getHoraActual();
			tvFechaVenta.setText(Variables.FORMAT_FECHA_3.format(fecha));
			tvHoraVenta.setText(new StringBuilder("Hrs. ").append(hora));
			if(nueva_venta==null){
				Toast.makeText(getActivity(), "Iniciando nueva venta", Toast.LENGTH_SHORT).show();
				lista_detalle_venta = new ArrayList<DetalleVenta>();
				nueva_venta = new Venta(generarIdVenta(), TIPO_VENTA, idusuario, Variables.ID_CLIENTE_DEFAULT, fecha, hora, 
									Variables.ID_USER_ADMIN, Variables.SIN_ESPECIFICAR, 0, Variables.SIN_ESPECIFICAR);
				my_adapter = new ListaDetalleVentaAdapter(getActivity(), lista_detalle_venta, this, Variables.PROV_VENTA);
				lvListaProd.setAdapter(my_adapter);
			}
			DialogAgregarProducto dAgregar = new DialogAgregarProducto(this);
			dAgregar.show(getFragmentManager(), "tagDAgregarProd");
		}else if (v.getId()==ivImagePersonal.getId()) {
			DialogAsignarPersonalEntrega dAsignar = new DialogAsignarPersonalEntrega(ivImagePersonal, tvCIPersonal, tvNombrePersonal, 
																lyVistaMaq, ivImageMaq, tvPlacaMaq, tvCapacidadMaq);
			dAsignar.show(getFragmentManager(), "tagDAsignarPersonal");
		}else if (v.getId()==btnRegistrar.getId()) {
			if(this.nueva_venta!=null && this.tvCostoTotal.getTag()!=null){
				String ci_cliente = actvCICliente.getText().toString().trim();
				if(!ci_cliente.equals("")){
					iniciarValidacionParaRegistroVenta(ci_cliente);
				}else{
					actvCICliente.requestFocus();
					actvCICliente.setError("Introduzca CI del cliente");
				}
			}else{
				Toast.makeText(getActivity(), "Faltan datos para la venta, llene correctamente por favor", Toast.LENGTH_LONG).show();
			}
		}else if(v.getId()==btnCancelar.getId()){
			limpiarCampos();
		}
	}
	
	public void iniciarValidacionParaRegistroVenta(String ci_cliente){
		this.cliente = getClientePorCI(ci_cliente);
		Time hora = Variables.getHoraActual();
		tvHoraVenta.setText(new StringBuilder("Hrs. ").append(hora));
		String txtNota = etNota.getText().toString();
		if(txtNota.equals("")){
			txtNota = Variables.SIN_ESPECIFICAR;
		}
		if(this.cliente!=null){
			if(this.TIPO_VENTA==Variables.VENTA_DIRECTA){
				nueva_venta.setIdcliente(this.cliente.getIdcliente());
				nueva_venta.setTipo_venta(TIPO_VENTA);
				nueva_venta.setHora_venta(hora);
				nueva_venta.setCosto_total((Double)tvCostoTotal.getTag());
				nueva_venta.setNota(txtNota);
				registrarVenta();
			}else if (this.TIPO_VENTA==Variables.VENTA_A_DOMICILIO) {
				if(tvNombrePersonal.getTag()!=null){
					String direccion_entrega = etDireccionEntrega.getText().toString();
					if(!direccion_entrega.equals("")){
						this.personal = (Personal)tvNombrePersonal.getTag();
						nueva_venta.setTipo_venta(TIPO_VENTA);
						nueva_venta.setHora_venta(hora);
						nueva_venta.setIdcliente(this.cliente.getIdcliente());
						nueva_venta.setIdpersonal(this.personal.getIdpersonal());
						nueva_venta.setDireccion(direccion_entrega);
						nueva_venta.setCosto_total((Double)tvCostoTotal.getTag());
						nueva_venta.setNota(txtNota);
						registrarVenta();
					}else{
						etDireccionEntrega.requestFocus();
						etDireccionEntrega.setError("Introduzca dirección para la entrega");
					}
				}else{
					Toast.makeText(getActivity(), "Seleccione un personal para la entrega de venta", Toast.LENGTH_LONG).show();
				}
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
	
	public void registrarVenta(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.insertarVenta(nueva_venta)){
				boolean control = false;
				for (int i = 0; i < my_adapter.getCount(); i++) {
					if(db.insertarDetalleVenta(my_adapter.getItem(i))){
						control=true;
					}else{
						control = false;
					}
				}
				if(control){
					//Toast.makeText(getActivity(), "Venta registrado exitosamente", Toast.LENGTH_LONG).show();
					//limpiarCampos();
					mensajeVentaRegistrado();
				}else{
					Toast.makeText(getActivity(), "Venta no se registro correctamente", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getActivity(), "No se pudo registrar venta", Toast.LENGTH_LONG).show();
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mensajeVentaRegistrado(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Venta registrada correctamente"));
		dialog.setMessage(new StringBuilder("¿Que desea hacer con la boleta detalle de venta?"));
		
		dialog.setNegativeButton(R.string.ver_imprimir_info, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mostrarInfoDetalleVentaPdf();
			}
		});
		
		if(!this.cliente.getEmail().equals(Variables.SIN_ESPECIFICAR)){
			dialog.setNeutralButton(R.string.enviar_info, new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DialogEnviarInformeVentas dEnviarInfo = new DialogEnviarInformeVentas(nueva_venta, cliente, personal, lista_detalle_venta);
					dEnviarInfo.show(getFragmentManager(), "tagEnviarInfo");
					limpiarCampos();
				}
			});
		}
		
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
		PDFCustomManager pdfManager = new PDFCustomManager(getActivity(), new StringBuilder("info_").append(nueva_venta.getIdventa()).toString());
		if(pdfManager.existeInfoPDF()){
			pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
			limpiarCampos();
		}else{
			if(nueva_venta!=null && cliente!=null && lista_detalle_venta!=null){
				if(pdfManager.crearInfoDetalleVentaPDF(nueva_venta, cliente, personal, lista_detalle_venta)){
					pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
					limpiarCampos();
				}else{
					Toast.makeText(getActivity(), "Error al generar o abrir el detalle en PDF",  Toast.LENGTH_LONG).show();
				}				
			}else{
				Toast.makeText(getActivity(), "Ocurrio un error",  Toast.LENGTH_SHORT).show();
			}
			
		}
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

	private void cargarAdaptarSpinnerTipoVenta() {
		String[] tipos = {"Venta directa", "Venta a domicilio"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, tipos);
		spTipoVenta.setAdapter(adapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int position, long parent) {
		if((position+1)==Variables.VENTA_DIRECTA){
			lyVistaPersonalDeEntrega.setVisibility(View.GONE);
			this.TIPO_VENTA = Variables.VENTA_DIRECTA;
			etDireccionEntrega.getText().clear();
			tvNombrePersonal.setTag(null);
		}else if ((position+1)==Variables.VENTA_A_DOMICILIO) {
			lyVistaPersonalDeEntrega.setVisibility(View.VISIBLE);
			this.TIPO_VENTA = Variables.VENTA_A_DOMICILIO;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapter) {
	}

	@Override
	public void onResume() {
		super.onResume();
		tvFechaVenta.setText(Variables.FORMAT_FECHA_3.format(Variables.getFechaActual()));
		tvHoraVenta.setText(new StringBuilder("Hrs. ").append(Variables.getHoraActual().toString()));
		//Toast.makeText(getActivity(), "Continuando Resgistrar Venta", Toast.LENGTH_SHORT).show();
		if(my_adapter!=null){
			if(my_adapter.getCount()!=0){
				tvAviso.setVisibility(View.INVISIBLE);
				lvListaProd.setVisibility(View.VISIBLE);
				lvListaProd.setAdapter(my_adapter);
				if(nueva_venta.getCosto_total()!=0){
					lyVistaCostoTotal.setVisibility(View.VISIBLE);
					tvCostoTotal.setText(new StringBuilder().append(nueva_venta.getCosto_total()).append(" Bs."));
					tvCostoTotal.setTag(nueva_venta.getCosto_total());
				}
			}
		}
	}
	
	public String generarIdVenta(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String ventaCode = "Venta-"+date;
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
		lyVistaMaq.setVisibility(View.GONE);
		spTipoVenta.setSelection(0);
		lista_detalle_venta = null;
		nueva_venta = null;
		my_adapter = null;
		lvListaProd.setVisibility(View.INVISIBLE);
		tvAviso.setVisibility(View.VISIBLE);
		tvCostoTotal.setTag(null);
		lyVistaCostoTotal.setVisibility(View.GONE);
	}
	
}
