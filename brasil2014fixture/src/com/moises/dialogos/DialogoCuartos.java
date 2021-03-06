package com.moises.dialogos;

import com.example.brasil2014fixture.Cuartos;
import com.example.brasil2014fixture.R;
import com.moises.data.base.fixture.DBFixture;
import com.moises.extras.VerificarFechaPartido;
import com.moises.modelo.PartidoCuartos;

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
public class DialogoCuartos extends DialogFragment{

	Context miContexto;
	PartidoCuartos partidoC;
	Cuartos cuartos;
	View vista;
	EditText etgol1,etgol2;
	int gol1,gol2;
	
	public DialogoCuartos(Context contexto, PartidoCuartos partido, Cuartos cuartos){
		this.miContexto=contexto;
		this.partidoC=partido;
		this.cuartos=cuartos;
	}
	
	public Dialog onCreateDialog(Bundle savedIntanceState){
		AlertDialog.Builder dialogo=new AlertDialog.Builder(miContexto);
		
		LayoutInflater infla=getActivity().getLayoutInflater();
		
		vista=infla.inflate(R.layout.modeloeditarcuartos, null);
		dialogo.setIcon(getResources().getDrawable(R.drawable.fuleco));
		etgol1=(EditText)vista.findViewById(R.id.etGol1C);
		etgol2=(EditText)vista.findViewById(R.id.etGol2C);
		
		TextView tvp1=(TextView)vista.findViewById(R.id.tvPais1C);
		tvp1.setText(partidoC.getPaisganador1());
		TextView tvp2=(TextView)vista.findViewById(R.id.tvPais2C);
		tvp2.setText(partidoC.getPaisganador2());
		
		ImageView ivband1=(ImageView)vista.findViewById(R.id.ivBand1C);
		ivband1.setImageResource(partidoC.getBandera1());
		
		ImageView ivband2=(ImageView)vista.findViewById(R.id.ivBand2C);
		ivband2.setImageResource(partidoC.getBandera2());
		
		VerificarFechaPartido v=new VerificarFechaPartido(partidoC.getFecha(), partidoC.getHora());
		
		if(v.verficar()){
			if(v.verificarHoraPartido()){
				dialogo.setTitle("Resultado cuartos");
				dialogo.setView(vista);
				dialogo.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						gol1=Integer.valueOf(etgol1.getText().toString());
						gol2=Integer.valueOf(etgol2.getText().toString());
						updatePartidoCuartos(partidoC.getIdpartidocuartos(), gol1, gol2);
						cuartos.adaptarListViewPartidosCuartos();
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
	
	public void updatePartidoCuartos(String idcuartos, int gol1, int gol2){
		DBFixture db=new DBFixture(miContexto);
		try {
			db.AbrirDB();
			db.actualizarPartidosCuartosGoles(idcuartos, gol1, gol2);
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
