package com.moises.httpurlconnection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.moises.connectiontoserver.R;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarLugar extends Fragment implements OnClickListener{

	View v;
	public EditText etLugar, etDir, etDescripcion;
	public Button btnAceptar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		v = inflater.inflate(R.layout.registrar_lugar, container, false);
		iniciarComponentes(v);
		return v;
	}
	
	public void iniciarComponentes(View v){
		getActivity().getActionBar().setTitle("Nuevo lugar");
		etLugar = (EditText)v.findViewById(R.id.etLugarRegLugar);
		etDir = (EditText)v.findViewById(R.id.etDireccionRegLugar);
		etDescripcion = (EditText)v.findViewById(R.id.etDescripcionRegLugar);
		btnAceptar = (Button)v.findViewById(R.id.btnAceptarRegLugar);
		btnAceptar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(btnAceptar.getId()==v.getId()){
			//Toast.makeText(getActivity(), "Probando", Toast.LENGTH_SHORT).show();
			String txtLugar = etLugar.getText().toString();
			String txtDir = etDir.getText().toString();
			String txtDescripcion = etDescripcion.getText().toString();
			if(!txtLugar.equals("")){
				if(!txtDir.equals("")){
					if(!txtDescripcion.equals("")){
						Lugar lugar = new Lugar(getIDLugar(), txtLugar, txtDir, txtDescripcion);
						new Registrar().execute(lugar);
					}else{
						etDescripcion.requestFocus();
						etDescripcion.setError("Introduzca descripcion");
					}	
				}else{
					etDir.requestFocus();
					etDir.setError("Introduzca direccion");
				}	
			}else{
				etLugar.requestFocus();
				etLugar.setError("Introduzca nombre del lugar");
			}
		}
	}
	
	public class Registrar extends AsyncTask<Lugar, Void, Boolean>{
		public ProgressDialog pd;
		public CustomHttpUrl custom;
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Registrando lugar...");
			pd.show();
		}
		
		@Override
		public Boolean doInBackground(Lugar... params){
			Lugar lugar = params[0];
			custom = new CustomHttpUrl(getActivity());
			if(custom.registrarLugar(lugar)){
				return true;
			}
			return false;
		}
		
		@Override
		public void onPostExecute(Boolean control){
			super.onPostExecute(control);
			pd.dismiss();
			if(control){
				Toast.makeText(getActivity(), "Lugar registrado exitosamente", Toast.LENGTH_SHORT).show();
				limpiar();
			}else{
				Toast.makeText(getActivity(), "No se pudo registrar lugar", Toast.LENGTH_SHORT).show();
			}
		}

		private void limpiar() {
			etLugar.getText().clear();
			etDir.getText().clear();
			etDescripcion.getText().clear();
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		CustomHttpUrl cHttp = new CustomHttpUrl(getActivity());
		if(!cHttp.exitsConnection()){
			Toast.makeText(getActivity(), "Sin conexion",  Toast.LENGTH_SHORT).show();
			btnAceptar.setEnabled(false);
		}else{
			Toast.makeText(getActivity(), "Con conexion :)",  Toast.LENGTH_SHORT).show();
		}
	}
	
	public String getIDLugar(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new Date());
		return "lugar_"+date;
	}
}
