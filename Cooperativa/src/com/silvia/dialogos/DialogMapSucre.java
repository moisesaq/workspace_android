package com.silvia.dialogos;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.silvia.cooperativa.MyServiceLocation;
import com.silvia.cooperativa.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class DialogMapSucre extends DialogFragment implements OnMapLongClickListener, OnClickListener{

	//Localizacion actual de sucre
	private final double CURRENT_LATITUDE = -19.0283764;
	private final double CURRENT_LONGITUDE = -65.2637391;
		
	private View v;
	private EditText etAddress;
	private TextView tvDescription;
	private GoogleMap googleMap;
	private ImageView ivSendLocationAddress;
	MyServiceLocation serviceLocation;
	
	public LatLng latLng;
	public double latitude, longitude;
	private EditText editText;
	private ImageView imageView;
	private Activity activity;
	private String txtAddress;
	private Marker marker;
	private MapFragment mf;
	
	public DialogMapSucre(EditText editText, ImageView imageView, Activity activity){
		this.editText = editText;
		this.imageView = imageView;
		this.activity = activity;
	}
	
	public DialogMapSucre(String txtAddress, double latitude, double longitude, Activity activity){
		this.txtAddress = txtAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.activity = activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		//LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = activity.getLayoutInflater().inflate(R.layout.dialog_map_sucre, null);
		managementGoogleMaps();
		serviceLocation = new MyServiceLocation(getActivity().getApplicationContext());
		etAddress = (EditText)v.findViewById(R.id.etAddressDialogMapSucre);
		tvDescription = (TextView)v.findViewById(R.id.tvDescriptionDialogMapSucre);
		ivSendLocationAddress = (ImageView)v.findViewById(R.id.ivSendLocationAddressDialogMapSucre);
		ivSendLocationAddress.setOnClickListener(this);
		if(editText==null){
			addMarkerCustom(this.txtAddress, this.latitude, this.longitude);
		}
		dialog.setView(v);
		return dialog.create();
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog d = getDialog();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		d.getWindow().setAttributes(lp);
	}
	
	public void managementGoogleMaps(){
		FragmentManager fm = activity.getFragmentManager();
		mf = (MapFragment)fm.findFragmentById(R.id.myDialogGoogleMapSucre);
		if(googleMap==null){
			googleMap = mf.getMap();
			googleMap.setOnMapLongClickListener(this);
			googleMap.getUiSettings().setCompassEnabled(true);
	        googleMap.getUiSettings().setZoomControlsEnabled(true);
	        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	        googleMap.getUiSettings().setAllGesturesEnabled(true);
	        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
	        googleMap.getUiSettings().setRotateGesturesEnabled(true);
	        googleMap.getUiSettings().setTiltGesturesEnabled(true);
	        googleMap.getUiSettings().setZoomGesturesEnabled(true);
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			latLng = new LatLng(CURRENT_LATITUDE, CURRENT_LONGITUDE);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
			googleMap.moveCamera(cameraUpdate);
		}
	}
	
	public void addMarkerCustom(String txtAddress, double latitude3, double longitude3){
		if(marker!=null){
			marker.remove();	
		}
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.title(txtAddress);
		markerOptions.snippet("Sucre, Bolivia");
		//markerOptions.snippet(latitude3+", "+longitude3);
		LatLng currentLatLng = new LatLng(latitude3, longitude3);
		markerOptions.position(currentLatLng);
		marker = googleMap.addMarker(markerOptions);
		tvDescription.setText("Direccion de entrega: "+txtAddress);
		//tvDescription.setTypeface(Bold);
		etAddress.setVisibility(View.GONE);
		ivSendLocationAddress.setVisibility(View.GONE);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 16);
		googleMap.moveCamera(cameraUpdate);
	}
	
	@Override
	public void onMapLongClick(LatLng latLng2) {
		Geocoder geocoder = new Geocoder(getActivity());
		List<Address> list = null;
		try {
			list = geocoder.getFromLocation(latLng2.latitude, latLng2.longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(list!=null){
			Address address = list.get(0);
			if(marker!=null){
				marker.remove();	
			}
			MarkerOptions markerOptions = new MarkerOptions();
			if(address.getAddressLine(0)!=null){
				etAddress.setText(address.getAddressLine(0));
				markerOptions.title(address.getAddressLine(0));
			}else{
				etAddress.setText(address.getLocality());
				markerOptions.title(address.getLocality());
			}
			markerOptions.snippet(latLng2.latitude+", "+latLng2.longitude);
			markerOptions.position(latLng2);
			editText.setTag(latLng2.latitude);
			imageView.setTag(latLng2.longitude);
			marker = googleMap.addMarker(markerOptions);
			
			String description = "";
			String[] data = {address.getAdminArea(), address.getLocality(), address.getCountryName(), address.getCountryCode()};
			for (int i = 0; i < data.length; i++) {
				if(data[i]!=null){
					description += data[i];
					if((i+1)!=data.length)
						description += ", ";
				}
			}
			tvDescription.setVisibility(View.VISIBLE);
			tvDescription.setText(description);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==ivSendLocationAddress.getId()){
			if(marker!=null){
				editText.setText(etAddress.getText().toString());
				this.dismiss();
			}else{
				Toast.makeText(getActivity(), "Primero debes seleccionar la ubicacion", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(mf!=null){
			activity.getFragmentManager().beginTransaction().remove(mf).commit();
		}
	}
	
}
