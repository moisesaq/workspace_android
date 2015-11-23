package com.silvia.dialogos;

import java.util.List;

import com.silvia.adapters.ListaPersonalAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.modelo.Personal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogSeleccionarPersonalParaEnviarInforme extends DialogFragment implements OnItemClickListener{

	public ListView lvListaPersonal;
	public TextView tvAviso, textView;
	public View v;
	
	public EditText etEmail;
	
	public DialogSeleccionarPersonalParaEnviarInforme(TextView textView, EditText etEmail){
		this.textView = textView;
		this.etEmail = etEmail;
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
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle(new StringBuilder("Seleccione personal"));
		v = getActivity().getLayoutInflater().inflate(R.layout.lista_personal, null);
		lvListaPersonal = (ListView)v.findViewById(R.id.lvListaPersonal);
		lvListaPersonal.setOnItemClickListener(this);
		tvAviso = (TextView)v.findViewById(R.id.tvNotaListaPersonal);
		cargarListaPersonal();
		dialog.setView(v);
		dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int wich) {
				dialog.cancel();
			}
		});
		return dialog.create();
	}
	
	private void cargarListaPersonal(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Personal> lista = db.getTodosLosPersonales();
			if(lista.size()!=0){
				ListaPersonalAdapter adapter = new ListaPersonalAdapter(getActivity(), lista);
				lvListaPersonal.setAdapter(adapter);
			}else{
				lvListaPersonal.setVisibility(View.INVISIBLE);
				tvAviso.setVisibility(View.VISIBLE);
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Personal personal = (Personal)adapter.getAdapter().getItem(position);
		if(!personal.getEmail().equals(Variables.SIN_ESPECIFICAR)){
			//textView.setVisibility(View.VISIBLE);
			textView.setText(new StringBuilder("Enviar informe a ").append(personal.getNombre()));
			textView.setTag(personal.getNombre());
			etEmail.setText(new StringBuilder(personal.getEmail()));
		}else{
			textView.setText("Email del personal seleccionado esta sin especificar, edite o intruduzca un email directamente");
			etEmail.getText().clear();
		}
		this.dismiss();
	}
	
}
