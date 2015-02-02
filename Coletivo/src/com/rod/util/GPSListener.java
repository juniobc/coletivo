package com.rod.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.rod.coletivo.auxiliar.Retorno;

public class GPSListener implements android.location.LocationListener {
	Context activity;
	LocationManager locationManager;	
	long ultimo_tempo = 0;
	
	Retorno ret;

	public GPSListener(Context context, Retorno ret){
		this.activity = context;
		this.ret = ret;
		locationManager = (LocationManager)	this.activity.getSystemService(Context.LOCATION_SERVICE);		
	}
	public void onLocationChanged(Location location) {
		ObjetosGlobais.lat = location.getLatitude();
		ObjetosGlobais.lng = location.getLongitude();
		ObjetosGlobais.precisao = location.getAccuracy();
		ObjetosGlobais.latLng_valida = true;		
	}
	public void onStatusChanged(String s, int i, Bundle b) {
	}
	public void onProviderDisabled(String s) {
	}
	public void onProviderEnabled(String s) {
	}
	public void setGPSParams(Long intervalo_tempo){
		try{
			locationManager.removeUpdates(this);
		}
		catch(NullPointerException e){
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"[setGpsParams]->"+e.toString());
		}

		/*locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				intervalo_tempo, 
				ParametrosGlobais.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
				this
				);*/
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
						activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton("N�o", new DialogInterface.OnClickListener() {
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