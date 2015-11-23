package com.silvia.fragmentos;

import java.util.List;

import com.silvia.adapters.ListaCargoAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.dialogos.DialogNuevoCargo;
import com.silvia.modelo.Cargo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListaCargos extends Fragment implements OnItemClickListener{
	
	private ListView lvListaCargos;
	private TextView tvNota;
	public  ListaCargoAdapter adapter; 

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_cargo, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista cargos");
		lvListaCargos = (ListView)v.findViewById(R.id.lvListaCargos);
		lvListaCargos.setOnItemClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaCargo);
		cargarMostrarListaCargo();
	}
	
	public void cargarMostrarListaCargo(){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Cargo> lista = db.getTodosLosCargos();
			adapter = new ListaCargoAdapter(getActivity(), lista);
			if(lista.size()==0 || lista==null){
				tvNota.setText("No se encontro ningun cargo, lista vacia..!");
				tvNota.setVisibility(View.VISIBLE);
				lvListaCargos.setVisibility(View.INVISIBLE);
			}else{
				tvNota.setVisibility(View.INVISIBLE);
				lvListaCargos.setVisibility(View.VISIBLE);
				lvListaCargos.setAdapter(adapter);
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Cargo cargo = (Cargo)adapter.getAdapter().getItem(position);
		//Toast.makeText(getActivity(), "Cargo "+cargo.getOcupacion(), Toast.LENGTH_SHORT).show();
		elegirOpcion(cargo);
	}
	
	public void elegirOpcion(final Cargo cargo){
		String[] opciones = {"Eliminar", "Modificar"}; 
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("Elija opcion");
		dialog.setItems(opciones, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0){
					confirmarEliminacion(cargo);
					dialog.dismiss();
				}else if(which==1){
					DialogNuevoCargo modificarC = new DialogNuevoCargo(cargo, ListaCargos.this);
					modificarC.show(getFragmentManager(), "tagModificarC");
					dialog.dismiss();
				}
			}
		});
		dialog.setNeutralButton(R.string.cancelar, new OnClickListener() {
			
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
	}
	
	public void confirmarEliminacion(final Cargo cargo){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("¿Eliminar?");
		dialog.setMessage("Recuerde que una vez elimine cargo no podra restaurarlo");
		dialog.setPositiveButton(R.string.aceptar, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBDuraznillo db = new DBDuraznillo(getActivity());
				try {
					db.abrirDB();
					if(db.eliminarCargo(cargo)){
						Toast.makeText(getActivity(), "Cargo eliminado", Toast.LENGTH_SHORT).show();
						adapter.remove(cargo);
						adapter.notifyDataSetChanged();
						//cargarMostrarListaCargo();
						dialog.dismiss();
					}else{
						Toast.makeText(getActivity(), "No se pudo eliminar cargo, intente mas tarde..!", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
					db.cerrarDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		dialog.setNeutralButton(R.string.cancelar, new OnClickListener() {
			
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.action_nuevo_cargo).setVisible(true);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_nuevo_cargo){
			DialogNuevoCargo dNC = new DialogNuevoCargo(this);
			dNC.show(getFragmentManager(), "tagNC");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
