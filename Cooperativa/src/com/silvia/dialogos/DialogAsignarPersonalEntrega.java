package com.silvia.dialogos;

import java.util.List;

import com.silvia.adapters.ListaPersonalAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogAsignarPersonalEntrega extends DialogFragment implements OnItemClickListener{

	ListView lvListaPersonal;
	public TextView tvAviso, tvCIPersonal, tvNombrePersonal, tvPlacaMaq, tvCapacidadMaq;
	public ImageView ivImagePersonal, ivImageMaq;
	public LinearLayout lyVistaMaq;
	public View v;
	
	public DialogAsignarPersonalEntrega(ImageView ivImagePersonal, TextView tvCIPersonal, TextView tvNombrePersonal, LinearLayout lyVistaMaq,
											ImageView ivImageMaq, TextView tvPlacaMaq, TextView tvCapacidadMaq){
		this.ivImagePersonal = ivImagePersonal;
		this.tvCIPersonal = tvCIPersonal;
		this.tvNombrePersonal = tvNombrePersonal;
		this.lyVistaMaq = lyVistaMaq;
		this.ivImageMaq = ivImageMaq;
		this.tvPlacaMaq = tvPlacaMaq;
		this.tvCapacidadMaq = tvCapacidadMaq;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDivideId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDivideId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Seleccione personal"));
		v = getActivity().getLayoutInflater().inflate(R.layout.lista_personal, null);
		lvListaPersonal = (ListView)v.findViewById(R.id.lvListaPersonal);
		lvListaPersonal.setOnItemClickListener(this);
		tvAviso = (TextView)v.findViewById(R.id.tvNotaListaPersonal);
		cargarListaPersonalConMaquinaria();
		dialog.setView(v);
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int wich) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}
	
	private void cargarListaPersonalConMaquinaria(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Personal> lista = db.getTodosLosPersonalesConMaquinaria();
			if(lista.size()!=0){
				ListaPersonalAdapter adapter = new ListaPersonalAdapter(getActivity(), lista);
				lvListaPersonal.setAdapter(adapter);
			}else{
				lvListaPersonal.setVisibility(View.INVISIBLE);
				tvAviso.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No se encontraro ningun personal con maquinaria", Toast.LENGTH_LONG).show();
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Personal personal = (Personal)adapter.getAdapter().getItem(position);
		Maquinaria maq = getMaquinaria(personal.getIdmaquinaria());
				
		tvCIPersonal.setText(new StringBuilder("CI: ").append(personal.getCi()));
		tvCIPersonal.setVisibility(View.VISIBLE);
		tvNombrePersonal.setTag(personal);
		if(personal.getApellido().equals(Variables.SIN_ESPECIFICAR)){
			tvNombrePersonal.setText(new StringBuilder("Nombre: ").append(personal.getNombre()));
		}else{
			tvNombrePersonal.setText(new StringBuilder("Nombre: ").append(personal.getNombre()).append(" ").append(personal.getApellido()));
		}
		if(!personal.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagenPersonal = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(personal.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagenPersonal);
			if(bitmap!=null){
				ivImagePersonal.setImageBitmap(bitmap);
				ivImagePersonal.setPadding(0, 0, 0, 0);
			}else{
				ivImagePersonal.setImageResource(R.drawable.ic_employees_128);
			}
		}else{
			ivImagePersonal.setImageResource(R.drawable.ic_employees_128);
		}
		lyVistaMaq.setVisibility(View.VISIBLE);
		tvPlacaMaq.setText(new StringBuilder("Placa: ").append(maq.getPlaca()));
		tvCapacidadMaq.setText(new StringBuilder("Cap.: ").append(maq.getCapacidad()).append(" cubos"));
		if(!maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagenMaq = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagenMaq);
			if(bitmap!=null){
				ivImageMaq.setImageBitmap(bitmap);
				ivImageMaq.setPadding(0, 0, 0, 0);
			}else{
				ivImageMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
		}else{
			ivImageMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
		}
		this.dismiss();
	}
	
	public Maquinaria getMaquinaria(String idmaquinaria){
		Maquinaria maq = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			maq = db.getMaquinaria(idmaquinaria);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maq;
	}
}
