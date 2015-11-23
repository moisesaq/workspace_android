package com.moisse.others;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

import com.moisse.modelo.Parqueo;

public class GenerarDatosSalida {
	private java.util.Date dateUtil = new java.util.Date();
	private Date fechaActualSalida = new Date(dateUtil.getTime());
	private Time horaActualSalida = new Time(dateUtil.getHours(), dateUtil.getMinutes(), dateUtil.getSeconds());
	
	private int tiempoDia = (int)MyVar.COSTO_DEFAULT_CERO;
	private int cantNoche = (int)MyVar.COSTO_DEFAULT_CERO; 
	private double costoDia = MyVar.COSTO_DEFAULT_CERO;
	private double costoNoche = MyVar.COSTO_DEFAULT_CERO;
	private double costoTotal = MyVar.COSTO_DEFAULT_CERO;
	
	private StringBuilder vistaTiempo = new StringBuilder();
	
	private Parqueo parqueo;
	private int tolerancia;
	private Time horaIngreso, horaSalida;
	private Date fechaIngreso, fechaSalida;
	private Time horaInicioDia;
	private Time horaFinDia;
	private Time horaInicioNoche;
	private Time horaFinNoche;
	
	int HA, HE, MA, ME;
	
	String mensaje = "";
	String tiempo = "Total: ";
	
	public GenerarDatosSalida(Parqueo parqueo, Time horaE, Date fechaE){
		this.parqueo = parqueo;
		this.horaIngreso = horaE;
		this.fechaIngreso = fechaE;
		this.tolerancia = this.parqueo.getTolerancia();
		this.horaInicioDia = this.parqueo.getInicioDia();
		this.horaFinDia = this.parqueo.getFinDia();
		this.horaInicioNoche = this.parqueo.getInicioNoche();
		this.horaFinNoche = this.parqueo.getFinNoche();
		generarDatos();
		generarCostos();
	}

	public GenerarDatosSalida(Parqueo parqueo, Time horaE, Date fechaE, Time horaS, Date fechaS){
		this.parqueo = parqueo;
		this.horaIngreso = horaE;
		this.fechaIngreso = fechaE;
		this.horaSalida = horaS;
		this.fechaSalida = fechaS;
		this.tolerancia = this.parqueo.getTolerancia();
		this.horaInicioDia = this.parqueo.getInicioDia();
		this.horaFinDia = this.parqueo.getFinDia();
		this.horaInicioNoche = this.parqueo.getInicioNoche();
		this.horaFinNoche = this.parqueo.getFinNoche();
		generarDatos();
	}
	
	public void generarDatos(){
//		HA = horaSalida.getHours();
//		HE = horaEntrada.getHours();
//		MA = horaSalida.getMinutes();
//		ME = horaEntrada.getMinutes();
		
		int HSalida = horaActualSalida.getHours();
		int HIngreso = horaIngreso.getHours();
		int MinSalida = horaActualSalida.getMinutes();
		int MinIngreso = horaIngreso.getMinutes();
		
		if(getDiferenciaDias()==0){
			if(HSalida==HIngreso){
				vistaTiempo.append(tiempo).append(MinSalida-MinIngreso).append(" Min.");
				this.tiempoDia = 1;
			}else if((HSalida-HIngreso)==1){
				if(MinSalida<MinIngreso){
					vistaTiempo.append(tiempo).append(60-(MinIngreso-MinSalida)).append(" Min.");
					this.tiempoDia = (HSalida-HIngreso);
				}else if(MinSalida==MinIngreso){
					this.tiempoDia = (HSalida-HIngreso);
					vistaTiempo.append(tiempo).append(HSalida-HIngreso).append(" Hr.");
				}else if(MinSalida>MinIngreso){
					vistaTiempo.append(tiempo).append(HSalida-HIngreso).append(" Hr. y ").append(MinSalida-MinIngreso).append(" Min.");
					if((MinSalida-MinIngreso)<=tolerancia){
						this.tiempoDia = (HSalida-HIngreso);
					}else{
						this.tiempoDia = (HSalida-HIngreso)+1;
					}
				}	
			}else if((HSalida-HIngreso)>1){
					if(MinSalida<MinIngreso){
						vistaTiempo.append(tiempo).append((HSalida-HIngreso)-1).append(" Hrs. y ").append(60-(MinIngreso-MinSalida)).append(" Min.");
						if((60-(MinIngreso-MinSalida))<=tolerancia){
							this.tiempoDia = (HSalida-HIngreso)-1;
						}else{
							this.tiempoDia = (HSalida-HIngreso);
						}
					}else if(MinSalida==MinIngreso){
						vistaTiempo.append(tiempo).append(HSalida-HIngreso).append(" Hrs.");
						this.tiempoDia = (HSalida-HIngreso);
					}else if(MinSalida>MinIngreso){
						vistaTiempo.append(tiempo).append(HSalida-HIngreso).append(" Hrs. y ").append(MinSalida-MinIngreso).append(" Min.");
						if((MinSalida-MinIngreso)<=tolerancia){
							this.tiempoDia = (HSalida-HIngreso);
						}else{
							this.tiempoDia = (HSalida-HIngreso)+1;
						}
					}
			}
		}else if(getDiferenciaDias()==1){
			this.cantNoche = 1;
			int hora_inicio_dia = horaInicioDia.getHours();
			int min_inicio_dia = horaInicioDia.getMinutes();
			
			if(HIngreso>=horaInicioNoche.getHours()){
				if(HSalida<hora_inicio_dia){
					vistaTiempo.append(tiempo).append("1 sola noche");
				}else if(HSalida==hora_inicio_dia){
					if(MinSalida<=min_inicio_dia+tolerancia){
						this.tiempoDia = 0;
						vistaTiempo.append(tiempo).append("1 sola noche");
					}else{
						vistaTiempo.append(tiempo).append("1 Noche, y ").append(MinSalida-min_inicio_dia).append(" Min.");
						this.tiempoDia = 1;
					}
				}else if ((HSalida-hora_inicio_dia)==1){
					if(MinSalida<min_inicio_dia){
						vistaTiempo.append(tiempo).append("1 Noche, y ").append(60-(min_inicio_dia-MinSalida)).append(" Min.");
						this.tiempoDia = HSalida-hora_inicio_dia;
					}else if(MinSalida==min_inicio_dia){
						this.tiempoDia = (HSalida-hora_inicio_dia);
						vistaTiempo.append(tiempo).append("1 Noche, y ").append(HSalida-hora_inicio_dia).append(" Hr.");
					}else if(MinSalida>min_inicio_dia){
						vistaTiempo.append(tiempo).append("1 Noche, ").append(HSalida-hora_inicio_dia).
																		append(" Hrs. y ").append(MinSalida-min_inicio_dia).append(" Min.");
						if((MinSalida-min_inicio_dia)<=tolerancia){
							this.tiempoDia = HSalida-hora_inicio_dia;
						}else{
							this.tiempoDia = (HSalida-hora_inicio_dia)+1;
						}
					}
				}else if((HSalida-hora_inicio_dia)>1){
					if(MinSalida<min_inicio_dia){
						vistaTiempo.append(tiempo).append("1 Noche, ").append((HSalida-hora_inicio_dia)-1).
																					append(" Hrs. y ").append(60-(min_inicio_dia-MinSalida)).append(" Hr.");
						if((60-(min_inicio_dia-MinSalida))<=tolerancia){
							this.tiempoDia = (HSalida-hora_inicio_dia)-1;
						}else{
							this.tiempoDia = (HSalida-hora_inicio_dia);
						}
					}else if(MinSalida==min_inicio_dia){
						vistaTiempo.append(tiempo).append("1 Noche, y ").append(HSalida-hora_inicio_dia).append(" Hrs.");
						this.tiempoDia = (HSalida-hora_inicio_dia);
					}else if(MinSalida>min_inicio_dia){
						vistaTiempo.append(tiempo).append("1 Noche, ").append(HSalida-hora_inicio_dia).
																				append(" Hrs. y ").append(MinSalida-min_inicio_dia).append(" Min.");
						if((MinSalida-min_inicio_dia)<=tolerancia){
							this.tiempoDia = (HSalida-hora_inicio_dia);
						}else{
							this.tiempoDia = (HSalida-hora_inicio_dia)+1;
						}
					}
				}
			}else if(HIngreso<horaInicioNoche.getHours()){
				int mins_antes = 0;
				int hrs_antes = 0;
				if(MinIngreso>0){
					mins_antes = 60-MinIngreso;
					hrs_antes = (horaFinDia.getHours()-HIngreso)-1;
				}else{
					mins_antes = 0;
					hrs_antes = (horaFinDia.getHours()-HIngreso);
				}
				int mins_hoy = 0;
				int hrs_hoy = 0;
				if(HSalida>hora_inicio_dia && MinSalida>=min_inicio_dia){
					mins_hoy = MinSalida-min_inicio_dia;
					hrs_hoy = (HSalida - hora_inicio_dia);
				}else if (HSalida==hora_inicio_dia) {
					if(MinSalida<min_inicio_dia){
						mins_hoy = 60-(min_inicio_dia-MinSalida);
					}else if (MinSalida>=min_inicio_dia) {
						mins_hoy = MinSalida-min_inicio_dia;
					}
				}
				Time time_antes = new Time(hrs_antes, mins_antes, 0);
				Time time_actual = getTimeModificado(time_antes, mins_hoy+(60*hrs_hoy));
				vistaTiempo.append(tiempo).append("1 Noche, ").append(time_actual.getHours()).
																append(" Hrs. y ").append(time_actual.getMinutes()).append(" Min.");
				if(time_actual.getMinutes()<=tolerancia){
					this.tiempoDia = time_actual.getHours();
				}else{
					this.tiempoDia = time_actual.getHours()+1;
				}
			}
		}else if (getDiferenciaDias()>1) {
			int noches_pasados = (int)getDiferenciaDias();
			this.cantNoche = noches_pasados;
			//TODO aqui recalcular bien la hora dia cuando empiece 07:30 o 08:00
			int cant_horas_dia = (horaFinDia.getHours()-horaInicioDia.getHours())*noches_pasados;
			
			int hora_inicio_dia = horaInicioDia.getHours();
			int min_inicio_dia = horaInicioDia.getMinutes();
			if(HIngreso>=horaInicioNoche.getHours()){
				if(HSalida<hora_inicio_dia){
					vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches");
				}else if(HSalida==hora_inicio_dia){
					if(MinSalida<=min_inicio_dia+tolerancia){
						this.tiempoDia = cant_horas_dia;
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia).append(" Hrs.");
					}else{
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia).append(" Hrs. y ").
																								append(MinSalida-min_inicio_dia).append(" Min.");
						this.tiempoDia = cant_horas_dia + 1;
					}
				}else if ((HSalida-hora_inicio_dia)==1){
					if(MinSalida<min_inicio_dia){
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia+(HSalida-hora_inicio_dia)).append(" Hrs. y ").
																								append(60-(min_inicio_dia-MinSalida)).append(" Min.");
						this.tiempoDia = cant_horas_dia+(HSalida-hora_inicio_dia);
					}else if(MinSalida==min_inicio_dia){
						this.tiempoDia = cant_horas_dia+(HSalida-hora_inicio_dia);
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia+(HSalida-hora_inicio_dia)).append(" Hrs.");
					}else if(MinSalida>min_inicio_dia){
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia+(HSalida-hora_inicio_dia)).append(" Hrs. y ").
																								append(MinSalida-min_inicio_dia).append(" Min.");
						if((MinSalida-min_inicio_dia)<=tolerancia){
							this.tiempoDia = cant_horas_dia+(HSalida-hora_inicio_dia);
						}else{
							this.tiempoDia = cant_horas_dia+((HSalida-hora_inicio_dia)+1);
						}
					}
				}else if((HSalida-hora_inicio_dia)>1){
					if(MinSalida<min_inicio_dia){
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append((cant_horas_dia+(HSalida-hora_inicio_dia))-1).append(" Hrs. y ").
																								append(60-(min_inicio_dia-MinSalida)).append(" Min.");
						if((60-(min_inicio_dia-MinSalida))<=tolerancia){
							this.tiempoDia = (cant_horas_dia+(HSalida-hora_inicio_dia))-1;
						}else{
							this.tiempoDia = cant_horas_dia+(HSalida-hora_inicio_dia);
						}
					}else if(MinSalida==min_inicio_dia){
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia+(HSalida-hora_inicio_dia)).append(" Hrs.");
						this.tiempoDia = cant_horas_dia+(HSalida-hora_inicio_dia);
					}else if(MinSalida>min_inicio_dia){
						vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia+(HSalida-hora_inicio_dia)).append(" Hrs. y ").
																								append(MinSalida-min_inicio_dia).append(" Min.");
						if((MinSalida-min_inicio_dia)<=tolerancia){
							this.tiempoDia = cant_horas_dia+(HSalida-hora_inicio_dia);
						}else{
							this.tiempoDia = cant_horas_dia+((HSalida-hora_inicio_dia)+1);
						}
					}
				}
			}else if(HIngreso<horaInicioNoche.getHours()){
				int mins_antes = 0;
				int hrs_antes = 0;
				if(MinIngreso>0){
					mins_antes = 60-MinIngreso;
					hrs_antes = (horaFinDia.getHours()-HIngreso)-1;
				}else{
					mins_antes = 0;
					hrs_antes = (horaFinDia.getHours()-HIngreso);
				}
				int mins_hoy = 0;
				int hrs_hoy = 0;
				if(HSalida>hora_inicio_dia && MinSalida>=min_inicio_dia){
					mins_hoy = MinSalida-min_inicio_dia;
					hrs_hoy = (HSalida - hora_inicio_dia);
				}else if (HSalida==hora_inicio_dia) {
					if(MinSalida<min_inicio_dia){
						mins_hoy = 60-(min_inicio_dia-MinSalida);
					}else if (MinSalida>=min_inicio_dia) {
						mins_hoy = MinSalida-min_inicio_dia;
					}
				}
				Time time_antes = new Time(hrs_antes, mins_antes, 0);
				Time time_actual = getTimeModificado(time_antes, mins_hoy+(60*hrs_hoy));
				vistaTiempo.append(tiempo).append(noches_pasados).append(" Noches, ").append(cant_horas_dia+time_actual.getHours()).append(" Hrs. y ").
																						append(time_actual.getMinutes()).append(" Min.");
				if(time_actual.getMinutes()<=tolerancia){
					this.tiempoDia = cant_horas_dia+time_actual.getHours();
				}else{
					this.tiempoDia = cant_horas_dia+(time_actual.getHours()+1);
				}
			}
		}
	}
	
	public void generarCostos(){
		this.costoDia = this.tiempoDia*parqueo.getPrecioHoraDia();
		if(this.cantNoche!=0){
			this.costoNoche = this.cantNoche*parqueo.getPrecioNoche();
		}
		this.costoTotal = this.costoDia+this.costoNoche;
	}
	
	public Time getTimeModificado(Time time, int min){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, time.getHours(), time.getMinutes(), time.getSeconds());
		calendar.add(Calendar.MINUTE, min);
		Time time_modificado = new Time(calendar.getTimeInMillis());
		return time_modificado;
	}
	
	public long getDiferenciaDias(){
		return (fechaActualSalida.getTime()-fechaIngreso.getTime())/(24*60*60*1000);
	}
	
	//-------------------------------------------------------------------------------------

	public int getTiempoDia() {
		return tiempoDia;
	}

	public int getCantNoche() {
		return cantNoche;
	}

	public StringBuilder getVistaTiempo() {
		return vistaTiempo;
	}

	public Time getHoraEntrada() {
		return horaIngreso;
	}

	public Date getFechaEntrada() {
		return fechaIngreso;
	}

	public double getCostoDia() {
		return costoDia;
	}

	public double getCostoNoche() {
		return costoNoche;
	}

	public double getCostoTotal() {
		return costoTotal;
	}
		
}
