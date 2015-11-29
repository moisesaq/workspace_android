package com.moises.googlemapstest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.SearchManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{
	
	//Localizacion actual de sucre
	private final double CURRENT_LATITUDE = -19.037454303409937;
	private final double CURRENT_LONGITUDE = -65.26455581188202;
	private Location currentLocation = new Location("current");
	private LatLng latLng;
	private int vista = 0;
	
	public ImageView ivMic;
	private TextView tvAddress, tvDescription;
	GoogleMap googleMap;
	
	private GestureDetector myDetector;
	private boolean isLongPress;
	SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		myDetector = new GestureDetector(this, new MyGestureListener());
		tvAddress = (TextView)findViewById(R.id.tvAddress);
		tvDescription = (TextView)findViewById(R.id.tvAddressDescription);
		
		ivMic = (ImageView)findViewById(R.id.ivMic);
		
		ivMic.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myDetector.onTouchEvent(event);
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(isLongPress){
						tvAddress.setText("Precionado largo rato");
					}
				}
				return true;
			}
		});
		
		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment map_frag = (SupportMapFragment)fm.findFragmentById(R.id.myGoogleMap);
		if(map_frag!=null){
			googleMap = map_frag.getMap();	
		}else{
			Toast.makeText(getApplicationContext(), "Cargado", Toast.LENGTH_SHORT).show();
		}
		
		latLng = new LatLng(CURRENT_LATITUDE, CURRENT_LONGITUDE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_buscar);
		SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
		
		searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
		
		if(searchView!=null){
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setQueryHint("Direccion Ej.: Junin 100");
			searchView.setIconified(true);
			SearchListener searchListener = new SearchListener();
			searchView.setOnQueryTextListener(searchListener);
			//MenuItemCompat.setOnActionExpandListener(searchItem, searchListener);
		}else{
			Toast.makeText(MainActivity.this, "Error la cargar SearchView", Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.action_cambiar_vista:
				cambiarVistaMapa();
				return true;
			case R.id.action_mover_vista:
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
				googleMap.moveCamera(cameraUpdate);
				return true;
			case R.id.action_animar_mapa:
				CameraUpdate camUpdate2 = CameraUpdateFactory.newLatLngZoom(latLng, 7);
				googleMap.animateCamera(camUpdate2);
				return true;
			case R.id.action_ver_3d:
				CameraPosition camPos = new CameraPosition.Builder()
				.target(latLng)
				.zoom(19)
				.bearing(45)
				.tilt(70)
				.build();
				CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
				googleMap.animateCamera(camUpdate);
				return true;
			case R.id.action_position:
				CameraPosition cam_pos = googleMap.getCameraPosition();
				LatLng pos = cam_pos.target;
				Toast.makeText(MainActivity.this, "Latitud: "+pos.latitude+" Longitud: "+pos.latitude, Toast.LENGTH_LONG).show();
				return true;
			case R.id.action_voice:
				Toast.makeText(MainActivity.this, "Google Voice", Toast.LENGTH_LONG).show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cambiarVistaMapa(){
		vista = (vista+1)%4;
		switch (vista) {
			case 0:
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case 2:
				googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 3:
				googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
		}
	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
		
	    @Override
	    public boolean onDown(MotionEvent e) {
	        switch (e.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	                isLongPress = false;
	                tvAddress.setText("Precionante...");
	                break;
	            case MotionEvent.ACTION_UP:
	                isLongPress = false;
	                tvAddress.setText("Soltaste...");
	                break;
	        }
	        return false;
	    }

	    @Override
	    public boolean onSingleTapConfirmed(MotionEvent e) {
	        switch (e.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	                tvAddress.setText("Click");
	                break;
	            case MotionEvent.ACTION_UP:
	                tvAddress.setText("Simple click");
	                isLongPress = false;
	                break;
	        }
	        return false;
	    }
	    
		public boolean onDoubleTapEvent(MotionEvent e) {
			switch (e.getAction()) {
	            case MotionEvent.ACTION_UP:
	            	tvAddress.setText("Doble click");
	                break;
	        }
			return false;
		}
		
		@Override
	    public void onLongPress(MotionEvent e) {
	        switch (e.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	            	tvAddress.setText("Largo precionando...");
	                isLongPress = true;
	                break;
	        }
	    }
	
	}
	
	public class SearchListener implements OnQueryTextListener, OnActionExpandListener{

		@Override
		public boolean onQueryTextChange(String text) {
			//Aca va ir viendo los cambios en la escritura
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String text) {
			if(text.length()>3){
				Toast.makeText(MainActivity.this, "Buscando... "+text, Toast.LENGTH_SHORT).show();
				List<Address> list = getAddresses(text);
				if(!list.isEmpty() && list!=null){
					ListAddressDialog dialogAddresses = new ListAddressDialog(list, MainActivity.this);
					dialogAddresses.show(getSupportFragmentManager(), "tagAddresses");
				}else{
					Toast.makeText(MainActivity.this, "No se encontro la direccion", Toast.LENGTH_SHORT).show();
				}
				
			}else{
				Toast.makeText(MainActivity.this, "Introduzca direccion", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		
		@Override
		public boolean onMenuItemActionCollapse(MenuItem item) {
			Toast.makeText(MainActivity.this, "CERRADO", Toast.LENGTH_SHORT).show();
			return false;
		}

		@Override
		public boolean onMenuItemActionExpand(MenuItem item) {
			Toast.makeText(MainActivity.this, "ABIERTO", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	//Devuelve las direcciones parecidas al buscado
	public List<Address> getAddresses(String textAddress){
		final double lowerLeftLatitude= CURRENT_LATITUDE-1;
		final double lowerLeftLongitude = CURRENT_LATITUDE-1;
		final double upperRightLatitude = CURRENT_LATITUDE+1;
		final double upperRightLongitude = CURRENT_LONGITUDE+1;
		Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
	
	/*public abstract class DoubleClickListener implements OnClickListener{

	private static final long DOUBLE_CLICK_TIME_DELTA = 400;
	long lastClickTime = 0;
	
	@Override
	public void onClick(View v) {
		long clickTime = System.currentTimeMillis();
		if(clickTime-lastClickTime < DOUBLE_CLICK_TIME_DELTA){
			onDoubleClick(v);
		}else{
			onSingleClick(v);
		}
		lastClickTime = clickTime;
	}
	
	public abstract void onSingleClick(View v);
	public abstract void onDoubleClick(View v);
}*/

}
