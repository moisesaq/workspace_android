package com.moisse.generarpdfenviar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentMyMenu extends Fragment implements OnClickListener{
	
	public static final String FOLDER_PRUEBAS_PDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/PruebasPDF/";
	public EditText etEmail, etNombrePdf;
	public Button btnValidarEmail, btnCrearPdf, btnVerPdf, btnEnviarPdf;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.my_menu, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		etEmail = (EditText)v.findViewById(R.id.etEmail);
		btnValidarEmail = (Button)v.findViewById(R.id.btnValidarEmail);
		btnValidarEmail.setOnClickListener(this);
		etNombrePdf = (EditText)v.findViewById(R.id.etNombrePdf);
		btnCrearPdf = (Button)v.findViewById(R.id.btnCrearPdf);
		btnCrearPdf.setOnClickListener(this);
		btnVerPdf = (Button)v.findViewById(R.id.btnVerPdf);
		btnVerPdf.setOnClickListener(this);
		btnEnviarPdf = (Button)v.findViewById(R.id.btnEnviarPdf);
		btnEnviarPdf.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnValidarEmail.getId()){
			String txtEmail = etEmail.getText().toString().trim();
			if(!txtEmail.equals("")){
				Toast.makeText(getActivity(), "Validando E-mail "+txtEmail, Toast.LENGTH_SHORT).show();
			}else{
				etEmail.requestFocus();
				etEmail.setError(new StringBuilder("Introduzca E-mail"));
			}
		}else if(v.getId()==btnCrearPdf.getId()){
			String txtPdf = etNombrePdf.getText().toString().trim();
			if(!txtPdf.equals("")){
				String nombre_pdf = new StringBuilder(txtPdf).append(".pdf").toString();
				crearMiPDF(nombre_pdf);
			}else{
				etNombrePdf.requestFocus();
				etNombrePdf.setError(new StringBuilder("Introduzca nombre del pdf"));
			}
		}else if(v.getId()==btnVerPdf.getId()){
			String txtPdf = etNombrePdf.getText().toString().trim();
			if(!txtPdf.equals("")){
				String nombre_pdf = new StringBuilder(txtPdf).append(".pdf").toString();
				mostrarArchivoPDF(nombre_pdf);
			}else{
				etNombrePdf.requestFocus();
				etNombrePdf.setError(new StringBuilder("Introduzca nombre del pdf"));
			}
		}else if(v.getId()==btnEnviarPdf.getId()){
			String txtPdf = etNombrePdf.getText().toString().trim();
			if(!txtPdf.equals("")){
				String nombre_pdf = new StringBuilder(txtPdf).append(".pdf").toString();
				String emailTo = "moisesapaza07@gmail.com";
				String emailCC = "epicoapaza@gmail.com";
				enviarArchivoPDFPorEmail(nombre_pdf, emailTo, emailCC);
			}else{
				etNombrePdf.requestFocus();
				etNombrePdf.setError(new StringBuilder("Introduzca nombre del pdf"));
			}
		}
	}
	
	public void crearMiPDF(String nombre_pdf){
		Document documento = new Document();
		File file = crearFichero(nombre_pdf);
		try {
			FileOutputStream ficheroPdf = new FileOutputStream(file.getAbsolutePath());
			PdfWriter.getInstance(documento, ficheroPdf);
			
			documento.open();
			Paragraph paragraph = new Paragraph("Moisse probando pdf, nombre de este archivo: "+nombre_pdf, CustomFont.myCustomFont);
			addLineaBasia(paragraph, 1);
			paragraph.setAlignment(Chunk.ALIGN_CENTER);
			documento.add(paragraph);
			InvoiceObject invoiceObject = crearObjetoFactura();
			addInvoiceDate(documento, invoiceObject);
			addImage(documento);
			addContenidoFactura(documento, invoiceObject.invoiceDetailsList);
			addInvoiceTotal(documento, invoiceObject);
			//addTable(documento);
			documento.close();
			Toast.makeText(getActivity(), "PDF "+nombre_pdf+" se creo exitosamente", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e){
			e.printStackTrace();
		}
	}
	
	public InvoiceObject crearObjetoFactura(){
		InvoiceObject invoiceObject = new InvoiceObject();
		invoiceObject.id_factura = 12345;
		invoiceObject.fecha_venta = "Fecha de venta: Miercoles, 03 de septiembre 2015";
		invoiceObject.nombre_cooperativa = "COOPERATIVA DURAZNILLO";
		invoiceObject.telf_coop = "Telf.: 64-29432";
		invoiceObject.direccion_coop = "Direccion: Comunidad de DURAZNILLO";
		invoiceObject.ciudad_coop = "Ciudad de SUCRE";
		invoiceObject.nombre_cliente = "Nombre: Silvia Vargas Vera";
		invoiceObject.ci_cliente = "CI: 7555302";
		invoiceObject.telf_cliente = "Telf. o Cel.: 71175807";
		invoiceObject.direccion_cliente = "Direccion: Buenos Aires #42";
		invoiceObject.costo_total = 1100.0;
		
		InvoiceDetails invoiceDetails1 = new InvoiceDetails();
		invoiceDetails1.codigo_prod = "PROD-1000";
		invoiceDetails1.nombre_prod = "ARENA FINA";
		invoiceDetails1.cantidad = 4;
		invoiceDetails1.precio = 150.0;
		invoiceDetails1.total = 600.0;
		
		InvoiceDetails invoiceDetails2 = new InvoiceDetails();
		invoiceDetails2.codigo_prod = "PROD-1100";
		invoiceDetails2.nombre_prod = "PIEDRA BLANCA";
		invoiceDetails2.cantidad = 5;
		invoiceDetails2.precio = 100.0;
		invoiceDetails2.total = 500.0;
		
		invoiceObject.invoiceDetailsList = new ArrayList<InvoiceDetails>();
		invoiceObject.invoiceDetailsList.add(invoiceDetails1);
		invoiceObject.invoiceDetailsList.add(invoiceDetails2);
		
		return invoiceObject;
	}

	public void addInvoiceDate(Document documento, InvoiceObject invoiceObject){
		Paragraph parrafo = new Paragraph();
		
		addLineaBasia(parrafo, 1);
		parrafo.add(new Paragraph("No-"+invoiceObject.id_factura, CustomFont.boldFont));
		parrafo.add(new Paragraph(invoiceObject.fecha_venta, CustomFont.italicFont));
		parrafo.add(new Paragraph(invoiceObject.nombre_cooperativa, CustomFont.normalFont));
		parrafo.add(new Paragraph(invoiceObject.telf_coop, CustomFont.normalFont));
		parrafo.add(new Paragraph(invoiceObject.direccion_coop, CustomFont.normalFont));
		parrafo.add(new Paragraph(invoiceObject.ciudad_coop, CustomFont.normalFont));
		
		addLineaBasia(parrafo, 1);
		parrafo.add(new Paragraph("Datos del cliente", CustomFont.boldFont));
		parrafo.add(new Paragraph(invoiceObject.nombre_cliente, CustomFont.normalFont));
		parrafo.add(new Paragraph(invoiceObject.ci_cliente, CustomFont.normalFont));
		parrafo.add(new Paragraph(invoiceObject.telf_cliente, CustomFont.normalFont));
		parrafo.add(new Paragraph(invoiceObject.direccion_cliente, CustomFont.normalFont));
		
		addLineaBasia(parrafo, 1);
		try {
			documento.add(parrafo);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public void addContenidoFactura(Document documento, List<InvoiceDetails> lista_invoiceDetails) throws DocumentException{
		Paragraph paragraph = new Paragraph();
		addLineaBasia(paragraph, 1);
		crearTablaFactura(paragraph, lista_invoiceDetails);
		documento.add(paragraph);
	}
	
	public void crearTablaFactura(Paragraph seccion_tabla, List<InvoiceDetails> lista_invoiceDetails) throws DocumentException{
		int columnas = 5;
		PdfPTable tabla = new PdfPTable(columnas);
		float[] tamanio_columnas = new float[]{80f, 170f, 80f, 80f, 100f};
		tabla.setWidths(tamanio_columnas);
		//ancho de tabla
		tabla.setWidthPercentage(100);
		//Definimos titulos para cada columna
		PdfPCell celda = new PdfPCell(new Phrase("Codigo", CustomFont.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla.addCell(celda);
		
		celda = new PdfPCell(new Phrase(getActivity().getResources().getString(R.string.producto), CustomFont.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Cantidad", CustomFont.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Precio", CustomFont.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Total", CustomFont.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla.addCell(celda);
		//Cabeceras de la tabla
		tabla.setHeaderRows(1);
		//Vamos aniadiendo las siguientes lineas a la tabla
		for (InvoiceDetails linea_factura: lista_invoiceDetails) {
			crearLineaDeTablaFactura(linea_factura, tabla);
		}
		seccion_tabla.add(tabla);
	}
	
	public void crearLineaDeTablaFactura(InvoiceDetails invoiceDetails, PdfPTable tabla){
		PdfPCell celda = new PdfPCell();
		
		tabla.addCell(invoiceDetails.codigo_prod);
		tabla.addCell(invoiceDetails.nombre_prod);
		
		celda.setPhrase(new Phrase(String.valueOf(invoiceDetails.cantidad), CustomFont.italicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(invoiceDetails.precio),CustomFont.italicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(invoiceDetails.total),CustomFont.italicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla.addCell(celda);
	}

	public void addInvoiceTotal(Document documento, InvoiceObject invoiceObject){
		Paragraph paragraph = new Paragraph("Costo total: "+invoiceObject.costo_total+" Bs.", CustomFont.boldFont);
		addLineaBasia(paragraph, 1);
		paragraph.setAlignment(Chunk.ALIGN_RIGHT);
		try {
			documento.add(paragraph);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addLineaBasia(Paragraph paragraph, int numero_lineas){
		for (int i = 0; i < numero_lineas; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	public void addImage(Document documento){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hello_android);
		Bitmap newBitmap = redimencionarImageMaximo(bitmap, 100, 100);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapData = stream.toByteArray();

		try {
			Image image = Image.getInstance(bitmapData);
			image.setAbsolutePosition(400f, 650f);
			//image.setAlignment(Element.ALIGN_RIGHT|Element.ALIGN_TOP);
			Paragraph paragraph = new Paragraph();
			paragraph.add(image);
			paragraph.setAlignment(Chunk.ALIGN_RIGHT|Chunk.ALIGN_TOP);
			documento.add(paragraph);
			
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(DocumentException e){
			e.printStackTrace();
		}
	}
	
	public Bitmap redimencionarImageMaximo(Bitmap bitmap, float newWidth, float newHeight){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		float scaleWidth = ((float)newWidth)/width;
		float scaleHeigth = ((float)newHeight)/height;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeigth);
		
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
	}
	
	//Tabla fue creada para una prueba rapida
	public void addTable(Document documento){
		PdfPTable tabla = new PdfPTable(3);
		for (int i = 0; i < 15; i++) {
			tabla.addCell("celda "+(i+1));
		}
		try {
			documento.add(tabla);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarArchivoPDF(String nombre_file_pdf){
		Toast.makeText(getActivity(), "Leendo documento "+nombre_file_pdf, Toast.LENGTH_LONG).show();
		String pathFilePdf = new StringBuilder(FOLDER_PRUEBAS_PDF).append(nombre_file_pdf).toString();
		File file = new File(pathFilePdf);
		if(file.exists()){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Dispositivo no dispone con aplicacion para ver archivos PDF", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getActivity(), "Archivo pdf no encontrado", Toast.LENGTH_LONG).show();
		}
	}
	
	public void enviarArchivoPDFPorEmail(String nombre_file_pdf, String emailTo, String emailCC){
		String pathFilePdf = new StringBuilder(FOLDER_PRUEBAS_PDF).append(nombre_file_pdf).toString();
		File file = new File(pathFilePdf);
		if(file.exists()){
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "hola pdf android");
			emailIntent.putExtra(Intent.EXTRA_TEXT, "Moisse trabajando con pdf en android");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
			emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{emailCC});
			Uri uri = Uri.fromFile(file);
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			emailIntent.setType("application/pdf");
			//emailIntent.setType("mesagge/rfc822"); //ESTO CREO Q ES PARA ENVIAR SOLO UN MENSAJE DE TEXTO
			try {
				startActivity(Intent.createChooser(emailIntent, "Inviar email usando"));
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), "Dispositivo no dispone con aplicacion para envio de archivos PDF", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getActivity(), "Archivo pdf no encontrado", Toast.LENGTH_LONG).show();
		}
	}
	
	public File crearFichero(String nombre_fichero){
		File ruta = getRuta();
		File fichero_pdf = null;
		if(ruta!=null){
			fichero_pdf = new File(ruta, nombre_fichero);
		}
		return fichero_pdf;
	}
	
	public File getRuta(){
		File ruta_pdf = new File(FOLDER_PRUEBAS_PDF);
		ruta_pdf.mkdirs();
		return ruta_pdf;
	}

}
