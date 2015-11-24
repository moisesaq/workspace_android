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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnTouchListener{
	
	//Localizacion actual de sucre
	private final double CURRENT_LATITUDE = -19.037454303409937;
	private final double CURRENT_LONGITUDE = -65.26455581188202;
	private Location currentLocation = new Location("current");
	private LatLng latLng;
	private int vista = 0;
	
	private ImageView ivMic;
	private TextView tvAddress, tvDescription;
	GoogleMap googleMap;
	
	private GestureDetector detector;
	private boolean isMoving = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniciarComponentes();
	}
	
	public void iniciarComponentes(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		tvAddress = (TextView)findViewById(R.id.tvAddress);
		tvDescription = (TextView)findViewById(R.id.tvAddressDescription);
		
		ivMic = (ImageView)findViewById(R.id.ivMic);
		
		ivMic.setOnClickListener(new DoubleClickListener() {
			
			@Override
			public void onSingleClick(View v) {
				tvAddress.setText("Click");
			}
			
			@Override
			public void onDoubleClick(View v) {
				if(!isMoving){
					tvAddress.setText("Double click se activo");
				}else{
					tvAddress.setText("Double click NO se activo");
				}
				
			}
		});
		ivMic.setOnTouchListener(MainActivity.this);
//		detector = new GestureDetector(new MyGestureListener());
//		ivMic.setOnTouchListener(this);
		
		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment map_frag = (SupportMapFragment)fm.findFragmentById(R.id.myGoogleMap);
		if(map_frag!=null){
			googleMap = map_frag.getMap();	
		}else{
			Toast.makeText(getApplicationContext(), "Cargado", Toast.LENGTH_SHORT).show();
		}
		
		latLng = new LatLng(CURRENT_LATITUDE, CURRENT_LONGITUDE);
	}
	
	private OnClickListener ivClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnTouchListener ivTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				tvDescription.setText("Presionaste");
				return true;
			case MotionEvent.ACTION_MOVE:
				isMoving = true;
				tvDescription.setText("Moviste");
				return true;
			case MotionEvent.ACTION_UP:
				isMoving = false;
				tvDescription.setText("Soltaste");
				return true;
		}
			return false;
		}
	}; 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView search = (SearchView)MenuItemCompat.getActionView(searchItem);
		if(search!=null){
			//SearchListener searchL = new SearchListener();
//			search.setOnQueryTextListener(this);
//			MenuItemCompat.setOnActionExpandListener(searchItem, this);
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
	public boolean onTouch(View v, MotionEvent event) {
//		if(v.getId()==ivMic.getId()){
//			if(detector.onTouchEvent(event)){
//				tvDescription.setText("Presionaste");
//			}else{
//				tvDescription.setText("dgsdgsdfsdf");
//			}
//		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				tvDescription.setText("Presionaste");
				return true;
			case MotionEvent.ACTION_MOVE:
				isMoving = true;
				tvDescription.setText("Moviste");
				return true;
			case MotionEvent.ACTION_UP:
				isMoving = false;
				tvDescription.setText("Soltaste");
				return true;
		}
		return false;
	}

	public abstract class DoubleClickListener implements OnClickListener{

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
	}
	
	public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return super.onDoubleTapEvent(e);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return super.onDown(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			super.onLongPress(e);
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return true;
		}
		
	}
	
//	public class MyTouchListener implements OnTouchListener{
//	GestureDetectorCompat mDetector = new GestureDetectorCompat(MainActivity.this, new MyGestureListener());
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				tvAddress.setText("Presionaste");
//				return true;
//			case MotionEvent.ACTION_MOVE:
//				tvAddress.setText("Moviste");
//				return true;
//			case MotionEvent.ACTION_UP:
//				tvAddress.setText("Soltaste");
//				return true;
//		}
//		return this.mDetector.onTouchEvent(event);
//	}
//}
	
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
