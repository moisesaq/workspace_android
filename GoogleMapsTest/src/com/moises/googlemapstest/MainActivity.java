package com.moises.googlemapstest;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnQueryTextListener, OnActionExpandListener{
	
	//Localizacion actual de sucre
//	private final double CURRENT_LATITUDE = -19.0431;
//	private final double CURRENT_LONGITUDE = -65.2592;
	private final double CURRENT_LATITUDE = -19.037454303409937;
	private final double CURRENT_LONGITUDE = -65.26455581188202;
	private Location currentLocation = new Location("current");
	private LatLng latLng;
	private int vista = 0;
	GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniciarComponentes();
	}
	
	public void iniciarComponentes(){
		ActionBar actionBar = getSupportActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
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
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView search = (SearchView)MenuItemCompat.getActionView(searchItem);
		if(search!=null){
			//SearchListener searchL = new SearchListener();
			search.setOnQueryTextListener(this);
			MenuItemCompat.setOnActionExpandListener(searchItem, this);
		}else{
			Toast.makeText(MainActivity.this, "Error al cargar menu", Toast.LENGTH_SHORT).show();
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
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10F);
				googleMap.moveCamera(cameraUpdate);
				return true;
			case R.id.action_animar_mapa:
				CameraUpdate camUpdate2 = CameraUpdateFactory.newLatLngZoom(latLng, 5F);
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
	
	@Override
	public boolean onQueryTextChange(String text) {
		//Aca va ir viendo los cambios en la escritura
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String text) {
		Toast.makeText(MainActivity.this, "Buscando...", Toast.LENGTH_SHORT).show();
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
	
//	public class SearchListener implements OnQueryTextListener, OnActionExpandListener{
//
//		@Override
//		public boolean onQueryTextChange(String text) {
//			//Aca va ir viendo los cambios en la escritura
//			return false;
//		}
//
//		@Override
//		public boolean onQueryTextSubmit(String text) {
//			Toast.makeText(MainActivity.this, "Buscando...", Toast.LENGTH_SHORT).show();
//			return true;
//		}
//		
//		@Override
//		public boolean onMenuItemActionCollapse(MenuItem item) {
//			Toast.makeText(MainActivity.this, "CERRADO", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//
//		@Override
//		public boolean onMenuItemActionExpand(MenuItem item) {
//			Toast.makeText(MainActivity.this, "ABIERTO", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//	}

}
