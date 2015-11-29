package com.silvia.fragmentos;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.silvia.cooperativa.MyServiceLocation;
import com.silvia.cooperativa.R;
import com.silvia.dialogos.DialogListAddresses;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class MapSucre extends Fragment implements OnMapLongClickListener{

	//Localizacion actual de sucre
	private final double CURRENT_LATITUDE = -19.0283764;
	private final double CURRENT_LONGITUDE = -65.2637391;
		
	private View v;
	private GoogleMap googleMap;
	private TextView tvAddress, tvDescription;
	private ImageView ivMic;
	MyServiceLocation serviceLocation;
	public LatLng latLng;
	OnMapSucreClickListener callBack;
	
	public Marker marker;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.map_sucre, container, false);
		getActivity().getActionBar().setTitle("Sucre Google Maps");
		setHasOptionsMenu(true);
		managementGoogleMaps();
		serviceLocation = new MyServiceLocation(getActivity().getApplicationContext());
		tvAddress = (TextView)v.findViewById(R.id.tvAddressMapSucre);
		tvDescription = (TextView)v.findViewById(R.id.tvAddressDescriptionMapSucre);
		ivMic = (ImageView)v.findViewById(R.id.ivMicMapSucre);
		ivMic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(serviceLocation.canGetLocation()){
//					setCurrentLocation(serviceLocation.getLatitude(), serviceLocation.getLongitude());
//					Toast.makeText(getActivity(), "Latitud: "+serviceLocation.getLatitude()+" Longitud: "+serviceLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//				}else{
//					Toast.makeText(getActivity(), "No se puede obtener location", Toast.LENGTH_SHORT).show();
//				}
				if(marker!=null){
					callBack.setAddressPedido(marker.getPosition().latitude, marker.getPosition().longitude);
				}else{
					Toast.makeText(getActivity(), "Primero debes seleccioar tu ubicacion", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_search_address).setVisible(false);
		MenuItem searchItem = menu.findItem(R.id.action_search_address);
		SearchView searchView = (SearchView)searchItem.getActionView();
		searchView.setQueryHint("Direccion");
		searchView.setOnQueryTextListener(new SearchListener());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return super.onOptionsItemSelected(item);
	}
	
	public void managementGoogleMaps(){
		FragmentManager fm = getFragmentManager();
		MapFragment mf = (MapFragment)fm.findFragmentById(R.id.myGoogleMap);
		googleMap = mf.getMap();
		if(googleMap!=null){
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
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
			googleMap.moveCamera(cameraUpdate);
		}
	}
	
	@Override
	public void onMapLongClick(LatLng latLng) {
		Geocoder geocoder = new Geocoder(getActivity());
		List<Address> list = null;
		try {
			list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(list!=null){
			Address address = list.get(0);
			if(marker!=null){
				marker.remove();	
			}
			MarkerOptions markerOoptions = new MarkerOptions()
								.title(address.getLocality())
								.position(latLng);
			marker = googleMap.addMarker(markerOoptions);
			
			if(address.getAddressLine(0)!=null){
				tvAddress.setText(address.getAddressLine(0));
			}
		}
	}

	public void setCurrentLocation(double latitude, double longitude){
		try{
			Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
			List<Address> list = geocoder.getFromLocation(latitude, longitude, 1000);
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
			}else{
				Toast.makeText(getActivity(), "No se puedo encontro tu ubicacion", Toast.LENGTH_SHORT).show();
			}
		}catch(IOException e){
			e.printStackTrace();
			Log.d("Error moises", e.toString());
		}
	}
	
	public class MyLocationListener implements LocationListener{
		
		@Override
		public void onLocationChanged(Location location) {
			tvDescription.setText("Localizacion: Latitud: "+location.getLatitude()+" Longitud: "+location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider) {
			tvAddress.setText("GPS desactivado");
		}

		@Override
		public void onProviderEnabled(String provider) {
			tvAddress.setText("Se activo");
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
	
	public interface OnMapSucreClickListener{
		public void setAddressPedido(double latitude, double longitude);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			callBack = (OnMapSucreClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" debe implementar metodo de OnMapSucreClickListener");
		}
	}
	
	//Devuelve las direcciones parecidas al buscado
	public List<Address> getAddresses(String textAddress){
		final double lowerLeftLatitude= CURRENT_LATITUDE-1;
		final double lowerLeftLongitude = CURRENT_LATITUDE-1;
		final double upperRightLatitude = CURRENT_LATITUDE+1;
		final double upperRightLongitude = CURRENT_LONGITUDE+1;
		Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
		try {
			List<Address> list_addresses = geocoder.getFromLocationName(textAddress, 1000, lowerLeftLatitude, lowerLeftLongitude
																					 		, upperRightLatitude , upperRightLongitude);
			return list_addresses;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Localiza la direccion con Latitud y longitud
	public void setLocation(Address address){
		LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
		MarkerOptions marketOptions = new MarkerOptions();
		marketOptions.position(latLng);
		marketOptions.title(address.getAddressLine(0));
		marketOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
		googleMap.addMarker(marketOptions);
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
											.target(latLng)
											.zoom(16)
											.tilt(60)
											.build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	public class SearchListener implements OnQueryTextListener{

		@Override
		public boolean onQueryTextChange(String text) {
			//Aca va ir viendo los cambios en la escritura
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String text) {
			if(text.length()>3){
				Toast.makeText(getActivity(), "Buscando... "+text, Toast.LENGTH_SHORT).show();
				List<Address> list = getAddresses(text);
				if(!list.isEmpty() && list!=null){
					DialogListAddresses dialogAddresses = new DialogListAddresses(list, MapSucre.this);
					dialogAddresses.show(getFragmentManager(), "tagAddresses");
				}else{
					Toast.makeText(getActivity(), "No se encontro la direccion", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getActivity(), "Introduzca direccion", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	}

	
	

		
}
