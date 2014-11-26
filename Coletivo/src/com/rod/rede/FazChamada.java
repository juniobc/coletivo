package com.rod.rede;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;

import com.rod.coletivo.auxiliar.Retorno;
import com.rod.util.Log;
import com.rod.util.ParametrosGlobais;

public class FazChamada {
	ProgressDialog pd;
	Context context;
	Retorno ret;
	
	
	public FazChamada(){
		
	}
	

	public static String execute(String... arg0) {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(arg0[0]);
	    String saida = "", linha=null;
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(arg0.length);
	        for(int i=1;i<arg0.length;i++){
	        	String[] aux = arg0[i].split("=");
	        	nameValuePairs.add(new BasicNameValuePair(aux[0], aux[1]));
	        }

	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        
	        // Read response until the end
	        while ((linha = rd.readLine()) != null) { saida += linha; }

	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	Log.grava(ParametrosGlobais.arq_log, "[fazChamada]->"+e.toString());
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	Log.grava(ParametrosGlobais.arq_log, "[fazChamada]->"+e.toString());
	    }
	    return saida;
	}

	
}
