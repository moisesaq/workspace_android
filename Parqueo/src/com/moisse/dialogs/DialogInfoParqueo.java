package com.moisse.dialogs;

import com.example.parqueo.R;
import com.moisse.modelo.Parqueo;
import com.moisse.modelo.Usuario;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogInfoParqueo extends DialogFragment implements OnClickListener{

	OnEditarParqueoClickListener  listener;
	private Parqueo parq_online;
	private Usuario user_online;
	private View v;
	private LinearLayout lyPrecioContratoNocturno, lyPrecioContratoDiurno, lyPrecioContratoDiaCompleto;
	private Button btnEditar, btnOk;
	
	public DialogInfoParqueo(Parqueo parq_online, Usuario user_online){
		this.parq_online = parq_online;
		this.user_online = user_online;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Parqueo ").append(parq_online.getNombreParqueo()));
		v = getActivity().getLayoutInflater().inflate(R.layout.informacion_parqueo, null);
		ImageView logo = (ImageView)v.findViewById(R.id.ivImageInfoParqueo);
		if(!parq_online.getLogo().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap myBitmap = BitmapFactory.decodeFile(new StringBuilder(MyVar.FOLDER_IMAGES_PARQUEO).append(parq_online.getLogo()).toString());
			if(myBitmap!=null){
				logo.setImageBitmap(myBitmap);
			}else{
				logo.setImageResource(R.drawable.ic_local_parking_white_48dp);
			}
		}else{
			logo.setImageResource(R.drawable.ic_local_parking_white_48dp);
		}
		TextView nom_parqueo = (TextView)v.findViewById(R.id.tvNombreParqueoInfoParqueo);
		nom_parqueo.setText(new StringBuilder(parq_online.getNombreParqueo()));
		TextView telf = (TextView)v.findViewById(R.id.tvTelfInfoParqueo);
		if(parq_online.getTelf()!=0){
			telf.setText(new StringBuilder().append(parq_online.getTelf()));
		}else{
			telf.setText(new StringBuilder(MyVar.NO_ESPECIFICADO));
		}
		TextView direccion = (TextView)v.findViewById(R.id.tvDireccionInfoParqueo);
		direccion.setText(new StringBuilder(parq_online.getDireccion()));
		
		TextView tolerancia = (TextView)v.findViewById(R.id.tvToleranciaInfoParqueo);
		tolerancia.setText(new StringBuilder().append(parq_online.getTolerancia()).append(" Min."));
		TextView capacidad = (TextView)v.findViewById(R.id.tvCapacidadInfoParqueo);
		capacidad.setText(new StringBuilder().append(parq_online.getCapacidad()));
		
		TextView precioDia = (TextView)v.findViewById(R.id.tvPrecioDiaInfoParqueo);
		precioDia.setText(new StringBuilder().append(parq_online.getPrecioHoraDia()).append(" Bs."));
		TextView precioNoche = (TextView)v.findViewById(R.id.tvPrecioNocheInfoParqueo);
		precioNoche.setText(new StringBuilder().append(parq_online.getPrecioNoche()).append(" Bs."));
		
		double precio_nocturno = parq_online.getPrecioContratoNocturno();
		if(precio_nocturno!=0){
			TextView contrato_nocturno = (TextView)v.findViewById(R.id.tvContratoNocturnoInfoParqueo);
			lyPrecioContratoNocturno = (LinearLayout)v.findViewById(R.id.lyVistaContratoNocturnoInfoParqueo);
			TextView precio_contrato_nocturno = (TextView)v.findViewById(R.id.tvPrecioContratoNocturnoInfoParqueo);
			contrato_nocturno.setText(new StringBuilder("SI"));
			lyPrecioContratoNocturno.setVisibility(View.VISIBLE);
			precio_contrato_nocturno.setText(new StringBuilder().append(precio_nocturno).append(" Bs."));
		}
		
		double precio_diurno = parq_online.getPrecioContratoDiurno();
		if(precio_diurno!=0){
			TextView contrato_diurno = (TextView)v.findViewById(R.id.tvContratoDiurnoInfoParqueo);
			lyPrecioContratoDiurno = (LinearLayout)v.findViewById(R.id.lyVistaContratoDiurnoInfoParqueo);
			TextView precio_contrato_diurno = (TextView)v.findViewById(R.id.tvPrecioContratoDiurnoInfoParqueo);
			contrato_diurno.setText(new StringBuilder("SI"));
			lyPrecioContratoDiurno.setVisibility(View.VISIBLE);
			precio_contrato_diurno.setText(new StringBuilder().append(precio_diurno).append(" Bs."));
		}
		
		double precio_dia_completo = parq_online.getPrecioContratoDiaCompleto();
		if(precio_dia_completo!=0){
			TextView contrato_dia_completo = (TextView)v.findViewById(R.id.tvContratoDiaCompletoInfoParqueo);
			lyPrecioContratoDiaCompleto = (LinearLayout)v.findViewById(R.id.lyVistaContratoDiaCompletoInfoParqueo);
			TextView precio_contrato_dia_completo = (TextView)v.findViewById(R.id.tvPrecioContratoDiaCompletoInfoParqueo);
			contrato_dia_completo.setText(new StringBuilder("SI"));
			lyPrecioContratoDiaCompleto.setVisibility(View.VISIBLE);
			precio_contrato_dia_completo.setText(new StringBuilder().append(parq_online.getPrecioContratoDiaCompleto()).append(" Bs."));
		}
		
		TextView inicio_dia = (TextView)v.findViewById(R.id.tvInicioDiaInfoParqueo);
		inicio_dia.setText(new StringBuilder(parq_online.getInicioDia().toString()));
		TextView fin_dia = (TextView)v.findViewById(R.id.tvFinDiaInfoParqueo);
		fin_dia.setText(new StringBuilder(parq_online.getFinDia().toString()));
		
		TextView inicio_noche = (TextView)v.findViewById(R.id.tvInicioNocheInfoParqueo);
		inicio_noche.setText(new StringBuilder(parq_online.getInicioNoche().toString()));
		TextView fin_noche = (TextView)v.findViewById(R.id.tvFinNocheInfoParqueo);
		fin_noche.setText(new StringBuilder(parq_online.getFinNoche().toString()));
		
		btnEditar = (Button)v.findViewById(R.id.btnEditarInfoParqueo);
		btnEditar.setOnClickListener(this);
		btnOk = (Button)v.findViewById(R.id.btnOKInfoParqueo);
		btnOk.setOnClickListener(this);
		
		if(user_online.getCargo()!=MyVar.CARGO_ADMIN){
			btnEditar.setVisibility(View.GONE);
		}
		
		dialog.setView(v);
		return dialog.create();
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==btnEditar.getId()){
			this.dismiss();
			listener.onEditarParqueoClick(parq_online.getIdparqueo());
		}else if(v.getId()==btnOk.getId()){
			this.dismiss();
		}
	}
	
	public interface OnEditarParqueoClickListener{
		public void onEditarParqueoClick(String idparqueo);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnEditarParqueoClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnEditarParqueoClickListener");
		}
	}
	
	
}
