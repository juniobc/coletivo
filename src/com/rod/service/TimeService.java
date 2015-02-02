package com.rod.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.rod.coletivo.auxiliar.Config;
import com.rod.coletivo.auxiliar.Retorno;
import com.rod.coletivo.db.MySQLiteColetivoHelper;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.rede.BuscaLinha;
import com.rod.rede.BuscaPosicaoValida;
import com.rod.rede.GravaBusLinha;
import com.rod.util.DeterminaLinha;
import com.rod.util.GPSListener;
import com.rod.util.Log;
import com.rod.util.ObjetosGlobais;
import com.rod.util.ParametrosGlobais;
import com.rod.util.VerificaIntervalo;

public class TimeService extends Service implements Retorno,Config{
	public BuscaLinha bl;
	public BuscaPosicaoValida bpv;
	public GravaBusLinha gbl;
	public List<String> linhas = new ArrayList<String>();

	MySQLiteColetivoHelper dbPL = new MySQLiteColetivoHelper(this);
	VerificaIntervalo vi;
	// constant
	public static boolean ativo;
	public static final long NOTIFY_INTERVAL = ParametrosGlobais.intervalo;
	public static final long NOTIFY_INTERVAL_STANDBY = 3600000;//a cada 1 hora
	public static final long VERIFICA_INTERVALO = 1200000;//a cada 20 minutos;

	
	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

	GPSListener MLL;

	Handler handler_ct, handler_ci;

	@Override
	public void Trata(Object so) {
		// TODO Auto-generated method stub
		linhas.clear(); 		
		try{
			if ( Integer.parseInt(((SoapObject)so).getProperty("erro").toString()) == 0 ){	
				Date now = Calendar.getInstance().getTime();
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
							Double.parseDouble(item.getPropertyAsString("lat")),
							Double.parseDouble(item.getPropertyAsString("lng")),
							Integer.parseInt(item.getPropertyAsString("seq")),
							1
							);
					dbPL.addPossivelLinha(pl);
					linhas.add(item.getPropertyAsString("numero"));
				}
			}
		}
		catch(NullPointerException e){
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"[trata]->"+e.toString());
			//Toast.makeText(getApplicationContext(), "Erro de acesso a rede",Toast.LENGTH_SHORT).show();			
		}
		catch(Exception e){
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"[trata]->"+e.toString());
			//Toast.makeText(getApplicationContext(), "Erro desconhecido",Toast.LENGTH_SHORT).show();
		}
		ObjetosGlobais.semaforo = true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// cancel if already existed
		super.onCreate();
		if(mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

		//Toast.makeText(getApplicationContext(), "iniciou servico",Toast.LENGTH_SHORT).show();
		ativo = true;
		
		//HANDLER PRA FICAR CHECANDO A TABELA
		handler_ct=new Handler();
		final Runnable r_ct = new Runnable(){
			public void run(){
				bpv = new BuscaPosicaoValida(TimeService.this,TimeService.this,ParametrosGlobais.ORIGEM_SERVICE);
				bpv.execute(new String[]{ParametrosGlobais.device_id});
				handler_ct.postDelayed(this, ParametrosGlobais.intervalo_checa_tabela);
			}
		};

		handler_ct.postDelayed(r_ct, ParametrosGlobais.intervalo_checa_tabela);
		
		//HANDLER PRA FICAR CHECANDO O INTERVALO
		handler_ci=new Handler();
		final Runnable r_ci = new Runnable(){
			public void run(){
				vi = new VerificaIntervalo(TimeService.this,TimeService.this);
				vi.execute("");
				handler_ci.postDelayed(this, VERIFICA_INTERVALO);
				//Log.grava(ParametrosGlobais.arq_log, "CHECA INTERVALO");
			}
		};

		handler_ci.postDelayed(r_ci, VERIFICA_INTERVALO); //a cada 20 minutos 
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		ativo = false;
	}

	class TimeDisplayTimerTask extends TimerTask {
		long data_hora;
		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					MLL = new GPSListener(TimeService.this,TimeService.this);    	
					MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);

					data_hora = (Calendar.getInstance().getTimeInMillis())/1000;

					if(ObjetosGlobais.precisao<200 && ObjetosGlobais.semaforo){
						ObjetosGlobais.semaforo = false;
						bl = new BuscaLinha(TimeService.this,TimeService.this, ParametrosGlobais.ORIGEM_SERVICE);
						bl.execute(new String[]{String.valueOf(ObjetosGlobais.lat),
								String.valueOf(ObjetosGlobais.lng), 
								String.valueOf(ParametrosGlobais.dist)});

						DeterminaLinha dl = new DeterminaLinha(dbPL);
						PossivelLinha target = dl.getLinha();
						if(target != null && ObjetosGlobais.latLng_valida){
							if(linhas.contains(target.numero)){
								ObjetosGlobais.linha_determinada = true;
								Toast.makeText(getApplicationContext(), "[ " +target.numero+" - "+target.nome+" ]", Toast.LENGTH_SHORT).show();
								PossivelLinha ultima_lat_lng = dbPL.getUltimoRegistroIdlinha(target.idlinha);
								Double lat=null, lng = null;
								if(ultima_lat_lng != null){
									lat = ultima_lat_lng.lat;
									lng = ultima_lat_lng.lng;
								}
								else{
									lat = ObjetosGlobais.lat;
									lng = ObjetosGlobais.lng;
								}
								gbl = new GravaBusLinha(TimeService.this,TimeService.this, ParametrosGlobais.ORIGEM_SERVICE);
								gbl.execute(
										new String[]{
												String.valueOf(target.idlinha),
												String.valueOf(ParametrosGlobais.device_id),
												String.valueOf(lat),
												String.valueOf(lng),
												String.valueOf(	data_hora )
										});

							}
							//else
								//Toast.makeText(getApplicationContext(), "Linha determinada não passa nessa rua.", Toast.LENGTH_SHORT).show();
						}
						else{
							ObjetosGlobais.linha_determinada = false;
							//Toast.makeText(getApplicationContext(), "Linha indeterminada", Toast.LENGTH_SHORT).show();
						}
					}
					//else
						//Log.grava(ParametrosGlobais.arq_log, "Posicao descartada. Precisao:"+ObjetosGlobais.precisao);
					//Toast.makeText(getApplicationContext(), "xxx", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void ChecaTabela(Object o) {
		// TODO Auto-generated method stub
		Long tempo_decorrido = (long) 0.0;
		PossivelLinha possivelLinha = dbPL.getUltimoRegistro();
		if(possivelLinha.datahora != null){
			tempo_decorrido = (TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis()) - possivelLinha.datahora) / 60;
		}

		try{
			if ( Integer.parseInt(((SoapObject)o).getProperty("retorno").toString()) == 0 || tempo_decorrido > 59 ){	
				Log.grava(ParametrosGlobais.arq_log, "Limpou tabela - tempo:"+tempo_decorrido+" [ ret: "+((SoapObject)o).getProperty("retorno").toString()+"]");				
				dbPL.deleteAllPossivelLinha();
			}
		}
		catch(NullPointerException e){
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"->"+e.toString());
			//Toast.makeText(getApplicationContext(), "Erro de acesso a rede",Toast.LENGTH_SHORT).show();			
		}
		catch(Exception e){
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"->"+e.toString());
			//Toast.makeText(getApplicationContext(), "Erro desconhecido",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void TrataJson(String str) {
		// TODO Auto-generated method stub
		linhas.clear(); 
		try {			
			JSONObject json = new JSONObject(str);
			if(json.getInt("erro") == 0){
				// now = Calendar.getInstance().getTime();
				//Timestamp timestamp = new Timestamp(now.getTime());	
				PossivelLinha pl;
				
				JSONArray json_array = json.getJSONArray("linha");	
				//Log.grava(ParametrosGlobais.arq_log, "Ret: "+String.valueOf(json_array.length())+" registros");
				
				for(int i=0;i<json_array.length();i++){
					JSONObject jLinha = json_array.getJSONObject(i);
					PossivelLinha pl_aux = dbPL.getUltimoRegistroIdlinha(Integer.parseInt(jLinha.getString("idlinha")));
					if(pl_aux != null){
						if(pl_aux.seq != Integer.parseInt(jLinha.getString("seq"))){
							pl = new PossivelLinha(
									0,
									Integer.parseInt(jLinha.getString("idlinha")),
									jLinha.getString("numero"),
									jLinha.getString("nome"),
									TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis()),
									Double.parseDouble(jLinha.getString("lat")),
									Double.parseDouble(jLinha.getString("lng")),
									Integer.parseInt(jLinha.getString("seq")),
									Integer.parseInt(jLinha.getString("ida"))
									);
							dbPL.addPossivelLinha(pl);
						}	
						else
							Log.grava(ParametrosGlobais.arq_log, "linha "+jLinha.getString("numero")+" contra mao");
							
					}
					else{
						pl = new PossivelLinha(
								0,
								Integer.parseInt(jLinha.getString("idlinha")),
								jLinha.getString("numero"),
								jLinha.getString("nome"),
								TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis()),
								Double.parseDouble(jLinha.getString("lat")),
								Double.parseDouble(jLinha.getString("lng")),
								Integer.parseInt(jLinha.getString("seq")),
								Integer.parseInt(jLinha.getString("ida"))
								);
						dbPL.addPossivelLinha(pl);
					}
					linhas.add(jLinha.getString("numero"));
					//Log.grava(ParametrosGlobais.arq_log, "HORA CORRENTE:"+String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeInMillis())));
			        
				}				
			}
			else
				Log.grava(ParametrosGlobais.arq_log, json.getString("msg"));	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"[trataJson]->"+e.toString());
			e.printStackTrace();
		}
		
		ObjetosGlobais.semaforo = true;

	}

	@Override
	public void alteraIntervalo(int hora) {
		// TODO Auto-generated method stub
		if(hora < 6){
			mTimer.cancel();
			mTimer = new Timer();		
			mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL_STANDBY);
			Log.grava(ParametrosGlobais.arq_log, "ALTEROU PRA INTERVADO DE STANDBY");
		}
		else{
			mTimer.cancel();
			mTimer = new Timer();		
			mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
			Log.grava(ParametrosGlobais.arq_log, "ALTEROU PRA INTERVADO PADRAO");
		}
		//Toast.makeText(getApplicationContext(), "hora:"+hora,Toast.LENGTH_SHORT).show();
	}
}    
