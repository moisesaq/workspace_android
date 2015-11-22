package com.moises.dialogos;

import com.example.brasil2014fixture.Octavos;
import com.example.brasil2014fixture.R;
import com.moises.data.base.fixture.DBFixture;
import com.moises.extras.VerificarFechaPartido;
import com.moises.modelo.PartidoOctavos;

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

@SuppressLint("ValidFragment")
public class DialogoOctavos extends DialogFragment{

	Context miContexto;
	PartidoOctavos partido;
	Octavos octavos;
	View vista;
	EditText tvgol1,tvgol2;
	int gol1,gol2;
	public DialogoOctavos(Context contexto, PartidoOctavos partido, Octavos octavos){
		this.miContexto=contexto;
		this.partido=partido;
		this.octavos=octavos;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder dialog=new AlertDialog.Builder(miContexto);
		
		
		
		LayoutInflater infla=getActivity().getLayoutInflater();
		vista=infla.inflate(R.layout.modeloeditaroctavos,null);
		
		dialog.setIcon(getResources().getDrawable(R.drawable.fuleco));
		tvgol1=(EditText)vista.findViewById(R.id.etGol1O);
		tvgol2=(EditText)vista.findViewById(R.id.etGol2O);
		
		
		
		TextView tvp1=(TextView)vista.findViewById(R.id.tvPais1O);
		tvp1.setText(partido.getPais1());
		TextView tvp2=(TextView)vista.findViewById(R.id.tvPais2O);
		tvp2.setText(partido.getPais2());
		
		ImageView ivband1=(ImageView)vista.findViewById(R.id.ivBand1O);
		ivband1.setImageResource(getImageBandera(partido.getIdoctavos1()));
		
		ImageView ivband2=(ImageView)vista.findViewById(R.id.ivBand2O);
		ivband2.setImageResource(getImageBandera(partido.getIdoctavos2()));
		
		VerificarFechaPartido v=new VerificarFechaPartido(partido.getFecha(), partido.getHora());
		//Alterando aca para poder hacer pruebas con !
		if(v.verficar()){
			if(v.verificarHoraPartido()){
				dialog.setView(vista);
				dialog.setTitle("Resultado octavos");
				dialog.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						gol1=Integer.valueOf(tvgol1.getText().toString());
						gol2=Integer.valueOf(tvgol2.getText().toString());
						updatePartidoOctavos(partido.getIdpartidooctavos(), gol1, gol2);
						octavos.adaptarListViewPartidosOctavos();
					}
				});
				
				dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			}else{
				dialog.setTitle("Brasil 2014");
				dialog.setMessage("Hola "+getUsuario()+","+v.horasParaPartido());
				dialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				});
			}
		}else{
			dialog.setTitle("Brasil 2014");
			dialog.setMessage("Hola "+getUsuario()+", falta "+v.diasParaPartido()+" dia(s) para este partido");
			dialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.cancel();
				}
			});
		}
		
		return dialog.create();
	}
	
	public int getImageBandera(String idoctavos){
		int imagen=0;
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			imagen=db.buscarBanderaPaisOctavos(idoctavos);
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, " obtener bandera");
		}
		return imagen;
	}
	
	public void updatePartidoOctavos(String idoctavos, int gol1, int gol2){
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			db.actualizarPartidosOctavosGoles(idoctavos, gol1, gol2);
			db.cerrarDB();
		} catch (Exception e) {
			String error=e.toString();
			mensajeError(error, "actualizar partido");
		}
	}
	
	public void mensajeError(String error, String titulo){
		Dialog mensaje=new Dialog(miContexto);
		mensaje.setTitle("Error al ");
		TextView texto=new TextView(miContexto);
		texto.setText(error);
		mensaje.setContentView(texto);
		mensaje.show();
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
}
