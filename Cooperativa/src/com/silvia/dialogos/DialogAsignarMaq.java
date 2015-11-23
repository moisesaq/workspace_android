package com.silvia.dialogos;

import java.util.List;

import com.silvia.adapters.ListaMaqAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Maquinaria;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class DialogAsignarMaq extends DialogFragment implements OnItemClickListener{

	ListView lvListaMaq;
	View v;
	
	ImageView ivImagenMaq;
	TextView tvPlacaMaq, tvDescripcionMaq;
	
	public DialogAsignarMaq(ImageView ivImagenMaq, TextView tvPlacaMaq, TextView tvDescripcionMaq){
		this.ivImagenMaq = ivImagenMaq;
		this.tvPlacaMaq = tvPlacaMaq;
		this.tvDescripcionMaq = tvDescripcionMaq;
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
		dialog.setTitle(new StringBuilder("Seleccione maquinaria"));
		v = getActivity().getLayoutInflater().inflate(R.layout.lista_maquinaria, null);
		lvListaMaq = (ListView)v.findViewById(R.id.lvListaMaquinaria);
		lvListaMaq.setOnItemClickListener(this);
		cargarListaMaquinarias();
		dialog.setView(v);
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int wich) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}
	
	private void cargarListaMaquinarias(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Maquinaria> lista = db.getTodosLasMaquinarias();
			//ArrayAdapter<Maquinaria> adapter = new ArrayAdapter<Maquinaria>(getActivity(), android.R.layout.simple_list_item_1, lista);
			ListaMaqAdapter adapter = new ListaMaqAdapter(getActivity(), lista);
			lvListaMaq.setAdapter(adapter);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Maquinaria maq = (Maquinaria)adapter.getAdapter().getItem(position);
		tvPlacaMaq.setTag(maq.getIdmaquinaria());
		tvPlacaMaq.setText("Placa: "+maq.getPlaca());
		tvDescripcionMaq.setVisibility(View.VISIBLE);
		tvDescripcionMaq.setText(new StringBuilder(maq.getDescripcion()).append(" de ").append(maq.getCapacidad()).append(" cubos de capacidad"));
		if(!maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
			if(bitmap!=null){
				ivImagenMaq.setImageBitmap(bitmap);
				ivImagenMaq.setPadding(0, 0, 0, 0);
			}else{
				ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
		}else{
			ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
		}
		this.dismiss();
	}
}
