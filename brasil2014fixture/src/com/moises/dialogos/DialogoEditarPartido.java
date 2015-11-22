package com.moises.dialogos;

import java.sql.Date;
import java.util.Calendar;

import com.example.brasil2014fixture.Fixture;
import com.example.brasil2014fixture.R;
import com.moises.data.base.fixture.DBFixture;
import com.moises.extras.VerificarFechaPartido;
import com.moises.modelo.Partido;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogoEditarPartido extends DialogFragment{

	Context miContexto;
	Fixture fixtu;
	ImageView ivband1,ivband2;
	EditText etgol1,etgol2;
	View view;
	int gol1,gol2,d,m,dp,mp;
	Partido partido;
	
	public DialogoEditarPartido(Context contexto,Partido partido, Fixture fixtu){
		this.miContexto=contexto;
		this.partido=partido;
		this.fixtu=fixtu;
	}

	public Dialog onCreateDialog(Bundle savedInstaceState){
		
		AlertDialog.Builder dialogo=new AlertDialog.Builder(miContexto);
		LayoutInflater infla=getActivity().getLayoutInflater();
		
		dialogo.setIcon(getResources().getDrawable(R.drawable.fuleco));
		
		view=infla.inflate(R.layout.modeloeditarpartido, null);
		
		etgol1=(EditText)view.findViewById(R.id.etGol1ep);
		etgol2=(EditText)view.findViewById(R.id.etGol2ep);
		TextView tvp1=(TextView)view.findViewById(R.id.tvPais1ep);
		tvp1.setText(partido.getPais1());
		TextView tvp2=(TextView)view.findViewById(R.id.tvPais2ep);
		tvp2.setText(partido.getPais2());
		
		ImageView ivband1=(ImageView)view.findViewById(R.id.ivBand1ep);
		ivband1.setImageResource(getImageBandera(partido.getPais1()));
		
		ImageView ivband2=(ImageView)view.findViewById(R.id.ivBand2ep);
		ivband2.setImageResource(getImageBandera(partido.getPais2()));
		
		VerificarFechaPartido verif=new VerificarFechaPartido(partido.getFecha(), partido.getHora());
		//Alterando aca para poder hacer pruebas con /*
		if(verif.verficar()){
			if(verif.verificarHoraPartido()){
				dialogo.setTitle("Resultado partido");
				dialogo.setView(view);
				dialogo.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						gol1=Integer.valueOf(etgol1.getText().toString());
						gol2=Integer.valueOf(etgol2.getText().toString());
						updatePartido(partido.getIdpartido(),partido.getPais1(),partido.getPais2(), gol1, gol2);
						fixtu.adaptarListViewPartidosFixtu(partido.getGrupo());
						fixtu.adaptarListViewPosicionesFixtu(partido.getGrupo());
					}
				});
				
				dialogo.setNegativeButton(android.R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();				
					}
				});
			}else{
				dialogo.setTitle("Brasil 2014");
				dialogo.setMessage("Hola "+getUsuario()+","+verif.horasParaPartido());
				dialogo.setNeutralButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				});
			}
		}else{
			dialogo.setTitle("Brasil 2014");
			dialogo.setMessage("Hola "+getUsuario()+", falta "+verif.diasParaPartido()+" dia(s) para este partido");
			dialogo.setNeutralButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.cancel();
				}
			});
		}
		
		return dialogo.create();
		
	}
	
	public int getImageBandera(String pais){
		int imagen=0;
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			imagen=db.getImagenBandera(pais);
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "cargar lista");
		}
		return imagen;
	}
	
	public void updatePartido(int id,String p1, String p2,int gol1, int gol2){
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			db.actualizarPartido(id, p1, p2, gol1, gol2);
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "actualizar partido");
		}
	}
	
	public String getUsuario(){
		String nombre="";
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			nombre=db.getNombre();
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "obtener usuario");
		}
		return nombre;
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(miContexto);
		mensaje.setTitle("Error al ");
		TextView texto=new TextView(miContexto);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
	}
}
