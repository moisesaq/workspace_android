package com.moisse.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.parqueo.R;
import com.moisse.database.DBParqueo;
import com.moisse.dialogs.DialogDefinirHora;
import com.moisse.dialogs.DialogListaCarril;
import com.moisse.dialogs.DialogMensaje;
import com.moisse.modelo.Carril;
import com.moisse.modelo.Cliente;
import com.moisse.modelo.Parqueo;
import com.moisse.others.CustomToast;
import com.moisse.others.HttpClientCustom;
import com.moisse.others.MyVar;
import com.moisse.others.UploadImage;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class NuevoParqueo extends Fragment implements OnClickListener, OnFocusChangeListener, OnCheckedChangeListener{
	
	ImageView ivLogo_parqueo;
	EditText etNombre_parqueo, etTelf, etDireccion, etTolerancia, etCapacidad, etPrecioHoraDia, etPrecioNoche; 
	EditText etPrecioContratoNocturno, etPrecioContratoDiurno, etPrecioContratoDiaCompleto;
	CheckBox cbContratoNocturno, cbContratoDiurno, cbContratoDiaCompleto;
	LinearLayout lyPrecioContratoNocturno, lyPrecioContratoDiurno, lyPrecioContratoDiaCompleto, lyCapacidad;
	TextView tvIniciaDia, tvFinDia, tvInicioNoche, tvFinNoche;
	ImageButton ibtnInicioDia, ibtnFinDia;	
	Button btnRegistrar_continuar;
	ProgressBar pbVerificarParqueo;
	
	OnParqueoClickListener listener;
	ProgressDialog pd;
	
	private String pathImageParqueo = MyVar.NO_ESPECIFICADO;
	private String idparqueo;
	private Parqueo parq_online;
	
	private Uri imageUri;
	private Bitmap imageBitmap;

	public boolean NOMBRE_PARQUEO_DISPONIBLE = false;
	HttpClientCustom httpCustom = new HttpClientCustom();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.nuevo_parqueo, container, false);
		setHasOptionsMenu(true);
		inicializarComponentes(view);
		return view;
	}

	private void inicializarComponentes(View view) {
		ivLogo_parqueo = (ImageView)view.findViewById(R.id.ivLogoParqueo);
		ivLogo_parqueo.setOnClickListener(this);
		pbVerificarParqueo = (ProgressBar)view.findViewById(R.id.pbVericarNombreParqueo);
		etNombre_parqueo = (EditText)view.findViewById(R.id.etNombreParqueo);
		etNombre_parqueo.addTextChangedListener(NombreParqueoWacher);
		etTelf = (EditText)view.findViewById(R.id.etTelefonoParqueo);
		etDireccion = (EditText)view.findViewById(R.id.etDireccionParqueo);
		etTolerancia = (EditText)view.findViewById(R.id.etToleranciaParqueo);
		lyCapacidad = (LinearLayout)view.findViewById(R.id.lyCapacidadParqueo);
		etCapacidad = (EditText)view.findViewById(R.id.etCapacidadParqueo);
		etPrecioHoraDia = (EditText)view.findViewById(R.id.etPrecioHoraDiaParqueo);
		etPrecioNoche = (EditText)view.findViewById(R.id.etPrecioNocheParqueo);
		
		lyPrecioContratoNocturno = (LinearLayout)view.findViewById(R.id.lyPrecioContratoNocturnoParqueo);
		cbContratoNocturno = (CheckBox)view.findViewById(R.id.cbContratoNocturnoParqueo);
		cbContratoNocturno.setOnCheckedChangeListener(this);
		etPrecioContratoNocturno = (EditText)view.findViewById(R.id.etPrecioContratoNocturnoParqueo);
		
		lyPrecioContratoDiurno = (LinearLayout)view.findViewById(R.id.lyPrecioContratoDiurnoParqueo);
		cbContratoDiurno = (CheckBox)view.findViewById(R.id.cbContratoDiurnoParqueo);
		cbContratoDiurno.setOnCheckedChangeListener(this);
		etPrecioContratoDiurno = (EditText)view.findViewById(R.id.etPrecioContratoDiurnoParqueo);
		
		lyPrecioContratoDiaCompleto = (LinearLayout)view.findViewById(R.id.lyPrecioContratoDiaCompletoParqueo);
		cbContratoDiaCompleto = (CheckBox)view.findViewById(R.id.cbContratoDiaCompletoParqueo);
		cbContratoDiaCompleto.setOnCheckedChangeListener(this);
		etPrecioContratoDiaCompleto = (EditText)view.findViewById(R.id.etPrecioContratoDiaCompletoParqueo);
		
		tvIniciaDia = (TextView)view.findViewById(R.id.tvInicioDiaParqueo);
		tvFinDia = (TextView)view.findViewById(R.id.tvFinDiaParqueo);
		tvInicioNoche = (TextView)view.findViewById(R.id.tvInicioNocheParqueo);
		tvFinNoche = (TextView)view.findViewById(R.id.tvFinNocheParqueo);
		ibtnInicioDia = (ImageButton)view.findViewById(R.id.ibtnDefinirHoraInicioDiaParqueo);
		ibtnInicioDia.setOnClickListener(this);
		ibtnFinDia = (ImageButton)view.findViewById(R.id.ibtnDefinirHoraFinDiaParqueo);
		ibtnFinDia.setOnClickListener(this);
		btnRegistrar_continuar = (Button)view.findViewById(R.id.btnRegParqueoContinuar);
		btnRegistrar_continuar.setOnClickListener(this);
		
		ActionBar actionB = getActivity().getActionBar();
		actionB.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionB.setTitle(new StringBuilder("Datos nuevo parqueo"));
		Bundle bundle = getArguments();
		this.idparqueo = bundle.getString("idparqueo");
		if(!idparqueo.equals(MyVar.NO_ESPECIFICADO)){
			actionB.setTitle(new StringBuilder("Config. parqueo"));
			cargarDatosParqueoParaEditar();
		}else{
			tvIniciaDia.setTag(MyVar.INICIO_DIA_DEFAULT);
			tvFinDia.setTag(MyVar.FIN_DIA_DEFAULT);
			tvInicioNoche.setTag(MyVar.INICIO_NOCHE_DEFAULT);
			tvFinNoche.setTag(MyVar.FIN_NOCHE_DEFAULT);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		if(!idparqueo.equals(MyVar.NO_ESPECIFICADO)){
			menu.findItem(R.id.action_search).setVisible(false);
			menu.findItem(R.id.action_edit_carriles).setVisible(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.action_edit_carriles){
			DialogListaCarril dLCarril = new DialogListaCarril(parq_online.getIdparqueo());
			dLCarril.show(getFragmentManager(), "tagLCarril");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private final TextWatcher NombreParqueoWacher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			VerifyNombreParqueo verify = new VerifyNombreParqueo();
			if(idparqueo.equals(MyVar.NO_ESPECIFICADO)){
				if(s.length()>=4 && s.length()<=10){
					verify.execute(s.toString());
				}else{
					verify.cancel(true);
					NOMBRE_PARQUEO_DISPONIBLE = false;
					etNombre_parqueo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				}	
			}else{
				if(!parq_online.getNombreParqueo().equals(s.toString())){
					if(s.length()>=4 && s.length()<=10){
						verify.execute(s.toString());
					}else{
						verify.cancel(true);
						NOMBRE_PARQUEO_DISPONIBLE = false;
						etNombre_parqueo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
					}
				}else{
					etNombre_parqueo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					NOMBRE_PARQUEO_DISPONIBLE = true;
				}
			}
				
		}
	};
	
	private class VerifyNombreParqueo extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pbVerificarParqueo.setVisibility(View.VISIBLE);
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String nombre_parqueo = params[0];		
			if(httpCustom.verificarDisponibilidadNombreParqueo(nombre_parqueo)){
				return true;
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pbVerificarParqueo.setVisibility(View.GONE);
			if(result){
				etNombre_parqueo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_good_22, 0);
				NOMBRE_PARQUEO_DISPONIBLE = true;
			}else{
				etNombre_parqueo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no_value_24, 0);
				NOMBRE_PARQUEO_DISPONIBLE = false;
			}
		}
	}

	private void cargarDatosParqueoParaEditar() {
		this.parq_online = getParqueoOnline();
		if(!parq_online.getLogo().equals(MyVar.NO_ESPECIFICADO)){
			Bitmap myBitmap = BitmapFactory.decodeFile(MyVar.FOLDER_IMAGES_PARQUEO+parq_online.getLogo());
			if(myBitmap!=null){
				ivLogo_parqueo.setImageBitmap(myBitmap);
			}
		}else{
			ivLogo_parqueo.setImageResource(R.drawable.ic_local_parking_white_48dp);
		}
		this.pathImageParqueo = parq_online.getLogo();
		
		etNombre_parqueo.setText(new StringBuilder(parq_online.getNombreParqueo()));
		//etNombre_parqueo.setEnabled(false);
		etTelf.setVisibility(View.VISIBLE);
		if(parq_online.getTelf()!=0){
			etTelf.setText(new StringBuilder().append(parq_online.getTelf()));
		}
		etDireccion.setVisibility(View.VISIBLE);
		if(!parq_online.getDireccion().equals(MyVar.NO_ESPECIFICADO)){
			etDireccion.setText(new StringBuilder(parq_online.getDireccion()));
		}
		etDireccion.requestFocus();
		etTolerancia.setText(new StringBuilder().append(parq_online.getTolerancia()));
		lyCapacidad.setVisibility(View.GONE);
		etPrecioHoraDia.setText(new StringBuilder().append(parq_online.getPrecioHoraDia()));
		etPrecioNoche.setText(new StringBuilder().append(parq_online.getPrecioNoche()));
		
		double precio_nocturno = parq_online.getPrecioContratoNocturno();
		if(precio_nocturno!=0){
			cbContratoNocturno.setChecked(true);
			etPrecioContratoNocturno.setText(new StringBuilder().append(precio_nocturno));
			cbContratoNocturno.setSelected(false);
		}
		
		double precio_diurno = parq_online.getPrecioContratoDiurno();
		if(precio_diurno!=0){
			cbContratoDiurno.setChecked(true);
			etPrecioContratoDiurno.setText(new StringBuilder().append(precio_diurno));
		}
		
		double precio_dia_completo = parq_online.getPrecioContratoDiaCompleto();
		if(precio_dia_completo!=0){
			cbContratoDiaCompleto.setChecked(true);
			etPrecioContratoDiaCompleto.setText(new StringBuilder().append(precio_dia_completo));
		}
		tvIniciaDia.setText(new StringBuilder(parq_online.getInicioDia().toString()));
		tvIniciaDia.setTag(parq_online.getInicioDia());
		tvFinDia.setText(new StringBuilder(parq_online.getFinDia().toString()));
		tvFinDia.setTag(parq_online.getFinDia());
		tvInicioNoche.setText(new StringBuilder(parq_online.getInicioNoche().toString()));
		tvInicioNoche.setTag(parq_online.getInicioNoche());
		tvFinNoche.setText(new StringBuilder(parq_online.getFinNoche().toString()));
		tvFinNoche.setTag(parq_online.getFinNoche());
		btnRegistrar_continuar.setText(new StringBuilder("Listo"));

	}
	
	public Parqueo getParqueoOnline(){
		Parqueo p = null;
		DBParqueo db = new DBParqueo(getActivity());
		try {
			db.openSQLite();
			p = db.getParqueo(this.idparqueo);
			db.closeSQLite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ivLogoParqueo){
			selectImage();
		}else if (v.getId()==btnRegistrar_continuar.getId()) {
			if(!idparqueo.equals(MyVar.NO_ESPECIFICADO)){
				validarIniciarActualizacionParqueo();
			}else{
				validarIniciarRegistroParqueo();
			}
		}else if (v.getId()==ibtnInicioDia.getId()) {
			DialogDefinirHora dialogDH1 = new DialogDefinirHora(tvIniciaDia, tvFinNoche, MyVar.DEFINIR_HORARIO_DIURNO, 
																	MyVar.INICIO_DIA_DEFAULT.getHours(), MyVar.INICIO_DIA_DEFAULT.getMinutes());
			dialogDH1.show(getFragmentManager(), "tagDDH1");
		}else if (v.getId()==ibtnFinDia.getId()) {
			DialogDefinirHora dialogDH2 = new DialogDefinirHora(tvFinDia, tvInicioNoche, MyVar.DEFINIR_HORARIO_NOCTURNO,
																	MyVar.FIN_DIA_DEFAULT.getHours(), MyVar.FIN_DIA_DEFAULT.getMinutes());
			dialogDH2.show(getFragmentManager(), "tagDDH1");
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getId()==cbContratoNocturno.getId()){
			if(cbContratoNocturno.isChecked()){
				lyPrecioContratoNocturno.setVisibility(View.VISIBLE);
				etPrecioContratoNocturno.requestFocus();
			}else{
				etPrecioContratoNocturno.getText().clear();
				lyPrecioContratoNocturno.setVisibility(View.GONE);
			}
		}else if (buttonView.getId()==cbContratoDiurno.getId()) {
			if (cbContratoDiurno.isChecked()) {
				lyPrecioContratoDiurno.setVisibility(View.VISIBLE);
				etPrecioContratoDiurno.requestFocus();
			}else{
				etPrecioContratoDiurno.getText().clear();
				lyPrecioContratoDiurno.setVisibility(View.GONE);
			}
		}else if (buttonView.getId()==cbContratoDiaCompleto.getId()) {
			if (cbContratoDiaCompleto.isChecked()) {
				lyPrecioContratoDiaCompleto.setVisibility(View.VISIBLE);
				etPrecioContratoDiaCompleto.requestFocus();
			}else{
				etPrecioContratoDiaCompleto.getText().clear();
				lyPrecioContratoDiaCompleto.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	public void onFocusChange(View view, boolean gainFocus) {
		CustomToast customToast;
		int y = getActivity().getActionBar().getHeight();
		if(view==etTelf && gainFocus){
			customToast = new CustomToast(getActivity(), "Telf. o celular del parqueo", R.drawable.ic_phone_white_36dp, 10, y+10);
			customToast.showCustomToast();
		}else if (gainFocus && view==etDireccion) {
			customToast = new CustomToast(getActivity(), "Direccion del parqueo", R.drawable.ic_directions_white_36dp, 10, y+10);
			customToast.showCustomToast();
		}else if (gainFocus && view==etPrecioHoraDia) {
			customToast = new CustomToast(getActivity(), "Precio parqueo de dia", R.drawable.ic_attach_money_white_36dp, 10, y+10);
			customToast.showCustomToast();
		}else if (gainFocus && view==etPrecioNoche) {
			customToast = new CustomToast(getActivity(), "Precio parqueo de noche", R.drawable.ic_attach_money_white_36dp, 10, y+10);
			customToast.showCustomToast();	
		}else if (gainFocus && view==etTolerancia) {
			customToast = new CustomToast(getActivity(), "Tiempo de espera o tolencia", R.drawable.ic_alarm_white_36dp, 10, y+10);
			customToast.showCustomToast();
		}else if (gainFocus && view==etNombre_parqueo) {
			customToast = new CustomToast(getActivity(), "Nombre del parqueo", R.drawable.ic_local_parking_white_36dp, 10, y+10);
			customToast.showCustomToast();
		}else if (gainFocus && view==etCapacidad) {
			customToast = new CustomToast(getActivity(), "Cantidad de carriles", R.drawable.ic_apps_white_36dp, 10, y+10);
			customToast.showCustomToast();
		}
	}
	
	public void selectImage(){
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, MyVar.SELECT_IMAGE);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(requestCode==MyVar.SELECT_IMAGE && resultCode==getActivity().RESULT_OK){
			imageUri = data.getData();
			pathImageParqueo = getRealPathFromURI(imageUri);
			imageBitmap = BitmapFactory.decodeFile(pathImageParqueo);
			if(imageBitmap.getWidth()>=MyVar.PIC_VALUE_MAX && imageBitmap.getHeight()>=MyVar.PIC_VALUE_MAX){
				performCrop(MyVar.PIC_VALUE_MAX);
			}else if (imageBitmap.getHeight()<MyVar.PIC_VALUE_MAX && imageBitmap.getHeight()<=imageBitmap.getWidth()) {
				int value = imageBitmap.getHeight();
				performCrop(value);
			}else if (imageBitmap.getWidth()<MyVar.PIC_VALUE_MAX && imageBitmap.getWidth()<=imageBitmap.getHeight()) {
				int value = imageBitmap.getWidth();
				performCrop(value);
			}
		}else if(requestCode==MyVar.PIC_CROP && resultCode==getActivity().RESULT_OK){
			Bundle extras = data.getExtras();
			imageBitmap = extras.getParcelable("data");
			ivLogo_parqueo.setImageBitmap(imageBitmap);
		}
	}
	
	private void performCrop(int value){
		try{
			Intent intentCrop = new Intent("com.android.camera.action.CROP");
			intentCrop.setDataAndType(imageUri, "image/*");
			intentCrop.putExtra("crop", true);
			intentCrop.putExtra("aspectX", 1);
			intentCrop.putExtra("aspectY", 1);
			intentCrop.putExtra("outputX", value);
			intentCrop.putExtra("outputY", value);
			intentCrop.putExtra("return-data", true);
			startActivityForResult(intentCrop, MyVar.PIC_CROP);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
			Toast.makeText(getActivity(), "Dispositivo no soporta recorte de imagen", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void validarIniciarRegistroParqueo(){
		int telf = 0;
		String direccion = MyVar.NO_ESPECIFICADO;
		
		int tolerancia = 0;
		if(!etTolerancia.getText().toString().equals("")){
			tolerancia = Integer.parseInt(etTolerancia.getText().toString());
		}
		int capacidad = 0;
		if(!etCapacidad.getText().toString().equals("")){
			capacidad = Integer.parseInt(etCapacidad.getText().toString());
		}
		double precio_hora_dia = 0;
		if(!etPrecioHoraDia.getText().toString().equals("")){
			precio_hora_dia = Double.parseDouble(etPrecioHoraDia.getText().toString());
		}
		double precio_noche = 0;
		if(!etPrecioNoche.getText().toString().equals("")){
			precio_noche = Double.parseDouble(etPrecioNoche.getText().toString());
		}
		
		double precio_contrato_nocturno = 0; String pc_nocturno = etPrecioContratoNocturno.getText().toString();
		if(!pc_nocturno.equals("")){
			precio_contrato_nocturno = Double.parseDouble(pc_nocturno);
		}
		
		double precio_contrato_diurno = 0; String pc_diurno = etPrecioContratoDiurno.getText().toString();
		if(!pc_diurno.equals("")){
			precio_contrato_diurno = Double.parseDouble(pc_diurno);
		}
		
		double precio_contrato_dia_completo = 0; String pc_dia_completo = etPrecioContratoDiaCompleto.getText().toString();
		if(!pc_dia_completo.equals("")){
			precio_contrato_dia_completo = Double.parseDouble(pc_dia_completo);
		}
		
		Time inicio_dia = (Time)tvIniciaDia.getTag();
		Time fin_dia = (Time)tvFinDia.getTag();
		Time inicio_noche = (Time)tvInicioNoche.getTag();
		Time fin_noche = (Time)tvFinNoche.getTag();
		
		if(!this.pathImageParqueo.equals(MyVar.NO_ESPECIFICADO)){
			copySaveImage();
		}
				
		String nombre = etNombre_parqueo.getText().toString().trim();
		if(!nombre.equals("") && nombre.length()>3){
			if(precio_hora_dia>0){
				if(precio_noche>0){
					if(tolerancia>0 && tolerancia<=50){
						if(capacidad>=5 && capacidad<=100){
							if(cbContratoNocturno.isChecked() && precio_contrato_nocturno!=0 || !cbContratoNocturno.isChecked() && precio_contrato_nocturno==0){
								
								if(cbContratoDiurno.isChecked() && precio_contrato_diurno!=0 || !cbContratoDiurno.isChecked() && precio_contrato_diurno==0){
									
									if(cbContratoDiaCompleto.isChecked() && precio_contrato_dia_completo!=0 || 
											!cbContratoDiaCompleto.isChecked() && precio_contrato_dia_completo==0){
										
										if(NOMBRE_PARQUEO_DISPONIBLE){
											Parqueo nuevo_parqueo = new Parqueo(getIDParqueoGenerado(), nombre, telf, direccion,
													tolerancia, capacidad, precio_hora_dia, precio_noche, precio_contrato_nocturno, precio_contrato_diurno, 
													precio_contrato_dia_completo, inicio_dia, fin_dia, inicio_noche, fin_noche, this.pathImageParqueo);
											new RegistrarParqueo().execute(nuevo_parqueo);
										}else{
											etNombre_parqueo.requestFocus();
											etNombre_parqueo.setError("Nombre parqueo ya existe, introduzca otro por favor");
										}
										
									}else{
										etPrecioContratoDiaCompleto.requestFocus();
										etPrecioContratoDiaCompleto.setError("Es necesario el precio de contrato dia completo");
									}
								}else{
									etPrecioContratoDiurno.requestFocus();
									etPrecioContratoDiurno.setError("Es necesario el precio de contrato diurno");
								}
							}else{
								etPrecioContratoNocturno.requestFocus();
								etPrecioContratoNocturno.setError("Es necesario el precio de contrato nocturno");
							}
						}else{
							etCapacidad.requestFocus();
							etCapacidad.setError("Capacidad de parqueo es necesario, minimo 5 y maximo 100 carriles");
						}
					}else{
						etTolerancia.requestFocus();
						etTolerancia.setError("Tiempo de tolerancia es necesario, debe ser mayor a 0 y menor a 50 min");
					}
				}else{
					etPrecioNoche.requestFocus();
					etPrecioNoche.setError("Precio noche es necesario, debe ser mayor a 0 Bs.");
				}
			}else{
				etPrecioHoraDia.requestFocus();
				etPrecioHoraDia.setError("Precio hora dia es necesario, debe ser mayor a 0 Bs.");
			}
		}else{
			etNombre_parqueo.requestFocus();
			etNombre_parqueo.setError("Nombre del parqueo es necesario, debe tener mínimo 4 caracteres");
		}
	}
	
	public void validarIniciarActualizacionParqueo(){
		int telf = 0;
		if(!etTelf.getText().toString().equals("")){
			telf = Integer.parseInt(etTelf.getText().toString());
		}
		String direccion = MyVar.NO_ESPECIFICADO;
		if(!etDireccion.getText().toString().equals("")){
			direccion = etDireccion.getText().toString();
		}
		
		int tolerancia = 0;
		if(!etTolerancia.getText().toString().equals("")){
			tolerancia = Integer.parseInt(etTolerancia.getText().toString());
		}
		
		double precio_hora_dia = 0;
		if(!etPrecioHoraDia.getText().toString().equals("")){
			precio_hora_dia = Double.parseDouble(etPrecioHoraDia.getText().toString());
		}
		double precio_noche = 0;
		if(!etPrecioNoche.getText().toString().equals("")){
			precio_noche = Double.parseDouble(etPrecioNoche.getText().toString());
		}
		
		double precio_contrato_nocturno = 0; String pc_nocturno = etPrecioContratoNocturno.getText().toString();
		if(!pc_nocturno.equals("")){
			precio_contrato_nocturno = Double.parseDouble(pc_nocturno);
		}
		
		double precio_contrato_diurno = 0; String pc_diurno = etPrecioContratoDiurno.getText().toString();
		if(!pc_diurno.equals("")){
			precio_contrato_diurno = Double.parseDouble(pc_diurno);
		}
		
		double precio_contrato_dia_completo = 0; String pc_dia_completo = etPrecioContratoDiaCompleto.getText().toString();
		if(!pc_dia_completo.equals("")){
			precio_contrato_dia_completo = Double.parseDouble(pc_dia_completo);
		}
		
		Time inicio_dia = (Time)tvIniciaDia.getTag();
		Time fin_dia = (Time)tvFinDia.getTag();
		Time inicio_noche = (Time)tvInicioNoche.getTag();
		Time fin_noche = (Time)tvFinNoche.getTag();
		
		if(!pathImageParqueo.equals(parq_online.getLogo())){
			copySaveImage();
		}
		String nombre = etNombre_parqueo.getText().toString().trim();
		
		if(!nombre.equals("") && nombre.length()>=4){
			if(precio_hora_dia>0){
				if(precio_noche>0){
					if(tolerancia>0 && tolerancia<=50){
						if(cbContratoNocturno.isChecked() && precio_contrato_nocturno!=0 || !cbContratoNocturno.isChecked() && precio_contrato_nocturno==0){
							
							if(cbContratoDiurno.isChecked() && precio_contrato_diurno!=0 || !cbContratoDiurno.isChecked() && precio_contrato_diurno==0){
								
								if(cbContratoDiaCompleto.isChecked() && precio_contrato_dia_completo!=0 || 
										!cbContratoDiaCompleto.isChecked() && precio_contrato_dia_completo==0){
									if(NOMBRE_PARQUEO_DISPONIBLE){
										Parqueo parqueo = new Parqueo(parq_online.getIdparqueo(), nombre, telf, direccion,
												tolerancia, parq_online.getCapacidad(), precio_hora_dia, precio_noche, precio_contrato_nocturno, precio_contrato_diurno, 
												precio_contrato_dia_completo, inicio_dia, fin_dia, inicio_noche, fin_noche, this.pathImageParqueo);
										new ActualizarParqueo().execute(parqueo);
									}else{
										etNombre_parqueo.requestFocus();
										etNombre_parqueo.setError("Nombre parqueo ya existe, introduzca otro por favor");
									}
								}else{
									etPrecioContratoDiaCompleto.requestFocus();
									etPrecioContratoDiaCompleto.setError("Es necesario el precio de contrato dia completo");
								}
							}else{
								etPrecioContratoDiurno.requestFocus();
								etPrecioContratoDiurno.setError("Es necesario el precio de contrato diurno");
							}
						}else{
							etPrecioContratoNocturno.requestFocus();
							etPrecioContratoNocturno.setError("Es necesario el precio de contrato nocturno");
						}	
					}else{
						etTolerancia.requestFocus();
						etTolerancia.setError("Tiempo de tolerancia es necesario, debe ser mayor a 0 y menor a 50 min");
					}
				}else{
					etPrecioNoche.requestFocus();
					etPrecioNoche.setError("Precio noche es necesario, debe ser mayor a 0 Bs.");
				}
			}else{
				etPrecioHoraDia.requestFocus();
				etPrecioHoraDia.setError("Precio hora dia es necesario, debe ser mayor a 0 Bs.");
			}
		}else{
			etNombre_parqueo.requestFocus();
			etNombre_parqueo.setError("Nombre del parqueo es necesario, debe tener mínimo 4 caracteres");
		}
	}
	
	private class RegistrarParqueo extends AsyncTask<Parqueo, Void, Boolean>{
		Parqueo parqueo;
		Cliente cliente_default;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Registrando parqueo...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Parqueo... params) {
			parqueo = params[0];
			if(httpCustom.insertarParqueo(parqueo)){
				String id_client_default = new StringBuilder(MyVar.ID_CLIENT_DEFAULT).append(parqueo.getIdparqueo()).toString();
				cliente_default = new Cliente(id_client_default, MyVar.CI_CLIENT_DEFAULT, MyVar.SIN_PROPIETARIO, MyVar.NO_ESPECIFICADO, 0, 
												MyVar.NO_ESPECIFICADO, MyVar.NO_ESPECIFICADO, MyVar.NO_ESPECIFICADO, MyVar.FECHA_DEFAULT, MyVar.NO_ESPECIFICADO, 
												MyVar.NO_ELIMINADO, MyVar.CLIENTE_OCASIONAL, MyVar.FECHA_DEFAULT, parqueo.getIdparqueo());
				if(httpCustom.insertarCliente(cliente_default)){
					DBParqueo db = new DBParqueo(getActivity());
					try {
						db.openSQLite();
						if(db.insertarParqueo(parqueo)){
							if(db.insertarCliente(cliente_default)){
								return true;
							}
						}	
						db.closeSQLite();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return false;
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(result){
				if(!parqueo.getLogo().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(MyVar.FOLDER_IMAGES_PARQUEO+parqueo.getLogo());
				}
				Toast.makeText(getActivity(), "Parqueo registrado exitosamente", Toast.LENGTH_LONG).show();
				limpiarCampos();
				RegistrarCarriles regCarriles = new RegistrarCarriles();
				regCarriles.setParqueo(this.parqueo);
				regCarriles.execute(getCarrilesGenerados(this.parqueo));
			}else{
				pd.dismiss();
				DialogMensaje dMsj = new DialogMensaje("Parqueo no se pudo registrar correctamente, intente mas tarde..!"+cliente_default.getIdcliente()+"- "+cliente_default.getCi()+" - "+cliente_default.getNombre());
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	public List<Carril> getCarrilesGenerados(Parqueo p){
		List<Carril> carrilesGeneradas = new ArrayList<Carril>();
		for (int i = 0; i < p.getCapacidad(); i++) {
			Carril c = new Carril(p.getIdparqueo()+(i+1),i+1, MyVar.SI, MyVar.NO, MyVar.NO_ELIMINADO, p.getIdparqueo());
			carrilesGeneradas.add(c);
		}
		return carrilesGeneradas;
	}
	
	private class RegistrarCarriles extends AsyncTask<List<Carril>, Void, Integer>{
		List<Carril> lista_carriles;
		Parqueo parqueo;
		int cont = 0;
		
		public void setParqueo(Parqueo parqueo){
			this.parqueo = parqueo;
		}
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd.setMessage("Generando carriles..");
		}
		
		@Override
		protected Integer doInBackground(List<Carril>... params) {
			lista_carriles = params[0];
			DBParqueo db = new DBParqueo(getActivity());
			try {
				db.openSQLite();
				for (int i = 0; i < lista_carriles.size(); i++) {
					if(httpCustom.insertarCarril(lista_carriles.get(i))){
						if(db.insertarCarril(lista_carriles.get(i))){
							cont++;
						}
						
					}
				}
				db.closeSQLite();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cont;
		}
		
		@Override
		protected void onPostExecute(Integer result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result==parqueo.getCapacidad()){
				Toast.makeText(getActivity(), "Carriles generados exitosamente", Toast.LENGTH_LONG).show();
				listener.onContinuarParqueoClick(parqueo.getIdparqueo());
			}else{
				DialogMensaje dMsj = new DialogMensaje("No se generaron correctamente los carriles, intente mas tarde..!");
				dMsj.show(getFragmentManager(), "tagDM");
			}
		}
	}
	
	private class ActualizarParqueo extends AsyncTask<Parqueo, Void, Boolean>{
		Parqueo parqueo;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Actualizando parqueo...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected Boolean doInBackground(Parqueo... params) {
			parqueo = params[0];
			if(httpCustom.actualizarParqueo(parqueo)){
				DBParqueo db = new DBParqueo(getActivity());
				try {
					db.openSQLite();
					if(db.actualizarParqueo(parqueo)){
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
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			pd.dismiss();
			if(result){
				if(!parqueo.getLogo().equals(parq_online.getLogo()) && !parqueo.getLogo().equals(MyVar.NO_ESPECIFICADO)){
					new UploadImage().execute(MyVar.FOLDER_IMAGES_PARQUEO+parqueo.getLogo());
				}
				Toast.makeText(getActivity(), "Parqueo actualizado", Toast.LENGTH_SHORT).show();
				listener.onContinuarParqueoClick(parq_online.getIdparqueo()); //Esto actualiza es estado en el menu
				listener.onBackFromParqueoClick();
			}else{
				listener.onBackFromParqueoClick();
			}
		}
	}
	
	public boolean copySaveImage(){
		File ruta_images = new File(MyVar.FOLDER_IMAGES_PARQUEO);
		ruta_images.mkdirs();
		
		String name_image = getNameImageParqueo();
		File file_image_parqueo = new File(ruta_images, name_image);
		
		FileOutputStream fos;
		try{
			this.pathImageParqueo = file_image_parqueo.getName();
			fos = new FileOutputStream(file_image_parqueo);
			imageBitmap.compress(CompressFormat.JPEG, 90, fos);
			fos.flush();
			scanImage(file_image_parqueo.getAbsolutePath());
			if(file_image_parqueo.exists()){
				return true;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
		
	public void scanImage(final String imagen){
		new MediaScannerConnectionClient() {
			private MediaScannerConnection msc = null; {
				msc = new MediaScannerConnection(getActivity().getApplicationContext(), this);msc.connect();
			}
			public void onMediaScannerConnected() { 
				msc.scanFile(imagen, null);
			}
			public void onScanCompleted(String path, Uri uri) { 
				msc.disconnect();
			} 
			};
	}
	
	public interface OnParqueoClickListener{
		public void onContinuarParqueoClick(String idParqueo);
		public void onBackFromParqueoClick();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnParqueoClickListener) activity;	
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" Debe implementar OnParqueoClickListener");
		}
		
	}
	
	public void limpiarCampos(){
		etNombre_parqueo.getText().clear();
		etTelf.getText().clear();
		etDireccion.getText().clear();
		etTolerancia.getText().clear();
		etCapacidad.getText().clear();
		etPrecioHoraDia.getText().clear();
		etPrecioNoche.getText().clear();
		ivLogo_parqueo.setImageResource(R.drawable.ic_local_parking_white_48dp);
		pathImageParqueo = MyVar.NO_ESPECIFICADO;
		cbContratoNocturno.setChecked(false);
		cbContratoDiurno.setChecked(false);
		cbContratoDiaCompleto.setChecked(false);
		imageBitmap = null;
	}
	
	public String getRealPathFromURI(Uri contentUri){
		@SuppressWarnings("unused")
		String [] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getActivity().managedQuery(contentUri, null, null, null, null);	
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public String getNameImageParqueo(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "logo_parqueo_"+date+".jpg";
		return imageCode;
	}
	
	public String getIDParqueoGenerado(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
		String date = dateFormat.format(new java.util.Date());
		String imageCode = "parqueo_"+date;
		return imageCode;
	}
}
