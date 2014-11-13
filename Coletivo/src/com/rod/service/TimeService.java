package com.rod.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.ksoap2.serialization.SoapObject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.rod.coletivo.auxiliar.Retorno;
import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.rede.BuscaLinha;
import com.rod.rede.GravaBusLinha;
import com.rod.util.DeterminaLinha;
import com.rod.util.GPSListener;
import com.rod.util.ObjetosGlobais;
import com.rod.util.ParametrosGlobais;

public class TimeService extends Service implements Retorno{
	public BuscaLinha bl;
	public GravaBusLinha gbl;
	public List<String> linhas = new ArrayList<String>();

	MySQLitePossivelLinhaHelper dbPL = new MySQLitePossivelLinhaHelper(this);
	// constant
	public static boolean ativo;
	public static final long NOTIFY_INTERVAL = ParametrosGlobais.intervalo;

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;
	
	GPSListener MLL;

	Handler handler;

	@Override
	public void Trata(Object so) {
		// TODO Auto-generated method stub
		linhas.clear(); 
		try{
			if ( Integer.parseInt(((SoapObject)so).getProperty("erro").toString()) == 0 ){	
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
					linhas.add(item.getPropertyAsString("numero"));
				}
			}
		}
		catch(NullPointerException e){
			Toast.makeText(getApplicationContext(), "Erro de acesso a rede",Toast.LENGTH_SHORT).show();			
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Erro desconhecido",Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// cancel if already existed
		if(mTimer != null) {
			mTimer.cancel();
		} else {
			// recreate new
			mTimer = new Timer();
		}
		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

		MLL = new GPSListener(this,this);    	
		MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);
		
		Toast.makeText(getApplicationContext(), "iniciou servico",Toast.LENGTH_SHORT).show();
		ativo = true;

		/*handler=new Handler();
        final Runnable r = new Runnable(){
            public void run(){
            	if(!ObjetosGlobais.gps_ligado)
            		if(ObjetosGlobais.linha_determinada)
            			MLL.setGPSParams((long)ParametrosGlobais.intervalo);
            		else
            			MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES_ONRESUME);
				else
					MLL.setGPSParams(ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);	
                handler.postDelayed(this, ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);
            }
        };

        handler.postDelayed(r, ParametrosGlobais.MINIMUM_TIME_BETWEEN_UPDATES);*/
		
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		ativo = false;
	}



	class TimeDisplayTimerTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					checaTabela();
											
					//ObjetosGlobais.gps_ligado = !ObjetosGlobais.gps_ligado;
					bl = new BuscaLinha(TimeService.this,TimeService.this, ParametrosGlobais.ORIGEM_SERVICE);
					bl.execute(new String[]{String.valueOf(ObjetosGlobais.lat),
							String.valueOf(ObjetosGlobais.lng), 
							String.valueOf(ObjetosGlobais.dist)});

					DeterminaLinha dl = new DeterminaLinha(dbPL);
					PossivelLinha target = dl.getLinha();
					if(target != null && ObjetosGlobais.latLng_valida){
						if(linhas.contains(target.numero)){
							ObjetosGlobais.linha_determinada = true;
							Toast.makeText(getApplicationContext(), "[ " +target.numero+" - "+target.nome+" ]", Toast.LENGTH_SHORT).show();
							gbl = new GravaBusLinha(TimeService.this,TimeService.this, ParametrosGlobais.ORIGEM_SERVICE);
							gbl.execute(new String[]{String.valueOf(target.idlinha),
									String.valueOf(ParametrosGlobais.device_id),
									String.valueOf(ObjetosGlobais.lat),
									String.valueOf(ObjetosGlobais.lng),
									String.valueOf(ObjetosGlobais.calendar.getTimeInMillis()/1000)});

						}
						else
							Toast.makeText(getApplicationContext(), "Linha determinada não passa nessa rua.", Toast.LENGTH_SHORT).show();
					}
					else{
						ObjetosGlobais.linha_determinada = false;
						Toast.makeText(getApplicationContext(), "Linha indeterminada", Toast.LENGTH_SHORT).show();
					}	
				}
				public void checaTabela(){
					PossivelLinha possivelLinha = dbPL.getUltimoRegistro();
					if(possivelLinha.datahora != null){
						Long tempo_decorrido = TimeUnit.MILLISECONDS.toSeconds(ObjetosGlobais.calendar.getTimeInMillis() - possivelLinha.datahora) / 60;
						if( tempo_decorrido > 59)
							dbPL.deleteAllPossivelLinha();						
					}
				}

			});
		}
	}
}    
