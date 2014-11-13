package com.rod.coletivo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rod.coletivo.auxiliar.Retorno;
import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.rede.BuscaLinha;
import com.rod.service.TimeService;
import com.rod.util.GPSListener;
import com.rod.util.ObjetosGlobais;
import com.rod.util.ParametrosGlobais;


public class MainActivity extends ActionBarActivity implements Retorno {
	EditText et_resultado,et_numero;
	TextView tv_dist;
	SeekBar sb_dist;
	Button btn_busca_linha, btn_gravado, btn_mapa;
	BuscaLinha bl;
	GPSListener MLL;
	MySQLitePossivelLinhaHelper dbPL;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MLL = new GPSListener(this,this);    	
		MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);


		et_resultado = (EditText) findViewById(R.id.et_resultado);
		et_numero = (EditText) findViewById(R.id.et_numero);
		/*sb_dist = (SeekBar) findViewById(R.id.sb_dist);
		tv_dist = (TextView) findViewById(R.id.tv_dist);
		
		btn_busca_linha = (Button) findViewById(R.id.btn_busca_linha);
		btn_busca_linha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bl = new BuscaLinha(MainActivity.this, MainActivity.this, ParametrosGlobais.ORIGEM_ACTIVITY);
				bl.execute(new String[]{String.valueOf(ObjetosGlobais.lat),
						String.valueOf(ObjetosGlobais.lng), 
						String.valueOf(ObjetosGlobais.dist)});
			}
		});
		btn_gravado = (Button) findViewById(R.id.btn_gravado);
		btn_gravado.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, PossivelLinhaActivity.class);
				startActivity(intent);
			}
		});
		btn_mapa = (Button) findViewById(R.id.btn_mapa);
		btn_mapa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_numero.getText().toString().length() > 0){
					Intent intent = new Intent(MainActivity.this, MapActivity.class);
					intent.putExtra("numero", et_numero.getText().toString());
					startActivity(intent);
				}
			}
		});
		sb_dist.incrementProgressBy(10);
		sb_dist.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			} 

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				tv_dist.setText(String.valueOf(seekBar.getProgress()));
				ObjetosGlobais.dist = sb_dist.getProgress();
			}
		});
		tv_dist.setText(String.valueOf(sb_dist.getProgress()));
		ObjetosGlobais.dist = sb_dist.getProgress();*/
		
		

		dbPL = new MySQLitePossivelLinhaHelper(this);	
		checaTabela();
		
		if(TimeService.ativo) { 
		    Toast.makeText(getBaseContext(), "Serviço já está ativo", Toast.LENGTH_SHORT).show();
		}else {			
		    Toast.makeText(getBaseContext(), "Iniciando serviço...", Toast.LENGTH_SHORT).show();
		    Intent service = new Intent(this, TimeService.class);			
		    startService(service);
		}
		/*ObjetosGlobais.pd = new ProgressDialog(this);		
		ObjetosGlobais.pd.setMessage("Aguardando GPS...");
		ObjetosGlobais.pd.setCancelable(false);
		ObjetosGlobais.pd.show();*/
		
		ParametrosGlobais.device_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); 
		
		et_resultado.setText(ParametrosGlobais.device_id);
	}
	
	public void checaTabela(){
		PossivelLinha possivelLinha = dbPL.getUltimoRegistro();
		if(possivelLinha.datahora != null){
			Long tempo_decorrido = TimeUnit.MILLISECONDS.toSeconds(ObjetosGlobais.calendar.getTimeInMillis() - possivelLinha.datahora) / 60;
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
		
		//et_resultado.setText(String.valueOf(new Timestamp(new Date().getTime()).getTime()));
		//et_resultado.setText(String.valueOf((ObjetosGlobais.calendar.getTimeInMillis()/1000)+ObjetosGlobais.calendar.getTimeZone().getDefault().));
		//ObjetosGlobais.gps_ligado = true;
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void Trata(Object so) {
		// TODO Auto-generated method stub
		et_resultado.setText("");
		if ( Integer.parseInt(((SoapObject)so).getProperty("erro").toString()) > 0 ){
			et_resultado.setText(((SoapObject)so).getPropertyAsString("msg"));
			return;
		}	

		Date now = ObjetosGlobais.calendar.getTime();
		Timestamp timestamp = new Timestamp(now.getTime());

		PossivelLinha pl;



		Vector<?> lista = (Vector<?>) ((SoapObject)so).getProperty("linha");
		for(int i=0;i<lista.size();i++){
			SoapObject item = (SoapObject) lista.get(i);
			pl = new PossivelLinha(
					0,
					Integer.parseInt(item.getPropertyAsString("idlinha")),
					item.getPropertyAsString("numero"),
					item.getPropertyAsString("nome"),
					timestamp.getTime(),
					Integer.parseInt(item.getPropertyAsString("seq")),
					1
					);
			dbPL.addPossivelLinha(pl);
			et_resultado.setText(et_resultado.getText() 
					+ item.getPropertyAsString("numero")+ " - " 
					+ item.getPropertyAsString("nome") + " - [" 
					+ item.getPropertyAsString("seq") + "] - " 
					+ item.getPropertyAsString("dist") + '\n');
		}

		/*EmpresaListItem[] ObjectItemData = new EmpresaListItem[lista.getPropertyCount()];

		for(int i = 0; i< lista.getPropertyCount();i++){
			SoapObject item = (SoapObject) lista.getProperty(i);	

			ObjectItemData[i] = new EmpresaListItem(
					Integer.parseInt(item.getPropertyAsString("cd_empresa")),
					item.getPropertyAsString("ds_empresa"),
					Double.parseDouble(item.getPropertyAsString("distancia"))
					);
		}


		// our adapter instance
		ArrayAdapterEmpresaListItem adapter = new ArrayAdapterEmpresaListItem(this, R.layout.simplerow, ObjectItemData);

		lv.setAdapter(adapter);*/
	}
	
}
