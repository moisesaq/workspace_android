package com.moisse.generarpdfenviar;

import java.util.List;

public class InvoiceObject {

	public int id_factura;
	public String fecha_venta;
	public String nombre_cooperativa;
	public String telf_coop;
	public String direccion_coop;
	public String ciudad_coop;
	
	public String nombre_cliente;
	public String ci_cliente;
	public String telf_cliente;
	public String direccion_cliente;
	
	public double costo_total;
	
	public List<InvoiceDetails> invoiceDetailsList;
}
