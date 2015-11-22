package com.moises.dialogos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brasil2014fixture.Cuartos;
import com.example.brasil2014fixture.Finales;
import com.example.brasil2014fixture.R;
import com.moises.data.base.fixture.DBFixture;
import com.moises.extras.VerificarFechaPartido;
import com.moises.modelo.PartidoCuartos;
import com.moises.modelo.PartidoFinales;

@SuppressLint("ValidFragment")
public class DialogoFinal extends DialogFragment{

	Context miContexto;
	PartidoFinales partidoF;
	Finales finales;
	View vista;
	EditText etgol1,etgol2;
	int gol1,gol2;
	
	public DialogoFinal(Context contexto, PartidoFinales partido, Finales finales){
		this.miContexto=contexto;
		this.partidoF=partido;
		this.finales=finales;
	}
	
	public Dialog onCreateDialog(Bundle savedIntanceState){
		AlertDialog.Builder dialogo=new AlertDialog.Builder(miContexto);
		
		LayoutInflater infla=getActivity().getLayoutInflater();
		
		vista=infla.inflate(R.layout.modeloeditarfinal, null);
		dialogo.setIcon(getResources().getDrawable(R.drawable.fuleco));
		etgol1=(EditText)vista.findViewById(R.id.etGol1F);
		etgol2=(EditText)vista.findViewById(R.id.etGol2F);
		
		TextView tvp1=(TextView)vista.findViewById(R.id.tvPais1F);
		tvp1.setText(partidoF.getPaisganador1());
		TextView tvp2=(TextView)vista.findViewById(R.id.tvPais2F);
		tvp2.setText(partidoF.getPaisganador2());
		
		ImageView ivband1=(ImageView)vista.findViewById(R.id.ivBand1F);
		ivband1.setImageResource(partidoF.getBandera1());
		
		ImageView ivband2=(ImageView)vista.findViewById(R.id.ivBand2F);
		ivband2.setImageResource(partidoF.getBandera2());
		
		VerificarFechaPartido v=new VerificarFechaPartido(partidoF.getFecha(), partidoF.getHora());
		
		if(v.verficar()){
			if(v.verificarHoraPartido()){
				dialogo.setTitle("Resultado cuartos");
				dialogo.setView(vista);
				dialogo.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						gol1=Integer.valueOf(etgol1.getText().toString());
						gol2=Integer.valueOf(etgol2.getText().toString());
						updatePartidoFinal(partidoF.getIdpartidofinales(), gol1, gol2);
						finales.adaptarListViewPartidosFinales();
						
						
						if(partidoF.getIdpartidofinales().equals("FG") && gol1>gol2){
							Intent inte=new Intent("android.intent.action.Campeon");
							inte.putExtra("bandera", partidoF.getBandera1());
							inte.putExtra("pais", partidoF.getPaisganador1());
							startActivity(inte);
						}else if (partidoF.getIdpartidofinales().equals("FG") && gol2>gol1) {
							Intent inte=new Intent("android.intent.action.Campeon");
							inte.putExtra("bandera", partidoF.getBandera2());
							inte.putExtra("pais", partidoF.getPaisganador2());
							startActivity(inte);
						}
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
				dialogo.setMessage("Hola "+getUsuario()+","+v.horasParaPartido());
				dialogo.setNeutralButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				});
			}
			
		}else{
			dialogo.setTitle("Brasil 2014");
			dialogo.setMessage("Hola "+getUsuario()+", falta "+v.diasParaPartido()+" dia(s) para este partido");
			dialogo.setNeutralButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.cancel();
				}
			});
		}
		return dialogo.create();
	}
	
	public void updatePartidoFinal(String idfinal, int gol1, int gol2){
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			db.actualizarFinalesGoles(idfinal, gol1, gol2);
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
