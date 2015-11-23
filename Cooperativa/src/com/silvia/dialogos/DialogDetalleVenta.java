package com.silvia.dialogos;

import java.util.List;

import com.silvia.adapters.ListaDetalleVentaAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.PDFCustomManager;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;
import com.silvia.modelo.Venta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetalleVenta extends DialogFragment{

	public LinearLayout lyVistaNota;
	public ImageView ivImageCliente, ivImagePersonal;
	public TextView tvNombreCliente, tvCICliente, tvTipoVenta, tvDireccion, tvNombrePersonal, tvCIPersonal, tvFechaVenta, tvHoraVenta, tvCostoTotal, tvNota;
	public LinearLayout lyPersonal;
	public ListView lvDetalleVenta;
	public View v;
	
	public Venta venta;
	public Cliente cliente;
	public Personal personal;
	public List<DetalleVenta> lista_detalle;
	
	public DialogDetalleVenta(Venta venta){
		this.venta = venta;
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
		dialog.setTitle(new StringBuilder("Detalle de venta"));
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_de_venta, null);
		ivImageCliente = (ImageView)v.findViewById(R.id.ivImageClienteDetalleVenta);
		tvNombreCliente = (TextView)v.findViewById(R.id.tvNombreClienteDetalleVenta);
		tvCICliente = (TextView)v.findViewById(R.id.tvCIClienteDetalleVenta);
		tvTipoVenta = (TextView)v.findViewById(R.id.tvTipoVentaDetalleVenta);
		lyPersonal = (LinearLayout)v.findViewById(R.id.lyPersonalDetalleVenta);
		tvDireccion = (TextView)v.findViewById(R.id.tvDireccionDetalleVenta);
		ivImagePersonal = (ImageView)v.findViewById(R.id.ivImagePersonalDetalleVenta);
		tvNombrePersonal = (TextView)v.findViewById(R.id.tvNombrePersonalDetalleVenta);
		tvCIPersonal = (TextView)v.findViewById(R.id.tvCIPersonalDetalleVenta);
		tvFechaVenta = (TextView)v.findViewById(R.id.tvFechaVentaDetalleVenta);
		tvHoraVenta = (TextView)v.findViewById(R.id.tvHoraVentaDetalleVenta);
		tvCostoTotal = (TextView)v.findViewById(R.id.tvCostoTotalDetalleVenta);
		lvDetalleVenta = (ListView)v.findViewById(R.id.lvListaDetalleVenta);
		lyVistaNota = (LinearLayout)v.findViewById(R.id.lyVistaNotaDetalleVenta);
		tvNota = (TextView)v.findViewById(R.id.tvNotaDetalleVenta);
		cliente = getCliente(venta.getIdcliente());
		cargarDatos(cliente);
		cargarListaDetalleVenta();
		dialog.setView(v);
		
		dialog.setNegativeButton(R.string.ver_imprimir_info, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mostrarInfoDetalleVentaPdf();
			}
		});
		
		dialog.setNeutralButton(R.string.enviar_info, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DialogEnviarInformeVentas dEnviarInfo = new DialogEnviarInformeVentas(venta, cliente, personal, lista_detalle);
				dEnviarInfo.show(getFragmentManager(), "tagEnviarInfo1");
			}
		});
				
		dialog.setPositiveButton(R.string.ok_info, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		return dialog.create();
	}
	
	public void mostrarInfoDetalleVentaPdf(){
		PDFCustomManager pdfManager = new PDFCustomManager(getActivity(), new StringBuilder("info_").append(venta.getIdventa()).toString());
		if(pdfManager.existeInfoPDF()){
			pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
		}else{
			if(venta!=null && cliente!=null && lista_detalle!=null){
				if(pdfManager.crearInfoDetalleVentaPDF(venta, cliente, personal, lista_detalle)){
					pdfManager.verEmprimirInfoPdf("Leendo detalle de venta en PDF");
				}else{
					Toast.makeText(getActivity(), "Error al generar o abrir el detalle en PDF",  Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getActivity(), "Ocurrio un error",  Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	private void cargarDatos(Cliente cli) {
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
		
		if(venta.getTipo_venta()==Variables.VENTA_DIRECTA){
			tvTipoVenta.setText(new StringBuilder("Tipo de venta: VENTA DIRECTA"));
		}else{
			tvTipoVenta.setText(new StringBuilder("Tipo de venta: VENTA A DOMICILIO"));
			lyPersonal.setVisibility(View.VISIBLE);
			tvDireccion.setText(new StringBuilder(venta.getDireccion()));
			personal = getPersonal(venta.getIdpersonal());
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
		}
		tvFechaVenta.setText(new StringBuilder().append(Variables.FORMAT_FECHA_1.format(venta.getFecha_venta())));
		tvHoraVenta.setText(new StringBuilder("Hrs. ").append(venta.getHora_venta()));
		tvCostoTotal.setText(new StringBuilder("Costo total: ").append(venta.getCosto_total()));
		if(!venta.getNota().equals(Variables.SIN_ESPECIFICAR)){
			tvNota.setText(venta.getNota());
		}
	}

	private void cargarListaDetalleVenta(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			lista_detalle = db.getDetalleVenta(venta.getIdventa());
			if(lista_detalle.size()!=0){
				ListaDetalleVentaAdapter adapter = new ListaDetalleVentaAdapter(getActivity(), lista_detalle,Variables.PROV_INFORME);
				lvDetalleVenta.setAdapter(adapter);
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
	
	public Usuario getUsuario(String idusuario){
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
	
}
