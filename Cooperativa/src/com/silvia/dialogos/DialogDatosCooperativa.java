package com.silvia.dialogos;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Cooperativa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogDatosCooperativa extends DialogFragment implements OnClickListener{

	public View v;
	public TextView tvNombre, tvNIT, tvTelf, tvEmail, tvDir, tvCiudad;
	public ImageButton ibtnEditarNombre, ibtnEditarNIT, ibtnEditarTelf, ibtnEditarEmail, ibtnEditarDir, ibtnEditarCiudad;
	public LinearLayout lyEditarNombre, lyEditarNIT, lyEditarTelf, lyEditarEmail, lyEditarDir, lyEditarCiudad;
	public EditText etNombre, etNIT, etTelf, etEmail, etDir, etCiudad;
	public ImageButton ibtnOKEditarNombre, ibtnOKEditarNIT, ibtnOKEditarTelf, ibtnOKEditarEmail, ibtnOKEditarDir, ibtnOKEditarCiudad;
	public Cooperativa cooperativa;
	
	public DialogDatosCooperativa(){
		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		final Resources res = getResources();
		Dialog d = getDialog();
		int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
		View titleDivider = d.findViewById(titleDividerId);
		if(titleDivider!=null){
			titleDivider.setBackgroundColor(res.getColor(R.color.AMARILLO_GOLD));
		}
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(lp);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Datos Cooperativa"));
		v = getActivity().getLayoutInflater().inflate(R.layout.datos_cooperativa, null);
		tvNombre = (TextView) v.findViewById(R.id.tvNombreDatosCoop);
		ibtnEditarNombre = (ImageButton)v.findViewById(R.id.ibtnEditarNombreDatosCoop);
		ibtnEditarNombre.setOnClickListener(this);
		lyEditarNombre = (LinearLayout)v.findViewById(R.id.lyEditarNombreDatosCoop);
		etNombre = (EditText)v.findViewById(R.id.etNombreDatosCoop);
		ibtnOKEditarNombre = (ImageButton)v.findViewById(R.id.ibtnOKEditarNombreDatosCoop);
		ibtnOKEditarNombre.setOnClickListener(this);
		
		tvNIT = (TextView)v.findViewById(R.id.tvNITDatosCoop);
		ibtnEditarNIT = (ImageButton)v.findViewById(R.id.ibtnEditarNITDatosCoop);
		ibtnEditarNIT.setOnClickListener(this);
		lyEditarNIT = (LinearLayout)v.findViewById(R.id.lyEditarNITDatosCoop);
		etNIT = (EditText)v.findViewById(R.id.etNITDatosCoop);
		ibtnOKEditarNIT = (ImageButton)v.findViewById(R.id.ibtnOKEditarNITDatosCoop);
		ibtnOKEditarNIT.setOnClickListener(this);
		
		tvTelf = (TextView)v.findViewById(R.id.tvTelfDatosCoop);
		ibtnEditarTelf = (ImageButton)v.findViewById(R.id.ibtnEditarTelfDatosCoop);
		ibtnEditarTelf.setOnClickListener(this);
		lyEditarTelf = (LinearLayout)v.findViewById(R.id.lyEditarTelfDatosCoop);
		etTelf = (EditText)v.findViewById(R.id.etTelfDatosCoop);
		ibtnOKEditarTelf = (ImageButton)v.findViewById(R.id.ibtnOKEditarTelfDatosCoop);
		ibtnOKEditarTelf.setOnClickListener(this);
		
		tvEmail = (TextView)v.findViewById(R.id.tvEmailDatosCoop);
		ibtnEditarEmail = (ImageButton)v.findViewById(R.id.ibtnEditarEmailDatosCoop);
		ibtnEditarEmail.setOnClickListener(this);
		lyEditarEmail = (LinearLayout)v.findViewById(R.id.lyEditarEmailDatosCoop);
		etEmail = (EditText)v.findViewById(R.id.etEmailDatosCoop);
		ibtnOKEditarEmail = (ImageButton)v.findViewById(R.id.ibtnOKEditarEmailDatosCoop);
		ibtnOKEditarEmail.setOnClickListener(this);
		
		tvDir = (TextView)v.findViewById(R.id.tvDireccionDatosCoop);
		ibtnEditarDir = (ImageButton)v.findViewById(R.id.ibtnEditarDireccionDatosCoop);
		ibtnEditarDir.setOnClickListener(this);
		lyEditarDir = (LinearLayout)v.findViewById(R.id.lyEditarDireccionDatosCoop);
		etDir = (EditText)v.findViewById(R.id.etDireccionDatosCoop);
		ibtnOKEditarDir = (ImageButton)v.findViewById(R.id.ibtnOKEditarDireccionDatosCoop);
		ibtnOKEditarDir.setOnClickListener(this);
		
		tvCiudad = (TextView)v.findViewById(R.id.tvCiudadDatosCoop);
		ibtnEditarCiudad = (ImageButton)v.findViewById(R.id.ibtnEditarCiudadDatosCoop);
		ibtnEditarCiudad.setOnClickListener(this);
		lyEditarCiudad = (LinearLayout)v.findViewById(R.id.lyEditarCiudadDatosCoop);
		etCiudad = (EditText)v.findViewById(R.id.etCiudadDatosCoop);
		ibtnOKEditarCiudad = (ImageButton)v.findViewById(R.id.ibtnOKEditarCiudadDatosCoop);
		ibtnOKEditarCiudad.setOnClickListener(this);
		
		cargarCooperativa();
		cargarMostrarDatosCooperativa();
		dialog.setNeutralButton(R.string.aceptar, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setView(v);
		return dialog.create();
	}
	
	private void cargarMostrarDatosCooperativa() {
		tvNombre.setText(cooperativa.getNombre());
		tvNIT.setText(cooperativa.getNit());
		tvTelf.setText(String.valueOf(cooperativa.getTelf_cel()));
		tvEmail.setText(cooperativa.getEmail());
		tvDir.setText(cooperativa.getDireccion());
		tvCiudad.setText(cooperativa.getCiudad());
	}

	public void cargarCooperativa(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try{
			db.abrirDB();
			cooperativa = db.getCooperativa(Variables.ID_COOP);
			db.cerrarDB();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ibtnEditarNombreDatosCoop:
				lyEditarNombre.setVisibility(View.VISIBLE);
				etNombre.setText(cooperativa.getNombre());
				etNombre.requestFocus();
				break;
			case R.id.ibtnOKEditarNombreDatosCoop:
				String txtNombre = etNombre.getText().toString().trim().toUpperCase();
				if(txtNombre.length()>=4){
					if(!txtNombre.equals(cooperativa.getNombre())){
						cooperativa.setNombre(txtNombre);
						if(modificarCoop(cooperativa)){
							tvNombre.setText(txtNombre);
							Toast.makeText(getActivity(), "Nombre modificado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo modificar nombre", Toast.LENGTH_SHORT).show();
						}
						lyEditarNombre.setVisibility(View.GONE);
					}else{
						lyEditarNombre.setVisibility(View.GONE);
					}
				}else{
					etNombre.requestFocus();
					etNombre.setError("Nombre debe tener minimo 4 caractes");
				}
				break;
			case R.id.ibtnEditarNITDatosCoop:
				lyEditarNIT.setVisibility(View.VISIBLE);
				etNIT.setText(cooperativa.getNit());
				etNIT.requestFocus();
				break;
			case R.id.ibtnOKEditarNITDatosCoop:
				String txtNIT = etNIT.getText().toString().trim();
				if(txtNIT.length()==10){
					if(!txtNIT.equals(cooperativa.getNit())){
						cooperativa.setNit(txtNIT);
						if(modificarCoop(cooperativa)){
							tvNIT.setText(txtNIT);
							Toast.makeText(getActivity(), "NIT modificado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo modificar NIT", Toast.LENGTH_SHORT).show();
						}
						lyEditarNIT.setVisibility(View.GONE);
					}else{
						lyEditarNIT.setVisibility(View.GONE);
					}
				}else{
					etNIT.requestFocus();
					etNIT.setError("NIT debe contener 10 caractes");
				}
				break;
			case R.id.ibtnEditarTelfDatosCoop:
				lyEditarTelf.setVisibility(View.VISIBLE);
				etTelf.requestFocus();
				if(!cooperativa.getTelf_cel().equals(Variables.SIN_ESPECIFICAR)){
					etTelf.setText(String.valueOf(cooperativa.getTelf_cel()));
				}
				break;
			case R.id.ibtnOKEditarTelfDatosCoop:
				String txtTelf_Cel = etTelf.getText().toString().trim();
				if(txtTelf_Cel.length()>=7){
					if(!txtTelf_Cel.equals(cooperativa.getTelf_cel())){
						cooperativa.setTelf_cel(txtTelf_Cel);
						if(modificarCoop(cooperativa)){
							tvTelf.setText(txtTelf_Cel);
							Toast.makeText(getActivity(), "Telefono editado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo editado Telefono", Toast.LENGTH_SHORT).show();
						}
						lyEditarTelf.setVisibility(View.GONE);
					}else{
						lyEditarTelf.setVisibility(View.GONE);
					}
				}else if(txtTelf_Cel.length()>0 && txtTelf_Cel.length()<7){
					etTelf.requestFocus();
					etTelf.setError("Telefono debe contener minimo 7 digitos");
				}else if(txtTelf_Cel.length()==0){
					if(!cooperativa.getTelf_cel().equals(Variables.SIN_ESPECIFICAR)){
						cooperativa.setTelf_cel(Variables.SIN_ESPECIFICAR);
						if(modificarCoop(cooperativa)){
							tvTelf.setText(Variables.SIN_ESPECIFICAR);
							Toast.makeText(getActivity(), "Telefono borrado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo borrar Telefono", Toast.LENGTH_SHORT).show();
						}
						lyEditarTelf.setVisibility(View.GONE);
					}else{
						lyEditarTelf.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.ibtnEditarEmailDatosCoop:
				lyEditarEmail.setVisibility(View.VISIBLE);
				etEmail.requestFocus();
				if(!cooperativa.getEmail().equals(Variables.SIN_ESPECIFICAR)){
					etEmail.setText(cooperativa.getEmail());
				}
				break;
			case R.id.ibtnOKEditarEmailDatosCoop:
				String txtEmail = etEmail.getText().toString().trim();
				if(txtEmail.length()>=15){
					if(!txtEmail.equals(cooperativa.getEmail())){
						cooperativa.setEmail(txtEmail);
						if(modificarCoop(cooperativa)){
							tvEmail.setText(txtEmail);
							Toast.makeText(getActivity(), "Email editado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo editar Email", Toast.LENGTH_SHORT).show();
						}
						lyEditarEmail.setVisibility(View.GONE);
					}else{
						lyEditarEmail.setVisibility(View.GONE);
					}
				}else if(txtEmail.length()>0 && txtEmail.length()<15){
					etEmail.requestFocus();
					etEmail.setError("Email debe contener minimo 15 caracteres");
				}else if(txtEmail.length()==0){
					if(!cooperativa.getEmail().equals(Variables.SIN_ESPECIFICAR)){
						cooperativa.setEmail(Variables.SIN_ESPECIFICAR);
						if(modificarCoop(cooperativa)){
							tvEmail.setText(Variables.SIN_ESPECIFICAR);
							Toast.makeText(getActivity(), "Email borrado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo borrar Email", Toast.LENGTH_SHORT).show();
						}
						lyEditarEmail.setVisibility(View.GONE);
					}else{
						lyEditarEmail.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.ibtnEditarDireccionDatosCoop:
				lyEditarDir.setVisibility(View.VISIBLE);
				etDir.requestFocus();
				if(!cooperativa.getDireccion().equals(Variables.SIN_ESPECIFICAR)){
					etDir.setText(cooperativa.getDireccion());
				}
				break;
			case R.id.ibtnOKEditarDireccionDatosCoop:
				String txtDireccion = etDir.getText().toString().trim();
				if(txtDireccion.length()>=6){
					if(!txtDireccion.equals(cooperativa.getEmail())){
						cooperativa.setDireccion(txtDireccion);
						if(modificarCoop(cooperativa)){
							tvDir.setText(txtDireccion);
							Toast.makeText(getActivity(), "Direccion editado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo editar Email", Toast.LENGTH_SHORT).show();
						}
						lyEditarDir.setVisibility(View.GONE);
					}else{
						lyEditarDir.setVisibility(View.GONE);
					}
				}else if(txtDireccion.length()>0 && txtDireccion.length()<6){
					etDir.requestFocus();
					etDir.setError("Direccion debe contener minimo 6 caracteres");
				}else if(txtDireccion.length()==0){
					if(!cooperativa.getDireccion().equals(Variables.SIN_ESPECIFICAR)){
						cooperativa.setDireccion(Variables.SIN_ESPECIFICAR);
						if(modificarCoop(cooperativa)){
							tvDir.setText(Variables.SIN_ESPECIFICAR);
							Toast.makeText(getActivity(), "Direccion borrado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo borrar direccion", Toast.LENGTH_SHORT).show();
						}
						lyEditarDir.setVisibility(View.GONE);
					}else{
						lyEditarDir.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.ibtnEditarCiudadDatosCoop:
				lyEditarCiudad.setVisibility(View.VISIBLE);
				etCiudad.requestFocus();
				if(!cooperativa.getCiudad().equals(Variables.SIN_ESPECIFICAR)){
					etCiudad.setText(cooperativa.getCiudad());
				}
				break;
			case R.id.ibtnOKEditarCiudadDatosCoop:
				String txtCiudad= etCiudad.getText().toString().trim().toUpperCase();
				if(txtCiudad.length()>=4){
					if(!txtCiudad.equals(cooperativa.getCiudad())){
						cooperativa.setCiudad(txtCiudad);
						if(modificarCoop(cooperativa)){
							tvCiudad.setText(txtCiudad);
							Toast.makeText(getActivity(), "Ciudad editado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo editar ciudad", Toast.LENGTH_SHORT).show();
						}
						lyEditarCiudad.setVisibility(View.GONE);
					}else{
						lyEditarCiudad.setVisibility(View.GONE);
					}
				}else if(txtCiudad.length()>0 && txtCiudad.length()<4){
					etCiudad.requestFocus();
					etCiudad.setError("Ciudad debe contener minimo 4 caracteres");
				}else if(txtCiudad.length()==0){
					if(!cooperativa.getDireccion().equals(Variables.SIN_ESPECIFICAR)){
						cooperativa.setCiudad(Variables.SIN_ESPECIFICAR);
						if(modificarCoop(cooperativa)){
							tvCiudad.setText(Variables.SIN_ESPECIFICAR);
							Toast.makeText(getActivity(), "Ciudad borrado", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "No se pudo borrar ciudad", Toast.LENGTH_SHORT).show();
						}
						lyEditarCiudad.setVisibility(View.GONE);
					}else{
						lyEditarCiudad.setVisibility(View.GONE);
					}
				}
				break;
		}
	}
	
	public boolean modificarCoop(Cooperativa coop){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try{
			db.abrirDB();
			if(db.actualizarCooperativa(coop)){
				return true;
			}
			db.cerrarDB();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
