package com.silvia.fragmentos;

import java.util.List;

import com.silvia.adapters.ListaPersonalAdapter;
import com.silvia.basedatos.DBDuraznillo;
import com.silvia.cooperativa.R;
import com.silvia.cooperativa.Variables;
import com.silvia.dialogos.DialogNuevoUsuario;
import com.silvia.modelo.Personal;
import com.silvia.modelo.Usuario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListaPersonal extends Fragment implements OnItemClickListener, OnItemLongClickListener{

	ListView lvListaPersonal;
	Usuario personalUser;
	TextView tvNota;
	
	OnListaPersonalClickListener listener;
	
	//TODO Si es posible cambiar esto por PopupMenu
	String[] opcionDefinir =  {"Eliminar", "Modificar", "Definir como usuario"};
	String[] opcionHabilitar =  {"Eliminar", "Modificar", "Habiltar usuario"};
	String[] opcionDeshabilitar =  {"Eliminar", "Modificar", "Deshabilitar usuario"};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.lista_personal, container, false);
		inicializarComponentes(v);
		return v;
	}

	private void inicializarComponentes(View v) {
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle("Lista personal");
		lvListaPersonal = (ListView)v.findViewById(R.id.lvListaPersonal);
		lvListaPersonal.setOnItemClickListener(this);
		lvListaPersonal.setOnItemLongClickListener(this);
		tvNota = (TextView)v.findViewById(R.id.tvNotaListaPersonal);
		Bundle cajon = getArguments();
		cargarDatosPersonalUser(cajon.getString("idusuario"));
		cargarMostrarListaPersonal();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_nuevo_personal).setVisible(true);
	}

	private void cargarMostrarListaPersonal() {
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			List<Personal> lista = db.getTodosLosPersonales(personalUser.getIdpersonal());
			if(lista.size()==0){
				tvNota.setText("No se encontro ningun personal, lista vacia..!");
				tvNota.setVisibility(View.VISIBLE);
				lvListaPersonal.setVisibility(View.INVISIBLE);
			}else{
				//ArrayAdapter<Personal> adapter = new ArrayAdapter<Personal>(getActivity(), android.R.layout.simple_list_item_1, lista);
				ListaPersonalAdapter adapter = new ListaPersonalAdapter(getActivity(), lista);
				lvListaPersonal.setAdapter(adapter);
			}
			
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long parent) {
		Personal personal = (Personal)adapter.getAdapter().getItem(position);
		listener.onVerDetallePersonalClick(personal.getIdpersonal());
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long parent) {
		Personal personal = (Personal)adapter.getAdapter().getItem(position);
		if(isUser(personal)){
			if(isUserActivate(personal)){
				escogerOpcion(personal, opcionDeshabilitar);
			}else{
				escogerOpcion(personal, opcionHabilitar);
			}
			
		}else{
			escogerOpcion(personal, opcionDefinir);
		}
		return false;
	}
	
	public void escogerOpcion(final Personal personal, String[] opciones){
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.Theme_CAT_Alert_Dialog);
		dialog.setTitle("Seleccione");
		dialog.setItems(opciones, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0){
					confirmarEliminarPersonal(personal);
					dialog.dismiss();
				}else if(which==1){
					listener.onEditarListaPersonalClick(personal.getIdpersonal());
					dialog.dismiss();
				}else if (which==2) {
					if(isUser(personal)){
						if(isUserActivate(personal)){
							if(deshabiltarUser(personal)){
								Toast.makeText(getActivity(), "Deshabiltado como usuario", Toast.LENGTH_SHORT).show();
							}
						}else{
							if(habilitarUser(personal)){
								Toast.makeText(getActivity(), "Habiltado como usuario", Toast.LENGTH_SHORT).show();
							}
						}
						cargarMostrarListaPersonal();
					}else{
						DialogNuevoUsuario nuevoUser = new DialogNuevoUsuario(personal, Variables.ACCION_NUEVO_USUARIO);
						nuevoUser.show(getFragmentManager(), "tagNU");
					}					
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
	
	public boolean isUserActivate(Personal personal){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.esPersonalUsuarioHabiltado(personal)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deshabiltarUser(Personal personal){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.deshabilitarUsuario(personal)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean habilitarUser(Personal personal){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			if(db.habilitarUsuario(personal)){
				return true;
			}
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void confirmarEliminarPersonal(final Personal personal) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
						cargarMostrarListaPersonal();
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
		dialog.create().show();
	}
	
	public interface OnListaPersonalClickListener{
		public void onVerDetallePersonalClick(String idpersonal);
		public void onEditarListaPersonalClick(String idpersonal);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (OnListaPersonalClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar OnListaPersonalClickListener");
		}
	}

	public void cargarDatosPersonalUser(String idusuario){
		DBDuraznillo db = new DBDuraznillo(getActivity());
		try {
			db.abrirDB();
			this.personalUser = db.getUsuario(idusuario);
			db.cerrarDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
