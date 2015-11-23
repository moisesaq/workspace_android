package com.silvia.dialogos;

import java.util.List;

import com.silvia.cooperativa.PDFCustomManager;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.InformeVentas;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Venta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class DialogEnviarInformeVentas extends DialogFragment implements android.view.View.OnClickListener{

	private InformeVentas informe_ventas;
	private Venta venta;
	private Cliente cliente;
	private Personal personal;
	private List<DetalleVenta> lista_detalle;
	public View v;
	public EditText etEmail;
	public TextView tvAviso;
	public ImageButton ibtnAgregarEmail;
	public Button btnCancelar, btnEnviar;
	
	public DialogEnviarInformeVentas(InformeVentas informe_ventas){
		this.informe_ventas = informe_ventas;
	}
	
	public DialogEnviarInformeVentas(Venta venta, Cliente cliente, Personal personal, List<DetalleVenta> lista_detalle){
		this.venta = venta;
		this.cliente = cliente;
		this.personal = personal;
		this.lista_detalle = lista_detalle;
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
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		if(informe_ventas!=null){
			dialog.setTitle(new StringBuilder("Enviar Informe Venta PDF"));
		}else{
			dialog.setTitle(new StringBuilder("Enviar Detalle Venta PDF"));
		}
		v = getActivity().getLayoutInflater().inflate(R.layout.modelo_enviar_informe_ventas, null);
		etEmail = (EditText)v.findViewById(R.id.etEmailEnviarInformeVentas);
		tvAviso = (TextView)v.findViewById(R.id.tvAvisoEnviarInformeVentas);
		ibtnAgregarEmail = (ImageButton)v.findViewById(R.id.ibtnAgregarEmailEnviarInformeVentas);
		ibtnAgregarEmail.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarEnviarInformeVentas);
		btnCancelar.setOnClickListener(this);
		btnEnviar = (Button)v.findViewById(R.id.btnAceptarEnviarInformeVentas);
		btnEnviar.setOnClickListener(this);
		
		if(cliente!=null){ //Aca se pregunta si es nulo para saber si vamos enviar un detalle venta o un informe de venta
			if(!cliente.getEmail().equals(Variables.SIN_ESPECIFICAR)){
				tvAviso.setText(new StringBuilder("Enviar informe a "+cliente.getNombre()+", si desea enviar a otro email introduzca o seleccione porfavor"));
				tvAviso.setTag(cliente.getNombre());
				etEmail.setText(cliente.getEmail());
			}else{
				tvAviso.setText(new StringBuilder("Email del cliente esta sin especificar, introduzca o seleccione un email porfavor"));
			}
		}else{
			tvAviso.setText(new StringBuilder("Seleccione un personal para enviar informe o introduzca un email por favor"));
		}
		dialog.setView(v);
		return dialog.create();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnEnviar.getId()){
			if(informe_ventas!=null){
				enviarInformeVenta();
			}else{
				enviarDetalleVenta();
			}
		}else if(v.getId()==btnCancelar.getId()){
			this.dismiss();
		}else if(v.getId()==ibtnAgregarEmail.getId()){
			DialogSeleccionarPersonalParaEnviarInforme dSeleccionar = new DialogSeleccionarPersonalParaEnviarInforme(tvAviso, etEmail);
			dSeleccionar.show(getFragmentManager(), "tagSelect");
		}
	}
	
	public void enviarInformeVenta(){
		String nombre_per = "";
		String email_to = etEmail.getText().toString();
		if(!email_to.equals("")){
			Toast.makeText(getActivity(), "Preparando para enviar...", Toast.LENGTH_SHORT).show();
			String name_informe = Variables.getNuevoCodeUnico("informe_ventas_");
			PDFCustomManager pdfCM = new PDFCustomManager(getActivity(), name_informe);
			if(pdfCM.crearInformeVentasPDF(informe_ventas)){
				if(tvAviso.getTag()!=null){
					nombre_per = (String)tvAviso.getTag();
				}else{
					nombre_per = "Señor(a)";
				}
				pdfCM.enviarInformePdf(nombre_per, email_to, "En este documento pdf esta el informe de ventas");
			}else{
				Toast.makeText(getActivity(), "Error al generar o enviar el informe en pdf", Toast.LENGTH_LONG).show();
			}
			this.dismiss();
		}else{
			etEmail.requestFocus();
			etEmail.setError("Introduzca un Email");
		}	
	}
	
	public void enviarDetalleVenta(){
		String email_to = etEmail.getText().toString();
		String nombre_per = "";
		if(!email_to.equals("")){
			Toast.makeText(getActivity(), "Preparando para enviar...", Toast.LENGTH_SHORT).show();
			if(tvAviso.getTag()!=null){
				nombre_per = (String)tvAviso.getTag();
			}else{
				nombre_per = "Señor(a)";
			}
			PDFCustomManager pdfManager = new PDFCustomManager(getActivity(), new StringBuilder("info_").append(venta.getIdventa()).toString());		
			if(pdfManager.existeInfoPDF()){
				pdfManager.enviarInformePdf(nombre_per, email_to, "En este documento pdf esta el detalle su compra");
			}else{
				if(venta!=null && cliente!=null && lista_detalle!=null){
					if(pdfManager.crearInfoDetalleVentaPDF(venta, cliente, personal, lista_detalle)){
						pdfManager.enviarInformePdf(nombre_per, email_to, "En este documento pdf esta el detalle su compra");
					}else{
						Toast.makeText(getActivity(), "Error al generar o abrir el detalle en PDF",  Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getActivity(), "Ocurrio un error",  Toast.LENGTH_SHORT).show();
				}
			}
			this.dismiss();
		}else{
			etEmail.requestFocus();
			etEmail.setError("Introduzca un Email");
		}
		
	}
	
}
