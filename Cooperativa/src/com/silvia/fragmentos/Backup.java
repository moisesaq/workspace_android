package com.silvia.fragmentos;

import java.util.ArrayList;
import java.util.List;

import com.silvia.basedatos.DBDuraznillo;
import com.silvia.basedatos.HttpURLCustom;
import com.silvia.cooperativa.R;
import com.silvia.modelo.Cargo;
import com.silvia.modelo.Maquinaria;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Backup extends Fragment implements OnClickListener{

	View v;
	private Button btnRespaldo;
	private HttpURLCustom httpCustom;
	private ProgressBar pbBackup;
	private TextView tvMessage;
	final static int SIN_DATOS = 1;
	final static int SIN_CONEXION = 2;
	final static int BACKUP_SACADO = 3;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		v = getActivity().getLayoutInflater().inflate(R.layout.backup, container, false);
		iniciarComponentes(v);
		return v;
	}
	private void iniciarComponentes(View view) {
		httpCustom = new HttpURLCustom(getActivity());
		pbBackup = (ProgressBar)v.findViewById(R.id.pbBackup);
		tvMessage = (TextView)v.findViewById(R.id.tvMessageBackup);
		btnRespaldo = (Button)view.findViewById(R.id.btnBackup);
		btnRespaldo.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnBackup:
				new BackupCargo().execute();
				break;
		}
	}
	
	public class BackupCargo extends AsyncTask<Void, Void, Integer>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pbBackup.setVisibility(View.VISIBLE);
			tvMessage.setText("Sancado backup cargos...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			List<Cargo> list = null;
			DBDuraznillo db = new DBDuraznillo(getActivity());
			if(httpCustom.exitsConnection()){
				try {
					db.abrirDB();
					list = db.getAllCargos();
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(list!=null && list.size()!=0){
					for (int i = 0; i < list.size(); i++) {
						if(httpCustom.regitrarCargo(list.get(i))){
							Log.d("tagMaq", list.get(i).toString());
						}
					}
					return BACKUP_SACADO;
				}else{
					return SIN_DATOS;
				}
			}
			return SIN_CONEXION;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			new BackupMaq().execute();
			switch (result) {
				case BACKUP_SACADO:
					Toast.makeText(getActivity(), "Backup sacado de cargos", Toast.LENGTH_SHORT).show();
					break;
				case SIN_DATOS:
					Toast.makeText(getActivity(), "No hay dato para sacar Backup", Toast.LENGTH_SHORT).show();
					break;
				case SIN_CONEXION:
					Toast.makeText(getActivity(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	
	public class BackupMaq extends AsyncTask<Void, Void, Integer>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvMessage.setText("Sacando backup maquinarias...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			List<Maquinaria> list = null;
			DBDuraznillo db = new DBDuraznillo(getActivity());
			if(httpCustom.exitsConnection()){
				try {
					db.abrirDB();
					list = db.getAllMaquinarias();
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(list!=null && list.size()!=0){
					for (int i = 0; i < list.size(); i++) {
						if(httpCustom.regitrarMaquinaria(list.get(i))){
							Log.d("tagMaq", list.get(i).toString());
						}
					}
					return BACKUP_SACADO;
				}else{
					return SIN_DATOS;
				}
			}
			return SIN_CONEXION;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			new BackupPer().execute();
			switch (result) {
				case BACKUP_SACADO:
					Toast.makeText(getActivity(), "Backup sacado de maquinarias", Toast.LENGTH_SHORT).show();
					break;
				case SIN_DATOS:
					Toast.makeText(getActivity(), "No hay dato para sacar Backup", Toast.LENGTH_SHORT).show();
					break;
				case SIN_CONEXION:
					Toast.makeText(getActivity(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	
	public class BackupPer extends AsyncTask<Void, Void, Integer>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvMessage.setText("Backup personales...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			List<Personal> list = null;
			DBDuraznillo db = new DBDuraznillo(getActivity());
			if(httpCustom.exitsConnection()){
				try {
					db.abrirDB();
					list = db.getAllPersonales();
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(list!=null && list.size()!=0){
					for (int i = 0; i < list.size(); i++) {
						if(httpCustom.regitrarPersonal(list.get(i))){
							Log.d("tagPer", list.get(i).toString());
						}
					}
					return BACKUP_SACADO;
				}else{
					return SIN_DATOS;
				}
			}
			return SIN_CONEXION;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			new BackupUsuario().execute();
			switch (result) {
				case BACKUP_SACADO:
					Toast.makeText(getActivity(), "Backup sacado de personales", Toast.LENGTH_SHORT).show();
					break;
				case SIN_DATOS:
					Toast.makeText(getActivity(), "No hay dato para sacar Backup", Toast.LENGTH_SHORT).show();
					break;
				case SIN_CONEXION:
					Toast.makeText(getActivity(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	
	public class BackupUsuario extends AsyncTask<Void, Void, Integer>{
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvMessage.setText("Sacando backup usuarios...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			List<Usuario> list = null;
			DBDuraznillo db = new DBDuraznillo(getActivity());
			if(httpCustom.exitsConnection()){
				try {
					db.abrirDB();
					list = db.getTodosLosUsuario();
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(list!=null && list.size()!=0){
					for (int i = 0; i < list.size(); i++) {
						if(httpCustom.regitrarUsuario(list.get(i))){
							Log.d("tagPer", list.get(i).toString());
						}
					}
					return BACKUP_SACADO;
				}else{
					return SIN_DATOS;
				}
			}
			return SIN_CONEXION;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			pbBackup.setVisibility(View.GONE);
			tvMessage.setText("Backup generado correctamente");
			switch (result) {
				case BACKUP_SACADO:
					Toast.makeText(getActivity(), "Backup sacado de usuarios", Toast.LENGTH_SHORT).show();
					break;
				case SIN_DATOS:
					Toast.makeText(getActivity(), "No hay dato para sacar Backup", Toast.LENGTH_SHORT).show();
					break;
				case SIN_CONEXION:
					Toast.makeText(getActivity(), "Sin conexion a internet", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	
}
