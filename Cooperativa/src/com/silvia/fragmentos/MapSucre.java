package com.silvia.fragmentos;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.silvia.cooperativa.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapSucre extends Fragment{

	//Localizacion actual de sucre
	private final double CURRENT_LATITUDE = -19.037454303409937;
	private final double CURRENT_LONGITUDE = -65.26455581188202;
		
	private View v;
	private GoogleMap google_map;
	private TextView tvAddress, tvDescription;
	private ImageView ivMic;
	private LocationManager location_manager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.map_sucre, container, false);
		tvAddress = (TextView)v.findViewById(R.id.tvAddressMapSucre);
		tvDescription = (TextView)v.findViewById(R.id.tvAddressDescriptionMapSucre);
		ivMic = (ImageView)v.findViewById(R.id.ivMicMapSucre);
		ivMic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				location_manager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
				if(location_manager!=null){
					managementGoogleMaps();
					setCurrentLocation();
				}else{
					Toast.makeText(getActivity(), "manager null", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
//		if(isEnableGPS()){
//			location_manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//			managementGoogleMaps();
//			//setCurrentLocation();
//		}else{
//			Toast.makeText(getActivity(), "ACTIVE SU GPS", Toast.LENGTH_SHORT).show();
//		}
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return super.onOptionsItemSelected(item);
	}
	
	public void managementGoogleMaps(){
		FragmentManager fm = getFragmentManager();
		MapFragment mf = (MapFragment)fm.findFragmentById(R.id.myGoogleMap);
		google_map = mf.getMap();
		google_map.getUiSettings().setCompassEnabled(true);
        google_map.getUiSettings().setZoomControlsEnabled(true);
        google_map.getUiSettings().setMyLocationButtonEnabled(true);
        google_map.getUiSettings().setAllGesturesEnabled(true);
        google_map.getUiSettings().setMyLocationButtonEnabled(true);
        google_map.getUiSettings().setRotateGesturesEnabled(true);
        google_map.getUiSettings().setTiltGesturesEnabled(true);
        google_map.getUiSettings().setZoomGesturesEnabled(true);
		google_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyLocationListener());
	}
	
	public void setCurrentLocation(){
		Location location = location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location!=null){
			location = location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if(location!=null){
			try{
				Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
				List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				if(!list.isEmpty()){
					Address address = list.get(0);
					tvAddress.setText(address.getAddressLine(0));
					StringBuilder description = new StringBuilder();
					if(address.getLocality()!=null){
						description.append(address.getLocality()).append(", ");
					}
					if(address.getAdminArea()!=null){
						description.append(address.getAdminArea()).append(", ");
					}
					if(address.getCountryName()!=null){
						description.append(address.getCountryName());
					}
					tvDescription.setText(description);
				}
			}catch(IOException e){
				e.printStackTrace();
				Log.d("Error moises", e.toString());
			}
			
		}else{
			Toast.makeText(getActivity(), "No se pudo encontrar tu ubicacion", Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean isEnableGPS(){
		location_manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		if(location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			return true;
		}
		return false;
	}
	
	public class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			tvDescription.setText("Se movio");
		}

		@Override
		public void onProviderDisabled(String provider) {
			tvDescription.setText("GPS desactivado");
		}

		@Override
		public void onProviderEnabled(String provider) {
			tvDescription.setText("Se activo");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Este metodo se ejecuta cada vez que se detecta un cambio en el
			// status del proveedor de localizacion (GPS)
			// Los diferentes Status son:
			// OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
			// TEMPORARILY_UNAVAILABLE -> Temp˜ralmente no disponible pero se
			// espera que este disponible en breve
			// AVAILABLE -> Disponible
		}
	}

}
