package com.silvia.dialogos;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.ListaMaquinaria;
import com.silvia.modelo.Maquinaria;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogDetalleMaquinaria extends DialogFragment implements OnClickListener{

	private Maquinaria maq;
	private ListaMaquinaria lista_maq;
	private Button btnEliminar, btnEditar, btnOk;
	private ImageView ivMaquinaria;
	private TextView tvPlaca, tvDescripcion, tvCapacidad, tvMarca, tvColor;
	private View v;
	private OnEditarMaquinariaClickListener listener;
	
	public DialogDetalleMaquinaria(Maquinaria maq, ListaMaquinaria lista_maquinaria){
		this.maq = maq;
		this.lista_maq = lista_maquinaria;
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
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("Datos maquinaria");
		v = getActivity().getLayoutInflater().inflate(R.layout.detalle_maquinaria, null);
		ivMaquinaria = (ImageView)v.findViewById(R.id.ivImagenDetalleMaquinaria);
		tvPlaca = (TextView)v.findViewById(R.id.tvPlacaDetalleMaquinaria);
		tvDescripcion = (TextView)v.findViewById(R.id.tvDescripcionDetalleMaquinaria);
		tvCapacidad = (TextView)v.findViewById(R.id.tvCapacidadDetalleMaquinaria);
		tvMarca = (TextView)v.findViewById(R.id.tvMarcaDetalleMaquinaria);
		tvColor = (TextView)v.findViewById(R.id.tvColorDetalleMaquinaria);
		btnEliminar = (Button)v.findViewById(R.id.btnEliminarDetalleMaquinaria);
		btnEliminar.setOnClickListener(this);
		btnEditar = (Button)v.findViewById(R.id.btnEditarDetalleMaquinaria);
		btnEditar.setOnClickListener(this);
		btnOk = (Button)v.findViewById(R.id.btnOkDetalleMaquinaria);
		btnOk.setOnClickListener(this);
		mostrarDatosMaquinaria();
		dialog.setView(v);
		return dialog.create();
	}
	
	public void mostrarDatosMaquinaria(){
		if(!maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString();
			Bitmap bitmap_imagen = BitmapFactory.decodeFile(pathImagen);
			if(bitmap_imagen!=null){
				ivMaquinaria.setImageBitmap(bitmap_imagen);
				ivMaquinaria.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivMaquinaria.setPadding(0, 0, 0, 0);
			}else{
				ivMaquinaria.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
		}else{
			ivMaquinaria.setImageResource(R.drawable.ic_local_shipping_white_48dp);
		}
		tvPlaca.setText(maq.getPlaca());
		tvDescripcion.setText(maq.getDescripcion());
		tvCapacidad.setText(new StringBuilder().append(maq.getCapacidad()).append(" cubos"));
		tvMarca.setText(maq.getMarca());
		tvColor.setText(maq.getColor());
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==btnEliminar.getId()){
			confimarEliminarMaquinaria();
		}else if(v.getId()==btnEditar.getId()){
			listener.onEditarMaquinariaClick(maq.getIdmaquinaria());
			dismiss();
		}else if(v.getId()==btnOk.getId()){
			dismiss();
		}
		
	}
	
	public void confimarEliminarMaquinaria(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Eliminar?");
		dialog.setMessage("Recuerde que una vez elimine maquinaria no podra restaurarlo");
		dialog.setPositiveButton(R.string.aceptar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.eliminarMaquinaria(maq)){
						Toast.makeText(getActivity(), "Maquinaria eliminado", Toast.LENGTH_SHORT).show();
						lista_maq.cargarMostrarListaMaquinaria();
						DialogDetalleMaquinaria.this.dismiss();
						dialog.dismiss();
						
					}else{
						Toast.makeText(getActivity(), "No se pudo eliminar maquinaria, intente mas tarde..!", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		dialog.setNeutralButton(R.string.cancelar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog d = dialog.show();
		int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(getResources().getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	public interface OnEditarMaquinariaClickListener{
		public void onEditarMaquinariaClick(String idmaquinaria);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (OnEditarMaquinariaClickListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+" debe implementar OnMostrarMaquinariaClickListener");
		}
	}	
}
