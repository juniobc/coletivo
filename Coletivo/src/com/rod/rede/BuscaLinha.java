package com.rod.rede;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rod.coletivo.auxiliar.Retorno;
import com.rod.util.ParametrosGlobais;
import com.rod.util.TemConexao;

public class BuscaLinha extends AsyncTask<String, Void, String> {
		
	Context context;
	Retorno  ret;
	String origem;
	
	ProgressDialog pd;
	
	public BuscaLinha(Context context, Retorno ret, String origem){
		this.context = context;
		this.ret = ret;
		this.origem = origem;
		
	}
	@Override
	protected String doInBackground(String... arg0) {
		
		String[] params = {
				"http://montra.com.br/cmtc/json/functions.php",
				"lat="+arg0[0],
				"lng="+arg0[1],
				"dist="+arg0[2],
				"op=BuscaLinha"
				};
		
		if(TemConexao.ativa(context))
			return FazChamada.execute(params);
		else
			return TemConexao.erro;
	}

	@Override
	protected void onPreExecute() {
		if(origem == ParametrosGlobais.ORIGEM_ACTIVITY){
			pd = new ProgressDialog(context);
			pd.setMessage("Carregando linhas...");
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