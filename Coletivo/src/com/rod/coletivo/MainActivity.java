package com.rod.coletivo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coletivo.R;
import com.google.android.gms.maps.model.LatLng;
import com.rod.coletivo.auxiliar.Retorno;
import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.rede.BuscaLinha;
import com.rod.service.TimeService;
import com.rod.util.GPSListener;
import com.rod.util.ParametrosGlobais;


public class MainActivity extends ActionBarActivity implements Retorno {

	EditText et_resultado, et_lat, et_lng;
	TextView tv_dist, tv_lat, tv_lng;
	SeekBar sb_dist;

	Button btn_busca_linha, btn_gravado, btn_mapa;

	BuscaLinha bl;

	GPSListener MLL;

	MySQLitePossivelLinhaHelper dbPL;

	Calendar calendar;
	
	public static Double lat=-21.0, lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MLL = new GPSListener(this);    	
		MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);


		et_resultado = (EditText) findViewById(R.id.et_resultado);
		et_lat = (EditText) findViewById(R.id.et_lat);
		et_lng = (EditText) findViewById(R.id.et_lng);
		sb_dist = (SeekBar) findViewById(R.id.sb_dist);
		tv_dist = (TextView) findViewById(R.id.tv_dist);
		tv_lat = (TextView) findViewById(R.id.tv_lat);
		tv_lng = (TextView) findViewById(R.id.tv_lng);

		btn_busca_linha = (Button) findViewById(R.id.btn_busca_linha);
		btn_busca_linha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bl = new BuscaLinha(MainActivity.this, MainActivity.this);
				bl.execute(new String[]{String.valueOf(lat),
						String.valueOf(lng), 
						tv_dist.getText().toString()});
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
				Intent intent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(intent);
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
			}
		});

		calendar = Calendar.getInstance();

		dbPL = new MySQLitePossivelLinhaHelper(this);	
		checaTabela();
		
		if(TimeService.ativo) { 
		    Toast.makeText(getBaseContext(), "Serviço já está ativo", Toast.LENGTH_SHORT).show();
		}else {			
		    Toast.makeText(getBaseContext(), "Iniciando serviço...", Toast.LENGTH_SHORT).show();
		    Intent service = new Intent(this, TimeService.class);			
		    startService(service);
		}
		
	}
	
	public void checaTabela(){
		PossivelLinha possivelLinha = dbPL.getUltimoRegistro();
		if(possivelLinha.datahora != null){
			Long tempo_decorrido = TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis() - possivelLinha.datahora) / 60;
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
		if (id == R.id.action_settings) {
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

		Date now = calendar.getTime();
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
	@Override
	public void TrataLatLng(Object o){}
}
