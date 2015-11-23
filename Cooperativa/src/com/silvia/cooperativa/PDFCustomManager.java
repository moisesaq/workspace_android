package com.silvia.cooperativa;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.fragmentos.InformeVentas;
import com.silvia.modelo.Cliente;
import com.silvia.modelo.Cooperativa;
import com.silvia.modelo.DetalleVenta;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Producto;
import com.silvia.modelo.Usuario;
import com.silvia.modelo.Venta;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.widget.Toast;

public class PDFCustomManager {

	public Activity activity;
	public String nombre_pdf;
	
	public PDFCustomManager(Activity activity, String nombre_pdf){
		this.activity = activity;
		this.nombre_pdf = new StringBuilder(nombre_pdf).append(".pdf").toString();
	}
	
	public boolean existeInfoPDF(){
		String pathFilePdf = new StringBuilder(Variables.FOLDER_INFO_VENTAS_PDF).append(this.nombre_pdf).toString();
		File file = new File(pathFilePdf);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean crearInfoDetalleVentaPDF(Venta venta, Cliente cliente, Personal personal, List<DetalleVenta> lista_detalle){
		Document documentPdf = new Document();
		File filePdf = crearFichero();
		try {
			FileOutputStream ficheroPdf = new FileOutputStream(filePdf.getAbsoluteFile());
			PdfWriter.getInstance(documentPdf, ficheroPdf);
			documentPdf.open();
			agregarTituloDatosInforme(documentPdf, "DETALLE DE VENTA");
			agregarDatosVenta(documentPdf, venta, cliente, personal);
			crearTablaDetalleVenta(documentPdf, lista_detalle);
			agregarCostoTotalDetalleVenta(documentPdf, venta);
			documentPdf.close();
			if(filePdf.exists()){
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean crearInformeVentasPDF(InformeVentas informe_ventas){
		Document pdf_documento = new Document();
		File pdf_file = crearFichero();
		try {
			FileOutputStream pdf_fichero = new FileOutputStream(pdf_file);
			PdfWriter.getInstance(pdf_documento, pdf_fichero);
			pdf_documento.open();
			agregarTituloDatosInforme(pdf_documento, informe_ventas.tvTitulo.getText().toString());
			crearTablaInformeVenta(pdf_documento, informe_ventas.lista_ventas);
			agregarIngresoTotalAInformeVentas(pdf_documento, informe_ventas);
			pdf_documento.close();
			if(pdf_file.exists()){
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e){
			e.printStackTrace();
		}
		return false;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	
	public void agregarTituloDatosInforme(Document documentPdf, String titulo){
		Paragraph parrafo = new Paragraph(titulo, FontCustom.normalBoldFont);
		agregarLineaVacia(parrafo, 1);
		parrafo.setAlignment(Chunk.ALIGN_CENTER);
		try {
			documentPdf.add(parrafo);
			agregarLogoCooperativa(documentPdf);
			agregarDatosCooperativa(documentPdf);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public void agregarDatosCooperativa(Document documentPdf){
		Cooperativa coop = getCooperativa();
		Paragraph parrafo = new Paragraph();
		agregarLineaVacia(parrafo, 1);
		
		Paragraph paragraph = new Paragraph(coop.getNombre()+"  NIT: "+coop.getNit(), FontCustom.boldItalicFont);
		paragraph.setAlignment(Chunk.ALIGN_CENTER);
		try {
			documentPdf.add(paragraph);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		
		if(!coop.getTelf_cel().equals(Variables.SIN_ESPECIFICAR)){
			parrafo.add(new Paragraph("TELEFONO - CELULAR: "+coop.getTelf_cel(), FontCustom.normalFont));
		}
		if(!coop.getEmail().equals(Variables.SIN_ESPECIFICAR)){
			parrafo.add(new Paragraph("E-MAIL: "+coop.getEmail(), FontCustom.normalFont));
		}
		if(!coop.getDireccion().equals(Variables.SIN_ESPECIFICAR)){
			parrafo.add(new Paragraph("DIRECCION: Comunidad de Duraznillo", FontCustom.normalFont));
		}
		if(!coop.getCiudad().equals(Variables.SIN_ESPECIFICAR)){
			parrafo.add(new Paragraph("CIUDAD DE "+coop.getCiudad(), FontCustom.normalFont));
		}
		
		try {
			documentPdf.add(parrafo);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public Cooperativa getCooperativa(){
		Cooperativa coop = null;
		DBDuraznillo db = new DBDuraznillo(activity);
		try {
			db.abrirDB();
			coop = db.getCooperativa(Variables.ID_COOP);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coop;
	}
	
	public void agregarLogoCooperativa(Document documentPdf){
		Bitmap bitmapRedimencionar = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_icon_coop_256);
		Bitmap bitmap = redimencionarImagen(bitmapRedimencionar, 90, 90);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapData = stream.toByteArray();
		try {
			Image image = Image.getInstance(bitmapData);
			//image.scaleToFit(100, 100); //Creo q es para escalar a un tamanio una imagen
			image.setAbsolutePosition(450f, 640f);
			Paragraph paragraph = new Paragraph();
			paragraph.add(image);
			paragraph.setAlignment(Chunk.ALIGN_RIGHT|Chunk.ALIGN_TOP);
			documentPdf.add(paragraph);
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e){
			e.printStackTrace();
		}
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	
	public void agregarDatosVenta(Document documentPdf, Venta venta, Cliente cliente, Personal personal){
		Paragraph parrafo = new Paragraph();
		agregarLineaVacia(parrafo, 1);
		parrafo.add(new Paragraph("DATOS DE LA VENTA", FontCustom.boldItalicFont));
		parrafo.add(new Paragraph("CODIGO: "+venta.getIdventa(), FontCustom.normalFont));
		parrafo.add(new Paragraph("FECHA: "+Variables.FORMAT_FECHA_1.format(venta.getFecha_venta())+" HORA: "+venta.getHora_venta().toString()
									, FontCustom.italicFont));
		Usuario user = getUsuario(venta.getIdusuario());
		parrafo.add(new Paragraph("ATENDIDO POR: "+user.getUsuario(), FontCustom.normalFont));
		if(venta.getTipo_venta()==Variables.VENTA_DIRECTA){
			parrafo.add(new Paragraph("TIPO: VENTA DIRECTA", FontCustom.normalFont));
		}else if(venta.getTipo_venta()==Variables.VENTA_A_DOMICILIO){
			parrafo.add(new Paragraph("TIPO: VENTA A DOMICILIO", FontCustom.normalFont));
			if(personal!=null){
				if(personal.getApellido().equals(Variables.SIN_ESPECIFICAR)){
					parrafo.add(new Paragraph("PERSONAL ENCARGADO DE LA ENTREGA: "+personal.getNombre()+" CI: "+personal.getCi(), FontCustom.normalFont));
				}else{
					parrafo.add(new Paragraph("PERSONAL ENCARGADO DE LA ENTREGA: "+personal.getNombre()+" "+personal.getApellido()+
							" CI: "+personal.getCi(), FontCustom.normalFont));
				}
			}
			parrafo.add(new Paragraph("DIRECCION DE ENTREGA: "+venta.getDireccion(), FontCustom.normalFont));
		}
		agregarLineaVacia(parrafo, 1);
		parrafo.add(new Paragraph("DATOS DEL CLIENTE", FontCustom.boldItalicFont));
		if(cliente.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			parrafo.add(new Paragraph("NOMBRE: "+cliente.getNombre()+"   CI/NIT: "+cliente.getCi(), FontCustom.normalFont));
		}else{
			parrafo.add(new Paragraph("NOMBRE: "+cliente.getNombre()+" "+cliente.getApellido()+"   CI/NIT: "+cliente.getCi(), FontCustom.normalFont));
		}
		if(cliente.getTelf()!=0){
			parrafo.add(new Paragraph("TELF./CEL.: "+cliente.getTelf(), FontCustom.normalFont));
		}else{
			parrafo.add(new Paragraph("TELF./CEL.: "+Variables.SIN_ESPECIFICAR, FontCustom.normalFont));
		}
		parrafo.add(new Paragraph("DIRECCION: "+cliente.getDireccion(), FontCustom.normalFont));
		
		try {
			documentPdf.add(parrafo);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	
	public void crearTablaDetalleVenta(Document documentPdf, List<DetalleVenta> lista_detalle){
		Paragraph seccion_tabla = new Paragraph();
		agregarLineaVacia(seccion_tabla, 1);
		int columnas = 6;
		PdfPTable tabla_detalle = new PdfPTable(columnas);
		float[] tamanio_columnas = new float[]{40f, 180f, 70f, 80f, 80f, 80f};
		try {
			tabla_detalle.setWidths(tamanio_columnas);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		tabla_detalle.setWidthPercentage(100);
		
		PdfPCell celda = new PdfPCell(new Phrase("No.", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Producto", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Precio", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Cubos", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		celda = new PdfPCell(new Phrase("C. Entrega", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Costo", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		tabla_detalle.setHeaderRows(1);
		
		int cont = 1;
		for(DetalleVenta linea_detalleV: lista_detalle){
			agregarLineaATablaDetalle(cont, linea_detalleV, tabla_detalle);
			cont++;
		}
		
		seccion_tabla.add(tabla_detalle);
		try {
			documentPdf.add(seccion_tabla);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public void agregarLineaATablaDetalle(int num, DetalleVenta detalleV, PdfPTable tabla_detalle){
		PdfPCell celda = new PdfPCell();
		Producto prod = getProducto(detalleV.getIdproducto());
		
		celda.setPhrase(new Phrase(String.valueOf(num), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_detalle.addCell(celda);
		
		celda.setPhrase(new Phrase(prod.getNombre_prod(), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_LEFT);
		tabla_detalle.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(prod.getPrecio()), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_detalle.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(detalleV.getCantidad()), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_detalle.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(detalleV.getCosto_entrega()), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_detalle.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(detalleV.getCosto()), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_detalle.addCell(celda);
	}
	
	public void agregarCostoTotalDetalleVenta(Document documentPdf, Venta venta){
		Paragraph paragraph = new Paragraph("Costo total: "+venta.getCosto_total(), FontCustom.normalFont);
		agregarLineaVacia(paragraph, 1);
		paragraph.setAlignment(Chunk.ALIGN_RIGHT);
		try {
			documentPdf.add(paragraph);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	
	public void crearTablaInformeVenta(Document pdf_documento, List<Venta> lista_ventas){
		Paragraph seccion_tabla = new Paragraph();
		agregarLineaVacia(seccion_tabla, 1);
		int columnas = 6;
		PdfPTable tabla_ventas = new PdfPTable(columnas);
		float[] tamanio_columnas = new float[]{40f, 180f, 70f, 80f, 80f, 80f};
		try {
			tabla_ventas.setWidths(tamanio_columnas);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		tabla_ventas.setWidthPercentage(100);
		
		PdfPCell celda = new PdfPCell(new Phrase("No.",FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Cliente", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda = new PdfPCell(new Phrase("CI/NIT", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Tipo de Venta", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Fecha", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda = new PdfPCell(new Phrase("Total", FontCustom.boldFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		tabla_ventas.setHeaderRows(1);
		
		int cont = 1;
		for(Venta venta: lista_ventas){
			agregarLineaAInformeVentas(cont, venta, tabla_ventas);
			cont++;
		}
		
		seccion_tabla.add(tabla_ventas);
		try {
			pdf_documento.add(seccion_tabla);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public void agregarLineaAInformeVentas(int num, Venta venta, PdfPTable tabla_ventas){
		PdfPCell celda = new PdfPCell();
		Cliente cliente = getCliente(venta.getIdcliente());
		
		celda.setPhrase(new Phrase(String.valueOf(num), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		if(cliente.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			celda.setPhrase(new Phrase(cliente.getNombre(), FontCustom.normalFont));
		}else{
			celda.setPhrase(new Phrase(cliente.getNombre()+" "+cliente.getApellido(), FontCustom.normalFont));
		}
		celda.setHorizontalAlignment(Element.ALIGN_LEFT);
		tabla_ventas.addCell(celda);
		
		celda.setPhrase(new Phrase(cliente.getCi(), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		if(venta.getTipo_venta()==Variables.VENTA_DIRECTA){
			celda.setPhrase(new Phrase("Venta directa", FontCustom.normalFont));
		}else if(venta.getTipo_venta()==Variables.VENTA_A_DOMICILIO){
			celda.setPhrase(new Phrase("Venta a domicilio", FontCustom.normalFont));
		}
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda.setPhrase(new Phrase(Variables.FORMAT_FECHA_2.format(venta.getFecha_venta()), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabla_ventas.addCell(celda);
		
		celda.setPhrase(new Phrase(String.valueOf(venta.getCosto_total()), FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ventas.addCell(celda);
	}
	
	public void agregarIngresoTotalAInformeVentas(Document pdf_documento, InformeVentas informe_ventas){
		List<Venta> lista_ventas = informe_ventas.lista_ventas;
		Paragraph seccion_ingresos = new Paragraph();
		agregarLineaVacia(seccion_ingresos, 1);
		
		int columnas_tabla = 2;
		PdfPTable tabla_ingresos = new PdfPTable(columnas_tabla);
		float[] tamanio_columnas = new float[]{200f, 200f};
		try {
			tabla_ingresos.setWidths(tamanio_columnas);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		tabla_ingresos.setWidthPercentage(100);
		
		PdfPCell celda = new PdfPCell(new Phrase("CANT. VENTAS DIRECTAS: ", FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase(String.valueOf(informe_ventas.getCantidadVentasDirectas(lista_ventas)), FontCustom.boldItalicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase("CANT. VENTAS A DOMICILIO: ", FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase(String.valueOf(informe_ventas.getCantidadVentasDomicilio(lista_ventas)), FontCustom.boldItalicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase("TOTAL VENTAS REGISTRADAS: ", FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase(String.valueOf(lista_ventas.size()), FontCustom.boldItalicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase("TOTAL INGRESO ECONOMICO: ", FontCustom.normalFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		celda = new PdfPCell(new Phrase(String.valueOf(informe_ventas.getIngresoTotal(lista_ventas)), FontCustom.boldItalicFont));
		celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tabla_ingresos.addCell(celda);
		
		seccion_ingresos.add(tabla_ingresos);
		
		try {
			pdf_documento.add(seccion_ingresos);
		} catch (DocumentException e) {
			e.printStackTrace();
		}	
	}
	
	//-----------------------------------------------------------------------------------------------------------------
		
	public void verEmprimirInfoPdf(String mensaje_leendo){
		String pathFilePdf = new StringBuilder(Variables.FOLDER_INFO_VENTAS_PDF).append(this.nombre_pdf).toString();
		File file_pdf = new File(pathFilePdf);
		if(file_pdf.exists()){
			Toast.makeText(activity, mensaje_leendo, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file_pdf), "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			try{
				activity.startActivity(Intent.createChooser(intent, "Ver e Imprimir usando"));
			} catch(ActivityNotFoundException e){
				e.printStackTrace();
				Toast.makeText(activity, "No dispone de una App para ver este documento, recomendamos instalar Adobe Acrobat", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(activity, "Lo sentimos ocurrio un error", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void enviarInformePdf(String nombre_persona, String emailPara, String mensaje){
		//String emailDeQuien = "epicoapaza@gmail.com";
		String pathFilePdf = new StringBuilder(Variables.FOLDER_INFO_VENTAS_PDF).append(this.nombre_pdf).toString();
		File file_pdf = new File(pathFilePdf);
		if(file_pdf.exists()){
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, "Coop. Duraznillo");
			intent.putExtra(Intent.EXTRA_TEXT, "Hola "+nombre_persona+", \n "+mensaje+" \n" +
												"COOPERATIVA DE AGREGADOS DURAZNILLO");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailPara});
			//Esta parte es para enviar una copia del correo a un correo electronico
			//intent.putExtra(Intent.EXTRA_CC, new String[]{"emailParaCopia@gmail.com"});
			//intent.putExtra(Intent.EXTRA_BCC, new String[]{emailDeQuien});
			Uri uri = Uri.fromFile(file_pdf);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
			intent.setType("application/pdf");
			try{
				activity.startActivity(Intent.createChooser(intent, "Enviar usando"));
			}catch(ActivityNotFoundException e){
				e.printStackTrace();
				Toast.makeText(activity, "No dispone de una App para enviar este informe, recomendamos instalar Gmail",  Toast.LENGTH_LONG).show();
			}	
		}else{
			Toast.makeText(activity, "Lo sentimos ocurrio un error", Toast.LENGTH_SHORT).show();
		}
	}
	
	public File crearFichero(){
		File ruta_infos = new File(Variables.FOLDER_INFO_VENTAS_PDF);
		ruta_infos.mkdirs();
		File file_pdf = new File(ruta_infos, nombre_pdf);
		return file_pdf;
	}
	
	public void agregarLineaVacia(Paragraph paragraph, int numero_lineas){
		for (int i = 0; i < numero_lineas; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
	public Usuario getUsuario(String id_usuario){
		DBDuraznillo db = new DBDuraznillo(activity);
		Usuario usuario = null;
		try {
			db.abrirDB();
			usuario = db.getUsuario(id_usuario);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}

	public Producto getProducto(String id_prod){
		DBDuraznillo db = new DBDuraznillo(activity);
		Producto prod = null;
		try {
			db.abrirDB();
			prod = db.getProducto(id_prod);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prod;
	}
	
	public Cliente getCliente(String id_cliente){
		DBDuraznillo db = new DBDuraznillo(activity);
		Cliente cliente = null;
		try{
			db.abrirDB();
			cliente = db.getCliente(id_cliente);
			db.cerrarDB();
		} catch(Exception e){
			e.printStackTrace();
		}
		return cliente;
	}
	
	public Personal getPersonal(String id_personal){
		DBDuraznillo db = new DBDuraznillo(activity);
		Personal personal = null;
		try{
			db.abrirDB();
			personal = db.getPersonal(id_personal);
			db.cerrarDB();
		} catch(Exception e){
			e.printStackTrace();
		}
		return personal;
	}

	public Bitmap redimencionarImagen(Bitmap bitmap, float newWidth, float newHeight){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		float scaleWidth = ((float)newWidth)/width;
		float scaleHeigth = ((float)newHeight)/height;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeigth);
		
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
	}
	
}
