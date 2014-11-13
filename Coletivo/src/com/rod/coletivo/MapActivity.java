package com.rod.coletivo;

import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rod.coletivo.R;
import com.rod.coletivo.auxiliar.Retorno;
import com.rod.rede.BuscaBusLinha;
import com.rod.util.ObjetosGlobais;
import com.rod.util.ParametrosGlobais;

public class MapActivity extends ActionBarActivity implements Retorno{
	private GoogleMap googleMap;
	LatLng GOIANIA;
	BuscaBusLinha bbl;
	String numero;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		numero = b.getString("numero");
		
		bbl = new BuscaBusLinha(this, this, ParametrosGlobais.ORIGEM_ACTIVITY);
		try {
			// Loading map
			initilizeMap();

		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	@SuppressLint("NewApi")
	private void initilizeMap() {
		
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.setMyLocationEnabled(true);

			bbl.execute(new String[]{numero});
			GOIANIA = new LatLng(ObjetosGlobais.lat, ObjetosGlobais.lng);
			googleMap.moveCamera(
					CameraUpdateFactory.newLatLngZoom(
							GOIANIA, 
							14.5f
							)
					);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void Trata(Object o) {
		// TODO Auto-generated method stub
		if ( Integer.parseInt(((SoapObject)o).getProperty("erro").toString()) > 0 ){
			return;
		}	

		Vector<?> lista = (Vector<?>) ((SoapObject)o).getProperty("buslinha");
		for(int i=0;i<lista.size();i++){
			SoapObject item = (SoapObject) lista.get(i);
			
			MarkerOptions mp = new MarkerOptions();
			mp.position(new LatLng(Double.parseDouble(item.getPropertyAsString("lat")), Double.parseDouble(item.getPropertyAsString("lng"))));
			mp.title(item.getPropertyAsString("numero")); 
			if(Integer.parseInt(item.getPropertyAsString("tempo")) < 1){
				mp.snippet("Agora mesmo");
			}
			else{
				if(Integer.parseInt(item.getPropertyAsString("tempo")) == 1){
					mp.snippet(item.getPropertyAsString("tempo")+" minuto");
				}
				else{
					mp.snippet(item.getPropertyAsString("tempo")+" minutos");
				}
			}
			mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus1));
			googleMap.addMarker(mp);
		}
	}
	
}
