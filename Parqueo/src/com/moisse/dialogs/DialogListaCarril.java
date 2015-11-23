package com.moisse.dialogs;

import java.util.ArrayList;
import java.util.List;
import com.example.parqueo.R;
import com.moisse.adapters.ListaCarrilAdapter;
import com.moisse.database.DBParqueo;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Parqueo;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogListaCarril extends DialogFragment implements android.view.View.OnClickListener{

	ListView lvListaCarril;
	EditText etCant;
	TextView tvCantAdicion;
	ImageButton ibtnAceptar;
	View v;
	public Parqueo parqueo_online;
	public String idparqueo;
	public ListaCarrilAdapter adapter;
	
	protected HttpClientCustom httpCustom = new HttpClientCustom();
	
	public DialogListaCarril(String idparqueo){
		this.idparqueo = idparqueo;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(new StringBuilder("Carriles del parqueo"));
		v = getActivity().getLayoutInflater().inflate(R.layout.lista_carril, null);
		lvListaCarril = (ListView)v.findViewById(R.id.lvListaCarriles);
		tvCantAdicion = (TextView)v.findViewById(R.id.tvCantAdicionListaCarriles);
		etCant = (EditText)v.findViewById(R.id.etCantidadListaCarriles);
		ibtnAceptar = (ImageButton)v.findViewById(R.id.ibtnOkListaCarriles);
		ibtnAceptar.setOnClickListener(this);
		cargarListaCarriles();
		dialog.setView(v);
		dialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int wich) {
				dialog.dismiss();
			}
		});
		return dialog.create();
	}
	
	public void cargarListaCarriles(){
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			this.parqueo_online = db.getParqueo(idparqueo);
			tvCantAdicion.setText(new StringBuilder("Cantidad (Min 1 - Max ").append(100-parqueo_online.getCapacidad()).append("):"));
			List<Carril> lista_carril = db.getAllCarril(parqueo_online);
			if(lista_carril.size()!=0){
				adapter = new ListaCarrilAdapter(getActivity(), lista_carril, this);
				lvListaCarril.setAdapter(adapter);
			}
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==ibtnAceptar.getId()){
			String txtCant = etCant.getText().toString().trim();
			if(!txtCant.equals("")){
				int cant = Integer.parseInt(txtCant);
				if(cant>0){
					int aux = 100-parqueo_online.getCapacidad();
					if(cant<=aux){
						mensajeConfirmacion(getGenerateCarrilesAdicionales(cant));
					}else{
						etCant.requestFocus();
						etCant.setError("No puedes execeder los 100 carriles");
					}
				}else{
					etCant.requestFocus();
					etCant.setError("Cantidad debe ser mayor a 0");
				}
			}else{
				etCant.requestFocus();
				etCant.setError("Introduzca cantidad de carriles que desea adicionar");
			}
		}
	}
	
	private void mensajeConfirmacion(final List<Carril> lista) {
		AlertDialog.Builder dialogConfi = new AlertDialog.Builder(getActivity());
		dialogConfi.setTitle(new StringBuilder("Confirmar adición"));
		dialogConfi.setMessage(new StringBuilder("Actualmente cuenta con ").append(parqueo_online.getCapacidad())
									.append(" carriles ¿Desea adicionar ").append(lista.size()).append(" carriles?"));
		dialogConfi.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new AdicionarCarriles().execute(lista);
			}
		});
		
		dialogConfi.setNegativeButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialogConfi.create().show();
	}
	
	public List<Carril> getGenerateCarrilesAdicionales(int cantidad){
		List<Carril> lista = new ArrayList<Carril>();
		int aux = parqueo_online.getCapacidad();
		for (int i = 1; i <= cantidad; i++) { //le puse hasta igual cantidad para q i impiece en 1
			aux = aux + 1;
			Carril carril = new Carril(parqueo_online.getIdparqueo()+aux, aux, MyVar.SI, MyVar.NO, MyVar.NO_ELIMINADO, parqueo_online.getIdparqueo());
			lista.add(carril);
		}
		return lista;
	}
	
	public class AdicionarCarriles extends AsyncTask<List<Carril>, Void, Boolean>{
		ProgressDialog pd;
		List<Carril> lista_carriles = null;
		int cont;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Adicionando carriles...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}
		
		@Override
		protected Boolean doInBackground(List<Carril>... params) {
			lista_carriles = params[0];
			if(lista_carriles.size()!=0 && lista_carriles!=null){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					Parqueo parq_update = new Parqueo(parqueo_online.getIdparqueo(), parqueo_online.getCapacidad()+lista_carriles.size());
					if(httpCustom.actualizarCapacidadParqueo(parq_update)){
						if(db.actualizarCapacidadParqueo(parq_update)){
							for (int i = 0; i < lista_carriles.size(); i++) {
								Carril carril = lista_carriles.get(i);
								if(httpCustom.insertarCarril(carril)){
									db.insertarCarril(carril);
								}
							}
							return true;
						}
					}
					db.closeSQLite();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(getActivity(), "Carriles adicionados exitosamente", Toast.LENGTH_LONG).show();
				cargarListaCarriles();
				etCant.getText().clear();
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se pudieron adicionar carriles correctamente, intente mas tarde...!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
}
