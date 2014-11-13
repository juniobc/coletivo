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

public class GravaBusLinha extends AsyncTask<String, Void, SoapObject> {
	public String METHOD_NAME = "grava_bus_linha";
	public String NAMESPACE = "http://montra.com.br/cmtc/soap/server.php";
	public String URL = "http://montra.com.br/cmtc/soap/server.php";

	public String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
	
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
	protected SoapObject doInBackground(String... params) {
		SoapObject result = null;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("Idlinha", params[0]);
		request.addProperty("Device_id", params[1]);		
		request.addProperty("Lat", params[2]);
		request.addProperty("Lng", params[3]);
		request.addProperty("Data_hora", params[4]);
		
		
		
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
		
	}
	
	@Override
	protected void onPostExecute(SoapObject result) {
		
	}
}