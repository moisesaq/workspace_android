package com.silvia.dialogos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.fragmentos.ListaCargos;
import com.silvia.modelo.Cargo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogNuevoCargo extends DialogFragment implements OnClickListener{

	private EditText etOcupacion, etSalario, etDescripcion;
	private Button btnAceptar, btnCancelar;
	
	private Cargo cargo_editar;
	private String idcargo;
	private ListaCargos lista_cargos;
	
	public DialogNuevoCargo(ListaCargos lista_cargos){
		this.lista_cargos = lista_cargos;
	}
	
	public DialogNuevoCargo(Cargo cargo, ListaCargos lista_cargos){
		this.cargo_editar = cargo;
		this.lista_cargos = lista_cargos;
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
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.nuevo_cargo, null);
		etOcupacion = (EditText)v.findViewById(R.id.etOcupacionNuevoCargo);
		etSalario = (EditText)v.findViewById(R.id.etSalarioNuevoCargo);
		etDescripcion = (EditText)v.findViewById(R.id.etDescripcionNuevoCargo);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarNuevoCargo);
		btnAceptar.setOnClickListener(this);
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarNuevoCargo);
		btnCancelar.setOnClickListener(this);
		if(cargo_editar==null){
			dialog.setTitle("Agregar nuevo cargo");
		}else{
			dialog.setTitle("Modificar cargo");
			preparaCargoParaEditar();
		}
		
		dialog.setView(v);
		
		return dialog.create();
	}
	
	public void preparaCargoParaEditar(){
		idcargo = cargo_editar.getIdcargo();
		etOcupacion.setText(cargo_editar.getOcupacion());
		etSalario.setText(String.valueOf(cargo_editar.getSalario()));
		if(!cargo_editar.getDescripcion().equals(Variables.SIN_ESPECIFICAR)){
			etDescripcion.setText(cargo_editar.getDescripcion());
		}
		btnAceptar.setText("Listo");
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnAceptar.getId()){
			if(idcargo==null){
				registrarCargo();
			}else{
				modificarCargo();
			}
			
		}else if(v.getId()==btnCancelar.getId()){
			dismiss();
		}
	}
	
	private void modificarCargo() {
		String ocupacion = etOcupacion.getText().toString().trim().toUpperCase();
		double salario = 0;
		if(!etSalario.getText().toString().equals("")){
			salario = Double.parseDouble(etSalario.getText().toString());
		}
		String descripcion = etDescripcion.getText().toString();
		if(descripcion.equals("")){
			descripcion = Variables.SIN_ESPECIFICAR;
		}
		DBDuraznillo db = new DBDuraznillo(getActivity());
		if(!ocupacion.equals("") && ocupacion.length()>3){
			if(salario!=0){
				try {
					db.abrirDB();
					Cargo cargo = new Cargo(idcargo, ocupacion, salario, descripcion, Variables.NO_ELIMINADO);
					if(db.modificarCargo(cargo)){
						Toast.makeText(getActivity(), "Cargo "+cargo.getOcupacion()+" se modifico correctamente", Toast.LENGTH_SHORT).show();
						lista_cargos.cargarMostrarListaCargo();
						this.dismiss();
					}else{
						Toast.makeText(getActivity(), "No se pudo modificar cargo", Toast.LENGTH_SHORT).show();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				etSalario.requestFocus();
				etSalario.setError("Introduzca salario");
			}
		}else{
			etOcupacion.requestFocus();
			etOcupacion.setError("Ocupacion debe contener minimo 4 caracteres");
		}
	}

	public void registrarCargo(){
		String ocupacion = etOcupacion.getText().toString().trim().toUpperCase();
		double salario = 0;
		if(!etSalario.getText().toString().equals("")){
			salario = Double.parseDouble(etSalario.getText().toString());
		}
		String descripcion = etDescripcion.getText().toString();
		if(descripcion.equals("")){
			descripcion = Variables.SIN_ESPECIFICAR;
		}
		DBDuraznillo db = new DBDuraznillo(getActivity());
		if(!ocupacion.equals("") && ocupacion.length()>3){
			if(salario!=0){
				try {
					db.abrirDB();
					Cargo cargo = new Cargo(generarIdCargo(), ocupacion, salario, descripcion, Variables.NO_ELIMINADO);
					if(db.insertarCargo(cargo)){
						Toast.makeText(getActivity(), "Cargo "+cargo.getOcupacion()+" se registro correctamente", Toast.LENGTH_SHORT).show();
						lista_cargos.cargarMostrarListaCargo();
//						lista_cargos.adapter.add(cargo);
//						lista_cargos.adapter.notifyDataSetChanged();
						this.dismiss();
					}else{
						Toast.makeText(getActivity(), "No se pudo registrar cargo", Toast.LENGTH_SHORT).show();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				etSalario.requestFocus();
				etSalario.setError("Introduzca salario");
			}
		}else{
			etOcupacion.requestFocus();
			etOcupacion.setError("Ocupacion debe contener minimo 4 caracteres");
		}
	}
	
	public String generarIdCargo(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new Date());
		String imageCode = "cargo-"+date;
		return imageCode;
	}
	
}
