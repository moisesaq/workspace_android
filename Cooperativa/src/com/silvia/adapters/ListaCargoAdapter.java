package com.silvia.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.silvia.cooperativa.R;
import com.silvia.modelo.Cargo;

public class ListaCargoAdapter extends ArrayAdapter<Cargo>{

	private Activity activity;
	
	public ListaCargoAdapter(Activity activity, List<Cargo> lista_cargo){
		super(activity, R.layout.modelo_item_lista_cargo, lista_cargo);
		this.activity = activity;
	}
	
	public class ViewHolder{
		TextView tvOcupacion;
		TextView tvSalario;
		TextView tvDescripcion;
	}
	
	@Override
	public Cargo getItem(int position){
		return super.getItem(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = convertView;
		final ViewHolder holder;
		if(convertView==null){
			view = activity.getLayoutInflater().inflate(R.layout.modelo_item_lista_cargo, null);
			holder = new ViewHolder();
			holder.tvOcupacion = (TextView)view.findViewById(R.id.tvOcupacionLCargo);
			holder.tvSalario = (TextView)view.findViewById(R.id.tvSalarioLCargo);
			holder.tvDescripcion = (TextView)view.findViewById(R.id.tvDescripcionLCargo);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		Cargo cargo = this.getItem(position);
		holder.tvOcupacion.setText(cargo.getOcupacion());
		holder.tvSalario.setText(cargo.getSalario()+" Bs.");
		holder.tvDescripcion.setText(cargo.getDescripcion());
		return view;
	}
}
