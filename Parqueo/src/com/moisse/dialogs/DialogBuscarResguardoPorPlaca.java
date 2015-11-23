package com.moisse.dialogs;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.fragments.ListaResguardo;
import com.moisse.modelo.Resguardo;
import com.moisse.modelo.Vehiculo;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogBuscarResguardoPorPlaca extends DialogFragment implements OnClickListener{
	View v;
	Button btnCancelar, btnAceptar;
	AutoCompleteTextView actvPlaca;

	ListaResguardo lista_resg;
	String idparqueo;
	public int DIGITOS_PARA_VALIDACION = 0;
	
	public DialogBuscarResguardoPorPlaca(ListaResguardo lista_resg, String idparqueo){
		this.lista_resg = lista_resg;
		this.idparqueo = idparqueo;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Filtrar por placa"));
		v = getActivity().getLayoutInflater().inflate(R.layout.modelo_resguardo_por_placa, null);
		actvPlaca = (AutoCompleteTextView)v.findViewById(R.id.actvPlacaResguardoPorPlaca);
		actvPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
		actvPlaca.addTextChangedListener(PlacaWacher);
		adapterAutoCompleteTextViewPlaca();
		btnCancelar = (Button)v.findViewById(R.id.btnCancelarResguardoPorPlaca);
		btnCancelar.setOnClickListener(this);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarResguardoPorPlaca);
		btnAceptar.setOnClickListener(this);
		dialog.setView(v);
		
		return dialog.create();
	}
	
	private final TextWatcher PlacaWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {	
			if(s.length()>=0 && s.length()<=2){
				actvPlaca.setInputType(InputType.TYPE_CLASS_NUMBER);
			}else if(s.length()==3){
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				//actvPlaca.setFilters(new InputFilter[]{MyVar.filterNumLetter});
			}else if(s.length()==4){
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				if(MyVar.isNumeric(s.toString())){
					DIGITOS_PARA_VALIDACION = 7;
					actvPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
				}else{
					DIGITOS_PARA_VALIDACION = 6;
					actvPlaca.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
				}
			}else if (s.length()==5) {
				actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
			}else if (s.length()==6) {
				if(DIGITOS_PARA_VALIDACION==0){
					DIGITOS_PARA_VALIDACION = 6;
					actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				}
			}else if (s.length()==7) {
				if(DIGITOS_PARA_VALIDACION==0){
					DIGITOS_PARA_VALIDACION = 7;
					actvPlaca.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				}
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	public static boolean isNumeric(String dato){
		try{
			Integer.parseInt(dato);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==btnCancelar.getId()){
			this.dismiss();
		}else if(v.getId()==btnAceptar.getId()){
			String placa = actvPlaca.getText().toString().toUpperCase();
			if(!placa.equals("") && placa.length()>=6){
				if(placa.length()==DIGITOS_PARA_VALIDACION){
					if(MyVar.isPlaca(placa)){
						Vehiculo vehiculo = getVehiculo(placa);
						if(vehiculo!=null){
							lista_resg.OPCION_ACT = MyVar.ACT_RESG_POR_PLACA;
							lista_resg.tvCuando.setText(new StringBuilder("Ingresos del vehículo ").append(vehiculo.getPlaca()));
							Toast.makeText(getActivity(), "Resguardos del vehículo "+vehiculo.getPlaca(), Toast.LENGTH_SHORT).show();
							lista_resg.cargarListaResguardos(getListaResguardoPorPlaca(vehiculo));
							this.dismiss();
						}else{
							Toast.makeText(getActivity(), "Vehículo con esta placa no esta registrado", Toast.LENGTH_SHORT).show();
						}
					}else{
						actvPlaca.requestFocus();
						actvPlaca.setError("Placa invalida Ej. 1234AAA - 555BBB");
					}
				}else{
					actvPlaca.requestFocus();
					actvPlaca.setError("Placa incorrecta falta un dígito");
				}
			}else{
				actvPlaca.requestFocus();
				actvPlaca.setError("Placa del vehículo debe tener 6 o 7 digítos");
			}
		}
	}
	
	public List<Resguardo> getListaResguardoPorPlaca(Vehiculo vehi){
		DBParqueo db = new DBParqueo(getActivity());
		List<Resguardo> listR = null;
		try {
			db.openSQLite();
			listR = db.getResguardoPorVehiculo(idparqueo, vehi.getIdvehiculo());
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listR;
	}
	
	public Vehiculo getVehiculo(String placa){
		DBParqueo db = new DBParqueo(getActivity());
		Vehiculo vehi = null;
		try {
			db.openSQLite();
			vehi = db.buscarVehiculoPorPlaca(placa, idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vehi;
	}
	
	public void adapterAutoCompleteTextViewPlaca(){
		List<Vehiculo> lista = null;
		String[] lista_placas = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			lista = db.getAllVehiculo(idparqueo, MyVar.NO_ELIMINADO);
			if(lista.size()!=0 && lista!=null){
				lista_placas = new String[lista.size()];
				for (int i = 0; i < lista_placas.length; i++) {
					lista_placas[i]=lista.get(i).getPlaca();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, lista_placas);
				actvPlaca.setThreshold(2);
				actvPlaca.setAdapter(adapter);
			}
			
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
