package com.rod.rede;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rod.coletivo.auxiliar.Retorno;
import com.rod.util.ParametrosGlobais;

public class BuscaBusLinha extends AsyncTask<String, Void, String> {
	Context context;
	Retorno  ret;
	String origem;
	
	ProgressDialog pd;
	
	public BuscaBusLinha(Context context, String origem){
		this.context = context;
		this.origem = origem;
		
	}
	
	public BuscaBusLinha(Context context, Retorno ret, String origem){
		this.context = context;
		this.ret = ret;
		this.origem = origem;
		
	}
	@Override
	protected String doInBackground(String... arg0) {
		
		String[] params = {
				"http://montra.com.br/cmtc/json/functions.php",
				"numero="+arg0[0],
				"op=BuscaBusLinha"
				};
		
		
		return FazChamada.execute(params);
	}

	@Override
	protected void onPreExecute() {
		if(origem == ParametrosGlobais.ORIGEM_ACTIVITY){
			pd = new ProgressDialog(context);
			pd.setMessage("Carregando onibus na linha...");
			pd.show();
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		ret.TrataJson(result);
		if(origem == ParametrosGlobais.ORIGEM_ACTIVITY)
			pd.dismiss();
	}
}