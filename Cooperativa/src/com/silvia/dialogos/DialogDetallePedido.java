package com.silvia.dialogos;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.silvia.adapters.ListaEditarDetallePedidoAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.ListaPedidos;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.DetallePedido;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Pedido;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;
import com.silvia.modelo.Venta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetallePedido extends DialogFragment implements android.view.View.OnClickListener{

	public ScrollView scrollViewParent;
	public ImageView ivImageCliente, ivImagePersonal, ivImageMaq;
	public TextView tvEstado, tvNombreCliente, tvCICliente, tvAtendidoPor, tvDireccion, tvNombrePersonal, tvCIPersonal, tvFechaPedido, 
						tvHoraPedido, tvFechaEntrega, tvCostoTotal, tvNota, tvPlacaMaq, tvCapacidadMaq;
	public ListView lvDetallePedido;
	public ImageButton ibtnConfirmarEntrega, ibtnEditarFechaEntrega, ibtnEditarDir, ibtnEditarPersonal,ibtnEditarNota;
	public Button btnVerPdf, btnAgregarProd;
	public EditText etEditarNota, etEditarDir;
	public View v;
	
	public Pedido pedido;
	public Cliente cliente;
	public Personal personal;
	public List<DetallePedido> lista_detalle;
	public OnDetallePedidoClickListener listener;
	public ListaPedidos lista_pedidos;
	public ListaEditarDetallePedidoAdapter my_adapter;
	
	public DialogDetallePedido(Pedido pedido, ListaPedidos lista_pedidos){
		this.pedido = pedido;
		this.lista_pedidos = lista_pedidos;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDivideId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDivideId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Detalle pedido"));
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_de_pedido, null);
		scrollViewParent = (ScrollView)v.findViewById(R.id.ScrollViewParentDetallePedido);
		scrollViewParent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				scrollViewParent.getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});
		tvEstado = (TextView)v.findViewById(R.id.tvEstadoDetallePedido);
		ibtnConfirmarEntrega = (ImageButton)v.findViewById(R.id.ibtnConfirmarEntregaDetallePedido);
		ibtnConfirmarEntrega.setOnClickListener(this);
		ivImageCliente = (ImageView)v.findViewById(R.id.ivImageClienteDetallePedido);
		tvNombreCliente = (TextView)v.findViewById(R.id.tvNombreClienteDetallePedido);
		tvCICliente = (TextView)v.findViewById(R.id.tvCIClienteDetallePedido);
		tvAtendidoPor = (TextView)v.findViewById(R.id.tvAtendidoPorDetallePedido);
		tvFechaPedido = (TextView)v.findViewById(R.id.tvFechaPedidoDetallePedido);
		tvHoraPedido = (TextView)v.findViewById(R.id.tvHoraVentaDetallePedido);
		tvFechaEntrega = (TextView)v.findViewById(R.id.tvFechaEntregaDetallePedido);
		ibtnEditarFechaEntrega = (ImageButton)v.findViewById(R.id.ibtnEditarFechaEntregaDetallePedido);
		ibtnEditarFechaEntrega.setOnClickListener(this);
		tvDireccion = (TextView)v.findViewById(R.id.tvDireccionDetallePedido);
		ibtnEditarDir = (ImageButton)v.findViewById(R.id.ibtnEditarDirEntregaDetallePedido);
		ibtnEditarDir.setOnClickListener(this);
		ibtnEditarPersonal = (ImageButton)v.findViewById(R.id.ibtnEditarPersonalEntregaDetallePedido);
		ibtnEditarPersonal.setOnClickListener(this);
		ivImagePersonal = (ImageView)v.findViewById(R.id.ivImagePersonalDetallePedido);
		tvNombrePersonal = (TextView)v.findViewById(R.id.tvNombrePersonalDetallePedido);
		tvCIPersonal = (TextView)v.findViewById(R.id.tvCIPersonalDetallePedido);
		ivImageMaq = (ImageView)v.findViewById(R.id.ivImageMaqDetallePedido);
		tvPlacaMaq = (TextView)v.findViewById(R.id.tvPlacaMaqDetallePedido);
		tvCapacidadMaq = (TextView)v.findViewById(R.id.tvCapacidadMaqDetallePedido);
		etEditarDir = (EditText)v.findViewById(R.id.etEditarDirEntregaDetallePedido);
		ibtnEditarNota = (ImageButton)v.findViewById(R.id.ibtnEditarNotaDetallePedido);
		ibtnEditarNota.setOnClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaDetallePedido);
		etEditarNota = (EditText)v.findViewById(R.id.etEditarNotaPedido);
		btnAgregarProd = (Button)v.findViewById(R.id.btnSeleccionarProdDetallePedido);
		btnAgregarProd.setOnClickListener(this);
		lvDetallePedido = (ListView)v.findViewById(R.id.lvListaDetallePedido);
		lvDetallePedido.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		btnVerPdf = (Button)v.findViewById(R.id.btnVerPdfDetallePedido);
		btnVerPdf.setOnClickListener(this);
		tvCostoTotal = (TextView)v.findViewById(R.id.tvCostoTotalDetallePedido);
		cliente = getCliente(pedido.getIdcliente());
		cargarDatos(cliente);
		cargarListaDetalleVenta();
		dialog.setView(v);
		
		if(pedido.getEstado()==Variables.PEDIDO_PENDIENTE){
			ibtnEditarFechaEntrega.setVisibility(View.VISIBLE);
			ibtnEditarDir.setVisibility(View.VISIBLE);
			ibtnEditarPersonal.setVisibility(View.VISIBLE);
			btnAgregarProd.setVisibility(View.VISIBLE);
			dialog.setNegativeButton(R.string.eliminar, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(pedido.getEstado()==Variables.PEDIDO_PENDIENTE){
						confirmarEliminarPedido(pedido);
					}else{
						Toast.makeText(getActivity(), "Imposible eliminar, pedido ya fue entregado", Toast.LENGTH_LONG).show();
					}
				}
			});
		}else{
			
		}
				
		dialog.setPositiveButton(R.string.ok_info, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(tvNombrePersonal.getTag()!=null){
					Personal per = (Personal)tvNombrePersonal.getTag();
					pedido.setIdpersonal(per.getIdpersonal());
					if(tvFechaEntrega.getTag()!=null){
						Date nueva_fecha_entrega = (Date)tvFechaEntrega.getTag();
						pedido.setFecha_entrega(nueva_fecha_entrega);
					}
					editarDatosPedido(pedido);
					Toast.makeText(getActivity(), "Personal de entrega modificado", Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
		
		return dialog.create();
	}
	
	public void mostrarInfoDetalleVentaPdf(){
//		PDFCustomManager pdfManager = new PDFCustomManager(getActivity(), new StringBuilder("info_").append(venta.getIdventa()).toString());
//		if(pdfManager.existeInfoPDF()){
//			pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
//		}else{
//			if(venta!=null && cliente!=null && lista_detalle!=null){
//				if(pdfManager.crearInfoDetalleVentaPDF(venta, cliente, personal, lista_detalle)){
//					pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
//				}else{
//					Toast.makeText(getActivity(), "Error al generar o abrir el detalle en PDF",  Toast.LENGTH_LONG).show();
//				}
//			}else{
//				Toast.makeText(getActivity(), "Ocurrio un error",  Toast.LENGTH_SHORT).show();
//			}
//			
//		}
	}
	
	private void cargarDatos(Cliente cli) {
		if(pedido.getEstado()==Variables.PEDIDO_PENDIENTE){
			tvEstado.setTextColor(getActivity().getResources().getColor(R.color.ROJO));
			tvEstado.setText("PENDIENTE");
		}else{
			tvEstado.setTextColor(getActivity().getResources().getColor(R.color.VERDE_LIME));
			tvEstado.setText("ENTREGADO");
			ibtnConfirmarEntrega.setVisibility(View.GONE);
		}
		if(!cli.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(cli.getImagen()).toString());
			if(bitmap!=null){
				ivImageCliente.setImageBitmap(bitmap);
				ivImageCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImageCliente.setPadding(0, 0, 0, 0);
			}else{
				ivImageCliente.setImageResource(R.drawable.ic_client_128);
			}
		}else{
			ivImageCliente.setImageResource(R.drawable.ic_client_128);
		}
		
		if(cli.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			tvNombreCliente.setText(new StringBuilder("Nombre: ").append(cli.getNombre()));
		}else{
			tvNombreCliente.setText(new StringBuilder("Nombre: ").append(cli.getNombre()).append(" ").append(cli.getApellido()));
		}
		tvCICliente.setText(new StringBuilder("CI/NIT: ").append(cli.getCi()));
		
		Usuario usuario = getUsuarioOnline(pedido.getIdusuario());
		if(usuario!=null){
			tvAtendidoPor.setText(new StringBuilder("ATENDIDO POR: ").append(usuario.getUsuario()));
		}
		
		tvDireccion.setText(new StringBuilder(pedido.getDireccion()));
		personal = getPersonal(pedido.getIdpersonal());
		if(!personal.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(personal.getImagen()).toString());
			if(bitmap!=null){
				ivImagePersonal.setImageBitmap(bitmap);
				ivImageCliente.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImageCliente.setPadding(0, 0, 0, 0);
			}else{
				ivImagePersonal.setImageResource(R.drawable.ic_employees_128);
			}
		}else{
			ivImagePersonal.setImageResource(R.drawable.ic_employees_128);
		}
		
		if(personal.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			tvNombrePersonal.setText(new StringBuilder("Nombre: ").append(personal.getNombre()));
		}else{
			tvNombrePersonal.setText(new StringBuilder("Nombre: ").append(personal.getNombre()).append(" ").append(personal.getApellido()));
		}
		tvCIPersonal.setText(new StringBuilder("CI: ").append(personal.getCi()));
		if(personal.getIdmaquinaria()!=Variables.ID_MAQ_DEFAULT){
			Maquinaria maq = getMaquinaria(personal.getIdmaquinaria());
			Bitmap bitmap = BitmapFactory.decodeFile(new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString());
			if(bitmap!=null){
				ivImageMaq.setImageBitmap(bitmap);
				ivImageMaq.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImageMaq.setPadding(0, 0, 0, 0);
			}else{
				ivImageMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
			tvPlacaMaq.setText(new StringBuilder("Placa: ").append(maq.getPlaca()));
			tvCapacidadMaq.setText(new StringBuilder("Cap.: " ).append(maq.getCapacidad()).append(" cubos"));
		}else{
			ivImageMaq.setVisibility(View.GONE);
			tvPlacaMaq.setVisibility(View.GONE);
			tvCapacidadMaq.setVisibility(View.GONE);
		}
		tvFechaPedido.setText(new StringBuilder().append(Variables.FORMAT_FECHA_1.format(pedido.getFecha_pedido())));
		tvHoraPedido.setText(new StringBuilder("Hrs. ").append(pedido.getHora_pedido()));
		tvFechaEntrega.setText(new StringBuilder(Variables.FORMAT_FECHA_1.format(pedido.getFecha_entrega())));
		tvCostoTotal.setText(new StringBuilder("Costo total: ").append(pedido.getCosto_total()));
		if(!pedido.getNota().equals(Variables.SIN_ESPECIFICAR)){
			tvNota.setText(pedido.getNota());
		}else{
			tvNota.setText("El pedido no tiene ninguna nota");
		}
	}

	private void cargarListaDetalleVenta(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista_detalle = db.getListaDetallePedido(pedido.getIdpedido());
			if(lista_detalle.size()!=0){
				my_adapter = new ListaEditarDetallePedidoAdapter(getActivity(), lista_detalle, this, this.pedido.getEstado());
				lvDetallePedido.setAdapter(my_adapter);
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cliente getCliente(String idcliente){
		Cliente cliente = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			cliente = db.getCliente(idcliente);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cliente;
	}
		
	public Personal getPersonal(String idpersonal){
		Personal personal = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			personal = db.getPersonal(idpersonal);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personal;
	}
		
	public Maquinaria getMaquinaria(String idmaquinaria){
		Maquinaria maq = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			maq = db.getMaquinaria(idmaquinaria);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maq;
	}
	
	public Usuario getUsuarioOnline(String idusuario){
		Usuario usuario = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			usuario = db.getUsuario(idusuario);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}
	
	public interface OnDetallePedidoClickListener{
		public void onEditarDetallePedidoClick(int accion);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (OnDetallePedidoClickListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+" implemente metodo de OnDetallePedidoClickListener");
		}
	}

	@Override
	public void onClick(View v) {
		if(ibtnConfirmarEntrega.getId()==v.getId()){
			confirmarEntregaDePedido();
		}else if(ibtnEditarNota.getId()==v.getId()){
			inicializarEdicionNota();
		}else if(ibtnEditarDir.getId()==v.getId()){
			inicializarEdicionDireccion();
		}else if(ibtnEditarPersonal.getId()==v.getId()){
			inicializarEdicionPersonalDeEntrega();
		}else if(btnAgregarProd.getId()==v.getId()){
			DialogAgregarProdDetallePedido dAProd = new DialogAgregarProdDetallePedido(this);
			dAProd.show(getFragmentManager(), "tagAProd");
		}else if (btnVerPdf.getId()==v.getId()) {
			mostrarInfoDetalleVentaPdf();
		}else if(ibtnEditarFechaEntrega.getId()==v.getId()){
			DialogFecha df = new DialogFecha(tvFechaEntrega);
			df.show(getFragmentManager(), "tagFE");
		}
	}
	
	private void inicializarEdicionPersonalDeEntrega() {
		DialogAsignarPersonalEntrega dAsignar = new DialogAsignarPersonalEntrega(ivImagePersonal, tvCIPersonal, tvNombrePersonal, 
																					new LinearLayout(getActivity()), ivImageMaq, tvPlacaMaq, tvCapacidadMaq);
		dAsignar.show(getFragmentManager(), "tagDAPE");
	}

	private void inicializarEdicionDireccion() {
		if(etEditarDir.getVisibility()==View.GONE){
			ibtnEditarDir.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_done_white_24dp));
			etEditarDir.setVisibility(View.VISIBLE);
			etEditarDir.setText(pedido.getDireccion());
			etEditarDir.requestFocus();
			Toast.makeText(getActivity(), "Editar direccion de entrega", Toast.LENGTH_SHORT).show();
		}else{
			String txtDir = etEditarDir.getText().toString();
			if(txtDir.length()<=5){
				etEditarDir.setError("Direccion debe tener minimo 5 caracteres");
				etEditarDir.requestFocus();
			}else if(!txtDir.equals(pedido.getDireccion())){
				pedido.setDireccion(txtDir);
				if(editarDatosPedido(pedido)){
					Toast.makeText(getActivity(), "Direccion editado", Toast.LENGTH_SHORT).show();
					tvDireccion.setText(txtDir);
				}
			}
			ibtnEditarDir.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_edit_black_24dp));
			etEditarDir.setVisibility(View.GONE);
		}
	}

	public void inicializarEdicionNota(){
		if(etEditarNota.getVisibility()==View.GONE){
			ibtnEditarNota.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_done_white_24dp));
			etEditarNota.setVisibility(View.VISIBLE);
			if(!pedido.getNota().equals(Variables.SIN_ESPECIFICAR)){
				etEditarNota.setText(pedido.getNota());
			}
			Toast.makeText(getActivity(), "Editar nota", Toast.LENGTH_SHORT).show();
		}else{
			String txtNota = etEditarNota.getText().toString();
			if(txtNota.equals("")){
				pedido.setNota(Variables.SIN_ESPECIFICAR);
				if(editarDatosPedido(pedido)){
					Toast.makeText(getActivity(), "Nota borrado", Toast.LENGTH_SHORT).show();
					tvNota.setText("El pedido no tiene ninguna nota");
				}
			}else if(!txtNota.equals(pedido.getNota())){
				pedido.setNota(txtNota);
				if(editarDatosPedido(pedido)){
					Toast.makeText(getActivity(), "Nota editado", Toast.LENGTH_SHORT).show();
					tvNota.setText(txtNota);
				}
			}
			ibtnEditarNota.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_edit_black_24dp));
			etEditarNota.setVisibility(View.GONE);
		}
	}
	
	public boolean editarDatosPedido(Pedido pedido){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.modificarDatosPedido(pedido)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void confirmarEliminarPedido(final Pedido pedido){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Eliminar?");
		dialog.setMessage("Recuerde que una vez eliminado el pedido no podra restauralo");
		dialog.setNegativeButton(R.string.cancelar, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		dialog.setPositiveButton(R.string.aceptar, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBDuraznillo db1 = new DBDuraznillo(lista_pedidos.getActivity());
				try{
					db1.abrirDB();
					if(db1.eliminarPedido(pedido)){
						Toast.makeText(lista_pedidos.getActivity(), "Pedido eliminado", Toast.LENGTH_SHORT).show();
					}
					db1.cerrarDB();
				}catch(Exception e){
					e.printStackTrace();
				}
				DialogDetallePedido.this.dismiss();
				lista_pedidos.my_adapter.remove(pedido);
				lista_pedidos.my_adapter.notifyDataSetChanged();
			}
		});
		Dialog d = dialog.show();
		
		Resources res = getActivity().getResources();
		int idTitleDivider = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(idTitleDivider);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public void confirmarEntregaDePedido(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Confirmar?");
		dialog.setMessage("Desea confirmar la entrega del pedido al señor(a) "+this.cliente.getNombre());
		dialog.setNegativeButton(R.string.cancelar, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		dialog.setPositiveButton(R.string.aceptar, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(tvNombrePersonal.getTag()!=null){
					Personal per = (Personal)tvNombrePersonal.getTag();
					pedido.setIdpersonal(per.getIdpersonal());
					if(tvFechaEntrega.getTag()!=null){
						Date nueva_fecha_entrega = (Date)tvFechaEntrega.getTag();
						pedido.setFecha_entrega(nueva_fecha_entrega);
					}
					if(editarDatosPedido(pedido)){
						tvNombrePersonal.setTag(null);
						tvFechaEntrega.setTag(null);
					}
				}
				if(registrarPedidoComoVenta()){
					Toast.makeText(getActivity(), "Pedido entregado y registrado en ventas", Toast.LENGTH_LONG).show();
					tvEstado.setText("ENTREGADO");
					tvEstado.setTextColor(getActivity().getResources().getColor(R.color.VERDE_LIME));
					ibtnEditarFechaEntrega.setVisibility(View.GONE);
					ibtnEditarDir.setVisibility(View.GONE);
					ibtnEditarPersonal.setVisibility(View.GONE);
					btnAgregarProd.setVisibility(View.GONE);
					ibtnConfirmarEntrega.setVisibility(View.GONE);
					lista_pedidos.my_adapter.notifyDataSetChanged();
				}
			}
		});
		Dialog d = dialog.show();
		
		Resources res = getActivity().getResources();
		int idTitleDivider = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(idTitleDivider);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public boolean registrarPedidoComoVenta(){
		List<DetallePedido> lista_dp = null;
		DBDuraznillo db = new DBDuraznillo(lista_pedidos.getActivity());
		try{
			db.abrirDB();
			lista_dp = db.getListaDetallePedido(pedido.getIdpedido());
			//Toast.makeText(getActivity(), "Pedido estado "+pedido.getEstado(), Toast.LENGTH_LONG).show();
			
			if(lista_dp.size()!=0 && lista_dp!=null){
				Venta venta = new Venta(generarIdVenta(), Variables.VENTA_A_DOMICILIO, pedido.getIdusuario(), pedido.getIdcliente(), 
						Variables.getFechaActual(), Variables.getHoraActual(), pedido.getIdpersonal(), pedido.getDireccion(), 
						pedido.getCosto_total(), pedido.getNota());
				if(db.insertarVenta(venta)){
					DetalleVenta dv = null;
					for (int i = 0; i < lista_dp.size(); i++) {
						String id = generarIdDetalleVenta()+""+i;
						DetallePedido dp = lista_dp.get(i);
						dv = new DetalleVenta(id, venta.getIdventa(), dp.getIdproducto(), 
																	dp.getCantidad(), dp.getCosto_entrega(), dp.getCosto());
						db.insertarDetalleVenta(dv);
						//Toast.makeText(getActivity(), "IDDetalleVenta "+dv.getIddetalle()+" -- IDVenta "+dv.getIdventa(), Toast.LENGTH_LONG).show();
					}
					this.pedido.setEstado(Variables.PEDIDO_ENTREGADO);
					//Toast.makeText(getActivity(), "Estado "+pedido.getEstado(), Toast.LENGTH_LONG).show();
					if(db.modificarDatosPedido(pedido)){
						return true;
					}
				}
			}
			db.cerrarDB();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public String generarIdVenta(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String ventaCode = "Venta-"+date;
		return ventaCode;
	}
	
	public String generarIdDetalleVenta(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String detalleCode = "detalle_v-"+date;
		return detalleCode;
	}
	
}
