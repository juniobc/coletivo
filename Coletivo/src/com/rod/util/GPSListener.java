package com.rod.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;

import com.example.coletivo.R;
import com.rod.coletivo.MainActivity;
import com.rod.coletivo.MapActivity;

public class GPSListener implements android.location.LocationListener {
	Activity activity;
	LocationManager locationManager;	

	public GPSListener(Context context){
		this.activity = (Activity)context;
		locationManager = (LocationManager)	this.activity.getSystemService(Context.LOCATION_SERVICE);		
	}
	public void onLocationChanged(Location location) {
		MainActivity.lat = location.getLatitude();
		MainActivity.lng = location.getLongitude();
		MapActivity.lat = location.getLatitude();
		MapActivity.lng = location.getLongitude();
		EditText et_lat = (EditText) activity.findViewById(R.id.et_lat);
		et_lat.setText(String.valueOf(location.getLatitude()));
		EditText et_lng = (EditText) activity.findViewById(R.id.et_lng);
		et_lng.setText(String.valueOf(location.getLongitude()));
		activity.setTitle(
				activity.getResources().getString(R.string.app_name) + " [ " + 
						String.valueOf(location.getAccuracy()) + " ] " +
						String.valueOf(location.getSpeed()*3.6) + " km/h"		
				);
	}
	public void onStatusChanged(String s, int i, Bundle b) {
		//Toast.makeText(MainActivity.this, "Provider status changed",Toast.LENGTH_SHORT).show();
	}
	public void onProviderDisabled(String s) {
		//Toast.makeText(MainActivity.this,"Provider disabled by the user. GPS turned off",Toast.LENGTH_SHORT).show();
	}
	public void onProviderEnabled(String s) {
		//System.out.println("==onProviderEnabled=" + s);
		//Toast.makeText(MainActivity.this, "Provider enabled by the user. GPS turned on",Toast.LENGTH_SHORT).show();
	}
	public void setGPSParams(Long intervalo_tempo){
		try{
			locationManager.removeUpdates(this);
		}
		catch(NullPointerException e){

		}

		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				intervalo_tempo, 
				ParametrosGlobais.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
				this
				);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 
				intervalo_tempo, 
				ParametrosGlobais.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, 
				this
				);    	
	}
	public boolean ativo(){
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(" GPS desativado, deseja ativar?")
				.setCancelable(false)
				.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}
				});
			final AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
		return true;
	}

}