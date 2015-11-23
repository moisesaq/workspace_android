package com.moisse.others;

import java.sql.Time;
import java.sql.Date;

import com.moisse.modelo.Parqueo;

public class GenerateDataOutput {
	
	private Parqueo parqueo_online;
	private StringBuilder vistaTiempo = new StringBuilder();
	private StringBuilder vistaTDia = new StringBuilder();
	private StringBuilder vistaTNoche = new StringBuilder();
	private StringBuilder vistaCosto = new StringBuilder();
	private StringBuilder vistaCDia = new StringBuilder();
	private StringBuilder vistaCNoche = new StringBuilder();
	private long tiempoDia, tiempoNoche, tiempoTotal;
	private double costoDia, costoNoche, costoTotal;

	private double precioDia, precioNoche;
	private int t_tolerancia;
	private java.util.Date dateUtil = new java.util.Date();
	private Date fechaActual = new Date(dateUtil.getTime());
	private Date fechaEntrada;
	private Time horaActual = new Time(dateUtil.getHours(), dateUtil.getMinutes(), dateUtil.getSeconds());
	private Time horaEntrada;
	
	public GenerateDataOutput(Time horaE, Date fechaE, Parqueo parqueo_online){
		this.horaEntrada = horaE;
		this.fechaEntrada = fechaE;
		this.parqueo_online = parqueo_online;
		this.precioDia = this.parqueo_online.getPrecioHoraDia();
		this.precioNoche = this.parqueo_online.getPrecioNoche();
		this.t_tolerancia = this.parqueo_online.getTolerancia();
		generarDatosParking();
		generarDatosDetalleParking();
	}
	
	public void generarDatosParking(){
		int HA = horaActual.getHours();
		int HE = horaEntrada.getHours();
		int MA = horaActual.getMinutes();
		int ME = horaEntrada.getMinutes();
		
		if(getDiferenciaDias()==0){
			if(HA==HE){
				this.tiempoTotal = 0;
				vistaTiempo.append("Total: ").append(MA-ME).append(" Min.-1");
			}else if((HA-HE)==1 && MA<=ME){
				this.tiempoTotal = 0;
				vistaTiempo.append("Total: ").append(60-(ME-MA)).append(" Min.-2");
			}else if((HA-HE)==1 && MA>ME){
				vistaTiempo.append("Total: ").append(" 1 Hrs. y ").append(MA-ME).append(" Min.-2.2");
				if((MA-ME)<=t_tolerancia){
					this.tiempoTotal = 1;
				}else{
					this.tiempoTotal = 2;
				}
			}else if((HA-HE)>1 && MA<ME){
				vistaTiempo.append("Total: ").append((HA-HE)).append(" Hrs. y ").append(60-(ME-MA)).append(" Min.1m");
				if((60-(ME-MA))<=t_tolerancia){
					this.tiempoTotal = (HA-HE);
				}else{
					this.tiempoTotal = (HA-HE)+1;
				}
			}else if((HA-HE)>1 && MA>=ME){
				vistaTiempo.append("Total: ").append((HA-HE)).append(" Hrs. y ").append(MA-ME).append(" Min.1mm");
				if((MA-ME)<=t_tolerancia){
					this.tiempoTotal = (HA-HE);
				}else{
					this.tiempoTotal = (HA-HE)+1;
				}
			}	
		}else{
			if(getDiferenciaDias()==1 && HA<HE){
				if(MA<ME){
					vistaTiempo.append("Total: ").append(24-(HE-HA)).append(" Hrs.2 y ").append(60-(ME-MA)).append(" Min.");
					if((60-(ME-MA))<=t_tolerancia){
						this.tiempoTotal = (24-(HE-HA));
					}else{
						this.tiempoTotal = (24-(HE-HA))+1;
					}
				}else if(MA>=ME){
					vistaTiempo.append("Total: ").append(24-(HE-HA)).append(" Hrs.2.1 y ").append(MA-ME).append(" Min.");
					if((MA-ME)<=t_tolerancia){
						this.tiempoTotal = (24-(HE-HA));
					}else{
						this.tiempoTotal = (24-(HE-HA))+1;
					}
				}
			}else{
				if(HA>HE){					
					if(MA<ME){
						vistaTiempo.append("Total: ").append(getDiferenciaDias()).append(" dia(s), ")
						   .append(HA-HE).append(" Hrs.3").append("y ").append(60-(ME-MA)).append(" Min.");
						if((60-(ME-MA))<=t_tolerancia){
							this.tiempoTotal = (getDiferenciaDias()*24)+(HA-HE);
						}else{
							this.tiempoTotal = ((getDiferenciaDias()*24)+(HA-HE))+1;
						}
					}else if(MA>=ME){
						vistaTiempo.append("Total: ").append(getDiferenciaDias()).append(" dia(s), ")
						   .append(HA-HE).append(" Hrs.3.1").append("y ").append(MA-ME).append(" Min.");
						if((MA-ME)<=t_tolerancia){
							this.tiempoTotal = (getDiferenciaDias()*24)+(HA-HE);
						}else{
							this.tiempoTotal = ((getDiferenciaDias()*24)+(HA-HE))+1;
						}
					}
				}else if(HA==HE){
					if(MA<ME){
						vistaTiempo.append("Total: ").append(getDiferenciaDias()).append(" dia(s), ")
						   .append("y ").append(60-(ME-MA)).append(" Min.");
						if((60-(ME-MA))<=t_tolerancia){
							this.tiempoTotal = (getDiferenciaDias()*24);
						}else{
							this.tiempoTotal = (getDiferenciaDias()*24)+1;
						}
					}else if(MA>=ME){
						vistaTiempo.append("Total: ").append(getDiferenciaDias()).append(" dia(s), ")
						   .append("y ").append(60-(ME-MA)).append(" Min.");
						if((MA-ME)<=t_tolerancia){
							this.tiempoTotal = (getDiferenciaDias()*24);
						}else{
							this.tiempoTotal = (getDiferenciaDias()*24)+1;
						}
					}
				}else if(HA<HE){
					if(MA<ME){
						vistaTiempo.append("Total: ").append(getDiferenciaDias()-1).append(" dia(s), ")
						   .append((24-(HE-HA))-1).append(" Hrs.-1.2").append("y ").append(60-(ME-MA)).append(" Min.");
						if((60-(ME-MA))<=t_tolerancia){
							this.tiempoTotal = ((getDiferenciaDias()-1)*24)+(24-(HE-HA))-1;
						}else{
							this.tiempoTotal = ((getDiferenciaDias()-1)*24)+(24-(HE-HA));
						}
					}else if(MA>=ME){
						vistaTiempo.append("Total: ").append(getDiferenciaDias()-1).append(" dia(s), ")
						   .append((24-(HE-HA))).append(" Hrs.3.1").append("y ").append(MA-ME).append(" Min.");
						if((MA-ME)<=t_tolerancia){
							this.tiempoTotal = ((getDiferenciaDias()-1)*24)+(24-(HE-HA));
						}else{
							this.tiempoTotal = ((getDiferenciaDias()-1)*24)+(24-(HE-HA))+1;
						}
					}
				}
			}
		}		
	}
	
	public void generarDatosDetalleParking(){
		int HE = horaEntrada.getHours();
		this.tiempoDia = 0;
		this.tiempoNoche = 0;
		
		if(this.tiempoTotal==0){
			if(HE>6 && HE<19){
				tiempoDia++;
			}else if(HE>-1 && HE<7 || HE>19 && HE<24){
				tiempoNoche++;
			}
		}else{
			for (int i = 0; i < this.tiempoTotal; i++) {
				if(HE>23){
					HE = 0;
				}
				if(HE>6 && HE<19){
					tiempoDia++; HE++;
				}else if(HE>-1 && HE<7 || HE>18 && HE<24){
					tiempoNoche++; HE++;
				}		
			}
		}

		vistaTDia.append("Dia: ").append(tiempoDia).append(" Hrs.");
		vistaTNoche.append("Noche: ").append(tiempoNoche).append(" Hrs.");
		this.costoDia = tiempoDia*precioDia;
		this.costoNoche = tiempoNoche*precioNoche;
		this.costoTotal = this.costoDia + this.costoNoche;
		vistaCDia.append("Dia: ").append(costoDia).append(" Bs.");
		vistaCNoche.append("Noche: ").append(costoNoche).append(" Bs.");
		vistaCosto.append("Total: ").append(costoTotal).append(" Bs.");
	}
	
	public long getDiferenciaDias(){
		return (fechaActual.getTime()-fechaEntrada.getTime())/(24*60*60*1000);
	}
	
	//-------------------------DATOS-----------------------------
	
	public double getCostoDia() {
		return costoDia;
	}

	public double getCostoNoche() {
		return costoNoche;
	}

	public double getCostoTotal() {
		return costoTotal;
	}
	
	//-------------------------VISTAS-----------------------------


	public StringBuilder getVistaTiempo() {
		return vistaTiempo;
	}

	public StringBuilder getVistaTDia() {
		return vistaTDia;
	}

	public StringBuilder getVistaTNoche() {
		return vistaTNoche;
	}

	public StringBuilder getVistaCosto() {
		return vistaCosto;
	}

	public StringBuilder getVistaCDia() {
		return vistaCDia;
	}

	public StringBuilder getVistaCNoche() {
		return vistaCNoche;
	}
	
}
