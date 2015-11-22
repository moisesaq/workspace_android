package com.moises.notificaciones;

import java.sql.Time;

import com.example.brasil2014fixture.MenuFixture;
import com.example.brasil2014fixture.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NoticacionPartido extends Activity{

	
	String pais1,pais2;
	Time hora;
	Context miContexto;
	NotificationManager notimanager;
	static final int codigoNotification=777888;
	
	public NoticacionPartido(String pais1, String pais2, Time hora, Context contexto){
		this.pais1=pais1;
		this.pais2=pais2;
		this.hora=hora;
		this.miContexto=contexto;
		
		notimanager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
	}
	
	public void activarAlarmaHora(){
		
		String mensaje="Hoy juega: "+pais1+" vs. "+pais2+" a las "+hora;
		Intent intento=new Intent(miContexto,MenuFixture.class);
		PendingIntent pi=PendingIntent.getActivity(miContexto, 0, intento, 0);
		Notification noti=new Notification(R.drawable.fuleco,mensaje,System.currentTimeMillis());
		noti.setLatestEventInfo(miContexto, "Fixture Brasil 2014", mensaje, pi);
		noti.defaults=Notification.DEFAULT_ALL;
		notimanager.notify(codigoNotification,noti);
	}
	
	public void calcalarNoticacionPartido(){
		notimanager.cancel(codigoNotification);
	}
}
