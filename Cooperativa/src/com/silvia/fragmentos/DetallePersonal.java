package com.silvia.fragmentos;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogNuevoUsuario;
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetallePersonal extends Fragment{
	
	private String idpersonal;
	private Personal personal;
	private int proviene;
	
	private ImageView ivImagenPersonal, ivImagenMaq;
	private TextView tvCI, tvNombre, tvApellido, tvDireccion, tvTelf, tvEmail, tvFechaNac, tvFechaIngreso, tvCargo, tvMaq, tvPlacaMaq, tvDescripcionMaq;
	private LinearLayout lyVistaMaq;
	
	OnDetallePersonalClickListener listener;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.detalle_personal, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(new StringBuilder("Datos personal"));
		Bundle bundle = getArguments();
		this.idpersonal = bundle.getString("idpersonal");
		this.proviene = bundle.getInt("proviene");
		cargarPersonal();
		ivImagenPersonal = (ImageView)v.findViewById(R.id.ivImagenDetallePersonal);
		tvCI = (TextView)v.findViewById(R.id.tvCIDetallePersonal);
		tvNombre = (TextView)v.findViewById(R.id.tvNombreDetallePersonal);
		tvApellido = (TextView)v.findViewById(R.id.tvApellidoDetallePersonal);
		tvDireccion = (TextView)v.findViewById(R.id.tvDireccionDetallePersonal);
		tvTelf = (TextView)v.findViewById(R.id.tvTelfDetallePersonal);
		tvEmail = (TextView)v.findViewById(R.id.tvEmailDetallePersonal);
		tvFechaNac = (TextView)v.findViewById(R.id.tvFechaNacDetallaPersonal);
		tvFechaIngreso = (TextView)v.findViewById(R.id.tvFechaIngresoDetallePersonal);
		tvCargo = (TextView)v.findViewById(R.id.tvCargoDetallePersonal);
		tvMaq = (TextView)v.findViewById(R.id.tvMAQDetallePersonal);
		lyVistaMaq = (LinearLayout)v.findViewById(R.id.lyVistaMaqDetallePersonal);
		ivImagenMaq = (ImageView)v.findViewById(R.id.ivImagenMaqDetallePersonal);
		tvPlacaMaq = (TextView)v.findViewById(R.id.tvPlacaMaqDetallePersonal);
		tvDescripcionMaq = (TextView)v.findViewById(R.id.tvDercripcionMaqDetallePersonal);
		cargarMostrarDatosPersonal();
	}

	private void cargarMostrarDatosPersonal() {
		if(!personal.getImagen().equals(Variables.SIN_ESPECIFICAR)){
			String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(personal.getImagen()).toString();
			Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
			if(bitmap!=null){
				ivImagenPersonal.setImageBitmap(bitmap);
				ivImagenPersonal.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivImagenPersonal.setPadding(0, 0, 0, 0);
			}else{
				ivImagenPersonal.setImageResource(R.drawable.ic_person_white_48dp);
			}
		}else{
			ivImagenPersonal.setImageResource(R.drawable.ic_person_white_48dp);
		}
		tvCI.setText(personal.getCi());
		tvNombre.setText(personal.getNombre());
		tvApellido.setText(personal.getApellido());
		tvDireccion.setText(personal.getDireccion());
		if(personal.getTelf()==0){
			tvTelf.setText(Variables.SIN_ESPECIFICAR);
		}else{
			tvTelf.setText(String.valueOf(personal.getTelf()));
		}
		tvEmail.setText(personal.getEmail());
		if(personal.getFecha_nac().equals(Variables.FECHA_DEFAULT)){
			tvFechaNac.setText(Variables.SIN_ESPECIFICAR);
		}else{
			tvFechaNac.setText(Variables.FORMAT_FECHA_1.format(personal.getFecha_nac()));
		}
		tvFechaIngreso.setText(Variables.FORMAT_FECHA_1.format(personal.getFecha_ingreso()));
		tvCargo.setText(getCargo(personal.getIdcargo()).getOcupacion());
		if(personal.getIdmaquinaria().equals(Variables.ID_MAQ_DEFAULT)){
			tvMaq.setText("NO");
		}else{
			tvMaq.setVisibility(View.GONE);
			lyVistaMaq.setVisibility(View.VISIBLE);
			Maquinaria maq = getMaquinaria(personal.getIdmaquinaria());
			tvPlacaMaq.setText("Placa: "+maq.getPlaca());
			tvDescripcionMaq.setText(maq.getDescripcion());
			if(!maq.getImagen().equals(Variables.SIN_ESPECIFICAR)){
				String pathImagen = new StringBuilder(Variables.FOLDER_IMAGES_COOPERATIVA).append(maq.getImagen()).toString();
				Bitmap bitmap = BitmapFactory.decodeFile(pathImagen);
				if(bitmap!=null){
					ivImagenMaq.setImageBitmap(bitmap);
					ivImagenMaq.setScaleType(ImageView.ScaleType.CENTER_CROP);
					ivImagenMaq.setPadding(0, 0, 0, 0);
				}else{
					ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
				}
			}else{
				ivImagenMaq.setImageResource(R.drawable.ic_local_shipping_white_48dp);
			}
		}
	}

	private void cargarPersonal() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			personal = db.getPersonal(idpersonal);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cargo getCargo(String idcargo){
		Cargo cargo = null;
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			cargo = db.getCargo(idcargo);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cargo;
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_editar_personal).setVisible(true);
		menu.findItem(R.id.action_listo_personal).setVisible(true);
		if(!isUser(personal)){
			menu.findItem(R.id.action_eliminar_personal).setVisible(true);
		}
		if(proviene==Variables.ACCION_CARGAR_PERFIL){
			menu.findItem(R.id.action_config_clave).setVisible(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_editar_personal){
			listener.onEditarDetallePersonalClick(this.personal.getIdpersonal());
		}else if(item.getItemId()==R.id.action_eliminar_personal){
			confirmarEliminarPersonal();
		}else if (item.getItemId()==R.id.action_config_clave) {
			//Toast.makeText(getActivity(), "Cambiar clave", Toast.LENGTH_LONG).show();
			DialogNuevoUsuario nuevoUser = new DialogNuevoUsuario(personal, Variables.ACCION_EDITAR_USUARIO);
			nuevoUser.show(getFragmentManager(), "tagEU");
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void confirmarEliminarPersonal() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Eliminar?");
		dialog.setMessage("Recuerde que una vez elimine personal no podra restaurarlo");
		dialog.setPositiveButton(R.string.aceptar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.eliminarPersonal(personal)){
						Toast.makeText(getActivity(), "Personal eliminado", Toast.LENGTH_SHORT).show();
						listener.onBackFromDetallePersonalClick();
						dialog.dismiss();
						
					}else{
						Toast.makeText(getActivity(), "No se pudo eliminar personal, intente mas tarde..!", Toast.LENGTH_SHORT).show();
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

	public boolean isUser(Personal personal){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.esPersonalUsuario(personal)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public interface OnDetallePersonalClickListener{
		public void onEditarDetallePersonalClick(String idpersonal);
		public void onBackFromDetallePersonalClick();
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnDetallePersonalClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnDetallePersonalClickListener");
		}
	}

}
