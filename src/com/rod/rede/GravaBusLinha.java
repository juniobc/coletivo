package com.rod.rede;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rod.coletivo.auxiliar.Retorno;

public class GravaBusLinha extends AsyncTask<String, Void, Void> {
		
	Context context;
	Retorno  ret;
	String origem;
	
	ProgressDialog pd;
	
	public GravaBusLinha(Context context, Retorno ret, String origem){
		this.context = context;
		this.ret = ret;
		this.origem = origem;
		
	}
	@Override
	protected Void doInBackground(String... arg0) {
			
		String[] params = {
				"http://montra.com.br/cmtc/json/functions.php",
				"Idlinha="+arg0[0],
				"Device_id="+arg0[1],		
				"Lat="+arg0[2],
				"Lng="+arg0[3],
				"Data_hora="+arg0[4],
				"op=GravaBusLinha"
				};
		
		
		FazChamada.execute(params);
		return null;
	}

	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected void onPostExecute(Void result) {
		
	}
}