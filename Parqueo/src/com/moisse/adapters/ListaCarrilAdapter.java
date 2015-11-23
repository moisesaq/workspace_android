package com.moisse.adapters;

import java.util.List;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogListaCarril;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.modelo.Carril;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ListaCarrilAdapter extends ArrayAdapter<Carril>{

	private Activity activity;
	private Button btnConfig;
	public DialogListaCarril dialog_lista_carril;
	protected HttpClientCustom httpCustom = new HttpClientCustom();
	
	public ListaCarrilAdapter(Activity activity, List<Carril> ListaCarril, DialogListaCarril dialog_lista_carril){
		super(activity, R.layout.modelo_item_lista_carril, ListaCarril);
		this.dialog_lista_carril = dialog_lista_carril;
		this.activity = activity;
	}
	
	public static class ViewHolder{
		TextView tvNumCarril;
		TextView tvDispCarril;
	}

	@Override
	public Carril getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if(view==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_lista_carril, null);
			holder = new ViewHolder();
			holder.tvNumCarril = (TextView)view.findViewById(R.id.tvCarrilLCarril);
			holder.tvDispCarril = (TextView)view.findViewById(R.id.tvDisponibleLCarril);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		
		final Carril carril = this.getItem(position);
		holder.tvNumCarril.setText(new StringBuilder("Carril No.: ").append(carril.getNum_carril()));
		if(carril.getDisponible().equals(MyVar.SI)){
			holder.tvDispCarril.setText(new StringBuilder("Disponible: ").append("SI"));
		}else{
			holder.tvDispCarril.setText(new StringBuilder("Disponible: ").append("NO"));
		}
		
		btnConfig = (Button)view.findViewById(R.id.btnConfigCarrilLCarril);
		btnConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(activity, "Carril "+carril.getIdcarril()+" - "+carril.getNum_carril()+" - "+carril.getDisponible(), Toast.LENGTH_LONG).show();
				if(carril.getEstado()==MyVar.NO_ELIMINADO){
					if(carril.getDisponible().equals(MyVar.SI)){
						mensajeConfirmacion(carril);
					}else{
						Toast.makeText(activity, "Carril no se puede deshabilitar por que esta ocupado", Toast.LENGTH_LONG).show();
					}
				}else if(carril.getEstado()==MyVar.ELIMINADO){
					new HabilitarCarril().execute(carril);
					//Toast.makeText(getContext(), "Habilitando jeejje", Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(carril.getEstado()==MyVar.NO_ELIMINADO){
			btnConfig.setText(new StringBuilder("Deshabilitar"));
		}else{
			btnConfig.setText(new StringBuilder("Habilitar"));
		}

		return view;
	}

	private void mensajeConfirmacion(final Carril carril) {
		AlertDialog.Builder dialogConfi = new AlertDialog.Builder(activity);
		dialogConfi.setMessage(new StringBuilder("¿Desea deshabilitar carril?"));
		dialogConfi.setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new DeshabilitarCarril().execute(carril);
				//Toast.makeText(getContext(), "Deshabilitando jejejeje", Toast.LENGTH_SHORT).show();
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

	public class HabilitarCarril extends AsyncTask<Carril, Void, Boolean>{
		ProgressDialog pd;
		Carril carril_habilitar;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Habilitando carril...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Carril... params) {
			carril_habilitar = params[0];
			if(httpCustom.habilitarCarril(carril_habilitar)){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					if(db.habilitarCarril(carril_habilitar)){
						return true;
					}
					db.closeSQLite();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(activity,"Carril habilitado",Toast.LENGTH_SHORT).show();
				dialog_lista_carril.cargarListaCarriles();
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo habilitar carril, intente mas tarde...!");
				mensaje.show(activity.getFragmentManager(), "tagMsj");
			}
		}
		
		
	}

	public class DeshabilitarCarril extends AsyncTask<Carril, Void, Boolean>{
		ProgressDialog pd;
		Carril carril_deshabilitar;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(activity);
			pd.setMessage("Deshabilitando carril...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Carril... params) {
			carril_deshabilitar = params[0];
			if(httpCustom.deshabilitarCarril(carril_deshabilitar)){
				DBParqueo db = new DBParqueo(activity);
				try {
					db.openSQLite();
					if(db.deshabilitarCarril(carril_deshabilitar)){
						return true;
					}
					db.closeSQLite();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				Toast.makeText(activity,"Carril deshabilitado",Toast.LENGTH_SHORT).show();
				dialog_lista_carril.cargarListaCarriles();
			}else{
				DialogMensaje mensaje = new DialogMensaje("No se pudo deshabilitar carril actualice sus carriles o intente mas tarde..!");
				mensaje.show(activity.getFragmentManager(), "tagMsj");
			}
		}	
	}
}
