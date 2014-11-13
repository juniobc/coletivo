package com.rod.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.rod.coletivo.db.MySQLitePossivelLinhaHelper;
import com.rod.coletivo.entidade.PossivelLinha;
import com.rod.util.DeterminaLinha;

public class TimeService extends Service{
	MySQLitePossivelLinhaHelper dbPL = new MySQLitePossivelLinhaHelper(this);
	// constant
	public static boolean ativo;
	public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

	// run on another Thread to avoid crash
	private Handler mHandler = new Handler();
	// timer handling
	private Timer mTimer = null;

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

		Toast.makeText(getApplicationContext(), "iniciou servico",Toast.LENGTH_SHORT).show();
		ativo = true;
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
					// display toast
					//List<PossivelLinha> list = dbPL.getAllPossivelLinha();					
					DeterminaLinha dl = new DeterminaLinha(dbPL);
					PossivelLinha target = dl.getLinha();
					if(target != null)
						Toast.makeText(getApplicationContext(), "[ " +target.numero+" - "+target.nome+" ]", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(), "Linha indeterminada", Toast.LENGTH_SHORT).show();	
				}

			});
		}

		private String getDateTime() {
			// get date time in custom format
			SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
			return sdf.format(new Date());
		}

	}
}    
