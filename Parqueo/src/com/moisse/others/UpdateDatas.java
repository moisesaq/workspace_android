package com.moisse.others;

import android.app.Activity;

import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Usuario;

public class UpdateDatas {

	protected Parqueo parqueo_online;
	protected Usuario usuario_online;
	protected Activity activity;
	protected HttpClientCustom httpCustom;
	public UpdateDatas(Parqueo parqueo_online, Usuario usuario_online, Activity activity){
		this.parqueo_online = parqueo_online;
		this.usuario_online = usuario_online;
		this.activity = activity;
		httpCustom = new HttpClientCustom();
	}
}
