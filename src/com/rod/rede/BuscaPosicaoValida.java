package com.rod.rede;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rod.coletivo.auxiliar.Retorno;
import com.rod.util.Log;
import com.rod.util.ParametrosGlobais;

public class BuscaPosicaoValida extends AsyncTask<String, Void, SoapObject> {
	public String METHOD_NAME = "buscaPosicaoValidaDevice";
	public String NAMESPACE = "http://montra.com.br/cmtc/soap/server.php";
	public String URL = "http://montra.com.br/cmtc/soap/server.php";

	public String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
	
	Context context;
	Retorno  ret;
	String origem;
	
	ProgressDialog pd;
	
	public BuscaPosicaoValida(Context context, Retorno ret, String origem){
		this.context = context;
		this.ret = ret;
		this.origem = origem;
		
	}
	@Override
	protected SoapObject doInBackground(String... params) {
		SoapObject result = null;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("Device_id", params[0]);
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			
			androidHttpTransport.call(SOAP_ACTION, envelope);		
			
			result = (SoapObject) envelope.getResponse();

		} 
		catch (NullPointerException e){
			//Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"->"+e.getMessage());
		}
		catch (Exception e) {
			//Log.grava(ParametrosGlobais.arq_log, this.getClass().getName()+"->"+e.getMessage());
			//Log.d("mytag", e.getMessage());
			//e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected void onPostExecute(SoapObject result) {
		ret.ChecaTabela(result);
		
	}
}