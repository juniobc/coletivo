package com.rod.coletivo;

import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.rod.coletivo.auxiliar.LinhaAdapter;
import com.rod.coletivo.auxiliar.Retorno;
import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.rede.BuscaBusLinha;
import com.rod.service.TimeService;
import com.rod.util.GPSListener;
import com.rod.util.ParametrosGlobais;


public class MainActivity extends ActionBarActivity implements Retorno {
	EditText et_numero;
	ListView et_resultado;
	TextView tv_dist;
	SeekBar sb_dist;
	Button btn_busca_linha, btn_gravado, btn_mapa;
	GPSListener MLL;
	MySQLitePossivelLinhaHelper dbPL;
	
	BuscaBusLinha bbl;
	ImageButton img_btn_buslinha;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.teste);

		MLL = new GPSListener(this,this);    	
		MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);


		et_resultado = (ListView) findViewById(R.id.et_resultado);
		et_numero = (EditText) findViewById(R.id.et_numero);
		
		img_btn_buslinha = (ImageButton) findViewById(R.id.img_btn_buslinha);
		img_btn_buslinha.setOnClickListener(new OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_numero.getText().toString().length() > 0){
					bbl = new BuscaBusLinha(MainActivity.this, MainActivity.this, ParametrosGlobais.ORIGEM_ACTIVITY);
					bbl.execute(new String[]{et_numero.getText().toString()});
					
				}
				else{
					Toast.makeText(MainActivity.this, "Informe o número da linha", Toast.LENGTH_SHORT).show();
				}	
			}
		});
		
		dbPL = new MySQLitePossivelLinhaHelper(this);	
		//checaTabela();
		
		if(!TimeService.ativo) { 
		    //Toast.makeText(getBaseContext(), "Serviço já está ativo", Toast.LENGTH_SHORT).show();
		//}else {			
		    //Toast.makeText(getBaseContext(), "Iniciando serviço...", Toast.LENGTH_SHORT).show();
		    Intent service = new Intent(this, TimeService.class);			
		    startService(service);
		}
		

		ParametrosGlobais.device_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); 
		//Log.grava(ParametrosGlobais.arq_log, "GRAVOU COM THREAD");*/
	}
	
	public void checaTabela(){
		PossivelLinha possivelLinha = dbPL.getUltimoRegistro();
		if(possivelLinha.datahora != null){
			Long tempo_decorrido = TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis() - possivelLinha.datahora) / 60;
			if( tempo_decorrido > 59)
				dbPL.deleteAllPossivelLinha();

			Toast.makeText(this, String.valueOf(tempo_decorrido) + " Minutos", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onResume(){
		super.onResume();
		MLL.ativo();		
		MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES_ONRESUME);
				
	}
	@Override
	protected void onPause(){
		super.onPause();
		MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_db) {
			Intent intent = new Intent(this, PossivelLinhaActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_map) {
			if(et_numero.getText().toString().length() > 0){
				Intent intent = new Intent(this, MapActivity.class);
				intent.putExtra("numero", et_numero.getText().toString());
				startActivity(intent);
			}
			else{
				Toast.makeText(MainActivity.this, "Informe o número da linha", Toast.LENGTH_SHORT).show();
			}	
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
		PossivelLinha[] possivelLinha = new PossivelLinha[lista.size()];
		for(int i=0;i<lista.size();i++){
			SoapObject item = (SoapObject) lista.get(i);	
	        PossivelLinha pl = new PossivelLinha();
	        pl.numero = item.getPropertyAsString("numero");
	        pl.nome = item.getPropertyAsString("nome");
	        possivelLinha[i] = pl;
	        
		}
		LinhaAdapter adapter = new LinhaAdapter(this,R.layout.linha_row,possivelLinha);            
        et_resultado.setAdapter(adapter);
		
	}

	@Override
	public void ChecaTabela(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void TrataJson(String str) {
		// TODO Auto-generated method stub
		try {			
			JSONObject json = new JSONObject(str);
			if(json.getInt("erro") == 0){
				JSONArray json_array = json.getJSONArray("linha");	
				PossivelLinha[] possivelLinha = new PossivelLinha[json_array.length()];
				
				for(int i=0;i<json_array.length();i++){
					PossivelLinha pl = new PossivelLinha();
					JSONObject jLinha = json_array.getJSONObject(i);
			        pl.numero = jLinha.getString("numero");
			        pl.nome = jLinha.getString("nome");
			        possivelLinha[i] = pl;
			        
				}
				LinhaAdapter adapter = new LinhaAdapter(this,R.layout.linha_row,possivelLinha);            
			    et_resultado.setAdapter(adapter);	
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	
}
