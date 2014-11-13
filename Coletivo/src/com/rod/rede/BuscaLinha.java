package com.rod.rede;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rod.coletivo.auxiliar.Retorno;

public class BuscaLinha extends AsyncTask<String, Void, SoapObject> {
	public String METHOD_NAME = "BuscaLinha";
	public String NAMESPACE = "http://montra.com.br/cmtc/soap/server.php";
	public String URL = "http://montra.com.br/cmtc/soap/server.php";

	public String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
	
	Context context;
	Retorno  ret;
	
	ProgressDialog pd;
	
	public BuscaLinha(Context context, Retorno ret){
		this.context = context;
		this.ret = ret;
		
	}
	@Override
	protected SoapObject doInBackground(String... params) {
		SoapObject result = null;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("lat", params[0]);
		request.addProperty("lng", params[1]);
		request.addProperty("dist", params[2]);
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		try {
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			
			androidHttpTransport.call(SOAP_ACTION, envelope);		
			
			result = (SoapObject) envelope.getResponse();

		} catch (Exception e) {
			Log.d("mytag", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPreExecute() {
		pd = new ProgressDialog(context);
		pd.setMessage("Carregando linhas...");
		pd.show();
	}
	
	@Override
	protected void onPostExecute(SoapObject result) {
		ret.Trata(result);
		pd.dismiss();
	}
}