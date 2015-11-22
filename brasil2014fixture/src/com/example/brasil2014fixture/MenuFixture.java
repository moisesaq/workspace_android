package com.example.brasil2014fixture;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import com.moises.adapters.AdapterPartidos;
import com.moises.data.base.fixture.DBFixture;
import com.moises.dialogos.DialogoAlerta;
import com.moises.dialogos.DialogoListaPartidos;
import com.moises.extras.VerificarFechaPartido;
import com.moises.modelo.Partido;
import com.moises.modelo.PartidoCuartos;
import com.moises.modelo.PartidoFinales;
import com.moises.modelo.PartidoOctavos;
import com.moises.modelo.PartidoSemis;
import com.moises.notificaciones.NoticacionPartido;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFixture extends Activity implements OnClickListener{
	
	ImageView ivOctavos, ivCuartos, ivSemis,ivFinales,ivBuscarPartidos,ivBuscarPais;
	ImageView ivGA,ivGB,ivGC,ivGD,ivGE,ivGF,ivGG,ivGH;
	
	EditText etFechaMenu;
	AutoCompleteTextView actvBuscador;
	
	int anio,mes,dia;
	VerificarFechaPartido verif1;
	
	NotificationManager notimanager;
	static final int codigoNotification=777999;
	NoticacionPartido notiPartido;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menufixture);
		viewGroups();
		
		notimanager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notimanager.cancel(codigoNotification);
		
		ivOctavos=(ImageView)findViewById(R.id.ivOctavos);
		ivOctavos.setOnClickListener(this);
		ivCuartos=(ImageView)findViewById(R.id.ivCuartos);
		ivCuartos.setOnClickListener(this);
		ivSemis=(ImageView)findViewById(R.id.ivSemis);
		ivSemis.setOnClickListener(this);
		ivFinales=(ImageView)findViewById(R.id.ivFinales);
		ivFinales.setOnClickListener(this);
		ivBuscarPartidos=(ImageView)findViewById(R.id.ivBuscarFechaPartido);
		ivBuscarPartidos.setOnClickListener(this);
		etFechaMenu=(EditText)findViewById(R.id.etFechaPartido);
		ivBuscarPais=(ImageView)findViewById(R.id.ivBuscarPais);
		ivBuscarPais.setOnClickListener(this);
		actvBuscador=(AutoCompleteTextView)findViewById(R.id.actvBuscadorPais);
		adaptarAutoCompleteTV();
		
		
		Thread test=new Thread(){
			public void run(){
				try{
					while (true) {
						sleep(30000);
						AlarmaPartidos();
					}
				}catch(InterruptedException e){
					String error=e.toString();
					mensajeError(error, " al ver hora");
				}
			}
		};
		test.start();

	}
	
	public void AlarmaPartidos(){
		ArrayList<Partido> partidosHoy=new ArrayList<Partido>();
		ArrayList<PartidoOctavos> partidosOctavos=new ArrayList<PartidoOctavos>();
		ArrayList<PartidoCuartos> partidosCuartos=new ArrayList<PartidoCuartos>();
		ArrayList<PartidoSemis> partidosSemis=new ArrayList<PartidoSemis>();
		ArrayList<PartidoFinales> partidosFinales=new ArrayList<PartidoFinales>();
		VerificarFechaPartido verif = new VerificarFechaPartido();
		Date fecha=verif.getFechaActual();
		DBFixture db=new DBFixture(this);
		try {
			db.AbrirDB();
			partidosHoy=db.getAdapterPatidosFecha(fecha);			
			for (int posicion = 0; posicion < partidosHoy.size(); posicion++) {
				verif1 = new VerificarFechaPartido(partidosHoy.get(posicion).getFecha(),partidosHoy.get(posicion).getHora());
				if(verif1.verificarPartidoHoy())
					if(verif1.verificarHoraParaAlarma()){
						activarAlarma(partidosHoy.get(posicion).getPais1(), partidosHoy.get(posicion).getPais2());
					}
			}
			
			partidosOctavos=db.getAdaptadorPartidosOctavos();			
			for (int posicion = 0; posicion < partidosOctavos.size(); posicion++) {
				verif1 = new VerificarFechaPartido(partidosOctavos.get(posicion).getFecha(),partidosOctavos.get(posicion).getHora());
				if(verif1.verificarPartidoHoy())
					if(verif1.verificarHoraParaAlarma()){
						activarAlarma(partidosOctavos.get(posicion).getPais1(), partidosOctavos.get(posicion).getPais2());
					}
			}
			
			partidosCuartos=db.getAdaptadorPartidosCuartos();
			for (int posicion = 0; posicion < partidosCuartos.size(); posicion++) {
				verif1 = new VerificarFechaPartido(partidosCuartos.get(posicion).getFecha(),partidosCuartos.get(posicion).getHora());
				if(verif1.verificarPartidoHoy())
					if(verif1.verificarHoraParaAlarma()){
						activarAlarma(partidosCuartos.get(posicion).getPaisganador1(), partidosCuartos.get(posicion).getPaisganador2());
					}
			}
			
			partidosSemis=db.getAdaptadorPartidosSemis();
			for (int posicion = 0; posicion < partidosSemis.size(); posicion++) {
				verif1 = new VerificarFechaPartido(partidosSemis.get(posicion).getFecha(),partidosSemis.get(posicion).getHora());
				if(verif1.verificarPartidoHoy())
					if(verif1.verificarHoraParaAlarma()){
						activarAlarma(partidosSemis.get(posicion).getPaisganador1(), partidosSemis.get(posicion).getPaisganador2());
					}
			}
			
			partidosFinales=db.getAdaptadorPartidosFinales();
			for (int posicion = 0; posicion < partidosFinales.size(); posicion++) {
				verif1 = new VerificarFechaPartido(partidosFinales.get(posicion).getFecha(),partidosFinales.get(posicion).getHora());
				if(verif1.verificarPartidoHoy())
					if(verif1.verificarHoraParaAlarma()){
						activarAlarma(partidosFinales.get(posicion).getPaisganador1(), partidosFinales.get(posicion).getPaisganador2());
					}
			}
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al verificar partidos");
		}
	}

	public void activarAlarma(String pais1, String pais2){
		String mensaje="Partido: "+pais1+" vs."+pais2+" en 30min.";
		Intent intento=new Intent(this,MenuFixture.class);
		PendingIntent pi=PendingIntent.getActivity(this, 0, intento, 0);
		Notification noti=new Notification(R.drawable.fuleco,mensaje,System.currentTimeMillis());
		noti.setLatestEventInfo(this, "Fixture Brasil 2014", mensaje, pi);
		noti.defaults=Notification.DEFAULT_ALL;
		notimanager.notify(codigoNotification,noti);	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivOctavos:
			Intent iOctavos= new Intent("android.intent.action.Octavos");
			startActivity(iOctavos);
			break;
		
		case R.id.ivCuartos:
			Intent iCuartos= new Intent("android.intent.action.Cuartos");
			startActivity(iCuartos);
			break;
		
		case R.id.ivSemis:
			Intent iSemis= new Intent("android.intent.action.Semis");
			startActivity(iSemis);
			break;
		
		case R.id.ivFinales:
			Intent iFinales= new Intent("android.intent.action.Finales");
			startActivity(iFinales);
			break;
			
		case R.id.ivBuscarFechaPartido:
			Calendar calendario = Calendar.getInstance();
			anio=calendario.get(Calendar.YEAR);
			mes=calendario.get(Calendar.MONTH);
			dia=calendario.get(Calendar.DAY_OF_MONTH);
			
			OnDateSetListener controlDatePicker=new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					anio=year;
					mes=monthOfYear;
					dia=dayOfMonth;
					int uno=1;
					etFechaMenu.setText(anio+"-"+(mes+uno)+"-"+dia);
					Date fecha=Date.valueOf(anio+"-"+(mes+uno)+"-"+dia);
					buscarPartido(fecha);
				}
			};
			DatePickerDialog miDateP = new DatePickerDialog(this, controlDatePicker, anio, mes, dia);
			miDateP.show();
			break;
		
		case R.id.ivBuscarPais:
			if(getGrupo(actvBuscador.getText().toString()).equals("")){
				Toast toast=Toast.makeText(this, "Ese equipo no esta en el mundial 2014", Toast.LENGTH_LONG);
				toast.show();
			}else{
				Intent iBuscado= new Intent("android.intent.action.FIXTU");
				Bundle cajonBuscado=new Bundle();
				cajonBuscado.putString("grupo", getGrupo(actvBuscador.getText().toString()));
				iBuscado.putExtras(cajonBuscado);
				startActivity(iBuscado);
			}
			break;
			
		case R.id.ivGA:
			Intent iGA=new Intent("android.intent.action.FIXTU");
			Bundle cajonGA=new Bundle();
			cajonGA.putString("grupo", "A");
			iGA.putExtras(cajonGA);
			startActivity(iGA);
			break;
		case R.id.ivGB:
			Intent iGB=new Intent("android.intent.action.FIXTU");
			Bundle cajonGB=new Bundle();
			cajonGB.putString("grupo", "B");
			iGB.putExtras(cajonGB);
			startActivity(iGB);
			break;
		case R.id.ivGC:
			Intent iGC=new Intent("android.intent.action.FIXTU");
			Bundle cajonGC=new Bundle();
			cajonGC.putString("grupo", "C");
			iGC.putExtras(cajonGC);
			startActivity(iGC);
			break;
		case R.id.ivGD:
			Intent iGD=new Intent("android.intent.action.FIXTU");
			Bundle cajonGD=new Bundle();
			cajonGD.putString("grupo", "D");
			iGD.putExtras(cajonGD);
			startActivity(iGD);
			break;
		case R.id.ivGE:
			Intent iGE=new Intent("android.intent.action.FIXTU");
			Bundle cajonGE=new Bundle();
			cajonGE.putString("grupo", "E");
			iGE.putExtras(cajonGE);
			startActivity(iGE);
			break;
		case R.id.ivGF:
			Intent iGF=new Intent("android.intent.action.FIXTU");
			Bundle cajonGF=new Bundle();
			cajonGF.putString("grupo", "F");
			iGF.putExtras(cajonGF);
			startActivity(iGF);
			break;
		case R.id.ivGG:
			Intent iGG=new Intent("android.intent.action.FIXTU");
			Bundle cajonGG=new Bundle();
			cajonGG.putString("grupo", "G");
			iGG.putExtras(cajonGG);
			startActivity(iGG);
			break;
		case R.id.ivGH:
			Intent iGH=new Intent("android.intent.action.FIXTU");
			Bundle cajonGH=new Bundle();
			cajonGH.putString("grupo", "H");
			iGH.putExtras(cajonGH);
			startActivity(iGH);
			break;
		default:
			break;
		}
	}
	
	public void viewGroups(){
		ivGA=(ImageView)findViewById(R.id.ivGA);
		ivGA.setOnClickListener(this);
		ivGB=(ImageView)findViewById(R.id.ivGB);
		ivGB.setOnClickListener(this);
		ivGC=(ImageView)findViewById(R.id.ivGC);
		ivGC.setOnClickListener(this);
		ivGD=(ImageView)findViewById(R.id.ivGD);
		ivGD.setOnClickListener(this);
		ivGE=(ImageView)findViewById(R.id.ivGE);
		ivGE.setOnClickListener(this);
		ivGF=(ImageView)findViewById(R.id.ivGF);
		ivGF.setOnClickListener(this);
		ivGG=(ImageView)findViewById(R.id.ivGG);
		ivGG.setOnClickListener(this);
		ivGH=(ImageView)findViewById(R.id.ivGH);
		ivGH.setOnClickListener(this);
	}
	
	public void buscarPartido(Date fecha){
		DBFixture dbf=new DBFixture(this);
		try {
			dbf.AbrirDB();
			ArrayList<Partido> lista=dbf.getAdapterPatidosFecha(fecha);
			if(lista.size()==0){
				DialogoAlerta dial=new DialogoAlerta("No hay partidos para esta fecha", "Aviso", this);
				dial.show(getFragmentManager(), "tagAviso");
			}else{
				AdapterPartidos adapter = new AdapterPartidos(this, lista);
				
				DialogoListaPartidos dialogo=new DialogoListaPartidos(this, "Lista partidos", adapter);
				dialogo.show(getFragmentManager(), "tagAlerta");
			}
			dbf.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al buscar partidos");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(this);
		mensaje.setTitle("Error "+titulo);
		TextView texto=new TextView(this);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
	
	public String getGrupo(String pais){
		String grupo="";
		DBFixture dbf=new DBFixture(this);
		try {
			dbf.AbrirDB();
			grupo=dbf.getGrupoEquipo(pais);
			dbf.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al buscar equipo");
		}
		return grupo;
	}
	
	public void adaptarAutoCompleteTV(){
		DBFixture dbfixtu=new DBFixture(this);
		try {
			dbfixtu.AbrirDB();
			String[] paises=dbfixtu.getListaPaises();
			ArrayAdapter<String> aaEquipos=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,paises);
			actvBuscador.setAdapter(aaEquipos);
			dbfixtu.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "al cargar AutoCompleteTextView");
		}
	}
}
