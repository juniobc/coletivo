package com.rod.util;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;

import com.rod.coletivo.auxiliar.Config;

public class VerificaIntervalo extends AsyncTask<String, Void, Integer> {
	Context context;
	Config  ret;
	
	public VerificaIntervalo(Context context, Config ret){
		this.context = context;
		this.ret = ret;
	}
	@Override
	protected Integer doInBackground(String... arg0) {
		Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        //DateFormat date = new SimpleDateFormat("HH");
		return currentLocalTime.getHours();
	}

	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		ret.alteraIntervalo(result);
		
	}
}