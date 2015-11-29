package com.silvia.cooperativa;


import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class MyServiceLocation extends Service implements LocationListener{
	Context context;
	boolean canGetLocation = false;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000*60*1;
	private static final long MIN_TIME_UPDATES = 10;
	private LocationManager locationManager;
	private Location location;
	private double latitude;
	private double longitude;
		
	public MyServiceLocation(Context context){
		super();
		this.context = context;
		getLocation();
	}
	
	public Location getLocation(){
		try {
			locationManager = (LocationManager)this.context.getSystemService(LOCATION_SERVICE);
			 isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			 isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if(!isGPSEnabled && !isNetworkEnabled){
				
			}else{
				this.canGetLocation = true;
				if(isGPSEnabled){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					if(locationManager!=null){
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location!=null){
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				if(isNetworkEnabled){
					if(location!=null){
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if(locationManager!=null){
							location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if(location!=null){
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}				
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return location;
	}
	
	public double getLatitude() {
		if(location!=null){
			latitude = location.getLatitude();
		}
		return latitude;
	}
	
	public double getLongitude() {
		if(location!=null){
			longitude = location.getLongitude();
		}
		return longitude;
	}
	
	public boolean canGetLocation(){
		return this.canGetLocation;
	}
	
	public void showSettingsAlert(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Configurar GPS");
		dialog.setMessage("GPS is not enabled, Do you want go to settings?");
		dialog.create().show();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onLocationChanged(Location location) {
//		if(location!=null){
//			this.location = location;
//			Toast.makeText(context, "Latitud: "+location.getLatitude()+" Longitud: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
//		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(context,"GPS desactivado", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(context,"GPS activado", Toast.LENGTH_SHORT).show();
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
